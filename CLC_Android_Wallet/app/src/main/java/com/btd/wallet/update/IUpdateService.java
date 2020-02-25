package com.btd.wallet.update;


import com.btd.library.base.http.model.BaseResultEntity;

import retrofit2.http.GET;
import retrofit2.http.Url;
import rx.Observable;

/**
 * 更新服务
 * Created by yzy on 2018/11/16 16:56
 */

public interface IUpdateService {
    /** 获取更新日志 **/
    @GET(DownloadManager.URL)
    Observable<BaseResultEntity<VersionModel>> getUpdateInfo();

    /** 获取更新日志 **/
    @GET
    Observable<BaseResultEntity<VersionModel>> getDirectUpdateInfo(@Url String url);
}
