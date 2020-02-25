/**
 * @file HDTSdkApi.java
 * @brief 比特米SDK相关的主调api
 * @details 向上层提供的主调api接口
 * @author zhangyang
 * @version 1.0
 * @date 2018-1-3
 * *****************************************************************************
 * Copyright (C) 2018 PeerSafe Technologies. All rights reserved.
 ******************************************************************************/
/**
 * @file HDTSdkApi.java
 * @brief 比特米SDK相关的主调api
 * @details 向上层提供的主调api接口
 * @author zhangyang
 * @version 1.0
 * @date 2018-1-3
 ******************************************************************************
 * Copyright (C) 2018 PeerSafe Technologies. All rights reserved.
 ******************************************************************************/

package com.peersafe.hdtsdk.api;

import com.peersafe.hdtsdk.inner.StringUtils;
import com.peersafe.hdtsdk.inner.ZXWalletManager;
import com.peersafe.hdtsdk.log.ZXLogger;

import java.util.List;

/**
 * @class HDTSdkApi
 * @brief 比特米SDK相关的主调api
 */
public class HDTSdkApi {
    /** 成功返回  */
    public final static int CODE_SUCCESS = 0;

    /** 失败返回 */
    public final static int CODE_FAIL = -1;

    /** 账户未激活 */
    public final static int CODE_ACCOUNT_NOT_ACTIVATE = 1;

    /** 账户未信任比特米 */
    public final static int CODE_ACCOUNT_NOT_TRUST = 2;

    /** 账户比特米被冻结 */
    public final static int CODE_ACCOUNT_HDT_FREEZED = 3;

    /** 钱包信任的交易类型 */
    public final static int TRANS_TYPE_TRUST = 1;

    /** 账户转入货币的交易类型 */
    public final static int TRANS_TYPE_TRANSFER_IN = 2;

    /** 账户转出货币的交易类型  */
    public final static int TRANS_TYPE_TRANSFER_OUT = 3;

    /** 账户挂单货币的交易类型  */
    public final static int OFFER_TYPE_TRANSFER_IN = 4;

    /**创建挂单类型*/
    public final static int OFFER_TYPE_CREATE = 1;

    /**取消挂单类型*/
    public final static int OFFER_TYPE_CANCEL = 2;

    /**查找挂单类型*/
    public final static int OFFER_TYPE_BOOK = 3;

    /**监听账户交易的变化*/
    public final static int RECEIVE_TYPE = 1;

    public enum CoinType {
//        HDT, BTD
         CLC
    }

    /**
     * SDK初始化
     * @param chainSqlNodeAddr chainsql节点地址
     * @param issueAddr 比特米发行的地址
     * @param connectDelegate chainsql连接状态代理回调
     * @return NA
     */
    public void sdkInit(String chainSqlNodeAddr,
                        String issueAddr, ConnectDelegate connectDelegate) {

        if (StringUtils.isEmpty(chainSqlNodeAddr)
                || StringUtils.isEmpty(issueAddr)
                || connectDelegate == null) {
            ZXLogger.e(HDTSdkApi.class.getSimpleName(), "sdkInit failed,param is invalid!");
            return;
        }
        ZXWalletManager.getInstance().sdkInit(chainSqlNodeAddr,
                issueAddr, connectDelegate);
    }

    /**
     * SDK关闭
     * @return NA
     */
    public void sdkClose() {
        ZXWalletManager.getInstance().sdkClose();
    }

    /**
     * 生成钱包
     * @return WalletInfo 钱包信息，返回空说明生成失败
     */
    public WalletInfo generateWallet() {
        return ZXWalletManager.getInstance().generateWallet();
    }

    /**
     * 生成钱包
     * @param secret 私钥
     * @return WalletInfo 钱包信息，返回空说明生成失败
     */
    public WalletInfo generateWallet(String secret) {
        return ZXWalletManager.getInstance().generateWallet(secret);
    }

