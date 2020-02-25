package com.btd.wallet.home.ui;

import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.btd.library.base.config.LanuageConfig;
import com.btd.library.base.util.LogUtils;
import com.btd.wallet.mvp.model.wallet.BaseRecordData;
import com.btd.wallet.mvp.model.wallet.RecordItem;
import com.btd.wallet.mvp.model.wallet.WalletEvent;
import com.btd.wallet.pure.R;
import com.btd.wallet.base.fragment.BaseSupportFragment;
import com.btd.wallet.config.SPKeys;
import com.btd.wallet.config.WalletType;
import com.btd.wallet.core.WorkApp;
import com.btd.wallet.home.adapter.WalletRecordAdapter;
import com.btd.wallet.manager.hdtsdk.ConnectCallBack;
import com.btd.wallet.manager.hdtsdk.HDTManage;

import com.btd.wallet.utils.MethodUtils;
import com.btd.wallet.utils.StringUtils;
import com.peersafe.hdtsdk.api.CurrencyTxDetail;
import com.peersafe.hdtsdk.api.CurrencyTxDetails;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;

/**
 * $Author: xhunmon
 * $Date: 2018-01-09
 * $Description: 交易记录详情页
 * 1-根据地址查询20条交易详情-然后就行分类日期和item
 */

public class WalletRecordFragment extends BaseSupportFragment {

    @BindView(R.id.rv_Record)
    RecyclerView mRvRecord;

    @BindView(R.id.empty)
    ConstraintLayout mEmpty;
    @BindView(R.id.txt_content)
    TextView txt_content;
    @BindView(R.id.img_pri_tip)
    ImageView imageView;

    LinearLayoutManager mLlManager;
    private WalletRecordAdapter mAdapter;
    private String mMarker;
    private HDTManage mManage = HDTManage.getInstance();
    private String mAddr;
    private List<BaseRecordData> mItems = new ArrayList<>();
    private Date mCurDate;
    private Date mLastDate;
    private Runnable mShowLoadingTask;
    /*查询没有数据，应该显示空布局*/
    private boolean isEmpty = true;
    private ClipboardManager mClipboardManager;
    private boolean isFirstDetail = false;
    /*是否是第一查询，前二十条数据*/
    private boolean isFirstRecodr = true;


    public static WalletRecordFragment newInstance(String mAddress) {
        Bundle args = new Bundle();
        args.putString(SPKeys.walletAddr ,mAddress);
        WalletRecordFragment fragment = new WalletRecordFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_wallet_record;
    }

    @Override
    protected void initToolbar(View rootView) {
        super.initToolbar(rootView);
        setBack();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void initView() {
        setTitle(getStr(R.string.wallet_record_title));
        mAddr = mBundle.getString(SPKeys.walletAddr);

        mClipboardManager = (ClipboardManager) mActivity.getSystemService(Context.CLIPBOARD_SERVICE);
        mLlManager = new LinearLayoutManager(mActivity);
        LogUtils.i("==mAddr: "+mAddr);
        mAdapter = new WalletRecordAdapter(mActivity, mItems);
        mRvRecord.setLayoutManager(mLlManager);
        mRvRecord.setAdapter(mAdapter);
        mAdapter.setOnLoadMoreListener(() -> {
            LogUtils.i("==setOnLoadMoreListener==mMarker: "+mMarker);
            if (!TextUtils.isEmpty(mMarker)) {
//                mShowLoadingTask = () -> PDialogUtil.startProgress(context, "",null);
//                x.task().postDelayed(mShowLoadingTask, 200);
                mManage.getIssueCurrencyTxDetail(mAddr, 20, mMarker);
            }
        },mRvRecord);
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            if(mItems.get(position).getT() instanceof RecordItem){
                RecordItem item = (RecordItem) mItems.get(position).getT();
                if(view.getId() == R.id.ll_address){
                    mClipboardManager.setText(item.getAddr());
                    showToast(getStr(R.string.copy_addr_success));
                }else if(view.getId() == R.id.ll_hash){
                    mClipboardManager.setText(item.getHash());
                    showToast(getStr(R.string.copy_hash_success));
                }

                LogUtils.i("==position: "+position+", item.getAddr():"+view.getId());
            }
        });
//        mShowLoadingTask = () -> PDialogUtil.startProgress(context, "",null);
//        x.task().postDelayed(mShowLoadingTask, 200);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void initData() {
        super.initData();
        mCurDate = StringUtils.longToDate(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss");
        /*1.第一次查询marker传入空*/
        LogUtils.i("=========mManage.isConnected():"+mManage.isConnected());
        if(mManage.isConnected()){
            mManage.getIssueCurrencyTxDetail(mAddr, 20, "");
        }else {
            isFirstDetail = true;
            if (MethodUtils.getCurNetworkStatus(WorkApp.workApp) == MethodUtils.None_NetWork) {
                LogUtils.i("没有网络,sdk断开");
                showToast(getStr(R.string.nonetwork));
                stopDialog();
                mEmpty.setVisibility(View.VISIBLE);
                txt_content.setText(R.string.nonetwork);
                imageView.setImageResource(R.drawable.default_error);
                //                getActivity().onBackPressed();
            }else {
                mManage.setSdkConnectedWithBlock(mActivity, true, new ConnectCallBack() {
                    @Override
                    public void hasConnect(boolean connect) {
                        if(connect){
                            mManage.getIssueCurrencyTxDetail(mAddr, 20, "");
                        }
                    }
                });
            }
        }

    }

