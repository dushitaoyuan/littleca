package com.taoyuanx.auth.client.impl.loadbalance;


import com.taoyuanx.auth.client.impl.loadbalance.impl.RandomLoadbalance;
import com.taoyuanx.auth.client.impl.loadbalance.impl.RoundLoadbalance;

public enum LoadbalanceEnum {
    Random(new RandomLoadbalance(), "随机"),
    Round(new RoundLoadbalance(), "轮询");
    private ILoadbalance loadbalance;
    private String desc;

    private LoadbalanceEnum(ILoadbalance loadbalance, String desc) {
        this.loadbalance = loadbalance;
        this.desc = desc;
    }

    public ILoadbalance getLoadbalance() {
        return loadbalance;
    }

    public String getDesc() {
        return desc;
    }
}
