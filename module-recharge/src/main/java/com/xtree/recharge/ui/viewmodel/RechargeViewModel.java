package com.xtree.recharge.ui.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.net.HttpCallBack;
import com.xtree.base.utils.CfLog;
import com.xtree.base.utils.UuidUtil;
import com.xtree.base.vo.ProfileVo;
import com.xtree.base.widget.LoadingDialog;
import com.xtree.recharge.data.RechargeRepository;
import com.xtree.recharge.data.source.request.ExRechargeOrderCheckRequest;
import com.xtree.recharge.data.source.response.ExRechargeOrderCheckResponse;
import com.xtree.recharge.vo.BankCardVo;
import com.xtree.recharge.vo.BannersVo;
import com.xtree.recharge.vo.FeedbackCheckVo;
import com.xtree.recharge.vo.FeedbackImageUploadVo;
import com.xtree.recharge.vo.FeedbackVo;
import com.xtree.recharge.vo.HiWalletVo;
import com.xtree.recharge.vo.OrderInfoVo;
import com.xtree.recharge.vo.PayOrderDataVo;
import com.xtree.recharge.vo.PaymentDataVo;
import com.xtree.recharge.vo.PaymentTypeVo;
import com.xtree.recharge.vo.PaymentVo;
import com.xtree.recharge.vo.RechargeOrderDetailVo;
import com.xtree.recharge.vo.RechargePayVo;
import com.xtree.recharge.vo.RechargeVo;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscription;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import me.xtree.mvvmhabit.base.BaseViewModel;
import me.xtree.mvvmhabit.bus.event.SingleLiveData;
import me.xtree.mvvmhabit.http.BaseResponse;
import me.xtree.mvvmhabit.http.BusinessException;
import me.xtree.mvvmhabit.utils.RxUtils;
import me.xtree.mvvmhabit.utils.SPUtils;

/**
 * 充值页 ViewModel
 */
public class RechargeViewModel extends BaseViewModel<RechargeRepository> {
    public SingleLiveData<String> itemClickEvent = new SingleLiveData<>();
    public SingleLiveData<PaymentVo> liveDataPayment = new SingleLiveData<>(); // (从接口加载用)
    public SingleLiveData<PaymentDataVo> liveDataPaymentData = new SingleLiveData<>(); // (从接口加载用)
    public SingleLiveData<HiWalletVo> liveData1kEntry = new SingleLiveData<>();
    //public SingleLiveData<List<RechargeVo>> liveDataRechargeList = new SingleLiveData<>(); // 充值列表(从缓存加载用)
    public SingleLiveData<List<PaymentTypeVo>> liveDataPayTypeList = new SingleLiveData<>(); // 充值列表(大类型 从缓存加载用)
    public SingleLiveData<List<String>> liveDataPayCodeArr = new SingleLiveData<>(); // 含弹出支付窗口的充值渠道类型列表(从缓存加载用)
    public SingleLiveData<String> liveDataTutorial = new SingleLiveData<>(); // 充值教程(从缓存加载用)
    public SingleLiveData<RechargeVo> liveDataRecharge = new SingleLiveData<>(); // 充值详情
    public SingleLiveData<RechargeVo> liveDataRechargeCache = new SingleLiveData<>(); // 充值详情,缓存用的
    public SingleLiveData<RechargeVo> curRechargeLiveData = new SingleLiveData<>(); // 当前充值详情
    public SingleLiveData<RechargePayVo> liveDataRechargePay = new SingleLiveData<>(); // 充值提交结果
    public SingleLiveData<PayOrderDataVo> liveDataExpOrderData = new SingleLiveData<>(); // 充值点下一步 (极速充值)
    public SingleLiveData<Map<String, String>> liveDataSignal = new SingleLiveData<>(); // 人工客服暗号
    public SingleLiveData<RechargeOrderDetailVo> liveDataOrderDetail = new SingleLiveData<>(); // 订单详情
    public SingleLiveData<List<BannersVo>> liveDataRcBanners = new SingleLiveData<>(); // 轮播图
    public SingleLiveData<FeedbackVo> feedbackVoSingleLiveData = new SingleLiveData<>();//进入反馈页面回去的数据
    public SingleLiveData<FeedbackImageUploadVo> imageUploadVoSingleLiveData = new SingleLiveData<>();//feedback图片上传
    public SingleLiveData<Object> feedbackAddSingleLiveData = new SingleLiveData<>();//feedback 下一步接口
    public SingleLiveData<FeedbackCheckVo> feedbackCheckVoSingleLiveData = new SingleLiveData<>();//feedbackCheck 反馈查看页面
    public SingleLiveData<ProfileVo> liveDataProfile = new SingleLiveData<>();
    public SingleLiveData<ExRechargeOrderCheckResponse> liveDataCurOrder = new SingleLiveData<>(); // 极速充值 未完成的订单(跳到订单页)
    public SingleLiveData<Boolean> liveDataExpNoOrder = new SingleLiveData<>(); // 极速充值 没有未完成的订单 (显示银行/姓名/金额/下一步)
    public SingleLiveData<String> liveDataExpTitle = new SingleLiveData<>(); // 极速充值流程渠道标题
    public SingleLiveData<Object>liveSkipGuideData = new SingleLiveData<>();//跳过引导接口

