package com.taoyuanx.ca.auth.config;

import com.taoyuanx.auth.AuthType;
import com.taoyuanx.auth.sign.ISign;
import com.taoyuanx.auth.sign.impl.hmac.HMacSign;
import com.taoyuanx.auth.sign.impl.RsaSign;
import com.taoyuanx.auth.sign.impl.Sm2Sign;
import com.taoyuanx.auth.token.TokenManager;
import com.taoyuanx.auth.token.impl.SimpleTokenManager;
import com.taoyuanx.ca.auth.controller.HmacAuthApiController;
import com.taoyuanx.ca.auth.controller.RsaAuthApiController;
import com.taoyuanx.ca.auth.controller.Sm2AuthApiController;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
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
    @ConditionalOnBean(AuthProperties.class)
    public TokenManager tokenManager(AuthProperties authProperties) throws Exception {
        ISign sign = null;
        AuthType type = AuthType.type(authProperties.getType());
        if (Objects.isNull(type)) {
            throw new RuntimeException("auth type" + authProperties.getType() + "not support");
        }
        switch (type) {
            case HMAC:
                AuthProperties.HmacAuthProperties hmac = authProperties.getHmac();
                sign = new HMacSign(authProperties.getHmac().getSignAlg(), hmac.getKey().getBytes("UTF-8"));
                break;
            case RSA:
                AuthProperties.RsaAuthProperties rsa = authProperties.getRsa();
                if (rsa.isVerifyOnly()) {
                    sign = new RsaSign(rsa.getServerPublicKey(), rsa.getSignAlg());
                } else {
                    sign = new RsaSign(rsa.getServerPublicKey(), rsa.getServerPrivateKey(), rsa.getSignAlg());
                }
                break;
            case SM2:
                AuthProperties.Sm2AuthProperties sm2 = authProperties.getSm2();
                if (sm2.isVerifyOnly()) {
                    sign = new Sm2Sign(sm2.getServerPublicKey(), sm2.getSignAlg());
                } else {
                    sign = new Sm2Sign(sm2.getServerPublicKey(), sm2.getServerPrivateKey(), sm2.getSignAlg());
                }
                break;
            default:
                throw new RuntimeException("auth type" + authProperties.getType() + "not support");
        }
        return new SimpleTokenManager(sign);

    }


}
