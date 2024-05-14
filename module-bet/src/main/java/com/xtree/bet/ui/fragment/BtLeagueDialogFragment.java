package com.xtree.bet.ui.fragment;

import static com.xtree.base.utils.BtDomainUtil.KEY_PLATFORM;
import static com.xtree.base.utils.BtDomainUtil.PLATFORM_PM;

import android.app.Application;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.ExpandableListView;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.xtree.bet.R;
import com.xtree.bet.bean.ui.InitialLeagueArea;
import com.xtree.bet.bean.ui.League;
import com.xtree.bet.bean.ui.LeagueArea;
import com.xtree.bet.contract.BetContract;
import com.xtree.bet.databinding.BtDialogLeagueBinding;
import com.xtree.bet.ui.adapter.SettingLeagueAdapter;
import com.xtree.bet.ui.viewmodel.TemplateBtSettingLeagueModel;
import com.xtree.bet.ui.viewmodel.factory.AppViewModelFactory;
import com.xtree.bet.ui.viewmodel.factory.PMAppViewModelFactory;
import com.xtree.bet.ui.viewmodel.fb.FBBtSettingLeagueModel;
import com.xtree.bet.ui.viewmodel.pm.PMBtSettingLeagueModel;
import com.xtree.bet.weight.SideBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import me.xtree.mvvmhabit.base.BaseDialogFragment;
import me.xtree.mvvmhabit.bus.RxBus;
import me.xtree.mvvmhabit.utils.ConvertUtils;
import me.xtree.mvvmhabit.utils.SPUtils;
import me.xtree.mvvmhabit.utils.Utils;

/**
 * 联赛筛选
 */
public class BtLeagueDialogFragment extends BaseDialogFragment<BtDialogLeagueBinding, TemplateBtSettingLeagueModel> {
    public final static String KEY_LEAGUE = "KEY_LEAGUE";
    public final static String KEY_SPORTID = "KEY_SPORTID";
    public final static String KEY_TYPE = "KEY_TYPE";
    public final static String KEY_LEAGUEIDS = "KEY_LEAGUEIDS";
    private String mPlatform = SPUtils.getInstance().getString(KEY_PLATFORM);
    // 已选联赛
    private List<Long> mLeagueIdList = new ArrayList<>();
    private int sportId;
    private int type;
    private boolean isSearch;
    private SideBar sideBar;
    private SettingLeagueAdapter settingLeagueAdapter;
    private List<League> mLeagueList = new ArrayList<>(); // 后台查询到的所有联赛列表
    private List<League> mSearchLeagueList = new ArrayList<>();
    private List<LeagueArea> mLeagueAreaList = new ArrayList<>(); // 联赛按区域分组数据
    private List<LeagueArea> mSearchLeagueAreaList = new ArrayList<>();
    private List<String> mInitialList = new ArrayList<>(); // 首字母列表

