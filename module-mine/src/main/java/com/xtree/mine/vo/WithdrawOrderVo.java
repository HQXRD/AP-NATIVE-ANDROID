package com.xtree.mine.vo;

/**
 * 充提-提现
 */
public class WithdrawOrderVo {
    public String entry; // "9920539",
    public String amount; // "100.0000",
    public int fee; // 0,
    public String attach_fee; // "0.00",
    public String accepttime; // "2024-01-10 17:52:02",
    // status 0:待处理, 1:失败, 2:成功, 3:银行处理中,
    // 4:等待风控审核, 5:操作中, 6:处理超时人工处理中, 20:出款中,
    public String status; // "1",
    public String bankname; // "USDT",
    public String finishtime; // "0000-00-00 00:00:00",
    public String agent_rate_cost_amount; // "0.5500"
    public String split_order_if; // 0 不显示分单按钮， 1 显示分单按钮
}
