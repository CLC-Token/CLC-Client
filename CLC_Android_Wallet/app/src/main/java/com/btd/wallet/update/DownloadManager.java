package com.btd.wallet.update;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.FileProvider;
import android.widget.RemoteViews;

import com.btd.library.base.http.callback.HttpCallback;
import com.btd.library.base.http.manager.RxHttpManager;
import com.btd.library.base.util.LogUtils;

import com.btd.wallet.config.Constants;
import com.btd.wallet.config.HttpUrl;
import com.btd.wallet.config.SPKeys;
import com.btd.wallet.core.WorkApp;

import com.btd.wallet.mvp.view.dialog.ConfirmDialog;
import com.btd.wallet.mvp.view.dialog.listener.DialogListener;
import com.btd.wallet.pure.R;
import com.btd.wallet.utils.MethodUtils;
import com.btd.wallet.utils.PDialogUtil;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import me.yokeyword.fragmentation.SupportActivity;
import rx.schedulers.Schedulers;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * 版本更新,下载apk任务
 * <preErcToBtd>
 * 创建: 廖林涛 2017/5/26 15:09;
 * 版本: $Rev: 4935 $ $Date: 2018-07-30 18:49:43 +0800 (周一, 30 七月 2018) $
 * </preErcToBtd>
 */
