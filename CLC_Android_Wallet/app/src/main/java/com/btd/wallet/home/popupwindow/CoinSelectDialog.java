package com.btd.wallet.home.popupwindow;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.btd.library.base.util.LogUtils;
import com.btd.wallet.pure.R;

/**
 * Description:  注册时，选择注册方式的对话框  <br>
 * Author: cxh <br>
 * Date: 2019/3/7 8:42
 */
public class CoinSelectDialog extends Dialog {
    public static final int REGISTER = 1;
    public static final int FORGET_PWD = 2;
    Context context;
    private OnPopItemClickListener mOnPopItemClickListener;

    private Window window;

    public CoinSelectDialog(final Activity context, OnPopItemClickListener listener) {
        super(context , R.style.dialog_bottom_full);
        this.context = context;
        mOnPopItemClickListener = listener;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.popup_reg_select);
            window = getWindow();
            WindowManager.LayoutParams params = window.getAttributes();
            window.setGravity(Gravity.BOTTOM);
            window.setWindowAnimations(R.style.dialog_animation);
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);//设置横向全屏

            findViewById(R.id.iv_close).setOnClickListener((v)->{
                dismiss();
            });

            //手机注册
            findViewById(R.id.txt_btd).setOnClickListener((v)->{
                dismiss();
                if(mOnPopItemClickListener!=null){
                    mOnPopItemClickListener.btd();
                }
            });

            //邮箱注册
            findViewById(R.id.txt_hdt).setOnClickListener((v)->{
                dismiss();
                if(mOnPopItemClickListener!=null){
                    mOnPopItemClickListener.hdt();
                }
            });

            //在屏幕底部，宽全屏
            Window window = getWindow();
            if(window != null){
                window.setLayout((ViewGroup.LayoutParams.MATCH_PARENT)
                        , ViewGroup.LayoutParams.WRAP_CONTENT);
                window.setGravity(Gravity.BOTTOM);
            }
        } catch (Exception e) {
            LogUtils.e(e.toString());
        }
    }

    /**
     * <p> 设置dialog的宽度
     */
    @Override
    public void show() {
        super.show();
    }


    public interface OnPopItemClickListener {
        void hdt();

        void btd();
    }

}
