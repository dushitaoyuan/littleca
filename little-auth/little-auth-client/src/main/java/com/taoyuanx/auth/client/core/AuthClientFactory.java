package com.taoyuanx.auth.client.core;


import com.taoyuanx.auth.client.impl.AuthClient;

/**
 * authclient 工厂
 */
public interface AuthClientFactory {
    AuthClient authClient();

}
