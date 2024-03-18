package com.xtree.mine.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.mine.BR;
import com.xtree.mine.R;
import com.xtree.mine.databinding.FragmentChooseWithdrawBinding;
import com.xtree.mine.ui.viewmodel.ChooseWithdrawViewModel;
import com.xtree.mine.ui.viewmodel.factory.AppViewModelFactory;

import me.xtree.mvvmhabit.base.BaseFragment;

@Route(path = RouterFragmentPath.Mine.PAGER_CHOOSE)
public class ChooseWithdrawFragment extends BaseFragment<FragmentChooseWithdrawBinding, ChooseWithdrawViewModel> {
    private ChooseWithdrawViewModel viewModel;
    private ChooseWithdrawalDialog chooseWithdrawalDialog;//提款选择dialog
    private BasePopupView basePopupView = null;

    @Override
    public void initView() {
        showChoose();
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_choose_withdraw;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public ChooseWithdrawViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getActivity().getApplication());
        return new ViewModelProvider(this, factory).get(ChooseWithdrawViewModel.class);
    }

    /**
     * 显示提款页面
     */
    private void showChoose() {
        basePopupView = new XPopup.Builder(getContext()).asCustom(ChooseWithdrawalDialog.newInstance(getContext(), this, new ChooseWithdrawalDialog.IChooseDialogBack() {
            @Override
            public void closeDialog() {
                basePopupView.dismiss();
                getActivity().finish();
            }

            @Override
            public void closeDialogByError() {
                getActivity().finish();
            }
        }, new BankWithdrawalDialog.BankWithdrawalClose() {
            @Override
            public void closeBankWithdrawal() {
                basePopupView.dismiss();
                getActivity().finish();
            }

            @Override
            public void closeBankByPSW() {
                basePopupView.dismiss();
                getActivity().finish();
            }
        }));
        basePopupView.show();
    }

}
