package com.btd.wallet.mvp.model.wallet;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * $Author: 杨紫员
 * $Date: 2018-05-02
 * $Description: btr米袋的表
 */
public class WalletConfig extends DataSupport implements Serializable {
    public int id;
    /*账号名称：账号1*/
    public String nickName;
    /*账号地址的二维码，实现扫描二维码跳转到转账页面*/
    public String code;
    /*Hdt账号余额*/
    public String balanceHdt;
    /*Ist账号余额*/
    public String balanceBtd;
    /*账号的米袋私钥*/
    public String privateKey;
    /*账号的米袋公钥*/
    public String publicKey;
    /*账号的米袋地址*/
    public String fromAddr;
    /*判断是哪个用的账号*/
//    public String accessId;
    /*判断该账户是否被冻结*/
    public boolean isFreeze;
    private boolean isFreezeBtd;
    private boolean isFreezeHdt;

    public String words;

    public String getWords() {
        return words;
    }

    public void setWords(String words) {
        this.words = words;
    }

    public boolean isFreezeBtd() {
        return isFreezeBtd;
    }

    public void setFreezeBtd(boolean freezeBtd) {
        isFreezeBtd = freezeBtd;
    }

    public boolean isFreezeHdt() {
        return isFreezeHdt;
    }

    public void setFreezeHdt(boolean freezeHdt) {
        isFreezeHdt = freezeHdt;
    }

    public String getBalanceHdt() {
        return balanceHdt;
    }

    public void setBalanceHdt(String balanceHdt) {
        this.balanceHdt = balanceHdt;
    }

    public String getBalanceBtd() {
        return balanceBtd;
    }

    public void setBalanceBtd(String balanceBtd) {
        this.balanceBtd = balanceBtd;
    }

    @Override
    public String toString() {
        return "WalletConfig{" +
                "id=" + id +
                ", nickName='" + nickName + '\'' +
                ", code='" + code + '\'' +
                ", balanceBtd='" + balanceBtd + '\'' +
                ", balanceHdt='" + balanceHdt + '\'' +
                ", privateKey='" + privateKey + '\'' +
                ", publicKey='" + publicKey + '\'' +
                ", fromAddr='" + fromAddr + '\'' +
//                ", accessId='" + accessId + '\'' +
                ", isFreeze=" + isFreeze +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getFromAddr() {
        return fromAddr;
    }

    public void setFromAddr(String fromAddr) {
        this.fromAddr = fromAddr;
    }

    public boolean isFreeze() {
        return isFreeze;
    }

    public void setFreeze(boolean freeze) {
        isFreeze = freeze;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null && obj instanceof WalletConfig && this == null){
            return true;
        }
        WalletConfig config = (WalletConfig) obj;
        return config != null && fromAddr != null && fromAddr.equals(config.getFromAddr());
    }
}
