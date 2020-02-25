package com.btd.wallet.mvp.view.main;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.btd.library.base.util.LogUtils;
import com.btd.wallet.MainActivity;
import com.btd.wallet.pure.R;
import com.btd.wallet.base.fragment.BaseFragment;

import com.btd.wallet.core.WorkApp;
import com.btd.wallet.mvp.contract.main.SplashContract;

import com.btd.wallet.mvp.presenter.main.SplashPresenter;
import com.btd.wallet.utils.DelayUtils;
import com.btd.wallet.utils.ViewClickUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 启动页
 * Created by yzy on 2018/10/25 18:42
 */
public class SplashFragment extends BaseFragment<SplashContract.IPresenter>
        implements SplashContract.IView {

    @BindView(R.id.fl_bottom)
    FrameLayout flBottom;
    @BindView(R.id.iv_splash)
    ImageView ivSplash;
    @BindView(R.id.iv_splash_icon)
    ImageView ivSplashIcon;
    @BindView(R.id.btn_skip)
    Button btnSkip;
    DelayUtils delayUtils;
    private String url;
    private String payNo;
    private boolean goAdWeb = false;

    public static SplashFragment newInstance() {
        Bundle args = new Bundle();
        SplashFragment fragment = new SplashFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new SplashPresenter(mActivity, this);
    }

    @Override
    protected void initView() {
        delayUtils = new DelayUtils(along -> {
            LogUtils.d("skip------ ");
            skip();
        });
        delayUtils.start(1000, 2000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (delayUtils != null) {
            delayUtils.stop();
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_splash;
    }



    @Override
    public void skipSplash() {
        skip();
    }

    @OnClick(value = {R.id.iv_splash, R.id.btn_skip})
    public void onClick(View view) {
        if (ViewClickUtil.isFastDoubleClick(view.getId()) || mPresenter == null) {
            return;
        }
        switch (view.getId()) {
            case R.id.iv_splash: {
                //停止页面，带到可视了再进入逻辑
//                if (!TextUtils.isEmpty(url)) {
//                    if(delayUtils != null){
//                        delayUtils.stop();
//                    }
//                    start(BaseWebFragment.newInstance(url));
//                }
                break;
            }
            case R.id.btn_skip: {
                skip();
                break;
            }
            default:
                break;
        }
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        if(goAdWeb){
            goAdWeb = false;
            skip();
        }else {
            if (!WorkApp.isSplashFirst) {
                skip();
            } else {
                mPresenter.loadSpash();
            }
        }
    }

    private void skip() {
        if(delayUtils != null){
            delayUtils.stop();
        }
        MainActivity.startActivityForNewTask(mActivity);
        activityFinish();
    }
}