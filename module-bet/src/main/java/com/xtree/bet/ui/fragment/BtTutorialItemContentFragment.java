package com.xtree.bet.ui.fragment;

import android.app.Application;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.ViewModelProvider;

import com.xtree.bet.BR;
import com.xtree.bet.R;
import com.xtree.bet.databinding.BtFragmentTutorialBinding;
import com.xtree.bet.databinding.BtTutorialItemContentBinding;
import com.xtree.bet.ui.viewmodel.factory.AppViewModelFactory;
import com.xtree.bet.ui.viewmodel.fb.FBMainViewModel;

import me.xtree.mvvmhabit.base.BaseFragment;
import me.xtree.mvvmhabit.base.BaseViewModel;
import me.xtree.mvvmhabit.utils.Utils;

/**
 *
 */
public class BtTutorialItemContentFragment extends BaseFragment<BtTutorialItemContentBinding, FBMainViewModel> {
    public final static String KEY_SOUTCE_CONTENT_RES = "key_soutce_content_res";
    private int sourceRes;

    public static BtTutorialItemContentFragment getInstance(int sourceRes){
        BtTutorialItemContentFragment btTutorialItemContentFragment = new BtTutorialItemContentFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_SOUTCE_CONTENT_RES, sourceRes);
        btTutorialItemContentFragment.setArguments(bundle);
        return btTutorialItemContentFragment;
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.bt_tutorial_item_content;
    }

    @Override
    public void initParam() {
        sourceRes = getArguments().getInt(KEY_SOUTCE_CONTENT_RES);
    }

    @Override
    public void initView() {
        binding.ivContent.setBackgroundResource(sourceRes);
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public FBMainViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance((Application) Utils.getContext());
        return new ViewModelProvider(this, factory).get(FBMainViewModel.class);
    }
}
