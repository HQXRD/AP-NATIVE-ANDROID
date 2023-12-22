package com.xtree.bet.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.xtree.bet.BR;
import com.xtree.bet.R;
import com.xtree.bet.bean.ui.Category;
import com.xtree.bet.bean.ui.Match;
import com.xtree.bet.bean.ui.PlayType;
import com.xtree.bet.constant.Constants;
import com.xtree.bet.constant.SPKey;
import com.xtree.bet.constant.SportTypeContants;
import com.xtree.bet.contract.BetContract;
import com.xtree.bet.databinding.BtLayoutDetailBinding;
import com.xtree.bet.databinding.BtLayoutDetailOptionBinding;
import com.xtree.bet.manager.BtCarManager;
import com.xtree.bet.ui.activity.BtDetailActivity;
import com.xtree.bet.ui.adapter.MatchDetailAdapter;
import com.xtree.bet.ui.viewmodel.BtDetailOptionViewModel;
import com.xtree.bet.ui.viewmodel.BtDetailViewModel;
import com.xtree.bet.ui.viewmodel.factory.AppViewModelFactory;
import com.xtree.bet.weight.BaseDetailDataView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.xtree.mvvmhabit.base.BaseActivity;
import me.xtree.mvvmhabit.base.BaseFragment;
import me.xtree.mvvmhabit.utils.SPUtils;

/**
 * Created by goldze on 2018/6/21
 */
public class BtDetailOptionFragment extends BaseFragment<BtLayoutDetailOptionBinding, BtDetailOptionViewModel> {
    private final static String KEY_PLAY_TYPE = "KEY_PLAY_TYPE";
    private final static String KEY_MATCH = "KEY_MATCH";
    private List<Category> mCategories;

    private BaseDetailDataView fbDataView;

    private MatchDetailAdapter detailPlayTypeAdapter;

    private List<PlayType> playTypeList = new ArrayList<>();

    private Match match;

    private int matchId;

    public static BtDetailOptionFragment getInstance(Match match, ArrayList<PlayType> playTypeList){
        BtDetailOptionFragment instance = new BtDetailOptionFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(KEY_PLAY_TYPE, playTypeList);
        bundle.putParcelable(KEY_MATCH, match);
        instance.setArguments(bundle);
        return instance;
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.bt_layout_detail_option;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public BtDetailOptionViewModel initViewModel() {
        //使用自定义的ViewModelFactory来创建ViewModel，如果不重写该方法，则默认会调用LoginViewModel(@NonNull Application application)构造方法
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getActivity().getApplication());
        return new ViewModelProvider(this, factory).get(BtDetailOptionViewModel.class);
    }

    @Override
    public void initView() {


    }

    @Override
    public void initData() {
        match = getArguments().getParcelable(KEY_MATCH);
        playTypeList = getArguments().getParcelableArrayList(KEY_PLAY_TYPE);
        viewModel.addSubscription();
        updateOptionData();

    }

    /**
     * 更新联赛数据
     */
    private void updateOptionData() {
        if (detailPlayTypeAdapter == null) {
            detailPlayTypeAdapter = new MatchDetailAdapter(getContext(), match, playTypeList);
            binding.aelOption.setAdapter(detailPlayTypeAdapter);
            for (int i = 0; i < binding.aelOption.getExpandableListAdapter().getGroupCount(); i++) {
                binding.aelOption.expandGroup(i);
            }
        } else {
            detailPlayTypeAdapter.setData(playTypeList);
        }
    }

    @Override
    public void initViewObservable() {

        viewModel.betContractListData.observe(this, betContract -> {
            if (betContract.action == BetContract.ACTION_OPTION_CHANGE) {
                playTypeList = (List<PlayType>) betContract.getData();
                match = ((BtDetailActivity)getContext()).getMatch();
                updateOptionData();
            }
        });
    }

}
