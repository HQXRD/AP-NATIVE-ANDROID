package com.xtree.recharge.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;

import com.xtree.recharge.data.RechargeRepository;

import io.reactivex.disposables.Disposable;
import me.xtree.mvvmhabit.base.BaseViewModel;
import me.xtree.mvvmhabit.bus.event.SingleLiveEvent;
import me.xtree.mvvmhabit.http.ApiSubscriber;
import me.xtree.mvvmhabit.http.HttpCallBack;
import me.xtree.mvvmhabit.utils.RxUtils;

/**
 * Created by goldze on 2018/6/21.
 */

public class RechargeViewModel extends BaseViewModel<RechargeRepository> {
    public SingleLiveEvent<String> itemClickEvent = new SingleLiveEvent<>();
    public RechargeViewModel(@NonNull Application application, RechargeRepository repository) {
        super(application, repository);
    }

    private void login(String username, String password) {
        Disposable disposable = (Disposable) model.login(username, password)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<Object>() {
                    @Override
                    public void onResult(Object o) {

                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                    }
                });

        addSubscribe(disposable);
    }

}
