package com.taoyuanx.test;

import com.taoyuanx.auth.client.cache.TokenCache;
import com.taoyuanx.auth.dto.request.AuthRefreshRequestDTO;
import com.taoyuanx.auth.dto.request.AuthRequestDTO;
import com.taoyuanx.auth.dto.response.AuthResultDTO;
import com.taoyuanx.auth.client.CacheTokenClientImpl;
import com.taoyuanx.auth.client.TokenClient;
import com.taoyuanx.auth.client.cache.impl.MemoryTokenCache;
import com.taoyuanx.auth.client.core.ClientConfig;
import com.taoyuanx.auth.client.impl.AuthClient;
import com.taoyuanx.auth.client.impl.DefaultSingletonAuthClientFactory;
import com.taoyuanx.auth.client.utils.RandomCodeUtil;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author dushitaoyuan
 * @date 2020/5/1123:59
 */
public class AuthClientTest {
    @Test
    public void authTest() {
        ClientConfig clientConfig = new ClientConfig();
        AuthClient authClient = new DefaultSingletonAuthClientFactory(clientConfig).authClient();
        AuthRequestDTO authRequestDTO = new AuthRequestDTO();
        authRequestDTO.setApiAccount(clientConfig.getApiAccount());
        authRequestDTO.setRandom(RandomCodeUtil.getRandCode(16));
        authRequestDTO.setTimestamp(System.currentTimeMillis());
        AuthResultDTO authResult = authClient.auth(authRequestDTO);
        System.out.println(authResult);
        AuthRefreshRequestDTO authRefreshRequestDTO = new AuthRefreshRequestDTO();
        authRefreshRequestDTO.setRefreshToken(authResult.getRefreshToken());
        authRefreshRequestDTO.setTimestamp(System.currentTimeMillis());
        AuthResultDTO authRefresh = authClient.authRefresh(authRefreshRequestDTO);
        System.out.println(authRefresh);
    }

    @Test
    public void authCaheTest() throws Exception {
        ClientConfig clientConfig = new ClientConfig();
        AuthClient authClient = new DefaultSingletonAuthClientFactory(clientConfig).authClient();
        TokenClient tokenClient = new CacheTokenClientImpl(authClient, clientConfig, new MemoryTokenCache());
        ExecutorService executorService = Executors.newFixedThreadPool(15);
        for (int i = 0; i < 10000; i++) {
            executorService.submit(() -> {
                System.out.println(tokenClient.auth());
            });
        }
        executorService.shutdown();
        while (!executorService.awaitTermination(3, TimeUnit.SECONDS)){
        }
        System.out.println("关闭成功");
    }
}
