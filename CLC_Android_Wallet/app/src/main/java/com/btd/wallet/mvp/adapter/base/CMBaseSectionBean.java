package com.btd.wallet.mvp.adapter.base;

import com.chad.library.adapter.base.entity.SectionEntity;

/**
 * @创建者 廖林涛
 * @创时间 2017年3月10日 下午2:12:26
 * @描述 TODO
 *
 * @版本 $Rev: 13481 $
 * @更新者 $Author$
 * @更新时间 $Date: 2019-01-29 11:19:08 +0800 (周二, 29 一月 2019) $
 * @更新描述 TODO
 */
public abstract class CMBaseSectionBean<T> extends SectionEntity<T> {
  /** <p> 组id,根据此id聚合分类 */
  public int sectionId;
  public int sectionIndex = -1;    //在分组中的index

  /**
   * 
   * @param isHeader
   *          是否是头部
   * @param header
   *          头部标题
   * @param sectionId
   *          组id,根据此id聚合分类
   */
  public CMBaseSectionBean(boolean isHeader, String header, int sectionId) {
    super(isHeader, header);
    this.sectionId = sectionId;
  }

  /**
   * 
   * @param t
   *          不是头部,具体数据
   * @param sectionId
   */
  public CMBaseSectionBean(T t, int sectionId) {
    super(t);
    this.sectionId = sectionId;
  }

  /**
   * @param t  如果不关心section,默认sectionId为0
   */
  public CMBaseSectionBean(T t) {
    super(t);
  }
}
