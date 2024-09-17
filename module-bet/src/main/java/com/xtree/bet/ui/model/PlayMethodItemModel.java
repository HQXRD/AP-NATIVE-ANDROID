package com.xtree.bet.ui.model;


import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;

import com.xtree.base.mvvm.recyclerview.BindModel;


/**
 * Created by KAKA on 2024/9/9.
 * Describe: 直播TAB数据模型
 */
public class PlayMethodItemModel extends BindModel {

    private ObservableField<String> label=new ObservableField<>();//名称
    private ObservableBoolean isSelect=new ObservableBoolean();//	是否选中

    public ObservableField<String> getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label.set(label);
    }

    public ObservableBoolean getIsSelect() {
        return isSelect;
    }

    public void setIsSelect(boolean isSelect) {
        this.isSelect.set(isSelect);
    }

}
