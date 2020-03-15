package com.taoyuanx.ca.tsa.client;

import org.bouncycastle.tsp.TimeStampResponse;

/**
 * @author dushitaoyuan
 * @desc 时间服务客户端 接口
 * @date 2019/7/10
 */
public interface TsaClient {
    /**
     *
     * @param signAlg 签名算法
     * @param messageDigest 数据摘要
     * @return
     */
    TimeStampResponse timestamp(String signAlg, byte[] messageDigest);
}
