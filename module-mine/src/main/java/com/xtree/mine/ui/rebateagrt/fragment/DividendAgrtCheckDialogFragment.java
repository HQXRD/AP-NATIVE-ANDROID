package com.xtree.mine.ui.rebateagrt.fragment;

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

import com.xtree.mine.BR;
import com.xtree.mine.R;
import com.xtree.mine.databinding.DialogDividendagrtCheckBinding;
import com.xtree.mine.ui.rebateagrt.viewmodel.DividendAgrtCheckViewModel;
import com.xtree.mine.ui.viewmodel.factory.AppViewModelFactory;
import com.xtree.mine.vo.request.DividendAgrtCheckRequest;

import java.util.Objects;

import me.xtree.mvvmhabit.base.BaseDialogFragment;
import me.xtree.mvvmhabit.bus.RxBus;

/**
 * Created by KAKA on 2024/3/19.
 * Describe: 分红契约查看dialog
 */
public class DividendAgrtCheckDialogFragment extends BaseDialogFragment<DialogDividendagrtCheckBinding, DividendAgrtCheckViewModel> {

    /**
     * 启动弹窗
     * @param activity 获取FragmentManager
     * @param model 入参
     */
    public static void show(FragmentActivity activity, DividendAgrtCheckRequest model) {
        RxBus.getDefault().postSticky(model);
        DividendAgrtCheckDialogFragment fragment = new DividendAgrtCheckDialogFragment();
        fragment.show(activity.getSupportFragmentManager(), DividendAgrtCheckDialogFragment.class.getName());
    }

    private DividendAgrtCheckDialogFragment() {
    }

    @Override
    public void initView() {
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.dialog_dividendagrt_check;
    }

    @Override
    public DividendAgrtCheckViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getActivity().getApplication());
        return new ViewModelProvider(this, factory).get(DividendAgrtCheckViewModel.class);
    }

    @Override
    public void initData() {
        super.initData();
        binding.setVariable(BR.model, viewModel);
        DividendAgrtCheckRequest stickyEvent = RxBus.getDefault().getStickyEvent(DividendAgrtCheckRequest.class);
        if (stickyEvent != null) {
            viewModel.setActivity(getActivity());
            viewModel.initData(stickyEvent);
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
}