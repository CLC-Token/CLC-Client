package com.btd.wallet.mvp.presenter.wallet;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;

import com.blankj.utilcode.util.SPUtils;

import com.btd.library.base.http.manager.RxHttpManager;
import com.btd.library.base.util.LogUtils;
import com.btd.wallet.mvp.model.wallet.WalletConfig;
import com.btd.wallet.mvp.service.UserServiceImpl;
import com.btd.wallet.pure.R;
import com.btd.wallet.base.presenter.BaseRefreshPresenter;
import com.btd.wallet.config.Constants;
import com.btd.wallet.config.SPKeys;
import com.btd.wallet.core.WorkApp;
import com.btd.wallet.event.BalanceChengeEvent;

import com.btd.wallet.manager.hdtsdk.ConnectCallBack;
import com.btd.wallet.manager.hdtsdk.HDTManage;

import com.btd.wallet.mvp.contract.wallet.WalletContract;

import com.btd.wallet.mvp.model.wallet.WalletModel;

import com.btd.wallet.update.DownloadManager;
import com.btd.wallet.update.UICallBackDao;
import com.btd.wallet.utils.PDialogUtil;
import com.btd.wallet.utils.StringUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.crud.DataSupport;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import rx.schedulers.Schedulers;

/**
 * Description: 我的钱包   <br>
 * Author: cxh <br>
 * Date: 2019/6/25 11:56
 */
