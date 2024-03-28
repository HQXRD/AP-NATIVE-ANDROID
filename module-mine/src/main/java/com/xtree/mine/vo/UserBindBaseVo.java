package com.xtree.mine.vo;

import java.util.List;

public class UserBindBaseVo<T> {
    public String sSystemImagesAndCssPath; // "",
    public String customer_service_link; // "https://vscaue.bx64fzsm.com/chatwindow.aspx?siteId=60003843&planId=39e9e819-e951-4e32-a173-ee9afd793ab8,https://vscaue.tsyan4oo.com/chatwindow.aspx?siteId=60003843&planId=39e9e819-e951-4e32-a173-ee9afd793ab8",
    public String pt_download_pc; // "http://cdn.vbet.club/happyslots/d/setupglx.exe",

    //public Object user; // {
    //public String parentid; // "2723540",
    //public String usertype; // "1",
    //public String iscreditaccount; // "0",
    //public String userrank; // "0",
    //public String availablebalance; // "0.0000",
    //public String preinfo; // "",
    //public String nickname; // "tst033",
    //public String messages; // "2"

    //public String push_service_status; // 1,
    //public String push_service_module; // "{"push_issuetime; //"1","push_issuecode; //"1","push_notice; //"0","push_usermessage; //"1","push_userbalance; //"1","push_userwonprize; //"1"}",
    //public String push_server_host; // "apre3push.oxldkm.com",
    //public String pub_channel_token; // "33c76f70561aa6753c8ff6549d3b296d",
    //public String pub_channel_id; // "3bb42e1dd900147b89ffa2",
    //public String user_channel_id; // "224025c785cda81f692753",
    //public String topprizes_herolist_enabled; // "1",
    //public String topprizes_publicity_enabled; // "1",
    //public String topprizes_wintips_enabled; // "1",
    //public String desK; // "WN9TNiq6zcV5F7wl5Qy4LtZGZ5KsXvWP",
    public BindCountsVo my_bind_counts;
    public String ur_here; // "卡号绑定列表",
    public String check; // "c2f6e3b0f5a3fe719acba28643a7d83b",
    public String has_phone; // 1,
    public String phone_number; // "132*****233",
    public String judgeRes; // 0,
    public String smstype; // 4,
    public List<T> banklist; // [

    public String num; // "5",
    public String isDisabled; // "0",
    public String binded; // "2",
    //public String sSystemMessagesByTom; // "SQL Info: 0 in 0 Sec, Memory Info: 4.445 MB, System Spend : 0.332213 Sec "

    // 失败/不通过,返回错误信息 msg_detail,msg_type,message
    //public String msg_detail;
    public int msg_type; // 1 (异常/失败的时候) 1,2-失败, 3-成功
    public String message; // "页面超时！请重试。",

    public int status; // 1 查询列表,列表为空时
    public String msg;
    public Object data;

}
