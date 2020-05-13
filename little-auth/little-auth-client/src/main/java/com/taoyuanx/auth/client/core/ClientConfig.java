package com.taoyuanx.auth.client.core;

import com.taoyuanx.auth.AuthType;
import com.taoyuanx.auth.client.exception.ClientAuthException;
import com.taoyuanx.auth.encode.IAuthEncode;
import com.taoyuanx.auth.dto.request.EncodeRequestDTO;
import com.taoyuanx.auth.dto.response.EncodeResponseDTO;
import com.taoyuanx.auth.client.impl.loadbalance.ILoadbalance;
import com.taoyuanx.auth.client.impl.loadbalance.LoadbalanceEnum;
import com.taoyuanx.auth.client.utils.ServerUtil;
import com.taoyuanx.auth.client.utils.StrUtil;

import com.taoyuanx.auth.sign.ISign;
import com.taoyuanx.auth.sign.impl.RsaSign;
import com.taoyuanx.auth.sign.impl.Sm2Sign;
import com.taoyuanx.auth.sign.impl.hmac.HMacAlgorithms;
import com.taoyuanx.auth.sign.impl.hmac.HMacSign;
import com.taoyuanx.ca.core.api.impl.RSA;
import com.taoyuanx.ca.core.api.impl.SM2;
import com.taoyuanx.ca.core.util.ByteStringUtil;
import com.taoyuanx.ca.core.util.CertUtil;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;

import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author 都市桃源
 * 客户端必要配置文件
 */
@Getter
@Setter
@Slf4j
public class ClientConfig {
    public static final String DEFAULT_CONFIG = "auth-client.properties";

    public static final String CONFIG_PREFIX = "littleauth.client.";
    /**
     * 服务地址
     */
    private List<AuthServer> authServer;
    /**
     * 连接超时时间 默认 5 秒
     */
    private Integer connectTimeout;
    /**
     * 连接数 默认 100
     */
    private Integer maxIdleConnections;
    /**
     * 连接保持时间 默认 15秒
     */
    private Integer keepAliveDuration;
    /**
     * api账户
     */
    private String apiAccount;
    /**
     * 心跳监测时间间隔 默认5秒
     */
    private Long heartIdleTime;
    /**
     * 负载策略 支持Random(随机),Round(轮询)
     */
    private ILoadbalance loadbalance;
    /**
     * client Holder
     */
    private OkHttpClient okHttpClient;
    /**
     * client api
     */
    private Map<AuthServiceApiEnum, String> apiMap;

    private IAuthEncode authEncode;
    /**
     * 认证类型
     */
    private String authType;

    /**
     * client 端token 缓存减少时间
     */
    private Long tokenCacheSubTime;

    /**
     * 相关配置
     */
    private ISign sign;

    private HMacConfig hmac;
    private Sm2Config sm2;
    private RsaConfig rsa;


    private static ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

    public ClientConfig() {
        this(DEFAULT_CONFIG);
    }

