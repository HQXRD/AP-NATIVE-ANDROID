package com.xtree.mine.vo;

import java.util.ArrayList;

import me.xtree.mvvmhabit.http.BaseResponse2;

public class VirtualCashVo extends BaseResponse2 {
    public String count;//今日提款次数
    public String rest;//今日提款额度
    public String id;
    public String exchangerate;//汇率
    public String withdraw_rand_on;
    public String freeWithDrawTimes;
    public String ourfee;
    public String ur_here;
    public String times;//显示提款次数
    public String limitarr;
    public String d_max_money;
    public String d_min_money;
    public String max_money; //单笔提款范围max
    public String min_money;//单笔提款范围 min
    public String usdt_type; //已经提款次数
    public Wraptime wraptime;

    public class Wraptime {
        public String starttime;
        public String endtime;

    }

    public ArrayList<Usdtinfo> usdtinfo;

    public class Usdtinfo {
        public String id;
        public String user_name;
        public String usdt_type;
        public String userid;
        public String usdt_card;
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
        public String username;
        public String min_money;
        public String max_money;
    }

    public User user;

    public class User {
        public String userid;
        public String username;
        public String availablebalance; //可提款金额
        public String registertime;
        public String relavailablebalance;
        public String formula;
        public String cafAvailableBalance;
        public String unSportActivityAward;
    }

}
