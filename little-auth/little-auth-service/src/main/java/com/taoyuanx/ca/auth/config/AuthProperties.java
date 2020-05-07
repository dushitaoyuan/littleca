package com.taoyuanx.ca.auth.config;

import com.taoyuanx.ca.auth.constants.AuthType;
import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Objects;

import com.taoyuanx.ca.core.util.CertUtil;

/**
 * @author dushitaoyuan
 * @desc 认证服务配置
 * @date 2020/2/24
 */
@Configuration
@ConfigurationProperties(prefix = "littleca.auth")
@Data
public class AuthProperties implements InitializingBean {
    private String type;
    private RsaAuthProperties rsa;
    private HmacAuthProperties hmac;
    private Sm2AuthProperties sm2;
    private Boolean whiteIpCheckSwitch = false;
    private Long tokenValidTime = 90 * 60 * 1000L;

    private Long refreshTokenAddTime = 30 * 60 * 1000L;

    @Override
    public void afterPropertiesSet() throws Exception {
        if (rsa != null && isAuthType(AuthType.AUTH_TYPE_RSA)) {
            if (Objects.nonNull(rsa.getServerPublicKey())) {
                return;
            }
            if (StringUtils.hasText(rsa.getCertP12Path())) {
                KeyStore keyStore = CertUtil.readKeyStore(rsa.getCertP12Path(), rsa.getCertP12Password());
                rsa.setServerPublicKey((RSAPublicKey) CertUtil.getPublicKey(keyStore, null));
                rsa.setServerPrivateKey((RSAPrivateKey) CertUtil.getPrivateKey(keyStore, rsa.getCertP12Password(), null));
            } else {
                rsa.setVerifyOnly(true);
                rsa.setServerPublicKey((RSAPublicKey) CertUtil.readPublicKeyPem(rsa.getPublicCertPath()));
            }
            return;
        }
        if (sm2 != null && isAuthType(AuthType.AUTH_TYPE_SM2)) {
            if (Objects.nonNull(sm2.getServerPublicKey())) {
                return;
            }
            if (StringUtils.hasText(sm2.getCertP12Path())) {
                KeyStore keyStore = CertUtil.readKeyStore(sm2.getCertP12Path(), sm2.getCertP12Password());
                sm2.setServerPublicKey(CertUtil.getPublicKey(keyStore, null));
                sm2.setServerPrivateKey(CertUtil.getPrivateKey(keyStore, sm2.getCertP12Password(), null));
            } else {
                sm2.setVerifyOnly(true);
                sm2.setServerPublicKey(CertUtil.readPublicKeyPem(sm2.getPublicCertPath()));
            }
            return;
        }

    }


    public boolean isAuthType(String authType) {
        if (StringUtils.hasText(type) && authType.equals(type)) {
            return true;
        }
        return false;
    }

    @Data
    public static class RsaAuthProperties {
        private String certP12Path;
        private String certP12Password;

        private String publicCertPath;

        private RSAPublicKey serverPublicKey;
        private RSAPrivateKey serverPrivateKey;
        private String signAlg;
        private boolean verifyOnly = false;


    }

    @Data
    public static class Sm2AuthProperties {
        private String certP12Path;
        private String certP12Password;

        private String publicCertPath;

        private PublicKey serverPublicKey;
        private PrivateKey serverPrivateKey;
        private String signAlg = "SM3WITHSM2";
        private boolean verifyOnly = false;


    }

    @Data
    public static class HmacAuthProperties {
        private String key;
        private String signAlg;
    }
}
