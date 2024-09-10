package com.xtree.mine.vo.WithdrawVo;

/**
 * 验证当前渠道
 */
public class WithdrawalVerifyVo{

    public boolean is_digital;
    public String money;
    public String money_real;
    public String fee;

    public String quota;
    public boolean quota_need_deduct;
    public String balance;
    public UserBankInfo user_bank_info;

    public class UserBankInfo {
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
