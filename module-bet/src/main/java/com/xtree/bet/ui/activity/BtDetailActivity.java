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
import com.xtree.bet.bean.ui.League;
import com.xtree.bet.databinding.BtLayoutDetailBinding;
import com.xtree.bet.databinding.FragmentMainBinding;
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

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.bt_layout_detail;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    public static void start(Context context){
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
            Log.e("test", "====verticalOffset===" + verticalOffset);
            if(Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()){// 收缩状态
                binding.toolbar1.setVisibility(View.VISIBLE);
                binding.toolbar.setVisibility(View.GONE);
            }else if(Math.abs(verticalOffset) == 0){//展开
                binding.toolbar1.setVisibility(View.GONE);
                binding.toolbar.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void initData() {

    }

    @Override
    public void initViewObservable() {

    }

    @Override
    public void onRefresh() {
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

    }
}
