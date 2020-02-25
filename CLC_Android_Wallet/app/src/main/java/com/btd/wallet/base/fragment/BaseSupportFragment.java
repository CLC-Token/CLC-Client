package com.btd.wallet.base.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.StringRes;
import android.support.v4.app.FragmentActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.btd.library.base.util.CMScreenUtils;
import com.btd.library.base.util.LogUtils;
import com.btd.wallet.pure.R;
import com.btd.wallet.core.AllActivity;
import com.btd.wallet.core.WorkApp;
import com.btd.wallet.event.select.EditToolbarVisibleEvent;
import com.btd.wallet.event.select.MenuExitEditStatusEvent;
import com.btd.wallet.event.select.MenuSelectAllEvent;
import com.btd.wallet.utils.DelayUtils;
import com.btd.wallet.utils.KeyboardUtils;

import com.btd.wallet.utils.MethodUtils;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.yokeyword.fragmentation.ISupportFragment;
import me.yokeyword.fragmentation.SupportActivity;
import me.yokeyword.fragmentation.SupportFragment;


/**
 * Description:    <br>
 * Author: cxh <br>
 * Date: 2018/10/19 18:20
 */
public abstract class BaseSupportFragment extends SupportFragment {

    private Unbinder mUnbinder;
    /**
     * 头部布局根节点
     */
    protected FrameLayout mVgToolbar;
    protected Toolbar mToolbar;
    private TextView mTxtTitle;
    protected SupportActivity mActivity;
    protected String TAG = BaseSupportFragment.class.getSimpleName();
    protected View mRootView;
    /**
     * 全屏模式下,statusBar是否可见
     */
    protected boolean fullScreen = true;
    private View mFakeStatusBar;
    protected Bundle mBundle;
    //编辑状态的控件
    // 编辑状态下的toolbar,根节点
    protected RelativeLayout mLayoutToolbarEdit;
    /**
     * 编辑状态下选中的数量
     */
    protected TextView mTxtSelectedNum;
    /**
     * 全选,全不选
     */
    protected TextView mTxtSelectAll;
    /**
     * 编辑状态取消
     **/
    protected TextView txtEditLeft;

    protected MenuItem messageItem;
    protected boolean hasNewMsg = false;//是否有新消息

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (SupportActivity) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        mRootView = null;
        try {
            mRootView = inflater.inflate(getContentView(), container, false);
            mUnbinder = ButterKnife.bind(this, mRootView);
            TAG = getClass().getSimpleName();
            LogUtils.d(TAG);
            mBundle = getArguments();
            initFragments(savedInstanceState);
            initToolbar(mRootView);
            initView();
            initData();

        } catch (Exception e) {
            LogUtils.e(e.toString());
        }
        if (null == mRootView) {
            mRootView = new View(mActivity);
        }
        return mRootView;

    }

    /**
     * 布局文件
     *
     * @return 默认父Fragment布局, 提供toolbar, 和一个id为contentFrame的FrameLayout
     */
    protected int getContentView() {
        return R.layout.fragment_base;
    }

    /**
     * 如果fragment是控制器,则需要覆盖此方法,来控制子fragment的初始化,
     * 在{@link #initView()} 之前调用.
     */
    protected void initFragments(Bundle savedInstanceState) {

    }

    protected abstract void initView();

    protected void initData() {
    }

    @Override
    public void onDestroy() {
        LogUtils.d(TAG);
        super.onDestroy();
        try{

        }catch (Exception e){
            if (mUnbinder != null) {
                mUnbinder.unbind();
            }
        }

    }

    /**
     * 提高menu布局文件id,填充为Toolbar的菜单,默认布局为menu_base.xml
     */
    protected int getMenuLayoutResId() {
        return R.menu.menu_base;
    }

    /**
     * toolbar配置菜单
     */
    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(getMenuLayoutResId(), menu);
        LogUtils.i(TAG + "菜单 = " + menu.size());
        LogUtils.i("mToolbar = " + mToolbar);
        setIconEnable(menu, true);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        LogUtils.i(TAG + "");
        if (mRootView != null) {
            initToolbar(mRootView);
        }
