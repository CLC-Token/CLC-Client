package com.btd.wallet.manager;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Description: 主线程和子线程自由切换   <br>
 * Author: cxh <br>
 * Date: 2019/4/3 15:52
 */
public class TaskManage {

    public static <T> void doTask(final Task<T> task) {
        Observable
                .create((ObservableOnSubscribe<T>) emitter -> {
                    task.ioThread();
                    emitter.onNext(task.getT());
                    emitter.onComplete();
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<T>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(T data) {
                        task.uiThread(data);
                        task.uiThread();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
