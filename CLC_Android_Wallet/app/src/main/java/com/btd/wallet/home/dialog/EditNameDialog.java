package com.btd.wallet.home.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.btd.library.base.util.LogUtils;
import com.btd.wallet.pure.R;
import com.btd.wallet.core.WorkApp;
import com.btd.wallet.dao.OnUpdateListener;
import com.btd.wallet.utils.MethodUtils;

/**
 * 
 * @创建者 廖林涛
 * @创时间 2016年9月5日 下午5:40:21
 * @描述 修改文件名称对话框
 *
 * @版本 $Rev: 3736 $
 * @更新者 $Author$
 * @更新时间 $Date: 2018-05-30 14:18:59 +0800 (周三, 30 五月 2018) $
 * @更新描述 TODO
 */
public class EditNameDialog extends Dialog {
  private Context context;
  private String fileName;
  private OnUpdateListener updateUser;
  private EditText mEdit_name;
  private String title;
  private String hint;
  private String notEmptyTip;

  public String getTitle() {
    return title;
  }

  public void setMyTitle(String title) {
    this.title = title;
  }

  public String getHint() {
    return hint;
  }

  public void setHint(String hint) {
    this.hint = hint;
  }

  public String getNotEmptyTip() {
    return notEmptyTip;
  }

  public void setNotEmptyTip(String notEmptyTip) {
    this.notEmptyTip = notEmptyTip;
  }

  public EditNameDialog(Context context, String fileName, OnUpdateListener updateUser) {
    super(context, R.style.FullScreenBaseDialog);
    this.context = context;
    this.fileName = fileName;
    this.updateUser = updateUser;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    // TODO Auto-generated method stub
    super.onCreate(savedInstanceState);
    setContentView(R.layout.dialog_edit_file_name);

    try {

      Button mOKView = (Button) findViewById(R.id.btn_update);
      Button mCancelView = (Button) findViewById(R.id.btn_no_update);
      TextView txt_title = (TextView) findViewById(R.id.txt_title);
      txt_title.setText(null == title ? MethodUtils.getString(R.string._set_file_name_dialog_tip) : title);
      MethodUtils.setVisible(txt_title, true);

      mEdit_name = (EditText) findViewById(R.id.edit_name);
      if (fileName.length() > 30) {
        fileName = fileName.substring(0, 30);
      }
      mEdit_name.setText(fileName);
      mEdit_name.setHint(hint == null ? MethodUtils.getString(R.string.dialog_edit_name_edit_filename) : hint);

      mEdit_name.setSelection(fileName.length());
      // mEdit_name.requestFocus();
      // InputMethodManager imm = (InputMethodManager)
      // mEdit_name.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
      // imm.showSoftInput(mEdit_name, 2);

      mOKView.setOnClickListener(v -> {
        String name = mEdit_name.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
          String toast1 = TextUtils.isEmpty(notEmptyTip) ? getContext().getString(R.string
              .EditFileNameDialog_not_null) : notEmptyTip;
          MethodUtils.showToast(WorkApp.workApp, toast1);
          return;
        }

        updateUser.update(name);
        MethodUtils.hideSoft(context, mEdit_name);
        EditNameDialog.this.dismiss();
      });

      mCancelView.setOnClickListener(v -> {
        MethodUtils.hideSoft(context, mEdit_name);
        EditNameDialog.this.dismiss();
      });
    } catch (Exception e) {
      LogUtils.e(e.toString());
    }
  }

//  @Override
//  public boolean onTouchEvent(android.view.MotionEvent event) {
//    MethodUtils.hideSoft(context, this.getCurrentFocus());
//    EditNameDialog.this.dismiss();
//    return true;
//  }

  @Override
  public void show() {
    super.show();
    MethodUtils.delayShowSoft(mEdit_name);
  }
}