package com.xtree.recharge.vo;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class RechargeVo {

    //{"status":0,"msg":"","data":{"bid":"176","paycode":"onepayfix3","type":"OTHER","typename":"其他","title":"小额网银渠道","loadmax":"30000","loadmin":"100","firemark":0,"randturnauto":0,"recharge_auto_minus_turn_on":false,"recharge_auto_minus_max":"","recharge_auto_minus_min":"","rechargedecimal_status":false,"sortnum":1,"recommend":0,"view_bank_card":true,"recharge_pattern":3,"phone_fillin_name":true,"ptype":1,"user_bank_info":{"1283079":"交通银行--*************0040"},"realchannel_status":true,"isusdt":false,"udtType":null,"usdtrate":"","depositfee_disabled":false,"depositfee_rate":"0.00","isdisabled":"0","isrechgetime":true,"starttime":1716825600,"endtime":1716911940,"addcredittimes":"10000","addcreditminsec":30,"PayCardId":"2116","namountdecimal":false,"nalipayname":false,"fixedamount_channelshow":false,"fixedamount_info":[],"phone_needbind":false,"showfee":true,"fee":"3","op_series":true,"op_thiriframe_use":true,"op_thiriframe_status":true,"op_thiriframe_msg":"","op_thiriframe_url":"https://pre-infogen.1-pay.co/op1/auction/bankcard/payment/eyJpdiI6ImYrRUpqWEhCenhsRDRjcTljVnRWYXc9PSIsInZhbHVlIjoiZ0RlbVNsZFhnaDRmQjV1RnIxVWZRVW14UGpyQmVRNzY5ZU5FU2hDTVBTUW1WMkVHY1g5dmxETU5xNjMrcjlOalp1MGFHNXdGZWZ3aHdQUGtxMzdGNzU3LzM2d2F0MnZOTGMvM1YvTk1HQ2h4c1NOR21FWHNMVDdaOWV5emUza3p4a2tGbnZoTE05WGo0V1hXVFAwQXFndk9wamh0QzYvWkRSQnZvUi9kYTYzbDE0MnFVKzdtOFBqUzlvR2lPSVE0OU93cUVWMVRmejd6SHU5Q2tMQUdzUzFYNVJ3VU04TWhYcVZqRWFaMExmMHBxNG5aVStnc3A2RDZrenYwTnpONTJZNU54ZDJEWHN3dFlPYUcrTjh1RUp3dGNkQUlKNTVaYWRVRnRKN2dTVzZxbG1xczlHYnRNSzZMUVhGcXAvc1dQN09zOW01MDBXbkhCSWx6TDArdHBsVHhkdkNRL21PWjRyb3NTSTFOZTA2SHJXR3kzdXA2ankrOGVEOVhObTdGNjFMV0pjaFpoWGFBWWo3N2c4TUMwSVVKZkhPMU91MVdMRmNwbnhFYlFLZFdWdkdsSEdnS2MwTEJSem0vUVMveWtIbXZVYUl4YWVrTWdodzRtZEVPY3FUaHNpMzdpWUNHMTJYU01iNVN5NEF3QTdMbFFOLy9zQ0toQWtEL0pjd094Qk5jaEs0bEswSUI1UmQ1aTFCbkZEZFV5VVJpMDJ6NHpPWjJVY2JkMjVyUG5Ha0RDaCtldW5IZ3JiaVdQN3laNzV6WkdRQWViUFpPV2NneVZSdUdwWXlhcERGMFZJd0tMRktTamFvaGwvUlZwN3pnTkVxMEE4cVAweU04dUdtQ2dFeDBFMERtK0E0eEtONmZqZGxwOUZQVDFWY3BMM0dwZEtLczYzbWZNL0FENk9DSTF6dXg5UCszWGlDRjhvc3JQOW84UXpxS2I1QlpCc1B6ZnpObVJsRU9YSnNzbnlvaTFVa2ZJTklRT1U2MUZHMUZNMlVNVjN0S24wTHhoMlFtIiwibWFjIjoiM2EzMDk2OWYxNWJiMDBiZTNhZWJhZTQwYzkwNzFiN2JjYTRiNzU0YzBiYjEyZjlhNjg4ZmQ5ZTdhMzZiYzRlZiIsInRhZyI6IiJ9","op_amount_list":[100,200,300,400,500,600,700,800,900,1000,1100,1200,1300,1400,1500,1600,2000,3000,5000],"op_bank_list":{"top":[{"bank_code":"ZJCZ","bank_name":"浙江稠州商业"},{"bank_code":"ZYB","bank_name":"中原银行"},{"bank_code":"ZJRC","bank_name":"浙江农信"},{"bank_code":"ZJTL","bank_name":"泰隆银行"},{"bank_code":"ZYCB","bank_name":"濮阳中原村镇银行"},{"bank_code":"行动西安银行","bank_name":"行动西安银行"}],"hot":[{"bank_code":"BCM","bank_name":"交通银行"}],"others":[{"bank_code":"ABC","bank_name":"中国农业银行"},{"bank_code":"ALPay","bank_name":"智慧支付"},{"bank_code":"BBL_XHR","bank_name":"泰国盘谷银行"},{"bank_code":"BCM","bank_name":"交通银行"},{"bank_code":"BOC","bank_name":"中国银行"},{"bank_code":"BOIM","bank_name":"内蒙古银行"},{"bank_code":"CCB","bank_name":"中国建设银行"},{"bank_code":"CEB","bank_name":"光大"},{"bank_code":"CGB","bank_name":"广发银行CGB"},{"bank_code":"CIB","bank_name":"兴业"},{"bank_code":"CMB","bank_name":"招行"},{"bank_code":"CMBC","bank_name":"民生银行"},{"bank_code":"CNCB","bank_name":"中信银行"},{"bank_code":"CZB","bank_name":"浙商银行"},{"bank_code":"EPay","bank_name":"易付"},{"bank_code":"HAPPY","bank_name":"樂付"},{"bank_code":"HDFC","bank_name":"HDFC Bank"},{"bank_code":"HXB","bank_name":"华夏银行"},{"bank_code":"ICBC","bank_name":"中国工商银行"},{"bank_code":"KKBK","bank_name":"科塔克馬辛德拉銀行"},{"bank_code":"KT","bank_name":"开泰银行"},{"bank_code":"KTB","bank_name":"泰京银行"},{"bank_code":"PAB","bank_name":"平安銀行"},{"bank_code":"PAY168","bank_name":"壹陆捌收单平台"},{"bank_code":"RCB","bank_name":"农村商业银行"},{"bank_code":"RCC","bank_name":"农村信用合作社"},{"bank_code":"SCB_XHR","bank_name":"泰国汇商银行"},{"bank_code":"Seagod","bank_name":"海神新平台"},{"bank_code":"SPD","bank_name":"浦发"},{"bank_code":"UTIB","bank_name":"艾克塞斯銀行"},{"bank_code":"PSBC","bank_name":"中国邮政储蓄银行"},{"bank_code":"AQQ","bank_name":"QQQQBANK"},{"bank_code":"ICIC","bank_name":"印度工業信貸投資銀行"},{"bank_code":"JSBC","bank_name":"江苏银行"},{"bank_code":"TRX","bank_name":"TRONTRX"},{"bank_code":"TRXUSDT","bank_name":"TRONUSDT"},{"bank_code":"YESB_XHR","bank_name":"YES BANK"},{"bank_code":"ACB","bank_name":"亚洲商业银行"},{"bank_code":"HXCB","bank_name":"桦川融兴村镇银行"},{"bank_code":"YNRC","bank_name":"云南农信银行"},{"bank_code":"LRC","bank_name":"辽宁省农村信用社联合社"},{"bank_code":"KirbyBank","bank_name":"KirbyBank"},{"bank_code":"HDB","bank_name":"邯郸银行"},{"bank_code":"BOB","bank_name":"北京银行"},{"bank_code":"BJRC","bank_name":"北京农商银行"},{"bank_code":"CDB","bank_name":"承德银行"},{"bank_code":"EGB","bank_name":"恒丰银行"},{"bank_code":"KLB","bank_name":"昆仑银行"},{"bank_code":"SRC","bank_name":"上海农商银行"},{"bank_code":"ZJCZ","bank_name":"浙江稠州商业"},{"bank_code":"BOHRB","bank_name":"哈尔滨银行"},{"bank_code":"CCBN","bank_name":"中信银行"},{"bank_code":"CDRCB","bank_name":"成都农商银行"},{"bank_code":"NJCB","bank_name":"南京银行"},{"bank_code":"TJCB","bank_name":"天津银行"},{"bank_code":"BOG","bank_name":"广州银行"},{"bank_code":"HSB","bank_name":"徽商银行"},{"bank_code":"SDRC","bank_name":"山东农村商业银行"},{"bank_code":"XIB","bank_name":"厦门国际银行"},{"bank_code":"CSCB","bank_name":"长沙银行"},{"bank_code":"DRCBCL","bank_name":"东莞农村商业银行"},{"bank_code":"ZYB","bank_name":"中原银行"},{"bank_code":"CSRC","bank_name":"常熟农商银行"},{"bank_code":"LFB","bank_name":"廊坊银行"},{"bank_code":"LYBANK","bank_name":"洛阳银行"},{"bank_code":"BQD","bank_name":"青岛银行"},{"bank_code":"NBCB","bank_name":"宁波银行"},{"bank_code":"BODL","bank_name":"大连银行"},{"bank_code":"BOXA","bank_name":"西安银行"},{"bank_code":"HBB","bank_name":"河北银行"},{"bank_code":"HBCL","bank_name":"湖北银行"},{"bank_code":"LZB","bank_name":"兰州银行"},{"bank_code":"XMB","bank_name":"厦门银行"},{"bank_code":"ZJRC","bank_name":"浙江农信"},{"bank_code":"CRBC","bank_name":"珠海华润银行"},{"bank_code":"STB","bank_name":"四川天府银行"},{"bank_code":"SDEB","bank_name":"顺德农村商业银行"},{"bank_code":"SZRCB","bank_name":"深圳农村商业银行"},{"bank_code":"JXB","bank_name":"江西银行"},{"bank_code":"FJCB","bank_name":"福建省农村信用社联合社"},{"bank_code":"HZCB","bank_name":"杭州银行"},{"bank_code":"ZJTL","bank_name":"泰隆银行"},{"bank_code":"CQB","bank_name":"重庆银行"},{"bank_code":"QLB","bank_name":"齐鲁银行"},{"bank_code":"HXBANK","bank_name":"华夏银行"},{"bank_code":"BOSC","bank_name":"上海银行"},{"bank_code":"SCRC","bank_name":"四川农信银行"},{"bank_code":"BOJJ","bank_name":"九江银行"},{"bank_code":"BOTS","bank_name":"唐山银行"},{"bank_code":"GRC","bank_name":"广州农村商业银行"},{"bank_code":"BBG","bank_name":"广西北部湾银行"},{"bank_code":"BOD","bank_name":"东莞银行"},{"bank_code":"BOJL","bank_name":"吉林银行"},{"bank_code":"BOZJK","bank_name":"张家口银行"},{"bank_code":"CSRB","bank_name":"长沙农商银行"},{"bank_code":"GDRC","bank_name":"广东农信(广东农商)"},{"bank_code":"GZCB","bank_name":"贵州农信"},{"bank_code":"GZRC","bank_name":"赣州银行"},{"bank_code":"HKB","bank_name":"汉口银行"},{"bank_code":"HNRC","bank_name":"河南农村信用社"},{"bank_code":"HRC","bank_name":"湖北农信"},{"bank_code":"JSNX","bank_name":"江苏省农村信用社联合社"},{"bank_code":"JSHB","bank_name":"晋商银行"},{"bank_code":"MYB","bank_name":"浙江网商银行"},{"bank_code":"NBBANK","bank_name":"宁波银行"},{"bank_code":"TZB","bank_name":"台州银行"},{"bank_code":"YHGM","bank_name":"广西银海国民村镇银行"},{"bank_code":"HBRC","bank_name":"河北农信"},{"bank_code":"HLJRC","bank_name":"黑龙江农信"},{"bank_code":"JLRC","bank_name":"吉林农信"},{"bank_code":"ARCU","bank_name":"安徽农商"},{"bank_code":"GDB","bank_name":"广发银行"},{"bank_code":"NACZ","bank_name":"宁安融兴村镇银行"},{"bank_code":"ZYCB","bank_name":"濮阳中原村镇银行"},{"bank_code":"行动西安银行","bank_name":"行动西安银行"},{"bank_code":"SXCB","bank_name":"陕西信合"},{"bank_code":"GZB","bank_name":"贵州银行"},{"bank_code":"CAB","bank_name":"长安银行"},{"bank_code":"QZB","bank_name":"泉州银行"},{"bank_code":"TCRCB","bank_name":"江苏太仓农村商业银行"},{"bank_code":"ALIPAY","bank_name":"支付宝"},{"bank_code":"JFB","bank_name":"支付宝"},{"bank_code":"QSB","bank_name":"齐商银行"},{"bank_code":"USF","bank_name":"云闪付"},{"bank_code":"WI","bank_name":"微信"},{"bank_code":"WHCCB","bank_name":"威海市商业银行"},{"bank_code":"HLM","bank_name":"汇来米"}],"userbank":["交通银行"]},"recharge_json_channel":true,"recharge_json_count_once":200,"recharge_json_day_notsucc":30,"isrecharge_additional":false,"low_rate_hint":0,"accountname":"李白","tips_recommended":0,"agent_rate_cost":"2|7.00","recommendMoney":["3000","8800","14600","30000"]},"timestamp":1716881633}

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
    public String usdtrate; // "", "7.32" USDT渠道费率
    public boolean depositfee_disabled; // false, 充值渠道赠送比例前台显示开关(false:关,true:开)
    public String depositfee_rate; // "0.00", "5.00" 充值渠道赠送比例
    //public String isdisabled; // "0", 是否维护中(0:正常,2:维护中)
    //public boolean isrechgetime; // true,
    public String starttime; // 1701619200,
    public String endtime; // 1701705540, false
    //public String addcredittimes; // "10000",
    //public int addcreditminsec; // 0,
    public String PayCardId; // "2350",
    //public boolean namountdecimal; // false,
    public boolean nalipayname; // false,
    public boolean fixedamount_channelshow; // false, true
    public String[] fixedamount_info; // [], ["100","200","300","500","1000","2000","3000","5000","10000","20000","30000","50000"]
    public String[] recommendMoney; // [], ["100","300","500","1000"] 快选金额
    public boolean phone_needbind; // true,
    //public boolean showfee; // false,
    //public int fee; // 0,
    //public boolean op_series; // false,
    public boolean op_thiriframe_use; // false,
    public boolean op_thiriframe_status; // false,
    public String op_thiriframe_msg; // "",
    public String op_thiriframe_url; // "",
    public boolean opfix_disable_bankstatus; // "",
    public boolean recharge_json_channel; // false,
    //public int recharge_json_count_once; // 5,
    //public int recharge_json_day_notsucc; // 3,
    public boolean isrecharge_additional; // false, 是否需要预先获取实际充值金额
    public String low_rate_hint; // "1", 当前渠道【{detail.title}】充值到账成功率较低，为了保证快速到账，请使用以下渠道进行充值或联系客服进行处理！
    public String accountname; // "",
    public int tips_recommended; // 0,
    public String bankcardstatus_onepaywx; // false 时需要弹窗提示绑定WX, 默认为null
    public String bankcardstatus_onepayzfb; // false 时需要弹窗提示绑定ZFB, 默认为null

    /**
     * opBankList
     */
    @SerializedName("op_bank_list")
    private OpBankListDTO opBankList;

    public String toInfo() {
        return "RechargeVo { " +
                "bid='" + bid + '\'' +
                ", paycode='" + paycode + '\'' +
                ", title='" + title + '\'' +
                ", op_thiriframe_use=" + op_thiriframe_use +
                ", phone_needbind=" + phone_needbind +
                ", view_bank_card=" + view_bank_card +
                ", type='" + type + '\'' +
                ", typename='" + typename + '\'' +
                ", PayCardId='" + PayCardId + '\'' +
                ", loadmin='" + loadmin + '\'' +
                ", loadmax='" + loadmax + '\'' +
                ", randturnauto=" + randturnauto +
                ", userBankList=" + Arrays.toString(userBankList.toArray()) +
                ", fixedamount_channelshow=" + fixedamount_channelshow +
                ", fixedamount_info=" + Arrays.toString(fixedamount_info) +
                ", recommendMoney=" + Arrays.toString(recommendMoney) +
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
                ", nalipayname=" + nalipayname +
                ", fixedamount_channelshow=" + fixedamount_channelshow +
                ", fixedamount_info=" + Arrays.toString(fixedamount_info) +
                ", recommendMoney=" + Arrays.toString(recommendMoney) +
                ", phone_needbind=" + phone_needbind +
                ", op_thiriframe_use=" + op_thiriframe_use +
                ", op_thiriframe_status=" + op_thiriframe_status +
                ", op_thiriframe_msg='" + op_thiriframe_msg + '\'' +
                ", op_thiriframe_url='" + op_thiriframe_url + '\'' +
                ", opfix_disable_bankstatus='" + opfix_disable_bankstatus + '\'' +
                ", recharge_json_channel=" + recharge_json_channel +
                ", isrecharge_additional=" + isrecharge_additional +
                ", low_rate_hint='" + low_rate_hint + '\'' +
                ", accountname='" + accountname + '\'' +
                ", tips_recommended=" + tips_recommended +
                ", bankcardstatus_onepaywx=" + bankcardstatus_onepaywx +
                ", bankcardstatus_onepayzfb=" + bankcardstatus_onepayzfb +
                '}';
    }

    public OpBankListDTO getOpBankList() {
        return opBankList;
    }

    public void setOpBankList(OpBankListDTO opBankList) {
        this.opBankList = opBankList;
    }

    public Object getUser_bank_info() {
        return user_bank_info;
    }

    public void setUser_bank_info(Object user_bank_info) {
        this.user_bank_info = user_bank_info;

        if (user_bank_info != null) {
            if (user_bank_info instanceof Map) {
                userBankList.clear();
                Map<String, String> map = (Map<String, String>) user_bank_info;
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    BankCardVo vo3 = new BankCardVo(entry.getKey(), entry.getValue());
                    userBankList.add(vo3);
                }
            }
        }
    }

    public static class OpBankListDTO {
        /**
         * top
         */
        @SerializedName("top")
        private List<BankInfoDTO> top;
        /**
         * hot
         */
        @SerializedName("hot")
        private List<BankInfoDTO> hot;
        /**
         * others
         */
        @SerializedName("others")
        private List<BankInfoDTO> others;

        /**
         * others
         */
        @SerializedName("used")
        private List<BankInfoDTO> used;

        /**
         * 用户绑定银行
         */
        private List<BankInfoDTO> mBind;

        public List<BankInfoDTO> getUsed() {
            return used;
        }

        public void setUsed(List<BankInfoDTO> used) {
            this.used = used;
        }

        public List<BankInfoDTO> getmBind() {
            return mBind;
        }

        public void setmBind(List<BankInfoDTO> mBind) {
            this.mBind = mBind;
        }

        public List<BankInfoDTO> getTop() {
            return top;
        }

        public void setTop(List<BankInfoDTO> top) {
            this.top = top;
        }

        public List<BankInfoDTO> getHot() {
            return hot;
        }

        public void setHot(List<BankInfoDTO> hot) {
            this.hot = hot;
        }

        public List<BankInfoDTO> getOthers() {
            return others;
        }

        public void setOthers(List<BankInfoDTO> others) {
            this.others = others;
        }

        public static class BankInfoDTO {
            /**
             * bankCode
             */
            @SerializedName("bank_code")
            private String bankCode;
            /**
             * bankName
             */
            @SerializedName("bank_name")
            private String bankName;

            public String getBankCode() {
                return bankCode;
            }

            public void setBankCode(String bankCode) {
                this.bankCode = bankCode;
            }

            public String getBankName() {
                return bankName;
            }

            public void setBankName(String bankName) {
                this.bankName = bankName;
            }
        }
    }
}
