package com.xtree.live.ui.main.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;

import me.xtree.mvvmhabit.base.BaseModel;
import me.xtree.mvvmhabit.base.BaseViewModel;

/**
 * Created by KAKA on 2024/9/9.
 * Describe: 直播门户viewModel
 */
public class LiveViewModel extends BaseViewModel {
    public LiveViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveViewModel(@NonNull Application application, BaseModel model) {
        super(application, model);
    }
}
