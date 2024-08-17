package com.xtree.mine.ui.rebateagrt.model;

import com.xtree.base.mvvm.recyclerview.BindModel;

/**
 * Created by KAKA on 2024/4/1.
 * Describe:
 */
public class LotteryDividendReportsTotalModel extends BindModel {

    private String profitloss;
    private String self_money;
    private String netloss;

    public String getProfitloss() {
        return profitloss;
    }

    public void setProfitloss(String profitloss) {
        this.profitloss = profitloss;
    }

    public String getSelf_money() {
        return self_money;
    }

    public void setSelf_money(String self_money) {
        this.self_money = self_money;
    }

    public String getNetloss() {
        return netloss;
    }

    public void setNetloss(String netloss) {
        this.netloss = netloss;
    }
}
