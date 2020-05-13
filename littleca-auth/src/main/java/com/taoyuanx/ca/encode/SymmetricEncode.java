package com.taoyuanx.ca.encode;


import com.taoyuanx.ca.encode.api.Encode;
import com.taoyuanx.ca.encode.api.SymmetricConfig;
import com.taoyuanx.ca.exception.EncodeException;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import java.security.Security;

/**
 * @author 都市桃源
 * 2018年6月2日下午8:19:27
 * TODO 对称加密算法 涉及 加密模式,填充方式 编码方式
 * 如 des,3des, aes,blowfish,rc2,rc4
 * <p>
 * DES                  key size must be equal to 56
 * DESede(TripleDES)    key size must be equal to 112 or 168
 * AES                  key size must be equal to 128, 192 or 256,but 192 and 256 bits may not be available
 * Blowfish             key size must be multiple of 8, and can only range from 32 to 448 (inclusive)
 * RC2                  key size must be between 40 and 1024 bits
 * RC4(ARCFOUR)         key size must be between 40 and 1024 bits
 */
public class SymmetricEncode implements Encode {
    private SymmetricConfig config;

    public SymmetricEncode(SymmetricConfig config) {
        super();
        this.config = config;
        if (config.isNeddBC()) {
            Security.addProvider(new BouncyCastleProvider());
        }
    }

    @Override
    public byte[] encode(byte[] content) {
        try {
            if (null == content) {
                throw new EncodeException("content can't be null");
            }
            Cipher cipher = getCipher(Cipher.ENCRYPT_MODE);
            return cipher.doFinal(content);
        } catch (Exception e) {
            throw new EncodeException(config.getType() + " encode exception", e);
        }
    }

    @Override
    public byte[] decode(byte[] content) {
        try {
            if (null == content) {
                throw new EncodeException("content can't be null");
            }
            Cipher cipher = getCipher(Cipher.DECRYPT_MODE);
            return cipher.doFinal(content);
        } catch (Exception e) {
            throw new EncodeException(config.getType() + " decode exception", e);
        }
    }

    private Cipher getCipher(int mode) throws Exception {
        Cipher cipher = Cipher.getInstance(config.getType());
        if (config.hasIv()) {
            cipher.init(mode, config.getKey(), new IvParameterSpec(config.getIv()));
        } else {
            cipher.init(mode, config.getKey());
        }
        return cipher;
    }

}
