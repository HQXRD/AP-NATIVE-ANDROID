package com.xtree.mine.vo;

/**
 * 银行卡提款确认提交后返回的Model
 */
public class PlatWithdrawConfirmVo {
    public User user ;
    public String msg_detail ;//"账户提款申请成功" "请刷新后重试！
    public int  msg_type;//2 账户提款申请成功
    public static class  User
    {
        /*
         "parentid":"2723540",
        "usertype":"1",
        "iscreditaccount":"0",
        "userrank":"0",
        "availablebalance":"999731.0000",
        "preinfo":"",
        "nickname":"tst033",
        "messages":"1"
         */
        public String parentid ;
        public String usertype ;
        public double iscreditaccount ;
        public String userrank ;
        public String relavailablebalance ;
        public String availablebalance ;
        public String preinfo ;
        public String nickname ;
        public String message ;
    }
}
