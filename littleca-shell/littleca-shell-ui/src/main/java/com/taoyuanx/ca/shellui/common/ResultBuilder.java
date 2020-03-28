package com.taoyuanx.ca.shellui.common;

public class ResultBuilder<T> {

    //简单结果生成

    /**
     * 成功
     *
     * @param info
     * @return
     */
    public static <T> Result<T> buildOkResult(T info) {
        Result<T> res = new Result<T>();
        res.setInfo(info);
        res.setStatus(ResultCode.OK.code);
        return res;
    }

    /**
     * 成功
     *
     * @return
     */
    public static Result buildOkResult(String msg) {
        Result res = new Result();
        res.setStatus(ResultCode.OK.code);
        res.setMsg(msg);
        return res;
    }

    public static Result buildOkStringResult(String info) {
        Result res = new Result();
        res.setStatus(ResultCode.OK.code);
        res.setInfo(info);
        return res;
    }

    /**
     * 失败
     *
     * @param code
     * @param msg
     * @return
     */
    public static Result buildFailResult(Integer code, String msg) {
        Result res = new Result();
        res.setCode(code);
        res.setStatus(ResultCode.FAIL.code);
        res.setMsg(msg);
        return res;
    }


}
