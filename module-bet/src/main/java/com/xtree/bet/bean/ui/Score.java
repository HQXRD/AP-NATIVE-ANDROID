package com.xtree.bet.bean.ui;

import java.util.List;

/**
 * 比分信息
 */
public interface Score {
    /**
     * 获取玩法阶段
     * @return
     */
    String getPeriod();

    /**
     * 获取比分
     * @return
     */
    List<Integer> getScores();

}
