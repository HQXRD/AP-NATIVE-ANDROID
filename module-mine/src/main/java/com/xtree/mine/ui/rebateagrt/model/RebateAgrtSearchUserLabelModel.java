package com.xtree.mine.ui.rebateagrt.model;

import androidx.databinding.ObservableBoolean;

import com.xtree.base.mvvm.recyclerview.BindModel;

/**
 * Created by KAKA on 2024/3/18.
 * Describe:
 */
public class RebateAgrtSearchUserLabelModel extends BindModel {

    public String userName;
    public String userId;

    public ObservableBoolean checked = new ObservableBoolean(false);

    public RebateAgrtSearchUserLabelModel(String userId, String userName) {
        this.userName = userName;
        this.userId = userId;
    }
}