public class DownloadManager {
    private static final int DOWN_OK = 1; // 下载完成
    private static final int DOWN_ERROR = 0;
    private static final int REQUEST_EXTRANS = 10021;
    /* 下载保存路径 */
    private String mSavePath;
    /* 是否取消更新 */
    private final String app_name;
    private final int notification_id = 0;
    private RemoteViews contentView;
    private PendingIntent pendingIntent;
    private UpdateVersionDialog updateDialog;
    private final Context mContext;
    private boolean isTip = true;
    private VersionModel versionModel;
    private NotificationManager notificationManager;
    private Notification notification;
    private final UICallBackDao uICallBackDao;
    private final static String HOST = HttpUrl.URL.UPDATE_HOST;
    public final static String URL = HOST + "clc-api/public/versions/latest?version_type=android&&bit=32"; //获取更新日志

    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DOWN_OK:
                    // 下载完成，点击安装
                    if (PDialogUtil.isShowing()) {
                        PDialogUtil.stopProgress();
                    }
//                    if (notificationManager != null) {
//                        notificationManager.cancel(notification_id);
//                        installApk();
//                    }
                    if (uICallBackDao != null) {
                        uICallBackDao.download(true);
                    }
//                    if (updateDialog != null) {
//                        updateDialog.dismiss();
//                        updateDialog.setBtnEnable(true);
//                    }
                    EventBus.getDefault().post(new UpdateDownloadDialogEvent(100));
                    installApk();
                    break;
                case DOWN_ERROR:
                    if (PDialogUtil.isShowing()) {
                        PDialogUtil.stopProgress();
                    }
                    // notification.setLatestEventInfo(mContext, app_name, "下载失败",
                    // pendingIntent);
//                    notification = new Notification.Builder(WorkApp.getInstance()).setContentTitle(app_name)
//                            .setContentText(WorkApp.getInstance().getString(R.string.download_fail)).setContentIntent(pendingIntent)
//                            .getNotification();
                    if (uICallBackDao != null) {
                        uICallBackDao.download(false);
                    }
//                    if (updateDialog != null) {
//                        updateDialog.setBtnEnable(true);
//                        updateDialog.updateProgress(-1);
//                    }
                    EventBus.getDefault().post(new UpdateDownloadDialogEvent(-1));
                    break;
                default: {
                    if (PDialogUtil.isShowing()) {
                        PDialogUtil.stopProgress();
                    }
                    break;
                }
            }
        }
    };

    private volatile boolean isStop = false;

    public DownloadManager(Context mContext, boolean isTip, UICallBackDao uICallBackDao) {
        super();
        this.mContext = mContext;
        this.isTip = isTip;
        this.uICallBackDao = uICallBackDao;
        app_name = mContext.getString(R.string.app_name);
    }

    /**
     * 方法名: </br>
     * 详述: <检查软件是否/br> 开发人员：王太顺</br>
     * 创建时间：2015-6-27</br>
     */
    public void updateVersion() {
        RxHttpManager.getInstance().startHttp(RxHttpManager.getInstance().getRetrofit(HOST).create(IUpdateService.class)
                .getUpdateInfo()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io()), new HttpCallback<VersionModel>() {
            @SuppressLint("CheckResult")
            @Override
            public void onSuccess(VersionModel versionModel) {
                try {
                    if (versionModel != null) {
                        DownloadManager.this.versionModel = versionModel;
                        if (Double.parseDouble(versionModel.getVersionCode()) > MethodUtils.getVersionCode()) {
                            versionModel.setUpdate(true);
                        } else {
                            versionModel.setUpdate(false);
                        }
                        if (versionModel.isUpdate()) {
                            if (mContext instanceof FragmentActivity) {
                                FragmentActivity mActivity = (FragmentActivity) mContext;
                                RxPermissions rxPermission = new RxPermissions(mActivity);
                                rxPermission.requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                        Manifest.permission.READ_EXTERNAL_STORAGE)
                                        .subscribe(permission -> {
                                            if (permission.granted) {
                                                updateDialog = new UpdateVersionDialog(mContext, versionModel, new UpdateVersionDialog.UpdateVersion() {
                                                    @Override
                                                    public void update() {
                                                        WorkApp.getShare().put(SPKeys.isupdate, true);
                                                        //   createNotification();
                                                        downloadApk();
                                                    }

                                                    @Override
                                                    public void cancel() {
                                                        LogUtils.d("取消下载");
                                                        isStop = true;
                                                    }
                                                });
                                                updateDialog.show();
                                            } else {
                                                // 用户拒绝了该权限，而且选中『不再询问』
                                                LogUtils.e(permission.name + " is denied.");
                                                new ConfirmDialog(mActivity, MethodUtils.getString(R.string.please_sure_read_write_extra_permisssion), new DialogListener() {
                                                    @Override
                                                    public void cancel() {
                                                        MethodUtils.exitAPP();
                                                    }

                                                    @Override
                                                    public void confirm() {
                                                        MethodUtils.exitAPP();
                                                    }
                                                },false).show();
                                            }
                                        });
                            }
                        } else {
                            WorkApp.getShare().put(SPKeys.isupdate, false);
                            if (isTip) {
                                MethodUtils.showToast(mContext, WorkApp.getInstance().getString(R.string.is_latest_version));
                            }
                        }
                    } else {
                        WorkApp.getShare().put(SPKeys.isupdate, false);
                        if (isTip) {
                            MethodUtils.showToast(mContext, WorkApp.getInstance().getString(R.string.is_latest_version));
                        }
                    }
                    uICallBackDao.onSuccess(1);
                } catch (Exception e) {
                    uICallBackDao.onFailure();
                }
                uICallBackDao.onSuccess(1);
            }

            @Override
            public void onError(Throwable e) {
                uICallBackDao.onFailure();
            }

            @Override
            public void onHttpFail(int code, String data, String info) {
                uICallBackDao.onFailure();
            }

            @Override
            public void onFinish() {
                uICallBackDao.onFininsh();
            }
        });

    }

    private void createNotification() {
        String id = "12";
        notificationManager = (NotificationManager) mContext.getSystemService(NOTIFICATION_SERVICE);
        // notification = new Notification();
        // notification.icon = R.drawable.logo;
        // 这个参数是通知提示闪出来的值.
        CharSequence tickerText = WorkApp.getInstance().getString(R.string.start_download);

    /*
     在这里我们用自定的view来显示Notification
     */
        contentView = new RemoteViews(mContext.getPackageName(), R.layout.content_view);
        contentView.setTextViewText(R.id.notificationTitle, WorkApp.getInstance().getString(R.string.start_download));
        contentView.setTextViewText(R.id.notificationPercent, WorkApp.getInstance().getString(R.string.download_zero_progress));
        contentView.setProgressBar(R.id.notificationProgress, 100, 0, false);


        Intent updateIntent = new Intent();
        updateIntent.setClass(WorkApp.getInstance(), SupportActivity.class);
        updateIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        pendingIntent = PendingIntent.getActivity(mContext, 0, updateIntent, 0);


        Notification.Builder notificationBuilder = new Notification.Builder(WorkApp.getInstance())
                .setSmallIcon(R.mipmap.logo)
                .setLargeIcon(BitmapFactory.decodeResource(WorkApp.workApp.getResources(), R.mipmap.logo))
                .setTicker(tickerText);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //只在Android O之上需要渠道
            NotificationChannel notificationChannel = new NotificationChannel(id,
                    "Btd", NotificationManager.IMPORTANCE_HIGH);
            //如果这里用IMPORTANCE_NOENE就需要在系统的设置里面开启渠道，
            //通知才能正常弹出
            notificationManager.createNotificationChannel(notificationChannel);
            notificationBuilder.setChannelId(id);
        }
        notification = notificationBuilder.build();
        notification.contentView = contentView;
        notification.contentIntent = pendingIntent;


        notificationManager.notify(notification_id, notification);

    }

    /**
     * 下载apk文件
     */
    private void downloadApk() {
        // 启动新线程下载软件
        new downloadApkThread().start();
    }

    /**
     * 下载文件线程
     */
    private class downloadApkThread extends Thread {
        @Override
        public void run() {
            try {
                // 判断SD卡是否存在，并且是否具有读写权限
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    int downloadCount = 0;
                    //          int currentSize = 0;
                    long totalSize = 0;
                    int updateTotalSize;
                    // 获得存储卡的路径
                    String sdpath = Environment.getExternalStorageDirectory() + "/";
                    mSavePath = sdpath + "download";
                    URL url = new URL(versionModel.getUpdateUrl());
                    // 创建连接
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.connect();
                    // 获取文件大小
                    updateTotalSize = conn.getContentLength();
                    // 创建输入流
                    InputStream is = conn.getInputStream();

                    File file = new File(mSavePath);
                    // 判断文件目录是否存在
                    if (!file.exists()) {
                        file.mkdir();
                    }
                    File apkFile = new File(mSavePath, "Btd_" + versionModel.getVersionName() + ".apk");
                    if (apkFile.exists()) {
                        apkFile.delete();
                        apkFile.createNewFile();
                    }
                    FileOutputStream fos = new FileOutputStream(apkFile);
                    // 写入到文件中
                    byte buffer[] = new byte[4096];
                    int readSize;
                    int progress;
                    while ((readSize = is.read(buffer)) > 0 && !isStop) {
                        fos.write(buffer, 0, readSize);
                        totalSize += readSize;
                        // 为了防止频繁的通知导致应用吃紧，百分比增加10才通知一次
//                        if ((downloadCount == 0) || (int) (totalSize * 100 / updateTotalSize) - 10 > downloadCount) {
//                            downloadCount += 10;
                        if ((downloadCount == 0) || (int) (totalSize * 100 / updateTotalSize) - 2 > downloadCount) {
                            downloadCount =(int) (totalSize * 100 / updateTotalSize);
                            // 计算进度条位置
                            progress = (int) (((float) totalSize / updateTotalSize) * 100);
//                            contentView.setTextViewText(R.id.notificationPercent, progress + "%");
//                            contentView.setProgressBar(R.id.notificationProgress, 100, progress, false);
//                            notificationManager.notify(notification_id, notification);
                            EventBus.getDefault().post(new UpdateDownloadDialogEvent(progress));
                        }
                    }

                    // 下载完成
                    if (totalSize == updateTotalSize && !isStop) {
//                        progress = (int) (((float) totalSize / updateTotalSize) * 100);
//                        contentView.setTextViewText(R.id.notificationPercent, progress + "%");
//                        contentView.setProgressBar(R.id.notificationProgress, 100, progress, false);
//                        notificationManager.notify(notification_id, notification);
                        mHandler.sendEmptyMessage(DOWN_OK);
                    }
                    fos.close();
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                isStop = false;
            }
        }
    }

    /**
     * 安装APK文件
     */
    private void installApk() {
//        notificationManager.cancel(notification_id);   //安装的时候关闭通知
        File apkFile = new File(mSavePath, "Btd_" + versionModel.getVersionName() + ".apk");
        if (!apkFile.exists()) {
            return;
        }
        // 通过Intent安装APK文件
        Intent intents = new Intent();
        intents.setAction("android.intent.action.VIEW");
        if (Build.VERSION.SDK_INT >= 24) { //7.0以上版本单独处理
            intents.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            // 给目标应用一个临时授权
            intents.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri data = FileProvider.getUriForFile(mContext, MethodUtils.getProviderName(mContext), apkFile);
            intents.setDataAndType(data, "application/vnd.android.package-archive");
        } else {
            Uri apk = Uri.parse(Constants.Protcols.FILE + apkFile.toString());
            intents.addCategory("android.intent.category.DEFAULT");
            intents.setType("application/vnd.android.package-archive");
            intents.setData(apk);
            intents.setDataAndType(apk, "application/vnd.android.package-archive");
            intents.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        // android.os.Process.killProcess(android.os.Process.myPid());
        // 如果不加上这句的话在apk安装完成之后点击单开会崩溃
        mContext.startActivity(intents);
    }

}