public class WalletPresenter extends BaseRefreshPresenter<WalletContract.IView
        , WalletModel> implements WalletContract.IPresenter {

    private List<WalletConfig> mConfigs;
    private final List<WalletModel> mModels = new ArrayList<>();
    private UserServiceImpl mService = new UserServiceImpl();
    private HDTManage mHDTManage = HDTManage.getInstance();
    private boolean isFinish;
    private BigDecimal mTotalBalanceBtd;
    private BigDecimal mTotalBalanceHdt;
    private boolean isFirst = true;
    private int queryCount = 0;
    private boolean hasGetSystemAddress = false;
    //10分钟才会去刷新
    private final int RequestWalletTimeRefresh=10;
    //不在当前页面时，有数据刷新。回到该界面时。强制刷新
    private Boolean mustRresh=false;

    public WalletPresenter(Activity activity, WalletContract.IView iv) {
        super(activity, iv,false);
    }

    @Override
    public void initView() {
        super.initView();
        SmartRefreshLayout smartRefreshLayout = getView().getRefrshLayout();
        smartRefreshLayout.setEnableLoadMore(false);
        smartRefreshLayout.setOnRefreshListener(refreshLayout -> {
            //下拉强制刷新余额。
            loadInitData();
            getBalanceInit(true);

        });
       // getNewest();
    }

    Boolean isUpdating=false;
    /*版本更新检测*/
    private void checkUpdate() {
//        if (MainActivity.isCheckUpdate) {
//            return;
//        }
        if(isUpdating){
            return;
        }

        if (!canUsePresenter()) {
            return;
        }
        isUpdating=true;
        DownloadManager downManager = new DownloadManager(mActivity, false, new UICallBackDao() {

            @Override
            public void onSuccess(long responseCode)
            {
                // MainActivity.isCheckUpdate = true;

            }

            @Override
            public void onFailure() {
                isUpdating=false;
            }

            @Override
            public void download(boolean success) {
                isUpdating=false;

            }

            @Override
            public void onFininsh() {


            }
        });
        downManager.updateVersion();
    }


    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        queryCount = 0;

        //无钱包的情况下，不主动连接。
        mConfigs = DataSupport.findAll(WalletConfig.class);
        if(mConfigs!=null&&mConfigs.size()>0){
            if(isFirst){
                isFirst = false;
                sdkInit();
            }
          //  if (mHDTManage.isConnected()) {
                initLoadData();
          //  }

        }else {
            mView.setStateVisiable(true);
            loadSuccess(null);
            getView().refreshItem(true, new BigDecimal("0"), new BigDecimal("0"), true);
        }

        checkUpdate();
    }

    private void sdkInit(){
//        mHDTManage.attach(connect -> {
//            if (connect && canUsePresenter() && getView().isFragmentVisible()) {
//                Log.d("Chainsql.c","Chainsql.c连接成功");
//
//            }
////            if(!connect) {
////                closeSdk();
////            }
//
//        });
    }

    private Boolean closeingSdk=false;
    private void closeSdk(){
        if(closeingSdk)return;
        closeingSdk=true;
        Log.d("Chainsql.c","Chainsql.c连接断开");

        mHDTManage.sdkClose();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("Chainsql.c","Chainsql.c连接断开，重连");
                            closeingSdk=false;
                            mHDTManage.setSdkConnected();
//                            mHDTManage=new HDTManage();
//                            sdkInit();
                        }
                    });
                }catch (Exception e){

                }
            }
        }).start();



    }

    private void initLoadData() {
        isFirst = false;
        loadInitData();
        if(mConfigs != null && mConfigs.size() > 0){
            mView.setStateVisiable(false);
            loadSuccess(mModels);
            long lasttime= SPUtils.getInstance().getLong(SPKeys.lastBalanceTime,0);
            if(mustRresh||lasttime==0||(System.currentTimeMillis()-lasttime)/1000>RequestWalletTimeRefresh*60) {//超过10分钟。才去获取。
                getBalanceInit(false);
                mustRresh=false;
            }
        }else {
            //没有数据显示空
            mView.setStateVisiable(true);
            loadSuccess(null);
            getView().refreshItem(true, new BigDecimal("0"), new BigDecimal("0"), true);
        }

//        TaskManage.doTask(new Task<Boolean>(false) {
//            @Override
//            public void ioThread() {
//                mConfigs = DataSupport.findAll(WalletConfig.class);
//                setT(mConfigs != null && mConfigs.size() > 0);
//                loadInitData();
//            }
//
//            @Override
//            public void uiThread(Boolean haveData) {
//                if (haveData) {
//                    loadSuccess(mModels);
//                    getBalance();
//                } else {
//                    //没有数据显示空
//                    loadSuccess(null);
//                    getView().refreshItem(true, new BigDecimal("0"), new BigDecimal("0"), true);
//                }
//            }
//        });
    }

    /**
     * 加载全部用户数据
     */
    private void loadInitData() {
        mModels.clear();
        isFinish = true;
//        mTotalBalanceBtd = 0;
//        mTotalBalanceHdt = 0;
        mTotalBalanceBtd=new BigDecimal("0");

        mTotalBalanceHdt=new BigDecimal("0");
        mConfigs = DataSupport.findAll(WalletConfig.class);
        if (mConfigs != null && mConfigs.size() > 0) {
            WalletModel m;
            ArrayList<String> address = new ArrayList<>();
            for (WalletConfig c : mConfigs) {
                address.add(c.getFromAddr());
                m = new WalletModel();
                m.setId(c.getId());
                m.setAddress(c.getFromAddr());
                m.setNickName(c.getNickName());
                m.setBalanceHdt(c.getBalanceHdt());
                m.setBalanceBtd(c.getBalanceBtd());
                m.setFreezeBtd(c.isFreezeBtd());
                m.setFreezeHdt(c.isFreezeHdt());
                m.setStrCode(c.getCode());
                if(c.getWords()!=null&&c.getWords().length()>0){
                    m.setWords(c.getWords());
                }else{
                    m.setWords("");
                }
                m.setLoadBalance(false);
                mModels.add(m);
                mTotalBalanceBtd=mTotalBalanceBtd.add(m.getBalanceBtdBigDecimal());
                mTotalBalanceHdt=mTotalBalanceHdt.add(m.getBalanceHdtBigDecimal());
            }
            getView().refreshItem(true, mTotalBalanceBtd, mTotalBalanceHdt, false);
            //监听余额的变化
            mHDTManage.subscribe(address, (code, message, receiveInfo) -> {
                EventBus.getDefault().postSticky(new BalanceChengeEvent(receiveInfo.getToAddr()));
                if (canUsePresenter() && getView().isFragmentVisible()) {
                    loadInitData();
                    getBalanceInit(false);
                }else{
                    mustRresh=true;
                }
            });
        }
    }
    //调获取余额时，把总余额置0
    private void getBalanceInit(Boolean showTip){
        showTip=false;
        if(mModels.size()==0){
            mView.setStateVisiable(true);
            getView().getRefrshLayout().finishRefresh();
            return ;
        }
        if(!mHDTManage.isConnected()){

           mHDTManage.setSdkConnectedWithBlock(mActivity,showTip,new ConnectCallBack() {
               @Override
               public void hasConnect(boolean connect) {
                   if(connect){
                       mTotalBalanceBtd=new BigDecimal("0");
                       mTotalBalanceHdt=new BigDecimal("0");
                       getBalance();
                   }else{//
                       if (!canUsePresenter()) {
                           return;
                       }
                       getView().getRefrshLayout().finishRefresh();
                       getView().showToast(R.string.connect_fail);
                   }
               }
           });
            return;
        }
        mTotalBalanceBtd=new BigDecimal("0");
        mTotalBalanceHdt=new BigDecimal("0");
        getBalance();
    }

    /**
     * 单个获取余额
     */
    private  void getBalance() {
        WalletModel c = getQuryModel();
        if(c == null){
            //都已经计算完了
            HDTManage.getHandler().post(new Runnable() {
                @Override
                public void run() {
                    getView().refreshItem(true, mTotalBalanceBtd, mTotalBalanceHdt, false);
                    SPUtils.getInstance().put(SPKeys.lastBalanceTime,System.currentTimeMillis());
                    getView().getRefrshLayout().finishRefresh();
                }
            });

            return;
        }
        Log.e("====","获取余额==="+c.getAddress());
        mHDTManage.getIssueCurrencyBalanceList(c.getAddress(), (i, s, info) -> {
            if (!canUsePresenter()) {
                return;
            }
            synchronized (mModels){
                boolean error = false;
                isFinish = true;
                if (i == 0 || i == 3) {
                    if (info != null) {
                        for (WalletModel m : mModels) {
                            if (m.getAddress().equals(c.getAddress())) {
                                m.setBalanceBtd(info.getBalanceBtd());
                                m.setBalanceHdt(info.getBalanceHdt());
                                m.setFreezeBtd(info.isFreezePeerBtd());
                                m.setFreezeHdt(info.isFreezePeerHdt());
                                if(!m.isLoadBalance()){
                                    mTotalBalanceBtd=mTotalBalanceBtd.add(m.getBalanceBtdBigDecimal());
                                    mTotalBalanceHdt=mTotalBalanceHdt.add(m.getBalanceHdtBigDecimal());
                                }
                                m.setLoadBalance(true);
                                WalletConfig walletConfig=new WalletConfig();
                                walletConfig.setId(c.getId());
                                walletConfig.setBalanceHdt(info.getBalanceHdt());
                                walletConfig.setBalanceBtd(info.getBalanceBtd());
                                walletConfig.setFreezeBtd(info.isFreezePeerBtd());
                                walletConfig.setFreezeHdt(info.isFreezePeerHdt());
                                walletConfig.update(walletConfig.getId());

                            }
                            if (!m.isLoadBalance()) {
                                isFinish = false;
                            }
                        }
                    } else {
                        error = true;
                    }
                } else {
                    error = true;
                }
                if (error) {
                    if(queryCount >= 5){
                        HDTManage.getHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                getView().showError();
                                getView().getRefrshLayout().finishRefresh();
                            }
                        });
                    }else {
                        queryCount++;
                        //这里需要判断
                     //   mHDTManage.setSdkConnected();
                        getBalance();
                    }
                } else {
                    HDTManage.getHandler().post(()->getView().refreshItem(isFinish, mTotalBalanceBtd, mTotalBalanceHdt, false));
                    if(!isFinish){
                        getBalance();
                    }else{
                        SPUtils.getInstance().put(SPKeys.lastBalanceTime,System.currentTimeMillis());
                        getView().getRefrshLayout().finishRefresh();
                    }
                }
            }
        });
    }

    private WalletModel getQuryModel(){
        for (WalletModel c : mModels) {
            if (!c.isLoadBalance()){
                return c;
            }
        }
        return null;
    }






    @Override
    public void onRefresh() {
        loadInitData();
        getBalanceInit(true);
    }


}
