package com.xtree.mine.vo;

/**
 * 填写好以后跳转到确认页用的,后台返回的
 */
public class UserBankConfirmVo extends UserBindBaseVo {

    // 卡检验 不通过,返回错误信息
    //public String msg_detail; // "您提交的银行卡号校验不正确，请核对后重新提交！","您的开户名与最近绑定的开户名存在差异","页面超时！请重试。"
    //public int msg_type; // 1 (失败的时候)
    //public String message; // "您提交的银行卡号校验不正确，请核对后重新提交！",
    //
    //// 卡检验通过,返回确认信息
    //public String ur_here; // "卡号绑定信息确认",

    public String nickname; // "tst033",
    public String bank_id; // 31,
    public String bank; // "广发银行",
    public String province_id; // 3,
    public String province; // "天津",
    public String city_id; // 52,
    public String city; // "天津",
    public String branch; // "天津支行",
    public String account_name; // "马总",
    public String account; // "6226983174236770",
    public String oldid; // null,
    public String flag; // "confirm",
    public String sSystemMessagesByTom; // "SQL Info: 0 in 0 Sec, Memory Info: 4.153 MB, System Spend : 0.260394 Sec ",
    public String checkcode; // "b9034873009a759fe49d5ba729d22884",
    public String checkcode_timeout; // null

    //public String ur_here; // "删除绑定检查",
    public String id; // "12***63", // 删除绑卡用的
    //public String flag; // "lock",
    public String action; // "deluserbank",

}
