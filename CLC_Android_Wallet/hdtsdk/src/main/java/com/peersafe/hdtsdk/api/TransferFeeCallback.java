/**************************************************************************/
/**
 * @file TransferFeeCallback.java
 * @brief 转账手续费信息的回调类
 * @details 转账手续费信息的回调类
 * @author zhangyang
 * @version 1.0
 * @date 2018-1-4
 ******************************************************************************
 * Copyright (C) 2018 PeerSafe Technologies. All rights reserved.
 ******************************************************************************/

package com.peersafe.hdtsdk.api;

/**
 * @class TransferFeeCallback
 * @brief 转账手续费信息的回调类
 */
public interface TransferFeeCallback {
    /**
     * 转账手续费信息回调
     * @param code 交易结果 0 成功 其他：失败
     * @param message 错误信息，只有发生错误时该字段才有值，正确时该字段为空
     * @param transferFee 转账手续费以字符串形式返回，上层进行转换
     */
    void transferFeeInfo(int code, String message, String transferFee);
}
