package com.xtree.bet.bean.ui;

import java.util.List;

public interface PlayType {
    /**
     * 获取玩法类型，如 亚盘、大小球等
     * @return
     */
    int getPlayType();

    /**
     * 获取玩法名称
     * @return
     */
    String getPlayTypeName();

    /**
     * 获取投注玩法列表
     * @return
     */
    List<OptionList> getOptionLists();

    /**
     * 获取投注玩法列表
     * @return
     */
    List<Option> getOptionList();
}