    /**
     * 注册用户交易通知
     * @param walletAddr 钱包地址
     * @param subscribeResultCallback 注册账户交易结果异步回调
     * @param accountTransactionCallback 该账户钱包交易异步回调 （收到别人给我转账）
     */
    public void subscribeAccountTransaction(String walletAddr,
                                            SubscribeResultCallback subscribeResultCallback,
                                            AccountTransactionCallback accountTransactionCallback) {
        ZXWalletManager.getInstance().subscribeAccountTransaction(walletAddr,
                subscribeResultCallback,
                accountTransactionCallback);
    }

    private int lastCode;
    private static final int INIT_CODE = 101010;
    /**
     * 信任比特米
     * @param privateKey 钱包私钥（注意私钥与最近监听的账户钱包地址对应）
     * @param commonTransactionCallback 信任结果回调
     */
    public void trustIssueCurrency(String privateKey, final CommonTransactionCallback commonTransactionCallback) {
        lastCode = INIT_CODE;
        ZXWalletManager.getInstance().trustIssueCurrency(ZXWalletManager.CURRENCY_TYPE_CLC, privateKey, new CommonTransactionCallback() {
            @Override
            public void transactionResult(int i, String s, CommonTransInfo commonTransInfo) {
               // if(i==0){
                    commonTransactionCallback.transactionResult(i, s, commonTransInfo);
               // }
//                if(lastCode == INIT_CODE){//第一次
//                    if(i != 0){//如果信任失败直接返回
//                        commonTransactionCallback.transactionResult(i, s, commonTransInfo);
//                    }
//                }else {//这是第二次
//                    if(lastCode == 0){//如果上一次成功才返回（因为如果第一次失败则已经返回了）
//                        commonTransactionCallback.transactionResult(i, s, commonTransInfo);
//                    }
//                }
//                lastCode = i;
            }
        });
//        ZXWalletManager.getInstance().trustIssueCurrency(ZXWalletManager.CURRENCY_TYPE_BTD, privateKey, new CommonTransactionCallback() {
//            @Override
//            public void transactionResult(int i, String s, CommonTransInfo commonTransInfo) {
//                if(lastCode == INIT_CODE){//第一次
//                    if(i != 0){//如果信任失败直接返回
//                        commonTransactionCallback.transactionResult(i, s, commonTransInfo);
//                    }
//                }else {//这是第二次
//                    if(lastCode == 0){//如果上一次成功才返回（因为如果第一次失败则已经返回了）
//                        commonTransactionCallback.transactionResult(i, s, commonTransInfo);
//                    }
//                }
//                lastCode = i;
//            }
//        });
    }

    /**
     * 转账
     * @param privateKey 钱包私钥（注意私钥与最近监听的账户钱包地址对应）
     * @param toWalletAddr 目标地址
     * @param amount 转账金额
     * @param accountTransactionCallback 转账结果回调,异步返回结果
     */
    public void transferCurrency(CoinType coinType, String privateKey, String toWalletAddr, String amount,
                                 String remark, String type, AccountTransactionCallback accountTransactionCallback) {
        ZXWalletManager.getInstance().transferCurrency(getCurrencyType(coinType), privateKey, toWalletAddr
                , amount, remark, type, accountTransactionCallback);
    }

    public void offerCreate(CoinType coinType, String privateKey, String payAmount
            , String getyAmount, String remark ,String type
            , OfferCallback offerCallback) {
        ZXWalletManager.getInstance().offerCreate(getCurrencyType(coinType), privateKey, payAmount
                , getyAmount, remark, type, offerCallback);
    }

    public void offerCancel(CoinType coinType, String privateKey, String remark ,String type, long sequence
            , OfferCallback offerCallback) {
        ZXWalletManager.getInstance().offerCancel(getCurrencyType(coinType), privateKey
                , remark, type,sequence, offerCallback);
    }


    public void accountOffers(String walletAddr, int limit, String marker, AccountOfferCallback accountOfferCallback) {
        ZXWalletManager.getInstance().accountOffers(walletAddr, limit, marker, accountOfferCallback);
    }
    public void subscribe(CoinType coinType, List<String> walletAddrs,boolean isLisenOffer
            ,SubscribeResultCallback subscribeResultCallback, ReceiveListCallback receiveListCallback) {
        subscribe(coinType,walletAddrs,isLisenOffer,false, subscribeResultCallback,receiveListCallback);
    }

