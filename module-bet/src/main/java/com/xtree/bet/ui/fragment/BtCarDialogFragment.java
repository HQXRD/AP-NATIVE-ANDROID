package com.xtree.bet.ui.fragment;

import android.app.Application;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.xtree.bet.R;
import com.xtree.bet.databinding.BtLayoutBtCarBinding;
import com.xtree.bet.ui.adapter.BetConfirmOptionAdapter;
import com.xtree.bet.ui.adapter.CgOddLimitAdapter;
import com.xtree.bet.ui.viewmodel.BtCarViewModel;
import com.xtree.bet.ui.viewmodel.factory.AppViewModelFactory;
import com.xtree.bet.weight.KeyboardView;

import me.xtree.mvvmhabit.base.BaseDialogFragment;
import me.xtree.mvvmhabit.utils.ConvertUtils;
import me.xtree.mvvmhabit.utils.ToastUtils;
import me.xtree.mvvmhabit.utils.Utils;

/**
 * 投注确认页面
 */
public class BtCarDialogFragment extends BaseDialogFragment<BtLayoutBtCarBinding, BtCarViewModel> {
    private BetConfirmOptionAdapter betConfirmOptionAdapter;
    private CgOddLimitAdapter cgOddLimitAdapter;
    private KeyboardView keyboardView;
    /**
     * 是否单关 true-是 false-否
     */
    private boolean isDanGuang;

    private KeyBoardListener listener = new KeyBoardListener() {
        @Override
        public void showKeyBoard(boolean isShow) {
            showOrHideKeyBoard(isShow);
        }

        @Override
        public void clearFocus(View view) {
            view.clearFocus();
        }
    };
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        binding.rvBtOption.setLayoutManager(new LinearLayoutManager(this.getContext()));
        binding.rvBtCg.setLayoutManager(new LinearLayoutManager(this.getContext()));
        keyboardView = new KeyboardView(getContext(), null);
        keyboardView.setVisibility(View.GONE);
        keyboardView.setListener(listener);
        binding.ivConfirm.setCallBack(() -> {
            ToastUtils.showLong("开始投注");
        });
        binding.llRoot.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            if(!isDanGuang) {
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) binding.llKeyboard.getLayoutParams();
                if (params == null) {
                    params = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                }
                params.bottomMargin = binding.cslBottom.getMeasuredHeight();
                binding.llKeyboard.setLayoutParams(params);
            }

            /*ViewGroup.MarginLayoutParams optionParams = (ViewGroup.MarginLayoutParams)binding.nsvOption.getLayoutParams();
            int topHeight = binding.cslTop.getMeasuredHeight();
            int bottomHeight = binding.cslBottom.getMeasuredHeight();
            int optionHeight = binding.nsvOption.getMeasuredHeight();
            if(topHeight + bottomHeight + optionHeight > ConvertUtils.getScreenHeight(getContext()) * 0.85){
                int height = (int) (ConvertUtils.getScreenHeight(getContext()) * 0.85 - topHeight - bottomHeight);
                optionParams.height = height;
                binding.nsvOption.setLayoutParams(optionParams);
            }*/
        });
    }

    @Override
    public void initData() {
        viewModel.addSubscription();
        viewModel.setOptionData(R.raw.test_bet);
        viewModel.setCgData(R.raw.test_bet);
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.bt_layout_bt_car;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void initViewObservable() {
        viewModel.btConfirmInfoDate.observe(this, betConfirmOptions -> {
            if (betConfirmOptions.size() > 1) { // 串关
                binding.btnAddMatch.setVisibility(View.VISIBLE);
                binding.ivCgType.setBackgroundResource(R.mipmap.bt_icon_cg);
                binding.llKeyboard.addView(keyboardView);
                isDanGuang = false;
            } else { // 单关
                binding.btnAddMatch.setVisibility(View.GONE);
                binding.ivCgType.setBackgroundResource(R.mipmap.bt_icon_dan);
                binding.llKeyboardDan.addView(keyboardView);
                isDanGuang = true;
                showOrHideKeyBoard(true);
            }
            if (betConfirmOptionAdapter == null) {
                betConfirmOptionAdapter = new BetConfirmOptionAdapter(getContext(), R.layout.bt_layout_car_bt_item, betConfirmOptions);
                binding.rvBtOption.setAdapter(betConfirmOptionAdapter);
            }
        });
        viewModel.cgOddLimitDate.observe(this, cgOddLimits -> {
            if (cgOddLimitAdapter == null) {
                cgOddLimitAdapter = new CgOddLimitAdapter(getContext(), R.layout.bt_layout_car_cg_item, cgOddLimits);
                cgOddLimitAdapter.setListener(listener);
                cgOddLimitAdapter.setKeyboardView(keyboardView);
                binding.rvBtCg.setAdapter(cgOddLimitAdapter);
            }
        });
    }

    public void showOrHideKeyBoard(boolean isShow){
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams)binding.vSpace.getLayoutParams();
        Log.e("test", "===params.height=====" + params.height);
        if(isShow) {
            keyboardView.setVisibility(View.VISIBLE);
            if(!isDanGuang) {
                binding.llKeyboard.postDelayed(() -> {
                    params.height = binding.llKeyboard.getMeasuredHeight();
                    binding.vSpace.setLayoutParams(params);
                }, 200);
            }
        }else{
            keyboardView.setVisibility(View.GONE);
            params.height = 0;
            binding.vSpace.setLayoutParams(params);
        }
    }

    public interface KeyBoardListener{
        void showKeyBoard(boolean isShow);
        void clearFocus(View view);
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public BtCarViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance((Application) Utils.getContext());
        return new ViewModelProvider(this, factory).get(BtCarViewModel.class);
    }
}
