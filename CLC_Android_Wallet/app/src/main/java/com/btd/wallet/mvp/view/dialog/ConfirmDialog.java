package com.btd.wallet.mvp.view.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.btd.library.base.util.LogUtils;
import com.btd.wallet.pure.R;
import com.btd.wallet.mvp.view.dialog.listener.DialogListener;

/**
 * 
 * 类名: ConfirmDialog</br>
 * 描述: <确认对话框/br> 发布版本号：</br>
 * 开发人员： 杨紫员</br>
 * 创建时间： 2018-11-1
 */
public class ConfirmDialog extends Dialog {
  private final Context context;
  private final String content;
  private final DialogListener listener;
  private String title;
  private String okButtonText;
  private String cancelButtonText;
  private boolean showCancel = true;
  public static boolean loginError = false;

  /**
   * 简单创建窗体方式(标题为默认、按钮为默认)
   * @param context 上下文
   * @param content 标题
   * @param listener 监听
   */
  public ConfirmDialog(Context context, String content, DialogListener listener) {
    this(context , null , content , listener);
  }

  /**
   * 自定义是否呈现取消按钮方法
   * @param context
   * @param content
   * @param listener
   * @param showCancel
   */
  public ConfirmDialog(Context context, String content, DialogListener listener, boolean showCancel) {
    this(context , null , content , listener);
    this.showCancel = showCancel;
  }

  /**
   * 自定义标题及取消按钮
   * @param context
   * @param title
   * @param content
   * @param listener
   * @param showCancel
   */
  public ConfirmDialog(Context context, String title, String content, DialogListener listener, boolean showCancel) {
    this(context , title , content , listener);
    this.showCancel = showCancel;
  }

  /**
   * 自定义标题及取消按钮
   * @param context
   * @param title
   * @param content
   * @param listener
   * @param showCancel
   */
  public ConfirmDialog(Context context, String title, String content, DialogListener listener, boolean showCancel , String oktitle) {
    this(context , title , content , listener);
    this.showCancel = showCancel;
    okButtonText = oktitle;
  }

  /**
   * 自定义标题
   * @param context
   * @param title
   * @param content
   * @param listener
   */
  public ConfirmDialog(Context context, String title, String content, DialogListener listener) {
    this(context , title , content , null , null , listener , true);
  }

  /**
   *
   * @param context
   * @param title
   * @param content
   * @param okButtonText
   * @param cancelButtonText
   * @param listener
   */
  public ConfirmDialog(Context context, String title, String content, String okButtonText, String cancelButtonText,
                       DialogListener listener) {
    this(context , title , content , okButtonText , cancelButtonText , listener , true);
  }

  /**
   * 完整参数
   * @param context
   * @param title
   * @param content
   * @param okButtonText
   * @param cancelButtonText
   * @param listener
   */
  public ConfirmDialog(Context context, String title, String content, String okButtonText, String cancelButtonText,
                       DialogListener listener , boolean showCancel) {
      super(context, R.style.FullScreenBaseDialog);
      setCanceledOnTouchOutside(false);
      this.showCancel = showCancel;
      this.context = context;
      this.listener = listener;
      this.title = title;
      this.content = content;
      this.okButtonText = okButtonText;
      this.cancelButtonText = cancelButtonText;
  }

  @Override
  public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
    if(keyCode == KeyEvent.KEYCODE_BACK){
      if(listener != null){
        listener.cancel();
      }
    }
    return loginError && keyCode == KeyEvent.KEYCODE_BACK || super.onKeyDown(keyCode, event);
  }

  @SuppressLint("SetTextI18n")
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.dialog_confirm);
    try {
      Button okView = findViewById(R.id.btn_update);
      TextView txt_content = findViewById(R.id.txt_content);
      TextView txt_title = findViewById(R.id.txt_title);
      Button cancelView = findViewById(R.id.btn_no_update);

      if(!showCancel){
         cancelView.setVisibility(View.GONE);
      }

      if (null != okButtonText) {
         okView.setText(okButtonText + "");
      }

      if (null != cancelButtonText) {
         cancelView.setText(cancelButtonText + "");
      }

      if (null != title && !TextUtils.isEmpty(title)) {
        txt_title.setText(title);
      }
//      else {
//        txt_title.setVisibility(View.GONE);
//        LinearLayout.LayoutParams layoutParams = (android.widget.LinearLayout.LayoutParams) txt_content
//            .getLayoutParams();
//        int left = DisplayUtil.dip2px(context, 15);
//        int top = DisplayUtil.dip2px(context, 25);
//        int right = DisplayUtil.dip2px(context, 15);
//        int bottom = 0;
//        layoutParams.setMargins(left, top, right, bottom);
//        txt_content.setLayoutParams(layoutParams);
//      }
      if (null != content && !TextUtils.isEmpty(content)) {
        txt_content.setText(content);
      } else {
        txt_content.setVisibility(View.GONE);
      }

      okView.setOnClickListener(v -> {
        ConfirmDialog.this.dismiss();
        if (null != listener) {
          listener.confirm();
        }
      });

      cancelView.setOnClickListener(v -> {
        ConfirmDialog.this.dismiss();
        if (null != listener) {
           listener.cancel();
        }
      });
    } catch (Exception e) {
      LogUtils.e(e.toString());
    }
  }

}