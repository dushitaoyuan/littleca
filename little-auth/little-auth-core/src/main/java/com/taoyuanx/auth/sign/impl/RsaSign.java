package com.taoyuanx.auth.sign.impl;

import com.taoyuanx.auth.sign.ISign;
import com.taoyuanx.ca.core.util.ByteStringUtil;
import com.taoyuanx.ca.core.util.RSAUtil;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * @author dushitaoyuan
 * @desc 用途描述
 * @date 2020/2/24
 */
public class RsaSign implements ISign {


    private RSAPublicKey publicKey;
    private RSAPrivateKey privateKey;
    private String signAlg;

    public RsaSign(RSAPublicKey publicKey, String signAlg) {
        //只验
        this.publicKey = publicKey;
        if (signAlg == null) {
            this.signAlg = "SHA256WITHRSA";
        }
    }

    public RsaSign(RSAPublicKey publicKey, RSAPrivateKey privateKey, String signAlg) {
        //签验
        this.privateKey = privateKey;
        this.publicKey = publicKey;
        if (signAlg == null) {
            this.signAlg = "SHA256WITHRSA";
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
        return RSAUtil.signData(data, signAlg, privateKey);
    }

    @Override
    public boolean verifySign(byte[] data, byte[] sign) throws Exception {
        return RSAUtil.vefySign(data, sign, signAlg, publicKey);
    }
}
