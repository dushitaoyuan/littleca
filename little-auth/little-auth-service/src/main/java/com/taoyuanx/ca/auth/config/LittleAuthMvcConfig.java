package com.taoyuanx.ca.auth.config;


import com.taoyuanx.auth.dto.Result;
import com.taoyuanx.auth.dto.ResultBuilder;
import com.taoyuanx.auth.token.exception.AuthException;
import com.taoyuanx.ca.auth.utils.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;


/**
 *
 */
@Configuration
@ControllerAdvice
@Slf4j
public class LittleAuthMvcConfig implements WebMvcConfigurer {
    @ExceptionHandler(Throwable.class)
    public ModelAndView handleException(Throwable e, HttpServletRequest request, HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView();
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        Integer errorCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
        String errorMsg = "系统异常:" + e.getMessage();
        if (e instanceof AuthException) {
            errorMsg = e.getMessage();
            AuthException serviceException = ((AuthException) e);
            if (Objects.nonNull(serviceException.getErrorCode())) {
                errorCode = serviceException.getErrorCode();
            }
        } else {
            log.error("系统异常", e);
        }
        Result errorResult = ResultBuilder.failed(errorCode, errorMsg);
        //返回json
        ResponseUtil.responseJson(response, errorResult, httpStatus.value());
        return modelAndView;
    }
}
