package com.xtree.home.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;

import com.xtree.home.data.HomeRepository;
import com.xtree.home.vo.BannersVo;

import java.util.List;

import io.reactivex.disposables.Disposable;
import me.xtree.mvvmhabit.base.BaseViewModel;
import me.xtree.mvvmhabit.bus.event.SingleLiveEvent;
import me.xtree.mvvmhabit.http.HttpCallBack;
import me.xtree.mvvmhabit.utils.RxUtils;
import me.xtree.mvvmhabit.utils.ToastUtils;

/**
 * Created by marquis on 2023/11/24.
 */

public class HomeViewModel extends BaseViewModel<HomeRepository> {
    public SingleLiveEvent<String> itemClickEvent = new SingleLiveEvent<>();

    public HomeViewModel(@NonNull Application application, HomeRepository repository) {
        super(application, repository);
    }

    public void getBanner() {
        Disposable disposable = (Disposable) model.getApiService().getBanners()
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<List<BannersVo>>() {


                    @Override
                    public void onResult(List<BannersVo> list) {

                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        ToastUtils.showLong("请求失败");
                    }
                });
        addSubscribe(disposable);

    }

}
