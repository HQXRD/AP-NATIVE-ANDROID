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
import com.xtree.mine.databinding.FragmentCommissionsReportsBinding;
import com.xtree.mine.ui.rebateagrt.model.RebateAreegmentModel;
import com.xtree.mine.ui.rebateagrt.viewmodel.CommissionsReportsViewModel;
import com.xtree.mine.ui.viewmodel.factory.AppViewModelFactory;

import me.xtree.mvvmhabit.base.BaseFragment;
import me.xtree.mvvmhabit.bus.RxBus;

/**
 * Created by KAKA on 2024/3/29.
 * Describe: 佣金报表
 */
@Route(path = RouterFragmentPath.Mine.PAGER_COMMISSIONSREPORTS)
public class CommissionsReportsFragment extends BaseFragment<FragmentCommissionsReportsBinding, CommissionsReportsViewModel> {
    @Override
    public void initView() {
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_commissions_reports;
    }

    @Override
    public int initVariableId() {
        return BR.model;
    }

    @Override
    public CommissionsReportsViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getActivity().getApplication());
        return new ViewModelProvider(this, factory).get(CommissionsReportsViewModel.class);
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
}
