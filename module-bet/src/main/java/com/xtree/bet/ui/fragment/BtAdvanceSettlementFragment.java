package com.xtree.bet.ui.fragment;

import static com.xtree.base.utils.BtDomainUtil.KEY_PLATFORM;
import static com.xtree.base.utils.BtDomainUtil.PLATFORM_PM;

import android.app.Application;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.gyf.immersionbar.ImmersionBar;
import com.xtree.base.utils.NumberUtils;
import com.xtree.bet.R;
import com.xtree.bet.bean.ui.BtResult;
import com.xtree.bet.databinding.BtDialogAdvanceSettlementBinding;
import com.xtree.bet.ui.viewmodel.TemplateBtRecordModel;
import com.xtree.bet.ui.viewmodel.factory.AppViewModelFactory;
import com.xtree.bet.ui.viewmodel.factory.PMAppViewModelFactory;
import com.xtree.bet.ui.viewmodel.fb.FBBtRecordModel;
import com.xtree.bet.ui.viewmodel.pm.PMBtRecordModel;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import me.xtree.mvvmhabit.base.BaseDialogFragment;
import me.xtree.mvvmhabit.utils.SPUtils;
import me.xtree.mvvmhabit.utils.ToastUtils;
import me.xtree.mvvmhabit.utils.Utils;

/**
 * 提前结算弹窗
 * Created by goldze on 2018/6/21
 */
public class BtAdvanceSettlementFragment extends BaseDialogFragment<BtDialogAdvanceSettlementBinding, TemplateBtRecordModel> {
    private final static String KEY_CAPITAL = "key_capital";
    private final static String KEY_BACK = "key_back";
    private final static String KEY_ID = "key_id";
    private final static String KEY_ACCEPT_ODDS_CHANGE = "key_accept_odds_change";
    private final static String KEY_PARLAY = "key_parlay";
    private final static String KEY_UNIT_CASHOUT_PAY_OUTSTAKE = "key_unit_cashout_pay_outstake";

    /**
     * 本金
     */
    private double mCaptialAmount;
    /**
     * 返还金额
     */
    private double mBackAmount;
    /**
     * 订单ID
     */
    private String mOrderId;
    /**
     * 是否串关
     */
    private boolean mParlay;
    /**
     * 是否接受下注时真实价格低于下注价格
     */
    private boolean mAcceptoddschange;
    /**
     * 提前结算报价接口返回的"单位提前结算价格"
     */
    private double mUnitCashOutPayoutStake;
    private String mPlatform = SPUtils.getInstance().getString(KEY_PLATFORM);
    private BtResult mBtResult;
    private Disposable timerDisposable;
    private DialogInterface.OnDismissListener mOnClickListener;


    public static BtAdvanceSettlementFragment getInstance(double captialAmount, double backAmount, String orderId, boolean acceptoddschange, boolean parlay, double unitCashOutPayoutStake) {
        BtAdvanceSettlementFragment instance = new BtAdvanceSettlementFragment();
        Bundle bundle = new Bundle();
        bundle.putDouble(KEY_CAPITAL, captialAmount);
        bundle.putDouble(KEY_BACK, backAmount);
        bundle.putString(KEY_ID, orderId);
        bundle.putBoolean(KEY_ACCEPT_ODDS_CHANGE, acceptoddschange);
        bundle.putBoolean(KEY_PARLAY, parlay);
        bundle.putDouble(KEY_UNIT_CASHOUT_PAY_OUTSTAKE, unitCashOutPayoutStake);
        instance.setArguments(bundle);
        return instance;
    }

    public void setOnDismissListener (DialogInterface.OnDismissListener listener) {
        //设置关闭弹框的回调
        mOnClickListener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =  super.onCreateView(inflater, container, savedInstanceState);
        getDialog().setCanceledOnTouchOutside(false);
        return v;
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.bt_dialog_advance_settlement;

    }

    @Override
    public TemplateBtRecordModel initViewModel() {
        if (!TextUtils.equals(mPlatform, PLATFORM_PM)) {
            AppViewModelFactory factory = AppViewModelFactory.getInstance((Application) Utils.getContext());
            return new ViewModelProvider(this, factory).get(FBBtRecordModel.class);
        } else {
            PMAppViewModelFactory factory = PMAppViewModelFactory.getInstance((Application) Utils.getContext());
            return new ViewModelProvider(this, factory).get(PMBtRecordModel.class);
        }
    }

    @Override
    public void initView() {
        binding.tvConfirm.setOnClickListener(this);
        binding.tvCancel.setOnClickListener(this);
    }

    @Override
    public void initData() {
        mCaptialAmount = getArguments().getDouble(KEY_CAPITAL);
        mBackAmount = getArguments().getDouble(KEY_BACK);
        mOrderId = getArguments().getString(KEY_ID);
        mAcceptoddschange = getArguments().getBoolean(KEY_ACCEPT_ODDS_CHANGE);
        mParlay = getArguments().getBoolean(KEY_PARLAY);
        mUnitCashOutPayoutStake = getArguments().getDouble(KEY_UNIT_CASHOUT_PAY_OUTSTAKE);
        binding.tvCost.setText(getString(R.string.bt_txt_btn_statement_amount, NumberUtils.format(mCaptialAmount, 2)));
        binding.tvBack.setText(getString(R.string.bt_txt_btn_statement_amount, NumberUtils.format(mBackAmount, 2)));
    }

    public void updatePrice(BtResult btResult){
        mCaptialAmount = btResult.getBtAmount();
        mBackAmount = btResult.getAdvanceSettleAmount();
        mUnitCashOutPayoutStake = btResult.getUnitCashOutPayoutStake();
        binding.tvCost.setText(getString(R.string.bt_txt_btn_statement_amount, NumberUtils.format(mCaptialAmount, 2)));
        binding.tvBack.setText(getString(R.string.bt_txt_btn_statement_amount, NumberUtils.format(mBackAmount, 2)));
        mBtResult = btResult;
    }

    @Override
    public void initViewObservable() {
        viewModel.btUpdateCashOutBet.observe(this, id -> {
            binding.getRoot().post(() -> initTimer(id));
        });
        viewModel.btUpdateCashOutStatus.observe(this, isSecucess -> {
            if(isSecucess){
                ToastUtils.showShort("提前兑现成功");
            }else {
                ToastUtils.showShort("提前兑现失败，请稍等重试");
            }
            mOnClickListener.onDismiss(getDialog());
            dismiss();
            if (timerDisposable != null) {
                viewModel.removeSubscribe(timerDisposable);
            }
        });
    }

    private void initTimer(String id) {
        if (timerDisposable != null) {
            viewModel.removeSubscribe(timerDisposable);
        }
        timerDisposable = Observable.interval(1, 2, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    viewModel.getCashOutsByIds(id);
                });
        viewModel.addSubscribe(timerDisposable);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.tv_confirm){
            binding.tvConfirm.setText("兑现中......");
            binding.tvCancel.setEnabled(false);
            viewModel.cashOutPricebBet(mOrderId, mCaptialAmount, mUnitCashOutPayoutStake, mAcceptoddschange, mParlay);
        } else if (id == R.id.tv_cancel) {
            dismiss();
        }
    }
}
