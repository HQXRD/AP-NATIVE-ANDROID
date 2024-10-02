package com.xtree.live.ui.main.fragment;

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
import com.xtree.live.BR;
import com.xtree.live.R;
import com.xtree.live.data.factory.AppViewModelFactory;
import com.xtree.live.databinding.FragmentChatBinding;
import com.xtree.live.ui.main.viewmodel.LiveViewModel;

import java.util.ArrayList;

import me.xtree.mvvmhabit.base.BaseFragment;

@Route(path = RouterFragmentPath.Live.PAGER_LIVE_CHAT)
public class ChatFragment extends BaseFragment<FragmentChatBinding, LiveViewModel> {
    private ArrayList<Fragment> fragmentList = new ArrayList<>();
    private ArrayList<String> tabList = new ArrayList<>();
    private FragmentStateAdapter mAdapter;

    @Override
    public void initView() {

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

        Fragment bindMsgFragment = new Fragment();
        Fragment bindMsgPersonFragment = new Fragment();

        String txtSquare = getString(R.string.txt_live_chat_square);
        String txtBetting = getString(R.string.txt_live_chat_betting);
        String txtPrivate = getString(R.string.txt_live_chat_private);
        String txtMsgAssistant = getString(R.string.txt_live_chat_assistant);

        fragmentList.add(bindMsgFragment);
        fragmentList.add(bindMsgPersonFragment);
        fragmentList.add(bindMsgFragment);
        fragmentList.add(bindMsgPersonFragment);
        tabList.add(txtSquare);
        tabList.add(txtBetting);
        tabList.add(txtPrivate);
        tabList.add(txtMsgAssistant);

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_chat;
    }

    @Override
    public int initVariableId() {
        return BR.model;
    }

    @Override
    public LiveViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getActivity().getApplication());
        return new ViewModelProvider(this, factory).get(LiveViewModel.class);
    }
}
