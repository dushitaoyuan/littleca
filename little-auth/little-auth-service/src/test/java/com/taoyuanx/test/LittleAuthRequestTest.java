package com.taoyuanx.test;

import com.taoyuanx.auth.dto.request.EncodeRequestDTO;
import com.taoyuanx.auth.sign.impl.hmac.HMacAlgorithms;
import com.taoyuanx.auth.sign.ISign;
import com.taoyuanx.auth.sign.impl.hmac.HMacSign;
import com.taoyuanx.auth.sign.impl.RsaSign;
import com.taoyuanx.auth.sign.impl.Sm2Sign;
import com.taoyuanx.auth.token.Token;
import com.taoyuanx.auth.token.impl.SimpleTokenManager;
import com.taoyuanx.auth.utils.JSONUtil;
import com.taoyuanx.ca.core.api.impl.RSA;
import com.taoyuanx.ca.core.api.impl.SM2;
import com.taoyuanx.ca.core.util.CertUtil;
import com.taoyuanx.ca.core.util.RSAUtil;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;

import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * @author dushitaoyuan
 * @desc 认证请求构造
 * @date 2020/2/25
 */
public class LittleAuthRequestTest {


    @Test
    public void tokenCancelTest() {
        String m = "eyJ0eXBlIjoxLCJjIjozLCJ1IjoiZHVzaGl0YW95dWFuLWhtYWMiLCJzIjoxNTg4ODM1ODkxNDI2LCJlIjoxNTg4ODQxMjkxNDI2fQ.QH_7HECy5wwr_kt81-WBn_zXVhzbDYA6y-55Xe-v8UA";

        HMacSign hMacSign = new HMacSign(HMacAlgorithms.HMAC_SHA_256, "dushitaoyuan".getBytes());
        SimpleTokenManager simpleTokenManager = new SimpleTokenManager(hMacSign);
        Token token = simpleTokenManager.parseToken(m);
        String tokenS = simpleTokenManager.createToken(token);
        System.out.println(tokenS);
        Token token1 = simpleTokenManager.parseToken(tokenS);
        System.out.println(token.getEndTime());
        System.out.println(DigestUtils.md5Hex(m));
    }

    @Test
    public void hmacAuthRequestBuildTest() throws Exception {
        String apiAccount = "dushitaoyuan-hmac";
        String apiSercet = "dushitaoyuan";
        ISign hMacSign = new HMacSign(HMacAlgorithms.HMAC_SHA_256, apiSercet.getBytes("UTF-8"));
        com.taoyuanx.ca.auth.dto.AuthRequestDTO authRequestDTO = newAuthRequest(hMacSign, apiAccount);
        System.out.println(JSONUtil.toJsonString(authRequestDTO));
    }

    @Test
    public void hmacAuthRefreshRequestBuildTest() throws Exception {
        String apiSercet = "dushitaoyuan";
        ISign hMacSign = new HMacSign(HMacAlgorithms.HMAC_SHA_256, apiSercet.getBytes("UTF-8"));
        String refreshToken = "eyJ0eXBlIjoyLCJjIjozLCJ1IjoiZHVzaGl0YW95dWFuLWhtYWMiLCJzIjoxNTg4OTExMTY4NjY2LCJlIjoxNTg4OTE4MzY4NjY2fQ.dVOsJmxQS7w2Jp6wuV1dSktE_hOsVJu7um2g-N65eKw";
        com.taoyuanx.ca.auth.dto.AuthRefreshRequestDTO authRefreshRequestDTO = newAuthRefreshRequest(hMacSign, refreshToken);
        System.out.println(JSONUtil.toJsonString(authRefreshRequestDTO));
    }

    @Test
    public void rsaAuthRequestBuildTest() throws Exception {
        String apiAccount = "dushitaoyuan-rsa";
        String p12Password = "123456";
        String signAlg = "SHA256WITHRSA";
        KeyStore keyStore = CertUtil.readKeyStore("d:/cert/p12/rsa/client/client.p12", p12Password);
        RSAPrivateKey privateKey = (RSAPrivateKey) CertUtil.getPrivateKey(keyStore, p12Password, null);
        RSAPublicKey publicKey = (RSAPublicKey) CertUtil.getPublicKey(keyStore, null);
        RSAPublicKey serverPublicKey = (RSAPublicKey) CertUtil.readPublicKeyPem("d:/cert/p12/rsa/server/server_pub.pem");
        ISign rsaSign = new RsaSign(publicKey, privateKey, signAlg);
        com.taoyuanx.ca.auth.dto.AuthRequestDTO authRequestDTO = newAuthRequest(rsaSign, apiAccount);
        byte[] data = JSONUtil.toJsonBytes(authRequestDTO);
        EncodeRequestDTO encodeRequestDTO = new EncodeRequestDTO();
        RSA rsa = new RSA();
        encodeRequestDTO.setData(Base64.encodeBase64URLSafeString(rsa.encrypt(data, serverPublicKey)));
        System.out.println(JSONUtil.toJsonString(encodeRequestDTO));
    }

