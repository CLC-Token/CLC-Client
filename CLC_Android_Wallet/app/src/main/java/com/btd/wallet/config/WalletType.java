package com.btd.wallet.config;

/**
 * $Author: xhunmon
 * $Date: 2018-01-10
 * $Description:
 */

public interface WalletType {
    int CREATE = 0;//当前处于创建阶段

    int RECORD = 4;//交易记录
    int TRANSFER = 5;//CLC转账

    int DETAIL = 8;//账户的详情页


    int SDK_INIT = 101;//当前btrsdk状态为初始化
    int SDK_TRUST = 102;//当前btrsdk状态为 信任
    int SDK_BALANCE = 103;//当前btrsdk状态为 查询余额
    int SDK_TRANSFER = 104;//当前btrsdk状态为 转账
    int SDK_RECORD = 105;//当前btrsdk状态为 交易记录
    int SDK_SYSTEM = 106;//当前btrsdk状态为 查询系统余额
    int SDK_FEE = 107;//当前btrsdk状态为 查询转账手续费
    int SDK_RECORD_BACK = 108;//当前btrsdk状态为 查询转账发生变化
    int SDK_OFFER_CREATE = 109;//当前btrsdk状态为 创建挂单
    int SDK_OFFER_CANCEL = 110;//当前btrsdk状态为 取消挂单
    int SDK_OFFER_BOOKS = 111;//当前btrsdk状态为  查询挂单
    int SDK_COIN_BTD = 112;//当前btrsdk状态为  币种
    int SDK_COIN_HDT = 113;//当前btrsdk状态为  币种


    String privatekey = "privateKey";
    String name = "name";

    /*判断米袋模块的前缀*/
    String strWalletPre = "CMBTD://";
    String strIstPayPre = "btdw://pay?";     //支付前缀
    String strIstPayAllPre = "btdw://pay?payReqCode=";     //支付全部前缀
    String strHdtPayPre = "hdtw://pay?";     //支付前缀

    /*用来做文件进行异或的key*/
    String enFileKey = "WWW.CUME.CC/btd";

}
