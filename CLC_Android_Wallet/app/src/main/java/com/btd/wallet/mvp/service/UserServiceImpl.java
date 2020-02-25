package com.btd.wallet.mvp.service;

import com.btd.library.base.http.callback.HttpCallback;
import com.btd.library.base.http.manager.RxHttpManager;

import com.btd.wallet.config.HttpUrl;

import com.btd.wallet.dao.HttpJsonData;
import com.btd.wallet.mvp.model.ActivateReq;
import com.btd.wallet.mvp.model.BaseReq;

import rx.schedulers.Schedulers;

/**
 * 用户Service
 * Created by yzy on 2018/10/22 17:06
 */

public class UserServiceImpl extends BtdBaseService<IUserService> {

    @Override
    protected IUserService getIService(String baseUrl) {
        return getRetrofit(baseUrl).create(IUserService.class);
    }

    protected IUserService getIService(String baseUrl , int connectTime) {
        return RxHttpManager.getInstance().getRetrofit(baseUrl , connectTime).create(IUserService.class);
    }

    /**
     * 激活钱包
     */
    public void activateWallet(ActivateReq req, HttpCallback httpCallback){
        BaseReq baseReq = getBaseReq();
        baseReq.setCmdName(HttpUrl.CmdName.activateWallet);
        baseReq.setUrl(HttpUrl.URL.activateWallet);
        baseReq.setData(req);
        RxHttpManager.getInstance().startHttp(getIService(HttpUrl.URL.WALLET_HOST)
                .activateWallet(HttpJsonData.getReq(baseReq))
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io()) , httpCallback);
    }


}
