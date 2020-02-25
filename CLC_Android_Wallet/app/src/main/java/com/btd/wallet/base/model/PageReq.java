package com.btd.wallet.base.model;

/**
 * 分页基础请求
 * Created by 杨紫员 on 2018/1/29.
 */

public class PageReq {
    private int pageNo = 1;
    private int pageSize = 20;

    public PageReq(int pageNo, int pageSize) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }

    public PageReq() {
    }

    public PageReq(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
