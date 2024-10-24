package com.xtree.bet.ui.activity;

import static com.xtree.base.utils.BtDomainUtil.KEY_PLATFORM;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.gyf.immersionbar.ImmersionBar;
import com.xtree.base.utils.TimeUtils;
import com.xtree.bet.BR;
import com.xtree.bet.R;
import com.xtree.bet.bean.ui.Match;
import com.xtree.bet.bean.ui.MatchPm;
import com.xtree.bet.bean.ui.PlayType;
import com.xtree.bet.constant.Constants;
import com.xtree.bet.contract.BetContract;
import com.xtree.bet.databinding.BtLayoutDetailResultBinding;
import com.xtree.bet.ui.fragment.BtDetailOptionFragment;
import com.xtree.bet.ui.viewmodel.factory.PMAppViewModelFactory;
import com.xtree.bet.ui.viewmodel.pm.PmBtDetailViewModel;
import com.xtree.bet.util.MatchDeserializer;
import com.xtree.bet.weight.BaseDetailDataView;

import java.util.ArrayList;
import java.util.List;

import me.xtree.mvvmhabit.base.BaseActivity;
import me.xtree.mvvmhabit.bus.RxBus;
import me.xtree.mvvmhabit.utils.KLog;
import me.xtree.mvvmhabit.utils.SPUtils;

/**
 * 投注赛果详情
 */
public class BtDetailResultActivity extends BaseActivity<BtLayoutDetailResultBinding, PmBtDetailViewModel> implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {
    private final static String KEY_MATCH = "KEY_MATCH_ID";
    private List<PlayType> mCategories = new ArrayList<>();

    private BaseDetailDataView mScoreDataView;

    private BtDetailOptionFragment fragment;

    private MatchPm mMatch;

    private int tabPos;

    private String mPlatform = SPUtils.getInstance().getString(KEY_PLATFORM);

    public Match getmMatch() {
        return mMatch;
    }

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.bt_layout_detail_result;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    /**
     * 初始化沉浸式
     * Init immersion bar.
     */
    protected void initImmersionBar() {
        //设置共同沉浸式样式
        ImmersionBar.with(this)
                .navigationBarColor(me.xtree.mvvmhabit.R.color.default_navigation_bar_color)
                .fitsSystemWindows(false)
                .statusBarDarkFont(false)
                .init();
    }

    public static void start(Context context, Match match) {
        Intent intent = new Intent(context, BtDetailResultActivity.class);
        SPUtils.getInstance().put(KEY_MATCH, new Gson().toJson(match));
        //intent.putExtra(KEY_MATCH, match);
        context.startActivity(intent);
    }

    @Override
    public PmBtDetailViewModel initViewModel() {
        PMAppViewModelFactory factory = PMAppViewModelFactory.getInstance(getApplication());
        return new ViewModelProvider(this, factory).get(PmBtDetailViewModel.class);
    }

