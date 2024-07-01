package com.xtree.main.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import com.xtree.main.data.MainRepository;

import me.xtree.mvvmhabit.base.BaseViewModel;

/**
 * Created by marquis on 2023/11/24.
 */
public class SplashViewModel extends BaseViewModel<MainRepository> {
    public MutableLiveData<Void> inMainData = new MutableLiveData<>();

    public SplashViewModel(@NonNull Application application, MainRepository model) {
        super(application, model);
    }
}
