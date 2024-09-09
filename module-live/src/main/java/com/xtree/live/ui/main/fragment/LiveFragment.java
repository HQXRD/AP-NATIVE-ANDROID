package com.xtree.live.ui.main.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.live.BR;
import com.xtree.live.R;
import com.xtree.live.data.factory.AppViewModelFactory;
import com.xtree.live.databinding.FragmentLiveBinding;
import com.xtree.live.ui.main.viewmodel.LiveViewModel;

import me.xtree.mvvmhabit.base.BaseFragment;

/**
 * Created by KAKA on 2024/9/9.
 * Describe: 直播门户页
 */
@Route(path = RouterFragmentPath.Live.PAGER_LIVE_MAIN)
public class LiveFragment extends BaseFragment<FragmentLiveBinding, LiveViewModel> {

    @Override
    public void initView() {

    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_live;
    }

    @Override
    public int initVariableId() {
        return BR.model;
    }

    @Override
    public LiveViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getActivity().getApplication());
        return new ViewModelProvider(this, factory).get(LiveViewModel.class);
    }

    @Override
    public void initData() {
        super.initData();

        viewModel.initData();
    }
}
