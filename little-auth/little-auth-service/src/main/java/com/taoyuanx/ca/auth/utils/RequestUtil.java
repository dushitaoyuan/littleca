package com.taoyuanx.ca.auth.utils;

import cn.hutool.core.util.StrUtil;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author dushitaoyuan
 * @desc 请求工具类
 * @date 2020/2/17
 */
public class RequestUtil {

    public static String getRequestValue(HttpServletRequest request, String key) {
        String value = request.getHeader(key);
        if (StrUtil.isNotEmpty(value)) {
            return value;
        }
        return request.getParameter(key);
    }


    /**
     * 获取请求ip
     *
     * @return
     */
    public static String getRemoteIp() {
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.
                        getRequestAttributes()).getRequest();
        return getRemoteIp(request);
    }


    public static String getRemoteIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (isValidIp(ip)) {
            return ip;
        }
        ip = request.getHeader("X-Real-IP");
        if (isValidIp(ip)) {
            return ip;
        }
        ip = request.getHeader("Proxy-Client-IP");
        if (isValidIp(ip)) {
            return ip;
        }
        ip = request.getHeader("WL-Proxy-Client-IP");
        if (isValidIp(ip)) {
            return ip;
        }
        ip = request.getHeader("HTTP_CLIENT_IP");
        if (isValidIp(ip)) {
            return ip;
        }
        ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        if (isValidIp(ip)) {
            return ip;
        }
        return request.getRemoteAddr();
    }

    private static boolean isValidIp(String ip) {
        if (StrUtil.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
            return true;
        }
        return false;
    }
}