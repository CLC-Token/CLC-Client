package com.btd.wallet.base.presenter;

import android.app.Activity;

import com.btd.library.base.mvp.persenter.BasePresenter;
import com.btd.wallet.base.contact.ListContract;

import java.util.ArrayList;
import java.util.List;

/**
 * 刷新管理器基类
 * Created by yzy on 2018/11/13 18:31
 */

public class BaseRefreshPresenter<V extends ListContract.IListRefreshView<T> , T> extends BasePresenter<V> implements ListContract.IListRefreshPersenter{
    protected List<T> mData = new ArrayList<>();

    public BaseRefreshPresenter(Activity activity, V iv) {
        super(activity, iv);
    }

    public BaseRefreshPresenter(Activity activity, V iv, boolean useEventBus) {
        super(activity, iv, useEventBus);
    }

    @Override
    public void loadData() {

    }

    protected void loadFail(boolean isFail , String info){
        if(canUsePresenter()){
            getView().loadFail(isFail , info);
        }
    }

    protected void loadSuccess(List<T> newData){
        if(canUsePresenter()) {
            if (newData == null || newData.size() == 0) {
                getView().loadFail(false, "");
            } else {
                getView().showContent(newData);
            }
        }
    }
}
