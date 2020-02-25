package com.btd.wallet;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.support.annotation.Nullable;

import android.view.Window;


import com.btd.wallet.base.activity.BaseSupportActivity;


import com.btd.wallet.mvp.view.wallet.WalletFragment;
import com.btd.wallet.pure.R;
import com.btd.wallet.utils.MethodUtils;
import com.btd.wallet.utils.StatusBarUtil;


public class MainActivity extends BaseSupportActivity {

    public static boolean isCheckUpdate = false;
    private static final int REQUEST_EXTRANS = 10021;
    private static final int GPS_REQUEST_CODE = 0x22;

    public static void startActivityForNewTask(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    @Override
    protected int getContentView() {
        return R.layout.activity_base;
    }

    @Override
    protected void initView() {
        //进行连接sdk
      //  HDTManage.getInstance();

    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        setTheme(R.style.FullscreenTheme);

        super.onCreate(savedInstanceState);
        MethodUtils.setMainStuteCorlor(this);
     //   EventBus.getDefault().register(this);
    }

    @Override
    protected void initFragments(Bundle savedInstanceState) {
        super.initFragments(savedInstanceState);
        if (getTopFragment() == null) {
            loadRootFragment(WalletFragment.newInstance());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
      //  EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GPS_REQUEST_CODE) {

        }
    }




//    @Override
//    protected void onResume() {
//        super.onResume();
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//    }
}
