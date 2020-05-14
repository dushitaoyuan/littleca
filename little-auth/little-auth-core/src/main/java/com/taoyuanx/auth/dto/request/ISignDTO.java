package com.taoyuanx.auth.dto.request;

import java.io.Serializable;

/**
 * @author dushitaoyuan
 * @date 2020/5/8
 */
public interface ISignDTO extends Serializable {
    /**
     * 构造签名字符
     */
    String toSignStr();
}
