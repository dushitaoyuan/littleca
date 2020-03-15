package com.taoyuanx.auth.dto;

/**
 * 结果构造
 */
public class ResultBuilder {

    public static Result success(Object data) {
        return Result.build().buildData(data).buildSuccess(ResultCode.OK.code);
    }
    public static Result successResult(String data) {
        return Result.build().buildData(data).buildSuccess(ResultCode.OK.code);
    }
    public static Result success() {
        return Result.build().buildSuccess(ResultCode.OK.code);
    }

    public static Result success(String msg) {
        return Result.build().buildSuccess(ResultCode.OK.code).buildMsg(msg);
    }


    public static Result failed(String msg) {
        return Result.build().buildSuccess(ResultCode.FAIL.code).buildMsg(msg);
    }


    public static Result failed(Integer code, String msg) {
        return Result.build().buildSuccess(ResultCode.FAIL.code)
                .buildCode(code).buildMsg(msg);
    }
}
