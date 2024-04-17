package com.xtree.mine.vo.response;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

/**
 * Created by KAKA on 2024/3/15.
 * Describe: 契约分红一键发放
 */
public class DividendAutoSendResponse {

    //{"status":1,"msg":"","data":{"1":{"pay_status":1,"self_money":600,"bill_num":1},"3":{"pay_status":3,"self_money":0,"bill_num":15},"2":{"pay_status":2,"self_money":0,"bill_num":0},"4":{"pay_status":4,"self_money":0,"bill_num":0}},"servertime":"2024-04-17 11:44:53","ts":0}

    /**
     * status
     */
    @SerializedName("status")
    private int status;
    /**
     * msg
     */
    @SerializedName("msg")
    private String msg;
    /**
     * data
     */
    @SerializedName("data")
    private Map<String, DataDTO> data;
    /**
     * servertime
     */
    @SerializedName("servertime")
    private String servertime;
    /**
     * ts
     */
    @SerializedName("ts")
    private int ts;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Map<String, DataDTO> getData() {
        return data;
    }

    public void setData(Map<String, DataDTO> data) {
        this.data = data;
    }

    public String getServertime() {
        return servertime;
    }

    public void setServertime(String servertime) {
        this.servertime = servertime;
    }

    public int getTs() {
        return ts;
    }

    public void setTs(int ts) {
        this.ts = ts;
    }

    public static class DataDTO {

        /**
         * payStatus
         */
        @SerializedName("pay_status")
        private int payStatus;
        /**
         * selfMoney
         */
        @SerializedName("self_money")
        private int selfMoney;
        /**
         * billNum
         */
        @SerializedName("bill_num")
        private int billNum;

        public int getPayStatus() {
            return payStatus;
        }

        public void setPayStatus(int payStatus) {
            this.payStatus = payStatus;
        }

        public int getSelfMoney() {
            return selfMoney;
        }

        public void setSelfMoney(int selfMoney) {
            this.selfMoney = selfMoney;
        }

        public int getBillNum() {
            return billNum;
        }

        public void setBillNum(int billNum) {
            this.billNum = billNum;
        }
    }
}
