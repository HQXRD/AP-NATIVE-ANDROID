package com.xtree.bet.ui.fragment;

import android.app.Application;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.xtree.bet.R;
import com.xtree.bet.bean.ui.InitialLeagueArea;
import com.xtree.bet.bean.ui.League;
import com.xtree.bet.bean.ui.LeagueArea;
import com.xtree.bet.contract.BetContract;
import com.xtree.bet.databinding.BtDialogLeagueBinding;
import com.xtree.bet.ui.adapter.SettingLeagueAdapter;
import com.xtree.bet.ui.viewmodel.BtSettingLeagueModel;
import com.xtree.bet.ui.viewmodel.factory.AppViewModelFactory;
import com.xtree.bet.weight.SideBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import me.xtree.mvvmhabit.base.BaseDialogFragment;
import me.xtree.mvvmhabit.bus.RxBus;
import me.xtree.mvvmhabit.utils.ConvertUtils;
import me.xtree.mvvmhabit.utils.Utils;

/**
 * 联赛筛选
 */
public class BtLeagueDialogFragment extends BaseDialogFragment<BtDialogLeagueBinding, BtSettingLeagueModel> {
    public final static String KEY_LEAGUE = "KEY_LEAGUE";
    public final static String KEY_SPORTID = "KEY_SPORTID";
    public final static String KEY_TYPE = "KEY_TYPE";
    private int sportId;
    private int type;
    private SideBar sideBar;
    private SettingLeagueAdapter settingLeagueAdapter;
    private List<League> mLeagueList; // 后台查询到的所有联赛列表
    private List<League> mSearchLeagueList = new ArrayList<>();
    private List<LeagueArea> mLeagueAreaList = new ArrayList<>(); // 联赛按区域分组数据
    private List<LeagueArea> mSearchLeagueAreaList = new ArrayList<>();
    private List<String> mInitialList = new ArrayList<>(); // 首字母列表

    public static BtLeagueDialogFragment getInstance(List<League> leagueList, int sportId, int type){
        BtLeagueDialogFragment btResultDialogFragment = new BtLeagueDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(KEY_LEAGUE, (ArrayList<? extends Parcelable>) leagueList);
        bundle.putInt(KEY_SPORTID, sportId);
        bundle.putInt(KEY_TYPE, type);
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
        binding.cbAll.setOnClickListener(v -> {
            boolean isChecked = binding.cbAll.isChecked();
            for(int i = 0; i < mLeagueAreaList.size(); i ++){
                mLeagueAreaList.get(i).setSelected(isChecked);
            }
            for(int i = 0; i < mLeagueList.size(); i ++){
                mLeagueList.get(i).setSelected(isChecked);
            }
            settingLeagueAdapter.notifyDataSetChanged();
        });
        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s == null || s.toString() == null){
                    viewModel.getLeagueAreaList(mLeagueList, true);
                }else {
                    mSearchLeagueList.clear();
                    for (League league : mLeagueList) {
                        if (league.getLeagueName().contains(s.toString())){
                            mSearchLeagueList.add(league);
                        }
                    }
                    viewModel.getLeagueAreaList(mSearchLeagueList, true);
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
        mLeagueList = (ArrayList)getArguments().getParcelableArrayList(KEY_LEAGUE);
        sportId = getArguments().getInt(KEY_SPORTID);
        type = getArguments().getInt(KEY_TYPE);
        viewModel.getLeagueAreaList(mLeagueList, false);
        viewModel.getOnSaleLeagues(sportId, type);
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
            binding.sbLeague.addView(sideBar);
            sideBar.setOnSelectListener(index -> {
                int position = mLeagueAreaList.indexOf(mInitialLeagueAreaList.get(index).getLeagueAreaList().get(0));
                binding.aelLeague.scroll(position);
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
                int position = mLeagueAreaList.indexOf(mSearchInitialLeagueAreaList.get(index).getLeagueAreaList().get(0));
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
            List<Integer> leagueIdList = new ArrayList<>();
            for (League league : mLeagueList) {
                if (league.isSelected()) {
                    leagueIdList.add(league.getId());
                }
            }
            RxBus.getDefault().post(new BetContract(BetContract.ACTION_CHECK_SEARCH_BY_LEAGUE, leagueIdList));
            dismiss();
        } else if (id == R.id.iv_close) {
            binding.etSearch.setText("");
            viewModel.getLeagueAreaList(mLeagueList, true);
        }
    }

    @Override
    public BtSettingLeagueModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance((Application) Utils.getContext());
        return new ViewModelProvider(this, factory).get(BtSettingLeagueModel.class);
    }
}
