package com.xtree.user.ui.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import android.text.TextUtils;

import me.xtree.mvvmhabit.base.BaseViewModel;
import me.xtree.mvvmhabit.binding.command.BindingAction;
import me.xtree.mvvmhabit.binding.command.BindingCommand;
import me.xtree.mvvmhabit.bus.RxBus;

/**
 * Created by goldze on 2018/6/21.
 */

public class UserDetailViewModel extends BaseViewModel {
    public ObservableField<String> nameObservable = new ObservableField();

    public UserDetailViewModel(@NonNull Application application) {
        super(application);
    }

    public void setName(String name) {
        nameObservable.set(name);
    }

    //回传参数
    public BindingCommand backOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            if (!TextUtils.isEmpty(nameObservable.get())) {
                //RxBus解耦组件通信
                RxBus.getDefault().post(nameObservable.get());
            }
            finish();
        }
    });
}