//        if(mActivity != null){
//            mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
//        }
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
//        if(mActivity != null){
//            mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//        }
    }

    DelayUtils mDelayUtils;

    /**
     * 关闭长按菜单栏触发toast问题
     */
    private void endMenuLong() {
        if (mDelayUtils != null) {
            mDelayUtils.stop();
            mDelayUtils = null;
        }
        mDelayUtils = new DelayUtils(along -> {
            if (mActivity == null || mActivity.isFinishing()) {
                return;
            }
            /*final View editView = mActivity.findViewById(R.id.item_edit);

            View.OnLongClickListener onLongClickListener = v -> true;
            if (editView != null) {
                editView.setOnLongClickListener(onLongClickListener);
            }*/
        });
        mDelayUtils.start();
    }

    /**
     * @param menu 菜单的图标是否显示
     */
    @SuppressLint("RestrictedApi")
    protected void setIconEnable(Menu menu, boolean show) {
        try {
            if (menu instanceof MenuBuilder) {
                ((MenuBuilder) menu).setOptionalIconsVisible(show);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 切换可编辑toolbar的显示与隐藏
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setEditToolbarVisible(EditToolbarVisibleEvent event) {
        if (!isSupportVisible()) {
            LogUtils.i("不可见");
            return;
        }
        LogUtils.i("event");
        initEditToolbar();
        if (mLayoutToolbarEdit != null) {
            MethodUtils.setVisible(mLayoutToolbarEdit, event.isVisible);
        }
    }

    /**
     * 初始化Toolbar
     */
    protected void initToolbar(View rootView) {
        if (mVgToolbar == null) {
            mVgToolbar = rootView.findViewById(R.id.vg_toolbar);
        }
        if (mTxtTitle == null) {
            mTxtTitle = rootView.findViewById(R.id.txt_title);
        }
        if (mToolbar == null) {
            mToolbar = rootView.findViewById(R.id.toolbar);
        }
        if (mToolbar == null) {
            LogUtils.i(TAG + ":toolbar为空");
            return;
        }
        // 不调用,不会走onCreateMenu方法
        mActivity.setSupportActionBar(mToolbar);
        if (mActivity.getSupportActionBar() != null) {
            // 关闭系统返回上一级的按钮,以及旁边的标题
            mActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            mActivity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        if (getMenuLayoutResId() > 0) {
            setHasOptionsMenu(true);
        }
        LogUtils.i(TAG + ":toolbar 创建");
    }

    /**
     * 初始化编辑状态下,toolbar的元素
     */
    protected void initEditToolbar() {
        if (mLayoutToolbarEdit == null) {
            mLayoutToolbarEdit = (RelativeLayout) mRootView.findViewById(R.id.layout_toolbar_edit);
        }

        if (mLayoutToolbarEdit == null) {
            LogUtils.i("mLayoutToolbarEdit == null");
            return;
        }

        if (mTxtSelectedNum == null) {
            mTxtSelectedNum = (TextView) mLayoutToolbarEdit.findViewById(R.id.txt_edit_center);
        }

        if (mTxtSelectAll == null) {
            mTxtSelectAll = (TextView) mLayoutToolbarEdit.findViewById(R.id.txt_edit_right);
            mTxtSelectAll.setOnClickListener(this::onNewClick);
        }
        if (txtEditLeft == null) {
            txtEditLeft = (TextView) mLayoutToolbarEdit.findViewById(R.id.txt_edit_left);
            txtEditLeft.setOnClickListener(this::onNewClick);
        }

    }

    protected void onNewClick(View v) {
        switch (v.getId()) {
            case R.id.txt_edit_left:
                // 取消,退出编辑状态
                EventBus.getDefault().post(new MenuExitEditStatusEvent());
                break;
            case R.id.txt_edit_right:
                // 全选,全不选切换
                EventBus.getDefault().post(new MenuSelectAllEvent());
                if (getStr(R.string.select_all).equals(mTxtSelectAll.getText().toString())) {
                    mTxtSelectAll.setText(R.string.unselect_all);
                } else {
                    mTxtSelectAll.setText(R.string.select_all);
                }
                break;
            default:
                break;
        }
    }

    /**
     * menu的单击事件
     *
     * @param view
     */
    protected void menuClick(View view) {
    }

    /**
     * 是否设置顶部的padding,用于全屏模式下往下调整界面
     *
     * @return
     */
    protected boolean setTopHeader() {
        return true;
    }

    /**
     * 头部padding的view
     *
     * @return
     */
    protected View getTopHeader() {
        return mVgToolbar;
    }

    /**
     * 设置toolbar标题
     */
    public void setTitle(String title) {
        setTitle(title, false);
    }

    /**
     * 设置toolbar标题
     */
    public void setTitle(@StringRes int strRes) {
        setTitle(strRes, false);
    }

    /**
     * 设置toolbar标题
     */
    protected void setTitle(String title, boolean isShowEditText) {
        title = TextUtils.isEmpty(title) ? "" : title;
        if (isShowEditText) {
            if (mTxtTitle != null) {
                mTxtTitle.setVisibility(View.GONE);
            }
        } else {
            if (mTxtTitle != null) {
                mTxtTitle.setVisibility(View.VISIBLE);
                mTxtTitle.setText(title);
            }
        }

    }

    protected void setTitle(@StringRes int strRes, boolean isShowEditText) {
        setTitle(getStr(strRes), isShowEditText);
    }

    /**
     * toolbar设置左上角按钮
     */
    protected void setNavigationIcon(@DrawableRes int drawableId) {
        if (mToolbar != null) {
            mToolbar.setNavigationIcon(drawableId);

        }
    }

    /**
     * toolbar清除左上角按钮
     */
    protected void clearNavigationIcon() {
        if (mToolbar != null) {
            mToolbar.setNavigationIcon(null);
        }
    }

    /**
     * toolbar左上角按钮监听
     */
    protected void setNavigationOnClickListener(View.OnClickListener listener) {
        if (mToolbar != null) {
            mToolbar.setNavigationOnClickListener(listener);
        }

    }

    /**
     * 显示通用返回键,需要在 {@link #onLazyInitView(Bundle)} 之后调用
     */
    protected void setBack() {
        setNavigationIcon(R.drawable.arrow_back);
        setNavigationOnClickListener(v -> {
            if (KeyboardUtils.isOpen(mActivity)) {
                KeyboardUtils.hideKeyboard(mActivity, v);
            }
            int count = mActivity.getSupportFragmentManager().getBackStackEntryCount();
            LogUtils.w("返回 count： " + count + ", mActivity:" + mActivity + ",size:" + AllActivity.getActivitySize());
            if (count > 1) {
                pop();
            } else {
                mActivity.onBackPressed();
            }
        });
        if (mToolbar != null) {
            LogUtils.i(TAG + "初始化左上角按钮,具备返回键功能");
        }
    }

    protected void startActivity(Class<? extends FragmentActivity> activity, @Nullable Bundle bundle) {
        Intent starter = new Intent(mActivity, activity);
        if (bundle != null) {
            starter.putExtras(bundle);
        }
        mActivity.startActivity(starter);
    }

    protected void startActivity(Class<? extends FragmentActivity> activity) {
        startActivity(activity, null);
    }


    /**
     * 填充布局,默认不绑定到根节点
     */
    protected View inflate(@LayoutRes int resource) {
        return mActivity.getLayoutInflater().inflate(resource, (ViewGroup) mActivity.findViewById(android.R.id.content), false);
    }

    /**
     * 默认布局id:R.id.contentFrame
     */
    public void loadRootFragment(SupportFragment toFragment) {
        super.loadRootFragment(R.id.contentFrame, toFragment);
    }

    /**
     * 默认布局id:R.id.contentFrame
     */
    public void loadRootFragment(SupportFragment toFragment, boolean canBack) {
        super.loadRootFragment(R.id.contentFrame, toFragment, canBack, true);
    }

    /**
     * 安全的获取资源
     */
    protected String getStr(@StringRes int strId) {
        return MethodUtils.getString(strId);
    }

    protected void setFullScreen(boolean fullScreen) {
        setFullScreen(fullScreen, true);
    }

    /**
     * {@link #fullScreen}
     */
    protected void setFullScreen(boolean fullScreen, boolean hideNavigation) {
        try {
            this.fullScreen = fullScreen;
            setFakeBarVisible(fullScreen);
            MethodUtils.setVisible(mToolbar, fullScreen);
            WindowManager.LayoutParams attrs = mActivity.getWindow().getAttributes();
            if (fullScreen) {
                // 显示
                attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
                if (Build.VERSION.SDK_INT >= 19 && hideNavigation) {
                    attrs.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE;
                    LogUtils.i("隐藏 navigationbar");
                }
            } else {
                // 隐藏
                attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
            }
            mActivity.getWindow().setAttributes(attrs);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e(e.toString());
        }
    }

    /**
     * 设置伪造的statusBar可见
     */
    protected void setFakeBarVisible(boolean fakeBarVisible) {
        if (mFakeStatusBar == null) {
            mFakeStatusBar = mRootView.findViewById(R.id.view_fake_status_bar);
        }
        if (mFakeStatusBar != null) {
            mFakeStatusBar.getLayoutParams().height = CMScreenUtils.getStatusHeight(mActivity);
        }
        MethodUtils.setVisible(mFakeStatusBar, fakeBarVisible);
    }

    protected void activityFinish() {
        if (mActivity != null && !mActivity.isFinishing()) {
            mActivity.finish();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (useEventBus()) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (useEventBus()) {
            EventBus.getDefault().unregister(this);
        }
        MethodUtils.hideSoft(mActivity, mRootView);
    }

    protected void startParentFragment(BaseSupportFragment baseSupportFragment, boolean canBack) {
        try {
            BaseSupportFragment parentFragment = (BaseSupportFragment) getParentFragment();
            if (parentFragment != null) {
                MethodUtils.hideSoft(mActivity, mRootView);
                if (canBack) {
                    parentFragment.start(baseSupportFragment);
                } else {
                    parentFragment.startWithPop(baseSupportFragment);
                }
            }
        } catch (Exception ex) {
            LogUtils.i(ex.getMessage());
        }
    }

    protected void startParentFragment(BaseSupportFragment baseSupportFragment, boolean canBack, int lunchModel) {
        try {
            BaseSupportFragment parentFragment = (BaseSupportFragment) getParentFragment();
            if (parentFragment != null) {
                MethodUtils.hideSoft(mActivity, mRootView);
                if (canBack) {
                    parentFragment.start(baseSupportFragment);
                } else {
                    parentFragment.startWithPop(baseSupportFragment);
                }
            }
        } catch (Exception ex) {
            LogUtils.i(ex.getMessage());
        }
    }

    protected void startParentFragment(BaseSupportFragment baseSupportFragment) {
        startParentFragment(baseSupportFragment, true);
    }

    protected void changeBottom(int selectNum) {

    }

    /**
     * fragment监听Activity中的onKeyDown
     *
     * @param keyCode
     * @param event
     * @return
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    @Override
    public void start(ISupportFragment toFragment) {
        MethodUtils.hideSoft(mActivity, mRootView);
        super.start(toFragment);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!WorkApp.workApp.fragmentName.contains(TAG)) {
            /* 过滤不纳入友盟统计的fragment */

        }
        LogUtils.d(TAG);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (!WorkApp.workApp.fragmentName.contains(TAG)) {
            /* 过滤不纳入友盟统计的fragment */

        }
        LogUtils.d(TAG);
    }

    /**
     * 友盟统计
     */
    public void onUMengEvent(String event)
    {

    }

    /**
     * 在MainActivity需要发送MainSelectNumChangeEvent广播改变状态
     */
    public boolean isMainSelectNumChangeEvent() {
        return true;
    }

    public void setSelectCenterVisible(boolean isShow) {
        if (null != mTxtSelectedNum) {
            mTxtSelectedNum.setVisibility(isShow ? View.VISIBLE : View.GONE);
        }
    }

    protected boolean useEventBus() {
        return false;
    }

    /**
     * 显示Toast
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    protected void showToast(@StringRes int stringResId) {
        if (mActivity != null && !mActivity.isFinishing() && !mActivity.isDestroyed() && this.isSupportVisible()) {
            MethodUtils.showToast(mActivity, getString(stringResId));
        }
    }

    /**
     * 显示Toast
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    protected void showToast(String des) {
        if (mActivity != null && !mActivity.isFinishing() && !mActivity.isDestroyed()) {
            MethodUtils.showToast(mActivity, des);
        }
    }

    /**
     * 替换根节点,对上一个Fragment不压入回退栈,会直接销毁上一个Fragment
     */
    public void replaceLoadRootFragment(SupportFragment toFragment, boolean canBack) {
//        loadRootFragment(toFragment,canBack);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }



}
