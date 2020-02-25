package com.btd.library.base.http.exception;

/**
 * 自定义异常捕捉,用于监控Http 401等请求
 * Created by yzy on 2018/11/18 15:34
 */

public class CreateInterceptorException extends Error{

    private int errorCode;
    private String retry_after;

    public CreateInterceptorException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getRetry_after() {
        return retry_after;
    }

    public void setRetry_after(String retry_after) {
        this.retry_after = retry_after;
    }
}
