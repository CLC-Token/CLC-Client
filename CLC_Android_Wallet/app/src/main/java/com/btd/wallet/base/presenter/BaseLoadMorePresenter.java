package com.btd.wallet.base.presenter;

import android.app.Activity;

import com.btd.library.base.util.LogUtils;
import com.btd.wallet.base.contact.ListContract;
import com.btd.wallet.base.model.PageReq;
import com.btd.wallet.utils.MethodUtils;

import java.util.List;

/**
 * 包含上拉加载更多
 * Created by yzy on 2018/11/13 18:31
 */

public class BaseLoadMorePresenter<V extends ListContract.IListLoadMoreView<T> , T> extends BaseRefreshPresenter<V , T> implements ListContract.IListLoadMorePersenter{
    protected int totalPage = 0;
    protected int pageNo = 1;
    protected int pageSize = 20;

    public BaseLoadMorePresenter(Activity activity, V iv) {
        super(activity, iv);
    }

    public BaseLoadMorePresenter(Activity activity, V iv, boolean useEventBus) {
        super(activity, iv, useEventBus);
    }

    @Override
    public void loadData() {

    }

    @Override
    public void loadData(PageReq pageReq) {
    }

    @Override
    public boolean hasLoadMore() {
        return pageNo < totalPage;
    }

    @Override
    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    protected void loadSuccess(PageReq pageReq , List<T> newData , int totalNum){
        if(!canUsePresenter()){
            return;
        }
        try {
            pageNo = pageReq.getPageNo();
            if(newData != null && newData.size() > 0 ){
                totalPage = MethodUtils.getTotalPage(pageReq.getPageSize() , totalNum);
                getView().setTotalNum(totalNum , totalPage);
                if(pageNo == 1) {
                    mData.clear();
                }
                mData.addAll(newData);
                if(mData.size() > 0){
                    getView().showContent(mData);
                }else{
                    loadFail(false , "");
                }
                if(pageNo == 1){
                    if(pageNo < totalPage){
                        getView().enableLoadMore(); //启用加载更多
                    }else {
                        getView().loadMoreComplete();
                    }
                }else{
                    getView().loadMoreComplete();
                }
            }else{
                totalPage = 0 ;
                getView().setTotalNum(0 , totalPage);
                loadFail(false , "");
            }
        }catch (Exception ex){
            LogUtils.i(ex.getMessage());
            loadFail(true , "");
        }
    }

}
