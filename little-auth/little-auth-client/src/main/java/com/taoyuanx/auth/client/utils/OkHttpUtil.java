package com.taoyuanx.auth.client.utils;


import com.taoyuanx.auth.client.Result;
import com.taoyuanx.auth.client.exception.ClientAuthException;

import com.taoyuanx.auth.utils.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.util.Objects;

@Slf4j
public class OkHttpUtil {
    public static <T> T request(OkHttpClient client, Request request, Class<T> type) throws ClientAuthException {
        Response response = null, temp;
        try {
            temp = response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                if (type == null) {
                    return null;
                }
                if (response.getClass().equals(type)) {
                    response = null;
                    return (T) temp;
                } else {
                    Result result = JSONUtil.parseObject(response.body().string(), Result.class);
                    if (type.equals(String.class)) {
                        return (T) result.getData();
                    } else if (type.equals(Result.class)) {
                        return (T) result;
                    }
                    return JSONUtil.parseObject(result.getData(), type);
                }
            }
            String errorData = response.body().string();
            Result result = JSONUtil.parseObject(errorData, Result.class);
            throw new ClientAuthException(result.getCode(), StrUtil.log4jFormat(result.getMsg() + ",异常结果:{}", errorData));
        } catch (Exception e) {
            throw ThrowUtil.throwException(e);
        } finally {
            if (Objects.nonNull(response)) {
                response.close();
            }
        }

    }


}
