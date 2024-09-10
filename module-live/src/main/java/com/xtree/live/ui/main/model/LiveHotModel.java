package com.xtree.live.ui.main.model;

import androidx.databinding.ObservableField;

import com.xtree.base.mvvm.recyclerview.BindModel;
import com.xtree.live.R;

import java.util.ArrayList;

/**
 * Created by KAKA on 2024/9/9.
 * Describe: 热门TAB数据模型
 */
public class LiveHotModel extends BindModel {

    public LiveHotModel() {
        //to do
    }

    public ObservableField<ArrayList<BindModel>> datas = new ObservableField<>(new ArrayList<>());
    public ObservableField<ArrayList<Integer>> itemType = new ObservableField<>(
            new ArrayList<Integer>() {
                {
                    add(R.layout.item_live_hot);
                }
            });

//    private final ArrayList<BindModel> bindModels = new ArrayList<BindModel>() {{
//        LiveHotModel liveHotModel = new LiveHotModel();
//
//        add(liveHotModel);
//    }};

}
