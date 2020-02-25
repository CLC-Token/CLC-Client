package com.btd.library.base.http.service;


import com.btd.library.base.http.manager.RxHttpManager;

import retrofit2.Retrofit;

/**
 * Created by yzy on 2018/10/18 10:55
 */

public abstract class BaseService<T> {

    protected Retrofit getRetrofit(String baseUrl){
        return RxHttpManager.getInstance().getRetrofit(baseUrl);
    }

    protected abstract T getIService(String baseUrl);   //自定义域名

    protected abstract T getIService();     //默认的域名
}
