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
import com.lxj.xpopup.core.BasePopupView;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.base.utils.CfLog;
import com.xtree.base.widget.LoadingDialog;
import com.xtree.base.widget.MsgDialog;
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
    ListMsgInfoDialog mListMsgInfoDialog = null; // 底部弹窗 (选择**菜单)
    BasePopupView ppw = null; // 底部弹窗
    int curPage = 0;
    int count = 0; // 资料总条数
    boolean refresh = true;
    List<MsgPersonVo> msgPersonVoList = new ArrayList<>();

    @Override
    public void initView() {
        mMsgPersonListAdapter = new MsgPersonListAdapter(getContext(), new MsgPersonListAdapter.ICallBack() {
            @Override
            public void onClick(MsgPersonVo vo) {
                viewModel.getMessagePerson(changeIdType(vo.id));
            }

            @Override
            public void onEnable(Boolean enabled) {
                binding.chbSelectAll.setChecked(enabled);
            }
        });
        binding.rvMsgPersonList.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvMsgPersonList.setAdapter(mMsgPersonListAdapter);

        binding.refreshLayout.setOnRefreshListener(refreshLayout -> {
            binding.chbSelectAll.setChecked(false);
            curPage = 1;
            msgPersonVoList.clear();
            viewModel.getMessagePersonList(String.valueOf(curPage));
        });

        binding.refreshLayout.setOnLoadMoreListener(refreshLayout -> {
            CfLog.i("****** load more");
            binding.chbSelectAll.setChecked(false);
            refresh = true;
            if (curPage == 0) {
                curPage = 1;
            }
            viewModel.getMessagePersonList(String.valueOf(++curPage));
        });

        binding.btnSelectAll.setOnClickListener(v -> {
            binding.chbSelectAll.setChecked(!binding.chbSelectAll.isChecked());
            for (MsgPersonVo msgPersonVo : msgPersonVoList) {
                msgPersonVo.selected_delete = binding.chbSelectAll.isChecked();
            }
            mMsgPersonListAdapter.clear();
            mMsgPersonListAdapter.addAll(msgPersonVoList);
            mMsgPersonListAdapter.notifyDataSetChanged();
        });

        binding.btnSelectedDelete.setOnClickListener(v -> {
            List<String> strings = new ArrayList<>();
            for (MsgPersonVo id : mMsgPersonListAdapter.getData()) {
                if (id.selected_delete) {
                    strings.add(id.id);
                }
            }
            String title = getString(R.string.txt_delete_msg_title);
            String msg = getString(R.string.txt_delete_part_msg);
            String txtRight = getString(R.string.txt_confirm_deltet);
            ppw = new XPopup.Builder(getContext()).asCustom(new MsgDialog(getContext(), title, msg, "", txtRight, new MsgDialog.ICallBack() {
                @Override
                public void onClickLeft() {
                    ppw.dismiss();
                }

                @Override
                public void onClickRight() {
                    LoadingDialog.show(getActivity());
                    viewModel.deletePartPersonInfo(strings);
                    ppw.dismiss();
                }
            }));
            ppw.show();
        });

        binding.btnAllDelete.setOnClickListener(v -> {
            String title = getString(R.string.txt_delete_msg_title);
            String msg = getString(R.string.txt_delete_all_msg);
            String txtRight = getString(R.string.txt_confirm_deltet);
            ppw = new XPopup.Builder(getContext()).asCustom(new MsgDialog(getContext(), title, msg, "", txtRight, new MsgDialog.ICallBack() {
                @Override
                public void onClickLeft() {
                    ppw.dismiss();
                }

                @Override
                public void onClickRight() {
                    LoadingDialog.show(getActivity());
                    viewModel.deleteAllPersonInfo();
                    ppw.dismiss();
                }
            }));
            ppw.show();
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
        return R.layout.fragment_msg_person_list;
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
            CfLog.d("msgPersonVoList.size() : " + msgPersonVoList.size() + " curPage : " + curPage);
            CfLog.d("count : " + count);
            if (msgPersonVoList.size() != 0 && curPage == 0) {
                if (count > msgPersonVoList.size()) {
                    binding.refreshLayout.setEnableLoadMore(true);
                } else {
                    binding.refreshLayout.setEnableLoadMore(false);
                }
                return;
            }

            msgPersonVoList.addAll(list);
            mMsgPersonListAdapter.addAll(list);

            if (list.size() == 0) {
                binding.refreshLayout.setEnableLoadMore(false);
                binding.tvwNoData.setVisibility(View.VISIBLE);
                binding.clDelete.setVisibility(View.GONE);
                mMsgPersonListAdapter.clear();
                return;
            }

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
            binding.clDelete.setVisibility(View.VISIBLE);
            mMsgPersonListAdapter.notifyDataSetChanged();
        });

        viewModel.liveDataMsgPersonCount.observe(this, count -> this.count = count);

        viewModel.liveDataMsgPersonInfo.observe(this, vo -> {
            mListMsgInfoDialog = (ListMsgInfoDialog) new XPopup.Builder(getContext()).asCustom(new ListMsgInfoDialog(getContext(), null, vo, 80));
            mListMsgInfoDialog.show();
        });

        viewModel.liveDataDeleteAll.observe(this, flag -> {
            curPage = 1;
            viewModel.getMessagePersonList(String.valueOf(1));
        });

        viewModel.liveDataDeletePart.observe(this, flag -> {
            curPage = 1;
            viewModel.getMessagePersonList(String.valueOf(1));
        });
    }

    public String changeIdType(String id) {
        double number = Double.parseDouble(id);
        return String.valueOf((int) number);
    }
}