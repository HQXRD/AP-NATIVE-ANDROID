package com.xtree.recharge.ui.viewmodel;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.luck.picture.lib.basic.PictureSelector;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.config.SelectMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.interfaces.OnResultCallbackListener;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.xtree.base.net.HttpCallBack;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.base.utils.CfLog;
import com.xtree.base.utils.ImageUploadUtil;
import com.xtree.base.widget.GlideEngine;
import com.xtree.base.widget.ImageFileCompressEngine;
import com.xtree.base.widget.LoadingDialog;
import com.xtree.recharge.R;
import com.xtree.recharge.data.RechargeRepository;
import com.xtree.recharge.data.source.request.ExCreateOrderRequest;
import com.xtree.recharge.data.source.request.ExOrderCancelRequest;
import com.xtree.recharge.data.source.request.ExReceiptUploadRequest;
import com.xtree.recharge.data.source.request.ExReceiptocrRequest;
import com.xtree.recharge.data.source.request.ExRechargeOrderCheckRequest;
import com.xtree.recharge.data.source.response.ExBankInfoResponse;
import com.xtree.recharge.data.source.response.ExReceiptUploadResponse;
import com.xtree.recharge.data.source.response.ExReceiptocrResponse;
import com.xtree.recharge.data.source.response.ExRechargeOrderCheckResponse;
import com.xtree.recharge.ui.fragment.BankPickDialogFragment;
import com.xtree.recharge.ui.fragment.ExSlipExampleDialogFragment;
import com.xtree.recharge.ui.fragment.extransfer.ExTransferCommitFragment;
import com.xtree.recharge.ui.fragment.extransfer.ExTransferConfirmFragment;
import com.xtree.recharge.ui.fragment.extransfer.ExTransferFailFragment;
import com.xtree.recharge.ui.fragment.extransfer.ExTransferPayeeFragment;
import com.xtree.recharge.ui.fragment.extransfer.ExTransferSuccessFragment;
import com.xtree.recharge.ui.fragment.extransfer.ExTransferVoucherFragment;
import com.xtree.recharge.ui.model.BankPickModel;
import com.xtree.recharge.ui.widget.Comm100ChatWindows;
import com.xtree.recharge.vo.RechargeVo;

import java.io.File;
import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Cancellable;
import me.xtree.mvvmhabit.base.AppManager;
import me.xtree.mvvmhabit.base.BaseViewModel;
import me.xtree.mvvmhabit.http.BaseResponse;
import me.xtree.mvvmhabit.http.BusinessException;
import me.xtree.mvvmhabit.utils.RxUtils;
import me.xtree.mvvmhabit.utils.ToastUtils;

/**
 * Created by KAKA on 2024/5/28.
 * Describe: 极速转账viewModel
 */
public class ExTransferViewModel extends BaseViewModel<RechargeRepository> {
    public ExTransferViewModel(@NonNull Application application) {
        super(application);
    }

    public ExTransferViewModel(@NonNull Application application, RechargeRepository model) {
        super(application, model);
    }

    //倒计时 剩余时间
    public MutableLiveData<String> leftTimeData = new MutableLiveData<>();
    //截止時間
    public MutableLiveData<String> deadlinesData = new MutableLiveData<>();
    //匹配普通银行卡时间
    public MutableLiveData<SpannableString> pairedTimeData = new MutableLiveData<>();
    //是否匹配普通银行卡
    public MutableLiveData<Boolean> pairedTimeStatus = new MutableLiveData<>(false);
    public MutableLiveData<String> waitTime = new MutableLiveData<>();
    //是否可以取消订单 true 可以
    public MutableLiveData<Boolean> cancleOrderStatus = new MutableLiveData<>(false);
    //是否可以取消匹配 true 可以
    public MutableLiveData<Boolean> cancleOrderWaitStatus = new MutableLiveData<>(true);
    //凭证图片
    public MutableLiveData<Uri> voucher = new MutableLiveData<>();
    //订单生成信息
    public MutableLiveData<ExRechargeOrderCheckResponse.DataDTO> payOrderData = new MutableLiveData<>();
    //订单请求信息
    public MutableLiveData<ExCreateOrderRequest> createOrderInfoData = new MutableLiveData<>();
    //订单充值银行卡信息
    public MutableLiveData<ExBankInfoResponse> bankInfoData = new MutableLiveData<>();
    //付款银行
    public MutableLiveData<String> bankCodeOfPayment = new MutableLiveData<>();
    public MutableLiveData<String> bankNameOfPayment = new MutableLiveData<>();
    //付款卡号
    public MutableLiveData<String> bankNumberOfPayment = new MutableLiveData<>();
    //上传凭证页面 是否显示银行卡信息输入框
    public MutableLiveData<Boolean> showBankEdit = new MutableLiveData<>(false);
    //充值提示
    public MutableLiveData<SpannableString> tip1 = new MutableLiveData<>();
    public MutableLiveData<SpannableString> tip2 = new MutableLiveData<>();
    private WeakReference<FragmentActivity> mActivity = null;
    public String canonicalName;
    private BasePopupView loadingDialog = null;
    public MutableLiveData<RechargeViewModel> rechargeLiveData = new MutableLiveData<>();
    //标题
    public MutableLiveData<String> titleLiveData = new MutableLiveData<>("小额网银");
    @SuppressLint("StaticFieldLeak")
    private Comm100ChatWindows serviceChatFlow = null;

