package com.xtree.bet.ui.fragment;

import android.app.Application;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.bet.BR;
import com.xtree.bet.R;
import com.xtree.bet.databinding.FragmentAtBinding;
import com.xtree.bet.ui.viewmodel.factory.AppViewModelFactory;
import com.xtree.bet.ui.viewmodel.fb.FBMainViewModel;

import me.xtree.mvvmhabit.base.BaseFragment;
import me.xtree.mvvmhabit.utils.Utils;

/**
 * 公告&教程
 */
@Route(path = RouterFragmentPath.Bet.PAGER_BET_AT)
public class BtATFragment extends BaseFragment<FragmentAtBinding, FBMainViewModel> {

    public static BtATFragment getInstance() {
        BtATFragment btResultDialogFragment = new BtATFragment();
        return btResultDialogFragment;
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_at;
    }

    @Override
    public void initView() {
        binding.ivwBack.setOnClickListener(v -> requireActivity().finish());
        binding.clAnnouncement.setOnClickListener(v -> startContainerFragment(RouterFragmentPath.Bet.PAGER_BET_ANNOUNCEMENT));
        binding.clTutorial.setOnClickListener(v -> startContainerFragment(RouterFragmentPath.Bet.PAGER_BET_TUTORIAL));

    }

    @Override
    public void initData() {

    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public FBMainViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance((Application) Utils.getContext());
        return new ViewModelProvider(this, factory).get(FBMainViewModel.class);
    }

}
