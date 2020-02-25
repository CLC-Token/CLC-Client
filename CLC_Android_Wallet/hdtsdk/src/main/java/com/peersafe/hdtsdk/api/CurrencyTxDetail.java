/**************************************************************************/
/**
 * @file CurrencyTxDetail.java
 * @brief 单个交易明细信息
 * @details 单个交易明细信息
 * @author zhangyang
 * @version 1.0
 * @date 2018-1-4
 ******************************************************************************
 * Copyright (C) 2018 PeerSafe Technologies. All rights reserved.
 ******************************************************************************/

package com.peersafe.hdtsdk.api;

import java.util.Date;

/**
 * @class CurrencyTxDetail
 * @brief 单个交易明细信息类
 */
public class CurrencyTxDetail {
    // 交易id
    private String mTxId;

    // 交易类型
    private int mTxType;

    // 转账发送方
    private String mFromAddr;

    // 转账接收方
    private String mToAddr;

    // 转账金额
    private String mAmount;

    // 转账时间
    private Date mDate;

    // 转账手续费
    private String mTransferFee;
    //当前操作的币种
    private String coinType;

    public String getCoinType() {
        return coinType;
    }

    public void setCoinType(String coinType) {
        this.coinType = coinType;
    }

    /**
     * 获取交易id
     * @return String 交易id
     */
    public String getTxId() {
        return mTxId;
    }

    /**
     * 设置交易的id
     * @param txId 交易的id
     * @return NA
     */
    public void setTxId(String txId) {
        mTxId = txId;
    }

    /**
     * 获取交易类型
     * @return int 交易类型
     */
    public int getTxType() {
        return mTxType;
    }

    /**
     * 设置交易的id
     * @param txType 交易类型
     * @return NA
     */
    public void setTxType(int txType) {
        mTxType = txType;
    }

    /**
     * 获取转账发送方
     * @return String 转账发送方
     */
    public String getFromAddr() {
        return mFromAddr;
    }

    /**
     * 设置转账的金额
     * @param fromAddr 转账发送方
     * @return NA
     */
    public void setFromAddr(String fromAddr) {
        mFromAddr = fromAddr;
    }

    /**
     * 获取转账接收方
     * @return String 转账接收方
     */
    public String getToAddr() {
        return mToAddr;
    }

    /**
     * 设置转账接收方
     * @param toAddr 转账接收方
     * @return NA
     */
    public void setToAddr(String toAddr) {
        mToAddr = toAddr;
    }

    /**
     * 获取转账的金额
     * @return String 转账的金额
     */
    public String getAmount() {
        return mAmount;
    }

    /**
     * 设置转账的金额
     * @param amount 转账的金额
     * @return NA
     */
    public void setAmount(String amount) {
        mAmount = amount;
    }

    /**
     * 获取转账的实际
     * @return String 转账的时间
     */
    public Date getDate() {
        return mDate;
    }

    /**
     * 设置转账的时间
     * @param date 转账的时间
     * @return NA
     */
    public void setDate(Date date) {
        mDate = date;
    }

    /**
     * 获取转账的手续费
     * @return String 转账的手续费
     */
    public String getTransferFee() {
        return mTransferFee;
    }

    /**
     * 设置转账的手续费
     * @param transferFee 转账的手续费
     * @return NA
     */
    public void setTransferFee(String transferFee) {
        mTransferFee = transferFee;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("txId:" + mTxId)
                .append(" txType:" + mTxType)
                .append(" fromAddr:" + mFromAddr)
                .append(" toAddr:" + mToAddr)
                .append(" amount:" + mAmount)
                .append(" date:" + mDate)
                .append(" transferFee:" + mTransferFee);
        return stringBuilder.toString();
    }
}
