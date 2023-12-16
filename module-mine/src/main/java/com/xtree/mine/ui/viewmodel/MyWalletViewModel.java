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

import java.util.HashMap;

import me.xtree.mvvmhabit.utils.RxUtils;

public class MyWalletViewModel extends BaseViewModel<MineRepository> {
    //public SingleLiveData<String> itemClickEvent = new SingleLiveData<>();
    public SingleLiveData<BalanceVo> liveDataBalance = new SingleLiveData<>(); // 中心钱包
    public SingleLiveData<GameBalanceVo> liveDataGameBalance = new SingleLiveData<>(); // 场馆余额
    public SingleLiveData<Boolean> liveData1kRecycle = new SingleLiveData<>(); // 1键回收
    public SingleLiveData<Boolean> liveDataAutoTrans = new SingleLiveData<>(); // 自动免转
    public SingleLiveData<Boolean> liveDataTransfer = new SingleLiveData<>(); // 转账

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
                        liveDataGameBalance.setValue(getFullGame(vo));
                    }

                    @Override
                    public void onError(Throwable t) {
                        CfLog.e("error, " + t.toString());
                        super.onError(t);
                    }
                });
        addSubscribe(disposable);
    }

    public void do1kAutoRecycle() {
        Disposable disposable = (Disposable) model.getApiService().do1kAutoRecycle()
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<Object>() {
                    @Override
                    public void onResult(Object vo) {
                        CfLog.d("******");
                        liveData1kRecycle.setValue(true);
                    }

                    @Override
                    public void onError(Throwable t) {
                        CfLog.e("error, " + t.toString());
                        liveData1kRecycle.setValue(false);
                        super.onError(t);
                    }
                });
        addSubscribe(disposable);
    }

    public void doAutoTransfer(HashMap map) {
        Disposable disposable = (Disposable) model.getApiService().doAutoTransfer(map)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<Object>() {
                    @Override
                    public void onResult(Object vo) {
                        CfLog.d("******");
                        liveDataAutoTrans.setValue(true);
                    }

                    @Override
                    public void onError(Throwable t) {
                        CfLog.e("error, " + t.toString());
                        liveDataAutoTrans.setValue(false);
                        super.onError(t);
                    }
                });
        addSubscribe(disposable);
    }

    public void doTransfer(HashMap map) {
        Disposable disposable = (Disposable) model.getApiService().doTransfer(map)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<Object>() {
                    @Override
                    public void onResult(Object vo) {
                        CfLog.d("******");
                        liveDataTransfer.setValue(true);
                    }

                    @Override
                    public void onError(Throwable t) {
                        CfLog.e("error, " + t.toString());
                        liveDataTransfer.setValue(false);
                        super.onError(t);
                    }
                });
        addSubscribe(disposable);
    }

    private GameBalanceVo getFullGame(GameBalanceVo vo) {
        GameBalanceVo t = null;

        switch (vo.gameAlias) {
            case "pt":
                t = new GameBalanceVo(vo.gameAlias, "PT娱乐", 1, vo.balance);
                break;
            case "bbin":
                t = new GameBalanceVo(vo.gameAlias, "BBIN娱乐", 2, vo.balance);
                break;
            case "ag":
                t = new GameBalanceVo(vo.gameAlias, "AG街机捕鱼", 3, vo.balance);
                break;
            case "obgdj":
                t = new GameBalanceVo(vo.gameAlias, "DB电竞", 4, vo.balance);
                break;
            case "yy":
                t = new GameBalanceVo(vo.gameAlias, "云游棋牌", 5, vo.balance);
                break;
            case "obgqp":
                t = new GameBalanceVo(vo.gameAlias, "DB棋牌", 6, vo.balance);
                break;
            case "shaba":
                t = new GameBalanceVo(vo.gameAlias, "沙巴体育", 7, vo.balance);
                break;
            default:
                CfLog.e("error, default, alias: " + vo);
                break;
        }

        return t;
    }

}
