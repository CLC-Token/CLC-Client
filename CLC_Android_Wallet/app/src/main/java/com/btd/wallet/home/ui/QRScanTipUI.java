package com.btd.wallet.home.ui;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.btd.wallet.pure.R;
import com.btd.wallet.base.activity.BaseSupportActivity;
import com.btd.wallet.config.SPKeys;
import com.btd.wallet.utils.MethodUtils;
import com.uuzuche.lib_zxing.activity.CaptureActivity;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * @创建者 廖林涛
 * @创时间 2017年3月3日 下午5:36:42
 * @描述 二维码扫描结束后,出现异常的提示页
 *
 * @版本 $Rev: 4452 $
 * @更新者 $Author$
 * @更新时间 $Date: 2018-07-10 14:38:49 +0800 (周二, 10 七月 2018) $
 * @更新描述 TODO
 */
public class QRScanTipUI extends BaseSupportActivity {

  @BindView(R.id.img_1)
  ImageView img_1;

  @BindView(R.id.txt_1)
  TextView txt_1;

  @BindView(R.id.txt_2)
  TextView txt_2;

  @BindView(R.id.btn_1)
  Button btn_1;

  @BindView(R.id.txt_back)
  TextView txt_back;

  @BindView(R.id.txt_title)
  TextView txt_title;

  /** 扫描结果值, 0表示无法识别二维码,1表示盒子底部二维码 */
  private int scanResultType = 0;
  private String scanResult = null;

  @Override
  protected int getContentView() {
    return R.layout.ui_qr_scan_tip;
  }

  @Override
  protected void initView() {
  }

  @Override
  protected void initData() {
      scanResult = getIntent().getStringExtra(SPKeys.DATA);
      txt_title.setText(getStr(R.string._scan_qrcode_title_result));
      img_1.setImageResource(R.drawable.hint_warning);
      txt_1.setText(scanResult + "");
      MethodUtils.setVisible(txt_1, true);
      txt_2.setText(getStr(R.string._scan_qrcode_tip1));
      btn_1.setText(getStr(R.string.string_copy));
  }

  @OnClick(value = {R.id.txt_back, R.id.btn_1, R.id.btn_2 })
  public void onNewClick(View v) {
    switch (v.getId()) {
      case R.id.btn_1:
        /* 重新扫描或复制 TODO */
        if (scanResultType == 1) {
          startActivity(new Intent(mActivity, CaptureActivity.class));
          finish();
        } else {
          MethodUtils.copyClipboard(mActivity, scanResult);
          MethodUtils.showToast(mActivity, getStr(R.string.copy_success));
        }

        break;
      case R.id.btn_2:
      case R.id.txt_back:
        /* 关闭 */
        finish();
        break;
    }
  }

}
