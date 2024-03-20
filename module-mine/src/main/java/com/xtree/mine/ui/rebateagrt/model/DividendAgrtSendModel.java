package com.xtree.mine.ui.rebateagrt.model;

import androidx.databinding.ObservableField;

import com.xtree.base.mvvm.recyclerview.BindModel;

/**
 * Created by KAKA on 2024/3/19.
 * Describe:
 */
public class DividendAgrtSendModel extends BindModel {
    public String userName = "-";
    public String bet = "-";
    public String netloss = "-";
    public String people = "-";
    public String cycle_percent = "-";
    public String cycle = "-";
    public String subMoney = "-";
    public String profitloss = "-";
    //契约状态
    public String payStatu = "-1";
    //契约状态文本
    public String payStatuText = "-";
    public String userid = "";
    public ObservableField<Boolean> checked = new ObservableField<>(false);
}
