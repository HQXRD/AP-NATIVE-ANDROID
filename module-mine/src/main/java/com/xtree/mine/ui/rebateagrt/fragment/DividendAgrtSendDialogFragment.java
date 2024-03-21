package com.xtree.mine.ui.rebateagrt.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.xtree.mine.BR;
import com.xtree.mine.R;
import com.xtree.mine.databinding.DialogDividendagrtCheckBinding;
import com.xtree.mine.databinding.DialogDividendagrtSendBinding;
import com.xtree.mine.ui.rebateagrt.viewmodel.DividendAgrtCheckViewModel;
import com.xtree.mine.ui.rebateagrt.viewmodel.DividendAgrtSendViewModel;
import com.xtree.mine.ui.viewmodel.factory.AppViewModelFactory;
import com.xtree.mine.vo.request.DividendAgrtCheckRequest;
import com.xtree.mine.vo.request.GameDividendAgrtRequest;
import com.xtree.mine.vo.response.GameDividendAgrtResponse;

import me.xtree.mvvmhabit.base.BaseFragment;
import me.xtree.mvvmhabit.base.BaseViewModel;
import me.xtree.mvvmhabit.bus.RxBus;

/**
 * Created by KAKA on 2024/3/19.
 * Describe:
 */
public class DividendAgrtSendDialogFragment extends BaseFragment<DialogDividendagrtSendBinding, DividendAgrtSendViewModel> {
    @Override
    public void initView() {
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.dialog_dividendagrt_send;
    }

    @Override
    public int initVariableId() {
        return BR.model;
    }

    @Override
    public DividendAgrtSendViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getActivity().getApplication());
        return new ViewModelProvider(requireActivity(), factory).get(DividendAgrtSendViewModel.class);
    }

    @Override
    public void initData() {
        super.initData();
        GameDividendAgrtRequest stickyEvent = RxBus.getDefault().getStickyEvent(GameDividendAgrtRequest.class);
        if (stickyEvent != null) {
            viewModel.setActivity(getActivity());
            viewModel.initData(stickyEvent);
        }
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        //这里其实可以封装到Base
        viewModel.getUC().getSmartRefreshListenerEvent().observe(this, integer -> {
            if (integer == BaseViewModel.ONFINISH_LOAD_MORE) {
                binding.refreshLayout.finishLoadMore();
            } else if (integer == BaseViewModel.ONFINISH_REFRESH) {
                binding.refreshLayout.finishRefresh();
            } else if (integer == BaseViewModel.ON_LOAD_MORE_WITH_NO_MORE_DATA) {
                binding.refreshLayout.finishLoadMoreWithNoMoreData();
                binding.refreshLayout.finishRefreshWithNoMoreData();
            } else if (integer == BaseViewModel.ONFINISH_REFRESH_FAILED) {
                binding.refreshLayout.finishRefresh(false);
            } else if (integer == BaseViewModel.ONFINISH_LOAD_MORE_FAILED) {
                binding.refreshLayout.finishLoadMore(false);
            }
        });
    }
}