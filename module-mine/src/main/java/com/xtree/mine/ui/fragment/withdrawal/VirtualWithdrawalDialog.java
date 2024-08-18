package com.xtree.mine.ui.fragment.withdrawal;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
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
import com.xtree.mine.R;
import com.xtree.mine.data.Injection;
import com.xtree.mine.databinding.DialogBankWithdrawalVirtualBinding;
import com.xtree.mine.ui.viewmodel.ChooseWithdrawViewModel;
import com.xtree.mine.vo.withdrawals.WithdrawalInfoVo;
import com.xtree.mine.vo.withdrawals.WithdrawalListVo;
import com.xtree.mine.vo.withdrawals.WithdrawalSubmitVo;
import com.xtree.mine.vo.withdrawals.WithdrawalVerifyVo;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import me.xtree.mvvmhabit.utils.SPUtils;
import me.xtree.mvvmhabit.utils.ToastUtils;
import me.xtree.mvvmhabit.utils.Utils;
import project.tqyb.com.library_res.databinding.ItemTextBinding;

/**
 * 虚拟币提款
 */
public class VirtualWithdrawalDialog extends BottomPopupView {
    private String checkCode;
    private String usdtType;

    private LifecycleOwner owner;
    ChooseWithdrawViewModel viewModel;
    private String wtype;
    private WithdrawalInfoVo.UserBankInfo selectorBankInfo;//选中的支付地址
    private WithdrawalListVo.WithdrawalItemVo listVo;
    private WithdrawalInfoVo infoVo;

    private WithdrawalVerifyVo verifyVo;
    private WithdrawalSubmitVo submitVo;
    @NonNull
    DialogBankWithdrawalVirtualBinding binding;
    private BankWithdrawalDialog.BankWithdrawalClose bankWithdrawalClose;
    private ProfileVo mProfileVo;
    private String userid;
    private BasePopupView ppwError;//显示异常弹窗
    private BasePopupView errorPopView;

    public VirtualWithdrawalDialog(@NonNull Context context) {
        super(context);
    }

    public static VirtualWithdrawalDialog newInstance(Context context,
                                                      LifecycleOwner owner,
                                                      final String wtype,
                                                      final WithdrawalListVo.WithdrawalItemVo listVo,
                                                      final WithdrawalInfoVo infoVo,
                                                      final String checkCode,
                                                      final String usdtType) {
        VirtualWithdrawalDialog dialog = new VirtualWithdrawalDialog(context);
        dialog.owner = owner;
        dialog.wtype = wtype;
        dialog.listVo = listVo;
        dialog.infoVo = infoVo;
        dialog.checkCode = checkCode;
        dialog.usdtType = usdtType;

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
        String json = SPUtils.getInstance().getString(SPKeyGlobal.HOME_PROFILE);
        mProfileVo = new Gson().fromJson(json, ProfileVo.class);

    }

    private void initView() {
        binding = DialogBankWithdrawalVirtualBinding.bind(findViewById(R.id.ll_root));
        binding.ivwClose.setOnClickListener(v -> dismiss());
        binding.tvwTitle.setText(listVo.name);
        refreshUI(infoVo);

    }

    /**
     * 刷新渠道页面
     *
     * @param infoVo
     */
    private void refreshUI(WithdrawalInfoVo infoVo) {
        hideKeyBoard();

        //用户名
        String userName = infoVo.user_bank_info.get(0).user_name;
        String nickName = infoVo.user_bank_info.get(0).nickname;

        if (!TextUtils.isEmpty(userName)) {
            binding.tvUserNameShow.setText(StringUtils.splitWithdrawUserName(userName));
        } else if (!TextUtils.isEmpty(nickName)) {
            binding.tvUserNameShow.setText(StringUtils.splitWithdrawUserName(nickName));
        }
        //提款类型
        binding.tvWithdrawalTypeShow.setText(infoVo.user_bank_info.get(0).usdt_type);
        //可提款金额
        binding.tvWithdrawalAmountShow.setText(infoVo.quota);
        //实际提款金额
        binding.tvInfoWithdrawalAmountShow.setText("0");

        //顶部公告区域
        String formatStr = getContext().getResources().getString(R.string.txt_withdraw_top_tip);
        String count, userCount, totalAmount;
        count = "<font color=#0C0319>" + infoVo.day_total_count + "</font>";
        userCount = "<font color=#F35A4E>" + infoVo.day_used_count + "</font>";
        totalAmount = "<font color=#F35A4E>" + infoVo.day_rest_amount + "</font>";
        String textTipSource = String.format(formatStr, count, userCount, totalAmount);
        binding.tvNotice.setText(HtmlCompat.fromHtml(textTipSource, HtmlCompat.FROM_HTML_MODE_LEGACY));

        //单笔取款范围
        String single = getContext().getString(R.string.txt_single_withdrawal_range);
        String moneyMin, moneyMax;
        moneyMin = "<font color=#F35A4E>" + infoVo.min_money + "</font>";
        moneyMax = "<font color=#F35A4E>" + infoVo.max_money + "</font>";
        String singleSource = String.format(single, moneyMin, moneyMax);
        binding.tvWithdrawalSingleShow.setText(HtmlCompat.fromHtml(singleSource, HtmlCompat.FROM_HTML_MODE_LEGACY));
        //收款地址
        if (infoVo.user_bank_info != null && !infoVo.user_bank_info.isEmpty()) {
            String showAddress = infoVo.user_bank_info.get(0).usdt_type + "--" + infoVo.user_bank_info.get(0).account;
            binding.tvBindAddress.setText(showAddress);
            //设置默认提款地址
            selectorBankInfo = infoVo.user_bank_info.get(0);
        } else {
            CfLog.e("****************** infoVo.user_bank_info is  null *********** ");
        }

        initListener();

    }

