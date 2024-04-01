package com.xtree.bet.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.gyf.immersionbar.ImmersionBar;
import com.xtree.bet.BR;
import com.xtree.bet.R;
import com.xtree.bet.bean.ui.Category;
import com.xtree.bet.bean.ui.Match;
import com.xtree.bet.bean.ui.PlayType;
import com.xtree.bet.contract.BetContract;
import com.xtree.bet.databinding.BtLayoutDetailOptionBinding;
import com.xtree.bet.ui.activity.BtDetailActivity;
import com.xtree.bet.ui.adapter.MatchDetailAdapter;
import com.xtree.bet.ui.viewmodel.BtDetailOptionViewModel;
import com.xtree.bet.ui.viewmodel.factory.AppViewModelFactory;
import com.xtree.bet.weight.BaseDetailDataView;

import java.util.ArrayList;
import java.util.List;

import me.xtree.mvvmhabit.base.BaseDialogFragment;
import me.xtree.mvvmhabit.base.BaseFragment;

/**
 * Created by goldze on 2018/6/21
 */
public class BtAdvanceSettlementFragment extends BaseDialogFragment<BtLayoutDetailOptionBinding, BtDetailOptionViewModel> {
    private final static String KEY_PLAY_TYPE = "KEY_PLAY_TYPE";
    private final static String KEY_MATCH = "KEY_MATCH";
    private List<Category> mCategories;

    private BaseDetailDataView fbDataView;

    private MatchDetailAdapter detailPlayTypeAdapter;

    private List<PlayType> playTypeList = new ArrayList<>();

    private Match match;

    private boolean isExpand = true;

    public static BtAdvanceSettlementFragment getInstance() {
        BtAdvanceSettlementFragment instance = new BtAdvanceSettlementFragment();
       /* Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(KEY_PLAY_TYPE, playTypeList);
        bundle.putParcelable(KEY_MATCH, match);
        instance.setArguments(bundle);*/
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
        return R.layout.bt_dialog_advance_settlement;

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
        viewModel.addSubscription();

    }

    @Override
    public void initViewObservable() {


    }

    @Override
    public void onClick(View v) {

    }
}
