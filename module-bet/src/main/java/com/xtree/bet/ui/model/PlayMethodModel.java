package com.xtree.bet.ui.model;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.drake.brv.BindingAdapter;
import com.xtree.base.mvvm.recyclerview.BaseDatabindingAdapter;
import com.xtree.base.mvvm.recyclerview.BindModel;
import com.xtree.bet.R;

import java.util.ArrayList;

/**
 * Created by KAKA on 2024/9/9.
 * Describe: 直播TAB数据模型
 */
public class PlayMethodModel extends BindModel {
    private final ArrayList<BindModel> bindModels = new ArrayList<>();
    public ObservableField<ArrayList<BindModel>> datas = new ObservableField<>(new ArrayList<>());
    public ObservableField<ArrayList<Integer>> itemTypeList = new ObservableField<>(
            new ArrayList<Integer>() {
                {
                    add(R.layout.item_play_method);
                }
            });
    private OnItemClick onItemClick;
    public BaseDatabindingAdapter.onBindListener onBindListener = new BaseDatabindingAdapter.onBindListener() {

        @Override
        public void onItemClick(int modelPosition, int layoutPosition, int itemViewType) {
            int length = bindModels.size();
            for (int i = 0; i < length; i++) {
                PlayMethodItemModel playMethodItemModel = (PlayMethodItemModel) bindModels.get(i);
                playMethodItemModel.setIsSelect(modelPosition == i);
            }
            notifyChange();
            if (onItemClick != null) {
                onItemClick.onClick(((PlayMethodItemModel) bindModels.get(modelPosition)).getLabel().get(), modelPosition);
            }
        }

        @Override
        public void onBind(@NonNull BindingAdapter.BindingViewHolder bindingViewHolder, @NonNull View view, int itemViewType) {
            // 将绑定的视图与数据模型绑定
            TextView tvLabel = bindingViewHolder.findView(R.id.tvLabel);
            PlayMethodItemModel playMethodItemModel = (PlayMethodItemModel) bindModels.get(bindingViewHolder.getModelPosition());
            tvLabel.setSelected(playMethodItemModel.getIsSelect().get());
        }
    };

    public PlayMethodModel(String[] playMethods) {
        int length = playMethods.length;
        for (int i = 0; i < length; i++) {
            String label = playMethods[i];
            PlayMethodItemModel playMethodItemModel = new PlayMethodItemModel();
            playMethodItemModel.setLabel(label);
            playMethodItemModel.setIsSelect(i == 0);
            bindModels.add(playMethodItemModel);
        }

        datas.set(bindModels);
    }

    public OnItemClick getOnItemClick() {
        return onItemClick;
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public void setSelectPosition(Integer selectPosition) {
        int length = bindModels.size();
        for (int i = 0; i < length; i++) {
            PlayMethodItemModel playMethodItemModel = (PlayMethodItemModel) bindModels.get(i);
            if (selectPosition == i && onItemClick != null) {
                onItemClick.onClick(playMethodItemModel.getLabel().get(), selectPosition);
            }
            playMethodItemModel.setIsSelect(selectPosition == i);
        }
        notifyChange();
    }

    public interface OnItemClick {
        void onClick(String label, Integer position);
    }

}
