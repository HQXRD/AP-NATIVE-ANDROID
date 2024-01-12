package com.xtree.bet.bean.request.pm;

public class BtRecordReq {

    /**
     * 注单类型 0：未结算，1：已结算 未结算按照投注时间查询，包括在确认中投注成功 已结算按照结算时间查询
     */
    private int orderStatus;
    /**
     * 币种id，免转钱包必传
     */
    private int size = 300;
    /**
     * 当前页
     */
    private int page = 1;
    /**
     * 时间类型 1：今天，2：昨日，3：七日内，4：一月内
     */
    private int timeType = 4;

    public int isOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getTimeType() {
        return timeType;
    }

    public void setTimeType(int timeType) {
        this.timeType = timeType;
    }
}
