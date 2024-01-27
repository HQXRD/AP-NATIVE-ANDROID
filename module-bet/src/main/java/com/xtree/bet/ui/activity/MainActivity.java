package com.xtree.bet.ui.activity;

import android.animation.ObjectAnimator;
import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.gyf.immersionbar.ImmersionBar;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
import com.xtree.base.router.RouterActivityPath;
import com.xtree.base.utils.TimeUtils;
import com.xtree.base.widget.MenuItemView;
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
import com.xtree.bet.weight.AnimatedExpandableListViewMax;
import com.xtree.bet.weight.PageHorizontalScrollView;

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
    public final static String KEY_PLATFORM_NAME = "KEY_PLATFORM_NAME";
    public final static String PLATFORM_FBXC = "fbxc";
    public final static String PLATFORM_FB = "fb";
    public final static String PLATFORM_PM = "obg";
    public final static String BET_EXPAND = "betExpand";

    private String mPlatform = PLATFORM_FBXC;
    private String mPlatformName;
    private boolean mIsShowLoading = true;
    private boolean mIsChange;
    private int mLastLeagueSize;
    /**
     * 赛事统计数据
     */
    private Map<String, List<Integer>> mStatisticalData;
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

    private int searchDatePos;

    /**
     * 玩法类型，默认为今日+滚球
     */
    private int playMethodType = 6;
    private int playMethodPos;
    private int sportTypePos;
    private int mOrderBy = 1;
    private int mOddType = 1;

    private NavigationController navigationController;
    private MenuItemView refreshMenu;
    private View mHeader;
    private ImageView ivHeaderExpand;
    private TextView tvHeaderName;
    private int mHotMatchCount;

    public List<League> getSettingLeagueList() {
        return settingLeagueList;
    }

    public int getPlayMethodType() {
        return playMethodType;
    }

    public int getSportId() {
        String sportId = viewModel.getSportId(playMethodType)[sportTypePos];
        if (sportId == null) {
            return 0;
        }
        return Integer.valueOf(sportId);
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
        mPlatformName = getIntent().getStringExtra(KEY_PLATFORM_NAME);
        SPUtils.getInstance().put(KEY_PLATFORM, mPlatform);
        SPUtils.getInstance().put(KEY_PLATFORM_NAME, mPlatformName);
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
        if (!TextUtils.equals(mPlatform, PLATFORM_PM)) {
            AppViewModelFactory factory = AppViewModelFactory.getInstance(getApplication());
            return new ViewModelProvider(this, factory).get(FBMainViewModel.class);
        } else {
            PMAppViewModelFactory factory = PMAppViewModelFactory.getInstance(getApplication());
            return new ViewModelProvider(this, factory).get(PMMainViewModel.class);
        }
    }

    @Override
    public void showDialog(String title) {
        if (binding.ivLoading.getVisibility() == View.GONE && mIsShowLoading) {
            binding.ivLoading.setVisibility(View.VISIBLE);
            ObjectAnimator.ofFloat(binding.ivLoading, "rotation", 0f, 360f).setDuration(700).start();
        }
    }

    @Override
    public void dismissDialog() {
        binding.ivLoading.clearAnimation();
        binding.ivLoading.setVisibility(View.GONE);
    }

    @Override
    public void initView() {
        binding.srlLeague.setOnRefreshLoadMoreListener(this);
        binding.ivBack.setOnClickListener(this);
        binding.tabPlayMethod.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                ((TextView) tab.getCustomView()).setTextSize(16);
                mIsChange = true;
                sportTypePos = 0;
                if (playMethodPos != tab.getPosition()) {
                    searchDatePos = 0;
                    playMethodType = playMethodTypeList.get(tab.getPosition());
                    showSearchDate();
                    playMethodPos = tab.getPosition();
                    if (playMethodPos == 4) {
                        binding.rvLeague.removeHeader();
                    }
                    if (tab.getPosition() == 4) {
                        binding.srlLeague.setEnableLoadMore(false);
                    } else {
                        binding.srlLeague.setEnableLoadMore(true);
                    }
                    BtCarManager.setIsCg(playMethodPos == 3);
                    binding.rlCg.setVisibility(!BtCarManager.isCg() ? View.GONE : BtCarManager.isEmpty() ? View.GONE : View.VISIBLE);
                    mLeagueGoingOnList.clear();
                    mLeagueList.clear();
                    updateStatisticalData();

                    viewModel.setSportIcons(playMethodPos);
                    viewModel.setSportItems(playMethodPos);


                    if (playMethodType == 4 || playMethodType == 2 || playMethodType == 11) {
                        binding.tabSearchDate.setVisibility(View.VISIBLE);
                    } else {
                        binding.tabSearchDate.setVisibility(View.GONE);
                    }
                    viewModel.statistical(playMethodType);
                    initTimer();
                    if (playMethodPos == 2 || playMethodPos == 4) {
                        getMatchData(viewModel.getSportId(playMethodType)[sportTypePos], mOrderBy, mLeagueIdList, null,
                                playMethodType, searchDatePos, false, true);
                    } else if (playMethodPos == 0 || playMethodPos == 3) {
                        viewModel.getHotMatchCount(playMethodType, viewModel.hotLeagueList);
                    }
                    //viewModel.getOnSaleLeagues(Integer.valueOf(viewModel.getSportId(playMethodType)[sportTypePos]), playMethodType);
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
                    mIsChange = true;
                    showSearchDate();
                    BtCarManager.clearBtCar();
                    setCgBtCar();
                    sportTypePos = tab.getPosition();
                    SPUtils.getInstance().put(SPKey.BT_SPORT_ID, sportTypePos);
                    mLeagueGoingOnList.clear();
                    mLeagueList.clear();
                    viewModel.statistical(playMethodType);
                    initTimer();
                    String sportId = viewModel.getSportId(playMethodType)[sportTypePos];
                    getMatchData(sportId, mOrderBy, mLeagueIdList, null,
                            playMethodType, searchDatePos, false, true);
                    if ((sportId == null || TextUtils.equals("0", sportId)) && (playMethodPos == 0 || playMethodPos == 3)) {
                        viewModel.getHotMatchCount(playMethodType, viewModel.hotLeagueList);
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
        binding.tabSearchDate.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mLeagueList.clear();
                mLeagueGoingOnList.clear();
                if (searchDatePos != tab.getPosition()) {
                    mIsChange = true;
                    if (playMethodType == 4 || playMethodType == 2 || playMethodType == 11) {
                        searchDatePos = tab.getPosition();
                        viewModel.statistical(playMethodType);
                        initTimer();
                        getMatchData(viewModel.getSportId(playMethodType)[sportTypePos], mOrderBy, mLeagueIdList, null,
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
        binding.rvLeague.setOnGroupClickListener((parent, v, groupPosition, id) -> {
            if (binding.rvLeague.isGroupExpanded(groupPosition)) {
                binding.rvLeague.collapseGroup(groupPosition);
            } else {
                binding.rvLeague.expandGroup(groupPosition);
            }
            binding.rvLeague.postDelayed(() -> {
                if(playMethodPos == 4){
                    checkChampionHeaderIsExpand();
                }else {
                    checkLeagueHeaderIsExpand(groupPosition);
                }
            }, 150);

            return true;
        });
        binding.rlCg.setOnClickListener(this);
        initBottomTab();
    }

    private void checkChampionHeaderIsExpand() {
        if (mChampionMatchAdapter == null) {
            return;
        }
        boolean hasExpand = isChampionHasExand();
        setChampionAllExpand(hasExpand);
        mChampionMatchAdapter.notifyDataSetChanged();
        ivHeaderExpand.setSelected(hasExpand);
    }

    private void checkLeagueHeaderIsExpand(int groupPosition) {
        if (mLeagueAdapter == null) {
            return;
        }
        int noLiveHeaderPosition = mLeagueAdapter.getNoLiveHeaderPosition();
        boolean hasExpand;
        if (noLiveHeaderPosition == 0) {
            hasExpand = isGoingOnHasExand();
            setGoingOnAllExpand(hasExpand);
        } else {

            if (groupPosition < noLiveHeaderPosition) {
                hasExpand = isGoingOnHasExand();
                setGoingOnAllExpand(hasExpand);
            } else {
                hasExpand = isWaitingHasExpand();
                setWaitingAllExpand(hasExpand);
            }
        }
        mLeagueAdapter.notifyDataSetChanged();
    }

    /**
     * 处理点击进行中或未开赛header展开收起事件
     *
     * @param betContract
     */
    private void handleExpandContract(BetContract betContract) {
        String data = (String) betContract.getData();
        int start = Integer.valueOf(data.split("/")[0]);
        int end = Integer.valueOf(data.split("/")[1]);

        boolean isGoingOn = mLeagueAdapter.isHandleGoingOnExpand(start); // 是否处理进行中的展开收起事件
        if (isGoingOn) {
            setGoingOnAllExpand(!isGoingOnAllExpand());
            mLeagueAdapter.setHeaderIsExpand(mLeagueAdapter.getLiveHeaderPosition(), isGoingOnAllExpand());
        } else {
            setWaitingAllExpand(!isWaitingAllExpand());
            mLeagueAdapter.setHeaderIsExpand(mLeagueAdapter.getNoLiveHeaderPosition(), isWaitingAllExpand());
        }
        boolean isAllExpand = isGoingOn ? isGoingOnAllExpand() : isWaitingAllExpand();
        binding.rvLeague.postDelayed(() -> {
            for (int i = start; i < end; i++) {
                if (isAllExpand) {
                    binding.rvLeague.expandGroup(i);
                } else {
                    binding.rvLeague.collapseGroup(i);
                }
            }
        }, 100);
    }

    private void initLeagueListView() {
        mHeader = LayoutInflater.from(this).inflate(R.layout.bt_fb_league_group_header, binding.rvLeague, false);
        ivHeaderExpand = mHeader.findViewById(R.id.iv_expand);
        ivHeaderExpand.setSelected(isGoingOnAllExpand());
        tvHeaderName = mHeader.findViewById(R.id.tv_header_name);
        binding.rvLeague.addHeader(mHeader);
        binding.rvLeague.setOnHeaderClick(() -> {
            if (playMethodPos == 4) {
                onChamptionHeaderClick();
            } else {
                onLeagueHeaderClick();
            }
        });
        binding.rvLeague.setOnScrollListener(new AnimatedExpandableListViewMax.OnScrollListenerImpl(mHeader, binding.rvLeague) {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                super.onScrollStateChanged(view, scrollState);
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //super.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
                if (playMethodPos == 4) {
                    onScrollChampion(firstVisibleItem);
                } else {
                    onScrollLeague(firstVisibleItem);
                }

            }
        });
    }

    private void onChamptionHeaderClick() {
        int start = 1;
        int end = mChampionMatchList.size();
        setChampionAllExpand(!isChampionAllExpand());
        boolean isAllExpand = isChampionAllExpand();
        binding.rvLeague.postDelayed(() -> {
            if (mChampionMatchList.size() > start) {
                for (int i = start; i < end; i++) {
                    if (isAllExpand) {
                        binding.rvLeague.expandGroup(i);
                    } else {
                        binding.rvLeague.collapseGroup(i);
                    }
                }
            }
        }, 100);
    }

    private void onLeagueHeaderClick() {
        int start;
        int end;
        boolean isAllExpand;
        int firstGroup = binding.rvLeague.getPackedPositionGroup(binding.rvLeague.getExpandableListPosition(binding.rvLeague.getFirstVisiblePosition()));
        if (mLeagueAdapter.getNoLiveHeaderPosition() > firstGroup) { // 点击进行中
            setGoingOnAllExpand(!isGoingOnAllExpand());
            ivHeaderExpand.setSelected(isGoingOnAllExpand());
            isAllExpand = isGoingOnAllExpand();
            start = 2;
            end = mLeagueAdapter.getNoLiveHeaderPosition() > 0 ? mLeagueAdapter.getNoLiveHeaderPosition() : mLeagueAdapter.getGroupCount();
        } else {
            setWaitingAllExpand(!isWaitingAllExpand());
            ivHeaderExpand.setSelected(isWaitingAllExpand());
            isAllExpand = isWaitingAllExpand();
            if (mLeagueAdapter.getNoLiveHeaderPosition() > 0) {
                start = mLeagueAdapter.getNoLiveHeaderPosition() + 2;
            } else { // 点击未开赛
                start = 0;
            }
            end = mLeagueAdapter.getGroupCount();
        }
        binding.rvLeague.postDelayed(() -> {
            if (mLeagueList.size() > start) {
                for (int i = start; i < end; i++) {
                    if (isAllExpand) {
                        binding.rvLeague.expandGroup(i);
                    } else {
                        binding.rvLeague.collapseGroup(i);
                    }
                }
            }
        }, 100);
    }

    private void onScrollChampion(int firstVisibleItem) {
        if (mHeader == null || mChampionMatchList == null || mChampionMatchList.isEmpty()) {
            return;
        }
        int firstGroup = binding.rvLeague.getPackedPositionGroup(binding.rvLeague.getExpandableListPosition(firstVisibleItem));
        if (mChampionMatchList.get(firstGroup).isHead()) {
            ivHeaderExpand.setSelected(isChampionAllExpand());
            tvHeaderName.setText(getResources().getString(R.string.bt_all_league));
        }
    }

    private void onScrollLeague(int firstVisibleItem) {
        if (mHeader == null || mLeagueList == null || mLeagueList.isEmpty()) {
            return;
        }
        int firstGroup = binding.rvLeague.getPackedPositionGroup(binding.rvLeague.getExpandableListPosition(firstVisibleItem));
        int nextGroup = binding.rvLeague.getPackedPositionGroup(binding.rvLeague.getExpandableListPosition(firstVisibleItem + 1));

        View child = binding.rvLeague.getChildAt(1);
        if (child == null) {
            return;
        }
        int top = child.getTop();
        int measuredHeight = mHeader.getMeasuredHeight();
        int measuredWidth = mHeader.getMeasuredWidth();

        if (firstGroup != 0 && mLeagueList.get(nextGroup).isHead() && mLeagueList.get(nextGroup).getHeadType() == League.HEAD_TYPE_LIVE_OR_NOLIVE) {
            if (top < measuredHeight) {
                int dy = measuredHeight - top;
                mHeader.layout(0, -dy, measuredWidth, measuredHeight - dy);
            } else {
                mHeader.layout(0, 0, measuredWidth, measuredHeight);
            }
        } else {
            mHeader.layout(0, 0, measuredWidth, measuredHeight);
        }

        if (mLeagueList.get(firstGroup).isHead() && mLeagueList.get(firstGroup).getHeadType() == League.HEAD_TYPE_LIVE_OR_NOLIVE) {
            ivHeaderExpand.setSelected(isWaitingAllExpand());
            tvHeaderName.setText(mLeagueList.get(firstGroup).getLeagueName());
        }
        if (mLeagueAdapter.getNoLiveHeaderPosition() > firstGroup) {
            ivHeaderExpand.setSelected(isGoingOnAllExpand());
            tvHeaderName.setText("进行中");
        }
    }

    private void setHeader(League league) {
        ((TextView) mHeader.findViewById(R.id.tv_league_name)).setText(league.getLeagueName());
        Glide.with(MainActivity.this)
                .load(league.getIcon())
                //.apply(new RequestOptions().placeholder(placeholderRes))
                .into((ImageView) mHeader.findViewById(R.id.iv_icon));
        ((ImageView) mHeader.findViewById(R.id.group_indicator)).setImageResource(league.isExpand() ? R.mipmap.bt_icon_expand : R.mipmap.bt_icon_unexpand);
    }

    private void initBottomTab() {
        refreshMenu = (MenuItemView) newItem(R.mipmap.bt_icon_menu_refresh, getResources().getString(R.string.bt_bt_menu_refresh));
        navigationController = binding.pagerBottomTab.custom()
                .addItem(newItem(R.mipmap.bt_icon_menu_tutorial, getResources().getString(R.string.bt_bt_menu_course)))
                .addItem(newItem(R.mipmap.bt_icon_menu_setting, getResources().getString(R.string.bt_bt_menu_setting)))
                .addItem(newItem(R.mipmap.bt_icon_menu_unbet, getResources().getString(R.string.bt_bt_menu_unbet)))
                .addItem(newItem(R.mipmap.bt_icon_menu_bet, getResources().getString(R.string.bt_bt_menu_bet)))
                .addItem(refreshMenu)
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
            refreshMenu.rotation();
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

    private void getMatchData(String sportId, int orderBy, List<Long> leagueIds, List<Long> matchids, int playMethodType, int searchDatePos, boolean isTimedRefresh, boolean isRefresh) {
        if (playMethodType == 7 || playMethodType == 100) { // 冠军
            viewModel.getChampionList(sportTypePos, sportId, orderBy, leagueIds, matchids,
                    playMethodType, mOddType, isTimedRefresh, isRefresh);
        } else {
            if (sportTypePos == 0 && (playMethodPos == 0 || playMethodPos == 3)) {
                viewModel.getLeagueList(sportTypePos, sportId, orderBy, viewModel.hotLeagueList, matchids,
                        playMethodType, searchDatePos, mOddType, isTimedRefresh, isRefresh);
            } else {
                viewModel.getLeagueList(sportTypePos, sportId, orderBy, leagueIds, matchids,
                        playMethodType, searchDatePos, mOddType, isTimedRefresh, isRefresh);
            }
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
                    //refreshLeague();
                    //viewModel.statistical(playMethodType);
                });
        viewModel.addSubscribe(timerDisposable);
    }

    private void refreshLeague() {
        int firstVisiblePos = binding.rvLeague.getFirstVisiblePosition();
        int lastVisiblePos = binding.rvLeague.getLastVisiblePosition();
        List<Long> matchIdList = new ArrayList<>();
        if (playMethodPos != 4) {
            for (int i = firstVisiblePos; i <= lastVisiblePos; i++) {
                long position = binding.rvLeague.getExpandableListPosition(i);
                int type = binding.rvLeague.getPackedPositionType(position);

                if (type == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
                    Match match = (Match) binding.rvLeague.getItemAtPosition(i);
                    if (match != null) {
                        matchIdList.add(match.getId());
                    }
                }
            }
        } else {
            for (Match match : mChampionMatchList) {
                matchIdList.add(match.getId());
            }
        }
        if (!matchIdList.isEmpty()) {
            getMatchData(viewModel.getSportId(playMethodType)[sportTypePos], mOrderBy, null, matchIdList,
                    playMethodType, searchDatePos, true, false);
        }
    }

    @Override
    public void initData() {
        playMethodType = Integer.valueOf(viewModel.getPlayMethodTypes()[0]);
        SPUtils.getInstance().put(SPKey.BT_SPORT_ID, sportTypePos);
        viewModel.getHotLeague(mPlatform);
        viewModel.setSportItems(playMethodPos);
        viewModel.setPlayMethodTabData();
        //viewModel.setPlaySearchDateData();
        viewModel.addSubscription();
        mOrderBy = SPUtils.getInstance().getInt(SPKey.BT_MATCH_LIST_ORDERBY, 1);
        mOddType = SPUtils.getInstance().getInt(SPKey.BT_MATCH_LIST_ODDTYPE, 1);
        viewModel.statistical(playMethodType);
        viewModel.getUserBalance();

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
        SPUtils.getInstance(BET_EXPAND).clear();
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
            binding.tabSportType.removeAllTabs();
            sportTypePos = 0;
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
            viewModel.setSportIds(playMethodPos);
            if (playMethodPos == 0 || playMethodPos == 1 || playMethodPos == 3) {
                binding.tabSportType.selectTab(binding.tabSportType.getTabAt(1));
            } else {
                binding.tabSportType.selectTab(binding.tabSportType.getTabAt(0));
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
            updateData();
        });

        viewModel.leagueWaitingTimerListData.observe(this, leagueAdapters -> {
            this.mLeagueList = leagueAdapters;
            updateData();
        });

        viewModel.championMatchTimerListData.observe(this, championMatcheList -> {
            mChampionMatchList = championMatcheList;
            updateData();
        });

        viewModel.championMatchListData.observe(this, championMatcheList -> {
            mChampionMatchList = championMatcheList;
            updateData();
        });

        viewModel.betContractListData.observe(this, betContract -> {
            if (betContract.action == BetContract.ACTION_EXPAND) {
                handleExpandContract(betContract);
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
                if (playMethodPos == 3 && mLeagueAdapter != null) {
                    mLeagueAdapter.notifyDataSetChanged();
                }
            } else if (betContract.action == BetContract.ACTION_SORT_CHANGE) {
                mOrderBy = (int) betContract.getData();
                getMatchData(viewModel.getSportId(playMethodType)[sportTypePos], mOrderBy, mLeagueIdList, null,
                        playMethodType, searchDatePos, false, true);
            } else if (betContract.action == BetContract.ACTION_MARKET_CHANGE) {
                mOddType = (int) betContract.getData();
                updateData();
            } else if (betContract.action == BetContract.ACTION_CHECK_SEARCH_BY_LEAGUE) {
                mLeagueIdList = (List<Long>) betContract.getData();
                getMatchData(viewModel.getSportId(playMethodType)[sportTypePos], mOrderBy, mLeagueIdList, null,
                        playMethodType, searchDatePos, false, true);
            }

        });
        viewModel.statisticalData.observe(this, statisticalData -> {
            this.mStatisticalData = statisticalData;
            updateStatisticalData();
        });

        viewModel.getUC().getSmartRefreshListenerEvent().observe(this, integer -> {
            if (integer == BaseViewModel.ONFINISH_LOAD_MORE) {
                binding.srlLeague.finishLoadMore();
            } else if (integer == BaseViewModel.ONFINISH_REFRESH) {
                binding.srlLeague.finishRefresh();
            } else if (integer == BaseViewModel.ON_LOAD_MORE_WITH_NO_MORE_DATA) {
                binding.srlLeague.finishLoadMoreWithNoMoreData();
                binding.srlLeague.finishRefreshWithNoMoreData();
            } else if (integer == BaseViewModel.ONFINISH_REFRESH_FAILED) {
                binding.srlLeague.finishRefresh(false);
            } else if (integer == BaseViewModel.ONFINISH_LOAD_MORE_FAILED) {
                binding.srlLeague.finishLoadMore(false);
            }
        });

        viewModel.settingLeagueData.observe(this, leagueList -> {
            settingLeagueList = leagueList;
        });

        viewModel.userBalanceData.observe(this, balance -> {
            binding.tvBalance.setText(balance);
        });

        viewModel.hotMatchCountData.observe(this, hotMatchCount -> {
            if (playMethodPos == 0 || playMethodPos == 3) {
                mHotMatchCount = hotMatchCount;
                if (playMethodPos == 0 || playMethodPos == 3) {
                    View view = binding.tabSportType.getTabAt(0).getCustomView();
                    TextView tvHotCount = view.findViewById(R.id.iv_match_count);
                    tvHotCount.setText(String.valueOf(mHotMatchCount));
                }
            }
        });
    }

    /**
     * 展开或收起全部进行中的联赛
     */
    private void goingOnExpandOrCollapseGroup() {
        binding.rvLeague.postDelayed(() -> {
            if (mLeagueList.isEmpty()) {
                return;
            }
            String data = (String) mLeagueAdapter.expandRangeLive();
            int start = Integer.valueOf(data.split("/")[0]);
            int end = Integer.valueOf(data.split("/")[1]);
            for (int i = start; i < end; i++) {
                League league = mLeagueList.get(i);
                if (isGoingOnAllExpand()) {
                    binding.rvLeague.expandGroup(i);
                    /*league.setExpand(true);*/
                } else {
                    binding.rvLeague.collapseGroup(i);
                    /*league.setExpand(false);*/
                }
            }
        }, 100);

    }

    /**
     * 展开或收起全部未开赛的联赛
     */
    private void waitingExpandOrCollapseGroup() {

        binding.rvLeague.postDelayed(() -> {
            String data = (String) mLeagueAdapter.expandRangeNoLive();
            int start = Integer.valueOf(data.split("/")[0]);
            int end = Integer.valueOf(data.split("/")[1]);
            for (int i = start; i < end; i++) {
                League league = mLeagueList.get(i);
                if (isWaitingAllExpand()) {
                    binding.rvLeague.expandGroup(i);
                    /*league.setExpand(true);*/
                } else {
                    binding.rvLeague.collapseGroup(i);
                    /*league.setExpand(false);*/
                }
            }
        }, 100);
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
            mIsChange = false;
            mLeagueAdapter = new LeagueAdapter(MainActivity.this, mLeagueList);
            initLeagueListView();
            binding.rvLeague.setAdapter(mLeagueAdapter);
            binding.rvLeague.postDelayed(() -> {
                goingOnExpandOrCollapseGroup();
                waitingExpandOrCollapseGroup();
                mLastLeagueSize = mLeagueList.size();
            }, 150);
            mLeagueAdapter.setOnScrollListener(new PageHorizontalScrollView.OnScrollListener() {
                @Override
                public void onScrolled() {
                    binding.rvLeague.setEnabled(true);
                }

                @Override
                public void onScrolling() {
                    binding.rvLeague.setEnabled(false);
                }
            });
        } else {
            if (!(binding.rvLeague.getExpandableListAdapter() instanceof LeagueAdapter)) {
                binding.rvLeague.setAdapter(mLeagueAdapter);
            }
            mLeagueAdapter.setData(mLeagueList);
            binding.rvLeague.postDelayed(() -> {
                if (mIsChange) {
                    setGoingOnAllExpand(true);
                    setWaitingAllExpand(true);
                    goingOnExpandOrCollapseGroup();
                    waitingExpandOrCollapseGroup();
                    mIsChange = false;
                } else {
                    if (mLeagueList.size() > mLastLeagueSize) {
                        for (int i = mLastLeagueSize; i < mLeagueList.size(); i++) {
                            binding.rvLeague.expandGroup(i);
                        }
                    }
                }
                mLastLeagueSize = mLeagueList.size();
            }, 150);
        }

        mIsShowLoading = true;
    }

    /**
     * 更新冠军赛事
     */
    private void updateChampionMatchData() {
        binding.rvLeague.postDelayed(() -> {
            if (mChampionMatchAdapter == null) {
                mChampionMatchAdapter = new ChampionMatchAdapter(MainActivity.this, mChampionMatchList);
                binding.rvLeague.setAdapter(mChampionMatchAdapter);
            } else {
                if (!(binding.rvLeague.getExpandableListAdapter() instanceof ChampionMatchAdapter)) {
                    binding.rvLeague.setAdapter(mChampionMatchAdapter);
                }
                mChampionMatchAdapter.setData(mChampionMatchList);
            }
        }, 10);

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

    private void updateStatisticalData() {
        int allCount = 0;
        TextView tvAllCount = null;
        if (playMethodPos == 1) {
            View view = binding.tabSportType.getTabAt(0).getCustomView();
            tvAllCount = view.findViewById(R.id.iv_match_count);
        }
        if (mStatisticalData != null) {
            int tabCount = binding.tabSportType.getTabCount();
            for (int i = 0; i < tabCount; i++) {
                View view = binding.tabSportType.getTabAt(i).getCustomView();
                TextView tvCount = view.findViewById(R.id.iv_match_count);
                Integer count = mStatisticalData.get(String.valueOf(playMethodType)).get(i);
                if (count != null) {
                    tvCount.setText(String.valueOf(count));
                } else {
                    count = 0;
                }
                if (playMethodPos == 1) {
                    allCount += count;
                }
            }
        }
        if (tvAllCount != null) {
            tvAllCount.setText(String.valueOf(allCount));
        }
    }

    private boolean isGoingOnAllExpand() {
        boolean isGoingOnAllExpand = SPUtils.getInstance(BET_EXPAND).getBoolean(SPKey.BT_GOINGON_SPORT_TYPE_EXPAND + mPlatformName + playMethodType + getSportId(), true);
        return isGoingOnAllExpand;
    }

    private void setGoingOnAllExpand(boolean isGoingExpand) {
        SPUtils.getInstance(BET_EXPAND).put(SPKey.BT_GOINGON_SPORT_TYPE_EXPAND + mPlatformName + playMethodType + getSportId(), isGoingExpand);
        mLeagueAdapter.setHeaderIsExpand(mLeagueAdapter.getLiveHeaderPosition(), isGoingExpand);
    }

    private boolean isWaitingAllExpand() {
        boolean isWaitingAllExpand = SPUtils.getInstance(BET_EXPAND).getBoolean(SPKey.BT_WAITING_SPORT_TYPE_EXPAND + mPlatformName + playMethodType + getSportId(), true);
        return isWaitingAllExpand;
    }

    private void setWaitingAllExpand(boolean isWatingExpand) {
        SPUtils.getInstance(BET_EXPAND).put(SPKey.BT_WAITING_SPORT_TYPE_EXPAND + mPlatformName + playMethodType + getSportId(), isWatingExpand);
        mLeagueAdapter.setHeaderIsExpand(mLeagueAdapter.getNoLiveHeaderPosition(), isWatingExpand);
    }

    private boolean isChampionAllExpand() {
        boolean isChampionAllExpand = SPUtils.getInstance(BET_EXPAND).getBoolean(SPKey.BT_CHAMPION_SPORT_TYPE_EXPAND + mPlatformName + playMethodType + getSportId(), false);
        return isChampionAllExpand;
    }

    private void setChampionAllExpand(boolean isChampionExpand) {
        SPUtils.getInstance(BET_EXPAND).put(SPKey.BT_CHAMPION_SPORT_TYPE_EXPAND + mPlatformName + playMethodType + getSportId(), isChampionExpand);
    }

    /**
     * 进行中是否有展开的的联赛
     *
     * @return
     */
    private boolean isGoingOnHasExand() {
        if (mLeagueList.isEmpty()) {
            return false;
        }
        String data = (String) mLeagueAdapter.expandRangeLive();
        int start = Integer.valueOf(data.split("/")[0]);
        int end = Integer.valueOf(data.split("/")[1]);
        for (int i = start; i < end; i++) {
            League league = mLeagueList.get(i);
            if (league.isExpand()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 未开赛是否有展开的的联赛
     *
     * @return
     */
    private boolean isWaitingHasExpand() {
        if (mLeagueList.isEmpty()) {
            return false;
        }
        String data = (String) mLeagueAdapter.expandRangeNoLive();
        int start = Integer.valueOf(data.split("/")[0]);
        int end = Integer.valueOf(data.split("/")[1]);
        for (int i = start; i < end; i++) {
            League league = mLeagueList.get(i);
            if (league.isExpand()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 进行中是否有展开的的联赛
     *
     * @return
     */
    private boolean isChampionHasExand() {
        if (mChampionMatchList.isEmpty()) {
            return false;
        }
        for (int i = 1; i < mChampionMatchList.size(); i++) {
            Match match = mChampionMatchList.get(i);
            if (match.isExpand()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.rl_cg) {
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
        mIsShowLoading = false;
        getMatchData(viewModel.getSportId(playMethodType)[sportTypePos], mOrderBy, mLeagueIdList, null,
                playMethodType, searchDatePos, false, true);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        mIsShowLoading = false;
        getMatchData(viewModel.getSportId(playMethodType)[sportTypePos], mOrderBy, mLeagueIdList, null,
                playMethodType, searchDatePos, false, false);
    }
}
