package com.btd.wallet.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.TextView;

import com.btd.library.base.util.LogUtils;
import com.btd.wallet.pure.R;
import com.btd.wallet.utils.MethodUtils;

/**
 * 
 * 类名: LoadingDialog</br> 
 * 包名：com.cm.weixiangji.view </br> 
 * 描述: <加载等待/br>
 * 发布版本号：</br>
 * 开发人员： 王太顺</br>
 * 创建时间： 2015-8-25
 */
public class LoadingDialog extends Dialog {
  private static final int CHANGE_TITLE_WHAT = 1;
  private static final int CHNAGE_TITLE_DELAYMILLIS = 300;
  private static final int MAX_SUFFIX_NUMBER = 3;
  private static final char SUFFIX = '.';

  private TextView tv;
  private TextView tv_point;
  private RotateAnimation mAnim;
  private Activity context;
  private boolean cancelable = true;

  private Handler handler = new Handler() {
    private int num = 0;

    public void handleMessage(android.os.Message msg) {
      if (msg.what == CHANGE_TITLE_WHAT) {
        StringBuilder builder = new StringBuilder();
        if (num >= MAX_SUFFIX_NUMBER) {
          num = 0;
        }
        num++;
        for (int i = 0; i < num; i++) {
          builder.append(SUFFIX);
        }
        tv_point.setText(builder.toString());
        if (isShowing()) {
          handler.sendEmptyMessageDelayed(CHANGE_TITLE_WHAT, CHNAGE_TITLE_DELAYMILLIS);
        } else {
          num = 0;
        }
      }
    };
  };

  public LoadingDialog(Context context) {
    super(context, R.style.Dialog_bocop);
    this.context = (Activity) context;
    init();
  }

  @SuppressWarnings("ResourceType")
  private void init() {
    View contentView = View.inflate(getContext(), R.layout.activity_custom_loding_dialog_layout, null);
    setContentView(contentView);

    contentView.setOnClickListener(v -> {
      if (cancelable) {
        dismiss();
      }
    });
    tv = (TextView) findViewById(R.id.tv);
    MethodUtils.setVisible(tv, false);
    tv_point = (TextView) findViewById(R.id.tv_point);
    initAnim();
    getWindow().setWindowAnimations(R.anim.alpha_in);
    setTranslucentStatus();
  }

  private void initAnim() {
    mAnim = new RotateAnimation(360, 0, Animation.RESTART, 0.5f, Animation.RESTART, 0.5f);
    mAnim.setDuration(5000);
    mAnim.setRepeatCount(Animation.INFINITE);
    mAnim.setRepeatMode(Animation.RESTART);
    mAnim.setStartTime(Animation.START_ON_FIRST_FRAME);
  }

  @Override
  public void show() {
    handler.sendEmptyMessage(CHANGE_TITLE_WHAT);
    super.show();
  }

  /**
   * 设置Dialog全部透明状态,解决dialog内容与activity的状态栏颜色不一致问题
   */
  private void setTranslucentStatus() {
    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0 全透明实现
      Window window = getWindow();
      window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
              | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
      window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
      window.setStatusBarColor(Color.TRANSPARENT);
    } else {//4.4 全透明状态栏
      getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }
  }

  @Override
  public void dismiss() {
    mAnim.cancel();

    if (context != null && !context.isFinishing()) {
      super.dismiss();
    }
  }

  @Override
  public void setCancelable(boolean flag) {
    cancelable = flag;
    super.setCancelable(flag);
  }

  @Override
  public void setTitle(CharSequence title) {
    try {
      if(MethodUtils.isEmpty(title+"")){
        MethodUtils.setVisible(tv, false);
      }else{
        tv.setText(title);
        MethodUtils.setVisible(tv, true);
      }
    } catch (Exception e) {
      LogUtils.e(e.toString());
    }
  }

  @Override
  public void setTitle(int titleId) {
    setTitle(getContext().getString(titleId));
  }
  
}
