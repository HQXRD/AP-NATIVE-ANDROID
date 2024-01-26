package com.xtree.mine.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.lxj.xpopup.XPopup;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.base.utils.CfLog;
import com.xtree.mine.BR;
import com.xtree.mine.R;
import com.xtree.mine.databinding.FragmentBindMsgBinding;
import com.xtree.mine.ui.viewmodel.MsgViewModel;
import com.xtree.mine.ui.viewmodel.factory.AppViewModelFactory;
import com.xtree.mine.vo.MsgVo;

import java.util.ArrayList;
import java.util.List;

import me.xtree.mvvmhabit.base.BaseFragment;

@Route(path = RouterFragmentPath.Mine.PAGER_MSG_LIST)
public class BindMsgFragment extends BaseFragment<FragmentBindMsgBinding, MsgViewModel> {
    MsgListAdapter mMsgListAdapter;
    ListMsgInfoDialog ppw = null; // 底部弹窗 (选择**菜单)
    int curPage = 0;
    int count = 0; // 资料总条数
    List<MsgVo> msgVoList = new ArrayList<>();

    @Override
    public void initView() {
        mMsgListAdapter = new MsgListAdapter(getContext(), vo -> viewModel.getMessage(changeIdType(vo.id)));
        binding.rvMsgList.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvMsgList.setAdapter(mMsgListAdapter);

        binding.refreshLayout.setOnRefreshListener(refreshLayout -> {
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
        });
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel.readCache();
        viewModel.getMessageList(String.valueOf(1));
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_bind_msg;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public MsgViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getActivity().getApplication());
        return new ViewModelProvider(this, factory).get(MsgViewModel.class);
    }

    @Override
    public void initViewObservable() {
        viewModel.liveDataMsg.observe(this, list -> {
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
            mMsgListAdapter.addAll(msgVoList);
            CfLog.d("msgVoList.size() : " + msgVoList.size());
            if (msgVoList.size() == 0) {
                binding.refreshLayout.setEnableLoadMore(false);
                binding.tvwNoData.setVisibility(View.VISIBLE);
                mMsgListAdapter.clear();
                return;
            }

            CfLog.d(count + " " + msgVoList.size());
            if (count > msgVoList.size()) {
                binding.refreshLayout.setEnableLoadMore(true);
            } else {
                binding.refreshLayout.setEnableLoadMore(false);
            }

            if (curPage == 1) {
                mMsgListAdapter.clear();
                mMsgListAdapter.addAll(list);
            }
            binding.tvwNoData.setVisibility(View.GONE);
        });

        viewModel.liveDataMsgCount.observe(this, count -> this.count = count);

        viewModel.liveDataMsgInfo.observe(this, vo -> {
            ppw = (ListMsgInfoDialog) new XPopup.Builder(getContext()).asCustom(new ListMsgInfoDialog(getContext(), vo, null, 80));
            ppw.show();
        });
    }

    public String changeIdType(String id) {
        double number = Double.parseDouble(id);
        return String.valueOf((int) number);
    }
}