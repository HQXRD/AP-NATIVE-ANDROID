package com.xtree.bet.bean.ui;

import com.xtree.base.vo.BaseBean;

/**
 *
 */
public interface BtResultOption extends BaseBean {
    /**
     * 获取联赛名称
     * @return
     */
    String getLeagueName();

    /**
     * 获取比赛时间
     * @return
     */
    long getMatchTime();

    /**
     * 获取投注名称
     * @return
     */
    String getOptionName();
    /**
     * 获取投注玩法
     * @return
     */
    String getPlayType();
    /**
     * 获取投注当时的比分
     * @return
     */
    String getScore();

    /**
     * 获取赔率
     * @return
     */
    String getOdd();

    /**
     * 获取队伍名称
     * @return
     */
    String getTeamName();
    /**
     * 获取投注结果
     * @return
     */
    String getBtResult();
    /**
     * 获取全场比分
     * @return
     */
    String getFullScore();
    /**
     * 注单是否已结算
     * @return
     */
    boolean isSettled();
    /**
     * 获取投注结果字体颜色
     * @return
     */
    int getResultColor();
}
