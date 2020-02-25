package com.btd.wallet.base.adapter;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.btd.wallet.base.fragment.BaseSupportFragment;
import com.btd.wallet.utils.CheckUtils;

/**
 * <p>封装了tab标题
 * <p>创建: 廖林涛 2017/5/17 9:59
 * <p>版本: $Rev: 15638 $ $Date: 2019-03-15 20:01:43 +0800 (周五, 15 3月 2019) $
 */
public abstract class FPAdapter extends FragmentPagerAdapter {
    public String[] mTabs;

    public FPAdapter(FragmentManager fm, @NonNull String[] tabs) {
        super(fm);
        mTabs = CheckUtils.checkNotNull(tabs);
    }

    @Override
    public abstract BaseSupportFragment getItem(int position);

    @Override
    public int getCount() {
        return mTabs.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTabs[position];
    }

}