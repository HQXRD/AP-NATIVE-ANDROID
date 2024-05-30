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
import androidx.lifecycle.ViewModelProvider;

import com.xtree.recharge.BR;
import com.xtree.recharge.R;
import com.xtree.recharge.databinding.DialogBankPickBinding;
import com.xtree.recharge.ui.model.BankPickModel;
import com.xtree.recharge.ui.viewmodel.BankPickViewModel;
import com.xtree.recharge.ui.viewmodel.factory.AppViewModelFactory;
import com.xtree.recharge.vo.RechargeVo;

import java.util.Objects;

import me.xtree.mvvmhabit.base.BaseDialogFragment;
import me.xtree.mvvmhabit.bus.RxBus;

/**
 * Created by KAKA on 2024/5/27.
 * Describe:
 */
public class BankPickDialogFragment extends BaseDialogFragment<DialogBankPickBinding, BankPickViewModel> {

    private BankPickDialogFragment() {
    }

    /**
     * 启动弹窗
     *
     * @param activity 获取FragmentManager
     */
    public static BankPickDialogFragment show(FragmentActivity activity, RechargeVo.OpBankListDTO bankList) {
        RxBus.getDefault().postSticky(bankList);
        BankPickDialogFragment fragment = new BankPickDialogFragment();
        fragment.show(activity.getSupportFragmentManager(), BankPickDialogFragment.class.getName());
        activity.getSupportFragmentManager().executePendingTransactions();
        return fragment;
    }

    public interface onPickListner{
        void onPick(BankPickModel model);
    }

    private BankPickDialogFragment.onPickListner onPickListner;

    @Override
    public void initView() {
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.dialog_bank_pick;
    }

    @Override
    public BankPickViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(requireActivity().getApplication());
        return new ViewModelProvider(requireActivity(), factory).get(BankPickViewModel.class);
    }

    @Override
    public void initData() {
        super.initData();
        binding.setVariable(BR.model, viewModel);

        RechargeVo.OpBankListDTO bankList = RxBus.getDefault().getStickyEvent(RechargeVo.OpBankListDTO.class);
        binding.getModel().initData(bankList);

        if (onPickListner != null) {
            binding.getModel().setOnPickListner(onPickListner);
        }
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        viewModel.getUC().getFinishEvent().removeObservers(this);
        viewModel.getUC().getFinishEvent().observe(this, new androidx.lifecycle.Observer<Void>() {
            @Override
            public void onChanged(@Nullable Void v) {
                dismissAllowingStateLoss();
            }
        });
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
    }

    public void setOnPickListner(BankPickDialogFragment.onPickListner onPickListner) {
        this.onPickListner = onPickListner;
        if (viewModel != null) {
            viewModel.setOnPickListner(onPickListner);
        }
    }
}