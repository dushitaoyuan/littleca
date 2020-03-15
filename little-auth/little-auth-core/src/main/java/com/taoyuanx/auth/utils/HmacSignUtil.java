package com.taoyuanx.auth.utils;


import com.taoyuanx.auth.mac.HMacAlgorithms;
import com.taoyuanx.ca.core.bc.ProviderInstance;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.SM3Digest;
import org.bouncycastle.crypto.macs.HMac;
import org.bouncycastle.crypto.params.KeyParameter;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.Security;

/**
 * hmac算法签名实现
 *
 * @author 都市桃源
 */

public class HmacSignUtil {

    static {
        Security.addProvider(ProviderInstance.getBCProvider());

    }

    public static com.taoyuanx.auth.sign.Mac getHmac(Digest digest, byte[] key) {
        KeyParameter keyParameter = new KeyParameter(key);
        HMac hmac = new HMac(new SM3Digest());
        hmac.init(keyParameter);

        return new com.taoyuanx.auth.sign.Mac() {
            @Override
            public byte[] doFinal(byte[] bytes) {
                hmac.update(bytes, 0, bytes.length);
                byte[] result = new byte[hmac.getMacSize()];
                hmac.doFinal(result, 0);
                return result;
            }
        };

    }


    public static com.taoyuanx.auth.sign.Mac getHmac(HMacAlgorithms macAlgorithms, byte[] key) {
        try {
            switch (macAlgorithms) {
                case HMAC_SM3:
                    return getHmac(new SM3Digest(), key);
                default:
                    Mac mac = Mac.getInstance(macAlgorithms.getName());
                    mac.init(new SecretKeySpec(key, macAlgorithms.getName()));
                    return new com.taoyuanx.auth.sign.Mac() {
                        @Override
                        public byte[] doFinal(byte[] bytes) {
                            return mac.doFinal(bytes);
                        }
                    };
            }

        } catch (Exception e) {

            throw new RuntimeException(e);
        }
    }


}
