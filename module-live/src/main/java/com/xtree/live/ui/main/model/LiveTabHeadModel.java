package com.xtree.live.ui.main.model;

import androidx.databinding.ObservableField;

import com.google.android.material.tabs.TabLayout;
import com.xtree.base.mvvm.recyclerview.BindHead;
import com.xtree.base.mvvm.recyclerview.BindModel;

import java.util.ArrayList;

/**
 * Created by KAKA on 2024/9/9.
 * Describe: 直播门户页Tab数据模型
 */
public class LiveTabHeadModel extends BindModel implements BindHead {

    public LiveTabHeadModel() {
    }

    public LiveTabHeadModel(TabLayout.OnTabSelectedListener tabListener) {
        this.tabListener.set(tabListener);
    }

    public ObservableField<ArrayList<String>> tabs = new ObservableField<>();
    public ObservableField<TabLayout.OnTabSelectedListener> tabListener = new ObservableField<>();

    public void setTabs(ArrayList<String> tabs) {
        this.tabs.set(tabs);
    }

    public void setTabListener(TabLayout.OnTabSelectedListener tabListener) {
        this.tabListener.set(tabListener);
    }

    @Override
    public boolean getItemHover() {
        return true;
    }

    @Override
    public void setItemHover(boolean b) {

    }
}