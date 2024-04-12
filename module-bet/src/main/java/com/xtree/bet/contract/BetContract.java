package com.xtree.bet.contract;

/**
 * 今日赛事列表中点击未开赛展开或收起赛事列表
 */

public class BetContract {
    private Object data;
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
    /**
     * 购物车数据改变
     */
    public static final String ACTION_OPTION_CHANGE = "ACTION_OPTION_CHANGE";
    /**
     * 赛事列表排序修改
     */
    public static final String ACTION_SORT_CHANGE = "ACTION_SORT_CHANGE";
    /**
     * 赛事列表盘口类型修改
     */
    public static final String ACTION_MARKET_CHANGE = "ACTION_MARKET_CHANGE";
    /**
     * 检查是否选择所有的联赛
     */
    public static final String ACTION_CHECK_ALL_CHECK = "ACTION_CHECK_ALL_CHECK";
    /**
     * 根据联赛查询列表
     */
    public static final String ACTION_CHECK_SEARCH_BY_LEAGUE = "ACTION_CHECK_SEARCH_BY_LEAGUE";
    /**
     * 投注成功
     */
    public static final String ACTION_BT_SUCESSED = "ACTION_BT_SUCESSED";

    public BetContract(String action){
        this.action = action;
    }

    public BetContract(String action, Object data){
        this.action = action;
        this.data = data;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
