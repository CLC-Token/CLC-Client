package com.btd.wallet.home.popupwindow;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.btd.library.base.util.LogUtils;
import com.btd.wallet.pure.R;
import com.btd.wallet.utils.MethodUtils;

/**
 * 支付页面支付弹框
 * Create by 杨紫员 2018/2/7
 */
public class PayInputPwdDialog extends Dialog {
    Context context;
    private OnPopItemClickListener mOnPopItemClickListener;

    EditText etPwd;
    TextView btnPay;
    TextView tvTitle;
    int type = 0;
    private Window window;

    public PayInputPwdDialog(final Activity context , int type){
        this(context);
        this.type = type;
    }

    public PayInputPwdDialog(final Activity context) {
        super(context , R.style.dialog_bottom_full);
        this.context = context;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.popup_pay_input_pwd);
            window = getWindow();
            WindowManager.LayoutParams params = window.getAttributes();
            window.setGravity(Gravity.BOTTOM);
            window.setWindowAnimations(R.style.dialog_animation);
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);//设置横向全屏
            etPwd = (EditText) findViewById(R.id.popuo_pay_input_pwd_etpwd);
            btnPay = (TextView) findViewById(R.id.popuo_pay_input_pwd_btn);
            tvTitle = (TextView) findViewById(R.id.popup_pay_input_pwd_title);
            etPwd.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(s.toString().length() >= 8){
                        btnPay.setEnabled(true);
                    }else{
                        btnPay.setEnabled(false);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            btnPay.setEnabled(false);
            if(type ==1){
                tvTitle.setText(MethodUtils.getString(R.string.wallet_look_private_key));
                btnPay.setText(MethodUtils.getString(R.string.string_submit));
            }else if(type==2){
                tvTitle.setText(MethodUtils.getString(R.string.wallet_look_word));
                btnPay.setText(MethodUtils.getString(R.string.string_submit));
            }
            findViewById(R.id.layout_close).setOnClickListener(v -> PayInputPwdDialog.this.dismiss());

            btnPay.setOnClickListener((v)->{
                dismiss();
                if(mOnPopItemClickListener!=null){
                    mOnPopItemClickListener.submit();
                }
            });
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
        void submit();
    }

    public PayInputPwdDialog setOnPopItemClickListener(OnPopItemClickListener onPopItemClickListener) {
        mOnPopItemClickListener = onPopItemClickListener;
        return this;
    }

    public String getPwd(){
        return etPwd.getText().toString();
    }

}
