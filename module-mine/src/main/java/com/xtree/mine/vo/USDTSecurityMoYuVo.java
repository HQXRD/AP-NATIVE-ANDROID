package com.xtree.mine.vo;
/*魔域 USDT 提款上一步 返回model*/
public class USDTSecurityMoYuVo {

   /* "usdt_card":"******r123",
            "usdtid":3209,
            "usdt_type":"ERC20_USDT",
            "exchangerate":"7.2",*/

    /*"nextcontroller": "security",
"nextaction": "platwithdraw",
"ur_here": "资金密码检查",
异常情况 需要弹出资金密码检查
*/
    public int msg_type; // 1 (异常/失败的时候) 1,2-失败, 3-成功
    public String message; // "页面超时！请重试。",
    public String nextcontroller ;
    public String nextaction ;
    public String  ur_here ;

    public String  usdt_card ;//提款地址
    public String usdtid ;
    public String  usdt_type ;//提款类型
    public String exchangerate;//汇率

    /* "drawal_type":"2",
             "channel_typenum":"1",
             "channel_child":null,
             "earnest_money_pl":0,
             "earnest_money":0,
             "check":true,
             "availablebalance":997851,
             "ur_here":"账户提款确认",
             "ourfee":"0",
             "name":"usdt",
             "usdtfee":"0.0"*/
    public String drawal_type ;
    public String channel_typenum ;
    public String channel_child ;
    public String earnest_money_pl ;
    public String earnest_money ;
    public boolean check ;
    public String availablebalance ;

    public String ourfee ;
    public String name ;
    public Security datas ;
    public  class  Security
    {
        public String money ;
        public String user_regtime ;
        public String drawal_type ;
        public String channel_typenum ;
        public String earnest_money ;
        public String arrive ;
        public String handing_fee ;
        public String fee_ratio ;
        public String plot_id ;
         /*"money":7,
                 "user_regtime":"2023-12-19 13:52:12",
                 "drawal_type":"2",
                 "channel_typenum":"1",
                 "earnest_money":0,
                 "arrive":5.6,
                 "handing_fee":"1.40",
                 "fee_ratio":"20.00",
                 "plot_id":"54"*/
    }

    public User user ;

    public class  User
    {
        public String userid ;
        public String username ;
        public String availablebalance ; //可提款金额
        public String registertime ;
        public String relavailablebalance ;
        public String formula ;
        public String cafAvailableBalance ;
        public String unSportActivityAward ;
    }
}
