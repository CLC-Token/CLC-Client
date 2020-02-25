/**************************************************************************/
/**
 * @file CommonTransInfo.java
 * @brief 比特米SDK钱包普通交易（例如激活，信任）信息
 * @details 比特米SDK钱包普通交易（例如激活，信任）信息
 * @author zhangyang
 * @version 1.0
 * @date 2018-1-3
 ******************************************************************************
 * Copyright (C) 2018 PeerSafe Technologies. All rights reserved.
 ******************************************************************************/

package com.peersafe.hdtsdk.api;

/**
 * @class CommonTransInfo
 * @brief 比特米SDK钱包普通交易（例如激活，信任）信息
 */
public class CommonTransInfo {
    // 交易类型
    private int mType;

    // 交易id
    private String mTxId;

    public CommonTransInfo() {

    }

    public CommonTransInfo(int type, String txId) {
        mType = type;
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
        stringBuilder.append("type is:" + mType);
        stringBuilder.append(" ");
        stringBuilder.append("txId is:" + mTxId);
        return stringBuilder.toString();
    }
}
