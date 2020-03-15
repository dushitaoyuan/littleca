package com.taoyuanx.ca.auth.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author dushitaoyuan
 * @desc 认证结果
 * @date 2020/2/17
 */
@Data
public class AuthResultDTO implements Serializable {
    private String businessToken;
    private String refreshToken;
    //过期时间
    private Long expire;

}
