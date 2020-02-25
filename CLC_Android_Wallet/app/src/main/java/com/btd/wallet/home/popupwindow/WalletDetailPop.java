package com.btd.wallet.home.popupwindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.btd.wallet.mvp.model.wallet.WalletConfig;
import com.btd.wallet.pure.R;
import com.btd.wallet.core.WorkApp;
import com.btd.wallet.utils.CMDimenUtils;


public class WalletDetailPop extends PopupWindow {
    Context context;
    private OnPopItemClickListener mOnPopItemClickListener;

    public WalletDetailPop(final Activity context, WalletConfig config) {
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View conentView = inflater.inflate(R.layout.popup_wallet_detail, null);
        conentView.findViewById(R.id.tv_rename).setOnClickListener(v -> {
            WalletDetailPop.this.dismiss();
            if(mOnPopItemClickListener != null){
                mOnPopItemClickListener.onRenameClick();
            }
        });
        /*导入一个米袋账户*/
        conentView.findViewById(R.id.tv_delete).setOnClickListener(v -> {
            WalletDetailPop.this.dismiss();
            if(mOnPopItemClickListener != null){
                mOnPopItemClickListener.onDeleteClicke();
            }
        });
        /*查询交易记录*/
        conentView.findViewById(R.id.tv_recode).setOnClickListener(v -> {
            WalletDetailPop.this.dismiss();
            if(mOnPopItemClickListener != null){
                mOnPopItemClickListener.onRecode();
            }
        });
        /** 查看私钥 **/
        conentView.findViewById(R.id.tv_look_private).setOnClickListener(v->{
            WalletDetailPop.this.dismiss();
            if(mOnPopItemClickListener != null){
                mOnPopItemClickListener.lookPrivateKey();
            }
        });

        if(config.getWords()!=null&&config.getWords().length()>0){
            conentView.findViewById(R.id.tv_look_word).setVisibility(View.VISIBLE);
            conentView.findViewById(R.id.line2).setVisibility(View.VISIBLE);
        }

        conentView.findViewById(R.id.tv_look_word).setOnClickListener(v->{
            WalletDetailPop.this.dismiss();
            if(mOnPopItemClickListener != null){
                mOnPopItemClickListener.lookWord();
            }
        });

        this.setContentView(conentView);
        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setFocusable(false);
        this.setOutsideTouchable(true);
        this.setBackgroundDrawable(new BitmapDrawable());
    }

    public void showPopupWindow(final View parent) {
        if (!this.isShowing()) {
            int[] location = new int[2];
            parent.getLocationOnScreen(location);
            this.showAtLocation(parent, Gravity.NO_GRAVITY, location[0] + parent.getWidth(), location[1]- CMDimenUtils.dp2px(WorkApp.workApp, 15));
        } else {
            this.dismiss();
        }
    }


    public interface OnPopItemClickListener {

        /** 点击创建一个 */
        void onRenameClick();

        /*点击导入*/
        void onDeleteClicke();

        void onRecode();

        void lookPrivateKey();

        void lookWord();
    }

    public void setOnPopItemClickListener(OnPopItemClickListener onPopItemClickListener) {
        mOnPopItemClickListener = onPopItemClickListener;
    }
}
