package com.taoyuanx.test;

import com.taoyuanx.auth.sign.impl.hmac.HMacAlgorithms;
import com.taoyuanx.auth.sign.impl.hmac.HMacSign;
import com.taoyuanx.auth.token.Token;
import com.taoyuanx.auth.token.TokenTypeEnum;
import com.taoyuanx.auth.token.impl.SimpleTokenManager;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author dushitaoyuan
 * @date 2020/3/1520:17
 */
public class TokenTest {
    SimpleTokenManager simpleTokenManager;

    @Before
    public void before() {
        String hamcKey = "123456";
        HMacSign hMacSign = new HMacSign(HMacAlgorithms.HMAC_SHA_256, hamcKey.getBytes());
        simpleTokenManager = new SimpleTokenManager(hMacSign);
    }

    @Test
    public void tokenTest() throws Exception {

        Token token = new Token();
        token.setApiAccount("dushitaoyuan");
        token.setApiId(1L);
        Long now = System.currentTimeMillis();
        token.setCreateTime(now);

        //token.setValidTime(now+20*60*1000L);
        token.setEndTime(now + 60 * 60 * 1000L);
        token.setType(TokenTypeEnum.BUSSINESS.code);
        String tokenStr = simpleTokenManager.createToken(token);
        System.out.println(tokenStr);
        System.out.println(simpleTokenManager.verify(tokenStr, TokenTypeEnum.BUSSINESS));
    }

    @Test
    public void tokenVerifyTest() throws Exception {
        long start = System.currentTimeMillis();
        String token = "eyJ0eXBlIjoxLCJ1IjoxLCJhIjoiZHVzaGl0YW95dWFuIiwiYyI6MTU5MjM2MzM4NTgxNywiZSI6MTU5MjM2Njk4NTgxN30.7p4VHZA7bx7iUnPCzpHzn8MmghPHfVJiQIL5yepjS3Y\n";
        ThreadPoolExecutor poolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
        for (int i = 0; i < 1000; i++) {
            int finalI = i;
            poolExecutor.submit(() -> {
                try {
                    simpleTokenManager.verify(token, TokenTypeEnum.BUSSINESS);
                } catch (Exception e) {
                    System.out.println(finalI + "验证异常" + e.getMessage());
                }

            });
        }
        poolExecutor.shutdown();
        while (!poolExecutor.awaitTermination(3, TimeUnit.SECONDS)) {
            Thread.sleep(3000);
        }
        long end = System.currentTimeMillis();

        System.out.println("finshed \t" + (end - start));

    }

}
