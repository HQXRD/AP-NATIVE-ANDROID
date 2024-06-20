package com.xtree.mine.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.xtree.base.global.Constant;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.base.utils.DomainUtil;
import com.xtree.base.widget.BrowserActivity;
import com.xtree.mine.BR;
import com.xtree.mine.R;
import com.xtree.mine.databinding.FragmentHelpCenterBinding;
import com.xtree.mine.ui.viewmodel.MineViewModel;
import com.xtree.mine.ui.viewmodel.factory.AppViewModelFactory;

import me.xtree.mvvmhabit.base.BaseFragment;

@Route(path = RouterFragmentPath.Mine.PAGER_INFO)
public class HelpCenterFragment extends BaseFragment<FragmentHelpCenterBinding, MineViewModel> {
    @Override
    public void initView() {
        binding.ivwBack.setOnClickListener(v -> getActivity().finish());
        binding.clLotteryInfo.setOnClickListener(v -> goWebView(getString(R.string.txt_lottery_info), Constant.URL_LOTTERY_INFO, true));
        binding.clQuestion.setOnClickListener(v -> startContainerFragment(RouterFragmentPath.Mine.PAGER_QUESTION));
        binding.clPlayIntro.setOnClickListener(v -> goWebView(getString(R.string.txt_play_intro), Constant.URL_PLAY_INTRO, true));
        binding.clLotteryTutorial.setOnClickListener(v -> goWebView(getString(R.string.txt_tutorial), Constant.URL_TUTORIAL, false));
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_help_center;
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

    private void goWebView(String title, String path, boolean isContainTitle) {
        String url = DomainUtil.getH5Domain2() + path;
        BrowserActivity.start(getContext(), title, url, isContainTitle);
    }
}
