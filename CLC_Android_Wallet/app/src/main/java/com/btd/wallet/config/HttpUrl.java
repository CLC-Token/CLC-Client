package com.btd.wallet.config;


import com.btd.wallet.pure.BuildConfig;

/**
 * Created by yzy on 2018/10/24 15:04
 */

public interface HttpUrl {
    interface URL {
//        String HOST = (BuildConfig.isRelease ? ("https://walletapi.bitdisk.io/")
////                : (BuildConfig.isDevelopment ? ("http://112.94.31.55:3117/")
//                : (BuildConfig.isDevelopment ? ("http://112.94.31.55:3115/")
//                : ("https://walletapitest.bitdisk.io/")));



        String WALLET_HOST = (BuildConfig.isRelease ? ("http://www.cifcula.com/clc-api/")
                : (BuildConfig.isDevelopment ? ("http://www.cifcula.com/clc-api/")
                : ("http://www.cifcula.com/clc-api/")));
        //升级host
        String UPDATE_HOST = (BuildConfig.isRelease ? ("http://www.cifcula.com/")
                : (BuildConfig.isDevelopment ? ("http://www.cifcula.com/")
                : ("http://www.cifcula.com/")));



        //激活钱包
        String activateWallet = WALLET_HOST + "activate-wallet";





    }

    interface CmdName {
        String activateWallet = "activateWallet";

        String create = "create";

        String cancel = "cancel";

        String config = "config";

        String list = "list";


        String preErcToBtd = "pre";

        String modify = "modify";

    }


    interface Config {

        /**
         * 私家云,接口加密用,明文,在数据包中显示携带
         */
        String APPID = "70076467277E4B1DD42F21B4DB5BD5A7";

    }
}
