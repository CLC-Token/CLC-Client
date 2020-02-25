package com.btd.wallet.mvp.service;

import com.btd.library.base.http.service.BaseService;
import com.btd.wallet.config.HttpUrl;

import com.btd.wallet.mvp.model.BaseReq;

/**
 * Created by yzy on 2018/10/24 16:41
 */

public abstract class BtdBaseService<T> extends BaseService<T> {

    public BtdBaseService(){

    }

    @Override
    protected T getIService() {
        return getIService(HttpUrl.URL.WALLET_HOST);
    }

    protected BaseReq getBaseReq(){
        BaseReq baseReq = new BaseReq<>();
      //  baseReq.setUserId(WorkApp.getUserMe().getUserId());
        baseReq.setReqType(5);
        baseReq.setAppId(HttpUrl.Config.APPID);
        return baseReq;
    }

    /**
     * 透传时需要传入deiviceId
     */
    protected BaseReq getDeviceIdReq(){
        BaseReq req = getBaseReq();
//        req.setDeviceId(WorkApp.workApp.getDeviceResp().getDeviceId());
//        req.setAccessId(WorkApp.workApp.getDeviceResp().getAccessId());
        return req;
    }
}
