package com.xtree.mine.ui.rebateagrt.model;

import com.xtree.base.mvvm.recyclerview.BindHead;
import com.xtree.base.mvvm.recyclerview.BindModel;

/**
 * Created by KAKA on 2024/3/19.
 * Describe:
 */
public class DividendAgrtSendHeadModel extends BindModel implements BindHead {

    //分页索引
    public int p = 1;
    //page count
    public int pn = 20;
    public String type = "1";
    private String payoff;
    private String owe;

    public String getPayoff() {
        return payoff;
    }

    public void setPayoff(String payoff) {
        this.payoff = payoff;
    }

    public String getOwe() {
        return owe;
    }

    public void setOwe(String owe) {
        this.owe = owe;
    }

    @Override
    public boolean getItemHover() {
        return false;
    }

    @Override
    public void setItemHover(boolean b) {

    }
}
