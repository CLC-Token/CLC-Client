package com.btd.wallet.manager.hdtsdk;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.btd.library.base.util.LogUtils;

import com.btd.wallet.mvp.model.wallet.WalletEvent;
import com.btd.wallet.pure.BuildConfig;
import com.btd.wallet.pure.R;
import com.btd.wallet.config.Constants;
import com.btd.wallet.config.WalletType;
import com.btd.wallet.event.OfferWarning;

import com.btd.wallet.utils.MethodUtils;
import com.btd.wallet.utils.PDialogUtil;
import com.btd.wallet.utils.SecureRandomUtils;
import com.google.common.base.Preconditions;
import com.google.common.base.Stopwatch;
import com.peersafe.base.crypto.ecdsa.Seed;
import com.peersafe.hdtsdk.api.AccountTransactionCallback;
import com.peersafe.hdtsdk.api.BalanceInfoCallback;
import com.peersafe.hdtsdk.api.BalanceListInfo;
import com.peersafe.hdtsdk.api.BalanceListInfoCallback;
import com.peersafe.hdtsdk.api.CommonTransInfo;
import com.peersafe.hdtsdk.api.CommonTransactionCallback;
import com.peersafe.hdtsdk.api.ConnectDelegate;
import com.peersafe.hdtsdk.api.CurrencyTxDetails;
import com.peersafe.hdtsdk.api.CurrencyTxsInfoCallback;
import com.peersafe.hdtsdk.api.HDTSdkApi;
import com.peersafe.hdtsdk.api.ReceiveInfo;
import com.peersafe.hdtsdk.api.ReceiveListCallback;
import com.peersafe.hdtsdk.api.SubscribeResultCallback;
import com.peersafe.hdtsdk.api.TransferFeeCallback;
import com.peersafe.hdtsdk.api.TransferInfo;
import com.peersafe.hdtsdk.api.WalletInfo;
import com.peersafe.hdtsdk.inner.ZXWalletManager;

import org.bitcoinj.core.Utils;
import org.bitcoinj.crypto.MnemonicCode;
import org.bitcoinj.crypto.PBKDF2SHA512;
import org.greenrobot.eventbus.EventBus;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


/**
 * $Author: xhunmon
 * $Date: 2018-01-09
 * $Description:该管理类用来统一管理hdt SDK的管理类
 */

public class HDTManage implements ConnectDelegate {

    public static final String TAG = HDTManage.class.getSimpleName();

    //主动发起连接超时时间秒默认10秒
    public static final int ConnectTimout=10;
    //无操作超时，关闭连接时间,默认5分钟
    public static final int ControlNormal=5*60;//20*60;

    //节点的地址，CLC发行地址，连接状态回调，账户交易通知回调。
    public final static String CHAIN_SQL_NODE_ADDR ="ws://www.cifcula.com:6006";


    //CLC发行地址
    public final static String ISSUE_ADDR = BuildConfig.isRelease ?"":"";

    private HDTSdkApi mHdtSdkApi = new HDTSdkApi();
    //主动连接的回调
    private ConnectCallBack connectCallBack;
    //主动连接的判断的计时器
    Timer connectTimer=null;
    //主动连接的判断倒计时
    private int currentConnectSec=0;



    private boolean isSdkConnected = false;

    //上次操作的时间
    private long lastEventTime=0;
    //倒计时
    private int currentSec=0;
    //无操作的计时器
    Timer timer=null;
    private HDTManage() {
        setSdkConnected();
    }


    public static HDTManage getInstance() {
        return Single.sInstance;
    }

    public static Handler getHandler() {
        return HDTManage.Single.handler;
    }

    private static class Single {
        private static final HDTManage sInstance = new HDTManage();
        private static final Handler handler = new Handler();
    }


    public void setSdkConnected() {

                LogUtils.i("CHAIN_SQL_NODE_ADDR: " + CHAIN_SQL_NODE_ADDR);
                LogUtils.i("ISSUE_ADDR: " + ISSUE_ADDR);
                if (!isConnected()) {
                    //1. 初始化sdk，与账户无关

                    mHdtSdkApi.sdkInit(CHAIN_SQL_NODE_ADDR, ISSUE_ADDR, HDTManage.this);
                }



    }

