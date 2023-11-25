package com.xtree.bet.ui.fragment;

import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.android.material.tabs.TabLayout;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.bet.BR;
import com.xtree.bet.ui.viewmodel.MainViewModel;
import com.xtree.bet.ui.viewmodel.factory.AppViewModelFactory;
import com.xtree.bet.R;
import com.xtree.bet.databinding.FragmentMainBinding;

import me.xtree.mvvmhabit.base.BaseFragment;
import me.xtree.mvvmhabit.utils.ToastUtils;

/**
 * Created by goldze on 2018/6/21
 */
@Route(path = RouterFragmentPath.Bet.PAGER_BET_HOME)
public class MainFragment extends BaseFragment<FragmentMainBinding, MainViewModel> {
    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_main;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public MainViewModel initViewModel() {
        //使用自定义的ViewModelFactory来创建ViewModel，如果不重写该方法，则默认会调用LoginViewModel(@NonNull Application application)构造方法
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getActivity().getApplication());
        return new ViewModelProvider(this, factory).get(MainViewModel.class);
    }

    @Override
    public void initData() {
        viewModel.playMethodTab.setValue(new String[]{"今日", "滚球", "早盘", "串关", "冠军"/*, "冠军", "冠军", "冠军", "冠军"*/});

    }

    @Override
    public void initViewObservable() {
        viewModel.itemClickEvent.observe(this, (Observer<String>) s -> ToastUtils.showShort(s));
        viewModel.playMethodTab.observe(this, (Observer<String[]>) titles -> {
            for (int i = 0; i < titles.length; i ++) {
                TextView textView = new TextView(getContext());
                textView.setText(titles[i]);
                ColorStateList colorStateList=getResources().getColorStateList(R.color.color_bet_top_tab_item_text);
                textView.setTextColor(colorStateList);
                if(i == 0){
                    textView.setTextSize(16);
                }else{
                    textView.setTextSize(12);
                }
                binding.tabPlayMethod.addTab(binding.tabPlayMethod.newTab().setCustomView(textView));
            }
            binding.tabPlayMethod.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    ((TextView)tab.getCustomView()).setTextSize(16);
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                    ((TextView)tab.getCustomView()).setTextSize(12);
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
        });
    }
}
