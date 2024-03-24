package com.xtree.recharge.vo;

import java.util.List;

import me.xtree.mvvmhabit.http.BaseResponse;

/**
 * 反馈查询页面 网络数据
 */
public class FeedbackCheckVo extends BaseResponse {

    public static class FeedbackBankInfo {

        public int id;
        public String name;

        public FeedbackBankInfo(int id, String name) {
            this.id = id;
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

    public List<FeedbackCheckInfo> list;
    public List<FeedbackBankInfo> banksInfo;

    public static class FeedbackCheckInfo {
        public String id;
        public String userpay_mode;
        public String userpay_virtual_protocol;
        public String third_orderid;
        public String userpay_amount;
        public String userpay_time;
        public String add_time;
        public String cash_time;
        public String order_status;

        public String order_status_text;

        public String order_edit_status;

        public String username;
        public String userpay_bank;
        public String userpay_name;
        public String receive_banknum;

        public String receive_name;
        public String receive_bank;

        public String receive_bank_text;//显示具体的名称

        public String userpay_picture;//图片下载地址

        @Override
        public String toString() {
            return "FeedbackCheckInfo{" +
                    "id='" + id + '\'' +
                    ", userpay_mode='" + userpay_mode + '\'' +
                    ", userpay_virtual_protocol='" + userpay_virtual_protocol + '\'' +
                    ", third_orderid='" + third_orderid + '\'' +
                    ", userpay_amount='" + userpay_amount + '\'' +
                    ", userpay_time='" + userpay_time + '\'' +
                    ", add_time='" + add_time + '\'' +
                    ", cash_time='" + cash_time + '\'' +
                    ", order_status='" + order_status + '\'' +
                    ", order_status_text='" + order_status_text + '\'' +
                    ", order_edit_status='" + order_edit_status + '\'' +
                    ", username='" + username + '\'' +
                    ", userpay_bank='" + userpay_bank + '\'' +
                    ", userpay_name='" + userpay_name + '\'' +
                    ", receive_banknum='" + receive_banknum + '\'' +
                    ", receive_name='" + receive_name + '\'' +
                    ", receive_bank='" + receive_bank + '\'' +
                    ", receive_bank_text='" + receive_bank_text + '\'' +
                    ", userpay_picture='" + userpay_picture + '\'' +
                    '}';
        }
    }

    public List<FeedbackProtocolInfo> protocolInfo;//虚拟币协议（虚拟币选项 协议选择使用）

}
