package com.taoyuanx.auth.client;

import com.taoyuanx.auth.dto.response.AuthResultDTO;

/**
 * @author dushitaoyuan
 * @date 2020/5/1321:07
 */
public interface TokenClient {

    AuthResultDTO auth();

    /**
     *
     * (当token被服务端注销时)强制刷新token
     */
    AuthResultDTO authForce();
}