    public void initData(FragmentActivity mActivity, ExCreateOrderRequest createOrderInfo) {
        setActivity(mActivity);

        createOrderInfoData.setValue(createOrderInfo);

        close();

        getOrder();
    }

    /**
     * 获取订单信息
     */
    public void getOrder() {

        ExCreateOrderRequest cOrderData = createOrderInfoData.getValue();
        if (cOrderData == null) {
            return;
        }

        if (mActivity != null && mActivity.get() != null) {
            if (loadingDialog != null) {
                loadingDialog.dismiss();
            }
            loadingDialog = new XPopup.Builder(mActivity.get())
                    .dismissOnTouchOutside(false)
                    .dismissOnBackPressed(true)
                    .asCustom(new LoadingDialog(mActivity.get()))
                    .show();
            loadingDialog.show();
        }

        ExRechargeOrderCheckRequest request = new ExRechargeOrderCheckRequest(cOrderData.getPid());
        Disposable disposable = (Disposable) model.rechargeOrderCheck(request)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<ExRechargeOrderCheckResponse>() {
                    @Override
                    public void onResult(ExRechargeOrderCheckResponse vo) {
                        CfLog.d(vo.toString());

                        if (loadingDialog != null) {
                            loadingDialog.dismiss();
                        }

                        ExRechargeOrderCheckResponse.DataDTO data = vo.getData();
                        if (data != null) {

                            payOrderData.setValue(data);

                            initOrder(data);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                        super.onError(t);
                        if (loadingDialog != null) {
                            loadingDialog.dismiss();
                        }
                    }

                    @Override
                    public void onFail(BusinessException t) {
                        super.onFail(t);
                        if (loadingDialog != null) {
                            loadingDialog.dismiss();
                        }
                    }
                });

        addSubscribe(disposable);
    }

    /**
     * 初始化订单信息
     */
    private void initOrder(ExRechargeOrderCheckResponse.DataDTO data) {
        initTip();

        checkCancleState();
        checkCancleWaitState();
        checkOrderStatus();

        if (!TextUtils.isEmpty(data.getBankAccount())) {
            ExBankInfoResponse bankInfo = new ExBankInfoResponse();
            bankInfo.setBankAccount(data.getBankAccount());
            bankInfo.setBankArea(data.getBankArea());
            bankInfo.setBankCode(data.getBankCode());
            bankInfo.setBankName(data.getBankName());
            bankInfo.setBankAccountName(data.getBankAccountName());
            bankInfo.setMerchantOrder(data.getMerchantOrder());
            bankInfo.setPayAmount(data.getPayAmount());
            bankInfo.setAllowCancel(data.getAllowCancel());
            bankInfo.setAllowCancelTime(data.getAllowCancelTime());
            bankInfo.setExpireTime(data.getExpireTime());
            bankInfoData.setValue(bankInfo);
        }

        deadlinesData.setValue("请于 " + data.getExpireTime() + " 内完成支付");
        cancleWaitTimeKeeping();
        cancleOrderTimeKeeping();
    }

    /**
     * 重新检查订单
     */
    public void reCheckOrder() {
        close();

        ExRechargeOrderCheckResponse.DataDTO pValue = payOrderData.getValue();
        if (pValue != null) {
            initOrder(pValue);
        } else {
            getOrder();
        }
    }

