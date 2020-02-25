package com.btd.library.base.mvp.ipersenter;

import android.support.annotation.StringRes;

/**
 * Description:    <br>
 * Author: cxh <br>
 * Date: 2018/10/18 14:43
 */
public interface IBaseView {
    void showToast(String t);

    void showToast(@StringRes int resouceId);

    void setTitle(String title);

    void setTitle(@StringRes int resouceId);

    boolean isFragmentVisible();
}
