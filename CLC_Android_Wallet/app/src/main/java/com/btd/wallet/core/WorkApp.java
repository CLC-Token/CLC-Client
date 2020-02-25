package com.btd.wallet.core;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;
import android.webkit.WebView;

import com.blankj.utilcode.util.Utils;
import com.btd.library.base.config.LanuageConfig;
import com.btd.library.base.glide.glidesetting.GlideOptions;
import com.btd.library.base.http.manager.RxHttpManager;
import com.btd.library.base.util.CMScreenUtils;
import com.btd.library.base.util.LogUtils;
import com.btd.wallet.pure.BuildConfig;
import com.btd.wallet.pure.R;
import com.btd.wallet.config.Constants;
import com.btd.wallet.config.GlideImageLoader;

import com.btd.wallet.config.SPKeys;

import com.btd.wallet.mvp.model.VisibleReFresh;

import com.btd.wallet.utils.CMPersistentUtils;

import com.btd.wallet.utils.StringUtils;
import com.btd.wallet.widget.MultiConfigure;
import com.facebook.stetho.Stetho;
import com.lzy.imagepicker.ImagePicker;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import org.greenrobot.eventbus.EventBus;
import org.litepal.LitePalApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import me.yokeyword.fragmentation.Fragmentation;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * Created by yzy on 2018/10/19 14:36
 */

public class WorkApp extends MultiDexApplication {
    public static WorkApp workApp;
    /**
     * 屏幕宽度
     **/
    public static int mScreenWidth;
    /**
     * SD卡显示文件夹名称,如果替换app名称
     */
    public static final String DIR_NAME_SHOW = "hdt";
    /**
     * SD卡隐藏文件夹名称,如果替换app名称,记得修改这里TODO
     */
    public static final String DIR_NAME_HIDE = ".hdt";
    public static boolean isSplashFirst = true;

    public static int mScreenHeight;
    public static String deviceToken;


    private static String uuid;         //客户端唯一标识
    private GlideOptions mGlideOptions;
    /*当页面显示时判断是否需要刷新*/
    public VisibleReFresh visibleReFresh;
    public static boolean firstCamera = false;
    public static String inviteCode; //客户注册时提交的邀请码
    public static String provinceCode; //客户注册时提交的城市编号

    public static boolean isFirstEnter = true;
    //浏览器启动app获取到的订单号
    public static String payNo;
    //跳转到实名认证
    public static boolean authRet;

    //当前正在聊天的id
    private String mImTradeNo;

    /**
     * 不纳入友盟统计的fragment集合
     */
    public final List<String> fragmentName = new ArrayList<>();


    public static CMPersistentUtils getShare() {
        return CMPersistentUtils.getInstance(workApp);
    }

    public static String getUuid() {
        if (StringUtils.isEmptyOrNull(uuid)) {
            uuid = getShare().getString(SPKeys.CLIENT_UUID);
        }
        if (StringUtils.isEmptyOrNull(uuid)) {
            setUuid();
        }
        return uuid;
    }

    public static void setUuid() {
        getShare().put(SPKeys.IS_FIRST_OPEN, false);
        WorkApp.uuid = UUID.randomUUID().toString();
        getShare().put(SPKeys.CLIENT_UUID, uuid);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        workApp = this;
        Utils.init(this);
        LitePalApplication.initialize(this);
        mScreenHeight = CMScreenUtils.getScreenHeight(this);
        mScreenWidth = CMScreenUtils.getScreenWidth(this);
        if (!BuildConfig.isRelease) {
//            LeakCanary.install(this);   //检测内存泄漏
            // Stetho初始化
            Stetho.initialize(Stetho.newInitializerBuilder(this)
                    .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                    .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                    .build()
            );
        }

        //初始化错误页
        MultiConfigure.setEmptyViewResId(R.layout.activity_empty); // 空页面
        MultiConfigure.setLoadingViewResId(R.layout.activity_loading_new); // 加载页面
        MultiConfigure.setErrorViewResId(R.layout.activity_error);// 网络错误页面.

        initFragmentConfig();   //初始化fragmention的配置

        ZXingLibrary.initDisplayOpinion(this);  //扫描二维码配置

      //  initLibrary();          //初始化cumelibrary;

        addFragmentName();


        mGlideOptions = new GlideOptions();

        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());   //设置图片加载器
        imagePicker.setShowCamera(true);  //显示拍照按钮
        imagePicker.setCrop(false);        //不允许裁剪（单选才有效）
        imagePicker.setMultiMode(false);   //false,单选 true 多选
        imagePicker.setSaveRectangle(true); //是否按矩形区域保存
        visibleReFresh = new VisibleReFresh();
        //配置网络请求返回的语言
//        LanuageConfig.isZn = !"en".equals(LocalDataProvider.getInstance().getLanguageCode());
////        setLanguage(LocalDataProvider.getInstance().getLanguageCode());
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
//            String processName = ProcessUtils.getProcessName(this);
//            if (!ProcessUtils.PROCESS_NAME.equalsIgnoreCase(processName)) {
//                //判断不等于默认进程名称
//                WebView.setDataDirectorySuffix(processName);
//            }
//        } else {
//            new WebView(this).destroy();
//        }


        //加载广告数据保存到本地

