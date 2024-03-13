package com.xtree.base.mvvm.recyclerview;

import androidx.annotation.NonNull;
import androidx.databinding.BaseObservable;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.ViewModel;

import com.drake.brv.BindingAdapter;
import com.drake.brv.item.ItemAttached;
import com.drake.brv.item.ItemBind;
import com.drake.brv.item.ItemPosition;
import com.drake.brv.item.ItemStableId;
import com.xtree.base.mvvm.viewmodel.BindViewModelFactory;
import com.xtree.base.mvvm.viewmodel.ViewModelUtils;

import me.xtree.mvvmhabit.base.BaseViewModel;

/**
 * Created by KAKA on 2024/3/8.
 * Describe: 搭配BaseDatabindingAdapter使用，作为item功能的扩展，item数据类需要继承此类
 */
public class BindModel extends BaseObservable implements ItemPosition, ItemBind, ItemAttached, ItemStableId {

    /**
     * 用于标识对应使用的itemType索引
     */
    private int itemType = 0;

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    @Override
    public int getItemPosition() {
        return 0;
    }

    @Override
    public void setItemPosition(int i) {

    }

    @Override
    public void onBind(@NonNull BindingAdapter.BindingViewHolder bindingViewHolder) {

    }

    @Override
    public void onViewAttachedToWindow(@NonNull BindingAdapter.BindingViewHolder bindingViewHolder) {

    }

    @Override
    public void onViewDetachedFromWindow(@NonNull BindingAdapter.BindingViewHolder bindingViewHolder) {

    }

    @Override
    public long getItemId() {
        return this.hashCode();
    }
}

