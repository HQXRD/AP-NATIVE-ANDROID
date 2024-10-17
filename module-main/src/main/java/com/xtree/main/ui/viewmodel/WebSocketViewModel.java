package com.xtree.main.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;

import com.xtree.base.net.HttpCallBack;
import com.xtree.base.utils.CfLog;
import com.xtree.base.vo.WsToken;
import com.xtree.main.data.MainRepository;

import io.reactivex.disposables.Disposable;
import me.xtree.mvvmhabit.base.BaseViewModel;
import me.xtree.mvvmhabit.bus.event.SingleLiveData;
import me.xtree.mvvmhabit.utils.RxUtils;

public class WebSocketViewModel extends BaseViewModel<MainRepository> {
    public SingleLiveData<WsToken> getWsTokenLiveData = new SingleLiveData<>();

    public WebSocketViewModel(@NonNull Application application) {
        super(application);
    }

    public WebSocketViewModel(@NonNull Application application, MainRepository model) {
        super(application, model);
    }

    public void getWsToken() {
        Disposable disposable = (Disposable) model.getApiService().getWsToken()
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<WsToken>() {
                    @Override
                    public void onResult(WsToken wsToken) {
                        CfLog.i("wsToken****** " + wsToken);
                        getWsTokenLiveData.setValue(wsToken);
                    }

                    @Override
                    public void onError(Throwable t) {
                        CfLog.e(t.toString());
                    }
                });
        addSubscribe(disposable);
    }
}
