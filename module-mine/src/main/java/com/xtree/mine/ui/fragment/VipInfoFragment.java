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
import com.xtree.mine.databinding.FragmentVipInfoBinding;
import com.xtree.mine.ui.viewmodel.MineViewModel;
import com.xtree.mine.ui.viewmodel.factory.AppViewModelFactory;

import me.xtree.mvvmhabit.base.BaseFragment;

@Route(path = RouterFragmentPath.Mine.PAGER_VIP_INFO)
public class VipInfoFragment extends BaseFragment<FragmentVipInfoBinding, MineViewModel> {
    private final String mVipNum;
    private final String mVipBack;
    private final String mVipBackThreeMonth;

    public VipInfoFragment(int vipNum, int vipBack, int vipBackThreeMonth) {
        mVipNum = String.valueOf(vipNum);
        mVipBack = String.valueOf(vipBack);
        mVipBackThreeMonth = String.valueOf(vipBackThreeMonth);
    }

    @Override
    public void initView() {
        binding.tvwVipNum.setText(mVipNum);
        binding.tvwVipBackRequireNum.setText(mVipBack);
        binding.tvwVipBackRequireThreeMonthNum.setText(mVipBackThreeMonth);
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_vip_info;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public MineViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getActivity().getApplication());
        return new ViewModelProvider(this, factory).get(MineViewModel.class);
    }
}
