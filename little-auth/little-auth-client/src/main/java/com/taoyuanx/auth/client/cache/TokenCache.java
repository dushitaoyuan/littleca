package com.taoyuanx.auth.client.cache;


import com.taoyuanx.auth.dto.response.AuthResultDTO;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * @author dushitaoyuan
 * @date 2020/5/1321:18
 * @desc 缓存接口
 */
public interface TokenCache {
      String TOKEN_CACHE_KEY = "t";

    void put(String cacheKey, AuthResultDTO cacheValue, Long expire, TimeUnit timeUnit, Function<AuthResultDTO,AuthResultDTO> valueReLoad);

    AuthResultDTO get(String cacheKey);




}
