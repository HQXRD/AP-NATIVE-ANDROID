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
import com.xtree.base.utils.ClickUtil;
import com.xtree.base.widget.LoadingDialog;
import com.xtree.mine.BR;
import com.xtree.mine.R;
import com.xtree.mine.databinding.FragmentEasterReportBinding;
import com.xtree.mine.ui.viewmodel.MineViewModel;
import com.xtree.mine.ui.viewmodel.factory.AppViewModelFactory;

import java.util.HashMap;

import me.xtree.mvvmhabit.base.BaseFragment;

@Route(path = RouterFragmentPath.Mine.PAGER_EASTER_REPORT)
public class EasterReportFragment extends BaseFragment<FragmentEasterReportBinding, MineViewModel> {
    EasterReportAdapter adapter;

    private String starttime;
    private String endtime;
    private String typeId = "0";
    private String status = "0";
    private String userName = "";

    @Override
    public void initView() {
        binding.ivwBack.setOnClickListener(v -> getActivity().finish());
        binding.fvMain.setVisibility(View.GONE, View.GONE, View.GONE, View.GONE, View.VISIBLE);
        binding.fvMain.setTopTotal("0", "0.0000");
        binding.fvMain.setDefEdit("代理名称", "", "请输入代理名称");

        binding.fvMain.setQueryListener(v -> {
            if (ClickUtil.isFastClick()) {
                return;
            }
            LoadingDialog.show(getActivity());
            adapter.clear();
            requestData();
        });


        adapter = new EasterReportAdapter(getContext(), member -> {
            binding.fvMain.setEdit(member);
            adapter.clear();
            requestData();
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
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getActivity().getApplication());
        return new ViewModelProvider(this, factory).get(MineViewModel.class);
    }

    @Override
    public void initViewObservable() {
        viewModel.liveDataEasterReport.observe(this, vo -> {
            binding.fvMain.setTopTotal("0", vo.total);

            if (!vo.data.isEmpty()) {
                binding.tvwNoData.setVisibility(View.GONE);

                adapter.addAll(vo.data);
            } else {
                binding.tvwNoData.setVisibility(View.VISIBLE);
            }
        });
    }

    private void requestData() {
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
        map.put("lotteryid", "");
        map.put("currencytype", "0");
        map.put("status", "1"); // ""-全部 0-未发放, 1-已发放, 2-已测回
        map.put("page", "1");

        viewModel.getEasterReport(map);
    }
}

