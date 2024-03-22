package com.xtree.mine.ui.rebateagrt.model;

import android.annotation.SuppressLint;

import com.xtree.base.mvvm.recyclerview.BindModel;

/**
 * Created by KAKA on 2024/3/8.
 * Describe: 返水契约列表数据
 */
public class RebateAreegmentModel extends BindModel {
    public String title;
    public int icon;
    public RebateAreegmentTypeEnum type;

    @SuppressLint("UseCompatLoadingForDrawables")
    public RebateAreegmentModel(RebateAreegmentTypeEnum type) {
        this.title = type.getName();
        this.icon = type.getDrawable();
        this.type = type;
    }
}

