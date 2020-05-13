package com.taoyuanx.auth.client.cache.impl;

import com.taoyuanx.auth.dto.response.AuthResultDTO;
import com.taoyuanx.auth.client.cache.TokenCache;
import lombok.Data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * @author dushitaoyuan
 * @date 2020/5/1321:21
 * 内存缓存 也可扩展为分布式缓存
 */
public class MemoryTokenCache implements TokenCache {

    private Map<String, CacheValue> cacheMap = new ConcurrentHashMap<>();

    public MemoryTokenCache() {

    }

    @Override
    public void put(String cacheKey, AuthResultDTO cacheValue, Long expire, TimeUnit timeUnit, Function<AuthResultDTO, AuthResultDTO> valueReLoad) {
        cacheMap.put(cacheKey, new CacheValue(cacheValue, timeUnit.toMillis(expire), valueReLoad));
    }

    @Override
    public AuthResultDTO get(String cacheKey) {
        CacheValue cacheValue = cacheMap.get(cacheKey);
        if (cacheValue == null) {
            return null;
        }
        if (cacheValue.getEndTime() < System.currentTimeMillis()) {
            cacheValue.reloadValue();
        }
        return cacheValue.getValue();
    }


    @Data
    private static class CacheValue {
        private AuthResultDTO value;
        private Long endTime;
        private Function<AuthResultDTO, AuthResultDTO> valueReLoad;
        private Long expire;

        public CacheValue(AuthResultDTO value, Long expire, Function<AuthResultDTO, AuthResultDTO> valueReLoad) {
            this.value = value;
            this.expire = expire;
            this.endTime = expire + System.currentTimeMillis();
            this.valueReLoad = valueReLoad;
        }

        public void reloadValue() {
            if (endTime < System.currentTimeMillis()) {
                synchronized (value) {
                    if (endTime < System.currentTimeMillis()) {
                        this.value = valueReLoad.apply(value);
                        this.endTime = expire + System.currentTimeMillis();
                    }
                }
            }
        }
    }
}
