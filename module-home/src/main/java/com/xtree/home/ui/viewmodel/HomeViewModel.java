package com.xtree.home.ui.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;

import com.xtree.home.data.HomeRepository;

import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import me.xtree.mvvmhabit.base.BaseViewModel;
import me.xtree.mvvmhabit.bus.event.SingleLiveEvent;
import me.xtree.mvvmhabit.utils.RxUtils;

/**
 * Created by goldze on 2018/6/21.
 */

public class HomeViewModel extends BaseViewModel<HomeRepository> {
    public SingleLiveEvent<String> itemClickEvent = new SingleLiveEvent<>();
    public HomeViewModel(@NonNull Application application, HomeRepository repository) {
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
