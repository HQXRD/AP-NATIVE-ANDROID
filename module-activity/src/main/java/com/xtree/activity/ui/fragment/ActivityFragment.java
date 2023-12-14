package com.xtree.activity.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.xtree.activity.BR;
import com.xtree.activity.R;
import com.xtree.activity.databinding.FragmentActivityBinding;
import com.xtree.activity.ui.viewmodel.ActivityViewModel;
import com.xtree.activity.ui.viewmodel.factory.AppViewModelFactory;
import com.xtree.activity.vo.CategoryVo;
import com.xtree.activity.vo.NewVo;
import com.xtree.base.router.RouterFragmentPath;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import me.xtree.mvvmhabit.base.BaseFragment;
import me.xtree.mvvmhabit.utils.ToastUtils;

/**
 * Created by goldze on 2018/6/21
 */
@Route(path = RouterFragmentPath.Activity.PAGER_ACTIVITY)
public class ActivityFragment extends BaseFragment<FragmentActivityBinding, ActivityViewModel> {

    private ArrayList<Fragment> fragmentList = new ArrayList<>();
    FragmentStateAdapter mAdapter;
    ArrayList<CategoryVo> list2 = new ArrayList<>();
    private Map<String, ArrayList<NewVo>> map = new HashMap<>();
    //private int curIndex = 0; // 当前选中的tab

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel.readCache();
        viewModel.getNewList();
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_activity;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public ActivityViewModel initViewModel() {
        //使用自定义的ViewModelFactory来创建ViewModel，如果不重写该方法，则默认会调用LoginViewModel(@NonNull Application application)构造方法
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getActivity().getApplication());
        return new ViewModelProvider(this, factory).get(ActivityViewModel.class);
    }

    @Override
    public void initView() {

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
        binding.vpMain.setAdapter(mAdapter);
        binding.vpMain.setUserInputEnabled(true); // true: ViewPager2可左右滑动
        binding.vpMain.setSaveEnabled(false);
        new TabLayoutMediator(binding.tblType, binding.vpMain, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                if (position < list2.size()) {
                    tab.setText(list2.get(position).category);
                    //tab.setTag(list2.get(position));
                }
            }
        }).attach();
    }

    @Override
    public void initData() {

    }

    @Override
    public void initViewObservable() {
        viewModel.itemClickEvent.observe(this, (Observer<String>) s -> ToastUtils.showShort(s));

        viewModel.liveDataNewList.observe(getViewLifecycleOwner(), new Observer<ArrayList<NewVo>>() {
            @Override
            public void onChanged(ArrayList<NewVo> list) {
                // 加载主页,
                setNewsView(list);
                // 缓存数据
            }
        });

    }

    private void setNewsView(ArrayList<NewVo> list) {

        fragmentList.clear();
        list2.clear();
        map.clear();

        list2.add(new CategoryVo(0, "全部优惠"));
        map.put("0", list);

        ArrayList<NewVo> tmpList;
        for (NewVo vo : list) {
            String key = String.valueOf(vo.category_order);
            if (!map.containsKey(key)) {
                list2.add(new CategoryVo(vo.category_order, vo.category));
                tmpList = new ArrayList<>();
            } else {
                tmpList = map.get(key);
            }
            tmpList.add(vo);
            map.put(key, tmpList);
        }

        Collections.sort(list2);
        for (CategoryVo t2 : list2) {
            if (t2.category.contains("小9直播")) {
                list2.remove(t2);
                list2.add(list2.size() - 1, t2);
                break;
            }
        }

        for (CategoryVo t2 : list2) {
            fragmentList.add(NewsFragment.newInstance(t2.category_order, t2.category, map.get(String.valueOf(t2.category_order))));
        }

        mAdapter.notifyDataSetChanged();

        //if (curIndex > 0 && curIndex < fragmentList.size()) {
        //    binding.vpMain.setCurrentItem(curIndex);
        //}
    }

}
