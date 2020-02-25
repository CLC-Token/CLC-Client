package com.btd.wallet.mvp.model.wallet;

import com.peersafe.hdtsdk.api.CurrencyTxDetails;

/**
 * $Author: xhunmon
 * $Date: 2018-01-12
 * $Description: 用于做btr米袋模块的event
 */

public class WalletEvent {
    /*1. 需要根据what来判定是那里来接收，如：what = WalletType.ME; 只针对个别需求设置为-1*/
    public int what;
    /*2. btrsdk执行状态，-1代码这不是进行btrsdk的操作，仅仅是页面间的传输*/
    public int sdkState;
    /*3. 初始化btrsdk时，要根据是否已经连接上然后再去查询余额等操作*/
    public boolean isConnected;
    /*3. 响应码*/
    public int code;
    /*4. 币种*/
    public int coinType;
    /*响应信息, 比如手续费等*/
    public String messag;
    /*账户CLC余额*/
    public String balance;
    /*同时查询Ist和hdt时，账户CLC余额*/
    public String balanceHdt;
    /*同时查询Ist和hdt时，账户CLC余额*/
    public String balanceBtd;
    /*同时查询Ist和hdt时，该币种是否被冻结*/
    public boolean isFreezeBtd;
    /*同时查询Ist和hdt时，该币种是否被冻结*/
    public boolean isFreezeHdt;
    /*发出的账户地址*/
    public String fromAddr;
    /*收入的账户地址*/
    public String toAddr;
    /*账户名称*/
    public String name;
    /*账户二维码*/
    public String strCode;

    /*sdk查询交易记录中需要到的*/
    public String marker;
    /*把整个对象传递过去*/
    public CurrencyTxDetails details;

    public String tradeHash;
    /**挂单完成返回的id，用来取消挂单*/
    public long sequence;
}