    private void initTip() {
        ExRechargeOrderCheckResponse.DataDTO pValue = payOrderData.getValue();
        if (pValue == null) {
            return;
        }

        if (tip1.getValue() == null) {
            String payName = pValue.getPayName();
            String payBankName = pValue.getPayBankName();
            String tip1String = "请使用" +
                    payName +
                    "的" +
                    payBankName +
                    "卡充值，确保后续可成功提现";
            int color = mActivity.get().getResources().getColor(R.color.clr_red_24);
            ForegroundColorSpan colorSpan1 = new ForegroundColorSpan(color);
            ForegroundColorSpan colorSpan2 = new ForegroundColorSpan(color);

            SpannableString tip1Sp = new SpannableString(tip1String);
            tip1Sp.setSpan(colorSpan1, tip1String.indexOf(payName),
                    tip1String.indexOf(payName) + payName.length(),
                    Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            tip1Sp.setSpan(colorSpan2, tip1String.indexOf(payBankName),
                    tip1String.indexOf(payBankName) + payBankName.length(),
                    Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            tip1.setValue(tip1Sp);
        }

        if (tip2.getValue() == null) {
            int color = mActivity.get().getResources().getColor(R.color.clr_red_24);
            ForegroundColorSpan colorSpan3 = new ForegroundColorSpan(color);
            ForegroundColorSpan colorSpan4 = new ForegroundColorSpan(color);
            SpannableString tip2Sp = new SpannableString("请转账成功后务必及时确认！否则可能造成延迟上分");
            tip2Sp.setSpan(colorSpan3, 3, 5, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            tip2Sp.setSpan(colorSpan4, tip2Sp.length() - 4, tip2Sp.length(),
                    Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            tip2.setValue(tip2Sp);
        }
    }

    /**
     * 设置订单超时计时器
     */
    private void cancleOrderTimeKeeping() {
        ExRechargeOrderCheckResponse.DataDTO value = payOrderData.getValue();
        String expireTime = value.getExpireTime();
        long cancleOrderDifference = getDifferenceTimeByNow(expireTime);
        if (cancleOrderDifference <= 0) {
            if (!canonicalName.equals(ExTransferConfirmFragment.class.getCanonicalName())) {
                toFail();
            }
            return;
        }

        Disposable disposable = (Disposable) Flowable.intervalRange(0, cancleOrderDifference, 0, 1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(aLong -> {
                    long l = cancleOrderDifference - aLong;
                    String formatTime = formatSeconds(l);
                    leftTimeData.setValue("剩余支付时间：" + formatTime);

                    String str1 = "等待匹配中，于";
                    String str2 = "后自动取消";
                    SpannableString spannableString = new SpannableString(str1 + formatTime + str2);
                    ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(getApplication().getResources().getColor(R.color.clr_purple_02));
                    spannableString.setSpan(foregroundColorSpan, str1.length(), str1.length() + formatTime.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                    pairedTimeData.setValue(spannableString);

                    //轮训三秒间隔
                    if (aLong % 3 == 0) {
                        checkOrder();
                    }
                })
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        if (!canonicalName.equals(ExTransferConfirmFragment.class.getCanonicalName())) {
                            toFail();
                        }
                    }
                })
                .subscribe();
        addSubscribe(disposable);
    }

    /**
     * 设置订单匹配计时器
     */
    private void cancleWaitTimeKeeping() {
        ExRechargeOrderCheckResponse.DataDTO value = payOrderData.getValue();
        if (value.getAllowCancelWait() == 1) {
            String cancelWaitTime = value.getCancelWaitTime();
            long cancelWaitDifference = getDifferenceTimeByNow(cancelWaitTime);
            if (cancelWaitDifference <= 0) {
                cancleOrderWaitStatus.setValue(false);
                return;
            }

            Disposable disposable = (Disposable) Flowable.intervalRange(0, cancelWaitDifference, 0, 1, TimeUnit.SECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnNext(aLong -> {
                        long l = cancelWaitDifference - aLong;
                        waitTime.setValue(formatSeconds(l));
                    })
                    .doOnComplete(new Action() {
                        @Override
                        public void run() throws Exception {
                            waitTime.setValue("");
                            cancleOrderWaitStatus.setValue(false);
                        }
                    })
                    .subscribe();
            addSubscribe(disposable);
        }
    }

    /**
     * 设置客服提示计时器
     */
    public void serviceChatTimeKeeping() {
        Flowable.intervalRange(0, 30, 0, 1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        if (serviceChatFlow != null) {
                            serviceChatFlow.showTip();
                        }
                    }
                })
                .subscribe();
    }

    /**
     * 查看当前订单信息
     */
    public void checkOrder() {

        ExRechargeOrderCheckResponse.DataDTO pOrderData = payOrderData.getValue();
        ExCreateOrderRequest cOrderData = createOrderInfoData.getValue();
        if (pOrderData == null || cOrderData == null) {
            return;
        }

        ExRechargeOrderCheckRequest request = new ExRechargeOrderCheckRequest(cOrderData.getPid());
        Disposable disposable = (Disposable) model.rechargeOrderCheck(request)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<ExRechargeOrderCheckResponse>() {
                    @Override
                    public void onResult(ExRechargeOrderCheckResponse vo) {
                        CfLog.d(vo.toString());

                        ExRechargeOrderCheckResponse.DataDTO data = vo.getData();
                        if (data != null) {
                            payOrderData.setValue(data);

                            checkCancleState();
                            checkCancleWaitState();
                            checkOrderStatus();
                        }
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
     * 检查订单状态
     */
    private void checkOrderStatus() {
        ExRechargeOrderCheckResponse.DataDTO data = payOrderData.getValue();
        String status = data.getStatus();
        switch (status) {
            case "11": //系统查核中
                if (bankInfoData.getValue() == null) {
                    ExBankInfoResponse bankInfo = new ExBankInfoResponse();
                    bankInfo.setBankAccount(data.getBankAccount());
                    bankInfo.setBankArea(data.getBankArea());
                    bankInfo.setBankCode(data.getBankCode());
                    bankInfo.setBankName(data.getBankName());
                    bankInfo.setBankAccountName(data.getBankAccountName());
                    bankInfo.setMerchantOrder(data.getMerchantOrder());
                    bankInfo.setPayAmount(data.getPayAmount());
                    bankInfo.setAllowCancel(data.getAllowCancel());
                    bankInfo.setAllowCancelTime(data.getAllowCancelTime());
                    bankInfo.setExpireTime(data.getExpireTime());

                    bankInfoData.setValue(bankInfo);
                }

                if (canonicalName.equals(ExTransferCommitFragment.class.getCanonicalName())) {
                    //已确认金额
                    if (data.getPayAmountStatus().equals("1")) {
                        toPayee();
                    }
                }
                break;
            case "14": // 回单审核中
                if (bankInfoData.getValue() == null) {
                    ExBankInfoResponse bankInfo = new ExBankInfoResponse();
                    bankInfo.setBankAccount(data.getBankAccount());
                    bankInfo.setBankArea(data.getBankArea());
                    bankInfo.setBankCode(data.getBankCode());
                    bankInfo.setBankName(data.getBankName());
                    bankInfo.setBankAccountName(data.getBankAccountName());
                    bankInfo.setMerchantOrder(data.getMerchantOrder());
                    bankInfo.setPayAmount(data.getPayAmount());
                    bankInfo.setAllowCancel(data.getAllowCancel());
                    bankInfo.setAllowCancelTime(data.getAllowCancelTime());
                    bankInfo.setExpireTime(data.getExpireTime());

                    bankInfoData.setValue(bankInfo);
                }
                break;
            case "03": //失败
                toFail();
                break;
            case "00": //成功
                toSuccess();
                break;
            case "13": //配对中
                break;
        }
    }

    /**
     * 检查是否可以取消订单
     */
    private void checkCancleState() {
        ExRechargeOrderCheckResponse.DataDTO pvalue = payOrderData.getValue();
        if (pvalue.getStatus().equals("11") && pvalue.getAllowCancel() == 1) {
            if (getDifferenceTimeByNow(pvalue.getAllowCancelTime()) > 0) {
                cancleOrderStatus.setValue(false);
                CfLog.i(pvalue.getAllowCancelTime()+"当时的时间1"+Calendar.getInstance().getTime());
            } else {
                CfLog.i(pvalue.getAllowCancelTime()+"当时的时间2"+Calendar.getInstance().getTime());
                cancleOrderStatus.setValue(true);
            }
        } else {
            cancleOrderStatus.setValue(false);
        }
    }

    /**
     * 检查是否可以取消订单匹配
     */
    private void checkCancleWaitState() {
        ExRechargeOrderCheckResponse.DataDTO pvalue = payOrderData.getValue();
        if (pvalue == null) {
            return;
        }
        if (pvalue.getStatus().equals("13") && pvalue.getAllowCancelWait() == 1) {
            if (getDifferenceTimeByNow(pvalue.getCancelWaitTime()) > 0) {
                cancleOrderWaitStatus.setValue(true);
            } else {
                cancleOrderWaitStatus.setValue(false);
            }
        } else {
            cancleOrderWaitStatus.setValue(false);
        }
    }

    /**
     * 取消订单
     */
    public void cancleOrder() {
        ExRechargeOrderCheckResponse.DataDTO pOrderData = payOrderData.getValue();
        ExCreateOrderRequest cOrderData = createOrderInfoData.getValue();
        if (pOrderData == null || cOrderData == null) {
            return;
        }

        if (mActivity != null && mActivity.get() != null) {
            if (loadingDialog != null) {
                loadingDialog.dismiss();
            }
            loadingDialog = new XPopup.Builder(mActivity.get())
                    .dismissOnTouchOutside(false)
                    .dismissOnBackPressed(true)
                    .asCustom(new LoadingDialog(mActivity.get()))
                    .show();
            loadingDialog.show();
        }

        ExOrderCancelRequest request = new ExOrderCancelRequest(cOrderData.getPid(), pOrderData.getPlatformOrder());

        Disposable disposable = (Disposable) model.cancelOrderProcess(request)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<BaseResponse>() {
                    @Override
                    public void onResult(BaseResponse response) {
                        if (loadingDialog != null) {
                            loadingDialog.dismiss();
                        }

                        toFail();
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        if (loadingDialog != null) {
                            loadingDialog.dismiss();
                        }
                    }

                    @Override
                    public void onFail(BusinessException t) {
                        super.onFail(t);
                        if (loadingDialog != null) {
                            loadingDialog.dismiss();
                        }
                    }
                });

        addSubscribe(disposable);
    }

    /**
     * 取消等待
     */
    public void cancleWait() {
        ExRechargeOrderCheckResponse.DataDTO pOrderData = payOrderData.getValue();
        ExCreateOrderRequest cOrderData = createOrderInfoData.getValue();
        if (pOrderData == null || cOrderData == null) {
            return;
        }

        if (mActivity != null && mActivity.get() != null) {
            if (loadingDialog != null) {
                loadingDialog.dismiss();
            }
            loadingDialog = new XPopup.Builder(mActivity.get())
                    .dismissOnTouchOutside(false)
                    .dismissOnBackPressed(true)
                    .asCustom(new LoadingDialog(mActivity.get()))
                    .show();
            loadingDialog.show();
        }

        ExOrderCancelRequest request = new ExOrderCancelRequest(cOrderData.getPid(), pOrderData.getPlatformOrder());

        Disposable disposable = (Disposable) model.cancelOrderWait(request)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<BaseResponse>() {
                    @Override
                    public void onResult(BaseResponse response) {
                        if (loadingDialog != null) {
                            loadingDialog.dismiss();
                        }
                        pairedTimeStatus.setValue(true);
                        waitTime.setValue("");
                        cancleOrderWaitStatus.setValue(false);
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        if (loadingDialog != null) {
                            loadingDialog.dismiss();
                        }
                    }

                    @Override
                    public void onFail(BusinessException t) {
                        super.onFail(t);
                        if (loadingDialog != null) {
                            loadingDialog.dismiss();
                        }
                    }
                });

        addSubscribe(disposable);
    }

    /**
     * 上传凭证
     */
    public void uploadVoucher() {
        ExRechargeOrderCheckResponse.DataDTO pOrderData = payOrderData.getValue();
        ExCreateOrderRequest cOrderData = createOrderInfoData.getValue();
        if (pOrderData == null || cOrderData == null) {
            return;
        }

        if (voucher.getValue() == null) {
            ToastUtils.show("请提交回执单", ToastUtils.ShowType.Default);
            return;
        }

        if (TextUtils.isEmpty(bankCodeOfPayment.getValue())) {
            ToastUtils.show("请选择付款银行", ToastUtils.ShowType.Default);
            return;
        }

        if (TextUtils.isEmpty(bankNumberOfPayment.getValue())) {
            ToastUtils.show("请输入付款账号", ToastUtils.ShowType.Default);
            return;
        }

        if (mActivity != null && mActivity.get() != null) {
            if (loadingDialog != null) {
                loadingDialog.dismiss();
            }
            loadingDialog = new XPopup.Builder(mActivity.get())
                    .dismissOnTouchOutside(false)
                    .dismissOnBackPressed(true)
                    .asCustom(new LoadingDialog(mActivity.get()))
                    .show();
            loadingDialog.show();
        }

        ExReceiptUploadRequest request = new ExReceiptUploadRequest();
        request.setPid(cOrderData.getPid());
        request.setReceipt(ImageUploadUtil.bitmapToString(voucher.getValue().getPath()));
        request.setPlatformOrder(pOrderData.getPlatformOrder());
        request.setPayBankCode(bankCodeOfPayment.getValue());
        request.setPayAccount(bankNumberOfPayment.getValue());
        request.setPayName(cOrderData.getPayName());

        Disposable disposable = (Disposable) model.rechargeReceiptUpload(request)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<ExReceiptUploadResponse>() {
                    @Override
                    public void onResult(ExReceiptUploadResponse response) {
                        if (loadingDialog != null) {
                            loadingDialog.dismiss();
                        }

                        toConfirm();
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        if (loadingDialog != null) {
                            loadingDialog.dismiss();
                        }
                    }
                    @Override
                    public void onFail(BusinessException t) {
                        super.onFail(t);
                        if (loadingDialog != null) {
                            loadingDialog.dismiss();
                        }
                    }
                });

        addSubscribe(disposable);
    }

    /**
     * 上传图片OCR识别
     */
    public void ocr() {
        ExRechargeOrderCheckResponse.DataDTO pOrderData = payOrderData.getValue();
        ExCreateOrderRequest cOrderData = createOrderInfoData.getValue();
        if (pOrderData == null || cOrderData == null || voucher.getValue() == null) {
            return;
        }

        String imageBase64 = ImageUploadUtil.bitmapToString(voucher.getValue().getPath());

        if (TextUtils.isEmpty(imageBase64)) {
            ToastUtils.show("图片无法识别，请重选", ToastUtils.ShowType.Default);
            return;
        }

        if (mActivity != null && mActivity.get() != null) {
            if (loadingDialog != null) {
                loadingDialog.dismiss();
            }
            loadingDialog = new XPopup.Builder(mActivity.get())
                    .dismissOnTouchOutside(false)
                    .dismissOnBackPressed(true)
                    .asCustom(new LoadingDialog(mActivity.get()))
                    .show();
            loadingDialog.show();
        }

        ExReceiptocrRequest request = new ExReceiptocrRequest();
        request.setPid(cOrderData.getPid());
        request.setReceipt(imageBase64);
        request.setPlatformOrder(pOrderData.getPlatformOrder());

        Disposable disposable = (Disposable) model.rechargeReceiptOCR(request)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<ExReceiptocrResponse>() {
                    @Override
                    public void onResult(ExReceiptocrResponse response) {
                        if (loadingDialog != null) {
                            loadingDialog.dismiss();
                        }

                        showBankEdit.setValue(true);

                        if (response != null) {
                            bankCodeOfPayment.setValue(null);
                            bankNameOfPayment.setValue(null);
                            bankNumberOfPayment.setValue(null);

                            bankCodeOfPayment.setValue(response.getBankcode());
                            bankNameOfPayment.setValue(getBankNameByCode(response.getBankcode()));
                            String payAccount = response.getPayAccount();

                            if (!TextUtils.isEmpty(payAccount) && isNumeric(payAccount)) {
                                bankNumberOfPayment.setValue(payAccount);
                            }
                        } else {
                            ToastUtils.show("图片无法识别，请重选", ToastUtils.ShowType.Default);
                        }

                    }
                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        if (loadingDialog != null) {
                            loadingDialog.dismiss();
                        }
                        showBankEdit.setValue(true);
                    }

                    @Override
                    public void onFail(BusinessException t) {
                        super.onFail(t);
                        if (loadingDialog != null) {
                            loadingDialog.dismiss();
                        }
                        showBankEdit.setValue(true);
                    }
                });

        addSubscribe(disposable);
    }

