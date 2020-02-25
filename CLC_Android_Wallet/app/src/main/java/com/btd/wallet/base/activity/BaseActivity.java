package com.btd.wallet.base.activity;

import android.os.Bundle;

import com.btd.library.base.mvp.ipersenter.IBasePresenter;
import com.btd.library.base.mvp.ipersenter.IBaseView;


/**
 * Description:    <br>
 * Author: cxh <br>
 * Date: 2018/10/18 15:15
 */
public abstract class BaseActivity<P extends IBasePresenter> extends BaseSupportActivity implements IBaseView {

    protected P mPresenter;

    protected abstract void initPresenter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        AllActivity.addActivity(this);//BaseSupportActivity已有
        initPresenter();
        checkPresenterIsNull();
        mPresenter.onCreate(getIntent() != null ? getIntent().getExtras() : null);
    }

    private void checkPresenterIsNull() {
        if (mPresenter == null) {
            throw new IllegalStateException("please init mPresenter in initPresenter() method ");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        AllActivity.removeActivity(this);
        mPresenter.onDestroy();
        mPresenter = null;
    }

    @Override
    public void showToast(String t) {

    }

    @Override
    protected void initView() {
        mPresenter.initView();
    }

    @Override
    protected void initData() {
        mPresenter.initData();
    }
}
