package com.btd.wallet.home.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.btd.library.base.util.LogUtils;
import com.btd.wallet.pure.R;
import com.btd.wallet.dao.UpdateData;
import com.btd.wallet.utils.CMDimenUtils;
import com.btd.wallet.utils.MethodUtils;


/**
 * 
 * 类名: ConfirmDialog</br>
 * 包名：com.cm.weixiangji.dao </br>
 * 描述: <确认对话框/br> 发布版本号：</br>
 * 开发人员： 王太顺</br>
 * 创建时间： 2015-9-24
 */
public class ConfirmDialog extends Dialog {
  private final Context context;
  private final String content;
  private final UpdateData updateUser;
  private String title;
  private Button okView;
  private Button cancelView;
  private boolean isSingleButton;
  private String okButtonText;
  private String cancelButtonText;
  private boolean showCancel = true;

  public ConfirmDialog(Context context, String title, String content, UpdateData updateUser, boolean showCancel) {
    this(context , title , content , updateUser);
    this.showCancel = showCancel;
  }

  public ConfirmDialog(Context context, String title, String content, UpdateData updateUser) {
    super(context, R.style.FullScreenBaseDialog);
    setCanceledOnTouchOutside(false);
    this.context = context;
    this.content = content;
    this.updateUser = updateUser;
    if (null == title || TextUtils.isEmpty(title)) {
      this.title = MethodUtils.getString(R.string.string_hint);
    } else {
      this.title = title;
    }
  }

  public ConfirmDialog(Context context, String title, String content, String okButtonText, String cancelButtonText,
                       UpdateData updateUser) {
    super(context, R.style.FullScreenBaseDialog);
    setCanceledOnTouchOutside(false);
    this.context = context;
    this.content = content;
    this.updateUser = updateUser;
    this.title = title;
    if (null == okButtonText || TextUtils.isEmpty(okButtonText)) {
      this.okButtonText = MethodUtils.getString(R.string.string_ok);
    } else {
      this.okButtonText = okButtonText;
    }
    if (null == cancelButtonText || TextUtils.isEmpty(cancelButtonText)) {
      this.cancelButtonText = MethodUtils.getString(R.string.string_cancel);
    } else {
      this.cancelButtonText = cancelButtonText;
    }
    if (null == title || TextUtils.isEmpty(title)) {
      this.title = MethodUtils.getString(R.string.string_hint);
    } else {
      this.title = title;
    }
  }

  /**
   * 
   * @param context activity
   * @param title 标题
   * @param content 文本描述
   * @param updateUser 回调接口
   * @param isSingleButton
   *          true 隐藏"取消"按钮
   * @param okButtonText
   *          按钮的提示语,默认为"确定"
   */
  public ConfirmDialog(Context context, String title, String content, UpdateData updateUser, boolean isSingleButton,
                       String okButtonText) {
    this(context, title, content, updateUser);
    this.isSingleButton = isSingleButton;
    if (null == okButtonText || TextUtils.isEmpty(okButtonText)) {
      this.okButtonText = MethodUtils.getString(R.string.string_ok);
    } else {
      this.okButtonText = okButtonText;
    }
  }

  @SuppressLint("SetTextI18n")
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    // TODO Auto-generated method stub
    super.onCreate(savedInstanceState);
    setContentView(R.layout.dialog_confire);

    try {

      okView = (Button) findViewById(R.id.btn_update);
      TextView txt_content = (TextView) findViewById(R.id.txt_content);
      TextView txt_title = (TextView) findViewById(R.id.txt_title);
      cancelView = (Button) findViewById(R.id.btn_no_update);

      if(!showCancel){
         cancelView.setVisibility(View.GONE);
      }

      if (isSingleButton) {
        cancelView.setVisibility(View.GONE);
        okView.setText(okButtonText + "");
      }

      if (null != okButtonText) {
        okView.setText(okButtonText + "");
      }

      if (null != cancelButtonText) {
        cancelView.setText(cancelButtonText + "");
      }

      if (null != title && !TextUtils.isEmpty(title)) {
        txt_title.setText(title);
      } else {
        txt_title.setVisibility(View.GONE);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) txt_content
            .getLayoutParams();
        int left = CMDimenUtils.dp2px(context, 15);
        int top = CMDimenUtils.dp2px(context, 25);
        int right = CMDimenUtils.dp2px(context, 15);
        int bottom = 0;
        layoutParams.setMargins(left, top, right, bottom);
        txt_content.setLayoutParams(layoutParams);
      }
      if (null != content && !TextUtils.isEmpty(content)) {
        txt_content.setText(content);
      } else {
        txt_content.setVisibility(View.GONE);
      }

      okView.setOnClickListener(v -> {
        ConfirmDialog.this.dismiss();
        if (null != updateUser) {
          updateUser.update();
        }
      });

      cancelView.setOnClickListener(v -> {
        ConfirmDialog.this.dismiss();
        if (null != updateUser) {
          updateUser.cancel();
        }
      });
    } catch (Exception e) {
      LogUtils.e(e.toString());
    }
  }
}