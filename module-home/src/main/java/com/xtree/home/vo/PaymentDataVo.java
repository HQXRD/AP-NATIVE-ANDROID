package com.xtree.home.vo;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class PaymentDataVo {
    public String bankdirect_url;
    public int chongzhiListCount;
    public List<String> payCodeArr;
    public List<PaymentTypeVo> chongzhiList;
    public ProcessingDataVo processingData;

    public class PaymentTypeVo {

        public String id; // "1",
        public String method_sort; // "1",
        public int recommend; // "1",
        public int is_hot; // "1",
        public String dispay_title; // "极速充值",
        public String selected_image; // "/uploads/bankimg/1711349643_4358.png",
        public String un_selected_image; // "/uploads/bankimg/1711349643.png",
        public String channel_tips; // "提示",

        public List<String> deChannelArr; // ["176","203","231","232"],
        public List<RechargeVo> payChannelList;

    }

    public static class RechargeVo {

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
        public OpBankListDTO opBankList;

        public static class BankCardVo {

            public String id;
            public String name;
            //public String desc;

            public BankCardVo(String id, String name) {
                this.id = id;
                this.name = name;
            }

        }

        public class OpBankListDTO {

            @SerializedName("top")
            public List<BankInfoDTO> top;

            @SerializedName("hot")
            public List<BankInfoDTO> hot;

            @SerializedName("others")
            public List<BankInfoDTO> others;

            @SerializedName("used")
            public List<BankInfoDTO> used;

            public List<BankInfoDTO> mBind;

            public class BankInfoDTO {

                @SerializedName("bank_code")
                public String bankCode;

                @SerializedName("bank_name")
                public String bankName;

            }
        }
    }

    public class ProcessingDataVo {

        public boolean depProcessCnt1; // false,
        public boolean depProcessCnt3; // true,
        public int userProcessCount; // 6, 您已经连续充值6次
        public int jsonProcessPendCount; // 0,
        public int jsonProcessFailCount; // 0

    }
}

