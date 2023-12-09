package com.xtree.bet.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.xtree.base.router.RouterActivityPath;
import com.xtree.bet.BR;
import com.xtree.bet.R;
import com.xtree.bet.bean.ui.Category;
import com.xtree.bet.bean.ui.League;
import com.xtree.bet.bean.ui.Match;
import com.xtree.bet.databinding.BtLayoutDetailBinding;
import com.xtree.bet.databinding.FragmentMainBinding;
import com.xtree.bet.ui.adapter.DetailPlayTypeAdapter;
import com.xtree.bet.ui.adapter.LeagueAdapter;
import com.xtree.bet.ui.viewmodel.BtDetailViewModel;
import com.xtree.bet.ui.viewmodel.MainViewModel;
import com.xtree.bet.ui.viewmodel.factory.AppViewModelFactory;

import java.util.ArrayList;
import java.util.List;

import me.xtree.mvvmhabit.base.BaseActivity;
import me.xtree.mvvmhabit.base.ContainerActivity;
import me.xtree.mvvmhabit.utils.ToastUtils;

/**
 * Created by goldze on 2018/6/21
 */
public class BtDetailActivity extends BaseActivity<BtLayoutDetailBinding, BtDetailViewModel> implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    private List<Category> mCategories;

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.bt_layout_detail;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, BtDetailActivity.class);
        context.startActivity(intent);
    }

    @Override
    public BtDetailViewModel initViewModel() {
        //使用自定义的ViewModelFactory来创建ViewModel，如果不重写该方法，则默认会调用LoginViewModel(@NonNull Application application)构造方法
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getApplication());
        return new ViewModelProvider(this, factory).get(BtDetailViewModel.class);
    }

    @Override
    public void initView() {
        binding.appBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {// 收缩状态
                binding.rlToolbar.setVisibility(View.VISIBLE);
                binding.tvLeagueName.setVisibility(View.GONE);
                binding.toolbar.setBackgroundResource(R.color.bt_color_detail_top_toolbar);
            } else if (Math.abs(verticalOffset) == 0) {//展开
                binding.rlToolbar.setVisibility(View.GONE);
                binding.tvLeagueName.setVisibility(View.VISIBLE);
                binding.toolbar.setBackgroundResource(android.R.color.transparent);
            }
        });

        binding.tabCategoryType.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.rvOption.setAdapter(new DetailPlayTypeAdapter(BtDetailActivity.this, R.layout.bt_fb_detail_item_play_type_item, mCategories.get(tab.getPosition()).getPlayTypeList()));
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
        viewModel.setCategoryList();
    }

    @Override
    public void initViewObservable() {
        viewModel.categoryListData.observe(this, categories -> {
            mCategories = categories;
            if (!categories.isEmpty()) {
                for (int i = 0; i < categories.size(); i++) {
                    View view = LayoutInflater.from(this).inflate(R.layout.bt_layout_bet_catory_tab_item, null);
                    TextView tvName = view.findViewById(R.id.tab_item_name);
                    String name = categories.get(i).getName();

                    tvName.setText(name);
                    ColorStateList colorStateList = getResources().getColorStateList(R.color.bt_color_category_tab_text);
                    tvName.setTextColor(colorStateList);

                    binding.tabCategoryType.addTab(binding.tabCategoryType.newTab().setCustomView(view));
                }
                binding.rvOption.setLayoutManager(new LinearLayoutManager(this));
                binding.rvOption.setAdapter(new DetailPlayTypeAdapter(this, R.layout.bt_fb_detail_item_play_type_item, mCategories.get(0).getPlayTypeList()));
            }
        });
    }

    @Override
    public void onRefresh() {
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

    }
}
