package com.taoyuanx.auth.exception;



/**
 * token失效异常
 */
public class TokenCancelException extends RuntimeException {
    private static final long serialVersionUID = 8793672380339632040L;
    private Integer errorCode;

    public TokenCancelException(String msg) {
        super(msg);
    }

    public TokenCancelException(Integer errorCode, String msg) {
        super(msg);
        if (errorCode != null) {
            this.errorCode = errorCode;
        }

    }
    public Integer getErrorCode() {
        return errorCode;
    }

    public TokenCancelException(String message, Throwable cause) {
        super(message, cause);
    }
}