    @Override
    public void initView() {

        binding.appBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {// 收缩状态
                binding.rlToolbarTime.setVisibility(View.VISIBLE);
                binding.ctlToolbarLeague.setVisibility(View.GONE);
                binding.llData.setVisibility(View.GONE);
                binding.toolbar.setBackgroundResource(R.color.bt_color_detail_top_toolbar);
            } else if (Math.abs(verticalOffset) == 0) {//展开
                binding.rlToolbarTime.setVisibility(View.GONE);
                binding.ctlToolbarLeague.setVisibility(View.VISIBLE);
                binding.llData.setVisibility(View.VISIBLE);
                binding.toolbar.setBackgroundResource(android.R.color.transparent);
            } else {
                binding.llData.setVisibility(View.VISIBLE);
            }
        });

        binding.tabCategoryType.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tabPos = tab.getPosition();
                for (int i = 0; i < binding.tabCategoryType.getTabCount(); i++) {
                    if (tabPos == i) {
                        binding.tabCategoryType.getTabAt(i).getCustomView().setBackgroundResource(R.mipmap.bt_bg_category_tab_selected);
                    } else {
                        binding.tabCategoryType.getTabAt(i).getCustomView().setBackgroundResource(R.drawable.bt_bg_category_tab);
                    }
                }

                updateOptionData();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        binding.ivExpand.setOnClickListener(this);
        binding.ivBack.setOnClickListener(this);

    }

    @Override
    public void initData() {
        //mMatch = getIntent().getParcelableExtra(KEY_MATCH);
        Gson gson = new GsonBuilder().serializeNulls().registerTypeAdapter(MatchPm.class, new MatchDeserializer()).create();
        mMatch = gson.fromJson(SPUtils.getInstance().getString(KEY_MATCH), MatchPm.class);
        KLog.i(new Gson().toJson(mMatch));
        viewModel.getMatchDetailResult(mMatch.getId());
        viewModel.getCategoryListResult(String.valueOf(mMatch.getId()), mMatch.getSportId());
        viewModel.addSubscription();
    }

    /**
     * 设置顶部背景图
     */
    private void setTopBg() {
        KLog.i(mMatch);
        if (mMatch != null && mMatch.getSportId() != null) {
            binding.ctlBg.setBackgroundResource(Constants.getBgMatchDetailTop(mMatch.getSportId()));
        }
    }

    /**
     * 更新投注选项数据
     */
    private void updateOptionData() {
        if (fragment == null) {
            if (mCategories != null && !mCategories.isEmpty()) {
                fragment = BtDetailOptionFragment.getInstance(mMatch, (ArrayList<PlayType>) mCategories, true);
                FragmentTransaction trans = getSupportFragmentManager()
                        .beginTransaction();
                trans.replace(R.id.fl_option, fragment);
                trans.commitAllowingStateLoss();
            }
        } else {
            if (mCategories != null && !mCategories.isEmpty()) {
                if (tabPos < mCategories.size()) {
                    RxBus.getDefault().post(new BetContract(BetContract.ACTION_OPTION_CHANGE, mCategories));
                }
            }
        }
        /*if (detailPlayTypeAdapter == null) {
            detailPlayTypeAdapter = new MatchDetailAdapter(BtDetailActivity.this, mCategories.get(tabPos).getPlayTypeList());
            //binding.aelOption.setAdapter(detailPlayTypeAdapter);
        } else {
            detailPlayTypeAdapter.setData(mCategories.get(tabPos).getPlayTypeList());
        }*/
    }

    @Override
    public void initViewObservable() {
        viewModel.matchDataResult.observe(this, match -> {
            this.mMatch = match;
            if (match == null) {
                return;
            }
            setTopBg();

            binding.tvLeagueName.setText(match.getLeague().getLeagueName());
            binding.tvTeamMain.setText(match.getTeamMain());
            binding.tvTeamVisisor.setText(match.getTeamVistor());
            Glide.with(this)
                    .load(match.getIconMain())
                    //.apply(new RequestOptions().placeholder(placeholderRes))
                    .into(binding.ivLogoMain);

            Glide.with(this)
                    .load(match.getIconVisitor())
                    //.apply(new RequestOptions().placeholder(placeholderRes))
                    .into(binding.ivLogoVisitor);

            Glide.with(this)
                    .load(match.getIconMain())
                    //.apply(new RequestOptions().placeholder(placeholderRes))
                    .into(binding.ivLogoMainTop);

            Glide.with(this)
                    .load(match.getIconVisitor())
                    //.apply(new RequestOptions().placeholder(placeholderRes))
                    .into(binding.ivLogoVisitorTop);

            String score;
            List<Integer> scoreList = mMatch.getScore(Constants.getScoreType());

            if (scoreList != null && scoreList.size() > 1) {
                String scoreMain = String.valueOf(scoreList.get(0));
                String scoreVisitor = String.valueOf(scoreList.get(1));
                score = scoreMain + " - " + scoreVisitor;
                binding.tvScore.setText(score);
                binding.tvScoreMainTop.setText(scoreMain);
                binding.tvScoreVisitorTop.setText(scoreVisitor);
            }

            binding.tvTime.setText(TimeUtils.longFormatString(match.getMatchTime(), TimeUtils.FORMAT_MM_DD_HH_MM));
            binding.tvTimeTop.setText(TimeUtils.longFormatString(match.getMatchTime(), TimeUtils.FORMAT_MM_DD_HH_MM));

            if (binding.llData.getChildCount() == 0) {
                mScoreDataView = BaseDetailDataView.getInstance(this, match, false);
                if (mScoreDataView != null) {
                    binding.llData.addView(mScoreDataView);
                }
            } else {
                mScoreDataView.setMatch(match, false);
            }

        });
        viewModel.updateCagegoryListData.observe(this, categories -> {
            //mCategories = categories;
        });
        viewModel.categoryResultListData.observe(this, categories -> {
            if (categories.isEmpty()) {
                binding.rlPlayMethod.setVisibility(View.GONE);
                binding.flOption.setVisibility(View.GONE);
                binding.llEnd.llEmpty.setVisibility(View.VISIBLE);
            } else {
                binding.rlPlayMethod.setVisibility(View.VISIBLE);
                binding.flOption.setVisibility(View.VISIBLE);
                binding.llEnd.llEmpty.setVisibility(View.GONE);
            }
            //if (mCategories.size() != categories.size()) {
            mCategories = categories;
            KLog.i("mCategories   ", mCategories);
            //if (binding.tabCategoryType.getTabCount() == 0) {
            //    for (int i = 0; i < categories.size(); i++) {
            //        View view = LayoutInflater.from(this).inflate(R.layout.bt_layout_bet_catory_tab_item, null);
            //        TextView tvName = view.findViewById(R.id.tab_item_name);
            //        String name = categories.get(i) == null ? "" : categories.get(i).getName();
            //
            //        tvName.setText(name);
            //        ColorStateList colorStateList = getResources().getColorStateList(R.color.bt_color_category_tab_text);
            //        tvName.setTextColor(colorStateList);
            //
            //        binding.tabCategoryType.addTab(binding.tabCategoryType.newTab().setCustomView(view));
            //
            //    }
            //} else {
            //    for (int i = 0; i < categories.size(); i++) {
            //        try {
            //            if (binding.tabCategoryType == null) {
            //                CfLog.e("=========binding.tabCategoryType == null=========");
            //            }
            //            if (categories.get(i) == null && binding.tabCategoryType != null && i < binding.tabCategoryType.getTabCount()) {
            //                binding.tabCategoryType.removeTabAt(i);
            //                if (binding.tabCategoryType.getTabCount() == 0) {
            //                    binding.rlPlayMethod.setVisibility(View.GONE);
            //                    binding.flOption.setVisibility(View.GONE);
            //                    binding.llEnd.llEmpty.setVisibility(View.VISIBLE);
            //                }
            //            }
            //        } catch (Exception e) {
            //            CfLog.e("binding.tabCategoryType.getTabCount()-------" + binding.tabCategoryType.getTabCount() + "-----" + i);
            //            CfLog.e(e.getMessage());
            //        }
            //    }
            //    viewModel.updateCategoryData();
            //}
            updateOptionData();
            /*} else {
                mCategories = categories;
                updateOptionData();
            }*/
        });

    }

    @Override
    public void onRefresh() {
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.iv_back) {
            finish();
        } else if (id == R.id.iv_expand) {
            if (fragment != null) {
                fragment.expand();
            }
        }
    }
}
