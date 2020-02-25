package com.btd.wallet.event.select;

/**
 * <preErcToBtd>
 * 创建: 廖林涛 2017/6/8 16:04;
 * 版本: $Rev: 16603 $ $Date: 2019-03-27 13:35:06 +0800 (周三, 27 3月 2019) $
 * </preErcToBtd>
 */
public class EditToolbarVisibleEvent {
    public boolean isVisible;

    public EditToolbarVisibleEvent(boolean isVisible) {
        this.isVisible = isVisible;
    }
}
