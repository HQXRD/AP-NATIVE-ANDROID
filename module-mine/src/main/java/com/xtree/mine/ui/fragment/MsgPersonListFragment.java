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
import com.xtree.mine.databinding.FragmentMsgPersonListBinding;
import com.xtree.mine.ui.viewmodel.MsgViewModel;
import com.xtree.mine.ui.viewmodel.factory.AppViewModelFactory;
import com.xtree.mine.vo.MsgPersonVo;

import java.util.ArrayList;
import java.util.List;

import me.xtree.mvvmhabit.base.BaseFragment;

@Route(path = RouterFragmentPath.Mine.PAGER_MSG_PERSON_LIST)
public class MsgPersonListFragment extends BaseFragment<FragmentMsgPersonListBinding, MsgViewModel> {
    MsgPersonListAdapter mMsgPersonListAdapter;
    ListMsgInfoDialog ppw = null; // 底部弹窗 (选择**菜单)
    int curPage = 0;
    int count = 0; // 资料总条数
    List<MsgPersonVo> msgPersonVoList = new ArrayList<>();

    @Override
    public void initView() {
        mMsgPersonListAdapter = new MsgPersonListAdapter(getContext(), vo -> viewModel.getMessagePerson(changeIdType(vo.id)));
        binding.rvMsgPersonList.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvMsgPersonList.setAdapter(mMsgPersonListAdapter);

        binding.refreshLayout.setOnRefreshListener(refreshLayout -> {
            curPage = 1;
            msgPersonVoList.clear();
            viewModel.getMessagePersonList(String.valueOf(curPage));
        });

        binding.refreshLayout.setOnLoadMoreListener(refreshLayout -> {
            CfLog.i("****** load more");
            if (curPage == 0) {
                curPage = 1;
            }
            viewModel.getMessagePersonList(String.valueOf(++curPage));
        });
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel.readCache();
        viewModel.getMessagePersonList(String.valueOf(1));
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_msg_person__list;
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
        viewModel.liveDataMsgPerson.observe(this, list -> {
            binding.refreshLayout.finishRefresh();
            binding.refreshLayout.finishLoadMore();
            if (msgPersonVoList.size() != 0 && curPage == 0) {
                CfLog.d(count + " " + msgPersonVoList.size());
                if (count > msgPersonVoList.size()) {
                    binding.refreshLayout.setEnableLoadMore(true);
                } else {
                    binding.refreshLayout.setEnableLoadMore(false);
                }
                return;
            }
            msgPersonVoList.addAll(list);
            mMsgPersonListAdapter.addAll(msgPersonVoList);
            CfLog.d("msgPersonVoList.size() : " + msgPersonVoList.size());
            if (msgPersonVoList.size() == 0) {
                binding.refreshLayout.setEnableLoadMore(false);
                binding.tvwNoData.setVisibility(View.VISIBLE);
                mMsgPersonListAdapter.clear();
                return;
            }

            CfLog.d(count + " " + msgPersonVoList.size());
            if (count > msgPersonVoList.size()) {
                binding.refreshLayout.setEnableLoadMore(true);
            } else {
                binding.refreshLayout.setEnableLoadMore(false);
            }

            if (curPage == 1) {
                mMsgPersonListAdapter.clear();
                mMsgPersonListAdapter.addAll(list);
            }

            binding.tvwNoData.setVisibility(View.GONE);
        });

        viewModel.liveDataMsgPersonCount.observe(this, count -> this.count = count);

        viewModel.liveDataMsgPersonInfo.observe(this, vo -> {
            ppw = (ListMsgInfoDialog) new XPopup.Builder(getContext()).asCustom(new ListMsgInfoDialog(getContext(), null, vo, 80));
            ppw.show();
        });
    }

    public String changeIdType(String id) {
        double number = Double.parseDouble(id);
        return String.valueOf((int) number);
    }
}