    public RechargeViewModel(@NonNull Application application) {
        super(application);
    }

    public RechargeViewModel(@NonNull Application application, RechargeRepository model) {
        super(application, model);
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
                .subscribeWith(new HttpCallBack<HiWalletVo>() {
                    @Override
                    public void onResult(HiWalletVo vo) {
                        CfLog.d(vo.toString());
                        liveData1kEntry.setValue(vo);
                    }

                    @Override
                    public void onError(Throwable t) {
                        CfLog.e(t.toString());
                    }

                    @Override
                    public void onFail(BusinessException t) {
                        CfLog.e(t.toString()); // 30435 网络繁忙，无法与CNYT建立连接，请稍后再试
                    }
                });

        addSubscribe(disposable);
    }

    /**
     * 获取 充值列表
     */
    public void getPaymentsTypeList() {
        Disposable disposable = (Disposable) model.getApiService().getPaymentsTypeList()
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<PaymentDataVo>() {
                    @Override
                    public void onResult(PaymentDataVo vo) {
                        CfLog.i(new Gson().toJson(vo));
                        //CfLog.d("chongzhiListCount: " + vo.chongzhiListCount);
                        if (vo == null || vo.chongzhiList == null || vo.chongzhiList.isEmpty()) {
                            return;
                        }

                        for (int i = 0; i < vo.chongzhiList.size(); i++) {
                            PaymentTypeVo t = vo.chongzhiList.get(i);
                            if (t.payChannelList == null || t.payChannelList.isEmpty()) {
                                continue;
                            }

                            for (int j = 0; j < t.payChannelList.size(); j++) {
                                RechargeVo vo2 = t.payChannelList.get(j);
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
                        }
                        SPUtils.getInstance().put(SPKeyGlobal.RC_PAYMENT_TYPE_OBJ, new Gson().toJson(vo));
                        liveDataPaymentData.setValue(vo);
                    }

                    @Override
                    public void onError(Throwable t) {
                        CfLog.e(t.toString());
                        //super.onError(t);
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

    public void getPaymentCache(String bid) {
        getPaymentDetail(bid, liveDataRechargeCache);
    }

    /**
     * 极速充值子渠道显示流程
     */
    public void getExPayment(String bid, Context context) {

        final String[] id = {bid};

        BasePopupView loadingDialog = new XPopup.Builder(context)
                .dismissOnTouchOutside(false)
                .dismissOnBackPressed(true)
                .asCustom(new LoadingDialog(context))
                .show();

        //先检查极速充值渠道是否有正在进行的订单
        Disposable disposable = (Disposable) model.getApiService().getPaymentsTypeList()
                .doOnSubscribe(new Consumer<Subscription>() {
                    @Override
                    public void accept(Subscription subscription) throws Exception {
                        if (loadingDialog != null) {
                            loadingDialog.show();
                        }
                    }
                })
                .flatMap(new Function<BaseResponse<PaymentDataVo>, Publisher<?>>() {
                    @Override
                    public Publisher<?> apply(BaseResponse<PaymentDataVo> paymentDataVoBaseResponse) throws Exception {

                        ExRechargeOrderCheckRequest request = new ExRechargeOrderCheckRequest(id[0]);

                        if (paymentDataVoBaseResponse.getData() != null) {
                            //如果存在充值中的订单，则使用订单的渠道查询订单
                            if (paymentDataVoBaseResponse.getData().pendingOnepayfixBid > 0) {
                                String pendingOnepayfixBid = String.valueOf(paymentDataVoBaseResponse.getData().pendingOnepayfixBid);
                                request.setPid(pendingOnepayfixBid);
                                id[0] = pendingOnepayfixBid;
                            }
                        }

                        return model.rechargeOrderCheck(request);
                    }
                })
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        if (loadingDialog.isShow()) {
                            loadingDialog.dismiss();
                        }
                        if (liveDataExpNoOrder.getValue() == true) {
                            getPayment(bid);
                            LoadingDialog.show(context);
                        }
                    }
                })
                .subscribeWith(new HttpCallBack<ExRechargeOrderCheckResponse>() {
                    @Override
                    public void onResult(ExRechargeOrderCheckResponse vo) {
                        CfLog.d(vo.toString());

                        ExRechargeOrderCheckResponse.DataDTO data = vo.getData();
                        if (data != null) {
                            vo.getData().setBid(id[0]); // 跳转要用
                            String status = data.getStatus();
                            switch (status) {
                                case "00": //成功
                                case "03": //失败
                                    liveDataExpNoOrder.setValue(true);
                                    break;
                                default:
                                    long differenceInSeconds = 0;
                                    try {
                                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        Date now = Calendar.getInstance().getTime();
                                        Date end = null;
                                        end = format.parse(vo.getData().getExpireTime());
                                        differenceInSeconds = (now.getTime() - end.getTime());
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    CfLog.i("sec: " + differenceInSeconds);
                                    if (differenceInSeconds < 0) {
                                        liveDataCurOrder.setValue(vo);
                                        liveDataExpNoOrder.setValue(false);
                                    } else {
                                        liveDataExpNoOrder.setValue(true); // 订单无效/没有订单
                                    }
                                    break;
                            }
                        } else {
                            CfLog.w("no order...");
                            liveDataExpNoOrder.setValue(true); // 订单无效/没有订单
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        CfLog.e(t.toString());
                        super.onError(t);
                        liveDataExpNoOrder.setValue(true);
                        if (loadingDialog.isShow()) {
                            loadingDialog.dismiss();
                        }
                    }

                    @Override
                    public void onFail(BusinessException t) {
                        CfLog.e(t.toString());
                        super.onFail(t);
                        liveDataExpNoOrder.setValue(true);
                        if (loadingDialog.isShow()) {
                            loadingDialog.dismiss();
                        }
                    }
                });

        addSubscribe(disposable);
    }

    /**
     * 极速充值下单前查一遍未完成状态
     */
    public void createOrderCheck(Map<String, String> map,Context context){
        final String[] id = {map.get("pid")};

        BasePopupView loadingDialog = new XPopup.Builder(context)
                .dismissOnTouchOutside(false)
                .dismissOnBackPressed(true)
                .asCustom(new LoadingDialog(context))
                .show();

        //先检查极速充值渠道是否有正在进行的订单
        Disposable disposable = (Disposable) model.getApiService().getPaymentsTypeList()
                .doOnSubscribe(new Consumer<Subscription>() {
                    @Override
                    public void accept(Subscription subscription) throws Exception {
                        if (loadingDialog != null) {
                            loadingDialog.show();
                        }
                    }
                })
                .flatMap(new Function<BaseResponse<PaymentDataVo>, Publisher<?>>() {
                    @Override
                    public Publisher<?> apply(BaseResponse<PaymentDataVo> paymentDataVoBaseResponse) throws Exception {

                        ExRechargeOrderCheckRequest request = new ExRechargeOrderCheckRequest(id[0]);

                        if (paymentDataVoBaseResponse.getData() != null) {
                            //如果存在充值中的订单，则使用订单的渠道查询订单
                            if (paymentDataVoBaseResponse.getData().pendingOnepayfixBid > 0) {
                                String pendingOnepayfixBid = String.valueOf(paymentDataVoBaseResponse.getData().pendingOnepayfixBid);
                                request.setPid(pendingOnepayfixBid);
                                id[0] = pendingOnepayfixBid;
                            }
                        }

                        return model.rechargeOrderCheck(request);
                    }
                })
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        if (loadingDialog.isShow()) {
                            loadingDialog.dismiss();
                        }
                        if (liveDataExpNoOrder.getValue() == true) {
                            createOrder(map);
                            LoadingDialog.show(context);
                        }
                    }
                })
                .subscribeWith(new HttpCallBack<ExRechargeOrderCheckResponse>() {
                    @Override
                    public void onResult(ExRechargeOrderCheckResponse vo) {
                        CfLog.d(vo.toString());

                        ExRechargeOrderCheckResponse.DataDTO data = vo.getData();
                        if (data != null) {
                            vo.getData().setBid(id[0]); // 跳转要用
                            String status = data.getStatus();
                            switch (status) {
                                case "00": //成功
                                case "03": //失败
                                    liveDataExpNoOrder.setValue(true);
                                    break;
                                default:
                                    long differenceInSeconds = 0;
                                    try {
                                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        Date now = Calendar.getInstance().getTime();
                                        Date end = null;
                                        end = format.parse(vo.getData().getExpireTime());
                                        differenceInSeconds = (now.getTime() - end.getTime());
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    CfLog.i("sec: " + differenceInSeconds);
                                    if (differenceInSeconds < 0) {
                                        liveDataCurOrder.setValue(vo);
                                        liveDataExpNoOrder.setValue(false);
                                    } else {
                                        liveDataExpNoOrder.setValue(true); // 订单无效/没有订单
                                    }
                                    break;
                            }
                        } else {
                            CfLog.w("no order...");
                            liveDataExpNoOrder.setValue(true); // 订单无效/没有订单
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        CfLog.e(t.toString());
                        super.onError(t);
                        liveDataExpNoOrder.setValue(true);
                        if (loadingDialog.isShow()) {
                            loadingDialog.dismiss();
                        }
                    }

                    @Override
                    public void onFail(BusinessException t) {
                        CfLog.e(t.toString());
                        super.onFail(t);
                        liveDataExpNoOrder.setValue(true);
                        if (loadingDialog.isShow()) {
                            loadingDialog.dismiss();
                        }
                    }
                });

        addSubscribe(disposable);
    }
    public void getPayment(String bid) {
        getPaymentDetail(bid, liveDataRecharge);
    }

    /**
     * 获取 充值详情
     */
    private void getPaymentDetail(String bid, SingleLiveData<RechargeVo> liveData) {

        Disposable disposable = (Disposable) model.getApiService().getPayment(bid)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<RechargeVo>() {
                    @Override
                    public void onResult(RechargeVo vo) {
                        CfLog.d(vo.toString());
                        if (vo.user_bank_info != null && vo.user_bank_info instanceof Map) {
                            Map<String, String> map = (Map<String, String>) vo.user_bank_info;
                            for (Map.Entry<String, String> entry : map.entrySet()) {
                                BankCardVo vo3 = new BankCardVo(entry.getKey(), entry.getValue());
                                vo.userBankList.add(vo3);
                            }
                        }
                        liveData.setValue(vo);

                    }

                    @Override
                    public void onError(Throwable t) {
                        CfLog.e(t.toString());
                        //super.onError(t);
                    }
                });

        addSubscribe(disposable);
    }

    /**
     * 查看当前是否有订单
     *
     * @param bid
     */
    public void checkOrder(String bid) {

        //先检查极速充值渠道是否有正在进行的订单
        Disposable disposable = (Disposable) model.getApiService().getPaymentsTypeList()
                .flatMap(new Function<BaseResponse<PaymentDataVo>, Publisher<?>>() {
                    @Override
                    public Publisher<?> apply(BaseResponse<PaymentDataVo> paymentDataVoBaseResponse) throws Exception {

                        ExRechargeOrderCheckRequest request = new ExRechargeOrderCheckRequest(bid);

                        if (paymentDataVoBaseResponse.getData() != null) {
                            //如果存在充值中的订单，则使用订单的渠道查询订单
                            if (paymentDataVoBaseResponse.getData().pendingOnepayfixBid > 0) {
                                request.setPid(String.valueOf(paymentDataVoBaseResponse.getData().pendingOnepayfixBid));
                            }
                        }

                        return model.rechargeOrderCheck(request);
                    }
                })
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<ExRechargeOrderCheckResponse>() {
                    @Override
                    public void onResult(ExRechargeOrderCheckResponse vo) {
                        CfLog.d(vo.toString());

                        ExRechargeOrderCheckResponse.DataDTO data = vo.getData();
                        if (data != null) {
                            vo.getData().setBid(bid); // 跳转要用
                            String status = data.getStatus();
                            switch (status) {
                                case "00": //成功
                                case "03": //失败
                                    liveDataExpNoOrder.setValue(true);
                                    break;
                                default:
                                    long differenceInSeconds = 0;
                                    try {
                                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        Date now = Calendar.getInstance().getTime();
                                        Date end = null;
                                        end = format.parse(vo.getData().getExpireTime());
                                        differenceInSeconds = (now.getTime() - end.getTime());
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    CfLog.i("sec: " + differenceInSeconds);
                                    if (differenceInSeconds < 0) {
                                        liveDataCurOrder.setValue(vo);
                                    } else {
                                        liveDataExpNoOrder.setValue(true); // 订单无效/没有订单
                                    }
                                    break;
                            }
                        } else {
                            CfLog.w("no order...");
                            liveDataExpNoOrder.setValue(true); // 订单无效/没有订单
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        CfLog.e(t.toString());
                        super.onError(t);
                        liveDataExpNoOrder.setValue(true);
                    }

                    @Override
                    public void onFail(BusinessException t) {
                        CfLog.e(t.toString());
                        super.onFail(t);
                        liveDataExpNoOrder.setValue(true);
                    }
                });

        addSubscribe(disposable);
    }

    /**
     * 获取 实际充值金额
     * { "status": 30405, "msg": "操作失败 (#06，请您联系在线客服。)", "data": [], "timestamp": 1708661879 }
     *
     * @param bid bid
     * @param map 充值参数
     */
    public void getRealMoney(String bid, Map<String, String> map) {
        Disposable disposable = (Disposable) model.getApiService().getRealMoney(bid, map)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<Object>() {
                    @Override
                    public void onResult(Object vo) {
                        CfLog.d("********"); // RechargePayVo
                    }

                    @Override
                    public void onError(Throwable t) {
                        CfLog.e("error, " + t.toString());
                    }

                    @Override
                    public void onFail(BusinessException t) {
                        CfLog.e("error, " + t.toString());
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
                    }
                });
        addSubscribe(disposable);
    }

    public void createOrder(Map<String, String> map) {
        Disposable disposable = (Disposable) model.getApiService().createOrder(map)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<PayOrderDataVo>() {
                    @Override
                    public void onResult(PayOrderDataVo vo) {
                        CfLog.d("********");
                        if (vo.orderinfo != null && vo.orderinfo instanceof Map) {
                            vo.orderInfo = new Gson().fromJson(new Gson().toJson(vo.orderinfo), OrderInfoVo.class);
                        } else {
                            vo.orderInfo = new OrderInfoVo();
                        }

                        liveDataExpOrderData.setValue(vo);
                    }

                });
        addSubscribe(disposable);
    }

