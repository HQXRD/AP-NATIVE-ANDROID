package com.xtree.mine.ui.rebateagrt.model;

import com.xtree.base.mvvm.recyclerview.BindModel;

/**
 * Created by KAKA on 2024/3/12.
 * Describe: 游戏场馆返水总计数据.
 */
public class GameRebateAgrtTotalModel extends BindModel {

    //总投注额
    private String sum_bet = "-";
    //有效投注额
    private String sum_effective_bet = "-";
    //返水总额
    private String sum_total_money = "-";
    //下级工资
    private String sum_sub_money = "-";
    //自身工资
    private String sum_self_money = "-";
    //流水
    private String sum_liushui;

    public String getSum_bet() {
        return sum_bet;
    }

    public void setSum_bet(String sum_bet) {
        if (sum_bet != null && !sum_bet.isEmpty()) {
            this.sum_bet = sum_bet;
        }
    }

    public String getSum_effective_bet() {
        return sum_effective_bet;
    }

    public void setSum_effective_bet(String sum_effective_bet) {
        if (sum_effective_bet != null && !sum_effective_bet.isEmpty()) {
            this.sum_effective_bet = sum_effective_bet;
        }
    }

    public String getSum_total_money() {
        return sum_total_money;
    }

    public void setSum_total_money(String sum_total_money) {
        if (sum_total_money != null && !sum_total_money.isEmpty()) {
            this.sum_total_money = sum_total_money;
        }
    }

    public String getSum_sub_money() {
        return sum_sub_money;
    }

    public void setSum_sub_money(String sum_sub_money) {
        if (sum_sub_money != null && !sum_sub_money.isEmpty()) {
            this.sum_sub_money = sum_sub_money;
        }
    }

    public String getSum_self_money() {
        return sum_self_money;
    }

    public void setSum_self_money(String sum_self_money) {
        if (sum_self_money != null && !sum_self_money.isEmpty()) {
            this.sum_self_money = sum_self_money;
        }
    }

    public String getSum_liushui() {
        return sum_liushui;
    }

    public void setSum_liushui(String sum_liushui) {
        if (sum_liushui != null && !sum_liushui.isEmpty()) {
            this.sum_liushui = sum_liushui;
        }
    }
}