package com.peersafe.hdtsdk.api;

/**
 * Description: 比特米SDK钱包挂单信息类   <br>
 * Author: cxh <br>
 * Date: 2019/3/28 11:20
 */
public class OfferInfo {
    //支付数量
    private String mTakerPays;
    //操作地址
    private String mAccount;
    //获取的数量
    private String mTakerGets;
    //交易id(hash)
    private String mTxId;
    // 交易类型
    private int mType;
    //创建挂单返回的id
    private long mSequence;

    public long getSequence() {
        return mSequence;
    }

    public void setSequence(long sequence) {
        mSequence = sequence;
    }

    public String getTxId() {
        return mTxId;
    }

    public void setTxId(String txId) {
        mTxId = txId;
    }

    public String getTakerPays() {
        return mTakerPays;
    }

    public void setTakerPays(String takerPays) {
        mTakerPays = takerPays;
    }

    public String getAccount() {
        return mAccount;
    }

    public void setAccount(String account) {
        mAccount = account;
    }

    public String getTakerGets() {
        return mTakerGets;
    }

    public void setTakerGets(String takerGets) {
        mTakerGets = takerGets;
    }

    public int getType() {
        return mType;
    }

    public void setType(int type) {
        mType = type;
    }


    @Override
    public String toString() {
        return "OfferInfo{" +
                ", mTxId='" + mTxId + '\'' +
                ", mType=" + mType +
                ", mSequence=" + mSequence +
                '}';
    }
}
