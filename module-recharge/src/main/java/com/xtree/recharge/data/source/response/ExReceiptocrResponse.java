package com.xtree.recharge.data.source.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by KAKA on 2024/5/28.
 * Describe:
 */
public class ExReceiptocrResponse {


    /**
     * bankcode
     */
    @SerializedName("bankcode")
    private String bankcode;
    /**
     * payAccount
     */
    @SerializedName("pay_account")
    private String payAccount;
    /**
     * payName
     */
    @SerializedName("pay_name")
    private String payName;

    public String getBankcode() {
        return bankcode;
    }

    public void setBankcode(String bankcode) {
        this.bankcode = bankcode;
    }

    public String getPayAccount() {
        return payAccount;
    }

    public void setPayAccount(String payAccount) {
        this.payAccount = payAccount;
    }

    public String getPayName() {
        return payName;
    }

    public void setPayName(String payName) {
        this.payName = payName;
    }
}
