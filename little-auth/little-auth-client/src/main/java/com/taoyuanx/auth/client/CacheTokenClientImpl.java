package com.taoyuanx.auth.client;


import com.taoyuanx.auth.api.AuthApi;
import com.taoyuanx.auth.dto.request.AuthRefreshRequestDTO;
import com.taoyuanx.auth.dto.request.AuthRequestDTO;
import com.taoyuanx.auth.dto.response.AuthResultDTO;
import com.taoyuanx.auth.client.cache.TokenCache;
import com.taoyuanx.auth.client.core.ClientConfig;
import com.taoyuanx.auth.client.utils.RandomCodeUtil;

import java.util.concurrent.TimeUnit;

/**
 * @author dushitaoyuan
 * @date 2020/5/1321:07
 * token 缓存实现
 */
public class CacheTokenClientImpl implements TokenClient {
    private AuthApi authApi;
    private TokenCache tokenCache;
    private ClientConfig clientConfig;
    private static Object lock = new Object();

    public CacheTokenClientImpl(AuthApi authApi, ClientConfig clientConfig, TokenCache tokenCache) {
        this.authApi = authApi;
        this.tokenCache = tokenCache;
        this.clientConfig = clientConfig;
    }

    @Override
    public AuthResultDTO auth() {
        AuthResultDTO authResultDTO = tokenCache.get(TokenCache.TOKEN_CACHE_KEY);
        if (null != authResultDTO) {
            return authResultDTO;
        }
        synchronized (lock) {
            authResultDTO = tokenCache.get(TokenCache.TOKEN_CACHE_KEY);
            if (null != authResultDTO) {
                return authResultDTO;
            }
        }
        return authForce();

    }

    @Override
    public AuthResultDTO authForce() {
        synchronized (lock) {
            AuthRequestDTO authRequestDTO = new AuthRequestDTO();
            authRequestDTO.setApiAccount(clientConfig.getApiAccount());
            authRequestDTO.setRandom(RandomCodeUtil.getRandCode(16));
            authRequestDTO.setTimestamp(System.currentTimeMillis());
            AuthResultDTO authResultDTO = authApi.auth(authRequestDTO);
            tokenCache.put(TokenCache.TOKEN_CACHE_KEY, authResultDTO, authResultDTO.getExpire() - clientConfig.getTokenCacheSubTime(), TimeUnit.MILLISECONDS, (expireToken) -> {
                AuthRefreshRequestDTO authRefreshRequestDTO = new AuthRefreshRequestDTO();
                authRefreshRequestDTO.setRefreshToken(expireToken.getRefreshToken());
                authRefreshRequestDTO.setTimestamp(System.currentTimeMillis());
                return authApi.authRefresh(authRefreshRequestDTO);
            });
            return authResultDTO;
        }

    }


}
