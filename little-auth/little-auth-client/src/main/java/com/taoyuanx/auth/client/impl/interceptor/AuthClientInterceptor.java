package com.taoyuanx.auth.client.impl.interceptor;

import com.taoyuanx.auth.client.core.AuthClientConstant;
import com.taoyuanx.auth.client.core.AuthServer;
import com.taoyuanx.auth.client.exception.ClientAuthException;
import com.taoyuanx.auth.client.core.ClientConfig;
import com.taoyuanx.auth.client.impl.loadbalance.ILoadbalance;
import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.net.ConnectException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * auth client 拦截器,用来实现 负载均衡
 */
@Slf4j
public class AuthClientInterceptor implements Interceptor {

    private ClientConfig clientConfig;

    private ILoadbalance loadbalance;

    public AuthClientInterceptor(ClientConfig clientConfig) {
        this.clientConfig = clientConfig;
        this.loadbalance = clientConfig.getLoadbalance();

    }


    @Override
    public Response intercept(Chain chain) throws IOException {
        Request userRequest = chain.request();
        HttpUrl requestUrl = userRequest.url();
        String stringUrl = requestUrl.toString();
        boolean needBase = Objects.nonNull(stringUrl) && stringUrl.startsWith(AuthClientConstant.FILE_CLIENT_BASE_URL);
        AuthServer choseServer = null;
        if (needBase) {
            /**
             * 替换base url
             */
            if (needBase) {
                Request.Builder requestBuilder = userRequest.newBuilder();
                choseServer = choseServer();
                requestBuilder.url(buildRealUrl(choseServer, requestUrl));
                userRequest = requestBuilder.build();
                return doProceed(chain, userRequest, choseServer, null);

            }
          
        }
        return chain.proceed(userRequest);
    }


    private Map<AuthServer, HttpUrl> serverUrlCache = new ConcurrentHashMap<>();

    /**
     * 递归执行,直到所有存活的authServer中有一个执行成功
     */
    private Response doProceed(Chain chain, Request userRequest, AuthServer choseServer, List<AuthServer> excludeAuthServer) throws IOException {
        try {
            return chain.proceed(userRequest);
        } catch (ConnectException e) {
            log.warn("auth server[{}] connect error", choseServer.getServerUrl());
            /**
             * 连接失败 标记server 不可用
             */
            if (Objects.nonNull(choseServer)) {
                choseServer.alive(false);
            }
            if (Objects.isNull(excludeAuthServer)) {
                excludeAuthServer = new ArrayList<>();
            }
            excludeAuthServer.add(choseServer);
            AuthServer newChoseServer = loopForAlive(excludeAuthServer);
            return doProceed(chain, userRequest.newBuilder().url(buildRealUrl(newChoseServer, userRequest.url())).build(), newChoseServer, excludeAuthServer);
        }
    }

    private HttpUrl buildRealUrl(AuthServer choseServer, HttpUrl oldUrl) {
        HttpUrl choseServerUrl = getAuthServerHttpUrl(choseServer);
        choseServerUrl = oldUrl.newBuilder().scheme(choseServerUrl.scheme()).host(choseServerUrl.host()).port(choseServerUrl.port()).build();
        return choseServerUrl;
    }

    private AuthServer choseServer() {
        AuthServer choseServer = loadbalance.chose(clientConfig.getAuthServer());
        if (choseServer == null) {
            throw new ClientAuthException("no alive authServer");
        }
        if (choseServer.isAlive()) {
            return choseServer;
        } else if (clientConfig.getAuthServer().size() > 1) {
            return loopForAlive(Arrays.asList(choseServer));
        }
        throw new ClientAuthException("no alive authServer");
    }


    private HttpUrl getAuthServerHttpUrl(AuthServer authServer) {
        if (!serverUrlCache.containsKey(authServer)) {
            serverUrlCache.put(authServer, HttpUrl.parse(authServer.getServerUrl()));
        }
        return serverUrlCache.get(authServer);
    }

    private AuthServer loopForAlive(List<AuthServer> excludeAuthServerList) {
        Optional<AuthServer> anyAliveAuthServer = clientConfig.getAuthServer().stream().filter(authServer -> {
            for (AuthServer excludeServer : excludeAuthServerList) {
                if (excludeServer.equals(authServer)) {
                    return false;
                }
            }
            return authServer.isAlive();
        }).findAny();
        if (anyAliveAuthServer.isPresent()) {
            return anyAliveAuthServer.get();
        }
        throw new ClientAuthException("no alive auth Server");

    }


}
