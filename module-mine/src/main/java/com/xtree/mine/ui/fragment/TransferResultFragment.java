package com.xtree.mine.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.mine.BR;
import com.xtree.mine.R;
import com.xtree.mine.databinding.FragmentTransferResultBinding;
import com.xtree.mine.ui.viewmodel.TransferResultViewModel;
import com.xtree.mine.ui.viewmodel.factory.AppViewModelFactory;
import com.xtree.mine.vo.TransferResultModel;

import me.xtree.mvvmhabit.base.BaseFragment;
import me.xtree.mvvmhabit.bus.RxBus;

/**
 * Created by KAKA on 2024/4/3.
 * Describe: 转账结果
 */
@Route(path = RouterFragmentPath.Wallet.PAGER_TRANSFER_RESULT)
public class TransferResultFragment extends BaseFragment<FragmentTransferResultBinding, TransferResultViewModel> {
    @Override
    public void initView() {

    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_transfer_result;
    }

    @Override
    public int initVariableId() {
        return BR.model;
    }

    @Override
    public TransferResultViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getActivity().getApplication());
        return new ViewModelProvider(this, factory).get(TransferResultViewModel.class);
    }

    @Override
    public void initData() {
        super.initData();

        TransferResultModel stickyEvent = RxBus.getDefault().getStickyEvent(TransferResultModel.class);

        if (stickyEvent != null) {
            viewModel.initData(stickyEvent);
        }
    }
}
