package com.xtree.mine.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;

import com.xtree.base.utils.CfLog;
import com.xtree.mine.data.MineRepository;
import com.xtree.mine.vo.BalanceVo;
import com.xtree.mine.vo.GameBalanceVo;

import io.reactivex.disposables.Disposable;
import me.xtree.mvvmhabit.base.BaseViewModel;
import me.xtree.mvvmhabit.bus.event.SingleLiveData;
import com.xtree.base.net.HttpCallBack;
import me.xtree.mvvmhabit.utils.RxUtils;

public class MyWalletViewModel extends BaseViewModel<MineRepository> {
    //public SingleLiveData<String> itemClickEvent = new SingleLiveData<>();
    public SingleLiveData<BalanceVo> liveDataBalance = new SingleLiveData<>();
    public SingleLiveData<GameBalanceVo> liveDataGameBalance = new SingleLiveData<>();

    public MyWalletViewModel(@NonNull Application application, MineRepository repository) {
        super(application, repository);
    }

    public void getBalance() {
        Disposable disposable = (Disposable) model.getApiService().getBalance()
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<BalanceVo>() {
                    @Override
                    public void onResult(BalanceVo vo) {
                        CfLog.d(vo.toString());
                        liveDataBalance.setValue(vo);
                    }

                    @Override
                    public void onError(Throwable t) {
                        CfLog.e("error, " + t.toString());
                        super.onError(t);
                    }
                });
        addSubscribe(disposable);
    }

    public void getGameBalance(String gameAlias) {
        Disposable disposable = (Disposable) model.getApiService().getGameBalance(gameAlias)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<GameBalanceVo>() {
                    @Override
                    public void onResult(GameBalanceVo vo) {
                        vo.gameAlias = gameAlias;
                        CfLog.d(vo.toString());
                        liveDataGameBalance.setValue(vo);
                    }

                    @Override
                    public void onError(Throwable t) {
                        CfLog.e("error, " + t.toString());
                        super.onError(t);
                    }
                });
        addSubscribe(disposable);
    }

}
