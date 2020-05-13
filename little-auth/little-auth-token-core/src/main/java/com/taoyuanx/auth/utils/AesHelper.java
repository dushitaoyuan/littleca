package com.taoyuanx.auth.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author dushitaoyuan
 * @date 2019/9/2521:17
 * @desc: Aes 帮助类
 */
public class AesHelper {


    /**
     * 密码位数 支持，16，24，32 分别对应 aes 128，192，256
     * @param iv 初始向量
     * @param password 密码
     * @param mode  操作模式(加密解密)
     * @param cipher_mode 加密模式
     * @return
     * @throws Exception
     */
    public static Cipher getAesCipher(byte[] iv,byte[] password, int mode,String cipher_mode) throws Exception {
        //创建aes格式的密码
        SecretKeySpec key = new SecretKeySpec(password, "AES");
        //设置加密模式
        Cipher cipher = Cipher.getInstance(cipher_mode);
        //设置初始向量和mode
        cipher.init(mode, key, new IvParameterSpec(iv));
        return cipher;
    }
}
