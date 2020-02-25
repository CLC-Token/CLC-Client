package com.btd.wallet.mvp.view.dialog.listener;

/**
 * 返回输入信息对话框单击监听
 * Created by yzy on 2018/11/2 14:57
 */

public interface InfoDialogListener {
    void cancel();
    void confirm(String data);
}
