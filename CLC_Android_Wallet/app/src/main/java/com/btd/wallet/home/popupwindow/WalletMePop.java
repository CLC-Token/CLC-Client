package com.btd.wallet.home.popupwindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.btd.wallet.pure.R;
import com.btd.wallet.core.WorkApp;
import com.btd.wallet.utils.CMDimenUtils;


public class WalletMePop extends PopupWindow {
    Context context;
    private OnPopItemClickListener mOnPopItemClickListener;

    public WalletMePop(final Activity context) {
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View conentView = inflater.inflate(R.layout.popup_wallet_me, null);
        /*创建一个新的账户*/
        conentView.findViewById(R.id.tv_rename).setOnClickListener(v -> {
            if (mOnPopItemClickListener != null) {
                mOnPopItemClickListener.onNewClick();
                dismiss();
            }
        });
        /*导入一个米袋账户*/
        conentView.findViewById(R.id.tv_import).setOnClickListener(v -> {
            if (mOnPopItemClickListener != null) {
                mOnPopItemClickListener.onImportClick();
                dismiss();
            }
        });


        this.setContentView(conentView);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
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
        /**
         * 点击创建一个
         */
        void onNewClick();

        /*点击导入*/
        void onImportClick();

        void onAbout();

        void exchange();

        void exchangeIst();

        void lock();

        void hdtExchange();
    }

    public void setOnPopItemClickListener(OnPopItemClickListener onPopItemClickListener) {
        mOnPopItemClickListener = onPopItemClickListener;
    }
}
