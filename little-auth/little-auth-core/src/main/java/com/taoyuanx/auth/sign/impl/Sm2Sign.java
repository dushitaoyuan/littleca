package com.taoyuanx.auth.sign.impl;

import com.taoyuanx.auth.sign.ISign;
import com.taoyuanx.ca.core.api.impl.SM2;
import com.taoyuanx.ca.core.sm.interfaces.Sm2PrivateKey;
import com.taoyuanx.ca.core.util.ByteStringUtil;
import com.taoyuanx.ca.core.util.RSAUtil;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * @author dushitaoyuan
 * @desc 国密 sm2 实现
 * @date 2020/2/24
 */
public class Sm2Sign implements ISign {


    private PublicKey publicKey;
    private PrivateKey privateKey;
    private String signAlg;
    private SM2 sm2 = new SM2();

    public Sm2Sign(PublicKey publicKey, String signAlg) {
        this(publicKey, null, signAlg);
    }

    public Sm2Sign(PublicKey publicKey, PrivateKey privateKey, String signAlg) {
        //签验
        this.privateKey = privateKey;
        this.publicKey = publicKey;
        if (signAlg == null) {
            this.signAlg = "SM3WITHSM2";
        } else {
            this.signAlg = signAlg;
        }

    }


    @Override
    public String sign(String data) throws Exception {
        return ByteStringUtil.toString(sign(data.getBytes("UTF-8")), ByteStringUtil.BASE64_URLSAFE);
    }

    @Override
    public boolean verifySign(String data, String sign) throws Exception {
        return verifySign(data.getBytes("UTF-8"), ByteStringUtil.toBytes(sign, ByteStringUtil.BASE64_URLSAFE));

    }

    @Override
    public byte[] sign(byte[] data) throws Exception {
        return sm2.sign(data, privateKey, signAlg);
    }

    @Override
    public boolean verifySign(byte[] data, byte[] sign) throws Exception {
        return sm2.verifySign(sign, data, publicKey, signAlg);

    }
}
