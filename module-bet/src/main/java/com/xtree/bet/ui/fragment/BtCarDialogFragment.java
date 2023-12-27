package com.xtree.bet.ui.fragment;

import android.app.Application;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.xtree.bet.R;
import com.xtree.bet.bean.ui.BetConfirmOption;
import com.xtree.bet.bean.ui.BetConfirmOptionFb;
import com.xtree.bet.bean.ui.CgOddLimit;
import com.xtree.bet.bean.ui.CgOddLimitFb;
import com.xtree.bet.constant.SportTypeContants;
import com.xtree.bet.contract.BetContract;
import com.xtree.bet.databinding.BtLayoutBtCarBinding;
import com.xtree.bet.manager.BtCarManager;
import com.xtree.bet.ui.activity.BtDetailActivity;
import com.xtree.bet.ui.adapter.BetConfirmOptionAdapter;
import com.xtree.bet.ui.adapter.CgOddLimitAdapter;
import com.xtree.bet.ui.viewmodel.BtCarViewModel;
import com.xtree.bet.ui.viewmodel.factory.AppViewModelFactory;
import com.xtree.bet.weight.KeyboardView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.xtree.mvvmhabit.base.BaseDialogFragment;
import me.xtree.mvvmhabit.bus.RxBus;
import me.xtree.mvvmhabit.utils.ToastUtils;
import me.xtree.mvvmhabit.utils.Utils;

/**
 * 投注确认页面
 */
public class BtCarDialogFragment extends BaseDialogFragment<BtLayoutBtCarBinding, BtCarViewModel> {
    public final static String KEY_BT_OPTION = "KEY_BT_OPTION";
    /**
     * 投注项列表
     */
    List<BetConfirmOption> betConfirmOptionList = new ArrayList<>();
    List<CgOddLimit> cgOddLimitList = new ArrayList<>();

    private BetConfirmOptionAdapter betConfirmOptionAdapter;
    private CgOddLimitAdapter cgOddLimitAdapter;
    private KeyboardView keyboardView;

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
            viewModel.bet(betConfirmOptionList, cgOddLimitList);
        });
        binding.llRoot.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            if(BtCarManager.isCg()) {
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

        if (BtCarManager.isCg() && !BtCarManager.isEmpty()) { // 串关
            binding.btnAddMatch.setVisibility(View.VISIBLE);
            binding.ivCgType.setBackgroundResource(R.mipmap.bt_icon_cg);
            binding.llKeyboard.addView(keyboardView);
            binding.ivBt.setBackgroundResource(R.mipmap.bt_ic_bt_dan);
        } else { // 单关
            binding.btnAddMatch.setVisibility(View.GONE);
            binding.ivCgType.setBackgroundResource(R.mipmap.bt_icon_dan);
            binding.llKeyboardDan.addView(keyboardView);
            showOrHideKeyBoard(true);
            binding.ivBt.setBackgroundResource(R.mipmap.bt_ic_bt_cg);
        }
        binding.ivBt.setOnClickListener(this);
        binding.ivDelete.setOnClickListener(this);
        binding.btnAddMatch.setOnClickListener(this);
    }

    @Override
    public void initData() {
        viewModel.addSubscription();

        if(!BtCarManager.getBtCarList().isEmpty()) {
            betConfirmOptionList = BtCarManager.getBtCarList();
        }else {
            BetConfirmOption betConfirmOption = getArguments().getParcelable(KEY_BT_OPTION);
            betConfirmOptionList.add(betConfirmOption);
        }
        betConfirmOptionAdapter = new BetConfirmOptionAdapter(getContext(), betConfirmOptionList);
        binding.rvBtOption.setAdapter(betConfirmOptionAdapter);
        betConfirmOptionAdapter.setBtCarDialogFragment(this);

        viewModel.batchBetMatchMarketOfJumpLine(betConfirmOptionList);

    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.addSubscribe(Observable.interval(5, 5, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    batchBetMatchMarketOfJumpLine();
                })
        );
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
            for (int i = 0; i < betConfirmOptionList.size(); i ++){
                betConfirmOptionList.get(i).setRealData(betConfirmOptions.get(i).getRealData());
            }

            if (betConfirmOptionAdapter == null) {
                betConfirmOptionAdapter = new BetConfirmOptionAdapter(getContext(), betConfirmOptionList);
                binding.rvBtOption.setAdapter(betConfirmOptionAdapter);
            }else{
                betConfirmOptionAdapter.setNewData(betConfirmOptionList);
            }
        });
        viewModel.cgOddLimitDate.observe(this, cgOddLimits -> {
            this.cgOddLimitList = cgOddLimits;
            if (cgOddLimitAdapter == null) {
                cgOddLimitAdapter = new CgOddLimitAdapter(getContext(), cgOddLimits);
                cgOddLimitAdapter.setListener(listener);
                cgOddLimitAdapter.setKeyboardView(keyboardView);
                binding.rvBtCg.setAdapter(cgOddLimitAdapter);
            }else{
                cgOddLimitAdapter.setNewData(cgOddLimits);
            }
        });
        viewModel.btResultInfoDate.observe(this, btResults -> {
            BtResultDialogFragment.getInstance(betConfirmOptionList, cgOddLimitList, btResults).show(getParentFragmentManager(), "BtResultDialogFragment");
            dismiss();
        });
    }

    public void batchBetMatchMarketOfJumpLine(){
        viewModel.batchBetMatchMarketOfJumpLine(betConfirmOptionList);
    }

    public void showOrHideKeyBoard(boolean isShow){
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams)binding.vSpace.getLayoutParams();
        if(isShow) {
            keyboardView.setVisibility(View.VISIBLE);
            if(BtCarManager.isCg()) {
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
        int id = view.getId();
        if(id == R.id.iv_bt){
            if(BtCarManager.isCg() && !BtCarManager.isEmpty()){
                if(getContext() instanceof BtDetailActivity){
                    BtCarManager.setIsCg(false);
                    BtCarManager.clearBtCar();
                }else {
                    viewModel.gotoToday();
                }
                binding.btnAddMatch.setVisibility(View.GONE);
                dismiss();
            }else{
                if(!betConfirmOptionList.isEmpty()) {
                    if (!betConfirmOptionList.get(0).getOptionList().isAllowCrossover()) {
                        ToastUtils.showShort(getContext().getResources().getText(R.string.bt_bt_is_not_allow_crossover));
                        return;
                    }
                    BetConfirmOption betConfirmOption = new BetConfirmOptionFb(betConfirmOptionList.get(0).getMatch(),
                            betConfirmOptionList.get(0).getPlayType(),
                            betConfirmOptionList.get(0).getPlayType().getOptionLists().get(0),
                            betConfirmOptionList.get(0).getOption());
                    binding.btnAddMatch.setVisibility(View.VISIBLE);
                    viewModel.gotoCg(betConfirmOption);
                    if(getContext() instanceof BtDetailActivity){
                        viewModel.finish();
                    }
                    dismiss();
                }
            }
        }else if(id == R.id.iv_delete){
            BtCarManager.clearBtCar();
            dismiss();
        }else if(id == R.id.btn_add_match){
            dismiss();
        }
    }

    @Override
    public BtCarViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance((Application) Utils.getContext());
        return new ViewModelProvider(this, factory).get(BtCarViewModel.class);
    }
}
