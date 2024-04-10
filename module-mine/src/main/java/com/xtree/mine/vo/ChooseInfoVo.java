package com.xtree.mine.vo;

import java.util.ArrayList;

/**
 * 可选择的提款方式
 */
public class ChooseInfoVo {
    public int networkStatus;//1 网络链接超时 ；2 网络链接异常 ；0 网络链接正常

    @Override
    public String toString() {
        return "ChooseInfoVo{" +
                "check=" + check +
                ", msg_detail='" + msg_detail + '\'' +
                ", msg_type='" + msg_type + '\'' +
                ", error='" + error + '\'' +
                ", message='" + message + '\'' +
                ", bankcardstatus_okpay=" + bankcardstatus_okpay +
                ", bankcardstatus_gopay=" + bankcardstatus_gopay +
                ", bankcardstatus_gobao=" + bankcardstatus_gobao +
                ", bankcardstatus_mpay=" + bankcardstatus_mpay +
                ", bankcardstatus_hiwallet=" + bankcardstatus_hiwallet +
                ", bankcardstatus_topay=" + bankcardstatus_topay +
                ", bankcardstatus_ebpay=" + bankcardstatus_ebpay +
                ", bankcardstatus_rmb=" + bankcardstatus_rmb +
                ", bankcardstatus_usdt=" + bankcardstatus_usdt +
                ", usdtchanneluse=" + usdtchanneluse +
                ", usdtchanneluse_msg='" + usdtchanneluse_msg + '\'' +
                ", ebpaychanneluse=" + ebpaychanneluse +
                ", ebpaychanneluse_msg='" + ebpaychanneluse_msg + '\'' +
                ", topaychanneluse=" + topaychanneluse +
                ", topaychanneluse_msg='" + topaychanneluse_msg + '\'' +
                ", hiwalletchanneluse=" + hiwalletchanneluse +
                ", hiwalletchanneluse_msg='" + hiwalletchanneluse_msg + '\'' +
                ", mpaychanneluse=" + mpaychanneluse +
                ", mpaychanneluse_msg='" + mpaychanneluse_msg + '\'' +
                ", gobaochanneluse=" + gobaochanneluse +
                ", gobaochanneluse_msg='" + gobaochanneluse_msg + '\'' +
                ", gopaychanneluse=" + gopaychanneluse +
                ", gopaychanneluse_msg='" + gopaychanneluse_msg + '\'' +
                ", okpaychanneluse=" + okpaychanneluse +
                ", okpaychanneluse_msg='" + okpaychanneluse_msg + '\'' +
                ", bankchanneluse=" + bankchanneluse +
                ", bankchanneluse_msg='" + bankchanneluse_msg + '\'' +
                /*", user=" + user +
                ", usdtInfo=" + usdtInfo +
                ", ebpayInfo=" + ebpayInfo +
                ", topayInfo=" + topayInfo +
                ", hiwalletInfo=" + hiwalletInfo +
                ", okpayInfo=" + okpayInfo +
                ", wdChannelList=" + wdChannelList +*/
                '}';
    }

    /*  "bankcardstatus_rmb":true,
                "bankcardstatus_usdt":true,
                "bankcardstatus_ebpay":true,
                "bankcardstatus_topay":true,
                "bankcardstatus_hiwallet":true,
                "bankcardstatus_mpay":false,
                "bankcardstatus_gobao":true,
                "bankcardstatus_gopay":true,
                "bankcardstatus_okpay":false,*/
    public boolean check;
    public String msg_detail; // "抱歉，您的有效流水不足，仍需要 1008.54 才可提款",
    public String msg_type;//2
    public String error;//"error"
    public String message;
    public boolean bankcardstatus_okpay;
    public boolean bankcardstatus_gopay;
    public boolean bankcardstatus_gobao;
    public boolean bankcardstatus_mpay;
    public boolean bankcardstatus_hiwallet;
    public boolean bankcardstatus_topay;
    public boolean bankcardstatus_ebpay;
    public boolean bankcardstatus_rmb;
    public boolean bankcardstatus_usdt;

    public int onepaywxchanneluse;
    public boolean bankcardstatus_onepaywx;//微信标志位
    public String onepaywxchanneluse_msg;//微信提示语
    public int onepayzfbchanneluse;
    public boolean bankcardstatus_onepayzfb;//支付宝标志位
    public String onepayzfbchanneluse_msg;//支付宝提示语

    public int usdtchanneluse;
    public String usdtchanneluse_msg;
    public int ebpaychanneluse;
    public String ebpaychanneluse_msg;
    public int topaychanneluse;
    public String topaychanneluse_msg;
    public int hiwalletchanneluse;
    public String hiwalletchanneluse_msg;
    public int mpaychanneluse;
    public String mpaychanneluse_msg;
    public int gobaochanneluse;
    public String gobaochanneluse_msg;
    public int gopaychanneluse;
    public String gopaychanneluse_msg;
    public int okpaychanneluse;
    public String okpaychanneluse_msg;
    public int bankchanneluse;
    public String bankchanneluse_msg;
    /*
    "usdtchanneluse":0,
    "usdtchanneluse_msg":"首次提款仅可使用银行卡方式提款！",
    "ebpaychanneluse":0,
    "ebpaychanneluse_msg":"首次提款仅可使用银行卡方式提款！",
    "topaychanneluse":0,
    "topaychanneluse_msg":"首次提款仅可使用银行卡方式提款！",
    "hiwalletchanneluse":0,
    "hiwalletchanneluse_msg":"首次提款仅可使用银行卡方式提款！",
    "mpaychanneluse":0,
    "mpaychanneluse_msg":"首次提款仅可使用银行卡方式提款！",
    "gobaochanneluse":0,
    "gobaochanneluse_msg":"首次提款仅可使用银行卡方式提款！",
    "gopaychanneluse":0,
    "gopaychanneluse_msg":"首次提款仅可使用银行卡方式提款！",
    "okpaychanneluse":0,
    "okpaychanneluse_msg":"首次提款仅可使用银行卡方式提款！",
    "bankchanneluse":1,
    "bankchanneluse_msg":"",
     */
   /* ;
    public Info usdtInfo ,ebpayInfo ,topayInfo ,hiwalletInfo,okpayInfo;
    */
    public Info usdtInfo;
    public User user;
    /**
     * 可使用支付渠道list
     */
    public ArrayList<ChannelInfo> wdChannelList;

