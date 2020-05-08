package com.taoyuanx.ca.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author dushitaoyuan
 * @desc 加密返回结果(对称加密, 暂时不用)
 * @date 2020/2/17
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EncodeResponseDTO implements Serializable {
    private String data;
    private String key;
    private String sign;
}
