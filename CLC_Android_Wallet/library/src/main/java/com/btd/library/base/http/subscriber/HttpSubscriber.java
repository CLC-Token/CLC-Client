package com.btd.library.base.http.subscriber;


import com.btd.library.base.config.HttpConfig;
import com.btd.library.base.config.LanuageConfig;
import com.btd.library.base.http.callback.ExtraCodeHttpCallBack;
import com.btd.library.base.http.callback.HttpCallback;
import com.btd.library.base.http.exception.CreateInterceptorException;
import com.btd.library.base.http.model.BaseResultEntity;
import com.btd.library.base.util.LogUtils;
import com.google.gson.Gson;

import rx.Subscriber;


/**
 * Http自定义订阅者
 * Created by yzy on 2018/10/18 15:56
 */

public class HttpSubscriber<T> extends Subscriber<BaseResultEntity<T>> {
    private HttpCallback mCallback;

    public HttpSubscriber(HttpCallback httpCallback){
        this.mCallback = httpCallback;
    }

    @Override
    public void onCompleted() {
        LogUtils.i("onCompleted");
    }

    /**
     * 返回异常回调
     * @param throwable
     */
    @Override
    public void onError(Throwable throwable) {
        LogUtils.i(throwable != null ? throwable.getMessage() : "");
        if(throwable instanceof CreateInterceptorException){
            CreateInterceptorException exception = (CreateInterceptorException) throwable;
            String message = exception.getMessage();
            BaseResultEntity entity = new Gson().fromJson(message , BaseResultEntity.class);
            if(canCallback()){
                mCallback.onHttpFail(entity.getCode() , "" , entity.getInfo());
                mCallback.onFinish();
            }
        }else {
            if(canCallback()){
                mCallback.onError(throwable);
                mCallback.onFinish();
            }
        }
    }

    /**
     * 数据返回成功的回调
     * @param tBaseResultEntity
     */
    @Override
    public void onNext(BaseResultEntity<T> tBaseResultEntity) {
        if(canCallback()){
            int code = tBaseResultEntity.getCode();
            if(code == HttpConfig.ServerResponse.SUCCESS_CODE){
                if(tBaseResultEntity.getExtraCode() == -1) {
                    mCallback.onSuccess(tBaseResultEntity.getData());
                }else{
                    if(mCallback instanceof ExtraCodeHttpCallBack) {
                        ((ExtraCodeHttpCallBack) mCallback).onExtraCodeSuccess(tBaseResultEntity.getData());
                    }else{
                        mCallback.onSuccess(tBaseResultEntity.getData());
                    }
                }
            }else{
                String info = tBaseResultEntity.get_info() != null ? (LanuageConfig.isZn?tBaseResultEntity.get_info().getZh_CN():tBaseResultEntity.get_info().getEn()) : tBaseResultEntity.getInfo();
                String backData = "";
                try {
                    backData = tBaseResultEntity.getData() != null ? new Gson().toJson(tBaseResultEntity.getData()) : "";
                }catch (Exception e){
                }
                mCallback.onHttpFail(code , backData , info);
            }
            mCallback.onFinish();
        }
    }

    private boolean canCallback(){
        return mCallback != null;
    }
}
