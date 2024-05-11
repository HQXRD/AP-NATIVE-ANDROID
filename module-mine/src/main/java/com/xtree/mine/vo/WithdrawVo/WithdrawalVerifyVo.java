package com.xtree.mine.vo.WithdrawVo;

import java.util.ArrayList;

import me.xtree.mvvmhabit.http.BaseResponse;
import me.xtree.mvvmhabit.http.BaseResponse2;

/**
 * 验证当前渠道信息
 */
public class WithdrawalVerifyVo  extends BaseResponse2 {
    /*{
	"status": 10000,
	"message": "success",
	"data": {
		"is_digital": true,
		"money": 999,
		"money_real": "999",
		"fee": 0,
		"quota": 9919659.25,
		"quota_need_deduct": true,
		"balance": 9919659.25,
		"user_bank_info": {
			"id": "3153",
			"user_name": "elly40",
			"usdt_type": "ERC20_USDT",
			"status": "1",
			"atime": "2024-01-31 10:22:43",
			"utime": "2024-01-31 10:22:43",
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
			"account": "******900b"
		}
	},
	"timestamp": 1714974367
}*/
    public boolean is_digital;
    public String money;
    public String money_real;
    public String fee;

    public String quota;
    public boolean quota_need_deduct;
    public String balance;
    public UserBankInfo user_bank_info;
    public class  UserBankInfo{
        public String id;
        public String nickname;
        public String user_id;
        public String user_name;
        public String email;
        public String bank_id;

        public String bank_name;
        public String province_id;
        public String province;
        public String city_id;

        public String city;
        public String branch;
        public String account_name;
        public String account;
        public String status;
        public String utime;
        public String atime;
        public String usdt_type;

        /*"id": "1283082",
                "nickname": "",
                "user_id": "2789630",
                "user_name": "yiersan",
                "email": null,
                "bank_id": "85",
                "bank_name": "鞍山银行",
                "province_id": "1",
                "province": "北京",
                "city_id": "34",
                "city": "北京",
                "branch": "张三",
                "account_name": "马西嫂",
                "account": "******5678",
                "status": "1",
                "utime": "2024-03-23 16:06:11",
                "atime": "2024-03-23 16:06:11"*/
    }

}
