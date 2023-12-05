package com.xtree.mine.ui.activity;

import android.os.Bundle;

import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.xtree.base.router.RouterActivityPath;
import com.xtree.mine.BR;
import com.xtree.mine.R;
import com.xtree.mine.databinding.ActivityMyWalletBinding;
import com.xtree.mine.ui.viewmodel.MyWalletViewModel;
import com.xtree.mine.ui.viewmodel.factory.AppViewModelFactory;

import me.xtree.mvvmhabit.base.BaseActivity;

@Route(path = RouterActivityPath.Mine.PAGER_MY_WALLET)
public class MyWalletActivity extends BaseActivity<ActivityMyWalletBinding, MyWalletViewModel> {
    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_my_wallet;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initView() {

    }

    @Override
    public MyWalletViewModel initViewModel() {
        // return super.initViewModel();
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getApplication());
        return new ViewModelProvider(this, factory).get(MyWalletViewModel.class);
    }
}
