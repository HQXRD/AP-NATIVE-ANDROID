package com.xtree.home.ui.viewmodel;


import androidx.annotation.NonNull;

import me.xtree.mvvmhabit.base.ItemViewModel;
import me.xtree.mvvmhabit.binding.command.BindingAction;
import me.xtree.mvvmhabit.binding.command.BindingCommand;

/**
 * Created by goldze on 2018/7/18.
 */

public class ViewPagerItemViewModel extends ItemViewModel<HomeViewModel> {
    public String text;

    public ViewPagerItemViewModel(@NonNull HomeViewModel viewModel, String text) {
        super(viewModel);
        this.text = text;
    }

    public BindingCommand onClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            //点击之后将逻辑转到adapter中处理
            viewModel.itemClickEvent.setValue(text);
        }
    });
}
