package com.xtree.live.ui.main.model.hot;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.xtree.base.mvvm.recyclerview.BindModel;
import com.xtree.live.R;

import java.util.ArrayList;

/**
 * Created by KAKA on 2024/9/9.
 * Describe: 热门TAB数据模型
 */
public class LiveHotModel extends BindModel {

    public LiveHotModel(String tag) {
        //to do
        //设置标签，用于显示TAB标题
        setTag(tag);

        datas.set(bindModels);
    }

    public ObservableField<ArrayList<BindModel>> datas = new ObservableField<>(new ArrayList<>());
    public ObservableField<ArrayList<Integer>> itemTypeList = new ObservableField<>(
            new ArrayList<Integer>() {
                {
                    add(R.layout.item_live_hot);
                }
            });

    private final ArrayList<BindModel> bindModels = new ArrayList<BindModel>() {{
        LiveHotItemModel itemModel = new LiveHotItemModel();
        itemModel.setText("热门TXT");

        add(itemModel);
        add(itemModel);
        add(itemModel);
    }};

    public OnLoadMoreListener onLoadMoreListener = new OnLoadMoreListener() {
        @Override
        public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

        }
    };

}
