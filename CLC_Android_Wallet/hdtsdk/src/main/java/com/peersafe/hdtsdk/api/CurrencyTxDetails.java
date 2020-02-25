/**************************************************************************/
/**
 * @file CurrencyTxDetails.java
 * @brief 交易明细列表
 * @details 交易明细列表
 * @author zhangyang
 * @version 1.0
 * @date 2018-1-4
 ******************************************************************************
 * Copyright (C) 2018 PeerSafe Technologies. All rights reserved.
 ******************************************************************************/

package com.peersafe.hdtsdk.api;

import java.util.ArrayList;

/**
 * @class CurrencyTxDetails
 * @brief 交易明细列表
 */
public class CurrencyTxDetails {
    // 当交易量较多时，服务端会分页返回记录，当服务端返回的marker字段有值时，
    // 则说明是分页返回，客户端初始请求时，该字段填空
    private String mMarker;

    // 交易明细详情列表
    private ArrayList<CurrencyTxDetail> mCurrencyTxDetailList;

    /**
     * 获取分页标记
     * @return String 分页标记
     */
    public String getMarker() {
        return mMarker;
    }

    /**
     * 设置分页标记
     * @param marker 分页标记
     * @return NA
     */
    public void setMarker(String marker) {
        mMarker = marker;
    }

    /**
     * 获取交易明细详情列表
     * @return ArrayList<CurrencyTxDetail> 交易明细详情列表
     */
    public ArrayList<CurrencyTxDetail> getCurrencyTxDetailList() {
        return mCurrencyTxDetailList;
    }

    /**
     * 设置交易明细详情列表
     * @param currencyTxDetailList 交易明细详情列表
     * @return NA
     */
    public void setCurrencyTxDetailList(ArrayList<CurrencyTxDetail> currencyTxDetailList) {
        mCurrencyTxDetailList = currencyTxDetailList;
    }
}
