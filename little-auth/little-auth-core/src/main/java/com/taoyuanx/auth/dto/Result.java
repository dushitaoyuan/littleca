package com.taoyuanx.auth.dto;


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
public class Result implements Serializable {
    private Integer code;
    private String msg;
    private Integer success;
    private Object data;
    private Object ext;


    public static Result build() {
        return new Result();
    }

    public Result buildCode(Integer code) {
        this.setCode(code);
        return this;
    }

    public Result buildMsg(String msg) {
        this.setMsg(msg);
        return this;
    }

    public Result buildSuccess(Integer success) {
        this.setSuccess(success);
        return this;
    }

    public Result buildData(Object data) {
        this.data = data;
        return this;
    }

    public Result buildExt(Object ext) {
        this.ext = ext;
        return this;

    }

    public boolean success() {
        return Objects.nonNull(success) && success.equals(1);
    }
}
