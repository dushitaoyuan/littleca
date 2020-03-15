package com.taoyuanx.ca.auth.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author dushitaoyuan
 * @desc token续期请求参数
 * @date 2020/2/17
 */
@Data
public class AuthRefreshRequestDTO implements Serializable {
    private String refreshToken;
    private String sign;
}
