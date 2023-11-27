package com.xtree.home.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.home.BR;
import com.xtree.home.R;
import com.xtree.home.databinding.FragmentHomeBinding;
import com.xtree.home.ui.viewmodel.HomeViewModel;
import com.xtree.home.ui.viewmodel.factory.AppViewModelFactory;

import me.xtree.mvvmhabit.base.BaseFragment;
import me.xtree.mvvmhabit.utils.ToastUtils;

/**
 * 首页
 */
@Route(path = RouterFragmentPath.Home.PAGER_HOME)
public class HomeFragment extends BaseFragment<FragmentHomeBinding, HomeViewModel> {
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
        //viewModel.getBanners();
        //viewModel.getSettings(getContext());

        String username = "testkite1002";
        String pwd = "kite123456";
        //viewModel.login(getContext(), username, pwd); // 要等公钥接口返回结果以后 才能调用

        binding.btnBanner.setOnClickListener(view -> viewModel.getBanners());
        binding.btnSetting.setOnClickListener(view -> viewModel.getSettings(getContext()));
        binding.btnCookie.setOnClickListener(view -> viewModel.getCookie(getContext()));
        binding.btnLogin.setOnClickListener(view -> viewModel.login(getContext(), username, pwd));


        binding.btnLogin2.setOnClickListener(view -> {
            String username2 = binding.edtName.getText().toString().trim();
            String pwd2 = binding.edtPwd.getText().toString().trim();
            if (!username2.isEmpty() && !pwd2.isEmpty()) {
                viewModel.login(getContext(), username2, pwd2);
            }
        });

    }

    @Override
    public void initViewObservable() {
        viewModel.itemClickEvent.observe(this, (Observer<String>) s -> ToastUtils.showShort(s));
    }
}