    public static BtLeagueDialogFragment getInstance(List<League> leagueList, int sportId, int type, List<Long> leagueIdList){
        BtLeagueDialogFragment btResultDialogFragment = new BtLeagueDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(KEY_LEAGUE, (ArrayList<? extends Parcelable>) leagueList);
        bundle.putInt(KEY_SPORTID, sportId);
        bundle.putInt(KEY_TYPE, type);
        long[] leagueIdArray = new long[leagueIdList.size()];
        for (int i = 0; i <leagueIdList.size(); i ++){
            leagueIdArray[i] = leagueIdList.get(i);
        }
        bundle.putLongArray(KEY_LEAGUEIDS, leagueIdArray);
        btResultDialogFragment.setArguments(bundle);
        return btResultDialogFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        binding.tvClose.setOnClickListener(this);
        binding.tvConfirm.setOnClickListener(this);
        binding.ivClose.setOnClickListener(this);
        binding.aelLeague.setOnGroupClickListener((parent, v, groupPosition, id) -> {
            if (binding.aelLeague.isGroupExpanded(groupPosition)){
                binding.aelLeague.collapseGroupWithAnimation(groupPosition);
            }else{
                binding.aelLeague.expandGroupWithAnimation(groupPosition);
            }
            return true;
        });

        binding.aelLeague.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                long position = binding.aelLeague.getExpandableListPosition(firstVisibleItem);
                int type = binding.aelLeague.getPackedPositionType(position);
                if (type == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
                    int groupPosition = binding.aelLeague.getPackedPositionGroup(position);
                    sideBar.setHint(groupPosition);
                }
            }
        });

        binding.cbAll.setOnClickListener(this);
        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.cbAll.setChecked(false);
                for(int i = 0; i < mLeagueAreaList.size(); i ++){
                    mLeagueAreaList.get(i).setSelected(false);
                }
                for(int i = 0; i < mLeagueList.size(); i ++){
                    mLeagueList.get(i).setSelected(false);
                }
                if(s == null || TextUtils.isEmpty(s.toString())){
                    binding.ivClose.setVisibility(View.GONE);
                    viewModel.getLeagueAreaList(mLeagueList, false, null);
                    isSearch = false;
                }else {
                    binding.ivClose.setVisibility(View.VISIBLE);
                    mSearchLeagueList.clear();
                    for (League league : mLeagueList) {
                        if (league.getLeagueName().contains(s.toString())){
                            mSearchLeagueList.add(league);
                        }
                    }
                    viewModel.getLeagueAreaList(mSearchLeagueList, true, null);
                    isSearch = true;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.ivClose.setOnClickListener(this);
    }

    @Override
    public void initData() {
        long[] leagues = getArguments().getLongArray(KEY_LEAGUEIDS);
        for (int i = 0; i < leagues.length; i++) {
            mLeagueIdList.add(leagues[i]);
        }
        sportId = getArguments().getInt(KEY_SPORTID);
        type = getArguments().getInt(KEY_TYPE);
        viewModel.getLeagueAreaList(mLeagueList, false, mLeagueIdList);
        viewModel.getOnSaleLeagues(sportId, type, mLeagueIdList);
        viewModel.addSubscription();
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.bt_dialog_league;
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = Objects.requireNonNull(getDialog()).getWindow();
        WindowManager.LayoutParams params = Objects.requireNonNull(window).getAttributes();
        //设置显示在底部
        params.gravity = Gravity.BOTTOM;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = (int) (ConvertUtils.getScreenHeight(getContext()) * 0.85);
        window.setAttributes(params);
    }

    @Override
    public void initViewObservable() {
        viewModel.settingInitialLeagueAreaData.observe(this, mInitialLeagueAreaList -> {
            mInitialList.clear();
            mLeagueAreaList.clear();
            for(InitialLeagueArea initialLeagueArea : mInitialLeagueAreaList){
                mInitialList.add(initialLeagueArea.getName());
                mLeagueAreaList.addAll(initialLeagueArea.getLeagueAreaList());
            }
            if(settingLeagueAdapter == null){
                settingLeagueAdapter = new SettingLeagueAdapter(getContext(), mLeagueAreaList);
                binding.aelLeague.setAdapter(settingLeagueAdapter);
            }else {
                settingLeagueAdapter.setData(mLeagueAreaList);
            }
            for (int i = 0; i < binding.aelLeague.getExpandableListAdapter().getGroupCount(); i++) {
                binding.aelLeague.expandGroup(i);
            }
            sideBar = new SideBar(getContext(), mInitialList);
            if(binding.sbLeague.getChildCount() > 0){
                binding.sbLeague.removeAllViews();
            }
            binding.sbLeague.addView(sideBar);
            sideBar.setOnSelectListener(index -> {
                if(index > -1) {
                    int position = mLeagueAreaList.indexOf(mInitialLeagueAreaList.get(index).getLeagueAreaList().get(0));
                    binding.aelLeague.scroll(position);
                }
            });
        });

        viewModel.settingSearchInitialLeagueAreaData.observe(this, mSearchInitialLeagueAreaList -> {
            mInitialList.clear();
            mSearchLeagueAreaList.clear();
            for(InitialLeagueArea initialLeagueArea : mSearchInitialLeagueAreaList){
                mInitialList.add(initialLeagueArea.getName());
                mSearchLeagueAreaList.addAll(initialLeagueArea.getLeagueAreaList());
            }

            if(settingLeagueAdapter == null){
                settingLeagueAdapter = new SettingLeagueAdapter(getContext(), mSearchLeagueAreaList);
                binding.aelLeague.setAdapter(settingLeagueAdapter);
            }else {
                settingLeagueAdapter.setData(mSearchLeagueAreaList);
            }
            for (int i = 0; i < binding.aelLeague.getExpandableListAdapter().getGroupCount(); i++) {
                binding.aelLeague.expandGroup(i);
            }
            sideBar = new SideBar(getContext(), mInitialList);
            if(binding.sbLeague.getChildCount() > 0){
                binding.sbLeague.removeAllViews();
            }
            binding.sbLeague.addView(sideBar);
            sideBar.setOnSelectListener(index -> {
                int position = mSearchLeagueAreaList.indexOf(mSearchInitialLeagueAreaList.get(index).getLeagueAreaList().get(0));
                binding.aelLeague.scroll(position);
            });
        });

        viewModel.settingLeagueData.observe(this, leagueList -> {
            this.mLeagueList = leagueList;
        });
        viewModel.betContractIsCheckedAllLeagueData.observe(this, betContract -> {
            if (betContract.action == BetContract.ACTION_CHECK_ALL_CHECK) {
                boolean isCheckAll = true;
                for(LeagueArea leagueArea : mLeagueAreaList){
                    if(!leagueArea.isSelected()){
                        isCheckAll = false;
                        break;
                    }
                }
                if(isCheckAll != binding.cbAll.isChecked()) {
                    binding.cbAll.setChecked(isCheckAll);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.tv_close){
            dismiss();
        } else if (id == R.id.tv_confirm) {
            List<Long> leagueIdList = new ArrayList<>();
            if(isSearch) {
                for (League league : mSearchLeagueList) {
                    if (league.isSelected()) {
                        leagueIdList.add(league.getId());
                    }
                }
            }else {
                for (League league : mLeagueList) {
                    if (league.isSelected()) {
                        leagueIdList.add(league.getId());
                    }
                }
            }
            RxBus.getDefault().post(new BetContract(BetContract.ACTION_CHECK_SEARCH_BY_LEAGUE, leagueIdList));
            dismiss();
        } else if (id == R.id.iv_close) {
            binding.etSearch.setText("");
            viewModel.getLeagueAreaList(mLeagueList, false, mLeagueIdList);
            isSearch = false;
        } else if (id == R.id.cb_all) {
            boolean isChecked = binding.cbAll.isChecked();
            if(isSearch){
                for(int i = 0; i < mSearchLeagueAreaList.size(); i ++){
                    mSearchLeagueAreaList.get(i).setSelected(isChecked);
                }
            }else {
                for (int i = 0; i < mLeagueAreaList.size(); i++) {
                    mLeagueAreaList.get(i).setSelected(isChecked);
                }
            }
            for(int i = 0; i < mLeagueList.size(); i ++){
                mLeagueList.get(i).setSelected(isChecked);
            }
            if(settingLeagueAdapter != null) {
                settingLeagueAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public TemplateBtSettingLeagueModel initViewModel() {
        if (!TextUtils.equals(mPlatform, PLATFORM_PM)) {
            AppViewModelFactory factory = AppViewModelFactory.getInstance((Application) Utils.getContext());
            return new ViewModelProvider(this, factory).get(FBBtSettingLeagueModel.class);
        } else {
            PMAppViewModelFactory factory = PMAppViewModelFactory.getInstance((Application) Utils.getContext());
            return new ViewModelProvider(this, factory).get(PMBtSettingLeagueModel.class);
        }
    }
}
