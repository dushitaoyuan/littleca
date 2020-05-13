package com.taoyuanx.auth.client.impl.loadbalance.impl;

import com.taoyuanx.auth.client.core.AuthServer;
import com.taoyuanx.auth.client.impl.loadbalance.ILoadbalance;

import java.util.List;
import java.util.Random;

public class RandomLoadbalance implements ILoadbalance {
    Random random = new Random();

    @Override
    public AuthServer chose(List<AuthServer> serverList) {
        if (serverList == null || serverList.isEmpty()) {
            return null;
        }
        AuthServer choseServer = null;
        if (serverList.size() == 1) {
            choseServer = serverList.get(0);
        } else {
            choseServer = serverList.get(random.nextInt(serverList.size()));
        }
        return choseServer;
    }
}