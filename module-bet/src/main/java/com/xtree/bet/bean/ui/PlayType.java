package com.xtree.bet.bean.ui;

import com.stx.xhb.androidx.entity.BaseBannerInfo;
import com.xtree.base.vo.BaseBean;

import java.util.List;

public interface PlayType extends BaseBean {
    /**
     * 获取玩法ID
     * @return
     */
    String getId();
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
     * 设置玩法名称
     * @return
     */
    String setPlayTypeName(String playTypeName);

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

    /**
     * 获取冠军赛事投注玩法列表
     * @return
     */
    List<Option> getChampionOptionList();

    /**
     * 获取玩法阶段，如足球上半场、全场等
     * @return
     */
    int getPlayPeriod();

    /**
     * 获取盘口组标签集合
     * @return
     */
    List<String> getTags();

    /**
     * 获取所属玩法集ID
     * @return
     */
    String getCategoryId();
}
