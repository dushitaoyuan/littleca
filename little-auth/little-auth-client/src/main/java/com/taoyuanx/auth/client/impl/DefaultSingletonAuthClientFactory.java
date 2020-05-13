package com.taoyuanx.auth.client.impl;


import com.taoyuanx.auth.client.core.*;
import com.taoyuanx.auth.client.exception.ClientAuthException;
import com.taoyuanx.auth.client.impl.interceptor.AuthClientInterceptor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ConnectionPool;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

import javax.net.ssl.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 *
 */
@Slf4j
public class DefaultSingletonAuthClientFactory implements AuthClientFactory {
    private ClientConfig config;


    public DefaultSingletonAuthClientFactory(ClientConfig config) {
        this.config = config;
    }

    public DefaultSingletonAuthClientFactory() {
        this.config = new ClientConfig(ClientConfig.DEFAULT_CONFIG);
    }

    private static AuthClient client;

    @Override
    public AuthClient authClient() {
        if (null == client) {
            synchronized (DefaultSingletonAuthClientFactory.class) {
                if (null == client) {
                    init();
                }
            }
        }
        return client;
    }


    /**
     * 初始化
     */
    private void init() {
        try {
            //1 初始化http client
            SSLParams sslParams = new SSLParams();
            SSLContext sslContext = SSLContext.getInstance("TLS");
            X509TrustManager trustManager = new UnSafeTrustManager();
            sslContext.init(null, new TrustManager[]{trustManager}, null);
            sslParams.sSLSocketFactory = sslContext.getSocketFactory();
            sslParams.trustManager = trustManager;
            Interceptor clientInterceptor = new AuthClientInterceptor(config);
            OkHttpClient okHttpClient = new OkHttpClient().newBuilder().hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            }).sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                    .connectTimeout(config.getConnectTimeout(), TimeUnit.SECONDS)
                    .connectionPool(new ConnectionPool(config.getMaxIdleConnections(), config.getKeepAliveDuration(), TimeUnit.SECONDS))
                    .retryOnConnectionFailure(false)
                    .addInterceptor(clientInterceptor)
                    .build();


            List<AuthServiceApiEnum> fdfsApiList = Arrays.asList(AuthServiceApiEnum.values());
            Map<AuthServiceApiEnum, String> apiMap = new HashMap(fdfsApiList.size());
            fdfsApiList.stream().forEach(api -> {
                apiMap.put(api, AuthClientConstant.FILE_CLIENT_BASE_URL + api.path);
            });
            config.setApiMap(apiMap);
            config.setOkHttpClient(okHttpClient);
            client = new AuthClient(config);
        } catch (Exception e) {
            log.error("初始化失败", e);
            throw new ClientAuthException("配置初始化失败", e);
        }
    }


    public static class SSLParams {
        public SSLSocketFactory sSLSocketFactory;
        public X509TrustManager trustManager;
    }

    private static class UnSafeTrustManager implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[]{};
        }
    }

}