    @Test
    public void rsaAuthRefreshRequestBuildTest() throws Exception {
        String p12Password = "123456";
        String signAlg = "SHA256WITHRSA";
        String refreshToken = "xxxxx";
        KeyStore keyStore = CertUtil.readKeyStore("d:/cert/p12/rsa/client/client.p12", p12Password);
        PrivateKey privateKey = CertUtil.getPrivateKey(keyStore, p12Password, null);
        PublicKey publicKey = CertUtil.getPublicKey(keyStore, null);
        RSAPublicKey serverPublicKey = (RSAPublicKey) CertUtil.readPublicKeyPem("d:/cert/p12/rsa/server/server_pub.pem");
        ISign rsaSign = new RsaSign((RSAPublicKey) publicKey, (RSAPrivateKey) privateKey, signAlg);
        com.taoyuanx.ca.auth.dto.AuthRefreshRequestDTO authRefreshRequestDTO = newAuthRefreshRequest(rsaSign, refreshToken);
        byte[] data = JSONUtil.toJsonBytes(authRefreshRequestDTO);
        EncodeRequestDTO encodeRequestDTO = new EncodeRequestDTO();
        RSA rsa = new RSA();
        encodeRequestDTO.setData(Base64.encodeBase64URLSafeString(rsa.encrypt(data, serverPublicKey)));
        System.out.println(JSONUtil.toJsonString(encodeRequestDTO));
    }

    @Test
    public void sm2AuthRequestBuildTest() throws Exception {
        String apiAccount = "dushitaoyuan-sm2";
        String p12Password = "123456";
        String signAlg = "SM3WITHSM2";
        KeyStore keyStore = CertUtil.readKeyStore("d:/cert/p12/sm2/client/client.p12", p12Password);
        PrivateKey privateKey = CertUtil.getPrivateKey(keyStore, p12Password, null);
        PublicKey publicKey = CertUtil.getPublicKey(keyStore, null);
        PublicKey serverPublicKey = CertUtil.readPublicKeyPem("d:/cert/p12/sm2/server/server_pub.pem");
        ISign sm2Sign = new Sm2Sign(publicKey, privateKey, signAlg);
        SM2 sm2 = new SM2();
        com.taoyuanx.ca.auth.dto.AuthRequestDTO authRequestDTO = newAuthRequest(sm2Sign, apiAccount);
        byte[] data = JSONUtil.toJsonBytes(authRequestDTO);
        EncodeRequestDTO encodeRequestDTO = new EncodeRequestDTO();
        encodeRequestDTO.setData(Base64.encodeBase64URLSafeString(sm2.encrypt(data, serverPublicKey)));
        System.out.println(JSONUtil.toJsonString(encodeRequestDTO));
    }

    @Test
    public void sm2AuthRefreshRequestBuildTest() throws Exception {
        String p12Password = "123456";
        String signAlg = "SHA256WITHRSA";
        String refreshToken = "xxxx";
        KeyStore keyStore = CertUtil.readKeyStore("d:/cert/p12/sm2/client/client.p12", p12Password);
        RSAPrivateKey privateKey = RSAUtil.getPrivateKey(keyStore, p12Password);
        RSAPublicKey publicKey = RSAUtil.getPublicKey(keyStore);
        RSAPublicKey serverPublicKey = (RSAPublicKey) CertUtil.readPublicKeyPem("d:/cert/sm2/rsa/server/server_pub.pem");
        ISign rsaSign = new RsaSign(publicKey, privateKey, signAlg);
        com.taoyuanx.ca.auth.dto.AuthRefreshRequestDTO authRefreshRequestDTO = newAuthRefreshRequest(rsaSign, refreshToken);
        String data = JSONUtil.toJsonString(authRefreshRequestDTO);
        EncodeRequestDTO encodeRequestDTO = new EncodeRequestDTO();
        encodeRequestDTO.setData(RSAUtil.encryptByPublicKey(data, serverPublicKey));
        System.out.println(JSONUtil.toJsonString(encodeRequestDTO));
    }

    public com.taoyuanx.ca.auth.dto.AuthRequestDTO newAuthRequest(ISign signImpl, String apiAccount) throws Exception {
        String random = RandomCodeUtil.getRandCode(16);
        Long timestamp = System.currentTimeMillis();
        com.taoyuanx.ca.auth.dto.AuthRequestDTO authRequestDTO = new com.taoyuanx.ca.auth.dto.AuthRequestDTO();
        authRequestDTO.setApiAccount(apiAccount);
        authRequestDTO.setRandom(random);
        authRequestDTO.setTimestamp(timestamp);
        String signStr = authRequestDTO.toSignStr();
        String sign = signImpl.sign(signStr);
        authRequestDTO.setSign(sign);
        return authRequestDTO;
    }

    public com.taoyuanx.ca.auth.dto.AuthRefreshRequestDTO newAuthRefreshRequest(ISign signImpl, String refreshToken) throws Exception {
        com.taoyuanx.ca.auth.dto.AuthRefreshRequestDTO authRefreshRequestDTO = new com.taoyuanx.ca.auth.dto.AuthRefreshRequestDTO();
        authRefreshRequestDTO.setRefreshToken(refreshToken);
        authRefreshRequestDTO.setTimestamp(System.currentTimeMillis());
        String signStr = authRefreshRequestDTO.toSignStr();
        String sign = signImpl.sign(signStr);
        authRefreshRequestDTO.setSign(sign);
        return authRefreshRequestDTO;
    }
}
