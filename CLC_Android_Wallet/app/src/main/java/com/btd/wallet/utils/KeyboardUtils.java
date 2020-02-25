package com.btd.wallet.utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by chen on 2016/8/11.
 */
public class KeyboardUtils {

    public static boolean flage = true;

    /**
     * 显示Keyboard
     *
     * @param context
     */
    public static void showKeyboard(Context context) {
        if(context == null){
            return ;
        }
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * view为接受软键盘输入的视图，SHOW_FORCED表示强制显示
     *
     * @param context
     */
    public static void showKeyBoard(Context context, View view) {
        if(context == null){
            return ;
        }
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    public static void hideKeyboard(Context context, View view) {
        if(context == null){
            return ;
        }
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * 测试软键盘的开关
     *
     * @param context
     * @param view
     */
    public static void autoDisplayKeyboard(Context context, View view) {
        if (flage) {
            showKeyboard(context);
            flage = false;
        } else {
            hideKeyboard(context, view);
            flage = true;
        }

    }

    /**
     * 获取输入法打开的状态
     */
    public static boolean isOpen(Context context) {
        if(context == null){
            return false;
        }
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        return imm.isActive();//isOpen若返回true，则表示输入法打开
    }
}
