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
import com.xtree.base.utils.TagUtils;
import com.xtree.base.widget.ListDialog;
import com.xtree.base.widget.LoadingDialog;
import com.xtree.base.widget.MsgDialog;
import com.xtree.base.widget.TipDialog;
import com.xtree.mine.R;
import com.xtree.mine.data.Injection;
import com.xtree.mine.databinding.DialogBankWithdrawalUsdtBinding;
import com.xtree.mine.ui.viewmodel.ChooseWithdrawViewModel;
import com.xtree.mine.vo.ChooseInfoVo;
import com.xtree.mine.vo.USDTCashVo;
import com.xtree.mine.vo.USDTConfirmVo;
import com.xtree.mine.vo.USDTSecurityVo;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import me.xtree.mvvmhabit.utils.ToastUtils;
import me.xtree.mvvmhabit.utils.Utils;
import project.tqyb.com.library_res.databinding.ItemTextBinding;

/**
 * USDT虚拟币提款
 */
public class USDTWithdrawalDialog extends BottomPopupView {
    private String type = "USDT";//默认选中USDT提款
    private LifecycleOwner owner;
    ChooseWithdrawViewModel viewModel;
    private ChooseInfoVo.ChannelInfo channelInfo;
    ArrayList<USDTCashVo.Usdtinfo> usdtinfoTRC = new ArrayList<>(); //TRC20地址 仅用于钱包
    private USDTCashVo.Usdtinfo selectUsdtInfo;//选中的支付
    private USDTCashVo.Channel firstChannel, secondChannel;

    private USDTCashVo usdtCashVo;

    private USDTSecurityVo usdtSecurityVo;
    private USDTConfirmVo usdtConfirmVo;
    private BankWithdrawalDialog.BankWithdrawalClose bankClose;

    private BasePopupView ppwError = null; // 底部弹窗 (显示错误信息)
    private
    @NonNull
    DialogBankWithdrawalUsdtBinding binding;
    private String usdtid;//第二步传递的 提款地址ide id

    public USDTWithdrawalDialog(@NonNull Context context) {
        super(context);
    }

    public static USDTWithdrawalDialog newInstance(Context context, LifecycleOwner owner, ChooseInfoVo.ChannelInfo channelInfo, BankWithdrawalDialog.BankWithdrawalClose bankClose) {
        USDTWithdrawalDialog dialog = new USDTWithdrawalDialog(context);
        dialog.owner = owner;
        dialog.channelInfo = channelInfo;
        dialog.bankClose = bankClose;
        CfLog.i("USDTWithdrawalDialog");
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
        hideKeyBoard();
        initData();
        initViewObservable();
        requestData();

    }

    private void initView() {
        binding = DialogBankWithdrawalUsdtBinding.bind(findViewById(R.id.ll_root));
        binding.ivwClose.setOnClickListener(v -> dismiss());
        binding.tvwTitle.setText(channelInfo.title);

        //显示设置请求View
        /*if (binding.llVirtualTop.getVisibility() == View.VISIBLE) {
            //点击嗨钱包
            binding.llOtherUsdt.setOnClickListener(v -> {
                binding.llOtherUsdt.setBackgroundResource(R.drawable.bg_dialog_top_bank_selected);
                binding.llUsdt.setBackgroundResource(R.drawable.bg_dialog_top_bank_noselected);
                binding.tvWithdrawalTypeShow.setText("USDT");//提款类型
            });
            //点击usdt
            binding.llUsdt.setOnClickListener(v -> {
                binding.llOtherUsdt.setBackgroundResource(R.drawable.bg_dialog_top_bank_noselected);
                binding.llUsdt.setBackgroundResource(R.drawable.bg_dialog_top_bank_selected);
                binding.tvWithdrawalTypeShow.setText("USDT");//提款类型
            });
        }*/
    }

    private void initData() {
        viewModel = new ChooseWithdrawViewModel((Application) Utils.getContext(), Injection.provideHomeRepository());
    }

