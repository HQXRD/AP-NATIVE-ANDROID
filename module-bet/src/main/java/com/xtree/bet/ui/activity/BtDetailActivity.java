package com.xtree.bet.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.xtree.base.utils.CfLog;
import com.xtree.base.utils.TimeUtils;
import com.xtree.bet.BR;
import com.xtree.bet.R;
import com.xtree.bet.bean.ui.Category;
import com.xtree.bet.bean.ui.Match;
import com.xtree.bet.bean.ui.PlayType;
import com.xtree.bet.constant.Constants;
import com.xtree.bet.constant.SPKey;
import com.xtree.bet.constant.SportTypeContants;
import com.xtree.bet.contract.BetContract;
import com.xtree.bet.manager.BtCarManager;
import com.xtree.bet.ui.adapter.MatchDetailAdapter;
import com.xtree.bet.ui.fragment.BtCarDialogFragment;
import com.xtree.bet.ui.fragment.BtDetailOptionFragment;
import com.xtree.bet.ui.viewmodel.BtDetailViewModel;
import com.xtree.bet.ui.viewmodel.factory.AppViewModelFactory;
import com.xtree.bet.weight.BaseDetailDataView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.xtree.mvvmhabit.bus.RxBus;
import me.xtree.mvvmhabit.utils.SPUtils;
import me.xtree.mvvmhabit.utils.ToastUtils;

/**
 * Created by goldze on 2018/6/21
 */
public class BtDetailActivity extends GSYBaseActivityDetail<StandardGSYVideoPlayer> implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {
    private final static String KEY_MATCH = "KEY_MATCH_ID";
    private List<Category> mCategories = new ArrayList<>();

    private BaseDetailDataView fbDataView;

    private MatchDetailAdapter detailPlayTypeAdapter;

    private BtDetailOptionFragment fragment;

    private Match mMatch;

    private int tabPos;

    public Match getmMatch() {
        return mMatch;
    }

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.bt_layout_detail;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    public static void start(Context context, Match match) {
        Intent intent = new Intent(context, BtDetailActivity.class);
        intent.putExtra(KEY_MATCH, match);
        context.startActivity(intent);
    }

