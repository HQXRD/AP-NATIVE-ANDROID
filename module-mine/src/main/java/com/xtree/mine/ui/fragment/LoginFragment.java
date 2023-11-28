package com.xtree.mine.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.mine.BR;
import com.xtree.mine.R;
import com.xtree.mine.databinding.FragmentLoginBinding;
import com.xtree.mine.ui.viewmodel.LoginViewModel;

import me.xtree.mvvmhabit.base.BaseFragment;
@Route(path = RouterFragmentPath.Mine.PAGER_LOGIN)
public class LoginFragment extends BaseFragment<FragmentLoginBinding, LoginViewModel> {

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_login;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }



}
