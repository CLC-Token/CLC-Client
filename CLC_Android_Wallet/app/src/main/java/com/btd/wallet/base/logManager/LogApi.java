package com.btd.wallet.base.logManager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;

import com.blankj.utilcode.util.DeviceUtils;
import com.blankj.utilcode.util.StringUtils;
import com.btd.library.base.http.callback.HttpCallback;
import com.btd.library.base.util.LogUtils;
import com.btd.wallet.core.WorkApp;
import com.btd.wallet.utils.CMFileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 日志上传API
 * 在收集到日志后,然后直接上传至服务器同时保存日志信息至文件中.
 * 上传成功:删除日志文件
 * 上传失败:当是及时上传时,保存日志数据到文件中;当为读取以前的日志文件时,不做任何操作
 *
 * 保存文件的名称:包名-格式化后的时间-时间搓-tag-className-level(在提交离线文件时分割文件来进行创建任务)
 * Created by yzy on 2018/2/25 14:23
 */
public class LogApi extends HttpCallback {

    private String filePath;
    LogRequest request;
    private static Map<String, String> infos;  //用户设备信息(只需要获取一次)
    LogServiceImpl service = new LogServiceImpl();
    /**
     * 上传日志(第一次获取到日志信息上传日志)
     * @param tag 查询日志的tag
     * @param className 产生日志的类
     * @param msg 消息内容
     * @param level 等级
     */
    public void uploadLog(String tag , String className, String msg, int level){
        request = new LogRequest();
        request.setTag(tag);
        request.setClassName(className);
        request.setLevel(level);
        request.setTime(System.currentTimeMillis());
        collectDeviceInfo(WorkApp.getInstance());
        for(Map.Entry<String, String> map : infos.entrySet()){
            msg += '\n' + map.getKey() + ":" + map.getValue();
        }
        request.setMsg(msg);
        upload(msg);
    }

    /**
     * 在上传失败后保存的文件直接读取对应文件的信息上传到服务器
     * @param filePath
     */
    public void uploadLog(String filePath){
        request = new LogRequest();
        this.filePath = filePath;
        request.setMsg(getFileData(filePath));
        upload(request.getMsg());
    }

    /**
     * 上传日志文件
     * @param logData
     */
    private void upload(String logData){
        if(StringUtils.isEmpty(logData)){
            deleteCompleteFile(filePath);
            return ;
        }
        try {
            PackageManager pm = WorkApp.getInstance().getPackageManager();
            PackageInfo packageInfo = pm.getPackageInfo(WorkApp.getInstance().getPackageName() , PackageManager.GET_ACTIVITIES);
            String appVersion = null;
            if(packageInfo!=null){
                appVersion = packageInfo.versionName;
            }
            request.setAppVersion(appVersion);
            request.setSdkVersion(Build.VERSION.RELEASE);
            request.setPhoneType(Build.BRAND + " " + DeviceUtils.getModel());
            service.uploadLog(request , this);
            LogUtils.i(request.toString());

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除上传完成的文件
     * @param filePath
     */
    private void deleteCompleteFile(String filePath){
        if(StringUtils.isEmpty(filePath)){
            return ;
        }
        File file = new File(filePath);
        if(file.exists()){
            file.delete();
        }
    }

    /**
     * 读取文件中的文本
     * @param filePath
     */
    private String getFileData(String filePath){
        try {
            StringBuilder stringBuffer = new StringBuilder();
            if (StringUtils.isEmpty(filePath)) {
                return null;
            }
            File file = new File(filePath);
            if (!file.exists() || !file.isFile()) {
                return null;
            }
            String fileName = file.getName();
            String[] fileDatas = fileName.split("-");
            request.setTime(Long.parseLong(fileDatas[2]));
            request.setTag(fileDatas[3]);
            request.setClassName(fileDatas[4]);
            request.setLevel(Integer.parseInt(fileDatas[5].substring(0 , fileDatas[5].indexOf("."))));
            InputStream inputStream = new FileInputStream(file);
            byte[] buffer = new byte[1024];
            int count;
            while ((count = inputStream.read(buffer))!=-1){
                stringBuffer.append(new String(buffer , 0 , count));
            }
            return stringBuffer.toString();
        }catch (Exception ex){
            LogUtils.i("read log file fial..." + ex!=null?ex.toString():"");
        }
        return null;
    }

    //用于格式化日期,作为日志文件名的一部分
    @SuppressLint("SimpleDateFormat")
    private DateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");

    /**
     * 保存日志信息文件
     * @param logData
     */
    private String saveLogFile(String logData){
        try {
            if(StringUtils.isEmpty(logData)){
                return null;
            }
            long timestamp = System.currentTimeMillis();
            String time = formatter.format(new Date());
            String fileName = WorkApp.getInstance().getPackageName()
                    + "-" + time
                    + "-" + timestamp
                    + "-" + request.getTag()
                    + "-" + request.getClassName()
                    + "-" + request.getLevel()
                    + ".log";
            LogUtils.i("fileName-->" + fileName);
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                String path = CMFileUtils.LOG_PATH;
                File file = new File(path , fileName);
                if (!file.exists()) {
                    file.getParentFile().mkdirs();
                }
                FileOutputStream out = new FileOutputStream(file);
                out.write(logData.getBytes());
                out.close();
                return file.getAbsolutePath();
            }
            return fileName;
        }catch (Exception e){
            LogUtils.i("an error occured while writing file...");
        }
        return null;
    }

    /**
     * 获取手机设备信息
     * @param context
     */
    private void collectDeviceInfo(Context context){
        if(infos == null || infos.size() == 0){
            infos = new HashMap<>();
            try {
                //获取软件版本信息
                PackageManager pm = context.getPackageManager();
                PackageInfo pi = pm.getPackageInfo(context.getPackageName() , PackageManager.GET_ACTIVITIES);
                if(pi != null){
                    String versionName = pi.versionName == null? "null" : pi.versionName;
                    String versionCode = pi.versionCode + "";
                    infos.put("versionName" , versionName);
                    infos.put("versionCode" , versionCode);
                }
                //获取手机的信息,通过反射机制
                Field[] fields = Build.class.getDeclaredFields();
                for(Field field : fields){
                    field.setAccessible(true); //设置字段可访问
                    infos.put(field.getName() , field.get(null).toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onSuccess(Object o) {
        //上传完成删除文件
        deleteCompleteFile(filePath);
    }

    @Override
    public void onError(Throwable e) {
        if(StringUtils.isEmpty(this.filePath)){
            saveLogFile(this.request.getMsg());
        }
    }

    @Override
    public void onHttpFail(int code, String info, String data) {
        if(StringUtils.isEmpty(this.filePath)){
            saveLogFile(request.getMsg());
        }
    }

    @Override
    public void onFinish() {

    }
}
