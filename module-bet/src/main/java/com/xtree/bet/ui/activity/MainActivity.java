package com.xtree.bet.ui.activity;

import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.android.material.tabs.TabLayout;
import com.gyf.immersionbar.ImmersionBar;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
import com.xtree.base.router.RouterActivityPath;
import com.xtree.base.utils.TimeUtils;
import com.xtree.bet.BR;
import com.xtree.bet.bean.ui.League;
import com.xtree.bet.bean.ui.Match;
import com.xtree.bet.constant.Constants;
import com.xtree.bet.constant.SPKey;
import com.xtree.bet.contract.BetContract;
import com.xtree.bet.databinding.FragmentMainBinding;
import com.xtree.bet.manager.BtCarManager;
import com.xtree.bet.ui.adapter.ChampionMatchAdapter;
import com.xtree.bet.ui.adapter.LeagueAdapter;
import com.xtree.bet.ui.fragment.BtCarDialogFragment;
import com.xtree.bet.ui.fragment.BtRecordDialogFragment;
import com.xtree.bet.ui.fragment.BtSettingDialogFragment;
import com.xtree.bet.ui.viewmodel.fb.FBMainViewModel;
import com.xtree.bet.ui.viewmodel.pm.PMMainViewModel;
import com.xtree.bet.ui.viewmodel.TemplateMainViewModel;
import com.xtree.bet.ui.viewmodel.factory.AppViewModelFactory;
import com.xtree.bet.R;
import com.xtree.bet.ui.viewmodel.factory.PMAppViewModelFactory;
import com.xtree.bet.weight.MenuItemView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import me.majiajie.pagerbottomtabstrip.NavigationController;
import me.majiajie.pagerbottomtabstrip.item.BaseTabItem;
import me.majiajie.pagerbottomtabstrip.listener.OnTabItemSelectedListener;
import me.xtree.mvvmhabit.base.BaseActivity;
import me.xtree.mvvmhabit.base.BaseViewModel;
import me.xtree.mvvmhabit.utils.SPUtils;
import me.xtree.mvvmhabit.utils.ToastUtils;

/**
 * Created by goldze on 2018/6/21
 */
@Route(path = RouterActivityPath.Bet.PAGER_BET_HOME)
public class MainActivity extends BaseActivity<FragmentMainBinding, TemplateMainViewModel> implements OnRefreshLoadMoreListener, View.OnClickListener {
    public final static String KEY_PLATFORM = "KEY_PLATFORM";
    public final static String PLATFORM_FB = "fbxc";
    private String mPlatform = PLATFORM_FB;

    /**
     * 赛事统计数据
     */
    private Map<String, List<Integer>> statisticalData;
    /**
     * 滚球比赛列表
     */
    private List<League> mLeagueGoingOnList = new ArrayList<>();
    private List<League> mLeagueList = new ArrayList<>();
    private List<Match> mChampionMatchList = new ArrayList<>();
    private LeagueAdapter mLeagueAdapter;
    private ChampionMatchAdapter mChampionMatchAdapter;
    private List<League> settingLeagueList = new ArrayList<>();
    private List<Long> mLeagueIdList = new ArrayList<>();

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
    private int mOrderBy = 1;
    private int mOddType = 1;

    public List<League> getSettingLeagueList() {
        return settingLeagueList;
    }

    public int getPlayMethodType() {
        return playMethodType;
    }

    public int getSportId() {
        return Integer.valueOf(viewModel.getSportId(playMethodType)[sportTypePos]);
    }

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.fragment_main;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initParam() {
        mPlatform = getIntent().getStringExtra(KEY_PLATFORM);
        SPUtils.getInstance().put(KEY_PLATFORM, mPlatform);
    }

    @Override
    protected void initImmersionBar() {
        //设置共同沉浸式样式
        ImmersionBar.with(this)
                .fitsSystemWindows(true)
                .statusBarDarkFont(true)
                .init();
    }

    @Override
    public TemplateMainViewModel initViewModel() {
        if (TextUtils.equals(mPlatform, PLATFORM_FB)) {
            AppViewModelFactory factory = AppViewModelFactory.getInstance(getApplication());
            return new ViewModelProvider(this, factory).get(FBMainViewModel.class);
        } else {
            PMAppViewModelFactory factory = PMAppViewModelFactory.getInstance(getApplication());
            return new ViewModelProvider(this, factory).get(PMMainViewModel.class);
        }
    }

