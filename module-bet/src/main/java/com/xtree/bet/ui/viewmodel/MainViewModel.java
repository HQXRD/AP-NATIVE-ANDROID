package com.xtree.bet.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.xtree.bet.data.BetRepository;

import io.reactivex.Flowable;
import me.xtree.mvvmhabit.base.BaseViewModel;
import me.xtree.mvvmhabit.bus.event.SingleLiveData;
import me.xtree.mvvmhabit.http.ApiCallBack;
import me.xtree.mvvmhabit.http.ApiSubscriber;
import me.xtree.mvvmhabit.http.BaseResponse;
import me.xtree.mvvmhabit.utils.RxUtils;

/**
 * Created by goldze on 2018/6/21.
 */

public class MainViewModel extends BaseViewModel<BetRepository> {
    public SingleLiveData<String> itemClickEvent = new SingleLiveData<>();

    public SingleLiveData<String[]> playMethodTab = new SingleLiveData<>();

    public MainViewModel(@NonNull Application application, BetRepository repository) {
        super(application, repository);
    }

    public void login(String username, String password) {
        Flowable<BaseResponse<Object>> flowable = model.login(username, password)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer());
        ApiCallBack apiBack = new ApiCallBack(new ApiSubscriber<Object>() {

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

    }

}
