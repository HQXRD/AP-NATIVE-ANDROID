package com.xtree.recharge.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.utils.CfLog;
import com.xtree.recharge.data.RechargeRepository;
import com.xtree.recharge.vo.PaymentVo;
import com.xtree.recharge.vo.RechargePayVo;
import com.xtree.recharge.vo.RechargeVo;

import java.util.List;
import java.util.Map;

import io.reactivex.disposables.Disposable;
import me.xtree.mvvmhabit.base.BaseViewModel;
import me.xtree.mvvmhabit.bus.event.SingleLiveData;
import me.xtree.mvvmhabit.http.HttpCallBack;
import me.xtree.mvvmhabit.utils.RxUtils;
import me.xtree.mvvmhabit.utils.SPUtils;
import me.xtree.mvvmhabit.utils.ToastUtils;

/**
 * Created by goldze on 2018/6/21.
 */

public class RechargeViewModel extends BaseViewModel<RechargeRepository> {
    public SingleLiveData<String> itemClickEvent = new SingleLiveData<>();
    //public SingleLiveData<PaymentVo> liveDataPaymentVo = new SingleLiveData<>();
    public SingleLiveData<List<RechargeVo>> liveDataRechargeList = new SingleLiveData<>(); // 充值列表
    public SingleLiveData<RechargeVo> liveDataRecharge = new SingleLiveData<>(); // 充值详情
    public SingleLiveData<RechargePayVo> liveDataRechargePay = new SingleLiveData<>(); // 充值提交结果

    public RechargeViewModel(@NonNull Application application, RechargeRepository repository) {
        super(application, repository);
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
                        liveDataRechargeList.setValue(vo.chongzhiList);
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
                .subscribeWith(new HttpCallBack<RechargePayVo>() {
                    @Override
                    public void onResult(RechargePayVo vo) {
                        CfLog.d("********");
                        liveDataRechargePay.setValue(vo);
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
