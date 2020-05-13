package com.taoyuanx.test;

import com.taoyuanx.auth.sign.impl.hmac.HMacAlgorithms;
import com.taoyuanx.auth.sign.impl.hmac.HMacSign;
import com.taoyuanx.auth.token.Token;
import com.taoyuanx.auth.token.TokenTypeEnum;
import com.taoyuanx.auth.token.impl.SimpleTokenManager;
import org.junit.Test;

/**
 * @author dushitaoyuan
 * @date 2020/3/1520:17
 */
public class TokenTest {
    @Test
    public  void tokenTest() throws Exception {
        String hamcKey="123456";
        HMacSign hMacSign=new HMacSign(HMacAlgorithms.HMAC_SM3,hamcKey.getBytes());
        String data="123455";
        String sign = hMacSign.sign(data);
        System.out.println(sign);
        System.out.println(hMacSign.verifySign(data,sign));

        SimpleTokenManager simpleTokenManager=new SimpleTokenManager(hMacSign);
        Token token=new Token();
        token.setApiAccount("dushitaoyuan");
        token.setApiId(1L);
        Long now=System.currentTimeMillis();
        token.setCreateTime(now);
        token.setEndTime(now+60*60*1000L);
        token.setType(TokenTypeEnum.BUSSINESS.code);
        String tokenStr = simpleTokenManager.createToken(token);
        System.out.println(tokenStr);
        System.out.println(simpleTokenManager.verify(tokenStr,TokenTypeEnum.BUSSINESS));
    }
}
