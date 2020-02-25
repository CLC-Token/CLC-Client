package com.btd.wallet.mvp.presenter.wallet;

import android.app.Activity;
import android.text.TextUtils;

import com.blankj.utilcode.util.SPUtils;
import com.btd.library.base.mvp.persenter.BasePresenter;
import com.btd.library.base.util.LogUtils;
import com.btd.wallet.mvp.model.wallet.WalletConfig;
import com.btd.wallet.mvp.service.UserServiceImpl;
import com.btd.wallet.pure.R;
import com.btd.wallet.config.IntentKeys;
import com.btd.wallet.config.SPKeys;
import com.btd.wallet.config.WalletType;
import com.btd.wallet.core.WorkApp;
import com.btd.wallet.manager.Task;
import com.btd.wallet.manager.TaskManage;
import com.btd.wallet.manager.hdtsdk.ConnectCallBack;
import com.btd.wallet.manager.hdtsdk.HDTManage;

import com.btd.wallet.mvp.contract.wallet.ImportContract;

import com.btd.wallet.utils.MethodUtils;
import com.btd.wallet.utils.PDialogUtil;
import com.btd.wallet.utils.StringUtils;
import com.peersafe.hdtsdk.api.BalanceListInfo;
import com.peersafe.hdtsdk.api.BalanceListInfoCallback;
import com.peersafe.hdtsdk.api.WalletInfo;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Description:  导入钱包  <br>
 * Author: cxh <br>
 * Date: 2019/7/2 11:20
 */
