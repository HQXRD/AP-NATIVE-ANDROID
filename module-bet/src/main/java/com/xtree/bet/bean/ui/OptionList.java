package com.xtree.bet.bean.ui;

import android.os.Parcelable;

import com.xtree.base.vo.BaseBean;

import java.util.List;

public interface OptionList extends BaseBean {

    long getId();

    /**
     * 是否开售，0暂停，1开售，-1未开售（未开售状态一般是不展示的）
     */
    boolean isOpen();

    /**
     * 是否支持串关，0 不可串关，1 可串关
     */
    boolean isAllowCrossover();

    /**
     * 是否为最优线，带线玩法可用该字段进行排序，从小到大
     * 代表优先级，比如让分玩法有-0.5 -0.25 0几个让球方式，这个属性就代码了它们的优先级
     */
    int getSort();

    /**
     * line值，带线玩法的线，例如大小球2.5线，部分玩法展示可用该字段进行分组展示
     */
    String getLine();

    /**
     * 玩法选项
     */
    List<Option> getOptionList();
}
