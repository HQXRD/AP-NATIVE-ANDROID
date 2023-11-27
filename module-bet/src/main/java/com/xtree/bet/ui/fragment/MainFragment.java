package com.xtree.bet.ui.fragment;

import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
        viewModel.setPlayMethodTabData();
        viewModel.setplaySearchDateData();
        viewModel.setMatchItems();
        viewModel.setFbLeagueData();
    }

    @Override
    public void initViewObservable() {
        viewModel.itemClickEvent.observe(this, s -> ToastUtils.showShort(s));
        viewModel.playMethodTab.observe(this, titleList -> {
            for (int i = 0; i < titleList.length; i ++) {
                TextView textView = new TextView(getContext());
                textView.setText(titleList[i]);
                ColorStateList colorStateList=getResources().getColorStateList(R.color.bt_color_bet_top_tab_item_text);
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
        viewModel.playSearchDate.observe(this, dateList -> {
            for (int i = 0; i < dateList.size(); i ++) {
                binding.tabSearchDate.addTab(binding.tabSearchDate.newTab().setText(dateList.get(i)));
            }
        });
        viewModel.matchItemDate.observe(this, matchitemList -> {
            for (int i = 0; i < matchitemList.size(); i ++) {
                View view = LayoutInflater.from(getContext()).inflate(R.layout.bt_layout_bet_match_item_tab_item, null);
                TextView tvName = view.findViewById(R.id.tab_item_name);
                TextView tvMatchCount = view.findViewById(R.id.iv_match_count);
                ImageView ivIcon = view.findViewById(R.id.iv_icon);

                tvName.setText(matchitemList.get(i).getName());
                tvMatchCount.setText(String.valueOf(matchitemList.get(i).getMatchCount()));
                ColorStateList colorStateList=getResources().getColorStateList(R.color.bt_color_bet_match_item_text);
                tvName.setTextColor(colorStateList);
                tvMatchCount.setTextColor(colorStateList);

                binding.tabMatchItem.addTab(binding.tabMatchItem.newTab().setCustomView(view));
            }
        });
        viewModel.leagueItemDate.observe(this, leagueItem -> {
            for (int i = 0; i < leagueItem.getLeagueNameList().length; i ++) {
                View view = LayoutInflater.from(getContext()).inflate(R.layout.bt_layout_bet_league_tab_item, null);
                TextView tvName = view.findViewById(R.id.tab_item_name);
                ImageView ivIcon = view.findViewById(R.id.iv_icon);
                String name = leagueItem.getLeagueNameList()[i];
                if(TextUtils.equals(name, "全部")){
                    ivIcon.setVisibility(View.GONE);
                }

                tvName.setText(name);
                ColorStateList colorStateList=getResources().getColorStateList(R.color.bt_color_bet_top_tab_item_text);
                tvName.setTextColor(colorStateList);

                binding.tabFbLeague.addTab(binding.tabFbLeague.newTab().setCustomView(view));
            }
        });
    }

}
