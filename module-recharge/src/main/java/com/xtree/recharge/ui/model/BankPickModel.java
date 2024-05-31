package com.xtree.recharge.ui.model;

import com.xtree.base.mvvm.recyclerview.BindModel;

import io.reactivex.functions.Consumer;

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
    private Consumer<BindModel> click;

    public void click() {
        if (click != null) {
            try {
                click.accept(this);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

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

    public void setClick(Consumer<BindModel> click) {
        this.click = click;
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
}
