package com.xtree.mine.ui.fragment;

import android.app.Application;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.lifecycle.LifecycleOwner;

import com.google.gson.Gson;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;
import com.xtree.base.adapter.CacheViewHolder;
import com.xtree.base.adapter.CachedAutoRefreshAdapter;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.utils.CfLog;
import com.xtree.base.utils.StringUtils;
import com.xtree.base.utils.UuidUtil;
import com.xtree.base.vo.ProfileVo;
import com.xtree.base.widget.ListDialog;
import com.xtree.base.widget.LoadingDialog;
import com.xtree.base.widget.MsgDialog;
import com.xtree.base.widget.TipDialog;
import com.xtree.mine.R;
import com.xtree.mine.data.Injection;
import com.xtree.mine.databinding.DialogBankWithdrawalUsdtBinding;
import com.xtree.mine.ui.viewmodel.ChooseWithdrawViewModel;
import com.xtree.mine.vo.WithdrawVo.WithdrawalInfoVo;
import com.xtree.mine.vo.WithdrawVo.WithdrawalListVo;
import com.xtree.mine.vo.WithdrawVo.WithdrawalSubmitVo;
import com.xtree.mine.vo.WithdrawVo.WithdrawalVerifyVo;

import java.text.DecimalFormat;
import java.util.HashMap;

import me.xtree.mvvmhabit.utils.SPUtils;
import me.xtree.mvvmhabit.utils.ToastUtils;
import me.xtree.mvvmhabit.utils.Utils;
import project.tqyb.com.library_res.databinding.ItemTextBinding;

/**
 * USDT虚拟币提款
 */
public class USDTWithdrawalDialog extends BottomPopupView {
    public interface IUSDTDialogCallback {
        public void closeUSDTDialog();
    }

    private String wtype;//验证当前渠道信息时使用
    private LifecycleOwner owner;
    private WithdrawalListVo listVo;
    private WithdrawalInfoVo infoVo;
    private WithdrawalVerifyVo verifyVo;
    private WithdrawalSubmitVo submitVo;
    private ChooseWithdrawViewModel viewModel;
    private IUSDTDialogCallback iUSDTCallback;
    private BasePopupView ppwError; // 底部弹窗 (显示错误信息)
    private BasePopupView bindAddressPopView;//显示绑定提款地址PopView
    private ItemTextBinding itemTextBinding;//PopView 内部Binding
    private DialogBankWithdrawalUsdtBinding binding;
    private WithdrawalInfoVo.UserBankInfo selectorBankInfo;//选中的支付信息

    private ProfileVo mProfileVo;

    public USDTWithdrawalDialog(@NonNull Context context) {
        super(context);
    }

    public static USDTWithdrawalDialog newInstance(Context context, LifecycleOwner owner, final WithdrawalListVo listVo, WithdrawalInfoVo infoVo, final String wtype, final IUSDTDialogCallback iUSDTCallback) {
        USDTWithdrawalDialog dialog = new USDTWithdrawalDialog(context);
        dialog.owner = owner;
        dialog.listVo = listVo;
        dialog.infoVo = infoVo;
        dialog.wtype = wtype;
        dialog.iUSDTCallback = iUSDTCallback;
        return dialog;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_bank_withdrawal_usdt;
    }

    @Override
    protected int getMaxHeight() {
        return (XPopupUtils.getScreenHeight(getContext()) * 90 / 100);
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        initView();

        initData();
        initViewObservable();

        refreshUI(infoVo);
        initListener();

        String json = SPUtils.getInstance().getString(SPKeyGlobal.HOME_PROFILE);
        mProfileVo = new Gson().fromJson(json, ProfileVo.class);
    }

