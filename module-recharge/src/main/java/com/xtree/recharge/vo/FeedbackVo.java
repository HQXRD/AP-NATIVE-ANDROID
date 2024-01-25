package com.xtree.recharge.vo;

import java.util.List;

/**
 * 进入反馈页面 返回的Model
 */
public class FeedbackVo {
    public int nowPaye ;//所在页码
    public int showRows;//
    public int count ;//多少条处理中反馈
    public List<OrderFeedbackVo> list ; //反馈中的信息
    public List<FeedbackBankInfo> banksInfo ;// 支付渠道选择

    public  List<FeedbackModeInfo>  modeInfo ;//顶部付款方是

    public List<FeedbackProtocolInfo> protocolInfo ;//虚拟币协议（虚拟币选项 协议选择使用）


    public static class FeedbackModeInfo
    {
        public   int id ;
        public  String name ;


        public FeedbackModeInfo(int id , String name)
        {
            this.id = id ;
            this.name = name;
        }


       public  String toString() {
            return "FeedbackModeInfo{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    '}';
        }
    }

    /**
     * 支付渠道
     */
    public static class FeedbackBankInfo
    {
        public   int id ;
        public  String name ;

        public FeedbackBankInfo(int id, String name)
        {
            this.id = id ;
            this.name = name;
        }

        @Override
        public String toString() {
            return "FeedbackBankInfo{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    '}';
        }
    }

    /**
     * 虚拟币支付协议
     */
    public static class FeedbackProtocolInfo {

        public int id ;
        public String name ;

        public FeedbackProtocolInfo(int id , String name)
        {
            this.id = id ;
            this.name = name;
        }

        @Override
        public String toString() {
            return "FeedbackProtocolInfo{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    '}';
        }

    }


}
