package com.btd.wallet.mvp.model;

import com.btd.library.base.util.JsonHelpUtil;

/**
 * Created by yzy on 2018/5/3 15:31
 */

public class ActivateReq {

    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return JsonHelpUtil.formatGson().create().toJson(this);
    }
}
