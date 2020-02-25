package com.btd.wallet.manager;

/**
 * Description:  主线程和子线程自由切换  <br>
 * Author: cxh <br>
 * Date: 2019/4/3 15:50
 */
public abstract class Task<T> {
    private T t;

    public Task(T t) {
        this.t = t;
    }

    public void setT(T t) {
        this.t = t;
    }

    public T getT() {
        return t;
    }

    //子线程
    public abstract void ioThread();

    //主线程
    public void uiThread(){

    }

    public void uiThread(T t){

    }
}