package com.btd.wallet.mvp.contract.wallet;


import android.widget.Button;
import android.widget.EditText;

import com.btd.library.base.mvp.ipersenter.IBasePresenter;
import com.btd.library.base.mvp.ipersenter.IBaseView;

/**
 * Description: 导入钱包   <br>
 * Author: cxh <br>
 * Date: 2019/7/2 11:56
 */
public interface ImportContract {

    interface IView extends IBaseView {
        EditText getEt();
        EditText getEtAgain();
        EditText getEtPrivate();
        Button getBtnOk();
        int currentType();

        void finish(boolean toMainFragment);
    }

    interface IPresenter extends IBasePresenter {
        void btnOk();
    }
}