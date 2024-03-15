package com.xtree.mine.vo;

import me.xtree.mvvmhabit.http.BaseResponse2;

/**
 * [魔域] 提款确认信息页面信息
 */
public class PlatWithdrawMoYuVo extends BaseResponse2 {

    //public String sTarget ;// "sTarget": "top",
    //public String status ;// "status": 20106,
    //public String sMsg ;// "sMsg": "由于您长时间未操作，请重新登录",

    public int  networkStatus ;//1 网络链接超时 ；2 网络链接异常 ；0 网络链接正常

    /*"nextcontroller": "security",
	"nextaction": "platwithdraw",
	"ur_here": "资金密码检查",
	异常情况 需要弹出资金密码检查
	*/
    public String nextcontroller ;
    public String nextaction ;
    //"ur_here": "账户提款确认", 业务正常
    // 	"ur_here": "资金密码检查", 弹起资金密码输入
    public String  ur_here ;
    public String check ;//业务正常返回的checkCode 用于下一个提款确认接口使用
    public String channel_typenum;
    public String channel_child;
    //public String message ;
    public PlatWithdrawInfo datas;
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

    public static class PlatWithdrawInfo {
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
        public String arrive; //真实提款金额
        public String handing_fee;
        public int fee_ratio;

        @Override
        public String toString() {
            return "PlatWithdrawInfo{" +
                    "money='" + money + '\'' +
                    ", user_regtime='" + user_regtime + '\'' +
                    ", bankname='" + bankname + '\'' +
                    ", province='" + province + '\'' +
                    ", bankcity='" + bankcity + '\'' +
                    ", truename='" + truename + '\'' +
                    ", bankno='" + bankno + '\'' +
                    ", cardid='" + cardid + '\'' +
                    ", user_bindbanktime='" + user_bindbanktime + '\'' +
                    ", drawal_type='" + drawal_type + '\'' +
                    ", channel_typenum=" + channel_typenum +
                    ", earnest_money=" + earnest_money +
                    ", arrive='" + arrive + '\'' +
                    ", handing_fee='" + handing_fee + '\'' +
                    ", fee_ratio=" + fee_ratio +
                    '}';
        }
/**
         "money": 100,
         "user_regtime": "2024-02-21 20:33:23",
         "bankname": "招商银行",
         "province": "广东",
         "bankcity": "惠州",
         "truename": "李虎",
         "bankno": "************3596",
         "cardid": "1282967",
         "user_bindbanktime": "2024-02-29 16:42:29",
         "drawal_type": "1",
         "channel_typenum": 1,
         "earnest_money": 0,
         "arrive": 100,
         "handing_fee": "0.00",
         "fee_ratio": 0
         */

    }
}
