package com.xtree.activity.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;


import com.xtree.activity.data.ActivityRepository;

import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import me.xtree.mvvmhabit.base.BaseViewModel;
import me.xtree.mvvmhabit.bus.event.SingleLiveEvent;
import me.xtree.mvvmhabit.utils.RxUtils;

/**
 * Created by goldze on 2018/6/21.
 */

public class ActivityViewModel extends BaseViewModel<ActivityRepository> {
    public SingleLiveEvent<String> itemClickEvent = new SingleLiveEvent<>();
    public ActivityViewModel(@NonNull Application application, ActivityRepository repository) {
        super(application, repository);
    }

    private void login() {


    }
}
