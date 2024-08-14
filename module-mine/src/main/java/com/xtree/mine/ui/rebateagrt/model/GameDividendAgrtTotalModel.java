package com.xtree.mine.ui.rebateagrt.model;

import com.xtree.base.mvvm.recyclerview.BindModel;
import com.xtree.mine.R;

import me.xtree.mvvmhabit.base.BaseApplication;

/**
 * Created by KAKA on 2024/3/14.
 * Describe:
 */
public class GameDividendAgrtTotalModel extends BindModel {

    private String bet = "-";
    private String netloss = "-";
    private String people = "-";
    private String cycle_percent = "-";
    private String cycle = "-";
    private String subMoney = "-";
    private String selfMoney = "-";
    //契约状态
    private String payStatu = "-1";
    //契约状态文本
    private String payStatuText = BaseApplication.getInstance().getString(R.string.txt_noagrement);
    private int payStatuColor = R.color.color_rebateagrt_state_bg_nodividend;
    private String userid = "";
    private String profitloss = "-";
    //连续亏损周期
    private String loseStreak = "-";

    public String getPayStatu() {
        return payStatu;
    }

    public void setPayStatu(String payStatu) {
        this.payStatu = payStatu;
        switch (payStatu) {
            case "1": //未结清:
                payStatuColor = R.color.color_rebateagrt_state_bg_unsettled;
                break;
            case "2": //已结清
                payStatuColor = R.color.color_rebateagrt_state_bg_settled;
                break;
            default:
                payStatuColor = R.color.color_rebateagrt_state_bg_nodividend;
                break;
        }
    }

    public int getPayStatuColor() {
        return BaseApplication.getInstance().getResources().getColor(payStatuColor);
    }

    public String getBet() {
        return bet;
    }

    public void setBet(String bet) {
        if (bet != null) {
            this.bet = bet;
        }
    }

    public String getNetloss() {
        return netloss;
    }

    public void setNetloss(String netloss) {
        if (netloss != null) {
            this.netloss = netloss;
        }
    }

    public String getPeople() {
        return people;
    }

    public void setPeople(String people) {
        if (people != null) {
            this.people = people;
        }
    }

    public String getCycle_percent() {
        return cycle_percent;
    }

    public void setCycle_percent(String cycle_percent) {
        if (cycle_percent != null) {
            this.cycle_percent = cycle_percent;
        }
    }

    public String getCycle() {
        return cycle;
    }

    public void setCycle(String cycle) {
        if (cycle != null) {
            this.cycle = cycle;
        }
    }

    public String getSubMoney() {
        return subMoney;
    }

    public void setSubMoney(String subMoney) {
        if (subMoney != null) {
            this.subMoney = subMoney;
        }
    }

    public String getSelfMoney() {
        return selfMoney;
    }

    public void setSelfMoney(String selfMoney) {
        if (selfMoney != null) {
            this.selfMoney = selfMoney;
        }
    }

    public String getPayStatuText() {
        return payStatuText;
    }

    public void setPayStatuText(String payStatuText) {
        if (payStatuText != null) {
            this.payStatuText = payStatuText;
        }
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        if (userid != null) {
            this.userid = userid;
        }
    }

    public String getProfitloss() {
        return profitloss;
    }

    public void setProfitloss(String profitloss) {
        if (profitloss != null) {
            this.profitloss = profitloss;
        }
    }

    public String getLoseStreak() {
        return loseStreak;
    }

    public void setLoseStreak(String loseStreak) {
        this.loseStreak = loseStreak;
    }
}
