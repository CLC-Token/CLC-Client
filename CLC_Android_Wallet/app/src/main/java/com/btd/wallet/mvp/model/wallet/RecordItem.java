package com.btd.wallet.mvp.model.wallet;

/**
 * $Author: xhunmon
 * $Date: 2018-01-10
 * $Description:
 */
public class RecordItem {
    private String date;
    private String balance;
    private String type;
    private String addr;
    private String hash;
    private boolean isLineShow;
    private String fee;
    private int coinType;
    private String systemAddressDesc;

    public String getSystemAddressDesc() {
        return systemAddressDesc;
    }

    public void setSystemAddressDesc(String systemAddressDesc) {
        this.systemAddressDesc = systemAddressDesc;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public int getCoinType() {
        return coinType;
    }

    public void setCoinType(int coinType) {
        this.coinType = coinType;
    }

    public boolean isLineShow() {
        return isLineShow;
    }

    public void setLineShow(boolean lineShow) {
        isLineShow = lineShow;
    }

    public RecordItem(){}

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getDate() {
        return date;
    }

    public String getBalance() {
        return balance;
    }

    public String getType() {
        return type;
    }

    public String getAddr() {
        return addr;
    }

    @Override
    public String toString() {
        return "RecordItem{" +
                "date='" + date + '\'' +
                ", balanceHdt='" + balance + '\'' +
                ", type='" + type + '\'' +
                ", addr='" + addr + '\'' +
                '}';
    }
}