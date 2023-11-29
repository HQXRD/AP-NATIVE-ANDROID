package com.xtree.bet.bean.ui;

import com.xtree.bet.bean.OptionInfo;

public class OptionFbAdapter implements Option{
    private OptionInfo optionInfo;

    public OptionFbAdapter(OptionInfo optionInfo){
        this.optionInfo = optionInfo;
    }

    /**
     * 选项全称，投注框一般用全称展示
     */
    public String getName() {
        return optionInfo.na;
    }

    /**
     * 选项简称(全名or简名，订单相关为全名，否则为简名)， 赔率列表一般都用简称展示
     */
    public String getSortName() {
        return optionInfo.nm;
    }

    /**
     * 选项类型，主、客、大、小等，投注时需要提交该字段作为选中的选项参数
     * @return
     */
    public int getOptionType() {
        return optionInfo.ty;
    }

    /**
     * 欧盘赔率，目前我们只提供欧洲盘赔率，投注是请提交该字段赔率值作为选项赔率，赔率小于0代表锁盘
     */
    public double getOdd() {
        return optionInfo.od;
    }

    /**
     * 赔率
     */
    public double getBodd() {
        return optionInfo.bod;
    }

    /**
     * 赔率类型
     */
    public int getOddType() {
        return optionInfo.odt;
    }

    /**
     * 选项结算结果，仅虚拟体育展示
     */
    public int getSettlementResult() {
        return optionInfo.otcm;
    }
}
