package com.xtree.bet.bean.ui;

import com.xtree.base.vo.BaseBean;

/**
 *
 */
public interface BetResultOption extends BaseBean {
    /**
     * 获取投注状态
     * @return
     */
    int getStatus();

    /**
     * 获取投注状态信息
     * @return
     */
    String getStatusDesc();

    /**
     * 获取注单ID
     * @return
     */
    int getId();
}
