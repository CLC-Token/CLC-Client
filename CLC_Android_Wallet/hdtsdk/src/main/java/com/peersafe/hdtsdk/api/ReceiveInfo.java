/**************************************************************************/
package com.peersafe.hdtsdk.api;

import java.util.List;

/**
 * Description: 接收到到用户账号余额发生变化的回调   <br>
 * Author: cxh <br>
 * Date: 2019/3/29 18:54
 */
public class ReceiveInfo {
    // 交易类型
    private int mType;

    // 转账的原地址
    private String mFromAddr;

    // 转账的目的地址
    private String mToAddr;

    // 挂单的地址
    private String mOfferAddr;

    //跟本地址兑换的地址；
    private List<String> mOtherOfferAddrs;

    // 转账的金额
    private String mAmount;

    // 交易id
    private String mTxId;

    //当前的币种名称： BTD/DHT
    private String mCurrencyType;

    public List<String> getOtherOfferAddrs() {
        return mOtherOfferAddrs;
    }

    public void setOtherOfferAddrs(List<String> otherOfferAddrs) {
        mOtherOfferAddrs = otherOfferAddrs;
    }

    public String getCurrencyType() {
        return mCurrencyType;
    }

    public void setCurrencyType(String currencyType) {
        mCurrencyType = currencyType;
    }

    public String getOfferAddr() {
        return mOfferAddr;
    }

    public void setOfferAddr(String offerAddr) {
        mOfferAddr = offerAddr;
    }

    public ReceiveInfo() {

    }

    public ReceiveInfo(int type, String fromAddr, String toAddr, String amount, String txId) {
        mType = type;
        mFromAddr = fromAddr;
        mToAddr = toAddr;
        mAmount = amount;
        mTxId = txId;
    }

    /**
     * 获取交易类型
     * @return int 交易类型
     */
    public int getType() {
        return mType;
    }

    /**
     * 设置交易类型
     * @param type 交易类型
     * @return NA
     */
    public void setType(int type) {
        mType = type;
    }

    /**
     * 获取转账的原地址
     * @return String 转账的原地址
     */
    public String getFromAddr() {
        return mFromAddr;
    }

    /**
     * 设置转账的原地址
     * @param fromAddr 转账的原地址
     * @return NA
     */
    public void setFromAddr(String fromAddr) {
        mFromAddr = fromAddr;
    }

    /**
     * 获取转账的目的地址
     * @return String 转账的原地址
     */
    public String getToAddr() {
        return mToAddr;
    }

    /**
     * 设置转账的目的地址
     * @param toAddr 转账的目的地址
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
     * 获取交易id
     * @return String 交易id
     */
    public String getTxId() {
        return mTxId;
    }

    /**
     * 设置交易id
     * @param txId 交易id
     * @return NA
     */
    public void setTxId(String txId) {
        mTxId = txId;
    }


    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("type is:" + mType).append(" ");
        stringBuilder.append("fromAddr is:" + mFromAddr).append(" ");
        stringBuilder.append("toAddr is:" + mToAddr).append(" ");
        stringBuilder.append("amount is:" + mAmount).append(" ");
        stringBuilder.append("txId is:" + mTxId);
        return stringBuilder.toString();
    }
}
