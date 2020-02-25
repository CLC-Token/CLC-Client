package com.peersafe.hdtsdk.inner;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.peersafe.base.client.Account;
import com.peersafe.base.client.Client;
import com.peersafe.base.client.enums.Command;
import com.peersafe.base.client.pubsub.Publisher;
import com.peersafe.base.client.requests.Request;
import com.peersafe.base.client.responses.Response;
import com.peersafe.base.client.transactions.ManagedTxn;
import com.peersafe.base.client.transactions.TransactionManager;
import com.peersafe.base.config.Config;
import com.peersafe.base.core.coretypes.AccountID;
import com.peersafe.base.core.coretypes.Amount;
import com.peersafe.base.core.coretypes.Currency;
import com.peersafe.base.core.coretypes.RippleDate;
import com.peersafe.base.core.coretypes.STArray;
import com.peersafe.base.core.coretypes.uint.UInt32;
import com.peersafe.base.core.fields.Field;
import com.peersafe.base.core.serialized.enums.EngineResult;
import com.peersafe.base.core.serialized.enums.TransactionType;
import com.peersafe.base.core.types.known.tx.Transaction;
import com.peersafe.base.core.types.known.tx.result.AffectedNode;
import com.peersafe.base.core.types.known.tx.result.TransactionResult;
import com.peersafe.base.core.types.known.tx.txns.OfferCancel;
import com.peersafe.base.core.types.known.tx.txns.OfferCreate;
import com.peersafe.base.core.types.known.tx.txns.Payment;
import com.peersafe.base.core.types.known.tx.txns.TrustSet;
import com.peersafe.base.crypto.ecdsa.IKeyPair;
import com.peersafe.base.crypto.ecdsa.Seed;
import com.peersafe.chainsql.core.Chainsql;
import com.peersafe.chainsql.net.Connection;
import com.peersafe.chainsql.util.Util;
import com.peersafe.hdtsdk.api.AccountOfferCallback;
import com.peersafe.hdtsdk.api.AccountTransactionCallback;
import com.peersafe.hdtsdk.api.BalanceInfoCallback;
import com.peersafe.hdtsdk.api.BalanceListInfo;
import com.peersafe.hdtsdk.api.BalanceListInfoCallback;
import com.peersafe.hdtsdk.api.CommonTransInfo;
import com.peersafe.hdtsdk.api.CommonTransactionCallback;
import com.peersafe.hdtsdk.api.ConnectDelegate;
import com.peersafe.hdtsdk.api.CurrencyTxDetail;
import com.peersafe.hdtsdk.api.CurrencyTxDetails;
import com.peersafe.hdtsdk.api.CurrencyTxsInfoCallback;
import com.peersafe.hdtsdk.api.HDTSdkApi;
import com.peersafe.hdtsdk.api.OfferCallback;
import com.peersafe.hdtsdk.api.OfferInfo;
import com.peersafe.hdtsdk.api.ReceiveInfo;
import com.peersafe.hdtsdk.api.ReceiveListCallback;
import com.peersafe.hdtsdk.api.SubscribeResultCallback;
import com.peersafe.hdtsdk.api.TransferFeeCallback;
import com.peersafe.hdtsdk.api.TransferInfo;
import com.peersafe.hdtsdk.api.WalletInfo;
import com.peersafe.hdtsdk.log.ZXLogger;

import org.bouncycastle.crypto.digests.RIPEMD160Digest;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.nio.channels.Channel;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Copyright (C) 2017 zhangyang. All rights reserved.
 *
 * @author zhangyang
 * @Name: HDTSdkJarMake
 * @Package: com.peersafe.hdtsdk.inner
 * @Description:
 * @date 上午11:50
 */
public class ZXWalletManager {
    private static final String TAG = "HDTSDK_ZXWalletManager";

    private static final String ACCOUNT_NOT_FOUND = "actNotFound";

    private static volatile ZXWalletManager instance = null;

    // 是否已订阅账户
    private static boolean mIsSubscribeAccount = false;

    // 私钥的key值
    private static final String SECRET = "secret";

    // 账户的key值
    private static final String ACCOUNT_ID = "account_id";

    // 公钥key值
    private static final String PUBLIC_KEY = "public_key";

    // 比特米货币码
//    public static final String CURRENCY_TYPE_HDT = "HDT";
//    public static final String CURRENCY_TYPE_BTD = "BTD";
    public static final String CURRENCY_TYPE_CLC = "CLC";

    // 钱包地址
//    private String mWalletAddr;

    // chainsql节点地址
    private String mChainSqlNodeAddr;

    // 比特米发行网关
    private String mIssueAddr;

    // 连接状态回调
    private ConnectDelegate mConnectDelegate;

    // 账户交易通知回调
    private AccountTransactionCallback mAccountTransactionCallback = null;
    // 账户转账，创建挂单，取消挂单队列的监听
    private ReceiveListCallback mReceiveListCallback = null;
    private boolean isReceiveListLisenOffer = false;
    private String mCurReceiveListCurrencyType;
    //同时监听两个币种吗？
    private boolean isBoth;
    private List<String> mReceiveListWalletAddrs;
    private String mLastTxId = "";

    //转账手续费（比特米需求只要后台配置固定金额手续费）
    private String mTransferFee = "";

    //记录当前sdk是否正在连接中
    private boolean mIsWebSocketConnecting = false;

    private ZXWalletManager() {

    }

    public static ZXWalletManager getInstance() {
        if (instance == null) {
            synchronized (ZXWalletManager.class) {
                if (instance == null) {
                    instance = new ZXWalletManager();
                }
            }
        }
        return instance;
    }

    /**
     * SDK初始化
     *
     * @param chainSqlNodeAddr chainsql节点地址
     * @param issueAddr        比特米发行的地址
     * @param connectDelegate  chainsql连接状态代理回调
     * @return NA
     */
    public void
    sdkInit(String chainSqlNodeAddr,
                        String issueAddr, ConnectDelegate connectDelegate) {
        if (StringUtils.isEmpty(chainSqlNodeAddr) || StringUtils.isEmpty(issueAddr) || connectDelegate == null) {
            ZXLogger.e(TAG, "!!!sdkInit fail,invalid params!");
            return;
        }
        try{
            sdkClose();
        }catch (Exception e){

        }


        mChainSqlNodeAddr = chainSqlNodeAddr;
        mIssueAddr = issueAddr;
        mConnectDelegate = connectDelegate;

        mConnectDelegate.connectState(ConnectDelegate.CONNECT_CONNECTING);
        mIsWebSocketConnecting = true;

        //进行websocket连接
        new Thread(new Runnable() {
            @Override
            public void run() {
                ZXLogger.d(TAG, "sdkInit,begin connect websocket!");
                Chainsql.c.connect(mChainSqlNodeAddr);

                setupChainsqlProcess();
            }
        }).start();
    }

    /**
     * SDK关闭
     *
     * @return NA
     */
    public void sdkClose() {
        if (mIsSubscribeAccount && mReceiveListWalletAddrs != null && mReceiveListWalletAddrs.size() > 0) {
            ZXWalletManager.getInstance().unSubscribeAccount(mReceiveListWalletAddrs);
        }
        mIsSubscribeAccount = false;
        mReceiveListWalletAddrs = null;
        mCurReceiveListCurrencyType = "";
        isBoth = false;
        mChainSqlNodeAddr = "";
        mIssueAddr = "";
        mLastTxId = "";
        mConnectDelegate = null;
        mAccountTransactionCallback = null;
        mReceiveListCallback = null;
        mIsWebSocketConnecting = false;
        isReceiveListLisenOffer = false;

     //   if (isConnect()) {
            ZXLogger.d(TAG, "sdkClose,disconnect websocket!");
            try{
                Chainsql.c.getConnection().disconnect();
            }catch (Exception e){

            }

      //  }
    }

    /**
     * 生成钱包
     *
     * @return WalletInfo 钱包信息
     */
    public WalletInfo generateWallet() {
        try {
            JSONObject jsonObject = Chainsql.c.generateAddress();

            String secret = jsonObject.getString(SECRET);
            String accountID = jsonObject.getString(ACCOUNT_ID);
            String publicKey = jsonObject.getString(PUBLIC_KEY);

            if (StringUtils.isEmpty(secret)) {
                ZXLogger.w(TAG, "generateWallet failed,secret is empty!");
                return null;
            }

            WalletInfo walletInfo = new WalletInfo(secret, publicKey, accountID);

            return walletInfo;

        } catch (Exception e) {
            e.printStackTrace();
            ZXLogger.w(TAG, "generateWallet failed,exception:" + e.toString());
            return null;
        }
    }
    private JSONObject generateAddress(Seed seed) {
        if (Config.isUseGM()) {
            seed.setGM();
        }

        IKeyPair keyPair = seed.keyPair();
        byte[] pubBytes = keyPair.canonicalPubBytes();
        SHA256Digest sha = new SHA256Digest();
        sha.update(pubBytes, 0, pubBytes.length);
        byte[] result = new byte[sha.getDigestSize()];
        sha.doFinal(result, 0);
        RIPEMD160Digest d = new RIPEMD160Digest();
        d.update(result, 0, result.length);
        byte[] o = new byte[d.getDigestSize()];
        d.doFinal(o, 0);
        String secretKey = Config.getB58IdentiferCodecs().encodeFamilySeed(seed.bytes());
        String publicKey = Config.getB58IdentiferCodecs().encode(pubBytes, 35);
        String address = Config.getB58IdentiferCodecs().encodeAddress(o);
        JSONObject obj = new JSONObject();
        try{
            if (!Config.isUseGM()) {
                obj.put("secret", secretKey);
            }

            obj.put("account_id", address);
            obj.put("public_key", publicKey);
            obj.put("publicKeyHex", Util.bytesToHex(pubBytes));
        }catch (Exception e){

        }

        return obj;
    }

