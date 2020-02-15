package com.taoyuanx.ca.util;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import java.io.InputStream;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;


public final class RSAUtil {
    private static Logger LOG = LoggerFactory.getLogger(RSAUtil.class);
    public static final String ENCRYPT_TYPE_RSA = "RSA";
    public static final String DEFAILT_ALGORITHM = "MD5withRSA";
    public static final String CERT_TYPE_X509 = "X.509";

    /**
     * 公钥加密
     *
     * @param data
     * @param publicKey
     * @return
     * @throws Exception
     */
    public static String encryptByPublicKey(String data, RSAPublicKey publicKey) throws Exception {
        if (null == publicKey) {
            throw new Exception("rsa publicKey is null");
        }
        Cipher cipher = Cipher.getInstance(ENCRYPT_TYPE_RSA);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        // 模长
        int key_len = publicKey.getModulus().bitLength() / 8;
        // 加密数据长度 <= 模长-11,如果明文长度大于模长-11则要分组加密
        key_len -= 11;
        byte[] datas = data.getBytes();
        byte[] dataReturn = null;
        for (int i = 0; i < datas.length; i += key_len) {
            byte[] doFinal = cipher.doFinal(Util.subarray(datas, i, i + key_len));
            dataReturn = Util.addAll(dataReturn, doFinal);
        }
        return Base64.encodeBase64String(dataReturn);

    }

    /**
     * 私钥解密
     *
     * @param data
     * @param privateKey
     * @return
     * @throws Exception
     */
    public static String decryptByPrivateKey(String data, RSAPrivateKey privateKey) throws Exception {
        if (null == privateKey) {
            // 如果私钥为空采用系统私钥
            throw new Exception("rsa privateKey is null");
        }
        Cipher cipher = Cipher.getInstance(ENCRYPT_TYPE_RSA);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        // 模长
        int key_len = privateKey.getModulus().bitLength() / 8;
        // base64解密
        byte[] bytes = Base64.decodeBase64(data);
        // 分组解密
        StringBuffer sb = new StringBuffer();
        // 如果密文长度大于模长则要分组解密
        for (int i = 0; i < bytes.length; i += key_len) {
            byte[] doFinal = cipher.doFinal(Util.subarray(bytes, i, i + key_len));
            sb.append(new String(doFinal));
        }
        return sb.toString();
    }

    public static X509Certificate getPublicKeyCer(InputStream publicInput) throws Exception {
        CertificateFactory certificatefactory = CertificateFactory.getInstance(CERT_TYPE_X509);
        X509Certificate cert = (X509Certificate) certificatefactory.generateCertificate(publicInput);
        return cert;
    }

    /**
     * 签名
     *
     * @param data       签名内容
     * @param algorithm  签名
     * @param privateKey 签名私钥
     * @return
     * @throws Exception
     */
    public static String signData(byte[] data, String algorithm, RSAPrivateKey privateKey) throws Exception {
        if (null == privateKey) {
            // 如果私钥为空采用系统私钥
            throw new Exception("rsa privateKey is null");
        }
        if (null == algorithm || algorithm.length() == 0) {
            algorithm = DEFAILT_ALGORITHM;
        }
        Signature signture = Signature.getInstance(algorithm);
        signture.initSign(privateKey);
        signture.update(data);
        return Base64.encodeBase64String(signture.sign());
    }


    /**
     * @param content   签名内容
     * @param signvalue 签名值
     * @param publicKey 公钥
     * @param signAlg   签名算法
     * @return
     */
    public static boolean vefySign(String content, String signvalue, PublicKey publicKey, String signAlg) {
        try {
            if (null == signvalue || null == content) {
                return false;
            }
            if (null == publicKey) {
                throw new Exception("rsa publicKey  is null");
            }
            Signature signature = null;
            if (null == signAlg || "".equals(signAlg)) {
                signature = Signature.getInstance(DEFAILT_ALGORITHM);
            } else {
                signature = Signature.getInstance(signAlg);
            }
            signature.initVerify(publicKey);
            signature.update(content.getBytes());
            boolean bverify = signature.verify(Base64.decodeBase64(signvalue));
            return bverify;
        } catch (Exception e) {
            LOG.error("签名验证异常->{}", e);
            return false;
        }
    }


}
