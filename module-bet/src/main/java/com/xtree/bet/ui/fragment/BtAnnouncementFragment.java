package com.xtree.bet.ui.fragment;

import static com.xtree.base.utils.BtDomainUtil.KEY_PLATFORM;
import static com.xtree.base.utils.BtDomainUtil.PLATFORM_PM;
import static com.xtree.base.utils.BtDomainUtil.PLATFORM_PMXC;

import android.app.Application;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.base.utils.AppUtil;
import com.xtree.bet.BR;
import com.xtree.bet.R;
import com.xtree.bet.databinding.BtFragmentAnnouncementBinding;
import com.xtree.bet.ui.adapter.BtAnnouncementAdapter;
import com.xtree.bet.ui.viewmodel.TemplateMainViewModel;
import com.xtree.bet.ui.viewmodel.factory.AppViewModelFactory;
import com.xtree.bet.ui.viewmodel.factory.PMAppViewModelFactory;
import com.xtree.bet.ui.viewmodel.fb.FBMainViewModel;
import com.xtree.bet.ui.viewmodel.pm.PMMainViewModel;

import me.xtree.mvvmhabit.base.BaseFragment;
import me.xtree.mvvmhabit.utils.SPUtils;
import me.xtree.mvvmhabit.utils.Utils;

/**
 * 赛事公告页面
 */
@Route(path = RouterFragmentPath.Bet.PAGER_BET_ANNOUNCEMENT)
public class BtAnnouncementFragment extends BaseFragment<BtFragmentAnnouncementBinding, TemplateMainViewModel> implements View.OnClickListener {
    private String mPlatform = SPUtils.getInstance().getString(KEY_PLATFORM);

    public static BtAnnouncementFragment getInstance() {
        BtAnnouncementFragment btAnnouncementFragment = new BtAnnouncementFragment();
        return btAnnouncementFragment;
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.bt_fragment_announcement;
    }

    @Override
    public void initView() {
        binding.ivCs.setOnClickListener(this);
        binding.ivMsg.setOnClickListener(this);
        binding.ivwBack.setOnClickListener(this);

    }

    @Override
    public void initData() {
        viewModel.getAnnouncement();
    }

    @Override
    public void initViewObservable() {
        viewModel.announcementData.observe(getViewLifecycleOwner(), list -> {
            binding.rvAnnouncement.setLayoutManager(new LinearLayoutManager(this.getContext()));
            binding.rvAnnouncement.setAdapter(new BtAnnouncementAdapter(list));
        });
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public TemplateMainViewModel initViewModel() {
        if (!TextUtils.equals(mPlatform, PLATFORM_PM) && !TextUtils.equals(mPlatform, PLATFORM_PMXC)) {
            AppViewModelFactory factory = AppViewModelFactory.getInstance((Application) Utils.getContext());
            return new ViewModelProvider(this, factory).get(FBMainViewModel.class);
        } else {
            PMAppViewModelFactory factory = PMAppViewModelFactory.getInstance((Application) Utils.getContext());
            return new ViewModelProvider(this, factory).get(PMMainViewModel.class);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.iv_cs) {
            AppUtil.goCustomerService(getContext());
        } else if (id == R.id.iv_msg) {
            startContainerFragment(RouterFragmentPath.Mine.PAGER_MSG);
        } else if (id == R.id.ivw_back) {
            viewModel.finish();
        }
    }
}
