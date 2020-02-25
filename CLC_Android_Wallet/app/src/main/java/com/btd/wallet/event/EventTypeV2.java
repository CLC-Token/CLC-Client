package com.btd.wallet.event;

/**
 * 友盟私家云自定义事件监听
 * <preErcToBtd>
 * 创建: 杨紫员 2018/8/17 16:49;
 * 版本: $Rev: 5620 $ $Date: 2018-08-23 09:56:56 +0800 (周四, 23 八月 2018) $
 * </preErcToBtd>
 */
public interface EventTypeV2 {

    /** 交易记录 **/
    String TRANSFER_RECORD = "EVENT_1008";

    /** 屯米锁仓 **/
    String LOCK_PLAN = "EVENT_1009";

    /** 转账 **/
    String TRANSFER = "EVENT_1007";

    /** ERC20兑换 **/
    String ERC20_EXCHANGE = "EVENT_1006";

    /** 查看私钥 **/
    String LOOK_PRIVATE = "EVENT_1005";

    /** 重命名钱包 **/
    String RENAME_WALLET = "EVENT_1004";

    /** 删除钱包 **/
    String DELETE_WALLET = "EVENT_1003";

    /** 导入钱包 **/
    String IMPORT_WALLET = "EVENT_1002";

    /** 创建钱包 **/
    String CREATE_WALLET = "EVENT_1001";

}
