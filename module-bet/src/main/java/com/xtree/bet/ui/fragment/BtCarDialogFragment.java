package com.xtree.bet.ui.fragment;

import static com.xtree.bet.ui.activity.MainActivity.KEY_PLATFORM;
import static com.xtree.bet.ui.activity.MainActivity.KEY_PLATFORM_NAME;
import static com.xtree.bet.ui.activity.MainActivity.PLATFORM_PM;

import android.animation.ObjectAnimator;
import android.app.Application;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.xtree.bet.R;
import com.xtree.bet.bean.ui.BetConfirmOption;
import com.xtree.bet.bean.ui.BetConfirmOptionUtil;
import com.xtree.bet.bean.ui.CgOddLimit;
import com.xtree.bet.databinding.BtLayoutBtCarBinding;
import com.xtree.bet.manager.BtCarManager;
import com.xtree.bet.ui.activity.BtDetailActivity;
import com.xtree.bet.ui.adapter.BetConfirmOptionAdapter;
import com.xtree.bet.ui.adapter.CgOddLimitSecAdapter;
import com.xtree.bet.ui.viewmodel.fb.FBBtCarViewModel;
import com.xtree.bet.ui.viewmodel.pm.PMBtCarViewModel;
import com.xtree.bet.ui.viewmodel.TemplateBtCarViewModel;
import com.xtree.bet.ui.viewmodel.factory.AppViewModelFactory;
import com.xtree.bet.ui.viewmodel.factory.PMAppViewModelFactory;
import com.xtree.bet.weight.KeyboardView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.xtree.mvvmhabit.base.BaseDialogFragment;
import me.xtree.mvvmhabit.utils.ConvertUtils;
import me.xtree.mvvmhabit.utils.SPUtils;
import me.xtree.mvvmhabit.utils.ToastUtils;
import me.xtree.mvvmhabit.utils.Utils;

/**
 * 投注确认页面
 */
public class BtCarDialogFragment extends BaseDialogFragment<BtLayoutBtCarBinding, TemplateBtCarViewModel> {
    public final static String KEY_BT_OPTION = "KEY_BT_OPTION";
    /**
     * 投注项列表
     */
    List<BetConfirmOption> betConfirmOptionList = new ArrayList<>();
    List<CgOddLimit> cgOddLimitList = new ArrayList<>();

    private BetConfirmOptionAdapter betConfirmOptionAdapter;
    private CgOddLimitSecAdapter cgOddLimitAdapter;
    private KeyboardView keyboardView;
    /**
     * 是否有已关闭的投注选项
     */
    private boolean hasCloseOption;
    private Runnable runnable;
    private int countdown = 5;
    private String platform = SPUtils.getInstance().getString(KEY_PLATFORM);

    private KeyBoardListener mListener = new KeyBoardListener() {
        @Override
        public void showKeyBoard(boolean isShow) {
            showOrHideKeyBoard(isShow);
        }

        @Override
        public void scroll(int height) {

        }
    };

