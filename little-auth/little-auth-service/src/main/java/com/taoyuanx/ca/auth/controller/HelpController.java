package com.taoyuanx.ca.auth.controller;

import com.taoyuanx.auth.dto.Result;
import com.taoyuanx.auth.dto.ResultBuilder;
import com.taoyuanx.ca.auth.constants.AuthCaheConstant;
import com.taoyuanx.ca.auth.helper.ApiAccountAuthHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

/**
 * @author dushitaoyuan
 * @desc 辅助api
 * @date 2020/5/7
 */
@Controller
public class HelpController {
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    ApiAccountAuthHelper apiAccountAuthHelper;

    /**
     * 时钟服务 获取服务端时间
     */
    @GetMapping(value = "timestamp")
    public void auth(HttpServletResponse response) throws Exception {
        response.setHeader("timestamp", String.valueOf(System.currentTimeMillis()));
    }

    /**
     * 提供主动注销token,主动激活token功能(可选,不常用,只允许管理端使用) 需配合认证client 再请求一次服务端验证token是否被主动注销
     * 实现逻辑:
     * 服务端缓存注销表,client验证通过后,在请求服务端验证token是否被主动注销,client查询结果缓存3分钟
     */
    /**
     * 主动注销token
     */
    @GetMapping(value = "v1/token/cancel")
    public void cancelToken(@RequestParam("h") String tokenMd5, @RequestParam("end") Long endTime, HttpServletResponse response) throws Exception {
        String tokenKey = apiAccountAuthHelper.newTokenCacheKeyForHash(tokenMd5.toLowerCase());
        long sub = endTime - System.currentTimeMillis();
        if (sub > AuthCaheConstant.TOKEN_TIME_WINDOW) {
            stringRedisTemplate.opsForValue().set(tokenKey, "1", sub, TimeUnit.MILLISECONDS);
            return;
        }
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

    }

    /**
     * 主动激活token
     */
    @GetMapping(value = "v1/token/active")
    public void activeToken(@RequestParam("h") String tokenMd5, @RequestParam("end") Long endTime, HttpServletResponse response) throws Exception {
        String tokenKey = apiAccountAuthHelper.newTokenCacheKeyForHash(tokenMd5.toLowerCase());
        long sub = endTime - System.currentTimeMillis();
        if (sub > AuthCaheConstant.TOKEN_TIME_WINDOW) {
            stringRedisTemplate.delete(tokenKey);
            return;
        }
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }


    /**
     * 判断token是否被注销
     * 1 注销 0 正常
     */
    @GetMapping(value = "v1/token/status")
    @ResponseBody
    public Result tokenStatus(@RequestParam("h") String tokenMd5) throws Exception {
        String tokenKey = apiAccountAuthHelper.newTokenCacheKeyForHash(tokenMd5.toLowerCase());
        if (stringRedisTemplate.hasKey(tokenKey)) {
            return ResultBuilder.success(stringRedisTemplate.opsForValue().get(tokenKey));
        }
        return ResultBuilder.success(0);
    }

}