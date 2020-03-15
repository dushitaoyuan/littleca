package com.taoyuanx.ca.core.util;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.Signature;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Enumeration;

public final class RSAUtil {
    private static final Logger LOG = LoggerFactory.getLogger(RSAUtil.class);


    public static final String ENCRYPT_TYPE_RSA = "RSA";
    public static final String CERT_TYPE_X509 = "X.509";
    public static final String DEFAILT_ALGORITHM = "SHA256withRSA";



    public static RSAPublicKey getPublicKey(KeyStore keyStore) throws Exception {
        String key_aliases = null;
        Enumeration<String> enumeration = keyStore.aliases();
        key_aliases = enumeration.nextElement();
        if (keyStore.isKeyEntry(key_aliases)) {
            RSAPublicKey publicKey = (RSAPublicKey) keyStore.getCertificate(key_aliases).getPublicKey();
            return publicKey;
        }
        return null;
    }

    public static RSAPrivateKey getPrivateKey(KeyStore keyStore, String keyPassword) throws Exception {
        String key_aliases = null;
        Enumeration<String> enumeration = keyStore.aliases();
        key_aliases = enumeration.nextElement();
        if (keyStore.isKeyEntry(key_aliases)) {
            RSAPrivateKey privateKey = (RSAPrivateKey) keyStore.getKey(key_aliases, keyPassword.toCharArray());
            return privateKey;
        }
        return null;
    }


    public static String encryptByPublicKey(String data, RSAPublicKey publicKey) throws Exception {
        return Base64.encodeBase64String(encryptByPublicKey(data.getBytes(), publicKey));
    }

    public static byte[] encryptByPublicKey(byte[] data, RSAPublicKey publicKey) throws Exception {
        if (null == publicKey) {
            throw new Exception("rsa publicKey is null");
        }
        Cipher cipher = Cipher.getInstance(ENCRYPT_TYPE_RSA);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        int key_len = publicKey.getModulus().bitLength() / 8;
        key_len -= 11;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        for (int i = 0; i < data.length; i += key_len) {
            byte[] doFinal = cipher.doFinal(Util.subarray(data, i, i + key_len));
            outputStream.write(doFinal, 0, doFinal.length);
        }
        return outputStream.toByteArray();

    }

    public static byte[] decryptByPrivateKey(byte[] data, RSAPrivateKey privateKey) throws Exception {
        if (null == privateKey) {
            throw new Exception("rsa privateKey is null");
        }
        Cipher cipher = Cipher.getInstance(ENCRYPT_TYPE_RSA);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        int key_len = privateKey.getModulus().bitLength() / 8;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        for (int i = 0; i < data.length; i += key_len) {
            byte[] doFinal = cipher.doFinal(Util.subarray(data, i, i + key_len));
            outputStream.write(doFinal);
        }
        return outputStream.toByteArray();
    }

    public static String decryptByPrivateKey(String base64Data, RSAPrivateKey privateKey) throws Exception {
        return new String(decryptByPrivateKey(Base64.decodeBase64(base64Data), privateKey));
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
    public static String signData(String data, String algorithm, RSAPrivateKey privateKey) throws Exception {
        return Base64.encodeBase64String(signData(data.getBytes("UTF-8"), algorithm, privateKey));
    }

    public static byte[] signData(byte[] data, String algorithm, RSAPrivateKey privateKey) throws Exception {
        if (null == privateKey) {
            throw new Exception("rsa privateKey is null");
        }
        if (null == algorithm || algorithm.length() == 0) {
            algorithm = DEFAILT_ALGORITHM;
        }
        Signature signture = Signature.getInstance(algorithm);
        signture.initSign(privateKey);
        signture.update(data);
        return signture.sign();
    }


    public static boolean vefySign(String content, String signvalue, String signAlg, RSAPublicKey publicKey) {
        try {
            return vefySign(content.getBytes("UTF-8"), Base64.decodeBase64(signvalue), signAlg, publicKey);
        } catch (Exception e) {
            LOG.error("验签异常{}", e);
            return false;
        }
    }

    /**
     * @param content   签名原文内容
     * @param signvalue 签名值
     * @return
     */
    public static boolean vefySign(byte[] content, byte[] signvalue, String signAlg, RSAPublicKey publicKey) {
        try {
            if (null == signvalue || null == content) {
                return false;
            }
            if (signAlg == null) {
                signAlg = DEFAILT_ALGORITHM;
            }
            Signature signature = Signature.getInstance(signAlg);
            signature.initVerify(publicKey);
            signature.update(content);
            return signature.verify(signvalue);
        } catch (Exception e) {
            LOG.error("验签异常{}", e);
            return false;
        }
    }






}