    public void getManualSignal() {
        Disposable disposable = (Disposable) model.getApiService().getManualSignal()
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<Map<String, String>>() {
                    @Override
                    public void onResult(Map<String, String> vo) {
                        CfLog.d("********" + vo.toString()); // RechargePayVo
                        liveDataSignal.setValue(vo);
                    }

                    @Override
                    public void onError(Throwable t) {
                        CfLog.e("error, " + t.toString());
                        super.onError(t);
                    }
                });
        addSubscribe(disposable);
    }

    public void getOrderDetail(String id) {
        Disposable disposable = (Disposable) model.getApiService().getOrderDetail(id)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<RechargeOrderDetailVo>() {
                    @Override
                    public void onResult(RechargeOrderDetailVo vo) {
                        CfLog.d("********"); // RechargePayVo
                        liveDataOrderDetail.setValue(vo);
                    }

                    @Override
                    public void onError(Throwable t) {
                        CfLog.e("error, " + t.toString());
                    }
                });
        addSubscribe(disposable);
    }

    public void getRechargeBanners() {
        Disposable disposable = (Disposable) model.getApiService().getRechargeBanners()
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<List<BannersVo>>() {
                    @Override
                    public void onResult(List<BannersVo> list) {
                        CfLog.d("********");
                        if (list != null && !list.isEmpty()) {
                            liveDataRcBanners.setValue(list);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        CfLog.e("error, " + t.toString());
                        //super.onError(t);
                    }
                });
        addSubscribe(disposable);
    }

    public void readProfile() {
        String json = SPUtils.getInstance().getString(SPKeyGlobal.HOME_PROFILE);
        ProfileVo mProfileVo = new Gson().fromJson(json, ProfileVo.class);
        if (mProfileVo != null) {
            liveDataProfile.setValue(mProfileVo);
        }
    }

    public void readCache() {
        CfLog.i("******");
        Gson gson = new Gson();
        String json;

        //json = SPUtils.getInstance().getString(SPKeyGlobal.RC_PAYMENT_OBJ);
        //PaymentVo vo = gson.fromJson(json, new TypeToken<PaymentVo>() {
        //}.getType());
        //if (vo != null) {
        //    //liveDataPayment.setValue(vo);
        //    liveDataRechargeList.setValue(vo.chongzhiList);
        //    liveDataTutorial.setValue(vo.bankdirect_url);
        //}

        json = SPUtils.getInstance().getString(SPKeyGlobal.RC_PAYMENT_TYPE_OBJ);
        PaymentDataVo vo2 = gson.fromJson(json, new TypeToken<PaymentDataVo>() {
        }.getType());
        if (vo2 != null) {
            //liveDataPayment.setValue(vo);
            //liveDataPaymentData.setValue(vo2);
            liveDataPayCodeArr.setValue(vo2.payCodeArr);
            liveDataPayTypeList.setValue(vo2.chongzhiList);
            liveDataTutorial.setValue(vo2.bankdirect_url);
        }

        readProfile();

    }

    public void feedbackInfo(HashMap<String, String> map) {

        Disposable disposable = (Disposable) model.getApiService().getFeedback(map).
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

    /**
     * 查询获取修改反馈页面信息
     */
    public void getEditFeedback(HashMap<String, String> map) {
        Disposable disposable = (Disposable) model.getApiService().getEditFeedback(map).
                compose(RxUtils.schedulersTransformer()).
                compose(RxUtils.exceptionTransformer()).
                subscribeWith(new HttpCallBack<FeedbackCheckVo>() {
                    @Override
                    public void onResult(FeedbackCheckVo feedbackVo) {
                        feedbackCheckVoSingleLiveData.setValue(feedbackVo);

                    }
                });
        addSubscribe(disposable);
    }

    /**
     * 上传图片
     */
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

    /**
     * 反馈提交确认
     */
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
    /**
     * 新增跳过引导接口
     * 点击跳过引导时 使用【需要与账号配合联调 暂未联调】
     */
    public void skipGuide(){
        Disposable disposable = (Disposable) model.getApiService().skipGuide().
                compose(RxUtils.schedulersTransformer()).
                compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<Object>() {
                    @Override
                    public void onResult(Object o) {
                        liveSkipGuideData.setValue(o);
                    }
                });
        addSubscribe(disposable);
    }

    /**
     * 通过bid获取充值渠道信息
     */
    public RechargeVo getChargeInfoById(String bid) {
        PaymentDataVo value = liveDataPaymentData.getValue();
        if (value != null && value.chongzhiList != null) {
            for (PaymentTypeVo paymentTypeVo : value.chongzhiList) {
                if (paymentTypeVo != null) {
                    for (RechargeVo rechargeVo : paymentTypeVo.payChannelList) {
                        if (rechargeVo != null && rechargeVo.bid.equals(bid)) {
                            return rechargeVo;
                        }
                    }
                }
            }
        }
        return null;
    }
}
