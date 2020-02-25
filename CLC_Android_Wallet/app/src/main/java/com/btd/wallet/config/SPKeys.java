package com.btd.wallet.config;

/**
 * 项目中需要使用的常量
 * Created by yzy on 2018/10/24 17:41
 */

public interface SPKeys {

    String wallet = "wallet";

    /*存储用户创建的米袋javabeen string集合, 分别是mPrivateKey，mPublicKey,mWalletAddr*/
    String walletInfo = "walletInfo";
    /*用来判断入口是否第一次创建米袋*/
    String isWalletFirst = "isWalletFirst";

    String isupdate = "isupdate";
    String walletAddr = "walletAddr";

    String privatekey = "privatekey";

    String walletScanAddr = "walletScanAddr";

    String isWalletAgree = "isWalletAgree";

    String DATA = "DATA";
    String language = "language";//语言
    String config = "config";//预申请

    String coinType = "coinType";

    String USER_SETTING_DATA = "user_setting_data";

    String IS_FIRST_OPEN = "isFirstOpen";

    String CLIENT_UUID = "clientUUID";  //客户端唯一标识

    String fromType = "fromType";
    String SplashListResp = "SplashListResp";
    String scanCode = "scanCode";

    String lastBalanceTime = "lastBalanceTime";       //上次请求余额的时间
}
