package com.taoyuanx.ca.ex;

/**
 * @author dushitaoyuan
 * @desc tsa 异常
 * @date 2019/7/10
 */
public class LittleTsaException extends RuntimeException {
    public LittleTsaException(String message) {
        super(message);
    }

    public LittleTsaException(Throwable cause) {
        super(cause);
    }

    public LittleTsaException(String message, Throwable cause) {
        super(message, cause);
    }
}
