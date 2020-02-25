package com.btd.wallet.home.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.btd.wallet.pure.R;
import com.btd.wallet.base.fragment.BackFragment;


/**
 * $Author: xhunmon
 * $Date: 2018-01-27
 * $Description: 标题管理的fragment
 */

public class ProtocolFragment extends BackFragment {


    public static ProtocolFragment newInstance() {
        Bundle args = new Bundle();
        ProtocolFragment fragment = new ProtocolFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initView() {
        setTitle(getStr(R.string.wallet_protocol_title));
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        loadRootFragment(ProtocolContentFragment.newInstance());
    }
}
