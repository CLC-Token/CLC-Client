package com.btd.library.base.config;

/**
 * libary配置
 * Created by yzy on 2018/10/18 16:15
 */

public interface HttpConfig {
    interface ServerResponse{
        int SUCCESS_CODE = 1;
        int NOT_AUTH = 401; //未登录接口
    }
}
