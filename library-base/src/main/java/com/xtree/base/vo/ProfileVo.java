package com.xtree.base.vo;

import java.util.Arrays;

public class ProfileVo {

    //public int bet_user; // 1,
    //public int bet_user_chess; // 1,
    //public int top_user; // 0,
    //public int top_user_hour; // 0,
    //public int top_user_day; // 0,
    //public int topUserSports; // 1,
    //public int topUserChess; // 0,
    //public int topUserLive; // 0,
    //public int topUserLottery; // 1,
    //public int topUserPinkLottery; // 1,
    //public int topUserPinkSports; // 0,
    //public int topUserPinkChess; // 0,
    //public int topUserPinkLive; // 0,
    //public int is_director; // 0,
    //public int frequency; // null,
    //public int liveStatus; // 0,
    //public int sportStatus; // 0,
    //public int maxLivePoint; // 0,
    //public int maxSportPoint; // 0,
    public int userid; // 5373118,
    public String username; // test032
    public String nickname; // test032
    public int agency_model; // 4,
    public int user_agency_model; // 2,
    public int usertype; // 1,
    public int parentid; // 3160900,
    public int lvtopid; // 3160839,
    public int iscreditaccount; // 0,
    public int is_top; // 0,
    public int auto_thrad_status; // 0,
    public String preinfo; // ""
    public int messages; // 0,
    public String availablebalance; // "0.0000",
    public String rebate_percentage; // "3.0%",
    public boolean has_securitypwd; // false,
    public boolean solo_challenge; // true,
    //public int set_question; // 0 或 [4,18,2]
    public int twofa; // 0,
    public int twofa_login_enabled; // "1",
    public int twofa_withdraw_enabled; // "1",
    public boolean is_binding_usdt; // false,
    public boolean is_binding_card; // false,
    public boolean is_binding_phone; // false,
    public boolean is_binding_email; // false,
    public int isFrozen; // 0,
    public int is_activity; // 0,
    //public int is_daily_wage; // 0,
    //public int is_hourly_rate; // 0,
    public CreditWalletVo creditwallet;
    //public boolean credit_USDT_enable; // false,
    //public boolean credit_RMB_enable; // false,
    //public String balance_U; // "0.0000"
    //public String balance_RMB; // "0.0000"

    public int user_type; // 1,
    public String birthday; // ""
    public USDTInfoVo[] binding_usdt_info; // [],
    public CardInfoVo[] binding_card_info; // [],
    public String binding_phone_info; // ""
    public String binding_email_info; // ""
    public String ratio; // ""
    //public String[] withdraw_risk_refused; // [],
    //public int act_userlabel_id; // 0,
    //public int act_userlabel_type; // 0,
    //public int act_userlabel_st; // 0,
    //public int act_userlabel_amount; // 0,
    //public int act_userlabel_cumrech; // 0,
    //public int act_userlabel_signupbonus; // 0,
    //public int act_userlabel_signupbonus_status; // 0,
    //public int act_userlabel_advance; // 0,
    //public int act_userlabel_todayuse; // 0,
    //public int act_userlabel_pupshow; // 1,
    public int phone_selfupdate; // 1,
    public String email_selfupdate; // "1"

    @Override
    public String toString() {
        return "ProfileVo{" +
                "userid=" + userid +
                ", username='" + username + '\'' +
                ", nickname='" + nickname + '\'' +
                ", agency_model=" + agency_model +
                ", user_agency_model=" + user_agency_model +
                ", usertype=" + usertype +
                ", parentid=" + parentid +
                ", lvtopid=" + lvtopid +
                ", iscreditaccount=" + iscreditaccount +
                ", is_top=" + is_top +
                ", auto_thrad_status=" + auto_thrad_status +
                ", preinfo='" + preinfo + '\'' +
                ", messages=" + messages +
                ", availablebalance='" + availablebalance + '\'' +
                ", rebate_percentage='" + rebate_percentage + '\'' +
                ", has_securitypwd=" + has_securitypwd +
                ", solo_challenge=" + solo_challenge +
                ", twofa=" + twofa +
                ", twofa_login_enabled=" + twofa_login_enabled +
                ", twofa_withdraw_enabled=" + twofa_withdraw_enabled +
                ", is_binding_usdt=" + is_binding_usdt +
                ", is_binding_card=" + is_binding_card +
                ", is_binding_phone=" + is_binding_phone +
                ", is_binding_email=" + is_binding_email +
                ", isFrozen=" + isFrozen +
                ", is_activity=" + is_activity +
                ", creditwallet=" + creditwallet +
                ", user_type=" + user_type +
                ", birthday='" + birthday + '\'' +
                ", binding_usdt_info=" + Arrays.toString(binding_usdt_info) +
                ", binding_card_info=" + Arrays.toString(binding_card_info) +
                ", binding_phone_info='" + binding_phone_info + '\'' +
                ", binding_email_info='" + binding_email_info + '\'' +
                ", ratio='" + ratio + '\'' +
                ", phone_selfupdate=" + phone_selfupdate +
                ", email_selfupdate='" + email_selfupdate + '\'' +
                '}';
    }

    public class CreditWalletVo {
        public boolean credit_USDT_enable; // false,
        public boolean credit_RMB_enable; // false,
        public String balance_U; // "0.0000"
        public String balance_RMB; // "0.0000"

        @Override
        public String toString() {
            return "CreditWalletVo{" +
                    "credit_USDT_enable=" + credit_USDT_enable +
                    ", credit_RMB_enable=" + credit_RMB_enable +
                    ", balance_U='" + balance_U + '\'' +
                    ", balance_RMB='" + balance_RMB + '\'' +
                    '}';
        }
    }

    public class USDTInfoVo {

        public String id; // "78048",
        public String atime; // "2022-12-08 21:17:23",
        public String uinuout_uptime; // "2023-07-18 15:24:38",
        public String usdt_card; // "******w8ds",
        public String usdt_type; // "ERC20_USDT",
        public String utime; // "2022-12-08 21:17:23"

        @Override
        public String toString() {
            return "USDTInfoVo{" +
                    "id='" + id + '\'' +
                    ", atime='" + atime + '\'' +
                    ", uinuout_uptime='" + uinuout_uptime + '\'' +
                    ", usdt_card='" + usdt_card + '\'' +
                    ", usdt_type='" + usdt_type + '\'' +
                    ", utime='" + utime + '\'' +
                    '}';
        }
    }

    public class CardInfoVo {

        public String account; // "***************8487",
        public String atime; // "2023-09-01 19:58:53",
        public String bank_id; // "6",
        public String bank_name; // "中国工商银行",
        public String branch; // "测试",
        public String city; // "梅州",
        public String city_id; // "292",
        public String province; // "广东",
        public String province_id; // "5",
        public String utime; // "2023-09-01 19:58:53"

        @Override
        public String toString() {
            return "CardInfoVo{" +
                    "account='" + account + '\'' +
                    ", atime='" + atime + '\'' +
                    ", bank_id='" + bank_id + '\'' +
                    ", bank_name='" + bank_name + '\'' +
                    ", branch='" + branch + '\'' +
                    ", city='" + city + '\'' +
                    ", city_id='" + city_id + '\'' +
                    ", province='" + province + '\'' +
                    ", province_id='" + province_id + '\'' +
                    ", utime='" + utime + '\'' +
                    '}';
        }
    }

}
