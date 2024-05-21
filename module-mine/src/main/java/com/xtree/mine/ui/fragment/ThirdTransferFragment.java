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
import com.xtree.mine.databinding.FragmentReportBinding;
import com.xtree.mine.databinding.ItemReport3rdTransferBinding;
import com.xtree.mine.ui.viewmodel.ReportViewModel;
import com.xtree.mine.ui.viewmodel.factory.AppViewModelFactory;
import com.xtree.mine.vo.OrderTypeVo;
import com.xtree.mine.vo.StatusVo;
import com.xtree.mine.vo.ThirdTransferReportVo;
import com.xtree.mine.vo.ThirdTransferVo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.xtree.mvvmhabit.base.BaseFragment;

/**
 * 第三方转账
 */
@Route(path = RouterFragmentPath.Mine.PAGER_THIRD_TRANSFER)
public class ThirdTransferFragment extends BaseFragment<FragmentReportBinding, ReportViewModel> {

    CachedAutoRefreshAdapter<ThirdTransferVo> mAdapter; // 主列表
    int curPage = 0;
    ThirdTransferReportVo mReportVo;

    List<FilterView.IBaseVo> listType = new ArrayList<>();
    List<FilterView.IBaseVo> listType2 = new ArrayList<>();
    List<FilterView.IBaseVo> listStatus = new ArrayList<>();

    private String starttime;
    private String endtime;
    private String typeId = "0";
    private String typeId2 = "0";
    private String status = "0";

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.tvwTitle.setText(R.string.txt_report_3d); //
        binding.fvMain.setData(listType, listType, listStatus);
        binding.fvMain.setVisibility(View.VISIBLE, View.VISIBLE, View.VISIBLE);
        binding.fvMain.setTypeTitle(getString(R.string.txt_platform_trans_out), getString(R.string.txt_platform_trans_in), getString(R.string.txt_status));
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

        mAdapter = new CachedAutoRefreshAdapter<ThirdTransferVo>() {

            @NonNull
            @Override
            public CacheViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                CacheViewHolder holder = new CacheViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_report_3rd_transfer, parent, false));
                return holder;
            }

            @Override
            public void onBindViewHolder(@NonNull CacheViewHolder holder, int position) {
                ItemReport3rdTransferBinding binding2 = ItemReport3rdTransferBinding.bind(holder.itemView);
                ThirdTransferVo vo = get(position);

                binding2.tvwActionTime.setText(vo.actiontime);
                binding2.tvwStatus.setText(vo.status);
                binding2.tvwOut.setText(vo.out);
                binding2.tvwIn.setText(vo.in);
                binding2.tvwAmount.setText(vo.amount);
                binding2.tvwOrderno.setText(vo.orderno);
                binding2.tvwPrebalance.setText(vo.prebalance);
                binding2.tvwChannelbalance.setText(vo.channelbalance);
            }
        };
        binding.rcvMain.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rcvMain.setAdapter(mAdapter);

        binding.fvMain.setTypeCallBack(vo -> resetType2(vo));

    }

    private void resetType2(FilterView.IBaseVo vo) {
        listType2.clear();
        switch (vo.getShowId()) {
            case "all":
                listType2.addAll(listType);
                break;

            case "cp":
                for (FilterView.IBaseVo t : listType) {
                    if (!t.getShowId().equals("all") && !t.getShowId().equals("cp")) {
                        listType2.add(t);
                    }
                }
                break;

            default:
                listType2.add(new OrderTypeVo("cp", getString(R.string.txt_venue_cp)));
                break;
        }

        binding.fvMain.setType2(listType2);

    }

    @Override
    public void initData() {
        super.initData();

        listType.add(new OrderTypeVo("all", getString(R.string.txt_venue_all)));
        listType.add(new OrderTypeVo("cp", getString(R.string.txt_venue_cp)));
        listType.add(new OrderTypeVo("pt", getString(R.string.txt_venue_pt)));
        listType.add(new OrderTypeVo("bbin", getString(R.string.txt_venue_bbin)));
        listType.add(new OrderTypeVo("ag", getString(R.string.txt_venue_ag)));
        listType.add(new OrderTypeVo("obgqp", getString(R.string.txt_venue_obgqp)));
        listType.add(new OrderTypeVo("wali", getString(R.string.txt_venue_wali)));
        listType.add(new OrderTypeVo("obgdj", getString(R.string.txt_venue_obgdj)));
        listType.add(new OrderTypeVo("yy", getString(R.string.txt_venue_yy)));

        listType2.addAll(listType);

        listStatus.add(new StatusVo("all", getString(R.string.txt_all)));
        listStatus.add(new StatusVo("success", getString(R.string.txt_succ)));
        listStatus.add(new StatusVo("fail", getString(R.string.txt_fail)));
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
        viewModel.liveDataThirdTransferVo.observe(this, vo -> {
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
            mAdapter.addAll(vo.allTransferInfoList);
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
        typeId = binding.fvMain.getTypeId("all");
        typeId2 = binding.fvMain.getTypeId2("all");
        status = binding.fvMain.getStatusId("all");

        HashMap<String, String> map = new HashMap<>();
        map.put("controller", "report");
        map.put("action", "fundreport");
        map.put("isgetdata", "1");
        map.put("starttime", starttime);
        map.put("endtime", endtime);

        map.put("out_money", typeId);
        map.put("in_money", typeId2);
        map.put("status", status);
        map.put("p", "" + page);
        map.put("page_size", "10");
        map.put("client", "m");

        CfLog.i(map.toString());
        viewModel.getThirdTransferReport(map);

    }

}