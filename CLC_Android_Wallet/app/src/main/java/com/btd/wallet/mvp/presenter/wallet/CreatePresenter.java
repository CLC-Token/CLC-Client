package com.btd.wallet.mvp.presenter.wallet;

import android.app.Activity;
import android.os.Looper;
import android.text.TextUtils;

import com.btd.library.base.mvp.persenter.BasePresenter;
import com.btd.library.base.util.LogUtils;
import com.btd.wallet.http.BaseHttpCallback;
import com.btd.wallet.mvp.model.ActivateReq;
import com.btd.wallet.mvp.model.NullResp;
import com.btd.wallet.mvp.model.wallet.WalletConfig;
import com.btd.wallet.mvp.service.UserServiceImpl;
import com.btd.wallet.pure.R;
import com.btd.wallet.config.WalletType;

import com.btd.wallet.manager.hdtsdk.ConnectCallBack;
import com.btd.wallet.manager.hdtsdk.HDTManage;

import com.btd.wallet.mvp.contract.wallet.CreateContract;

import com.btd.wallet.utils.MethodUtils;
import com.btd.wallet.utils.StringUtils;
import com.peersafe.hdtsdk.api.WalletInfo;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Description:  创建钱包  <br>
 * Author: cxh <br>
 * Date: 2019/7/2 11:20
 */
