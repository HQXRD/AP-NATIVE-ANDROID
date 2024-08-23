package com.xtree.mine.ui.fragment.withdrawal;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.text.HtmlCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.Gson;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;
import com.xtree.base.adapter.CacheViewHolder;
import com.xtree.base.adapter.CachedAutoRefreshAdapter;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.router.RouterActivityPath;
import com.xtree.base.utils.CfLog;
import com.xtree.base.utils.DomainUtil;
import com.xtree.base.utils.StringUtils;
import com.xtree.base.utils.TagUtils;
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
import com.xtree.mine.vo.BankCardCashVo;
import com.xtree.mine.vo.ChooseInfoVo;
import com.xtree.mine.vo.PlatWithdrawConfirmVo;
import com.xtree.mine.vo.PlatWithdrawVo;

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
public class BankWithdrawalDialog extends BottomPopupView implements IAmountCallback, IFruitHorCallback {

    public interface BankWithdrawalClose {
        void closeBankWithdrawal();

        void closeBankByPSW();//资金密码检查
    }

    private String typenum;//上一级界面传递过来的typenum
    private Context context;
    private ChooseInfoVo.ChannelInfo channelInfo;
    private GridViewViewAdapter adapter;
    private LifecycleOwner owner;
    private int selectType = 0;//默认设置顶部选项卡
    private BankCardCashVo.ChanneBankVo channeBankVo; //选中的银行
    private BankCardCashVo bankCardCashVo;//银行卡提现model
    private BankCardCashVo.ChannelVo selectChanneVo;
    private PlatWithdrawVo platWithdrawVo;//提交订单后返回model
    private PlatWithdrawConfirmVo platWithdrawConfirmVo;//确认订单后返回的model

    private BankWithdrawalClose bankClose;//关闭提现

    private ChooseWithdrawViewModel viewModel;

    private DialogBankWithdrawalBankBinding binding;

    private FruitHorRecyclerViewAdapter recyclerViewAdapter;
    private BasePopupView ppw2;//绑卡
    private BasePopupView ppwError;//显示异常View
    private String checkCode;
    private ProfileVo mProfileVo;

    public static BankWithdrawalDialog newInstance(Context context, LifecycleOwner owner, ChooseInfoVo.ChannelInfo channelInfo, BankWithdrawalClose bankClose) {
        BankWithdrawalDialog dialog = new BankWithdrawalDialog(context);
        dialog.context = context;
        dialog.owner = owner;
        dialog.channelInfo = channelInfo;
        dialog.bankClose = bankClose;
        return dialog;
    }

    public static BankWithdrawalDialog newInstance(Context context, LifecycleOwner owner, ChooseInfoVo.ChannelInfo channelInfo, BankWithdrawalClose bankClose, final String checkCode) {
        BankWithdrawalDialog dialog = new BankWithdrawalDialog(context);
        dialog.context = context;
        dialog.owner = owner;
        dialog.channelInfo = channelInfo;
        dialog.bankClose = bankClose;
        dialog.checkCode = checkCode;
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
        initView();
        initData();
        initViewObservable();
        requestData();
        initListener();
        initMoreListener();

        String json = SPUtils.getInstance().getString(SPKeyGlobal.HOME_PROFILE);
        mProfileVo = new Gson().fromJson(json, ProfileVo.class);
    }

    private void initView() {

        binding = DialogBankWithdrawalBankBinding.bind(findViewById(R.id.ll_withdrawal_bank_root));
        binding.ivwClose.setOnClickListener(v -> dismiss());
        binding.tvwTitle.setText("银行卡提款");
        hideKeyBoard();

    }

