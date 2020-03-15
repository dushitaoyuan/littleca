package com.taoyuanx.ca.auth.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author dushitaoyuan
 * @desc api账户实体
 * @date 2020/2/17
 */
@Data
@TableName("api_account")
public class ApiAccountEntity implements Serializable {
    @TableId(type = IdType.ID_WORKER)
    private Long id;
    /**
     * api账户名
     */
    private String apiAccount;
    /**
     * api账户密钥
     */
    private String apiSecret;

    /**
     * api账户公钥
     */
    private String apiPub;
    /**
     * 状态 0禁用,1可用
     */
    private Integer status;

    /**
     * client白名单
     */
    private String whiteIp;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 公钥激活时间
     */
    private Date activeTime;
    /**
     * 公钥过期时间
     */
    private Date endTime;

}
