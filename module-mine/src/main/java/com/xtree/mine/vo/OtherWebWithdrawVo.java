package com.xtree.mine.vo;

import java.util.ArrayList;

import me.xtree.mvvmhabit.http.BaseResponse2;

/**
 * other提款请求返回体(支付宝、微信)
 */
public class OtherWebWithdrawVo extends BaseResponse2 {
    @Override
    public String toString() {
        return "OtherWebWithdrawVo{" +
                "channel_list=" + channel_list +
                ", banks=" + banks +
                ", wraptime=" + wraptime +
                ", user=" + user +
                ", usdt_type='" + usdt_type + '\'' +
                ", min_money='" + min_money + '\'' +
                ", max_money='" + max_money + '\'' +
                ", d_min_money='" + d_min_money + '\'' +
                ", d_max_money='" + d_max_money + '\'' +
                ", limitarr='" + limitarr + '\'' +
                ", count='" + count + '\'' +
                ", times='" + times + '\'' +
                ", ur_here='" + ur_here + '\'' +
                ", ourfee='" + ourfee + '\'' +
                ", usdtfee='" + usdtfee + '\'' +
                ", freeWithDrawTimes='" + freeWithDrawTimes + '\'' +
                ", withdraw_rand_on='" + withdraw_rand_on + '\'' +
                ", exchangerate='" + exchangerate + '\'' +
                '}';
    }
    public String rest ;
    public ArrayList<ChannelInfo> channel_list;
    public ArrayList<BankInfo> banks;
    public WrapTime wraptime;
    public User user;

    public String usdt_type;
    public String min_money;
    public String max_money;
    public String d_min_money;
    public String d_max_money;
    public String limitarr;
    public String count;
    public String times;
    public String ur_here;
    public String ourfee;
    public String usdtfee;
    public String freeWithDrawTimes;
    public String withdraw_rand_on;
    public String exchangerate;

    public class User {
        /*"userid": "5228811",
          "username": "lisi005b6@as",
          "availablebalance": 3834.49,
          "registertime": "2024-01-30 15:05:49",
          "relavailablebalance": "3834.4900",
          "formula": "(3834.49)",
          "cafAvailableBalance": 3834.49,
          "unSportActivityAward": 0
        * */
        public String userid;
        public String username;
        public String availablebalance;
        public String registertime;
        public String relavailablebalance;
        public String formula;
        public String cafAvailableBalance;
        public String unSportActivityAward;
    }

    public class WrapTime {
        public String starttime;
        public String endtime;
    }

    public class BankInfo {
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
        public String is_difname;
        public String lavewdrawnum;
        public String min_money;
        public String max_money;

        /*"id": "1283283",
                "nickname": "",
                "user_id": "5228811",
                "user_name": "lisi005b6@as",
                "email": null,
                "bank_id": "17",
                "bank_name": "兴业银行",
                "province_id": "28",
                "province": "内蒙古",
                "city_id": "95",
                "city": "赤峰",
                "branch": "赤峰支行",
                "account_name": "李斯",
                "account": "**************3999",
                "status": "1",
                "utime": "2024-01-31 11:14:01",
                "atime": "2024-01-31 11:14:01",
                "is_difname": 0,
                "lavewdrawnum": "",
                "min_money": 33,
                "max_money": 680000*/
    }

    /*页面加载所需的数据*/
    public class ChannelInfo {
        @Override
        public String toString() {
            return "ChannelInfo{" +
                    "typenum='" + typenum + '\'' +
                    ", name='" + name + '\'' +
                    ", thiriframe_use='" + thiriframe_use + '\'' +
                    ", thiriframe_status='" + thiriframe_status + '\'' +
                    ", thiriframe_msg='" + thiriframe_msg + '\'' +
                    ", thiriframe_url='" + thiriframe_url + '\'' +
                    ", channel_amount_use='" + channel_amount_use + '\'' +
                    ", fixamount_list_status='" + fixamount_list_status + '\'' +
                    ", fixamount_list=" + fixamount_list +
                    ", fixamount_list_status1='" + fixamount_list_status1 + '\'' +
                    ", fixamount_list1=" + fixamount_list1 +
                    ", fixamount_rule_status='" + fixamount_rule_status + '\'' +
                    ", fixamount_rule=" + fixamount_rule +
                    ", earnest_money_pl='" + earnest_money_pl + '\'' +
                    ", didvided_list=" + didvided_list +
                    ", opfast_min_money='" + opfast_min_money + '\'' +
                    ", opfast_max_money='" + opfast_max_money + '\'' +
                    ", fee_ratio='" + fee_ratio + '\'' +
                    ", configkey='" + configkey + '\'' +
                    ", min_money='" + min_money + '\'' +
                    ", max_money='" + max_money + '\'' +
                    ", recommend='" + recommend + '\'' +
                    '}';
        }

        public String typenum;
        public String name;
        public String thiriframe_use;
        public String thiriframe_status;//异常状态码 1：成功 0：失败
        public String thiriframe_msg; //异常显示信息
        public String thiriframe_url;//加载的web地址
        public String channel_amount_use;
        public String fixamount_list_status;
        public String fixamount_list;
        public String fixamount_list_status1;
        public String fixamount_list1;
        public String fixamount_rule_status;
        public String fixamount_rule;
        public String earnest_money_pl;
        public String didvided_list;
        public String opfast_min_money;
        public String opfast_max_money;
        public String fee_ratio;
        public String configkey; //提款类型  onepaywx 微信
        public String min_money;
        public String max_money;
        public String recommend;
       /*"typenum": "14",
               "name": "极速微信提款",
               "thiriframe_use": 1,
               "thiriframe_status": 1,
               "thiriframe_msg": "",
               "thiriframe_url": "https://pre-infogen.1-pay.co/op1/auction/alipay/withdraw/eyJpdiI6IkRyR1QvUmo1aG85M2tWa2lmMXgrZEE9PSIsInZhbHVlIjoiMVVTUUsyLzVUSS85c3lFRDBqRVc2SWhwMXNRYnczbmdwRGttTnVRWkZ4T1dKdzF3RzNjTGZveTN0a0hTN0lSVFBMZUphOHBsVi9pMHJnbVozLzdhTTdHOEV1ZGZVeVRNREpMbG9zSzd5N25pQjZPUVNyR3VxYlp4Q0ZMcTgzOWsrRVk5NWs2Z08vZ1paa0w1bW9BU3RUVWttc0pjV1poTUlqVnJIWTJIRHZ3NlVMUXBydUhVM05NbEQ1NGJjdHk5IiwibWFjIjoiZTYzMjYwZjFlZWY1ZmJlNzI1MDA3MTkzZjU2OTFmMjk0NTMwMDQ1OWNiMGViYWVmZTBlNjhjZTQ5MzljNjRkYyIsInRhZyI6IiJ9",
               "channel_amount_use": 0,
               "fixamount_list_status": 0,
               "fixamount_list": "[]",
               "fixamount_list_status1": 0,
               "fixamount_list1": "[]",
               "fixamount_rule_status": 0,
               "fixamount_rule": "[]",
               "earnest_money_pl": 0,
               "didvided_list": "[]",
               "opfast_min_money": 0,
               "opfast_max_money": 0,
               "fee_ratio": "",
               "configkey": "onepaywx",*/
    }

}
