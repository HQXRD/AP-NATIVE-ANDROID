package com.xtree.recharge.vo;

import java.util.List;

/**
 * 充值订单详情
 */
public class RechargeOrderDetailVo {

    public String id; // 6352
    public RechargePayVo res;
    public List<CancelReasonVo> cancel_reason; // [{"id": 1, "name": "支付宝/微信风险提示"}, {"id": 5, "name": "穷"}]

    public static class CancelReasonVo {
        public int id;
        public String name;
    }

}
