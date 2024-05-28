package com.xtree.recharge.ui.model;

import com.xtree.base.mvvm.recyclerview.BindModel;

/**
 * Created by KAKA on 2024/5/27.
 * Describe:
 */
public class BankPickModel extends BindModel {

    private String bankName = "";

    public BankPickModel(String bankName) {
        this.bankName = bankName;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
}
