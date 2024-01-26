package com.xtree.mine.ui.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.net.HttpCallBack;
import com.xtree.base.utils.CfLog;
import com.xtree.mine.data.MineRepository;
import com.xtree.mine.vo.BalanceVo;
import com.xtree.mine.vo.GameBalanceVo;
import com.xtree.mine.vo.GameMenusVo;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import io.reactivex.disposables.Disposable;
import me.xtree.mvvmhabit.base.BaseViewModel;
import me.xtree.mvvmhabit.bus.event.SingleLiveData;
import me.xtree.mvvmhabit.http.BusinessException;
import me.xtree.mvvmhabit.utils.RxUtils;
import me.xtree.mvvmhabit.utils.SPUtils;

public class MyWalletViewModel extends BaseViewModel<MineRepository> {
    public SingleLiveData<BalanceVo> liveDataBalance = new SingleLiveData<>(); // 中心钱包
    public SingleLiveData<GameBalanceVo> liveDataGameBalance = new SingleLiveData<>(); // 场馆余额
    public SingleLiveData<List<GameBalanceVo>> listSingleLiveData = new SingleLiveData<>(); // 暂存场馆余额
    public SingleLiveData<Boolean> liveData1kRecycle = new SingleLiveData<>(); // 1键回收
    public SingleLiveData<Boolean> liveDataAutoTrans = new SingleLiveData<>(); // 自动免转
    public SingleLiveData<Boolean> liveDataTransfer = new SingleLiveData<>(); // 转账
    public MutableLiveData<List<GameMenusVo>> liveDataTransGameType = new MutableLiveData<>(); // 可转账的平台

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

    public void getTransThirdGameType(Context ctx) {
        new Thread(() -> {
            try {
                List<GameMenusVo> canUseTransGame = new ArrayList<>();
                List<GameMenusVo> gameMenusVoList = readFromRaw(ctx);
                for (int i = 0; i < gameMenusVoList.size(); i++) {
                    GameMenusVo vo = gameMenusVoList.get(i);
                    if (vo.needTransfer) {
                        CfLog.d(vo.name);
                        canUseTransGame.add(vo);
                    }
                }
                liveDataTransGameType.setValue(canUseTransGame);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).run();
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
                        map.put(gameAlias, getFullGame(vo.gameAlias, vo.balance));
                        SPUtils.getInstance().put(SPKeyGlobal.WLT_GAME_ROOM_BLC, new Gson().toJson(map.values()));
                        liveDataGameBalance.setValue(getFullGame(vo.gameAlias, vo.balance));
                    }

                    @Override
                    public void onError(Throwable t) {
                        CfLog.e("error, " + t.toString());
                        super.onError(t);
                    }

                    @Override
                    public void onFail(BusinessException t) {
                        map.put(gameAlias, getFullGame(gameAlias, "0.0000"));
                        SPUtils.getInstance().put(SPKeyGlobal.WLT_GAME_ROOM_BLC, new Gson().toJson(map.values()));
                        liveDataGameBalance.setValue(getFullGame(gameAlias, "0.0000"));
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
        listSingleLiveData.setValue(list);

    }

    private GameBalanceVo getFullGame(String gameAlias, String balance) {
        GameBalanceVo t = null;

        switch (gameAlias) {
            case "pt":
                t = new GameBalanceVo(gameAlias, "PT娱乐", 1, balance);
                break;
            case "bbin":
                t = new GameBalanceVo(gameAlias, "BBIN娱乐", 2, balance);
                break;
            case "ag":
                t = new GameBalanceVo(gameAlias, "AG街机捕鱼", 4, balance);
                break;
            case "obgdj":
                t = new GameBalanceVo(gameAlias, "DB电竞", 40, balance);
                break;
            case "yy":
                t = new GameBalanceVo(gameAlias, "云游棋牌", 20, balance);
                break;
            case "obgqp":
                t = new GameBalanceVo(gameAlias, "DB棋牌", 32, balance);
                break;
            default:
                CfLog.e("error, default, alias: " + gameAlias);
                break;
        }

        return t;
    }

    private List<GameMenusVo> readFromRaw(Context context) throws IOException {
        InputStream is = context.getAssets().open("game_menus.json");
        return readText(is);
    }

    private List<GameMenusVo> readText(InputStream is) {
        List<GameMenusVo> gameMenusVoArrayList = new ArrayList<>();
        try {
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            // 将字节数组转换为字符串
            String json = new String(buffer, "UTF-8");

            Gson gson = new Gson();
            gameMenusVoArrayList = Arrays.asList(gson.fromJson(json, GameMenusVo[].class));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return gameMenusVoArrayList;
    }
}
