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
import com.xtree.recharge.vo.FeedbackCheckVo;
import com.xtree.recharge.vo.FeedbackImageUploadVo;
import com.xtree.recharge.vo.FeedbackVo;
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
 * 充值页 ViewModel
 */
public class RechargeViewModel extends BaseViewModel<RechargeRepository> {
    public SingleLiveData<String> itemClickEvent = new SingleLiveData<>();
    public SingleLiveData<PaymentVo> liveDataPayment = new SingleLiveData<>(); // (从接口加载用)
    public SingleLiveData<String> liveData1kEntry = new SingleLiveData<>();
    public SingleLiveData<List<RechargeVo>> liveDataRechargeList = new SingleLiveData<>(); // 充值列表(从缓存加载用)
    public SingleLiveData<String> liveDataTutorial = new SingleLiveData<>(); // 充值教程(从缓存加载用)
    public SingleLiveData<RechargeVo> liveDataRecharge = new SingleLiveData<>(); // 充值详情
    public SingleLiveData<RechargePayVo> liveDataRechargePay = new SingleLiveData<>(); // 充值提交结果
    public SingleLiveData<FeedbackVo> feedbackVoSingleLiveData = new SingleLiveData<>();//进入反馈页面回去的数据
    public SingleLiveData<FeedbackImageUploadVo> imageUploadVoSingleLiveData = new SingleLiveData<>();//feedback图片上传
    public SingleLiveData<Object> feedbackAddSingleLiveData = new SingleLiveData<>();//feedback 下一步接口
    public SingleLiveData<FeedbackCheckVo> feedbackCheckVoSingleLiveData = new SingleLiveData<>();//feedbackCheck 反馈查看页面

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
                        SPUtils.getInstance().put(SPKeyGlobal.RC_PAYMENT_OBJ, new Gson().toJson(vo));

                        liveDataPayment.setValue(vo);
                        //liveDataRechargeList.setValue(vo.chongzhiList);
                        //liveDataTutorial.setValue(vo.bankdirect_url);
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
     * 获取反馈查询页面配信息
     */
    public void getFeedbackCheckInfo(String starttime, String endtime) {
        Disposable disposable = (Disposable) model.getApiService().feedbackCheckInfo(starttime, endtime).
                compose(RxUtils.schedulersTransformer()).
                compose(RxUtils.exceptionTransformer()).
                subscribeWith(new HttpCallBack<FeedbackVo>() {
                    @Override
                    public void onResult(FeedbackVo vo) {
                        feedbackVoSingleLiveData.setValue(vo);
                    }
                });
        addSubscribe(disposable);

    }

    /**
     * 获取 反馈页面详情
     *
     * @param id
     */
    public void getFeedbackCheckDetailInfo(String id) {
        Disposable disposable = (Disposable) model.getApiService().feedbackCheckDetailsInfo(id)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<FeedbackCheckVo>() {
                    @Override
                    public void onResult(FeedbackCheckVo checkVo) {
                        feedbackCheckVoSingleLiveData.setValue(checkVo);
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
                            if (t.domain_list != null && new Gson().toJson(t.domain_list).startsWith("[")) {
                                List<String> list = new Gson().fromJson(new Gson().toJson(t.domain_list), new TypeToken<List<String>>() {
                                }.getType());
                                t.domainList.addAll(list);
                            }
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
            //liveDataPayment.setValue(vo);
            liveDataRechargeList.setValue(vo.chongzhiList);
            liveDataTutorial.setValue(vo.bankdirect_url);
        }

    }

    public void feedbackInfo(String startTime, String endTime) {

        CfLog.i("startTime = " + startTime + " ===========" + "endTime = " + endTime);
        Disposable disposable = (Disposable) model.getApiService().getFeedback(startTime, endTime).
                compose(RxUtils.schedulersTransformer()).
                compose(RxUtils.exceptionTransformer()).
                subscribeWith(new HttpCallBack<FeedbackVo>() {
                    @Override
                    public void onResult(FeedbackVo feedbackVo) {
                        feedbackVoSingleLiveData.setValue(feedbackVo);

                    }
                });
        addSubscribe(disposable);

    }

    /**上传图片*/
    public void feedbackImageUp(Map<String, String> uploadMap) {
        Disposable disposable = (Disposable) model.getApiService().feedbackFileUpLoad(uploadMap).
                compose(RxUtils.schedulersTransformer()).
                compose(RxUtils.exceptionTransformer()).
                subscribeWith(new HttpCallBack<FeedbackImageUploadVo>() {
                    @Override
                    public void onResult(FeedbackImageUploadVo feedbackImageUploadVo) {
                        imageUploadVoSingleLiveData.setValue(feedbackImageUploadVo);
                    }

                });
        addSubscribe(disposable);
    }

    /**反馈提交确认*/
    public void feedbackCustomAdd(Map<String, String> addMap) {
        Disposable disposable = (Disposable) model.getApiService().feedbackCustomAdd(addMap).
                compose(RxUtils.schedulersTransformer()).
                compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<Object>() {
                    @Override
                    public void onResult(Object o) {
                        feedbackAddSingleLiveData.setValue(o);
                    }
                });
        addSubscribe(disposable);
    }
}
