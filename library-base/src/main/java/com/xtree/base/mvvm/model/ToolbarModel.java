package com.xtree.base.mvvm.model;

import androidx.lifecycle.MutableLiveData;

/**
 * Created by KAKA on 2024/3/8.
 * Describe:@layout_base_toolbar 配置数据
 */
public interface ToolbarModel {
    void onBack();

    MutableLiveData<String> getTitle();
}
