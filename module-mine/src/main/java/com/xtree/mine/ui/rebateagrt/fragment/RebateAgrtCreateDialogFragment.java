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
import com.xtree.mine.databinding.DialogRebateagrtCreateBinding;
import com.xtree.mine.ui.rebateagrt.model.RebateAgrtDetailModel;
import com.xtree.mine.ui.rebateagrt.model.RebateAgrtSearchUserResultModel;
import com.xtree.mine.ui.rebateagrt.viewmodel.RebateAgrtCreateViewModel;
import com.xtree.mine.ui.viewmodel.factory.AppViewModelFactory;

import java.util.Objects;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import me.xtree.mvvmhabit.base.BaseDialogFragment;
import me.xtree.mvvmhabit.bus.RxBus;

/**
 * Created by KAKA on 2024/3/13.
 * Describe: 分红契约创建弹窗
 */
@Route(path = RouterFragmentPath.Mine.PAGER_REBATEAGRT_CREATE_DIALOG)
public class RebateAgrtCreateDialogFragment extends BaseDialogFragment<DialogRebateagrtCreateBinding, RebateAgrtCreateViewModel> {

    /**
     * 启动弹窗
     * @param activity 获取FragmentManager
     * @param model 入参
     */
    public static void show(FragmentActivity activity, RebateAgrtDetailModel model) {
        RxBus.getDefault().postSticky(model);
        RebateAgrtCreateDialogFragment fragment = new RebateAgrtCreateDialogFragment();
        fragment.show(activity.getSupportFragmentManager(), RebateAgrtCreateDialogFragment.class.getName());
    }
    private RebateAgrtCreateDialogFragment() {
    }

    @Override
    public void initView() {
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.dialog_rebateagrt_create;
    }

    @Override
    public RebateAgrtCreateViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getActivity().getApplication());
        return new ViewModelProvider(this, factory).get(RebateAgrtCreateViewModel.class);
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

        //用户搜索
        RxBus.getDefault().toObservable(RebateAgrtSearchUserResultModel.class).subscribe(new Observer<RebateAgrtSearchUserResultModel>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(RebateAgrtSearchUserResultModel model) {
                viewModel.setData(model);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

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
