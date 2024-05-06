package com.xtree.mine.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.base.utils.AppUtil;
import com.xtree.mine.BR;
import com.xtree.mine.R;
import com.xtree.mine.databinding.FragmentTransferResultBinding;
import com.xtree.mine.ui.viewmodel.MyWalletViewModel;
import com.xtree.mine.ui.viewmodel.factory.AppViewModelFactory;

import me.xtree.mvvmhabit.base.BaseFragment;

@Route(path = RouterFragmentPath.Wallet.PAGER_TRANSFER_RESULT)
public class TransferResultFragment extends BaseFragment<FragmentTransferResultBinding, MyWalletViewModel> {
    public static final String IS_SUCCESS = "is_success";
    public static final String FROM_PLATFORM = "from_platform";
    public static final String TO_PLATFORM = "to_platform";
    public static final String TRANSFER_MONEY = "transfer_money";
    public static final String RESULT_STRING = "result_string";

    boolean isSuccess = false;
    String formPlatform = "";
    String toPlatform = "";
    String transferMoney = "";
    String resultString = "";

    @Override
    public void initView() {
        if (getArguments() != null) {
            isSuccess = getArguments().getBoolean(IS_SUCCESS);
            formPlatform = getArguments().getString(FROM_PLATFORM);
            toPlatform = getArguments().getString(TO_PLATFORM);
            transferMoney = getArguments().getString(TRANSFER_MONEY);
            resultString = getArguments().getString(RESULT_STRING);
        }

        if (isSuccess) {
            binding.ivmResult.setImageResource(R.mipmap.ic_transfer_success);
            binding.tvwResultDetail.setText(String.format(getContext().getResources().getString(R.string.txt_transfer_success_detail), formPlatform, toPlatform, transferMoney));
            binding.tvwResultError.setVisibility(View.GONE);
        } else {
            binding.ivmResult.setImageResource(R.mipmap.ic_transfer_fail);
            binding.tvwResultDetail.setText(String.format(getContext().getResources().getString(R.string.txt_transfer_fail_detail), formPlatform, toPlatform, transferMoney));
            binding.tvwResultError.setVisibility(View.VISIBLE);
            binding.tvwResultError.setText(resultString);
        }

        binding.ivwBack.setOnClickListener(v -> getActivity().finish());

        binding.ivwMsg.setOnClickListener(v -> startContainerFragment(RouterFragmentPath.Mine.PAGER_MSG));

        binding.ivwCs.setOnClickListener(v -> AppUtil.goCustomerService(getContext()));

        binding.ivmContinue.setOnClickListener(v -> getActivity().finish());
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_transfer_result;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public MyWalletViewModel initViewModel() {
        // return super.initViewModel();
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getActivity().getApplication());
        return new ViewModelProvider(this, factory).get(MyWalletViewModel.class);
    }
}
