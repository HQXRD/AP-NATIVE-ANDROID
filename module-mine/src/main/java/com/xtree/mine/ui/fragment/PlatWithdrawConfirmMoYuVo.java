package com.xtree.mine.ui.fragment;

import com.xtree.mine.vo.PlatWithdrawConfirmVo;

import me.xtree.mvvmhabit.http.BaseResponse2;
/*【魔域】 银行卡提现 确认订单 */
public class PlatWithdrawConfirmMoYuVo extends BaseResponse2 {

    public User user;
  /*  public String msg_detail;//"账户提款申请成功" "请刷新后重试！
    public int msg_type;//2 账户提款申请成功*/

    public static class User {
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
        public String parentid;
        public String usertype;
        public double iscreditaccount;
        public String userrank;
        public String availablebalance;
        public String preinfo;
        public String nickname;
        public String message;
    }
}
