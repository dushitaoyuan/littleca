package com.taoyuanx.ca.auth.controller;

import com.taoyuanx.auth.dto.Result;
import com.taoyuanx.auth.dto.ResultBuilder;
import com.taoyuanx.ca.auth.constants.AuthType;
import com.taoyuanx.ca.auth.dto.AuthRefreshRequestDTO;
import com.taoyuanx.ca.auth.dto.AuthRequestDTO;
import com.taoyuanx.ca.auth.dto.AuthResultDTO;
import com.taoyuanx.ca.auth.helper.ApiAccountAuthHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author dushitaoyuan
 * @desc hmac认证服务接口
 * @date 2019/12/20
 */
@RequestMapping("v1/auth")
@Slf4j
public class HmacAuthApiController {
    @Autowired
    ApiAccountAuthHelper apiAccountAuthHelper;
    @PostMapping
    @ResponseBody
    public Result auth(@RequestBody AuthRequestDTO authRequestDTO, HttpServletRequest request) {
        AuthResultDTO authResultDTO = apiAccountAuthHelper.auth(authRequestDTO, AuthType.HMAC, request).getAuthResult();
        return ResultBuilder.success(authResultDTO);
    }

    @PostMapping("refresh")
    @ResponseBody
    public Result authRefresh(@RequestBody AuthRefreshRequestDTO authRefreshRequestDTO, HttpServletRequest request) {
        AuthResultDTO authResultDTO = apiAccountAuthHelper.authRefresh(authRefreshRequestDTO,  AuthType.HMAC, request).getAuthResult();
        return ResultBuilder.success(authResultDTO);
    }
}
