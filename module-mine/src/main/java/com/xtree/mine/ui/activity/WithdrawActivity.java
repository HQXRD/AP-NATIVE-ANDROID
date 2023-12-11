package com.xtree.mine.ui.activity;

import android.os.Bundle;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.xtree.base.router.RouterActivityPath;
import com.xtree.mine.BR;
import com.xtree.mine.R;
import com.xtree.mine.databinding.ActivityWithdrawBinding;
import com.xtree.mine.ui.viewmodel.MyWalletViewModel;
import com.xtree.mine.ui.viewmodel.factory.AppViewModelFactory;
import com.xtree.mine.vo.BalanceVo;

import me.xtree.mvvmhabit.base.BaseActivity;

@Route(path = RouterActivityPath.Mine.PAGER_WITHDRAW)
public class WithdrawActivity extends BaseActivity<ActivityWithdrawBinding, MyWalletViewModel> {
    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_withdraw;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    /*@Override
    public WithdrawViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getApplication());
        return new ViewModelProvider(this, factory).get(WithdrawViewModel.class);
    }*/
    @Override
    public MyWalletViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getApplication());
        return new ViewModelProvider(this, factory).get(MyWalletViewModel.class);
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

        viewModel.getBalance();
    }

    @Override
    public void initViewObservable() {
        viewModel.liveDataBalance.observe(this, new Observer<BalanceVo>() {
            @Override
            public void onChanged(BalanceVo vo) {
                binding.tvwBalance.setText(vo.balance);
            }
        });
    }

}