package com.taoyuanx.ca.auth.controller;

import com.taoyuanx.auth.TokenManager;
import com.taoyuanx.auth.TokenTypeEnum;
import com.taoyuanx.auth.dto.ApiAccountDTO;
import com.taoyuanx.auth.dto.Result;
import com.taoyuanx.auth.dto.ResultBuilder;
import com.taoyuanx.auth.exception.AuthException;
import com.taoyuanx.auth.mac.HMacAlgorithms;
import com.taoyuanx.auth.sign.ISign;
import com.taoyuanx.auth.sign.impl.HMacSign;
import com.taoyuanx.auth.token.Token;
import com.taoyuanx.ca.auth.config.AuthProperties;
import com.taoyuanx.ca.auth.dto.AuthRefreshRequestDTO;
import com.taoyuanx.ca.auth.dto.AuthRequestDTO;
import com.taoyuanx.ca.auth.helper.ApiAccountAuthHelper;
import com.taoyuanx.ca.auth.service.ApiAccountService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeUnit;

/**
 * @author dushitaoyuan
 * @desc hmac认证服务接口
 * @date 2019/12/20
 */
@RequestMapping("v1/auth")
@Slf4j

public class HmacAuthApiController {
    @Autowired
    ApiAccountService apiAccountService;
    @Autowired
    AuthProperties authProperties;

    @Autowired
    TokenManager hmacTokenManager;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    ApiAccountAuthHelper apiAccountAuthHelper;


    @PostMapping
    @ResponseBody
    public Result auth(@RequestBody AuthRequestDTO authRequestDTO, HttpServletRequest request) {
        apiAccountAuthHelper.checkAuthRequest(authRequestDTO);
        String apiAccount = authRequestDTO.getApiAccount();
        ApiAccountDTO apiAccountDTO = apiAccountService.getByApiAccount(apiAccount);
        apiAccountAuthHelper.checkAuthRequestSign(authRequestDTO, newSign(apiAccountDTO.getApiSecret()));
        apiAccountAuthHelper.checkApiAccount(apiAccountDTO, ApiAccountAuthHelper.AUTH_TYPE_HMAC);
        return ResultBuilder.success(apiAccountAuthHelper.successAuth(apiAccountDTO, request));
    }

    @PostMapping("refresh")
    @ResponseBody
    public Result authRefresh(@Valid @RequestBody AuthRefreshRequestDTO authRefreshRequestDTO, HttpServletRequest request) {
        apiAccountAuthHelper.checkAuthRefreshRequest(authRefreshRequestDTO);
        /**
         * 校验refreshToken 校验逻辑
         * 1. refreshToken 是否验证通过
         * 2.refreshToken 只能使用一次
         */
        apiAccountAuthHelper.checkAuthRefreshRequest(authRefreshRequestDTO);
        Token refreshToken = hmacTokenManager.parseToken(authRefreshRequestDTO.getRefreshToken());
        hmacTokenManager.verify(refreshToken, TokenTypeEnum.REFRESH);
        ApiAccountDTO apiAccountDTO = apiAccountService.getByApiAccount(refreshToken.getApiAccount());
        apiAccountAuthHelper.checkApiAccount(apiAccountDTO, ApiAccountAuthHelper.AUTH_TYPE_HMAC);
        apiAccountAuthHelper.checkAuthRequestSign(authRefreshRequestDTO.getRefreshToken(), authRefreshRequestDTO.getSign(), newSign(apiAccountDTO.getApiSecret()));
        String refreshTokenCacheKey = apiAccountAuthHelper.newRefreshTokenCacheKey(authRefreshRequestDTO.getRefreshToken());
        if (stringRedisTemplate.hasKey(refreshTokenCacheKey)) {
            throw new AuthException("refreshToken已失效");
        }
        //标记
        stringRedisTemplate.opsForValue().set(refreshTokenCacheKey, "1", refreshToken.getEndTime() - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
        return ResultBuilder.success(apiAccountAuthHelper.successAuth(apiAccountDTO, request));
    }

    private ISign newSign(String key) {
        try {
            return new HMacSign(HMacAlgorithms.HMAC_SHA_256, key.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

}
