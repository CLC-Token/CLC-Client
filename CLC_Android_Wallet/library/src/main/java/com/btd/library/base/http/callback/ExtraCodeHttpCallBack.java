package com.btd.library.base.http.callback;

/**
 * Created by yzy on 2018/11/9 18:35
 */

public abstract class ExtraCodeHttpCallBack<T> extends HttpCallback<T> {

    /**
     * 存在ExtraCode时,回调该方法
     * @param t : 自定义的返回成功的泛型
     */
    public abstract void onExtraCodeSuccess(T t);


}