    private void initData() {
       // LoadingDialog.show(getContext());
        viewModel = new ChooseWithdrawViewModel((Application) Utils.getContext(), Injection.provideHomeRepository());
    }

    /**
     * 验证当前渠道信息
     */
    private void checkVerify() {
        if (infoVo == null || infoVo.user_bank_info == null || infoVo.user_bank_info.isEmpty()) {
            ToastUtils.showError(getContext().getString(R.string.txt_network_error));

            return;
        } else {
            String money = binding.etInputMoney.getText().toString().trim();
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
        map.put("check", checkCode);
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
        map.put("check", checkCode);
        map.put("wtype", wtype);
        map.put("nonce", UuidUtil.getID24());

        CfLog.e("requestSubmit -->" + map);

        viewModel.postWithdrawalSubmit(map);

    }

    /**
     * 刷新确认提款信息
     *
     * @param verifyVo
     */
    private void refreshVerifyUI(final WithdrawalVerifyVo verifyVo) {
        //刷新顶部进度条颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            binding.tvSetWithdrawalRequest.setTextColor(getContext().getColor(R.color.red));
            binding.tvConfirmWithdrawalRequest.setTextColor(getContext().getColor(R.color.red));
        }
        binding.llSetRequestView.setVisibility(GONE);
        binding.llVirtualConfirmView.setVisibility(VISIBLE);
        //用户名
        String userName = verifyVo.user_bank_info.user_name;
        String nickName = verifyVo.user_bank_info.nickname;
        if (!TextUtils.isEmpty(userName)) {
            binding.tvName.setText(StringUtils.splitWithdrawUserName(userName));
        } else if (!TextUtils.isEmpty(nickName)) {
            binding.tvName.setText(StringUtils.splitWithdrawUserName(nickName));
        }
        //可提款金额
        binding.tvWithdrawalAmount.setText(verifyVo.quota);

        //提款金额
        binding.tvWithdrawalActualAmount.setText(verifyVo.money_real);
        //实际提款
        binding.tvWithdrawalRequestAmount.setText(verifyVo.money_real);
        //提款类型
        binding.tvVirtualMoneyType.setText(verifyVo.user_bank_info.usdt_type);
        //提款地址
        binding.tvWithdrawalAddressShow.setText(verifyVo.user_bank_info.account);
        //手续费
        binding.tvWithdrawalHandlingFeeShow.setText(verifyVo.fee);
        //下一步
        binding.ivConfirmNext.setOnClickListener(v -> {
            requestSubmit(verifyVo);
        });
        //上一步骤
        binding.ivConfirmPrevious.setOnClickListener(v -> {
            binding.llSetRequestView.setVisibility(VISIBLE);
            binding.llVirtualConfirmView.setVisibility(GONE);
            //刷新顶部进度条颜色
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                binding.tvSetWithdrawalRequest.setTextColor(getContext().getColor(R.color.red));
                binding.tvConfirmWithdrawalRequest.setTextColor(getContext().getColor(R.color.cl_over_tip));
            }
        });
    }

    /**
     * 刷新提款结果页
     *
     * @param submitVo
     */
    private void refreshSubmitUI(final WithdrawalSubmitVo submitVo, final String message) {

        //刷新顶部进度条颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            binding.tvSetWithdrawalRequest.setTextColor(getContext().getColor(R.color.red));
            binding.tvConfirmWithdrawalRequest.setTextColor(getContext().getColor(R.color.red));
            binding.tvOverWithdrawalRequest.setTextColor(getContext().getColor(R.color.red));
        }
        binding.llVirtualConfirmView.setVisibility(GONE);
        binding.llOverApply.setVisibility(VISIBLE);

        //继续提款
        binding.ivContinueConfirmNext.setOnClickListener(v -> {
            dismiss();
        });
        //返回
        binding.ivContinueConfirmPrevious.setOnClickListener(v -> {
            dismiss();
        });

        if (submitVo != null) {
            if (submitVo != null && submitVo.message != null && !TextUtils.isEmpty(submitVo.message)) {
                if (TextUtils.equals("账户提款申请成功", submitVo.message)) {
                    binding.ivOverApply.setVisibility(VISIBLE);
                    binding.ivOverApply.setBackgroundResource(R.mipmap.ic_over_apply);
                    binding.tvOverMsg.setVisibility(VISIBLE);
                    binding.tvOverMsg.setText(submitVo.message);

                } else if (TextUtils.equals("请刷新后重试", submitVo.message)) {
                    binding.tvOverMsg.setVisibility(VISIBLE);
                    binding.tvOverMsg.setText(submitVo.message);
                    binding.ivOverApply.setVisibility(VISIBLE);
                    binding.ivOverApply.setBackgroundResource(R.mipmap.ic_over_apply_err);
                } else {
                    binding.tvOverMsg.setVisibility(VISIBLE);
                    binding.tvOverMsg.setText(submitVo.message);
                    binding.ivOverApply.setVisibility(VISIBLE);
                    binding.ivOverApply.setBackgroundResource(R.mipmap.ic_over_apply_err);
                }
            }
        } else if (message != null && !TextUtils.isEmpty(message)) {
            if (TextUtils.equals("账户提款申请成功", message)) {
                binding.ivOverApply.setVisibility(VISIBLE);
                binding.ivOverApply.setBackgroundResource(R.mipmap.ic_over_apply);
                binding.tvOverMsg.setVisibility(VISIBLE);
                if (TextUtils.isEmpty(message)) {
                    binding.tvOverMsg.setText(message);
                }
            } else if (TextUtils.equals("请刷新后重试", message)) {
                binding.tvOverMsg.setVisibility(VISIBLE);
                if (TextUtils.isEmpty(message)) {
                    binding.tvOverMsg.setText(message);
                }
                binding.ivOverApply.setVisibility(VISIBLE);
                binding.ivOverApply.setBackgroundResource(R.mipmap.ic_over_apply_err);
            } else {
                binding.tvOverMsg.setVisibility(VISIBLE);
                if (TextUtils.isEmpty(message)) {
                    binding.tvOverMsg.setText(message);
                }
                binding.ivOverApply.setVisibility(VISIBLE);
                binding.ivOverApply.setBackgroundResource(R.mipmap.ic_over_apply_err);
            }
        }

    }

    private void initViewObservable() {
        hideKeyBoard();
       /* //虚拟币提款设置提款请求 返回model
        viewModel.virtualCashMoYuVoMutableLiveData.observe(owner, vo -> {
            virtualCashVo = vo;
            if (virtualCashVo.msg_type == 1 || virtualCashVo.msg_type == 2) {
                if (virtualCashVo.msg_type == 2 && !TextUtils.isEmpty(virtualCashVo.message)) {
                    showErrorDialog(virtualCashVo.message);
                } else {
                    ToastUtils.showError(getContext().getString(R.string.txt_network_error));
                    dismiss();
                }
                return;
            }
            refreshSetUI();
        });
        //虚拟币确认提款信息
        viewModel.virtualSecurityMoYuVoMutableLiveData.observe(owner, vo -> {
            virtualSecurityVo = vo;

            if (virtualSecurityVo.datas == null
                    || (!TextUtils.isEmpty(virtualSecurityVo.message) && virtualSecurityVo.message.contains("抱歉"))
                    || "2".equals(virtualSecurityVo.msg_type)) {
                showErrorDialog(virtualSecurityVo.message);
            } else {
                refreshSecurityUI();
            }
        });
        //虚拟币完成申请
        viewModel.virtualConfirmMuYuVoMutableLiveData.observe(owner, vo -> {
            TagUtils.tagEvent(getContext(), "wd", "vc");
            virtualConfirmVo = vo;
            refreshConfirmUI();
        });*/
        // 验证当前渠道信息
        viewModel.verifyVoMutableLiveData.observe(owner, vo -> {
            verifyVo = vo;
            if (verifyVo != null) {
                refreshVerifyUI(verifyVo);
            } else {
                ToastUtils.showError(getContext().getString(R.string.txt_network_error));
            }

        });
        // 验证当前渠道信息 错误信息
        viewModel.verifyVoErrorData.observe(owner, vo -> {
            final String message = vo;
            if (message != null && !TextUtils.isEmpty(message)) {
                showErrorDialog(message);
            } else {
                ToastUtils.showError(getContext().getString(R.string.txt_network_error));
            }
        });
        //完成提交
        viewModel.submitVoMutableLiveData.observe(owner, vo -> {
            submitVo = vo;
            if (submitVo != null) {
                if (submitVo != null && TextUtils.isEmpty(submitVo.message)) {
                    refreshSubmitUI(submitVo, null);
                } else if (!TextUtils.isEmpty(submitVo.message) && TextUtils.equals(getContext().getString(R.string.txt_withdraw_submit_tip), submitVo.message)) {
                    refreshSubmitUI(submitVo, null);
                } else if (!TextUtils.isEmpty(submitVo.message) && !TextUtils.equals(getContext().getString(R.string.txt_withdraw_submit_tip), submitVo.message)) {
                    ToastUtils.showError(submitVo.message);
                } else {
                    ToastUtils.showError(getContext().getString(R.string.txt_network_error));
                }
            }

        });
        //提款完成申请 错误信息
        viewModel.submitVoErrorData.observe(owner, vo -> {
            final String message = vo;
            if (message != null && !TextUtils.isEmpty(message)) {
                refreshSubmitUI(null, message);
            } else {
                ToastUtils.showError(getContext().getString(R.string.txt_network_error));
            }
        });
        //完成提交 状态确认页面
        viewModel.submitVoErrorData.observe(owner, vo -> {
            final String message = vo;
            if (message != null && !TextUtils.isEmpty(message)) {
                refreshSubmitUI(null, message);
            } else {
                ToastUtils.showError(getContext().getString(R.string.txt_network_error));
            }
        });

    }


    /**
     * 显示异常Dialog
     */
    private void showErrorDialog(String showMessage) {
        if (showMessage == null) {
            return;
        }
        errorPopView = new XPopup.Builder(getContext())
                .asCustom(new MsgDialog(getContext(), getContext().getString(R.string.txt_kind_tips), showMessage, false, new MsgDialog.ICallBack() {
                    @Override
                    public void onClickLeft() {
                        errorPopView.dismiss();
                        //callBack.closeDialog();
                    }

                    @Override
                    public void onClickRight() {
                        errorPopView.dismiss();
                        // callBack.closeDialog();
                    }
                }));
        errorPopView.show();
    }

    private void initListener() {
        hideKeyBoard();
        //提款金额输入框与提款金额显示View
        binding.etInputMoney.clearFocus();
        binding.etInputMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                binding.tvInfoWithdrawalAmountShow.setText(s.toString());
                //换算到账个数 tv_info_actual_number_show
                String temp = s.toString();
                if (temp != null && !TextUtils.isEmpty(temp) && infoVo.fee != null && TextUtils.equals("0", infoVo.fee)) {
                    float f1 = Float.parseFloat(temp);
                    float f2 = Float.parseFloat(infoVo.fee);

                    DecimalFormat df = new DecimalFormat("0.00");
                    df.format(f1 / f2);
                    binding.tvInfoActualNumberShow.setText(df.format(f1 / f2));
                } else if (TextUtils.isEmpty(temp)) {
                    binding.tvInfoActualNumberShow.setText("0");
                } else {
                    binding.tvInfoActualNumberShow.setText(temp);
                }
            }
        });
        //点击USDT收款地址
        binding.llCollectionUsdtInput.setOnClickListener(v -> {
            showCollectionDialog(infoVo, infoVo.user_bank_info);
        });
        //点击下一步
        binding.ivNext.setOnClickListener(v -> {
            checkVerify();
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
    private void showCollectionDialog(final WithdrawalInfoVo infoVo, ArrayList<WithdrawalInfoVo.UserBankInfo> list) {
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
                String showMessage = vo.usdt_type + "--" + vo.account;

                binding2.tvwTitle.setText(showMessage);
                binding2.tvwTitle.setOnClickListener(v -> {
                    binding.tvBindAddress.setText(showMessage);

                    String single = getContext().getString(R.string.txt_single_withdrawal_range);
                    String moneyMin, moneyMax;
                    moneyMin = "<font color=#F35A4E>" + infoVo.min_money + "</font>";
                    moneyMax = "<font color=#F35A4E>" + infoVo.max_money + "</font>";
                    String singleSource = String.format(single, moneyMin, moneyMax);
                    binding.tvWithdrawalSingleShow.setText(HtmlCompat.fromHtml(singleSource, HtmlCompat.FROM_HTML_MODE_LEGACY));

                    selectorBankInfo = vo;

                    ppw.dismiss();
                });
            }
        };
        adapter.clear();
        adapter.addAll(list);
        String selectString = getContext().getString(R.string.txt_select_add);
        ppw = new XPopup.Builder(getContext()).asCustom(new ListDialog(getContext(), selectString, adapter, 40));
        ppw.show();
    }

}
