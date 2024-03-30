package com.xtree.mine.ui.rebateagrt.model;

import com.xtree.base.mvvm.recyclerview.BindModel;

/**
 * Created by KAKA on 2024/3/15.
 * Describe:
 */
public class RecommendedReportsModel extends BindModel {
    private String userName = "-";
    private String bet = "-";
    private String profitloss = "-";
    private String people = "-";
    private String label = "-";
    private String cycle = "-";
    private String subMoney;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        if (userName != null && !userName.isEmpty()) {
            this.userName = userName;
        }
    }

    public String getBet() {
        return bet;
    }

    public void setBet(String bet) {
        if (bet != null && !bet.isEmpty()) {
            this.bet = bet;
        }
    }

    public String getProfitloss() {
        return profitloss;
    }

    public void setProfitloss(String profitloss) {
        if (profitloss != null && !profitloss.isEmpty()) {
            this.profitloss = profitloss;
        }
    }

    public String getPeople() {
        return people;
    }

    public void setPeople(String people) {
        if (people != null && !people.isEmpty()) {
            this.people = people;
        }
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        if (label != null && !label.isEmpty()) {
            this.label = label;
        }
    }

    public String getCycle() {
        return cycle;
    }

    public void setCycle(String cycle) {
        if (cycle != null && !cycle.isEmpty()) {
            this.cycle = cycle;
        }
    }

    public String getSubMoney() {
        return subMoney;
    }

    public void setSubMoney(String subMoney) {
        if (subMoney != null && !subMoney.isEmpty()) {
            this.subMoney = subMoney;
        }
    }
}
