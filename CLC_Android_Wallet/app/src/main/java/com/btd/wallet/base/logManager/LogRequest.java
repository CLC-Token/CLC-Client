package com.btd.wallet.base.logManager;

/**
 * 日志上传实体参数
 * Created by yzy on 2018/2/27 13:55
 */

public class LogRequest {
    private String tag;         //tag
    private String className;   //类名
    private int level;          //等级
    private long time;          //产生时间
    private String appVersion;  //app版本
    private String sdkVersion;  //sdk版本(7.0等)
    private String phoneType;   //手机类型(oppo a51)
    private String platform = "Android";    //平台(android,ios)
    private String msg;         //消息
    /**
     * accessId : dgsdrt3845oj423uo424j242432
     * deviceId : fasfsafse242423423
     */

    private String accessId;
    private String deviceId;

    public LogRequest() {
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getSdkVersion() {
        return sdkVersion;
    }

    public void setSdkVersion(String sdkVersion) {
        this.sdkVersion = sdkVersion;
    }

    public String getPhoneType() {
        return phoneType;
    }

    public void setPhoneType(String phoneType) {
        this.phoneType = phoneType;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

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

    @Override
    public String toString() {
        return "LogRequest{" +
                "tag='" + tag + '\'' +
                ", className='" + className + '\'' +
                ", level=" + level +
                ", time=" + time +
                ", appVersion='" + appVersion + '\'' +
                ", sdkVersion='" + sdkVersion + '\'' +
                ", phoneType='" + phoneType + '\'' +
                ", platform='" + platform + '\'' +
                ", msg='" + msg + '\'' +
                ", accessId='" + accessId + '\'' +
                ", deviceId='" + deviceId + '\'' +
                '}';
    }
}
