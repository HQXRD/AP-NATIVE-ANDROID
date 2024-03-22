package com.xtree.mine.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.android.material.tabs.TabLayoutMediator;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.base.utils.AppUtil;
import com.xtree.mine.BR;
import com.xtree.mine.R;
import com.xtree.mine.databinding.FragmentMsgBinding;
import com.xtree.mine.ui.viewmodel.MsgViewModel;
import com.xtree.mine.ui.viewmodel.factory.AppViewModelFactory;

import java.util.ArrayList;

import me.xtree.mvvmhabit.base.BaseFragment;

@Route(path = RouterFragmentPath.Mine.PAGER_MSG)
public class MsgFragment extends BaseFragment<FragmentMsgBinding, MsgViewModel> {
    private ArrayList<Fragment> fragmentList = new ArrayList<>();
    private ArrayList<String> tabList = new ArrayList<>();
    private FragmentStateAdapter mAdapter;

    @Override
    public void initView() {

        binding.ivwBack.setOnClickListener(view -> getActivity().finish());

        binding.ivwCs.setOnClickListener(v -> AppUtil.goCustomerService(getContext()));

        mAdapter = new FragmentStateAdapter(getChildFragmentManager(), getLifecycle()) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                return fragmentList.get(position);
            }

            @Override
            public int getItemCount() {
                return fragmentList.size();
            }
        };

        binding.vpMain.setAdapter(mAdapter);
        binding.vpMain.setUserInputEnabled(true); // ViewPager2 左右滑动

        new TabLayoutMediator(binding.tblType, binding.vpMain, (tab, position) -> {
            tab.setText(tabList.get(position));
        }).attach();

        MsgListFragment bindMsgFragment = new MsgListFragment();
        MsgPersonListFragment bindMsgPersonFragment = new MsgPersonListFragment();
        MsgChatFragment bindMsgChatFragment = new MsgChatFragment();

        String txtMsg = getString(R.string.txt_msg_message);
        String txtMsgPerson = getString(R.string.txt_msg_message_person);
        String txtMsgChat = getString(R.string.txt_msg_message_talk);

        fragmentList.add(bindMsgFragment);
        fragmentList.add(bindMsgPersonFragment);
        fragmentList.add(bindMsgChatFragment);

        tabList.add(txtMsg);
        tabList.add(txtMsgPerson);
        tabList.add(txtMsgChat);

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_msg;
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
}