    public WalletInfo generateWalletByWord(Seed seed) {
        try {
            JSONObject jsonObject = generateAddress(seed);

            String secret = jsonObject.getString(SECRET);
            String accountID = jsonObject.getString(ACCOUNT_ID);
            String publicKey = jsonObject.getString(PUBLIC_KEY);

            if (StringUtils.isEmpty(secret)) {
                ZXLogger.w(TAG, "generateWallet failed,secret is empty!");
                return null;
            }

            WalletInfo walletInfo = new WalletInfo(secret, publicKey, accountID);

            return walletInfo;

        } catch (Exception e) {
            e.printStackTrace();
            ZXLogger.w(TAG, "generateWallet failed,exception:" + e.toString());
            return null;
        }
    }

    /**
     * 根据私钥获取钱包信息
     *
     * @param secret1 私钥
     * @return 钱包实体(其中有生成的私钥 、 公钥 、 地址)
     */
    public WalletInfo generateWallet(String secret1) {
        try {
            JSONObject jsonObject = Chainsql.c.generateAddress(secret1);

            String secret = jsonObject.getString(SECRET);
            String accountID = jsonObject.getString(ACCOUNT_ID);
            String publicKey = jsonObject.getString(PUBLIC_KEY);

            if (StringUtils.isEmpty(secret)) {
                ZXLogger.w(TAG, "generateWallet failed,secret is empty!");
                return null;
            }

            WalletInfo walletInfo = new WalletInfo(secret, publicKey, accountID);

            return walletInfo;

        } catch (Exception e) {
            e.printStackTrace();
            ZXLogger.w(TAG, "generateWallet failed,exception:" + e.toString());
            return null;
        }
    }

    /**
     * 注册用户交易通知
     *
     * @param walletAddr                 钱包地址
     * @param subscribeResultCallback    注册账户交易结果异步回调
     * @param accountTransactionCallback 该账户钱包交易异步回调 （收到别人给我转账）
     */
    public void subscribeAccountTransaction(final String walletAddr,
                                            final SubscribeResultCallback subscribeResultCallback,
                                            final AccountTransactionCallback accountTransactionCallback) {
        if (!isConnect() || StringUtils.isEmpty(walletAddr) || subscribeResultCallback == null || accountTransactionCallback == null) {
            ZXLogger.e(TAG, "subscribeAccountTransaction failed,not connect or wallet or callback is null!");
            if (null != subscribeResultCallback) {
                subscribeResultCallback.subscribeResult(HDTSdkApi.CODE_FAIL, "", HDTSdkApi.CODE_FAIL);
            }
            return;
        }

        //先解注册账户的交易通知
        if (mIsSubscribeAccount && mReceiveListWalletAddrs != null && mReceiveListWalletAddrs.size() > 0) {
            ZXWalletManager.getInstance().unSubscribeAccount(mReceiveListWalletAddrs);
        }

        Chainsql.c.getConnection().getClient().makeManagedRequest(Command.subscribe, new Request.Manager<JSONObject>() {
            public boolean retryOnUnsuccessful(Response r) {
                return false;
            }

            public void cb(Response response, JSONObject jsonObject) throws JSONException {
                ZXLogger.d(TAG, "subscribeAccountTransaction response jsonObject:" + jsonObject.toString());

                if (null != jsonObject && !jsonObject.isNull("status")) {
                    String status = jsonObject.getString("status");
                    if (status.equals("success")) {
                        mIsSubscribeAccount = true;
//                        mWalletAddr = walletAddr;
                        mAccountTransactionCallback = accountTransactionCallback;
                        subscribeResultCallback.subscribeResult(HDTSdkApi.CODE_SUCCESS, "", HDTSdkApi.CODE_SUCCESS);
                    } else {
                        subscribeResultCallback.subscribeResult(HDTSdkApi.CODE_FAIL, response.engineResult().human, HDTSdkApi.CODE_FAIL);
                    }
                } else {
                    subscribeResultCallback.subscribeResult(HDTSdkApi.CODE_FAIL, response.engineResult().human, HDTSdkApi.CODE_FAIL);
                }
            }
        }, new Request.Builder<JSONObject>() {
            public void beforeRequest(Request request) {
                JSONArray accounts_arr = new JSONArray();
                AccountID accountId = AccountID.fromAddress(walletAddr);
                accounts_arr.put(accountId);
                request.json("accounts", accounts_arr);
            }

            public JSONObject buildTypedResponse(Response response) {
                return response.message;
            }
        });
    }

