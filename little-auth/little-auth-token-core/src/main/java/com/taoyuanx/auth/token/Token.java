package com.taoyuanx.auth.token;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author dushitaoyuan
 * @desc token实体
 * @date 2020/2/17
 */
public class Token {
    /**
     * api账户id
     */
    @JsonProperty("u")
    private Long apiId;
    /**
     * api账户
     */
    @JsonProperty("a")
    private String apiAccount;
    /**
     * 创建时间
     */
    @JsonProperty("c")
    private Long createTime;
    /**
     * 截止时间
     */
    @JsonProperty("e")
    private Long endTime;
    /**
     * 生效时间
     */
    @JsonProperty("v")
    private Long validTime;
    /**
     * token类型
     */
    private Integer type;

    /**
     * token 签名
     */
    @JsonIgnore
    private String sign;
    /**
     * token 数据
     */
    @JsonIgnore
    private byte[] data;


    public Long getValidTime() {
        return validTime;
    }

    public void setValidTime(Long validTime) {
        this.validTime = validTime;
    }

    public String getApiAccount() {
        return apiAccount;
    }

    public void setApiAccount(String apiAccount) {
        this.apiAccount = apiAccount;
    }

    public Long getApiId() {
        return apiId;
    }

    public void setApiId(Long apiId) {
        this.apiId = apiId;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
