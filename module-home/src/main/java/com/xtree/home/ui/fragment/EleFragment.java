package com.xtree.home.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.android.material.tabs.TabLayout;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.home.BR;
import com.xtree.home.R;
import com.xtree.home.databinding.FragmentEleBinding;
import com.xtree.home.ui.viewmodel.HomeViewModel;
import com.xtree.home.ui.viewmodel.factory.AppViewModelFactory;
import com.xtree.home.vo.GameVo;

import me.xtree.mvvmhabit.base.BaseFragment;

/**
 * PG电子，PP电子，PT电子，奥丁电子
 */
@Route(path = RouterFragmentPath.Home.ELE)
public class EleFragment extends BaseFragment<FragmentEleBinding, HomeViewModel> {
    private GameVo vo;
    private FragmentStateAdapter mAdapter;

    @Override
    public void initView() {
        vo = getArguments().getParcelable("vo");

        binding.tvwTitle.setText(vo.name);
        binding.ivwBack.setOnClickListener(v -> getActivity().finish());

        binding.vpEle.setUserInputEnabled(false);

        binding.tbEle.addTab(binding.tbEle.newTab().setText("全部游戏"));
        binding.tbEle.addTab(binding.tbEle.newTab().setText("热门游戏"));
        binding.tbEle.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.vpEle.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        mAdapter = new FragmentStateAdapter(getChildFragmentManager(), getLifecycle()) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                return EleChildFragment.newInstance(position, vo);
            }

            @Override
            public int getItemCount() {
                return 2;
            }
        };
        binding.vpEle.setAdapter(mAdapter);
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_ele;
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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

}
