package com.xtree.mine.vo;
/* 魔域 USDT提款确认后返回的model*/
public class USDTConfirmMoYuVo {
    /*"nextcontroller": "security",
"nextaction": "platwithdraw",
"ur_here": "资金密码检查",
异常情况 需要弹出资金密码检查
*/
    public String nextcontroller ;
    public String nextaction ;
    public String  ur_here ;
    public User user;
    public String  msg_detail ;
    public String  msg_type;
    public String message ;
    /*"msg_detail": "账户提款申请成功",
            "msg_type": 2,*/
    public String error ;//提款失败后才有该字段
    public class  User
    {
        public String parentid ;
        public String usertype ;
        public String iscreditaccount ; //可提款金额
        public String userrank ;
        public String availablebalance ;
        public String preinfo ;
        public String nickname ;
        public String messages ;
        /*"parentid": "2723540",
                "usertype": "1",
                "iscreditaccount": "0",
                "userrank": "0",
                "availablebalance": "997858.0000",
                "preinfo": "",
                "nickname": "tst033",
                "messages": "2"*/
    }
}
