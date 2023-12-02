package com.xtree.bet.ui.activity;

import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.android.material.tabs.TabLayout;
import com.xtree.base.router.RouterActivityPath;
import com.xtree.bet.BR;
import com.xtree.bet.bean.ui.League;
import com.xtree.bet.ui.adapter.LeagueAdapter;
import com.xtree.bet.ui.viewmodel.MainViewModel;
import com.xtree.bet.ui.viewmodel.factory.AppViewModelFactory;
import com.xtree.bet.R;
import com.xtree.bet.databinding.FragmentMainBinding;

import java.util.ArrayList;
import java.util.List;

import me.xtree.mvvmhabit.base.BaseActivity;
import me.xtree.mvvmhabit.utils.ToastUtils;

/**
 * Created by goldze on 2018/6/21
 */
@Route(path = RouterActivityPath.Bet.PAGER_BET_HOME)
public class MainActivity extends BaseActivity<FragmentMainBinding, MainViewModel> implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    private List<League> mLeagueAdapters = new ArrayList<>();
    private LeagueAdapter mLeagueAdapter;

    private boolean isGoingExpand = true;
    private boolean isWatingExpand = true;

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.fragment_main;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public MainViewModel initViewModel() {
        //使用自定义的ViewModelFactory来创建ViewModel，如果不重写该方法，则默认会调用LoginViewModel(@NonNull Application application)构造方法
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getApplication());
        return new ViewModelProvider(this, factory).get(MainViewModel.class);
    }

    @Override
    public void initView() {
        binding.srlLeague.setOnRefreshListener(this);
        binding.rvLeague.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                int topRowVerticalPosition =
                        (recyclerView == null || recyclerView.getChildCount() == 0) ? 0 : recyclerView.getChildAt(0).getTop();
                Log.e("test", "============" + topRowVerticalPosition);
                binding.srlLeague.setEnabled(topRowVerticalPosition >= 0);
            }
        });
        binding.llGoingOn.setOnClickListener(this);
        binding.tabPlayMethod.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                ((TextView) tab.getCustomView()).setTextSize(16);
                playMethodPos = tab.getPosition();
                mLeagueAdapters.clear();
                viewModel.setLeagueList(playMethodPos, sportTypePos);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                ((TextView) tab.getCustomView()).setTextSize(12);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        binding.tabSportType.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                sportTypePos = tab.getPosition();
                mLeagueAdapters.clear();
                viewModel.setLeagueList(playMethodPos, sportTypePos);
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
    public void initData() {
        viewModel.setPlayMethodTabData();
        viewModel.setplaySearchDateData();
        viewModel.setMatchItems();
        viewModel.setFbLeagueData();
        viewModel.addSubscription();
        viewModel.setLeagueList(playMethodPos, sportTypePos);
    }

    int playMethodPos;
    int sportTypePos;

    @Override
    public void initViewObservable() {
        viewModel.itemClickEvent.observe(this, s -> ToastUtils.showShort(s));
        viewModel.playMethodTab.observe(this, titleList -> {
            for (int i = 0; i < titleList.length; i++) {
                TextView textView = new TextView(this);
                textView.setText(titleList[i]);
                ColorStateList colorStateList = getResources().getColorStateList(R.color.bt_color_bet_top_tab_item_text);
                textView.setTextColor(colorStateList);
                if (i == 0) {
                    textView.setTextSize(16);
                } else {
                    textView.setTextSize(12);
                }
                binding.tabPlayMethod.addTab(binding.tabPlayMethod.newTab().setCustomView(textView));
            }

        });
        viewModel.playSearchDate.observe(this, dateList -> {
            for (int i = 0; i < dateList.size(); i++) {
                binding.tabSearchDate.addTab(binding.tabSearchDate.newTab().setText(dateList.get(i)));
            }
        });
        viewModel.matchItemDate.observe(this, matchitemList -> {
            for (int i = 0; i < matchitemList.size(); i++) {
                View view = LayoutInflater.from(this).inflate(R.layout.bt_layout_bet_match_item_tab_item, null);
                TextView tvName = view.findViewById(R.id.tab_item_name);
                TextView tvMatchCount = view.findViewById(R.id.iv_match_count);
                ImageView ivIcon = view.findViewById(R.id.iv_icon);

                tvName.setText(matchitemList.get(i).getName());
                tvMatchCount.setText(String.valueOf(matchitemList.get(i).getMatchCount()));
                ColorStateList colorStateList = getResources().getColorStateList(R.color.bt_color_bet_match_item_text);
                tvName.setTextColor(colorStateList);
                tvMatchCount.setTextColor(colorStateList);

                binding.tabSportType.addTab(binding.tabSportType.newTab().setCustomView(view));
            }
        });
        viewModel.leagueItemDate.observe(this, leagueItem -> {
            for (int i = 0; i < leagueItem.getLeagueNameList().length; i++) {
                View view = LayoutInflater.from(this).inflate(R.layout.bt_layout_bet_league_tab_item, null);
                TextView tvName = view.findViewById(R.id.tab_item_name);
                ImageView ivIcon = view.findViewById(R.id.iv_icon);
                String name = leagueItem.getLeagueNameList()[i];
                if (TextUtils.equals(name, "全部")) {
                    ivIcon.setVisibility(View.GONE);
                }

                tvName.setText(name);
                ColorStateList colorStateList = getResources().getColorStateList(R.color.bt_color_bet_top_tab_item_text);
                tvName.setTextColor(colorStateList);

                binding.tabFbLeague.addTab(binding.tabFbLeague.newTab().setCustomView(view));
            }
        });

        viewModel.leagueWaitingListDate.observe(this, leagueAdapters -> {
            if(!leagueAdapters.isEmpty()){
                League league = leagueAdapters.get(0).instance();
                league.setHead(true);
                this.mLeagueAdapters.add(league);
            }
            this.mLeagueAdapters.addAll(leagueAdapters);
            mLeagueAdapter = new LeagueAdapter(this, this.mLeagueAdapters);
            binding.rvLeague.setLayoutManager(new LinearLayoutManager(this));
            binding.rvLeague.setAdapter(mLeagueAdapter);
        });

        viewModel.leagueGoingOnListDate.observe(this, leagueAdapters -> {
            this.mLeagueAdapters.clear();
            this.mLeagueAdapters.addAll(leagueAdapters);
        });

        viewModel.expandContractListDate.observe(this, expandContract -> {
            isWatingExpand = !isWatingExpand;
            int index = -1;
            for(int i = 0; i < mLeagueAdapters.size(); i ++){
                League league = mLeagueAdapters.get(i);

                if(league.isHead()){
                    index = i;
                }
                if(index > 0) {
                    league.setExpand(isWatingExpand);
                }
            }
            mLeagueAdapter.notifyDataSetChanged();
        });
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(() -> binding.srlLeague.setRefreshing(false), 500);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.ll_going_on || id == R.id.ll_all_league) {
            isGoingExpand = !isGoingExpand;
            for (League league : this.mLeagueAdapters) {
                if (league.isHead()) {
                    break;
                }
                league.setExpand(isGoingExpand);
            }
            mLeagueAdapter.notifyDataSetChanged();
        }
    }
}
