package com.xtree.activity.ui.fragment;

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
import com.google.android.material.tabs.TabLayoutMediator;
import com.xtree.activity.BR;
import com.xtree.activity.R;
import com.xtree.activity.databinding.FragmentActivityBinding;
import com.xtree.activity.ui.viewmodel.ActivityViewModel;
import com.xtree.activity.ui.viewmodel.factory.AppViewModelFactory;
import com.xtree.activity.vo.CategoryVo;
import com.xtree.activity.vo.NewVo;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.base.utils.AppUtil;
import com.xtree.base.utils.CfLog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import me.xtree.mvvmhabit.base.BaseFragment;

/**
 * 活动页-主页
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
    protected void initImmersionBar() {
    }

    @Override
    public ActivityViewModel initViewModel() {
        //使用自定义的ViewModelFactory来创建ViewModel，如果不重写该方法，则默认会调用LoginViewModel(@NonNull Application application)构造方法
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getActivity().getApplication());
        return new ViewModelProvider(this, factory).get(ActivityViewModel.class);
    }

    /**
     * 使用hide和show后，可见不可见切换时，不再执行fragment生命周期方法，
     * 需要刷新时，使用onHiddenChanged代替
     */
    @Override
    public void onResume() {
        super.onResume();
        //KLog.i("测试显示隐藏", "onResumeActivity");
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {   // 隐藏
            //KLog.i("测试显示隐藏", "HideOnHiddenChangedAcitivity");
        } else {  // 第一次可见，不会执行到这里，只会执行onResume
            //网络数据刷新
            //KLog.i("测试显示隐藏", "ShowOnHiddenChangedAcitivity");
        }
    }

    @Override
    public void initView() {
        binding.ivwCs.setOnClickListener(v -> AppUtil.goCustomerService(getContext()));

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
        new TabLayoutMediator(binding.tblType, binding.vpMain, (tab, position) -> {
            if (position < list2.size()) {
                tab.setText(list2.get(position).category);
                //tab.setTag(list2.get(position));
            }
        }).attach();
    }

    @Override
    public void initData() {

    }

    @Override
    public void initViewObservable() {

        viewModel.liveDataNewList.observe(getViewLifecycleOwner(), list -> {
            // 加载主页,
            setNewsView(list);
        });

    }

    private void setNewsView(ArrayList<NewVo> list) {

        fragmentList.clear();
        list2.clear();
        map.clear();

        list2.add(new CategoryVo(0, getString(R.string.txt_all_discount)));
        map.put("0", list);

        ArrayList<NewVo> tmpList;
        for (NewVo vo : list) {
            if (vo.category_order == 0) {
                // 避免死循环; 0的已按照默认“全部优惠”处理
                CfLog.d("****** default: " + vo);
                continue;
            }
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
            if (t2.category.contains(getString(R.string.txt_xiao_9_live))) {
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
