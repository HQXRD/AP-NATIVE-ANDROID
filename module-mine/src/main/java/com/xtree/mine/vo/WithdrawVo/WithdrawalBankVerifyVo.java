package com.xtree.mine.vo.WithdrawVo;

import me.xtree.mvvmhabit.http.BaseResponse2;

/**
 * 银行卡验证当前渠道信息
 */
public class WithdrawalBankVerifyVo extends BaseResponse2 {
   /*{
	"status": 10000,
	"message": "success",
	"data": {
		"is_digital": false,
		"money": 100,
		"money_real": "100",
		"fee": 0,
		"quota": 1000,
		"quota_need_deduct": false,
		"balance": 1000,
		"user_bank_info": {
			"id": "1283155",
			"nickname": "",
			"user_id": "5225050",
			"user_name": "elly41",
			"email": null,
			"bank_id": "30",
			"bank_name": "交通银行",
			"province_id": "2",
			"province": "上海",
			"city_id": "140",
			"city": "上海",
			"branch": "交通银行",
			"account_name": "李白一",
			"account": "******2820",
			"status": "1",
			"utime": "2024-02-03 10:49:49",
			"atime": "2024-02-03 10:49:49"
		}
	},
	"timestamp": 1715322110
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
