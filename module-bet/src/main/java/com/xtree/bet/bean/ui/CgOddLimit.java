package com.xtree.bet.bean.ui;

/**
 * 串关组合赔率及限额
 */
public interface CgOddLimit {

    /**
     * 获取串关名称，如2串1
     * @return
     */
    String getCgName();

    /**
     * 获取单关，最小投注额
     * @return
     */
    int getDMin();
    /**
     * 获取单关，最大投注额
     * @return
     */
    int getDMax();

    /**
     * 获取串关，最小投注额
     * @return
     */
    int getCMin();
    /**
     * 获取串关，最大投注额
     * @return
     */
    int getCMax();
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
     * 获取投注金额
     * @return
     */
    int getPay(int amount);

    /**
     * 获取投注注数
     * @return
     */
    int getBtCount();
}
