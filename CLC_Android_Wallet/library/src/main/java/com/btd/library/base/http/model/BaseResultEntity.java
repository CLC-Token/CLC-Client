package com.btd.library.base.http.model;

/**
 * 统一返回数据基类
 * Created by yzy on 2018/10/18 15:57
 */

public class BaseResultEntity<T> {
    //  判断标示
    private int code;
    //    提示信息
    private String info;
    //显示数据（用户需要关心的数据）
    private T data;
    private int extraCode = -1;
    private ErrorInfo _info;

    public ErrorInfo get_info() {
        return _info;
    }

    public void set_info(ErrorInfo _info) {
        this._info = _info;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getExtraCode() {
        return extraCode;
    }

    public void setExtraCode(int extraCode) {
        this.extraCode = extraCode;
    }
}
