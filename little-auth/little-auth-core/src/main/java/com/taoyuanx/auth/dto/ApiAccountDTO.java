package com.taoyuanx.auth.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.taoyuanx.ca.core.exception.CertException;
import com.taoyuanx.ca.core.util.CertUtil;
import lombok.Data;

import java.io.Serializable;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.Objects;

/**
 * @author dushitaoyuan
 * @desc api账户
 * @date 2020/2/17
 */
@Data
public class ApiAccountDTO implements Serializable {

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
    /**
     * 账户状态
     */
    public static final Integer STATUS_VALID = 1, STATUS_INVALID = 0;

    @JsonIgnore
    public boolean isValid() {
        return Objects.equals(STATUS_VALID, status);
    }

    @JsonIgnore
    private PublicKey publicKey;

    public PublicKey readToPublicKey() {
        if (publicKey != null) {
            return publicKey;
        }
        synchronized (this) {
            if (publicKey == null) {
                if (apiPub == null || apiPub.isEmpty()) {
                    return null;
                }
                try {
                    publicKey = CertUtil.readPublicKeyPemString(apiPub);
                } catch (CertException e) {
                    throw new RuntimeException("read pem error", e);
                }
            }
        }
        return publicKey;
    }
}
