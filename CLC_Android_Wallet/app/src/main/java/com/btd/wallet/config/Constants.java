package com.btd.wallet.config;



/**
 * Created by yzy on 2018/10/23 17:51
 */

public interface Constants {


    String Appkey = "31DB9F24AE14A5F05F425B68DDECFB4E";

    String USER_AGT = "https://bitrice.io/userAgt_new";

    String ETH_RECORD_URL = "https://etherscan.io/address/";

    //BTD免责声明（和之前btr的一样）
    String BIT_RICE_USER_AGT = "https://bitrice.io/bitdisk_agreement";
    //后台挂单的总地址
    String EXCHANGE_ACCOUNT = "zH1z5cXrjxVchDDnJzvPJRYbAHuSe3t5h5";

    /**
     * 常见问题(用户帮助)
     */
    String URL_HELP = "https://bitrice.io/help";


    // 是否第一次app启动
    String isLaunch = "isLaunch";


    String oauth2 = "oauth2:";
    String btdwalletRsp = "btdwalletRsp://";
    String scanLogin = "wallet-shop-login:";


    interface Protcols {
        String HTTP = "http://";
        String HTTPS = "https://";

        /**
         * 本地文件协议
         */
        String FILE = "file://";
    }



    interface SharedPrefrences {
        String cookie_share = "cookie_share";
        String cookie_key = "cookie_key";
        String cookies_size = "cookie_size";
    }


}
