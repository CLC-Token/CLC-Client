package com.btd.wallet.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

import com.google.gson.Gson;

/**
 *
 * 类名: CMPersistentUtils</br>
 * 包名：com.cm.weixiangji.utils </br>
 * 描述: 持久化存储数据的工具类,基于SharedPreferences实现,实现对基本数据类型的存储,实现对model对象的存储</br>
 * 发布版本号：</br>
 * 开发人员： 廖林涛</br>
 * 创建时间： 2015年12月10日
 */
public class CMPersistentUtils {
  private static volatile CMPersistentUtils instance;
  private static String FILE_NAME;
  private static Context context;

  private CMPersistentUtils(Context contex) {
    context = contex.getApplicationContext();
  }

  public static CMPersistentUtils getInstance(Context contex) {
    if (instance == null) {
      synchronized (CMPersistentUtils.class) {
        if (instance == null) {
          instance = new CMPersistentUtils(contex);
        }
      }
    }

    if (TextUtils.isEmpty(FILE_NAME)) {
      FILE_NAME = context.getPackageName() + ".share";
    }
    return instance;
  }

  public void clearCache() {
    Editor editor = context.getSharedPreferences(FILE_NAME, 0).edit();
    editor.clear();
    editor.commit();
  }

  public <T> Boolean saveDao(String key, T t) {
    Gson gson = new Gson();
    String json = gson.toJson(t);
    if (TextUtils.isEmpty(json)) {
      return Boolean.valueOf(false);
    }
    SharedPreferences sp = context.getSharedPreferences(FILE_NAME, 0);
    return Boolean.valueOf(sp.edit().putString(key, json).commit());
  }

  public <T> T getDao(String key, Class<T> clazz) {
    Object t = null;
    Gson gson = new Gson();
    SharedPreferences sp = context.getSharedPreferences(FILE_NAME, 0);
    String json = sp.getString(key, null);
    if (!TextUtils.isEmpty(json)) {
      t = gson.fromJson(json, clazz);
    }

    return (T) t;
  }

  public boolean put(String key, String value) {
    SharedPreferences settings = context.getSharedPreferences(FILE_NAME, 0);
    Editor editor = settings.edit();
    editor.putString(key, value);
    return editor.commit();
  }

  public boolean put(String key, int value) {
    SharedPreferences settings = context.getSharedPreferences(FILE_NAME, 0);
    Editor editor = settings.edit();
    editor.putInt(key, value);
    return editor.commit();
  }

  public boolean put(String key, long value) {
    SharedPreferences settings = context.getSharedPreferences(FILE_NAME, 0);
    Editor editor = settings.edit();
    editor.putLong(key, value);
    return editor.commit();
  }

  public boolean put(String key, Boolean value) {
    SharedPreferences settings = context.getSharedPreferences(FILE_NAME, 0);
    Editor editor = settings.edit();
    editor.putBoolean(key, value.booleanValue());
    return editor.commit();
  }

  public String getString(String key) {
    SharedPreferences settings = context.getSharedPreferences(FILE_NAME, 0);
    return settings.getString(key, "");
  }

  public Boolean getBoolean(String key) {
    SharedPreferences settings = context.getSharedPreferences(FILE_NAME, 0);
    return Boolean.valueOf(settings.getBoolean(key, true));
  }

  public Boolean getBoolean(String key, boolean defaulted) {
    SharedPreferences settings = context.getSharedPreferences(FILE_NAME, 0);
    return Boolean.valueOf(settings.getBoolean(key, defaulted));
  }

  public int getInt(String key) {
    SharedPreferences settings = context.getSharedPreferences(FILE_NAME, 0);
    return settings.getInt(key, -1);
  }

  public int getInt(String key, int defaultd) {
    SharedPreferences settings = context.getSharedPreferences(FILE_NAME, 0);
    return settings.getInt(key, defaultd);
  }

  public long getLong(String key) {
    SharedPreferences settings = context.getSharedPreferences(FILE_NAME, 0);
    return settings.getLong(key, 0L);
  }

  public boolean remove(String key) {
    SharedPreferences settings = context.getSharedPreferences(FILE_NAME, 0);
    return settings.edit().remove(key).commit();
  }

  /**
   * 获得boolean类型的值，没有时默认值为false
   *
   * @param context
   * @param key
   * @return
   */
  public  boolean getBoolean(Context context, String key) {
    return getBoolean(context, key, false);
  }

  /**
   * 获得boolean类型的数据
   *
   * @param context
   * @param key
   * @param defValue
   * @return
   */
  public  boolean getBoolean(Context context, String key, boolean defValue) {
    SharedPreferences sp = context.getSharedPreferences(FILE_NAME, 0);
    return sp.getBoolean(key, defValue);
  }

  /**
   * 设置boolean类型的值
   *
   * @param context
   * @param key
   * @param value
   */
  public void putBoolean(Context context, String key, boolean value) {
    SharedPreferences sp = context.getSharedPreferences(FILE_NAME, 0);
    Editor editor = sp.edit();
    editor.putBoolean(key, value);
    editor.commit();
  }

}