    private void initView() {

        hideKeyBoard();
        binding = DialogBankWithdrawalUsdtBinding.bind(findViewById(R.id.ll_root_usdt));
        if (listVo == null || TextUtils.isEmpty(listVo.title)) {
            binding.tvwTitle.setText(getContext().getString(R.string.txt_withdrawal_usdt_top));
        } else {
            binding.tvwTitle.setText(listVo.title);
        }

        binding.ivwBack.setOnClickListener(v -> {
            dismiss();
        });
        //设置请求
        binding.fragmentWithdrawalUsdtRequest.withdrawalRequestNext.setOnClickListener(v -> {
            checkVerify();

        });
        binding.fragmentWithdrawalUsdtRequest.withdrawalRequestCancel.setOnClickListener(v -> {
            iUSDTCallback.closeUSDTDialog();
            dismiss();
        });
        //第二部确认提款
        binding.fragmentWithdrawalUsdtConfirm.withdrawalConfirmNext.setOnClickListener(v -> {
            requestSubmit(verifyVo);
        });
        binding.fragmentWithdrawalUsdtConfirm.withdrawalConfirmCancel.setOnClickListener(v -> {
            binding.withdrawalUsdtRequest.setVisibility(View.VISIBLE);
            binding.withdrawalUsdtConfirm.setVisibility(View.GONE);
        });
        //结果页面
        binding.llWithdrawalResults.ivNext.setOnClickListener(v -> {
            iUSDTCallback.closeUSDTDialog();
            dismiss();
        });
        binding.llWithdrawalResults.ivClose.setOnClickListener(v -> {
            iUSDTCallback.closeUSDTDialog();
            dismiss();
        });
    }

    private void initData() {
        viewModel = new ChooseWithdrawViewModel((Application) Utils.getContext(), Injection.provideHomeRepository());
    }

    private void initViewObservable() {
        // 验证当前渠道信息
        viewModel.verifyVoMutableLiveData.observe(owner, vo -> {
            verifyVo = vo;
            if (verifyVo != null) {
                binding.withdrawalUsdtRequest.setVisibility(View.GONE);
                binding.withdrawalUsdtConfirm.setVisibility(View.VISIBLE);
                refreshVerifyUI(verifyVo);
            }

        });
        //完成提交
        viewModel.submitVoMutableLiveData.observe(owner, vo -> {
            submitVo = vo;
            refreshSubmitUI(submitVo);
        });

    }

