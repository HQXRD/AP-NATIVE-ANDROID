package com.xtree.mine.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.base.utils.CfLog;
import com.xtree.base.utils.ClickUtil;
import com.xtree.base.widget.FilterView;
import com.xtree.base.widget.LoadingDialog;
import com.xtree.mine.BR;
import com.xtree.mine.R;
import com.xtree.mine.databinding.FragmentEasterReportBinding;
import com.xtree.mine.ui.viewmodel.MineViewModel;
import com.xtree.mine.ui.viewmodel.factory.AppViewModelFactory;
import com.xtree.mine.vo.LotteryItemVo;
import com.xtree.mine.vo.StatusVo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.xtree.mvvmhabit.base.BaseFragment;

@Route(path = RouterFragmentPath.Mine.PAGER_EASTER_REPORT)
public class EasterReportFragment extends BaseFragment<FragmentEasterReportBinding, MineViewModel> {
    List<FilterView.IBaseVo> listType = new ArrayList<>();
    List<FilterView.IBaseVo> listStatus = new ArrayList<>();
    int curPage = 1;
    EasterReportAdapter adapter;

    private String starttime;
    private String endtime;
    private String typeId = "0";
    private String status = "0";
    private String userName = "";

    @Override
    public void initView() {
        binding.ivwBack.setOnClickListener(v -> getActivity().finish());
        binding.fvMain.setVisibility(View.GONE, View.VISIBLE, View.GONE, View.VISIBLE, View.VISIBLE);
        binding.fvMain.setTopTotal("0", "0.0000");
        binding.fvMain.setDefEdit("用户名", "", "输入会员帐号");
        viewModel.getLottery();

        listType.add(new StatusVo(-1, getString(R.string.txt_lottery_all_kind)));

        listStatus.add(new StatusVo(-1, getString(R.string.txt_all_lottery)));
        listStatus.add(new StatusVo(0, getString(R.string.txt_not_give_lottery)));
        listStatus.add(new StatusVo(1, getString(R.string.txt_give_lottery)));
        listStatus.add(new StatusVo(2, getString(R.string.txt_cancel_lottery)));

        binding.fvMain.setData(listType, listStatus);

        binding.fvMain.setTypeTitle(getString(R.string.txt_lottery_kind), null, getString(R.string.txt_status));

        binding.refreshLayout.setEnableLoadMore(false);
        binding.refreshLayout.setEnableRefresh(false);

        binding.fvMain.setQueryListener(v -> {
            if (ClickUtil.isFastClick()) {
                return;
            }
            LoadingDialog.show(getActivity());
            adapter.clear();
            curPage = 1;
            requestData(1);
        });

        binding.refreshLayout.setOnRefreshListener(refreshLayout -> {
            adapter.clear();
            curPage = 1;
            requestData(1);
        });
        binding.refreshLayout.setOnLoadMoreListener(refreshLayout -> {
            CfLog.i("****** load more");
            requestData(++curPage);
        });

        adapter = new EasterReportAdapter(getContext(), member -> {
            binding.fvMain.setEdit(member);
            adapter.clear();
            curPage = 1;
            requestData(1);
        });

        binding.rcvMain.setAdapter(adapter);
        binding.rcvMain.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_easter_report;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public MineViewModel initViewModel() {
        //使用自定义的ViewModelFactory来创建ViewModel，如果不重写该方法，则默认会调用LoginViewModel(@NonNull Application application)构造方法
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getActivity().getApplication());
        return new ViewModelProvider(this, factory).get(MineViewModel.class);
    }

    @Override
    public void initViewObservable() {
        viewModel.liveDataLotteryAll.observe(this, vo -> {
            for (LotteryItemVo item : vo) {
                listType.add(new StatusVo(item.lotteryid, item.cnname));
            }
            binding.fvMain.setData(listType, listStatus);
        });

        viewModel.liveDataEasterReport.observe(this, vo -> {
            binding.refreshLayout.finishRefresh();
            binding.refreshLayout.finishLoadMore();
            binding.fvMain.setTopTotal(vo.mobile_page.recordsCount, vo.mobile_page.total_sum);

            if (!vo.list.isEmpty()) {
                binding.tvwNoData.setVisibility(View.GONE);

                if (vo.maxPage == curPage) {
                    binding.refreshLayout.setEnableLoadMore(false);
                    binding.refreshLayout.setEnableRefresh(true);
                } else {
                    binding.refreshLayout.setEnableLoadMore(true);
                    binding.refreshLayout.setEnableRefresh(true);
                }

                adapter.addAll(vo.list);
            } else {
                binding.tvwNoData.setVisibility(View.VISIBLE);
            }
        });
    }

    private void requestData(int page) {
        starttime = binding.fvMain.getStartDate();
        endtime = binding.fvMain.getEndDate();
        typeId = binding.fvMain.getTypeId("");
        status = binding.fvMain.getStatusId("");
        userName = binding.fvMain.getEdit("");

        if (typeId.equals("-1")) {
            typeId = "";
        }

        if (status.equals("-1")) {
            status = "";
        }

        HashMap<String, String> map = new HashMap<>();
        map.put("from", starttime);
        map.put("to", endtime);
        map.put("username", userName);
        map.put("lotteryid", typeId);
        map.put("currencytype", "0");
        map.put("status", status); // ""-全部 0-未发放, 1-已发放, 2-已测回
        map.put("page", "" + page);

        viewModel.getEasterReport(map);
    }
}

