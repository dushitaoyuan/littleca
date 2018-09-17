package com.taoyuanx.ca.sm.util;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;

import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.engines.SM2Engine;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.crypto.params.ParametersWithID;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.crypto.signers.SM2Signer;

import com.taoyuanx.ca.sm.Sm2Result;
import com.taoyuanx.ca.sm.interfaces.Sm2KeyPair;
import com.taoyuanx.ca.sm.interfaces.Sm2PrivateKey;
import com.taoyuanx.ca.sm.interfaces.Sm2PublicKey;

public class Sm2Util  {


    /**
     * ECC公钥加密
     *
     * @param pubKey  ECC公钥
     * @param srcData 源数据
     * @return SM2密文，实际包含三部分：ECC公钥、真正的密文、公钥和原文的SM3-HASH值
     * @throws InvalidCipherTextException
     */
    public static byte[] encrypt(Sm2PublicKey pubKey,byte[] srcData)
        throws InvalidCipherTextException {
        SM2Engine engine = new SM2Engine();
        ParametersWithRandom pwr = new ParametersWithRandom(pubKey.getPublicKeyParameters(), new SecureRandom());
        engine.init(true, pwr);
        return engine.processBlock(srcData, 0, srcData.length);
    }

    /**
     * ECC私钥解密
     *
     * @param priKey        ECC私钥
     * @param sm2CipherText SM2密文，实际包含三部分：ECC公钥、真正的密文、公钥和原文的SM3-HASH值
     * @return 原文
     * @throws InvalidCipherTextException
     */
    public static byte[] decrypt(Sm2PrivateKey priKey, byte[] sm2CipherText)
        throws InvalidCipherTextException {
        SM2Engine engine = new SM2Engine();
        engine.init(false, priKey.getPrivateKeyParameters());
        return engine.processBlock(sm2CipherText, 0, sm2CipherText.length);
    }
    /**
     * ECC私钥签名
     * 不指定withId，则默认withId为字节数组:"1234567812345678".getBytes()
     *
     * @param priKey  ECC私钥
     * @param srcData 源数据
     * @return 签名
     * @throws NoSuchAlgorithmException
     * @throws NoSuchProviderException
     * @throws CryptoException
     */
    public static byte[] sign(Sm2PrivateKey priKey, byte[] srcData)
        throws Exception {
    	 SM2Signer signer = new SM2Signer();
         CipherParameters param = null;
         byte[] withId=priKey.getWithId();
         ParametersWithRandom pwr = new ParametersWithRandom(priKey.getPrivateKeyParameters(), new SecureRandom());
         if (withId != null) {
             param = new ParametersWithID(pwr, withId);
         } else {
             param = pwr;
         }
         signer.init(true, param);
         signer.update(srcData, 0, srcData.length);
         return signer.generateSignature();
    }

   

    /**
     * ECC公钥验签
     * 不指定withId，则默认withId为字节数组:"1234567812345678".getBytes()
     *
     * @param pubKey  ECC公钥
     * @param srcData 源数据
     * @param sign    签名
     * @return 验签成功返回true，失败返回false
     */
    public static boolean verify(Sm2PublicKey pubKey, byte[] srcData, byte[] sign) {
    	 SM2Signer signer = new SM2Signer();
         CipherParameters param = null;
         byte[] withId=pubKey.getWithId();
         if (withId != null) {
             param = new ParametersWithID(pubKey.getPublicKeyParameters(), withId);
         } else {
             param = pubKey.getPublicKeyParameters();
         }
         signer.init(false, param);
         signer.update(srcData, 0, srcData.length);
         return signer.verifySignature(sign);
    }

    
    
    
    
    /**
     * ECC私钥签名
     *
     * @param priKey  ECC私钥
     * @param withId  可以为null，若为null，则默认withId为字节数组:"1234567812345678".getBytes()
     * @param srcData 源数据
     * @return 签名
     * @throws NoSuchAlgorithmException
     * @throws NoSuchProviderException
     * @throws CryptoException
     */
    public static byte[] sign(ECPrivateKeyParameters priKey, byte[] withId, byte[] srcData)
        throws NoSuchAlgorithmException, NoSuchProviderException, CryptoException {
        SM2Signer signer = new SM2Signer();
        CipherParameters param = null;
        ParametersWithRandom pwr = new ParametersWithRandom(priKey, new SecureRandom());
        if (withId != null) {
            param = new ParametersWithID(pwr, withId);
        } else {
            param = pwr;
        }
        signer.init(true, param);
        signer.update(srcData, 0, srcData.length);
        return signer.generateSignature();
    }
    /**
     * ECC公钥验签
     *
     * @param pubKey  ECC公钥
     * @param withId  可以为null，若为null，则默认withId为字节数组:"1234567812345678".getBytes()
     * @param srcData 源数据
     * @param sign    签名
     * @return 验签成功返回true，失败返回false
     */
    public static boolean verify(ECPublicKeyParameters pubKey, byte[] withId, byte[] srcData,
                                 byte[] sign) {
        SM2Signer signer = new SM2Signer();
        CipherParameters param = null;
        if (withId != null) {
            param = new ParametersWithID(pubKey, withId);
        } else {
            param = pubKey;
        }
        signer.init(false, param);
        signer.update(srcData, 0, srcData.length);
        return signer.verifySignature(sign);
    }
    
    
    
    
    /**
     * 分解SM2密文
     *
     * @param cipherText SM2密文
     * @return
     */
    public static Sm2Result parseSm2CipherText(byte[] cipherText) {
        int curveLength = BCECUtil.getCurveLength(Sm2KeyPair.DOMAIN_PARAMS);
        return parseSm2CipherText(curveLength, Sm2KeyPair.SM3_DIGEST_LENGTH, cipherText);
    }

    /**
     * 分解SM2密文
     *
     * @param curveLength  ECC曲线长度
     * @param digestLength HASH长度
     * @param cipherText   SM2密文
     * @return
     */
    public static Sm2Result parseSm2CipherText(int curveLength, int digestLength,
                                                      byte[] cipherText) {
        byte[] c1 = new byte[curveLength * 2 + 1];
        System.arraycopy(cipherText, 0, c1, 0, c1.length);
        byte[] c2 = new byte[cipherText.length - c1.length - digestLength];
        System.arraycopy(cipherText, c1.length, c2, 0, c2.length);
        byte[] c3 = new byte[digestLength];
        System.arraycopy(cipherText, c1.length + c2.length, c3, 0, c2.length);
        Sm2Result result = new Sm2Result();
        result.setC1(c1);
        result.setC2(c2);
        result.setC3(c3);
        result.setCipherText(cipherText);
        return result;
    }
}
