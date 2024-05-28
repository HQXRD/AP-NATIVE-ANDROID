package com.xtree.recharge.ui.fragment.extransfer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.recharge.BR;
import com.xtree.recharge.R;
import com.xtree.recharge.databinding.FragmentExtransferCommitBinding;
import com.xtree.recharge.ui.viewmodel.ExTransferViewModel;
import com.xtree.recharge.ui.viewmodel.factory.AppViewModelFactory;

import me.xtree.mvvmhabit.base.BaseFragment;

/**
 * Created by KAKA on 2024/5/28.
 * Describe: 极速转账-提交订单流程
 */
@Route(path = RouterFragmentPath.Transfer.PAGER_TRANSFER_EX_COMMIT)
public class ExTransferCommitFragment extends BaseFragment<FragmentExtransferCommitBinding, ExTransferViewModel> {

    @Override
    public void initView() {

    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_extransfer_commit;
    }

    @Override
    public int initVariableId() {
        return BR.model;
    }

    @Override
    public ExTransferViewModel initViewModel() {
        ExTransferViewModel viewmodel = new ViewModelProvider(getActivity()).get(ExTransferViewModel.class);
        AppViewModelFactory instance = AppViewModelFactory.getInstance(getActivity().getApplication());
        viewmodel.setModel(instance.getmRepository());
        return viewmodel;
    }

    @Override
    public void initData() {
        super.initData();

        binding.getModel().initData(getActivity());
    }
}
