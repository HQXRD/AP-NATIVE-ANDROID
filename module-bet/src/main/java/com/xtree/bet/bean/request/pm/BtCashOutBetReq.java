package com.xtree.bet.bean.request.pm;

public class BtCashOutBetReq {
    /**
     * 订单号
     */
    private String orderNo;
    /**
     * 结算金额
     */
    private String settleAmount;
    /**
     * 结算设备类型 1：H5(默认)，2：PC，3：Android，4：IOS
     */
    private double deviceType = 3;
    /**
     * 前端展示的预计获得金额
     */
    private double frontSettleAmount;

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public void setSettleAmount(String settleAmount) {
        this.settleAmount = settleAmount;
    }

    public void setDeviceType(double deviceType) {
        this.deviceType = deviceType;
    }

    public void setFrontSettleAmount(double frontSettleAmount) {
        this.frontSettleAmount = frontSettleAmount;
    }
}
