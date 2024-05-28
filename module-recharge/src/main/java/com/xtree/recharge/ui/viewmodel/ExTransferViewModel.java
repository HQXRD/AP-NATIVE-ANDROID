package com.xtree.recharge.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;

import com.xtree.recharge.data.RechargeRepository;

import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import me.xtree.mvvmhabit.base.BaseViewModel;

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

    public MutableLiveData<String> paymentTime = new MutableLiveData<>();
    public MutableLiveData<String> waitTime = new MutableLiveData<>();

    private WeakReference<FragmentActivity> mActivity = null;

    public void initData(FragmentActivity mActivity) {
        setActivity(mActivity);

        int timeCount = 60 * 30;
        Flowable.intervalRange(0, timeCount, 0, 1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(aLong -> {
                    long l = timeCount - aLong;
                    paymentTime.setValue(formatSeconds(l));
                })
                .doOnComplete(() -> {
                })
                .subscribe();
    }

    private void setActivity(FragmentActivity mActivity) {
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
    public void cancle() {

    }

    /**
     * 上传凭证
     */
    public void uploadVoucher() {

    }

    /**
     * 确认付款
     */
    public void confirmationPayment() {

    }

    /**
     * 回执单示例
     */
    public void showExample() {

    }

}

