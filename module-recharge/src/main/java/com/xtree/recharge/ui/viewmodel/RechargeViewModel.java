package com.xtree.recharge.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;

import com.xtree.recharge.data.RechargeRepository;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import me.xtree.mvvmhabit.base.BaseViewModel;
import me.xtree.mvvmhabit.bus.event.SingleLiveEvent;
import me.xtree.mvvmhabit.utils.RxUtils;

/**
 * Created by goldze on 2018/6/21.
 */

public class RechargeViewModel extends BaseViewModel<RechargeRepository> {
    public SingleLiveEvent<String> itemClickEvent = new SingleLiveEvent<>();
    public RechargeViewModel(@NonNull Application application, RechargeRepository repository) {
        super(application, repository);
    }

    private void login() {


    }
}
