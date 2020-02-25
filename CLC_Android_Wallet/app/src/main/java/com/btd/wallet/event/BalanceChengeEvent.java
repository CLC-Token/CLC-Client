package com.btd.wallet.event;

/**
 * Description:  钱包余额发生变化  <br>
 * Author: cxh <br>
 * Date: 2019/7/5 14:17
 */
public class BalanceChengeEvent {
    private String address;

    public BalanceChengeEvent(String address) {
        this.address = address;
    }
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
