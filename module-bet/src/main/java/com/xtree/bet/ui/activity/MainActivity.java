package com.xtree.bet.ui.activity;

import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.android.material.tabs.TabLayout;
import com.xtree.base.router.RouterActivityPath;
import com.xtree.base.utils.TimeUtils;
import com.xtree.bet.BR;
import com.xtree.bet.bean.ui.League;
import com.xtree.bet.constant.Constants;
import com.xtree.bet.constant.SPKey;
import com.xtree.bet.constant.SportTypeContants;
import com.xtree.bet.contract.BetContract;
import com.xtree.bet.manager.BtCarManager;
import com.xtree.bet.ui.adapter.LeagueAdapter;
import com.xtree.bet.ui.fragment.BtCarDialogFragment;
import com.xtree.bet.ui.viewmodel.MainViewModel;
import com.xtree.bet.ui.viewmodel.factory.AppViewModelFactory;
import com.xtree.bet.R;
import com.xtree.bet.databinding.FragmentMainBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import me.xtree.mvvmhabit.base.BaseActivity;
import me.xtree.mvvmhabit.utils.SPUtils;
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
    /**
     * 滚球比赛列表
     */
    private List<League> mLeagueGoingOnList = new ArrayList<>();
    private List<League> mLeagueList = new ArrayList<>();
    private LeagueAdapter mLeagueAdapter;

    private Disposable timerDisposable;

    private boolean isGoingExpand = true;
    private boolean isWatingExpand = true;
    private int searchDatePos;

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
        binding.rvLeague.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
                int topRowVerticalPosition =
                        (absListView == null || absListView.getChildCount() == 0) ? 0 : absListView.getChildAt(0).getTop();
                binding.srlLeague.setEnabled(topRowVerticalPosition >= 0);
            }
        });
        binding.llGoingOn.setOnClickListener(this);
        binding.tabPlayMethod.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                searchDatePos = 0;
                showSearchDate();
                ((TextView) tab.getCustomView()).setTextSize(16);
                if (playMethodPos != tab.getPosition()) {
                    playMethodPos = tab.getPosition();
                    BtCarManager.setIsCg(playMethodPos == 3);
                    binding.rlCg.setVisibility(!BtCarManager.isCg() ? View.GONE : BtCarManager.isEmpty() ? View.GONE : View.VISIBLE);
                    mLeagueGoingOnList.clear();
                    playMethodType = playMethodTypeList.get(tab.getPosition());
                    mLeagueList.clear();
                    updateStatisticalData();
                    if (playMethodType == 4 || playMethodType == 2) {
                        binding.tabSearchDate.setVisibility(View.VISIBLE);
                    } else {
                        binding.tabSearchDate.setVisibility(View.GONE);
                    }
                    viewModel.getLeagueList(Integer.valueOf(SportTypeContants.SPORT_IDS[sportTypePos]), 1, null, null,
                            playMethodType, searchDatePos, false);
                }
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
                showSearchDate();
                if (sportTypePos != tab.getPosition()) {
                    BtCarManager.clearBtCar();
                    setCgBtCar();
                    sportTypePos = tab.getPosition();
                    SPUtils.getInstance().put(SPKey.BT_SPORT_ID, sportTypePos);
                    mLeagueGoingOnList.clear();
                    mLeagueList.clear();
                    viewModel.getLeagueList(Integer.valueOf(SportTypeContants.SPORT_IDS[sportTypePos]), 1, null, null,
                            playMethodType, searchDatePos, false);
                }
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
                mLeagueList.clear();
                mLeagueGoingOnList.clear();
                if (searchDatePos != tab.getPosition()) {
                    if (playMethodType == 4 || playMethodType == 2) {
                        searchDatePos = tab.getPosition();
                        viewModel.getLeagueList(Integer.valueOf(SportTypeContants.SPORT_IDS[sportTypePos]), 1, null, null,
                                playMethodType, searchDatePos, false);
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        /*binding.rvLeague.setOnGroupClickListener((parent, v, groupPosition, id) -> {
            if (binding.rvLeague.isGroupExpanded(groupPosition)){
                binding.rvLeague.collapseGroupWithAnimation(groupPosition);
            }else{
                binding.rvLeague.expandGroupWithAnimation(groupPosition);
            }
            return true;
        });*/
        binding.rlCg.setOnClickListener(this);
    }

    private void showSearchDate() {
        if (playMethodType == 4 || playMethodType == 2) {
            viewModel.setPlaySearchDateData();
        }
    }

    @Override
    public void initData() {
        SPUtils.getInstance().put(SPKey.BT_SPORT_ID, sportTypePos);
        viewModel.setSportItems();
        viewModel.setPlayMethodTabData();
        viewModel.setPlaySearchDateData();
        viewModel.setFbLeagueData();
        viewModel.addSubscription();
        viewModel.statistical();
        viewModel.getLeagueList(Integer.valueOf(SportTypeContants.SPORT_IDS[sportTypePos]), 1, null, null,
                playMethodType, searchDatePos, false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        timerDisposable = Observable.interval(10, 10, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    /*int firstVisiblePos = binding.rvLeague.getFirstVisiblePosition();
                    int lastVisiblePos = binding.rvLeague.getLastVisiblePosition();
                    List<Integer> matchIdList = new ArrayList<>();
                    for(int i = firstVisiblePos; i < lastVisiblePos; i ++){
                        League league = (League)mLeagueAdapter.getGroup(i);
                        for(Match match : league.getMatchList()){
                            matchIdList.add(match.getId());
                        }
                    }*/
                    viewModel.getLeagueList(Integer.valueOf(SportTypeContants.SPORT_IDS[sportTypePos]), 1, null, null,
                            playMethodType, searchDatePos, true);
                    viewModel.statistical();
                });
        viewModel.addSubscribe(timerDisposable);
        setCgBtCar();
        if(mLeagueAdapter != null) {
            mLeagueAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        viewModel.removeSubscribe(timerDisposable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BtCarManager.destroy();
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
                if (i == 0) {
                    textView.setText("今日");
                } else if (i == dateList.size() - 1) {
                    textView.setText("其他");
                } else {
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
            if (!mLeagueGoingOnList.isEmpty() && mLeagueList.isEmpty()) {
                this.mLeagueList.addAll(mLeagueGoingOnList);
            }
            if (!leagueAdapters.isEmpty()) {
                League league = leagueAdapters.get(0).instance();
                league.setHead(true);
                this.mLeagueList.add(league);
            }
            setTopShow();
            this.mLeagueList.addAll(leagueAdapters);
            updateLeagueData();
        });

        viewModel.leagueGoingOnListData.observe(this, leagueAdapters -> {
            this.mLeagueGoingOnList.clear();
            this.mLeagueGoingOnList.addAll(leagueAdapters);
            if (playMethodType == 1) {
                this.mLeagueList.clear();
                this.mLeagueList.addAll(mLeagueGoingOnList);
                updateLeagueData();
            }
        });

        viewModel.leagueWaitingListData.observe(this, leagueAdapters -> {
            if (!mLeagueGoingOnList.isEmpty() && mLeagueList.isEmpty()) {
                this.mLeagueList.addAll(mLeagueGoingOnList);
            }
            if (!leagueAdapters.isEmpty()) {
                League league = leagueAdapters.get(0).instance();
                league.setHead(true);
                this.mLeagueList.add(league);
            }
            setTopShow();
            this.mLeagueList.addAll(leagueAdapters);
            updateLeagueData();
        });

        viewModel.leagueWaitingTimerListData.observe(this, leagueAdapters -> {
            this.mLeagueList.clear();
            this.mLeagueList.addAll(mLeagueGoingOnList);
            if(!leagueAdapters.isEmpty()) {
                League league = leagueAdapters.get(0).instance();
                league.setHead(true);
                this.mLeagueList.add(league);
            }
            setTopShow();
            this.mLeagueList.addAll(leagueAdapters);
            updateLeagueData();
        });

        viewModel.betContractListData.observe(this, betContract -> {
            if (betContract.action == BetContract.ACTION_EXPAND) {
                handleExpandContract();
            } else if (betContract.action == BetContract.ACTION_OPEN_CG) {
                binding.tabPlayMethod.selectTab(binding.tabPlayMethod.getTabAt(3));
                setCgBtCar();
                if (playMethodPos == 3) {
                    mLeagueAdapter.notifyDataSetChanged();
                }
            } else if (betContract.action == BetContract.ACTION_OPEN_TODAY) {
                binding.tabPlayMethod.selectTab(binding.tabPlayMethod.getTabAt(0));
                setCgBtCar();
            } else if (betContract.action == BetContract.ACTION_BTCAR_CHANGE) {
                setCgBtCar();
                if (playMethodPos == 3) {
                    mLeagueAdapter.notifyDataSetChanged();
                }
            }

        });
        viewModel.statisticalData.observe(this, statisticalData -> {
            this.statisticalData = statisticalData;
            updateStatisticalData();
        });
    }

    private void handleExpandContract() {
        isWatingExpand = !isWatingExpand;
        isGoingExpand = !isGoingExpand;
        int index = -1;
        for (int i = 0; i < binding.rvLeague.getExpandableListAdapter().getGroupCount(); i++) {
            League league = mLeagueList.get(i);
            if (league.isHead()) {
                index = i;
            }
            if (index > 0) {
                if (isGoingExpand) {
                    if (!binding.rvLeague.isGroupExpanded(i)) {
                        binding.rvLeague.expandGroup(i);
                    }
                } else {
                    if (binding.rvLeague.isGroupExpanded(i)) {
                        binding.rvLeague.collapseGroup(i);
                    }
                }
            }
        }
    }

    /**
     * 设置串关数量与显示与否
     */
    public void setCgBtCar(){
        binding.tvCgCount.setText(String.valueOf(BtCarManager.size()));
        binding.rlCg.setVisibility(!BtCarManager.isCg() ? View.GONE : BtCarManager.isEmpty() ? View.GONE : View.VISIBLE);
    }

    /**
     * 更新联赛数据
     */
    private void updateLeagueData() {
        if (mLeagueAdapter == null) {
            mLeagueAdapter = new LeagueAdapter(this, this.mLeagueList);
            binding.rvLeague.setAdapter(mLeagueAdapter);
            for (int i = 0; i < binding.rvLeague.getExpandableListAdapter().getGroupCount(); i++) {
                binding.rvLeague.expandGroup(i);
            }
        } else {
            mLeagueAdapter.setData(this.mLeagueList);
        }
    }

    public String getScore(int matchId){
        return viewModel.getScore(this.mLeagueList, matchId);
    }

    /**
     * +
     * 设置赛事列表头显示的信息（进行中或全部比赛）
     */
    private void setTopShow() {
        if (playMethodType == 4) {
            binding.llAllLeague.setVisibility(View.VISIBLE);
            binding.llGoingOn.setVisibility(View.GONE);
        } else if (playMethodType == 7) {
            binding.llAllLeague.setVisibility(View.GONE);
            binding.llGoingOn.setVisibility(View.GONE);
        } else {
            binding.llAllLeague.setVisibility(View.GONE);
            binding.llGoingOn.setVisibility(View.VISIBLE);
        }
    }

    private void updateStatisticalData() {
        if (statisticalData != null) {
            int tabCount = binding.tabSportType.getTabCount();
            for (int i = 0; i < tabCount; i++) {
                View view = binding.tabSportType.getTabAt(i).getCustomView();
                TextView tvCount = view.findViewById(R.id.iv_match_count);
                tvCount.setText(String.valueOf(statisticalData.get(String.valueOf(playMethodType)).get(i)));
            }
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
            isWatingExpand = !isWatingExpand;
            for (int i = 0; i < binding.rvLeague.getExpandableListAdapter().getGroupCount(); i++) {
                League league = mLeagueList.get(i);
                if (league.isHead()) {
                    return;
                }
                if (isWatingExpand) {
                    if (!binding.rvLeague.isGroupExpanded(i)) {
                        binding.rvLeague.expandGroup(i);
                    }
                } else {
                    if (binding.rvLeague.isGroupExpanded(i)) {
                        binding.rvLeague.collapseGroup(i);
                    }
                }

            }
        } else if (id == R.id.rl_cg) {
            if (BtCarManager.size() <= 1) {
                ToastUtils.showLong(getText(R.string.bt_bt_must_have_two_match));
                return;
            }
            BtCarDialogFragment btCarDialogFragment = new BtCarDialogFragment();
            btCarDialogFragment.show(MainActivity.this.getSupportFragmentManager(), "btCarDialogFragment");
        }
    }
}
