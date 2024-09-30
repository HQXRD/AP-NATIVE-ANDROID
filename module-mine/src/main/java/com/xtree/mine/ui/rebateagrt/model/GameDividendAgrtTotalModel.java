package com.xtree.mine.ui.rebateagrt.model;

import com.xtree.base.mvvm.recyclerview.BindModel;
import com.xtree.base.utils.ClickUtil;
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
    private String activity_people = "-";
    private String income = "-";
    private String ratio = "-";
    private String actual = "-";
    private String due = "-";
    private String self_money = "-";
    private GameDividendAgrtHeadModel.OnCallBack onCallBack;

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

    public void setOnCallBack(GameDividendAgrtHeadModel.OnCallBack onCallBack) {
        this.onCallBack = onCallBack;
    }

    public void myAgrt() {
        if (ClickUtil.isFastClick()) {
            return;
        }
        if (onCallBack != null) {
            onCallBack.myAgrt();
        }
    }

    public String getDue() {
        return due;
    }

    public void setDue(String due) {
        this.due = due;
    }

    public String getBet() {
        return bet;
    }

    public void setBet(String bet) {
        if (bet != null) {
            this.bet = bet;
        }
    }

    public String getActivity_people() {
        return activity_people;
    }

    public void setActivity_people(String activity_people) {
        this.activity_people = activity_people;
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public String getRatio() {
        return ratio;
    }

    public void setRatio(String ratio) {
        this.ratio = ratio;
    }

    public String getActual() {
        return actual;
    }

    public void setActual(String actual) {
        this.actual = actual;
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
}
