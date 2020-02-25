/**************************************************************************/
/**
 * @file ConnectDelegate.java
 * @brief chainsql连接状态的回调接口类
 * @details chainsql连接状态的回调接口类
 * @author zhangyang
 * @version 1.0
 * @date 2018-1-3
 ******************************************************************************
 * Copyright (C) 2018 PeerSafe Technologies. All rights reserved.
 ******************************************************************************/

package com.peersafe.hdtsdk.api;

/**
 * @class ConnectDelegate
 * @brief chainsql连接状态的回调接口类
 */
public interface ConnectDelegate {
    int CONNECT_FAIL = -1;
    int CONNECT_SUCCESS = 0;
    int CONNECT_CONNECTING = 1;

    /**
     * 连接状态
     * @param connectState 连接状态 -1：失败 0：成功 1：连接中
     */
    void connectState(int connectState);
}
