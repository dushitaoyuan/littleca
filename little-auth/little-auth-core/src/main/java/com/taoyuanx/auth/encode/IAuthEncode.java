package com.taoyuanx.auth.encode;

import com.taoyuanx.auth.dto.request.EncodeRequestDTO;
import com.taoyuanx.auth.dto.response.EncodeResponseDTO;

/**
 * @author dushitaoyuan
 * @date 2020/5/1123:09
 * @desc: 认证加密接口
 */
public interface IAuthEncode {
    /**
     * 加密
     */
    EncodeRequestDTO encode(String data);

    /**
     * 解密
     */
    String decode(EncodeResponseDTO encodeData);

}
