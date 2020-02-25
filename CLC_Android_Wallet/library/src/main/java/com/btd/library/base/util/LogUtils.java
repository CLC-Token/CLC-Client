package com.btd.library.base.util;

import android.annotation.SuppressLint;
import android.os.Environment;
import android.util.Log;

import com.btd.library.BuildConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * 类名: LogUtils</br>
 * 包名：com.cm.weixiangji.utils </br>
 * 描述: log工具类,对原生log的包装,从Peace_SDK中提取出来,修改包名为com.cm.weixiangji</br>
 * 发布版本号：</br>
 * 开发人员： 廖林涛</br>
 * 创建时间： 2015年12月10日
 */
public class LogUtils {
  /** 控制日志打印 */
  public static Boolean DEBUG = BuildConfig.DEBUG;
//  public static Boolean DEBUG = true;
  /** 控制日志输出到文件 */
  public static Boolean isWrite = false;

  private static int LOG_MAXLENGTH = 2000;

  private static File file = null;

  private static File errorFile = null;


  private static SimpleDateFormat logSDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

  private static SimpleDateFormat logFileDate = new SimpleDateFormat("yyyy-MM-dd");

  private static final String TAG = "Miners";
  /** 控制日志打印 */
  public static SimpleDateFormat sdf = new SimpleDateFormat("[yyyy年MM月dd日 HH:mm:ss.SSS]");

  public static void d(String TAG, String msg) {
    if (DEBUG) {
      msg = sdf.format(new Date()) + "---" + Thread.currentThread().getName() + "---" + msg;
      Log.d(TAG, msg);
    }

    setLog("DEBUG", TAG, msg);
  }

  public static void i(String TAG, String msg) {
    if (DEBUG) {
      if(msg == null)
      {
        Log.i(TAG, "");
        return ;
      }
      msg = sdf.format(new Date()) + "---" + Thread.currentThread().getName() + "---" + msg;
      int strLength = msg.length();
      int start = 0;
      int end = LOG_MAXLENGTH;
      for (int i = 0; i < 100; i++) {
        if (strLength > end) {
          Log.i(TAG + i, msg.substring(start, end));
          start = end;
          end = end + LOG_MAXLENGTH;
        } else {
          Log.i(TAG + i, msg.substring(start, strLength));
          break;
        }
      }
    }

    setLog("INFO", TAG, msg);
  }

  public static void w(String TAG, String msg) {
    if (DEBUG) {
      msg = sdf.format(new Date()) + "---" + Thread.currentThread().getName() + "---" + msg;
      Log.w(TAG, msg);
    }

    setLog("WARN", TAG, msg);
  }

  public static void e(String TAG, String msg) {
    if (DEBUG) {
      msg = sdf.format(new Date()) + "---" + Thread.currentThread().getName() + "---" + msg;
      Log.e(TAG, msg);
    }

    setLog("ERROR", TAG, msg);

    setErrorLog(TAG, msg);
  }

  public static void e(String TAG, Throwable ex) {
    String exString = getStackElement(ex);

    if (DEBUG) {
      exString = sdf.format(new Date()) + "---" + Thread.currentThread().getName() + "---" + exString;
      Log.e(TAG, exString);
    }

    setLog("ERROR", TAG, exString);

    setErrorLog(TAG, exString);
  }

  public static void i(String content) {
    StackTraceElement caller = CMOtherUtils.getCallerStackTraceElement();
    String TAG = generateTag(caller);
    content = sdf.format(new Date()) + "---" + Thread.currentThread().getName() + "---" + content;

    i(TAG, content);
  }

  public static void w(String content) {
    StackTraceElement caller = CMOtherUtils.getCallerStackTraceElement();
    String TAG = generateTag(caller);
    content = sdf.format(new Date()) + "---" + Thread.currentThread().getName() + "---" + content;

    w(TAG, content);
  }

