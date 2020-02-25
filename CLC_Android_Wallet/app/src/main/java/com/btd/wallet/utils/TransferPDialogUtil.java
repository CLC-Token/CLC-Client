package com.btd.wallet.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.view.KeyEvent;

import com.btd.library.base.util.LogUtils;
import com.btd.wallet.widget.LoadingDialog;

/**
 * ProgressDialogUtil,显示loading对话框，按返回键不起作用
  * <preErcToBtd>
  * 创建: 廖林涛 2017/6/21 11:07;
  * 版本: $Rev: 13792 $ $Date: 2019-02-14 23:38:39 +0800 (周四, 14 二月 2019) $
  * </preErcToBtd>
  */
public class TransferPDialogUtil {

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
           onKeyBack();
       } catch (Exception e) {
           LogUtils.e(e.toString());
       }
   }

   public static void stopProgress() {
       try {
           if (dialog != null) {
               dialog.dismiss();
               dialog = null;
           }
       }catch (Exception e){
           LogUtils.e(e.toString());
       }

   }

   public static LoadingDialog getDialog() {
       return dialog;
   }

   public static void setDialog(LoadingDialog dialog) {
       TransferPDialogUtil.dialog = dialog;
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
               if (TransferPDialogUtil.isShowing() && keyCode == KeyEvent.KEYCODE_BACK) {
//                   TransferPDialogUtil.stopProgress();
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
