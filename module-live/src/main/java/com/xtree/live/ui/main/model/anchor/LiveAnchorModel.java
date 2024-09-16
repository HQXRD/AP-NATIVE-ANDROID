package com.xtree.live.ui.main.model.anchor;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.drake.brv.BindingAdapter;
import com.xtree.base.mvvm.recyclerview.BaseDatabindingAdapter;
import com.xtree.base.mvvm.recyclerview.BindModel;
import com.xtree.live.R;

import java.util.ArrayList;

import me.xtree.mvvmhabit.utils.ToastUtils;

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

    public BaseDatabindingAdapter.onBindListener onBindListener = new BaseDatabindingAdapter.onBindListener() {

        @Override
        public void onItemClick(int modelPosition, int layoutPosition, int itemViewType) {

            ToastUtils.show(""+modelPosition, ToastUtils.ShowType.Default);
        }

        @Override
        public void onBind(@NonNull BindingAdapter.BindingViewHolder bindingViewHolder, @NonNull View view, int itemViewType) {

        }
    };

}
