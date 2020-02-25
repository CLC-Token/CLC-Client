package com.peersafe.hdtsdk.api;

import com.peersafe.hdtsdk.inner.AccountOfferModel;

/**
 * Description:  查询当前账户挂单队列（没有区分HDT或IST）    <br>
 * Author: cxh <br>
 * Date: 2019/4/1 16:33
 */
public interface AccountOfferCallback {
    /**
     * 钱包账户交易结果
     * @param code 交易结果 0 成功 其他：失败
     * @param message 错误信息，只有发生错误时该字段才有值，正确时该字段为空
     * @param offerInfo 挂单信息
     */
    void accountOfferCallback(int code, String message, AccountOfferModel offerInfo);
}
