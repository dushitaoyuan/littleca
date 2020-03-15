package com.taoyuanx.auth.utils;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * @author dushitaoyuan
 * @date 2019/9/11 12:30
 * @desc: aes 加密
 */
public class AesUtil {
    /**
     * pading 初始向量
     */
    public static final byte[] iv = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
    /**
     * 加密模式
     */
    public static final String CIPHER_MODE = "AES/CBC/PKCS5Padding";

    /**
     * 加密二进制
     *
     * @param data
     * @param password
     * @return
     * @throws Exception
     */
    public static byte[] encrypt(byte[] data, String password) throws Exception {
        Cipher cipher = AesHelper.getAesCipher(iv, password.getBytes(), Cipher.ENCRYPT_MODE, CIPHER_MODE);
        byte[] result = cipher.doFinal(data);
        return result;
    }

    /**
     * 解密二进制
     *
     * @param data
     * @param password
     * @throws Exception
     * @retu
     */
    public static byte[] decrypt(byte[] data, String password) throws Exception {
        Cipher cipher = AesHelper.getAesCipher(iv, password.getBytes(), Cipher.DECRYPT_MODE, CIPHER_MODE);
        byte[] result = cipher.doFinal(data);
        return result;
    }


    /**
     * 加密字符串
     *
     * @param data
     * @param password
     * @return
     * @throws Exception
     */
    public static String encrypt(String data, String password) throws Exception {
        return Base64.encodeBase64String(encrypt(data.getBytes(), password));
    }

    /**
     * 解密字符串
     *
     * @param base64Data
     * @param password
     * @return
     * @throws Exception
     */
    public static String decrypt(String base64Data, String password) throws Exception {
        byte[] decodeBase64 = Base64.decodeBase64(base64Data);
        byte[] decrypt = decrypt(decodeBase64, password);
        return new String(decrypt);
    }

    /**
     * 文件加解密
     */
    public static int BUFF_SIZE = 4 << 20;

    public static void encryptInputStream(InputStream inputStream, String password, OutputStream out) throws Exception {
        try {
            Cipher cipher = AesHelper.getAesCipher(iv, password.getBytes(), Cipher.ENCRYPT_MODE, CIPHER_MODE);

            CipherInputStream cipherInputStream = new CipherInputStream(inputStream, cipher);
            byte[] buf = new byte[BUFF_SIZE];
            int len = -1;
            while ((len = cipherInputStream.read(buf)) != -1) {
                out.write(buf, 0, len);
                out.flush();
            }
            out.close();
            cipherInputStream.close();
            inputStream.close();
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 加密输入流
     *
     * @param inputStream
     * @return
     */
    public static InputStream encryptInputStream(InputStream inputStream, String password) throws Exception {
        try {
            if (inputStream == null) {
                return null;
            }
            Cipher cipher = AesHelper.getAesCipher(iv, password.getBytes(), Cipher.ENCRYPT_MODE, CIPHER_MODE);
            CipherInputStream cipherInputStream = new CipherInputStream(inputStream, cipher);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buf = new byte[BUFF_SIZE];
            int len = -1;
            while ((len = cipherInputStream.read(buf)) != -1) {
                out.write(buf, 0, len);
            }
            byte[] result = cipher.doFinal(out.toByteArray());
            return new ByteArrayInputStream(result); // 加密
        } catch (Exception e) {
            throw e;
        }

    }


    public static void decryptInputStream(InputStream inputStream, String password, OutputStream out) throws Exception {
        try {
            Cipher cipher = AesHelper.getAesCipher(iv, password.getBytes(), Cipher.DECRYPT_MODE, CIPHER_MODE);
            CipherOutputStream cipherOutputStream = new CipherOutputStream(out, cipher);
            byte[] buf = new byte[BUFF_SIZE];
            int len = -1;
            while ((len = inputStream.read(buf)) != -1) {
                cipherOutputStream.write(buf, 0, len);
                cipherOutputStream.flush();
            }
            cipherOutputStream.close();
            out.close();
            inputStream.close();
        } catch (Exception e) {
            throw e;
        }
    }


}