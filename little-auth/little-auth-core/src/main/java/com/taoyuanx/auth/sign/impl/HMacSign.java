package com.taoyuanx.auth.sign.impl;

import com.taoyuanx.auth.mac.HMacAlgorithms;
import com.taoyuanx.auth.sign.ISign;
import com.taoyuanx.auth.sign.Mac;
import com.taoyuanx.auth.utils.HmacSignUtil;
import com.taoyuanx.ca.core.util.ByteStringUtil;
import org.bouncycastle.crypto.Digest;

import java.util.Arrays;

/**
 * @author dushitaoyuan
 * @desc hmac 签名
 * @date 2020/2/24
 */
public class HMacSign implements ISign {

    private Mac hMac;

    public HMacSign(HMacAlgorithms hMacAlgorithms, byte[] key) {

        hMac = HmacSignUtil.getHmac(hMacAlgorithms, key);
    }

    public HMacSign(Mac hMac) {
        this.hMac = hMac;
    }

    public HMacSign(Digest digest, byte[] key) {
        this.hMac = HmacSignUtil.getHmac(digest, key);
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
        return hMac.doFinal(data);
    }

    @Override
    public boolean verifySign(byte[] data, byte[] sign) throws Exception {
        return Arrays.equals(sign, hMac.doFinal(data));
    }
}
