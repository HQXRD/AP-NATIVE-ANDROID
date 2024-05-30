package com.xtree.bet.ui.activity;

import static com.xtree.base.utils.BtDomainUtil.KEY_PLATFORM;
import static com.xtree.base.utils.BtDomainUtil.KEY_PLATFORM_NAME;
import static com.xtree.base.utils.BtDomainUtil.PLATFORM_FB;
import static com.xtree.base.utils.BtDomainUtil.PLATFORM_FBXC;
import static com.xtree.base.utils.BtDomainUtil.PLATFORM_PM;

import android.animation.ObjectAnimator;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.gyf.immersionbar.ImmersionBar;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.router.RouterActivityPath;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.base.utils.BtDomainUtil;
import com.xtree.base.utils.CfLog;
import com.xtree.base.utils.ClickUtil;
import com.xtree.base.utils.DomainUtil;
import com.xtree.base.utils.TimeUtils;
import com.xtree.base.widget.MenuItemView;
import com.xtree.base.widget.MsgDialog;
import com.xtree.base.widget.TipDialog;
import com.xtree.bet.BR;
import com.xtree.bet.R;
import com.xtree.bet.bean.request.UploadExcetionReq;
import com.xtree.bet.bean.response.fb.HotLeague;
import com.xtree.bet.bean.ui.League;
import com.xtree.bet.bean.ui.Match;
import com.xtree.bet.constant.Constants;
import com.xtree.bet.constant.PMConstants;
import com.xtree.bet.constant.SPKey;
import com.xtree.bet.contract.BetContract;
import com.xtree.bet.databinding.FragmentMainBinding;
import com.xtree.bet.manager.BtCarManager;
import com.xtree.bet.ui.adapter.ChampionMatchAdapter;
import com.xtree.bet.ui.adapter.LeagueAdapter;
import com.xtree.bet.ui.fragment.BtCarDialogFragment;
import com.xtree.bet.ui.fragment.BtRecordDialogFragment;
import com.xtree.bet.ui.fragment.BtSettingDialogFragment;
import com.xtree.bet.ui.viewmodel.TemplateMainViewModel;
import com.xtree.bet.ui.viewmodel.factory.AppViewModelFactory;
import com.xtree.bet.ui.viewmodel.factory.PMAppViewModelFactory;
import com.xtree.bet.ui.viewmodel.fb.FBMainViewModel;
import com.xtree.bet.ui.viewmodel.pm.PMMainViewModel;
import com.xtree.bet.weight.AnimatedExpandableListViewMax;
import com.xtree.bet.weight.BettingNetFloatingWindows;
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
import me.xtree.mvvmhabit.bus.Messenger;
import me.xtree.mvvmhabit.utils.SPUtils;
import me.xtree.mvvmhabit.utils.ToastUtils;

/**
 * Created by goldze on 2018/6/21
 */
@Route(path = RouterActivityPath.Bet.PAGER_BET_HOME)
public class MainActivity extends BaseActivity<FragmentMainBinding, TemplateMainViewModel> implements OnRefreshLoadMoreListener, View.OnClickListener {

    public final static String BET_EXPAND = "betExpand";
    private boolean isFirstInto = true;
    private String mPlatform = PLATFORM_FBXC;
    private String mPlatformName;
    private boolean mIsShowLoading = true;
    private boolean mIsChange = true;
    private boolean mIsFirstNetworkFinished;
    private UploadExcetionReq mUploadExcetionReq;
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
    private Disposable firstNetworkFinishedDisposable;
    private Disposable firstNetworkExceptionDisposable;

    private int searchDatePos;

    /**
     * 玩法类型，默认为今日+滚球
     */
    private int playMethodType = 6;
    private int playMethodPos;
    private int sportTypePos = -1;
    private int hotLeaguePos;
    private int mOrderBy = 1;
    private int mOddType = 1;

    private boolean isFloating = false;

    private NavigationController navigationController;
    private MenuItemView refreshMenu;
    private View mHeader;
    private ImageView ivHeaderExpand;
    private TextView tvHeaderName;
    private ImageView ivHeaderName;
    private int mHotMatchCount;
    private String mSportName;
    private List<HotLeague> mLeagueItemList;
    private Bundle mSavedInstanceState;
    private BasePopupView changeAgentTipView;
    private BasePopupView changeAgentView;
    private BettingNetFloatingWindows mBettingNetFloatingWindows;

    private Handler mHandler = new Handler();
    private Runnable searchRunnable;

    public List<League> getSettingLeagueList() {
        return settingLeagueList;
    }

    public int getPlayMethodType() {
        return playMethodType;
    }

