package com.btd.wallet.mvp.contract.wallet;


import android.widget.Button;
import android.widget.EditText;

import com.btd.library.base.mvp.ipersenter.IBasePresenter;
import com.btd.library.base.mvp.ipersenter.IBaseView;

import java.util.List;

/**
 * Description: 创建钱包   <br>
 * Author: cxh <br>
 * Date: 2019/7/2 11:56
 */
public interface CreateContract {

    interface IView extends IBaseView {
        EditText getEt();
        EditText getEtAgain();
        Button getBtnCreate();

        void success(String address, String privateKey, List<String> words);

        void creatting();

        void createFail();
    }

    interface IPresenter extends IBasePresenter {

        void btnCreate();
    }
}