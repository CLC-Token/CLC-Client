package com.btd.wallet.utils;

import com.btd.library.base.util.LogUtils;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 延时任务工具类(也可进行定时器任务的处理)
 *
 * 首先new DelayUtils
 * 然后直接调用对应的start方法即可
 *
 * Created by yzy on 2018/10/26 15:30
 */
public class DelayUtils {
    private long delayTime = 500;       //延时时间
    private long period = 2000;         //任务间隔
    private Subscription subscription;  //任务的回调,可手动取消
    private DelayListener mListener;    //监听
    private boolean isEach = false; //是否是定时器

    public DelayUtils(DelayListener listener) {
        mListener = listener;
    }

    public DelayUtils(){}

    /**
     * 使用默认参数开启任务
     */
    public void start(){
        start(delayTime , period);
    }

    /**
     * 定义延时时间、定时时间
     * @param delayTime 延时时间
     * @param period    定时时间
     */
    public void start(long delayTime , long period){
        start(delayTime , period , mListener);
    }

    /**
     * 增加回调
     * @param delayTime 延时时间
     * @param period    定时时间
     * @param delayListener 延时时间
     */
    public void start(long delayTime , long period , DelayListener delayListener){
        start(delayTime , period , isEach , delayListener);
    }

    /**
     * 完整的参数
     * @param delayTime 延时时间
     * @param period    定时时间
     * @param isEach    是否触发定时器
     * @param delayListener 回调
     */
    public void start(long delayTime , long period , boolean isEach , DelayListener delayListener){
        stop();
        subscription = Observable.interval(delayTime , period , TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    try {
                        if (!isEach) {
                            stop();
                        }
                        if (delayListener != null) {
                            delayListener.run(aLong);
                        }
                    }catch (Exception ex){
                        LogUtils.i(ex.getMessage());
                    }
                });
    }

    public void stop(){
        if(subscription != null){
            subscription.unsubscribe();
            subscription = null;
            mListener = null;
        }
    }

    public void setDelayAndPeriod(long delayTime , long period){
        this.delayTime = delayTime;
        this.period = period;
    }

    public long getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(long delayTime) {
        this.delayTime = delayTime;
    }

    public long getPeriod() {
        return period;
    }

    public void setPeriod(long period) {
        this.period = period;
    }

    public DelayListener getListener() {
        return mListener;
    }

    public void setListener(DelayListener listener) {
        mListener = listener;
    }

    public boolean isEach() {
        return isEach;
    }

    public void setEach(boolean each) {
        isEach = each;
    }

    public interface DelayListener{
        void run(long along);
    }
}
