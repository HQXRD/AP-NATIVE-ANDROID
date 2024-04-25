package com.xtree.mine.ui.rebateagrt.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.base.widget.lottery.LotteryPickView;
import com.xtree.mine.BR;
import com.xtree.mine.R;
import com.xtree.mine.databinding.FragmentRebateAgreementBinding;
import com.xtree.mine.ui.rebateagrt.viewmodel.RebateAgreementViewModel;
import com.xtree.mine.ui.viewmodel.factory.AppViewModelFactory;

import java.util.ArrayList;
import java.util.List;

import me.xtree.mvvmhabit.base.BaseFragment;

/**
 * Created by KAKA on 2024/3/8.
 * Describe: 返水契约
 */
@Route(path = RouterFragmentPath.Mine.PAGER_REBATE_AGREEMENT)
public class RebateAgreementFragment extends BaseFragment<FragmentRebateAgreementBinding, RebateAgreementViewModel> {
    @Override
    public void initView() {
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_rebate_agreement;
    }

    @Override
    public int initVariableId() {
        return BR.model;
    }

    @Override
    public RebateAgreementViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getActivity().getApplication());
        return new ViewModelProvider(this, factory).get(RebateAgreementViewModel.class);
    }

    @Override
    public void initData() {
        super.initData();
        viewModel.setActivity(getActivity());
        viewModel.initData();

        binding.lotteryPickView.setPickListener(new LotteryPickView.onPickListener() {
            @Override
            public void onPick(List<Integer> pickNums) {
                pickNums.toString();
            }
        });

        ArrayList<String> strings = new ArrayList<>();
        strings.add("龍");
        strings.add("虎");
        strings.add("莊");
        strings.add("閑");

        ArrayList<Integer> olist = new ArrayList<>();
        olist.add(12);
        olist.add(3);
        olist.add(44);
        olist.add(555);
        olist.add(223);
        olist.add(4);
        olist.add(4);
        olist.add(5);
        olist.add(23);
        olist.add(68);
        binding.lotteryPickView
                .toSingleMode();

        binding.bt1.setOnClickListener(v -> binding.lotteryPickView.showOmission(olist));
        binding.bt2.setOnClickListener(v -> binding.lotteryPickView.closeTag());
        binding.bt3.setOnClickListener(v -> binding.lotteryPickView.showHotCold(olist));
        binding.bt4.setOnClickListener(v -> binding.lotteryPickView.pickOdd());
        binding.bt5.setOnClickListener(v -> binding.lotteryPickView.pickEven());
        binding.bt6.setOnClickListener(v -> binding.lotteryPickView.pickClear());
    }
}