public class CreatePresenter extends BasePresenter<CreateContract.IView>
        implements CreateContract.IPresenter {

    private UserServiceImpl service;
    private HDTManage mHDTManage = HDTManage.getInstance();
    /*本地的米袋数量*/
    private int mWalletCount;
    private WalletInfo mWallet;
    private String mNickName;
    private int mNickNameNum;
    private String mPwd;

    public CreatePresenter(Activity activity, CreateContract.IView iv) {
        super(activity, iv);
    }

    @Override
    public void initView() {
        super.initView();
    }

    @Override
    public void initData() {
        super.initData();
        /*看是本地存储的第几个用户*/
        List<WalletConfig> configs = DataSupport.findAll(WalletConfig.class);
        mWalletCount = configs == null ? 0 : configs.size();
        mNickNameNum = mWalletCount + 1;
        mNickName = MethodUtils.getString(R.string.wallet_account_name) + mNickNameNum;
        createNickName(configs);
    }

    @Override
    public void btnCreate() {
        getView().creatting();
        String pwd = getView().getEt().getText().toString();
        String confirmPwd = getView().getEtAgain().getText().toString();
        getView().getBtnCreate().setEnabled(false);
        //1.输入框不能为空
        if (TextUtils.isEmpty(pwd) || TextUtils.isEmpty(confirmPwd)) {
            resetBtnAndToast(MethodUtils.getString(R.string.wallet_toast_pwd_empty));
            return;
        }
        //3.输入需要一致
        if (!pwd.equals(confirmPwd)) {
            resetBtnAndToast(MethodUtils.getString(R.string.wallet_toast_difference));
            return;
        }
        //4.输入是否大于8个数字
        if (pwd.length() < 8) {
            resetBtnAndToast(MethodUtils.getString(R.string.wallet_toast_not_subter_8));
            return;
        }
        mPwd = pwd;
        if(mHDTManage.isConnected()){
            createWallet();
        }else{
            mHDTManage.setSdkConnectedWithBlock(mActivity, true, new ConnectCallBack() {
                @Override
                public void hasConnect(boolean connect) {
                    if(connect){
                        createWallet();
                    }else{
                       resetBtnAndToast(MethodUtils.getString(R.string.connect_fail));
                    }
                }
            });
        }

    }

    private void createWallet() {
    //    mWallet = mHDTManage.generateWallet();
       mWallet=mHDTManage.generateWalletByWord(mActivity);
        if (mWallet == null || TextUtils.isEmpty(mWallet.toString())) {
            LogUtils.i(" 创建米袋失败");
            resetBtnAndToast(MethodUtils.getString(R.string.wallet_create_dialog_failed));
        } else {
            //创建成功，使用md5加密保存公私钥地址到本地文件，可以导出和导入
            LogUtils.i("地址：" + mWallet.getWalletAddr() + "私钥：" + mWallet.getPrivateKey()
                    + "公钥：" + mWallet.getPublicKey());
            activeWallet(mWallet.getWalletAddr());
        }
    }

    /**
     * 此处需要先进行激活，调用后台激活接口，
     * <p>
     * 成功后（1）进行先查询有没有系统币（2）然后再信任
     */
    private void activeWallet(String addr) {
        mHDTManage.setSdkConnected();
        ActivateReq req = new ActivateReq();
        req.setAddress(addr);
        if (service == null) {
            service = new UserServiceImpl();
        }
        service.activateWallet(req, new BaseHttpCallback<NullResp>() {
            @Override
            public void onSuccess(NullResp nullResp) {
                super.onSuccess(nullResp);
                if(canUsePresenter()){
                    if (mHDTManage.isConnected()) {
                        getSysCoinBalance();
                    } else {
                        resetBtnAndToast(MethodUtils.getString(R.string.timeout));
                    }
                }
            }
            @Override
            public void onError(Throwable e) {
                super.onError(e);
                if (canUsePresenter()) {
                    resetBtnAndToast(MethodUtils.getString(R.string.timeout));
                }
            }
            @Override
            public void onHttpFail(int code, String data, String info) {
                super.onHttpFail(code, data, info);
                if (canUsePresenter()) {
                    resetBtnAndToast(info);
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });
    }

    /**
     * 获取账户的系统币
     */
    private void getSysCoinBalance() {
        mHDTManage.getSysCoinBalance(mWallet.getWalletAddr(), (i, s, balance) -> {
            LogUtils.i("i : " + i + ", balance: " + balance);
            if(canUsePresenter()){
                if (!"0".equals(balance)) {
                    //如果系统币大于0则表示米袋激活成功，进行sdk信任
                    mHDTManage.trustIssueCurrency(mWallet.getPrivateKey(), (i1, s1, info) -> {
                        if(canUsePresenter()){
                            if (i1 == 0) {
                                toSuccessFragment();
                            } else {
                                resetBtnAndToast(MethodUtils.getString(R.string.wallet_create_dialog_failed));
                            }
                        }
                    });
                } else {
                    resetBtnAndToast(MethodUtils.getString(R.string.wallet_create_dialog_failed));
                }
            }
        });
    }

    /**
     * 激活米袋成功后，把数据保存到本地，然后跳转页面
     */
    private void toSuccessFragment() {
        saveToLocal(mNickName
                , mWallet.getWalletAddr()
                , StringUtils.enCodePrivate(mWallet.getPrivateKey(), mPwd)
                , mWallet.getPublicKey(),mWallet.getWords());
        if (isMainThread()) {
            getView().success(mWallet.getWalletAddr(), mWallet.getPrivateKey(),mWallet.getWords());
        } else {
            HDTManage.getHandler().post(() -> getView().success(mWallet.getWalletAddr(), mWallet.getPrivateKey(),mWallet.getWords()));
        }
    }

    /**
     * 把创建或者导入的米袋账户基本信息保存到本地
     *
     * @param nickName   昵称
     * @param fromAddr   地址
     * @param privateKey 加密后的私钥
     * @param publicKey  公钥
     */
    private void saveToLocal(String nickName, String fromAddr, String privateKey, String publicKey,List<String>words) {
        WalletConfig config = new WalletConfig();
        config.nickName = nickName;
        //只是保存要生成二维码的字符串
        config.code = WalletType.strWalletPre + fromAddr;
        config.balanceHdt = "0";
        config.balanceBtd = "0";
        config.fromAddr = fromAddr;
        config.privateKey = privateKey;
        config.publicKey = publicKey;
        //默认空字符串
        config.words="";
        if(words!=null&&words.size()!=0){
            StringBuffer buffer=new StringBuffer();
            for(int i=0;i<words.size();i++){
                buffer.append(words.get(i));
                if(i!=words.size()-1){
                    buffer.append(" ");
                }
            }
            config.words=StringUtils.enCodePrivate(buffer.toString(), mPwd);
        }

        config.save();
    }

    /**
     * 恢复默认
     */
    private void resetBtnAndToast(String toast) {
        if (isMainThread()) {
            resetBtnAndToastMainThread(toast);
        } else {
            HDTManage.getHandler().post(() -> resetBtnAndToastMainThread(toast));
        }
    }

    private void resetBtnAndToastMainThread(String toast) {
        getView().showToast(toast);
        getView().getBtnCreate().setEnabled(true);
        getView().createFail();
    }

    /**
     * 默认取名字
     */
    private void createNickName(List<WalletConfig> configs) {
        for (int i = 0; i < mWalletCount; i++) {
            if (mNickName.equals(configs.get(i).nickName)) {
                mNickName = MethodUtils.getString(R.string.wallet_account_name) + (mNickNameNum++);
                createNickName(configs);
                break;
            }
        }
    }

    private boolean isMainThread() {
        return Looper.getMainLooper() == Looper.myLooper();
    }
}
