package com.taoyuanx.auth.client.impl.loadbalance;

import com.taoyuanx.auth.client.core.AuthServer;

import java.util.List;

/**
 * @author dushitaoyuan
 * @date 2020/4/512:40
 * 负载接口
 */
public interface ILoadbalance {
    AuthServer chose(List<AuthServer> serverList);
}
