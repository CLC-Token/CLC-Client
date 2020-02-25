package com.btd.wallet.base.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.btd.library.base.mvp.ipersenter.IBasePresenter;
import com.btd.library.base.mvp.ipersenter.IBaseView;
import com.btd.wallet.utils.MethodUtils;

/**
 * Description:    <br>
 * Author: cxh <br>
 * Date: 2018/10/18 15:15
 */
public abstract class BaseFragment<P extends IBasePresenter> extends BaseSupportFragment implements IBaseView {
    protected P mPresenter;

    protected abstract void initPresenter();

    private void checkPresenterIsNull() {
        if (mPresenter == null) {
            throw new IllegalStateException("please init mPresenter in initPresenter() method ");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initPresenter();
        checkPresenterIsNull();
        mPresenter.onCreate(getArguments());
        mPresenter.initView();
        mPresenter.initData();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.onResume();
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        mPresenter.onSupportVisible();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.onPause();
    }

    @Override
    public boolean isFragmentVisible(){
        return isSupportVisible();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mPresenter!=null)
        mPresenter.onDestroy();

        mPresenter = null;
    }

    @Override
    public void showToast(String t) {
        MethodUtils.showToast(mActivity, t);
    }

    @Override
    public void showToast(int resouceId) {
        showToast(getString(resouceId));
    }



//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        if (newConfig.fontScale != 1)//非默认值
//            getResources();
//        super.onConfigurationChanged(newConfig);
//    }
//
//    @Override
//    public Resources getResources() {
//        Resources res = super.getResources();
//        if (res.getConfiguration().fontScale != 1) {//非默认值
//            Configuration newConfig = new Configuration();
//            newConfig.setToDefaults();//设置默认
//            res.updateConfiguration(newConfig, res.getDisplayMetrics());
//        }
//        return res;
//    }
}
