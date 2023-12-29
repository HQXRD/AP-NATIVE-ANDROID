package com.xtree.bet.bean.ui;

import com.xtree.base.vo.BaseBean;

public interface Option extends BaseBean {
    
    /**
     * 选项全称，投注框一般用全称展示
     */
     String getName();

    /**
     * 选项简称(全名or简名，订单相关为全名，否则为简名)， 赔率列表一般都用简称展示
     */
     String getSortName();

    /**
     * 选项类型，主、客、大、小等，投注时需要提交该字段作为选中的选项参数
     * @return
     */
     int getOptionType();

    /**
     * 欧盘赔率，目前我们只提供欧洲盘赔率，投注是请提交该字段赔率值作为选项赔率，赔率小于0代表锁盘，此方法用于显示
     */
     double getOdd();

    /**
     * 真实欧盘赔率，目前我们只提供欧洲盘赔率，投注是请提交该字段赔率值作为选项赔率，赔率小于0代表锁盘，此方法用于投注
     */
    double getRealOdd();

    /**
     * 赔率
     */
     double getBodd();

    /**
     * 赔率类型
     */
     int getOddType();

    /**
     * 是否香港盘
     * @return
     */
     boolean isHongKongMarket();

    /**
     * 选项结算结果，仅虚拟体育展示
     */
     int getSettlementResult();
    /**
     * 设置是否选中
     * @return
     */
    boolean setSelected(boolean isSelected);
    /**
     * 是否选中
     * @return
     */
     boolean isSelected();

    /**
     * 	line值，带线玩法的线，例如大小球2.5线，部分玩法展示可用该字段进行分组展示
     * @return
     */
     String getLine();

    /**
     * 设置投注选择唯一标识
     * @param code
     */
     void setCode(String code);

    /**
     * 获取投注选择唯一标识
     * @return
     */
     String getCode();

    /**
     * 设置投注项赔率的变化
     * @return
     */
    void setChange(double newOdd);
    /**
     * 赔率是否上升
     * @return
     */
    boolean isUp();
    /**
     * 赔率是否下降
     * @return
     */
    boolean isDown();
    void reset();

    /**
     * 获取投注选项所属的投注线
     * @return
     */
    OptionList getOptionList();
}
