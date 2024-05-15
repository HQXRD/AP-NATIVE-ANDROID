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
import com.xtree.mine.databinding.DialogWithdrawalEbpayBinding;
import com.xtree.mine.ui.viewmodel.ChooseWithdrawViewModel;
import com.xtree.mine.vo.WithdrawVo.WithdrawalInfoVo;
import com.xtree.mine.vo.WithdrawVo.WithdrawalListVo;
import com.xtree.mine.vo.WithdrawVo.WithdrawalSubmitVo;
import com.xtree.mine.vo.WithdrawVo.WithdrawalVerifyVo;

import java.util.ArrayList;
import java.util.HashMap;

import me.xtree.mvvmhabit.utils.SPUtils;
import me.xtree.mvvmhabit.utils.ToastUtils;
import me.xtree.mvvmhabit.utils.Utils;
import project.tqyb.com.library_res.databinding.ItemTextBinding;

/**
 * EBPay虚拟币提款
 */
public class EBPayWithdrawalDialog extends BottomPopupView {

    public interface IEBPayCallback {
        public void closeEBPayDialog();
    }

    private Context context;
    private LifecycleOwner owner;
    private WithdrawalListVo listVo;
    private WithdrawalInfoVo infoVo;
    private WithdrawalVerifyVo verifyVo;
    private WithdrawalSubmitVo submitVo;
    private String wtype;//验证当前渠道信息时使用
    private IEBPayCallback iebPayCallback;
    private WithdrawalInfoVo.UserBankInfo selectUsdtInfo;//选中的支付地址
    private ChooseWithdrawViewModel viewModel;
    @NonNull
    DialogWithdrawalEbpayBinding binding;

    private BasePopupView ppwError = null; // 底部弹窗 (显示错误信息)
    private ProfileVo mProfileVo;
    private ItemTextBinding itemTextBinding;
    private BasePopupView bindAddressPopView = null; // 底部弹窗 (选择**菜单)

    public EBPayWithdrawalDialog(@NonNull Context context) {
        super(context);
    }

    public static EBPayWithdrawalDialog newInstance(Context context, LifecycleOwner owner, final WithdrawalListVo listVo, final WithdrawalInfoVo infoVo, final String wtype, final IEBPayCallback iebPayCallback) {
        EBPayWithdrawalDialog dialog = new EBPayWithdrawalDialog(context);
        dialog.owner = owner;
        dialog.listVo = listVo;
        dialog.infoVo = infoVo;
        dialog.wtype = wtype;
        dialog.iebPayCallback = iebPayCallback;
        return dialog;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_withdrawal_ebpay;
    }

    @Override
    protected int getMaxHeight() {
        return (XPopupUtils.getScreenHeight(getContext()) * 90 / 100);
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        initView(infoVo);
        initData();
        initViewObservable();

        String json = SPUtils.getInstance().getString(SPKeyGlobal.HOME_PROFILE);
        mProfileVo = new Gson().fromJson(json, ProfileVo.class);
    }