    public int getSportId() {
        String[] sportIds = viewModel.getSportId(playMethodType);
        String sportId = null;
        if (sportTypePos < sportIds.length) {
            sportId = sportIds[sportTypePos == -1 ? 0 : sportTypePos];
        }
        // 以下规则只用于PM体育
        if (playMethodPos != 4) { // 刚开始进入PM体育场馆时，会有sportId为空的情况
            if (sportId == null) { // 获取相应玩法中默认的球种
                if (playMethodPos == 2) {
                    sportTypePos = 0;
                    sportId = sportIds[0];
                } else {
                    sportTypePos = 1;
                    sportId = sportIds[1];
                }
                mSportName = viewModel.getSportName(playMethodType)[sportTypePos];
            }
        }
        if (sportId == null) {
            sportId = playMethodPos == 4 ? "0" : PMConstants.SPORT_IDS_DEFAULT[1];
        }
        return Integer.valueOf(sportId);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //initFirstNetworkFinishTimer();
        //initFirstNetworkExceptionTimer();
        mSavedInstanceState = savedInstanceState;
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
        initPlatFormName();
        SPUtils.getInstance().put(KEY_PLATFORM, mPlatform);
        SPUtils.getInstance().put(KEY_PLATFORM_NAME, mPlatformName);
        BtDomainUtil.initDomainUrl();
        initDomain();
    }

    /**
     * 初始化场馆名称
     */
    private void initPlatFormName() {
        if (TextUtils.equals(mPlatform, PLATFORM_FBXC)) {
            mPlatformName = getString(R.string.bt_platform_name_fbxc);
        } else if (TextUtils.equals(mPlatform, PLATFORM_FB)) {
            mPlatformName = getString(R.string.bt_platform_name_fb);
        } else {
            mPlatformName = getString(R.string.bt_platform_name_pm);
        }
    }

    /**
     * 初始化代理UI
     */
    private void initAgentUi(Map<String, String> mapSwitch) {
        boolean bGameSwitch = TextUtils.equals(mapSwitch.get(mPlatform), "1");
        SPUtils.getInstance().put(SPKeyGlobal.KEY_GAME_SWITCH + mPlatform, bGameSwitch);
        boolean isAgent = SPUtils.getInstance().getBoolean(SPKeyGlobal.KEY_USE_AGENT + mPlatform);

        if (!bGameSwitch) {
            if (!BtDomainUtil.isMutiLine()) {
                if (!isFloating) {
                    mBettingNetFloatingWindows.removeView();
                    isFloating = false;
                }
            } else {
                if (!isFloating) {
                    CfLog.i("bettingNetFloatingWindows.show");
                    mBettingNetFloatingWindows.show();
                    isFloating = true;
                }

            }
        } else {
            if (!isFloating) {
                CfLog.i("bettingNetFloatingWindows.show");
                mBettingNetFloatingWindows.show();
                isFloating = true;
            }
        }

        if (!bGameSwitch) {
            SPUtils.getInstance().put(SPKeyGlobal.KEY_USE_AGENT + mPlatform, false);
            if (isAgent) {
                SPUtils.getInstance().put(SPKeyGlobal.KEY_USE_LINE_POSITION + mPlatform, 0);
                initDomain();
                resetViewModel();
            }
        }
    }

    /**
     * 初始化场馆domain线路
     */
    private void initDomain() {
        boolean isAgent = SPUtils.getInstance().getBoolean(SPKeyGlobal.KEY_USE_AGENT + mPlatform);
        int useLinePosition = SPUtils.getInstance().getInt(SPKeyGlobal.KEY_USE_LINE_POSITION + mPlatform, 0);
        if (useLinePosition > BtDomainUtil.getDomainUrl().size() - 1) {
            useLinePosition = 0;
            SPUtils.getInstance().put(SPKeyGlobal.KEY_USE_LINE_POSITION + mPlatform, 0);
        }
        if (TextUtils.equals(mPlatform, PLATFORM_FBXC)) {
            if (isAgent) {
                SPUtils.getInstance().put(SPKeyGlobal.FBXC_API_SERVICE_URL, DomainUtil.getDomain());
            } else {
                SPUtils.getInstance().put(SPKeyGlobal.FBXC_API_SERVICE_URL, BtDomainUtil.getDomainUrl().get(useLinePosition));
            }
        } else if (TextUtils.equals(mPlatform, PLATFORM_FB)) {
            if (isAgent) {
                SPUtils.getInstance().put(SPKeyGlobal.FB_API_SERVICE_URL, DomainUtil.getDomain());
            } else {
                SPUtils.getInstance().put(SPKeyGlobal.FB_API_SERVICE_URL, BtDomainUtil.getDomainUrl().get(useLinePosition));
            }
        } else {
            if (isAgent) {
                SPUtils.getInstance().put(SPKeyGlobal.PM_API_SERVICE_URL, DomainUtil.getDomain());
            } else {
                SPUtils.getInstance().put(SPKeyGlobal.PM_API_SERVICE_URL, BtDomainUtil.getDomainUrl().get(useLinePosition));
            }
        }
    }

