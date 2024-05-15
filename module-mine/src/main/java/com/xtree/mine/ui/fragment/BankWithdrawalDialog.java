package com.xtree.mine.ui.fragment;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;
import com.xtree.base.adapter.CacheViewHolder;
import com.xtree.base.adapter.CachedAutoRefreshAdapter;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.utils.CfLog;
import com.xtree.base.utils.DomainUtil;
import com.xtree.base.utils.StringUtils;
import com.xtree.base.utils.UuidUtil;
import com.xtree.base.vo.ProfileVo;
import com.xtree.base.widget.ListDialog;
import com.xtree.base.widget.LoadingDialog;
import com.xtree.base.widget.MsgDialog;
import com.xtree.base.widget.TipDialog;
import com.xtree.mine.R;
import com.xtree.mine.data.Injection;
import com.xtree.mine.databinding.DialogBankWithdrawalBankBinding;
import com.xtree.mine.ui.viewmodel.ChooseWithdrawViewModel;
import com.xtree.mine.vo.WithdrawVo.WithdrawalBankInfoVo;
import com.xtree.mine.vo.WithdrawVo.WithdrawalListVo;
import com.xtree.mine.vo.WithdrawVo.WithdrawalSubmitVo;
import com.xtree.mine.vo.WithdrawVo.WithdrawalVerifyVo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import me.xtree.mvvmhabit.utils.SPUtils;
import me.xtree.mvvmhabit.utils.ToastUtils;
import me.xtree.mvvmhabit.utils.Utils;
import project.tqyb.com.library_res.databinding.ItemTextBinding;

/**
 * 银行卡提款Dialog
 */
public class BankWithdrawalDialog extends BottomPopupView implements BankRecyclerViewAdapter.IBankRecyclerCallback, IAmountCallback {

    /**
     * 点击头部选项卡回调
     *
     * @param selectVo
     */
    @Override
    public void callbackWithFruitHor(WithdrawalListVo selectVo) {
        if (selectVo != null) {
            selectorItemVo = selectVo;
            wtype = selectorItemVo.name;
            requestData(selectVo);
        }

    }

    /**
     * 点击固额会回调
     */
    @Override
    public void callbackWithAmount(String amount) {
        binding.fragmentWithdrawalBankRequest.fragmentWithdrawalBankNativeRequest.tvInputMoney.setText(amount);
        binding.fragmentWithdrawalBankRequest.fragmentWithdrawalBankNativeRequest.tvRequestNumber.setText(amount);
    }

    public interface IBankDialogCallback {
        public void closeBankDialog();
    }

    private Context mContext;
    private LifecycleOwner owner;
    private ArrayList<WithdrawalListVo> topWithdrawalList;//顶部提款选项
    private WithdrawalListVo selectorItemVo;//选中的顶部选项卡
    private WithdrawalBankInfoVo infoVo;
    private WithdrawalVerifyVo verifyVo;
    private WithdrawalSubmitVo submitVo;
    private String wtype;//验证当前渠道信息时使用
    private IBankDialogCallback iBankDialogCallback;
    private CashWithdrawalPopWindow webPopWindow;//webView 提款外部跳转浮窗
    private WithdrawalBankInfoVo.UserBankInfo selectUsdtInfo;//选中的支付地址
    private ChooseWithdrawViewModel viewModel;

    private BasePopupView ppw2;//绑卡
    private BasePopupView ppwError; // 底部弹窗 (显示错误信息)
    private BasePopupView ppwErrorByTime;
    private ProfileVo mProfileVo;
    private ItemTextBinding itemTextBinding;
    private BasePopupView bindAddressPopView = null; // 底部弹窗 (选择**菜单)
    DialogBankWithdrawalBankBinding binding;
    private BankRecyclerViewAdapter recyclerViewAdapter;
    private AmountViewViewAdapter amountViewViewAdapter;

    public static BankWithdrawalDialog newInstance(Context context, LifecycleOwner owner, final String wtype, ArrayList<WithdrawalListVo> infoVoList,
                                                   final WithdrawalBankInfoVo infoVo, final IBankDialogCallback iBankDialogCallback) {
        BankWithdrawalDialog dialog = new BankWithdrawalDialog(context);
        dialog.mContext = context;
        dialog.owner = owner;
        dialog.topWithdrawalList = infoVoList;
        dialog.infoVo = infoVo;
        dialog.wtype = wtype;
        dialog.iBankDialogCallback = iBankDialogCallback;
        return dialog;
    }

