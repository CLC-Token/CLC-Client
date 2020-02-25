package com.btd.wallet.mvp.presenter.main;

import android.app.Activity;

import com.btd.library.base.mvp.persenter.BasePresenter;

import com.btd.wallet.core.WorkApp;
import com.btd.wallet.mvp.contract.main.SplashContract;

import com.btd.wallet.mvp.service.UserServiceImpl;



import rx.Subscription;

/**
 * Description: 闪屏页   <br>
 * Author: cxh <br>
 * Date: 2019/3/21 14:14
 */
public class SplashPresenter extends BasePresenter<SplashContract.IView> implements SplashContract.IPresenter {

    private UserServiceImpl mService = new UserServiceImpl();
    private Subscription subscription;

    public SplashPresenter(Activity activity, SplashContract.IView iv) {
        super(activity, iv, false);
    }

    @Override
    public void initView() {
        super.initView();
    }

    @Override
    public void loadSpash() {
        WorkApp.isSplashFirst = false;
       // getView().skipSplash();
    }
}
