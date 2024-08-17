package com.xtree.mine.ui.rebateagrt.model;

import com.xtree.base.mvvm.recyclerview.BindModel;

/**
 * Created by KAKA on 2024/4/1.
 * Describe: 彩票分红报表数据
 */
public class LotteryDividendReportsModel extends BindModel {

    private String username;
    private String day_people;
    private String profitloss;
    private String ratio;
    private String sub_money;
    private String people;
    private String week_people;
    private String netloss;
    private String self_money;
    private String title;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDay_people() {
        return day_people;
    }

    public void setDay_people(String day_people) {
        this.day_people = day_people;
    }

    public String getProfitloss() {
        return profitloss;
    }

    public void setProfitloss(String profitloss) {
        this.profitloss = profitloss;
    }

    public String getRatio() {
        return ratio;
    }

    public void setRatio(String ratio) {
        this.ratio = ratio;
    }

    public String getSub_money() {
        return sub_money;
    }

    public void setSub_money(String sub_money) {
        this.sub_money = sub_money;
    }

    public String getPeople() {
        return people;
    }

    public void setPeople(String people) {
        this.people = people;
    }

    public String getWeek_people() {
        return week_people;
    }

    public void setWeek_people(String week_people) {
        this.week_people = week_people;
    }

    public String getNetloss() {
        return netloss;
    }

    public void setNetloss(String netloss) {
        this.netloss = netloss;
    }

    public String getSelf_money() {
        return self_money;
    }

    public void setSelf_money(String self_money) {
        this.self_money = self_money;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
