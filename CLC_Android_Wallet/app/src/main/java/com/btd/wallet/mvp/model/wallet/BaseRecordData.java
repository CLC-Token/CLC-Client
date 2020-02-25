package com.btd.wallet.mvp.model.wallet;

/**
 * $Author: xhunmon
 * $Date: 2018-01-10
 * $Description:
 */
public class BaseRecordData<T> {
    //用来装载不同类型的item数据bean
    T t;
    //item数据bean的类型
    int dataType;

    public BaseRecordData() {
    }

    public BaseRecordData(T t, int dataType) {
        this.t = t;
        this.dataType = dataType;
    }

    public T getT () {
        return t;
    }

    public void setT (T t) {
        this.t = t;
    }

    public int getDataType () {
        return dataType;
    }

    public void setDataType (int dataType) {
        this.dataType = dataType;
    }
}
