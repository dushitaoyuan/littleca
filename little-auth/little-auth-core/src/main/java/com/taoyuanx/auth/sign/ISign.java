package com.taoyuanx.auth.sign;

/**
 * @author dushitaoyuan
 * @desc 签名接口
 * @date 2020/2/24
 */
public interface ISign {
    String sign(String data) throws Exception;

    boolean verifySign(String data, String sign) throws Exception;

    byte[] sign(byte[] data) throws Exception;

    boolean verifySign(byte[] data, byte[] sign) throws Exception;

}
