package com.xtree.mine.ui.rebateagrt.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.xtree.mine.BR;
import com.xtree.mine.R;
import com.xtree.mine.databinding.DialogDividendagrtCheckBinding;
import com.xtree.mine.databinding.DialogRebateagrtCreateBinding;
import com.xtree.mine.ui.rebateagrt.model.RebateAgrtDetailModel;
import com.xtree.mine.ui.rebateagrt.viewmodel.DividendAgrtCheckViewModel;
import com.xtree.mine.ui.rebateagrt.viewmodel.RebateAgrtCreateViewModel;
import com.xtree.mine.ui.viewmodel.factory.AppViewModelFactory;
import com.xtree.mine.vo.request.DividendAgrtCheckRequest;

import me.xtree.mvvmhabit.base.BaseFragment;
import me.xtree.mvvmhabit.bus.RxBus;

/**
 * Created by KAKA on 2024/3/19.
 * Describe: 分红契约查看dialog
 */
public class DividendAgrtCheckDialogFragment extends BaseFragment<DialogDividendagrtCheckBinding, DividendAgrtCheckViewModel> {
    @Override
    public void initView() {
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.dialog_dividendagrt_check;
    }

    @Override
    public int initVariableId() {
        return BR.model;
    }

    @Override
    public DividendAgrtCheckViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getActivity().getApplication());
        return new ViewModelProvider(requireActivity(), factory).get(DividendAgrtCheckViewModel.class);
    }

    @Override
    public void initData() {
        super.initData();
        DividendAgrtCheckRequest stickyEvent = RxBus.getDefault().getStickyEvent(DividendAgrtCheckRequest.class);
        if (stickyEvent != null) {
            viewModel.setActivity(getActivity());
            viewModel.initData(stickyEvent);
        }
    }
}