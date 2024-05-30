package com.xtree.recharge.ui.viewmodel;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.text.TextUtils;

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
import com.xtree.base.net.HttpCallBack;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.base.utils.CfLog;
import com.xtree.base.utils.ImageUploadUtil;
import com.xtree.base.widget.GlideEngine;
import com.xtree.base.widget.ImageFileCompressEngine;
import com.xtree.base.widget.LoadingDialog;
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
import com.xtree.recharge.ui.fragment.extransfer.ExTransferVoucherFragment;
import com.xtree.recharge.ui.model.BankPickModel;
import com.xtree.recharge.vo.RechargeVo;

import org.reactivestreams.Subscription;

import java.io.File;
import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Stack;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import me.xtree.mvvmhabit.base.AppManager;
import me.xtree.mvvmhabit.base.BaseViewModel;
import me.xtree.mvvmhabit.http.BaseResponse;
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
    public MutableLiveData<String> waitTime = new MutableLiveData<>();
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
    private WeakReference<FragmentActivity> mActivity = null;
    public String canonicalName;

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

                            ExBankInfoResponse bankInfo = new ExBankInfoResponse();
                            bankInfo.setBankAccount(data.getBankAccount());
                            bankInfo.setBankArea(data.getBankArea());
                            bankInfo.setBankCode(data.getBankCode());
                            bankInfo.setBankAccountName(data.getBankAccountName());
                            bankInfo.setMerchantOrder(data.getMerchantOrder());
                            bankInfo.setPayAmount(data.getPayAmount());
                            bankInfo.setAllowCancel(data.getAllowCancel());
                            bankInfo.setAllowCancelTime(data.getAllowCancelTime());
                            bankInfo.setExpireTime(data.getExpireTime());
                            bankInfoData.setValue(bankInfo);

                            deadlinesData.setValue("请于 " + data.getExpireTime() + " 内完成支付");
                            cancleWaitTimeKeeping();
                            cancleOrderTimeKeeping();
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
                            String status = data.getStatus();
                            switch (status) {
                                case "11":
                                    if (bankInfoData.getValue() == null) {
                                        ExBankInfoResponse bankInfo = new ExBankInfoResponse();
                                        bankInfo.setBankAccount(data.getBankAccount());
                                        bankInfo.setBankArea(data.getBankArea());
                                        bankInfo.setBankCode(data.getBankCode());
                                        bankInfo.setBankAccountName(data.getBankAccountName());
                                        bankInfo.setMerchantOrder(data.getMerchantOrder());
                                        bankInfo.setPayAmount(data.getPayAmount());
                                        bankInfo.setAllowCancel(data.getAllowCancel());
                                        bankInfo.setAllowCancelTime(data.getAllowCancelTime());
                                        bankInfo.setExpireTime(data.getExpireTime());

                                        bankInfoData.setValue(bankInfo);
                                    }

                                    if (canonicalName.equals(ExTransferCommitFragment.class.getCanonicalName())) {
                                        toPayee();
                                    }
                                    break;
                                case "12":
                                    if (bankInfoData.getValue() == null) {
                                        ExBankInfoResponse bankInfo = new ExBankInfoResponse();
                                        bankInfo.setBankAccount(data.getBankAccount());
                                        bankInfo.setBankArea(data.getBankArea());
                                        bankInfo.setBankCode(data.getBankCode());
                                        bankInfo.setBankAccountName(data.getBankAccountName());
                                        bankInfo.setMerchantOrder(data.getMerchantOrder());
                                        bankInfo.setPayAmount(data.getPayAmount());
                                        bankInfo.setAllowCancel(data.getAllowCancel());
                                        bankInfo.setAllowCancelTime(data.getAllowCancelTime());
                                        bankInfo.setExpireTime(data.getExpireTime());

                                        bankInfoData.setValue(bankInfo);
                                    }
                                    break;
                                case "01":
                                case "02":
                                case "03":
                                case "04":
                                case "06":
                                case "061":
                                case "065":
                                case "066":
                                case "07":
                                case "08":
                                case "09":
                                case "10":
                                    toFail();
                                    break;
                            }
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

    private void cancleOrderTimeKeeping() {
        ExRechargeOrderCheckResponse.DataDTO value = payOrderData.getValue();
        String expireTime = value.getExpireTime();
        long cancleOrderDifference = getDifferenceTimeByNow(expireTime);
        Disposable disposable = (Disposable) Flowable.intervalRange(0, cancleOrderDifference, 0, 1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(aLong -> {
                    long l = cancleOrderDifference - aLong;
                    leftTimeData.setValue("剩余支付时间：" + formatSeconds(l));

                    checkOrder();
                })
                .doOnComplete(this::toFail)
                .subscribe();
        addSubscribe(disposable);
    }

    private void cancleWaitTimeKeeping() {
        ExRechargeOrderCheckResponse.DataDTO value = payOrderData.getValue();
        if (value.getAllowCancelWait() == 1) {
            String cancelWaitTime = value.getCancelWaitTime();
            long cancelWaitDifference = getDifferenceTimeByNow(cancelWaitTime);
            Disposable disposable = (Disposable) Flowable.intervalRange(0, cancelWaitDifference, 0, 1, TimeUnit.SECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnNext(aLong -> {
                        long l = cancelWaitDifference - aLong;
                        waitTime.setValue(formatSeconds(l));
                    })
                    .doOnComplete(this::toFail)
                    .subscribe();
            addSubscribe(disposable);
        }
    }

    private long getDifferenceTimeByNow(String time) {
        long differenceInSeconds = 0;
        Date now = null;
        Date end = null;
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            now = Calendar.getInstance().getTime();
            end = format.parse(time);
            differenceInSeconds = Math.abs((now.getTime() - end.getTime()) / 1000);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return differenceInSeconds;
    }

    public void setActivity(FragmentActivity mActivity) {
        this.mActivity = new WeakReference<>(mActivity);
    }

    public String formatSeconds(long seconds) {
        long minutes = seconds / 60;
        long remainingSeconds = seconds % 60;
        return String.format("%02d:%02d", minutes, remainingSeconds);
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

        ExOrderCancelRequest request = new ExOrderCancelRequest(cOrderData.getPid(), pOrderData.getPlatformOrder());

        Disposable disposable = (Disposable) model.cancelOrderWait(request)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(new Consumer<Subscription>() {
                    @Override
                    public void accept(Subscription subscription) throws Exception {
                        LoadingDialog.show(mActivity.get());
                    }
                })
                .subscribeWith(new HttpCallBack<BaseResponse>() {
                    @Override
                    public void onResult(BaseResponse response) {
                        toFail();
                    }
                });

        addSubscribe(disposable);
    }

    /**
     * 取消订单
     */
    public void cancleWait() {
        ExRechargeOrderCheckResponse.DataDTO pOrderData = payOrderData.getValue();
        ExCreateOrderRequest cOrderData = createOrderInfoData.getValue();
        if (pOrderData == null || cOrderData == null) {
            return;
        }

        ExOrderCancelRequest request = new ExOrderCancelRequest(cOrderData.getPid(), pOrderData.getPlatformOrder());

        Disposable disposable = (Disposable) model.cancelOrderWait(request)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(new Consumer<Subscription>() {
                    @Override
                    public void accept(Subscription subscription) throws Exception {
                        LoadingDialog.show(mActivity.get());
                    }
                })
                .subscribeWith(new HttpCallBack<BaseResponse>() {
                    @Override
                    public void onResult(BaseResponse response) {
                        toFail();
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

        if (bankNumberOfPayment.getValue() == null) {
            ToastUtils.show("请输入付款账号", ToastUtils.ShowType.Default);
            return;
        }

        if (bankCodeOfPayment.getValue() == null) {
            ToastUtils.show("请选择付款银行", ToastUtils.ShowType.Default);
            return;
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
                .doOnSubscribe(new Consumer<Subscription>() {
                    @Override
                    public void accept(Subscription subscription) throws Exception {
                        LoadingDialog.show(mActivity.get());
                    }
                })
                .subscribeWith(new HttpCallBack<ExReceiptUploadResponse>() {
                    @Override
                    public void onResult(ExReceiptUploadResponse response) {
                        toConfirm();
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

        ExReceiptocrRequest request = new ExReceiptocrRequest();
        request.setPid(cOrderData.getPid());
        request.setReceipt(imageBase64);
        request.setPlatformOrder(pOrderData.getPlatformOrder());

        Disposable disposable = (Disposable) model.rechargeReceiptOCR(request)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(new Consumer<Subscription>() {
                    @Override
                    public void accept(Subscription subscription) throws Exception {
                        LoadingDialog.show(mActivity.get());
                    }
                })
                .subscribeWith(new HttpCallBack<ExReceiptocrResponse>() {
                    @Override
                    public void onResult(ExReceiptocrResponse response) {
                        if (response != null) {
                            bankCodeOfPayment.setValue(response.getBankcode());
                            bankNameOfPayment.setValue(getBankNameByCode(response.getBankcode()));
                            bankNumberOfPayment.setValue(response.getPayAccount());
                        }
                    }
                });

        addSubscribe(disposable);
    }

    private String getBankNameByCode(String bankCode) {
        ExRechargeOrderCheckResponse.DataDTO pvalue = payOrderData.getValue();
        if (pvalue == null) {
            return "";
        }

        String bankName = "";
        ExRechargeOrderCheckResponse.DataDTO.OpBankListDTO opBankList = pvalue.getOpBankList();
        ArrayList<RechargeVo.OpBankListDTO.BankInfoDTO> bankInfoDTOS = new ArrayList<>();
        bankInfoDTOS.addAll(opBankList.getHot());
        bankInfoDTOS.addAll(opBankList.getOthers());
        bankInfoDTOS.addAll(opBankList.getUsed());
        bankInfoDTOS.addAll(opBankList.getTop());

        for (RechargeVo.OpBankListDTO.BankInfoDTO b : bankInfoDTOS) {
            if (b.getBankCode().equals(bankCode)) {
                bankName = b.getBankName();
                break;
            }
        }
        return bankName;
    }

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

        RechargeVo.OpBankListDTO bankSearchData = new RechargeVo.OpBankListDTO();
        bankSearchData.setHot(opBankList.getHot());
        bankSearchData.setOthers(opBankList.getOthers());
        bankSearchData.setTop(opBankList.getTop());
        bankSearchData.setUsed(opBankList.getUsed());
        opBankList.setmBind(map);

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
     * 确认付款
     */
    public void confirmationPayment() {

    }

    public void toPayee() {
        startContainerActivity(RouterFragmentPath.Transfer.PAGER_TRANSFER_EX_PAYEE);
    }

    public void toConfirm() {
        startContainerActivity(RouterFragmentPath.Transfer.PAGER_TRANSFER_EX_CONFIRM);
        Stack<Activity> activityStack = AppManager.getActivityStack();
        for (Activity activity : activityStack) {
            FragmentActivity fa = (FragmentActivity) activity;
            for (Fragment fragment : fa.getSupportFragmentManager().getFragments()) {
                if (fragment.getClass().getCanonicalName().equals(ExTransferCommitFragment.class.getCanonicalName())) {
                    fa.finish();
                }
                if (fragment.getClass().getCanonicalName().equals(ExTransferConfirmFragment.class.getCanonicalName())) {
                    fa.finish();
                }
                if (fragment.getClass().getCanonicalName().equals(ExTransferPayeeFragment.class.getCanonicalName())) {
                    fa.finish();
                }
                if (fragment.getClass().getCanonicalName().equals(ExTransferVoucherFragment.class.getCanonicalName())) {
                    fa.finish();
                }
            }
        }
    }

    public void toVoucher() {
        startContainerActivity(RouterFragmentPath.Transfer.PAGER_TRANSFER_EX_VOUCHER);
    }

    public void toFail() {
        startContainerActivity(RouterFragmentPath.Transfer.PAGER_TRANSFER_EX_FAIL);
        close();
        Stack<Activity> activityStack = AppManager.getActivityStack();
        for (Activity activity : activityStack) {
            FragmentActivity fa = (FragmentActivity) activity;
            for (Fragment fragment : fa.getSupportFragmentManager().getFragments()) {
                if (fragment.getClass().getCanonicalName().equals(ExTransferCommitFragment.class.getCanonicalName())) {
                    fa.finish();
                }
                if (fragment.getClass().getCanonicalName().equals(ExTransferConfirmFragment.class.getCanonicalName())) {
                    fa.finish();
                }
                if (fragment.getClass().getCanonicalName().equals(ExTransferPayeeFragment.class.getCanonicalName())) {
                    fa.finish();
                }
                if (fragment.getClass().getCanonicalName().equals(ExTransferVoucherFragment.class.getCanonicalName())) {
                    fa.finish();
                }
            }
        }
    }

    /**
     * 回执单示例
     */
    public void showExample() {
        ExSlipExampleDialogFragment.show(mActivity.get());
    }

    /**
     * 图片选择
     */
    public void gotoSelectMedia() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                Intent getpermission = new Intent();
                getpermission.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                mActivity.get().startActivity(getpermission);
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

    public void close() {
        if (getmCompositeDisposable() != null) {
            getmCompositeDisposable().clear();
        }
    }

    public void finish() {
        close();

        Stack<Activity> activityStack = AppManager.getActivityStack();
        for (Activity activity : activityStack) {
            FragmentActivity fa = (FragmentActivity) activity;
            for (Fragment fragment : fa.getSupportFragmentManager().getFragments()) {
                if (fragment.getClass().getCanonicalName().equals(ExTransferCommitFragment.class.getCanonicalName())) {
                    fa.finish();
                }
                if (fragment.getClass().getCanonicalName().equals(ExTransferConfirmFragment.class.getCanonicalName())) {
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
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mActivity != null) {
            mActivity.clear();
            mActivity = null;
        }
    }
}

