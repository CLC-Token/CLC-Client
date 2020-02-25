package com.btd.wallet.base.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.webkit.DownloadListener;

import com.btd.library.base.util.LogUtils;


/**
 * @创建者 廖林涛
 * @创时间 2016年8月1日 下午3:21:58
 * @描述 webview调用系统浏览器下载文件
 *
 * @版本 $Rev: 13538 $
 * @更新者 $Author$
 * @更新时间 $Date: 2019-01-29 17:14:05 +0800 (周二, 29 一月 2019) $
 * @更新描述 TODO
 */
public class WebViewDownLoadListener implements DownloadListener {

  private Context context;

  public WebViewDownLoadListener(Context context) {
    super();
    this.context = context;
  }

  @Override
  public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,
                              long contentLength) {
    try {
      Uri uri = Uri.parse(url);
      Intent intent = new Intent(Intent.ACTION_VIEW, uri);
      if (null != context) {
        context.startActivity(intent);
      } else {
        LogUtils.e("WebViewDownLoadListener Context is null");
      }
    }catch (Exception e){
      LogUtils.e("onDownloadStart "+e.getMessage());
    }
  }

}
