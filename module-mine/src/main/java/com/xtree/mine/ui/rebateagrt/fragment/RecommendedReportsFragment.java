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
import com.xtree.mine.databinding.FragmentRecommendedReportsBinding;
import com.xtree.mine.ui.rebateagrt.model.RebateAreegmentModel;
import com.xtree.mine.ui.rebateagrt.viewmodel.RecommendedReportsViewModel;
import com.xtree.mine.ui.viewmodel.factory.AppViewModelFactory;

import me.xtree.mvvmhabit.base.BaseFragment;
import me.xtree.mvvmhabit.base.BaseViewModel;
import me.xtree.mvvmhabit.bus.RxBus;

/**
 * Created by KAKA on 2024/3/15.
 * Describe: 游戏推荐报表
 */
@Route(path = RouterFragmentPath.Mine.PAGER_RECOMMENDED_REPORTS_GAME)
public class RecommendedReportsFragment extends BaseFragment<FragmentRecommendedReportsBinding, RecommendedReportsViewModel> {
    @Override
    public void initView() {
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_recommended_reports;
    }

    @Override
    public int initVariableId() {
        return BR.model;
    }

    @Override
    public RecommendedReportsViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getActivity().getApplication());
        return new ViewModelProvider(this, factory).get(RecommendedReportsViewModel.class);
    }

    @Override
    public void initData() {
        super.initData();
        viewModel.setActivity(getActivity());
        RebateAreegmentModel rebateAreegmentModel = RxBus.getDefault().getStickyEvent(RebateAreegmentModel.class);
        if (rebateAreegmentModel != null) {
            viewModel.initData(rebateAreegmentModel.type);
        }
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