    private void initViewObservable() {
        //USDT提款设置提款请求 返回model
        viewModel.usdtCashVoMutableLiveData.observe(owner, vo -> {
            usdtCashVo = vo;

            if (usdtCashVo == null || usdtCashVo.channel_list == null || usdtCashVo.usdtinfo == null || usdtCashVo.usdtinfo.isEmpty()) {
                showErrorByChannel();
            }
            //异常
            else if (usdtCashVo.msg_type == 2 || usdtCashVo.msg_type == 1) {
                final String leftString = getContext().getString(R.string.txt_no_withdrawals_available_tip);

                if (TextUtils.equals(leftString, usdtCashVo.message)) {
                    refreshError(usdtCashVo.message);
                } else {
                    ToastUtils.show(usdtCashVo.message, ToastUtils.ShowType.Fail);
                    dismiss();
                }
                return;
            }
            // 提现选项卡不能为空
            else if (usdtCashVo.channel_list == null || usdtCashVo.channel_list.isEmpty() ||
                    usdtCashVo.usdtinfo == null || usdtCashVo.usdtinfo.isEmpty()) {
                refreshError(getContext().getString(R.string.txt_network_error));
                return;
            } else {
                for (int i = 0; i < usdtCashVo.usdtinfo.size(); i++) {
                    if (usdtCashVo.usdtinfo.get(i).usdt_type.contains("TRC20")) {
                        usdtinfoTRC.add(usdtCashVo.usdtinfo.get(i));
                    }
                }
                // selectUsdtInfo = usdtCashVo.usdtinfo.get(0);
                refreshSetUI();
            }

        });
        //USDT确认提款信息
        viewModel.usdtSecurityVoMutableLiveData.observe(owner, vo -> {
            usdtSecurityVo = vo;
            //|| usdtSecurityVo.user == null
            if (usdtSecurityVo == null || usdtSecurityVo.datas == null) {

                if ("2".equals(usdtSecurityVo.msg_type) && TextUtils.equals("抱歉，您的提款金额低于单笔最低提现金额，请确认后再进行操作", usdtSecurityVo.message)) {
                    ToastUtils.showError(usdtSecurityVo.message);
                } else {
                    if (!TextUtils.isEmpty(usdtSecurityVo.message)) {
                        ToastUtils.showError(usdtSecurityVo.message);

                    } else {
                        ToastUtils.showError(getContext().getString(R.string.txt_network_error));
                        dismiss();
                    }

                }

            } else {
                refreshSecurityUI();
            }
        });
        //USDT完成申请
        viewModel.usdtConfirmVoMutableLiveData.observe(owner, vo -> {
            TagUtils.tagEvent(getContext(), "wd", "ut");
            usdtConfirmVo = vo;
            if (usdtConfirmVo.user == null) {
                ToastUtils.showError(getContext().getString(R.string.txt_network_error));
                dismiss();
            } else {
                refreshConfirmUI();
            }
        });

    }

    private void requestData() {
        LoadingDialog.show(getContext());
        HashMap<String, String> map = new HashMap<>();
        map.put("usdt_type", channelInfo.type);
        CfLog.i("requestData =" + channelInfo.toString());
        viewModel.getChooseWithdrawUSDT(map);
    }

