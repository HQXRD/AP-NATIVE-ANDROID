package com.xtree.bet.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.xtree.bet.R;
import com.xtree.bet.databinding.BtLayoutBtCarBinding;
import com.xtree.bet.ui.viewmodel.MainViewModel;

import me.xtree.mvvmhabit.base.BaseDialogFragment;

public class BtCarDialogFragment extends BaseDialogFragment<BtLayoutBtCarBinding, MainViewModel> {

    @Override
    public void initView() {

    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.bt_layout_bt_car;
    }

    @Override
    public void onClick(View view) {

    }
}
