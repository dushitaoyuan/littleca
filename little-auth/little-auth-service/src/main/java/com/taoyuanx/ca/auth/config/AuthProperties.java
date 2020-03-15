package com.taoyuanx.ca.auth.config;

import com.taoyuanx.ca.core.util.CertUtil;
import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

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

    private Boolean whiteIpCheckSwitch = false;
    private Long tokenValidTime = 90 * 60 * 1000L;

    private Long refreshTokenAddTime = 30 * 60 * 1000L;

    @Override
    public void afterPropertiesSet() throws Exception {
        if (rsa != null && isRsa()) {
            if (!StringUtils.isEmpty(rsa.getCertP12Path())) {
                KeyStore keyStore = CertUtil.readKeyStore(rsa.getCertP12Path(), rsa.getCertP12Password());
                rsa.setServerPublicKey((RSAPublicKey) CertUtil.getPublicKey(keyStore, null));
                rsa.setServerPrivateKey((RSAPrivateKey) CertUtil.getPrivateKey(keyStore, rsa.getCertP12Password(), null));
            } else {
                rsa.setVerifyOnly(true);
                rsa.setServerPublicKey((RSAPublicKey) CertUtil.getPublicKeyCer(new FileInputStream(rsa.getPublicCertPath())));
            }

        }

    }

    public boolean isRsa() {
        return type.equals("rsa");
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
    public static class HmacAuthProperties {
        private String key;
        private String signAlg;
    }
}
