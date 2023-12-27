package com.xtree.bet.bean.ui;

import com.xtree.base.vo.BaseBean;

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
}
