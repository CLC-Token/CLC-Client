package com.btd.wallet.mvp.contract.wallet;


import com.btd.wallet.base.contact.ListContract;
import com.btd.wallet.mvp.model.wallet.WalletModel;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.math.BigDecimal;

/**
 * Description: 我的钱包   <br>
 * Author: cxh <br>
 * Date: 2019/6/25 11:56
 */
public interface WalletContract {

    interface IView extends ListContract.IListRefreshView<WalletModel> {

        void refreshItem(boolean finish, BigDecimal btd, BigDecimal hdt, boolean isEmpy);

        void showError();

        SmartRefreshLayout getRefrshLayout();

        void setStateVisiable(Boolean visiable);
    }

    interface IPresenter extends ListContract.IListRefreshPersenter {
        void onRefresh();
    }
}