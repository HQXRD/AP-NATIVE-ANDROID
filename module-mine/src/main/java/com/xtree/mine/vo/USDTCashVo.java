package com.xtree.mine.vo;

import java.util.ArrayList;

import me.xtree.mvvmhabit.http.BaseResponse2;

/**
 * USDT 提款请求返回体
 */
public class USDTCashVo extends BaseResponse2 {
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
   /* "usdt_type": "2",
            "": 33,
            "": 680000,
            "": 33,
            "": 680000,
            "": "{\"6\":[10,550],\"7\":[3,100],\"23\":[100,50000],\"10\":[10,800]}",
            "": "1",
            "": "2000",
            "": "账户提款",
            "": "0",
            "usdtfee": "0.0",
            "": 0,
            "": "0",
            "": "7.2"*/

    public User user;
    public Wraptime wraptime;

    public ArrayList<Usdtinfo> usdtinfo;

    public String availablebalance;
    public String channel_list_use;
    public ArrayList<Channel> channel_list;

    /**
     * 顶部选项卡
     */
    public class Channel {
        public String name;
        public String id;
        public String title;
        public String type;

        @Override
        public String toString() {
            return "Channel{" +
                    "name='" + name + '\'' +
                    ", id='" + id + '\'' +
                    ", title='" + title + '\'' +
                    ", type='" + type + '\'' +
                    '}';
        }
    }

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
        public String quota ;//虚拟币提款余额

        /*"id": "3212",
                "user_name": "tst033@as",
                "usdt_type": "ERC20_USDT",
                "userid": "5228471",
                "usdt_card": "******r122",
                "status": "1",
                "atime": "2024-01-18 11:28:57",
                "utime": "2024-01-18 11:28:57",

                "user_quota": "0.0000",
                "restrictions_quota": "16124.0000",
                "restrictions_teamquota": "16124.0000",
                "userlimitswitch": "0",
                "teamlinitswitch": "0",
                "uinuout_uptime": "2024-01-18 11:28:57",
                "effective_quota": "0.0000",
                "effective_data": null,

                "cnybank_backblance": "0.0000",
                "card_type": "1",
                "vip_virtual_currency_quota": null,
                "vvcq_updated_at": null,
                "username": "tst033@as",
                "min_money": 7,
                "max_money": 133213*/
    }

    public class Wraptime {
        public String starttime;
        public String endtime;

    }

    public class User {
        public String nickname ;
        public String userid;
        public String username;
        public String availablebalance; //可提款金额
        public String registertime;
        public String relavailablebalance;
        public String formula;
        public String cafAvailableBalance;
        public String unSportActivityAward;
        /*"userid": "5228471",
                "username": "tst033@as",
                "availablebalance": 997858,
                "registertime": "2023-12-19 13:52:12",
                "relavailablebalance": "997858.0000",
                "formula": "(997858)",
                "cafAvailableBalance": 997858,
                "unSportActivityAward": 0*/
    }
}
