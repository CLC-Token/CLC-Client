package com.btd.wallet.base.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.btd.library.base.util.LogUtils;
import com.btd.wallet.pure.R;
import com.btd.wallet.base.fragment.BaseSupportFragment;
import com.btd.wallet.core.AllActivity;
import com.btd.wallet.core.WorkApp;

import com.btd.wallet.utils.MethodUtils;


import java.lang.reflect.Field;
import java.lang.reflect.Method;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.yokeyword.fragmentation.ISupportFragment;
import me.yokeyword.fragmentation.SupportActivity;
import me.yokeyword.fragmentation.SupportFragment;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

/**
 * Description:    <br>
 * Author: cxh <br>
 * Date: 2018/10/18 14:55
 */
public abstract class BaseSupportActivity extends SupportActivity {

    protected BaseSupportActivity mActivity;
    protected String TAG = BaseSupportActivity.class.getSimpleName();
    private Unbinder mUnbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O && isTranslucentOrFloating()) {
            boolean result = fixOrientation();
            LogUtils.d("onCreate fixOrientation when Oreo, result = " + result);
        }
        super.onCreate(savedInstanceState);
        try {
            AllActivity.addActivity(this);
            mActivity = this;
            TAG = getClass().getSimpleName();
            LogUtils.d(TAG);
            // 内容部分
            View contentView = View.inflate(this, getContentView(), null);
            FrameLayout rootLayout = new FrameLayout(this);
            rootLayout.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT
                    , FrameLayout.LayoutParams.MATCH_PARENT));
            rootLayout.addView(contentView);
            setContentView(rootLayout);
            mUnbinder = ButterKnife.bind(this);
//            // 设置当前页面是否常亮
//            CMOsUtils.setScreenLight(this, AutoUploadTasks.getInstance().isAutoUpload());
            Bundle bundle = getIntent().getExtras();
//            setSystemColor();
            parseBundle(bundle);
            initFragments(savedInstanceState);
            initView();
            initData();

        } catch (Exception e) {
            LogUtils.i(e.getMessage());
        }

    }

    private boolean fixOrientation() {
        try {
            Field field = Activity.class.getDeclaredField("mActivityInfo");
            field.setAccessible(true);
            ActivityInfo o = (ActivityInfo) field.get(this);
            o.screenOrientation = -1;
            field.setAccessible(false);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean isTranslucentOrFloating() {
        boolean isTranslucentOrFloating = false;
        try {
            int[] styleableRes = (int[]) Class.forName("com.android.internal.R$styleable").getField("Window").get(null);
            final TypedArray ta = obtainStyledAttributes(styleableRes);
            Method m = ActivityInfo.class.getMethod("isTranslucentOrFloating", TypedArray.class);
            m.setAccessible(true);
            isTranslucentOrFloating = (boolean) m.invoke(null, ta);
            m.setAccessible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isTranslucentOrFloating;
    }


    /**
     * 初始化界面
     */
    protected abstract void initView();

    /**
     * 初始化数据
     */
    protected void initData() {

    }

    /**
     * 在{@link #initFragments(Bundle)}之前调用;
     *
     * @param data activity传递过来的数据
     */
    protected void parseBundle(Bundle data) {

    }

    /**
     * 布局文件
     *
     * @return 默认返回一个壳, FrameLayout的id为contentFrame
     */
    protected int getContentView() {
        return R.layout.activity_base;
    }

    /**
     * 空实现
     * <p>初始化fragment,初始化presenter</p>
     * <p>如果包含fragment,建议在这里初始化fragment,并处理fragment在内存重启时的动作.
     */
    protected void initFragments(Bundle savedInstanceState) {

    }

    protected void startActivity(Class<? extends FragmentActivity> activity, @Nullable Bundle bundle) {
        Intent starter = new Intent(mActivity, activity);
        if (bundle != null) {
            starter.putExtras(bundle);
        }
        startActivity(starter);
    }

    protected void startActivity(Class<? extends FragmentActivity> activity) {
        startActivity(activity, null);
    }

    /**
     * 填充布局,默认不绑定到根节点
     */
    protected View inflate(@LayoutRes int resource) {
        return getLayoutInflater().inflate(resource, (ViewGroup) findViewById(android.R.id.content), false);
    }

    /**
     * 默认布局id:R.id.contentFrame
     */
    public void loadRootFragment(SupportFragment toFragment) {
        super.loadRootFragment(R.id.contentFrame, toFragment);
    }

    /**
     * 替换根节点,对上一个Fragment不压入回退栈,会直接销毁上一个Fragment
     */
    public void replaceFragment(SupportFragment toFragment) {
        super.replaceFragment(toFragment, false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtils.i(TAG);
        AllActivity.removeActivity(this);
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        ISupportFragment topFragment = getTopFragment();
        LogUtils.i(topFragment != null ? topFragment.getClass().getSimpleName() : "无顶层Fragment");
        if (topFragment != null && topFragment instanceof BaseSupportFragment) {
            BaseSupportFragment baseSupportFragment = (BaseSupportFragment) topFragment;
            if (baseSupportFragment.onKeyDown(keyCode, event)) {
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        return new FragmentAnimator();
        // 默认竖向(和安卓5.0以上的动画相同)
//        return super.onCreateFragmentAnimator();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ISupportFragment topFragment = getTopFragment();
        LogUtils.i(topFragment != null ? topFragment.getClass().getSimpleName() : "无顶层Fragment");
        if (topFragment != null && topFragment instanceof BaseSupportFragment) {
            BaseSupportFragment baseSupportFragment = (BaseSupportFragment) topFragment;
            baseSupportFragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public boolean checkPermission(@NonNull String permission) {
        return ActivityCompat.checkSelfPermission(mActivity, permission) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    protected void onResume() {
        super.onResume();

    }


    @Override
    protected void onPause() {
        super.onPause();

    }

    public void onBackToFirstFragment() {

    }

    /**
     * 安全的获取资源
     */
    protected String getStr(@StringRes int strId) {
        return MethodUtils.getString(strId);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
    }
}
