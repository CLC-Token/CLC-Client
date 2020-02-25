package com.btd.wallet.home.ui;

import android.os.Bundle;
import android.view.View;

import com.btd.wallet.pure.R;
import com.btd.wallet.base.fragment.BaseSupportFragment;

import butterknife.OnClick;

/**
 * $Author: xhunmon
 * $Date: 2018-01-09
 * $Description:
 */

public class WalletTransferFailedFragment extends BaseSupportFragment {


    public static WalletTransferFailedFragment newInstance() {
        Bundle args = new Bundle();
        WalletTransferFailedFragment fragment = new WalletTransferFailedFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initToolbar(View rootView) {
        super.initToolbar(rootView);
        setBack();
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_wallet_transfer_failed;
    }

    @Override
    protected void initView() {
        setTitle(getStr(R.string.wallet_transfer_failed_title));
    }

    @OnClick(value = {R.id.btn_again})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.btn_again://点击返回重试
                this.pop();
                break;
        }
    }

}
