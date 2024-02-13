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
import com.xtree.mine.databinding.ItemReportProfitLossBinding;
import com.xtree.mine.ui.viewmodel.ReportViewModel;
import com.xtree.mine.ui.viewmodel.factory.AppViewModelFactory;
import com.xtree.mine.vo.OrderTypeVo;
import com.xtree.mine.vo.ProfitLossReportVo;
import com.xtree.mine.vo.ProfitLossVo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.xtree.mvvmhabit.base.BaseFragment;

/**
 * 盈亏报表
 */
@Route(path = RouterFragmentPath.Mine.PAGER_PROFIT_LOSS)
public class ProfitLossFragment extends BaseFragment<FragmentReportBinding, ReportViewModel> {

    CachedAutoRefreshAdapter<ProfitLossVo> mAdapter; // 主列表
    int curPage = 0;
    ProfitLossReportVo mReportVo;

    List<FilterView.IBaseVo> listType = new ArrayList<>();

    private String starttime;
    private String endtime;
    private String typeId = "0";
    //private String status = "0";

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.tvwTitle.setText(R.string.txt_report_profit); //
        binding.fvMain.setData(listType);
        binding.fvMain.setVisibility(View.VISIBLE, View.GONE, View.GONE);
        binding.fvMain.setTypeTitle(getString(R.string.txt_bt_platform), null, null);
        LoadingDialog.show(getContext());
        requestType();
        //requestData(1);
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

        mAdapter = new CachedAutoRefreshAdapter<ProfitLossVo>() {

            @NonNull
            @Override
            public CacheViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                CacheViewHolder holder = new CacheViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_report_profit_loss, parent, false));
                return holder;
            }

            @Override
            public void onBindViewHolder(@NonNull CacheViewHolder holder, int position) {
                ItemReportProfitLossBinding binding2 = ItemReportProfitLossBinding.bind(holder.itemView);
                ProfitLossVo vo = get(position);

                binding2.tvwSbet.setText(vo.sbet);
                binding2.tvwSpirze.setText(vo.sprize);
                binding2.tvwSeffectiveBet.setText(vo.seffective_bet);
                binding2.tvwSprofitLoss.setText(vo.sprofit_loss);
                binding2.tvwWages.setText(vo.wages);
                binding2.tvwSactivePrize.setText(vo.sactive_prize);

                if (Double.parseDouble(vo.sprofit_loss) > 0) {
                    binding2.tvwSprofitLoss.setText("+" + vo.sprofit_loss);
                    binding2.tvwSprofitLoss.setSelected(true); // +
                } else {
                    binding2.tvwSprofitLoss.setSelected(false); // -
                }
            }
        };
        binding.rcvMain.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rcvMain.setAdapter(mAdapter);
    }

    @Override
    public void initData() {
        super.initData();

        listType.add(new OrderTypeVo(0, getString(R.string.txt_all)));

        //listStatus.add(new StatusVo(0, getString(R.string.txt_all)));
        //listStatus.add(new StatusVo(1, getString(R.string.txt_succ)));
        //listStatus.add(new StatusVo(2, getString(R.string.txt_fail)));
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
        viewModel.liveDataGameType.observe(this, list -> {
            //listType.add(new ThirdGameTypeVo("0", getString(R.string.txt_all)));
            listType.addAll(list);
            binding.fvMain.setData(listType);
        });

        viewModel.liveDataProfitLoss.observe(this, vo -> {
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
                //listType.add(new OrderTypeVo(0, getString(R.string.txt_all)));
                listType.addAll(vo.fromgameArr);

                binding.fvMain.reSetData(listType);
            }

            if (curPage == 1) {
                mAdapter.clear();
            }
            mAdapter.addAll(vo.aProfitLoss);
            if (mAdapter.isEmpty()) {
                binding.tvwNoData.setVisibility(View.VISIBLE);
            } else {
                binding.tvwNoData.setVisibility(View.GONE);
            }
        });

    }

    private void requestType() {
        viewModel.getThirdGameType();
    }

    private void requestData(int page) {
        CfLog.i();
        starttime = binding.fvMain.getStartTime();
        endtime = binding.fvMain.getEndTime();
        typeId = binding.fvMain.getTypeId("0");
        //status = binding.fvMain.getStatusId("0");

        HashMap<String, String> map = new HashMap<>();
        map.put("starttime", starttime);
        map.put("endtime", endtime);
        map.put("cid", typeId);
        //map.put("status", status);
        //map.put("p", "" + page);
        //map.put("pn", "20");
        map.put("client", "m");

        CfLog.i(map.toString());
        viewModel.getProfitLoss(map);
    }

}