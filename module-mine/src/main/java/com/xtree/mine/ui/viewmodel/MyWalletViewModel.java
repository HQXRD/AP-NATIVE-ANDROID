package com.xtree.mine.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;

import com.xtree.mine.data.MineRepository;

import me.xtree.mvvmhabit.base.BaseViewModel;

public class MyWalletViewModel extends BaseViewModel<MineRepository> {
    public MyWalletViewModel(@NonNull Application application, MineRepository repository) {
        super(application, repository);
    }
}
