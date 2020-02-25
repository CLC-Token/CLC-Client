/**************************************************************************/
/**
 * @file SubscribeResultCallback.java
 * @brief 注册账户通知结果的回调类
 * @details 注册账户通知结果的回调类
 * @author zhangyang
 * @version 1.0
 * @date 2018-1-23
 ******************************************************************************
 * Copyright (C) 2018 PeerSafe Technologies. All rights reserved.
 ******************************************************************************/

package com.peersafe.hdtsdk.api;

/**
 * @class SubscribeResultCallback
 * @brief 注册账户通知结果的回调类
 */
public interface SubscribeResultCallback {
    /**
     * 注册账户通知结果的回调
     * @param code 结果 0 成功 其他：失败
     * @param message 错误信息，只有发生错误时该字段才有值，正确时该字段为空
     * @param result 注册结果 0 成功 -1 失败
     */
    void subscribeResult(int code, String message, int result);
}
