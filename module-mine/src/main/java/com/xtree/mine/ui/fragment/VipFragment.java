package com.xtree.mine.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.lxj.xpopup.XPopup;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.base.utils.CfLog;
import com.xtree.mine.BR;
import com.xtree.mine.R;
import com.xtree.mine.databinding.FragmentVipBinding;
import com.xtree.mine.ui.viewmodel.MineViewModel;
import com.xtree.mine.ui.viewmodel.factory.AppViewModelFactory;
import com.xtree.mine.vo.VipUpgradeItemVo;

import java.util.ArrayList;
import java.util.List;

import me.xtree.mvvmhabit.base.BaseFragment;

@Route(path = RouterFragmentPath.Mine.PAGER_VIP_UPGRADE)
public class VipFragment extends BaseFragment<FragmentVipBinding, MineViewModel> {
    private List<VipUpgradeItemVo> vipUpgradeItems = new ArrayList<>();
    private ArrayList<Fragment> fragmentList = new ArrayList<>();
    private ArrayList<String> tabList = new ArrayList<>();
    private int level;
    private FragmentStateAdapter mAdapter;
    private VipBackPercentDialog ppw = null;

    @Override
    public void initView() {
        binding.ivwVipBack.setOnClickListener(v -> getActivity().finish());
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

        binding.vpMain.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                CfLog.d("position : " + position);
                binding.tvwLevelName.setText("VIP" + vipUpgradeItems.get(position).level + "尊享");
                binding.tvwVipCardNum.setText(vipUpgradeItems.get(position).upgrade_bonus);
                binding.tvwVipWithdrawNum.setText(vipUpgradeItems.get(position).withdrawals_limit);
                binding.tvwVipWithdrawTimeNum.setText(vipUpgradeItems.get(position).withdrawals_num);
                binding.tvwVipBirthdayNum.setText(vipUpgradeItems.get(position).birthday_gift);
                binding.tvwVipGiftNum.setText(vipUpgradeItems.get(position).week_red);

            }
        });

        binding.tblType.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        binding.btnVipDetail.setOnClickListener(v -> {
            ppw = (VipBackPercentDialog) new XPopup.Builder(getContext()).asCustom(new VipBackPercentDialog(getContext(), vipUpgradeItems, level, 80));
            ppw.show();
        });
    }

    @Override
    public void initData() {
        viewModel.readVipCache();
        viewModel.getVipUpgradeInfo();
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_vip;
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

    @Override
    public void initViewObservable() {
        viewModel.liveDataVipUpgrade.observe(this, vo -> {
            vipUpgradeItems = vo.vip_upgrade;
            level = vo.level;

            binding.tvwNowProgress.setText("当前流水 : " + vo.current_activity + " (" + vo.current_activity + "/" + vo.vip_upgrade.get(vo.level + 1).active + ")");
            binding.tvwVipNowLevelNum.setText("" + vo.level);
            binding.tvwVipNowLevelStart.setText("VIP" + vo.level);
            binding.tvwVipNextLevelEnd.setText("VIP" + (vo.level + 1));

            double progress = vo.current_activity / vo.vip_upgrade.get(vo.level + 1).active;
            binding.pbVip.setProgress((int) (progress * 100));
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) binding.ivwVipPeople.getLayoutParams();
            params.setMarginStart((int) (binding.pbVip.getWidth() * progress));
            binding.ivwVipPeople.setLayoutParams(params);
            if (fragmentList.isEmpty()) {
                fragmentList.add(new VipInfoFragment(vo.vip_upgrade.get(0).level, vo.vip_upgrade.get(0).active, vo.vip_upgrade.get(0).new_active));
                fragmentList.add(new VipInfoFragment(vo.vip_upgrade.get(1).level, vo.vip_upgrade.get(1).active, vo.vip_upgrade.get(1).new_active));
                fragmentList.add(new VipInfoFragment(vo.vip_upgrade.get(2).level, vo.vip_upgrade.get(2).active, vo.vip_upgrade.get(2).new_active));
                fragmentList.add(new VipInfoFragment(vo.vip_upgrade.get(3).level, vo.vip_upgrade.get(3).active, vo.vip_upgrade.get(3).new_active));
                fragmentList.add(new VipInfoFragment(vo.vip_upgrade.get(4).level, vo.vip_upgrade.get(4).active, vo.vip_upgrade.get(4).new_active));
                fragmentList.add(new VipInfoFragment(vo.vip_upgrade.get(5).level, vo.vip_upgrade.get(5).active, vo.vip_upgrade.get(5).new_active));
                fragmentList.add(new VipInfoFragment(vo.vip_upgrade.get(6).level, vo.vip_upgrade.get(6).active, vo.vip_upgrade.get(6).new_active));
                fragmentList.add(new VipInfoFragment(vo.vip_upgrade.get(7).level, vo.vip_upgrade.get(7).active, vo.vip_upgrade.get(7).new_active));
                fragmentList.add(new VipInfoFragment(vo.vip_upgrade.get(8).level, vo.vip_upgrade.get(8).active, vo.vip_upgrade.get(8).new_active));
                fragmentList.add(new VipInfoFragment(vo.vip_upgrade.get(9).level, vo.vip_upgrade.get(9).active, vo.vip_upgrade.get(9).new_active));
                fragmentList.add(new VipInfoFragment(vo.vip_upgrade.get(10).level, vo.vip_upgrade.get(10).active, vo.vip_upgrade.get(10).new_active));
            }

            if (tabList.isEmpty()) {
                tabList.add("VIP 0");
                tabList.add("VIP 1");
                tabList.add("VIP 2");
                tabList.add("VIP 3");
                tabList.add("VIP 4");
                tabList.add("VIP 5");
                tabList.add("VIP 6");
                tabList.add("VIP 7");
                tabList.add("VIP 8");
                tabList.add("VIP 9");
                tabList.add("VIP 10");
            }
            mAdapter.notifyDataSetChanged();

            binding.vpMain.setCurrentItem(level);
        });

        viewModel.liveDataProfile.observe(this, vo -> {
            binding.tvwVipUsername.setText("HI," + vo.username);
        });
    }
}