    public BankWithdrawalDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_bank_withdrawal_bank;
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

    private void initView(final WithdrawalBankInfoVo infoVo) {
        binding = DialogBankWithdrawalBankBinding.bind(findViewById(R.id.ll_bank_root));
        binding.ivwBack.setOnClickListener(v -> {
            iBankDialogCallback.closeBankDialog();
            dismiss();
        });
        binding.tvwTitle.setText(getContext().getString(R.string.txt_withdrawal_bank));
        hideKeyBoard();

        initTopView(topWithdrawalList);

        if (infoVo.fast_iframe_url != null && !TextUtils.isEmpty(infoVo.fast_iframe_url)) {
            refreshWebView(infoVo);
        } else if (TextUtils.isEmpty(infoVo.fast_iframe_url)) {
            refreshNativeView(infoVo);
        }
    }

    /**
     * 初始化顶部提款View
     */
    private void initTopView(final ArrayList<WithdrawalListVo> infoList) {
        for (int i = 0; i < infoList.size(); i++) {
            if (i == 0) {
                infoList.get(0).flag = true;
            } else {
                infoList.get(i).flag = false;
            }
        }
        recyclerViewAdapter = new BankRecyclerViewAdapter(getContext(), infoList, this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this.mContext);
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        binding.fragmentWithdrawalBankRequest.rvBankCard.setLayoutManager(layoutManager);
        binding.fragmentWithdrawalBankRequest.rvBankCard.addItemDecoration(new BankRecyclerViewAdapter.SpacesItemDecoration(10));
        binding.fragmentWithdrawalBankRequest.rvBankCard.setAdapter(recyclerViewAdapter);
        binding.fragmentWithdrawalBankRequest.rvBankCard.setItemAnimator(new DefaultItemAnimator());

    }

