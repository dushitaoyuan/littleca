package com.taoyuanx.auth.client;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.taoyuanx.auth.client.utils.ResultFiledStringDeSerializer;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

/**
 * 统一返回结果
 * <p>
 * code 错误码
 * msg 错误消息
 * success 请求成功标识 1成功,0失败
 * data 结果体
 * ext 扩展信息
 */
@Data
@JsonDeserialize(using = ResultFiledStringDeSerializer.class)
public class Result implements Serializable {
    private Integer code;
    private String msg;
    private Integer success;
    private String data;
    private String ext;

    public boolean success() {
        return Objects.nonNull(success) && success.equals(1);
    }
}
