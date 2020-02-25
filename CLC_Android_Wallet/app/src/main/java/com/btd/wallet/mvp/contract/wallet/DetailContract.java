package com.btd.wallet.mvp.contract.wallet;


import android.graphics.Bitmap;

import com.btd.library.base.mvp.ipersenter.IBasePresenter;
import com.btd.library.base.mvp.ipersenter.IBaseView;
import com.btd.wallet.mvp.model.wallet.WalletConfig;


/**
 * Description: 单个钱包详情页   <br>
 * Author: cxh <br>
 * Date: 2019/7/23 15:00
 */
public interface DetailContract {

    interface IView extends IBaseView {

        void showInit(WalletConfig config);
    }

    interface IPresenter extends IBasePresenter {
        Bitmap getCode();

        void rename(String name);

        WalletConfig getCofig();
    }
}