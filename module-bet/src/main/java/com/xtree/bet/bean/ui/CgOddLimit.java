package com.xtree.bet.bean.ui;

import android.os.Parcelable;

/**
 * 串关组合赔率及限额
 */
public interface CgOddLimit extends Parcelable {
    /**
     * 获取串关子单选项个数，如：投注4场比赛的3串1，此字段为3，如果是全串关（4串11*11），则为0；对应batchBetMatchMarketOfJumpLine接口的data.sos.sn
     * @return
     */
    int getCgCount();

    /**
     * 获取串关名称，如2串1
     * @return
     */
    String getCgName();

    /**
     * 返回串关类型
     * @return
     */
    String getCgType();

    /**
     * 获取单关，最小投注额
     * @return
     */
    double getDMin();
    /**
     * 获取单关，最大投注额
     * @return
     */
    double getDMax();

    /**
     * 获取串关，最小投注额
     * @return
     */
    double getCMin();
    /**
     * 获取串关，最大投注额
     * @return
     */
    double getCMax();
    /**
     * 获取单关赔率
     * @return
     */
    double getDOdd();
    /**
     * 获取串关赔率
     * @return
     */
    double getCOdd();
    /**
     * 获取可赢金额
     * @return
     */
    double getWin(double amount);

    /**
     * 获取投注注数
     * @return
     */
    int getBtCount();

    /**
     * 设置投注金额
     * @return
     */
    void setBtAmount(double count);
    /**
     * 获取投注金额
     * @return
     */
    double getBtAmount();
    /**
     * 获取总投注金额
     * @return
     */
    double getBtTotalAmount();
}
