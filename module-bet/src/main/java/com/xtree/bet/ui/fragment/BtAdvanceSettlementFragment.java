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
import com.xtree.bet.databinding.BtDialogAdvanceSettlementBinding;
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
 * 提前结算弹窗
 * Created by goldze on 2018/6/21
 */
public class BtAdvanceSettlementFragment extends BaseDialogFragment<BtDialogAdvanceSettlementBinding, BtDetailOptionViewModel> {
    private final static String KEY_CAPITAL = "key_capital";
    private final static String KEY_BACK = "key_back";
    /**
     * 本金
     */
    private double mCaptialAmount;
    /**
     * 返还金额
     */
    private double mBackAmount;

    public static BtAdvanceSettlementFragment getInstance(double captialAmount, double backAmount) {
        BtAdvanceSettlementFragment instance = new BtAdvanceSettlementFragment();
        Bundle bundle = new Bundle();
        bundle.putDouble(KEY_CAPITAL, captialAmount);
        bundle.putDouble(KEY_BACK, backAmount);
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
        mCaptialAmount = getArguments().getDouble(KEY_CAPITAL);
        mBackAmount = getArguments().getDouble(KEY_BACK);
        binding.tvCost.setText(getString(R.string.bt_txt_btn_statement_amount, String.valueOf(mCaptialAmount)));
        binding.tvBack.setText(getString(R.string.bt_txt_btn_statement_amount, String.valueOf(mBackAmount)));
    }

    @Override
    public void initViewObservable() {


    }

    @Override
    public void onClick(View v) {

    }
}
