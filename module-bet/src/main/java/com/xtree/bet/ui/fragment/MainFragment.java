package com.xtree.bet.ui.fragment;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.bet.BR;
import com.xtree.bet.R;
import com.xtree.bet.databinding.FragmentHomeBinding;
import com.xtree.bet.ui.viewmodel.HomeViewModel;
import com.xtree.bet.ui.viewmodel.factory.AppViewModelFactory;

import me.xtree.mvvmhabit.base.BaseFragment;
import me.xtree.mvvmhabit.utils.ToastUtils;

/**
 * Created by goldze on 2018/6/21
 */
@Route(path = RouterFragmentPath.Bet.PAGER_BET_HOME)
public class MainFragment extends BaseFragment<FragmentHomeBinding, HomeViewModel> {
    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_home;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public HomeViewModel initViewModel() {
        //使用自定义的ViewModelFactory来创建ViewModel，如果不重写该方法，则默认会调用LoginViewModel(@NonNull Application application)构造方法
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getActivity().getApplication());
        return new ViewModelProvider(this, factory).get(HomeViewModel.class);
    }

    @Override
    public void initData() {
        viewModel.login("testkite1002", "kite123456");
    }

    @Override
    public void initViewObservable() {
        viewModel.itemClickEvent.observe(this, (Observer<String>) s -> ToastUtils.showShort(s));
    }
}
