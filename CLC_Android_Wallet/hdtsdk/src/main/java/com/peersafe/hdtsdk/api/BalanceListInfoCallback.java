/**************************************************************************/
/**
 * @file BalanceInfoCallback.java
 * @brief 余额信息的回调类
 * @details 余额信息的回调类
 * @author zhangyang
 * @version 1.0
 * @date 2018-1-3
 ******************************************************************************
 * Copyright (C) 2018 PeerSafe Technologies. All rights reserved.
 ******************************************************************************/

package com.peersafe.hdtsdk.api;

/**
 * @class BalanceListInfoCallback
 * @brief 同时查询hdt和ist余额信息的回调类
 */
public interface BalanceListInfoCallback {
    /**
     * 余额信息回调
     * @param code 交易结果 0 成功 其他：失败
     * @param message 错误信息，只有发生错误时该字段才有值，正确时该字段为空
     * @param info 余额列表
     */
    void balanceInfo(int code, String message, BalanceListInfo info);
}
