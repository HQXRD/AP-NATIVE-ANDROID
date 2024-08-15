package com.xtree.mine.ui.rebateagrt.model;

import androidx.databinding.ObservableField;

import com.xtree.base.mvvm.recyclerview.BindModel;
import com.xtree.mine.R;

import me.xtree.mvvmhabit.base.BaseApplication;

/**
 * Created by KAKA on 2024/3/19.
 * Describe:
 */
public class DividendAgrtSendModel extends BindModel {
    public ObservableField<Boolean> checked = new ObservableField<>(false);
    private String userName = "-";
    private String bet = "-";
    private String netloss = "-";
    private String people = "-";
    private String cycle_percent = "-";
    private String cycle = "-";
    private String subMoney = "-";
    private String selfMoney = "-";
    private String profitloss = "-";
    //契约状态
    private String payStatu = "-1";
    //契约状态文本
    private String payStatuText = BaseApplication.getInstance().getString(R.string.txt_noagrement);
    private int payStatuColor = R.color.color_rebateagrt_state_bg_nodividend;
    private String userid = "";
    private String loseStreak = "-";

    public String getLoseStreak() {
        return loseStreak;
    }

    public void setLoseStreak(String loseStreak) {
        this.loseStreak = loseStreak;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getBet() {
        return bet;
    }

    public void setBet(String bet) {
        this.bet = bet;
    }

    public String getNetloss() {
        return netloss;
    }

    public void setNetloss(String netloss) {
        this.netloss = netloss;
    }

    public String getPeople() {
        return people;
    }

    public void setPeople(String people) {
        this.people = people;
    }

    public String getCycle_percent() {
        return cycle_percent;
    }

    public void setCycle_percent(String cycle_percent) {
        this.cycle_percent = cycle_percent;
    }

    public String getCycle() {
        return cycle;
    }

    public void setCycle(String cycle) {
        this.cycle = cycle;
    }

    public String getSubMoney() {
        return subMoney;
    }

    public void setSubMoney(String subMoney) {
        this.subMoney = subMoney;
    }

    public String getSelfMoney() {
        return selfMoney;
    }

    public void setSelfMoney(String selfMoney) {
        this.selfMoney = selfMoney;
    }

    public String getProfitloss() {
        return profitloss;
    }

    public void setProfitloss(String profitloss) {
        this.profitloss = profitloss;
    }

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

    public String getPayStatuText() {
        return payStatuText;
    }

    public void setPayStatuText(String payStatuText) {
        this.payStatuText = payStatuText;
    }

    public int getPayStatuColor() {
        return BaseApplication.getInstance().getResources().getColor(payStatuColor);
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public ObservableField<Boolean> getChecked() {
        return checked;
    }

    public void setChecked(ObservableField<Boolean> checked) {
        this.checked = checked;
    }
}
