package com.xtree.bet.bean.request.fb;

public class BtCashOutBetReq {
    private String languageType = "CMN";
    private String orderId;
    private double cashOutStake;
    private double unitCashOutPayoutStake;
    private boolean acceptOddsChange;
    private boolean parlay;

    public void setLanguageType(String languageType) {
        this.languageType = languageType;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setCashOutStake(double cashOutStake) {
        this.cashOutStake = cashOutStake;
    }

    public void setUnitCashOutPayoutStake(double unitCashOutPayoutStake) {
        this.unitCashOutPayoutStake = unitCashOutPayoutStake;
    }

    public void setAcceptOddsChange(boolean acceptOddsChange) {
        this.acceptOddsChange = acceptOddsChange;
    }

    public void setParlay(boolean parlay) {
        this.parlay = parlay;
    }
}
