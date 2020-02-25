package com.peersafe.hdtsdk.api;

/**
 * Description: 接收到到用户账号余额发生变化的回调   <br>
 * Author: cxh <br>
 * Date: 2019/3/29 18:54
 */
public interface ReceiveListCallback {
    void receiveListCallback(int code, String message, ReceiveInfo receiveInfo);
}
