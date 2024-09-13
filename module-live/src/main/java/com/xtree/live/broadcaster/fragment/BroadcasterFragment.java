package com.xtree.live.broadcaster.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.xtree.live.BR;
import com.xtree.live.R;
import com.xtree.live.databinding.FragmentLiveBroadcasterBinding;
import com.xtree.live.ui.main.viewmodel.LiveViewModel;

import me.xtree.mvvmhabit.base.BaseFragment;

/**
 * 主播列表
 */
public class BroadcasterFragment extends BaseFragment <FragmentLiveBroadcasterBinding, LiveViewModel>{
    private FragmentLiveBroadcasterBinding binding ;
    @Override
    public void initView() {

    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return  R.layout.fragment_live_broadcaster;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }
}
