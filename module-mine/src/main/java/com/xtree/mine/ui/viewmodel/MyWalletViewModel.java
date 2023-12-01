package com.xtree.mine.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;

import com.xtree.mine.data.MineRepository;

import me.xtree.mvvmhabit.base.BaseViewModel;
import me.xtree.mvvmhabit.bus.event.SingleLiveData;

public class MyWalletViewModel extends BaseViewModel<MineRepository> {
    public SingleLiveData<String> itemClickEvent = new SingleLiveData<>();
    public MyWalletViewModel(@NonNull Application application, MineRepository repository) {
        super(application, repository);
    }
}
