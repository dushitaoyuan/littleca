package com.taoyuanx.ca.encode;


import com.taoyuanx.ca.encode.api.HmacConfig;
import com.taoyuanx.ca.encode.api.Sign;
import com.taoyuanx.ca.exception.SignException;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.Security;

/**
 * hmac算法签名实现
 *
 * @author 都市桃源
 */

public class HmacSign implements Sign {
    private HmacConfig config;

    public HmacSign(HmacConfig config) {
        super();
        this.config = config;
        if (config.isNeedBc()) {
            Security.addProvider(new BouncyCastleProvider());
        }
    }

    @Override
    public byte[] sign(byte[] data) {
        try {
            if (null == data) {
                throw new SignException("签名数据不能为空");
            }
            Mac mac = Mac.getInstance(config.getHmacAlgorithm());
            mac.init(new SecretKeySpec(config.getKey(), config.getHmacAlgorithm()));
            return mac.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean verify(byte[] data, byte[] signData) {
        return MessageDigest.isEqual(sign(data), signData);
    }

}