        initBackgroundCallBack();
    }


    int appCount = 0;
    Boolean isRunInBackground = true;


    private void initBackgroundCallBack() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            }

            @Override
            public void onActivityStarted(Activity activity) {
                appCount++;
                if (isRunInBackground) {
                    //应用从后台回到前台 需要做的操作
                    back2App(activity);
                    LogUtils.i("进入前台=========:");

                }
            }

            @Override
            public void onActivityResumed(Activity activity) {
            }

            @Override
            public void onActivityPaused(Activity activity) {
            }

            @Override
            public void onActivityStopped(Activity activity) {
                appCount--;
                if (appCount == 0) {
                    //应用进入后台 需要做的操作
                    leaveApp(activity);
                    LogUtils.i("进入后台=========:");
                }
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
            }
        });
    }

    /**
     * 从后台回到前台需要执行的逻辑
     *
     * @param activity
     */
    private void back2App(Activity activity) {
        isRunInBackground = false;
      //
    }

    /**
     * 离开应用 压入后台或者退出应用
     *
     * @param activity
     */
    private void leaveApp(Activity activity) {
        isRunInBackground = true;
    }

    static {//使用static代码段可以防止内存泄漏
        //全局设置默认的 Header
        SmartRefreshLayout.setDefaultRefreshHeaderCreator((context, layout) -> {
            //开始设置全局的基本参数（这里设置的属性只跟下面的MaterialHeader绑定，其他Header不会生效，能覆盖DefaultRefreshInitializer的属性和Xml设置的属性）
//            layout.setEnableHeaderTranslationContent(true);
            layout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white);
//            return new BoxRefreshHead(context);
            return new MaterialHeader(context);
        });
    }



    /**
     * <p> 不纳入友盟统计飞fragment
     */
    private void addFragmentName() {

    }


    public static SharedPreferences getCustomShare() {
        return WorkApp.workApp.getSharedPreferences(SPKeys.USER_SETTING_DATA, Context.MODE_PRIVATE);
    }

    public static void initFragmentConfig() {
        Fragmentation.builder()
                // 设置 栈视图 模式为 （默认）悬浮球模式   SHAKE: 摇一摇唤出  NONE：隐藏， 仅在Debug环境生效
                .stackViewMode(Fragmentation.BUBBLE)
                .debug(!BuildConfig.isRelease)
                /**
                 * 可以获取到{@link me.yokeyword.fragmentation.exception.AfterSaveStateTransactionWarning}
                 * 在遇到After onSaveInstanceState时，不会抛出异常，会回调到下面的ExceptionHandler
                 */
                .handleException(e -> {
                    LogUtils.e(e.toString());
                    // 以Bugtags为例子: 把捕获到的 Exception 传到 Bugtags 后台。
                    // Bugtags.sendException(e);
                })
                .install();
    }

    /**
     * 初始化cumeLibrary
     */
//    private void initLibrary() {
//        RxHttpManager.getInstance().setCookieJar(new CookieJar() {
////            private final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();
//
//            @Override
//            public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
//                String strUrl = url.url().toString();
//                if (strUrl.endsWith("login") || strUrl.endsWith("getAuthCodeToken") || strUrl.endsWith("register")) {
//                    //在请求成功之后，获取cookies信息
//                    if (cookies != null && cookies.size() > 0) {
//                        SharedPreferences.Editor editor = WorkApp.workApp.getSharedPreferences(Constants.SharedPrefrences.cookie_share
//                                , 0).edit();
//                        editor.clear().apply();
//                        int i = 0;
//                        StringBuilder result = new StringBuilder();
//                        for (Cookie cookie : cookies) {
//                            String name = cookie.name();
//                            String value = cookie.value();
//                            editor.putString(Constants.SharedPrefrences.cookie_key + ++i, name + "=" + value);
//                            if (!StringUtils.isEmpty(name + "=" + value)) {
//                                result.append(StringUtils.isEmpty(result.toString()) ? "" : ";").append(name + "=" + value);
//                            }
//                            LogUtils.i(url + "-->" + Constants.SharedPrefrences.cookie_key + i + ":" + name + "=" + value);
//                        }
//                        editor.putInt(Constants.SharedPrefrences.cookies_size, i);
//                        editor.apply();
//                    }
//                }
////                cookieStore.put(url.host(), cookies);
//            }
//
//            @Override
//            public List<Cookie> loadForRequest(HttpUrl url) {
//                String uri = url.url().toString();
//                if (uri.endsWith("login") || uri.endsWith("register")) { //登录和注册时时清除掉edit
//                    WorkApp.getContext().getSharedPreferences(Constants.SharedPrefrences.cookie_share, 0).edit().clear().apply();
//                    return new ArrayList<>();
//                } else {
//                    List<Cookie> cookies = new ArrayList<>();
//                    StringBuilder result = new StringBuilder();
//                    SharedPreferences sharedPreferences = WorkApp.getContext().getSharedPreferences(Constants.SharedPrefrences.cookie_share
//                            , 0);
//                    int cookie_size = sharedPreferences.getInt(Constants.SharedPrefrences.cookies_size, 0);
//                    for (int i = 1; i <= cookie_size; i++) {
//                        String cookie = sharedPreferences.getString(Constants.SharedPrefrences.cookie_key + i, "");
//                        if (!StringUtils.isEmptyOrNull(cookie)) {
//                            result.append(StringUtils.isEmpty(result.toString()) ? "" : ";").append(cookie);
//                            cookies.add(Cookie.parse(HttpUrl.get(com.btd.wallet.config.HttpUrl.URL.HOST), cookie));
//                        }
//                        LogUtils.i(result.toString());
//                    }
//                    return cookies;
//                }
//            }
//        });
//    }


    public GlideOptions getGlideOptions() {
        return mGlideOptions;
    }




    public static WorkApp getContext() {
        return workApp;
    }

    /**
     * 重启程序
     */
    public static void reset() {
        AllActivity.finishApp();
        final Intent intent = workApp.getPackageManager().getLaunchIntentForPackage(workApp.getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        workApp.startActivity(intent);
    }

    /**
     * 退出登录
     */
    public static void logout() {


    }

    public static WorkApp getInstance() {
        return workApp;
    }

    /**
     * 设置语言
     */
    public static void setLanguage(String languagleCode) {

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }

}
