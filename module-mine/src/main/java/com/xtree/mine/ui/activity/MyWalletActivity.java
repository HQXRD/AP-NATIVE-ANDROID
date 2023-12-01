package com.xtree.mine.ui.activity;

import android.os.Bundle;

import com.xtree.mine.BR;
import com.xtree.mine.R;
import com.xtree.mine.databinding.ActivityMyWalletBinding;
import com.xtree.mine.ui.viewmodel.MyWalletViewModel;

import me.xtree.mvvmhabit.base.BaseActivity;

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
}
