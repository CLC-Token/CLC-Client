package com.btd.wallet.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.view.KeyEvent;

import com.btd.library.base.util.LogUtils;
import com.btd.wallet.widget.LoadingDialog;


/**
 * ProgressDialogUtil,显示loading对话框
  * <preErcToBtd>
  * 创建: 廖林涛 2017/6/21 11:07;
  * 版本: $Rev: 3736 $ $Date: 2018-05-30 14:18:59 +0800 (周三, 30 五月 2018) $
  * </preErcToBtd>
  */
public class PDialogUtil {

   private static LoadingDialog dialog;

   /** 无提示loading */
   public static void startProgress(Context context) {
       startProgress(context, "");
   }

   public static void startProgress(Context context, CharSequence message) {
       startProgress(context ,message , false);
   }

   public static void startProgress(Context context , CharSequence message , DialogInterface.OnDismissListener onDismissListener) {
       startProgress(context, message);
       if (dialog !=null) {
           dialog.setOnDismissListener(onDismissListener);
       }
   }

    /**
     * 设置dialog的标题
     * @param context
     * @param message
     */
   public static void setTitle(Context context, CharSequence message){
       if(dialog !=null){
           dialog.setTitle(message);
       }else{
           startProgress(context,message);
       }
   }

   public static void startProgress(Context context, CharSequence message, boolean cancelable) {

       try {
           dialog = new LoadingDialog(context);
           dialog.setTitle(message);
           dialog.setCancelable(cancelable);
           dialog.show();
           if(cancelable){
               onKeyBack();
           }
       } catch (Exception e) {
           LogUtils.e(e.toString());
       }
   }

   public static void stopProgress() {
       if (dialog != null) {
           dialog.dismiss();
           dialog = null;
       }
   }

   public static LoadingDialog getDialog() {
       return dialog;
   }

   public static void setDialog(LoadingDialog dialog) {
       PDialogUtil.dialog = dialog;
   }

   public static boolean isShowing() {
       try {
           if (null != dialog) {
               return dialog.isShowing();
           } else {
               return false;
           }
       } catch (Exception e) {
           LogUtils.e(e.toString());
           return false;
       }
   }

   public static void setOnKeyListener(OnKeyListener onKeyListener) {
       try {
           if (null != dialog) {
               dialog.setOnKeyListener(onKeyListener);
           }
       } catch (Exception e) {
           LogUtils.e(e.toString());
       }
   }

   public static void updateTitle(String title) {
       try {
           if (dialog == null) {
               return;
           }
           dialog.setTitle(title);
       } catch (Exception e) {
         e.printStackTrace();
         LogUtils.e(e.toString());
       }
   }

   public static void onKeyBack() {
       setOnKeyListener((dialog, keyCode, event) -> {
           try {
               if (PDialogUtil.isShowing() && keyCode == KeyEvent.KEYCODE_BACK) {
                   PDialogUtil.stopProgress();
                   return true;
               }
           } catch (Exception e) {
               LogUtils.e(e.toString());
           }
           return false;
       });
   }


    public static void startProgress(Context context, boolean cancelable, CharSequence message) {
        try {
            dialog = new LoadingDialog(context);
            dialog.setTitle(message);
            dialog.setCancelable(cancelable);
            dialog.setCanceledOnTouchOutside(cancelable);
            dialog.show();
            if(cancelable) {
                onKeyBack();
            }
        } catch (Exception e) {
            LogUtils.e(e.toString());
        }
    }


    public static void setCancelable(boolean cancelable){
        if(dialog!=null){
            dialog.setCanceledOnTouchOutside(cancelable);
            dialog.setCancelable(cancelable);
            if(cancelable){
                onKeyBack();
            }
        }
    }

}
