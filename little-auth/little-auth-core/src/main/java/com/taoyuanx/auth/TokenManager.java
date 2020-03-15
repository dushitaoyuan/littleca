package com.taoyuanx.auth;


import com.taoyuanx.auth.token.Token;

/**
 * @author dushitaoyuan
 * @desc token操作
 * @date 2020/2/17
 */
public interface TokenManager {


    String createToken(Token token);

    Token parseToken(String token);

    boolean verify(Token token, TokenTypeEnum tokenType);

    boolean verify(String token, TokenTypeEnum tokenType);
}
