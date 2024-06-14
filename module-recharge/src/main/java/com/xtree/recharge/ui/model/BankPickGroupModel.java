package com.xtree.recharge.ui.model;

import android.view.View;

import androidx.databinding.ObservableField;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xtree.base.mvvm.recyclerview.BindModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KAKA on 2024/6/12.
 * Describe: 银行卡组
 */
public class BankPickGroupModel extends BindModel {
    //标题
    private String title;
    //银行卡列表
    public ObservableField<List<BindModel>> bindModels = new ObservableField<>();
    private ArrayList<Integer> itemTypes;
    private RecyclerView.RecycledViewPool recycledViewPool;
    private int spaceCount = 3;

    public BankPickGroupModel() {
        recycledViewPool = new RecyclerView.RecycledViewPool();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<Integer> getItemTypes() {
        return itemTypes;
    }

    public void setItemTypes(ArrayList<Integer> itemTypes) {
        this.itemTypes = itemTypes;
    }

    public RecyclerView.RecycledViewPool getRecycledViewPool() {
        return recycledViewPool;
    }

    public void setRecycledViewPool(RecyclerView.RecycledViewPool recycledViewPool) {
        this.recycledViewPool = recycledViewPool;
    }

    public void setSpaceCount(int spaceCount) {
        this.spaceCount = spaceCount;
    }

    public int getSpaceCount() {
        return spaceCount;
    }

    public RecyclerView.LayoutManager getLayoutManager(View view) {
        return new GridLayoutManager(view.getContext(), spaceCount);
    }
}
