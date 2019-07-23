package com.taoyuanx.test;

import com.taoyuanx.ca.tsa.TsaTranserConstant;
import com.taoyuanx.ca.tsa.client.TsaClient;
import com.taoyuanx.ca.tsa.client.impl.LittleTsaClientImpl;
import okhttp3.OkHttpClient;
import org.bouncycastle.tsp.TimeStampResponse;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * @author dushitaoyuan
 * @desc 客户端测试
 * @date 2019/7/10
 */
public class LittleTsaClientTest {
    private TsaClient tsaClient;

    @Before
    public void before() {
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder().connectTimeout(50, TimeUnit.SECONDS).readTimeout(200, TimeUnit.SECONDS).build();
        String tsaUrl = "http://127.0.0.1:8081/tsa";
        tsaClient = new LittleTsaClientImpl(TsaTranserConstant.TRANSFER_ENCODING_BASE64, tsaUrl, okHttpClient);
    }

    @Test
    public void clientTest() {
        TimeStampResponse sha256 = tsaClient.timestamp("SHA-256", "123".getBytes());
        System.out.println(sha256);
    }
}
