package com.taoyuanx.ca.auth.config;

import com.taoyuanx.auth.SimpleTokenManager;
import com.taoyuanx.auth.TokenManager;
import com.taoyuanx.auth.mac.HMacAlgorithms;
import com.taoyuanx.auth.sign.ISign;
import com.taoyuanx.auth.sign.impl.HMacSign;
import com.taoyuanx.auth.sign.impl.RsaSign;
import com.taoyuanx.auth.sign.impl.Sm2Sign;
import com.taoyuanx.ca.auth.constants.AuthType;
import com.taoyuanx.ca.auth.controller.HmacAuthApiController;
import com.taoyuanx.ca.auth.controller.RsaAuthApiController;
import com.taoyuanx.ca.auth.controller.Sm2AuthApiController;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

/**
 * @author dushitaoyuan
 * @desc 认证服务配置
 * @date 2020/2/25
 */
@Configuration
public class LittleAuthConfig {
    @Bean
    @ConditionalOnProperty(name = "littleca.auth.type", havingValue = "hmac")
    public HmacAuthApiController hmacAuthApiController() {
        return new HmacAuthApiController();
    }

    @Bean
    @ConditionalOnProperty(name = "littleca.auth.type", havingValue = "rsa")
    public RsaAuthApiController rsaAuthApiController() {
        return new RsaAuthApiController();
    }

    @Bean
    @ConditionalOnProperty(name = "littleca.auth.type", havingValue = "sm2")
    public Sm2AuthApiController sm2AuthApiController() {
        return new Sm2AuthApiController();
    }

    @Bean
    @ConditionalOnMissingBean(TokenManager.class)
    public TokenManager tokenManager(AuthProperties authProperties) throws Exception {

        ISign sign = null;
        if (authProperties.isAuthType(AuthType.AUTH_TYPE_RSA)) {
            AuthProperties.RsaAuthProperties rsa = authProperties.getRsa();
            if (rsa.isVerifyOnly()) {
                sign = new RsaSign(rsa.getServerPublicKey(), rsa.getSignAlg());
            } else {
                sign = new RsaSign(rsa.getServerPublicKey(), rsa.getServerPrivateKey(), rsa.getSignAlg());
            }

        } else if (authProperties.isAuthType(AuthType.AUTH_TYPE_SM2)) {
            AuthProperties.Sm2AuthProperties sm2 = authProperties.getSm2();
            if (sm2.isVerifyOnly()) {
                sign = new Sm2Sign(sm2.getServerPublicKey(), sm2.getSignAlg());
            } else {
                sign = new Sm2Sign(sm2.getServerPublicKey(), sm2.getServerPrivateKey(), sm2.getSignAlg());
            }
        } else if (authProperties.isAuthType(AuthType.AUTH_TYPE_HMAC)) {
            AuthProperties.HmacAuthProperties hmac = authProperties.getHmac();
            sign = new HMacSign(HMacAlgorithms.valueOf(authProperties.getHmac().getSignAlg()), hmac.getKey().getBytes("UTF-8"));
        } else {
            throw new RuntimeException("auth type" + authProperties.getType() + "not support");
        }
        return new SimpleTokenManager(sign);

    }


}
