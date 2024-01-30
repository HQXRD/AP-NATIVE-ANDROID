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
import com.xtree.mine.BR;
import com.xtree.mine.R;
import com.xtree.mine.databinding.FragmentReportBinding;
import com.xtree.mine.databinding.ItemReportFeedbackBinding;
import com.xtree.mine.databinding.ItemReportReachargeBinding;
import com.xtree.mine.databinding.ItemReportWithdrawBinding;
import com.xtree.mine.ui.viewmodel.ReportViewModel;
import com.xtree.mine.ui.viewmodel.factory.AppViewModelFactory;
import com.xtree.mine.vo.FeedbackOrderVo;
import com.xtree.mine.vo.OrderTypeVo;
import com.xtree.mine.vo.RechargeOrderVo;
import com.xtree.mine.vo.WithdrawOrderVo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.xtree.mvvmhabit.base.BaseFragment;

/**
 * 充提记录 (充值,提现,未到账反馈)
 */
@Route(path = RouterFragmentPath.Mine.PAGER_RECHARGE_WITHDRAW)
public class RechargeWithdrawFragment extends BaseFragment<FragmentReportBinding, ReportViewModel> {
    private final String TYPE_RECHARGE = "recharge";
    private final String TYPE_WITHDRAW = "withdraw";
    private final String TYPE_FEEDBACK = "feedback";
    CachedAutoRefreshAdapter<RechargeOrderVo> mAdapter; // 主列表
    CachedAutoRefreshAdapter<WithdrawOrderVo> mAdapter2; // 主列表
    CachedAutoRefreshAdapter<FeedbackOrderVo> mAdapter3; // 主列表

    int curPage = 0;
    //RechargeOrderVo mReportVo;

    List<FilterView.IBaseVo> listType = new ArrayList<>();

    private String starttime;
    private String endtime;
    private String typeId = "0";

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.tvwTitle.setText(R.string.txt_report_recharge); //
        binding.fvMain.setData(listType, null, null);
        binding.fvMain.setVisibility(View.VISIBLE, View.GONE, View.GONE);
        binding.fvMain.setTypeTitle(getString(R.string.txt_query_type), "", "");
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

