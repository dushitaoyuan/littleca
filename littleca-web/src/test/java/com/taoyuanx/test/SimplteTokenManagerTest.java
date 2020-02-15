package com.taoyuanx.test;

import com.taoyuanx.ca.ex.SecurityException;
import com.taoyuanx.ca.util.SimpleTokenManager;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author dushitaoyuan
 * @date 2020/1/1818:32
 */
public class SimplteTokenManagerTest {
    @Test
    public void tokenTest() throws SecurityException {
        SimpleTokenManager tokenManager = new SimpleTokenManager("1Q2yUHaj");
        Map<String, String> map = new HashMap<String, String>(16);
        map.put("123", "123");
        String createToken = tokenManager.createToken(map, 30L, TimeUnit.MINUTES);
        System.out.println(createToken);
        Map<String, String> vafy = tokenManager.vafy(createToken);
        System.out.println(vafy);

    }
}
