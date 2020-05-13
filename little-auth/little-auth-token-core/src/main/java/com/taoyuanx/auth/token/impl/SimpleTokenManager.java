package com.taoyuanx.auth.token.impl;


import com.taoyuanx.auth.exception.TokenException;
import com.taoyuanx.auth.sign.ISign;
import com.taoyuanx.auth.token.Token;
import com.taoyuanx.auth.token.TokenForamtUtil;
import com.taoyuanx.auth.token.TokenManager;
import com.taoyuanx.auth.token.TokenTypeEnum;
import com.taoyuanx.auth.utils.JSONUtil;
import com.taoyuanx.ca.core.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;


/**
 * @author dushitaoyuan
 * @desc token 实现
 * @date 2020/2/17
 */
@Slf4j
public class SimpleTokenManager implements TokenManager {
    private ISign signImpl;

    public SimpleTokenManager(ISign sign) {
        this.signImpl = sign;
    }

    @Override
    public String createToken(Token token) {

        try {
            byte[] data = JSONUtil.toJsonBytes(token);
            byte[] sign = doSign(data);
            return TokenForamtUtil.format(data, sign);
        } catch (Exception e) {
            log.error("生成token异常", e);
            throw new TokenException("生成token异常");
        }
    }


    @Override
    public Token parseToken(String token) {
        if (Util.isEmpty(token)) {
            throw new TokenException("token格式非法");
        }
        String[] split = TokenForamtUtil.splitToken(token);
        if (split.length != 2) {
            throw new TokenException("token格式非法");
        }
        byte[] data = Base64.decodeBase64(split[TokenForamtUtil.DATA_INDEX].getBytes());
        Token tokenObj = JSONUtil.parseObject(data, Token.class);
        tokenObj.setData(data);
        tokenObj.setSign(split[TokenForamtUtil.SING_INDEX]);
        return tokenObj;


    }

    @Override
    public boolean verify(Token token, TokenTypeEnum tokenType) {
        if (tokenType == null || !tokenType.equals(TokenTypeEnum.type(token.getType()))) {
            throw new TokenException("token非法");
        }
        Long end = token.getEndTime();
        if (end < System.currentTimeMillis()) {
            throw new TokenException("token过期");
        }
        if (!doVerifySign(token.getData(), Base64.decodeBase64(token.getSign()))) {
            throw new TokenException("token签名非法");
        }
        return true;
    }

    @Override
    public boolean verify(String token, TokenTypeEnum tokenType) {
        return verify(parseToken(token), tokenType);
    }

    private byte[] doSign(byte[] data) {
        try {
            return this.signImpl.sign(data);
        } catch (Exception e) {
            log.error("生成token异常,签名异常", e);
            throw new TokenException("生成token异常");
        }
    }


    private boolean doVerifySign(byte[] data, byte[] signValue) {
        try {
            return this.signImpl.verifySign(data, signValue);
        } catch (Exception e) {
            log.error("验证token异常,签名异常", e);
            throw new TokenException("验证token异常,签名异常");
        }
    }

}
