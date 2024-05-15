package com.xtree.mine.ui.fragment;

import android.app.Application;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.lifecycle.LifecycleOwner;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;
import com.xtree.base.adapter.CacheViewHolder;
import com.xtree.base.adapter.CachedAutoRefreshAdapter;
import com.xtree.base.utils.CfLog;
import com.xtree.base.utils.StringUtils;
import com.xtree.base.utils.UuidUtil;
import com.xtree.base.widget.ListDialog;
import com.xtree.base.widget.LoadingDialog;
import com.xtree.base.widget.MsgDialog;
import com.xtree.base.widget.TipDialog;
import com.xtree.mine.R;
import com.xtree.mine.data.Injection;
import com.xtree.mine.databinding.DialogBankWithdrawalVirtualBinding;
import com.xtree.mine.ui.viewmodel.ChooseWithdrawViewModel;
import com.xtree.mine.vo.WithdrawVo.WithdrawalInfoVo;
import com.xtree.mine.vo.WithdrawVo.WithdrawalListVo;
import com.xtree.mine.vo.WithdrawVo.WithdrawalSubmitVo;
import com.xtree.mine.vo.WithdrawVo.WithdrawalVerifyVo;

import java.util.ArrayList;
import java.util.HashMap;

import me.xtree.mvvmhabit.utils.ToastUtils;
import me.xtree.mvvmhabit.utils.Utils;
import project.tqyb.com.library_res.databinding.ItemTextBinding;

/**
 * 虚拟币提款
 */
public class VirtualWithdrawalDialog extends BottomPopupView {
    public interface IVirtualDialogCallback {
        public void closeVirtualDialog();
    }

    private String wtype;//验证当前渠道信息时使用
    private LifecycleOwner owner;
    private WithdrawalListVo listVo;
    private WithdrawalInfoVo infoVo;
    private WithdrawalInfoVo.UserBankInfo selectorBankInfo;
    private WithdrawalVerifyVo verifyVo;
    private WithdrawalSubmitVo submitVo;
    private ChooseWithdrawViewModel viewModel;
    private VirtualWithdrawalDialog.IVirtualDialogCallback iVirtualDialogCallback;
    @NonNull
    DialogBankWithdrawalVirtualBinding binding;

    private BasePopupView ppwError = null; // 底部弹窗 (显示错误信息)

    public VirtualWithdrawalDialog(@NonNull Context context) {
        super(context);
    }

    public static VirtualWithdrawalDialog newInstance(Context context, LifecycleOwner owner, final WithdrawalListVo listVo, final WithdrawalInfoVo infoVo, final String wtype, final VirtualWithdrawalDialog.IVirtualDialogCallback iebPayCallback) {
        VirtualWithdrawalDialog dialog = new VirtualWithdrawalDialog(context);
        dialog.owner = owner;
        dialog.listVo = listVo;
        dialog.infoVo = infoVo;
        dialog.wtype = wtype;
        dialog.iVirtualDialogCallback = iebPayCallback;
        return dialog;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_bank_withdrawal_virtual;
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

    }

    private void initView() {
        binding = DialogBankWithdrawalVirtualBinding.bind(findViewById(R.id.ll_root_virtual));
        binding.tvwTitle.setText(listVo.title);
        binding.ivwBack.setOnClickListener(v -> {
            dismiss();
        });
        refreshUI(infoVo);

    }

    private void initData() {
        LoadingDialog.show(getContext());
        viewModel = new ChooseWithdrawViewModel((Application) Utils.getContext(), Injection.provideHomeRepository());
    }

