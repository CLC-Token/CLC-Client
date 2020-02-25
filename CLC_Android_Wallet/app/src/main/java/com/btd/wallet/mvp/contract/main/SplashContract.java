package com.btd.wallet.mvp.contract.main;

import com.btd.library.base.mvp.ipersenter.IBasePresenter;
import com.btd.library.base.mvp.ipersenter.IBaseView;


/**
 * Description: 闪屏页   <br>
 * Author: cxh <br>
 * Date: 2019/3/21 14:14
 */
public interface SplashContract {
    interface IView extends IBaseView {


        void skipSplash();
    }

    interface IPresenter extends IBasePresenter {
        void loadSpash();
    }
}
