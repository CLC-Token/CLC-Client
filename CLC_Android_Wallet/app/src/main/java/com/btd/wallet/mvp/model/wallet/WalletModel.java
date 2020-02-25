package com.btd.wallet.mvp.model.wallet;

import java.math.BigDecimal;

/**
 * Description:  钱包主页的bean  <br>
 * Author: cxh <br>
 * Date: 2019/6/25 11:39
 */
public class WalletModel {
    public int id;


    private String address;
    private String nickName;
    private String strCode;
    private String localPrivate;
    private boolean isLoadBalance;
    private String balanceBtd;
    private String balanceHdt;
    private boolean freezeBtd;
    private boolean freezeHdt;
    private BigDecimal balanceBtdBigDecimal;
    private BigDecimal balanceHdtBigDecimal;

    private String words;

    public String getWords() {
        return words;
    }

    public void setWords(String words) {
        this.words = words;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLocalPrivate() {
        return localPrivate;
    }

    public void setLocalPrivate(String localPrivate) {
        this.localPrivate = localPrivate;
    }

    public BigDecimal getBalanceBtdBigDecimal() {
        return balanceBtdBigDecimal;
    }

    public BigDecimal getBalanceHdtBigDecimal() {
        return balanceHdtBigDecimal;
    }

    public void setBalanceBtdBigDecimal(BigDecimal balanceBtdBigDecimal) {
        this.balanceBtdBigDecimal = balanceBtdBigDecimal;
    }

    public void setBalanceHdtBigDecimal(BigDecimal balanceHdtBigDecimal) {
        this.balanceHdtBigDecimal = balanceHdtBigDecimal;
    }

    public String getStrCode() {
        return strCode;
    }

    public void setStrCode(String strCode) {
        this.strCode = strCode;
    }

    public boolean isLoadBalance() {
        return isLoadBalance;
    }

    public void setLoadBalance(boolean loadBalance) {
        isLoadBalance = loadBalance;
    }

    public String getBalanceBtd() {
        return balanceBtd;
    }

    public void setBalanceBtd(String balanceBtd) {
        if(balanceBtd==null||balanceBtd.equals("null")){
            balanceBtd="0";
        }
        this.balanceBtdBigDecimal=new BigDecimal(balanceBtd);
        this.balanceBtd = balanceBtd;
    }

    public String getBalanceHdt() {
        return balanceHdt;
    }

    public void setBalanceHdt(String balanceHdt) {
        if(balanceHdt==null||balanceHdt.equals("null")){
            balanceHdt="0";
        }
        this.balanceHdtBigDecimal=new BigDecimal(balanceHdt);
        this.balanceHdt = balanceHdt;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public boolean isFreezeBtd() {
        return freezeBtd;
    }

    public void setFreezeBtd(boolean freezeBtd) {
        this.freezeBtd = freezeBtd;
    }

    public boolean isFreezeHdt() {
        return freezeHdt;
    }

    public void setFreezeHdt(boolean freezeHdt) {
        this.freezeHdt = freezeHdt;
    }
}
