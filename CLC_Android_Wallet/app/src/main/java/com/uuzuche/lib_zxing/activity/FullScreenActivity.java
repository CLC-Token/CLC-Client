package com.uuzuche.lib_zxing.activity;

import android.os.Bundle;
import android.view.WindowManager;

import com.btd.wallet.base.activity.BaseSupportActivity;

/**
 * <preErcToBtd>
 * 创建: 廖林涛 2017/6/26 10:45;
 * 版本: $Rev: 9514 $ $Date: 2018-11-27 13:57:27 +0800 (周二, 27 11月 2018) $
 * </preErcToBtd>
 */
public abstract class FullScreenActivity extends BaseSupportActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 在全屏模式下,切换状态栏的显示与隐藏,不会重新布局
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        // 将window扩展至全屏，也就是全屏显示，并且不会覆盖状态栏。
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        super.onCreate(savedInstanceState);
    }
}
