package com.xtree.mine.ui.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.android.material.tabs.TabLayout;
import com.xtree.base.adapter.CacheViewHolder;
import com.xtree.base.adapter.CachedAutoRefreshAdapter;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.base.utils.CfLog;
import com.xtree.base.widget.FilterView;
import com.xtree.base.widget.LoadingDialog;
import com.xtree.mine.BR;
import com.xtree.mine.R;
import com.xtree.mine.databinding.FragmentReportAccBinding;
import com.xtree.mine.databinding.ItemReportAccChangeBinding;
import com.xtree.mine.ui.viewmodel.ReportViewModel;
import com.xtree.mine.ui.viewmodel.factory.AppViewModelFactory;
import com.xtree.mine.vo.AccountChangeVo;
import com.xtree.mine.vo.GameChangeVo;
import com.xtree.mine.vo.GameItemVo;
import com.xtree.mine.vo.OrderTypeVo;
import com.xtree.mine.vo.OrderVo;
import com.xtree.mine.vo.StatusVo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.xtree.mvvmhabit.base.BaseFragment;

/**
 * 账变记录
 */
@Route(path = RouterFragmentPath.Mine.PAGER_ACCOUNT_CHANGE)
public class AccountChangeFragment extends BaseFragment<FragmentReportAccBinding, ReportViewModel> {
    CachedAutoRefreshAdapter<OrderVo> mAccAdapter; // 主列表
    CachedAutoRefreshAdapter<GameItemVo> mGameAdapter; // 游戏列表
    int curPage = 0;
    int curGamePage = 0;
    AccountChangeVo mAccReportVo;
    GameChangeVo gameChangeVo;

    List<FilterView.IBaseVo> listType = new ArrayList<>();
    List<FilterView.IBaseVo> listStatus = new ArrayList<>();
    List<FilterView.IBaseVo> listGameType = new ArrayList<>();

