package com.xtree.bet.bean.request.fb;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * 投注时串关数据
 */
public class BtCgReq {
    private int oddsChange;
    private double unitStake;
    private int seriesValue;
    private List<BtOptionReq> betOptionList = new ArrayList<>();
    private String thirdRemark = new Gson().toJson(new ThirdRemark());
    public int getOddsChange() {
        return oddsChange;
    }

    public void setOddsChange(int oddsChange) {
        this.oddsChange = oddsChange;
    }

    public double getUnitStake() {
        return unitStake;
    }

    public void setUnitStake(double unitStake) {
        this.unitStake = unitStake;
    }

    public int getSeriesValue() {
        return seriesValue;
    }

    public void setSeriesValue(int seriesValue) {
        this.seriesValue = seriesValue;
    }

    public List<BtOptionReq> getBetOptionList() {
        return betOptionList;
    }

    public void addBetOptionList(BtOptionReq betOption) {
        this.betOptionList.add(betOption);
    }
}
