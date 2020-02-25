package com.btd.library.base.http.exception;

/**
 * 被订阅者异常
 * Created by yzy on 2018/10/18 15:52
 */

public class ObservableException extends Exception{
    public ObservableException(){
        super();
    }

    public ObservableException(String message){
        super(message);
    }

    public ObservableException(String message , Throwable throwable){
        super(message , throwable);
    }

    public ObservableException(Throwable throwable){
        super(throwable);
    }
}
