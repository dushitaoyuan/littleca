package com.taoyuanx.auth.client.utils;

import com.taoyuanx.auth.client.core.AuthServer;
import com.taoyuanx.auth.client.core.AuthServiceApiEnum;
import com.taoyuanx.auth.client.core.ClientConfig;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Request;

/**
 * @author dushitaoyuan
 * @date 2020/4/515:22
 */
@Slf4j
public class ServerUtil {
    /**
     * 判断file server是否存活
     */
    public static boolean checkServerAlive(AuthServer fileServer, ClientConfig clientConfig) {

        if (fileServer.isAlive()) {
            return true;
        }
        synchronized (fileServer) {
            if (!fileServer.isAlive()) {
                try {
                    OkHttpUtil.request(clientConfig.getOkHttpClient(), new Request.Builder()
                            .url(fileServer.getServerUrl() + AuthServiceApiEnum.HELLO.path).get().build(), null);
                    fileServer.alive(true);
                    return true;
                } catch (Exception e) {
                    fileServer.alive(false);
                    return false;
                }
            }
        }
        return true;
    }

    public static void heartBeatCheck(ClientConfig clientConfig) {
        if (clientConfig.getAuthServer() == null) {
            return;
        }
        clientConfig.getAuthServer().stream().filter(authServer -> {
            return !authServer.isAlive();
        }).forEach(fileServer -> {
            try {
                OkHttpUtil.request(clientConfig.getOkHttpClient(), new Request.Builder()
                        .url(fileServer.getServerUrl() + AuthServiceApiEnum.HELLO.path).get().build(), null);
                fileServer.alive(true);
            } catch (Exception e) {
                log.warn("fileserver ->{}, heart error", fileServer.getServerUrl());
            }
        });

    }
}
