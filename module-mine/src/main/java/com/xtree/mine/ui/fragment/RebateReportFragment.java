package com.xtree.mine.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.xtree.base.adapter.CacheViewHolder;
import com.xtree.base.adapter.CachedAutoRefreshAdapter;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.base.utils.CfLog;
import com.xtree.base.widget.FilterView;
import com.xtree.base.widget.LoadingDialog;
import com.xtree.mine.BR;
import com.xtree.mine.R;
import com.xtree.mine.databinding.FragmentReportRebateBinding;
import com.xtree.mine.databinding.ItemReportRebateBinding;
import com.xtree.mine.ui.viewmodel.ReportViewModel;
import com.xtree.mine.ui.viewmodel.factory.AppViewModelFactory;
import com.xtree.mine.vo.OrderTypeVo;
import com.xtree.mine.vo.RebateReportVo;
import com.xtree.mine.vo.RebateVo;
import com.xtree.mine.vo.StatusVo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.xtree.mvvmhabit.base.BaseFragment;

/**
 * 返水报表
 */
@Route(path = RouterFragmentPath.Mine.PAGER_REBATE_REPORT)
public class RebateReportFragment extends BaseFragment<FragmentReportRebateBinding, ReportViewModel> {

    CachedAutoRefreshAdapter<RebateVo> mAdapter; // 主列表
    int curPage = 0;
    RebateReportVo mReportVo;

    List<FilterView.IBaseVo> listType = new ArrayList<>();
    List<FilterView.IBaseVo> listStatus = new ArrayList<>();

    private String starttime;
    private String endtime;
    private String typeId = "0";
    private String status = "0";

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.tvwTitle.setText(R.string.txt_report_rebate); //
        binding.fvMain.setData(listType, listStatus);
        //binding.fvMain.setVisibility(View.VISIBLE, View.GONE, View.VISIBLE);
        //binding.fvMain.setTypeTitle(getString(R.string.txt_type), null, getString(R.string.txt_status));
        LoadingDialog.show(getContext());
        requestData(1);
    }

    @Override
    public void initView() {
        binding.ivwBack.setOnClickListener(v -> getActivity().finish());

        binding.fvMain.setQueryListener(v -> {
            curPage = 0;
            requestData(1);
        });

        binding.refreshLayout.setOnRefreshListener(refreshLayout -> {
            curPage = 0;
            requestData(1);
        });
        binding.refreshLayout.setOnLoadMoreListener(refreshLayout -> {
            CfLog.i("****** load more");
            requestData(curPage + 1);
        });

        mAdapter = new CachedAutoRefreshAdapter<RebateVo>() {

            @NonNull
            @Override
            public CacheViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                CacheViewHolder holder = new CacheViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_report_rebate, parent, false));
                return holder;
            }

            @Override
            public void onBindViewHolder(@NonNull CacheViewHolder holder, int position) {
                ItemReportRebateBinding binding2 = ItemReportRebateBinding.bind(holder.itemView);
                RebateVo vo = get(position);

                int status = "1".equals(vo.pstatus) ? R.string.txt_received : R.string.txt_unreceived;
                if (!"1".equals(vo.pstatus)) {
                    binding2.tvwPstatus.setSelected(true); // +
                } else {
                    binding2.tvwPstatus.setSelected(false); // -
                }

                String typeName = getTypeName(vo.type);

                binding2.tvwDate.setText(vo.date);
                binding2.tvwType.setText(typeName); //
                binding2.tvwPstatus.setText(status); //
                binding2.tvwBet.setText(vo.bet);
                binding2.tvwLiushui.setText(vo.liushui);
                binding2.tvwSelfMoney.setText(vo.self_money);

            }
        };
        binding.rcvMain.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rcvMain.setAdapter(mAdapter);
    }

    @Override
    public void initData() {
        super.initData();

        listType.add(new OrderTypeVo(0, getString(R.string.txt_all))); // 0-全部
        listType.add(new OrderTypeVo(3, getString(R.string.txt_rebate_ty))); // 3-体育
        listType.add(new OrderTypeVo(2, getString(R.string.txt_rebate_zr))); // 2-真人
        listType.add(new OrderTypeVo(4, getString(R.string.txt_rebate_qp))); // 4-棋牌
        listType.add(new OrderTypeVo(6, getString(R.string.txt_rebate_dj))); // 5-电竞

        listStatus.add(new StatusVo(0, getString(R.string.txt_all_status))); // 0-所有状态
        listStatus.add(new StatusVo(1, getString(R.string.txt_received))); // 1-已到账
        listStatus.add(new StatusVo(2, getString(R.string.txt_unreceived))); // 2-未到账
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_report_rebate;
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
        viewModel.liveDataRebateReport.observe(this, vo -> {
            CfLog.i("******");
            binding.refreshLayout.finishRefresh();
            binding.refreshLayout.finishLoadMore();
            if (vo == null) {
                binding.refreshLayout.setEnableLoadMore(false);
                return;
            }

            mReportVo = vo;
            curPage = vo.mobile_page.p;

            int total_page = vo.mobile_page.total_page.equals("false") ? 0 : Integer.parseInt(vo.mobile_page.total_page);
            if (vo.mobile_page.p < total_page) {
                binding.refreshLayout.setEnableLoadMore(true);
            } else {
                binding.refreshLayout.setEnableLoadMore(false);
            }

            if (curPage == 1) {
                mAdapter.clear();
            }
            mAdapter.addAll(vo.data);
            if (mAdapter.isEmpty()) {
                binding.tvwNoData.setVisibility(View.VISIBLE);
            } else {
                binding.tvwNoData.setVisibility(View.GONE);
            }

            // 本页合计
            binding.tvwBet.setText(vo.count.bet);
            binding.tvwEffectiveBet.setText(vo.count.effective_bet);
            binding.tvwSelfMoney.setText(vo.count.self_money);

            // 总合计
            binding.tvwSumBet.setText(vo.total.sum_bet);
            binding.tvwSumEffectiveBet.setText(vo.total.sum_effective_bet);
            binding.tvwSumSelfMoney.setText(vo.total.sum_self_money);
        });

    }

    private String getTypeName(String id) {
        for (FilterView.IBaseVo t : listType) {
            if (id.equals(t.getShowId())) {
                return t.getShowName();
            }
        }
        return "--";
    }

    private void requestData(int page) {
        CfLog.i();
        starttime = binding.fvMain.getStartTime();
        endtime = binding.fvMain.getEndTime();
        typeId = binding.fvMain.getTypeId("0");
        status = binding.fvMain.getStatusId("0");

        HashMap<String, String> map = new HashMap<>();
        map.put("controller", "compact");
        map.put("action", "userantireport");
        map.put("starttime", starttime);
        map.put("endtime", endtime);
        map.put("type", typeId);
        map.put("pstatus", status);
        map.put("p", "" + page);
        map.put("pn", "15");
        map.put("orderby", "date");
        map.put("sort", "desc");
        map.put("client", "m");

        CfLog.i(map.toString());
        viewModel.getRebateReport(map);
    }

}