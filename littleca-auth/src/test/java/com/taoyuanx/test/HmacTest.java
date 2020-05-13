package com.taoyuanx.test;


import com.taoyuanx.ca.encode.HmacSign;
import com.taoyuanx.ca.encode.api.HmacConfig;
import com.taoyuanx.ca.encode.eum.HmacAlgorithms;
import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.crypto.digests.SM3Digest;
import org.bouncycastle.crypto.macs.HMac;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.Test;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;


public class HmacTest {
    @Test
    public void testHmac() {
        HmacConfig config = new HmacConfig();
        config.setHmacAlgorithm(HmacAlgorithms.HMAC_MD5.getName());
        config.setKey(getKey(168));
        config.setNeedBc(true);
        HmacSign sign = new HmacSign(config);
        HmacAlgorithms[] values = HmacAlgorithms.values();
        Set<String> set = new HashSet<String>();
        for (HmacAlgorithms v : values) {
            config.setHmacAlgorithm(v.getName());
            String signStr = Base64.encodeBase64String(sign.sign("123456".getBytes()));
            if (null == signStr) {
                set.add(v.getName());
            }
            boolean verify = sign.verify("123456".getBytes(), Base64.decodeBase64(signStr));
            System.out.println("alg:" + v.getName() + "\tsign:" + signStr + "\tverfify\t" + verify);
        }

        System.out.println(set);

    }

    public static void testHmac(String alg, byte[] data, byte[] key) {
        HmacConfig config = new HmacConfig();
        config.setHmacAlgorithm(alg);
        config.setKey(key);
        config.setNeedBc(true);
        HmacSign sign = new HmacSign(config);
        String signStr = Base64.encodeBase64String(sign.sign(data));
        boolean verify = sign.verify(data, Base64.decodeBase64(signStr));
        System.out.println("alg:" + alg + "\tsign:" + signStr + "\tverfify" + verify);


    }

    /**
     * 测试所有允许的mac算法
     */
    public static void testAllMac() {
        BouncyCastleProvider bouncyCastleProvider = new BouncyCastleProvider();
        Set<String> names = bouncyCastleProvider.stringPropertyNames();
        Set<String> hmacAlg = new HashSet<>();
        for (String key : names) {
            if (key.startsWith("Mac.HMAC")) {
                System.out.println("key:" + key + "\tvalue:" + bouncyCastleProvider.getProperty(key));
            }
            //	System.out.println("key:"+key+"\tvalue:"+bouncyCastleProvider.getProperty(key));

        }
    }

    public static void main(String[] args) {

        byte[] data = "123".getBytes();
        byte[] key = getKey(64);
        KeyParameter keyParameter = new KeyParameter(key);
        SM3Digest digest = new SM3Digest();
        HMac mac = new HMac(digest);
        mac.update(data, 0, data.length);
        byte[] result = new byte[mac.getMacSize()];
        mac.doFinal(result, 0);
        String signStr = Base64.encodeBase64String(result);
        System.out.println(signStr);
        //testHmac(HmacAlgorithms.VMPCMAC.getName(), data,key);

        //testHmac();

        //testAllMac();
        //testAllMac();
    }

    public static byte[] getKey(Integer len) {
        char[] keyChars = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        StringBuilder buf = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < len; i++) {
            buf.append(keyChars[random.nextInt(keyChars.length)]);
        }
        return buf.toString().getBytes();
    }
}
