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
import com.xtree.mine.databinding.FragmentRebateAgreementBinding;
import com.xtree.mine.ui.rebateagrt.viewmodel.GameRebateAgrtViewModel;
import com.xtree.mine.ui.viewmodel.factory.AppViewModelFactory;

import me.xtree.mvvmhabit.base.BaseFragment;

/**
 * Created by KAKA on 2024/3/9.
 * Describe: 游戏场馆返水契约
 */
@Route(path = RouterFragmentPath.Mine.PAGER_REBATE_AGREEMENT_GAME)
public class GameRebateAgrtFragment extends BaseFragment<FragmentRebateAgreementBinding, GameRebateAgrtViewModel> {
    @Override
    public void initView() {
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_game_rebateagrt;
    }

    @Override
    public int initVariableId() {
        return BR.model;
    }

    @Override
    public GameRebateAgrtViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getActivity().getApplication());
        return new ViewModelProvider(this, factory).get(GameRebateAgrtViewModel.class);
    }

}
