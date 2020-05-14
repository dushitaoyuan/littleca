package com.taoyuanx.auth.dto.request;

import lombok.Data;


/**
 * @author dushitaoyuan
 * @desc token续期请求参数
 * @date 2020/2/17
 */
@Data
public class AuthRefreshRequestDTO implements ISignDTO {

    private String refreshToken;


    /**
     * 时间戳
     */
    private Long timestamp;

    /**
     * refreshToken+timestamp 签名
     */
    private String sign;


    @Override
    public String toSignStr() {
        return refreshToken+timestamp;
    }
}