        mAdapter = new CachedAutoRefreshAdapter<RechargeOrderVo>() {

            @NonNull
            @Override
            public CacheViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                CacheViewHolder holder = new CacheViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_report_reacharge, parent, false));
                return holder;
            }

            @Override
            public void onBindViewHolder(@NonNull CacheViewHolder holder, int position) {
                ItemReportReachargeBinding binding2 = ItemReportReachargeBinding.bind(holder.itemView);
                RechargeOrderVo vo = get(position);
                int status;
                if ("0".equals(vo.status)) {
                    status = R.string.txt_to_process;
                } else if ("2".equals(vo.status)) {
                    status = R.string.txt_succ;
                } else if ("4".equals(vo.status)) {
                    status = R.string.txt_expired;
                } else {
                    status = R.string.txt_to_process;
                }

                String amount = vo.amount == null ? "--" : vo.amount;
                String fee = vo.fee == null ? "--" : vo.fee;
                //String notes = vo.fee == null ? "--" : vo.fee;

                binding2.tvwId.setText(vo.id);
                binding2.tvwPayportNickname.setText(vo.payport_nickname);
                binding2.tvwStatus.setText(status);
                binding2.tvwMoney.setText(vo.money);
                binding2.tvwAmount.setText(amount);
                binding2.tvwFee.setText(fee);
                //binding2.tvwNotes.setText(notes);
                binding2.tvwCreated.setText(vo.created);
                binding2.tvwModified.setText(vo.modified);

            }
        };
        mAdapter2 = new CachedAutoRefreshAdapter<WithdrawOrderVo>() {

            @NonNull
            @Override
            public CacheViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                CacheViewHolder holder = new CacheViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_report_withdraw, parent, false));
                return holder;
            }

            @Override
            public void onBindViewHolder(@NonNull CacheViewHolder holder, int position) {
                ItemReportWithdrawBinding binding2 = ItemReportWithdrawBinding.bind(holder.itemView);
                WithdrawOrderVo vo = get(position);

                int status = "2".equals(vo.status) ? R.string.txt_succ : R.string.txt_fail;
                String fee = vo.fee > 0 ? String.valueOf(vo.fee) : "--";
                binding2.tvwEntry.setText(vo.entry);
                binding2.tvwBankName.setText(vo.bankname);
                binding2.tvwStatus.setText(status);
                binding2.tvwAmount.setText(vo.amount);
                binding2.tvwFee.setText(fee);
                binding2.tvwAcceptTime.setText(vo.accepttime);
                binding2.tvwFinishTime.setText(vo.finishtime);

            }
        };

        mAdapter3 = new CachedAutoRefreshAdapter<FeedbackOrderVo>() {

            @NonNull
            @Override
            public CacheViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                CacheViewHolder holder = new CacheViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_report_feedback, parent, false));
                return holder;
            }

            @Override
            public void onBindViewHolder(@NonNull CacheViewHolder holder, int position) {
                ItemReportFeedbackBinding binding2 = ItemReportFeedbackBinding.bind(holder.itemView);
                FeedbackOrderVo vo = get(position);
                binding2.tvwOrderStatusText.setText(vo.order_status_text);
                binding2.tvwThirdOrderId.setText(vo.third_orderid);
                binding2.tvwUserpayAmount.setText(vo.userpay_amount);
                binding2.tvwUserpayTime.setText(vo.userpay_time);
                binding2.tvwAddTime.setText(vo.add_time);
                binding2.tvwDetail.setOnClickListener(v -> {
                    CfLog.i("****** ");
                    Bundle bundle = new Bundle();
                    bundle.putString("id", vo.id);
                    startContainerFragment(RouterFragmentPath.Mine.PAGER_RECHARGE_FEEDBACK_DETAIL, bundle);
                });
            }
        };

        binding.rcvMain.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rcvMain.setAdapter(mAdapter);

        binding.fvMain.setTypeCallBack(vo -> resetList(vo));

    }

    private void resetList(FilterView.IBaseVo vo) {
        CfLog.i(vo.toString());
        curPage = 0;

        switch (vo.getShowId()) {
            case TYPE_WITHDRAW:
                binding.rcvMain.setAdapter(mAdapter2);
                break;
            case TYPE_FEEDBACK:
                binding.rcvMain.setAdapter(mAdapter3);
                break;
            case TYPE_RECHARGE:
            default:
                binding.rcvMain.setAdapter(mAdapter);
                break;
        }

    }

    @Override
    public void initData() {
        super.initData();

        listType.add(new OrderTypeVo(TYPE_RECHARGE, getString(R.string.txt_record_recharge)));
        listType.add(new OrderTypeVo(TYPE_WITHDRAW, getString(R.string.txt_record_withdraw)));
        listType.add(new OrderTypeVo(TYPE_FEEDBACK, getString(R.string.txt_record_feedback)));
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
        viewModel.liveDataRechargeReport.observe(this, vo -> {
            CfLog.i("******");
            binding.refreshLayout.finishRefresh();
            binding.refreshLayout.finishLoadMore();
            if (vo == null) {
                binding.refreshLayout.setEnableLoadMore(false);
                return;
            }

            curPage = vo.pages.p;
            int total_page = vo.pages.total_page.equals("false") ? 0 : Integer.parseInt(vo.pages.total_page);
            if (vo.pages.p < total_page) {
                binding.refreshLayout.setEnableLoadMore(true);
            } else {
                binding.refreshLayout.setEnableLoadMore(false);
            }

            if (curPage == 1) {
                mAdapter.clear();
            }
            mAdapter.addAll(vo.result);
            if (mAdapter.isEmpty()) {
                binding.tvwNoData.setVisibility(View.VISIBLE);
            } else {
                binding.tvwNoData.setVisibility(View.GONE);
            }
        });

        viewModel.liveDataWithdrawReport.observe(this, vo -> {
            CfLog.i("******");
            binding.refreshLayout.finishRefresh();
            binding.refreshLayout.finishLoadMore();
            if (vo == null) {
                binding.refreshLayout.setEnableLoadMore(false);
                return;
            }

            curPage = vo.mobile_page.p;
            int total_page = vo.mobile_page.total_page.equals("false") ? 0 : Integer.parseInt(vo.mobile_page.total_page);
            if (vo.mobile_page.p < total_page) {
                binding.refreshLayout.setEnableLoadMore(true);
            } else {
                binding.refreshLayout.setEnableLoadMore(false);
            }

            if (curPage == 1) {
                mAdapter2.clear();
            }
            mAdapter2.addAll(vo.aProject);
            if (mAdapter2.isEmpty()) {
                binding.tvwNoData.setVisibility(View.VISIBLE);
            } else {
                binding.tvwNoData.setVisibility(View.GONE);
            }
        });

        viewModel.liveDataFeedback.observe(this, vo -> {
            CfLog.i("******");
            binding.refreshLayout.finishRefresh();
            binding.refreshLayout.finishLoadMore();
            if (vo == null) {
                binding.refreshLayout.setEnableLoadMore(false);
                return;
            }

            curPage = vo.nowPage;
            if (vo.list.size() >= vo.showRows) {
                binding.refreshLayout.setEnableLoadMore(true);
            } else {
                binding.refreshLayout.setEnableLoadMore(false);
            }

            if (curPage == 1) {
                mAdapter3.clear();
            }
            mAdapter3.addAll(vo.list);
            if (mAdapter3.isEmpty()) {
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

        HashMap<String, String> map = new HashMap<>();
        map.put("starttime", starttime);
        map.put("endtime", endtime);
        map.put("p", "" + page);
        map.put("pn", "10");
        map.put("client", "m");

        CfLog.i(map.toString());
        if (TYPE_RECHARGE.equals(typeId)) {
            viewModel.getRechargeReport(map);
        } else if (TYPE_WITHDRAW.equals(typeId)) {
            viewModel.getWithdrawReport(map);
        } else if (TYPE_FEEDBACK.equals(typeId)) {
            map.remove("pn");
            map.put("starttime", starttime.substring(0, 16));
            map.put("endtime", endtime.substring(0, 16));
            map.put("page_size", "10");
            CfLog.i(map.toString());
            viewModel.getFeedbackReport(map);
        } else {
            viewModel.getRechargeReport(map);
        }
    }

}