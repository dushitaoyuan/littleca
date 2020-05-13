package com.taoyuanx.ca.auth.controller;

import cn.hutool.core.util.StrUtil;
import com.taoyuanx.auth.AuthType;
import com.taoyuanx.auth.dto.response.AuthResultDTO;
import com.taoyuanx.auth.dto.response.EncodeResponseDTO;
import com.taoyuanx.auth.dto.Result;
import com.taoyuanx.auth.dto.ResultBuilder;
import com.taoyuanx.auth.exception.AuthException;
import com.taoyuanx.auth.token.TokenManager;
import com.taoyuanx.auth.utils.JSONUtil;
import com.taoyuanx.ca.auth.config.AuthProperties;
import com.taoyuanx.ca.auth.dto.AuthRefreshRequestDTO;
import com.taoyuanx.ca.auth.dto.AuthRequestDTO;
import com.taoyuanx.auth.dto.request.EncodeRequestDTO;
import com.taoyuanx.ca.auth.helper.ApiAccountAuthHelper;
import com.taoyuanx.ca.auth.helper.AuthResultWrapper;
import com.taoyuanx.ca.auth.service.ApiAccountService;
import com.taoyuanx.ca.core.api.impl.RSA;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author dushitaoyuan
 * @desc rsa认证服务接口
 * @date 2019/12/20
 */
@RequestMapping("v1/auth")
@Slf4j
public class RsaAuthApiController {
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

    private RSA rsa = new RSA();

    @PostMapping
    @ResponseBody
    public Result auth(@RequestBody EncodeRequestDTO authRequestEncode, HttpServletRequest request) throws Exception {
        String encodeData = authRequestEncode.getData();
        if (StrUtil.isEmpty(encodeData)) {
            throw new AuthException("参数为空");
        }
        AuthProperties.RsaAuthProperties rsaConfig = authProperties.getRsa();
        String data = null;
        try {
            data = new String(rsa.decrypt(Base64.decodeBase64(encodeData), rsaConfig.getServerPrivateKey()), "UTF-8");
        } catch (Exception e) {
            log.error("认证服务解密异常:[{}],加密数据:[{}],异常信息:{}", e, encodeData, e);
            throw new AuthException("认证服务解密异常");
        }
        AuthRequestDTO authRequestDTO = JSONUtil.parseObject(data, AuthRequestDTO.class);
        AuthResultWrapper resultWrapper = apiAccountAuthHelper.auth(authRequestDTO, AuthType.RSA, request);
        return encodeResult(resultWrapper, rsaConfig);
    }

    @PostMapping("refresh")
    @ResponseBody
    public Result authRefresh(@RequestBody EncodeRequestDTO authRequestEncode, HttpServletRequest request) throws Exception {
        String encodeData = authRequestEncode.getData();
        if (StrUtil.isEmpty(encodeData)) {
            throw new AuthException("参数为空");
        }
        AuthProperties.RsaAuthProperties rsaConfig = authProperties.getRsa();
        String data = null;
        try {
            data = new String(rsa.decrypt(Base64.decodeBase64(encodeData), rsaConfig.getServerPrivateKey()), "UTF-8");
        } catch (Exception e) {
            log.error("认证服务解密异常:[{}],加密数据:[{}]", e, encodeData);
            throw new AuthException("认证服务解密异常");
        }
        AuthRefreshRequestDTO authRefreshRequestDTO = JSONUtil.parseObject(data, AuthRefreshRequestDTO.class);
        AuthResultWrapper resultWrapper = apiAccountAuthHelper.authRefresh(authRefreshRequestDTO, AuthType.RSA, request);
        return encodeResult(resultWrapper, rsaConfig);
    }

    private Result encodeResult(AuthResultWrapper resultWrapper, AuthProperties.RsaAuthProperties rsaConfig) throws Exception {
        AuthResultDTO authResultDTO = resultWrapper.getAuthResult();
        EncodeResponseDTO encodeResponseDTO = new EncodeResponseDTO();
        byte[] data = JSONUtil.toJsonBytes(authResultDTO);
        if (rsaConfig.isResultEncode()) {
            String encryptData = Base64.encodeBase64URLSafeString(rsa.encrypt(data, resultWrapper.getApiAccount().getPublicKey()));
            encodeResponseDTO.setData(encryptData);
            encodeResponseDTO.setEncode(EncodeResponseDTO.ENCODE_YES);
        } else {
            encodeResponseDTO.setEncode(EncodeResponseDTO.ENCODE_NO);
            encodeResponseDTO.setData(JSONUtil.toJsonString(authResultDTO));
        }
        encodeResponseDTO.setSign(Base64.encodeBase64String(rsa.sign(data,rsaConfig.getServerPrivateKey(),rsaConfig.getSignAlg())));
        return ResultBuilder.success(encodeResponseDTO);

    }

}
