package com.btd.wallet.http;

import com.btd.library.base.http.callback.ExtraCodeHttpCallBack;


/**
 * Created by yzy on 2018/11/18 15:50
 */

public class BaseHttpCallback<T> extends ExtraCodeHttpCallBack<T> {
    @Override
    public void onSuccess(T t) {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onHttpFail(int code, String data, String info) {


    }

    @Override
    public void onFinish() {

    }

    @Override
    public void onExtraCodeSuccess(T t) {

    }
}