    @Override
    public void initView() {
        binding.srlLeague.setOnRefreshLoadMoreListener(this);
        binding.llGoingOn.setOnClickListener(this);
        binding.ivBack.setOnClickListener(this);
        binding.tabPlayMethod.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                ((TextView) tab.getCustomView()).setTextSize(16);
                if (playMethodPos != tab.getPosition()) {
                    searchDatePos = 0;
                    playMethodType = playMethodTypeList.get(tab.getPosition());
                    showSearchDate();
                    playMethodPos = tab.getPosition();
                    if (playMethodType == 6) {
                        binding.srlLeague.setEnableLoadMore(false);
                    } else {
                        binding.srlLeague.setEnableLoadMore(true);
                    }
                    BtCarManager.setIsCg(playMethodPos == 3);
                    binding.rlCg.setVisibility(!BtCarManager.isCg() ? View.GONE : BtCarManager.isEmpty() ? View.GONE : View.VISIBLE);
                    mLeagueGoingOnList.clear();
                    mLeagueList.clear();
                    updateStatisticalData();
                    if (playMethodType == 4 || playMethodType == 2 || playMethodType == 11) {
                        binding.tabSearchDate.setVisibility(View.VISIBLE);
                    } else {
                        binding.tabSearchDate.setVisibility(View.GONE);
                    }
                    viewModel.statistical(playMethodType);
                    initTimer();
                    getMatchData(Integer.valueOf(viewModel.getSportId(playMethodType)[sportTypePos]), mOrderBy, mLeagueIdList, null,
                            playMethodType, searchDatePos, false, true);

                    viewModel.getOnSaleLeagues(Integer.valueOf(viewModel.getSportId(playMethodType)[sportTypePos]), playMethodType);
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

                if (sportTypePos != tab.getPosition()) {
                    searchDatePos = 0;
                    showSearchDate();
                    BtCarManager.clearBtCar();
                    setCgBtCar();
                    sportTypePos = tab.getPosition();
                    SPUtils.getInstance().put(SPKey.BT_SPORT_ID, sportTypePos);
                    mLeagueGoingOnList.clear();
                    mLeagueList.clear();
                    viewModel.statistical(playMethodType);
                    initTimer();
                    getMatchData(Integer.valueOf(viewModel.getSportId(playMethodType)[sportTypePos]), mOrderBy, mLeagueIdList, null,
                            playMethodType, searchDatePos, false, true);
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
                    if (playMethodType == 4 || playMethodType == 2 || playMethodType == 11) {
                        searchDatePos = tab.getPosition();
                        viewModel.statistical(playMethodType);
                        initTimer();
                        getMatchData(Integer.valueOf(viewModel.getSportId(playMethodType)[sportTypePos]), mOrderBy, mLeagueIdList, null,
                                playMethodType, searchDatePos, false, true);
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
        initBottomTab();
    }

    private void initBottomTab() {
        NavigationController navigationController = binding.pagerBottomTab.custom()
                .addItem(newItem(R.mipmap.bt_icon_menu_tutorial, getResources().getString(R.string.bt_bt_menu_course)))
                .addItem(newItem(R.mipmap.bt_icon_menu_setting, getResources().getString(R.string.bt_bt_menu_setting)))
                .addItem(newItem(R.mipmap.bt_icon_menu_unbet, getResources().getString(R.string.bt_bt_menu_unbet)))
                .addItem(newItem(R.mipmap.bt_icon_menu_bet, getResources().getString(R.string.bt_bt_menu_bet)))
                .addItem(newItem(R.mipmap.bt_icon_menu_refresh, getResources().getString(R.string.bt_bt_menu_refresh)))
                .build();
        //navigationController.setMessageNumber(2, 8);
        //底部按钮的点击事件监听
        navigationController.addTabItemSelectedListener(new OnTabItemSelectedListener() {
            @Override
            public void onSelected(int index, int old) {
                menuOnClick(index);
            }

            @Override
            public void onRepeat(int index) {
                menuOnClick(index);
            }
        });
    }

    private void menuOnClick(int index) {
        if (index == 4) {
            refreshLeague();
        } else if (index == 2) {
            BtRecordDialogFragment btRecordDialogFragment = BtRecordDialogFragment.getInstance(false);
            btRecordDialogFragment.show(getSupportFragmentManager(), "BtRecordDialogFragment");
        } else if (index == 3) {
            BtRecordDialogFragment btRecordDialogFragment = BtRecordDialogFragment.getInstance(true);
            btRecordDialogFragment.show(getSupportFragmentManager(), "BtRecordDialogFragment");
        } else if (index == 1) {
            BtSettingDialogFragment btSettingDialogFragment = BtSettingDialogFragment.getInstance(mLeagueIdList);
            btSettingDialogFragment.show(getSupportFragmentManager(), "BtSettingDialogFragment");
        }
    }

    //创建一个Item
    private BaseTabItem newItem(int drawable, String text) {
        MenuItemView normalItemView = new MenuItemView(this);
        normalItemView.initialize(drawable, drawable, text);
        normalItemView.setTextDefaultColor(getResources().getColor(R.color.bt_text_color_primary));
        normalItemView.setTextCheckedColor(getResources().getColor(R.color.bt_text_color_primary));
        return normalItemView;
    }

    private void showSearchDate() {
        viewModel.setPlaySearchDateData();
    }

    private void getMatchData(int sportId, int orderBy, List<Long> leagueIds, List<Long> matchids, int playMethodType, int searchDatePos, boolean isTimedRefresh, boolean isRefresh) {
        if (playMethodType == 7 || playMethodType == 100) { // 冠军
            viewModel.getChampionList(sportTypePos, sportId, orderBy, leagueIds, matchids,
                    playMethodType, mOddType, isTimedRefresh, isRefresh);
        } else {
            viewModel.getLeagueList(sportTypePos, sportId, orderBy, leagueIds, matchids,
                    playMethodType, searchDatePos, mOddType, isTimedRefresh, isRefresh);
        }
    }

    private void initTimer() {
        if (timerDisposable != null) {
            viewModel.removeSubscribe(timerDisposable);
        }
        timerDisposable = Observable.interval(5, 5, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    refreshLeague();
                    viewModel.statistical(playMethodType);
                });
        viewModel.addSubscribe(timerDisposable);
    }

    private void refreshLeague() {
        List<League> updateLeague = new ArrayList<>();
        int firstVisiblePos = binding.rvLeague.getFirstVisiblePosition();
        int lastVisiblePos = binding.rvLeague.getLastVisiblePosition();
        List<Long> matchIdList = new ArrayList<>();
        if(playMethodPos != 4) {
            for (int i = firstVisiblePos; i <= lastVisiblePos; i++) {
                long position = binding.rvLeague.getExpandableListPosition(i);
                int type = binding.rvLeague.getPackedPositionType(position);

                if (type == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
                    int childPosition = binding.rvLeague.getPackedPositionChild(i);
                    Match match = (Match)binding.rvLeague.getItemAtPosition(i);
                    if(match != null) {
                        matchIdList.add(match.getId());
                    }
                }
            }
        }else {
            for (Match match : mChampionMatchList) {
                matchIdList.add(match.getId());
            }
        }
        if (!matchIdList.isEmpty()) {
            //viewModel.setUpdateLeagueList(updateLeague);
            getMatchData(Integer.valueOf(viewModel.getSportId(playMethodType)[sportTypePos]), mOrderBy, null, matchIdList,
                    playMethodType, searchDatePos, true, false);
        }
    }

    @Override
    public void initData() {
        playMethodType = Integer.valueOf(viewModel.getPlayMethodTypes()[0]);
        SPUtils.getInstance().put(SPKey.BT_SPORT_ID, sportTypePos);
        viewModel.setSportItems();
        viewModel.setPlayMethodTabData();
        //viewModel.setPlaySearchDateData();
        viewModel.setFbLeagueData();
        viewModel.addSubscription();
        mOrderBy = SPUtils.getInstance().getInt(SPKey.BT_MATCH_LIST_ORDERBY, 1);
        mOddType = SPUtils.getInstance().getInt(SPKey.BT_MATCH_LIST_ODDTYPE, 1);
        viewModel.statistical(playMethodType);
        getMatchData(Integer.valueOf(viewModel.getSportId(playMethodType)[sportTypePos]), mOrderBy, mLeagueIdList, null,
                playMethodType, searchDatePos, false, true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initTimer();
        setCgBtCar();
        if (mLeagueAdapter != null) {
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
                playMethodTypeList.add(Integer.valueOf(viewModel.getPlayMethodTypes()[i]));
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
            if (playMethodType == 4 || playMethodType == 2 || playMethodType == 11) {
                binding.tabSearchDate.setVisibility(View.VISIBLE);
            } else {
                binding.tabSearchDate.setVisibility(View.GONE);
            }
            for (int i = 0; i < dateList.size(); i++) {
                TextView textView = new TextView(this);
                if (i == 0) {
                    if (playMethodType == 2 || playMethodType == 11) {
                        textView.setText("今日");
                    } else {
                        textView.setText("全部");
                    }
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

        viewModel.leagueGoingOnListData.observe(this, leagueAdapters -> {
            this.mLeagueList = leagueAdapters;
            if (playMethodType == 1) {
                updateData();
            }
        });

        viewModel.leagueGoingOnTimerListData.observe(this, leagueList -> {
            this.mLeagueList = leagueList;
            updateData();
        });

        viewModel.leagueWaitingListData.observe(this, leagueAdapters -> {
            this.mLeagueList = leagueAdapters;
            setTopShow();
            updateData();
        });

        viewModel.leagueWaitingTimerListData.observe(this, leagueAdapters -> {
            this.mLeagueList = leagueAdapters;
            setTopShow();
            updateData();
        });

        viewModel.championMatchTimerListData.observe(this, championMatcheList -> {
            mChampionMatchList = championMatcheList;
            updateData();
        });

        viewModel.championMatchListData.observe(this, championMatcheList -> {
            mChampionMatchList = championMatcheList;
            setTopShow();
            updateData();
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
            } else if (betContract.action == BetContract.ACTION_SORT_CHANGE) {
                mOrderBy = (int) betContract.getData();
                getMatchData(Integer.valueOf(viewModel.getSportId(playMethodType)[sportTypePos]), mOrderBy, mLeagueIdList, null,
                        playMethodType, searchDatePos, false, true);
            } else if (betContract.action == BetContract.ACTION_MARKET_CHANGE) {
                mOddType = (int) betContract.getData();
                updateData();
            } else if (betContract.action == BetContract.ACTION_CHECK_SEARCH_BY_LEAGUE) {
                mLeagueIdList = (List<Long>) betContract.getData();
                getMatchData(Integer.valueOf(viewModel.getSportId(playMethodType)[sportTypePos]), mOrderBy, mLeagueIdList, null,
                        playMethodType, searchDatePos, false, true);
            }

        });
        viewModel.statisticalData.observe(this, statisticalData -> {
            this.statisticalData = statisticalData;
            updateStatisticalData();
        });

        viewModel.getUC().getSmartRefreshListenerEvent().observe(this, integer -> {
            if (integer == BaseViewModel.ONFINISH_LOAD_MORE) {
                binding.srlLeague.finishLoadMore();
            } else if (integer == BaseViewModel.ONFINISH_REFRESH) {
                binding.srlLeague.finishRefresh();
            } else if (integer == BaseViewModel.ON_LOAD_MORE_WITH_NO_MORE_DATA) {
                binding.srlLeague.finishLoadMoreWithNoMoreData();
            } else if (integer == BaseViewModel.ONFINISH_REFRESH_FAILED) {
                binding.srlLeague.finishRefresh(false);
            } else if (integer == BaseViewModel.ONFINISH_LOAD_MORE_FAILED) {
                binding.srlLeague.finishLoadMore(false);
            }
        });

        viewModel.settingLeagueData.observe(this, leagueList -> {
            settingLeagueList = leagueList;
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
    public void setCgBtCar() {
        binding.tvCgCount.setText(String.valueOf(BtCarManager.size()));
        binding.rlCg.setVisibility(!BtCarManager.isCg() ? View.GONE : BtCarManager.isEmpty() ? View.GONE : View.VISIBLE);
    }

    private void updateData() {
        if (playMethodPos == 4) {
            updateChampionMatchData();
        } else {
            updateLeagueData();
        }
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
            if (!(binding.rvLeague.getExpandableListAdapter() instanceof LeagueAdapter)) {
                binding.rvLeague.setAdapter(mLeagueAdapter);
            }
            mLeagueAdapter.setData(this.mLeagueList);
        }

    }

    /**
     * 更新冠军赛事
     */
    private void updateChampionMatchData() {
        if (mChampionMatchAdapter == null) {
            mChampionMatchAdapter = new ChampionMatchAdapter(this, this.mChampionMatchList);
            binding.rvLeague.setAdapter(mChampionMatchAdapter);
        } else {
            if (!(binding.rvLeague.getExpandableListAdapter() instanceof ChampionMatchAdapter)) {
                binding.rvLeague.setAdapter(mChampionMatchAdapter);
            }
            mChampionMatchAdapter.setData(this.mChampionMatchList);
        }
    }

    /**
     * 获取当前比分
     *
     * @param matchId
     * @return
     */
    public String getScore(long matchId) {
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
                Integer count = statisticalData.get(String.valueOf(playMethodType)).get(i);
                if (count == null) {
                    count = 0;
                }
                tvCount.setText(String.valueOf(count));
            }
        }
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
        } else if (id == R.id.iv_back) {
            finish();
        }
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        getMatchData(Integer.valueOf(viewModel.getSportId(playMethodType)[sportTypePos]), mOrderBy, mLeagueIdList, null,
                playMethodType, searchDatePos, false, true);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        getMatchData(Integer.valueOf(viewModel.getSportId(playMethodType)[sportTypePos]), mOrderBy, mLeagueIdList, null,
                playMethodType, searchDatePos, false, false);
    }
}
