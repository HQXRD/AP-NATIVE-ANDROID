package com.xtree.recharge.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.net.HttpCallBack;
import com.xtree.base.utils.CfLog;
import com.xtree.base.utils.UuidUtil;
import com.xtree.recharge.data.RechargeRepository;
import com.xtree.recharge.vo.BankCardVo;
import com.xtree.recharge.vo.PaymentVo;
import com.xtree.recharge.vo.RechargePayVo;
import com.xtree.recharge.vo.RechargeVo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.disposables.Disposable;
import me.xtree.mvvmhabit.base.BaseViewModel;
import me.xtree.mvvmhabit.bus.event.SingleLiveData;
import me.xtree.mvvmhabit.utils.RxUtils;
import me.xtree.mvvmhabit.utils.SPUtils;
import me.xtree.mvvmhabit.utils.ToastUtils;

/**
 * Created by goldze on 2018/6/21.
 */

public class RechargeViewModel extends BaseViewModel<RechargeRepository> {
    public SingleLiveData<String> itemClickEvent = new SingleLiveData<>();
    //public SingleLiveData<PaymentVo> liveDataPaymentVo = new SingleLiveData<>();
    public SingleLiveData<String> liveData1kEntry = new SingleLiveData<>();
    public SingleLiveData<List<RechargeVo>> liveDataRechargeList = new SingleLiveData<>(); // 充值列表
    public SingleLiveData<String> liveDataTutorial = new SingleLiveData<>(); // 充值教程
    public SingleLiveData<RechargeVo> liveDataRecharge = new SingleLiveData<>(); // 充值详情
    public SingleLiveData<RechargePayVo> liveDataRechargePay = new SingleLiveData<>(); // 充值提交结果

    public RechargeViewModel(@NonNull Application application, RechargeRepository repository) {
        super(application, repository);
    }

    /**
     * 获取 一键进入 的链接
     */
    public void get1kEntry() {
        Map<String, String> map = new HashMap<>();
        map.put("nonce", UuidUtil.getID16());
        Disposable disposable = (Disposable) model.getApiService().get1kEntry(map)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<Map<String, String>>() {
                    @Override
                    public void onResult(Map<String, String> vo) {
                        CfLog.d(vo.toString());
                        liveData1kEntry.setValue(vo.get("login_url"));
                    }

                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                        super.onError(t);
                    }
                });

        addSubscribe(disposable);
    }

    /**
     * 获取 充值列表
     */
    public void getPayments() {
        Disposable disposable = (Disposable) model.getApiService().getPayments()
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<PaymentVo>() {
                    @Override
                    public void onResult(PaymentVo vo) {
                        CfLog.d("chongzhiListCount: " + vo.chongzhiListCount);
                        SPUtils.getInstance().put(SPKeyGlobal.RC_PAYMENT_OBJ, new Gson().toJson(vo));

                        // 转换银行卡数据 把map格式的转换成list
                        for (int i = 0; i < vo.chongzhiList.size(); i++) {
                            RechargeVo vo2 = vo.chongzhiList.get(i);
                            if (vo2.user_bank_info != null) {
                                if (vo2.user_bank_info instanceof Map) {
                                    Map<String, String> map = (Map<String, String>) vo2.user_bank_info;
                                    for (Map.Entry<String, String> entry : map.entrySet()) {
                                        BankCardVo vo3 = new BankCardVo(entry.getKey(), entry.getValue());
                                        vo2.userBankList.add(vo3);
                                    }
                                }
                            }

                        }

                        liveDataRechargeList.setValue(vo.chongzhiList);
                        liveDataTutorial.setValue(vo.bankdirect_url);
                    }

                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                        super.onError(t);
                    }
                });

        addSubscribe(disposable);
    }

    /**
     * 获取 充值详情
     *
     * @param bid bid
     */
    public void getPayment(String bid) {
        //Map<String ,String> map = new HashMap<>();
        //map.put("bid", bid);
        Disposable disposable = (Disposable) model.getApiService().getPayment(bid)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<RechargeVo>() {
                    @Override
                    public void onResult(RechargeVo vo) {
                        CfLog.d(vo.toString());
                        liveDataRecharge.setValue(vo);
                    }

                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                        super.onError(t);
                    }
                });

        addSubscribe(disposable);
    }

    /**
     * 提交充值请求
     *
     * @param bid bid
     * @param map 充值参数
     */
    public void rechargePay(String bid, Map<String, String> map) {
        Disposable disposable = (Disposable) model.getApiService().rechargePay(bid, map)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<Object>() {
                    @Override
                    public void onResult(Object vo) {
                        CfLog.d("********"); // RechargePayVo
                        if (new Gson().toJson(vo).startsWith("{")) {
                            RechargePayVo t = new Gson().fromJson(new Gson().toJson(vo), RechargePayVo.class);
                            liveDataRechargePay.setValue(t);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        CfLog.e("error, " + t.toString());
                        super.onError(t);
                        ToastUtils.showLong("提交充值请求失败");
                    }
                });
        addSubscribe(disposable);
    }

    public void readCache() {
        CfLog.i("******");
        Gson gson = new Gson();
        String json = SPUtils.getInstance().getString(SPKeyGlobal.RC_PAYMENT_OBJ);

        PaymentVo vo = gson.fromJson(json, new TypeToken<PaymentVo>() {
        }.getType());
        if (vo != null) {
            liveDataRechargeList.setValue(vo.chongzhiList);
        }

    }

}
