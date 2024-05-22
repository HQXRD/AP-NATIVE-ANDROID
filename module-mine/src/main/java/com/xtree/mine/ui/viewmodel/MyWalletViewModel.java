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
import com.xtree.base.vo.ProfileVo;
import com.xtree.mine.data.MineRepository;
import com.xtree.mine.ui.fragment.TransferResultFragment;
import com.xtree.mine.vo.AwardsRecordVo;
import com.xtree.mine.vo.BalanceVo;
import com.xtree.mine.vo.GameBalanceVo;
import com.xtree.mine.vo.GameMenusVo;
import com.xtree.mine.vo.TransferResultModel;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import io.reactivex.disposables.Disposable;
import me.xtree.mvvmhabit.base.BaseViewModel;
import me.xtree.mvvmhabit.bus.RxBus;
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
    public MutableLiveData<AwardsRecordVo> awardrecordVoMutableLiveData = new MutableLiveData<>();//流水
    public MutableLiveData<ProfileVo> liveDataProfile = new MutableLiveData<>(); // 个人信息
    private HashMap<String, GameBalanceVo> map = new HashMap<>();

    public MyWalletViewModel(@NonNull Application application, MineRepository repository) {
        super(application, repository);
    }

    /**
     * 获取流水
     */
    public void getAwardRecord() {
        Disposable disposable = (Disposable) model.getApiService().getAwardRecord()
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<AwardsRecordVo>() {
                    @Override
                    public void onResult(AwardsRecordVo awardrecordVo) {
                        if (awardrecordVo != null) {
                            awardrecordVoMutableLiveData.setValue(awardrecordVo);
                        } else {
                            CfLog.i("awardrecordVo IS NULL ");
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        //ToastUtils.showLong("请求失败");
                    }
                });
        addSubscribe(disposable);
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
                        //super.onError(t);
                        liveDataGameBalance.setValue(getFullGame("gameAlias", "维护中"));
                    }

                    @Override
                    public void onFail(BusinessException t) {
                        map.put(gameAlias, getFullGame(gameAlias, t.message));
                        SPUtils.getInstance().put(SPKeyGlobal.WLT_GAME_ROOM_BLC, new Gson().toJson(map.values()));
                        liveDataGameBalance.setValue(getFullGame(gameAlias, t.message));
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

                    @Override
                    public void onFail(BusinessException t) {
                        CfLog.e("error, " + t.toString());
                        liveData1kRecycle.setValue(false);
                        super.onFail(t);
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

                    @Override
                    public void onFail(BusinessException t) {
                        CfLog.e("error, " + t.toString());
                        liveDataAutoTrans.setValue(false);
                        super.onFail(t);
                    }
                });
        addSubscribe(disposable);
    }

    public void doTransfer(HashMap<String, String> map) {
        Disposable disposable = (Disposable) model.getApiService().doTransfer(map)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<Object>() {
                    @Override
                    public void onResult(Object vo) {
                        CfLog.d("******");
                        liveDataTransfer.setValue(true);
                        TransferResultModel transferResultModel = new TransferResultModel();
                        transferResultModel.from = map.get("from");
                        transferResultModel.to = map.get("to");
                        transferResultModel.money = map.get("money");
                        transferResultModel.status = 1;
                        RxBus.getDefault().postSticky(transferResultModel);
                        startContainerActivity(TransferResultFragment.class.getCanonicalName());
                    }

                    @Override
                    public void onFail(BusinessException t) {
                        super.onFail(t);
                        CfLog.e("error, " + t.toString());
                        liveDataTransfer.setValue(false);

                        TransferResultModel transferResultModel = new TransferResultModel();
                        transferResultModel.from = map.get("from");
                        transferResultModel.to = map.get("to");
                        transferResultModel.money = map.get("money");
                        transferResultModel.status = 0;
                        transferResultModel.errorMsg = t.message;
                        RxBus.getDefault().postSticky(transferResultModel);
                        startContainerActivity(TransferResultFragment.class.getCanonicalName());
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
        CfLog.i("json: " + json);
        List<GameBalanceVo> list = new ArrayList<>();
        if (json.equals("[]")) {
            list.add(new GameBalanceVo("pt", "PT娱乐", 1, " "));
            list.add(new GameBalanceVo("bbin", "BBIN娱乐", 2, " "));
            list.add(new GameBalanceVo("ag", "AG街机捕鱼", 4, " "));
            list.add(new GameBalanceVo("obgdj", "DB电竞", 40, " "));
            list.add(new GameBalanceVo("yy", "云游棋牌", 20, " "));
            list.add(new GameBalanceVo("obgqp", "DB棋牌", 32, " "));
            list.add(new GameBalanceVo("wali", "瓦力棋牌", 80, ""));
        } else {
            list = gson.fromJson(json, new TypeToken<List<GameBalanceVo>>() {
            }.getType());
        }
        listSingleLiveData.setValue(list);

        json = SPUtils.getInstance().getString(SPKeyGlobal.HOME_PROFILE);
        ProfileVo vo = gson.fromJson(json, ProfileVo.class);
        if (vo != null) {
            liveDataProfile.setValue(vo);
        }

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
            case "wali":
                t = new GameBalanceVo(gameAlias, "瓦力棋牌", 80, balance);
                break;
            default:
                t = new GameBalanceVo("维护中", "维护中", 0, "--");
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
