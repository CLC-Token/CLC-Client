package com.btd.wallet;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.btd.library.base.util.LogUtils;
import com.btd.wallet.base.activity.BaseSupportActivity;
import com.btd.wallet.config.Constants;
import com.btd.wallet.config.SPKeys;
import com.btd.wallet.core.WorkApp;
import com.btd.wallet.mvp.view.dialog.ConfirmDialog;
import com.btd.wallet.mvp.view.dialog.listener.DialogListener;
import com.btd.wallet.mvp.view.main.SplashFragment;
import com.btd.wallet.pure.R;
import com.btd.wallet.utils.MethodUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;

/**
 * 引导页
 */
public class SplashAcitvity extends BaseSupportActivity {



    @Override
    protected int getContentView() {
        return R.layout.activity_splash;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                , WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        if (WorkApp.getShare().getBoolean(SPKeys.IS_FIRST_OPEN)) {
            WorkApp.setUuid();
        }
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initView() {
        RxPermissions rxPermission = new RxPermissions(mActivity);
        rxPermission.requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(permission -> {
                    if (permission.granted) {
                        if (permission.name.equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            toGo();
                        }
                    } else {

                            // 用户拒绝了该权限，而且选中『不再询问』
                            LogUtils.e(permission.name + " is denied.");
                            new ConfirmDialog(mActivity, MethodUtils.getString(R.string.please_sure_read_write_extra_permisssion),
                                    new DialogListener() {
                                        @Override
                                        public void cancel() {
                                            MethodUtils.exitAPP();
                                        }

                                        @Override
                                        public void confirm() {
                                            MethodUtils.exitAPP();
                                        }
                                    }, false).show();
                        }

                });
    }

    private void toGo() {
        setIntent();
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
        if (WorkApp.getShare().getBoolean(Constants.isLaunch)) {
            // 第一次启动app,显示欢迎页
            WorkApp.getShare().put(Constants.isLaunch, false);
//            loadRootFragment(R.id.fl_container, IntroContentFragment.newInstance());
            loadRootFragment(R.id.fl_container, SplashFragment.newInstance());
        } else {
            loadRootFragment(R.id.fl_container, SplashFragment.newInstance());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        setIntent();
    }

    private void setIntent() {
        Intent intent = getIntent();
        Uri uri = intent.getData();
        if (uri != null) {
            WorkApp.payNo = uri.getQueryParameter("payReqCode");
            String authRet = uri.getQueryParameter("authRet");
            if (authRet != null) {
                WorkApp.authRet = true;
            }
            LogUtils.d("WorkApp.payNo: " + WorkApp.payNo + ", authRet: " + WorkApp.authRet);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
