package com.btd.wallet.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;

import com.btd.wallet.core.WorkApp;

import java.util.List;

/**
 * <preErcToBtd>
 * 创建: 廖林涛 2017/5/26 18:51;
 * 版本: $Rev: 13481 $ $Date: 2019-01-29 11:19:08 +0800 (周二, 29 一月 2019) $
 * </preErcToBtd>
 */
public class CMActivityUtils {

    private static ActivityManager.RunningTaskInfo getTopTask() {
        ActivityManager mActivityManager = (ActivityManager) WorkApp.workApp.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = mActivityManager.getRunningTasks(1);
        if (tasks != null && !tasks.isEmpty()) {
            return tasks.get(0);
        }
        return null;
    }

    private static boolean isTopActivity(
            ActivityManager.RunningTaskInfo topTask,
            String packageName,
            String activityName) {
        if (topTask != null) {
            ComponentName topActivity = topTask.topActivity;

            if (topActivity.getPackageName().equals(packageName) &&
                    topActivity.getClassName().equals(activityName)) {
                return true;
            }
        }

        return false;
    }

    /** 当前activity是否正在显示 */
    public static boolean isShowing(Activity activity) {
        return isTopActivity(getTopTask(), WorkApp.getContext().getPackageName(), activity.getClass().getName());
    }
}
