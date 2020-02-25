package com.btd.wallet.home.dialog;

import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.btd.library.base.util.LogUtils;
import com.btd.wallet.pure.R;
import com.btd.wallet.utils.MethodUtils;

/**
 *
 * 描述: <秘钥对话框/br> 发布版本号：</br>
 * 开发人员： 杨紫员</br>
 * 创建时间： 2018-3-19
 */
public class PrivateKeyDialog extends Dialog {
  private final Context context;
  private String address;
  private String privateKey;

  public PrivateKeyDialog(Context context , String address , String privateKey) {
    super(context, R.style.FullScreenBaseDialog);
    this.address = address;
    this.privateKey = privateKey;
    this.context = context;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    // TODO Auto-generated method stub
    super.onCreate(savedInstanceState);
    setContentView(R.layout.dialog_private_key);
    try {
      setCanceledOnTouchOutside(false);
//      getWindow().setBackgroundDrawableResource(android.R.color.transparent);  //设置整体的背景色
      TextView txtAddress = (TextView) findViewById(R.id.address);
      TextView txtPrivateKey = (TextView) findViewById(R.id.private_key);
      TextView txtTip = (TextView) findViewById(R.id.look_private_key_tip);
      txtAddress.setText(address);
      txtPrivateKey.setText(privateKey);
      txtTip.setText(MethodUtils.getString(R.string.look_private_key_tip).replace("\\n" , "\n"));
      findViewById(R.id.iv_close).setOnClickListener(v -> dismiss());
      findViewById(R.id.btn_copy).setOnClickListener(v -> {
        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        String text = MethodUtils.getString(R.string.address_) + address + " "
                      + MethodUtils.getString(R.string.private_key_) + privateKey;
        clipboardManager.setText(text);
        MethodUtils.showToast(context , MethodUtils.getString(R.string.copy_success));
        dismiss();
      });
    } catch (Exception e) {
      LogUtils.e(e.toString());
    }
  }

}