package com.btd.library.base.http.callback;


import rx.Observable;

/**
 * Http请求回调
 * Created by yzy on 2018/10/18 15:38
 */
public abstract class HttpCallback<T> {

    /**
     * 请求成功回调
     * @param t : 自定义的返回成功的泛型
     */
    public abstract void onSuccess(T t);

    /**
     * 成功后的Observable返回,扩展链式调用,可不重写
     * @param observable
     */
    public void onSuccess(Observable observable){}

    /**
     * 请求失败返回的异常(网络异常等)
     * @param e : 异常信息
     */
    public abstract void onError(Throwable e);

    /**
     * 服务器返回结果,但是服务器认证失败错误
     * @param code  : 错误码
     * @param data  : 错误数据
     * @param info  : 错误信息提示
     */
    public abstract void onHttpFail(int code , String data , String info);

    /**
     * 请求完成(无论成功或失败)回调
     */
    public abstract void onFinish();
}
