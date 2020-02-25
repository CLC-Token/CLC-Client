package com.btd.wallet.manager.hdtsdk;

/**
 * Description:  sdk连接变化的观察者  <br>
 * Author: cxh <br>
 * Date: 2019/7/5 9:34
 */
public interface ConnectObserver {
    void change(boolean connect);
}
