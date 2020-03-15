package com.taoyuanx.test;

import com.taoyuanx.auth.mac.HMacAlgorithms;
import com.taoyuanx.auth.sign.ISign;
import com.taoyuanx.auth.sign.impl.HMacSign;
import com.taoyuanx.auth.sign.impl.RsaSign;
import com.taoyuanx.auth.utils.JSONUtil;
import com.taoyuanx.ca.auth.dto.AuthRefreshRequestDTO;
import com.taoyuanx.ca.auth.dto.AuthRequestDTO;
import com.taoyuanx.ca.auth.dto.EncodeRequestDTO;
import com.taoyuanx.ca.core.util.CertUtil;
import com.taoyuanx.ca.core.util.RSAUtil;
import org.junit.Test;

import java.io.File;
import java.security.KeyStore;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * @author dushitaoyuan
 * @desc 认证请求构造
 * @date 2020/2/25
 */
public class LittleAuthRequestTest {
    String apiAccount = "dushitaoyuan";
    String apiSercet = "dushitaoyuan";

    @Test
    public void hmacAuthRequestBuildTest() throws Exception {

        ISign hMacSign = new HMacSign(HMacAlgorithms.HMAC_SHA_256, apiSercet.getBytes("UTF-8"));
        AuthRequestDTO authRequestDTO = newAuthRequest(hMacSign);
        System.out.println(JSONUtil.toJsonString(authRequestDTO));
    }

    @Test
    public void hmacAuthRefreshRequestBuildTest() throws Exception {
        ISign hMacSign = new HMacSign(HMacAlgorithms.HMAC_SHA_256, apiSercet.getBytes("UTF-8"));
        String refreshToken = "eyJhcGlJZCI6MSwiYXBpQWNjb3VudCI6ImR1c2hpdGFveXVhbiIsImNyZWF0ZVRpbWUiOjE1ODI2OTA4MTQ2NTYsImVuZFRpbWUiOjE1ODI2OTgwMTQ2NTYsInR5cGUiOjJ9.MYfi9e7ygm2ATy3GcRTdo7CUwM3Sg0NDNemWDGBjqjM";
        AuthRefreshRequestDTO authRefreshRequestDTO = newAuthRefreshRequest(hMacSign, refreshToken);
        System.out.println(JSONUtil.toJsonString(authRefreshRequestDTO));
    }

    @Test
    public void rsaAuthRequestBuildTest() throws Exception {
        String p12Password = "123456";
        String signAlg = "SHA256WITHRSA";
        KeyStore keyStore = CertUtil.readKeyStore("e://cert/client.p12", p12Password);
        RSAPrivateKey privateKey = (RSAPrivateKey) CertUtil.getPrivateKey(keyStore, p12Password, null);
        RSAPublicKey publicKey = (RSAPublicKey) CertUtil.getPublicKey(keyStore, null);
        RSAPublicKey serverPublicKey = (RSAPublicKey) CertUtil.readPublicKeyCer(new File("e://cert/server_pub.cer"));
        ISign rsaSign = new RsaSign(publicKey, privateKey, signAlg);
        AuthRequestDTO authRequestDTO = newAuthRequest(rsaSign);
        String data = JSONUtil.toJsonString(authRequestDTO);
        EncodeRequestDTO encodeRequestDTO = new EncodeRequestDTO();
        encodeRequestDTO.setData(RSAUtil.encryptByPublicKey(data, serverPublicKey));
        System.out.println(JSONUtil.toJsonString(encodeRequestDTO));
    }

    @Test
    public void rsaAuthRefreshRequestBuildTest() throws Exception {
        String p12Password = "123456";
        String signAlg = "SHA256WITHRSA";
        String refreshToken = "eyJhcGlJZCI6MSwiY3JlYXRlVGltZSI6MTU4MjY4OTI0ODQ3MCwiZW5kVGltZSI6MTU4MjY5NjQ0ODQ3MCwidHlwZSI6Mn0.EIYPcReD70RTMOnroQvRWEGc8Vo1SnL8nSdi---Hjkk";
        KeyStore keyStore = CertUtil.readKeyStore("e://cert/client.p12", p12Password);
        RSAPrivateKey privateKey = RSAUtil.getPrivateKey(keyStore, p12Password);
        RSAPublicKey publicKey = RSAUtil.getPublicKey(keyStore);
        RSAPublicKey serverPublicKey = (RSAPublicKey) CertUtil.readPublicKeyCer(new File("e://cert/server_pub.cer"));
        ISign rsaSign = new RsaSign(publicKey, privateKey, signAlg);
        AuthRefreshRequestDTO authRefreshRequestDTO = newAuthRefreshRequest(rsaSign, refreshToken);
        String data = JSONUtil.toJsonString(authRefreshRequestDTO);
        EncodeRequestDTO encodeRequestDTO = new EncodeRequestDTO();
        encodeRequestDTO.setData(RSAUtil.encryptByPublicKey(data, serverPublicKey));
        System.out.println(JSONUtil.toJsonString(encodeRequestDTO));
    }

    public AuthRequestDTO newAuthRequest(ISign signImpl) throws Exception {
        String random = RandomCodeUtil.getRandCode(16);
        Long timestamp = System.currentTimeMillis();
        AuthRequestDTO authRequestDTO = new AuthRequestDTO();
        authRequestDTO.setApiAccount(apiAccount);
        authRequestDTO.setRandom(random);
        authRequestDTO.setTimestamp(timestamp);
        String signStr = random + apiAccount + timestamp;
        String sign = signImpl.sign(signStr);
        authRequestDTO.setSign(sign);
        return authRequestDTO;
    }

    public AuthRefreshRequestDTO newAuthRefreshRequest(ISign signImpl, String refreshToken) throws Exception {
        AuthRefreshRequestDTO authRefreshRequestDTO = new AuthRefreshRequestDTO();
        authRefreshRequestDTO.setRefreshToken(refreshToken);
        String signStr = refreshToken;
        String sign = signImpl.sign(signStr);
        authRefreshRequestDTO.setSign(sign);
        return authRefreshRequestDTO;
    }
}
