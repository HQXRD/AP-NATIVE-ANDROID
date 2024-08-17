package com.xtree.mine.ui.rebateagrt.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.mine.BR;
import com.xtree.mine.R;
import com.xtree.mine.databinding.FragmentCommissionsReports2Binding;
import com.xtree.mine.ui.rebateagrt.viewmodel.CommissionsReports2ViewModel;
import com.xtree.mine.ui.viewmodel.factory.AppViewModelFactory;

import me.xtree.mvvmhabit.base.BaseFragment;
import me.xtree.mvvmhabit.base.BaseViewModel;

/**
 * Created by KAKA on 2024/3/29.
 * Describe: 佣金报表查看
 */
@Route(path = RouterFragmentPath.Mine.PAGER_COMMISSIONSREPORTS2)
public class CommissionsReports2Fragment extends BaseFragment<FragmentCommissionsReports2Binding, CommissionsReports2ViewModel> {
    @Override
    public void initView() {
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_commissions_reports2;
    }

    @Override
    public int initVariableId() {
        return BR.model;
    }

    @Override
    public CommissionsReports2ViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getActivity().getApplication());
        return new ViewModelProvider(this, factory).get(CommissionsReports2ViewModel.class);
    }

    @Override
    public void initData() {
        super.initData();
        viewModel.setActivity(getActivity());
        viewModel.initData();
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        //这里其实可以封装到Base
        viewModel.getUC().getSmartRefreshListenerEvent().observe(this, integer -> {
            if (integer == BaseViewModel.ONFINISH_LOAD_MORE) {
                binding.refreshLayout.resetNoMoreData();
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
