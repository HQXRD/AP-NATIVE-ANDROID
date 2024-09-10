package com.xtree.recharge.data.source.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by KAKA on 2024/5/28.
 * Describe:
 */
public class ExCreateOrderResponse{

    /**
     * 三方订单号
     */
    @SerializedName("merchant_order")
    private String merchantOrder;
    /**
     * 本地订单号
     */
    @SerializedName("platform_order")
    private String platformOrder;
    /**
     * 充值金额
     */
    @SerializedName("amount")
    private int amount;
    /**
     * 订单详情,使用方法参考onepay文档
     */
    @SerializedName("orderinfo")
    private OrderinfoDTO orderinfo;

    public String getMerchantOrder() {
        return merchantOrder;
    }

    public void setMerchantOrder(String merchantOrder) {
        this.merchantOrder = merchantOrder;
    }

    public String getPlatformOrder() {
        return platformOrder;
    }

    public void setPlatformOrder(String platformOrder) {
        this.platformOrder = platformOrder;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public OrderinfoDTO getOrderinfo() {
        return orderinfo;
    }

    public void setOrderinfo(OrderinfoDTO orderinfo) {
        this.orderinfo = orderinfo;
    }

    public static class OrderinfoDTO {
        /**
         * returncode
         */
        @SerializedName("returncode")
        private String returncode;
        /**
         * messageX
         */
        @SerializedName("message")
        private String messageX;
        /**
         * 三方订单号
         */
        @SerializedName("merchant_order")
        private String merchantOrder;
        /**
         * statusX
         */
        @SerializedName("status")
        private String statusX;
        /**
         * createTime
         */
        @SerializedName("create_time")
        private String createTime;
        /**
         * 订单过期时间
         */
        @SerializedName("expire_time")
        private String expireTime;
        /**
         * 是否可以取消等待
         */
        @SerializedName("allow_cancel_wait")
        private int allowCancelWait;
        /**
         * 取消等待时间
         */
        @SerializedName("cancel_wait_time")
        private String cancelWaitTime;
        /**
         * 是否可以取消审核
         */
        @SerializedName("allow_cancel")
        private int allowCancel;
        /**
         * 取消审核时间
         */
        @SerializedName("allow_cancel_time")
        private String allowCancelTime;
        /**
         * 付款账号
         */
        @SerializedName("bank_account")
        private String bankAccount;
        /**
         * 付款账户名
         */
        @SerializedName("bank_account_name")
        private String bankAccountName;
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
        /**
         * bankArea
         */
        @SerializedName("bank_area")
        private String bankArea;
        /**
         * qrcode
         */
        @SerializedName("qrcode")
        private String qrcode;

        public String getReturncode() {
            return returncode;
        }

        public void setReturncode(String returncode) {
            this.returncode = returncode;
        }

        public String getMessageX() {
            return messageX;
        }

        public void setMessageX(String messageX) {
            this.messageX = messageX;
        }

        public String getMerchantOrder() {
            return merchantOrder;
        }

        public void setMerchantOrder(String merchantOrder) {
            this.merchantOrder = merchantOrder;
        }

        public String getStatusX() {
            return statusX;
        }

        public void setStatusX(String statusX) {
            this.statusX = statusX;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getExpireTime() {
            return expireTime;
        }

        public void setExpireTime(String expireTime) {
            this.expireTime = expireTime;
        }

        public int getAllowCancelWait() {
            return allowCancelWait;
        }

        public void setAllowCancelWait(int allowCancelWait) {
            this.allowCancelWait = allowCancelWait;
        }

        public String getCancelWaitTime() {
            return cancelWaitTime;
        }

        public void setCancelWaitTime(String cancelWaitTime) {
            this.cancelWaitTime = cancelWaitTime;
        }

        public int getAllowCancel() {
            return allowCancel;
        }

        public void setAllowCancel(int allowCancel) {
            this.allowCancel = allowCancel;
        }

        public String getAllowCancelTime() {
            return allowCancelTime;
        }

        public void setAllowCancelTime(String allowCancelTime) {
            this.allowCancelTime = allowCancelTime;
        }

        public String getBankAccount() {
            return bankAccount;
        }

        public void setBankAccount(String bankAccount) {
            this.bankAccount = bankAccount;
        }

        public String getBankAccountName() {
            return bankAccountName;
        }

        public void setBankAccountName(String bankAccountName) {
            this.bankAccountName = bankAccountName;
        }

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

        public String getBankArea() {
            return bankArea;
        }

        public void setBankArea(String bankArea) {
            this.bankArea = bankArea;
        }

        public String getQrcode() {
            return qrcode;
        }

        public void setQrcode(String qrcode) {
            this.qrcode = qrcode;
        }
    }
}
