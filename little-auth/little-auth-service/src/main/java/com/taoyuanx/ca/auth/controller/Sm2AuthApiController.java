package com.taoyuanx.ca.auth.controller;

import cn.hutool.core.util.StrUtil;
import com.taoyuanx.auth.TokenManager;
import com.taoyuanx.auth.TokenTypeEnum;
import com.taoyuanx.auth.dto.ApiAccountDTO;
import com.taoyuanx.auth.dto.Result;
import com.taoyuanx.auth.dto.ResultBuilder;
import com.taoyuanx.auth.exception.AuthException;
import com.taoyuanx.auth.sign.ISign;
import com.taoyuanx.auth.sign.impl.Sm2Sign;
import com.taoyuanx.auth.token.Token;
import com.taoyuanx.auth.utils.JSONUtil;
import com.taoyuanx.ca.auth.config.AuthProperties;
import com.taoyuanx.ca.auth.constants.AuthType;
import com.taoyuanx.ca.auth.dto.AuthRefreshRequestDTO;
import com.taoyuanx.ca.auth.dto.AuthRequestDTO;
import com.taoyuanx.ca.auth.dto.AuthResultDTO;
import com.taoyuanx.ca.auth.dto.EncodeRequestDTO;
import com.taoyuanx.ca.auth.helper.ApiAccountAuthHelper;
import com.taoyuanx.ca.auth.service.ApiAccountService;
import com.taoyuanx.ca.core.api.impl.SM2;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.security.PublicKey;
import java.util.concurrent.TimeUnit;

/**
 * @author dushitaoyuan
 * @desc sm2认证服务接口
 * @date 2019/12/20
 */
@RequestMapping("v1/auth")
@Slf4j
public class Sm2AuthApiController {
    @Autowired
    ApiAccountService apiAccountService;
    @Autowired
    AuthProperties authProperties;

    @Autowired
    TokenManager rsaTokenManager;

    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    ApiAccountAuthHelper apiAccountAuthHelper;

    private SM2 sm2 = new SM2();

    @PostMapping
    @ResponseBody
    public Result auth(@RequestBody EncodeRequestDTO authRequestEncode, HttpServletRequest request) throws Exception {
        String encodeData = authRequestEncode.getData();
        if (StrUtil.isEmpty(encodeData)) {
            throw new AuthException("参数为空");
        }
        AuthProperties.Sm2AuthProperties sm2Config = authProperties.getSm2();
        String data = null;
        try {
            data = new String(sm2.decrypt(Base64.decodeBase64(encodeData), sm2Config.getServerPrivateKey()), "UTF-8");
        } catch (Exception e) {
            log.error("认证服务解密异常:[{}],加密数据:[{}],异常信息:{}", e, encodeData, e);
            throw new AuthException("认证服务解密异常");
        }
        AuthRequestDTO authRequestDTO = JSONUtil.parseObject(data, AuthRequestDTO.class);
        apiAccountAuthHelper.checkAuthRequest(authRequestDTO);
        String apiAccount = authRequestDTO.getApiAccount();
        ApiAccountDTO apiAccountDTO = apiAccountService.getByApiAccount(apiAccount);
        apiAccountAuthHelper.checkApiAccount(apiAccountDTO, AuthType.AUTH_TYPE_RSA);
        apiAccountAuthHelper.checkAuthRequestSign(authRequestDTO, newSign(apiAccountDTO.getPublicKey()));
        //加密结果
        AuthResultDTO authResultDTO = apiAccountAuthHelper.successAuth(apiAccountDTO, request);
        String encryptData = Base64.encodeBase64URLSafeString(sm2.encrypt(JSONUtil.toJsonBytes(authResultDTO), apiAccountDTO.getPublicKey()));
        return ResultBuilder.successResult(encryptData);
    }

    @PostMapping("refresh")
    @ResponseBody
    public Result authRefresh(@RequestBody EncodeRequestDTO authRequestEncode, HttpServletRequest request) throws Exception {
        String encodeData = authRequestEncode.getData();
        if (StrUtil.isEmpty(encodeData)) {
            throw new AuthException("参数为空");
        }
        AuthProperties.Sm2AuthProperties sm2Config = authProperties.getSm2();
        String data = null;
        try {
            data = new String(sm2.decrypt(Base64.decodeBase64(encodeData), sm2Config.getServerPrivateKey()), "UTF-8");
        } catch (Exception e) {
            log.error("认证服务解密异常:[{}],加密数据:[{}]", e, encodeData);
            throw new AuthException("认证服务解密异常");
        }
        /**
         * 校验refreshToken 校验逻辑
         * 1. refreshToken 是否验证通过
         * 2.refreshToken 只能使用一次
         */
        AuthRefreshRequestDTO authRefreshRequestDTO = JSONUtil.parseObject(data, AuthRefreshRequestDTO.class);
        apiAccountAuthHelper.checkAuthRefreshRequest(authRefreshRequestDTO);
        Token refreshToken = rsaTokenManager.parseToken(authRefreshRequestDTO.getRefreshToken());
        rsaTokenManager.verify(refreshToken, TokenTypeEnum.REFRESH);
        ApiAccountDTO apiAccountDTO = apiAccountService.getByApiAccount(refreshToken.getApiAccount());
        apiAccountAuthHelper.checkApiAccount(apiAccountDTO, AuthType.AUTH_TYPE_RSA);
        apiAccountAuthHelper.checkAuthRequestSign(authRefreshRequestDTO.getRefreshToken(), authRefreshRequestDTO.getSign(), newSign(apiAccountDTO.getPublicKey()));
        String refreshTokenCacheKey = apiAccountAuthHelper.newTokenCacheKey(authRefreshRequestDTO.getRefreshToken());
        if (stringRedisTemplate.hasKey(refreshTokenCacheKey)) {
            throw new AuthException("refreshToken已失效");
        }
        //标记
        stringRedisTemplate.opsForValue().set(refreshTokenCacheKey, "1", refreshToken.getEndTime() - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
        //加密结果
        AuthResultDTO authResultDTO = apiAccountAuthHelper.successAuth(apiAccountDTO, request);
        String encryptData = Base64.encodeBase64URLSafeString(sm2.encrypt(JSONUtil.toJsonBytes(authResultDTO), apiAccountDTO.getPublicKey()));
        return ResultBuilder.successResult(encryptData);
    }


    private ISign newSign(PublicKey rsaPublicKey) {
        return new Sm2Sign(rsaPublicKey, "SM3WITHSM2");
    }

}
