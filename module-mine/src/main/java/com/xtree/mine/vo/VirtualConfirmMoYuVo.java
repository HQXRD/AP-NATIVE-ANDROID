package com.xtree.mine.vo;

public class VirtualConfirmMoYuVo {
    /*"nextcontroller": "security",
"nextaction": "platwithdraw",
"ur_here": "资金密码检查",
异常情况 需要弹出资金密码检查
*/
    public String nextcontroller ;
    public String nextaction ;
    public String  ur_here ;
    @Override
    public String toString() {
        return "VirtualConfirmVo{" +
                "user=" + user +
                ", msg_detail='" + msg_detail + '\'' +
                ", msg_type='" + msg_type + '\'' +
                '}';
    }

    public User user;
    public String  msg_detail ;
    public String  msg_type;
    /*"msg_detail": "账户提款申请成功",
            "msg_type": 2,

            "msg_detail": "请刷新后重试！",
   "msg_type": 3,*/
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
