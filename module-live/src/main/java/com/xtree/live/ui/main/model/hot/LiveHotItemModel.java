package com.xtree.live.ui.main.model.hot;

import com.xtree.base.mvvm.recyclerview.BindModel;

/**
 * Created by KAKA on 2024/9/11.
 * Describe: 热门列表数据模型
 */
public class LiveHotItemModel extends BindModel {
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
