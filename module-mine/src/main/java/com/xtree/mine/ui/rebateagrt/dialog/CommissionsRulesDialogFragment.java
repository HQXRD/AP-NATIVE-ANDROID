package com.xtree.mine.ui.rebateagrt.dialog;

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
import com.xtree.mine.databinding.DialogCommissionsRulesBinding;
import com.xtree.mine.ui.rebateagrt.viewmodel.CommissionsRulesViewModel;
import com.xtree.mine.ui.viewmodel.factory.AppViewModelFactory;

import java.util.Objects;

import me.xtree.mvvmhabit.base.BaseDialogFragment;
import me.xtree.mvvmhabit.bus.RxBus;

/**
 * Created by KAKA on 2024/4/1.
 * Describe: 佣金报表-规则展示弹窗
 */
public class CommissionsRulesDialogFragment extends BaseDialogFragment<DialogCommissionsRulesBinding, CommissionsRulesViewModel> {

    //佣金制度
    public static final int COMMISSIONS_MODE = 0x01;
    //代理制度
    public static final int AGENT_MODE = 0x02;

    private CommissionsRulesDialogFragment() {
    }

    /**
     * 启动弹窗
     *
     * @param activity 获取FragmentManager
     * @param model    入参
     */
    public static void show(FragmentActivity activity, int model) {
        RxBus.getDefault().postSticky(model);
        CommissionsRulesDialogFragment fragment = new CommissionsRulesDialogFragment();
        fragment.show(activity.getSupportFragmentManager(), CommissionsRulesDialogFragment.class.getName());
    }

    @Override
    public void initView() {
        binding.getRoot().setOnClickListener(v -> dismissAllowingStateLoss());
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().setCanceledOnTouchOutside(true);
        return R.layout.dialog_commissions_rules;
    }

    @Override
    public CommissionsRulesViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getActivity().getApplication());
        return new ViewModelProvider(this, factory).get(CommissionsRulesViewModel.class);
    }

    @Override
    public void initData() {
        super.initData();
        binding.setVariable(BR.model, viewModel);
        Integer stickyEvent = RxBus.getDefault().getStickyEvent(Integer.class);
        if (stickyEvent != null) {
            viewModel.setActivity(getActivity());
            viewModel.initData(stickyEvent);
        }
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
