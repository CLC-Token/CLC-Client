/**************************************************************************/
/**
 * @file WalletInfo.java
 * @brief 比特米SDK钱包信息类
 * @details 比特米SDK钱包信息类
 * @author zhangyang
 * @version 1.0
 * @date 2018-1-3
 ******************************************************************************
 * Copyright (C) 2018 PeerSafe Technologies. All rights reserved.
 ******************************************************************************/

package com.peersafe.hdtsdk.api;

import java.util.List;

/**
 * @class WalletInfo
 * @brief 比特米SDK钱包信息类
 */
public class WalletInfo {

    //钱包私钥
    private String mPrivateKey;

    //钱包公钥
    private String mPublicKey;

    //钱包地址
    private String mWalletAddr;

    private List<String> words;


    public List<String> getWords() {
        return words;
    }

    public void setWords(List<String> words) {
        this.words = words;
    }

    public WalletInfo() {

    }

    public WalletInfo(String privateKey, String publicKey, String walletAddr) {
        mPrivateKey = privateKey;
        mPublicKey = publicKey;
        mWalletAddr = walletAddr;
    }

    /**
     * 获取钱包私钥
     * @return String 钱包私钥
     */
    public String getPrivateKey() {
        return mPrivateKey;
    }

    /**
     * 设置钱包私钥
     * @param privateKey 钱包私钥
     * @return NA
     */
    public void setPrivateKey(String privateKey) {
        mPrivateKey = privateKey;
    }

    /**
     * 获取钱包公钥
     * @return  String 钱包公钥
     */
    public String getPublicKey() {
        return mPublicKey;
    }

    /**
     * 设置钱包公钥
     * @param publicKey 钱包公钥
     * @return NA
     */
    public void setPublicKey(String publicKey) {
        mPublicKey = publicKey;
    }


    /**
     * 获取钱包地址
     * @return  String 钱包地址
     */
    public String getWalletAddr() {
        return mWalletAddr;
    }

    /**
     * 设置钱包地址
     * @param walletAddr 钱包地址
     * @return NA
     */
    public void setWalletAddr(String walletAddr) {
        mWalletAddr = walletAddr;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("privateKey is:" + mPrivateKey).append(" ");
        stringBuilder.append("publicKey is:" + mPublicKey).append(" ");
        stringBuilder.append("walletAddr is:" + mWalletAddr);
        return stringBuilder.toString();
    }
}
