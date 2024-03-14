package com.xtree.mine.ui.rebateagrt.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.viewmodel.CreationExtras;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.mine.BR;
import com.xtree.mine.R;
import com.xtree.mine.databinding.DialogRebateagrtCreateBinding;
import com.xtree.mine.databinding.FragmentRebateAgreementBinding;
import com.xtree.mine.ui.rebateagrt.viewmodel.RebateAgreementViewModel;
import com.xtree.mine.ui.rebateagrt.viewmodel.RebateAgrtCreateViewModel;
import com.xtree.mine.ui.viewmodel.factory.AppViewModelFactory;

import me.xtree.mvvmhabit.base.BaseDialogFragment;
import me.xtree.mvvmhabit.base.BaseFragment;

/**
 * Created by KAKA on 2024/3/13.
 * Describe:
 */
@Route(path = RouterFragmentPath.Mine.PAGER_REBATEAGRT_CREATE_DIALOG)
public class RebateAgrtCreateDialogFragment extends BaseDialogFragment<DialogRebateagrtCreateBinding, RebateAgrtCreateViewModel> {
    @Override
    public void initView() {
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.dialog_rebateagrt_create;
    }

    @Override
    public RebateAgrtCreateViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getActivity().getApplication());
        return new ViewModelProvider(this, factory).get(RebateAgrtCreateViewModel.class);
    }

    @Override
    public void onClick(View v) {

    }
}
