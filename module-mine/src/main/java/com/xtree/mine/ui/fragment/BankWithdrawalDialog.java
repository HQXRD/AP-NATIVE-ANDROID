package com.xtree.mine.ui.fragment;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.xtree.base.widget.ListDialog;
import com.xtree.base.widget.LoadingDialog;
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

    ChooseWithdrawViewModel viewModel;

    DialogBankWithdrawalBankBinding binding;

    private FruitHorRecyclerViewAdapter recyclerViewAdapter;

    public static BankWithdrawalDialog newInstance(Context context, LifecycleOwner owner, ChooseInfoVo.ChannelInfo channelInfo) {
        BankWithdrawalDialog dialog = new BankWithdrawalDialog(context);
        context = context;
        dialog.context = context;
        dialog.owner = owner;
        dialog.channelInfo = channelInfo;
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
        //return super.getMaxHeight();
        return (XPopupUtils.getScreenHeight(getContext()) * 80 / 100);
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

    }

    private void initView() {
        binding = DialogBankWithdrawalBankBinding.bind(findViewById(R.id.ll_bank_root));
        binding.ivwClose.setOnClickListener(v -> dismiss());
        binding.tvwTitle.setText("银行卡提款");

    }

    /**
     * 注册监听
     */
    private void initListener() {
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

            String inputString = binding.bankWithdrawalView.tvActualWithdrawalAmountShow.getText().toString();
            String bankInfo = binding.bankWithdrawalView.tvActualWithdrawalAmountBankShow.getText().toString();
            String typeNumber = selectChanneVo.typenum;
            if (TextUtils.isEmpty(inputString)) {
                ToastUtils.showLong(R.string.txt_input_amount_tip);
            } else if (Integer.valueOf(inputString) > Integer.valueOf(channeBankVo.max_money)) {
                ToastUtils.showLong(R.string.txt_input_amount_tip);
            } else if (Integer.valueOf(inputString) < Integer.valueOf(channeBankVo.min_money)) {
                ToastUtils.showLong(R.string.txt_input_amount_tip);
            } else {
                hideKeyBoard();
                requestNext(channelInfo.type, typeNumber, inputString, channeBankVo.id);
            }
        });

        //确认订单下一步 bank_confirm_view
        binding.bankConfirmView.ivConfirmNext.setOnClickListener(V -> {

            String money = platWithdrawVo.datas.arrive;
            String cardid = platWithdrawVo.datas.cardid;
            String channel_child = platWithdrawVo.channel_child;
            requestConfirmWithdraw(money, cardid, channel_child);
        });
        //确定订单 上一步
        binding.bankConfirmView.ivConfirmPrevious.setOnClickListener(v -> {
            refreshWithdrawView(platWithdrawVo);
        });
        //确定提款继续提现
        binding.llOverViewApply.ivContinueConfirmNext.setOnClickListener(v -> {
            dismiss();
        });
        //关闭提现
        binding.llOverViewApply.ivContinueConfirmPrevious.setOnClickListener(V -> {
            dismiss();
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
                CfLog.i("inputString = " + inputString + " \n typeNumber= " + typeNumber + "\n selectVo=" + selectChanneVo.toString() + "\n channelInfo = " + channelInfo.toString());
                requestNext(channelInfo.type, typeNumber, inputString, channeBankVo.id);
            }
        });

        //订单确认页 下一步
        binding.bankConfirmView.ivConfirmNext.setOnClickListener(v -> {
            String money = platWithdrawVo.datas.arrive;
            String cardid = platWithdrawVo.datas.cardid;
            String channel_child = platWithdrawVo.channel_child;
            requestConfirmWithdraw(money, cardid, channel_child);
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
        viewModel.channelDetailVoMutableLiveData.observe(this.owner, vo -> {
            bankCardCashVo = vo;
            if (bankCardCashVo.msg_type == 1 || bankCardCashVo.msg_type == 2) {
                ToastUtils.showError(bankCardCashVo.message);
                dismiss();
                return;
            }
            //1.初始化顶部选项卡
            refreshTopUI(bankCardCashVo);
            //2.为注意view设置相关值
            refreshNoticeView(bankCardCashVo);
            //3.刷新第一次获取的数据
            refreshInitView(bankCardCashVo);
        });
        //银行卡提现
        viewModel.platwithdrawVoMutableLiveData.observe(this.owner, vo -> {
            platWithdrawVo = vo;
            if (platWithdrawVo.msg_type == 2) {
                showError(platWithdrawVo.message);
            } else {
                refreshWithdrawView(platWithdrawVo);
            }

        });
        viewModel.platWithdrawConfirmVoMutableLiveData.observe(this.owner, ov -> {
            platWithdrawConfirmVo = ov;
            refreshWithdrawConfirmView(platWithdrawConfirmVo);
        });
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
        LoadingDialog.show(getContext());
        viewModel.getChooseWithdrawBankDetailInfo("1");

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
            CfLog.i("refreshInitView ChannelVo = bankCardCashVo.channel_list.get(0).isWebView == 1");
            binding.nsDefaultView.setVisibility(View.GONE);
            binding.nsErrorView.setVisibility(View.GONE);//隐藏错误信息页面
            binding.nsSetWithdrawalRequest.setVisibility(View.GONE);//单数据页面隐藏
            binding.nsSetWithdrawalRequestMore.setVisibility(View.GONE);//多金额页面隐藏
            binding.nsConfirmWithdrawalRequest.setVisibility(View.GONE); //确认提款页面隐藏
            binding.nsH5View.setVisibility(View.VISIBLE);//h5展示
            binding.wvH5View.setBackground(getContext().getDrawable(R.color.red));
            String url = bankCardCashVo.channel_list.get(0).thiriframe_url;
            if (!StringUtils.isStartHttp(url)) {
                url = DomainUtil.getDomain2() + url;
            }
            binding.wvH5View.loadUrl(url, getHeader());
            binding.wvH5View.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
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
                refreshRequestView(bankCardCashVo, bankCardCashVo.channel_list.get(0));
            }
        }

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
        String times, count, starttime, endtime, rest;
        times = String.valueOf(bankCardCashVo.times);
        count = bankCardCashVo.count;
        starttime = bankCardCashVo.wraptime.starttime;
        endtime = bankCardCashVo.wraptime.endtime;
        rest = StringUtils.formatToSeparate(Float.valueOf(bankCardCashVo.rest));
        String textSource
                = "注意：每日限制提款" + times + ",您已提款" + count + "次\n" + "提款时间为"
                + starttime + "至" + endtime + "您今日剩余额度为" + rest + "元";
        binding.tvShowNoticeInfo.setText(textSource);
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
            binding.bankWithdrawalView.tvUserNameShow.setText(bankCardCashVo.user.username);
            binding.bankWithdrawalView.tvWithdrawalTypeShow.setText("银行卡");
            binding.bankWithdrawalView.tvWithdrawalAmountMethod.setText(bankCardCashVo.user.cafAvailableBalance);
        } else if (binding.nsSetWithdrawalRequestMore.getVisibility() == View.VISIBLE) {
            binding.tvUserNameShowMore.setText(bankCardCashVo.user.username);
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
                binding.bankWithdrawalView.tvWithdrawalAmountShow.setTextColor(getContext().getColor(R.color.red));
            }
            String bankInfoString = bankCardCashVo.banks.get(0).bank_name + " " + bankCardCashVo.banks.get(0).account;
            binding.bankWithdrawalView.tvActualWithdrawalAmountBankShow.setText(bankInfoString);

        } else if (binding.nsSetWithdrawalRequestMore.getVisibility() == View.VISIBLE) {
            binding.tvWithdrawalAmountShowMore.setText(textSource);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                binding.bankWithdrawalView.tvWithdrawalAmountShow.setTextColor(getContext().getColor(R.color.red));
            }
            binding.tvActualWithdrawalAmountBankShowMore.setText(bankCardCashVo.banks.get(0).bank_name + " " + bankCardCashVo.banks.get(0).account);
        }
        channeBankVo = bankCardCashVo.banks.get(0);
    }

    /**
     * 刷新多个金额选择View
     *
     * @param channelVo
     */
    private void refreshSelectAmountUI(BankCardCashVo.ChannelVo channelVo) {
        if (channelVo.fixamountList.size() > 0) {
            CfLog.i("刷新 多金额选择区域 size = " + channelVo.fixamountList.size());
            if (adapter == null) {
                CfLog.i("adapter  == null 刷新 多金额选择区域 size = " + channelVo.fixamountList.size());

                adapter = new GridViewViewAdapter(context, (ArrayList<String>) channelVo.fixamountList, this);
            } else {
                CfLog.i("adapter  != null 刷新 多金额选择区域 size = " + channelVo.fixamountList.size());
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
            binding.tvSetWithdrawalRequest.setTextColor(getContext().getColor(R.color.red));
            binding.tvConfirmWithdrawalRequest.setTextColor(getContext().getColor(R.color.red));
        }
        binding.nsErrorView.setVisibility(View.GONE);//展示错误信息页面
        binding.nsSetWithdrawalRequest.setVisibility(View.GONE);//单数据页面展示
        binding.nsSetWithdrawalRequestMore.setVisibility(View.GONE);//多金额页面隐藏
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

    /**
     * 刷新提交点订单后页面
     */
    private void refreshWithdrawConfirmView(PlatWithdrawConfirmVo vo) {
        binding.llShowChooseCard.setVisibility(View.GONE);//顶部通用、大额提现View隐藏
        binding.llShowNoticeInfo.setVisibility(View.GONE); //顶部提示信息隐藏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            binding.tvSetWithdrawalRequest.setTextColor(getContext().getColor(R.color.black));
            binding.tvConfirmWithdrawalRequest.setTextColor(getContext().getColor(R.color.black));
            binding.tvOverWithdrawalRequest.setTextColor(getContext().getColor(R.color.red));
        }
        binding.nsErrorView.setVisibility(View.GONE);//展示错误信息页面
        binding.nsSetWithdrawalRequest.setVisibility(View.GONE);//单数据页面展示
        binding.nsSetWithdrawalRequestMore.setVisibility(View.GONE);//多金额页面隐藏
        binding.nsH5View.setVisibility(View.GONE);//h5隐藏
        binding.nsConfirmWithdrawalRequest.setVisibility(View.GONE); //确认提款页面隐藏
        binding.nsOverView.setVisibility(View.VISIBLE);
        CfLog.i("refreshWithdrawConfirmView = " + vo.toString());
        //msg_type 1 2 状态均为成功
        if (vo.msg_type == 1 || vo.msg_type == 2) {
            //成功
            binding.nsOverView.setVisibility(View.VISIBLE); //订单结果页面
            binding.llOverViewApply.tvOverMsg.setText(vo.msg_detail);
        } else if (vo.msg_type == 4) {
            //稍后刷新重试
            binding.nsOverView.setVisibility(View.VISIBLE); //订单结果页面展示
            binding.llOverViewApply.tvOverMsg.setText(vo.msg_detail);
        } else {
            //失败
            binding.nsOverView.setVisibility(View.VISIBLE); //订单结果页面展示
            binding.llOverViewApply.tvOverMsg.setText(vo.msg_detail);
        }
    }

    /**
     * 固额提现 提款金额 多个选择金额btn 点击回调
     */
    @Override
    public void callbackWithAmount(String amount) {

        CfLog.i("callbackWithAmount = " + amount);
        binding.etInputMoneyMore.setText(amount);
        binding.tvActualWithdrawalAmountShowMore.setText(amount);

    }

    /**
     * 顶部选项卡点击回调
     */
    @Override
    public void callbackWithFruitHor(BankCardCashVo.ChannelVo selectVO) {
        selectChanneVo = selectVO;//设置选中的channelVo
        CfLog.i("callbackWithFruitHor == " + selectChanneVo.toString());
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
                binding.nsErrorView.setVisibility(View.GONE);//展示错误信息页面
                binding.nsSetWithdrawalRequest.setVisibility(View.GONE);//单数据
                // 页面隐藏
                binding.nsSetWithdrawalRequestMore.setVisibility(View.GONE);//多金额页面隐藏
                binding.nsConfirmWithdrawalRequest.setVisibility(View.GONE); //确认提款页面隐藏
                binding.nsH5View.setVisibility(View.VISIBLE);//h5展示
                String url = bankCardCashVo.channel_list.get(0).thiriframe_url;
                if (!StringUtils.isStartHttp(url)) {
                    url = DomainUtil.getDomain2() + url;
                }
                binding.wvH5View.loadUrl(url, getHeader());
                binding.wvH5View.setWebViewClient(new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        view.loadUrl(url);
                        return true;
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
                    CfLog.i("单金额状态 = " + showMessage);
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
                    CfLog.i("多金额状态 = " + showMessage);
                    binding.tvActualWithdrawalAmountBankShowMore.setText(showMessage);
                    ppw.dismiss();
                });
            }
        };

        adapter.addAll(bankCardCashVo.banks);

        ppw = new XPopup.Builder(getContext()).asCustom(new ListDialog(getContext(), getContext().getString(R.string.txt_select_bank), adapter, 20));
        ppw.show();
    }

    /**
     * 提交订单
     *
     * @param channel_typenum 上一级列表传递过来的 id
     * @param channel_child   channel_list内选中的bean typenum
     * @param money           输入的金额
     * @param bankinfo        选中的收款银行卡的id
     */
    private void requestNext(String channel_typenum, String channel_child, String money, String bankinfo) {

        HashMap<String, String> map = new HashMap<>();
        map.put("action", "platwithdraw");
        map.put("bankinfo", bankinfo);
        map.put("channel_child", "1");
        map.put("channel_typenum", channel_typenum);
        map.put("check", "");
        map.put("controller", "security"); //列表也选择的取款类型
        map.put("flag", "withdraw");
        map.put("money", money);
        map.put("usdtType", "1");

        CfLog.i("requestNext --> " + map.toString());
        viewModel.getPlatWithdraw(map);
    }

    /**
     * 确定提交
     */
    private void requestConfirmWithdraw(String money, String cardid, String channel_child) {

        HashMap<String, String> map = new HashMap<>();
        map.put("controller", "security");
        map.put("action", "platwithdraw");
        map.put("flag", "confirm");
        map.put("check", "");
        map.put("money", money);
        map.put("handing_fee", platWithdrawVo.datas.handing_fee);
        map.put("cardid", cardid);
        map.put("usdtType", "1"); //列表也选择的取款类型
        map.put("play_source", String.valueOf(1));
        map.put("usdt_type", "1");
        map.put("channel_child", channel_child);
        map.put("smscode", "");
        map.put("smstype", "");
        CfLog.i("确定提交requestConfirmWithdraw --> " + map.toString());
        viewModel.postConfirmWithdraw(map);

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
}
