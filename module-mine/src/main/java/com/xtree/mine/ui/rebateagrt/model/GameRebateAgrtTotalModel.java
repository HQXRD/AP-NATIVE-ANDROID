package com.xtree.mine.ui.rebateagrt.model;

import com.xtree.base.mvvm.recyclerview.BindHead;
import com.xtree.base.mvvm.recyclerview.BindModel;

/**
 * Created by KAKA on 2024/3/12.
 * Describe: 游戏场馆返水总计数据.
 */
public class GameRebateAgrtTotalModel extends BindModel {

    //总投注额
    public String sum_bet;
    //有效投注额
    public String sum_effective_bet;
    //返水总额
    public String sum_total_money;
    //下级工资
    public String sum_sub_money;
    //自身工资
    public String sum_self_money;
    //流水
    public String sum_liushui;
}