    @Override
    protected void initImmersionBar() {
        //设置共同沉浸式样式
        ImmersionBar.with(this)
                .navigationBarColor(me.xtree.mvvmhabit.R.color.default_navigation_bar_color)
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
        binding.tvBalance.setOnClickListener(this);
        binding.ivwGameSearch.setOnClickListener(this);
        binding.tvwCancel.setOnClickListener(this);

        mBettingNetFloatingWindows = BettingNetFloatingWindows.getInstance(this, (useAgent, isChangeDomain, checkBox) -> {
            checkBox.setChecked(useAgent);
            setDomain(useAgent);
            resetViewModel();
            setChangeDomainVisible();
            uploadException(useAgent, isChangeDomain);
            mBettingNetFloatingWindows.hideSecondaryLayout();
        });
        setChangeDomainVisible();

        binding.tabPlayMethod.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                ((TextView) tab.getCustomView()).setTextSize(16);
                if (playMethodPos != tab.getPosition()) {
                    hideSearchView();
                    mIsChange = true;
                    isFirstInto = true;
                    mLeagueIdList.clear();
                    binding.srlLeague.resetNoMoreData();
                    searchDatePos = 0;
                    playMethodType = playMethodTypeList.get(tab.getPosition());
                    if (playMethodPos == 4) {
                        BtCarManager.clearBtCar();
                        setCgBtCar();
                    }
                    showSearchDate();
                    playMethodPos = tab.getPosition();
                    if (tab.getPosition() == 4) {
                        binding.srlLeague.setEnableLoadMore(false);
                    } else {
                        binding.srlLeague.setEnableLoadMore(true);
                    }
                    BtCarManager.setIsCg(playMethodPos == 3);
                    binding.rlCg.setVisibility(!BtCarManager.isCg() ? View.GONE : BtCarManager.isEmpty() ? View.GONE : View.VISIBLE);
                    mLeagueGoingOnList.clear();
                    mLeagueList.clear();
                    viewModel.setSportIcons(playMethodPos);
                    viewModel.setSportItems(playMethodPos, playMethodType);

                    if (playMethodPos == 2 || playMethodPos == 3) {
                        binding.tabSearchDate.setVisibility(View.VISIBLE);
                    } else {
                        binding.tabSearchDate.setVisibility(View.GONE);
                    }
                    if (playMethodPos == 0 || playMethodPos == 3) {
                        viewModel.getHotMatchCount(playMethodType, viewModel.hotLeagueList);
                    }
                    /*viewModel.statistical(playMethodType);
                    initTimer();
                    if (playMethodPos == 2 || playMethodPos == 4) {
                        getMatchData(String.valueOf(getSportId()), mOrderBy, mLeagueIdList, null,
                                playMethodType, searchDatePos, false, true);
                    } else if (playMethodPos == 0 || playMethodPos == 3) {
                        viewModel.getHotMatchCount(playMethodType, viewModel.hotLeagueList);
                    }*/
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
                    hideSearchView();
                    binding.srlLeague.resetNoMoreData();
                    searchDatePos = 0;
                    mIsChange = true;
                    isFirstInto = true;
                    mLeagueIdList.clear();
                    showSearchDate();
                    sportTypePos = tab.getPosition();
                    mSportName = viewModel.getSportName(playMethodType)[sportTypePos];
                    viewModel.setHotLeagueList(mSportName);
                    mLeagueGoingOnList.clear();
                    mLeagueList.clear();
                    updateStatisticalData();
                    viewModel.statistical(playMethodType);
                    initTimer();
                    String sportId = String.valueOf(getSportId());
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
                if (searchDatePos != tab.getPosition()) {
                    hideSearchView();
                    mLeagueList.clear();
                    mLeagueGoingOnList.clear();
                    binding.srlLeague.resetNoMoreData();
                    mIsChange = true;
                    isFirstInto = true;
                    if (playMethodPos == 2 || playMethodPos == 3) {
                        searchDatePos = tab.getPosition();
                        viewModel.statistical(playMethodType);
                        initTimer();
                        getMatchData(String.valueOf(getSportId()), mOrderBy, mLeagueIdList, null,
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
        binding.tabFbLeague.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if (hotLeaguePos != tab.getPosition()) {
                    hideSearchView();
                    hotLeaguePos = tab.getPosition();
                    mLeagueList.clear();
                    mLeagueGoingOnList.clear();
                    binding.srlLeague.resetNoMoreData();
                    mIsChange = true;
                    isFirstInto = true;
                    mLeagueIdList.clear();
                    mLeagueIdList.addAll(mLeagueItemList.get(hotLeaguePos).leagueid);
                    viewModel.statistical(playMethodType);
                    initTimer();
                    getMatchData(String.valueOf(getSportId()), mOrderBy, mLeagueIdList, null,
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
        binding.rvLeague.setOnGroupClickListener((parent, v, groupPosition, id) -> {
            if (binding.rvLeague.isGroupExpanded(groupPosition)) {
                binding.rvLeague.collapseGroup(groupPosition);
            } else {
                binding.rvLeague.expandGroup(groupPosition);
            }
            binding.rvLeague.postDelayed(() -> {
                if (playMethodPos == 4) {
                    checkChampionHeaderIsExpand();
                } else {
                    checkLeagueHeaderIsExpand(groupPosition);
                }
            }, 150);

            return true;
        });
        binding.rlCg.setOnClickListener(this);
        initBottomTab();

        binding.edtGameSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mHandler.removeCallbacks(searchRunnable);
            }

            @Override
            public void afterTextChanged(Editable s) {
                // 在文本改变后调用，设置一个新的 Runnable

                searchRunnable = new Runnable() {
                    @Override
                    public void run() {
                        if (!isFirstInto) {
                            String search = binding.edtGameSearch.getText().toString();
                            viewModel.searchMatch(search, playMethodPos == 4);
                        }
                        isFirstInto = false;
                    }
                };

                // 延迟 1 秒后执行 Runnable
                mHandler.postDelayed(searchRunnable, 1000);
            }
        });
    }

    /**
     * 是否使用代理或其他非默认线路
     */
    private void setChangeDomainVisible() {
        boolean isAgent = SPUtils.getInstance().getBoolean(SPKeyGlobal.KEY_USE_AGENT + mPlatform);
        mBettingNetFloatingWindows.setIsSelected(isAgent);
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
        ivHeaderName = mHeader.findViewById(R.id.iv_header);
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
        if (firstGroup >= 0 && mChampionMatchList.get(firstGroup).isHead()) {
            ivHeaderExpand.setSelected(isChampionAllExpand());
            tvHeaderName.setText(getResources().getString(R.string.bt_all_league));
            ivHeaderName.setBackgroundResource(R.mipmap.bt_icon_all_league);
        }
    }

    private void onScrollLeague(int firstVisibleItem) {
        if (mHeader == null || mLeagueList == null || mLeagueList.isEmpty() || firstVisibleItem < 0) {
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

        if (firstGroup != 0 && nextGroup > -1 && mLeagueList.size() > nextGroup &&
                mLeagueList.get(nextGroup).isHead() && mLeagueList.get(nextGroup).getHeadType() == League.HEAD_TYPE_LIVE_OR_NOLIVE) {
            if (top < measuredHeight) {
                int dy = measuredHeight - top;
                mHeader.layout(0, -dy, measuredWidth, measuredHeight - dy);
            } else {
                mHeader.layout(0, 0, measuredWidth, measuredHeight);
            }
        } else {
            mHeader.layout(0, 0, measuredWidth, measuredHeight);
        }
        League league = mLeagueList.get(firstGroup);

        if (league.isHead() && league.getHeadType() == League.HEAD_TYPE_LIVE_OR_NOLIVE) {
            ivHeaderExpand.setSelected(isWaitingAllExpand());
            tvHeaderName.setText(league.getLeagueName());
            if (league.getLeagueName().equals(getResources().getString(R.string.bt_game_going_on))) {
                ivHeaderName.setBackgroundResource(R.mipmap.bt_icon_going_on);
            } else if (league.getLeagueName().equals(getResources().getString(R.string.bt_game_waiting))) {
                ivHeaderName.setBackgroundResource(R.mipmap.bt_icon_waiting);
            } else if (league.getLeagueName().equals(getResources().getString(R.string.bt_all_league))) {
                ivHeaderName.setBackgroundResource(R.mipmap.bt_icon_all_league);
            }
        }
        if (mLeagueAdapter.getNoLiveHeaderPosition() > firstGroup) {
            ivHeaderExpand.setSelected(isGoingOnAllExpand());
            tvHeaderName.setText(getResources().getString(R.string.bt_game_going_on));
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
        } else if (index == 0) {
            startContainerFragment(RouterFragmentPath.Bet.PAGER_BET_TUTORIAL);
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

    /**
     * 定时刷新赛事列表
     */
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

    /**
     * 监听第一次进入主页时获取列表数据是否完成，如果未完成，弹出切换线路提示弹窗
     */
    private void initFirstNetworkFinishTimer() {
        if (BtDomainUtil.isMutiLine()) {
            firstNetworkFinishedDisposable = Observable.interval(5, 5, TimeUnit.SECONDS)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(aLong -> {
                        if (!mIsFirstNetworkFinished) {
                            showChangeDomainTip();
                        }
                        if (firstNetworkExceptionDisposable != null) {
                            viewModel.removeSubscribe(firstNetworkExceptionDisposable);
                        }
                        viewModel.removeSubscribe(firstNetworkFinishedDisposable);
                        firstNetworkFinishedDisposable = null;
                    });
            viewModel.addSubscribe(firstNetworkFinishedDisposable);
        }
    }

    /**
     * 监听第一次进入主页时获取列表数据是否发生异常，如果发生异常，则上报服务器
     */
    private void initFirstNetworkExceptionTimer() {
        if (BtDomainUtil.isMutiLine()) {
            firstNetworkExceptionDisposable = Observable.interval(1, 2, TimeUnit.SECONDS)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(aLong -> {
                        if (mUploadExcetionReq != null) {
                            showChangeDomainTip();
                            if (firstNetworkFinishedDisposable != null) {
                                viewModel.removeSubscribe(firstNetworkFinishedDisposable);
                            }
                            viewModel.uploadException(mUploadExcetionReq);
                            mUploadExcetionReq = null;
                            firstNetworkExceptionDisposable = null;
                        }
                        viewModel.removeSubscribe(firstNetworkExceptionDisposable);
                    });
            viewModel.addSubscribe(firstNetworkExceptionDisposable);
        }
    }

    /**
     * 显示切换线路弹窗
     */
    private void showChangeDomainTip() {
        boolean bGameSwitch = SPUtils.getInstance().getBoolean(SPKeyGlobal.KEY_GAME_SWITCH + mPlatform);
        boolean isAgent = SPUtils.getInstance().getBoolean(SPKeyGlobal.KEY_USE_AGENT + mPlatform);
        if ((bGameSwitch && isAgent) || (changeAgentTipView != null && changeAgentTipView.isShow())) {
            return;
        }

        final String title = getString(R.string.txt_kind_tips);
        String showMessage = "当前您的网络环境较差，如继续游戏将无法保证游戏体验，是否需要继续等待？";
        changeAgentTipView = new XPopup.Builder(this).asCustom(new MsgDialog(this, title, "", showMessage, "继续等待", "切换线路", false, new TipDialog.ICallBack() {
            @Override
            public void onClickLeft() {
                changeAgentTipView.dismiss();
            }

            @Override
            public void onClickRight() {
                //showChangeDomainDialog();
                changeAgentTipView.dismiss();
            }
        }));
        changeAgentTipView.show();
    }

    /**
     * 上传切换线路或开关代理操作日志
     *
     * @param useAgent
     * @param isChangeDomain
     */
    private void uploadException(boolean useAgent, boolean isChangeDomain) {
        int useLinePosition = SPUtils.getInstance().getInt(SPKeyGlobal.KEY_USE_LINE_POSITION + mPlatform, 0);
        UploadExcetionReq uploadExcetionReq = new UploadExcetionReq();
        String platform = SPUtils.getInstance().getString(KEY_PLATFORM);
        String domainUrl;
        if (TextUtils.equals(platform, PLATFORM_FBXC)) {
            domainUrl = SPUtils.getInstance().getString(SPKeyGlobal.FBXC_API_SERVICE_URL);
            uploadExcetionReq.setLogTag("fbxc_url_" + (isChangeDomain ? "swithapiaddress" : "swithdelegate"));
        } else if (TextUtils.equals(platform, PLATFORM_FB)) {
            domainUrl = SPUtils.getInstance().getString(SPKeyGlobal.FB_API_SERVICE_URL);
            uploadExcetionReq.setLogTag("fb_url_" + (isChangeDomain ? "swithapiaddress" : "swithdelegate"));
        } else {
            domainUrl = SPUtils.getInstance().getString(SPKeyGlobal.PM_API_SERVICE_URL);
            uploadExcetionReq.setLogTag("pm_url_" + (isChangeDomain ? "swithapiaddress" : "swithdelegate"));
        }
        uploadExcetionReq.setApiUrl(domainUrl);
        uploadExcetionReq.setLogType("-");
        uploadExcetionReq.setMsg(isChangeDomain ? "切换线路" + (useLinePosition + 1) : useAgent ? "开启默认代理服务器" : "关闭默认代理服务器");
        CfLog.e("==============" + uploadExcetionReq.getMsg());
        viewModel.uploadException(uploadExcetionReq);
    }

    /**
     * 更换线路后重置viewmodel
     */
    private void resetViewModel() {
        if (!TextUtils.equals(mPlatform, PLATFORM_PM)) {
            AppViewModelFactory.init();
        } else {
            PMAppViewModelFactory.init();
        }
        //解除Messenger注册
        Messenger.getDefault().unregister(viewModel);
        if (viewModel != null) {
            viewModel.removeRxBus();
        }
        if (binding != null) {
            binding.unbind();
        }
        viewModel = null;
        initViewDataBinding(mSavedInstanceState);
        if (!TextUtils.equals(mPlatform, PLATFORM_PM)) {
            viewModel.setModel(AppViewModelFactory.getInstance(getApplication()).getRepository());
        } else {
            viewModel.setModel(PMAppViewModelFactory.getInstance(getApplication()).getRepository());
        }
        initView();
        //页面数据初始化方法
        initData();
        //页面事件监听的方法，一般用于ViewModel层转到View层的事件注册
        initViewObservable();
        //注册RxBus
        viewModel.registerRxBus();
    }

    /**
     * 设置当前场馆所用的domain线路
     *
     * @param isChecked
     */
    private void setDomain(boolean isChecked) {
        SPUtils.getInstance().put(SPKeyGlobal.KEY_USE_AGENT + mPlatform, isChecked);
        if (!TextUtils.equals(mPlatform, PLATFORM_PM)) {
            // FB体育使用线路位置
            int useLinePotion = SPUtils.getInstance().getInt(SPKeyGlobal.KEY_USE_LINE_POSITION + mPlatform, 0);

            if (isChecked) {
                if (TextUtils.equals(mPlatform, PLATFORM_FBXC)) {
                    SPUtils.getInstance().put(SPKeyGlobal.FBXC_API_SERVICE_URL, DomainUtil.getDomain());
                } else {
                    SPUtils.getInstance().put(SPKeyGlobal.FB_API_SERVICE_URL, DomainUtil.getDomain());
                }
            } else {
                if (TextUtils.equals(mPlatform, PLATFORM_FBXC)) {
                    SPUtils.getInstance().put(SPKeyGlobal.FBXC_API_SERVICE_URL, BtDomainUtil.getFbxcDomainUrl().get(useLinePotion));
                } else {
                    SPUtils.getInstance().put(SPKeyGlobal.FB_API_SERVICE_URL, BtDomainUtil.getFbDomainUrl().get(useLinePotion));
                }
            }
        } else {
            if (isChecked) {
                SPUtils.getInstance().put(SPKeyGlobal.PM_API_SERVICE_URL, DomainUtil.getDomain());
            } else {
                SPUtils.getInstance().put(SPKeyGlobal.PM_API_SERVICE_URL, BtDomainUtil.getDefaultPmDomainUrl());
            }
        }
    }

    /**
     * 刷新赛事列表
     */
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
                if (!match.isHead() && match.isExpand()) {
                    matchIdList.add(match.getId());
                }
            }
        }
        if (!matchIdList.isEmpty()) {
            getMatchData(String.valueOf(getSportId()), mOrderBy, mLeagueIdList, matchIdList,
                    playMethodType, searchDatePos, true, false);
        }
    }

    @Override
    public void initData() {
        playMethodType = Integer.valueOf(viewModel.getPlayMethodTypes()[0]);
        viewModel.getHotLeague(mPlatform);
        viewModel.setSportItems(playMethodPos, playMethodType);
        viewModel.setPlayMethodTabData();
        viewModel.addSubscription();
        mOrderBy = SPUtils.getInstance().getInt(SPKey.BT_MATCH_LIST_ORDERBY, 1);
        mOddType = SPUtils.getInstance().getInt(SPKey.BT_MATCH_LIST_ODDTYPE, 1);
        viewModel.statistical(playMethodType);
        viewModel.getUserBalance();
        viewModel.getGameSwitch();
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
        mBettingNetFloatingWindows.clearInstance();
    }

    List<Integer> playMethodTypeList = new ArrayList<>();

    @Override
    public void initViewObservable() {
        viewModel.reNewViewModel.observe(this, unused -> {
            if (!TextUtils.equals(mPlatform, PLATFORM_PM)) {
                AppViewModelFactory.init();
            } else {
                PMAppViewModelFactory.init();
            }
            //解除Messenger注册
            Messenger.getDefault().unregister(viewModel);
            if (viewModel != null) {
                viewModel.removeRxBus();
            }
            if (binding != null) {
                binding.unbind();
            }
            viewModel = null;
            initViewDataBinding(mSavedInstanceState);
            if (!TextUtils.equals(mPlatform, PLATFORM_PM)) {
                viewModel.setModel(AppViewModelFactory.getInstance(getApplication()).getRepository());
            } else {
                viewModel.setModel(PMAppViewModelFactory.getInstance(getApplication()).getRepository());
            }
        });
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
            sportTypePos = -1;
            for (int i = 0; i < matchitemList.length; i++) {
                View view = LayoutInflater.from(this).inflate(R.layout.bt_layout_bet_match_item_tab_item, null);
                TextView tvName = view.findViewById(R.id.tab_item_name);
                TextView tvMatchCount = view.findViewById(R.id.iv_match_count);
                ImageView ivIcon = view.findViewById(R.id.iv_icon);
                if (i < Constants.SPORT_ICON.length) {
                    ivIcon.setBackgroundResource(Constants.SPORT_ICON[i]);
                }
                tvName.setText(matchitemList[i]);
                tvMatchCount.setText(String.valueOf(0));
                ColorStateList colorStateList = getResources().getColorStateList(R.color.bt_color_bet_match_item_text);
                tvName.setTextColor(colorStateList);
                tvMatchCount.setTextColor(colorStateList);

                binding.tabSportType.addTab(binding.tabSportType.newTab().setCustomView(view), false);
            }
            String[] sportNameArray = viewModel.getSportName(playMethodType);

            if (!TextUtils.isEmpty(mSportName)) {
                int index = 0;
                if (playMethodPos != 4) {
                    index = 1;
                }
                for (int i = 0; i < sportNameArray.length; i++) {
                    if (TextUtils.equals(mSportName, sportNameArray[i])) {
                        index = i;
                        break;
                    }
                }

                final int fIndex = index;
                binding.tabSportType.post(() -> binding.tabSportType.getTabAt(fIndex).select());

            } else {
                binding.tabSportType.getTabAt(1).select();
            }

        });
        viewModel.leagueItemData.observe(this, leagueItemList -> {
            mLeagueItemList = leagueItemList;
            if (leagueItemList == null) {
                binding.tabFbLeague.setVisibility(View.GONE);
            } else {
                binding.tabFbLeague.setVisibility(View.VISIBLE);
                binding.tabFbLeague.removeAllTabs();
                for (int i = 0; i < leagueItemList.size(); i++) {
                    View view = LayoutInflater.from(this).inflate(R.layout.bt_layout_bet_league_tab_item, null);
                    TextView tvName = view.findViewById(R.id.tab_item_name);
                    ImageView ivIcon = view.findViewById(R.id.iv_icon);
                    String name = leagueItemList.get(i).name;
                    if (TextUtils.equals(name, "全部")) {
                        ivIcon.setVisibility(View.GONE);
                    }
                    ivIcon.setBackgroundResource(Constants.getHotLeagueIcon(leagueItemList.get(i).code));

                    tvName.setText(name);
                    ColorStateList colorStateList = getResources().getColorStateList(R.color.bt_color_bet_top_tab_item_text);
                    tvName.setTextColor(colorStateList);

                    binding.tabFbLeague.addTab(binding.tabFbLeague.newTab().setCustomView(view));
                }
            }
        });

        viewModel.leagueLiveListData.observe(this, leagueList -> {
            if (playMethodType == 1) {
                this.mLeagueList = leagueList;
                updateData();
            }
        });

        viewModel.leagueLiveTimerListData.observe(this, leagueList -> {
            this.mLeagueList = leagueList;
            updateData();
        });

        viewModel.leagueNoLiveListData.observe(this, leagueAdapters -> {
            this.mLeagueList = leagueAdapters;
            updateData();
        });

        viewModel.leagueNoLiveTimerListData.observe(this, leagueAdapters -> {
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
                getMatchData(String.valueOf(getSportId()), mOrderBy, mLeagueIdList, null,
                        playMethodType, searchDatePos, false, true);
            } else if (betContract.action == BetContract.ACTION_MARKET_CHANGE) {
                mOddType = (int) betContract.getData();
                updateData();
            } else if (betContract.action == BetContract.ACTION_CHECK_SEARCH_BY_LEAGUE) {
                mLeagueIdList = (List<Long>) betContract.getData();
                getMatchData(String.valueOf(getSportId()), mOrderBy, mLeagueIdList, null,
                        playMethodType, searchDatePos, false, true);
            } else if (betContract.action == BetContract.ACTION_BT_SUCESSED) {
                viewModel.getUserBalance();
                setCgBtCar();
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
                    if (binding.tabSportType.getTabAt(0) != null) {
                        View view = binding.tabSportType.getTabAt(0).getCustomView();
                        TextView tvHotCount = view.findViewById(R.id.iv_match_count);
                        tvHotCount.setText(String.valueOf(mHotMatchCount));
                    }
                }
            }
        });

