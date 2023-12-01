package com.xtree.bet.bean.ui;

import java.util.List;

/**
 * 联赛
 */
public interface League {
    /**
     * 获取联赛名称
     * @return
     */
    String getLeagueName();
    /**
     * 获取联赛ID
     * @return
     */
    int getId();

    /**
     * 获取比赛列表
     * @return
     */
    List<Match> getMatchList();

    /**
     * 排序
     * @return
     */
    int getSort();

    void setSort(int sort);

    String getIcon();

    /**
     * 设置是否展开赛事，true-展开，false-关闭
     * @return
     */
    boolean setExpand(boolean isExpand);

    /**
     * 获取是否展开赛事，true-展开，false-关闭
     * @return
     */
    boolean isExpand();

    boolean isHead();

    void setHead(boolean isHead);

    League instance();
}