    /**
     * 注册监听
     */
    private void initRequestListener() {
        hideKeyBoard();
        //提款金额输入框与提款金额显示View
        binding.fragmentWithdrawalBankRequest.fragmentWithdrawalBankNativeRequest.etInputMoney.clearFocus();
        binding.fragmentWithdrawalBankRequest.fragmentWithdrawalBankNativeRequest.etInputMoney.addTextChangedListener(new TextWatcher() {
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
                    binding.fragmentWithdrawalBankRequest.fragmentWithdrawalBankNativeRequest.tvRequestNumber.setText(s.toString());
                } else if (TextUtils.isEmpty(temp)) {
                    binding.fragmentWithdrawalBankRequest.fragmentWithdrawalBankNativeRequest.tvRequestNumber.setText("0");
                } else {
                    binding.fragmentWithdrawalBankRequest.fragmentWithdrawalBankNativeRequest.tvRequestNumber.setText("0");
                }
            }
        });

    }

    /**
     * 显示USDT收款地址
     */
    private void showBindAddress(ArrayList<WithdrawalBankInfoVo.UserBankInfo> list) {
        CachedAutoRefreshAdapter adapter = new CachedAutoRefreshAdapter<WithdrawalBankInfoVo.UserBankInfo>() {
            @NonNull
            @Override
            public CacheViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                CacheViewHolder holder = new CacheViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_text, parent, false));
                return holder;
            }

            @Override
            public void onBindViewHolder(@NonNull CacheViewHolder holder, int position) {
                itemTextBinding = ItemTextBinding.bind(holder.itemView);
                WithdrawalBankInfoVo.UserBankInfo vo = get(position);
                selectUsdtInfo = vo;
                String showMessage = vo.bank_name + "--" + vo.account;

                itemTextBinding.tvwTitle.setText(showMessage);
                itemTextBinding.tvwTitle.setOnClickListener(v -> {
                    binding.fragmentWithdrawalBankRequest.fragmentWithdrawalBankNativeRequest.tvBindAddress.setText(showMessage);
                    binding.fragmentWithdrawalBankRequest.fragmentWithdrawalBankNativeRequest.tvPaymentNumber.setText(showMessage);

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

    /**
     * 关闭键盘
     */
    public void hideKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(this.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private void initData() {
        viewModel = new ChooseWithdrawViewModel((Application) Utils.getContext(), Injection.provideHomeRepository());
    }

    private void initViewObservable() {
        //刷新第一步 数据
        viewModel.bankInfoVoMutableLiveData.observe(owner, vo -> {
            infoVo = vo;
            if (!TextUtils.isEmpty(infoVo.message)) {

                ToastUtils.showError(infoVo.message);
                return;
            } else if (infoVo.fast_iframe_url != null && !TextUtils.isEmpty(infoVo.fast_iframe_url)) {
                refreshWebView(vo);
            } else if (TextUtils.isEmpty(infoVo.fast_iframe_url)) {
                refreshNativeView(vo);
            }
        });
        //提款第二步骤
        viewModel.verifyVoMutableLiveData.observe(owner, vo -> {

            verifyVo = vo;
            if (!TextUtils.isEmpty(verifyVo.message)) {

                ToastUtils.showError(verifyVo.message);
                return;
            } else {
                refreshSecurityUI(verifyVo);
            }

        });
        //提款第三步 显示提款结果
        viewModel.submitVoMutableLiveData.observe(owner, vo -> {
            submitVo = vo;
            refreshConfirmUI(submitVo);
        });
    }

    private void requestData(final WithdrawalListVo itemVo) {
        if (itemVo != null && !TextUtils.isEmpty(itemVo.name)) {
            LoadingDialog.show(getContext());
            selectorItemVo = itemVo;
            viewModel.getWithdrawalBankInfo(itemVo.name);
        }
    }

    /**
     * 刷新WebView页面
     *
     * @param infoVo
     */
    private void refreshWebView(final WithdrawalBankInfoVo infoVo) {
        binding.fragmentWithdrawalBankRequest.fragmentWithdrawalBankNativeRequest.llBankNativeRequest.setVisibility(GONE);
        binding.fragmentWithdrawalBankRequest.fragmentWithdrawalBankWebRequest.llBankWebRequest.setVisibility(VISIBLE);
        //ns_h5_view
        String url = infoVo.fast_iframe_url;
        if (!StringUtils.isStartHttp(url)) {
            url = DomainUtil.getDomain2() + url;
        }
        binding.fragmentWithdrawalBankRequest.fragmentWithdrawalBankWebRequest.nsH5View.loadUrl(url, getHeader());
        initWebView(binding.fragmentWithdrawalBankRequest.fragmentWithdrawalBankWebRequest.nsH5View);
        binding.fragmentWithdrawalBankRequest.fragmentWithdrawalBankWebRequest.nsH5View.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // LoadingDialog.show(getContext());
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                dismissLoading();
                binding.fragmentWithdrawalBankRequest.fragmentWithdrawalBankWebRequest.maskH5View.setVisibility(View.GONE);
            }
        });
    }

    /**
     * 刷新原生页面
     *
     * @param infoVo
     */
    private void refreshNativeView(final WithdrawalBankInfoVo infoVo) {
        binding.fragmentWithdrawalBankRequest.fragmentWithdrawalBankWebRequest.llBankWebRequest.setVisibility(GONE);
        binding.fragmentWithdrawalBankRequest.fragmentWithdrawalBankNativeRequest.llBankNativeRequest.setVisibility(VISIBLE);
        //tv_number_accounts
        //账户数量
        String format = getContext().getResources().getString(R.string.txt_withdraw_top_number_tip);
        String textSource = String.format(format, String.valueOf(infoVo.user_bank_info.size()));
        binding.fragmentWithdrawalBankRequest.fragmentWithdrawalBankNativeRequest.tvRequestNumber.setText(textSource);
        //默认支付id
        selectUsdtInfo = infoVo.user_bank_info.get(0);
        //支付银行
        final String bankName = infoVo.user_bank_info.get(0).bank_name;
        final String bankNum = infoVo.user_bank_info.get(0).account;
        binding.fragmentWithdrawalBankRequest.fragmentWithdrawalBankNativeRequest.tvBindAddress.setText(bankName + "--" + bankNum);
        binding.fragmentWithdrawalBankRequest.fragmentWithdrawalBankNativeRequest.tvBindAddress.setOnClickListener(v -> {
            showBindAddress(infoVo.user_bank_info);
        });

        //用户名
        if (infoVo.user_bank_info == null || infoVo.user_bank_info.isEmpty()) {
            final String name = StringUtils.splitWithdrawUserName(mProfileVo.username);
            binding.fragmentWithdrawalBankRequest.fragmentWithdrawalBankNativeRequest.tvUserName.setText(name);
        } else {
            final WithdrawalBankInfoVo.UserBankInfo userBankInfo = infoVo.user_bank_info.get(0);
            if (!TextUtils.isEmpty(userBankInfo.user_name)) {
                binding.fragmentWithdrawalBankRequest.fragmentWithdrawalBankNativeRequest.tvUserName.setText(userBankInfo.user_name);
            } else if (!TextUtils.isEmpty(userBankInfo.nickname)) {
                binding.fragmentWithdrawalBankRequest.fragmentWithdrawalBankNativeRequest.tvUserName.setText(userBankInfo.nickname);
            }
        }

        //提款类型
        binding.fragmentWithdrawalBankRequest.fragmentWithdrawalBankNativeRequest.tvConfirmAmount.setText(getContext().getString(R.string.txt_withdrawal_bank_confirm));
        //可提款金额
        binding.fragmentWithdrawalBankRequest.fragmentWithdrawalBankNativeRequest.tvAmountNumber.setText(infoVo.quota);

        //顶部公告区域
        String formatStr = getContext().getResources().getString(R.string.txt_withdraw_top_tip);
        String count, userCount, totalAmount;
        count = "<font color=#97A89E>" + infoVo.day_rest_count + "</font>";
        userCount = "<font color=#97A89E>" + infoVo.day_used_amount + "</font>";
        totalAmount = "<font color=#F35A4E>" + infoVo.day_rest_amount + "</font>";
        String textTipSource = String.format(formatStr, count, userCount, totalAmount);
        binding.fragmentWithdrawalBankRequest.fragmentWithdrawalBankNativeRequest.tvTip.setText(HtmlCompat.fromHtml(textTipSource, HtmlCompat.FROM_HTML_MODE_LEGACY));

        //单笔取款范围
        String single = getContext().getString(R.string.txt_single_withdrawal_range);
        String moneyMin, moneyMax;
        moneyMin = "<font color=#F35A4E>" + infoVo.min_money + "</font>";
        moneyMax = "<font color=#F35A4E>" + infoVo.max_money + "</font>";
        String singleSource = String.format(single, moneyMin, moneyMax);
        binding.fragmentWithdrawalBankRequest.fragmentWithdrawalBankNativeRequest.tvWithdrawalSingleShow.setText(HtmlCompat.fromHtml(singleSource, HtmlCompat.FROM_HTML_MODE_LEGACY));
        //实际提款金额 tv_request_number
        binding.fragmentWithdrawalBankRequest.fragmentWithdrawalBankNativeRequest.tvRequestNumber.setText("0");
        //收款账户 tv_payment_number
        binding.fragmentWithdrawalBankRequest.fragmentWithdrawalBankNativeRequest.tvPaymentNumber.setText(bankName + "--" + bankNum);
        //开启固定金额
        if (infoVo.money_fixed) {
            for (int i = 0; i < infoVo.amountVoList.size(); i++) {
                CfLog.e("infoVo.amountVoList Amount = " + infoVo.amountVoList.get(i).toString());
            }
            if (amountViewViewAdapter == null) {
                amountViewViewAdapter = new AmountViewViewAdapter(mContext,infoVo.amountVoList, this);
            }
            binding.fragmentWithdrawalBankRequest.fragmentWithdrawalBankNativeRequest.etInputMoney.setVisibility(GONE);
            binding.fragmentWithdrawalBankRequest.fragmentWithdrawalBankNativeRequest.tvInputMoney.setVisibility(VISIBLE);
            binding.fragmentWithdrawalBankRequest.fragmentWithdrawalBankNativeRequest.gvSelectAmountMore.setVisibility(VISIBLE);
            binding.fragmentWithdrawalBankRequest.fragmentWithdrawalBankNativeRequest.gvSelectAmountMore.setAdapter(amountViewViewAdapter);
        } else {
            binding.fragmentWithdrawalBankRequest.fragmentWithdrawalBankNativeRequest.tvInputMoney.setVisibility(GONE);
            binding.fragmentWithdrawalBankRequest.fragmentWithdrawalBankNativeRequest.gvSelectAmountMore.setVisibility(GONE);
            binding.fragmentWithdrawalBankRequest.fragmentWithdrawalBankNativeRequest.etInputMoney.setVisibility(VISIBLE);

            initRequestListener();
        }
        //第一步 的下一步
        binding.fragmentWithdrawalBankRequest.fragmentWithdrawalBankNativeRequest.withdrawalRequestNext.setOnClickListener(v -> {
            checkVerify();
        });
        binding.fragmentWithdrawalBankRequest.fragmentWithdrawalBankNativeRequest.withdrawalRequestCancel.setOnClickListener(v -> {
            dismiss();
        });
    }

    private void refreshSecurityUI(final WithdrawalVerifyVo verifyVo) {
        binding.withdrawalBankRequest.setVisibility(GONE);
        binding.withdrawalBankConfirm.setVisibility(VISIBLE);
        //用户名
        if (verifyVo.user_bank_info != null) {
            if (verifyVo.user_bank_info.user_name != null && !TextUtils.isEmpty(verifyVo.user_bank_info.user_name)) {
                binding.fragmentWithdrawalBankConfirm.tvUserName.setText(verifyVo.user_bank_info.user_name);
            } else if (verifyVo.user_bank_info.nickname != null && !TextUtils.isEmpty(verifyVo.user_bank_info.nickname)) {
                binding.fragmentWithdrawalBankConfirm.tvUserName.setText(verifyVo.user_bank_info.nickname);
            } else {
                binding.fragmentWithdrawalBankConfirm.tvUserName.setText(mProfileVo.username);
            }
        }
        //提款金额
        binding.fragmentWithdrawalBankConfirm.tvAmountShow.setText(verifyVo.money);
        //提款类型
        binding.fragmentWithdrawalBankConfirm.tvWithdrawalAmountType.setText(getContext().getString(R.string.txt_withdrawal_bank_confirm));
        //实际提款金额
        binding.fragmentWithdrawalBankConfirm.tvRealityMoney.setText(verifyVo.money_real);
        //手续费
        binding.fragmentWithdrawalBankConfirm.tvHandlingFee.setText(verifyVo.fee);
        //银行账号
        binding.fragmentWithdrawalBankConfirm.tvBankNumber.setText(verifyVo.user_bank_info.account);

        //下一步
        binding.fragmentWithdrawalBankConfirm.withdrawalConfirmNext.setOnClickListener(v -> {
            requestSubmit(verifyVo);
        });
        //上一步
        binding.fragmentWithdrawalBankConfirm.withdrawalConfirmCancel.setOnClickListener(v -> {
            binding.withdrawalBankRequest.setVisibility(VISIBLE);
            binding.withdrawalBankConfirm.setVisibility(GONE);
        });

    }

    /**
     * 刷新完成申请UI
     */
    private void refreshConfirmUI(final WithdrawalSubmitVo submitVo) {

        if (submitVo != null && submitVo.message != null && !TextUtils.isEmpty(submitVo.message)) {
            binding.withdrawalBankConfirm.setVisibility(View.GONE);
            binding.nsWithdrawalResults.setVisibility(View.VISIBLE);

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
     * 检查
     */
    private void checkVerify() {

        if (infoVo == null || infoVo.user_bank_info == null || infoVo.user_bank_info.isEmpty()) {
            ToastUtils.showError(getContext().getString(R.string.txt_network_error));
            return;
        } else {
            String money;
            if (binding.fragmentWithdrawalBankRequest.fragmentWithdrawalBankNativeRequest.etInputMoney.getVisibility() == VISIBLE) {
                money = binding.fragmentWithdrawalBankRequest.fragmentWithdrawalBankNativeRequest.etInputMoney.getText().toString().trim();
            } else {
                money = binding.fragmentWithdrawalBankRequest.fragmentWithdrawalBankNativeRequest.tvInputMoney.getText().toString().trim();
            }

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
    private void requestVerify(final String money, final WithdrawalBankInfoVo.UserBankInfo selectUsdtInfo) {
        if (money == null || TextUtils.isEmpty(money) || selectUsdtInfo == null || TextUtils.isEmpty(selectUsdtInfo.bank_id) || TextUtils.isEmpty(wtype)) {
            CfLog.e("设置提款 请求 下一步 参数有误");
            return;
        }

        LoadingDialog.show(getContext());

        HashMap<String, Object> map = new HashMap<>();
        map.put("bank_id", selectUsdtInfo.id);
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

        CfLog.i("requestSubmit -->" + map);

        viewModel.postWithdrawalSubmit(map);

    }

    private void showError(String errorMessage) {
        /*binding.llBankWithdrawalTop.setVisibility(View.GONE);
        binding.nsErrorView.setVisibility(View.GONE);
        binding.nsSetWithdrawalRequest.setVisibility(View.GONE);//单数据页面展示
        binding.nsSetWithdrawalRequestMore.setVisibility(View.GONE);//多金额页面隐藏
        binding.nsH5View.setVisibility(View.GONE);//h5隐藏
        binding.nsOverView.setVisibility(View.GONE); //订单结果页面隐藏
        binding.nsConfirmWithdrawalRequest.setVisibility(View.GONE); //确认提款页面隐藏
        binding.llBankWithdrawalNumberError.setVisibility(View.VISIBLE);//
        binding.tvShowNumberErrorMessage.setText(errorMessage);*/
    }

    /**
     * 初始化webView
     *
     * @param webView
     */
    private void initWebView(final WebView webView) {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setSupportZoom(true);

        //settings.setAppCacheEnabled(true);
        settings.setUseWideViewPort(true);
        //settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        //settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        settings.setLoadWithOverviewMode(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setLoadsImagesAutomatically(true);
        settings.setSupportZoom(true);
    }

    /**
     * 定义GridViewViewAdapter 显示大额固额金额选择
     */
    private static class AmountViewViewAdapter extends BaseAdapter {
        public IAmountCallback callback;
        private Context context;
        public ArrayList<WithdrawalBankInfoVo.WithdrawalAmountVo> arrayList;

        public AmountViewViewAdapter(Context context,ArrayList<WithdrawalBankInfoVo.WithdrawalAmountVo> list, IAmountCallback callback) {
            super();
            this.context = context;
            this.arrayList = list;
            this.callback = callback;
        }

        @Override
        public int getCount() {
            return this.arrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return arrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            HolderView holderView = null;
            if (view == null) {
                view = LayoutInflater.from(context).inflate(R.layout.dialog_bank_withdrawal_amount_child, parent, false);
                holderView = new HolderView();
                holderView.textView = (TextView) view.findViewById(R.id.tvw_title);
               // holderView.linear = (LinearLayout) view.findViewById(R.id.cl_title);
                view.setTag(holderView);
            } else {
                holderView = (HolderView) view.getTag();
            }
/*            finalHolderView.linear.setBackgroundResource(R.mipmap.bg_money_btn_selector);
        } else {
            //finalHolderView.linear.setBackgroundResource(R.drawable.bg_dialog_top_bank_amount_noselected);
            finalHolderView.linear.setBackgroundResource(R.mipmap.bg_money_btn_noselector);*/
         /*
            if (arrayList.get(position).flag) {
                holderView.linear.setBackgroundResource(R.mipmap.bg_money_btn_selector);
            } else {
                holderView.linear.setBackgroundResource(R.mipmap.bg_money_btn_noselector);
            }
            holderView.setShowAmount(arrayList.get(position).amount);*/
            if (arrayList.get(position).flag) {
                holderView.textView.setBackgroundResource(R.mipmap.bg_money_btn_selector);
            } else {
                holderView.textView.setBackgroundResource(R.mipmap.bg_money_btn_noselector);
            }
            holderView.textView.setText(arrayList.get(position).amount);
            holderView.getTextView().setOnClickListener(v -> {

                referArray(arrayList.get(position), arrayList);
                notifyDataSetChanged();
                if (callback != null) {
                    callback.callbackWithAmount(arrayList.get(position).amount);
                }
            });
           /* holderView.linear.setOnClickListener(v -> {
                referArray(arrayList.get(position), arrayList);
                notifyDataSetChanged();
                if (callback != null) {
                    callback.callbackWithAmount(arrayList.get(position).amount);
                }
            });*/
            return view;
        }

        private void referArray(WithdrawalBankInfoVo.WithdrawalAmountVo vo, ArrayList<WithdrawalBankInfoVo.WithdrawalAmountVo> arrayList) {
            for (int i = 0; i < arrayList.size(); i++) {
                if (arrayList.get(i).amount.equals(vo.amount)) {
                    arrayList.get(i).flag = true;
                } else {
                    arrayList.get(i).flag = false;
                }

            }
        }

        private class HolderView {
            private String showAmount;

            public void setShowAmount(String showAmount) {
                this.showAmount = showAmount;
                this.textView.setText(showAmount);
            }

            public String getShowAmount() {
                return showAmount;
            }

            private TextView textView;
            private LinearLayout linear;

            public void setTextView(TextView textView) {
                this.textView = textView;
            }

            public TextView getTextView() {
                return textView;
            }
        }
    }
    private Map<String, String> getHeader() {
        String cookie = "auth=" + SPUtils.getInstance().getString(SPKeyGlobal.USER_TOKEN)
                + ";" + SPUtils.getInstance().getString(SPKeyGlobal.USER_SHARE_COOKIE_NAME)
                + "=" + SPUtils.getInstance().getString(SPKeyGlobal.USER_SHARE_SESSID)
                + ";";

        String auth = "bearer " + SPUtils.getInstance().getString(SPKeyGlobal.USER_TOKEN);
        Map<String, String> header = new HashMap<>();
        header.put("Cookie", cookie);
        header.put("Authorization", auth);
        header.put("App-RNID", "87jumkljo");
        return header;
    }

    /*显示銀行卡提款loading */
    private void showMaskLoading() {
        if (ppw2 == null) {
            ppw2 = new XPopup.Builder(getContext()).asCustom(new LoadingDialog(getContext()));
        }

        ppw2.show();
    }

    /*关闭loading*/
    private void dismissLoading() {
        if (ppw2 != null) {
            ppw2.dismiss();
        }

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

    /**
     * 显示绑定未满6小时温馨提示弹窗
     * <p>
     * private void showErrorMessageByTime(final String message, final View view) {
     * if (ppwErrorByTime == null) {
     * final String title = getContext().getString(R.string.txt_kind_tips);
     * AttachDialog attachDialog = new AttachDialog(getContext(), title, message, true, new AttachDialog.ICallBack() {
     *
     * @Override public void onClickLeft() {
     * ppwErrorByTime.dismiss();
     * <p>
     * }
     * @Override public void onClickRight() {
     * ppwErrorByTime.dismiss();
     * <p>
     * }
     * });
     * ppwErrorByTime = new XPopup.Builder(getContext()).atView(view).offsetY(-20).asCustom(attachDialog);
     * }
     * ppwErrorByTime.show();
     * <p>
     * }
     */

    private void showErrorMessage(final String message) {
        if (ppwError == null) {
            final String title = getContext().getString(R.string.txt_kind_tips);
            ppwError = new XPopup.Builder(getContext()).asCustom(new MsgDialog(getContext(), title, message, true, new TipDialog.ICallBack() {
                @Override
                public void onClickLeft() {
                    ppwError.dismiss();

                }

                @Override
                public void onClickRight() {
                    ppwError.dismiss();

                }
            }));

        }
        ppwError.show();
    }

    /*展示外跳View*/
    private void showCashPopView(final String jumpUrl) {
        if (webPopWindow == null) {
            webPopWindow = new CashWithdrawalPopWindow(getContext(), jumpUrl);
        }
        webPopWindow.show();
    }

    /*关闭外跳View*/
    private void closeCashPopView() {
        if (webPopWindow != null) {
            webPopWindow.closeView();
            webPopWindow.setVisibility(View.GONE);
        }
    }

}
