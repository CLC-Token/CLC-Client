package com.btd.wallet.home.ui;

import android.os.Bundle;

import com.btd.wallet.base.activity.BaseSupportActivity;


/**
 * $Author: xhunmon
 * $Date: 2018-01-09
 * $Description: 米袋的Activity入口，根据当前状态进入哪一个fragment；
 *              比如：1.刚开始的时候是创建米袋；
 */

public class ProtocolActivity extends BaseSupportActivity {

    @Override
    protected void initView() {
    }

    @Override
    protected void initFragments(Bundle savedInstanceState) {
        super.initFragments(savedInstanceState);
        loadRootFragment(ProtocolFragment.newInstance());
    }
}
