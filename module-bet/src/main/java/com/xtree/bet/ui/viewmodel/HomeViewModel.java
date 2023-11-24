package com.xtree.bet.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;

import com.xtree.bet.data.BetRepository;

import io.reactivex.Flowable;
import me.xtree.mvvmhabit.base.BaseViewModel;
import me.xtree.mvvmhabit.bus.event.SingleLiveEvent;
import me.xtree.mvvmhabit.http.ApiBack;
import me.xtree.mvvmhabit.http.ApiCallBack;
import me.xtree.mvvmhabit.http.BaseResponse;
import me.xtree.mvvmhabit.utils.RxUtils;

/**
 * Created by goldze on 2018/6/21.
 */

public class HomeViewModel extends BaseViewModel<BetRepository> {
    public SingleLiveEvent<String> itemClickEvent = new SingleLiveEvent<>();

    public HomeViewModel(@NonNull Application application, BetRepository repository) {
        super(application, repository);
    }

    public void login(String username, String password) {
        Flowable<BaseResponse<Object>> flowable = model.login(username, password)
                .compose(RxUtils.schedulersTransformer1()) //线程调度
                .compose(RxUtils.exceptionTransformer1());
        ApiBack apiBack = new ApiBack(new ApiCallBack<Object>() {

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
        }, flowable);

        
        addSubscribe(apiBack.getDisposable(apiBack));

        /*addSubscribe(model.login(username, password)
                .compose(RxUtils.schedulersTransformer1()) //线程调度
                .compose(RxUtils.exceptionTransformer1()), new ApiCallBack<Object>() {

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
        });*/


    }

}
