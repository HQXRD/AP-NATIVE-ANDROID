package com.xtree.mine.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.android.material.tabs.TabLayoutMediator;
import com.lxj.xpopup.XPopup;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.base.utils.CfLog;
import com.xtree.base.widget.LoadingDialog;
import com.xtree.mine.BR;
import com.xtree.mine.R;
import com.xtree.mine.databinding.FragmentVipBinding;
import com.xtree.mine.ui.viewmodel.MineViewModel;
import com.xtree.mine.ui.viewmodel.factory.AppViewModelFactory;
import com.xtree.mine.vo.VipUpgradeInfoVo;

import java.util.ArrayList;

import me.xtree.mvvmhabit.base.BaseFragment;

@Route(path = RouterFragmentPath.Mine.PAGER_VIP_UPGRADE)
public class VipFragment extends BaseFragment<FragmentVipBinding, MineViewModel> {
    private ArrayList<Fragment> fragmentList = new ArrayList<>();
    private ArrayList<String> tabList = new ArrayList<>();
    private VipUpgradeInfoVo mVipUpgradeInfoVo;
    private FragmentStateAdapter mAdapter;
    private VipBackPercentDialog ppw = null;
    private TextView tvwItem[] = new TextView[11];

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
            View customView = LayoutInflater.from(getContext()).inflate(R.layout.custom_tab_item, null);
            TextView tvwItem = customView.findViewById(R.id.tvw_item);
            tvwItem.setText(tabList.get(position));

            this.tvwItem[position] = tvwItem;