    @Override
    public BtDetailViewModel initViewModel() {
        //使用自定义的ViewModelFactory来创建ViewModel，如果不重写该方法，则默认会调用LoginViewModel(@NonNull Application application)构造方法
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getApplication());
        return new ViewModelProvider(this, factory).get(BtDetailViewModel.class);
    }

    @Override
    public void initView() {
        binding.appBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {// 收缩状态
                binding.rlToolbarTime.setVisibility(View.VISIBLE);
                binding.ctlToolbarLeague.setVisibility(View.GONE);
                binding.llLive.setVisibility(View.GONE);
                binding.llData.setVisibility(View.GONE);
                binding.toolbar.setBackgroundResource(R.color.bt_color_detail_top_toolbar);
            } else if (Math.abs(verticalOffset) == 0) {//展开
                binding.rlToolbarTime.setVisibility(View.GONE);
                if(binding.videoPlayer.getVisibility() != View.VISIBLE && binding.wvAmin.getVisibility() != View.VISIBLE) {
                    binding.ctlToolbarLeague.setVisibility(View.VISIBLE);
                }else {
                    binding.ctlToolbarLeague.setVisibility(View.GONE);
                }
                binding.llLive.setVisibility(View.VISIBLE);
                binding.llData.setVisibility(View.VISIBLE);
                binding.toolbar.setBackgroundResource(android.R.color.transparent);
            } else {
                binding.llLive.setVisibility(View.VISIBLE);
                binding.llData.setVisibility(View.VISIBLE);
            }
        });

        binding.tabCategoryType.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tabPos = tab.getPosition();
                updateOptionData();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        binding.rlCg.setOnClickListener(this);

        initVideoPlayer();
        setWebView();
        setTopBg();
    }

    @Override
    public void initData() {
        mMatch = getIntent().getParcelableExtra(KEY_MATCH);
        viewModel.getMatchDetail(mMatch.getId());
        viewModel.matchData.postValue(mMatch);
        viewModel.addSubscription();
        setCgBtCar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.addSubscribe(Observable.interval(5, 5, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    viewModel.getMatchDetail(mMatch.getId());
                })
        );
    }

    /**
     * 设置顶部背景图
     */
    private void setTopBg() {
        int sportId = SPUtils.getInstance().getInt(SPKey.BT_SPORT_ID);
        binding.ctlBg.setBackgroundResource(Constants.DETAIL_BG_SPORT_ICON[sportId]);
    }

    /**
     * 初始化播放器相关控件
     */
    private void initVideoPlayer() {
        //增加title
        binding.tvLive.setOnClickListener(this);
        binding.tvAnimi.setOnClickListener(this);
        binding.ivBack.setOnClickListener(this);
    }

    private void setWebView() {

        WebView webView = binding.wvAmin;
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setLoadsImagesAutomatically(true);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                CfLog.d("onPageStarted url:  " + url);
                //Log.d("---", "onPageStarted url:  " + url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                CfLog.d("onPageFinished url: " + url);
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                ToastUtils.showShort("");
            }

        });
    }

    @Override
    public StandardGSYVideoPlayer getGSYVideoPlayer() {
        return binding.videoPlayer;
    }

    @Override
    public GSYVideoOptionBuilder getGSYVideoOptionBuilder() {
        String videoUrl = "";
        if(!TextUtils.isEmpty(mMatch.getVideoInfo().m3u8SD)){
            videoUrl = mMatch.getVideoInfo().m3u8SD;
        } else if (!TextUtils.isEmpty(mMatch.getVideoInfo().m3u8HD)) {
            videoUrl = mMatch.getVideoInfo().m3u8HD;
        } else if (!TextUtils.isEmpty(mMatch.getVideoInfo().flvSD)) {
            videoUrl = mMatch.getVideoInfo().flvSD;
        } else if (!TextUtils.isEmpty(mMatch.getVideoInfo().flvHD)) {
            videoUrl = mMatch.getVideoInfo().flvHD;
        } else if (!TextUtils.isEmpty(mMatch.getVideoInfo().web)) {
            videoUrl = mMatch.getVideoInfo().web;
        }
        String score = "";
        if (mMatch.getScore(Constants.SCORE_TYPE_SCORE) != null && mMatch.getScore(Constants.SCORE_TYPE_SCORE).size() > 1) {
            score = mMatch.getScore(Constants.SCORE_TYPE_SCORE).get(0) + " - " + mMatch.getScore(Constants.SCORE_TYPE_SCORE).get(1);
        }
        ImageView thumb = new ImageView(this);
        int sportId = SPUtils.getInstance().getInt(SPKey.BT_SPORT_ID);
        thumb.setBackgroundResource(Constants.DETAIL_BG_SPORT_ICON[sportId]);
        return new GSYVideoOptionBuilder()
                .setThumbImageView(thumb)
                .setUrl(videoUrl)
                .setCacheWithPlay(false)
                .setShrinkImageRes(R.mipmap.bt_video_shrink)
                .setEnlargeImageRes(R.mipmap.bt_video_enlarge)
                .setVideoTitle(mMatch.getTeamMain() + score + mMatch.getTeamVistor())
                .setIsTouchWiget(false)
                //.setAutoFullWithSize(true)
                .setRotateViewAuto(false)
                .setLockLand(false)
                .setShowFullAnimation(false)//打开动画
                .setNeedLockFull(false)
                .setSeekRatio(1);
    }

    @Override
    public void clickForFullScreen() {

    }

    /**
     * 是否启动旋转横屏，true表示启动
     */
    @Override
    public boolean getDetailOrientationRotateAuto() {
        return true;
    }

    /**
     * 更新联赛数据
     */
    private void updateOptionData() {
        if(fragment == null) {
            if(mCategories != null && !mCategories.isEmpty()) {
                fragment = BtDetailOptionFragment.getInstance(mMatch, (ArrayList<PlayType>) mCategories.get(tabPos).getPlayTypeList());
                FragmentTransaction trans = getSupportFragmentManager()
                        .beginTransaction();
                trans.replace(R.id.fl_option, fragment);
                trans.commitAllowingStateLoss();
            }
        }else{
            if(mCategories != null && !mCategories.isEmpty()) {
                RxBus.getDefault().post(new BetContract(BetContract.ACTION_OPTION_CHANGE, mCategories.get(tabPos).getPlayTypeList()));
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
        viewModel.matchData.observe(this, match -> {
            this.mMatch = match;
            if(match.hasAs()){
                binding.tvAnimi.setVisibility(View.VISIBLE);
            }else{
                binding.tvAnimi.setVisibility(View.INVISIBLE);
            }

            if(match.hasVideo()){
                binding.tvLive.setVisibility(View.VISIBLE);
            }else{
                binding.tvLive.setVisibility(View.INVISIBLE);
            }
            binding.tvLeagueName.setText(match.getLeague().getLeagueName());
            binding.tvTeamMain.setText(match.getTeamMain());
            binding.tvTeamVisisor.setText(match.getTeamVistor());
            Glide.with(this)
                    .load(match.getIconMain())
                    //.apply(new RequestOptions().placeholder(placeholderRes))
                    .into(binding.ivLogoMain);

            binding.tvTeamVisisor.setText(match.getTeamVistor());
            Glide.with(this)
                    .load(match.getIconVisitor())
                    //.apply(new RequestOptions().placeholder(placeholderRes))
                    .into(binding.ivLogoVisitor);

            binding.tvTeamVisisor.setText(match.getTeamVistor());
            Glide.with(this)
                    .load(match.getIconMain())
                    //.apply(new RequestOptions().placeholder(placeholderRes))
                    .into(binding.ivLogoMainTop);

            binding.tvTeamVisisor.setText(match.getTeamVistor());
            Glide.with(this)
                    .load(match.getIconVisitor())
                    //.apply(new RequestOptions().placeholder(placeholderRes))
                    .into(binding.ivLogoVisitorTop);

            if (match.getScore(Constants.SCORE_TYPE_SCORE) != null && match.getScore(Constants.SCORE_TYPE_SCORE).size() > 1) {
                binding.tvScore.setText(match.getScore(Constants.SCORE_TYPE_SCORE).get(0) + "-" + match.getScore(Constants.SCORE_TYPE_SCORE).get(1));
            }
            int sportType = SPUtils.getInstance().getInt(SPKey.BT_SPORT_ID);
            String sport = SportTypeContants.SPORT_IDS[sportType];

            // 比赛未开始
            if(match.isUnGoingon()){
                binding.tvTimeTop.setText(TimeUtils.longFormatString(match.getMatchTime(), TimeUtils.FORMAT_MM_DD_HH_MM));
                binding.tvTime.setText(TimeUtils.longFormatString(match.getMatchTime(), TimeUtils.FORMAT_MM_DD_1));
                binding.tvScore.setText(TimeUtils.longFormatString(match.getMatchTime(), TimeUtils.FORMAT_HH_MM));
            }else {
                if (sport.equals(SportTypeContants.SPORT_ID_FB) || sport.equals(SportTypeContants.SPORT_ID_BSB)) {
                    binding.tvTime.setText(match.getStage() + " " + match.getTime());
                    binding.tvTimeTop.setText(match.getStage() + " " + match.getTime());
                } else {
                    binding.tvTime.setText(match.getStage());
                    binding.tvTimeTop.setText(match.getStage());
                }
            }

            if (binding.llData.getChildCount() == 0) {
                fbDataView = BaseDetailDataView.getInstance(this, match);
                if (fbDataView != null) {
                    binding.llData.addView(fbDataView);
                }
            } else {
                fbDataView.setMatch(match);
            }

        });
        viewModel.categoryListData.observe(this, categories -> {

            if(mCategories.size() != categories.size() ) {
                mCategories = categories;
                if (!categories.isEmpty()) {

                    for (int i = 0; i < categories.size(); i++) {
                        View view = LayoutInflater.from(this).inflate(R.layout.bt_layout_bet_catory_tab_item, null);
                        TextView tvName = view.findViewById(R.id.tab_item_name);
                        String name = categories.get(i).getName();

                        tvName.setText(name);
                        ColorStateList colorStateList = getResources().getColorStateList(R.color.bt_color_category_tab_text);
                        tvName.setTextColor(colorStateList);

                        binding.tabCategoryType.addTab(binding.tabCategoryType.newTab().setCustomView(view));

                    }

                }
            }else{
                mCategories = categories;
                updateOptionData();
            }
        });

        viewModel.betContractListData.observe(this, betContract -> {
            if (betContract.action == BetContract.ACTION_BTCAR_CHANGE) {
                setCgBtCar();
                updateOptionData();
                if(!BtCarManager.isCg()){
                    binding.rlCg.postDelayed(() -> RxBus.getDefault().postSticky(new BetContract(BetContract.ACTION_OPEN_TODAY)), 500);
                }
            } else if (betContract.action == BetContract.ACTION_OPEN_CG) {
                setCgBtCar();
                updateOptionData();
            }
        });
    }

    /**
     * 设置串关数量与显示与否
     */
    public void setCgBtCar(){
        binding.tvCgCount.setText(String.valueOf(BtCarManager.size()));
        binding.rlCg.setVisibility(!BtCarManager.isCg() ? View.GONE : BtCarManager.isEmpty() ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onRefresh() {
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
            btCarDialogFragment.show(BtDetailActivity.this.getSupportFragmentManager(), "btCarDialogFragment");
        } else if (id == R.id.tv_live) {
            binding.videoPlayer.setVisibility(View.VISIBLE);
            binding.ctlToolbarLeague.setVisibility(View.GONE);
            binding.rlToolbarTime.setVisibility(View.GONE);
            initVideoBuilderMode();
        } else if (id == R.id.tv_animi) {
            if(mMatch.hasAs()) {
                if (!TextUtils.isEmpty(mMatch.getAnmiUrls().get(0))) {
                    setWebView();
                    binding.wvAmin.setVisibility(View.VISIBLE);
                    binding.ctlToolbarLeague.setVisibility(View.GONE);
                    binding.rlToolbarTime.setVisibility(View.GONE);
                    binding.wvAmin.loadUrl(mMatch.getAnmiUrls().get(0));
                }
            }

        } else if (id == R.id.iv_back) {
            if(binding.videoPlayer.getVisibility() == View.VISIBLE || binding.videoPlayer.getGSYVideoManager().isPlaying()) {
                binding.videoPlayer.release();
                binding.videoPlayer.setVisibility(View.GONE);
            } else if (binding.wvAmin.getVisibility() == View.VISIBLE) {
                binding.wvAmin.destroy();
                binding.wvAmin.setVisibility(View.GONE);
            } else {
                finish();
            }
        }
    }
}
