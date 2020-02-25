package com.btd.wallet.base.contact;

import com.btd.library.base.mvp.ipersenter.IBasePresenter;
import com.btd.library.base.mvp.ipersenter.IBaseView;
import com.btd.wallet.base.model.PageReq;
import com.btd.wallet.widget.MultiStateView;

import java.util.List;

/**
 * 列表管理器
 * Created by yzy on 2018/10/26 16:29
 */

public interface ListContract {
    //下拉刷新View层
    interface IListRefreshView<T> extends IBaseView {

        //加载失败
        void loadFail(boolean isError, String info);

        //加载完成
        void loadFinish();

        //设置加载页的状态
        void setViewState(MultiStateView.ViewState viewState);

        //获取数据成功,展示数据
        void showContent(List<T> t);

        //刷新数据
        void onRefresh();
    }

    //下拉刷新+上拉加载更多View层
    interface IListLoadMoreView<T> extends IListRefreshView<T>{
        //加载更多完成
        void loadMoreComplete();

        //启用下拉加载更多
        void enableLoadMore();

        //返回总数
        void setTotalNum(int totalNum, int totalPage);
    }

    //下拉刷新Presenter层
    interface IListRefreshPersenter extends IBasePresenter {

        //未分页数据,一次性查全部数据
        void loadData();
    }

    //下拉刷新+上拉加载更多Presenter
    interface IListLoadMorePersenter extends IListRefreshPersenter{

        //加载数据
        void loadData(PageReq pageReq);

        //是否有加载更多
        boolean hasLoadMore();

        void setPageNo(int pageNo);
    }


}