            tab.setCustomView(customView);
        }).attach();

        binding.vpMain.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                CfLog.d("position : " + position);
                if (mVipUpgradeInfoVo.sp.equals("0")) {
                    binding.tvwLevelName.setText("VIP" + mVipUpgradeInfoVo.vip_upgrade.get(position).level + "尊享");
                } else {
                    binding.tvwLevelName.setText("VIP" + mVipUpgradeInfoVo.vip_upgrade.get(position).display_level + "尊享");
                }
                binding.tvwVipCardNum.setText(mVipUpgradeInfoVo.vip_upgrade.get(position).upgrade_bonus);
                binding.tvwVipWithdrawNum.setText(mVipUpgradeInfoVo.vip_upgrade.get(position).withdrawals_limit);
                binding.tvwVipWithdrawTimeNum.setText(mVipUpgradeInfoVo.vip_upgrade.get(position).withdrawals_num);
                binding.tvwVipBirthdayNum.setText(mVipUpgradeInfoVo.vip_upgrade.get(position).birthday_gift);
                binding.tvwVipGiftNum.setText(mVipUpgradeInfoVo.vip_upgrade.get(position).week_red);

                for (int i = 0; i < tvwItem.length; i++) {
                    if (i == position) {
                        tvwItem[i].setBackground(getResources().getDrawable(R.drawable.ic_vip_select));
                    } else {
                        tvwItem[i].setBackground(getResources().getDrawable(R.drawable.ic_vip_unselect));
                    }
                }
            }
        });

        binding.btnVipDetail.setOnClickListener(v -> {
            ppw = (VipBackPercentDialog) new XPopup.Builder(getContext()).asCustom(new VipBackPercentDialog(getContext(), mVipUpgradeInfoVo, 80));
            ppw.show();
        });
    }

    @Override
    public void initData() {
        viewModel.readVipCache();
        LoadingDialog.show(getContext());
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
            mVipUpgradeInfoVo = vo;
            if (vo.sp.equals("0")) {
                if (vo.level < vo.vip_upgrade.size() - 1) {
                    binding.tvwNowProgress.setText("当前流水 : " + vo.current_activity + " (" + vo.current_activity + "/" + vo.vip_upgrade.get(vo.level + 1).active + ")");
                    binding.tvwVipNowLevelNum.setText("" + vo.level);
                    binding.tvwVipNowLevelStart.setText("VIP" + vo.level);
                    binding.tvwVipNextLevelEnd.setText("VIP" + (vo.level + 1));

                    double progress = ((double) vo.current_activity / (double) vo.vip_upgrade.get(vo.level + 1).active) * 100;
                    CfLog.e("vo.current_activity : " + vo.current_activity + " vo.vip_upgrade.get(vo.level + 1).active : " + vo.vip_upgrade.get(vo.level + 1).active + " progress : " + progress);
                    binding.pbVip.setProgress((int) (progress));

                    ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) binding.ivwVipPeople.getLayoutParams();
                    params.setMarginStart((int) (binding.pbVip.getWidth() * (progress / 100)));
                    binding.ivwVipPeople.setLayoutParams(params);
                } else {
                    binding.tvwNowProgress.setText("当前流水 : " + vo.current_activity + " (" + vo.current_activity + "/" + vo.vip_upgrade.get(vo.level).active + ")");
                    binding.tvwVipNowLevelNum.setText("" + vo.level);
                    binding.tvwVipNowLevelStart.setText("VIP" + vo.level);
                    binding.tvwVipNextLevelEnd.setText("VIP" + vo.level);

                    binding.pbVip.setProgress(100);

                    ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) binding.ivwVipPeople.getLayoutParams();
                    params.setMarginStart((int) (binding.pbVip.getWidth() * 0.9));
                    binding.ivwVipPeople.setLayoutParams(params);
                }

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
                    tabList.add("VIP " + vo.vip_upgrade.get(0).level);
                    tabList.add("VIP " + vo.vip_upgrade.get(1).level);
                    tabList.add("VIP " + vo.vip_upgrade.get(2).level);
                    tabList.add("VIP " + vo.vip_upgrade.get(3).level);
                    tabList.add("VIP " + vo.vip_upgrade.get(4).level);
                    tabList.add("VIP " + vo.vip_upgrade.get(5).level);
                    tabList.add("VIP " + vo.vip_upgrade.get(6).level);
                    tabList.add("VIP " + vo.vip_upgrade.get(7).level);
                    tabList.add("VIP " + vo.vip_upgrade.get(8).level);
                    tabList.add("VIP " + vo.vip_upgrade.get(9).level);
                    tabList.add("VIP " + vo.vip_upgrade.get(10).level);
                }
                mAdapter.notifyDataSetChanged();

                binding.vpMain.setCurrentItem(vo.level);
                tvwItem[vo.level].setBackground(getResources().getDrawable(R.drawable.ic_vip_select));
            } else {
                if (vo.level < vo.vip_upgrade.size() - 1) {
                    binding.tvwNowProgress.setText("当前流水 : " + vo.current_activity + " (" + vo.current_activity + "/" + vo.vip_upgrade.get(vo.level + 1).display_active + ")");
                    binding.tvwVipNowLevelNum.setText("" + vo.display_level);
                    binding.tvwVipNowLevelStart.setText("VIP" + vo.vip_upgrade.get(vo.level).display_level);
                    binding.tvwVipNextLevelEnd.setText("VIP" + vo.vip_upgrade.get(vo.level + 1).display_level);

                    double progress = ((double) vo.current_activity / (double) vo.vip_upgrade.get(vo.level + 1).display_active) * 100;
                    CfLog.e("vo.current_activity : " + vo.current_activity + " vo.vip_upgrade.get(vo.level + 1).display_active : " + vo.vip_upgrade.get(vo.level + 1).display_active + " progress : " + progress);
                    binding.pbVip.setProgress((int) (progress));

                    ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) binding.ivwVipPeople.getLayoutParams();
                    params.setMarginStart((int) (binding.pbVip.getWidth() * (progress / 100)));
                    binding.ivwVipPeople.setLayoutParams(params);
                } else {
                    binding.tvwNowProgress.setText("当前流水 : " + vo.current_activity + " (" + vo.current_activity + "/" + vo.vip_upgrade.get(vo.level).display_active + ")");
                    binding.tvwVipNowLevelNum.setText("" + vo.display_level);
                    binding.tvwVipNowLevelStart.setText("VIP" + vo.display_level);
                    binding.tvwVipNextLevelEnd.setText("VIP" + vo.display_level);

                    binding.pbVip.setProgress(100);

                    ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) binding.ivwVipPeople.getLayoutParams();
                    params.setMarginStart((int) (binding.pbVip.getWidth() * 0.9));
                    binding.ivwVipPeople.setLayoutParams(params);
                }

                if (fragmentList.isEmpty()) {
                    fragmentList.add(new VipInfoFragment(vo.vip_upgrade.get(0).display_level, vo.vip_upgrade.get(0).display_active, vo.vip_upgrade.get(0).new_active));
                    fragmentList.add(new VipInfoFragment(vo.vip_upgrade.get(1).display_level, vo.vip_upgrade.get(1).display_active, vo.vip_upgrade.get(1).new_active));
                    fragmentList.add(new VipInfoFragment(vo.vip_upgrade.get(2).display_level, vo.vip_upgrade.get(2).display_active, vo.vip_upgrade.get(2).new_active));
                    fragmentList.add(new VipInfoFragment(vo.vip_upgrade.get(3).display_level, vo.vip_upgrade.get(3).display_active, vo.vip_upgrade.get(3).new_active));
                    fragmentList.add(new VipInfoFragment(vo.vip_upgrade.get(4).display_level, vo.vip_upgrade.get(4).display_active, vo.vip_upgrade.get(4).new_active));
                    fragmentList.add(new VipInfoFragment(vo.vip_upgrade.get(5).display_level, vo.vip_upgrade.get(5).display_active, vo.vip_upgrade.get(5).new_active));
                    fragmentList.add(new VipInfoFragment(vo.vip_upgrade.get(6).display_level, vo.vip_upgrade.get(6).display_active, vo.vip_upgrade.get(6).new_active));
                    fragmentList.add(new VipInfoFragment(vo.vip_upgrade.get(7).display_level, vo.vip_upgrade.get(7).display_active, vo.vip_upgrade.get(7).new_active));
                    fragmentList.add(new VipInfoFragment(vo.vip_upgrade.get(8).display_level, vo.vip_upgrade.get(8).display_active, vo.vip_upgrade.get(8).new_active));
                    fragmentList.add(new VipInfoFragment(vo.vip_upgrade.get(9).display_level, vo.vip_upgrade.get(9).display_active, vo.vip_upgrade.get(9).new_active));
                    fragmentList.add(new VipInfoFragment(vo.vip_upgrade.get(10).display_level, vo.vip_upgrade.get(10).display_active, vo.vip_upgrade.get(10).new_active));
                }

                if (tabList.isEmpty()) {
                    tabList.add("VIP " + vo.vip_upgrade.get(0).display_level);
                    tabList.add("VIP " + vo.vip_upgrade.get(1).display_level);
                    tabList.add("VIP " + vo.vip_upgrade.get(2).display_level);
                    tabList.add("VIP " + vo.vip_upgrade.get(3).display_level);
                    tabList.add("VIP " + vo.vip_upgrade.get(4).display_level);
                    tabList.add("VIP " + vo.vip_upgrade.get(5).display_level);
                    tabList.add("VIP " + vo.vip_upgrade.get(6).display_level);
                    tabList.add("VIP " + vo.vip_upgrade.get(7).display_level);
                    tabList.add("VIP " + vo.vip_upgrade.get(8).display_level);
                    tabList.add("VIP " + vo.vip_upgrade.get(9).display_level);
                    tabList.add("VIP " + vo.vip_upgrade.get(10).display_level);
                }
                mAdapter.notifyDataSetChanged();

                binding.vpMain.setCurrentItem(vo.level);
                tvwItem[vo.level].setBackground(getResources().getDrawable(R.drawable.ic_vip_select));
            }
        });

        viewModel.liveDataProfile.observe(this, vo -> {
            binding.tvwVipUsername.setText("HI," + vo.username);
        });
    }
}