    private void initView(final WithdrawalInfoVo infoVo) {
        binding = DialogWithdrawalEbpayBinding.bind(findViewById(R.id.ll_root_ebpay));
        if (listVo == null || TextUtils.isEmpty(listVo.title)) {
            binding.tvwTitle.setText(getContext().getString(R.string.txt_withdrawal_ebpay_top));
        } else {
            binding.tvwTitle.setText(listVo.title);
        }

        binding.ivwBack.setOnClickListener(v -> {
            dismiss();
        });
        if (infoVo == null || infoVo.user_bank_info == null || infoVo.user_bank_info.isEmpty()) {
            //返回数据异常
            ToastUtils.showError(getContext().getString(R.string.txt_network_error));
            return;
        } else {
            //账户数量
            String format = getContext().getResources().getString(R.string.txt_withdraw_top_number_tip);
            String textSource = String.format(format, String.valueOf(infoVo.user_bank_info.size()));
            binding.fragmentWithdrawalEbpayRequest.tvRequestNumber.setText(textSource);
            //绑定地址
            String bindAddressText = infoVo.user_bank_info.get(0).usdt_type + "" + infoVo.user_bank_info.get(0).account;
            binding.fragmentWithdrawalEbpayRequest.tvBindAddress.setText(bindAddressText);
            binding.fragmentWithdrawalEbpayRequest.tvRequestAccount.setText(bindAddressText); //收款地址
            selectUsdtInfo = infoVo.user_bank_info.get(0);

            //实际提款金额
            binding.fragmentWithdrawalEbpayRequest.tvRequestMoney.setText("0");

            //顶部公告区域
            String formatStr = getContext().getResources().getString(R.string.txt_withdraw_top_tip);
            String count, userCount, totalAmount;
            count = "<font color=#97A89E>" + infoVo.day_rest_count + "</font>";
            userCount = "<font color=#97A89E>" + infoVo.day_used_amount + "</font>";
            totalAmount = "<font color=#F35A4E>" + infoVo.day_rest_amount + "</font>";
            String textTipSource = String.format(formatStr, count, userCount, totalAmount);
            binding.fragmentWithdrawalEbpayRequest.tvTip.setText(HtmlCompat.fromHtml(textTipSource, HtmlCompat.FROM_HTML_MODE_LEGACY));

            //单笔取款范围
            String single = getContext().getString(R.string.txt_single_withdrawal_range);
            String moneyMin, moneyMax;
            moneyMin = "<font color=#F35A4E>" + infoVo.min_money + "</font>";
            moneyMax = "<font color=#F35A4E>" + infoVo.max_money + "</font>";
            String singleSource = String.format(single, moneyMin, moneyMax);
            binding.fragmentWithdrawalEbpayRequest.tvWithdrawalSingleShow.setText(HtmlCompat.fromHtml(singleSource, HtmlCompat.FROM_HTML_MODE_LEGACY));

            initRequestListener();
        }

        //Test
        //第一步
        binding.fragmentWithdrawalEbpayRequest.withdrawalRequestNext.setOnClickListener(v -> {

            checkVerify();

        });
        binding.fragmentWithdrawalEbpayRequest.withdrawalRequestCancel.setOnClickListener(v -> {
            hideKeyBoard();
            dismiss();
        });

        //第二步 下一步
        binding.fragmentWithdrawalEbpayConfirm.withdrawalConfirmNext.setOnClickListener(v -> {
            /*binding.withdrawalEbpayConfirm.setVisibility(View.GONE);
            binding.withdrawalResults.setVisibility(View.VISIBLE);*/
            requestSubmit(verifyVo);
        });
        //第二步 上一步
        binding.fragmentWithdrawalEbpayConfirm.withdrawalConfirmCancel.setOnClickListener(v -> {
            binding.withdrawalEbpayConfirm.setVisibility(View.GONE);
            binding.withdrawalEbpayRequest.setVisibility(View.VISIBLE);
        });
        //第三步
        binding.llWithdrawalResults.ivNext.setOnClickListener(v -> {
            iebPayCallback.closeEBPayDialog();
            dismiss();
        });
        binding.llWithdrawalResults.ivClose.setOnClickListener(v -> {
            iebPayCallback.closeEBPayDialog();
            dismiss();
        });
    }

    private void initData() {
        viewModel = new ChooseWithdrawViewModel((Application) Utils.getContext(), Injection.provideHomeRepository());
    }

    private void initViewObservable() {
        hideKeyBoard();
        viewModel.verifyVoMutableLiveData.observe(owner, vo -> {
            verifyVo = vo;
            refreshSecurityUI(verifyVo);
        });
        viewModel.submitVoMutableLiveData.observe(owner, vo -> {
            submitVo = vo;
            if (submitVo != null
                    && submitVo.status == 0
                    && !TextUtils.isEmpty(submitVo.message)
                    && TextUtils.equals(getContext().getString(R.string.txt_withdrawal_close_info), submitVo.message)) {
                //渠道关闭
                ToastUtils.showError(submitVo.message);
                return;
            } else {
                refreshConfirmUI(submitVo);
            }

        });
    }

