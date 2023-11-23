package com.xtree.home.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;

import com.xtree.home.data.HomeRepository;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;
import me.xtree.mvvmhabit.base.BaseViewModel;
import me.xtree.mvvmhabit.bus.event.SingleLiveEvent;
import me.xtree.mvvmhabit.http.ApiCallBack;
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
        addSubscribe(model.login(username, password)
                .compose(RxUtils.schedulersTransformer1()) //线程调度
                .compose(RxUtils.exceptionTransformer1())
                .doOnSubscribe(HomeViewModel.this), new ApiCallBack<Object>() {

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onResult(Object o) {

            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
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
