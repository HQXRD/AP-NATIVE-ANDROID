package com.xtree.home.ui.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;

import com.xtree.home.data.HomeRepository;

import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
import me.xtree.mvvmhabit.base.BaseViewModel;
import me.xtree.mvvmhabit.bus.event.SingleLiveEvent;
import me.xtree.mvvmhabit.http.ApiDisposableObserver;
import me.xtree.mvvmhabit.http.BaseResponse;
import me.xtree.mvvmhabit.http.ResponseThrowable;
import me.xtree.mvvmhabit.utils.RxUtils;
import me.xtree.mvvmhabit.utils.ToastUtils;

/**
 * Created by goldze on 2018/6/21.
 */

public class HomeViewModel extends BaseViewModel<HomeRepository> {
    public SingleLiveEvent<String> itemClickEvent = new SingleLiveEvent<>();
    public HomeViewModel(@NonNull Application application, HomeRepository repository) {
        super(application, repository);
    }

    public void login(String username, String password) {
        model.login(username, password)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe((Consumer<Disposable>) disposable -> showDialog())
                .subscribe(new ApiDisposableObserver<Object>() {
                    @Override
                    public void onResult(Object o) {
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        dismissDialog();
                    }
                });

    }

    /*new ApiDisposableObserver<Object>() {
        @Override
        public void onResult(Object o) {

        }

        @Override
        public void onError(Throwable e) {

        }
    }*/
}
