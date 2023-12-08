package com.xtree.recharge.vo;

public class RechargeVo {

    public String bid; // "122",
    public String paycode; // "hqppayzfb3",
    public String type; // "ZFBONL",
    public String typename; // "支付宝在线支付",
    public String title; // "聚合支付宝",
    public String loadmax; // "20000",
    public String loadmin; // "50",
    public int firemark; // 0,
    //public int randturnauto; // 0,
    //public boolean recharge_auto_minus_turn_on; // false,
    //public String recharge_auto_minus_max; // "",
    //public String recharge_auto_minus_min; // "",
    //public boolean rechargedecimal_status; // false,
    public int sortnum; // 10,
    public int recommend; // 0,
    //public boolean view_bank_card; // false,
    //public int recharge_pattern; // 2,
    public boolean phone_fillin_name; // true,
    //public int ptype; // 1,
    //public String[] user_bank_info; // [],
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
    //public boolean fixedamount_channelshow; // false,
    //public String[] fixedamount_info; // [],
    public boolean phone_needbind; // true,
    //public boolean showfee; // false,
    //public int fee; // 0,
    //public boolean op_series; // false,
    public boolean op_thiriframe_use; // false,
    public boolean op_thiriframe_status; // false,
    public String op_thiriframe_msg; // "",
    public String op_thiriframe_url; // "",
    //public boolean recharge_json_channel; // false,
    //public int recharge_json_count_once; // 5,
    //public int recharge_json_day_notsucc; // 3,
    //public boolean isrecharge_additional; // false,
    //public String low_rate_hint; // "1",
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
                '}';
    }

    public String toInfo2() {
        return "RechargeVo { " +
                "bid='" + bid + '\'' +
                ", paycode='" + paycode + '\'' +
                ", title='" + title + '\'' +
                ", type='" + type + '\'' +
                ", typename='" + typename + '\'' +
                ", loadmax='" + loadmax + '\'' +
                ", loadmin='" + loadmin + '\'' +
                ", firemark=" + firemark +
                ", sortnum=" + sortnum +
                ", recommend=" + recommend +
                ", starttime='" + starttime + '\'' +
                ", endtime='" + endtime + '\'' +
                ", PayCardId='" + PayCardId + '\'' +
                ", tips_recommended=" + tips_recommended +
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
                ", firemark=" + firemark +
                ", sortnum=" + sortnum +
                ", recommend=" + recommend +
                ", type='" + type + '\'' +
                ", typename='" + typename + '\'' +
                ", starttime='" + starttime + '\'' +
                ", endtime='" + endtime + '\'' +
                ", PayCardId='" + PayCardId + '\'' +
                ", phone_needbind=" + phone_needbind +
                ", op_thiriframe_use=" + op_thiriframe_use +
                ", op_thiriframe_status=" + op_thiriframe_status +
                ", op_thiriframe_msg='" + op_thiriframe_msg + '\'' +
                ", op_thiriframe_url='" + op_thiriframe_url + '\'' +
                ", tips_recommended=" + tips_recommended +
                '}';
    }

/*@Override
    public String toString() {
        return "RechargeVo{" +
                "bid='" + bid + '\'' +
                ", paycode='" + paycode + '\'' +
                ", type='" + type + '\'' +
                ", typename='" + typename + '\'' +
                ", title='" + title + '\'' +
                ", loadmax='" + loadmax + '\'' +
                ", loadmin='" + loadmin + '\'' +
                ", firemark=" + firemark +
                ", randturnauto=" + randturnauto +
                ", recharge_auto_minus_turn_on=" + recharge_auto_minus_turn_on +
                ", recharge_auto_minus_max='" + recharge_auto_minus_max + '\'' +
                ", recharge_auto_minus_min='" + recharge_auto_minus_min + '\'' +
                ", rechargedecimal_status=" + rechargedecimal_status +
                ", sortnum=" + sortnum +
                ", recommend=" + recommend +
                ", view_bank_card=" + view_bank_card +
                ", recharge_pattern=" + recharge_pattern +
                ", phone_fillin_name=" + phone_fillin_name +
                ", ptype=" + ptype +
                ", user_bank_info=" + Arrays.toString(user_bank_info) +
                ", realchannel_status=" + realchannel_status +
                ", isusdt=" + isusdt +
                ", udtType='" + udtType + '\'' +
                ", usdtrate='" + usdtrate + '\'' +
                ", depositfee_disabled=" + depositfee_disabled +
                ", depositfee_rate='" + depositfee_rate + '\'' +
                ", isdisabled='" + isdisabled + '\'' +
                ", isrechgetime=" + isrechgetime +
                ", starttime=" + starttime +
                ", endtime=" + endtime +
                ", addcredittimes='" + addcredittimes + '\'' +
                ", addcreditminsec=" + addcreditminsec +
                ", PayCardId='" + PayCardId + '\'' +
                ", namountdecimal=" + namountdecimal +
                ", nalipayname=" + nalipayname +
                ", fixedamount_channelshow=" + fixedamount_channelshow +
                ", fixedamount_info=" + Arrays.toString(fixedamount_info) +
                ", phone_needbind=" + phone_needbind +
                ", showfee=" + showfee +
                ", fee=" + fee +
                ", op_series=" + op_series +
                ", op_thiriframe_use=" + op_thiriframe_use +
                ", op_thiriframe_status=" + op_thiriframe_status +
                ", op_thiriframe_msg='" + op_thiriframe_msg + '\'' +
                ", op_thiriframe_url='" + op_thiriframe_url + '\'' +
                ", recharge_json_channel=" + recharge_json_channel +
                ", recharge_json_count_once=" + recharge_json_count_once +
                ", recharge_json_day_notsucc=" + recharge_json_day_notsucc +
                ", isrecharge_additional=" + isrecharge_additional +
                ", low_rate_hint='" + low_rate_hint + '\'' +
                ", accountname='" + accountname + '\'' +
                ", tips_recommended=" + tips_recommended +
                '}';
    }*/
}
