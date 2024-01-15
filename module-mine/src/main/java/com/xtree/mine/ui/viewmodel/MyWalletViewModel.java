package com.xtree.mine.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.net.HttpCallBack;
import com.xtree.base.utils.CfLog;
import com.xtree.mine.data.MineRepository;
import com.xtree.mine.vo.BalanceVo;
import com.xtree.mine.vo.GameBalanceVo;

import java.util.HashMap;
import java.util.List;

import io.reactivex.disposables.Disposable;
import me.xtree.mvvmhabit.base.BaseViewModel;
import me.xtree.mvvmhabit.bus.event.SingleLiveData;
import me.xtree.mvvmhabit.utils.RxUtils;
import me.xtree.mvvmhabit.utils.SPUtils;

public class MyWalletViewModel extends BaseViewModel<MineRepository> {
    public SingleLiveData<BalanceVo> liveDataBalance = new SingleLiveData<>(); // 中心钱包
    public SingleLiveData<GameBalanceVo> liveDataGameBalance = new SingleLiveData<>(); // 场馆余额
    public SingleLiveData<Boolean> liveData1kRecycle = new SingleLiveData<>(); // 1键回收
    public SingleLiveData<Boolean> liveDataAutoTrans = new SingleLiveData<>(); // 自动免转
    public SingleLiveData<Boolean> liveDataTransfer = new SingleLiveData<>(); // 转账

    private HashMap<String, GameBalanceVo> map = new HashMap<>();

    public MyWalletViewModel(@NonNull Application application, MineRepository repository) {
        super(application, repository);
    }

    public void getBalance() {
        Disposable disposable = (Disposable) model.getApiService().getBalance()
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<BalanceVo>() {
                    @Override
                    public void onResult(BalanceVo vo) {
                        CfLog.d(vo.toString());
                        SPUtils.getInstance().put(SPKeyGlobal.WLT_CENTRAL_BLC, vo.balance);
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
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<GameBalanceVo>() {
                    @Override
                    public void onResult(GameBalanceVo vo) {
                        vo.gameAlias = gameAlias;
                        CfLog.d(vo.toString());
                        map.put(gameAlias, getFullGame(vo));
                        SPUtils.getInstance().put(SPKeyGlobal.WLT_GAME_ROOM_BLC, new Gson().toJson(map.values()));
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
                .compose(RxUtils.schedulersTransformer())
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
                .compose(RxUtils.schedulersTransformer())
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
                .compose(RxUtils.schedulersTransformer())
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

    public void readCache() {
        CfLog.i("******");
        Gson gson = new Gson();
        String balance = SPUtils.getInstance().getString(SPKeyGlobal.WLT_CENTRAL_BLC, "0.0000");
        liveDataBalance.setValue(new BalanceVo(balance));

        String json = SPUtils.getInstance().getString(SPKeyGlobal.WLT_GAME_ROOM_BLC, "[]");
        CfLog.e("json: " + json);
        List<GameBalanceVo> list = gson.fromJson(json, new TypeToken<List<GameBalanceVo>>() {
        }.getType());
        for (GameBalanceVo vo : list) {
            liveDataGameBalance.setValue(vo);
        }
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
