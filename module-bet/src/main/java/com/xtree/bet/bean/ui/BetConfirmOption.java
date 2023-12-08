package com.xtree.bet.bean.ui;

/**
 * 玩法选项实时赔率及限额信息(为了适配FB和PM体育用)
 */
public interface BetConfirmOption {
    /**
     * 获取玩法ID
     * @return
     */
    int getPlayTypeId();

    /**
     * 获取投注项信息
     * @return
     */
    Option getOption();

    /**
     * 获取单关最小投注额限制
     * @return
     */
    double getDanMin();
    /**
     * 获取单关最大投注额限制
     * @return
     */
    double getDanMax();

    /**
     * 玩法销售状态，0暂停，1开售，-1未开售
     * @return
     */
    int isClose();
    /**
     * 足球让球当前比分， 如1-1
     * @return
     */
    String getScore();
    /**
     * 获取比赛双方名称
     * @return
     */
    String getTeamName();
}
