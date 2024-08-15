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
import com.xtree.mine.BR;
import com.xtree.mine.R;
import com.xtree.mine.databinding.FragmentTeamActivityBinding;
import com.xtree.mine.ui.viewmodel.ReportViewModel;
import com.xtree.mine.ui.viewmodel.factory.AppViewModelFactory;
import com.xtree.mine.vo.TeamActivityReportVo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.xtree.mvvmhabit.base.BaseFragment;

/**
 *团队活跃人数活动报表
 */
@Route(path = RouterFragmentPath.Mine.PAGER_TEAM_ACTIVITY)
public class TeamActivityFragment extends BaseFragment<FragmentTeamActivityBinding, ReportViewModel> {
    TeamActivityListAdapter teamActivityListAdapter;
    ListMsgInfoDialog ppw = null; // 底部弹窗 (选择**菜单)
    int curPage = 0;
    int count = 0; // 资料总条数
    List<TeamActivityReportVo> msgVoList = new ArrayList<>();

    @Override
    public void initView() {

        teamActivityListAdapter = new TeamActivityListAdapter(getActivity() );
        teamActivityListAdapter.clear();
        binding.ivwBack.setOnClickListener(v -> {
            getActivity().finish();
        });


      /*  binding.refreshLayout.setOnRefreshListener(refreshLayout -> {
            curPage = 1;
            msgVoList.clear();
            viewModel.getMessageList(String.valueOf(curPage));
        });

        binding.refreshLayout.setOnLoadMoreListener(refreshLayout -> {
            CfLog.i("****** load more");
            if (curPage == 0) {
                curPage = 1;
            }
            viewModel.getMessageList(String.valueOf(++curPage));
        });*/
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        HashMap map = new HashMap();
        map.put("aid" , "258");
        viewModel.getTeamActivityReport(map);
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_team_activity;
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
        viewModel.liveDataTeamActivity.observe(this, list -> {
            binding.refreshLayout.finishRefresh();
            binding.refreshLayout.finishLoadMore();
            if (msgVoList.size() != 0 && curPage == 0) {
                CfLog.d(count + " " + msgVoList.size());
                if (count > msgVoList.size()) {
                    binding.refreshLayout.setEnableLoadMore(true);
                } else {
                    binding.refreshLayout.setEnableLoadMore(false);
                }
                return;
            }
            msgVoList.addAll(list);

            teamActivityListAdapter.addAll(msgVoList);
            teamActivityListAdapter.notifyDataSetChanged();
            binding.rcvMain.setLayoutManager(new LinearLayoutManager(getContext()));
            binding.rcvMain.setAdapter(teamActivityListAdapter);

            CfLog.d("msgVoList.size() : " + msgVoList.size());
            if (msgVoList.size() == 0) {
                binding.refreshLayout.setEnableLoadMore(false);
                binding.tvwNoData.setVisibility(View.VISIBLE);
                teamActivityListAdapter.clear();
                return;
            }

            CfLog.d(count + " " + msgVoList.size());
            if (count > msgVoList.size()) {
                binding.refreshLayout.setEnableLoadMore(true);
            } else {
                binding.refreshLayout.setEnableLoadMore(false);
            }

            if (curPage == 1) {
                teamActivityListAdapter.clear();
                teamActivityListAdapter.addAll(list);
            }
            binding.tvwNoData.setVisibility(View.GONE);
        });


    }

    public String changeIdType(String id) {
        double number = Double.parseDouble(id);
        return String.valueOf((int) number);
    }
}