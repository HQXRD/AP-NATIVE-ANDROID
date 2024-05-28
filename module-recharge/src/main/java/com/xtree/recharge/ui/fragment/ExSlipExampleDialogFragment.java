package com.xtree.recharge.ui.fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.xtree.recharge.R;
import com.xtree.recharge.databinding.DialogEreplyslipExampleBinding;

import java.util.Objects;

import me.xtree.mvvmhabit.base.BaseDialogFragment;
import me.xtree.mvvmhabit.base.BaseViewModel;

/**
 * Created by KAKA on 2024/5/28.
 * Describe: 电子回单示例
 */
public class ExSlipExampleDialogFragment extends BaseDialogFragment<DialogEreplyslipExampleBinding, BaseViewModel> {

    private ExSlipExampleDialogFragment() {
    }

    /**
     * 启动弹窗
     *
     * @param activity 获取FragmentManager
     */
    public static void show(FragmentActivity activity) {
        ExSlipExampleDialogFragment fragment = new ExSlipExampleDialogFragment();
        fragment.show(activity.getSupportFragmentManager(), ExSlipExampleDialogFragment.class.getName());
    }

    @Override
    public void initView() {
        binding.ereplyslipExampleClose.setOnClickListener(v -> dismissAllowingStateLoss());
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.dialog_ereplyslip_example;
    }

    @Override
    public BaseViewModel initViewModel() {
        return new BaseViewModel(getActivity().getApplication());
    }

    @Override
    public void initData() {
        super.initData();
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = Objects.requireNonNull(getDialog()).getWindow();
        WindowManager.LayoutParams params = Objects.requireNonNull(window).getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(params);
        View decorView = window.getDecorView();
        decorView.setBackground(new ColorDrawable(Color.TRANSPARENT));
        getDialog().setCanceledOnTouchOutside(true);
    }
}