    /**
     * 注册监听
     */
    private void initListener() {

        //binding.bankWithdrawalView.etInputMoney.setRegion(100,10);
        binding.bankWithdrawalView.etInputMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (binding.bankWithdrawalView.etInputMoney.hasFocus())//获取焦点
                {
                    //实际金额
                    binding.bankWithdrawalView.tvActualWithdrawalAmountShow.setText(s.toString());
                }
            }
        });
        //选择银行卡
        binding.bankWithdrawalView.llActualWithdrawalBank.setOnClickListener(v -> {
            popShowBank(bankCardCashVo);
        });
        binding.bankWithdrawalView.tvActualWithdrawalAmountBankShow.setOnClickListener(v -> {
            popShowBank(bankCardCashVo);
        });

        //提交订单 下一步
        binding.bankWithdrawalView.tvActualWithdrawalNext.setOnClickListener(v -> {

            String inputString = binding.bankWithdrawalView.tvActualWithdrawalAmountShow.getText().toString().trim();

            String typeNumber = selectChanneVo.typenum;
            if (TextUtils.isEmpty(inputString)) {
                ToastUtils.showLong(R.string.txt_input_amount_tip);
            } else if (Double.valueOf(inputString) > Double.valueOf(channeBankVo.max_money)) {
                ToastUtils.showLong(R.string.txt_input_amount_tip);
            } else if (Double.valueOf(inputString) < Double.valueOf(channeBankVo.min_money)) {
                ToastUtils.showLong(R.string.txt_input_amount_tip);
            } else {
                hideKeyBoard();
                requestNext(bankCardCashVo.check, inputString, channeBankVo.id);
            }
        });

        //确认订单下一步 bank_confirm_view
        binding.bankConfirmView.ivConfirmNext.setOnClickListener(V -> {

            String money = platWithdrawVo.datas.money;
            String handingFee = platWithdrawVo.datas.handing_fee;
            String bankInfo = platWithdrawVo.datas.cardid;
            String cardId = platWithdrawVo.datas.cardid;
            String checkCode = platWithdrawVo.check;
            requestConfirmWithdraw(money, handingFee, bankInfo, cardId, checkCode);
        });
        //确定订单 上一步
        binding.bankConfirmView.ivConfirmPrevious.setOnClickListener(v -> {
            refreshWithdrawView(platWithdrawVo);
        });
        //确定提款继续提现
        binding.llOverViewApply.ivContinueConfirmNext.setOnClickListener(v -> {
            dismiss();
        });
       /* //关闭提现
        binding.llOverViewApply.ivContinueConfirmClose.setOnClickListener(V -> {
            dismiss();
            bankClose.closeBankWithdrawal();
        });*/

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

    private void initMoreListener() {

        binding.etInputMoneyMore.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (binding.etInputMoneyMore.hasFocus())//获取焦点
                {
                    ///实际金额
                    binding.tvActualWithdrawalAmountShowMore.setText(s.toString());
                }
            }
        });

        //选择银行卡
        binding.llActualWithdrawalAmountBankShowMore.setOnClickListener(v -> {
            popShowBankMore(bankCardCashVo);
        });
        binding.tvActualWithdrawalAmountBankShowMore.setOnClickListener(v -> {
            popShowBankMore(bankCardCashVo);
        });
        //下一步
        binding.tvActualWithdrawalNextMore.setOnClickListener(v -> {
            String inputString = binding.tvActualWithdrawalAmountShowMore.getText().toString();
            String typeNumber = selectChanneVo.typenum;
            if (TextUtils.isEmpty(inputString)) {
                ToastUtils.showLong(R.string.txt_input_amount_tip);
            } else {
                requestNext(bankCardCashVo.check, inputString, channeBankVo.id);
            }
        });

        //订单确认页 下一步
        binding.bankConfirmView.ivConfirmNext.setOnClickListener(v -> {
            String money = platWithdrawVo.datas.money;
            String handingFee = platWithdrawVo.datas.handing_fee;
            String bankInfo = platWithdrawVo.datas.cardid;
            String cardId = platWithdrawVo.datas.cardid;
            String checkCode = platWithdrawVo.check;

            requestConfirmWithdraw(money, handingFee, bankInfo, cardId, checkCode);
        });
        //订单确认 上一步
        binding.bankConfirmView.ivConfirmPrevious.setOnClickListener(v -> {

            CfLog.i("订单上一步");
            binding.nsErrorView.setVisibility(View.GONE);//展示错误信息页面
            binding.llShowChooseCard.setVisibility(View.VISIBLE);
            binding.llShowNoticeInfo.setVisibility(View.VISIBLE);
            if (selectType == 1) {
                binding.nsSetWithdrawalRequest.setVisibility(View.VISIBLE);//单数据页面展示
                binding.nsSetWithdrawalRequestMore.setVisibility(View.GONE);//多金额页面隐藏
            } else if (selectType == 2) {
                binding.nsSetWithdrawalRequest.setVisibility(View.GONE);//单数据页面展示
                binding.nsSetWithdrawalRequestMore.setVisibility(View.VISIBLE);//多金额页面隐藏
            }

            binding.nsH5View.setVisibility(View.GONE);//h5隐藏
            binding.nsOverView.setVisibility(View.GONE); //订单结果页面隐藏
            binding.nsConfirmWithdrawalRequest.setVisibility(View.GONE); //确认提款页面隐藏

        });
    }

    private void initData() {
        viewModel = new ChooseWithdrawViewModel((Application) Utils.getContext(), Injection.provideHomeRepository());
    }

    private void initViewObservable() {
        //银行卡提现详情model
        viewModel.bankCardCashMoYuVoMutableLiveData.observe(this.owner, vo -> {
            bankCardCashVo = vo;
            dismissLoading();
            if (bankCardCashVo == null || bankCardCashVo.banks == null || bankCardCashVo.rest == null) {
                if (bankCardCashVo.msg_type == 2 && !TextUtils.isEmpty(bankCardCashVo.message)) {
                    showError(bankCardCashVo.message);
                    return;
                } else {
                    ToastUtils.showError(getContext().getString(R.string.txt_network_error));
                    dismiss();
                }

            } else if (bankCardCashVo.msg_type == 1 || bankCardCashVo.msg_type == 2) {
                ToastUtils.showError(bankCardCashVo.message);
                dismiss();
                return;

            } else if (bankCardCashVo.msg_type == 2 && TextUtils.equals(getContext().getString(R.string.txt_exhausted), bankCardCashVo.message)) {
                ToastUtils.showError(bankCardCashVo.message);
                dismiss();
                return;
            }
            //"ur_here": "资金密码检查",
            else if (!TextUtils.isEmpty(bankCardCashVo.ur_here) && TextUtils.equals(getContext().getString(R.string.txt_withdraw_password_check), bankCardCashVo.ur_here)) {
                ToastUtils.showError(bankCardCashVo.ur_here);
                // bankClose.closeBankByPSW();
                //dismiss();
                return;
            } else if ("2".equals(bankCardCashVo.msg_type) && getContext().getString(R.string.txt_fund_account_locked).equals(bankCardCashVo.message)) {
                ToastUtils.showError(bankCardCashVo.message);
                dismiss();
                return;
            } else {
                //1.初始化顶部选项卡
                refreshTopUI(bankCardCashVo);
                //2.为注意view设置相关值
                refreshNoticeView(bankCardCashVo);
                //3.刷新第一次获取的数据
                refreshInitView(bankCardCashVo);
            }

        });
        //银行卡提现 下一步
        viewModel.platWithdrawMoYuVoMutableLiveData.observe(this.owner, vo -> {
            platWithdrawVo = vo;
            if (platWithdrawVo == null || platWithdrawVo.datas == null || platWithdrawVo.user == null) {
                // 针对异常情况
                if (platWithdrawVo.msg_type == 2 && !TextUtils.isEmpty(platWithdrawVo.message)) {
                    ToastUtils.showError(platWithdrawVo.message);
                    return;
                } else {
                    ToastUtils.showError(getContext().getString(R.string.txt_network_error));
                    dismiss();
                }
            } else if (platWithdrawVo.datas != null) {
                refreshWithdrawView(platWithdrawVo);
            } else if (getContext().getString(R.string.txt_withdraw_password_check).equals(platWithdrawVo.ur_here)) {
                //业务异常跳转资金安全密码
                ToastUtils.showError(getContext().getString(R.string.txt_withdraw_fun_timeout));
            } else if ("2".equals(platWithdrawVo.msg_type) && getContext().getString(R.string.txt_fund_account_locked).equals(platWithdrawVo.message)) {
                ToastUtils.showError(platWithdrawVo.message);
                dismiss();
                return;
            } else if (getContext().getString(R.string.txt_withdraw_relogin).equals(platWithdrawVo.message)) {
                ToastUtils.showError(platWithdrawVo.message);
                popLoginView();
            }

        });
        //银行卡提现 确认
        viewModel.platWithdrawConfirmMoYuVoMutableLiveData.observe(this.owner, ov -> {
            TagUtils.tagEvent(getContext(), "wd", "bkc");
            if (ov == null || ov.user == null) {
                // 针对异常情况
                if (ov.msg_type == 2 && !TextUtils.isEmpty(ov.message)) {
                    ToastUtils.showError(ov.message);
                    return;
                } else {
                    ToastUtils.showError(getContext().getString(R.string.txt_network_error));
                    dismiss();
                }
            } else {
                platWithdrawConfirmVo = ov;
                refreshWithdrawConfirmView(platWithdrawConfirmVo);
            }

        });
    }

    /*业务异常 跳转登录*/
    public void popLoginView() {
        ARouter.getInstance().build(RouterActivityPath.Mine.PAGER_LOGIN_REGISTER).navigation();
    }

    private void showError(String errorMessage) {
        binding.nsErrorView.setVisibility(View.VISIBLE);
        binding.tvShowErrorMessage.setText(errorMessage);
        binding.nsSetWithdrawalRequest.setVisibility(View.GONE);//单数据页面展示
        binding.nsSetWithdrawalRequestMore.setVisibility(View.GONE);//多金额页面隐藏
        binding.nsH5View.setVisibility(View.GONE);//h5隐藏
        binding.nsOverView.setVisibility(View.GONE); //订单结果页面隐藏
        binding.nsConfirmWithdrawalRequest.setVisibility(View.VISIBLE); //确认提款页面隐藏

    }

    private void requestData() {
        showMaskLoading();
        viewModel.getChooseWithdrawBankDetailInfo(checkCode);

    }

    /**
     * 刷新第一次获取数据后选择的View
     */
    public void refreshInitView(BankCardCashVo bankCardCashVo) {
        //关闭软键盘
        binding.bankWithdrawalView.etInputMoney.clearFocus();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(binding.bankWithdrawalView.etInputMoney.getWindowToken(), 0);
        }

        selectChanneVo = bankCardCashVo.channel_list.get(0);
        // 展示错误信息 例如关闭提款通道
        /*if (bankCardCashVo.channel_list.get(0).isShowErrorView == 1)
        {
            binding.nsErrorView.setVisibility(View.VISIBLE);//展示错误信息页面
            binding.nsSetWithdrawalRequest.setVisibility(View.GONE);//原始数据页面隐藏
            binding.nsSetWithdrawalRequestMore.setVisibility(View.GONE);//多金额页面隐藏
            binding.nsH5View.setVisibility(View.GONE);//h5页面隐藏
            binding.nsConfirmWithdrawalRequest.setVisibility(View.GONE); //确认提款页面隐藏

            binding.tvShowErrorMessage.setText(bankCardCashVo.channel_list.get(0).thiriframe_msg);
        }
        else
        {

        }*/
        //展示WebView界面
        if (bankCardCashVo.channel_list.get(0).isWebView == 1) {
            binding.nsDefaultView.setVisibility(View.GONE);
            binding.nsErrorView.setVisibility(View.GONE);//隐藏错误信息页面隐藏
            binding.nsSetWithdrawalRequest.setVisibility(View.GONE);//单数据页面展示
            binding.nsSetWithdrawalRequestMore.setVisibility(View.GONE);//多金额页面隐藏
            binding.nsConfirmWithdrawalRequest.setVisibility(View.GONE); //确认提款页面隐藏
            binding.maskH5View.setVisibility(View.VISIBLE);
            binding.nsH5View.setVisibility(View.VISIBLE);//h5展示
            String url = bankCardCashVo.channel_list.get(0).thiriframe_url;
            if (!StringUtils.isStartHttp(url)) {
                url = DomainUtil.getDomain2() + url;
            }
            binding.nsH5View.loadUrl(url, getHeader());
            initWebView();
            binding.nsH5View.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    showMaskLoading();
                    view.loadUrl(url);
                    return true;
                }

                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    // LoadingDialog.show(getContext());
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    //LoadingDialog.finish();
                    dismissLoading();
                    binding.maskH5View.setVisibility(View.GONE);
                }

            });
        }
        //展示原生页面
        else if (bankCardCashVo.channel_list.get(0).isWebView == 2) {
            //展示多金额页面
            if (bankCardCashVo.channel_list.get(0).fixamount_list_status == 1) {
                binding.nsErrorView.setVisibility(View.GONE);//展示错误信息页面
                binding.nsSetWithdrawalRequest.setVisibility(View.GONE);//单数据页面展示
                binding.nsSetWithdrawalRequestMore.setVisibility(View.VISIBLE);//多金额页面隐藏
                binding.nsConfirmWithdrawalRequest.setVisibility(View.GONE); //确认提款页面隐藏
                binding.nsH5View.setVisibility(View.GONE);//h5隐藏
                refreshRequestMoreView(bankCardCashVo, bankCardCashVo.channel_list.get(0));
                refreshSelectAmountUI(bankCardCashVo.channel_list.get(0));
            } else {
                binding.nsErrorView.setVisibility(View.GONE);//展示错误信息页面
                binding.nsSetWithdrawalRequest.setVisibility(View.VISIBLE);//单数据页面展示
                binding.nsSetWithdrawalRequestMore.setVisibility(View.GONE);//多金额页面隐藏
                binding.nsConfirmWithdrawalRequest.setVisibility(View.GONE); //确认提款页面隐藏
                binding.nsH5View.setVisibility(View.GONE);//h5隐藏
                if (bankCardCashVo != null && !bankCardCashVo.channel_list.isEmpty()){
                    refreshRequestView(bankCardCashVo, bankCardCashVo.channel_list.get(0));
                }else{
                    CfLog.e("bankCardCashVo.channel_list.isEmpty()");
                }
            }
        }

    }

    private void initWebView() {
        WebSettings settings = binding.nsH5View.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setSupportZoom(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setLoadsImagesAutomatically(true);
        settings.setSupportZoom(true);
    }

    private void refreshTopUI(BankCardCashVo bankCardCashVo) {
        for (int i = 0; i < bankCardCashVo.channel_list.size(); i++) {
            if (i == 0) {
                bankCardCashVo.channel_list.get(0).flag = true;
            } else {
                bankCardCashVo.channel_list.get(i).flag = false;
            }
        }
        recyclerViewAdapter = new FruitHorRecyclerViewAdapter(context, bankCardCashVo.channel_list, this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this.context);
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        binding.rvShowChooseCard.setLayoutManager(layoutManager);
        binding.rvShowChooseCard.addItemDecoration(new FruitHorRecyclerViewAdapter.SpacesItemDecoration(10));
        binding.rvShowChooseCard.setAdapter(recyclerViewAdapter);
        binding.rvShowChooseCard.setItemAnimator(new DefaultItemAnimator());
    }

    /**
     * 刷新注意View
     */
    private void refreshNoticeView(BankCardCashVo bankCardCashVo) {
        final String notice = "<font color=#99A0B1>注意:</font>";
        String times, count, startTime, endTime, rest;
        times = "<font color=#99A0B1>" + String.valueOf(bankCardCashVo.times) + "</font>";
        count = "<font color=#99A0B1>" + bankCardCashVo.count + "</font>";
        startTime = "<font color=#99A0B1>" + bankCardCashVo.wraptime.starttime + "</font>";
        endTime = "<font color=#99A0B1>" + bankCardCashVo.wraptime.endtime + "</font>";
        rest = StringUtils.formatToSeparate(Float.valueOf(bankCardCashVo.rest));
        String testTxt = "<font color=#FF6C6C>" + rest + "</font>";
        String format = getContext().getResources().getString(R.string.txt_withdraw_bank_top_tip);
        String textSource = String.format(format, notice, times, count, startTime, endTime, testTxt);

        binding.tvShowNoticeInfo.setText(HtmlCompat.fromHtml(textSource, HtmlCompat.FROM_HTML_MODE_LEGACY));
    }

    /**
     * 刷新单输入View
     */
    private void refreshRequestView(BankCardCashVo bankCardCashVo, BankCardCashVo.ChannelVo channelVo) {
        refreshUserView(bankCardCashVo);
        refreshAmountUI(bankCardCashVo, channelVo);
    }

    /**
     * 刷新多金额选择View
     */
    private void refreshRequestMoreView(BankCardCashVo bankCardCashVo, BankCardCashVo.ChannelVo channelVo) {
        refreshUserView(bankCardCashVo);
        refreshAmountUI(bankCardCashVo, channelVo);
    }

    /**
     * 刷新用户信息View
     */
    private void refreshUserView(BankCardCashVo bankCardCashVo) {
        if (binding.nsSetWithdrawalRequest.getVisibility() == View.VISIBLE) {
            if (bankCardCashVo.user != null) {
                if (bankCardCashVo.user.username != null) {
                    binding.bankWithdrawalView.tvUserNameShow.setText(bankCardCashVo.user.username);
                } else if (bankCardCashVo.user.nickname != null) {
                    binding.bankWithdrawalView.tvUserNameShow.setText(bankCardCashVo.user.nickname);
                }
            } else if (mProfileVo != null && mProfileVo.username != null) {
                final String name = StringUtils.splitWithdrawUserName(mProfileVo.username);
                binding.bankWithdrawalView.tvUserNameShow.setText(name);
            }
            binding.bankWithdrawalView.tvWithdrawalTypeShow.setText("银行卡");
            binding.bankWithdrawalView.tvWithdrawalAmountMethod.setText(bankCardCashVo.user.cafAvailableBalance);
        } else if (binding.nsSetWithdrawalRequestMore.getVisibility() == View.VISIBLE) {
            if (bankCardCashVo.user != null) {
                if (bankCardCashVo.user.username != null) {
                    binding.bankWithdrawalView.tvUserNameShow.setText(bankCardCashVo.user.username);
                } else if (bankCardCashVo.user.nickname != null) {
                    binding.bankWithdrawalView.tvUserNameShow.setText(bankCardCashVo.user.nickname);
                }
            } else if (mProfileVo != null && mProfileVo.username != null) {
                final String name = StringUtils.splitWithdrawUserName(mProfileVo.username);
                binding.bankWithdrawalView.tvUserNameShow.setText(name);
            }
            binding.tvWithdrawalTypeShowMore.setText("银行卡");
            binding.tvWithdrawalAmountMethodMore.setText(bankCardCashVo.user.cafAvailableBalance);
        }

    }

    /**
     * 刷新提现金额、收款银行开信息
     */
    private void refreshAmountUI(BankCardCashVo bankCardCashVo, BankCardCashVo.ChannelVo channelVo) {
        String textSource = "单笔最低提现金额：" + channelVo.min_money + ",最高:" + channelVo.max_money;
        if (binding.nsSetWithdrawalRequest.getVisibility() == View.VISIBLE) {
            binding.bankWithdrawalView.tvWithdrawalAmountShow.setText(textSource);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                binding.bankWithdrawalView.tvWithdrawalAmountShow.setTextColor(getContext().getColor(R.color.clr_withdrawal_text_tip));
            }
            if (!bankCardCashVo.banks.isEmpty()){
                String bankInfoString = bankCardCashVo.banks.get(0).bank_name + " " + bankCardCashVo.banks.get(0).account;
                binding.bankWithdrawalView.tvActualWithdrawalAmountBankShow.setText(bankInfoString);
            }else{
                CfLog.e("bankCardCashVo.banks  is null");
            }
        } else if (binding.nsSetWithdrawalRequestMore.getVisibility() == View.VISIBLE) {
            binding.tvWithdrawalAmountShowMore.setText(textSource);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                binding.bankWithdrawalView.tvWithdrawalAmountShow.setTextColor(getContext().getColor(R.color.clr_withdrawal_text_tip));
            }
            binding.tvActualWithdrawalAmountBankShowMore.setText(bankCardCashVo.banks.get(0).bank_name + " " + bankCardCashVo.banks.get(0).account);
        }
        if (!bankCardCashVo.banks.isEmpty()){
            channeBankVo = bankCardCashVo.banks.get(0);
        }

    }

    /**
     * 刷新多个金额选择View
     *
     * @param channelVo
     */
    private void refreshSelectAmountUI(BankCardCashVo.ChannelVo channelVo) {
        if (channelVo.fixamountList.size() > 0) {
            if (adapter == null) {
                adapter = new GridViewViewAdapter(context, (ArrayList<String>) channelVo.fixamountList, this);
            }
            binding.gvSelectAmountMore.setAdapter(adapter);
        } else {
            CfLog.i("refreshSelectAmountUI channelVo.size = 0?");
        }
    }

    /**
     * 刷新确认提交订单页面
     */
    private void refreshWithdrawView(PlatWithdrawVo platWithdrawVo) {
        binding.llShowChooseCard.setVisibility(View.GONE);//顶部通用、大额提现View隐藏
        binding.llShowNoticeInfo.setVisibility(View.GONE); //顶部提示信息隐藏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            binding.tvSetWithdrawalRequest.setTextColor(getContext().getColor(R.color.clr_choose_20));
            binding.tvConfirmWithdrawalRequest.setTextColor(getContext().getColor(R.color.clr_choose_20));
        }
        binding.nsErrorView.setVisibility(View.GONE);//展示错误信息页面
        binding.nsSetWithdrawalRequest.setVisibility(View.GONE);//单数据页面展示
        binding.nsSetWithdrawalRequestMore.setVisibility(View.GONE);//多金额页面隐藏
        binding.maskH5View.setVisibility(View.GONE);
        binding.nsH5View.setVisibility(View.GONE);//h5隐藏
        binding.nsOverView.setVisibility(View.GONE); //订单结果页面隐藏
        binding.nsConfirmWithdrawalRequest.setVisibility(View.VISIBLE); //确认提款页面隐藏
        binding.bankConfirmView.tvConfirmUserNameShow.setText(platWithdrawVo.user.username);
        String showMoney = platWithdrawVo.user.cafAvailableBalance;
        binding.bankConfirmView.tvConfirmWithdrawalTypeShow.setText(showMoney);
        String showAmountMoney = platWithdrawVo.datas.money;
        binding.bankConfirmView.tvConfirmAmountShow.setText(showAmountMoney);
        String arriveString = StringUtils.formatToSeparate(Float.valueOf(platWithdrawVo.datas.arrive));
        binding.bankConfirmView.tvWithdrawalAmountTypeShow.setText(arriveString);
        binding.bankConfirmView.tvWithdrawalActualArrivalShow.setText(platWithdrawVo.datas.bankname);
        binding.bankConfirmView.tvWithdrawalExchangeRateShow.setText(platWithdrawVo.datas.bankcity);
        binding.bankConfirmView.tvWithdrawalAddressShow.setText(platWithdrawVo.datas.truename);
        binding.bankConfirmView.tvWithdrawalHandlingFeeShow.setText(platWithdrawVo.datas.bankno);
    }

    /*暂时废弃
    private void refreshWithdrawConfirmView() {
        binding.llShowChooseCard.setVisibility(View.GONE);//顶部通用、大额提现View隐藏
        binding.nsDefaultView.setVisibility(View.GONE);
        binding.llShowNoticeInfo.setVisibility(View.GONE); //顶部提示信息隐藏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            binding.tvSetWithdrawalRequest.setTextColor(getContext().getColor(R.color.clr_choose_20));
            binding.tvConfirmWithdrawalRequest.setTextColor(getContext().getColor(R.color.clr_choose_20));
            binding.tvOverWithdrawalRequest.setTextColor(getContext().getColor(R.color.clr_choose_20));
        }
        binding.nsErrorView.setVisibility(View.GONE);//展示错误信息页面
        binding.nsSetWithdrawalRequest.setVisibility(View.GONE);//单数据页面展示
        binding.nsSetWithdrawalRequestMore.setVisibility(View.GONE);//多金额页面隐藏
        binding.maskH5View.setVisibility(View.GONE);
        binding.nsH5View.setVisibility(View.GONE);//h5隐藏
        binding.nsConfirmWithdrawalRequest.setVisibility(View.GONE); //确认提款页面隐藏
        binding.nsOverView.setVisibility(View.VISIBLE);//订单结果页面
        binding.llOverViewApply.ivOverApply.setBackground(getContext().getDrawable(R.mipmap.ic_over_apply_err));
        binding.etInputMoneyMore.clearFocus();
        binding.bankWithdrawalView.etInputMoney.clearFocus();
        binding.nsOverView.setOnClickListener(v -> {
            CfLog.e("ivContinueConfirmNextnsOverView");
        });
        binding.llOverViewApply.ivContinueConfirmNext.setOnClickListener(v -> {
            ToastUtils.showError("ivContinueConfirmNext");
            CfLog.e("ivContinueConfirmNext");
            dismiss();
        });
        binding.llOverViewApply.ivContinueConfirmClose.setOnClickListener(v -> {
            ToastUtils.showError("ivContinueConfirmClose");
            dismiss();
        });
        binding.llOverViewApply.ivOverApply.setOnClickListener(v -> {
            ToastUtils.showError("ivOverApply");
            dismiss();
        });
    }*/

    /**
     * 刷新提交点订单后页面
     */
    private void refreshWithdrawConfirmView(PlatWithdrawConfirmVo vo) {
        //msg_type 1 2 状态均为成功
        if (vo.msg_type == 1 || (vo.msg_type == 2 && TextUtils.equals("账户提款申请成功", vo.msg_detail))) {
            //成功
            binding.llShowChooseCard.setVisibility(View.GONE);//顶部通用、大额提现View隐藏
            binding.nsDefaultView.setVisibility(View.GONE);
            binding.llShowNoticeInfo.setVisibility(View.GONE); //顶部提示信息隐藏
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                binding.tvSetWithdrawalRequest.setTextColor(getContext().getColor(R.color.clr_choose_20));
                binding.tvConfirmWithdrawalRequest.setTextColor(getContext().getColor(R.color.clr_choose_20));
                binding.tvOverWithdrawalRequest.setTextColor(getContext().getColor(R.color.clr_choose_20));
            }
            binding.nsErrorView.setVisibility(View.GONE);//展示错误信息页面
            binding.nsSetWithdrawalRequest.setVisibility(View.GONE);//单数据页面展示
            binding.nsSetWithdrawalRequestMore.setVisibility(View.GONE);//多金额页面隐藏
            binding.maskH5View.setVisibility(View.GONE);
            binding.nsH5View.setVisibility(View.GONE);//h5隐藏
            binding.nsConfirmWithdrawalRequest.setVisibility(View.GONE); //确认提款页面隐藏
            binding.nsOverView.setVisibility(View.VISIBLE);//订单结果页面

            binding.llOverViewApply.tvOverMsg.setText(vo.msg_detail);
        } else if (vo.msg_type == 2 && !TextUtils.equals("账户提款申请成功", vo.msg_detail)) {
            binding.nsConfirmWithdrawalRequest.setVisibility(View.VISIBLE); //确认提款页面展示
            showErrorBySystem(vo.msg_detail);
            /*//失败
            binding.llOverViewApply.ivOverApply.setBackground(getContext().getDrawable(R.mipmap.ic_over_apply_err));
            binding.llOverViewApply.tvOverMsg.setText(vo.msg_detail);
            binding.llOverViewApply.tvMessageTip.setVisibility(View.GONE);*/
        } else if (vo.msg_type == 4) {
            //稍后刷新重试
            binding.llOverViewApply.tvOverMsg.setText(vo.msg_detail);
            binding.llOverViewApply.tvMessageTip.setVisibility(View.GONE);
        } else {
            //失败
            binding.llOverViewApply.ivOverApply.setBackground(getContext().getDrawable(R.mipmap.ic_over_apply_err));
            binding.llOverViewApply.tvOverMsg.setText(vo.msg_detail);
            binding.llOverViewApply.tvMessageTip.setVisibility(View.GONE);
        }
        binding.llOverViewApply.ivContinueConfirmNext.setOnClickListener(v -> {
            dismiss();
        });
      /*  binding.llOverViewApply.ivContinueConfirmClose.setOnClickListener(v -> {
            dismiss();
        });*/

    }

    /**
     * 固额提现 提款金额 多个选择金额btn 点击回调
     */
    @Override
    public void callbackWithAmount(String amount) {
        binding.etInputMoneyMore.setText(amount);
        binding.tvActualWithdrawalAmountShowMore.setText(amount);

    }

    /**
     * 顶部选项卡点击回调
     */
    @Override
    public void callbackWithFruitHor(BankCardCashVo.ChannelVo selectVO) {
        selectChanneVo = selectVO;//设置选中的channelVo
        if (selectVO.isShowErrorView == 1) //展示错误信息
        {
            binding.nsErrorView.setVisibility(View.VISIBLE);//展示错误信息页面
            binding.nsSetWithdrawalRequest.setVisibility(View.GONE);//原始数据页面隐藏
            binding.nsSetWithdrawalRequestMore.setVisibility(View.GONE);//多金额页面隐藏
            binding.nsH5View.setVisibility(View.GONE);//h5页面隐藏
            binding.nsConfirmWithdrawalRequest.setVisibility(View.GONE); //确认提款页面隐藏

            binding.tvShowErrorMessage.setText(bankCardCashVo.channel_list.get(0).thiriframe_msg);
        } else if (selectVO.isShowErrorView == 0) {
            if (selectVO.isWebView == 1)//展示WebView
            {
                binding.nsDefaultView.setVisibility(View.GONE);
                binding.nsErrorView.setVisibility(View.GONE);//隐藏错误信息页面隐藏
                binding.nsSetWithdrawalRequest.setVisibility(View.GONE);//单数据页面展示
                binding.nsSetWithdrawalRequestMore.setVisibility(View.GONE);//多金额页面隐藏
                binding.nsConfirmWithdrawalRequest.setVisibility(View.GONE); //确认提款页面隐藏
                binding.maskH5View.setVisibility(View.VISIBLE);
                binding.nsH5View.setVisibility(View.VISIBLE);//h5展示
                String url = selectVO.thiriframe_url;
                if (!StringUtils.isStartHttp(url)) {
                    url = DomainUtil.getDomain2() + url;
                }
                binding.nsH5View.loadUrl(url, getHeader());
                initWebView();
                binding.nsH5View.setWebViewClient(new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        showMaskLoading();
                        view.loadUrl(url);
                        return true;
                    }

                    @Override
                    public void onPageStarted(WebView view, String url, Bitmap favicon) {
                        // LoadingDialog.show(getContext());
                    }

                    @Override
                    public void onPageFinished(WebView view, String url) {
                        super.onPageFinished(view, url);
                        //LoadingDialog.finish();
                        dismissLoading();
                        binding.maskH5View.setVisibility(View.GONE);
                    }

                });
            } else if (selectVO.fixamount_list_status == 0) {
                selectType = 1;//选中单独金额选项View
                binding.nsErrorView.setVisibility(View.GONE);//隐藏错误信息页面隐藏
                binding.nsSetWithdrawalRequest.setVisibility(View.VISIBLE);//单数据页面展示
                binding.nsSetWithdrawalRequestMore.setVisibility(View.GONE);//多金额页面隐藏
                binding.nsConfirmWithdrawalRequest.setVisibility(View.GONE); //确认提款页面隐藏
                binding.nsH5View.setVisibility(View.GONE);//h5隐藏
                refreshRequestView(bankCardCashVo, selectVO);
            } else if (selectVO.fixamount_list_status == 1) {
                selectType = 2;//选中多金额选项View
                binding.nsErrorView.setVisibility(View.GONE);//展示错误信息页面隐藏
                binding.nsSetWithdrawalRequest.setVisibility(View.GONE);//单数据页面展示隐藏
                binding.nsSetWithdrawalRequestMore.setVisibility(View.VISIBLE);//多金额页面隐藏
                binding.nsConfirmWithdrawalRequest.setVisibility(View.GONE); //确认提款页面隐藏
                binding.nsH5View.setVisibility(View.GONE);//h5隐藏
                refreshRequestMoreView(bankCardCashVo, selectVO);

                refreshSelectAmountUI(selectVO);
            }
        }

    }

    ItemTextBinding binding2;
    BasePopupView ppw = null; // 底部弹窗 (选择**菜单)

    /**
     * 显示银行卡信息
     */
    private void popShowBank(BankCardCashVo bankCardCashVo) {
        CachedAutoRefreshAdapter adapter = new CachedAutoRefreshAdapter<BankCardCashVo.ChanneBankVo>() {

            @NonNull
            @Override
            public CacheViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                CacheViewHolder holder = new CacheViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_text, parent, false));
                return holder;
            }

            @Override
            public void onBindViewHolder(@NonNull CacheViewHolder holder, int position) {
                binding2 = ItemTextBinding.bind(holder.itemView);
                BankCardCashVo.ChanneBankVo vo = get(position);
                channeBankVo = vo;
                String showMessage = vo.bank_name + " " + vo.account;
                binding2.tvwTitle.setText(showMessage);
                binding2.tvwTitle.setOnClickListener(v -> {
                    binding.bankWithdrawalView.tvActualWithdrawalAmountBankShow.setText(showMessage);
                    ppw.dismiss();
                });

            }
        };

        adapter.addAll(bankCardCashVo.banks);

        ppw = new XPopup.Builder(getContext()).asCustom(new ListDialog(getContext(), getContext().getString(R.string.txt_select_bank), adapter, 20));
        ppw.show();
    }

    private void popShowBankMore(BankCardCashVo bankCardCashVo) {
        CachedAutoRefreshAdapter adapter = new CachedAutoRefreshAdapter<BankCardCashVo.ChanneBankVo>() {

            @NonNull
            @Override
            public CacheViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                CacheViewHolder holder = new CacheViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_text, parent, false));
                return holder;
            }

            @Override
            public void onBindViewHolder(@NonNull CacheViewHolder holder, int position) {
                binding2 = ItemTextBinding.bind(holder.itemView);
                BankCardCashVo.ChanneBankVo vo = get(position);
                channeBankVo = vo;
                String showMessage = vo.bank_name + " " + vo.account;
                binding2.tvwTitle.setText(showMessage);
                binding2.tvwTitle.setOnClickListener(v -> {
                    binding.tvActualWithdrawalAmountBankShowMore.setText(showMessage);
                    ppw.dismiss();
                });
            }
        };

        adapter.addAll(bankCardCashVo.banks);

        ppw = new XPopup.Builder(getContext()).asCustom(new ListDialog(getContext(), getContext().getString(R.string.txt_select_bank), adapter, 40));
        ppw.show();
    }

    /**
     * 提交订单
     */
    private void requestNext(@NonNull String checkCode, @NonNull String money, @NonNull String bankinfo) {
        LoadingDialog.show(getContext());
        HashMap<String, String> map = new HashMap<>();
        map.put("action", "platwithdraw");
        map.put("bankinfo", bankinfo);
        map.put("check", checkCode);
        map.put("controller", "security"); //列表也选择的取款类型
        map.put("flag", "withdraw");
        map.put("money", money);
        map.put("nonce", UuidUtil.getID24());
        map.put("realCount", "");
        map.put("usdtType", "1");

        CfLog.i("requestNext --> " + map.toString());

        viewModel.getPlatWithdrawMoYu(map);
    }

    /**
     * 确定提交
     */
    private void requestConfirmWithdraw(String money, String handingFee, String bankInfo, String cardId, String checkCode) {
        LoadingDialog.show(getContext());
        HashMap<String, String> map = new HashMap<>();
        map.put("action", "platwithdraw");
        map.put("controller", "security");
        map.put("flag", "confirm");
        map.put("usdtType", "1"); //列表也选择的取款类型
        map.put("nonce", UuidUtil.getID24());

        map.put("money", money);
        map.put("handing_fee", handingFee);
        map.put("bankinfo", bankInfo);
        map.put("cardid", cardId);
        map.put("check", checkCode);

        CfLog.i("确定提交requestConfirmWithdraw --> " + map.toString());
        viewModel.postConfirmWithdrawMoYu(map);

    }

    /**
     * 定义GridViewViewAdapter 显示大额固额金额选择
     */
    private static class GridViewViewAdapter extends BaseAdapter {
        public IAmountCallback callback;
        private Context context;
        public ArrayList<String> arrayList;

        public GridViewViewAdapter(Context context, ArrayList<String> list, IAmountCallback callback) {
            super();
            this.context = context;
            this.arrayList = list;
            this.callback = callback;
        }

        @Override
        public int getCount() {
            return arrayList.size();
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
                view = LayoutInflater.from(context).inflate(R.layout.dialog_bank_withdrawal_btn, parent, false);
                holderView = new HolderView();
                holderView.textView = (TextView) view.findViewById(R.id.tv_withdrawal_amount);
                holderView.linear = (ConstraintLayout) view.findViewById(R.id.cl_bank_status);
                view.setTag(holderView);
            } else {
                holderView = (HolderView) view.getTag();
            }
            holderView.setShowAmount(arrayList.get(position));

            holderView.getTextView().setOnClickListener(v -> {
                if (callback != null) {
                    callback.callbackWithAmount(arrayList.get(position));
                }
            });
            holderView.linear.setOnClickListener(v -> {
                if (callback != null) {
                    callback.callbackWithAmount(arrayList.get(position));
                }
            });
            return view;
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
            private ConstraintLayout linear;

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
        ppw2.dismiss();
    }

    /* 由于权限原因弹窗*/
    private void showErrorBySystem(final String message) {
        if (ppwError == null) {
            final String title = getContext().getString(R.string.txt_kind_tips);
            ppwError = new XPopup.Builder(getContext()).asCustom(new MsgDialog(getContext(), title, message, false, new TipDialog.ICallBack() {
                @Override
                public void onClickLeft() {
                    ppwError.dismiss();
                    dismiss();
                }

                @Override
                public void onClickRight() {
                    //确认
                    ppwError.dismiss();
                    //bankClose.closeBankWithdrawal();
                    dismiss();
                }
            }));
        }
        ppwError.show();
    }
}