  public static void d(String content) {
    StackTraceElement caller = CMOtherUtils.getCallerStackTraceElement();
    String TAG = generateTag(caller);
    content = sdf.format(new Date()) + "---" + Thread.currentThread().getName() + "---" + content;

    d(TAG, content);
  }

  public static void e(String content) {
    StackTraceElement caller = CMOtherUtils.getCallerStackTraceElement();
    String TAG = generateTag(caller);
    content = sdf.format(new Date()) + "---" + Thread.currentThread().getName() + "---" + content;

    e(TAG, content);
  }

  @SuppressLint("DefaultLocale")
  private static String generateTag(StackTraceElement caller) {
    String TAG = "%s.%s(L:%d)";
    String callerClazzName = caller.getClassName();
    callerClazzName = callerClazzName.substring(callerClazzName.lastIndexOf(".") + 1);
    TAG = String.format(TAG,
            callerClazzName, caller.getMethodName(), caller.getLineNumber());

    return TAG;
  }

  private static void setLog(String level, String TAG, String msg) {
    if (isWrite)
      writeLog(level, TAG, msg);
  }

  private static void setErrorLog(String TAG, String exString) {
    if (isWrite)
      writeErrorLog(TAG, exString);
  }

  private static File creatSDFile(String path, String fileName) throws Exception {
    File file = new File(path + fileName);

    if (!file.exists()) {
      if (file.getParentFile().mkdirs()) {
        file.createNewFile();
      }
    }

    return file;
  }

  private static void writeLog(String level, String TAG, String msg) {
    try {
      Calendar nowTime = Calendar.getInstance();

      String SDPATH = Environment.getExternalStorageDirectory() + "/LogManager/" + "testRxJavaRetrofit" + "/";

      file = creatSDFile(SDPATH, logFileDate.format(nowTime.getTime()) + "log.txt");

      FileOutputStream fout = new FileOutputStream(file, true);

      String WriteMessage = "\r\n";

      WriteMessage = logSDF.format(nowTime.getTime());

      WriteMessage = WriteMessage + "  " + level;

      WriteMessage = WriteMessage + "  " + TAG;

      WriteMessage = WriteMessage + "  " + msg + "\r\n";

      byte[] bytes = WriteMessage.getBytes();

      fout.write(bytes);

      fout.flush();

      fout.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void delFile(Calendar beforeCalendar) {
    String needDelFieDate = logFileDate.format(beforeCalendar);

    File file = null;
    try {
      String SDPATH = Environment.getExternalStorageDirectory() + "/LogManager/" + "testRxJavaRetrofit" + "/";

      file = creatSDFile(SDPATH, needDelFieDate + "log.txt");

      if (file.exists()) {
        file.delete();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static void writeErrorLog(String TAG, String msg) {
    try {
      Calendar nowTime = Calendar.getInstance();

      String SDPATH = Environment.getExternalStorageDirectory() + "/LogManager/" +  "testRxJavaRetrofit" + "/Error/";

      errorFile = creatSDFile(SDPATH, logFileDate.format(nowTime.getTime()) + "error_log.txt");

      FileOutputStream fout = new FileOutputStream(errorFile, true);

      String WriteMessage = "\r\n";

      WriteMessage = logSDF.format(Calendar.getInstance().getTime());

      WriteMessage = WriteMessage + "  " + TAG;

      WriteMessage = WriteMessage + "  " + msg + "\r\n";

      byte[] bytes = WriteMessage.getBytes();

      fout.write(bytes);

      fout.flush();

      fout.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static String getStackElement(Throwable ex) {
    StackTraceElement[] ste = ex.getStackTrace();

    StringBuffer sb = new StringBuffer();

    sb.append(" message = " + ex.getMessage());

    sb.append("\r\n");

    for (int i = 0; i < ste.length; i++) {
      sb.append(ste[i]);

      sb.append("\r\n");
    }

    return sb.toString();
  }
}
