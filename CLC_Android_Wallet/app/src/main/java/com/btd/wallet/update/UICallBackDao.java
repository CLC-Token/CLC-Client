package com.btd.wallet.update;

/**
 * 下载apk任务的回调
 * <preErcToBtd>
 * 创建: 廖林涛 2017/5/26 15:10;
 * 版本: $Rev: 4935 $ $Date: 2018-07-30 18:49:43 +0800 (周一, 30 七月 2018) $
 * </preErcToBtd>
 */
public interface UICallBackDao {
    void onSuccess(long responseCode);

    void onFailure();

    void download(boolean success);

    void onFininsh();

}
