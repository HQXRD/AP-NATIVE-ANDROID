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
import com.lxj.xpopup.XPopup;
import com.xtree.base.adapter.CacheViewHolder;
import com.xtree.base.adapter.CachedAutoRefreshAdapter;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.base.utils.CfLog;
import com.xtree.base.widget.FilterView;
import com.xtree.mine.BR;
import com.xtree.mine.R;
import com.xtree.mine.databinding.FragmentReportBinding;
import com.xtree.mine.databinding.ItemReportBtBinding;
import com.xtree.mine.ui.activity.BtDetailDialog;
import com.xtree.mine.ui.viewmodel.ReportViewModel;
import com.xtree.mine.ui.viewmodel.factory.AppViewModelFactory;
import com.xtree.mine.vo.BtOrderVo;
import com.xtree.mine.vo.BtReportVo;
import com.xtree.mine.vo.StatusVo;
import com.xtree.mine.vo.BtPlatformVo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.xtree.mvvmhabit.base.BaseFragment;
import me.xtree.mvvmhabit.utils.SPUtils;

/**
 * 投注记录
 */
@Route(path = RouterFragmentPath.Mine.PAGER_BT_REPORT)
public class BtReportFragment extends BaseFragment<FragmentReportBinding, ReportViewModel> {

    CachedAutoRefreshAdapter<BtOrderVo> mAdapter; // 主列表
    int curPage = 0;
    BtReportVo mReportVo;

    List<FilterView.IBaseVo> listType = new ArrayList<>();
    List<FilterView.IBaseVo> listStatus = new ArrayList<>();

    private String starttime;
    private String endtime;
    private String typeId = "0";
    private String status = "0";
    private String userId = "";
    private String userName = "";

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.tvwTitle.setText(R.string.txt_report_bt); //
        binding.fvMain.setData(listType, listStatus);
        //binding.fvMain.setVisibility(View.VISIBLE, View.GONE, View.VISIBLE);
        binding.fvMain.setTypeTitle(getString(R.string.txt_bt_platform), null, getString(R.string.txt_status));
        binding.llValidBet.setVisibility(View.GONE);

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


        mAdapter = new CachedAutoRefreshAdapter<BtOrderVo>() {

            @NonNull
            @Override
            public CacheViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                CacheViewHolder holder = new CacheViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_report_bt, parent, false));
                return holder;
            }

            @Override
            public void onBindViewHolder(@NonNull CacheViewHolder holder, int position) {
                ItemReportBtBinding binding2 = ItemReportBtBinding.bind(holder.itemView);
                BtOrderVo vo = get(position);

                // T-和,L-输,W-胜, 其它-未结算
                if (vo.project_BetResult.equals("T")) {
                    binding2.tvwBtResult.setText(R.string.txt_rst_tie);
                    binding2.tvwBtResult.setActivated(true);
                    binding2.tvwSum.setActivated(true);
                } else if (vo.project_BetResult.equals("L")) {
                    binding2.tvwBtResult.setText(R.string.txt_rst_lose);
                    binding2.tvwBtResult.setSelected(false);
                    binding2.tvwSum.setSelected(false);
                } else if (vo.project_BetResult.equals("W")) {
                    binding2.tvwBtResult.setText(R.string.txt_rst_win);
                    binding2.tvwBtResult.setSelected(true);
                    binding2.tvwSum.setSelected(true);
                } else {
                    binding2.tvwBtResult.setText(R.string.txt_unsettle); // 未结算
                    binding2.tvwBtResult.setActivated(true);
                    binding2.tvwSum.setActivated(true);
                }

                String win = vo.project_win.equals("0") ? "--" : vo.project_win;

                binding2.tvwUsername.setText(vo.project_username);
                //binding2.tvwBtResult.setText(vo.project_BetResult); // T,L,W
                binding2.tvwBet.setText(vo.project_bet);
                binding2.tvwWin.setText(win); // --
                binding2.tvwSum.setText(vo.sum); // color
                binding2.tvwGameName.setText(vo.project_Game_name);
                binding2.tvwGameCode.setText(vo.project_Game_code);
                binding2.tvwGameDate.setText(vo.project_Game_date);

                binding2.tvwDetail.setOnClickListener(v -> {
                    CfLog.i("****** ");
                    BtDetailDialog dialog = BtDetailDialog.newInstance(getActivity(), getViewLifecycleOwner(), vo.project_Game_code, vo.p, getPlatformName(vo.p));
                    new XPopup.Builder(getContext()).asCustom(dialog).show();
                });

            }
        };
        binding.rcvMain.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rcvMain.setAdapter(mAdapter);
    }

    private String getPlatformName(String code) {
        for (FilterView.IBaseVo vo : listType) {
            if (vo.getShowId().equals(code)) {
                return vo.getShowName();
            }
        }

        return "--";
    }

    @Override
    public void initData() {
        super.initData();

        listType.add(new BtPlatformVo("", "", getString(R.string.txt_all)));

        listStatus.add(new StatusVo(0, getString(R.string.txt_all)));
        listStatus.add(new StatusVo(1, getString(R.string.txt_settled)));
        listStatus.add(new StatusVo(2, getString(R.string.txt_unsettle)));

        userId = SPUtils.getInstance().getString(SPKeyGlobal.USER_ID);
        userName = SPUtils.getInstance().getString(SPKeyGlobal.USER_NAME);
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
        viewModel.liveDataBtPlatform.observe(this, list -> {
            //listType.add(new BtPlatformVo("0", getString(R.string.txt_all)));
            listType.addAll(list);
            binding.fvMain.setData(listType);
        });

        viewModel.liveDataBtReport.observe(this, vo -> {
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
                listType.add(new BtPlatformVo("", "", getString(R.string.txt_all)));
                listType.addAll(vo.fromgameArr);

                binding.fvMain.reSetData(listType);
            }

            if (curPage == 1) {
                mAdapter.clear();
            }
            mAdapter.addAll(vo.aProject.list);
            if (mAdapter.isEmpty()) {
                binding.tvwNoData.setVisibility(View.VISIBLE);
            } else {
                binding.tvwNoData.setVisibility(View.GONE);
            }
        });

    }

    private void requestType() {
        viewModel.getBtPlatformType();
    }

    private void requestData(int page) {
        CfLog.i();
        starttime = binding.fvMain.getStartTime();
        endtime = binding.fvMain.getEndTime();
        typeId = binding.fvMain.getTypeId(""); //
        status = binding.fvMain.getStatusId("0");

        HashMap<String, String> map = new HashMap<>();
        map.put("isgetdata", "1");
        map.put("userid", userId); //
        map.put("startDate", starttime);
        map.put("endDate", endtime);
        map.put("platform", typeId); // PT
        map.put("bet_result", status); // 0-全部, 1-已结算, 2-未结算
        map.put("ischild", "0"); //
        map.put("p", "" + page);
        map.put("pn", "10");
        map.put("client", "m");

        CfLog.i(map.toString());
        viewModel.getBtReport(map);
    }

}