    //private String userId;
    private String starttime;
    private String endtime;
    private String typeId = "0";
    private String status = "0";

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.tvwTitle.setText(R.string.txt_report_acc_change);
        // 账变记录
        binding.fvMain.setData(listType, listStatus);
        binding.fvMain.setVisibility(View.VISIBLE, View.GONE, View.VISIBLE);
        binding.fvMain.setTypeTitle(getString(R.string.txt_acc_change_type), null, getString(R.string.txt_acc_change_status));
        // 游戏账变记录
        binding.fvGameMain.setData(listGameType);
        binding.fvGameMain.setVisibility(View.VISIBLE, View.GONE, View.GONE);
        binding.fvGameMain.setTypeTitle(getString(R.string.txt_acc_change_type), null, null);
        LoadingDialog.show(getContext());
        requestAccChangeData(1);
        //requestGameChangeData(1);
    }

    @Override
    public void initView() {
        binding.ivwBack.setOnClickListener(v -> getActivity().finish());
        // 账变记录
        binding.fvMain.setQueryListener(v -> {
            LoadingDialog.show(getContext());
            curPage = 0;
            requestAccChangeData(1);
        });

        binding.refreshLayout.setOnRefreshListener(refreshLayout -> {
            curPage = 0;
            requestAccChangeData(1);
        });

        binding.refreshLayout.setOnLoadMoreListener(refreshLayout -> {
            CfLog.i("****** load more");
            requestAccChangeData(curPage + 1);
        });
        // 游戏账变记录
        binding.fvGameMain.setQueryListener(v -> {
            LoadingDialog.show(getContext());
            curGamePage = 0;
            requestGameChangeData(1);
        });

        binding.refreshGameLayout.setOnRefreshListener(refreshLayout -> {
            curGamePage = 0;
            requestGameChangeData(1);
        });
        binding.refreshGameLayout.setOnLoadMoreListener(refreshLayout -> {
            CfLog.i("****** load more");
            requestGameChangeData(curGamePage + 1);
        });

        mAccAdapter = new CachedAutoRefreshAdapter<OrderVo>() {

            @NonNull
            @Override
            public CacheViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                CacheViewHolder holder = new CacheViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_report_acc_change, parent, false));
                return holder;
            }

            @Override
            public void onBindViewHolder(@NonNull CacheViewHolder holder, int position) {
                ItemReportAccChangeBinding binding2 = ItemReportAccChangeBinding.bind(holder.itemView);
                OrderVo vo = get(position);

                // 1,3 失败; 其它成功
                int status = "1".equals(vo.transferstatus) || "3".equals(vo.transferstatus) ? R.string.txt_fail : R.string.txt_succ;
                String operations = "1".equals(vo.operations) ? "+" : "-";
                // 收入:绿色, 支出:红色
                if (!"1".equals(vo.operations)) {
                    binding2.tvwAmount.setSelected(true); // -
                    binding2.tvwInOut.setText(R.string.txt_outlay); // 支出
                } else {
                    binding2.tvwAmount.setSelected(false); // +
                    binding2.tvwInOut.setText(R.string.txt_income); // 收入
                }
                String notes = TextUtils.isEmpty(vo.notes) ? "--" : vo.notes;

                binding2.tvwUsername.setText(mAccReportVo.username); //
                binding2.tvwTimes.setText(vo.times);
                binding2.tvwCntitle.setText(vo.cntitle);
                binding2.tvwAmount.setText(operations + vo.amount); // + -

                binding2.tvwBalance.setText(vo.availablebalance);
                binding2.tvwStatus.setText(status); //
                binding2.tvwOrderno.setText(vo.orderno);
                binding2.tvwNotes.setText(notes);

            }
        };
        binding.rcvMain.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rcvMain.setAdapter(mAccAdapter);

        mGameAdapter = new CachedAutoRefreshAdapter<GameItemVo>() {

            @NonNull
            @Override
            public CacheViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                CacheViewHolder holder = new CacheViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_report_acc_change, parent, false));
                return holder;
            }

            @Override
            public void onBindViewHolder(@NonNull CacheViewHolder holder, int position) {
                ItemReportAccChangeBinding binding2 = ItemReportAccChangeBinding.bind(holder.itemView);
                GameItemVo vo = get(position);

                binding2.tvwStatusTitle.setVisibility(View.GONE);
                binding2.tvwStatus.setVisibility(View.GONE);

                String operations = "1".equals(vo.operations) ? "+" : "-";
                // 收入:绿色, 支出:红色
                if (!"1".equals(vo.operations)) {
                    binding2.tvwAmount.setSelected(true); // -
                    binding2.tvwInOut.setText(R.string.txt_outlay); // 支出
                } else {
                    binding2.tvwAmount.setSelected(false); // +
                    binding2.tvwInOut.setText(R.string.txt_income); // 收入
                }
                String notes = TextUtils.isEmpty(vo.description) ? "--" : vo.description;

                binding2.tvwUsername.setText(vo.lotteryname);
                binding2.tvwTimes.setText(vo.methodname);
                binding2.tvwCntitle.setText(vo.title);
                binding2.tvwAmount.setText(operations + vo.amount);

                binding2.tvwBalance.setText(vo.availablebalance);
                binding2.tvwOrderno.setText(vo.orderno);
                binding2.tvwNotes.setText(notes);

            }
        };
        binding.rcvGameChangeMain.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rcvGameChangeMain.setAdapter(mGameAdapter);

        // 其他页面也会使用，固xml设定Gone，但
        binding.tblType.setVisibility(View.VISIBLE);

        binding.tblType.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                CfLog.e(tab.getPosition() + "");
                if (tab.getPosition() == 0) {
                    binding.fvMain.setVisibility(View.VISIBLE);
                    binding.fvGameMain.setVisibility(View.GONE);
                    binding.refreshLayout.setVisibility(View.VISIBLE);
                    binding.refreshGameLayout.setVisibility(View.GONE);
                } else if (tab.getPosition() == 1) {
                    binding.fvMain.setVisibility(View.GONE);
                    binding.fvGameMain.setVisibility(View.VISIBLE);
                    binding.refreshLayout.setVisibility(View.GONE);
                    binding.refreshGameLayout.setVisibility(View.VISIBLE);
                }
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
        super.initData();

        listType.add(new OrderTypeVo(0, getString(R.string.txt_all)));

        listStatus.add(new StatusVo(0, getString(R.string.txt_all)));
        listStatus.add(new StatusVo(1, getString(R.string.txt_succ)));
        listStatus.add(new StatusVo(2, getString(R.string.txt_fail)));

        listGameType.add(new StatusVo(0, getString(R.string.txt_all)));
        listGameType.add(new StatusVo(3, getString(R.string.txt_add_game)));
        listGameType.add(new StatusVo(5, getString(R.string.txt_send_bonus)));
        listGameType.add(new StatusVo(6, getString(R.string.txt_minus_money)));
        listGameType.add(new StatusVo(7, getString(R.string.txt_now_return_money)));
        listGameType.add(new StatusVo(8, getString(R.string.txt_game_minus)));
        listGameType.add(new StatusVo(9, getString(R.string.txt_return_order)));
        listGameType.add(new StatusVo(15, getString(R.string.txt_special_money)));
        listGameType.add(new StatusVo(80, getString(R.string.txt_over_money_minus)));
        listGameType.add(new StatusVo(82, getString(R.string.txt_over_money_return)));

        binding.fvGameMain.setData(listGameType);
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_report_acc;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public ReportViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getActivity().getApplication());
        return new ViewModelProvider(this, factory).get(ReportViewModel.class);
    }

    @Override
    public void initViewObservable() {
        viewModel.liveDataAccountChange.observe(this, vo -> {
            CfLog.i("******");
            binding.refreshLayout.finishRefresh();
            binding.refreshLayout.finishLoadMore();
            if (vo == null) {
                binding.refreshLayout.setEnableLoadMore(false);
                return;
            }

            mAccReportVo = vo;
            curPage = vo.mobile_page.p;

            int total_page = vo.mobile_page.total_page.equals("false") ? 0 : Integer.parseInt(vo.mobile_page.total_page);
            if (vo.mobile_page.p < total_page) {
                binding.refreshLayout.setEnableLoadMore(true);
            } else {
                binding.refreshLayout.setEnableLoadMore(false);
            }

            if (curPage == 1) {
                listType.clear();
                listStatus.clear();

                listType.add(new OrderTypeVo(0, getString(R.string.txt_all)));
                listType.addAll(vo.ordertypes.values());

                listStatus.add(new StatusVo(0, getString(R.string.txt_all)));
                listStatus.add(new StatusVo(1, getString(R.string.txt_succ)));
                listStatus.add(new StatusVo(2, getString(R.string.txt_fail)));

                binding.fvMain.reSetData(listType, listStatus);
            }

            if (curPage == 1) {
                mAccAdapter.clear();
            }
            mAccAdapter.addAll(vo.orders);
            if (mAccAdapter.isEmpty()) {
                binding.tvwNoData.setVisibility(View.VISIBLE);
            } else {
                binding.tvwNoData.setVisibility(View.GONE);
            }
        });

        viewModel.liveDataGameChange.observe(this, vo -> {
            CfLog.i("******");
            binding.refreshGameLayout.finishRefresh();
            binding.refreshGameLayout.finishLoadMore();
            if (vo == null) {
                binding.refreshGameLayout.setEnableLoadMore(false);
                return;
            }

            gameChangeVo = vo;
            curGamePage = vo.mobilePage.p;

            if (vo.mobilePage.p < vo.mobilePage.totalPage) {
                binding.refreshGameLayout.setEnableLoadMore(true);
            } else {
                binding.refreshGameLayout.setEnableLoadMore(false);
            }

            if (curPage == 1) {
                mGameAdapter.clear();
            }

            mGameAdapter.addAll(vo.data);
            if (mGameAdapter.isEmpty()) {
                binding.tvwGmaeNoData.setVisibility(View.VISIBLE);
            } else {
                binding.tvwGmaeNoData.setVisibility(View.GONE);
            }
        });

    }

    private void requestAccChangeData(int page) {
        CfLog.i();
        starttime = binding.fvMain.getStartTime();
        endtime = binding.fvMain.getEndTime();
        typeId = binding.fvMain.getTypeId("0");
        status = binding.fvMain.getStatusId("0");

        HashMap<String, String> map = new HashMap<>();
        //map.put("userid", userId);
        map.put("starttime", starttime);
        map.put("endtime", endtime);
        map.put("ordertype", typeId);
        map.put("status", status);
        map.put("p", "" + page);
        map.put("pn", "20");
        map.put("client", "m");

        CfLog.i(map.toString());
        viewModel.getAccountChangeReport(map);
    }

    private void requestGameChangeData(int page) {
        CfLog.i();
        starttime = binding.fvGameMain.getStartTime();
        endtime = binding.fvGameMain.getEndTime();
        typeId = binding.fvGameMain.getTypeId("0");

        HashMap<String, String> map = new HashMap<>();
        map.put("starttime", starttime);
        map.put("endtime", endtime);
        map.put("ordertype", typeId);
        map.put("p", "" + page);
        map.put("page_size", "20");

        CfLog.i(map.toString());
        viewModel.getGameChangeReport(map);
    }
}