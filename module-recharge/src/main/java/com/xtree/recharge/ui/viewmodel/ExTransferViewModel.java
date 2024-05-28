package com.xtree.recharge.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.xtree.recharge.data.RechargeRepository;

import java.lang.ref.WeakReference;

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

    private WeakReference<FragmentActivity> mActivity = null;

    public void initData(FragmentActivity mActivity) {
        setActivity(mActivity);
    }

    private void setActivity(FragmentActivity mActivity) {
        this.mActivity = new WeakReference<>(mActivity);
    }

}

