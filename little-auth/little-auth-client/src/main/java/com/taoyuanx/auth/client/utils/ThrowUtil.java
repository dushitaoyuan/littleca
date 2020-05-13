package com.taoyuanx.auth.client.utils;

import com.taoyuanx.auth.client.exception.ClientAuthException;

/**
 * @author dushitaoyuan
 * @date 2020/5/1323:33
 */
public class ThrowUtil {
    public static ClientAuthException throwException(Exception e) {
        if (e instanceof ClientAuthException) {
            throw (ClientAuthException) e;
        }
        throw new ClientAuthException("认证异常", e);

    }

    public static ClientAuthException throwException(Exception e,String msg) {
        if (e instanceof ClientAuthException) {
            throw (ClientAuthException) e;
        }
        throw new ClientAuthException(msg, e);

    }
}
