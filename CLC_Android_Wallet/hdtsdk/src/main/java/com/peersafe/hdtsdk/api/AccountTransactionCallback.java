/**************************************************************************/
/**
 * @file AccountTransactionCallback.java
 * @brief 钱包交易通知的回调接口类
 * @details 钱包交易通知的回调接口类
 * @author zhangyang
 * @version 1.0
 * @date 2018-1-3
 ******************************************************************************
 * Copyright (C) 2018 PeerSafe Technologies. All rights reserved.
 ******************************************************************************/

package com.peersafe.hdtsdk.api;

/**
 * @class AccountTransactionCallback
 * @brief 钱包交易通知的回调接口类
 */
public interface AccountTransactionCallback {
    /**
     * 钱包账户交易结果
     * @param code 交易结果 0 成功 其他：失败
     * @param message 错误信息，只有发生错误时该字段才有值，正确时该字段为空
     * @param transferInfo 转账信息
     */
    void accountTransactionResult(int code, String message, TransferInfo transferInfo);
}
