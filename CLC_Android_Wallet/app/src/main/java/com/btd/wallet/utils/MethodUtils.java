
package com.btd.wallet.utils;


import android.app.Activity;
import android.app.ActivityManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import com.blankj.utilcode.util.ToastUtils;
import com.btd.library.base.util.LogUtils;
import com.btd.wallet.MainActivity;
import com.btd.wallet.pure.R;
import com.btd.wallet.core.AllActivity;
import com.btd.wallet.core.WorkApp;
import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import de.keyboardsurfer.android.widget.crouton.Configuration;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * <preErcToBtd>
 * 创建: 廖林涛 2017/5/26 9:40;
 * 版本: $Rev: 13582 $ $Date: 2019-01-30 15:54:59 +0800 (周三, 30 一月 2019) $
 * </preErcToBtd>
 */
@SuppressWarnings(value = {"deprecation", "SimpleDateFormat"})
public class MethodUtils {
    private static final String TAG = MethodUtils.class.getSimpleName();

    /**
     * 无网络连接
     */
    public static final int None_NetWork = 0;
    /**
     * 2G/3G/4G网络状态
     */
    public static final int Wap_NetWork = 1;
    /**
     * wifi网络状态
     */
    public static final int Wifi_NetWork = 2;




    /**
     * 方法名: </br>
     * 详述: <获取当前网络连接状态/br> 开发人员：王太顺</br>
     * 创建时间：2014-8-6</br>
     *
     * @return 3G Wifi None
     */
    public static int getCurNetworkStatus(Context context) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            NetworkInfo mobNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            // 判断wifi网络
            if (activeNetInfo != null && activeNetInfo.isConnected()) {
                return Wifi_NetWork;
            }
            // 判断3G网络
            if (mobNetInfo != null && mobNetInfo.isConnected()) {
                return Wap_NetWork;
            }
            return None_NetWork;
        } catch (Exception e) {
            return None_NetWork;
        }
    }



    /**
     * 方法名: </br>
     * 详述: <隐藏键盘/br> 开发人员：王太顺</br>
     * 创建时间：2015-9-23</br>
     */
    public static void hideSoft(Context context, View view) {
        try {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0); // 强制隐藏键盘
        } catch (Exception e) {
            LogUtils.e(e.toString());
        }
    }
    public static void hideKeyBoard(Activity context) {
        try {
            InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
            //如果window上view获取焦点 && view不为空
            if(imm.isActive()&& context.getCurrentFocus()!=null){
                //拿到view的token 不为空
                if (context.getCurrentFocus().getWindowToken()!=null) {
                    //表示软键盘窗口总是隐藏，除非开始时以SHOW_FORCED显示。
                    imm.hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        } catch (Exception e) {
            LogUtils.e(e.toString());
        }
    }






    /**


    /**
     * Update by yzy
     * on 2018/7/17
     * comment 优化,将返回值直接改为Acitvity,之前是boolean,
     * 由于之后还要一个强转成Activity省略成一个强转
     *
     * @return 判断是否为activity的context
     */
    private static Activity isActivityContext(Context context) {
        try {
            return (Activity) context;
        } catch (Exception e) {
            // e.printStackTrace();
            // 说明是ApplicationContext
            return null;
        }
    }

    /**
     * @return 根据文件获取后缀名
     */
    public static String getExtension(final File file) {
        try {
            String name = file.getName();
            return getExtension(name);
        } catch (Exception e) {
            LogUtils.e(e.toString());
            return "";
        }
    }

    /**
     * @return 返回不带 点 的后缀名
     */
    public static String getExtension(String name) {
        try {
            String suffix = "";
            final int idx = name.lastIndexOf(".");
            if (idx >= 0) {
                suffix = name.substring(idx + 1);
            }
            return suffix;
        } catch (Exception e) {
            LogUtils.e(e.toString());
            return "";
        }

    }








    /**
     * <p> Toast从标题栏底部滑动出现
     *
     * @param context 如果是activity的上下文,则调用Crouton显示;如果不是,则调用默认Toast
     */
    public static void showToast(Context context, String content) {
        // TODO: 2017/5/27 还可以再继续优化,暂时没有时间优化了
        if (context == null) {
            return;
        }
        Activity activity = isActivityContext(context);
        try {
            if (activity != null) {
                /* 是activity */
                if (activity.isDestroyed() || activity.isFinishing()) {
                    // activity被销毁的时候,不显示
                    return;
                }
                View view = View.inflate(context, R.layout.fragment_toast, null);
                TextView textContent = (TextView) view.findViewById(R.id.txt_toast_content);
                textContent.setText(content);
                if (CMActivityUtils.isShowing(activity)) {
                    Crouton.cancelAllCroutons();
                    LogUtils.i("crouton hide");
                }
                Crouton mCrouton = Crouton.make(activity, view);
                mCrouton.setConfiguration(
                        new Configuration.Builder()
                                .setInAnimation(R.anim.fade_in_down)
                                .setOutAnimation(R.anim.fade_out_up)
                                .build()
                );
                mCrouton.show();
            } else {
                /* 不是activity */
                if (isAppForeground()) {
                    /* App在后台不显示Toast */
                    makeCustomSystemToast(context, content);
                }
            }
        } catch (Exception e) {
            LogUtils.e(e.toString());
        }
    }

    /**
     * <p> 显示自定义Toast, 注意使用Application的上下文,得到View会使用系统默认的主题样式，如果你自定义了某些样式可能不会被使用
     */
    private static void makeCustomSystemToast(Context context, String content) {
        View view = View.inflate(context, R.layout.fragment_toast, null);
        TextView textContent = (TextView) view.findViewById(R.id.txt_toast_content);
        textContent.setText(content);
        ToastUtils.setView(view);
        ToastUtils.setGravity(Gravity.TOP, 0, (int) getDimension(R.dimen.toast_margin_top));
        ToastUtils.showShortSafe(content);
    }



    /**
     * 判断App是否处于前台
     *
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean isAppForeground() {
        ActivityManager manager = (ActivityManager) WorkApp.getContext().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> info = manager.getRunningAppProcesses();
        if (info == null || info.size() == 0) {
            return false;
        }
        for (ActivityManager.RunningAppProcessInfo aInfo : info) {
            if (aInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return aInfo.processName.equals(WorkApp.getContext().getPackageName());
            }
        }
        return false;
    }

    /**
     * <p> 复制文本
     */
    public static void copyClipboard(Context context, String text) {
        try {
            ClipboardManager myClipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData newPlainText = ClipData.newPlainText("text", text);
            myClipboard.setPrimaryClip(newPlainText);
        } catch (Exception e) {
            LogUtils.e(e.toString());
        }
    }



    /**
     * 设置view可见不可见
     */
    public static void setVisible(View view, boolean visible) {
        try {
            if (null == view) {
                LogUtils.w(TAG, "null == view");
                return;
            }
            view.setVisibility(visible ? View.VISIBLE : View.GONE);
        } catch (Exception e) {
            LogUtils.e(e.toString());
        }
    }

    /**
     * 设置view可见不可见
     *
     * @param invisible true:INVISIBLE;false:GONE
     */
    public static void setInVisible(View view, boolean invisible) {
        try {
            if (null == view) {
                LogUtils.w(TAG, "null == view");
                return;
            }
            view.setVisibility(invisible ? View.INVISIBLE : View.GONE);
        } catch (Exception e) {
            LogUtils.e(e.toString());
        }
    }

    /**
     * 设置view可见不可见
     *
     * @param invisible true:INVISIBLE;false:VISIBLE
     */
    public static void setInVisibleVisible(View view, boolean invisible) {
        try {
            if (null == view) {
                LogUtils.w(TAG, "null == view");
                return;
            }
            view.setVisibility(invisible ? View.INVISIBLE : View.VISIBLE);
        } catch (Exception e) {
            LogUtils.e(e.toString());
        }
    }




    public static String getString(int stringId) {
        try {
            return WorkApp.workApp.getResources().getString(stringId);
        } catch (Exception e) {
            LogUtils.e(e.toString());
            return "";
        }
    }

    public static String getString(int stringId, Object[] objects) {
        try {
            return WorkApp.workApp.getResources().getString(stringId, objects);
        } catch (Exception e) {
            LogUtils.e(e.toString());
            return "";
        }
    }

    public static Drawable getDrawable(int id) {
        try {
            return WorkApp.workApp.getResources().getDrawable(id);
        } catch (Exception e) {
            LogUtils.e(e.toString());
            return null;
        }
    }

    public static float getDimension(int id) {
        try {
            return WorkApp.workApp.getResources().getDimension(id);
        } catch (Exception e) {
            LogUtils.e(e.toString());
            return 0f;
        }
    }

    public static int getColor(int id) {
        try {
            return WorkApp.workApp.getResources().getColor(id);
        } catch (Exception e) {
            LogUtils.e(e.toString());
            return 0;
        }
    }

    public static String getText(EditText edit) {
        try {
            return edit.getText().toString().trim();
        } catch (Exception e) {
            LogUtils.e(e.toString());
            return "";
        }
    }







    private static Subscription subscription;

    public static void delayShowSoft(final EditText mEdit_name) {
        try {
            if (null == mEdit_name) {
                return;
            }
            if (subscription != null) {
                subscription.unsubscribe();
            }
            subscription = Observable.interval(500, 2000, TimeUnit.MILLISECONDS)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .unsubscribeOn(Schedulers.io())
                    .subscribe(new Subscriber<Long>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(Long aLong) {
                            if (subscription != null) {
                                subscription.unsubscribe();
                            }
                            mEdit_name.setFocusable(true);
                            mEdit_name.setFocusableInTouchMode(true);
                            mEdit_name.requestFocus();
                            InputMethodManager imm = (InputMethodManager) WorkApp.workApp
                                    .getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.showSoftInput(mEdit_name, InputMethodManager.SHOW_IMPLICIT);
                        }
                    });
        } catch (Exception e) {
            LogUtils.e(e.toString());
        }
    }

    public static boolean isEmpty(String s) {
        return s != null && !"null".equals(s.toLowerCase()) && s.length() > 0;
    }



    public static int getTotalPage(int pageSize, int totalCount) {
        int totalPage;
        if (totalCount % pageSize == 0) {
            totalPage = totalCount / pageSize;
        } else {
            totalPage = totalCount / pageSize + 1;
        }
        return totalPage;
    }


    /**
     * 获取设备型号
     * <p>如MI2SC</p>
     *
     * @return 设备型号
     */
    public static String getModel() {
        String model = Build.MODEL;
        if (model != null) {
            model = model.trim().replaceAll("\\s*", "");
        } else {
            model = "";
        }
        return model;
    }



    public static void exitAPP() {
        MainActivity.isCheckUpdate = false;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            ActivityManager activityManager = (ActivityManager) WorkApp.workApp.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
            if(activityManager == null) return;
            List<ActivityManager.AppTask> appTaskList = activityManager.getAppTasks();
            for (ActivityManager.AppTask appTask : appTaskList) {
                appTask.finishAndRemoveTask();
            }
        }else {
            AllActivity.finishApp();
        }
    }




    public static String getProviderName(Context context){
        return context.getApplicationContext().getPackageName()+".fileProvider";
    }



    public static int bigDecimalCompareToZero(BigDecimal source){
        return source.compareTo(new BigDecimal("0"));
    }

    public static void setMainStuteCorlor(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            StatusBarUtil.setColor(activity, MethodUtils.getColor(R.color.white), 0);
            StatusBarUtil.setStatusTextColor(true, activity);
        } else {
            StatusBarUtil.setColor(activity, MethodUtils.getColor(R.color.white), 150);
        }
    }

    private static boolean isSpace(String s) {
        if (s == null) {
            return true;
        }
        for (int i = 0, len = s.length(); i < len; ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * @return 获取版本code
     */
    public static int getVersionCode() {
        String packageName = WorkApp.getContext().getPackageName();
        if (isSpace(packageName)) {
            return -1;
        }
        try {
            PackageManager pm = WorkApp.getContext().getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            return pi == null ? -1 : pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return -1;
        }
    }
}
