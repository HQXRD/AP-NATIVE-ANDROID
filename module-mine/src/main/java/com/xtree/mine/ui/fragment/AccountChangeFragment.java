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
import com.xtree.base.adapter.CacheViewHolder;
import com.xtree.base.adapter.CachedAutoRefreshAdapter;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.base.utils.CfLog;
import com.xtree.base.widget.FilterView;
import com.xtree.base.widget.LoadingDialog;
import com.xtree.mine.BR;
import com.xtree.mine.R;
import com.xtree.mine.databinding.FragmentReportBinding;
import com.xtree.mine.databinding.ItemReportAccChangeBinding;
import com.xtree.mine.ui.viewmodel.ReportViewModel;
import com.xtree.mine.ui.viewmodel.factory.AppViewModelFactory;
import com.xtree.mine.vo.AccountChangeVo;
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
public class AccountChangeFragment extends BaseFragment<FragmentReportBinding, ReportViewModel> {

    CachedAutoRefreshAdapter<OrderVo> mAdapter; // 主列表
    int curPage = 0;
    AccountChangeVo mReportVo;

    List<FilterView.IBaseVo> listType = new ArrayList<>();
    List<FilterView.IBaseVo> listStatus = new ArrayList<>();

    private String starttime;
    private String endtime;
    private String typeId = "0";
    private String status = "0";

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.tvwTitle.setText(R.string.txt_report_acc_change); //
        binding.fvMain.setData(listType, listStatus);
        binding.fvMain.setVisibility(View.VISIBLE, View.GONE, View.VISIBLE);
        binding.fvMain.setTypeTitle(getString(R.string.txt_acc_change_type), null, getString(R.string.txt_acc_change_status));
        LoadingDialog.show(getContext());
        requestData(1);
    }

    @Override
    public void initView() {
        binding.ivwBack.setOnClickListener(v -> getActivity().finish());

        binding.fvMain.setQueryListener(v -> {
            LoadingDialog.show(getContext());
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

        mAdapter = new CachedAutoRefreshAdapter<OrderVo>() {

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

                binding2.tvwUsername.setText(mReportVo.username); //
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
        binding.rcvMain.setAdapter(mAdapter);
    }

    @Override
    public void initData() {
        super.initData();

        listType.add(new OrderTypeVo(0, getString(R.string.txt_all)));

        listStatus.add(new StatusVo(0, getString(R.string.txt_all)));
        listStatus.add(new StatusVo(1, getString(R.string.txt_succ)));
        listStatus.add(new StatusVo(2, getString(R.string.txt_fail)));
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_report;
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

            mReportVo = vo;
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
                mAdapter.clear();
            }
            mAdapter.addAll(vo.orders);
            if (mAdapter.isEmpty()) {
                binding.tvwNoData.setVisibility(View.VISIBLE);
            } else {
                binding.tvwNoData.setVisibility(View.GONE);
            }
        });

    }

    private void requestData(int page) {
        CfLog.i();
        starttime = binding.fvMain.getStartTime();
        endtime = binding.fvMain.getEndTime();
        typeId = binding.fvMain.getTypeId("0");
        status = binding.fvMain.getStatusId("0");

        HashMap<String, String> map = new HashMap<>();
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

}