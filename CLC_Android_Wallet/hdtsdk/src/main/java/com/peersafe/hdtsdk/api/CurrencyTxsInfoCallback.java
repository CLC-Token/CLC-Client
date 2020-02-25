/**************************************************************************/
/**
 * @file CurrencyTxsInfoCallback.java
 * @brief 交易明细信息的回调类
 * @details 交易明细信息的回调类
 * @author zhangyang
 * @version 1.0
 * @date 2018-1-4
 ******************************************************************************
 * Copyright (C) 2018 PeerSafe Technologies. All rights reserved.
 ******************************************************************************/

package com.peersafe.hdtsdk.api;

/**
 * @class CurrencyTxsInfoCallback
 * @brief 交易明细信息的回调类
 */
public interface CurrencyTxsInfoCallback {
    /**
     * 交易明细信息的回调
     * @param code 获取结果
     * @param message 错误信息，只有发生错误时该字段才有值，正确时该字段为空
     * @param currencyTxDetails 比特米的交易明细列表
     */
    void currencyTxsInfo(int code, String message, CurrencyTxDetails currencyTxDetails);
}
