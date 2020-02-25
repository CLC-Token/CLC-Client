package com.btd.wallet.base.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import com.btd.wallet.base.fragment.WebFragment;


/**
 * $Author: xhunmon
 * $Date: 2018-01-09
 * $Description: 米袋的Activity入口，根据当前状态进入哪一个fragment；
 *              比如：1.刚开始的时候是创建米袋；
 */

public class WebActivity extends BaseSupportActivity {

    WebFragment webFragment;

    @Override
    protected void initView() {
    }

    @Override
    protected void initFragments(Bundle savedInstanceState) {
        super.initFragments(savedInstanceState);
        Intent intent = getIntent();
        if(intent.hasExtra("data")){
            webFragment = WebFragment.newInstance(intent.getIntExtra("type" , 0) , intent.getStringExtra("data"));
        }else {
            webFragment = WebFragment.newInstance(getIntent().getIntExtra("type", 0));
        }
        loadRootFragment(webFragment);
    }

    // 如果不做任何处理，浏览网页，点击系统“Back”键，整个Browser会调用finish()而结束自身，
    // 如果希望浏览的网 页回退而不是推出浏览器，需要在当前Activity中处理并消费掉该Back事件。
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webFragment.canBack()) {
            webFragment.goBack();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }
}
