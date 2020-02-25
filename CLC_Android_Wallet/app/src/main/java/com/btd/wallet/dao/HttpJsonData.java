package com.btd.wallet.dao;

import android.util.Base64;

import com.btd.library.base.util.JsonHelpUtil;
import com.btd.library.base.util.LogUtils;
import com.btd.wallet.config.Constants;

import com.btd.wallet.mvp.model.BaseReq;
import com.btd.wallet.utils.MD5Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by yzy on 2018/5/3 16:29
 */

public class HttpJsonData {
    /**
     * @return 将私家云1级json需要加密的键值对存储起来
     */
    private static <T> HashMap<String, Object> getWXJBaseJsonKeyValue(BaseReq model) {
        HashMap<String, Object> keyValues = new HashMap<>();
        keyValues.put(BaseReq.KeyName.accessId, model.getAccessId()==null?"":model.getAccessId());
        keyValues.put(BaseReq.KeyName.AppId, model.getAppId());
        keyValues.put(BaseReq.KeyName.cmdName, model.getCmdName());
        keyValues.put(BaseReq.KeyName.deviceId, model.getDeviceId()==null?"":model.getDeviceId());
        keyValues.put(BaseReq.KeyName.reqType, model.getReqType());
        keyValues.put(BaseReq.KeyName.TimeStamp, model.getTimeStamp());
     //   keyValues.put(BaseReq.KeyName.userId, model.getUserId()==null?"":model.getUserId());
        keyValues.put(BaseReq.KeyName.v, model.getV()==null?"":model.getV());
        keyValues.put(BaseReq.KeyName.data, model.getData() == null ? "" : model.getData());
     //   keyValues.put(KeyName.language , model.getLanguage());
        return keyValues;
    }

    public static BaseReq getReq(BaseReq baseReq){
        String strData = baseReq.getData()==null ? "{}" : JsonHelpUtil.formatGson().create().toJson(baseReq.getData());
        LogUtils.d("getReq  BaseReq : "+ JsonHelpUtil.formatGson().create().toJson(baseReq));
        String strB64Data = Base64.encodeToString(strData.getBytes(), Base64.NO_WRAP);
        baseReq.setData(strB64Data);
        baseReq.setTimeStamp(System.currentTimeMillis());
        String sign = "";
         /* 计算sign的值 */
        HashMap<String, Object> wxjBaseJsonKeyValue = getWXJBaseJsonKeyValue(baseReq);
        ArrayList<String> sortKeyName = new ArrayList<>(wxjBaseJsonKeyValue.keySet());
        Collections.sort(sortKeyName);
        for (int i = 0; i < sortKeyName.size(); i++) {
            sign = sign + wxjBaseJsonKeyValue.get(sortKeyName.get(i));
        }
        sign = sign + Constants.Appkey;
        sign = MD5Util.md5(sign);
        baseReq.setSign(sign);
        return baseReq;
    }
}
