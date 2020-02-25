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

public class WalletTransferSuccessFragment extends BaseSupportFragment {

    public static WalletTransferSuccessFragment newInstance() {
        Bundle args = new Bundle();
        WalletTransferSuccessFragment fragment = new WalletTransferSuccessFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_wallet_transfer_success;
    }

    @Override
    protected void initToolbar(View rootView) {
        super.initToolbar(rootView);
        setBack();
    }

    @Override
    protected void initView() {
        setTitle(getStr(R.string.wallet_transfer_success_title));
    }

    @OnClick(value = {R.id.btn_back})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.btn_back://点击返回
                pop();
                break;
        }
    }

}
