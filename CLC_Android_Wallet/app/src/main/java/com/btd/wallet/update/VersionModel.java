package com.btd.wallet.update;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * 
 * 类名: VersionModel</br> 包名：com.cm.weixiangji.model </br> 描述: </br> 发布版本号：</br>
 * 开发人员： 王太顺</br> 创建时间： 2015-9-2
 */
public class VersionModel implements Serializable {
  private static final long serialVersionUID = 68660760544046333L;
  /**
   * id : 155
   * version_type : android
   * app : 私家云
   * version_code : 321
   * update_url : http://cume-download.oss-cn-shenzhen.aliyuncs.com/Android-test/cumebox_321_2.1.5wyxj.apk
   * is_publication : true
   * is_force : true
   * version_name : 2.1.5
   * create_date : 1521641791000
   * modify_date : 1521700496000
   */

  private int id;
  private String versionType;
  private String app;
  private String versionCode;
  private String updateUrl;
  private boolean isPublication;
  private boolean isForce;// Boolean 是 是否强制升级
  private String versionName;
  @SerializedName("createAt")
  private long createDate;
  @SerializedName("updateAt")
  private long modifyDate;
  private boolean isUpdate; // Boolean 是 是否需要升级
  /**
   * descriptions : 测试强制升级
   */

  private String descriptions;

  public boolean isUpdate() {
    return isUpdate;
  }

  public void setUpdate(boolean isUpdate) {
    this.isUpdate = isUpdate;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getApp() {
    return app;
  }

  public void setApp(String app) {
    this.app = app;
  }

  public void setVersionType(String versionType) {
    this.versionType = versionType;
  }

  public void setVersionCode(String versioncode) {
    this.versionCode = versioncode;
  }

  public void setUpdateUrl(String updateUrl) {
    this.updateUrl = updateUrl;
  }

  public void setPublication(boolean publication) {
    isPublication = publication;
  }

  public void setForce(boolean force) {
    isForce = force;
  }

  public void setVersionName(String versionName) {
    this.versionName = versionName;
  }

  public void setCreateDate(long createDate) {
    this.createDate = createDate;
  }

  public void setModifyDate(long modifyDate) {
    this.modifyDate = modifyDate;
  }

  public String getVersionType() {
    return versionType;
  }

  public String getVersionCode() {
    return versionCode;
  }

  public String getUpdateUrl() {
    return updateUrl;
  }

  public boolean isPublication() {
    return isPublication;
  }

  public boolean isForce() {
    return isForce;
  }

  public String getVersionName() {
    return versionName;
  }

  public long getCreateDate() {
    return createDate;
  }

  public long getModifyDate() {
    return modifyDate;
  }

  public String getDescriptions() {
    return descriptions;
  }

  public void setDescriptions(String descriptions) {
    this.descriptions = descriptions;
  }
}
