package com.taoyuanx.auth.dto.response;

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
    private String sign;
    /**
     * 是否加密 1 加密 0 不加密
     */
    private Integer encode;

    /**
     * 对称加密key
     */
    private String key;

    public static final Integer ENCODE_YES=1,ENCODE_NO=0;


}
