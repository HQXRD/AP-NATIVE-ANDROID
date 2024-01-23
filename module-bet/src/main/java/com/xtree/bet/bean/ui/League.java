package com.xtree.bet.bean.ui;

import com.xtree.base.vo.BaseBean;

import java.util.List;

/**
 * 联赛
 */
public interface League extends BaseBean {
    /**
     * 头部类型 1-进行中或未开赛 2-球种名称
     */
    int HEAD_TYPE_LIVE_OR_NOLIVE = 1;
    /**
     * 头部类型 1-进行中或未开赛 2-球种名称
     */
    int HEAD_TYPE_SPORT_NAME = 2;
    /**
     * 获取联赛区域名称
     * @return
     */
    String getLeagueAreaName();
    /**
     * 获取联赛区域ID
     * @return
     */
    int getAreaId();
    /**
     * 设置联赛名称
     * @return
     */
    void setLeagueName(String leagueName);
    /**
     * 获取联赛名称
     * @return
     */
    String getLeagueName();
    /**
     * 获取联赛ID
     * @return
     */
    long getId();

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
    int getMatchCount();
    void setMatchCount(int matchCount);

    void setHead(boolean isHead);

    /**
     * 该联赛开售的赛事统计
     * @return
     */
    int getSaleCount();

    /**
     * 是否选中
     * @return
     */
    boolean isSelected();

    /**
     * 设置是否选中
     * @param selected
     */
    void setSelected(boolean selected);

    /**
     * 是否热门
     * @return
     */
    boolean isHot();

    /**
     * 获取头部类型 1-进行中或未开赛 2-球种名称
     * @return
     */
    int getHeadType();

    /**
     * 设置头部类型 1-进行中或未开赛 2-球种名称
     * @return
     */
    void setHeadType(int headType);
}
