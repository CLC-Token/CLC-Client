package com.btd.wallet.base.logManager;


import com.btd.library.base.http.model.BaseResultEntity;
import com.btd.wallet.mvp.model.BaseReq;
import com.btd.wallet.mvp.model.NullResp;


import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Description:    <br>
 * Author: cxh <br>
 * Date: 2019/6/27 13:54
 */
public interface ILogService {
    @POST
    Observable<BaseResultEntity<NullResp>> uploadLog(@Url String url, @Body BaseReq req);
}
