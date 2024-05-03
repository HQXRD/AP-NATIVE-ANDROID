package com.xtree.mine.vo;

import me.xtree.mvvmhabit.http.BaseResponse2;

/**
 * 提款返回model
 */
public class PlatWithdrawVo extends BaseResponse2 {
    public String channel_typenum;
    public String channel_child;
    //public String message ;
    public PlatwithdrawInfo datas;
    public User user;

    public static class User {
        /**
         * "userid":"5228471",
         * "username":"tst033@as",
         * "availablebalance":999775,
         * "registertime":"2023-12-19 13:52:12",
         * "relavailablebalance":"999775.0000",
         * "formula":"(999775)",
         * "cafAvailableBalance":999775,
         * "unSportActivityAward":0
         * ///////////////////////
         * "parentid": "2723540",
         * "usertype": "1",
         * "iscreditaccount": "0",
         * "userrank": "0",
         * "availablebalance": "999476.0000",
         * "preinfo": "",
         * "nickname": "tst033",
         * "messages": "1"
         */
        public String nickname;
        public String userid;
        public String username;
        public String availablebalance;
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
                    ", availablebalance=" + availablebalance +
                    ", registertime='" + registertime + '\'' +
                    ", relavailablebalance='" + relavailablebalance + '\'' +
                    ", formula='" + formula + '\'' +
                    ", cafAvailableBalance='" + cafAvailableBalance + '\'' +
                    ", unSportActivityAward='" + unSportActivityAward + '\'' +
                    '}';
        }
    }

    public static class PlatwithdrawInfo {
        public String money;
        public String user_regtime;
        public String bankname;
        public String province;
        public String bankcity;
        public String truename;
        public String bankno;
        public String cardid;
        public String user_bindbanktime;
        public String drawal_type;
        public int channel_typenum;
        public int earnest_money;
        public String arrive;
        public String handing_fee;
        public int fee_ratio;

        /**
         "money":100,
         "user_regtime":"2023-12-19 13:52:12",
         "bankname":"民生银行",
         "province":"内蒙古",
         "bankcity":"乌兰察布盟",
         "truename":"马总",
         "bankno":"************4010",
         "cardid":"1283196",
         "user_bindbanktime":"2024-01-20 13:43:36",
         "drawal_type":"1",
         "channel_typenum":1,
         "earnest_money":0,
         "arrive":100,
         "handing_fee":"0.00",
         "fee_ratio":0
         */

    }
}
