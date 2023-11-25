package com.xtree.activity.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;


import com.xtree.activity.data.ActivityRepository;

import me.xtree.mvvmhabit.base.BaseViewModel;
import me.xtree.mvvmhabit.bus.event.SingleLiveData;

/**
 * Created by goldze on 2018/6/21.
 */

public class ActivityViewModel extends BaseViewModel<ActivityRepository> {
    public SingleLiveData<String> itemClickEvent = new SingleLiveData<>();
    public ActivityViewModel(@NonNull Application application, ActivityRepository repository) {
        super(application, repository);
    }

    private void login() {


    }
}
