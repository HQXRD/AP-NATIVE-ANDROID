package com.xtree.recharge.data.source.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by KAKA on 2024/5/28.
 * Describe:
 */
public class ExBankInfoResponse {


    /**
     * bankCode
     */
    @SerializedName("bank_code")
    private String bankCode;
    /**
     * 银行卡名称
     */
    private String bankName;
    /**
     * merchantOrder
     */
    @SerializedName("merchant_order")
    private String merchantOrder;
    /**
     * bankArea
     */
    @SerializedName("bank_area")
    private String bankArea;
    /**
     * bankAccountName
     */
    @SerializedName("bank_account_name")
    private String bankAccountName;
    /**
     * bankAccount
     */
    @SerializedName("bank_account")
    private String bankAccount;
    /**
     * payAmount
     */
    @SerializedName("pay_amount")
    private String payAmount;
    /**
     * expireTime
     */
    @SerializedName("expire_time")
    private String expireTime;
    /**
     * allowCancel
     */
    @SerializedName("allow_cancel")
    private int allowCancel;
    /**
     * allowCancelTime
     */
    @SerializedName("allow_cancel_time")
    private String allowCancelTime;

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getMerchantOrder() {
        return merchantOrder;
    }

    public void setMerchantOrder(String merchantOrder) {
        this.merchantOrder = merchantOrder;
    }

    public String getBankArea() {
        return bankArea;
    }

    public void setBankArea(String bankArea) {
        this.bankArea = bankArea;
    }

    public String getBankAccountName() {
        return bankAccountName;
    }

    public void setBankAccountName(String bankAccountName) {
        this.bankAccountName = bankAccountName;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(String payAmount) {
        this.payAmount = payAmount;
    }

    public String getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(String expireTime) {
        this.expireTime = expireTime;
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
}
