package com.xtree.recharge.ui.model;

import com.xtree.base.mvvm.recyclerview.BindModel;

import java.util.Objects;

/**
 * Created by KAKA on 2024/5/27.
 * Describe:
 */
public class BankPickModel extends BindModel {

    //用户绑定银行卡id
    private String bankId;
    //三方银行卡code
    private String bankCode;
    private String bankName;

    @Override
    public String toString() {
        return "BankPickModel { " +
                "bankId='" + bankId + '\'' +
                ", bankCode='" + bankCode + '\'' +
                ", bankName='" + bankName + '\'' +
                '}';
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BankPickModel myClass = (BankPickModel) o;
        return Objects.equals(bankName, myClass.bankName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bankName);
    }
}