    public void showOrHideKeyBoard(boolean isShow){
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams)binding.vSpace.getLayoutParams();
        if(isShow) {
            keyboardView.show();
            if(BtCarManager.isCg()) {
                binding.llKeyboard.postDelayed(() -> {
                    params.height = binding.llKeyboard.getMeasuredHeight() + ConvertUtils.dp2px(10);
                    binding.vSpace.setLayoutParams(params);
                }, 100);
            }
        }else{
            keyboardView.hide();
            params.height = 0;
            binding.vSpace.setLayoutParams(params);
        }
    }

    @Override
    public void initView() {
        binding.rvBtOption.setLayoutManager(new LinearLayoutManager(this.getContext()));
        //binding.rvBtCg.setLayoutManager(new LinearLayoutManager(this.getContext()));
        keyboardView = new KeyboardView(getContext(), null);
        keyboardView.setVisibility(View.GONE);
        keyboardView.setKeyBoardListener(mListener);
        keyboardView.setParent(binding.nsvOption);
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

        binding.tvPlatform.setText(SPUtils.getInstance().getString(KEY_PLATFORM_NAME));

        binding.ivBt.setOnClickListener(this);
        binding.ivDelete.setOnClickListener(this);
        binding.btnAddMatch.setOnClickListener(this);
        binding.ivReflesh.setOnClickListener(this);
    }

    @Override
    public void initData() {
        viewModel.getUserBalance();
        if(!BtCarManager.getBtCarList().isEmpty()) {
            betConfirmOptionList = BtCarManager.getBtCarList();
        }else {
            BetConfirmOption betConfirmOption = getArguments().getParcelable(KEY_BT_OPTION);
            betConfirmOptionList.add(betConfirmOption);
        }
        betConfirmOptionAdapter = new BetConfirmOptionAdapter(getContext(), betConfirmOptionList);
        binding.rvBtOption.setAdapter(betConfirmOptionAdapter);
        betConfirmOptionAdapter.setBtCarDialogFragment(this);
        binding.tvTimer.setText(String.valueOf(countdown));
        viewModel.batchBetMatchMarketOfJumpLine(betConfirmOptionList);
        if(BtCarManager.getBtCarList().isEmpty()) {
            binding.tvTimer.setVisibility(View.VISIBLE);
            runnable = () -> {
                binding.tvTimer.setText(String.valueOf(countdown--));
                if (countdown == -1) {
                    countdown = 5;
                }
                binding.tvTimer.postDelayed(runnable, 1000);
            };
            binding.tvTimer.postDelayed(runnable, 1000);
        }
    }
    int index = 0;
    @Override
    public void onResume() {
        super.onResume();
        viewModel.addSubscribe(Observable.interval(5, 5, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    //if(index == 0) {
                        batchBetMatchMarketOfJumpLine();
                    //}
                    //index ++;
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
            hasCloseOption = false;
            for (int i = 0; i < betConfirmOptionList.size(); i ++){
                if(!hasCloseOption){
                    hasCloseOption = betConfirmOptionList.get(i).isClose();
                }
                if (!TextUtils.equals(platform, PLATFORM_PM)) {
                    betConfirmOptionList.get(i).setRealData(betConfirmOptions.get(i).getRealData());
                }else{
                    for (BetConfirmOption option : betConfirmOptions){
                        if(TextUtils.equals(((BetConfirmOption)betConfirmOptionList.get(i)).getMatchId(), ((BetConfirmOption)option).getMatchId())){
                            betConfirmOptionList.get(i).setRealData(option.getRealData());
                            break;
                        }
                    }
                }
            }

            if(hasCloseOption){
                binding.ivConfirm.setEnabled(false);
            }else{
                binding.ivConfirm.setEnabled(true);
            }

            if (betConfirmOptionAdapter == null) {
                betConfirmOptionAdapter = new BetConfirmOptionAdapter(getContext(), betConfirmOptionList);
                binding.rvBtOption.setAdapter(betConfirmOptionAdapter);
            }else{
                betConfirmOptionAdapter.setNewData(betConfirmOptionList);
            }
        });
        viewModel.cgOddLimitDate.observe(this, cgOddLimits -> {

            if (cgOddLimitAdapter == null) {
                this.cgOddLimitList = cgOddLimits;
                cgOddLimitAdapter = new CgOddLimitSecAdapter(getContext(), cgOddLimits);
                cgOddLimitAdapter.setKeyBoardListener(mListener);
                cgOddLimitAdapter.setKeyboardView(keyboardView);
                binding.rvBtCg.setAdapter(cgOddLimitAdapter);
            }else{
                for (int i = 0; i < cgOddLimits.size(); i ++) {
                    cgOddLimits.get(i).setBtAmount(cgOddLimitList.get(i).getBtAmount());
                }
                this.cgOddLimitList = cgOddLimits;
                cgOddLimitAdapter.setNewData(cgOddLimits);
                cgOddLimitAdapter.setRefresh(true);
            }
        });
        viewModel.btResultInfoDate.observe(this, btResults -> {
            BtResultDialogFragment.getInstance(betConfirmOptionList, cgOddLimitList, btResults).show(getParentFragmentManager(), "BtResultDialogFragment");
            dismiss();
        });
        viewModel.userBalanceData.observe(this, balance -> {
            binding.tvBalance.setText(balance);
        });
    }

    public void batchBetMatchMarketOfJumpLine(){
        if(!BtCarManager.getBtCarList().isEmpty()) {
            betConfirmOptionList = BtCarManager.getBtCarList();
        }
        viewModel.batchBetMatchMarketOfJumpLine(betConfirmOptionList);
    }

    public interface KeyBoardListener{
        void showKeyBoard(boolean isShow);
        void scroll(int height);
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
                    BetConfirmOption betConfirmOption = BetConfirmOptionUtil.getInstance(betConfirmOptionList.get(0).getMatch(),
                            betConfirmOptionList.get(0).getPlayType(),
                            betConfirmOptionList.get(0).getPlayType().getOptionLists().get(0),
                            betConfirmOptionList.get(0).getOption());
                    betConfirmOption.setRealData(betConfirmOptionList.get(0).getRealData());
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
        else if(id == R.id.iv_reflesh){
            ObjectAnimator.ofFloat(binding.ivReflesh, "rotation", 0f, 360f).setDuration(700).start();

            /*ObjectAnimator animator = ObjectAnimator.ofFloat(binding.ivReflesh, "rotation", 0f, 359f).setDuration(700);
            animator.setRepeatMode(ValueAnimator.RESTART);
            animator.setRepeatCount(ValueAnimator.INFINITE);
            animator.start();*/

            viewModel.getUserBalance();
        }
    }

    @Override
    public TemplateBtCarViewModel initViewModel() {
        if (!TextUtils.equals(platform, PLATFORM_PM)) {
            AppViewModelFactory factory = AppViewModelFactory.getInstance((Application) Utils.getContext());
            return new ViewModelProvider(this, factory).get(FBBtCarViewModel.class);
        } else {
            PMAppViewModelFactory factory = PMAppViewModelFactory.getInstance((Application) Utils.getContext());
            return new ViewModelProvider(this, factory).get(PMBtCarViewModel.class);
        }
    }
}
