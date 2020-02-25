package com.btd.wallet.base.logManager;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.btd.library.base.util.LogUtils;
import com.btd.wallet.utils.CMFileUtils;

import java.io.File;

/**
 * 上传日志文件的Service
 * Created by yzy on 2018/2/25 16:22
 */

public class LogUploadService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void onCreate() {
        super.onCreate();
        asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    /**
     * 上传错误日志任务
     */
    @SuppressLint("StaticFieldLeak")
    AsyncTask asyncTask = new AsyncTask() {
        @Override
        protected Object doInBackground(Object[] objects) {
            File file = new File(CMFileUtils.LOG_PATH);
            if(file.exists()){
               File[] files = file.listFiles();
               if(files != null){
                   LogUtils.i("LogApi" , files.length + "");
                   for(File file1 : files){
                       new LogApi().uploadLog(file1.getAbsolutePath());
                   }
               }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            //执行完毕关闭Service
            LogUploadService.this.stopSelf();
        }
    };
}
