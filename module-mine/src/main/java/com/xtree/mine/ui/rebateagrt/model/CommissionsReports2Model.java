package com.xtree.mine.ui.rebateagrt.model;

import com.xtree.base.mvvm.recyclerview.BindModel;

/**
 * Created by KAKA on 2024/4/1.
 * Describe: 佣金报表查看数据model
 */
public class CommissionsReports2Model extends BindModel {

    private String username;
    private String activity_people;
    private String pay_fee;
    private String fee;
    private String income;
    private String due;
    private String c_agency_model;
    private String profit;
    private String wages;
    private String remain;
    private String plan_ratio;
    private String sys_adjust;

    public String getDue() {
        return due;
    }

    public void setDue(String due) {
        this.due = due;
    }

    public String getPlan_ratio() {
        return plan_ratio;
    }

    public void setPlan_ratio(String plan_ratio) {
        this.plan_ratio = plan_ratio;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getActivity_people() {
        return activity_people;
    }

    public void setActivity_people(String activity_people) {
        this.activity_people = activity_people;
    }

    public String getPay_fee() {
        return pay_fee;
    }

    public void setPay_fee(String pay_fee) {
        this.pay_fee = pay_fee;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public String getC_agency_model() {
        return c_agency_model;
    }

    public void setC_agency_model(String c_agency_model) {
        this.c_agency_model = c_agency_model;
    }

    public String getProfit() {
        return profit;
    }

    public void setProfit(String profit) {
        this.profit = profit;
    }

    public String getWages() {
        return wages;
    }

    public void setWages(String wages) {
        this.wages = wages;
    }

    public String getRemain() {
        return remain;
    }

    public void setRemain(String remain) {
        this.remain = remain;
    }

    public String getSys_adjust() {
        return sys_adjust;
    }

    public void setSys_adjust(String sys_adjust) {
        this.sys_adjust = sys_adjust;
    }
}
