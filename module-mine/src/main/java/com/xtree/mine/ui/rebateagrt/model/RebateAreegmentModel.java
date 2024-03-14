package com.xtree.mine.ui.rebateagrt.model;

import android.graphics.drawable.Drawable;

import com.xtree.base.mvvm.recyclerview.BindModel;

/**
 * Created by KAKA on 2024/3/8.
 * Describe: 返水契约列表数据
 */
public class RebateAreegmentModel extends BindModel {
    public String title;
    public Drawable icon;
    public RebateAreegmentTypeEnum type;

    public RebateAreegmentModel(String title, Drawable icon, RebateAreegmentTypeEnum type) {
        this.title = title;
        this.icon = icon;
        this.type = type;
    }
}