        viewModel.tokenInvalidEvent.observe(this, unused -> {
            String sportId = String.valueOf(getSportId());
            /*if (sportTypePos != -1) {
                if(playMethodPos == 2 || playMethodPos == 4){
                    sportTypePos = 0;
                }else{
                    sportTypePos = 1;
                }
            }*/
            getMatchData(sportId, mOrderBy, mLeagueIdList, null,
                    playMethodType, searchDatePos, false, true);
        });

        viewModel.tooManyRequestsEvent.observe(this, unused -> {
            /*getMatchData(String.valueOf(getSportId()), mOrderBy, mLeagueIdList, null,
                    playMethodType, searchDatePos, false, true);
            binding.tabSportType.setEnabled(false);
            binding.tabPlayMethod.setEnabled(false);
            binding.tabSearchDate.setEnabled(false);
            binding.rvLeague.postDelayed(() -> {

            }, 1000);*/
        });
        viewModel.firstNetworkFinishData.observe(this, unused -> {
            mIsFirstNetworkFinished = true;
        });
        viewModel.firstNetworkExceptionData.observe(this, uploadExcetionReq -> {
            //CfLog.e(viewModel.firstNetworkExceptionData + "=====");
            mUploadExcetionReq = uploadExcetionReq;
        });
        viewModel.agentSwitchData.observe(this, switchMap -> {
            initAgentUi(switchMap);
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
                if (isGoingOnAllExpand()) {
                    binding.rvLeague.expandGroup(i);
                } else {
                    binding.rvLeague.collapseGroup(i);
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
                if (isWaitingAllExpand()) {
                    binding.rvLeague.expandGroup(i);
                } else {
                    binding.rvLeague.collapseGroup(i);
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
            mLeagueAdapter = new LeagueAdapter(MainActivity.this, mLeagueList);
            initLeagueListView();
            binding.rvLeague.setAdapter(mLeagueAdapter);
            setGoingOnAllExpand(true);
            setWaitingAllExpand(true);
            goingOnExpandOrCollapseGroup();
            waitingExpandOrCollapseGroup();
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
            if (mIsChange) {
                setGoingOnAllExpand(true);
                setWaitingAllExpand(true);
                goingOnExpandOrCollapseGroup();
                waitingExpandOrCollapseGroup();
                if (!mLeagueList.isEmpty()) {
                    mIsChange = false;
                }
            } else {

            }
        }
        if (mLeagueList.isEmpty()) {
            binding.nsvLeague.setVisibility(View.GONE);
            binding.llEmpty.llEmpty.setVisibility(View.VISIBLE);
        } else {
            binding.nsvLeague.setVisibility(View.VISIBLE);
            binding.llEmpty.llEmpty.setVisibility(View.GONE);
        }

        mIsShowLoading = true;
    }

    /**
     * 更新冠军赛事
     */
    private void updateChampionMatchData() {
        if (mChampionMatchAdapter == null) {
            mChampionMatchAdapter = new ChampionMatchAdapter(MainActivity.this, mChampionMatchList);
            binding.rvLeague.setAdapter(mChampionMatchAdapter);
        } else {
            if (!(binding.rvLeague.getExpandableListAdapter() instanceof ChampionMatchAdapter)) {
                binding.rvLeague.setAdapter(mChampionMatchAdapter);
            }
            mChampionMatchAdapter.setData(mChampionMatchList);
        }
        if (mChampionMatchList.isEmpty()) {
            binding.nsvLeague.setVisibility(View.GONE);
            binding.llEmpty.llEmpty.setVisibility(View.VISIBLE);
        } else {
            binding.nsvLeague.setVisibility(View.VISIBLE);
            binding.llEmpty.llEmpty.setVisibility(View.GONE);
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
                int size = mStatisticalData.get(String.valueOf(playMethodType)).size();
                if (i == size) {
                    break;
                }
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
            if (ClickUtil.isFastClick()) {
                return;
            }
            BtCarDialogFragment btCarDialogFragment = new BtCarDialogFragment();
            btCarDialogFragment.show(MainActivity.this.getSupportFragmentManager(), "btCarDialogFragment");
        } else if (id == R.id.iv_back) {
            finish();
        } else if (id == R.id.tv_balance) {
            Bundle bundle = new Bundle();
            bundle.putBoolean("isShowBack", true);
            startContainerFragment(RouterFragmentPath.Recharge.PAGER_RECHARGE, bundle);
        } else if (id == R.id.ivw_game_search) {
            binding.clGameSearch.setVisibility(View.VISIBLE);
            binding.clTab.setVisibility(View.GONE);
        } else if (id == R.id.tvw_cancel) {
            hideSearchView();
            viewModel.searchMatch(null, playMethodPos == 4);
        }
    }

    /**
     * 隐藏搜索UI
     */
    private void hideSearchView() {
        binding.clGameSearch.setVisibility(View.GONE);
        binding.clTab.setVisibility(View.VISIBLE);
        binding.edtGameSearch.setText("");
        viewModel.mSearchWord = null;
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        mIsShowLoading = false;
        getMatchData(String.valueOf(getSportId()), mOrderBy, mLeagueIdList, null,
                playMethodType, searchDatePos, false, true);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        mIsShowLoading = false;
        getMatchData(String.valueOf(getSportId()), mOrderBy, mLeagueIdList, null,
                playMethodType, searchDatePos, false, false);
    }
}