    /**
     * 判断是否全为数字
     */
    private boolean isNumeric(String str) {
        // 使用正则表达式判断是否全为数字
        return str.matches("\\d+");
    }

    /**
     * 通过bankCode获取bankName
     */
    private String getBankNameByCode(String bankCode) {
        ExRechargeOrderCheckResponse.DataDTO pvalue = payOrderData.getValue();
        if (pvalue == null || pvalue.getOpBankList() == null) {
            return "";
        }

        String bankName = "";
        ExRechargeOrderCheckResponse.DataDTO.OpBankListDTO opBankList = pvalue.getOpBankList();
        ArrayList<RechargeVo.OpBankListDTO.BankInfoDTO> bankInfoDTOS = new ArrayList<>();
        if (opBankList.getHot() != null) {
            bankInfoDTOS.addAll(opBankList.getHot());
        }
        if (opBankList.getOthers() != null) {
            bankInfoDTOS.addAll(opBankList.getOthers());
        }
        if (opBankList.getUsed() != null) {
            bankInfoDTOS.addAll(opBankList.getUsed());
        }
        if (opBankList.getTop() != null) {
            bankInfoDTOS.addAll(opBankList.getTop());
        }


        for (RechargeVo.OpBankListDTO.BankInfoDTO b : bankInfoDTOS) {
            if (b.getBankCode().equals(bankCode)) {
                bankName = b.getBankName();
                break;
            }
        }
        return bankName;
    }

