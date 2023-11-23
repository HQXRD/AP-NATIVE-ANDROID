package com.xtree.recharge.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;

import com.xtree.recharge.data.RechargeRepository;

import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
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

        //RaJava模拟登录
        addSubscribe(model.login()
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .doOnSubscribe((Consumer<Disposable>) disposable -> showDialog())
                .subscribe((Consumer<Object>) o -> {
                    dismissDialog();
                    //进入DemoActivity页面
                    //关闭页面
                    finish();
                }));

    }
}
