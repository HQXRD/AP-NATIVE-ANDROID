package com.xtree.bet.bean.request.pm;

import java.util.List;

public class SeriesOrder {
    /**
     * 串关类型 1：单关，2001：2串1，3001：3串1，3004：3串4，4001：4串1，
     * 40011：4串11，5001：5串1，50026：5串26，6001：6串1， 60057：6串57，7001：7串1，700120：
     * 7串120，8001：8串1， 800247：8串247，9001：9串1，900502：502，10001：10串1， 10001013：10串1013。预约投注此字段只能为1
     */
    private String seriesType;
    /**
     * 串关数量，前端计算获得。预约投注此字段只能为1
     */
    private int seriesSum;
    /**
     * 是否满额投注 1：是，0：否
     */
    private int fullBet;
    /**
     * 注单集合对象
     */
    private List<OrderDetail> orderDetailList;

    public String getSeriesType() {
        return seriesType;
    }

    public void setSeriesType(String seriesType) {
        this.seriesType = seriesType;
    }

    public int getSeriesSum() {
        return seriesSum;
    }

    public void setSeriesSum(int seriesSum) {
        this.seriesSum = seriesSum;
    }

    public int getFullBet() {
        return fullBet;
    }

    public void setFullBet(int fullBet) {
        this.fullBet = fullBet;
    }

    public List<OrderDetail> getOrderDetailList() {
        return orderDetailList;
    }

    public void setOrderDetailList(List<OrderDetail> orderDetailList) {
        this.orderDetailList = orderDetailList;
    }
}