    public void setSdkConnectedWithBlock(Context context,Boolean showTip, ConnectCallBack callBack){

        if(!isConnected()){
          //  MethodUtils.showToast(context,"正在连接钱包");
            if(showTip){
                PDialogUtil.startProgress(context,"",true);
            }

            connectCallBack=callBack;

            if(connectTimer!=null){
                connectTimer.cancel();
                connectTimer=null;
            }
            currentConnectSec=0;
            connectTimer=new Timer();
            connectTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    currentConnectSec++;
                    if(currentConnectSec==ConnectTimout){
                        connectTimer.cancel();
                        connectTimer=null;

                        getHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                LogUtils.e("连接超时返回handler");
                                sdkClose();
                                if(showTip){
                                    PDialogUtil.stopProgress();
                                    MethodUtils.showToast(context,context.getString(R.string.connect_fail));
                                }
                                if(connectCallBack!=null){
                                    connectCallBack.hasConnect(false);
                                    connectCallBack=null;
                                }


                            }
                        });


                    }
                }
            },0,1000);

            setSdkConnected();


        }else{
            callBack.hasConnect(true);
        }
    }

    /**
     * 更新触发sdk的时间，并重置倒计时的时间
     */
    private synchronized void resetEventTime(){

        if(timer!=null){
            timer.cancel();
            timer= null;
        }
        lastEventTime=System.currentTimeMillis();
        currentSec=0;
        timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                currentSec++;
                if(currentSec==ControlNormal){
                    getHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            LogUtils.e("关闭链");
                            timer.cancel();
                            timer=null;
                            sdkClose();
                        }
                    });

                }
            }
        },1000,1000);
    }

    /**
     * 2. 创建米袋，客户会得到一个米袋地址
     */
    public WalletInfo generateWallet() {
        resetEventTime();
        return mHdtSdkApi.generateWallet();
    }


    public WalletInfo generateWalletByWord(Context context){

        List<String> words=new ArrayList<>();
        try{
            MnemonicCode mnemonicCode = new MnemonicCode(context.getAssets().open("english.txt"), null);
            SecureRandom secureRandom = SecureRandomUtils.secureRandom();
            byte[] initialEntropy = new byte[16];//算法需要，必须是被4整除
            secureRandom.nextBytes(initialEntropy);
            List<String> wd = mnemonicCode.toMnemonic(initialEntropy);
            if (wd == null || wd.size() != 12)
                throw new RuntimeException("generate word error");
            else {
                words.clear();
                words.addAll(wd);
                LogUtils.d(words.toString());
            }
        }catch (Exception e){

        }

        String passphrase="";
        Preconditions.checkNotNull(passphrase, "A null passphrase is not allowed.");
        String pass = Utils.SPACE_JOINER.join(words);
        String salt = "mnemonic" + passphrase;
        Stopwatch watch = Stopwatch.createStarted();
        byte[] seed = PBKDF2SHA512.derive(pass, salt, 2048, 16);
        watch.stop();


      //  byte[] seed =MnemonicCode.toSeed(words, "");
        System.out.println("seed: " + new BigInteger(1,seed).toString(16));
        Seed seed1=new Seed(seed);
        WalletInfo info=ZXWalletManager.getInstance().generateWalletByWord(seed1);
        info.setWords(words);
       return info;
    }

    public WalletInfo importByWord(String word){
        List<String> words=new ArrayList<>();
        String[] strs=word.split(" ");
        for(int i=0;i<strs.length;i++){
            words.add(strs[i]);
        }
        resetEventTime();
        String passphrase="";
        Preconditions.checkNotNull(passphrase, "A null passphrase is not allowed.");
        String pass = Utils.SPACE_JOINER.join(words);
        String salt = "mnemonic" + passphrase;
        Stopwatch watch = Stopwatch.createStarted();
        byte[] seed = PBKDF2SHA512.derive(pass, salt, 2048, 16);
        watch.stop();


        //  byte[] seed =MnemonicCode.toSeed(words, "");
        System.out.println("seed: " + new BigInteger(1,seed).toString(16));
        Seed seed1=new Seed(seed);
        WalletInfo info=ZXWalletManager.getInstance().generateWalletByWord(seed1);
        info.setWords(words);
        return info;
    }
    /**
     * 2. 创建米袋，客户会得到一个米袋地址
     * @param privateKey 私钥
     */
    public WalletInfo generateWallet(String privateKey) {
        resetEventTime();
        return mHdtSdkApi.generateWallet(privateKey);
    }

    /**
     * 3. 信任CLC（信任之后才能对CLC进行操作），在创建米袋成功之后执行
     * <p>
     * 注：信任CLC的前提是生成了米袋，并且该米袋已经激活（激活由后台发送40个系统币）
     *
     */
    public void trustIssueCurrency(String walletAddr, String privateKey) {
        resetEventTime();
        //注意只有在sdk已连接状态才可以进行信任，取余额等操作！！！
        if (isSdkConnected) {
            mHdtSdkApi.trustIssueCurrency(privateKey, new CommonTransactionCallback() {
                @Override
                public void transactionResult(int i, String s, CommonTransInfo commonTransInfo) {
                    final int code = i;
                    final String msg = s;
                    final CommonTransInfo commontrans = commonTransInfo;
                    StringBuilder message = new StringBuilder().append("code is:" + code)
                            .append("\nmessage is:" + msg).append("\nCommonTransInfo is:" + commontrans.toString());
                    LogUtils.i("信任网关结果: " + message.toString());
                    WalletEvent event = new WalletEvent();
                    event.sdkState = WalletType.SDK_TRUST;
                    event.what = WalletType.CREATE;
                    event.code = i;
                    event.messag = s;
                    EventBus.getDefault().postSticky(event);
                }
            });

            //（该流程用于注册账户通知，只有走了该流程，才可以实时的收到其他人给这个账号的转账的回调，如果只是查询，不需要调用注册）
           /* mHdtSdkApi.subscribeAccountTransaction(walletAddr, new SubscribeResultCallback() {
                @Override
                public void subscribeResult(int i, String s, int i1) {
                    Log.i(TAG, "subscribeResult code is:" + i);
                    Log.i(TAG, "subscribeResult message is:" + s);
                    Log.i(TAG, "subscribeResult result is:" + i1);

                    mHdtSdkApi.trustIssueCurrency(privateKey, new CommonTransactionCallback() {
                        @Override
                        public void transactionResult(int i, String s, CommonTransInfo commonTransInfo) {
                            final int code = i;
                            final String msg = s;
                            final CommonTransInfo commontrans = commonTransInfo;
                            StringBuilder message = new StringBuilder().append("code is:" + code)
                                    .append("\nmessage is:" + msg).append("\nCommonTransInfo is:" + commontrans.toString());
                            LogUtils.i("信任网关结果: " + message.toString());
                            WalletEvent event = new WalletEvent();
                            event.sdkState = WalletType.SDK_TRUST;
                            event.what = WalletType.CREATE;
                            event.code = i;
                            event.messag = s;
                            EventBus.getDefault().postSticky(event);
                        }
                    });
                }
            }, new AccountTransactionCallback() {//用于app上层异步接收他人转账CLC的信息
                @Override
                public void accountTransactionResult(int i, String s, TransferInfo transferInfo) {
                    LogUtils.i("accountTransactionResult code is:" + i);
                    LogUtils.i("accountTransactionResult message is:" + s);
                    LogUtils.i("accountTransactionResult transferInfo is:" + transferInfo.toString());
                }
            });*/
        } else {
            LogUtils.i("=====提醒", "sdk未完成初始化!");
        }
    }

    public void accountTransactionResult(String walletAddr) {
        resetEventTime();
        mHdtSdkApi.subscribeAccountTransaction(walletAddr, new SubscribeResultCallback() {
            @Override
            public void subscribeResult(int i, String s, int i1) {
            }
        }, new AccountTransactionCallback() {//用于app上层异步接收他人转账CLC的信息
            @Override
            public void accountTransactionResult(int i, String s, TransferInfo transferInfo) {
                LogUtils.i("accountTransactionResult code is:" + i);
                LogUtils.i("accountTransactionResult message is:" + s);
                LogUtils.i("accountTransactionResult transferInfo is:" + transferInfo.toString());
                WalletEvent event = new WalletEvent();
                event.sdkState = WalletType.SDK_RECORD_BACK;
                event.what = WalletType.DETAIL;
                event.code = i;
                if (transferInfo != null) {
                    event.balance = transferInfo.getAmount();
                }
                EventBus.getDefault().postSticky(event);
            }
        });
    }


    /**
     * 4. 切换账户，进行交易
     * 测试用已经信任的账户初始化，并且已经有CLC余额的货币
     * （该流程用于切换账户，只有走了该流程，才可以实时的收到其他人给这个账号转账的回调，如果只是查询，不需要调用注册）
     *
     * @param walletAddr
     */
    public void subscribeAccountTransaction(String walletAddr) {
        resetEventTime();
        if (isSdkConnected) {
            mHdtSdkApi.subscribeAccountTransaction(walletAddr, new SubscribeResultCallback() {
                @Override
                public void subscribeResult(int i, String s, int i1) {
                    Log.i(TAG, "subscribeResult code is:" + i);
                    Log.i(TAG, "subscribeResult message is:" + s);
                    Log.i(TAG, "subscribeResult result is:" + i1);
                    final int result = i1;
                    LogUtils.i("注册交易通知结果: " + result);
                }
            }, new AccountTransactionCallback() {
                @Override
                public void accountTransactionResult(int i, String s, TransferInfo transferInfo) {
                    Log.i(TAG, "accountTransactionResult code is:" + i);
                    Log.i(TAG, "accountTransactionResult message is:" + s);
                    Log.i(TAG, "accountTransactionResult transferInfo is:" + transferInfo.toString());
                    final TransferInfo transInfo = transferInfo;
                    LogUtils.i("收到CLC转入 信息: " + transInfo.toString());
                }
            });
        }
    }


    /*注意:下面几个操作之前，请保证sdk已经初始化成功，
        下面查询接口demo使用的米袋是已经信任的账户，可以修改米袋地址，查询任意账户的相关信息*/

    /**
     * 查询系统余额
     *
     * @param walletAddr
     */
    public void getSysCoinBalance(String walletAddr) {
        resetEventTime();
        mHdtSdkApi.getSysCoinBalance(walletAddr, new BalanceInfoCallback() {
            @Override
            public void balanceInfo(int i, String s, String s1) {
                Log.i(TAG, "code is:" + i);
                Log.i(TAG, "message is:" + s);
                Log.i(TAG, "balance is:" + s1);
                WalletEvent event = new WalletEvent();
                event.sdkState = WalletType.SDK_SYSTEM;
                event.what = WalletType.CREATE;
                event.code = i;
                event.messag = s;
                event.balance = s1;
                EventBus.getDefault().postSticky(event);
            }
        });
    }


    /**
     * 获取CLC余额
     * （获取CLC不用进行信任切换）
     *  注意：需要确认查询哪一张币的余额
     */
    public void getIssueCurrencyBalanceBTP(String walletAddr, int fromWath) {
        resetEventTime();
        getIssueCurrencyBalanceToSdk(HDTSdkApi.CoinType.CLC,walletAddr,fromWath);
    }













    /**
     * 判断地址是否合法
     * @param address  地址
     */
    public boolean isLegalAddress(String address){
        resetEventTime();
        return mHdtSdkApi.isLegalAddress(address);
    }




    /**
     * 查询交易明细
     * 注意：第一次传递进入的marker为空，如果一次查不完会达到一个marker；
     * 第二次查询时把marker作为参数传进去；
     *
     * @param walletAddr 账户地址
     * @param number     查询数量
     * @param marker     根据时候marker查询下一批的交易明细
     */
    public void getIssueCurrencyTxDetail(String walletAddr, int number, String marker) {
        resetEventTime();
        getIssueCurrencyTxDetailToSdk(walletAddr, number, marker);
    }

    /**
     * 监听余额的变化
     * @param walletAddrs  监听的地址队列
     * @param isLisenOffer  是否连挂单也一起监听
     */
    public void subscribeBoth(List<String> walletAddrs, boolean isLisenOffer, int fromWath) {
        resetEventTime();
        subscribeToSdk(HDTSdkApi.CoinType.CLC, walletAddrs, isLisenOffer,fromWath, true);
    }




    /**
     * 获取转账手续费
     */
    public void getTransferFee() {
        mHdtSdkApi.getTransferFee(new TransferFeeCallback() {
            @Override
            public void transferFeeInfo(int i, String s, String s1) {
                resetEventTime();
                final StringBuilder stringBuilder = new StringBuilder("code:" + i).append("\nmessage:" + s);
                stringBuilder.append("\nTransferFee is:" + s1);
                WalletEvent event = new WalletEvent();
                event.what = WalletType.TRANSFER;
                event.sdkState = WalletType.SDK_FEE;
                event.code = i;
                event.messag = s1;
                EventBus.getDefault().postSticky(event);
            }
        });
    }


    @Override
    public void connectState(int i) {
        if (CONNECT_SUCCESS == i) {
            isSdkConnected = true;
        } else {
            isSdkConnected = false;
        }
        LogUtils.i("!!!connectState:" + i + ", isConnected: " + isSdkConnected);
        notify(isSdkConnected);
        WalletEvent event = new WalletEvent();
//        event.what = WalletType.ME;
//        event.what = WalletType.CREATE;
        event.sdkState = WalletType.SDK_INIT;
        event.isConnected = isSdkConnected;
        EventBus.getDefault().postSticky(event);
    }

    /**
     * 判断当前sdk是否已经开启
     *
     * @return
     */
    public boolean isConnected() {
        int state = mHdtSdkApi.getConnectState();
        if (CONNECT_SUCCESS == state) {
            isSdkConnected = true;
        } else {
            isSdkConnected = false;
        }
        LogUtils.i("==isSdkConnected: " + isSdkConnected);
        return isSdkConnected;
    }

    /**
     * 关闭sdk
     */
    public void sdkClose() {
        LogUtils.i("==sdkClose:断开连接 " );
       // if (mHdtSdkApi.getConnectState() != -1) {//-1为断开，1为连接中，0为开启
            mHdtSdkApi.sdkClose();
        //}
    }


    //统一封装获取余额总接口
    private void getIssueCurrencyBalanceToSdk(HDTSdkApi.CoinType type, String walletAddr, int fromWath){
        resetEventTime();
        mHdtSdkApi.getIssueCurrencyBalance(type,walletAddr, new BalanceInfoCallback() {
            @Override
            public void balanceInfo(int i, String s, String s1) {
                LogUtils.i("code is:" + i);
                LogUtils.i("message is:" + s);
                LogUtils.i("balance is:" + s1);

                final StringBuilder message = new StringBuilder().append("code:" + i)
                        .append("\nmessag:" + s).append("\nbalance:" + s1);
                LogUtils.i("CLC余额信息", message.toString());
                WalletEvent event = new WalletEvent();
                event.sdkState = WalletType.SDK_BALANCE;
                event.what = fromWath;
                event.code = i;
                event.messag = s;
                event.balance = s1;
                event.fromAddr = walletAddr;
                EventBus.getDefault().postSticky(event);
            }
        });
    }

    /**
     * 同时获取ist和hdt的账户余额
     */
    public void getIssueCurrencyBalanceList(String walletAddr, int fromWath){
        resetEventTime();
        mHdtSdkApi.getIssueCurrencyBalanceList(walletAddr, new BalanceListInfoCallback() {

            @Override
            public void balanceInfo(int i, String s, BalanceListInfo s1) {
                LogUtils.i("code is:" + i);
                LogUtils.i("message is:" + s);
                LogUtils.i("balance is:" + s1);

                final StringBuilder message = new StringBuilder().append("code:" + i)
                        .append("\nmessag:" + s).append("\nbalance:" + s1);
                LogUtils.i("CLC余额信息", message.toString());
                WalletEvent event = new WalletEvent();
                event.sdkState = WalletType.SDK_BALANCE;
                event.what = fromWath;
                event.code = i;
                event.messag = s;
                event.balanceBtd = s1.getBalanceBtd();
                event.balanceHdt = s1.getBalanceHdt();
                event.isFreezeBtd = s1.isFreezePeerBtd();
                event.isFreezeHdt = s1.isFreezePeerHdt();
                s1.isFreezePeerBtd();
                event.fromAddr = walletAddr;
                EventBus.getDefault().postSticky(event);
            }
        });
    }

    //统一封装转账总接口
    private void transferCurrencyToSdk(HDTSdkApi.CoinType coinType, String privateKey, String toAddr, String amount, String remark, String type){
        resetEventTime();
        mHdtSdkApi.transferCurrency(coinType, privateKey, toAddr, amount, remark, type,
                (i, s, transferInfo) -> {
                    LogUtils.i("transferCurrency，accountTransactionResult code is:" + i);
                    LogUtils.i("transferCurrency，accountTransactionResult message is:" + s);
                    LogUtils.i("transferCurrency，accountTransactionResult transferInfo is:" + transferInfo.toString());
                    final StringBuilder message = new StringBuilder().append("code:" + i)
                            .append("\nmessage:" + s).append("\ntransferInfo:" + transferInfo.toString());
                    LogUtils.i("CLC余额信息", message.toString());
                    WalletEvent event = new WalletEvent();
                    event.sdkState = WalletType.SDK_TRANSFER;
                    event.what = WalletType.TRANSFER;
                    event.code = i;
                    event.tradeHash = transferInfo != null ? transferInfo.getTxId() : "";
                    EventBus.getDefault().postSticky(event);
                });
    }

    //统一封装生成挂单总接口(takerGet是你要卖的币，takerPay是你要买的币。这个理解上会有点绕)
    private void offerCreateToSdk(HDTSdkApi.CoinType coinType, String privateKey, String payAmount
            , String getAmount, String remark , String type){
        resetEventTime();
        mHdtSdkApi.offerCreate(coinType, privateKey, payAmount
                , getAmount, remark, type,
                (i, s, offerInfo) -> {
                    LogUtils.i("offerCreateToSdk，offerResult code is:" + i);
                    LogUtils.i("offerCreateToSdk，offerResult message is:" + s);
                    LogUtils.i("offerCreateToSdk，offerResult offerInfo is:" + offerInfo.toString());
                    //Sequence字段作为取消时的id
                    WalletEvent event = new WalletEvent();
                    event.sdkState = WalletType.SDK_OFFER_CREATE;
                    event.code = i;
                    event.tradeHash = offerInfo != null ? offerInfo.getTxId() : "";
                    EventBus.getDefault().postSticky(event);
                });
    }

    //统一封装取消挂单总接口
    private void offerCancelToSdk(HDTSdkApi.CoinType coinType, String privateKey, String remark , String type , long sequence){
        resetEventTime();
        mHdtSdkApi.offerCancel(coinType, privateKey, remark, type,sequence,
                (i, s, offerInfo) -> {
                    LogUtils.i("offerCancelToSdk，offerResult code is:" + i);
                    LogUtils.i("offerCancelToSdk，offerResult message is:" + s);
                    LogUtils.i("offerCancelToSdk，offerResult offerInfo is:" + offerInfo.toString());
                    WalletEvent event = new WalletEvent();
                    event.sdkState = WalletType.SDK_OFFER_CREATE;
                    event.code = i;
                    event.tradeHash = offerInfo != null ? offerInfo.getTxId() : "";
                    EventBus.getDefault().postSticky(event);
                });
    }

    /**
     * 统一封装查询挂单总接口
     * @param walletAddr    账户地址
     * @param limit         一次性查询的批量（10-400）
     * @param marker        第一次marker为空，下一批次填回调的
     */
    public void accountOffers(String walletAddr, int limit, String marker){
        resetEventTime();
        mHdtSdkApi.accountOffers(walletAddr,limit,marker,
                (i, s, offerInfo) -> {
                    LogUtils.i("accountOffers，offerResult code is:" + i);
                    LogUtils.i("accountOffers，offerResult message is:" + s);
                    LogUtils.i("accountOffers，offerResult offerInfo is:" + offerInfo.toString());
                    WalletEvent event = new WalletEvent();
                    event.sdkState = WalletType.SDK_OFFER_BOOKS;
                    event.code = i;
                    EventBus.getDefault().postSticky(event);
                });
    }

    //统一封装获取明细的接口
    private void getIssueCurrencyTxDetailToSdk(String walletAddr, int number, String marker) {
        resetEventTime();
        LogUtils.i("walletAddr: " + walletAddr + ", number:" + number + ", marker: " + marker);
        mHdtSdkApi.getIssueCurrencyTxDetail(walletAddr, number, marker, new CurrencyTxsInfoCallback() {
            @Override
            public void currencyTxsInfo(int i, String s, CurrencyTxDetails details) {
                WalletEvent event = new WalletEvent();
                event.what = WalletType.RECORD;
                event.sdkState = WalletType.SDK_RECORD;
                event.code = i;
                if (null != details) {
                    event.details = details;
                }
                if (null != details && details.getMarker() != null) {
                    event.marker = details.getMarker();
                }
                LogUtils.i("currencyTxsInfo i: " + i);
                EventBus.getDefault().postSticky(event);
            }
        });
    }

    //统一封装监听账户余额变化队列接口
    private void subscribeToSdk(HDTSdkApi.CoinType coinType, List<String> walletAddrs, boolean isLisenOffer, int fromWath, boolean both){
        resetEventTime();
        mHdtSdkApi.subscribe(coinType, walletAddrs, isLisenOffer,both, new SubscribeResultCallback() {
            @Override
            public void subscribeResult(int code, String message, int result) {
                Log.i(TAG, "subscribeResult code is:" + code);
                Log.i(TAG, "subscribeResult message is:" + message);
                Log.i(TAG, "subscribeResult result is:" + result);
                //如果code == 0 则表示成功建立起监听
            }
        }, new ReceiveListCallback() {
            @Override
            public void receiveListCallback(int code, String message, ReceiveInfo receiveInfo) {
                //监听的返回
                Log.i(TAG, "subscribeResult code is:" + code);
                Log.i(TAG, "subscribeResult message is:" + message);
                if(receiveInfo != null){
                    Log.i(TAG,"subscribeResult receiveInfo type is:"+receiveInfo.getType());
                    Log.i(TAG,"subscribeResult receiveInfo currencyType is:"+receiveInfo.getCurrencyType());
                    Log.i(TAG,"subscribeResult receiveInfo txId is:"+receiveInfo.getTxId());
                    if(receiveInfo.getType() == HDTSdkApi.OFFER_TYPE_TRANSFER_IN){
                        Log.i(TAG,"subscribeResult receiveInfo getOfferAddr is:"+receiveInfo.getOfferAddr());
                        boolean isWarming = false;
                        ArrayList<String> warningAddrs = new ArrayList<>();
                        for(String addr : receiveInfo.getOtherOfferAddrs()){
                            Log.i(TAG,"subscribeResult receiveInfo getOtherOfferAddrs is:"+addr);
                            if(!addr.equals(Constants.EXCHANGE_ACCOUNT)){
                                isWarming = true;
                                warningAddrs.add(addr);
                            }
                        }
                        WalletEvent event = new WalletEvent();
                        event.sdkState = WalletType.SDK_RECORD_BACK;
                        event.what = fromWath;
                        event.code = code;
                        event.messag = message;
                        event.fromAddr = receiveInfo.getOfferAddr();
                        EventBus.getDefault().postSticky(event);
                        if(isWarming){
                            EventBus.getDefault().postSticky(new OfferWarning(warningAddrs));
                        }
                    }else if(receiveInfo.getType() == HDTSdkApi.TRANS_TYPE_TRANSFER_IN){
                        Log.i(TAG,"subscribeResult receiveInfo getFromAddr is:"+receiveInfo.getFromAddr());
                        Log.i(TAG,"subscribeResult receiveInfo getToAddr is:"+receiveInfo.getToAddr());
                        WalletEvent event = new WalletEvent();
                        event.sdkState = WalletType.SDK_RECORD_BACK;
                        event.what = fromWath;
                        event.code = code;
                        event.messag = message;
                        event.balance = receiveInfo.getAmount();
                        event.fromAddr = receiveInfo.getFromAddr();
                        event.toAddr = receiveInfo.getToAddr();
                        EventBus.getDefault().postSticky(event);
                    }
                }
            }
        });
    }

    /*****************************直接使用回调处理*****************************/
    /**
     * 获取转账手续费
     */
    public void getTransferFee(TransferFeeCallback callback) {
        resetEventTime();
        mHdtSdkApi.getTransferFee(callback);
    }



    /**
     * 获取CLC余额
     * （获取CLC不用进行信任切换）
     *  注意：需要确认查询哪一张币的余额
     */
    public void getIssueCurrencyBalance(HDTSdkApi.CoinType type, String walletAddr, BalanceInfoCallback callback) {
        resetEventTime();
        mHdtSdkApi.getIssueCurrencyBalance(type,walletAddr,callback);
    }

    /**
     * 同时获取ist和hdt的账户余额
     */
    public void getIssueCurrencyBalanceList(String walletAddr, BalanceListInfoCallback callback){
        resetEventTime();
        BalanceListInfoCallback balanceListInfoCallback = new BalanceListInfoCallback() {
            @Override
            public void balanceInfo(int code, String message, BalanceListInfo info) {
                if(callback != null){
                    getHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            callback.balanceInfo(code,message,info);
                        }
                    });
                }
            }
        };
        mHdtSdkApi.getIssueCurrencyBalanceList(walletAddr, balanceListInfoCallback);
    }

    /**
     * 查询系统余额
     */
    public void getSysCoinBalance(String walletAddr, BalanceInfoCallback callback) {
        resetEventTime();
        mHdtSdkApi.getSysCoinBalance(walletAddr, callback);
    }

    /**
     * 3. 信任CLC（信任之后才能对CLC进行操作），在创建米袋成功之后执行
     * <p>
     * 注：信任CLC的前提是生成了米袋，并且该米袋已经激活（激活由后台发送40个系统币）
     *
     */
    public void trustIssueCurrency(String privateKey, CommonTransactionCallback callback) {
        resetEventTime();
        //注意只有在sdk已连接状态才可以进行信任，取余额等操作！！！
        mHdtSdkApi.trustIssueCurrency(privateKey, callback);
    }

    /**
     * 回调形式转账
     */
    public void transferCurrency(HDTSdkApi.CoinType coinType, String privateKey, String toAddr
            , String amount, String remark, String type , AccountTransactionCallback callback){
        resetEventTime();
        mHdtSdkApi.transferCurrency(coinType, privateKey, toAddr, amount, remark, type, callback);
    }

    /**
     * 注册监听转账
     */
    public void subscribe(List<String> walletAddrs, ReceiveListCallback callback){
        ReceiveListCallback receiveListCallback=new ReceiveListCallback() {
            @Override
            public void receiveListCallback(int code, String message, ReceiveInfo receiveInfo) {
                if(callback != null){
                    getHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            callback.receiveListCallback(code,message,receiveInfo);
                        }
                    });
                }
            }
        };


        mHdtSdkApi.subscribe(HDTSdkApi.CoinType.CLC, walletAddrs, false,true, new SubscribeResultCallback() {
            @Override
            public void subscribeResult(int code, String message, int result) {
                //注意： 如果code == 0 则表示成功建立起监听，否则监听失败（在有必须要监听结果的地方需要判断）
                LogUtils.i("code: "+code);
            }
        }, receiveListCallback);
    }
    /*****************************直接使用回调处理*****************************/


    /*******************************处理sdk连接异常的回调*************************************/
    private List<ConnectObserver> mObservers = new ArrayList<>();

    public void attach(ConnectObserver observer){
        mObservers.add(observer);
    }

    public void detach(ConnectObserver observer){
        mObservers.remove(observer);
    }

    public  void notify(boolean connect){
        for(ConnectObserver observer : mObservers){
            observer.change(connect);
        }
        if(connect&&connectCallBack!=null){
            getHandler().post(new Runnable() {
                @Override
                public void run() {
                    LogUtils.e("连接成功后回调");
                    try{
                        PDialogUtil.stopProgress();
                        connectCallBack.hasConnect(connect);
                        if(connectTimer!=null){
                            connectTimer.cancel();
                            connectTimer=null;
                        }

                        connectCallBack=null;
                    }catch (Exception e){

                    }

                }
            });

        }
    }
}