    /**
     * 可选择的支付渠道信息
     */
    public static class ChannelInfo {
        public String bindType;// 绑定type 向绑定页面传值
        public boolean isBind;//是否绑定标志位 true:绑定； false:未绑定
        public int channeluse;//支付渠道可否跳转状态 1可以跳转下一个页面； 0：不可跳转下一个页面
        public String channeluseMessage = "";//支付渠道不可跳转下一个页面弹出的消息
        /*"id":"3",
            "type":"2",
            "title":"USDT提款",
            "method_sort":"0",
            "recommend":"1",
            "configkey":"usdt",
            "dispay_title":"USDT提款2",
            "channel_sort":"0",
            "channel_name":"USDT提款",
            "utime":"2023-12-29 17:30:50",
            "agent_rate_cost_type":"2",
            "agent_rate_cost":"11.00",
            "userid":"2721239",
            "min_limit":"0.00",
            "max_limit":"0.00"*/
        public String id;
        public String type;
        public String title;
        public String method_sort;
        public String recommend;
        public String configkey;
        public String dispay_title;
        public String channel_sort;
        public String channel_name;
        public String utime;
        public String agent_rate_cost_type;
        public String agent_rate_cost;
        public String userid;
        public String min_limit;
        public String max_limit;
        public boolean flag;//是否点击

        @Override
        public String toString() {
            return "ChannelInfo{" +
                    "bindType='" + bindType + '\'' +
                    ", channeluse=" + channeluse +
                    ", channeluseMessage='" + channeluseMessage + '\'' +
                    ", id='" + id + '\'' +
                    ", type='" + type + '\'' +
                    ", title='" + title + '\'' +
                    ", method_sort='" + method_sort + '\'' +
                    ", recommend='" + recommend + '\'' +
                    ", configkey='" + configkey + '\'' +
                    ", dispay_title='" + dispay_title + '\'' +
                    ", channel_sort='" + channel_sort + '\'' +
                    ", channel_name='" + channel_name + '\'' +
                    ", utime='" + utime + '\'' +
                    ", agent_rate_cost_type='" + agent_rate_cost_type + '\'' +
                    ", agent_rate_cost='" + agent_rate_cost + '\'' +
                    ", userid='" + userid + '\'' +
                    ", min_limit='" + min_limit + '\'' +
                    ", max_limit='" + max_limit + '\'' +
                    ", flag=" + flag +
                    '}';
        }
    }

    public static class User {
        /*
        *  "parentid":"2723540",
        "usertype":"1",
        "iscreditaccount":"0",
        "userrank":"0",
        "availablebalance":"0.0000",
        "preinfo":"",
        "nickname":"tst033",
        "messages":"2"
    */

        @Override
        public String toString() {
            return "User{" +
                    "parentid='" + parentid + '\'' +
                    ", usertype='" + usertype + '\'' +
                    ", iscreditaccount='" + iscreditaccount + '\'' +
                    ", userrank='" + userrank + '\'' +
                    ", availablebalance='" + availablebalance + '\'' +
                    ", preinfo='" + preinfo + '\'' +
                    ", nickname='" + nickname + '\'' +
                    ", messages='" + messages + '\'' +
                    '}';
        }

        public String parentid;
        public String usertype;
        public String iscreditaccount;
        public String userrank;
        public String availablebalance;
        public String preinfo;
        public String nickname;
        public String messages;

        public User() {

        }
    }

    /**
     * 虚拟币信息
     */
    public static class Info {
        @Override
        public String toString() {
            return "Info{" +
                    "status=" + status +
                    ", nedusdt=" + nedusdt +
                    /*   ", nocard=" + nocard +*/
                    ", quota=" + quota +
                    ", relquota='" + relquota + '\'' +
                    ", formula='" + formula + '\'' +
                    ", error_code='" + error_code + '\'' +
                    ", intype='" + intype + '\'' +
                    ", vip_level=" + vip_level +
                    ", vip_virtual_currency_quota='" + vip_virtual_currency_quota + '\'' +
                    ", blebalance=" + blebalance +
                    '}';
        }

        /*"status":false,
                "nedusdt":true,
                "nocard":false,
                "quota":0,
                "relquota":"16124.0000",
                "formula":"(0)-(0+0+0+0-0-0+0.0000)+ (16124.0000)",
                "error_code":30532,
                "intype":"ebpay",
                "vip_level":0,
                "vip_virtual_currency_quota":"0.0000",
                "blebalance":0*/
        public boolean status;
        public boolean nedusdt;
        //public boolean nocard;
        public String quota;
        public String relquota;
        public String formula;
        public String error_code;
        public String intype;
        public String vip_level;
        public String vip_virtual_currency_quota;
        public String blebalance;

        public Info() {

        }

    }

    public ChooseInfoVo() {

    }

}