    /**
     * 刷新渠道页面
     *
     * @param infoVo
     */
    private void refreshUI(WithdrawalInfoVo infoVo) {
        hideKeyBoard();
        if (infoVo == null || infoVo.user_bank_info == null || infoVo.user_bank_info.isEmpty()) {
            //返回数据异常
            ToastUtils.showError(getContext().getString(R.string.txt_network_error));
            return;
        } else {
            //账户数量
            String format = getContext().getResources().getString(R.string.txt_withdraw_top_number_tip);
            String textSource = String.format(format, String.valueOf(infoVo.user_bank_info.size()));
            binding.fragmentWithdrawalUsdtRequest.tvRequestNumberAccounts.setText(textSource);
            //绑定地址
            String bindAddressText = infoVo.user_bank_info.get(0).usdt_type + "--" + infoVo.user_bank_info.get(0).account;
            binding.fragmentWithdrawalUsdtRequest.tvBindAddress.setText(bindAddressText);
            binding.fragmentWithdrawalUsdtRequest.tvRequestAccount.setText(bindAddressText); //收款地址
            selectorBankInfo = infoVo.user_bank_info.get(0);

            //用户名
            String userName = infoVo.user_bank_info.get(0).user_name;
            String nickName = infoVo.user_bank_info.get(0).nickname;

            if (!TextUtils.isEmpty(userName)) {
                binding.fragmentWithdrawalUsdtRequest.tvUserName.setText(StringUtils.splitWithdrawUserName(userName));
            } else if (!TextUtils.isEmpty(nickName)) {
                binding.fragmentWithdrawalUsdtRequest.tvUserName.setText(StringUtils.splitWithdrawUserName(nickName));
            }
            //提款类型
            binding.fragmentWithdrawalUsdtRequest.tvAmountType.setText("USDT");
            //可提款金额
            binding.fragmentWithdrawalUsdtRequest.tvAmountNumber.setText(infoVo.quota);
            //提款金额
            binding.fragmentWithdrawalUsdtRequest.tvWithdrawalMoney.setText("0");
            //汇率
            binding.fragmentWithdrawalUsdtRequest.tvRequestRate.setText(infoVo.rate);
            //手续费
            if (infoVo.fee == null || TextUtils.isEmpty(infoVo.fee)) {
                binding.fragmentWithdrawalUsdtRequest.tvHandlingFee.setText("0");
            } else {
                binding.fragmentWithdrawalUsdtRequest.tvHandlingFee.setText(infoVo.fee);
            }
            //实际到账个数
            binding.fragmentWithdrawalUsdtRequest.tvActualNumber.setText("0");
            //顶部公告区域
            String formatStr = getContext().getResources().getString(R.string.txt_withdraw_top_tip);
            String count, userCount, totalAmount;
            count = "<font color=#EE5A5A>" + infoVo.day_rest_count + "</font>";
            userCount = "<font color=#000000>" + infoVo.day_used_amount + "</font>";
            totalAmount = "<font color=#000000>" + infoVo.day_rest_amount + "</font>";
            String textTipSource = String.format(formatStr, count, userCount, totalAmount);

            binding.fragmentWithdrawalUsdtRequest.tvTip.setText(HtmlCompat.fromHtml(textTipSource, HtmlCompat.FROM_HTML_MODE_LEGACY));
            //单笔取款范围
            String single = getContext().getString(R.string.txt_single_withdrawal_range);
            String moneyMin, moneyMax;
            moneyMin = "<font color=#F35A4E>" + infoVo.min_money + "</font>";
            moneyMax = "<font color=#F35A4E>" + infoVo.max_money + "</font>";
            String singleSource = String.format(single, moneyMin, moneyMax);
            binding.fragmentWithdrawalUsdtRequest.tvWithdrawalSingleShow.setText(HtmlCompat.fromHtml(singleSource, HtmlCompat.FROM_HTML_MODE_LEGACY));

            //输入框与实际到账个数之间转换
            binding.fragmentWithdrawalUsdtRequest.etInputMoney.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }
                @Override
                public void afterTextChanged(Editable s) {
                    // tv_withdrawal_money

                    //换算到账个数
                    String temp = s.toString();
                    if (temp != null && !TextUtils.isEmpty(temp)) {
                        Double f1 = Double.parseDouble(temp);
                        Double f2 = Double.parseDouble(infoVo.rate);
                        DecimalFormat df = new DecimalFormat("0.00");
                        binding.fragmentWithdrawalUsdtRequest.tvActualNumber.setText(df.format(f1 / f2));
                        binding.fragmentWithdrawalUsdtRequest.tvWithdrawalMoney.setText(s.toString().trim());
                    } else if (TextUtils.isEmpty(temp)) {
                        binding.fragmentWithdrawalUsdtRequest.tvActualNumber.setText("0");
                        binding.fragmentWithdrawalUsdtRequest.tvWithdrawalMoney.setText("0");
                    } else {
                        binding.fragmentWithdrawalUsdtRequest.tvActualNumber.setText("0");
                        binding.fragmentWithdrawalUsdtRequest.tvWithdrawalMoney.setText("0");
                    }
                }
            });
        }
    }

    private void refreshVerifyUI(final WithdrawalVerifyVo verifyVo) {

        //用户名
        String userName = verifyVo.user_bank_info.user_name;
        String nickName = verifyVo.user_bank_info.nickname;
        String proUserName = mProfileVo.username;
        if (!TextUtils.isEmpty(userName)) {
            binding.fragmentWithdrawalUsdtConfirm.tvUserName.setText(StringUtils.splitWithdrawUserName(userName));
        } else if (!TextUtils.isEmpty(nickName)) {
            binding.fragmentWithdrawalUsdtConfirm.tvUserName.setText(StringUtils.splitWithdrawUserName(nickName));
        } else if (!TextUtils.isEmpty(proUserName)) {
            binding.fragmentWithdrawalUsdtConfirm.tvUserName.setText(StringUtils.splitWithdrawUserName(proUserName));
        }
        //提款金额
        binding.fragmentWithdrawalUsdtConfirm.tvAmountMoney.setText(verifyVo.money_real);
        //提款类型
        binding.fragmentWithdrawalUsdtConfirm.tvWithdrawalType.setText(verifyVo.user_bank_info.usdt_type);
        //虚拟币类型
        binding.fragmentWithdrawalUsdtConfirm.tvVirtualType.setText(verifyVo.user_bank_info.usdt_type);
        //提款地址
        binding.fragmentWithdrawalUsdtConfirm.tvWithdrawalAddress.setText(verifyVo.user_bank_info.account);
        //手续费
        binding.fragmentWithdrawalUsdtConfirm.tvHandlingFee.setText(verifyVo.fee);

    }

    private void refreshSubmitUI(final WithdrawalSubmitVo submitVo) {
        binding.withdrawalUsdtConfirm.setVisibility(View.GONE);
        binding.withdrawalResults.setVisibility(View.VISIBLE);

        if (submitVo != null && submitVo.message != null && !TextUtils.isEmpty(submitVo.message)) {
            if (TextUtils.equals("账户提款申请成功", submitVo.message)) {
                binding.llWithdrawalResults.ivOverApply.setVisibility(VISIBLE);
                binding.llWithdrawalResults.ivOverApply.setBackgroundResource(R.mipmap.ic_over_apply);
                binding.llWithdrawalResults.tvOverMsg.setVisibility(VISIBLE);
                binding.llWithdrawalResults.tvOverMsg.setText(submitVo.message);
                binding.llWithdrawalResults.tvOverDetailedMsg.setVisibility(VISIBLE);

            } else if (TextUtils.equals("请刷新后重试", submitVo.message)) {
                binding.llWithdrawalResults.tvOverMsg.setVisibility(VISIBLE);
                binding.llWithdrawalResults.tvOverMsg.setText(submitVo.message);
                binding.llWithdrawalResults.ivOverApply.setVisibility(VISIBLE);
                binding.llWithdrawalResults.ivOverApply.setBackgroundResource(R.mipmap.ic_over_apply_err);
            } else {
                binding.llWithdrawalResults.tvOverMsg.setVisibility(VISIBLE);
                binding.llWithdrawalResults.tvOverMsg.setText(submitVo.message);
                binding.llWithdrawalResults.ivOverApply.setVisibility(VISIBLE);
                binding.llWithdrawalResults.ivOverApply.setBackgroundResource(R.mipmap.ic_over_apply_err);
            }
        } else if (submitVo == null) {
            ToastUtils.showError(getContext().getString(R.string.txt_network_error));
            return;
        }
    }

    /**
     * 初始化点击监听
     */
    private void initListener() {
        hideKeyBoard();
        binding.fragmentWithdrawalUsdtRequest.tvBindAddress.setOnClickListener(v -> {
            if (infoVo == null || infoVo.user_bank_info == null || infoVo.user_bank_info.isEmpty()) {
                ToastUtils.showError(getContext().getString(R.string.txt_network_error));
                return;
            } else {
                showBindAddress();
            }
        });

    }

    /**
     * 关闭键盘
     */
    public void hideKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(this.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 显示绑定提款地址PopView
     */
    private void showBindAddress() {
        CachedAutoRefreshAdapter adapter = new CachedAutoRefreshAdapter<WithdrawalInfoVo.UserBankInfo>() {
            @NonNull
            @Override
            public CacheViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                CacheViewHolder holder = new CacheViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_text, parent, false));
                return holder;
            }

            @Override
            public void onBindViewHolder(@NonNull CacheViewHolder holder, int position) {

                for (int i = 0; i < infoVo.user_bank_info.size(); i++) {
                    itemTextBinding = ItemTextBinding.bind(holder.itemView);
                    WithdrawalInfoVo.UserBankInfo vo = infoVo.user_bank_info.get(position);
                    String showMessage = vo.usdt_type + " -- " + vo.account;

                    selectorBankInfo = vo;

                    itemTextBinding.tvwTitle.setText(showMessage);
                    itemTextBinding.tvwTitle.setOnClickListener(v -> {
                        binding.fragmentWithdrawalUsdtRequest.tvBindAddress.setText(showMessage);
                        binding.fragmentWithdrawalUsdtRequest.tvRequestAccount.setText(showMessage); //收款地址

                        bindAddressPopView.dismiss();
                    });
                }
            }
        };
        adapter.clear();
        adapter.add(infoVo.user_bank_info);
        adapter.notifyDataSetChanged();
        String selectString = getContext().getString(R.string.txt_select_add);

        if (bindAddressPopView == null) {
        }
        bindAddressPopView = new XPopup.Builder(getContext()).asCustom(new ListDialog(getContext(), selectString, adapter, 40));
        bindAddressPopView.show();
    }

    /**
     * 验证当前渠道信息
     */
    private void checkVerify() {
        if (infoVo == null || infoVo.user_bank_info == null || infoVo.user_bank_info.isEmpty()) {
            ToastUtils.showError(getContext().getString(R.string.txt_network_error));
            return;
        } else {
            String money = binding.fragmentWithdrawalUsdtRequest.etInputMoney.getText().toString().trim();
            if (TextUtils.isEmpty(money)) {
                ToastUtils.showError(getContext().getString(R.string.txt_withdraw_input_error_tip));
                return;
            } else if (Double.valueOf(money) < Double.valueOf(infoVo.min_money)) {
                ToastUtils.showError(getContext().getString(R.string.txt_withdraw_input_mix_tip));
                return;
            } else if (Double.valueOf(money) > Double.valueOf(infoVo.max_money)) {
                ToastUtils.showError(getContext().getString(R.string.txt_withdraw_input_max_tip));
                return;
            } else if (selectorBankInfo == null) {
                ToastUtils.showError(getContext().getString(R.string.txt_withdraw_address_tip));
                return;
            } else {
                requestVerify(money, selectorBankInfo);
            }
        }

    }

    /**
     * 设置提款 请求 下一步
     */
    private void requestVerify(final String money, final WithdrawalInfoVo.UserBankInfo selectorBankInfo) {
        LoadingDialog.show(getContext());
        HashMap<String, Object> map = new HashMap<>();
        map.put("bank_id", selectorBankInfo.id);
        map.put("money", money);
        map.put("wtype", wtype);
        map.put("nonce", UuidUtil.getID24());
        CfLog.e("requestVerify -->" + map);
        viewModel.postWithdrawalVerify(map);
    }

    /**
     * 设置提款 完成申请
     */
    private void requestSubmit(final WithdrawalVerifyVo verifyVo) {
        LoadingDialog.show(getContext());
        HashMap<String, Object> map = new HashMap<>();
        map.put("bank_id", verifyVo.user_bank_info.id);
        map.put("money", verifyVo.money);
        map.put("wtype", wtype);
        map.put("nonce", UuidUtil.getID24());

        CfLog.e("requestSubmit -->" + map);

        viewModel.postWithdrawalSubmit(map);

    }

    /* 由于权限原因弹窗*/
    private void showErrorByChannel() {
        if (ppwError == null) {
            final String title = getContext().getString(R.string.txt_kind_tips);
            final String message = getContext().getString(R.string.txt_withdrawal_not_supported_tip);
            ppwError = new XPopup.Builder(getContext()).asCustom(new MsgDialog(getContext(), title, message, true, new TipDialog.ICallBack() {
                @Override
                public void onClickLeft() {
                    ppwError.dismiss();
                    dismiss();
                }

                @Override
                public void onClickRight() {
                    ppwError.dismiss();
                    dismiss();
                }
            }));
        }
        ppwError.show();
    }
}
