package com.xtree.bet.contract;

/**
 * 今日赛事列表中点击未开赛展开或收起赛事列表
 */

public class BetContract {
    public String action;
    public static final String ACTION_EXPAND = "ACTION_EXPAND";
    /**
     * 打开串关
     */
    public static final String ACTION_OPEN_CG = "ACTION_OPEN_CG";
    /**
     * 打开今日
     */
    public static final String ACTION_OPEN_TODAY = "ACTION_OPEN_TODAY";
    /**
     * 购物车数据改变
     */
    public static final String ACTION_BTCAR_CHANGE = "ACTION_BTCAR_CHANGE";

    public BetContract(String action){
        this.action = action;
    }
}
