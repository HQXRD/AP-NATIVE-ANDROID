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

import com.alibaba.android.arouter.facade.annotation.Route;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.mine.BR;
import com.xtree.mine.R;
import com.xtree.mine.databinding.DialogRebateagrtSearchuserBinding;
import com.xtree.mine.ui.rebateagrt.model.RebateAgrtDetailModel;
import com.xtree.mine.ui.rebateagrt.viewmodel.RebateAgrtSearchUserViewModel;
import com.xtree.mine.ui.viewmodel.factory.AppViewModelFactory;

import java.util.Objects;

import me.xtree.mvvmhabit.base.BaseDialogFragment;
import me.xtree.mvvmhabit.bus.RxBus;

/**
 * Created by KAKA on 2024/3/18.
 * Describe: 返水契约-创建契约-用户选择搜索
 */
@Route(path = RouterFragmentPath.Mine.PAGER_REBATEAGRT_SEARCHUSER_DIALOG)
public class RebateAgrtSearchUserDialogFragment extends BaseDialogFragment<DialogRebateagrtSearchuserBinding, RebateAgrtSearchUserViewModel> {

    /**
     * 启动弹窗
     * @param activity 获取FragmentManager
     * @param model 入参
     */
    public static void show(FragmentActivity activity, RebateAgrtDetailModel model) {
        RxBus.getDefault().postSticky(model);
        RebateAgrtSearchUserDialogFragment fragment = new RebateAgrtSearchUserDialogFragment();
        fragment.show(activity.getSupportFragmentManager(), RebateAgrtSearchUserDialogFragment.class.getName());
    }

    private RebateAgrtSearchUserDialogFragment() {
    }

    @Override
    public void initView() {
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.dialog_rebateagrt_searchuser;
    }

    @Override
    public RebateAgrtSearchUserViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getActivity().getApplication());
        return new ViewModelProvider(this, factory).get(RebateAgrtSearchUserViewModel.class);
    }

    @Override
    public void initData() {
        super.initData();
        binding.setVariable(BR.model, viewModel);
        RebateAgrtDetailModel stickyEvent = RxBus.getDefault().getStickyEvent(RebateAgrtDetailModel.class);
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
