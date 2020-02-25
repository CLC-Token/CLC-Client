package com.btd.wallet.base.fragment;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.btd.library.base.util.LogUtils;
import com.btd.wallet.pure.R;
import com.btd.wallet.config.Constants;

import com.btd.wallet.utils.PDialogUtil;

import butterknife.BindView;


/**
 * $Author: xhunmon
 * $Date: 2018-01-27
 * $Description: 标题管理的fragment
 */

public class WebFragment extends BackFragment {


    @BindView(R.id.webView)
    WebView mWebView;

    public static WebFragment newInstance(int type) {
        Bundle args = new Bundle();
        args.putInt("type" , type);
        WebFragment fragment = new WebFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static WebFragment newInstance(int type , String data){
        Bundle args = new Bundle();
        args.putInt("type" , type);
        args.putString("data" , data);
        WebFragment fragment = new WebFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_web;
    }

    @Override
    protected void initView() {
        PDialogUtil.startProgress(mActivity, "", null);
        int type = getArguments().getInt("type" , 0);
        String url;
        if(type == 1){
            url = Constants.USER_AGT ;
            setTitle(R.string.use_idea);
        }else if(type == 3){
            url = Constants.ETH_RECORD_URL + getArguments().getString("data");
            setTitle(R.string.exchange_info);
        }else{
            url = Constants.URL_HELP ;
            setTitle(R.string.question);
        }
        startReadUrl(url);
    }

    // btn_login的触发事件 点击后 webView开始读取url
    @SuppressLint("SetJavaScriptEnabled")
    protected void startReadUrl(String url) {
        // TODO Auto-generated method stub

        WebSettings ws = mWebView.getSettings();

        ws.setJavaScriptEnabled(true);
        ws.setSupportMultipleWindows(true);

        //自适应屏幕
        ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        ws.setLoadWithOverviewMode(true);

        ws.setJavaScriptCanOpenWindowsAutomatically(true);
        ws.setDatabaseEnabled(true);
        ws.setDomStorageEnabled(true);
        ws.setDefaultTextEncodingName("utf-8");//设置编码格式

        ws.setAllowFileAccess(true);
        ws.setCacheMode(WebSettings.LOAD_NO_CACHE);
        ws.setAppCacheEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //允许https与http混合使用,在5.0以上不允许使用,设置该属性后为允许
            ws.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                LogUtils.i("url:" + url);
                super.onPageFinished(view, url);
            }

            @Override
            public void onReceivedError(final WebView view, int errorCode, String description, String failingUrl) {
                LogUtils.e("webview错误-->" + errorCode + "," + description);

                view.stopLoading();
                view.clearView();
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
                }
                super.onProgressChanged(view, newProgress);
            }
        });
        // webView加载web资源
        mWebView.loadUrl(url);
    }

    /**
     * 停止并移除对话框
     */
    private void stopDialog() {
        PDialogUtil.stopProgress();
    }

    private void finish(){
        if(getActivity()!=null){
            getActivity().finish();
        }
    }

    public void goBack(){
        mWebView.goBack();
    }

    public boolean canBack() {
        return mWebView.canGoBack();
    }

}
