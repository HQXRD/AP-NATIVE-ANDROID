package com.xtree.bet.ui.fragment;

import android.app.Application;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.android.material.tabs.TabLayout;
import com.lxj.xpopup.XPopup;
import com.xtree.base.global.Constant;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.base.utils.DomainUtil;
import com.xtree.base.widget.BrowserDialog;
import com.xtree.bet.BR;
import com.xtree.bet.R;
import com.xtree.bet.databinding.BtDialogSettingBinding;
import com.xtree.bet.databinding.BtFragmentTutorialBinding;
import com.xtree.bet.ui.viewmodel.factory.AppViewModelFactory;
import com.xtree.bet.ui.viewmodel.fb.FBBtCarViewModel;
import com.xtree.bet.ui.viewmodel.fb.FBMainViewModel;

import me.xtree.mvvmhabit.base.BaseFragment;
import me.xtree.mvvmhabit.base.BaseViewModel;
import me.xtree.mvvmhabit.utils.Utils;

/**
 * 盘口教程页面
 */
@Route(path = RouterFragmentPath.Bet.PAGER_BET_TUTORIAL)
public class BtTutorialFragment extends BaseFragment<BtFragmentTutorialBinding, FBMainViewModel> implements View.OnClickListener {
    private int tabPos;
    public static BtTutorialFragment getInstance(){
        BtTutorialFragment btResultDialogFragment = new BtTutorialFragment();
        return btResultDialogFragment;
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.bt_fragment_tutorial;
    }

    @Override
    public void initView() {
        String[] tabName = new String[]{"单关投注", "串关投注", "查看订单"};
        int[][] tabContent = new int[][]{new int[]{R.mipmap.bt_tutorial_dg_1, R.mipmap.bt_tutorial_dg_2, R.mipmap.bt_tutorial_dg_3, R.mipmap.bt_tutorial_dg_4},
                new int[]{R.mipmap.bt_tutorial_cg_1, R.mipmap.bt_tutorial_cg_2, R.mipmap.bt_tutorial_cg_3, R.mipmap.bt_tutorial_cg_4},
                new int[]{R.mipmap.bt_tutorial_order_1, R.mipmap.bt_tutorial_order_2, R.mipmap.bt_tutorial_order_3, R.mipmap.bt_tutorial_order_4}};

        binding.ivCs.setOnClickListener(this);
        binding.ivMsg.setOnClickListener(this);
        binding.ivwBack.setOnClickListener(this);

        for (int i = 0; i < tabName.length; i++) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.bt_layout_tutorial_tab_item, null);
            TextView tvName = view.findViewById(R.id.tab_item_name);
            String name = tabName[i];

            tvName.setText(name);
            ColorStateList colorStateList = getResources().getColorStateList(R.color.bt_color_tutorial_tab_text);
            tvName.setTextColor(colorStateList);
            binding.tabTutorialType.addTab(binding.tabTutorialType.newTab().setCustomView(view));
        }

        binding.vpTutorial.setAdapter(new FragmentStateAdapter(getActivity()) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                return BtTutorialItemFragment.getInstance(tabContent[position]);
            }

            @Override
            public int getItemCount() {
                return tabName.length;
            }
        });
        binding.vpTutorial.setUserInputEnabled(false);
        binding.vpTutorial.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);

        binding.tabTutorialType.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.vpTutorial.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    @Override
    public void initData() {

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

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.iv_cs){
            String title = getContext().getString(R.string.txt_custom_center);
            String url = DomainUtil.getDomain2() + Constant.URL_CUSTOMER_SERVICE;
            new XPopup.Builder(getContext()).asCustom(new BrowserDialog(getContext(), title, url)).show();
        } else if (id == R.id.iv_msg) {
            startContainerFragment(RouterFragmentPath.Mine.PAGER_MSG);
        } else if (id == R.id.ivw_back) {
            viewModel.finish();
        }
    }
}
