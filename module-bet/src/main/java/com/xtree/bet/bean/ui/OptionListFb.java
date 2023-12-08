package com.xtree.bet.bean.ui;

import com.xtree.bet.bean.OptionDataListInfo;
import com.xtree.bet.bean.OptionInfo;

import java.util.ArrayList;
import java.util.List;

public class OptionListFb implements OptionList {
    OptionDataListInfo optionDataListInfo;

    public OptionListFb(OptionDataListInfo optionDataListInfo){
        this.optionDataListInfo = optionDataListInfo;
    }

    public int getId() {
        return optionDataListInfo.id;
    }

    /**
     * 玩法销售状态，0暂停，1开售，-1未开售（未开售状态一般是不展示的）
     */
    public int getSellState() {
        return optionDataListInfo.ss;
    }

    /**
     * 是否支持串关，0 不可串关，1 可串关
     */
    public int getAllowCrossover() {
        return optionDataListInfo.au;
    }

    /**
     * 是否为最优线，带线玩法可用该字段进行排序，从小到大
     * 代表优先级，比如让分玩法有-0.5 -0.25 0几个让球方式，这个属性就代码了它们的优先级
     */
    public int getSort() {
        return optionDataListInfo.mbl;
    }

    /**
     * line值，带线玩法的线，例如大小球2.5线，部分玩法展示可用该字段进行分组展示
     */
    public String getLine() {
        return optionDataListInfo.li;
    }

    /**
     * 玩法选项
     */
    @Override
    public List<Option> getOptionList() {
        List<Option> optionList = new ArrayList<>();
        for (OptionInfo optionInfo : optionDataListInfo.op) {
            optionList.add(new OptionFb(optionInfo));
        }
        return optionList;
    }
}
