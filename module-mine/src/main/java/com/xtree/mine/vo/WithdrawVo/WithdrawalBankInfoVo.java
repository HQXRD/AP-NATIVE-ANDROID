package com.xtree.mine.vo.WithdrawVo;

import java.util.ArrayList;
import java.util.List;

/**
 * 銀行卡提款 渠道信息
 */
public class WithdrawalBankInfoVo{
    public String check;//post多加一个check参数，新增返回参数：check ,需带入下一个接口 {{host}}/api/withdrawal/verify
    public String code;
    /*
       "is_digital": false,
        "quota": 1000,
        "quota_need_deduct": false,
        "balance": 1000,
        "day_rest_count": "101",
        "day_rest_amount": "102000.00",
        "day_total_count": 101,
        "day_total_amount": "102000.0000",
        "day_used_count": 0,
        "day_used_amount": 0,
        "min_money": "10",
        "max_money": "4444",
        "fee": 0,
        "rate": 0,
     */
    public  String fast_iframe_url;//三方web加载地址
    public Object money_options; //多金额选项
    public List<String> fixamountList = new ArrayList<>(); // 自己加的
    public ArrayList<WithdrawalAmountVo> amountVoList = new ArrayList<>();//针对固额 多金额封装对象

    public boolean money_fixed; //固额开启标志 true 代表有固额选项；false:没有固额选线
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

    public static class UserBankInfo {
        /*
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
         */
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
        public String branch;
        public String account_name;

        public String account;
        public String status;
        public String utime;
        public String atime;

    }
    /**
     * 固额 金额选项
     */
    public static class WithdrawalAmountVo {

        @Override
        public String toString() {
            return "WithdrawalAmountVo{" +
                    "amount='" + amount + '\'' +
                    ", flag=" + flag +
                    '}';
        }

        public String amount ;
        public boolean flag;//是否被选中的标记
    }

}