    /**
     * 银行选择器
     */
    public void showBankList() {

        ExRechargeOrderCheckResponse.DataDTO pvalue = payOrderData.getValue();
        if (pvalue == null) {
            return;
        }
        HashMap<String, String> map = null;
        if (pvalue.getUserBankInfo() != null) {
            String jsonString = JSON.toJSONString(pvalue.getUserBankInfo());
            map = JSON.parseObject(jsonString,
                    new TypeReference<HashMap<String, String>>() {
                    });
        }

        ExRechargeOrderCheckResponse.DataDTO.OpBankListDTO opBankList = pvalue.getOpBankList();
        ArrayList<RechargeVo.OpBankListDTO.BankInfoDTO> bankInfoDTOS = new ArrayList<>();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            RechargeVo.OpBankListDTO.BankInfoDTO bankInfoDTO = new RechargeVo.OpBankListDTO.BankInfoDTO();
            bankInfoDTO.setBankCode(entry.getKey());
            bankInfoDTO.setBankName(entry.getValue());
            bankInfoDTOS.add(bankInfoDTO);
        }

        RechargeVo.OpBankListDTO bankSearchData = new RechargeVo.OpBankListDTO();
        bankSearchData.setHot(opBankList.getHot());
        bankSearchData.setOthers(opBankList.getOthers());
        bankSearchData.setTop(opBankList.getTop());
        bankSearchData.setUsed(opBankList.getUsed());
        bankSearchData.setmBind(bankInfoDTOS);

