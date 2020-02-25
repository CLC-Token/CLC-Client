package com.btd.wallet.base.fragment;

import android.view.View;

/**
 * <p>左上角返回</p>
 * <p>创建: 廖林涛 2017/5/20 9:48
 * <p>版本: $Rev: 13481 $ $Date: 2019-01-29 11:19:08 +0800 (周二, 29 一月 2019) $
 */
public abstract class BackFragment extends BaseSupportFragment {

    /** 初始化左上角按钮,具备返回键功能 */
    @Override
    protected void initToolbar(View rootView) {
        super.initToolbar(rootView);
        setBack();
    }

}
