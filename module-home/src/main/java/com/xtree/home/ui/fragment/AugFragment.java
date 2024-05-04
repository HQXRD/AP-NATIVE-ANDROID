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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.home.BR;
import com.xtree.home.R;
import com.xtree.home.databinding.FragmentAugBinding;
import com.xtree.home.ui.viewmodel.HomeViewModel;
import com.xtree.home.ui.viewmodel.factory.AppViewModelFactory;
import com.xtree.home.vo.AugVo;

import java.util.ArrayList;
import java.util.HashMap;

import me.xtree.mvvmhabit.base.BaseFragment;
import me.xtree.mvvmhabit.utils.SPUtils;

@Route(path = RouterFragmentPath.Home.AUG)
public class AugFragment extends BaseFragment<FragmentAugBinding, HomeViewModel> {

    private ArrayList<Fragment> fragmentList = new ArrayList<>();
    private FragmentStateAdapter mAdapter;

    @Override
    public void initView() {

        String json = SPUtils.getInstance().getString(SPKeyGlobal.AUG_LIST);
        if (!json.isEmpty()) {
            Gson gson = new Gson();
            HashMap<String, ArrayList<AugVo>> map = gson.fromJson(json, new TypeToken<HashMap<String, ArrayList<AugVo>>>() {
            }.getType());
            viewModel.liveDataAug.setValue(map);
        }
        binding.tvwTitle.setText(getString(R.string.txt_venue_aug));
        binding.ivwBack.setOnClickListener(v -> getActivity().finish());
        mAdapter = new FragmentStateAdapter(getChildFragmentManager(), getLifecycle()) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                return fragmentList.get(position);
            }

            @Override
            public int getItemCount() {
                return fragmentList.size();
            }
        };
        binding.vpAug.setUserInputEnabled(false);
        binding.vpAug.setOffscreenPageLimit(1);
        binding.vpAug.setAdapter(mAdapter);
        binding.tbAug.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.vpAug.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_aug;
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
        viewModel.getAugList(getContext());
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        viewModel.liveDataAug.observe(getViewLifecycleOwner(), augMap -> {
            fragmentList.clear();
            binding.tbAug.removeAllTabs();
            for (String key : augMap.keySet()) {//遍历map的键
                TabLayout.Tab tab = binding.tbAug.newTab();
                tab.setText(key);
                binding.tbAug.addTab(tab);
                fragmentList.add(AugChildFragment.newInstance(augMap.get(key)));
            }
            mAdapter.notifyDataSetChanged();
        });
    }

}
