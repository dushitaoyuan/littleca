package com.taoyuanx.auth.utils;


import com.taoyuanx.auth.sign.impl.hmac.HMacAlgorithms;
import com.taoyuanx.ca.core.bc.ProviderInstance;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.SM3Digest;
import org.bouncycastle.crypto.macs.HMac;
import org.bouncycastle.crypto.params.KeyParameter;

import com.taoyuanx.auth.sign.impl.hmac.Mac;

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

    public static Mac getHmac(Digest digest, byte[] key) {
        KeyParameter keyParameter = new KeyParameter(key);
        HMac hmac = new HMac(new SM3Digest());
        hmac.init(keyParameter);

        return new Mac() {
            @Override
            public byte[] doFinal(byte[] bytes) {
                hmac.update(bytes, 0, bytes.length);
                byte[] result = new byte[hmac.getMacSize()];
                hmac.doFinal(result, 0);
                return result;
            }
        };

    }


    public static Mac getHmac(HMacAlgorithms macAlgorithms, byte[] key) {
        try {
            switch (macAlgorithms) {
                case HMAC_SM3:
                    return getHmac(new SM3Digest(), key);
                default:
                    javax.crypto.Mac mac = javax.crypto.Mac.getInstance(macAlgorithms.getName());
                    mac.init(new SecretKeySpec(key, macAlgorithms.getName()));
                    return new Mac() {
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