    /**
     * 监听交易通知
     * @param coinType      当前操作的币种（注：如果both为true则忽略该参数）
     * @param walletAddrs   监听的地址队列
     * @param isLisenOffer  是否监听挂单（现在挂单极难维护，以后估计不会再使用）
     * @param both          是否监听两种币种（HDT和BTD）
     * @param subscribeResultCallback   监听是否成功的通知
     * @param receiveListCallback       监听交易变化的通知（必须建立在subscribeResultCallback成功返回）
     */
    public void subscribe(CoinType coinType, List<String> walletAddrs,boolean isLisenOffer, boolean both
            ,SubscribeResultCallback subscribeResultCallback, ReceiveListCallback receiveListCallback) {
        ZXWalletManager.getInstance().subscribe(getCurrencyType(coinType), walletAddrs,isLisenOffer
                ,both, subscribeResultCallback,receiveListCallback);
    }

    /**
     * 获取钱包系统币
     * @param walletAddr 钱包地址
     * @param balanceInfoCallback 获取系统币以异步方式返回
     */
    public void getSysCoinBalance(String walletAddr, BalanceInfoCallback balanceInfoCallback) {
        ZXWalletManager.getInstance().getSysCoinBalance(walletAddr, balanceInfoCallback);
    }

    /**
     * 获取钱包比特米的余额
     * @param type 当前监听的币种（HDT或者BTD）
     * @param walletAddr 钱包地址
     * @param balanceInfoCallback 获取比特米余额以异步方式返回
     */
    public void getIssueCurrencyBalance(CoinType type, String walletAddr, BalanceInfoCallback balanceInfoCallback) {
        ZXWalletManager.getInstance().getIssueCurrencyBalance(getCurrencyType(type), walletAddr, balanceInfoCallback);
    }


    /**
     * 同时获取钱包两种币的余额
     * @param walletAddr 钱包地址
     * @param balanceInfoCallback 获取比特米余额以异步方式返回
     */
    public void getIssueCurrencyBalanceList(String walletAddr, BalanceListInfoCallback balanceInfoCallback) {
        ZXWalletManager.getInstance().getIssueCurrencyBalanceList(walletAddr, balanceInfoCallback);
    }

    /**
     * 获取钱包比特米交易明细
     * @param walletAddr 钱包地址
     * @param limit 本次获取的明细数目
     * @param marker 客户端初始请求时，该字段填空,当交易量较多时，服务端会分页返回记录，当服务端返回的marker字段有值时，则说明是分页返回
     * @param currencyTxsInfoCallback 交易明细信息回调
     */
    public void getIssueCurrencyTxDetail(String walletAddr, int limit, String marker,
                                         CurrencyTxsInfoCallback currencyTxsInfoCallback) {
        ZXWalletManager.getInstance().getIssueCurrencyTxDetail(walletAddr, limit, marker, currencyTxsInfoCallback);
    }

    /**
     * 获取转账手续费
     * @param transferFeeCallback 转账手续费信息的回调，异步方式返回
     */
    public void getTransferFee(TransferFeeCallback transferFeeCallback) {
        ZXWalletManager.getInstance().getTransferFee(transferFeeCallback);
    }

    /**
     * 判断地址是否合法
     * @param address  地址
     */
    public boolean isLegalAddress(String address) {
        return ZXWalletManager.getInstance().isLegalAddress(address);
    }

    /**
     * 获取sdk连接状态的接口
     * @return -1失败 0 成功 1连接中
     */
    public int getConnectState() {
        return ZXWalletManager.getInstance().getConnectState();
    }

    /**
     * 根据枚举获取当前的币种名称
     */
    public String getCurrencyType(CoinType type) {
       // return type == CoinType.HDT ? ZXWalletManager.CURRENCY_TYPE_BTP : ZXWalletManager.CURRENCY_TYPE_BTD;
        return ZXWalletManager.CURRENCY_TYPE_CLC;
    }
}
