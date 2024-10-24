package com.xtree.bet.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.gyf.immersionbar.ImmersionBar;
import com.xtree.bet.BR;
import com.xtree.bet.R;
import com.xtree.bet.bean.ui.Match;
import com.xtree.bet.bean.ui.PlayType;
import com.xtree.bet.contract.BetContract;
import com.xtree.bet.databinding.BtLayoutDetailOptionBinding;
import com.xtree.bet.ui.activity.BtDetailActivity;
import com.xtree.bet.ui.activity.BtDetailResultActivity;
import com.xtree.bet.ui.adapter.MatchDetailAdapter;
import com.xtree.bet.ui.viewmodel.BtDetailOptionViewModel;
import com.xtree.bet.ui.viewmodel.factory.AppViewModelFactory;

import java.util.ArrayList;
import java.util.List;

import me.xtree.mvvmhabit.base.BaseFragment;

/**
 * Created by goldze on 2018/6/21
 */
public class BtDetailOptionFragment extends BaseFragment<BtLayoutDetailOptionBinding, BtDetailOptionViewModel> {
    private final static String KEY_PLAY_TYPE = "KEY_PLAY_TYPE";
    private final static String KEY_MATCH = "KEY_MATCH";

    private MatchDetailAdapter detailPlayTypeAdapter;

    private List<PlayType> playTypeList = new ArrayList<>();

    private Match match;

    private boolean isExpand = true;
    private boolean isResult;

    public static BtDetailOptionFragment getInstance(Match match, ArrayList<PlayType> playTypeList, Boolean isResult) {
        BtDetailOptionFragment instance = new BtDetailOptionFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(KEY_PLAY_TYPE, playTypeList);
        bundle.putParcelable(KEY_MATCH, match);
        bundle.putBoolean("isResult", isResult);
        instance.setArguments(bundle);
        return instance;
    }

    /**
     * 初始化沉浸式
     * Init immersion bar.
     */
    protected void initImmersionBar() {
        //设置共同沉浸式样式
        ImmersionBar.with(this)
                .navigationBarColor(me.xtree.mvvmhabit.R.color.default_navigation_bar_color)
                .fitsSystemWindows(false)
                .statusBarDarkFont(false)
                .init();
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
        isResult = getArguments().getBoolean("isResult");
        viewModel.addSubscription();
        updateOptionData();

    }

    /**
     * 更新联赛数据
     */
    private void updateOptionData() {
        if (detailPlayTypeAdapter == null) {
            detailPlayTypeAdapter = new MatchDetailAdapter(getContext(), match, playTypeList, isResult);
            binding.aelOption.setAdapter(detailPlayTypeAdapter);
            for (int i = 0; i < binding.aelOption.getExpandableListAdapter().getGroupCount(); i++) {
                binding.aelOption.expandGroup(i);
            }
        } else {
            detailPlayTypeAdapter.setData(playTypeList);
        }
    }

    public void expand() {
        for (int i = 0; i < binding.aelOption.getExpandableListAdapter().getGroupCount(); i++) {
            if (!isExpand) {
                binding.aelOption.expandGroup(i);
            } else {
                binding.aelOption.collapseGroup(i);
            }
        }
        isExpand = !isExpand;
    }

    @Override
    public void initViewObservable() {

        viewModel.betContractListData.observe(this, betContract -> {
            if (betContract.action == BetContract.ACTION_OPTION_CHANGE) {
                playTypeList = (List<PlayType>) betContract.getData();
                if (isResult) {
                    match = ((BtDetailResultActivity) getContext()).getmMatch();
                } else {
                    match = ((BtDetailActivity) getContext()).getmMatch();
                }
                updateOptionData();
            }
        });
    }

}
