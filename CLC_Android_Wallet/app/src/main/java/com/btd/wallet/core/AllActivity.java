package com.btd.wallet.core;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * activity管理类
 *
 * Created by 杨紫员 on 2017/10/20.
 */

public class AllActivity {
    private static List<Activity> allActivity = new ArrayList<>();

    public static void addActivity(Activity activity){
        if(!allActivity.contains(activity)){
            allActivity.add(activity);
        }
    }

    public static void removeActivity(Activity activity){
        if(allActivity.contains(activity)){
            allActivity.remove(activity);
        }
    }

    public static void finishApp(){
        if(allActivity!=null && allActivity.size()>0){
            for(Activity activity : allActivity){
                if(activity!=null){
                    activity.finish();
                }
            }
        }
    }

    public static int getActivitySize(){
        return allActivity.size();
    }

    /**
     * 获取顶部的Activity
     * @return
     */
   public static Activity getTopActivity(){
        return allActivity == null || allActivity.size() == 0 ? null : allActivity.get(allActivity.size() - 1);
   }
}