    private void initViewObservable() {
        // 验证当前渠道信息
        viewModel.verifyVoMutableLiveData.observe(owner, vo -> {
            verifyVo = vo;
            if (verifyVo != null) {
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
     * 验证当前渠道信息
     */
    private void checkVerify() {
        if (infoVo == null || infoVo.user_bank_info == null || infoVo.user_bank_info.isEmpty()) {
            ToastUtils.showError(getContext().getString(R.string.txt_network_error));
            return;
        } else {
            String money = binding.fragmentWithdrawalVitualRequest.etInputMoney.getText().toString().trim();
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
            binding.fragmentWithdrawalVitualRequest.tvRequestNumberAccounts.setText(textSource);
            //绑定地址
            String bindAddressText = infoVo.user_bank_info.get(0).usdt_type + "--" + infoVo.user_bank_info.get(0).account;
            binding.fragmentWithdrawalVitualRequest.tvBindAddress.setText(bindAddressText);
            binding.fragmentWithdrawalVitualRequest.tvRequestAccount.setText(bindAddressText); //收款地址
            selectorBankInfo = infoVo.user_bank_info.get(0);

            //用户名
            String userName = infoVo.user_bank_info.get(0).user_name;
            String nickName = infoVo.user_bank_info.get(0).nickname;

            if (!TextUtils.isEmpty(userName)) {
                binding.fragmentWithdrawalVitualRequest.tvUserName.setText(StringUtils.splitWithdrawUserName(userName));
            } else if (!TextUtils.isEmpty(nickName)) {
                binding.fragmentWithdrawalVitualRequest.tvUserName.setText(StringUtils.splitWithdrawUserName(nickName));
            }
            //提款类型
            binding.fragmentWithdrawalVitualRequest.tvAmountType.setText(listVo.type);
            //可提款金额
            binding.fragmentWithdrawalVitualRequest.tvAmountNumber.setText(infoVo.quota);
            //提款金额
            binding.fragmentWithdrawalVitualRequest.tvWithdrawalMoney.setText("0");

            //实际到账个数
            binding.fragmentWithdrawalVitualRequest.tvWithdrawalMoney.setText("0");
            //顶部公告区域
            String formatStr = getContext().getResources().getString(R.string.txt_withdraw_top_tip);
            String count, userCount, totalAmount;
            count = "<font color=#97A89E>" + infoVo.day_rest_count + "</font>";
            userCount = "<font color=#97A89E>" + infoVo.day_used_amount + "</font>";
            totalAmount = "<font color=#F35A4E>" + infoVo.day_rest_amount + "</font>";
            String textTipSource = String.format(formatStr, count, userCount, totalAmount);

            binding.fragmentWithdrawalVitualRequest.tvTip.setText(HtmlCompat.fromHtml(textTipSource, HtmlCompat.FROM_HTML_MODE_LEGACY));
            //单笔取款范围
            String single = getContext().getString(R.string.txt_single_withdrawal_range);
            String moneyMin, moneyMax;
            moneyMin = "<font color=#F35A4E>" + infoVo.min_money + "</font>";
            moneyMax = "<font color=#F35A4E>" + infoVo.max_money + "</font>";
            String singleSource = String.format(single, moneyMin, moneyMax);
            binding.fragmentWithdrawalVitualRequest.tvWithdrawalSingleShow.setText(HtmlCompat.fromHtml(singleSource, HtmlCompat.FROM_HTML_MODE_LEGACY));
            //地址
            binding.fragmentWithdrawalVitualRequest.tvBindAddress.setOnClickListener(v -> {
                showCollectionDialog(infoVo.user_bank_info);
            });
            //下一步
            binding.fragmentWithdrawalVitualRequest.withdrawalRequestNext.setOnClickListener(v -> {
                checkVerify();
            });
            //上一步
            binding.fragmentWithdrawalVitualRequest.withdrawalRequestCancel.setOnClickListener(v -> {
                dismiss();
            });
            initListener();
        }
    }

    private void refreshVerifyUI(final WithdrawalVerifyVo verifyVo) {
        binding.withdrawalVitualRequest.setVisibility(GONE);
        binding.withdrawalVitualConfirm.setVisibility(VISIBLE);
        //用户名
        String userName = verifyVo.user_bank_info.user_name;
        String nickName = verifyVo.user_bank_info.nickname;
        if (!TextUtils.isEmpty(userName)) {
            binding.fragmentWithdrawalVitualConfirm.tvUserName.setText(StringUtils.splitWithdrawUserName(userName));
        } else if (!TextUtils.isEmpty(nickName)) {
            binding.fragmentWithdrawalVitualConfirm.tvUserName.setText(StringUtils.splitWithdrawUserName(nickName));
        }
        //提款金额
        binding.fragmentWithdrawalVitualConfirm.tvAmountMoney.setText(verifyVo.money_real);
        //提款类型
        binding.fragmentWithdrawalVitualConfirm.tvWithdrawalType.setText(verifyVo.user_bank_info.usdt_type);
        //下一步
        binding.fragmentWithdrawalVitualConfirm.withdrawalConfirmNext.setOnClickListener(v -> {
            requestSubmit(verifyVo);
        });
        //上一步骤
        binding.fragmentWithdrawalVitualConfirm.withdrawalConfirmCancel.setOnClickListener(v -> {
            binding.withdrawalVitualConfirm.setVisibility(GONE);
            binding.withdrawalVitualRequest.setVisibility(VISIBLE);
        });
    }

    private void refreshSubmitUI(final WithdrawalSubmitVo submitVo) {
        binding.withdrawalVitualConfirm.setVisibility(GONE);
        binding.withdrawalResults.setVisibility(VISIBLE);
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
        //继续提款
        binding.llWithdrawalResults.ivNext.setOnClickListener(v -> {
            dismiss();
        });
        //返回
        binding.llWithdrawalResults.ivClose.setOnClickListener(v -> {
            dismiss();
        });
    }

    private void initListener() {
        hideKeyBoard();
        //提款金额输入框与提款金额显示View
        binding.fragmentWithdrawalVitualRequest.etInputMoney.clearFocus();
        binding.fragmentWithdrawalVitualRequest.etInputMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                //换算到账个数 tv_info_actual_number_show
                String temp = s.toString();
                if (temp != null && !TextUtils.isEmpty(temp)) {

                    binding.fragmentWithdrawalVitualRequest.tvWithdrawalMoney.setText(s.toString());
                } else if (TextUtils.isEmpty(temp)) {

                    binding.fragmentWithdrawalVitualRequest.tvWithdrawalMoney.setText("0");

                    binding.fragmentWithdrawalVitualRequest.tvWithdrawalMoney.setText("0");
                }
            }
        });
        //点击USDT收款地址 tv_bind_address
        binding.fragmentWithdrawalVitualRequest.tvBindAddress.setOnClickListener(v -> {
            showCollectionDialog(infoVo.user_bank_info);
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

    ItemTextBinding binding2;
    BasePopupView ppw = null; // 底部弹窗 (选择**菜单)

    /**
     * 显示USDT收款地址
     */
    private void showCollectionDialog(ArrayList<WithdrawalInfoVo.UserBankInfo> list) {
        CachedAutoRefreshAdapter adapter = new CachedAutoRefreshAdapter<WithdrawalInfoVo.UserBankInfo>() {
            @NonNull
            @Override
            public CacheViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                CacheViewHolder holder = new CacheViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_text, parent, false));
                return holder;
            }

            @Override
            public void onBindViewHolder(@NonNull CacheViewHolder holder, int position) {
                binding2 = ItemTextBinding.bind(holder.itemView);
                WithdrawalInfoVo.UserBankInfo vo = get(position);
                String showMessage = vo.usdt_type + " -- " + vo.account;

                binding2.tvwTitle.setText(showMessage);
                binding2.tvwTitle.setOnClickListener(v -> {

                    binding.fragmentWithdrawalVitualRequest.tvBindAddress.setText(showMessage);
                    binding.fragmentWithdrawalVitualRequest.tvRequestAccount.setText(showMessage); //收款地址

                    selectorBankInfo = vo;

                    ppw.dismiss();
                });
            }
        };
        adapter.clear();
        adapter.addAll(list);
        String selectString = getContext().getString(R.string.txt_select_add);
        ppw = new XPopup.Builder(getContext()).asCustom(new ListDialog(getContext(), selectString, adapter, 40 , true));
        ppw.show();
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
