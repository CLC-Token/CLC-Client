package com.btd.wallet.home.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.btd.wallet.pure.R;
import com.btd.wallet.base.fragment.BaseSupportFragment;
import com.btd.wallet.config.Constants;
import com.btd.wallet.config.SPKeys;


import butterknife.BindView;
import butterknife.OnClick;

/**
 * $Author: xhunmon
 * $Date: 2018-01-27
 * $Description: 是否同意创建米袋账户协议的页面
 */

public class ProtocolContentFragment extends BaseSupportFragment {

    @BindView(R.id.webView)
    WebView mWebView;
    private Runnable mShowLoadingTask;

    public static ProtocolContentFragment newInstance() {
        Bundle args = new Bundle();
        ProtocolContentFragment fragment = new ProtocolContentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_wallet_protocol;
    }

    @Override
    protected void initView() {
//        mShowLoadingTask = () -> PDialogUtil.startProgress(context, "", null);
//        x.task().postDelayed(mShowLoadingTask, 200);
//        startReadUrl("http://test.cume.cc/userAgt.html");//测试的
        startReadUrl(Constants.BIT_RICE_USER_AGT );
    }

    @OnClick(value = {R.id.btn_bottom})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.btn_bottom:
                agree();
                break;
        }
    }

    /**
     * 同意协议，返回为true
     */
    private void agree() {
        Intent intent = new Intent();
        intent.putExtra(SPKeys.isWalletAgree, true);
        mActivity.setResult(1235,intent);
        mActivity.onBackPressed();
    }

    // btn_login的触发事件 点击后 webView开始读取url
    protected void startReadUrl(String url) {
        // TODO Auto-generated method stub
        // webView加载web资源
        mWebView.loadUrl(url);
        // 覆盖webView默认通过系统或者第三方浏览器打开网页的行为
        // 如果为false调用系统或者第三方浏览器打开网页的行为
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                // webView加载web资源
                view.loadUrl(url);
                return true;
            }
        });
        // 启用支持javascript
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        // web加载页面优先使用缓存加载
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        // 当打开页面时 显示进度条 页面打开完全时 隐藏进度条
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    stopDialog();
                } else {

                }
                super.onProgressChanged(view, newProgress);
            }
        });
    }

    /**
     * 停止并移除对话框
     */
    private void stopDialog() {
//        if (mShowLoadingTask != null) {
//            x.task().removeCallbacks(mShowLoadingTask);
//        }
//        PDialogUtil.stopProgress();
    }
}