public class ImportPresenter extends BasePresenter<ImportContract.IView>
        implements ImportContract.IPresenter {

    private UserServiceImpl service;
    private HDTManage mHDTManage = HDTManage.getInstance();
    /*本地的米袋数量*/
    private int mWalletCount;
    private WalletConfig mConfig;
    private WalletInfo mWallet;
    private String mNickName;
    private int mNickNameNum;
    private String mPwd;
    public static final int CONTRACT = 1;
    private int mType;

    public ImportPresenter(Activity activity, ImportContract.IView iv) {
        super(activity, iv);
    }

    @Override
    public void initView() {
        super.initView();
        if(mBundle != null){
            mType = mBundle.getInt(IntentKeys.TYPE);
        }
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

    /**
     * 把创建或者导入的米袋账户基本信息保存到本地
     *
     * @param nickName   昵称
     * @param fromAddr   地址
     * @param privateKey 加密后的私钥
     * @param publicKey  公钥
     */
    private void saveToLocal(String nickName, String fromAddr, String privateKey, String publicKey,List<String> words) {
        mConfig = new WalletConfig();
        mConfig.nickName = nickName;
        //只是保存要生成二维码的字符串
        mConfig.code = WalletType.strWalletPre + fromAddr;
        mConfig.balanceHdt = "0";
        mConfig.balanceBtd = "0";
        if(balanceListInfo!=null){
            mConfig.setBalanceBtd(balanceListInfo.getBalanceBtd());
            mConfig.setBalanceHdt(balanceListInfo.getBalanceHdt());
            mConfig.setFreezeBtd(balanceListInfo.isFreezePeerBtd());
            mConfig.setFreezeHdt(balanceListInfo.isFreezePeerHdt());
        }
        mConfig.fromAddr = fromAddr;
        mConfig.privateKey = privateKey;
        mConfig.publicKey = publicKey;
        mConfig.words="";
        if(words!=null&&words.size()!=0){
            StringBuffer buffer=new StringBuffer();
            for(int i=0;i<words.size();i++){
                buffer.append(words.get(i));
                if(i!=words.size()-1){
                    buffer.append(" ");
                }
            }

            mConfig.words=StringUtils.enCodePrivate(buffer.toString() , mPwd);

        }
        mConfig.save();

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

    @Override
    public void btnOk() {

        getView().getBtnOk().setEnabled(false);

        String privateKey = getView().getEtPrivate().getText().toString().trim().replaceAll(" ","");
        if(mView.currentType()==0){
            if(StringUtils.isEmpty(privateKey)){
                resetBtnAndToast(MethodUtils.getString(R.string.wallet_private_key_hint));
                return ;
            }
            if(mHDTManage.isConnected()){
                btnOkDoing(privateKey);
            }else{
                mHDTManage.setSdkConnectedWithBlock(mActivity, true, new ConnectCallBack() {
                    @Override
                    public void hasConnect(boolean connect) {
                        if(connect){
                            btnOkDoing(privateKey);
                        }
                    }
                });
            }
        }else{
           String[] words= getView().getEtPrivate().getText().toString().split(" ");
           if(words.length!=12){
               resetBtnAndToast(MethodUtils.getString(R.string.word_import_tips));
               return;
           }
            if(mHDTManage.isConnected()){
                btnOkDoingByWord(getView().getEtPrivate().getText().toString());
            }else{
                mHDTManage.setSdkConnectedWithBlock(mActivity, true, new ConnectCallBack() {
                    @Override
                    public void hasConnect(boolean connect) {
                        if(connect){
                            btnOkDoingByWord(getView().getEtPrivate().getText().toString());
                        }
                    }
                });
            }
        }


    }

    private void btnOkDoingByWord(String word){
        PDialogUtil.startProgress(mActivity);
        //获取通过私钥获取地址
        mWallet = mHDTManage.importByWord(word);
        if(mWallet == null){
            resetBtnAndToast(MethodUtils.getString(R.string.wallet_scret_error));
            return ;
        }

        //判断本地是否存在
        if(DataSupport.where("fromAddr = ?",mWallet.getWalletAddr()).find(WalletConfig.class).size() > 0){
            resetBtnAndToast(MethodUtils.getString(R.string.phone_is_exist));
            return;
        }

        String pwd = getView().getEt().getText().toString();
        String confirmPwd = getView().getEtAgain().getText().toString();
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
        getBalance();
    }

    private void btnOkDoing(String privateKey){
        PDialogUtil.startProgress(mActivity);
        //获取通过私钥获取地址
        mWallet = mHDTManage.generateWallet(privateKey);
        if(mWallet == null){
            resetBtnAndToast(MethodUtils.getString(R.string.wallet_scret_error));
            return ;
        }

        //判断本地是否存在
        if(DataSupport.where("fromAddr = ?",mWallet.getWalletAddr()).find(WalletConfig.class).size() > 0){
            resetBtnAndToast(MethodUtils.getString(R.string.phone_is_exist));
            return;
        }

        String pwd = getView().getEt().getText().toString();
        String confirmPwd = getView().getEtAgain().getText().toString();
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
        getBalance();
    }
    BalanceListInfo balanceListInfo;

    private void getBalance() {
        mHDTManage.getIssueCurrencyBalanceList(mWallet.getWalletAddr(), new BalanceListInfoCallback() {
            @Override
            public void balanceInfo(int i, String s, BalanceListInfo info) {

                balanceListInfo=info;
                TaskManage.doTask(new Task<Boolean>(false) {
                    @Override
                    public void ioThread() {
                        if (i == 0 || i == 3) {
                            if (info != null) {
                                setT(false);
                            } else {
                                setT(true);
                            }
                        } else {
                            setT(true);
                        }
                    }

                    @Override
                    public void uiThread(Boolean error) {
                        if (error) {
                            resetBtnAndToast(MethodUtils.getString(R.string.wallet_create_import_fail));
                        } else {
                            vailScret();
                        }
                    }
                });
            }
        });
    }

    private void vailScret(){
        if(mWallet != null){
            LogUtils.i(mWallet.toString());
            saveToLocal(mNickName ,mWallet.getWalletAddr() ,
                    StringUtils.enCodePrivate(mWallet.getPrivateKey() , mPwd) , mWallet.getPublicKey(),mWallet.getWords());
            PDialogUtil.stopProgress();
            getView().showToast(MethodUtils.getString(R.string.wallet_create_import_success));
            SPUtils.getInstance(SPKeys.wallet).put(SPKeys.isWalletFirst, false);
            WorkApp.workApp.visibleReFresh.setWalletMe(true);
            PDialogUtil.stopProgress();
            if(mType == CONTRACT){
                WorkApp.workApp.visibleReFresh.setContractDetail(true);
                getView().finish(false);
            }else {
                getView().finish(true);
            }
        }else{
            resetBtnAndToast(MethodUtils.getString(R.string.wallet_scret_error));
        }

    }

    /**
     * 恢复默认
     */
    private void resetBtnAndToast(String toast) {
        getView().showToast(toast);
        PDialogUtil.stopProgress();
        getView().getBtnOk().setEnabled(true);
    }
}
