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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.android.material.tabs.TabLayout;
import com.xtree.base.router.RouterActivityPath;
import com.xtree.base.utils.TimeUtils;
import com.xtree.bet.BR;
import com.xtree.bet.bean.ui.League;
import com.xtree.bet.constant.Constants;
import com.xtree.bet.ui.adapter.LeagueAdapter;
import com.xtree.bet.ui.viewmodel.MainViewModel;
import com.xtree.bet.ui.viewmodel.factory.AppViewModelFactory;
import com.xtree.bet.R;
import com.xtree.bet.databinding.FragmentMainBinding;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import me.xtree.mvvmhabit.base.BaseActivity;
import me.xtree.mvvmhabit.utils.ToastUtils;

/**
 * Created by goldze on 2018/6/21
 */
@Route(path = RouterActivityPath.Bet.PAGER_BET_HOME)
public class MainActivity extends BaseActivity<FragmentMainBinding, MainViewModel> implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {
    /**
     * 赛事统计数据
     */
    private Map<String, List<Integer>> statisticalData;

    private List<League> mLeagueAdapters = new ArrayList<>();
    private LeagueAdapter mLeagueAdapter;

    private boolean isGoingExpand = true;
    private boolean isWatingExpand = true;
    private int searchDatePos = -1;

    /**
     * 玩法类型，默认为今日+滚球
     */
    private int playMethodType = 6;
    private int playMethodPos;
    private int sportTypePos;

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
                binding.srlLeague.setEnabled(topRowVerticalPosition >= 0);
            }
        });
        binding.llGoingOn.setOnClickListener(this);
        binding.tabPlayMethod.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                ((TextView) tab.getCustomView()).setTextSize(16);
                if(playMethodPos != tab.getPosition()) {
                    playMethodType = playMethodTypeList.get(tab.getPosition());
                    playMethodPos = tab.getPosition();
                    mLeagueAdapters.clear();
                    updateStatisticalData();
                    if(playMethodType == 4 || playMethodType == 2){
                        binding.tabSearchDate.setVisibility(View.VISIBLE);
                        searchDatePos = 0;
                    }else{
                        binding.tabSearchDate.setVisibility(View.GONE);
                        searchDatePos = -1;
                    }
                    viewModel.getLeagueList(Integer.valueOf(Constants.SPORT_IDS[sportTypePos]), 1, null, null,
                            playMethodType, searchDatePos);
                }
                viewModel.setPlaySearchDateData();
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
                viewModel.getLeagueList(Integer.valueOf(Constants.SPORT_IDS[sportTypePos]), 1, null, null,
                        playMethodType, searchDatePos);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        binding.tabSearchDate.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(playMethodType == 4 || playMethodType == 2) {
                    searchDatePos = tab.getPosition();
                    viewModel.getLeagueList(Integer.valueOf(Constants.SPORT_IDS[sportTypePos]), 1, null, null,
                            playMethodType, searchDatePos);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        binding.rvLeague.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void initData() {
        viewModel.setSportItems();
        viewModel.setPlayMethodTabData();
        viewModel.setPlaySearchDateData();
        viewModel.setFbLeagueData();
        viewModel.addSubscription();
        viewModel.statistical();
    }

    List<Integer> playMethodTypeList = new ArrayList<>();
    @Override
    public void initViewObservable() {
        viewModel.itemClickEvent.observe(this, s -> ToastUtils.showShort(s));
        viewModel.playMethodTab.observe(this, titleList -> {
            for (int i = 0; i < titleList.length; i++) {
                TextView textView = new TextView(this);
                playMethodTypeList.add(Integer.valueOf(Constants.PLAY_METHOD_IDS[i]));
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
        viewModel.playSearchData.observe(this, dateList -> {
            binding.tabSearchDate.removeAllTabs();
            for (int i = 0; i < dateList.size(); i++) {
                TextView textView = new TextView(this);
                if(i == 0){
                    textView.setText("全部");
                }else if(i == dateList.size() - 1){
                    textView.setText("其他");
                }else{
                    textView.setText(TimeUtils.getTime(dateList.get(i), TimeUtils.FORMAT_MM_DD));
                }

                ColorStateList colorStateList = getResources().getColorStateList(R.color.bt_color_bet_top_tab_item_text);
                textView.setTextColor(colorStateList);
                binding.tabSearchDate.addTab(binding.tabSearchDate.newTab().setCustomView(textView));
            }
        });
        viewModel.sportItemData.observe(this, matchitemList -> {
            for (int i = 0; i < matchitemList.length; i++) {
                View view = LayoutInflater.from(this).inflate(R.layout.bt_layout_bet_match_item_tab_item, null);
                TextView tvName = view.findViewById(R.id.tab_item_name);
                TextView tvMatchCount = view.findViewById(R.id.iv_match_count);
                ImageView ivIcon = view.findViewById(R.id.iv_icon);
                ivIcon.setBackgroundResource(Constants.SPORT_ICON[i]);
                tvName.setText(matchitemList[i]);
                tvMatchCount.setText(String.valueOf(0));
                ColorStateList colorStateList = getResources().getColorStateList(R.color.bt_color_bet_match_item_text);
                tvName.setTextColor(colorStateList);
                tvMatchCount.setTextColor(colorStateList);

                binding.tabSportType.addTab(binding.tabSportType.newTab().setCustomView(view));
            }
        });
        viewModel.leagueItemData.observe(this, leagueItem -> {
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

        viewModel.leagueWaitingListData.observe(this, leagueAdapters -> {
            if(playMethodType != 6){
                this.mLeagueAdapters.clear();
            }
            if(!leagueAdapters.isEmpty()){
                League league = leagueAdapters.get(0).instance();
                league.setHead(true);
                this.mLeagueAdapters.add(league);
            }
            setTopShow();
            this.mLeagueAdapters.addAll(leagueAdapters);
            mLeagueAdapter = new LeagueAdapter(this, this.mLeagueAdapters);
            binding.rvLeague.setAdapter(mLeagueAdapter);
        });

        viewModel.leagueGoingOnListData.observe(this, leagueAdapters -> {
            this.mLeagueAdapters.clear();
            this.mLeagueAdapters.addAll(leagueAdapters);
            if(playMethodType == 1){
                this.mLeagueAdapters.addAll(leagueAdapters);
                mLeagueAdapter = new LeagueAdapter(this, this.mLeagueAdapters);
                binding.rvLeague.setAdapter(mLeagueAdapter);
            }
        });

        viewModel.expandContractListData.observe(this, expandContract -> {
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
        viewModel.statisticalData.observe(this, statisticalData -> {
            this.statisticalData = statisticalData;
            updateStatisticalData();
        });
    }

    /**+
     * 设置赛事列表头显示的信息（进行中或全部比赛）
     */
    private void setTopShow() {
        if(playMethodType == 4){
            binding.llAllLeague.setVisibility(View.VISIBLE);
            binding.llGoingOn.setVisibility(View.GONE);
        }else if(playMethodType == 7){
            binding.llAllLeague.setVisibility(View.GONE);
            binding.llGoingOn.setVisibility(View.GONE);
        }else{
            binding.llAllLeague.setVisibility(View.GONE);
            binding.llGoingOn.setVisibility(View.VISIBLE);
        }
    }

    private void updateStatisticalData(){
        int tabCount = binding.tabSportType.getTabCount();
        for(int i = 0; i < tabCount; i ++){
            View view = binding.tabSportType.getTabAt(i).getCustomView();
            TextView tvCount = view.findViewById(R.id.iv_match_count);
            tvCount.setText(String.valueOf(statisticalData.get(String.valueOf(playMethodType)).get(i)));
        }
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
