package com.btd.wallet.mvp.model;

import com.btd.library.base.util.JsonHelpUtil;
import com.btd.wallet.config.HttpUrl;


import java.io.Serializable;

/**
 * Created by yzy on 2018/5/3 15:45
 */

public class BaseReq<T> implements Serializable{

    public String getV() {
        return v;
    }

    public void setV(String v) {
        this.v = v;
    }

    /** 接口签名所需要的字段, data除外 */
    public interface KeyName {
        String AppId = "AppId";
        String TimeStamp = "TimeStamp";
        String cmdName = "cmdName";
        String reqType = "reqType";
        String userId = "userId";
        String accessId = "accessId";
        String deviceId = "deviceId";
        String v = "v";
        String data = "data";
        String language = "language";
    }

    /**
     * data : {"address":"zPy7F26YWWKarmusgHwR4m3egYL93ZJ9Br"}
     * userId : b79b573880b04ec2ad6c6dc1db7af83c
     * reqType : 5
     * deviceId : gh_5a6d62591f97_ad37243f558efaf9
     * cmdName : activateWallet
     * accessId : b79b573880b04ec2ad6c6dc1db7af83c
     * AppId : 70076467277E4B1DD42F21B4DB5BD5A7
     * TimeStamp : 1516107541415
     * sign : 41e8075fe487be97ead49b1bdd837c
     */

    private T data;
    private String userId = "";
    private int reqType = 5;
    private String cmdName;
    private String AppId = HttpUrl.Config.APPID;
    private long TimeStamp;
    private String sign;
    private String deviceId;
    private String accessId;
    private String v = "2.6";




    public String getAccessId() {
        return accessId;
    }

    public void setAccessId(String accessId) {
        this.accessId = accessId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
    //反序列化的话 deserialize = false; 序列化 serialize
//    @Expose(serialize = false, deserialize = false)

    private transient String url;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getReqType() {
        return reqType;
    }

    public void setReqType(int reqType) {
        this.reqType = reqType;
    }

    public String getCmdName() {
        return cmdName;
    }

    public void setCmdName(String cmdName) {
        this.cmdName = cmdName;
    }

    public String getAppId() {
        return AppId;
    }

    public void setAppId(String AppId) {
        this.AppId = AppId;
    }

    public long getTimeStamp() {
        return TimeStamp;
    }

    public void setTimeStamp(long TimeStamp) {
        this.TimeStamp = TimeStamp;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "url:" + url + '\n' + JsonHelpUtil.formatGson().disableHtmlEscaping().create().toJson(this);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
