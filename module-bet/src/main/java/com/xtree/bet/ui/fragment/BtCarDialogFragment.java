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

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.xtree.base.utils.NumberUtils;
import com.xtree.base.utils.TagUtils;
import com.xtree.base.widget.MsgDialog;
import com.xtree.bet.R;
import com.xtree.bet.bean.ui.BetConfirmOption;
import com.xtree.bet.bean.ui.BetConfirmOptionUtil;
import com.xtree.bet.bean.ui.CgOddLimit;
import com.xtree.bet.databinding.BtLayoutBtCarBinding;
import com.xtree.bet.manager.BtCarManager;
import com.xtree.bet.ui.activity.BtDetailActivity;
import com.xtree.bet.ui.adapter.BetConfirmOptionAdapter;
import com.xtree.bet.ui.adapter.CgOddLimitSecAdapter;
import com.xtree.bet.ui.viewmodel.TemplateBtCarViewModel;
import com.xtree.bet.ui.viewmodel.factory.AppViewModelFactory;
import com.xtree.bet.ui.viewmodel.factory.PMAppViewModelFactory;
import com.xtree.bet.ui.viewmodel.fb.FBBtCarViewModel;
import com.xtree.bet.ui.viewmodel.pm.PMBtCarViewModel;
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
    private BasePopupView basePopupView;
    private BasePopupView ppw;

    private KeyBoardListener mKeyBoardListener = new KeyBoardListener() {
        @Override
        public void showKeyBoard(boolean isShow) {
            showOrHideKeyBoard(isShow);
        }

        @Override
        public void scroll(int height) {

        }
    };

    private CgOddLimitSecAdapter.TextChangedListener mTextChangedListener = () -> {
        double btAmount = 0;
        double btWin = 0;
        for (CgOddLimit cgOddLimit : cgOddLimitList) {
            btAmount += cgOddLimit.getBtAmount();
            double odd = cgOddLimitList.size() > 1 ? cgOddLimit.getCOdd() : cgOddLimit.getDOdd();
            btWin += odd * cgOddLimit.getBtAmount();
        }
        if (isAdded()) {//BtCarDialogFragment消失后，禁止调用binding
            binding.tvBtAmount.setText(getString(R.string.bt_bt_pay_1, NumberUtils.format(btAmount, 2)));
            binding.tvTopWin.setText(getString(R.string.bt_bt_top_win, NumberUtils.format(btWin, 2)));
        }
    };

    public void showOrHideKeyBoard(boolean isShow) {
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) binding.vSpace.getLayoutParams();
        if (isShow) {
            keyboardView.show();
            if (BtCarManager.isCg()) {
                binding.llKeyboard.postDelayed(() -> {
                    params.height = binding.llKeyboard.getMeasuredHeight() + ConvertUtils.dp2px(10);
                    binding.vSpace.setLayoutParams(params);
                }, 100);
            }
        } else {
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
        keyboardView.setKeyBoardListener(mKeyBoardListener);
        keyboardView.setParent(binding.nsvOption);
        binding.ivConfirm.setCallBack(() -> {
            int acceptOdds = binding.cbAccept.isChecked() ? 1 : 2;
            viewModel.bet(betConfirmOptionList, cgOddLimitList, acceptOdds);
        });
        binding.llRoot.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            if (BtCarManager.isCg()) {
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) binding.llKeyboard.getLayoutParams();
                if (params == null) {
                    params = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                }
                params.bottomMargin = binding.cslBottom.getMeasuredHeight();
                binding.llKeyboard.setLayoutParams(params);
            }
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
        binding.ivTip.setOnClickListener(this);

    }

    @Override
    public void initData() {
        viewModel.getUserBalance();
        if (BtCarManager.isCg()) {
            betConfirmOptionList = BtCarManager.getBtCarList();
        } else {
            BetConfirmOption betConfirmOption = getArguments().getParcelable(KEY_BT_OPTION);
            betConfirmOptionList.add(betConfirmOption);
        }
        /*if (!BtCarManager.getBtCarList().isEmpty()) {
            betConfirmOptionList = BtCarManager.getBtCarList();
        } else {
            BetConfirmOption betConfirmOption = getArguments().getParcelable(KEY_BT_OPTION);
            betConfirmOptionList.add(betConfirmOption);
        }*/
        if (betConfirmOptionList.size() > 1) {
            binding.ivDelete.setVisibility(View.VISIBLE);
        } else {
            binding.ivDelete.setVisibility(View.GONE);
        }
        betConfirmOptionAdapter = new BetConfirmOptionAdapter(getContext(), betConfirmOptionList);
        binding.rvBtOption.setAdapter(betConfirmOptionAdapter);
        betConfirmOptionAdapter.setBtCarDialogFragment(this);
        binding.tvTimer.setText(String.valueOf(countdown));
        viewModel.batchBetMatchMarketOfJumpLine(betConfirmOptionList);
        if (!BtCarManager.isCg()) {
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
        viewModel.addSubscribe(Observable.interval(6, 6, TimeUnit.SECONDS)
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
            for (int i = 0; i < betConfirmOptionList.size(); i++) {
                if (!hasCloseOption) {
                    hasCloseOption = betConfirmOptionList.get(i).isClose();
                }
                if (!TextUtils.equals(platform, PLATFORM_PM)) {
                    betConfirmOptionList.get(i).setRealData(betConfirmOptions.get(i).getRealData());
                } else {
                    for (BetConfirmOption option : betConfirmOptions) {
                        if (TextUtils.equals(((BetConfirmOption) betConfirmOptionList.get(i)).getMatchId(), ((BetConfirmOption) option).getMatchId())) {
                            betConfirmOptionList.get(i).setRealData(option.getRealData());
                            break;
                        }
                    }
                }
            }

            if (hasCloseOption) {
                binding.ivConfirm.setEnabled(false);
                binding.ivBtBg.setEnabled(false);
                binding.ivBt.setEnabled(false);
            } else {
                binding.ivConfirm.setEnabled(true);
                binding.ivBtBg.setEnabled(true);
                binding.ivBt.setEnabled(true);
            }

            if (betConfirmOptionAdapter == null) {
                betConfirmOptionAdapter = new BetConfirmOptionAdapter(getContext(), betConfirmOptionList);
                binding.rvBtOption.setAdapter(betConfirmOptionAdapter);
            } else {
                betConfirmOptionAdapter.setNewData(betConfirmOptionList);
            }
        });
        viewModel.cgOddLimitDate.observe(this, cgOddLimits -> {
            if (cgOddLimitAdapter == null) {
                this.cgOddLimitList = cgOddLimits;
                cgOddLimitAdapter = new CgOddLimitSecAdapter(getContext(), cgOddLimits, new CgOddLimitSecAdapter.ICallBack() {
                    @Override
                    public void onClick(CgOddLimit vo) {
                        showCollusionTip(vo);
                    }
                });
                cgOddLimitAdapter.setKeyBoardListener(mKeyBoardListener);
                cgOddLimitAdapter.setTextChangedListener(mTextChangedListener);
                cgOddLimitAdapter.setKeyboardView(keyboardView);
                binding.rvBtCg.setAdapter(cgOddLimitAdapter);
            } else {
                if (cgOddLimitList.size() == cgOddLimits.size()) {
                    for (int i = 0; i < cgOddLimits.size(); i++) {
                        cgOddLimits.get(i).setBtAmount(cgOddLimitList.get(i).getBtAmount());
                    }
                }
                this.cgOddLimitList = cgOddLimits;
                cgOddLimitAdapter.setNewData(this.cgOddLimitList);
            }
        });
        viewModel.btResultInfoDate.observe(this, btResults -> {
            TagUtils.tagEvent(getContext(), "bt", platform);
            BtResultDialogFragment.getInstance(betConfirmOptionList, cgOddLimitList, btResults).show(getParentFragmentManager(), "BtResultDialogFragment");
            BtCarManager.clearBtCar();
            dismiss();
        });
        viewModel.userBalanceData.observe(this, balance -> {
            binding.tvBalance.setText(balance);
        });
        viewModel.noBetAmountDate.observe(this, unused -> {
            ToastUtils.showShort(R.string.bt_txt_no_amount);
        });
    }

    public void batchBetMatchMarketOfJumpLine() {
        if (BtCarManager.isCg()) {
            betConfirmOptionList = BtCarManager.getBtCarList();
        }
        viewModel.batchBetMatchMarketOfJumpLine(betConfirmOptionList);
    }

    public interface KeyBoardListener {
        void showKeyBoard(boolean isShow);

        void scroll(int height);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.iv_bt) {
            if (BtCarManager.isCg() && !BtCarManager.isEmpty()) {
                if (getContext() instanceof BtDetailActivity) {
                    BtCarManager.setIsCg(false);
                    BtCarManager.clearBtCar();
                } else {
                    viewModel.gotoToday();
                }
                binding.btnAddMatch.setVisibility(View.GONE);
                dismiss();
            } else {
                if (!betConfirmOptionList.isEmpty()) {
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
                    if (getContext() instanceof BtDetailActivity) {
                        viewModel.finish();
                    }
                    dismiss();
                }
            }
        } else if (id == R.id.iv_delete) {
            BtCarManager.clearBtCar();
            dismiss();
        } else if (id == R.id.btn_add_match) {
            dismiss();
        } else if (id == R.id.iv_reflesh) {
            ObjectAnimator.ofFloat(binding.ivReflesh, "rotation", 0f, 360f).setDuration(700).start();

            /*ObjectAnimator animator = ObjectAnimator.ofFloat(binding.ivReflesh, "rotation", 0f, 359f).setDuration(700);
            animator.setRepeatMode(ValueAnimator.RESTART);
            animator.setRepeatCount(ValueAnimator.INFINITE);
            animator.start();*/

            viewModel.getUserBalance();
        } else if (id == R.id.iv_tip) {
            showTip();
        }
    }

    /**
     * 启动自动接受更好赔率提示
     */
    private void showTip() {
        if (basePopupView == null) {
            basePopupView = new XPopup.Builder(getContext())
                    .dismissOnTouchOutside(false)
                    .asCustom(new TipOddsDialog(getContext(), () -> basePopupView.dismiss()));
        }
        basePopupView.show();
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

    /**
     * // 串关子单个数，如 投注4场比赛的3串1*4，此字段为4，全串关（4串11×11），则为11
     * var in: Int = 0
     * // 串关子单选项个数，如：投注4场比赛的3串1，此字段为3，如果是全串关（4串11×11），则为0
     * var sn: Int = 0
     * // 串关，最小投注额
     * var mi: String = ""
     * // 串关，最大投注额
     * var mx: String = ""
     * // 串关对应的赔率
     * var sodd: String = ""
     * //vo.getCgCount();sn
     * //vo.getBtCount();in
     */
    private void showCollusionTip(CgOddLimit vo) {
        int orderCount = betConfirmOptionList.size();
        int n = vo.getCgCount() == 0 ? orderCount : vo.getCgCount();
        int i = vo.getCgCount() == 0 ? vo.getBtCount() : 1;
        String title = "什么是" + vo.getCgName();
        String msg;

        //Log.i("count", orderCount + "   " + vo.getCgCount());
        if (orderCount == vo.getCgCount()) {// 全部串成一个
            msg = vo.getCgName() + "是有" + n + "场比赛组成的1个注单\n" +
                    "选择" + orderCount + "场赛事投注" + n + "串" + i + "时，系统将所选的" + orderCount + "场赛事合并为一个注单，" + orderCount + "场赛事必须全赢才可获得盈利";
        } else {// 拆分
            if (vo.getCgCount() == 0) {
                // 全串关
                // 比如 选了 4个选项，全串关 则是
                // 有 c 个2串一，有 d 个3串一，有 e 个4串一，有 f 个5串一，有 g 个6串一，有 h 个7串一，有 i 个8串一，有 j 个9串一，有 k 个10串一
                ArrayList<String> tempStrArr = new ArrayList<>();
                for (int j = 2; j <= n; j++) {
                    tempStrArr.add(combination(n, n - j + 2) + "个" + (n - j + 2) + "串1");
                }
                msg = vo.getCgName() + "是有" + n + "场比赛组成的" + i + "个注单    " +
                        "选择" + orderCount + "场赛事投注" + n + "串" + i + "时，系统将从" + orderCount + "场赛事中拆出" + String.join("，", tempStrArr)
                        + "，一个串关为1单，一共" + i + "个注单 ";

            } else {// n 串 1
                msg = vo.getCgName() + "是有" + n + "场比赛组成的一个注单\n" +
                        "选择" + orderCount + "场赛事投注" + n + "串" + i + "时，系统将所选的" + orderCount + "场赛事中拆出" + n + "个串关为1单，一共"
                        + vo.getBtCount() + "个注单";
            }
        }

        MsgDialog dialog = new MsgDialog(getContext(), title, msg, true, new MsgDialog.ICallBack() {
            @Override
            public void onClickLeft() {
            }

            @Override
            public void onClickRight() {
                ppw.dismiss();
            }
        });
        ppw = new XPopup.Builder(getContext())
                .dismissOnTouchOutside(true)
                .dismissOnBackPressed(true)
                .asCustom(dialog);
        ppw.show();
    }

    // 计算阶乘
    public static int factorial(int n) {
        if (n == 0 || n == 1) {
            return 1;
        }
        int result = 1;
        for (int i = 1; i <= n; i++) {
            result *= i;
        }
        return result;
    }

    // 计算组合数 C(n, k)
    public static int combination(int n, int k) {
        return factorial(n) / (factorial(k) * factorial(n - k));
    }
}
