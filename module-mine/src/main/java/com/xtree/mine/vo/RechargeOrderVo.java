package com.xtree.mine.vo;

/**
 * 充提-充值
 */
public class RechargeOrderVo {
    public String id; // "33805428",
    public String money; // "100.0000",
    // 0:待处理, 1:已审核, 2:充值成功, 3:充值失败, 4:超时无效, 5:已没收
    public String status; // "0", 0-待处理, 2-成功, 4-超时无效
    public String created; // "2024-01-11 09:41:45",
    public String modified; // "2024-01-11 09:41:45",
    public String cancel_status; // "0",
    public String agent_rate_cost_amount; // "1.1000",
    public String payport_nickname; // "固额快充",
    public String sysparam_prefix; // "hqppay2",
    public String amount; // null,
    public String fee; // null,
    public String recharge_json_channel; // false
}
