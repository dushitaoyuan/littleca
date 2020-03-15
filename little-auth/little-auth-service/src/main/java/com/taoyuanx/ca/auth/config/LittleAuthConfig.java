package com.taoyuanx.ca.auth.config;

import com.taoyuanx.auth.SimpleTokenManager;
import com.taoyuanx.auth.TokenManager;
import com.taoyuanx.auth.mac.HMacAlgorithms;
import com.taoyuanx.auth.sign.ISign;
import com.taoyuanx.auth.sign.impl.HMacSign;
import com.taoyuanx.auth.sign.impl.RsaSign;
import com.taoyuanx.ca.auth.controller.HmacAuthApiController;
import com.taoyuanx.ca.auth.controller.RsaAuthApiController;
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
    @ConditionalOnMissingBean(TokenManager.class)
    public TokenManager tokenManager(AuthProperties authProperties) throws Exception {
        if (Objects.isNull(authProperties.getType())) {
            return null;
        }
        authProperties.afterPropertiesSet();
        ISign sign = null;
        if (authProperties.isRsa()) {
            AuthProperties.RsaAuthProperties rsa = authProperties.getRsa();
            if (rsa.isVerifyOnly()) {
                sign = new RsaSign(rsa.getServerPublicKey(), rsa.getSignAlg());
            } else {
                sign = new RsaSign(rsa.getServerPublicKey(), rsa.getServerPrivateKey(), rsa.getSignAlg());
            }
        } else {
            AuthProperties.HmacAuthProperties hmac = authProperties.getHmac();
            sign = new HMacSign(HMacAlgorithms.valueOf(authProperties.getHmac().getSignAlg()), hmac.getKey().getBytes("UTF-8"));
        }
        return new SimpleTokenManager(sign);


    }


}
