package com.btd.library.base.mvp.persenter;

import android.app.Activity;
import android.os.Bundle;

import com.btd.library.base.mvp.ipersenter.IBasePresenter;
import com.btd.library.base.mvp.ipersenter.IBaseView;

import org.greenrobot.eventbus.EventBus;

/**
 * Description:    <br>
 * Author: cxh <br>
 * Date: 2018/10/18 14:46
 */
public class BasePresenter<V extends IBaseView> implements IBasePresenter {

    protected V mView;
    protected Activity mActivity;
    protected boolean mUseEventBus = false;
    protected Bundle mBundle;

    public BasePresenter(Activity activity, V iv,boolean useEventBus) {
        this(activity,iv);
        mUseEventBus = useEventBus;
    }

    public BasePresenter(Activity activity, V iv) {
        mActivity = activity;
        mView = iv;
    }

    @Override
    public void onCreate(Bundle bundle) {
        this.mBundle = bundle;
        if(mUseEventBus) EventBus.getDefault().register(this);
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onSupportVisible() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onDestroy() {
        mView = null;
        mActivity = null;
        if(mUseEventBus) EventBus.getDefault().unregister(this);
    }

    @Override
    public void finish() {
        mActivity.finish();
    }

    @Override
    public void log(String s) {

    }

    protected V getView(){
        return mView;
    }

    protected boolean canUseView(){
        return getView() != null;
    }

    protected boolean canUseActivity(){
        return mActivity != null && !mActivity.isDestroyed() && !mActivity.isFinishing();
    }

    protected boolean canUsePresenter(){
        return canUseActivity() && canUseView();
    }
}