    private void initRequestListener() {
        hideKeyBoard();
        //提款金额输入框与提款金额显示View
        binding.fragmentWithdrawalEbpayRequest.etInputMoney.clearFocus();
        binding.fragmentWithdrawalEbpayRequest.etInputMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String temp = s.toString();
                if (temp != null && !TextUtils.isEmpty(temp)) {
                    binding.fragmentWithdrawalEbpayRequest.tvRequestMoney.setText(s.toString());
                } else if (TextUtils.isEmpty(temp)) {
                    binding.fragmentWithdrawalEbpayRequest.tvRequestMoney.setText("0");
                } else {
                    binding.fragmentWithdrawalEbpayRequest.tvRequestMoney.setText("0");
                }
            }
        });
        //点击收款地址
        binding.fragmentWithdrawalEbpayRequest.tvBindAddress.setOnClickListener(v -> {
            if (infoVo == null || infoVo.user_bank_info == null || infoVo.user_bank_info.isEmpty()) {
                ToastUtils.showError(getContext().getString(R.string.txt_network_error));
                return;
            } else {
                showBindAddress(infoVo.user_bank_info);
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
     * 刷新确认提款UI
     */
    private void refreshSecurityUI(final WithdrawalVerifyVo verifyVo) {

        if (verifyVo != null && verifyVo.money != null && !TextUtils.isEmpty(verifyVo.money)) {

            binding.withdrawalEbpayRequest.setVisibility(View.GONE);
            binding.withdrawalEbpayConfirm.setVisibility(View.VISIBLE);

            //用户名
            String userName = verifyVo.user_bank_info.user_name;
            String nickName = verifyVo.user_bank_info.nickname;
            String proUserName = mProfileVo.username;
            if (!TextUtils.isEmpty(userName)) {
                binding.fragmentWithdrawalEbpayConfirm.tvUserName.setText(StringUtils.splitWithdrawUserName(userName));
            } else if (!TextUtils.isEmpty(nickName)) {
                binding.fragmentWithdrawalEbpayConfirm.tvUserName.setText(StringUtils.splitWithdrawUserName(nickName));
            } else if (!TextUtils.isEmpty(proUserName)) {
                binding.fragmentWithdrawalEbpayConfirm.tvUserName.setText(StringUtils.splitWithdrawUserName(proUserName));
            }

            binding.fragmentWithdrawalEbpayConfirm.tvConfirmAmountShow.setText(verifyVo.money_real);
            binding.fragmentWithdrawalEbpayConfirm.tvAmountType.setText(verifyVo.user_bank_info.usdt_type);
        }
        //下一步
        binding.fragmentWithdrawalEbpayConfirm.withdrawalConfirmNext.setOnClickListener(v -> {
            requestSubmit(verifyVo);
        });
        //上一步
        binding.fragmentWithdrawalEbpayConfirm.withdrawalConfirmCancel.setOnClickListener(v -> {
            binding.withdrawalEbpayRequest.setVisibility(View.VISIBLE);
            binding.withdrawalEbpayConfirm.setVisibility(View.GONE);
        });
    }

    /**
     * 刷新完成申请UI
     */
    private void refreshConfirmUI(final WithdrawalSubmitVo submitVo) {
        if (submitVo != null && submitVo.message != null && !TextUtils.isEmpty(submitVo.message)) {
            binding.withdrawalEbpayConfirm.setVisibility(View.GONE);
            binding.withdrawalResults.setVisibility(View.VISIBLE);

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

        }
        //继续提款
        binding.llWithdrawalResults.ivNext.setOnClickListener(v -> {
            dismiss();
        });
        //返回
        binding.llWithdrawalResults.ivClose.setOnClickListener(v -> {
            dismiss();
        });
    }

    /**
     * 显示USDT收款地址
     */
    private void showBindAddress(ArrayList<WithdrawalInfoVo.UserBankInfo> list) {
        CachedAutoRefreshAdapter adapter = new CachedAutoRefreshAdapter<WithdrawalInfoVo.UserBankInfo>() {
            @NonNull
            @Override
            public CacheViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                CacheViewHolder holder = new CacheViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_text, parent, false));
                return holder;
            }

            @Override
            public void onBindViewHolder(@NonNull CacheViewHolder holder, int position) {
                itemTextBinding = ItemTextBinding.bind(holder.itemView);
                WithdrawalInfoVo.UserBankInfo vo = get(position);

                String showMessage = vo.usdt_type + "--" + vo.account;

                itemTextBinding.tvwTitle.setText(showMessage);
                itemTextBinding.tvwTitle.setOnClickListener(v -> {
                    binding.fragmentWithdrawalEbpayRequest.tvBindAddress.setText(showMessage);
                    binding.fragmentWithdrawalEbpayRequest.tvRequestAccount.setText(showMessage);

                    selectUsdtInfo = vo;

                    bindAddressPopView.dismiss();
                });
            }
        };
        adapter.clear();
        adapter.addAll(list);
        String selectString = getContext().getString(R.string.txt_select_add);
        bindAddressPopView = new XPopup.Builder(getContext()).asCustom(new ListDialog(getContext(), selectString, adapter, 50 , true));
        bindAddressPopView.show();
    }

    private void checkVerify() {
        if (infoVo == null || infoVo.user_bank_info == null || infoVo.user_bank_info.isEmpty()) {
            ToastUtils.showError(getContext().getString(R.string.txt_network_error));
            return;
        } else {
            String money = binding.fragmentWithdrawalEbpayRequest.etInputMoney.getText().toString().trim();
            if (TextUtils.isEmpty(money)) {
                ToastUtils.showError(getContext().getString(R.string.txt_withdraw_input_error_tip));
                return;
            } else if (Double.valueOf(money) < Double.valueOf(infoVo.min_money)) {
                ToastUtils.showError(getContext().getString(R.string.txt_withdraw_input_mix_tip));
                return;
            } else if (Double.valueOf(money) > Double.valueOf(infoVo.max_money)) {
                ToastUtils.showError(getContext().getString(R.string.txt_withdraw_input_max_tip));
                return;
            } else if (selectUsdtInfo == null) {
                ToastUtils.showError(getContext().getString(R.string.txt_withdraw_address_tip));
                return;
            } else {
                requestVerify(money, selectUsdtInfo);
            }
        }
    }

    /**
     * 设置提款 请求 下一步
     */
    private void requestVerify(final String money, final WithdrawalInfoVo.UserBankInfo selectUsdtInfo) {
        LoadingDialog.show(getContext());

        HashMap<String, Object> map = new HashMap<>();
        map.put("bank_id", selectUsdtInfo.id);
        map.put("money", money);
        map.put("wtype", wtype);
        map.put("nonce", UuidUtil.getID24());
        CfLog.i("requestVerify -->" + map);
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

        CfLog.i("requestSubmit -->" + map);

        viewModel.postWithdrawalSubmit(map);

    }

    /* 由于权限原因弹窗*/
    private void showError() {
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
