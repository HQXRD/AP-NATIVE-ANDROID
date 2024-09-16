package com.xtree.recharge.data.source.request;

/**
 * Created by KAKA on 2024/5/28.
 * Describe:
 */
public class ExCreateOrderRequest {
    //pid=176&payAmount=100&payBankCode=ABC&payName=张飞

    private String pid;
    private String payAmount;
    private String payBankCode = "";
    private String payName;
    private String userBankId = "";
    private String payBankName = "";

    public String getPayBankName() {
        return payBankName;
    }

    public void setPayBankName(String payBankName) {
        this.payBankName = payBankName;
    }

    public String getUserBankId() {
        return userBankId;
    }

    public void setUserBankId(String userBankId) {
        this.userBankId = userBankId;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(String payAmount) {
        this.payAmount = payAmount;
    }

    public String getPayBankCode() {
        return payBankCode;
    }

    public void setPayBankCode(String payBankCode) {
        this.payBankCode = payBankCode;
    }

    public String getPayName() {
        return payName;
    }

    public void setPayName(String payName) {
        this.payName = payName;
    }
}
