package com.xtree.bet.bean.ui;

import com.xtree.base.vo.BaseBean;

import java.util.List;

public interface BtResult extends BaseBean {
    /**
     * 获取订单状态
     * @return
     */
    int getStatus();

    /**
     * 获取订单状态说明
     * @return
     */
    String getStatusDesc();

    /**
     * 订单ID
     * @return
     */
    String getId();

    /**
     * 是否投注成功
     * @return
     */
    boolean isSuccessed();
    /**
     * 获取串关名称，如2串1
     * @return
     */
    String getCgName();
    /**
     * 获取投注金额
     * @return
     */
    double getBtAmount();
    /**
     * 获取可赢金额
     * @return
     */
    double getBtWin();

    /**
     * 获取投注时间
     * @return
     */
    long getBtDate();

    /**
     * 获取投注比赛选项列表
     * @return
     */
    List<BtResultOption> getBetResultOption();

    /**
     * 是否可以提前结算
     * @return
     */
    boolean canAdvanceSettle();

    /**
     * 是否可以提前结算
     * @return
     */
    double getAdvanceSettleAmount();
    /**
     * 获取"单位提前结算价格"
     * @return
     */
    double getUnitCashOutPayoutStake();

    /**
     * 是否有提前结算数据
     * @return
     */
    boolean isAdvanceSettlement();

    /**
     * 获取提前结算状态
     * @return
     */
    String getAdvanceSettlementStatus();

    /**
     * 获取提前结算时间
     * @return
     */
    String getAdvanceSettlementDate();
    /**
     * 获取提前结算本金
     * @return
     */
    double getAdvanceSettlementCost();
    /**
     * 获取提前结算输赢
     * @return
     */
    double getAdvanceSettlementResult();

    /**
     * 获取提前结算返还金额
     * @return
     */
    double getAdvanceSettlementBack();
}
