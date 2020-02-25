/**************************************************************************/
/**
 * @file CommonTransactionCallback.java
 * @brief 比特米SDK钱包普通交易（例如激活，信任）通知的回调类
 * @details 比特米SDK钱包普通交易（例如激活，信任）通知的回调类
 * @author zhangyang
 * @version 1.0
 * @date 2018-1-3
 ******************************************************************************
 * Copyright (C) 2018 PeerSafe Technologies. All rights reserved.
 ******************************************************************************/

package com.peersafe.hdtsdk.api;

/**
 * @class CommonTransactionCallback
 * @brief 比特米SDK钱包普通交易（例如激活，信任）通知的回调类
 */
public interface CommonTransactionCallback {
    /**
     * 钱包普通交易回调结果
     * @param code 交易结果 0 成功 其他：失败
     * @param message 错误信息，只有发生错误时该字段才有值，正确时该字段为空
     * @param commonTransInfo 交易信息
     */
    void transactionResult(int code, String message, CommonTransInfo commonTransInfo);
}
