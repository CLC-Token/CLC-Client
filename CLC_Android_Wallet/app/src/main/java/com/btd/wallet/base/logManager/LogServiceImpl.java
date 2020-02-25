package com.btd.wallet.base.logManager;

import com.btd.library.base.http.callback.HttpCallback;
import com.btd.library.base.http.manager.RxHttpManager;
import com.btd.wallet.dao.HttpJsonData;
import com.btd.wallet.pure.BuildConfig;
import com.btd.wallet.config.Constants;

import com.btd.wallet.mvp.model.BaseReq;
import com.btd.wallet.mvp.service.BtdBaseService;

import rx.schedulers.Schedulers;

/**
 * 日志上传接口
 * Created by yzy on 2018/2/27 19:05
 */

public class LogServiceImpl extends BtdBaseService<ILogService> {
    private String HOST = Constants.Protcols.HTTPS + (!BuildConfig.isRelease ? "admintest.cume.cc" : "admin.cume.cc");

    private String LOG_OUT_URL = HOST + "/system/error-log-out";

    private String LOG_CMD_NAME = "uploadLog";

    /**
     * 上传日志
     */
    public void uploadLog(LogRequest req, HttpCallback httpCallback) {
        BaseReq baseReq = getBaseReq();
        baseReq.setCmdName(LOG_CMD_NAME);
        baseReq.setUrl(LOG_OUT_URL);
        baseReq.setData(req);
        RxHttpManager.getInstance().startHttp(getIService(HOST)
                .uploadLog(LOG_OUT_URL, HttpJsonData.getReq(baseReq))
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io()), httpCallback);
    }

    @Override
    protected ILogService getIService(String baseUrl) {
        return getRetrofit(baseUrl).create(ILogService.class);
    }
}
