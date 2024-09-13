package com.xtree.live.ui.main.model.anchor;

import androidx.databinding.ObservableField;

import com.xtree.base.mvvm.recyclerview.BindModel;
import com.xtree.live.R;

import java.util.ArrayList;

/**
 * Created by KAKA on 2024/9/9.
 * Describe: 直播TAB数据模型
 */
public class LiveAnchorModel extends BindModel {

    public LiveAnchorModel() {
        //设置标签，用于显示TAB标题
        setTag("直播");

        datas.set(bindModels);
    }

    public ObservableField<ArrayList<BindModel>> datas = new ObservableField<>(new ArrayList<>());
    public ObservableField<ArrayList<Integer>> itemTypeList = new ObservableField<>(
            new ArrayList<Integer>() {
                {
                    add(R.layout.item_live_anchor);
                }
            });


    private final ArrayList<BindModel> bindModels = new ArrayList<BindModel>() {{
        LiveAnchorItemModel itemModel = new LiveAnchorItemModel();
        itemModel.setText("直播TXT");

        add(itemModel);
        add(itemModel);
        add(itemModel);
        add(itemModel);
        add(itemModel);
        add(itemModel);
        add(itemModel);
        add(itemModel);
    }};

}