        BankPickDialogFragment.show(mActivity.get(), bankSearchData)
                .setOnPickListner(new BankPickDialogFragment.onPickListner() {
                    @Override
                    public void onPick(BankPickModel model) {
                        if (TextUtils.isEmpty(model.getBankCode())) {
                            bankCodeOfPayment.setValue(model.getBankId());
                        } else {
                            bankCodeOfPayment.setValue(model.getBankCode());
                        }
                        bankNameOfPayment.setValue(model.getBankName());
                    }
                });
    }

    /**
     * 获取现在距离时间过去了多久
     */
    private long getDifferenceTimeByNow(String time) {
        long differenceInSeconds = 0;
        Date now = null;
        Date end = null;
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            now = Calendar.getInstance().getTime();
            end = format.parse(time);
            long t = now.getTime() - end.getTime();
            if (t < 0) {
                differenceInSeconds = Math.abs(t) / 1000;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return differenceInSeconds;
    }

    private long getDifferenceTime(String start, String end) {
        long differenceInSeconds = 0;
        Date startDate = null;
        Date endDate = null;
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            startDate = format.parse(start);
            endDate = format.parse(end);
            long t = startDate.getTime() - endDate.getTime();
            if (t < 0) {
                differenceInSeconds = Math.abs(t) / 1000;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return differenceInSeconds;
    }

    public void setActivity(FragmentActivity mActivity) {
        this.mActivity = new WeakReference<>(mActivity);
    }

    /**
     * 设置当前客服浮窗
     */
    public void setFlowWindow(Comm100ChatWindows chatWindows) {
        this.serviceChatFlow = chatWindows;
    }

    /**
     * 解析时间
     */
    public String formatSeconds(long seconds) {
        long minutes = seconds / 60;
        long remainingSeconds = seconds % 60;
        return String.format("%02d:%02d", minutes, remainingSeconds);
    }

    public void toPayee() {
        startContainerActivity(RouterFragmentPath.Transfer.PAGER_TRANSFER_EX_PAYEE);
    }

    public void toConfirm() {
        startContainerActivity(RouterFragmentPath.Transfer.PAGER_TRANSFER_EX_CONFIRM);
    }

    public void toVoucher() {
        startContainerActivity(RouterFragmentPath.Transfer.PAGER_TRANSFER_EX_VOUCHER);
    }

    public void toFail() {
        startContainerActivity(RouterFragmentPath.Transfer.PAGER_TRANSFER_EX_FAIL);
        close();
    }

    public void toSuccess() {
        startContainerActivity(RouterFragmentPath.Transfer.PAGER_TRANSFER_EX_SUCCESS);
        close();
    }

    /**
     * 回执单示例
     */
    public void showExample() {
        if (isActivityNull()) {
            return;
        }

        ExSlipExampleDialogFragment.show(mActivity.get());
    }

    /**
     * 图片选择
     */
    public void gotoSelectMedia() {

        if (isActivityNull()) {
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                Intent appIntent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                appIntent.setData(Uri.parse("package:" + mActivity.get().getPackageName()));
                try {
                    mActivity.get().startActivity(appIntent);
                } catch (ActivityNotFoundException ex) {
                    ex.printStackTrace();
                    Intent allFileIntent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                    mActivity.get().startActivity(allFileIntent);
                }
                return;
            }
        }
        PictureSelector.create(mActivity.get())
                .openGallery(SelectMimeType.ofImage())
                .isDisplayCamera(false)
                .setMaxSelectNum(1)
                .setImageEngine(GlideEngine.createGlideEngine())
                .setCompressEngine(ImageFileCompressEngine.create())
                .forResult(new OnResultCallbackListener<LocalMedia>() {
                    @Override
                    public void onResult(ArrayList<LocalMedia> result) {
                        if (result != null) {
                            for (int i = 0; i < result.size(); i++) {
                                String imageRealPathString = result.get(i).getCompressPath();
                                if (TextUtils.isEmpty(imageRealPathString)) {
                                    imageRealPathString = result.get(i).getRealPath();
                                }

                                if (PictureMimeType.isContent(imageRealPathString)) {
                                    voucher.setValue(Uri.parse(imageRealPathString));
                                } else {
                                    voucher.setValue(Uri.fromFile(new File(imageRealPathString)));
                                }
                                CfLog.i("获取图片地址是 uri ====== " + voucher.getValue());
                            }
                            ocr();
                        }
                    }

                    @Override
                    public void onCancel() {

                    }
                });
    }

    private Boolean isActivityNull() {
        if (mActivity == null || mActivity.get() == null) {
            Stack<Activity> activityStack = AppManager.getActivityStack();
            for (Activity activity : activityStack) {
                FragmentActivity fa = (FragmentActivity) activity;
                for (Fragment fragment : fa.getSupportFragmentManager().getFragments()) {
                    if (fragment.getClass().getCanonicalName().equals(canonicalName)) {
                        this.mActivity = new WeakReference<>(fa);
                    }
                }
            }
            if (mActivity == null || mActivity.get() == null) {
                return true;
            }
            return false;
        } else {
            return false;
        }
    }

    public void setRechargeViewModel(RechargeViewModel rechargeViewModel) {
        rechargeLiveData.setValue(rechargeViewModel);

        //外部传入的极速渠道名称
        String expTitleValue = rechargeViewModel.liveDataExpTitle.getValue();
        if (!TextUtils.isEmpty(expTitleValue)) {
            titleLiveData.setValue(expTitleValue);
            rechargeViewModel.liveDataExpTitle.setValue(null);
            return;
        }

        //充值页当前选择的渠道
        RechargeVo rechargeVo = rechargeViewModel.curRechargeLiveData.getValue();
        if (rechargeVo != null) {
            //设置标题
            titleLiveData.setValue(rechargeVo.title);
        }
    }

    /**
     * 清理资源
     */
    public void close() {
        if (getmCompositeDisposable() != null) {
            getmCompositeDisposable().clear();
        }
    }

    /**
     * 清除数据
     */
    private void clear() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
            loadingDialog = null;
        }

        if (mActivity != null) {
            mActivity.clear();
            mActivity = null;
        }

        if (serviceChatFlow != null) {
            serviceChatFlow.removeView();
            serviceChatFlow = null;
        }

        if (rechargeLiveData != null && rechargeLiveData.getValue() != null) {
            rechargeLiveData.setValue(null);
        }

        canonicalName = null;
        showBankEdit.setValue(false);
        cancleOrderWaitStatus.setValue(true);
        cancleOrderStatus.setValue(false);
        bankNumberOfPayment.setValue(null);
        bankNameOfPayment.setValue(null);
        bankCodeOfPayment.setValue(null);
        bankInfoData.setValue(null);
        createOrderInfoData.setValue(null);
        payOrderData.setValue(null);
        voucher.setValue(null);
        waitTime.setValue(null);
        deadlinesData.setValue(null);
        leftTimeData.setValue(null);
        tip1.setValue(null);
        tip2.setValue(null);
    }

    /**
     * 清理所有极速流程页面
     */
    public void finish() {
        close();

        clear();

        Stack<Activity> activityStack = AppManager.getActivityStack();
        for (Activity activity : activityStack) {
            FragmentActivity fa = (FragmentActivity) activity;
            for (Fragment fragment : fa.getSupportFragmentManager().getFragments()) {
                if (fragment.getClass().getCanonicalName().equals(ExTransferCommitFragment.class.getCanonicalName())) {
                    fa.finish();
                }
                if (fragment.getClass().getCanonicalName().equals(ExTransferPayeeFragment.class.getCanonicalName())) {
                    fa.finish();
                }
                if (fragment.getClass().getCanonicalName().equals(ExTransferVoucherFragment.class.getCanonicalName())) {
                    fa.finish();
                }
                if (fragment.getClass().getCanonicalName().equals(ExTransferFailFragment.class.getCanonicalName())) {
                    fa.finish();
                }
                if (fragment.getClass().getCanonicalName().equals(ExTransferSuccessFragment.class.getCanonicalName())) {
                    fa.finish();
                }
                if (fragment.getClass().getCanonicalName().equals(ExTransferConfirmFragment.class.getCanonicalName())) {
                    fa.finish();
                }
            }
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        clear();
    }

    /**
     * 跳过极速引导引导
     */
    public void skipGuide(){
        Disposable disposable = (Disposable) model.skipGuide()
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<ExRechargeOrderCheckResponse>() {
                    @Override
                    public void onResult(ExRechargeOrderCheckResponse vo) {
                        CfLog.d(vo.toString());

                        if (loadingDialog != null) {
                            loadingDialog.dismiss();
                        }

                        ExRechargeOrderCheckResponse.DataDTO data = vo.getData();
                        if (data != null) {

                            payOrderData.setValue(data);

                            initOrder(data);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                        super.onError(t);
                        if (loadingDialog != null) {
                            loadingDialog.dismiss();
                        }
                    }

                    @Override
                    public void onFail(BusinessException t) {
                        super.onFail(t);
                        if (loadingDialog != null) {
                            loadingDialog.dismiss();
                        }
                    }
                });

        addSubscribe(disposable);
    }

}