    /**
     * 刷新初始UI
     */
    private void refreshSetUI() {
        binding.llSetRequestView.setVisibility(View.VISIBLE);
        if (usdtCashVo.channel_list.size() == 1) {
            binding.tvVirtualUsdt.setText(usdtCashVo.channel_list.get(0).title);
            binding.tvVirtualOther.setVisibility(View.GONE);
            firstChannel = usdtCashVo.channel_list.get(0);
        } else if (usdtCashVo.channel_list.size() == 2) {
            binding.tvVirtualUsdt.setText(usdtCashVo.channel_list.get(0).title);
            binding.tvVirtualOther.setText(usdtCashVo.channel_list.get(1).title);
            firstChannel = usdtCashVo.channel_list.get(0);
            secondChannel = usdtCashVo.channel_list.get(1);
        }
        final String notice = "<font color=#EE5A5A>注意:</font>";
        String times, count, startTime, endTime, rest;
        times = "<font color=#EE5A5A>" + String.valueOf(usdtCashVo.times) + "</font>";
        count = "<font color=#EE5A5A>" + usdtCashVo.count + "</font>";
        startTime = "<font color=#000000>" + usdtCashVo.wraptime.starttime + "</font>";
        endTime = "<font color=#000000>" + usdtCashVo.wraptime.endtime + "</font>";
        rest = StringUtils.formatToSeparate(Float.valueOf(usdtCashVo.rest));
        String testTxt = "<font color=#EE5A5A>" + rest + "</font>";
        String format = getContext().getResources().getString(R.string.txt_withdraw_bank_top_tip);
        String textSource = String.format(format, notice, times, count, startTime, endTime, testTxt);

        binding.tvNotice.setText(HtmlCompat.fromHtml(textSource, HtmlCompat.FROM_HTML_MODE_LEGACY));

        binding.tvUserNameShow.setText(usdtCashVo.user.username);
        binding.tvWithdrawalTypeShow.setText("USDT");//提款类型

        // binding.tvWithdrawalAmountMethod.setText(usdtCashVo.channel_list.get(0).title);//设置收款USDT账户 firstChannel
        if (firstChannel != null && !TextUtils.isEmpty(firstChannel.title)) {
            binding.tvWithdrawalAmountMethod.setText(firstChannel.title);//提款方式
        } else if (!TextUtils.isEmpty(usdtCashVo.usdtinfo.get(0).usdt_type)) {
            binding.tvWithdrawalAmountMethod.setText(usdtCashVo.usdtinfo.get(0).usdt_type);//提款方式设置为提款类型
        }
        String quota = usdtCashVo.availablebalance;
        binding.tvWithdrawalAmountShow.setText(quota);//提款余额
        String temp = usdtCashVo.usdtinfo.get(0).min_money + "元,最高" + usdtCashVo.usdtinfo.get(0).max_money + "元";
        usdtid = usdtCashVo.usdtinfo.get(0).id;
        binding.tvWithdrawalTypeShow1.setText(temp);
        binding.tvInfoExchangeRateShow.setText(usdtCashVo.exchangerate);
        CfLog.e("firstChannel.title " + firstChannel.toString());
        //默认第一个是trc20

        if (TextUtils.equals("嗨钱包usdt", firstChannel.title)) {
            binding.tvCollectionUsdt.setText(usdtinfoTRC.get(0).usdt_type + " " + usdtinfoTRC.get(0).usdt_card);
            selectUsdtInfo = usdtinfoTRC.get(0);
            type = "TRC";
        } else {
            binding.tvCollectionUsdt.setText(usdtCashVo.usdtinfo.get(0).usdt_type + " " + usdtCashVo.usdtinfo.get(0).usdt_card);
            selectUsdtInfo = usdtCashVo.usdtinfo.get(0);
            type = "USDT";
        }

        //关闭软键盘弹起
        binding.etInputMoney.clearFocus();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(binding.etInputMoney.getWindowToken(), 0);
        }
        //注册监听
        initListener();

    }

    private void initListener() {
        hideKeyBoard();
       /* binding.llUsdt.setOnClickListener(v -> {
            binding.llUsdt.setBackgroundResource(R.drawable.bg_dialog_top_bank_selected);
            binding.llOtherUsdt.setBackgroundResource(R.drawable.bg_dialog_top_bank_noselected);
            type = "USDT";
        });
        binding.tvVirtualUsdt.setOnClickListener(v -> {
            binding.llOtherUsdt.setBackgroundResource(R.drawable.bg_dialog_top_bank_selected);
            binding.llUsdt.setBackgroundResource(R.drawable.bg_dialog_top_bank_noselected);
            type = "USDT";
        });*/
        //选中非USDT提款 提币地址更换为支持TRC20
        binding.tvVirtualUsdt.setOnClickListener(v -> {
            binding.llUsdt.setBackgroundResource(R.drawable.bg_dialog_top_bank_selected);
            binding.llOtherUsdt.setBackgroundResource(R.drawable.bg_dialog_top_bank_noselected);
            final String name = binding.tvVirtualUsdt.getText().toString().trim();
            if (name.contains("嗨钱包usdt")) {
                type = "TRC";
            } else {
                type = "USDT";
            }
            //显示地址
            if (type.equals("USDT")) {
                binding.tvCollectionUsdt.setText(usdtCashVo.usdtinfo.get(0).usdt_type + " " + usdtCashVo.usdtinfo.get(0).usdt_card);
                selectUsdtInfo = usdtCashVo.usdtinfo.get(0);
                String temp = selectUsdtInfo.min_money + "元,最高" + selectUsdtInfo.max_money + "元";
                binding.tvWithdrawalTypeShow1.setText(temp);
            } else {
                if (usdtinfoTRC.size() > 0) {
                    binding.tvCollectionUsdt.setText(usdtinfoTRC.get(0).usdt_type + " " + usdtinfoTRC.get(0).usdt_card);
                    selectUsdtInfo = usdtinfoTRC.get(0);
                    String temp = selectUsdtInfo.min_money + "元,最高" + selectUsdtInfo.max_money + "元";
                    binding.tvWithdrawalTypeShow1.setText(temp);
                } else {
                    binding.tvCollectionUsdt.setText("");
                    binding.tvWithdrawalTypeShow1.setText("");
                }
            }

            binding.tvWithdrawalAmountMethod.setText(firstChannel.title);//提款方式
            CfLog.e("点击 USDT firstChannel.title = " + firstChannel.toString());
        });
        //USDT提款
        binding.tvVirtualOther.setOnClickListener(v -> {
            binding.llUsdt.setBackgroundResource(R.drawable.bg_dialog_top_bank_noselected);
            binding.llOtherUsdt.setBackgroundResource(R.drawable.bg_dialog_top_bank_selected);
            final String name = binding.tvVirtualOther.getText().toString().trim();
            if (name.contains("嗨钱包usdt")) {
                type = "TRC";
            } else {
                type = "USDT";
            }
            //显示地址
            if (type.equals("USDT")) {
                binding.tvCollectionUsdt.setText(usdtCashVo.usdtinfo.get(0).usdt_type + " " + usdtCashVo.usdtinfo.get(0).usdt_card);
                selectUsdtInfo = usdtCashVo.usdtinfo.get(0);
                String temp = selectUsdtInfo.min_money + "元,最高" + selectUsdtInfo.max_money + "元";
                binding.tvWithdrawalTypeShow1.setText(temp);
            } else {
                if (usdtinfoTRC.size() > 0) {
                    binding.tvCollectionUsdt.setText(usdtinfoTRC.get(0).usdt_type + " " + usdtinfoTRC.get(0).usdt_card);
                    selectUsdtInfo = usdtinfoTRC.get(0);
                    String temp = selectUsdtInfo.min_money + "元,最高" + selectUsdtInfo.max_money + "元";
                    binding.tvWithdrawalTypeShow1.setText(temp);
                } else {
                    binding.tvCollectionUsdt.setText("");
                    binding.tvWithdrawalTypeShow1.setText("");
                }

            }
            binding.tvWithdrawalAmountMethod.setText(secondChannel.title);//提款方式
            CfLog.e("点击 USDT secondChannel.title = " + secondChannel.toString());

        });
        //提款金额输入框与提款金额显示View
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
                //换算到账个数
                String temp = s.toString();
                if (temp != null && !TextUtils.isEmpty(temp)) {
                    Double f1 = Double.parseDouble(temp);
                    Double f2 = Double.parseDouble(usdtCashVo.exchangerate);
                    DecimalFormat df = new DecimalFormat("0.00");
                    binding.tvInfoActualNumberShow.setText(df.format(f1 / f2));
                } else if (TextUtils.isEmpty(temp)) {
                    binding.tvInfoActualNumberShow.setText("0");
                } else {
                    binding.tvInfoActualNumberShow.setText("0");
                }
            }
        });
        //点击USDT收款地址
        binding.tvCollectionUsdt.setOnClickListener(v -> {
            showAllCollectionDialog(type);
        });
        binding.llCollectionUsdtInput.setOnClickListener(v -> {
            showAllCollectionDialog(type);
        });
        //点击下一步
        binding.ivNext.setOnClickListener(v -> {
            if (binding.etInputMoney.getText().length() > 9) {
                ToastUtils.showLong(R.string.txt_input_amount_tip);
            } else if (TextUtils.isEmpty(binding.etInputMoney.getText().toString())) {
                ToastUtils.showLong(R.string.txt_input_amount_tip);
            } else if (Double.valueOf(binding.etInputMoney.getText().toString()) > Double.valueOf(selectUsdtInfo.max_money)) {
                ToastUtils.showLong(R.string.txt_input_amount_tip);
            } else if (Double.valueOf(binding.etInputMoney.getText().toString()) < Double.valueOf(selectUsdtInfo.min_money)) {
                ToastUtils.showLong(R.string.txt_input_amount_tip);
            } else if (TextUtils.isEmpty(binding.tvWithdrawalTypeShow1.getText().toString())) {
                ToastUtils.showLong(R.string.txt_select_withdrawal_address);
            } else {
                hideKeyBoard();
                requestWithdrawUSDT();
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
    private void refreshSecurityUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            binding.tvConfirmWithdrawalRequest.setTextColor(getContext().getColor(R.color.red));
        }
        binding.llSetRequestView.setVisibility(View.GONE);
        binding.llVirtualConfirmView.setVisibility(View.VISIBLE);
        binding.tvConfirmWithdrawalAmount.setText(usdtCashVo.user.username);
        binding.tvConfirmWithdrawalTypeShow.setText(StringUtils.formatToSeparate(Float.valueOf(usdtCashVo.user.availablebalance)));
        binding.tvConfirmAmountShow.setText(usdtSecurityVo.usdt_type);
        binding.tvWithdrawalVirtualTypeShow.setText(usdtSecurityVo.usdt_type);
        binding.tvWithdrawalTypeShow.setText(usdtSecurityVo.usdt_type);
        binding.tvWithdrawalAmountTypeShow.setText(usdtSecurityVo.usdt_type);
        binding.tvWithdrawalActualArrivalShow.setText(usdtSecurityVo.datas.arrive);
        binding.tvWithdrawalExchangeRateShow.setText(usdtSecurityVo.exchangerate);
        binding.tvWithdrawalAddressShow.setText(usdtSecurityVo.usdt_card);
        binding.tvWithdrawalHandlingFeeShow.setText(usdtSecurityVo.datas.handing_fee);

        //下一步
        binding.ivConfirmNext.setOnClickListener(v -> {
            requestConfirmUSDT(usdtSecurityVo);
        });
        //上一步
        binding.ivConfirmPrevious.setOnClickListener(v -> {
            binding.llSetRequestView.setVisibility(View.VISIBLE);
            binding.llVirtualConfirmView.setVisibility(View.GONE);
        });
    }

    /**
     * 刷新完成申请UI
     */
    private void refreshConfirmUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            binding.tvOverWithdrawalRequest.setTextColor(getContext().getColor(R.color.red));
        }
        binding.llVirtualConfirmView.setVisibility(View.GONE);
        binding.llOverApply.setVisibility(View.VISIBLE);
        //msg_type 为2 或者msg_detail为账户提款申请成功

        if (TextUtils.equals("账户提款申请成功", usdtConfirmVo.msg_detail) && usdtConfirmVo.msg_type.equals("2")) {
            binding.ivOverApply.setBackgroundResource(R.mipmap.ic_over_apply);
        } else if (usdtConfirmVo.error != null) {
            binding.tvOverMsg.setText("账户提款申请失败");
            binding.tvOverDetail.setText(usdtConfirmVo.msg_detail);
            binding.ivOverApply.setBackgroundResource(R.mipmap.ic_over_apply_err);
        } else {
            binding.tvOverMsg.setText("账户提款申请失败");
            binding.tvOverDetail.setText(usdtConfirmVo.msg_detail);
            binding.ivOverApply.setBackgroundResource(R.mipmap.ic_over_apply_err);
        }
        //继续提现
        binding.ivContinueConfirmNext.setOnClickListener(v -> {
            dismiss();
        });
        //关闭
        binding.ivContinueConfirmPrevious.setOnClickListener(v -> {
            dismiss();
            bankClose.closeBankWithdrawal();
        });
    }

    /*显示错误信息*/
    private void refreshError(String message) {
        binding.llTop.setFocusableInTouchMode(true);
        binding.llVirtualTop.setVisibility(View.GONE);
        binding.llSetRequestView.setVisibility(View.GONE);

        binding.llVirtualConfirmView.setVisibility(View.GONE);
        binding.llOverApply.setVisibility(View.GONE);
        binding.etInputMoney.clearFocus();
        // hideKeyBoard();
        binding.tvShowNumberErrorMessage.setVisibility(View.VISIBLE);
        binding.tvShowNumberErrorMessage.setText(message);
    }

    ItemTextBinding binding2;
    BasePopupView ppw = null; // 底部弹窗 (选择**菜单)

    /**
     * 显示USDT收款地址
     */
    private void showAllCollectionDialog(String type) {
        if (type.contains("TRC") || type.contains("trc")) {
            showCollectionDialog(usdtinfoTRC);
        } else {
            showCollectionDialog(usdtCashVo.usdtinfo);
        }
    }

    private void showCollectionDialog(ArrayList<USDTCashVo.Usdtinfo> list) {
        CachedAutoRefreshAdapter adapter = new CachedAutoRefreshAdapter<USDTCashVo.Usdtinfo>() {
            @NonNull
            @Override
            public CacheViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                CacheViewHolder holder = new CacheViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_text, parent, false));
                return holder;
            }

            @Override
            public void onBindViewHolder(@NonNull CacheViewHolder holder, int position) {
                binding2 = ItemTextBinding.bind(holder.itemView);
                USDTCashVo.Usdtinfo vo = get(position);
                selectUsdtInfo = vo;
                String showMessage = vo.usdt_type + " " + vo.usdt_card;

                binding2.tvwTitle.setText(showMessage);
                binding2.tvwTitle.setOnClickListener(v -> {
                    binding.tvCollectionUsdt.setText(showMessage);
                    String temp = vo.min_money + "元,最高" + vo.max_money + "元";
                    binding.tvWithdrawalTypeShow1.setText(temp);
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

    /**
     * 设置提款 请求 下一步
     */
    private void requestWithdrawUSDT() {
        LoadingDialog.show(getContext());
        String money = binding.etInputMoney.getText().toString();
        HashMap<String, String> map = new HashMap<>();
        map.put("action", "platwithdraw");
        map.put("controller", "security");
        map.put("flag", "withdraw");
        map.put("check", "");
        map.put("channel_child", "1");
        map.put("channel_typenum", "1");
        String usdtType = channelInfo.type;
        map.put("usdtType", "2");
        map.put("money", money);
        if (type.equals("USDT")) {
            map.put("name", "usdt");
        } else {
            map.put("name", "TRC20_USDT");
        }
        final String usdtId = binding.tvCollectionUsdt.getText().toString().trim();

        map.put("usdtid", selectUsdtInfo.id);
        CfLog.e("requestWithdrawUSDT = " + map);
        viewModel.postPlatWithdrawUSDT(map);
    }

    /**
     * 设置提款 完成申请
     */
    private void requestConfirmUSDT(final USDTSecurityVo vo) {
        LoadingDialog.show(getContext());
        HashMap<String, String> map = new HashMap<>();
        map.put("action", "platwithdraw");
        map.put("check", "");
        map.put("channel_child", null);
        map.put("controller", "security");
        map.put("flag", "confirm");
        map.put("smscode", "");
        map.put("smstype", "");

        map.put("handing_fee", vo.datas.handing_fee);
        map.put("money", vo.datas.arrive);
        map.put("name", vo.name);
        map.put("play_source", "1");
        map.put("usdt_type", vo.datas.drawal_type);
        map.put("usdtid", vo.drawal_type);
        /* map.put("cardid", "");*/

        /* map.put("usdt_type", usdtSecurityVo.usdt_type);*/
/*        map.put("plot_id", selectUsdtInfo.id);
        map.put("channel_child", usdtSecurityVo.usdt_type);*/

        CfLog.i("requestConfirmUSDT = " + map);

        viewModel.postConfirmWithdrawUSDT(map);

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
