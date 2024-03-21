package com.xtree.mine.ui.rebateagrt.model;

import androidx.databinding.ObservableField;

import com.xtree.base.mvvm.recyclerview.BindModel;

/**
 * Created by KAKA on 2024/3/19.
 * Describe:
 */
public class DividendAgrtCheckModel extends BindModel {
    //标题
    public ObservableField<String> numText = new ObservableField<>();
    //分红比例
    private String ratio;
    //累计投注额
    private String profit;
    //累计亏损额
    private String netProfit;
    //活跃人数
    private String people;

    public String getRatio() {
        return ratio + "%";
    }

    public void setRatio(String ratio) {
        this.ratio = ratio;
    }

    public String getProfit() {
        return profit;
    }

    public void setProfit(String profit) {
        this.profit = profit;
    }

    public String getNetProfit() {
        return netProfit;
    }

    public void setNetProfit(String netProfit) {
        this.netProfit = netProfit;
    }

    public String getPeople() {
        return people;
    }

    public void setPeople(String people) {
        this.people = people;
    }
}
