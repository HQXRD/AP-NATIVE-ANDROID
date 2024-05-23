package com.xtree.mine.vo;

import java.util.ArrayList;

import me.xtree.mvvmhabit.http.BaseResponse2;

/*魔域 虚拟币提款 */
public class VirtualCashMoYuVo extends BaseResponse2 {
    /*"nextcontroller": "security",
"nextaction": "platwithdraw",
"ur_here": "资金密码检查",
异常情况 需要弹出资金密码检查
*/
    public String nextcontroller ;
    public String nextaction ;
    public String  ur_here ;
    public String count;//今日提款次数
    public String rest;//今日提款额度
    public String id;
    public String exchangerate;//汇率
    public String withdraw_rand_on;
    public String freeWithDrawTimes;
    public String ourfee;

    public String times;//显示提款次数
    public String limitarr;
    public String d_max_money;
    public String d_min_money;
    public String max_money; //单笔提款范围max
    public String min_money;//单笔提款范围 min
    public String usdt_type; //已经提款次数
    public WrapTime wraptime;
    public String availablebalance; //可提款金额
    public class WrapTime {
        public String starttime;
        public String endtime;

    }

    public ArrayList<UsdtInfo> usdtinfo;

    public class UsdtInfo {

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
        public String quota;//虚拟币提款余额


        @Override
        public String toString() {
            return "UsdtInfo{" +
                    "id='" + id + '\'' +
                    ", user_name='" + user_name + '\'' +
                    ", usdt_type='" + usdt_type + '\'' +
                    ", userid='" + userid + '\'' +
                    ", usdt_card='" + usdt_card + '\'' +
                    ", status='" + status + '\'' +
                    ", atime='" + atime + '\'' +
                    ", utime='" + utime + '\'' +
                    ", user_quota='" + user_quota + '\'' +
                    ", restrictions_quota='" + restrictions_quota + '\'' +
                    ", restrictions_teamquota='" + restrictions_teamquota + '\'' +
                    ", userlimitswitch='" + userlimitswitch + '\'' +
                    ", teamlinitswitch='" + teamlinitswitch + '\'' +
                    ", uinuout_uptime='" + uinuout_uptime + '\'' +
                    ", effective_quota='" + effective_quota + '\'' +
                    ", effective_data='" + effective_data + '\'' +
                    ", cnybank_backblance='" + cnybank_backblance + '\'' +
                    ", card_type='" + card_type + '\'' +
                    ", vip_virtual_currency_quota='" + vip_virtual_currency_quota + '\'' +
                    ", vvcq_updated_at='" + vvcq_updated_at + '\'' +
                    ", username='" + username + '\'' +
                    ", min_money='" + min_money + '\'' +
                    ", max_money='" + max_money + '\'' +
                    ", quota='" + quota + '\'' +
                    '}';
        }
    }

    public User user;

    public class User {
        public String nickname;
        public String userid;
        public String username;
        public String availablebalance; //可提款金额
        public String registertime;
        public String relavailablebalance;
        public String formula;
        public String cafAvailableBalance;
        public String unSportActivityAward;

        @Override
        public String toString() {
            return "User{" +
                    "userid='" + userid + '\'' +
                    ", username='" + username + '\'' +
                    ", availablebalance='" + availablebalance + '\'' +
                    ", registertime='" + registertime + '\'' +
                    ", relavailablebalance='" + relavailablebalance + '\'' +
                    ", formula='" + formula + '\'' +
                    ", cafAvailableBalance='" + cafAvailableBalance + '\'' +
                    ", unSportActivityAward='" + unSportActivityAward + '\'' +
                    '}';
        }
    }

}
