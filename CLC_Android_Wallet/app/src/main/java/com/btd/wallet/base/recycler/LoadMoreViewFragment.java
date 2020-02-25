package com.btd.wallet.base.recycler;

import android.support.v7.widget.RecyclerView;

import com.btd.library.base.util.LogUtils;
import com.btd.wallet.pure.R;
import com.btd.wallet.base.contact.ListContract;
import com.btd.wallet.base.model.PageReq;
import com.btd.wallet.utils.DelayUtils;
import com.btd.wallet.utils.StringUtils;
import com.btd.wallet.widget.MultiStateView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.loadmore.LoadMoreView;

import java.util.List;

/**
 *
 * 具有上拉加载更多的Fragment
 *
 * Created by yzy on 2018/8/13 09:53
 */

public abstract class LoadMoreViewFragment<T extends BaseQuickAdapter
        , P extends ListContract.IListLoadMorePersenter
        , K> extends RefreshFragment<T , P , K>
        implements BaseQuickAdapter.RequestLoadMoreListener,ListContract.IListLoadMoreView<K>{

    protected int pageNo = 1;
    protected int pageSize = 20;
    protected int totalPage = 0;    //总页数
    protected int totalNum = 0;     //总数

    /** 没有更多 */
    protected void loadMoreEnd() {
        mRecyclerView.post(() -> {
            mAdapter.loadMoreEnd();
            LogUtils.i(" onLoadMoreRequested loadComplete onFinish 没有更多了");
        });
    }

    /** 加载更多失败 */
    protected void loadMoreFail() {
        mRecyclerView.post(() -> {
            mAdapter.loadMoreFail();
            LogUtils.i("finish");
        });
    }

    /** loading,失败,没有更多 */
    protected LoadMoreView initLoadMoreView() {
        return new LoadMoreView() {
            @Override
            public int getLayoutId() {
                return R.layout.v2_layout_load_more;
            }

            /**
             * 如果返回true，数据全部加载完毕后会隐藏加载更多
             * 如果返回false，数据全部加载完毕后会显示getLoadEndViewId()布局
             */
            @Override
            public boolean isLoadEndGone() {
                return false;
            }

            @Override
            protected int getLoadingViewId() {
                return R.id.load_more_loading_view;
            }

            @Override
            protected int getLoadFailViewId() {
                return R.id.load_more_load_fail_view;
            }

            @Override
            protected int getLoadEndViewId() {
                return R.id.load_more_load_end_view;
            }
        };
    }

    @Override
    public void onRefresh() {
        pageNo = 1;
        mAdapter.setEnableLoadMore(false);
        getData();
    }

    @Override
    public void onSupportVisible() {
        if(isSupportVisible()){
            LogUtils.d("界面可见刷新重置page");
            pageNo = 1;
            mPresenter.setPageNo(pageNo);
        }
        super.onSupportVisible();
    }

    @Override
    public void onLoadMoreRequested() {
        if(!mPresenter.hasLoadMore()){
            loadMoreEnd();
            return ;
        }
        mSwipeRefreshLayout.setEnabled(false);
        pageNo ++;
        getData();
    }

    /**
     * 数据加载失败方法,重置方法以及设置错误页
     * @param isError
     * @param text
     */
    @Override
    public void loadFail(boolean isError, String text){
        try {
            if (mMultiStateView == null) {
                return;
            }
            if (pageNo == 1) {
                if (isError) {
                    if(mAdapter != null && mAdapter.getData() != null) {
                        mAdapter.getData().clear();
                        mAdapter.notifyDataSetChanged();
                    }
                    if(StringUtils.isEmptyOrNull(text)){
                        mMultiStateView.setViewState(MultiStateView.ViewState.ERROR);
                    }else {
                        mMultiStateView.setViewStateAndContent(MultiStateView.ViewState.ERROR, text);
                    }
                } else {
                    mAdapter.getData().clear();
                    mMultiStateView.setViewStateAndContent(MultiStateView.ViewState.EMPTY, getStr(R.string.activity_empty_txt_content));
                }
            } else {
                if (isError) {
                    loadMoreFail();
                } else {
                    loadMoreEnd();
                }
            }
            loadFinish();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    /**
     * 数据加载完成调用的方法,重置控件
     */
    @Override
    public void loadFinish() {
        try {
            isLoadData = false;
            if (mSwipeRefreshLayout == null || mAdapter == null) {
                return;
            }
            mSwipeRefreshLayout.closeHeaderOrFooter();
            mSwipeRefreshLayout.finishRefresh(false);
            mSwipeRefreshLayout.setEnabled(canOnRefresh());
            if(pageNo == 1){
                mAdapter.setEnableLoadMore(mPresenter.hasLoadMore());
            }else {
                mAdapter.setEnableLoadMore(true);
            }
        }catch (Exception ex){
            LogUtils.i(ex.getMessage());
        }
    }

    /** 加载更多完成 */
    @Override
    public void loadMoreComplete() {
        mRecyclerView.post(() -> {
            mAdapter.loadMoreComplete();
            LogUtils.i("complete");
        });
    }

    /**
     * 启用上拉加载更多
     */
    @Override
    public void enableLoadMore() {
        new DelayUtils().start(200, 5000, along -> {
            if(null != mAdapter) {
                mAdapter.setLoadMoreView(initLoadMoreView());
                mAdapter.setOnLoadMoreListener(LoadMoreViewFragment.this);
            }
        });
    }

    @Override
    protected void getData() {
        LogUtils.i(TAG + "-->" + pageNo + "," + pageSize+","+totalPage+","+totalNum);
        if(isLoadData){
            LogUtils.i(TAG + ":正在加载数据,忽略改请求");
            mSwipeRefreshLayout.finishRefresh(false);
            mSwipeRefreshLayout.setEnabled(canOnRefresh());
            return ;
        }
        isLoadData = true;
        mPresenter.loadData(new PageReq(pageNo , pageSize));
    }

    @Override
    protected void normalShowContent(List<K> k){
        if(pageNo == 1) {
            mAdapter.setNewData(k);
        }else{
            if(mRecyclerView.getScrollState() == RecyclerView.SCROLL_STATE_IDLE
                    && !mRecyclerView.isComputingLayout()) {
                mAdapter.notifyDataSetChanged();
            }
        }
        setViewState(MultiStateView.ViewState.CONTENT);
        loadFinish();
    }

    @Override
    public void setTotalNum(int totalNum , int totalPage) {
        this.totalPage = totalPage;
        this.totalNum = totalNum;
    }

}
