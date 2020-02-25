package com.btd.wallet.base.recycler;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.btd.library.base.util.LogUtils;
import com.btd.wallet.pure.R;
import com.btd.wallet.base.contact.ListContract;
import com.btd.wallet.base.fragment.BaseFragment;
import com.btd.wallet.mvp.adapter.base.OnESItemClickListener;
import com.btd.wallet.utils.MethodUtils;
import com.btd.wallet.utils.StringUtils;
import com.btd.wallet.utils.recyclerviewflexibledivider.HorizontalDividerItemDecoration;
import com.btd.wallet.widget.MultiStateView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

import butterknife.BindView;

/**
 * 支持下拉刷新的Fragment
 *
 * T : 适配器
 * P : 必须是继承 IListRefreshPersenter的适配器
 * K ：是数据返回类型(相当于直接加载到界面上),为List的数据
 *
 * initView中初始化控件以及adapter,子类重写initView时记得super.initView()
 *
 * initAdapter()记得实例化对应的Adapter
 *
 * 加载数据需要重写loadData();   不同加载数据需要重写loadData
 *
 * 2018/9/13 新增是否可见刷新方法,支持某些页面不需要呈现前台重新刷新条件
 * 2018/10/26 将fragment与mvp模式相结合
 *
 * Created by yzy on 2018/8/13 09:45
 */

public abstract class RefreshFragment<T extends BaseQuickAdapter
        , P extends ListContract.IListRefreshPersenter, K>
        extends BaseFragment<P> implements OnRefreshListener,ListContract.IListRefreshView<K>
        ,OnESItemClickListener.IESItemClick<K , T> {

    @BindView(R.id.swipeLayout)
    protected SmartRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.recycler)
    protected RecyclerView mRecyclerView;
    @BindView(R.id.stateview)
    protected MultiStateView mMultiStateView;
    protected T mAdapter;
    protected boolean isLoadData = false;

    @Override
    protected void initView() {
        mSwipeRefreshLayout.setEnabled(canOnRefresh());
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mAdapter = (T) initAdapter();
        mRecyclerView.setAdapter(mAdapter);
      //  mRecyclerView.setLayoutManager(new WrapContentLinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        mRecyclerView.addItemDecoration(initItemDecoration());
        OnItemClickListener onItemClickListener = initOnItemClickListener();
        if (onItemClickListener != null) {
            mRecyclerView.addOnItemTouchListener(onItemClickListener);
        }
    }

    /**
     * 获取数据只需重写getData方法即可
     */
    protected void getData(){
        LogUtils.i(TAG);
        if(isLoadData){
            LogUtils.i(TAG + ":正在加载数据,忽略改请求");
            mSwipeRefreshLayout.finishRefresh(false);
            mSwipeRefreshLayout.setEnabled(canOnRefresh());
            return ;
        }
        isLoadData = true;
        mPresenter.loadData();
    }

    @Override
    protected int getContentView() {
        return R.layout.refresh_fragment;
    }

    @Override
    public void onRefresh() {
        getData();
    }

    protected RecyclerView.ItemDecoration initItemDecoration() {
        return new HorizontalDividerItemDecoration
                .Builder(mActivity)
                .marginResId(R.dimen.item_margin_right_left)
                .sizeProvider((position, parent) -> (int) MethodUtils.getDimension(R.dimen.divider_normal))
                .colorProvider((position, parent) -> MethodUtils.getColor(R.color.div_line))
                .build();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        onRefresh();
    }

    /**
     * 一定要重写该方法
     * @return
     */
    protected abstract BaseQuickAdapter initAdapter();

    private boolean isFirst = true;  //是否第一次进入页面,跟isSupportRefresh()方法一起控制页面是否只刷新一次

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        if(isSupportRefresh() || isFirst) {
            isFirst = false;
            getData();
        }
    }

    /**
     * 是否显示在前台刷新界面
     * @return true:刷新页面(默认) false:只在创建时刷新页面
     */
    protected boolean isSupportRefresh(){
        return true;
    }

    /**
     * 是否能够下拉刷新
     * @return true:能下拉刷新(默认) false:不能下拉刷新
     */
    protected boolean canOnRefresh(){
        return true;
    }

    /**
     * 数据获取完成调用方法,可重置控件
     */
    @Override
    public void loadFinish(){
        try {
            isLoadData = false;
            if (mSwipeRefreshLayout == null || mAdapter == null) {
                return;
            }
            mSwipeRefreshLayout.finishRefresh(false);
            mSwipeRefreshLayout.setEnabled(canOnRefresh());
        }catch (Exception ex){
            LogUtils.i(ex.getMessage());
        }
    }

    /**
     * 数据加载失败方法,可重置控件,以及设置错误页
     * @param isError
     * @param text
     */
    @Override
    public void loadFail(boolean isError, String text){
        try {
            if (mMultiStateView == null) {
                return;
            }
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
                mMultiStateView.setViewState(MultiStateView.ViewState.EMPTY);
            }
            loadFinish();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void setViewState(MultiStateView.ViewState viewState) {
        if(mMultiStateView != null){
            mMultiStateView.setViewState(viewState);
        }
    }

    protected void normalShowContent(List<K> k){
        mAdapter.setNewData(k);
        setViewState(MultiStateView.ViewState.CONTENT);
        loadFinish();
    }

    @Override
    public void onStop() {
        super.onStop();
        isLoadData = false;
    }

    @Override
    public void showContent(List<K> t) {
        normalShowContent(t);
    }

    /************************************************ 实现对RecyclerView的监听 ************************************************/
    protected OnItemClickListener initOnItemClickListener() {
        return new OnESItemClickListener<>(this);
    }

    @Override
    public void onItemClick(T adapter, View view, int position, K item) {

    }

    @Override
    public void onItemEditClick(T adapter, View view, int position, K item) {
        onItemClick(adapter , view , position , item);
    }

    @Override
    public void onItemLongClick(T adapter, View view, int position, K item) {

    }

    @Override
    public void onItemEditLongClick(T adapter, View view, int position, K item) {

    }

    @Override
    public void onItemHeaderChildClick(T adapter, View view, int position, K item) {

    }

    @Override
    public void onItemHeaderEditChildClick(T adapter, View view, int position, K item) {

    }

    @Override
    public void onItemChildClick(T adapter, View view, int position, K item) {
    }

    /**
     * 编辑状态下点击事件
     * @param adapter
     * @param view
     * @param position
     * @param item
     */
    @Override
    public void onItemEditChildClick(T adapter, View view, int position, K item) {
    }

    @Override
    public void onItemHeaderChildLongClick(T adapter, View view, int position, K item) {

    }

    @Override
    public void onItemHeaderEditChildLongClick(T adapter, View view, int position, K item) {

    }

    @Override
    public void onItemChildLongClick(T adapter, View view, int position, K item) {

    }

    @Override
    public void onItemEditChildLongClick(T adapter, View view, int position, K item) {

    }
    /************************************************ 实现对RecyclerView的监听 ************************************************/
}
