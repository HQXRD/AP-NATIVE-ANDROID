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

    List<Match> getMatchList();

    int getSort();

    void setSort(int sort);
}
