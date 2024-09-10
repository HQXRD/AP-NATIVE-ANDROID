package com.xtree.mine.vo.WithdrawVo;

import java.util.ArrayList;

/**
 * 提款渠道详情
 */
public class WithdrawalInfoVo{
    public String code;
    public String fast_iframe_url;//第三方支付 不为空时候加载web页面

    @Override
    public String toString() {
        return "WithdrawalInfoVo{" +

                ", money_fixed=" + money_fixed +
                ", is_digital=" + is_digital +
                ", quota='" + quota + '\'' +
                ", quota_need_deduct=" + quota_need_deduct +
                ", balance='" + balance + '\'' +
                ", day_rest_count='" + day_rest_count + '\'' +
                ", day_rest_amount='" + day_rest_amount + '\'' +
                ", day_total_count='" + day_total_count + '\'' +
                ", day_total_amount='" + day_total_amount + '\'' +
                ", day_used_count='" + day_used_count + '\'' +
                ", day_used_amount='" + day_used_amount + '\'' +
                ", min_money='" + min_money + '\'' +
                ", max_money='" + max_money + '\'' +
                ", fee='" + fee + '\'' +
                ", rate='" + rate + '\'' +
                ", user_bank_info=" + user_bank_info +
                ", money_options=" + money_options +
                '}';
    }

    /*  public int status ;
      public String message;*/
    public boolean money_fixed;
    public boolean is_digital;
    public String quota;
    public boolean quota_need_deduct;
    public String balance;
    public String day_rest_count; //每日限制次数
    public String day_total_count;//每日限制次数
    public String day_rest_amount;//提款额度

    public String day_total_amount;//提款额度
    public String day_used_count;//已提款次数
    public String day_used_amount;//已提款次数
    public String min_money;
    public String max_money;
    public String fee;
    public String rate;
    public ArrayList<UserBankInfo> user_bank_info;
    public Object money_options;
     /*"is_digital": false,
        "quota": 63968.8,
        "quota_need_deduct": false,
        "balance": 63968.8,
        "day_rest_count": "100",
        "day_rest_amount": "200000.00",
        "day_total_count": "100",
        "day_total_amount": "200000",
        "day_used_count": 0,
        "day_used_amount": 0,
        "min_money": "100",
        "max_money": "20000",
        "fee": "2",
        "rate": 0,*/

    public static class UserBankInfo {
        public String id;
        public String nickname;
        public String user_name;
        public String usdt_type;
        public String status;
        public String atime;
        public String utime;

        public String user_quota;
        public String restrictions_quota;
        public String restrictions_teamquota;
        public String userlimitswitch;

        public String teamlinitswitch;
        public String uinuout_uptime;
        public String effective_quota;
        public String effective_data;
        public String cnybank_backblance;
        public String card_type;
        public String vip_virtual_currency_quota;
        public String vvcq_updated_at;
        public String user_id;
        public String account;

        /*""id": "3131",
				"user_name": "elly40",
				"usdt_type": "TRC20_USDT",
				"status": "1",
				"atime": "2024-01-27 17:20:35",
				"utime": "2024-01-27 17:20:35",
				"user_quota": "14000.0000",
				"restrictions_quota": "0.0000",
				"restrictions_teamquota": "0.0000",
				"userlimitswitch": "0",
				"teamlinitswitch": "0",
				"uinuout_uptime": "2024-05-03 19:51:06",
				"effective_quota": "0.0000",
				"effective_data": null,
				"cnybank_backblance": "0.0000",
				"card_type": "1",
				"vip_virtual_currency_quota": "112901.0000",
				"vvcq_updated_at": "2024-05-03 19:50:09",
				"user_id": "5224744",
				"account": "******3280" */
    }
}
