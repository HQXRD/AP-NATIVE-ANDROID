package com.xtree.recharge.vo;

import java.util.ArrayList;
import java.util.Arrays;

public class RechargeVo {

    public String bid; // "122",
    public String paycode; // "hqppayzfb3",
    public String type; // "ZFBONL",
    public String typename; // "支付宝在线支付",
    public String title; // "聚合支付宝",
    public String loadmax; // "20000",
    public String loadmin; // "50",
    public int firemark; // 0,
    public int randturnauto; // 0-默认; 1-金额后面会增加随机的小数
    //public boolean recharge_auto_minus_turn_on; // false,
    //public String recharge_auto_minus_max; // "",
    //public String recharge_auto_minus_min; // "",
    //public boolean rechargedecimal_status; // false,
    public int sortnum; // 10,
    public int recommend; // 0,
    public boolean view_bank_card; // false,
    //public int recharge_pattern; // 2,
    public boolean phone_fillin_name; // true,
    //public int ptype; // 1,
    public Object user_bank_info; // 格式不固定 [], {"1452165":"中国工商银行--***************8487"}
    public ArrayList<BankCardVo> userBankList = new ArrayList<>(); // 自己加的,方便显示银行卡列表
    public boolean realchannel_status; // true,
    public boolean isusdt; // false, true
    public String udtType; // null, "TRC20"
    public String usdtrate; // "", "7.32"
    public boolean depositfee_disabled; // false,
    public String depositfee_rate; // "0.00", "5.00"
    //public String isdisabled; // "0",
    //public boolean isrechgetime; // true,
    public String starttime; // 1701619200,
    public String endtime; // 1701705540, false
    //public String addcredittimes; // "10000",
    //public int addcreditminsec; // 0,
    public String PayCardId; // "2350",
    //public boolean namountdecimal; // false,
    //public boolean nalipayname; // false,
    public boolean fixedamount_channelshow; // false, true
    public String[] fixedamount_info; // [], ["100","200","300","500","1000","2000","3000","5000","10000","20000","30000","50000"]
    public boolean phone_needbind; // true,
    //public boolean showfee; // false,
    //public int fee; // 0,
    //public boolean op_series; // false,
    public boolean op_thiriframe_use; // false,
    public boolean op_thiriframe_status; // false,
    public String op_thiriframe_msg; // "",
    public String op_thiriframe_url; // "",
    public boolean recharge_json_channel; // false,
    //public int recharge_json_count_once; // 5,
    //public int recharge_json_day_notsucc; // 3,
    //public boolean isrecharge_additional; // false,
    public String low_rate_hint; // "1", 当前渠道【{detail.title}】充值到账成功率较低，为了保证快速到账，请使用以下渠道进行充值或联系客服进行处理！
    public String accountname; // "",
    public int tips_recommended; // 0,

    public String toInfo() {
        return "RechargeVo { " +
                "bid='" + bid + '\'' +
                ", paycode='" + paycode + '\'' +
                ", title='" + title + '\'' +
                ", type='" + type + '\'' +
                ", typename='" + typename + '\'' +
                ", PayCardId='" + PayCardId + '\'' +
                ", loadmin='" + loadmin + '\'' +
                ", loadmax='" + loadmax + '\'' +
                ", randturnauto=" + randturnauto +
                ", userBankList=" + Arrays.toString(userBankList.toArray()) +
                ", fixedamount_channelshow=" + fixedamount_channelshow +
                ", fixedamount_info=" + Arrays.toString(fixedamount_info) +
                '}';
    }

    @Override
    public String toString() {
        return "RechargeVo{" +
                "bid='" + bid + '\'' +
                ", paycode='" + paycode + '\'' +
                ", title='" + title + '\'' +
                ", loadmin='" + loadmin + '\'' +
                ", loadmax='" + loadmax + '\'' +
                ", type='" + type + '\'' +
                ", typename='" + typename + '\'' +
                ", firemark=" + firemark +
                ", randturnauto=" + randturnauto +
                ", sortnum=" + sortnum +
                ", recommend=" + recommend +
                ", view_bank_card=" + view_bank_card +
                ", phone_fillin_name=" + phone_fillin_name +
                ", user_bank_info=" + user_bank_info +
                //", userBankList=" + userBankList +
                ", realchannel_status=" + realchannel_status +
                ", isusdt=" + isusdt +
                ", udtType='" + udtType + '\'' +
                ", usdtrate='" + usdtrate + '\'' +
                ", depositfee_disabled=" + depositfee_disabled +
                ", depositfee_rate='" + depositfee_rate + '\'' +
                ", starttime='" + starttime + '\'' +
                ", endtime='" + endtime + '\'' +
                ", PayCardId='" + PayCardId + '\'' +
                ", fixedamount_channelshow=" + fixedamount_channelshow +
                ", fixedamount_info=" + Arrays.toString(fixedamount_info) +
                ", phone_needbind=" + phone_needbind +
                ", op_thiriframe_use=" + op_thiriframe_use +
                ", op_thiriframe_status=" + op_thiriframe_status +
                ", op_thiriframe_msg='" + op_thiriframe_msg + '\'' +
                ", op_thiriframe_url='" + op_thiriframe_url + '\'' +
                ", low_rate_hint='" + low_rate_hint + '\'' +
                ", accountname='" + accountname + '\'' +
                ", tips_recommended=" + tips_recommended +
                '}';
    }
}
