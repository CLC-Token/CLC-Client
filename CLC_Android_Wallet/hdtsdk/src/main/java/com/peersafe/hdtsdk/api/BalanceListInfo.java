package com.peersafe.hdtsdk.api;

/**
 * Description:    <br>
 * Author: cxh <br>
 * Date: 2019/4/12 9:40
 */
public class BalanceListInfo {
    /**
     * Btd的余额
     */
    private String balanceBtd;
    /**
     * hdt的余额
     */
    private String balanceHdt;

    /**
     * btd是否冻结
     */
    private boolean isFreezePeerBtd;
    /**
     * hdt是否冻结
     */
    private boolean isFreezePeerHdt;

    public boolean isFreezePeerBtd() {
        return isFreezePeerBtd;
    }

    public void setFreezePeerBtd(boolean freezePeerBtd) {
        isFreezePeerBtd = freezePeerBtd;
    }

    public boolean isFreezePeerHdt() {
        return isFreezePeerHdt;
    }

    public void setFreezePeerHdt(boolean freezePeerHdt) {
        isFreezePeerHdt = freezePeerHdt;
    }

    public String getBalanceBtd() {
        return balanceBtd;
    }

    public void setBalanceBtd(String balanceBtd) {
        this.balanceBtd = balanceBtd;
    }

    public String getBalanceHdt() {
        return balanceHdt;
    }

    public void setBalanceHdt(String balanceHdt) {
        this.balanceHdt = balanceHdt;
    }

    @Override
    public String toString() {
        return "BalanceListInfo{" +
                "balanceBtd='" + balanceBtd + '\'' +
                ", balanceHdt='" + balanceHdt + '\'' +
                '}';
    }
}