    public ClientConfig(String configPath) {
        try {
            Properties config = new Properties();
            config.load(ClientConfig.class.getClassLoader().getResourceAsStream(configPath));
            this.authServer = new CopyOnWriteArrayList<AuthServer>(Arrays.stream(getProperty(config, CONFIG_PREFIX, "authserver").split(",")).map(AuthServer::new).collect(Collectors.toList()));
            this.connectTimeout = getProperty(config, Integer.class, CONFIG_PREFIX, "connectTimeout", 5);
            this.maxIdleConnections = this.connectTimeout = getProperty(config, Integer.class, CONFIG_PREFIX, "maxIdleConnections", 100);
            this.keepAliveDuration = this.maxIdleConnections = this.connectTimeout = getProperty(config, Integer.class, CONFIG_PREFIX, "keepAliveDuration", 15);
            this.apiAccount = getProperty(config, CONFIG_PREFIX, "apiAccount");
            this.authType = getProperty(config, CONFIG_PREFIX, "authType");
            this.tokenCacheSubTime = getProperty(config, Long.class, CONFIG_PREFIX, "tokenCacheSubTime", 30 * 60 * 1000L);


            AuthType type = AuthType.type(authType);
            switch (type) {
                case HMAC:
                    HMacConfig hmacConfig = new HMacConfig();
                    hmacConfig.setApiSecret(getProperty(config, CONFIG_PREFIX, HMacConfig.PREFIX + "authSecret"));
                    hmacConfig.setSignAlg(HMacAlgorithms.valueOf(getProperty(config, CONFIG_PREFIX, HMacConfig.PREFIX + "signAlg")));
                    this.sign = new HMacSign(hmacConfig.getSignAlg(), hmacConfig.getApiSecret().getBytes("UTF-8"));
                    break;
                case RSA: {
                    RsaConfig rsaConfig = new RsaConfig();
                    String p12Path = getProperty(config, CONFIG_PREFIX, RsaConfig.PREFIX + "clientP12");
                    String p12Password = getProperty(config, CONFIG_PREFIX, RsaConfig.PREFIX + "clientP12Password");
                    String signAlg = getProperty(config, CONFIG_PREFIX, RsaConfig.PREFIX + "signAlg");
                    KeyStore keyStore = CertUtil.readKeyStore(p12Path, p12Password);
                    rsaConfig.setRsaPublicKey(CertUtil.getPublicKey(keyStore, null));
                    rsaConfig.setRsaPrivateKey(CertUtil.getPrivateKey(keyStore, p12Password, null));
                    rsaConfig.setRsaServerPublicKey(CertUtil.readPublicKeyPem(getProperty(config, CONFIG_PREFIX, RsaConfig.PREFIX + "serverPublicKey")));
                    rsaConfig.setSignAlg(signAlg);
                    RSA rsa = rsaConfig.getRsa();
                    this.sign = new RsaSign((RSAPublicKey) rsaConfig.getRsaPublicKey(), (RSAPrivateKey) rsaConfig.getRsaPrivateKey(), rsaConfig.getSignAlg());
                    this.authEncode = new IAuthEncode() {
                        @Override
                        public EncodeRequestDTO encode(String data) {
                            try {
                                EncodeRequestDTO encodeRequestDTO = new EncodeRequestDTO();
                                encodeRequestDTO.setData(ByteStringUtil.toString(rsa.encrypt(data.getBytes(), rsaConfig.getRsaServerPublicKey()), ByteStringUtil.BASE64));
                                return encodeRequestDTO;
                            } catch (Exception e) {
                                throw new ClientAuthException("加密失败", e);
                            }
                        }

                        @Override
                        public String decode(EncodeResponseDTO encodeData) {
                            try {
                                byte[] data = null;
                                if (EncodeResponseDTO.ENCODE_YES.equals(encodeData.getEncode())) {
                                    data = rsa.decrypt(ByteStringUtil.toBytes(encodeData.getData(), ByteStringUtil.BASE64), rsaConfig.getRsaPrivateKey());
                                } else {
                                    data = encodeData.getData().getBytes("UTF-8");
                                }
                                byte[] sign = ByteStringUtil.toBytes(encodeData.getSign(), ByteStringUtil.BASE64);
                                if (rsa.verifySign(data, sign, rsaConfig.getRsaServerPublicKey(), rsaConfig.getSignAlg())) {
                                    throw new ClientAuthException("验签失败");
                                }
                                return new String(data, "UTF-8");
                            } catch (ClientAuthException e) {
                                throw e;
                            } catch (Exception e) {
                                throw new ClientAuthException("解密失败", e);
                            }
                        }
                    };
                    this.rsa = rsaConfig;
                }
                break;
                case SM2: {
                    Sm2Config sm2Config = new Sm2Config();
                    String p12Path = getProperty(config, CONFIG_PREFIX, Sm2Config.PREFIX + "clientP12");
                    String p12Password = getProperty(config, CONFIG_PREFIX, Sm2Config.PREFIX + "clientP12Password");
                    String signAlg = getProperty(config, CONFIG_PREFIX, Sm2Config.PREFIX + "signAlg");

                    KeyStore keyStore = CertUtil.readKeyStore(p12Path, p12Password);
                    sm2Config.setSm2PublicKey(CertUtil.getPublicKey(keyStore, null));
                    sm2Config.setSm2PrivateKey(CertUtil.getPrivateKey(keyStore, p12Password, null));
                    sm2Config.setSm2ServerPublicKey(CertUtil.readPublicKeyPem(getProperty(config, CONFIG_PREFIX, Sm2Config.PREFIX + "serverPublicKey")));
                    sm2Config.setSignAlg(signAlg);
                    SM2 sm2 = sm2Config.getSm2();
                    this.sign = new Sm2Sign(sm2Config.getSm2PublicKey(), sm2Config.getSm2PrivateKey(), sm2Config.getSignAlg());
                    this.authEncode = new IAuthEncode() {
                        @Override
                        public EncodeRequestDTO encode(String data) {
                            try {
                                EncodeRequestDTO encodeRequestDTO = new EncodeRequestDTO();
                                encodeRequestDTO.setData(ByteStringUtil.toString(sm2.encrypt(data.getBytes("UTF-8"), sm2Config.getSm2ServerPublicKey()), ByteStringUtil.BASE64));
                                return encodeRequestDTO;
                            } catch (Exception e) {
                                throw new ClientAuthException("加密失败", e);
                            }
                        }

                        @Override
                        public String decode(EncodeResponseDTO encodeData) {
                            try {
                                byte[] data = null;
                                if (EncodeResponseDTO.ENCODE_YES.equals(encodeData.getEncode())) {
                                    data = sm2.decrypt(ByteStringUtil.toBytes(encodeData.getData(), ByteStringUtil.BASE64), sm2Config.getSm2PrivateKey());
                                } else {
                                    data = encodeData.getData().getBytes("UTF-8");
                                }
                                byte[] sign = ByteStringUtil.toBytes(encodeData.getSign(), ByteStringUtil.BASE64);
                                if (sm2.verifySign(data, sign, sm2Config.getSm2ServerPublicKey(), sm2Config.getSignAlg())) {
                                    throw new ClientAuthException("验签失败");
                                }
                                return new String(data, "UTF-8");
                            } catch (ClientAuthException e) {
                                throw e;
                            } catch (Exception e) {
                                throw new ClientAuthException("解密失败", e);
                            }
                        }
                    };
                    this.sm2 = sm2Config;

                }
                break;
            }

            this.loadbalance = LoadbalanceEnum.valueOf(getProperty(config, String.class, CONFIG_PREFIX, "loadbalance", LoadbalanceEnum.Round.name())).getLoadbalance();
            this.heartIdleTime = getProperty(config, Long.class, CONFIG_PREFIX, "heartIdleTime", 5L);
            /**
             * 定时心跳监测
             */
            ClientConfig clientConfig = this;
            scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    ServerUtil.heartBeatCheck(clientConfig);
                }
            }, 5000L, this.heartIdleTime, TimeUnit.SECONDS);
        } catch (ClientAuthException e) {
            throw e;
        } catch (Exception e) {
            log.error("加载默认配置[" + DEFAULT_CONFIG + "]失败", e);
            throw new ClientAuthException("加载默认配置[" + DEFAULT_CONFIG + "]失败", e);
        }
    }


    private <T> T getProperty(Properties config, Class<T> type, String configPrefix, String key, T defaultValue) {
        if (StrUtil.isNotEmpty(configPrefix)) {
            key = configPrefix + key;
        }
        String value = config.getProperty(key);
        if (StrUtil.isEmpty(value)) {
            return defaultValue;
        }
        if (type.equals(String.class)) {
            return (T) value;
        }
        try {
            if (type.equals(Long.class)) {
                Long result = Long.parseLong(value);
                return (T) result;
            }
            if (type.equals(Integer.class)) {
                Integer result = Integer.parseInt(value);
                return (T) result;
            }
            if (type.equals(Boolean.class)) {
                Boolean result = Boolean.valueOf(value);
                return (T) result;
            }

        } catch (Exception e) {
        }
        return defaultValue;

    }

    private String getProperty(Properties config, String configPrefix, String key) {
        if (StrUtil.isNotEmpty(configPrefix)) {
            key = configPrefix + key;
        }
        String value = config.getProperty(key);
        if (StrUtil.isEmpty(value)) {
            throw new ClientAuthException("config 异常:" + key);
        }
        return value;

    }

    @Data
    public static class HMacConfig {

        public static final String PREFIX = "hmac.";
        /**
         * 秘钥
         */
        private String apiSecret;
        private HMacAlgorithms signAlg;

    }

    @Data
    public static class RsaConfig {

        public static final String PREFIX = "rsa.";
        /**
         * client公私钥
         */
        private PublicKey rsaPublicKey;

        private PrivateKey rsaPrivateKey;
        /**
         * 服务端公钥
         */
        private PublicKey rsaServerPublicKey;

        private String signAlg;
        private RSA rsa = new RSA();

    }

    @Data
    public static class Sm2Config {

        public static final String PREFIX = "sm2.";
        /**
         * client公私钥
         */
        private PublicKey sm2PublicKey;

        private PrivateKey sm2PrivateKey;
        /**
         * 服务端公钥
         */
        private PublicKey sm2ServerPublicKey;

        private String signAlg;
        private SM2 sm2 = new SM2();

    }
}
