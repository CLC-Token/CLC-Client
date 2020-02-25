package com.peersafe.hdtsdk.api;

/**
 * Description: 挂单结果回调   <br>
 * Author: cxh <br>
 * Date: 2019/3/28 11:39
 */
public interface OfferCallback {
    /**
     * 钱包账户交易结果
     * @param code 交易结果 0 成功 其他：失败
     * @param message 错误信息，只有发生错误时该字段才有值，正确时该字段为空
     * @param offerInfo 挂单信息
     */
    void offerResult(int code, String message, OfferInfo offerInfo);
}