    @Override
    public void onStart() {
        super.onStart();
//        if (MethodUtils.getCurNetworkStatus(WorkApp.workApp) == MethodUtils.None_NetWork) {
//            LogUtils.i("没有网络,sdk断开");
//            stopDialog();
//            getActivity().onBackPressed();
//        }
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }

    /*设置在异步接收，更新UI需要转到UI线程*/
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void btrWalletEvent(WalletEvent event) {
        WalletEvent e = event;
        EventBus.getDefault().removeStickyEvent(event);
        LogUtils.i("===========e.sdkState: "+e.sdkState);
        if (e.sdkState == WalletType.SDK_INIT) {
            if (e.isConnected) {//已连接sdk
                if(isFirstDetail){
                    isFirstDetail = false;
                    mManage.getIssueCurrencyTxDetail(mAddr, 20, "");
                }
            } else {
//                if (MethodUtils.getCurNetworkStatus(WorkApp.workApp) == MethodUtils.None_NetWork) {
//                    LogUtils.i("没有网络,sdk断开");
//                    showToast(getStr(R.string.nonetwork));
//                    stopDialog();
//                    getActivity().onBackPressed();
//                }
            }
        }
        if (e.what != WalletType.RECORD) {
            return;
        }
        if (e.sdkState == WalletType.SDK_RECORD) {//查询交易记录
            if (e.code == 0) {//成功
                LogUtils.i("==成功获取记录 e.details is null: " + (e.details == null));
                dealwithDetils(e.details);
                mMarker = e.marker;
            }else {
                LogUtils.i("==结果码不为0，是否显示空布局 ? isEmpty: "+isEmpty);
                if(isEmpty){
                    mEmpty.setVisibility(View.VISIBLE);
                }
            }
            stopDialog();
        }
    }

    /**
     * 处理每一次的交易的详情
     */
    private void dealwithDetils(CurrencyTxDetails details) {
        if (details != null) {
            ArrayList<CurrencyTxDetail> detailList = details.getCurrencyTxDetailList();
            int size = detailList.size();
            LogUtils.i("===size: " + size);
            if(size==0 && isEmpty){//查询到0条记录
                mEmpty.setVisibility(View.VISIBLE);
                return;
            }
            CurrencyTxDetail detail;
            Date date;
            Date nextDate = null;
            BaseRecordData<RecordItem> itemData;
            BaseRecordData<String> timeData;
            RecordItem item;
            for (int i = 0; i < size; i++) {
                detail = detailList.get(i);
                itemData = new BaseRecordData<>();
                timeData = new BaseRecordData<>();
                item = new RecordItem();
                date = detail.getDate();
                if(i<size-1){
                    if(detailList.get(i+1) != null){
                        nextDate = detailList.get(i+1).getDate();
                    }else {
                        nextDate = null;
                    }
                }
                /*设置时间的item*/
                if (i == 0 && isFirstRecodr) {//第一个判断是否是“今天”的交易
                    isFirstRecodr = false;
                    timeData.setDataType(WalletRecordAdapter.ITEM_TYPE_TIME);
                    if (StringUtils.inSameDay(mCurDate, date)) {
                        timeData.setT(getStr(R.string.wallet_record_item_time));
                    } else {
                        timeData.setT(StringUtils.dateToString(date, "yyyy.MM.dd"));
                    }
                    mItems.add(timeData);
                } else {
                    /*这次的日期跟上次的日期不是同一天的时候才要加time item*/
                    if (!StringUtils.inSameDay(date, mLastDate)) {
                        timeData.setDataType(WalletRecordAdapter.ITEM_TYPE_TIME);
                        timeData.setT(StringUtils.dateToString(date, "yyyy.MM.dd"));
                        mItems.add(timeData);
                    }
                }
                mLastDate = date;

                /*设置正常的item*/
                //1. 判断交易类型：a-还需要服务器的接口，判断是否是交易的；b-转入，c-转出
                //2. 设置对方的地址
                if (detail.getFromAddr().equals(mAddr)) {
                    /*这里还应该加一层判断是否是兑换产品的    */
                    item.setType(getStr(R.string.wallet_record_item_out));
                    item.setAddr(detail.getToAddr());
                } else {
                    item.setType(getStr(R.string.wallet_record_item_in));
                    item.setAddr(detail.getFromAddr());
                }

                /*判断这一条数据跟下一条数据是同一天则visible，否则unvisible*/
                item.setLineShow(StringUtils.inSameDay(date, nextDate));

              //  item.setBalance(detail.getAmount());
                item.setBalance(StringUtils.decimalToString(new BigDecimal(detail.getAmount())));
                item.setFee(StringUtils.killZero(Double.parseDouble(detail.getTransferFee())));
                item.setDate(StringUtils.dateToString(date, "yyyy.MM.dd HH:mm"));
                item.setCoinType("HDT".equals(detail.getCoinType())? WalletType.SDK_COIN_HDT: WalletType.SDK_COIN_BTD);
                item.setHash(detail.getTxId());
                itemData.setDataType(WalletRecordAdapter.ITEM_TYPE_NOMAL);
                itemData.setT(item);
                mItems.add(itemData);
            }
            /*一轮之后更新*/
            mAdapter.setNewData(mItems);
            isEmpty = false;
//            mAdapter.updateData(mItems);
        }
    }



    /**
     * 停止并移除对话框
     */
    private void stopDialog() {
//        if (mShowLoadingTask != null) {
//            x.task().removeCallbacks(mShowLoadingTask);
//        }
//        PDialogUtil.stopProgress();
    }

}
