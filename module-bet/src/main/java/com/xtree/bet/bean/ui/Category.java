package com.xtree.bet.bean.ui;

import java.util.List;

/**
 * 玩法分类（如热门，斗场等）
 */
public interface Category {
    /**
     * 获取玩法分类名称
     * @return
     */
    String getName();

    /**
     * 获取玩法列表
     * @return
     */
    List<PlayType> getPlayTypeList();

    /**
     * 增加玩法列表
     * @return
     */
    void addPlayTypeList(PlayType playType);

    /**
     * 获取分类优先级
     * @return
     */
    int getSort();
}
