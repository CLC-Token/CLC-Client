package com.btd.wallet.mvp.presenter.wallet;

import android.app.Activity;
import android.graphics.Bitmap;
import android.text.TextUtils;

import com.btd.library.base.mvp.persenter.BasePresenter;
import com.btd.wallet.mvp.model.wallet.WalletConfig;
import com.btd.wallet.pure.R;
import com.btd.wallet.config.SPKeys;
import com.btd.wallet.event.BalanceChengeEvent;
import com.btd.wallet.manager.hdtsdk.ConnectCallBack;
import com.btd.wallet.manager.hdtsdk.HDTManage;

import com.btd.wallet.mvp.contract.wallet.DetailContract;
import com.btd.wallet.utils.MethodUtils;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.crud.DataSupport;

/**
 * Description: 单个钱包详情页   <br>
 * Author: cxh <br>
 * Date: 2019/7/23 15:00
 */
public class DetailPresenter extends BasePresenter<DetailContract.IView>
        implements DetailContract.IPresenter {

    private HDTManage mHDTManage = HDTManage.getInstance();
    private WalletConfig mConfig;
    private Bitmap mQrImg;

    public DetailPresenter(Activity activity, DetailContract.IView iv) {
        super(activity, iv,true);
    }

    @Override
    public void initView() {
        super.initView();
        mConfig = (WalletConfig) mBundle.getSerializable(SPKeys.walletInfo);

    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        if(mConfig != null){
            WalletConfig config = DataSupport.where("fromAddr = ?", mConfig.getFromAddr())
                    .findFirst(WalletConfig.class);
            mConfig.setPrivateKey(config.getPrivateKey());
            mConfig.setId(config.getId());
            getBalance();
            getView().showInit(mConfig);
            mQrImg = CodeUtils.createImage(mConfig.getCode(),
                    (int) MethodUtils.getDimension(R.dimen.qrcode_size),
                    (int) MethodUtils.getDimension(R.dimen.qrcode_size), null);
        }
    }

    private void getBalance() {
        if(mConfig == null) return;
        if(mHDTManage.isConnected()){
            getBalanceDoing();
        }else{
            mHDTManage.setSdkConnectedWithBlock(mActivity,true, new ConnectCallBack() {
                @Override
                public void hasConnect(boolean connect) {
                    if(connect){
                        getBalanceDoing();
                    }
                }
            });
        }

    }

    private void getBalanceDoing(){
        mHDTManage.getIssueCurrencyBalanceList(mConfig.getFromAddr(), (code, message, info)
                -> HDTManage.getHandler().post(() -> {
            if(canUsePresenter()){
                if(code == 0 || code == 3){
                    mConfig.setBalanceBtd(info.getBalanceBtd());
                    mConfig.setBalanceHdt(info.getBalanceHdt());
                    mConfig.setFreezeBtd(info.isFreezePeerBtd());
                    mConfig.setFreezeHdt(info.isFreezePeerHdt());
                    mConfig.update(mConfig.getId());
                    getView().showInit(mConfig);
                }
            }
        }));
    }


    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onBalanceChengeEvent(BalanceChengeEvent event) {
        EventBus.getDefault().removeStickyEvent(event);
        getBalance();
    }

    @Override
    public Bitmap getCode() {
        return mQrImg;
    }

    @Override
    public void rename(String name) {
        if (TextUtils.isEmpty(name)) {
            getView().showToast(R.string.input_empty);
            return;
        }
        if (name.length() > 10) {
            getView().showToast(R.string.input_too_long);
            return;
        }
        mConfig.setNickName(name);
        mConfig.update(mConfig.getId());
        getView().showInit(mConfig);
    }

    @Override
    public WalletConfig getCofig() {
        return mConfig;
    }
}
