package com.btd.wallet.mvp.service;


import com.btd.library.base.http.model.BaseResultEntity;
import com.btd.wallet.config.HttpUrl;
import com.btd.wallet.mvp.model.BaseReq;
import com.btd.wallet.mvp.model.NullResp;


import retrofit2.http.Body;

import retrofit2.http.POST;
import rx.Observable;

/**
 * 有关用户的请求接口
 * Created by yzy on 2018/10/XXXXXXXXX22 16:59
 */

public interface IUserService {

    @POST(HttpUrl.URL.activateWallet)
    Observable<BaseResultEntity<NullResp>> activateWallet(@Body BaseReq req);


}
