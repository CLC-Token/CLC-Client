package com.btd.wallet.utils;

import android.support.annotation.Nullable;

/**
 *  校验参数工具类
 *
 * <p>创建者 廖林涛
 * <p>创时间 2017/4/18 17:05
 * <p>版本 $Rev: 13481 $
 * <p>更新者 $Author$
 * <p>更新时间 $Date: 2019-01-29 11:19:08 +0800 (周二, 29 一月 2019) $
 * <p>更新描述
 */
public class CheckUtils {

    public static <T> T checkNotNull(T reference) {
        if (reference == null) {
            throw new NullPointerException();
        } else {
            return reference;
        }
    }

    public static <T> T checkNotNull(T reference, @Nullable Object errorMessage) {
        if(reference == null) {
            throw new NullPointerException(String.valueOf(errorMessage));
        } else {
            return reference;
        }
    }
}
