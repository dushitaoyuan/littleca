package com.taoyuanx.ca.encode.api;

/**
 * @author 都市桃源
 * 2018年5月28日下午10:43:48
 * 加密 解密
 */
public interface Encode {
    /**
     * 加密
     *
     * @param content
     * @return
     */
    byte[] encode(byte[] content);

    /**
     * 解密
     *
     * @param content
     * @return
     */
    byte[] decode(byte[] content);

}