    /**
     * 获取钱包系统币
     *
     * @param walletAddr          钱包地址
     * @param balanceInfoCallback 获取系统币以异步方式返回
     */
    public void getSysCoinBalance(final String walletAddr, final BalanceInfoCallback balanceInfoCallback) {
        if (null == balanceInfoCallback) {
            ZXLogger.e(TAG, "getSysCoinBalance failed,balanceInfoCallback is null!");
            return;
        }

        if (!isConnect() || StringUtils.isEmpty(walletAddr)) {
            ZXLogger.e(TAG, "getSysCoinBalance failed,not connect or wallet is null!");
            balanceInfoCallback.balanceInfo(HDTSdkApi.CODE_FAIL, "getSysCoinBalance failed,not connect or wallet is null!", "0");
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                Gson gson = new Gson();
                AccountInfoModel accountInfoModule;

                try {

                    Request request = Chainsql.c.getConnection().getClient().accountInfo(AccountID.fromAddress(walletAddr));

                    if (null != request.response && request.response.result != null && !StringUtils.isEmpty(request.response.result.toString())) {
                        ZXLogger.i(TAG, "fetchAccountInfo request response result : " + request.response.result);
                        accountInfoModule = gson.fromJson(request.response.result.toString(), AccountInfoModel.class);

                        String balance = accountInfoModule.getAccount_data().getBalance();
                        BigDecimal divide = new BigDecimal(balance).subtract(new BigDecimal(1000000)).stripTrailingZeros();

                        balanceInfoCallback.balanceInfo(HDTSdkApi.CODE_SUCCESS, "", divide.toPlainString());

                    } else if (null != request.response && ACCOUNT_NOT_FOUND.equals(request.response.error)) {
                        ZXLogger.i(TAG, "fetchAccountInfo ACCOUNT_NOT_FOUND!");
                        balanceInfoCallback.balanceInfo(HDTSdkApi.CODE_ACCOUNT_NOT_ACTIVATE, "", "0");
                    } else {
                        balanceInfoCallback.balanceInfo(HDTSdkApi.CODE_FAIL, request.response.engineResult().human, "0");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    ZXLogger.e(TAG, "getSysCoinBalance,error:" + e.toString());
                    balanceInfoCallback.balanceInfo(HDTSdkApi.CODE_FAIL, e.toString(), "0");
                }
            }
        }).start();
    }

    /**
     * 获取钱包比特米的余额
     *
     * @param walletAddr          钱包地址
     * @param balanceInfoCallback 获取比特米余额以异步方式返回
     */
    public void getIssueCurrencyBalance(final String currencyType, final String walletAddr, final BalanceInfoCallback balanceInfoCallback) {
        if (null == balanceInfoCallback) {
            ZXLogger.e(TAG, "getIssueCurrencyBalance failed,balanceInfoCallback is null!");
            return;
        }

        if (!isConnect() || StringUtils.isEmpty(walletAddr)) {
            ZXLogger.e(TAG, "getIssueCurrencyBalance failed,not connect or wallet is null!");
            balanceInfoCallback.balanceInfo(HDTSdkApi.CODE_FAIL, "getIssueCurrencyBalance failed,not connect or wallet is null!", "0");
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                Gson gson = new Gson();
                AccountLineModel accountLineModule;

                try {
                    Request request = Chainsql.c.getConnection().getClient().requestAccountLines(AccountID.fromAddress(walletAddr));

                    if (null != request.response && request.response.result != null && !StringUtils.isEmpty(request.response.result.toString())) {
                        ZXLogger.i(TAG, "getIssueCurrencyBalance request response result : " + request.response.result);
                        accountLineModule = gson.fromJson(request.response.result.toString(), AccountLineModel.class);

                        String balance = null;
                        boolean isFreezePeer = false;
                        List<AccountLineModel.LinesBean> lines = accountLineModule.getLines();
                        for (AccountLineModel.LinesBean item : lines) {
                            if (item.getCurrency().equals(currencyType)) {
                                // 找到有比特米
                                balance = item.getBalance();
                                isFreezePeer = item.isFreezePeer();
                                break;
                            }
                        }

                        if (null == balance) {
                            balanceInfoCallback.balanceInfo(HDTSdkApi.CODE_ACCOUNT_NOT_TRUST, "", "0");
                        } else if (isFreezePeer) {
                            balanceInfoCallback.balanceInfo(HDTSdkApi.CODE_ACCOUNT_HDT_FREEZED, "", balance);
                        } else {
                            balanceInfoCallback.balanceInfo(HDTSdkApi.CODE_SUCCESS, "", balance);
                        }

                    } else if (null != request.response && ACCOUNT_NOT_FOUND.equals(request.response.error)) {
                        balanceInfoCallback.balanceInfo(HDTSdkApi.CODE_ACCOUNT_NOT_ACTIVATE, "", "0");
                    } else {
                        balanceInfoCallback.balanceInfo(HDTSdkApi.CODE_FAIL, null != request.response?request.response.error_message:"", "0");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    ZXLogger.e(TAG, "getIssueCurrencyBalance,error:" + e.toString());
                    balanceInfoCallback.balanceInfo(HDTSdkApi.CODE_FAIL, e.toString(), "0");
                }
            }
        }).start();
    }

    /**
     * 获取钱包比特米的余额
     *
     * @param walletAddr          钱包地址
     * @param balanceInfoCallback 获取比特米余额以异步方式返回
     */
    public void getIssueCurrencyBalanceList(final String walletAddr, final BalanceListInfoCallback balanceInfoCallback) {
        if (null == balanceInfoCallback) {
            ZXLogger.e(TAG, "getIssueCurrencyBalance failed,balanceInfoCallback is null!");
            return;
        }

        if (!isConnect() || StringUtils.isEmpty(walletAddr)) {
            ZXLogger.e(TAG, "getIssueCurrencyBalance failed,not connect or wallet is null!");
            BalanceListInfo info = new BalanceListInfo();
            info.setBalanceBtd("0");

            balanceInfoCallback.balanceInfo(HDTSdkApi.CODE_FAIL, "getIssueCurrencyBalance failed,not connect or wallet is null!", info);
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                Gson gson = new Gson();
                AccountLineModel accountLineModule;
                BalanceListInfo info = new BalanceListInfo();
                info.setBalanceBtd("0");

                try {
                    Request request = Chainsql.c.getConnection().getClient().requestAccountLines(AccountID.fromAddress(walletAddr));

                    if (null != request.response && request.response.result != null && !StringUtils.isEmpty(request.response.result.toString())) {
                        ZXLogger.i(TAG, "getIssueCurrencyBalance request response result : " + request.response.result);
                        accountLineModule = gson.fromJson(request.response.result.toString(), AccountLineModel.class);
                        String balance = null;
                        boolean isFreezePeer = false;
                        boolean geted = false;
                        List<AccountLineModel.LinesBean> lines = accountLineModule.getLines();
                        for (AccountLineModel.LinesBean item : lines) {
                            if (item.getCurrency().equals(CURRENCY_TYPE_CLC)) {
                                // 找到有比特米
                                info.setBalanceBtd(item.getBalance());
                                info.setFreezePeerBtd(item.isFreezePeer());
                                isFreezePeer = item.isFreezePeer();
                                if(geted){
                                    break;
                                }
                                geted = true;
                                balance = item.getBalance();
                            }
                        }

                        if (null == balance) {
                            balanceInfoCallback.balanceInfo(HDTSdkApi.CODE_ACCOUNT_NOT_TRUST, "", info);
                        } else if (isFreezePeer) {
                            balanceInfoCallback.balanceInfo(HDTSdkApi.CODE_ACCOUNT_HDT_FREEZED, "", info);
                        } else {
                            balanceInfoCallback.balanceInfo(HDTSdkApi.CODE_SUCCESS, "", info);
                        }

                    } else if (null != request.response && ACCOUNT_NOT_FOUND.equals(request.response.error)) {
                        balanceInfoCallback.balanceInfo(HDTSdkApi.CODE_ACCOUNT_NOT_ACTIVATE, "", info);
                    } else {
                        balanceInfoCallback.balanceInfo(HDTSdkApi.CODE_FAIL, null != request.response?request.response.error_message:"", info);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    ZXLogger.e(TAG, "getIssueCurrencyBalance,error:" + e.toString());
                    balanceInfoCallback.balanceInfo(HDTSdkApi.CODE_FAIL, e.toString(), info);
                }
            }
        }).start();
    }

    /**
     * 获取钱包比特米交易明细
     *
     * @param walletAddr              钱包地址
     * @param limit                   本次获取的明细数目
     * @param marker                  客户端初始请求时，该字段填空,当交易量较多时，服务端会分页返回记录，当服务端返回的marker字段有值时，则说明是分页返回
     * @param currencyTxsInfoCallback 交易明细信息回调
     */
    public void getIssueCurrencyTxDetail(final String walletAddr, int limit, String marker,
                                         final CurrencyTxsInfoCallback currencyTxsInfoCallback) {
        if (null == currencyTxsInfoCallback) {
            ZXLogger.e(TAG, "getIssueCurrencyTxDetail failed,currencyTxsInfoCallback is null!");
            return;
        }

        if (!isConnect() || StringUtils.isEmpty(walletAddr)) {
            ZXLogger.e(TAG, "getIssueCurrencyTxDetail failed,not connect or wallet is null!");
            currencyTxsInfoCallback.currencyTxsInfo(HDTSdkApi.CODE_FAIL, "getIssueCurrencyTxDetail failed,not connect or wallet is null!", null);
            return;
        }

        try {
            JSONObject jsonMarker = null;
            if (!StringUtils.isEmpty(marker)) {
                jsonMarker = new JSONObject(marker);
            }
            Chainsql.c.getConnection().getClient().getTransactions(walletAddr, limit, jsonMarker, new Publisher.Callback<JSONObject>() {
                @Override
                public void called(JSONObject jsonObject) {
                    if (null == jsonObject) {
                        if (null != currencyTxsInfoCallback) {
                            currencyTxsInfoCallback.currencyTxsInfo(HDTSdkApi.CODE_FAIL, "Called json is null,input param may be invalid!", null);
                        }
                        return;
                    }

                    ZXLogger.d(TAG, "getTransactions:" + jsonObject.toString());
                    CurrencyTxDetails currencyTxDetails = new CurrencyTxDetails();

                    try {
                        if (jsonObject.isNull("marker")) {
                            currencyTxDetails.setMarker("");
                        } else {
                            JSONObject newJsonMarker = jsonObject.getJSONObject("marker");
                            if (null == newJsonMarker) {
                                currencyTxDetails.setMarker("");
                            } else {
                                currencyTxDetails.setMarker(newJsonMarker.toString());
                            }
                        }

                        ArrayList<CurrencyTxDetail> currencyTxDetailList = new ArrayList<CurrencyTxDetail>();

                        JSONArray txArray = jsonObject.getJSONArray("transactions");
                        if (null != txArray) {
                            for (int i = 0; i < txArray.length(); i++) {
                                JSONObject tempTxJson = txArray.getJSONObject(i);
                                if (!tempTxJson.isNull("tx")) {
                                    JSONObject txJson = tempTxJson.getJSONObject("tx");
                                    String transactionType = txJson.getString("TransactionType");
                                    if (transactionType.equals("Payment")) {
                                        boolean isSysCoinPayment = false;
                                        JSONObject amountJson = null;
                                        try {
                                            amountJson = txJson.getJSONObject("Amount");
                                        } catch (Exception e) {
                                            isSysCoinPayment = true;
                                        }

                                        if (null != amountJson && !isSysCoinPayment) {
                                            String currency = amountJson.getString("currency");
                                            if (currency.equals(CURRENCY_TYPE_CLC)) {
                                                String value = amountJson.getString("value");
                                                String account = txJson.getString("Account");
                                                String destination = txJson.getString("Destination");
                                                String txId = txJson.getString("hash");
                                                long time = txJson.getLong("date");
                                                Date txDate = RippleDate.fromSecondsSinceRippleEpoch(time);

                                                CurrencyTxDetail currencyTxDetail = new CurrencyTxDetail();
                                                currencyTxDetail.setTxId(txId);
                                                currencyTxDetail.setDate(txDate);
                                                if (destination.equals(walletAddr)) {
                                                    currencyTxDetail.setTxType(HDTSdkApi.TRANS_TYPE_TRANSFER_IN);
                                                } else {
                                                    currencyTxDetail.setTxType(HDTSdkApi.TRANS_TYPE_TRANSFER_OUT);
                                                }

                                                currencyTxDetail.setFromAddr(account);
                                                currencyTxDetail.setToAddr(destination);
                                                currencyTxDetail.setAmount(value);
                                                currencyTxDetail.setCoinType(currency);

                                                //获取手续费
                                                if (txJson.has("SendMax")) {
                                                    JSONObject sendMaxJson = txJson.getJSONObject("SendMax");
                                                    String sendMaxValue = sendMaxJson.getString("value");
                                                    BigDecimal transferFeeDecimal = new BigDecimal(sendMaxValue).subtract(new BigDecimal(value));
                                                    currencyTxDetail.setTransferFee(transferFeeDecimal.toPlainString());
                                                } else {
                                                    currencyTxDetail.setTransferFee("0");
                                                }

                                                currencyTxDetailList.add(currencyTxDetail);
                                            }
                                        }
                                    }
                                }
                            }

                            currencyTxDetails.setCurrencyTxDetailList(currencyTxDetailList);
                            if (null != currencyTxsInfoCallback) {
                                currencyTxsInfoCallback.currencyTxsInfo(HDTSdkApi.CODE_SUCCESS, "", currencyTxDetails);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        if (null != currencyTxsInfoCallback) {
                            currencyTxsInfoCallback.currencyTxsInfo(HDTSdkApi.CODE_FAIL, e.toString(), null);
                        }
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            currencyTxsInfoCallback.currencyTxsInfo(HDTSdkApi.CODE_FAIL, e.toString(), null);
        }
    }

    /**
     * 信任比特米
     *
     * @param privateKey                钱包私钥（注意私钥与最近监听的账户钱包地址对应）
     * @param commonTransactionCallback 信任结果回调
     */
    public void trustIssueCurrency(final String currencyType, String privateKey, final CommonTransactionCallback commonTransactionCallback) {
        if (null == commonTransactionCallback) {
            ZXLogger.e(TAG, "trustIssueCurrency failed,commonTransactionCallback is null");
            return;
        }

        if (!isConnect() || StringUtils.isEmpty(privateKey) || StringUtils.isEmpty(mIssueAddr)) {
            ZXLogger.e(TAG, "trustIssueCurrency failed,invalid param,is sdk inited?! or private key null");
            CommonTransInfo commonTransInfo = new CommonTransInfo(HDTSdkApi.TRANS_TYPE_TRUST, "");
            commonTransactionCallback.transactionResult(HDTSdkApi.CODE_FAIL, "trustIssueCurrency failed,invalid param,is sdk inited?! or private key null", commonTransInfo);
            return;
        }

        try {
            Account account = Chainsql.c.getConnection().getClient().accountFromSeed(privateKey);
            TransactionManager tm = account.transactionManager();
            TrustSet trustSet = new TrustSet();

            Amount limitAmount = new Amount(new BigDecimal(100000000),
                    Currency.fromString(currencyType),
                    AccountID.fromAddress(mIssueAddr));

            trustSet.as(Amount.LimitAmount, limitAmount);
            trustSet.as(AccountID.Account, account.id());

            tm.queue(tm.manage(trustSet)
                    .onSubmitSuccess(new ManagedTxn.OnSubmitSuccess() {
                        @Override
                        public void called(Response response) {
                            ZXLogger.i(TAG, "trustIssueCurrency,submit success!");
                        }
                    })
                    .onError(new Publisher.Callback<Response>() {
                        @Override
                        public void called(Response response) {
                            ZXLogger.e(TAG, "trustIssueCurrency,error:" + response.result.toString());
                            try {
                                JSONObject result = response.result;
                                String engine_result = String.valueOf(result.get("engine_result"));

                                if (engine_result.equals("terQUEUED")) {
                                    //交易排队不做错误处理，仍然返回正常
                                    return;
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            CommonTransInfo commonTransInfo = new CommonTransInfo(HDTSdkApi.TRANS_TYPE_TRUST, "");
                            commonTransactionCallback.transactionResult(HDTSdkApi.CODE_FAIL, response.engineResult().human, commonTransInfo);
                        }
                    })
                    .onValidated(new Publisher.Callback<ManagedTxn>() {
                        @Override
                        public void called(ManagedTxn managedTxn) {
                            TransactionResult result = managedTxn.result;
                            EngineResult engineResult = result.engineResult;
                            if (engineResult == EngineResult.tesSUCCESS) {
                                CommonTransInfo commonTransInfo = new CommonTransInfo(HDTSdkApi.TRANS_TYPE_TRUST, result.hash.toString());
                                commonTransactionCallback.transactionResult(HDTSdkApi.CODE_SUCCESS, "", commonTransInfo);
                            } else {
                                CommonTransInfo commonTransInfo = new CommonTransInfo(HDTSdkApi.TRANS_TYPE_TRUST, "");
                                commonTransactionCallback.transactionResult(HDTSdkApi.CODE_FAIL, result.engineResult.human, commonTransInfo);
                            }
                        }
                    }));
        } catch (Exception e) {
            e.printStackTrace();
            CommonTransInfo commonTransInfo = new CommonTransInfo(HDTSdkApi.TRANS_TYPE_TRUST, "");
            commonTransactionCallback.transactionResult(HDTSdkApi.CODE_FAIL, e.toString(), commonTransInfo);
        }
    }

    ManagedTxn transferTxn=null;

    /**
     * 转账
     *
     * @param privateKey                 钱包私钥（注意私钥与最近监听的账户钱包地址对应）
     * @param toWalletAddr               目标地址
     * @param amount                     转账金额
     * @param accountTransactionCallback 转账结果回调,异步返回结果
     */
    public void transferCurrency(final String currencyType, String privateKey, String toWalletAddr, final String amount,
                                 String remark, String type, final AccountTransactionCallback accountTransactionCallback) {
        if (null == accountTransactionCallback) {
            ZXLogger.e(TAG, "transferCurrency failed,invalid param,accountTransactionCallback is null");
            return;
        }

        if (!isConnect() || StringUtils.isEmpty(privateKey) || StringUtils.isEmpty(mIssueAddr)) {
            ZXLogger.e(TAG, "transferCurrency failed,invalid param,is sdk inited?! or private key null");
            TransferInfo transferInfo = new TransferInfo();
            transferInfo.setType(HDTSdkApi.TRANS_TYPE_TRANSFER_OUT);
            accountTransactionCallback.accountTransactionResult(HDTSdkApi.CODE_FAIL,
                    "transferCurrency failed,invalid param,is sdk inited?! or private key null", transferInfo);
            return;
        }

        //预防私钥输入错误
        Account account;
        final String address;
        try {
            account = Chainsql.c.getConnection().getClient().accountFromSeed(privateKey);
            address = account.id().address;
        } catch (Exception e) {
            e.printStackTrace();
            TransferInfo transferInfo = new TransferInfo();
            transferInfo.setType(HDTSdkApi.TRANS_TYPE_TRANSFER_OUT);
            transferInfo.setFromAddr("");
            transferInfo.setToAddr(toWalletAddr);
            transferInfo.setAmount(amount);
            accountTransactionCallback.accountTransactionResult(HDTSdkApi.CODE_FAIL, e.toString(), transferInfo);
            return;
        }
        try {

            TransactionManager tm = account.transactionManager();
            Payment payment = new Payment();
            BigDecimal value = new BigDecimal(amount);
            Amount amountToSend = new Amount(value, Currency.fromString(currencyType),
                    AccountID.fromAddress(mIssueAddr));

            payment.as(AccountID.Account, account.id());
            payment.as(AccountID.Destination, toWalletAddr);
            payment.as(Amount.Amount, amountToSend);

            //新加备注字段 2018.7.12  start
            if (!TextUtils.isEmpty(type) || !TextUtils.isEmpty(remark)) {
                JSONArray array = new JSONArray();
                JSONObject jsonObject = new JSONObject();
                JSONObject memo = new JSONObject();
                String strType = StringUtils.str2HexStr(type);
                remark = StringUtils.str2HexStr(remark);
                memo.put("MemoType", strType);
                memo.put("MemoData", remark);
                jsonObject.put("Memo", memo);
                array.put(jsonObject);
                STArray stArray = STArray.translate.fromJSONArray(array);
                payment.put(STArray.Memos, stArray);
            }
            //新加备注字段 2018.7.12  end

            if (!StringUtils.isEmpty(mTransferFee)) {
                BigDecimal transferFee = new BigDecimal(mTransferFee);
                BigDecimal sendMaxValue = value.add(transferFee);
                Amount sendMax = new Amount(sendMaxValue, Currency.fromString(currencyType),
                        AccountID.fromAddress(mIssueAddr));

                payment.as(Amount.SendMax, sendMax);
            }

            transferTxn= tm.manage(payment)
                    .onSubmitSuccess(new ManagedTxn.OnSubmitSuccess() {
                        @Override
                        public void called(Response response) {
                            ZXLogger.i(TAG, "transferCurrency,submit success!");
                        }
                    })
                    .onError(new Publisher.Callback<Response>() {
                        @Override
                        public void called(Response response) {
                            ZXLogger.e(TAG, "transferCurrency,error:" + response.result.toString());

                            try {
                                JSONObject result = response.result;
                                String engine_result = String.valueOf(result.get("engine_result"));

                                if (engine_result.equals("terQUEUED")) {
                                    //交易排队不做错误处理，仍然返回正常
                                    return;
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            TransferInfo transferInfo = new TransferInfo();
                            transferInfo.setTxId(transferTxn.hash.toString());
                            //String hash= txn.hash.toString();
                            transferInfo.setType(HDTSdkApi.TRANS_TYPE_TRANSFER_OUT);
                            accountTransactionCallback.accountTransactionResult(HDTSdkApi.CODE_FAIL, response.engineResult().human, transferInfo);
                        }
                    })
                    .onValidated(new Publisher.Callback<ManagedTxn>() {
                        @Override
                        public void called(ManagedTxn managedTxn) {
                            TransactionResult result = managedTxn.result;
                            EngineResult engineResult = result.engineResult;
                            if (engineResult == EngineResult.tesSUCCESS) {
                                TransferInfo transferInfo = new TransferInfo();
                                transferInfo.setType(HDTSdkApi.TRANS_TYPE_TRANSFER_OUT);
                                transferInfo.setFromAddr(address);
                                transferInfo.setTxId(result.hash.toString());
                                AccountID destination = result.txn.get(AccountID.Destination);
                                transferInfo.setToAddr(destination.address);
                                transferInfo.setAmount(amount);
                                accountTransactionCallback.accountTransactionResult(HDTSdkApi.CODE_SUCCESS, "", transferInfo);
                            } else {
                                TransferInfo transferInfo = new TransferInfo();
                                transferInfo.setType(HDTSdkApi.TRANS_TYPE_TRANSFER_OUT);
                                accountTransactionCallback.accountTransactionResult(HDTSdkApi.CODE_FAIL, result.engineResult.human, transferInfo);
                            }
                        }
                    });

            tm.queue(transferTxn);

        } catch (Exception e) {
            e.printStackTrace();
            TransferInfo transferInfo = new TransferInfo();
            transferInfo.setType(HDTSdkApi.TRANS_TYPE_TRANSFER_OUT);
            transferInfo.setFromAddr(address);
            transferInfo.setToAddr(toWalletAddr);
            transferInfo.setTxId(transferTxn.hash.toString());
            transferInfo.setAmount(amount);
            accountTransactionCallback.accountTransactionResult(HDTSdkApi.CODE_FAIL, e.toString(), transferInfo);
        }
    }

    /**
     * (takerGet是你要卖的币，takerPay是你要买的币。这个理解上会有点绕)
     *
     * @param currencyType  当前操作的币种（转出）
     * @param privateKey    私钥
     * @param payAmount     需要获取的数量
     * @param getAmount     支付的数量
     * @param remark        标记
     * @param type          标记的类型
     * @param offerCallback 回调
     */
    public void offerCreate(final String currencyType, String privateKey, final String payAmount
            , final String getAmount, String remark, String type, final OfferCallback offerCallback) {
        if (null == offerCallback) {
            ZXLogger.e(TAG, "offerCreate failed,invalid param,accountTransactionCallback is null");
            return;
        }

        if (!isConnect() || StringUtils.isEmpty(privateKey) || StringUtils.isEmpty(mIssueAddr)) {
            ZXLogger.e(TAG, "offerCreate failed,invalid param,is sdk inited?! or private key null");
            OfferInfo offerInfo = new OfferInfo();
            offerInfo.setType(HDTSdkApi.OFFER_TYPE_CREATE);
            offerCallback.offerResult(HDTSdkApi.CODE_FAIL,
                    "offerCreate failed,invalid param,is sdk inited?! or private key null", offerInfo);
            return;
        }
        //预防私钥输入错误
        Account account;
        final String addr;
        try {
            account = Chainsql.c.getConnection().getClient().accountFromSeed(privateKey);
            addr = account.id().address;
        } catch (Exception e) {
            e.printStackTrace();
            OfferInfo offerInfo = new OfferInfo();
            offerInfo.setType(HDTSdkApi.OFFER_TYPE_CREATE);
            offerInfo.setTakerPays(payAmount);
            offerInfo.setTakerGets(getAmount);
            offerInfo.setAccount("");
            offerCallback.offerResult(HDTSdkApi.CODE_FAIL, e.toString(), offerInfo);
            return;
        }
        try {
            TransactionManager tm = account.transactionManager();
            final OfferCreate offerCreate = new OfferCreate();
            String payCurrentcyType =CURRENCY_TYPE_CLC;
            //pay为请求获取得到的
            BigDecimal payValue = new BigDecimal(payAmount);
            Amount amountPay = new Amount(payValue, Currency.fromString(payCurrentcyType),
                    AccountID.fromAddress(mIssueAddr));
            offerCreate.as(AccountID.Account, account.id());
            offerCreate.as(Amount.TakerPays, amountPay);

            BigDecimal getValue = new BigDecimal(getAmount);
            Amount amountGet = new Amount(getValue, Currency.fromString(currencyType),
                    AccountID.fromAddress(mIssueAddr));
            offerCreate.as(Amount.TakerGets, amountGet);

            if (!TextUtils.isEmpty(type) || !TextUtils.isEmpty(remark)) {
                JSONArray array = new JSONArray();
                JSONObject jsonObject = new JSONObject();
                JSONObject memo = new JSONObject();
                String strType = StringUtils.str2HexStr(type);
                remark = StringUtils.str2HexStr(remark);
                memo.put("MemoType", strType);
                memo.put("MemoData", remark);
                jsonObject.put("Memo", memo);
                array.put(jsonObject);
                STArray stArray = STArray.translate.fromJSONArray(array);
                offerCreate.put(STArray.Memos, stArray);
            }

            tm.queue(tm.manage(offerCreate)
                    .onSubmitSuccess(new ManagedTxn.OnSubmitSuccess() {
                        @Override
                        public void called(Response response) {
                            ZXLogger.i(TAG, "offerCreate,submit success!");
                        }
                    })
                    .onError(new Publisher.Callback<Response>() {
                        @Override
                        public void called(Response response) {
                            ZXLogger.e(TAG, "offerCreate,error:" + response.result.toString());

                            try {
                                JSONObject result = response.result;
                                String engine_result = String.valueOf(result.get("engine_result"));

                                if (engine_result.equals("terQUEUED")) {
                                    //交易排队不做错误处理，仍然返回正常
                                    return;
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            OfferInfo offerInfo = new OfferInfo();
                            offerInfo.setType(HDTSdkApi.OFFER_TYPE_CREATE);
                            offerCallback.offerResult(HDTSdkApi.CODE_FAIL, response.engineResult().human, offerInfo);
                        }
                    })
                    .onValidated(new Publisher.Callback<ManagedTxn>() {
                        @Override
                        public void called(ManagedTxn managedTxn) {
                            TransactionResult result = managedTxn.result;
                            EngineResult engineResult = result.engineResult;
                            if (engineResult == EngineResult.tesSUCCESS) {
                                OfferInfo offerInfo = new OfferInfo();
                                offerInfo.setType(HDTSdkApi.OFFER_TYPE_CREATE);
                                offerInfo.setAccount(addr);
                                offerInfo.setTxId(result.hash.toString());
                                AccountID destination = result.txn.get(AccountID.Destination);
                                offerInfo.setTakerPays(payAmount);
                                offerInfo.setTakerGets(getAmount);
                                offerInfo.setSequence(result.txn.sequence().value());
                                offerCallback.offerResult(HDTSdkApi.CODE_SUCCESS, "", offerInfo);
                            } else {
                                OfferInfo offerInfo = new OfferInfo();
                                offerInfo.setType(HDTSdkApi.OFFER_TYPE_CREATE);
                                offerCallback.offerResult(HDTSdkApi.CODE_FAIL, result.engineResult.human, offerInfo);
                            }
                        }
                    }));

        } catch (Exception e) {
            e.printStackTrace();
            OfferInfo offerInfo = new OfferInfo();
            offerInfo.setType(HDTSdkApi.OFFER_TYPE_CREATE);
            offerInfo.setTakerPays(payAmount);
            offerInfo.setTakerGets(getAmount);
            offerInfo.setAccount(addr);
            offerCallback.offerResult(HDTSdkApi.CODE_FAIL, e.toString(), offerInfo);
        }
    }


    /**
     * 取消挂单
     *
     * @param currencyType  当前操作币的类型
     * @param privateKey    私钥
     * @param remark        标志
     * @param type          标志的类型
     * @param offerSequence 取消的id
     * @param offerCallback 回调
     */
    public void offerCancel(final String currencyType, String privateKey, String remark, String type
            , long offerSequence, final OfferCallback offerCallback) {
        if (null == offerCallback) {
            ZXLogger.e(TAG, "offerCancel failed,invalid param,accountTransactionCallback is null");
            return;
        }

        if (!isConnect() || StringUtils.isEmpty(privateKey) || StringUtils.isEmpty(mIssueAddr)) {
            ZXLogger.e(TAG, "offerCancel failed,invalid param,is sdk inited?! or private key null");
            OfferInfo offerInfo = new OfferInfo();
            offerInfo.setType(HDTSdkApi.OFFER_TYPE_CANCEL);
            offerCallback.offerResult(HDTSdkApi.CODE_FAIL,
                    "offerCancel failed,invalid param,is sdk inited?! or private key null", offerInfo);
            return;
        }

        //预防私钥输入错误
        Account account;
        final String address;
        try {
            account = Chainsql.c.getConnection().getClient().accountFromSeed(privateKey);
            address = account.id().address;
        } catch (Exception e) {
            e.printStackTrace();
            OfferInfo offerInfo = new OfferInfo();
            offerInfo.setType(HDTSdkApi.OFFER_TYPE_CANCEL);
            offerInfo.setAccount("");
            offerCallback.offerResult(HDTSdkApi.CODE_FAIL, e.toString(), offerInfo);
            return;
        }

        try {

            TransactionManager tm = account.transactionManager();
            OfferCancel offerCancel = new OfferCancel();

            offerCancel.as(AccountID.Account, account.id());

            UInt32 int32 = UInt32.translate.fromLong(offerSequence);
            offerCancel.put(Field.OfferSequence, int32);

            if (!TextUtils.isEmpty(type) || !TextUtils.isEmpty(remark)) {
                JSONArray array = new JSONArray();
                JSONObject jsonObject = new JSONObject();
                JSONObject memo = new JSONObject();
                String strType = StringUtils.str2HexStr(type);
                remark = StringUtils.str2HexStr(remark);
                memo.put("MemoType", strType);
                memo.put("MemoData", remark);
                jsonObject.put("Memo", memo);
                array.put(jsonObject);
                STArray stArray = STArray.translate.fromJSONArray(array);
                offerCancel.put(STArray.Memos, stArray);
            }

            tm.queue(tm.manage(offerCancel)
                    .onSubmitSuccess(new ManagedTxn.OnSubmitSuccess() {
                        @Override
                        public void called(Response response) {
                            ZXLogger.i(TAG, "offerCancel,submit success!" + response.result);
                        }
                    })
                    .onError(new Publisher.Callback<Response>() {
                        @Override
                        public void called(Response response) {
                            ZXLogger.e(TAG, "offerCancel,error:" + response.result.toString());

                            try {
                                JSONObject result = response.result;
                                String engine_result = String.valueOf(result.get("engine_result"));

                                if (engine_result.equals("terQUEUED")) {
                                    //交易排队不做错误处理，仍然返回正常
                                    return;
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            OfferInfo offerInfo = new OfferInfo();
                            offerInfo.setType(HDTSdkApi.OFFER_TYPE_CANCEL);
                            offerCallback.offerResult(HDTSdkApi.CODE_FAIL, response.engineResult().human, offerInfo);
                        }
                    })
                    .onValidated(new Publisher.Callback<ManagedTxn>() {
                        @Override
                        public void called(ManagedTxn managedTxn) {
                            TransactionResult result = managedTxn.result;
                            EngineResult engineResult = result.engineResult;
                            if (engineResult == EngineResult.tesSUCCESS) {
                                OfferInfo offerInfo = new OfferInfo();
                                offerInfo.setType(HDTSdkApi.OFFER_TYPE_CANCEL);
                                offerInfo.setAccount(address);
                                offerInfo.setTxId(result.hash.toString());
                                AccountID destination = result.txn.get(AccountID.Destination);
                                offerCallback.offerResult(HDTSdkApi.CODE_SUCCESS, "", offerInfo);
                            } else {
                                OfferInfo offerInfo = new OfferInfo();
                                offerInfo.setType(HDTSdkApi.OFFER_TYPE_CANCEL);
                                offerCallback.offerResult(HDTSdkApi.CODE_FAIL, result.engineResult.human, offerInfo);
                            }
                        }
                    }));

        } catch (Exception e) {
            e.printStackTrace();
            OfferInfo offerInfo = new OfferInfo();
            offerInfo.setType(HDTSdkApi.OFFER_TYPE_CANCEL);
            offerInfo.setAccount(address);
            offerCallback.offerResult(HDTSdkApi.CODE_FAIL, e.toString(), offerInfo);
        }
    }

    /**
     * 查询账户的挂单情况
     *
     * @param walletAddr           账户地址
     * @param limit                一次性查询的批量（10-400）
     * @param marker               第一次marker为空，下一批次填回调的
     * @param accountOfferCallback 查询回调
     */
    public void accountOffers(final String walletAddr, final int limit
            , final String marker, final AccountOfferCallback accountOfferCallback) {
        if (null == accountOfferCallback) {
            ZXLogger.e(TAG, "accountOffers failed,invalid param,accountTransactionCallback is null");
            return;
        }

        if (StringUtils.isEmpty(walletAddr) || StringUtils.isEmpty(mIssueAddr)) {
            ZXLogger.e(TAG, "accountOffers failed,invalid param,is sdk inited?! or private key null");
            accountOfferCallback.accountOfferCallback(HDTSdkApi.CODE_FAIL,
                    "accountOffers failed,invalid param,is sdk inited?! or private key null", null);
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    /*//使用book_offers start
                    final Issue issuePay = new Issue(Currency.fromString(currencyType), AccountID.fromAddress(mIssueAddr));
                    String otherCurrentType = currencyType.equals(CURRENCY_TYPE_HDT)?CURRENCY_TYPE_BTD:CURRENCY_TYPE_HDT;
                    final Issue issueGet = new Issue(Currency.fromString(otherCurrentType), AccountID.fromAddress(mIssueAddr));
                    Request request = Chainsql.c.getConnection().getClient().newRequest(Command.book_offers);
                    request.json("taker_gets", issueGet.toJSON());
                    request.json("taker_pays", issuePay.toJSON());
                    request.json("taker", walletAddr);
                    request.json("limit", limit);
                    if (!StringUtils.isEmpty(marker)) {
                        request.json("marker", marker);
                    }
                    request.request();
                    waiting(request);
                    //使用book_offers end
                    */

                    //使用account_offers start
                    Request request = Chainsql.c.getConnection().getClient().newRequest(Command.account_offers);
                    request.json("account", walletAddr);
                    request.json("limit", limit);
                    if (!StringUtils.isEmpty(marker)) {
                        request.json("marker", marker);
                    }
                    request.request();
                    waiting(request);
                    //使用account_offers end

                    if (null != request.response && request.response.result != null && !StringUtils.isEmpty(request.response.result.toString())) {
                        ZXLogger.i(TAG, "accountOffers request response result : " + request.response.result);
                        Gson gson = new Gson();
                        AccountOfferModel accountOfferModel = gson.fromJson(request.response.result.toString(), AccountOfferModel.class);
                        if (accountOfferModel != null) {
                            accountOfferCallback.accountOfferCallback(HDTSdkApi.CODE_SUCCESS, "", accountOfferModel);
                        } else {
                            accountOfferCallback.accountOfferCallback(HDTSdkApi.CODE_FAIL, request.response.result.toString(), null);
                        }
                    } else {
                        ZXLogger.e(TAG, "accountOffers,error:" + request.toString());
                        AccountOfferModel offerInfo = new AccountOfferModel();
                        offerInfo.setAccount(walletAddr);
                        accountOfferCallback.accountOfferCallback(HDTSdkApi.CODE_FAIL, request.toString(), offerInfo);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    ZXLogger.e(TAG, "accountOffers,error:" + e.toString());
                    AccountOfferModel offerInfo = new AccountOfferModel();
                    offerInfo.setAccount(walletAddr);
                    accountOfferCallback.accountOfferCallback(HDTSdkApi.CODE_FAIL, e.toString(), offerInfo);
                }
            }
        }).start();
    }

    /**
     * 建立监听账户到账，挂单到账
     *
     * @param currencyType            当前操作币的类型
     * @param walletAddrs             需要监听的账号队列
     * @param isLisenOffer            监听挂单吗？
     * @param subscribeResultCallback 建立起监听的回调
     * @param receiveListCallback     监听到账的回调
     */
    public void subscribe(final String currencyType, final List<String> walletAddrs
            , final boolean isLisenOffer,final boolean both, final SubscribeResultCallback subscribeResultCallback,
                          final ReceiveListCallback receiveListCallback) {
        if (null == receiveListCallback || subscribeResultCallback == null
                || walletAddrs == null || walletAddrs.size() < 1 || StringUtils.isEmpty(mIssueAddr)) {
            ZXLogger.e(TAG, "subscribe failed,invalid param,is sdk inited?! or walletAddrs key null");
            OfferInfo offerInfo = new OfferInfo();
            offerInfo.setType(HDTSdkApi.RECEIVE_TYPE);
            if (null != subscribeResultCallback) {
                subscribeResultCallback.subscribeResult(HDTSdkApi.CODE_FAIL, "", HDTSdkApi.CODE_FAIL);
            }
            return;
        }


        //先解注册账户的交易通知
        if (mIsSubscribeAccount && mReceiveListWalletAddrs != null && mReceiveListWalletAddrs.size() > 0) {
            ZXWalletManager.getInstance().unSubscribeAccount(mReceiveListWalletAddrs);
        }

        Chainsql.c.getConnection().getClient().makeManagedRequest(Command.subscribe, new Request.Manager<JSONObject>() {
            public boolean retryOnUnsuccessful(Response r) {
                return false;
            }

            public void cb(Response response, JSONObject jsonObject) throws JSONException {
                ZXLogger.d(TAG, "subscribeAccountTransaction response jsonObject:" + jsonObject.toString());

                if (null != jsonObject && !jsonObject.isNull("status")) {
                    String status = jsonObject.getString("status");
                    if (status.equals("success")) {
                        mIsSubscribeAccount = true;
                        mReceiveListWalletAddrs = walletAddrs;
                        mReceiveListCallback = receiveListCallback;
                        mCurReceiveListCurrencyType = currencyType;
                        isBoth = both;
                        isReceiveListLisenOffer = true;
                        subscribeResultCallback.subscribeResult(HDTSdkApi.CODE_SUCCESS, "", HDTSdkApi.CODE_SUCCESS);
                    } else {
                        subscribeResultCallback.subscribeResult(HDTSdkApi.CODE_FAIL, response.engineResult().human, HDTSdkApi.CODE_FAIL);
                    }
                } else {
                    subscribeResultCallback.subscribeResult(HDTSdkApi.CODE_FAIL, response.engineResult().human, HDTSdkApi.CODE_FAIL);
                }
            }
        }, new Request.Builder<JSONObject>() {
            public void beforeRequest(Request request) {
                JSONArray accounts_arr = new JSONArray();
                for (String addr : walletAddrs) {
                    accounts_arr.put(addr);
                }
                if (isLisenOffer) {
                    try {
                        String otherCurrentType =CURRENCY_TYPE_CLC;
                        JSONObject taker_pays;
                        JSONObject taker_gets;
                        JSONObject object = new JSONObject();
                        JSONArray books = new JSONArray();
                        taker_pays = new JSONObject();

                        taker_pays.put("currency", currencyType);

                        taker_pays.put("issuer", mIssueAddr);
                        object.put("taker_pays", taker_pays);

                        taker_gets = new JSONObject();
                        taker_gets.put("currency", otherCurrentType);
                        taker_gets.put("issuer", mIssueAddr);
                        object.put("taker_gets", taker_gets);

                        object.put("snapshot", true);
                        books.put(object);
                        request.json("books", books);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                request.json("accounts", accounts_arr);
            }

            public JSONObject buildTypedResponse(Response response) {
                return response.message;
            }
        });
    }

    private void waiting(Request request) {
        int count = 50;

        while (request.response == null) {
            Util.waiting();
            --count;
            if (count == 0) {
                break;
            }
        }

    }

    /**
     * 判断地址是否合法
     * @param address  地址
     */
    public boolean isLegalAddress(String address){
        try{
            AccountID accountID = AccountID.fromAddress(address);
            if(accountID.address.equals(address)){
                return true;
            }else {
                return false;
            }
        }catch (Exception e){
            return false;
        }
    }

    /**
     * 获取转账手续费
     *
     * @param transferFeeCallback 转账手续费信息的回调，异步方式返回
     */
    public void getTransferFee(final TransferFeeCallback transferFeeCallback) {
        if (null == transferFeeCallback) {
            ZXLogger.e(TAG, "getTransferFee failed,invalid param,transferFeeCallback is null!");
            return;
        }

        if (!isConnect() || StringUtils.isEmpty(mIssueAddr)) {
            ZXLogger.e(TAG, "getTransferFee failed,invalid param,is sdk inited?!");
            transferFeeCallback.transferFeeInfo(HDTSdkApi.CODE_FAIL,
                    "getTransferFee failed,invalid param,is sdk inited?!", "0");
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                Gson gson = new Gson();
                AccountInfoModel accountInfoModule;

                try {

                    Request request = Chainsql.c.getConnection().getClient().accountInfo(AccountID.fromAddress(mIssueAddr));

                    if (null != request.response && request.response.result != null && !StringUtils.isEmpty(request.response.result.toString())) {
                        ZXLogger.i(TAG, "getTransferFee request response result : " + request.response.result);

                        accountInfoModule = gson.fromJson(request.response.result.toString(), AccountInfoModel.class);
                        String transferFeeMin = accountInfoModule.getAccount_data().getTransferFeeMin();
                        ZXLogger.i(TAG, "!!!!transferFeeFix:" + transferFeeMin);

                        if (!StringUtils.isEmpty(transferFeeMin)) {
                            mTransferFee = transferFeeMin;
                        } else {
                            mTransferFee = "0";
                        }

                        transferFeeCallback.transferFeeInfo(HDTSdkApi.CODE_SUCCESS, "", mTransferFee);
                    } else {
                        transferFeeCallback.transferFeeInfo(HDTSdkApi.CODE_FAIL, null==request.response?"":request.response.toString(), "0");
                        mTransferFee = "0";
                    }

                } catch (Exception e) {
                    mTransferFee = "0";
                    e.printStackTrace();
                    ZXLogger.e(TAG, "getTransferFee,error:" + e.toString());
                    transferFeeCallback.transferFeeInfo(HDTSdkApi.CODE_FAIL, e.toString(), "0");
                }
            }
        }).start();
    }

    /**
     * 获取sdk连接状态的接口
     *
     * @return -1失败 0 成功 1连接中
     */
    public int getConnectState() {
        if (mIsWebSocketConnecting) {
            return ConnectDelegate.CONNECT_CONNECTING;
        }

        if (isConnect()) {
            return ConnectDelegate.CONNECT_SUCCESS;
        }

        return ConnectDelegate.CONNECT_FAIL;
    }

    private void setupChainsqlProcess() {

        Chainsql.c.getConnection().getClient().onConnected(new Client.OnConnected() {
            @Override
            public void called(Client client) {
                ZXLogger.d(TAG, "Chainsql.c.getConnection().getClient().onConnected!");

                mIsWebSocketConnecting = false;
                if (null != mConnectDelegate) {
                    mConnectDelegate.connectState(ConnectDelegate.CONNECT_SUCCESS);
                }

                //注册账户的交易通知,先取消之前的注册
                if (mIsSubscribeAccount && mReceiveListWalletAddrs != null && mReceiveListWalletAddrs.size() > 0) {
                    ZXWalletManager.getInstance().unSubscribeAccount(mReceiveListWalletAddrs);
                }

                if (mReceiveListWalletAddrs != null && mReceiveListWalletAddrs.size() > 0) {
                    ZXWalletManager.getInstance().subscribeAccount(mReceiveListWalletAddrs);
                }

                //每次连接之后，取一次转账费率
                getTransferFee(new TransferFeeCallback() {
                    @Override
                    public void transferFeeInfo(int code, String message, String transferFee) {

                    }
                });
            }
        });

        Chainsql.c.getConnection().getClient().onDisconnected(new Client.OnDisconnected() {
            @Override
            public void called(Client client) {
                ZXLogger.d(TAG, "Chainsql.c.getConnection().getClient().onDisconnected!");

                mIsWebSocketConnecting = false;
                if (null != mConnectDelegate) {
                    mConnectDelegate.connectState(ConnectDelegate.CONNECT_FAIL);
                }
            }
        });

        //服务器推送消息解析
        Chainsql.c.getConnection().getClient().OnMessage(new Client.OnMessage() {
            @Override
            public void called(JSONObject jsonObject) {
                ZXLogger.i(TAG, "onMessage json object " + jsonObject.toString());
                if (jsonObject != null && !TextUtils.isEmpty(mCurReceiveListCurrencyType)) {
                    String type = null;
                    try {
                        type = jsonObject.getString("type");
                    } catch (JSONException e) {
                        e.printStackTrace();
                        return;
                    }

                    if (type.equals("transaction") && mReceiveListWalletAddrs != null && mReceiveListWalletAddrs.size() > 0) {

                        TransactionResult result = new TransactionResult(jsonObject,
                                TransactionResult.Source.transaction_subscription_notification);
                        if (result.validated && !mLastTxId.equals(result.hash.toString())) {

                            Transaction txn = result.txn;
                            AccountID account = txn.get(AccountID.Account);
                            AccountID destination = txn.get(AccountID.Destination);
                            String txId = result.hash.toString();
                            //只有收到别人给我转账(和挂单)才通知上层, 监听账户操作的变化，子线程操作
                            if (mReceiveListCallback != null && mReceiveListWalletAddrs != null && mReceiveListWalletAddrs.size() > 0) {
                                TransactionType transactionType = result.transactionType();
                                if (transactionType == TransactionType.Payment) {
                                    Amount recvAmount = txn.get(Amount.Amount);
                                    String coinType = recvAmount.currency().humanCode();
                                    if (coinType.equals(CURRENCY_TYPE_CLC)) {
                                        ReceiveInfo receiveInfo = new ReceiveInfo();
                                        receiveInfo.setType(HDTSdkApi.TRANS_TYPE_TRANSFER_IN);
                                        receiveInfo.setFromAddr(account.address);
                                        receiveInfo.setTxId(txId);
                                        receiveInfo.setToAddr(destination.address);
                                        receiveInfo.setCurrencyType(coinType);
                                        receiveInfo.setAmount(recvAmount.value().toPlainString());
                                        if(isBoth){
                                            mReceiveListCallback.receiveListCallback(HDTSdkApi.CODE_SUCCESS, "", receiveInfo);
                                        }else {
                                            if(coinType.equals(mCurReceiveListCurrencyType)){
                                                mReceiveListCallback.receiveListCallback(HDTSdkApi.CODE_SUCCESS, "", receiveInfo);
                                            }
                                        }
                                        mLastTxId = txId;
                                    }
                                } else if (result.transactionType() == TransactionType.OfferCreate) {
                                    if (!isReceiveListLisenOffer) {
                                        return;
                                    }
                                    String status = jsonObject.optString("status");
                                    //创建挂单就马上完成了
                                    if("closed".equals(status)){
                                        JSONObject transaction = jsonObject.optJSONObject("transaction");
                                        if(transaction != null){
                                            String offerAccount = transaction.optString("Account");
                                            boolean isMyOfferCreate = false;
                                            for (String add : mReceiveListWalletAddrs) {
                                                if (add.equals(offerAccount)) {
                                                    isMyOfferCreate = true;
                                                    break;
                                                }
                                            }
                                            if(isMyOfferCreate){
                                                String coinType = "";
                                                JSONObject takerGets = transaction.optJSONObject("TakerGets");
                                                if(takerGets != null){
                                                    coinType = takerGets.optString("currency");
                                                }
                                                //是那些人给我对消了我的挂单？
                                                ArrayList<String> otherAccounts = new ArrayList<>();
                                                Iterable<AffectedNode> affectedNodes = result.meta.affectedNodes();
                                                while (affectedNodes.iterator().hasNext()) {
                                                    AffectedNode affectedNode = affectedNodes.iterator().next();
                                                    JSONObject affecteObject = affectedNode.toJSONObject();
                                                    JSONObject modifiedNode = affecteObject.optJSONObject("ModifiedNode");
                                                    if(modifiedNode != null){
                                                        JSONObject finalFields = modifiedNode.optJSONObject("FinalFields");
                                                        if(finalFields != null){
                                                            String otherAccount = finalFields.optString("Account");
                                                            if(!offerAccount.equals(otherAccount) && !TextUtils.isEmpty(otherAccount)){
                                                                otherAccounts.add(otherAccount);
                                                            }
                                                        }
                                                    }
                                                }
                                                ReceiveInfo info = new ReceiveInfo();
                                                info.setTxId(txId);
                                                info.setType(HDTSdkApi.OFFER_TYPE_TRANSFER_IN);
                                                info.setOfferAddr(offerAccount);
                                                info.setCurrencyType(coinType);
                                                info.setOtherOfferAddrs(otherAccounts);
                                                if(isBoth){
                                                    mReceiveListCallback.receiveListCallback(HDTSdkApi.CODE_SUCCESS, "", info);
                                                }else {
                                                    if(coinType.equals(mCurReceiveListCurrencyType)){
                                                        mReceiveListCallback.receiveListCallback(HDTSdkApi.CODE_SUCCESS, "", info);
                                                    }
                                                }
                                                mLastTxId = txId;
                                            }
                                        }
                                    }else {
                                        ReceiveInfo info = new ReceiveInfo();
                                        Iterable<AffectedNode> affectedNodes = result.meta.affectedNodes();
                                        while (affectedNodes.iterator().hasNext()) {
                                            AffectedNode affectedNode = affectedNodes.iterator().next();
                                            JSONObject affecteObject = affectedNode.toJSONObject();
                                            //DeletedNode节点表示该申请得到彻底的回应，要消除该笔挂单
                                            JSONObject deletedNodeObject = affecteObject.optJSONObject("DeletedNode");
                                            if (deletedNodeObject != null) {
                                                JSONObject finalFieldsObject = deletedNodeObject.optJSONObject("FinalFields");
                                                if (finalFieldsObject != null) {
                                                    String accountStr = finalFieldsObject.optString("Account");
                                                    if (!TextUtils.isEmpty(accountStr)) {
                                                        JSONObject takerGetsObject = finalFieldsObject.optJSONObject("TakerGets");
                                                        //判断当前监听的类型
                                                        if(takerGetsObject != null){
                                                            String coinType = takerGetsObject.optString("currency");
                                                            if (coinType.equals(CURRENCY_TYPE_CLC)) {
                                                                for (String add : mReceiveListWalletAddrs) {
                                                                    if (add.equals(accountStr)) {
                                                                        info.setTxId(txId);
                                                                        info.setType(HDTSdkApi.OFFER_TYPE_TRANSFER_IN);
                                                                        info.setOfferAddr(add);
                                                                        info.setCurrencyType(coinType);
                                                                        break;
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                            if(!TextUtils.isEmpty(info.getOfferAddr())){
                                                //是那些人给我对消了我的挂单？
                                                ArrayList<String> otherAccounts = new ArrayList<>();
                                                JSONObject modifiedNode = affecteObject.optJSONObject("ModifiedNode");
                                                if(modifiedNode != null){
                                                    JSONObject finalFields = modifiedNode.optJSONObject("FinalFields");
                                                    if(finalFields != null){
                                                        String otherAccount = finalFields.optString("Account");
                                                        if(!info.getOfferAddr().equals(otherAccount) && !TextUtils.isEmpty(otherAccount)){
                                                            otherAccounts.add(otherAccount);
                                                        }
                                                    }
                                                }
                                                info.setOtherOfferAddrs(otherAccounts);
                                                if(isBoth){
                                                    mReceiveListCallback.receiveListCallback(HDTSdkApi.CODE_SUCCESS, "", info);
                                                }else {
                                                    if(info.getCurrencyType().equals(mCurReceiveListCurrencyType)){
                                                        mReceiveListCallback.receiveListCallback(HDTSdkApi.CODE_SUCCESS, "", info);
                                                    }
                                                }
                                                mLastTxId = txId;
                                            }

                                    }

                                    }
                                }
                            }
                        }
                    }
                }
            }
        });
    }

    private synchronized boolean subscribeAccount(List<String> walletAddrs) {
        if (!isConnect() || walletAddrs==null || walletAddrs.size()<1) {
            ZXLogger.e(TAG, "subscribeAccount failed invalid param!");
            return false;
        }

        try {
            int size = walletAddrs.size();
            AccountID[] accountIDs = new AccountID[size];
            for(int i=0; i<size; i++){
                accountIDs[i] = AccountID.fromAddress(walletAddrs.get(i));
            }
            Request requestAccount = Chainsql.c.getConnection().getClient()
                    .subscribeAccount(accountIDs);

            Chainsql.c.getConnection().getClient().sendRequest(requestAccount);

            mIsSubscribeAccount = true;
            ZXLogger.i(TAG, "subscribeAccount");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private synchronized boolean unSubscribeAccount(List<String> walletAddrs) {
        if (!isConnect() || walletAddrs==null || walletAddrs.size()<1) {
            ZXLogger.e(TAG, "unSubscribeAccount failed invalid param!");
            return false;
        }

        try {
            int size = walletAddrs.size();
            AccountID[] accountIDs = new AccountID[size];
            for(int i=0; i<size; i++){
                accountIDs[i] = AccountID.fromAddress(walletAddrs.get(i));
            }
            Request requestAccount = Chainsql.c.getConnection().getClient()
                    .unsubscribeAccount(accountIDs);

            Chainsql.c.getConnection().getClient().sendRequest(requestAccount);

            mIsSubscribeAccount = false;
//        mWalletAddr = "";
//        mAccountTransactionCallback = null;
            ZXLogger.i(TAG, "unSubscribeAccount");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * websocket是否连接
     *
     * @return true 连接 false 未连接
     */
    private boolean isConnect() {
        Connection connection = Chainsql.c.getConnection();
        if (connection == null) {
            return false;
        }

        Client client = connection.getClient();
        if (client == null) {
            return false;
        }

        boolean connected = client.connected;
        if (!connected) {
            return false;
        }

        return true;
    }
}