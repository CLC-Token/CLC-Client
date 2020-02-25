package com.btd.library.base.mvp.ipersenter;

import android.os.Bundle;

/**
 * Description:    <br>
 * Author: cxh <br>
 * Date: 2018/10/18 14:44
 */
public interface IBasePresenter {
    void onCreate(Bundle bundle);

    void initView();

    void initData();

    void onResume();

    void onSupportVisible();

    void onPause();

    void onDestroy();

    void finish();

    void log(String s);

}
