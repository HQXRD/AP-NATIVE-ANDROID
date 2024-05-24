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
import com.xtree.base.utils.UuidUtil;
import com.xtree.base.widget.ListDialog;
import com.xtree.base.widget.LoadingDialog;
import com.xtree.base.widget.MsgDialog;
import com.xtree.mine.R;
import com.xtree.mine.data.Injection;
import com.xtree.mine.databinding.DialogBankWithdrawalVirtualBinding;
import com.xtree.mine.ui.viewmodel.ChooseWithdrawViewModel;
import com.xtree.mine.vo.ChooseInfoVo;
import com.xtree.mine.vo.VirtualCashVo;
import com.xtree.mine.vo.VirtualConfirmVo;
import com.xtree.mine.vo.VirtualSecurityVo;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import me.xtree.mvvmhabit.utils.ToastUtils;
import me.xtree.mvvmhabit.utils.Utils;
import project.tqyb.com.library_res.databinding.ItemTextBinding;

/**
 * 虚拟币提款
 */
public class VirtualWithdrawalDialog extends BottomPopupView {
    private String checkCode;
    private String usdtType;
    private Context context;
    private LifecycleOwner owner;
    ChooseWithdrawViewModel viewModel;
    private ChooseInfoVo.ChannelInfo channelInfo;

    private VirtualCashVo.UsdtInfo selectUsdtInfo;//选中的支付
    private VirtualCashVo virtualCashVo;

    private VirtualSecurityVo usdtSecurityVo;
    private VirtualConfirmVo usdtConfirmVo;
    @NonNull
    DialogBankWithdrawalVirtualBinding binding;
    private BankWithdrawalDialog.BankWithdrawalClose bankWithdrawalClose;

    private String userid;

    public VirtualWithdrawalDialog(@NonNull Context context) {
        super(context);
    }

    public static VirtualWithdrawalDialog newInstance(Context context, LifecycleOwner owner, ChooseInfoVo.ChannelInfo channelInfo, BankWithdrawalDialog.BankWithdrawalClose bankWithdrawalClose) {
        VirtualWithdrawalDialog dialog = new VirtualWithdrawalDialog(context);
        context = context;
        dialog.context = context;
        dialog.owner = owner;
        dialog.channelInfo = channelInfo;
        dialog.bankWithdrawalClose = bankWithdrawalClose;
        CfLog.i("VirtualWithdrawalDialog");
        return dialog;
    }

    public static VirtualWithdrawalDialog newInstance(Context context, LifecycleOwner owner, ChooseInfoVo.ChannelInfo channelInfo, BankWithdrawalDialog.BankWithdrawalClose bankWithdrawalClose, final String checkCode, final String usdtType) {
        VirtualWithdrawalDialog dialog = new VirtualWithdrawalDialog(context);
        context = context;
        dialog.context = context;
        dialog.owner = owner;
        dialog.channelInfo = channelInfo;
        dialog.bankWithdrawalClose = bankWithdrawalClose;
        dialog.checkCode = checkCode;
        dialog.usdtType = usdtType;
        CfLog.i("VirtualWithdrawalDialog");
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
        requestData();

    }

    private void initView() {
        binding = DialogBankWithdrawalVirtualBinding.bind(findViewById(R.id.ll_root));
        binding.ivwClose.setOnClickListener(v -> dismiss());
        binding.tvwTitle.setText(channelInfo.title);
        //显示设置请求View
        if (binding.llVirtualTop.getVisibility() == View.VISIBLE) {
            //点击嗨钱包
            binding.llOtherUsdt.setOnClickListener(v -> {
                binding.llOtherUsdt.setBackgroundResource(R.drawable.bg_dialog_top_bank_selected);
                binding.llUsdt.setBackgroundResource(R.drawable.bg_dialog_top_bank_noselected);
            });
            //点击usdt
            binding.llUsdt.setOnClickListener(v -> {
                binding.llOtherUsdt.setBackgroundResource(R.drawable.bg_dialog_top_bank_noselected);
                binding.llUsdt.setBackgroundResource(R.drawable.bg_dialog_top_bank_selected);
            });
        }

    }

    private void initData() {
        LoadingDialog.show(getContext());
        viewModel = new ChooseWithdrawViewModel((Application) Utils.getContext(), Injection.provideHomeRepository());
    }

    private void initViewObservable() {
        hideKeyBoard();
        //虚拟币提款设置提款请求 返回model
        viewModel.virtualCashMoYuVoMutableLiveData.observe(owner, vo -> {
            virtualCashVo = vo;
            if (virtualCashVo.msg_type == 1 || virtualCashVo.msg_type == 2) {
                ToastUtils.showError(virtualCashVo.message);
                dismiss();
                return;
            }
            refreshSetUI();
        });
        //虚拟币确认提款信息
        viewModel.virtualSecurityMoYuVoMutableLiveData.observe(owner, vo -> {
            usdtSecurityVo = vo;

            if (usdtSecurityVo.datas == null
                    || (!TextUtils.isEmpty(usdtSecurityVo.message) && usdtSecurityVo.message.contains("抱歉"))
                    || "2".equals(usdtSecurityVo.msg_type)) {
                showErrorDialog(usdtSecurityVo.message);
            } else {
                refreshSecurityUI();
            }
        });
        //虚拟币完成申请
        viewModel.virtualConfirmMuYuVoMutableLiveData.observe(owner, vo -> {
            TagUtils.tagEvent(getContext(), "wd", "vc");
            usdtConfirmVo = vo;
            refreshConfirmUI();
        });

    }

    private void requestData() {

        LoadingDialog.show(getContext());
        viewModel.getChooseWithdrawVirtualMoYu(checkCode, usdtType);
    }

    /**
     * 刷新初始UI
     */
    private void refreshSetUI() {
        binding.llSetRequestView.setVisibility(View.VISIBLE);
        final String notice = "<font color=#99A0B1>注意:</font>";
        String times, count, startTime, endTime, rest;
        times = "<font color=#99A0B1>" + String.valueOf(virtualCashVo.times) + "</font>";
        count = "<font color=#99A0B1>" + virtualCashVo.count + "</font>";
        startTime = "<font color=#99A0B1>" + virtualCashVo.wraptime.starttime + "</font>";
        endTime = "<font color=#99A0B1>" + virtualCashVo.wraptime.endtime + "</font>";
        rest = StringUtils.formatToSeparate(Float.valueOf(virtualCashVo.rest));
        String testTxt = "<font color=#FF6C6C>" + rest + "</font>";
        String format = getContext().getResources().getString(R.string.txt_withdraw_bank_top_tip);
        String textSource = String.format(format, notice, times, count, startTime, endTime, testTxt);

        binding.tvNotice.setText(HtmlCompat.fromHtml(textSource, HtmlCompat.FROM_HTML_MODE_LEGACY));

        binding.tvUserNameShow.setText(virtualCashVo.user.username);
        binding.tvWithdrawalTypeShow.setText(channelInfo.title);
        String quota = virtualCashVo.availablebalance;

        binding.tvWithdrawalAmountShow.setText(quota);//虚拟币 提款金额
        String temp = virtualCashVo.usdtinfo.get(0).min_money + "元,最高" + virtualCashVo.usdtinfo.get(0).max_money + "元";
        binding.tvWithdrawalTypeShow1.setText(temp);
        binding.tvInfoExchangeRateShow.setText(virtualCashVo.exchangerate);
        binding.tvCollectionUsdt.setText(virtualCashVo.usdtinfo.get(0).usdt_type + " " + virtualCashVo.usdtinfo.get(0).usdt_card);

        userid = virtualCashVo.usdtinfo.get(0).id;

        //注册监听
        initListener();

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
                if (temp != null && !TextUtils.isEmpty(temp)) {
                    float f1 = Float.parseFloat(temp);
                    float f2 = Float.parseFloat(virtualCashVo.exchangerate);
                    DecimalFormat df = new DecimalFormat("0.00");
                    df.format(f1 / f2);
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
            showCollectionDialog(virtualCashVo.usdtinfo);
        });
        binding.llCollectionUsdtInput.setOnClickListener(v -> {
            showCollectionDialog(virtualCashVo.usdtinfo);
        });
        //点击下一步
        binding.ivNext.setOnClickListener(v -> {
            if (TextUtils.isEmpty(binding.etInputMoney.getText().toString())) {
                ToastUtils.showLong(R.string.txt_input_amount_tip);
            } else if (Double.valueOf(binding.etInputMoney.getText().toString()) > Double.valueOf(virtualCashVo.usdtinfo.get(0).max_money)) {
                ToastUtils.showLong(R.string.txt_input_amount_tip);
            } else if (Double.valueOf(binding.etInputMoney.getText().toString()) < Double.valueOf(virtualCashVo.usdtinfo.get(0).min_money)) {
                ToastUtils.showLong(R.string.txt_input_amount_tip);
            } else {
                hideKeyBoard();
                String money = binding.etInputMoney.getText().toString().trim();
                String realCount = binding.tvInfoActualNumberShow.getText().toString().trim();
                // String usdtId = binding.tvCollectionUsdt.getText().toString().trim();
                String usdtId = userid;
                requestWithdrawVirtual(money, realCount, usdtId, checkCode, virtualCashVo);

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
            binding.tvConfirmWithdrawalRequest.setTextColor(getContext().getColor(R.color.clr_choose_20));
        }
        binding.llSetRequestView.setVisibility(View.GONE);
        binding.llVirtualConfirmView.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(virtualCashVo.user.username)) {
            binding.tvConfirmWithdrawalAmount.setText(virtualCashVo.user.username);
        }

        binding.tvConfirmWithdrawalTypeShow.setText(StringUtils.formatToSeparate(Float.valueOf(virtualCashVo.user.availablebalance)));
        //可提款金额
        binding.tvConfirmAmountShow.setText(usdtSecurityVo.datas.money);
        binding.tvWithdrawalVirtualTypeShow.setText(usdtSecurityVo.usdt_type);
        binding.tvWithdrawalTypeShow.setText(usdtSecurityVo.usdt_type);
        binding.tvWithdrawalAmountTypeShow.setText(usdtSecurityVo.usdt_type);
        if (usdtSecurityVo.datas.arrive == null) {
            binding.tvWithdrawalActualArrivalShow.setVisibility(View.GONE);
        } else {
            binding.tvWithdrawalActualArrivalShow.setText(usdtSecurityVo.datas.arrive);
        }

        binding.tvWithdrawalExchangeRateShow.setText(usdtSecurityVo.exchangerate);
        binding.tvWithdrawalAddressShow.setText(usdtSecurityVo.usdt_card);
        binding.tvWithdrawalHandlingFeeShow.setText(usdtSecurityVo.datas.handing_fee);

        //下一步
        binding.ivConfirmNext.setOnClickListener(v -> {
            requestConfirmVirtual(usdtSecurityVo);
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
            binding.tvOverWithdrawalRequest.setTextColor(getContext().getColor(R.color.clr_choose_20));
        }
        binding.llVirtualConfirmView.setVisibility(View.GONE);
        binding.llOverApply.setVisibility(View.VISIBLE);
        if (usdtConfirmVo.msg_detail != null) {
            //msg_type 2的状态提款成功
            if (usdtConfirmVo.msg_detail.equals("账户提款申请成功") && usdtConfirmVo.msg_type.equals("2")) {
                binding.ivOverApply.setBackgroundResource(R.mipmap.ic_over_apply);
            } else if (usdtConfirmVo.msg_detail.equals("请刷新后重试")) {
                binding.tvOverMsg.setText("账户提款申请失败");
                binding.tvOverDetail.setText(usdtConfirmVo.msg_detail);
                binding.ivOverApply.setBackgroundResource(R.mipmap.ic_over_apply_err);
            } else {
                binding.tvOverMsg.setText("账户提款申请失败");
                binding.tvOverDetail.setText(usdtConfirmVo.msg_detail);
                binding.ivOverApply.setBackgroundResource(R.mipmap.ic_over_apply_err);
            }
        }

        //继续提现
        binding.ivContinueConfirmNext.setOnClickListener(v -> {
            dismiss();
        });
        //关闭
        binding.ivContinueConfirmPrevious.setOnClickListener(v -> {
            dismiss();
        });
    }

    ItemTextBinding binding2;
    BasePopupView ppw = null; // 底部弹窗 (选择**菜单)

    /**
     * 显示USDT收款地址
     */
    private void showCollectionDialog(ArrayList<VirtualCashVo.UsdtInfo> list) {
        CachedAutoRefreshAdapter adapter = new CachedAutoRefreshAdapter<VirtualCashVo.UsdtInfo>() {
            @NonNull
            @Override
            public CacheViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                CacheViewHolder holder = new CacheViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_text, parent, false));
                return holder;
            }

            @Override
            public void onBindViewHolder(@NonNull CacheViewHolder holder, int position) {
                binding2 = ItemTextBinding.bind(holder.itemView);
                VirtualCashVo.UsdtInfo vo = get(position);
                String showMessage = vo.usdt_type + " " + vo.usdt_card;

                binding2.tvwTitle.setText(showMessage);
                binding2.tvwTitle.setOnClickListener(v -> {
                    binding.tvCollectionUsdt.setText(showMessage);
                    String temp = vo.min_money + "元,最高" + vo.max_money + "元";
                    CfLog.i("------onBindViewHolder = " + temp);
                    binding.tvWithdrawalTypeShow1.setText(temp);
                    selectUsdtInfo = vo;

                    userid = selectUsdtInfo.id;

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
     * 显示异常Dialog
     */
    private void showErrorDialog(String showMessage) {

        ppw = new XPopup.Builder(getContext())
                .asCustom(new MsgDialog(getContext(), getContext().getString(R.string.txt_kind_tips), showMessage, false, new MsgDialog.ICallBack() {
                    @Override
                    public void onClickLeft() {
                        ppw.dismiss();
                    }

                    @Override
                    public void onClickRight() {
                        ppw.dismiss();
                    }
                }));
        ppw.show();
    }

    /**
     * 设置提款 请求 下一步
     */
    private void requestWithdrawVirtual(String money, String realCount, String usdtId, String checkCode, final VirtualCashVo virtualCashVo) {

        HashMap<String, String> map = new HashMap<>();
        map.put("action", "platwithdraw");
        map.put("controller", "security");
        map.put("flag", "withdraw");
        map.put("usdtType", virtualCashVo.usdt_type);
        map.put("nonce", UuidUtil.getID24());

        map.put("money", money);
        map.put("realCount", realCount);
        map.put("usdtid", usdtId);
        map.put("check", checkCode);
        CfLog.i("requestWithdrawUSDT = " + map);
        viewModel.postPlatWithdrawVirtualMoYu(map);

    }

    /**
     * 设置提款 完成申请
     */
    private void requestConfirmVirtual(VirtualSecurityVo vo) {
        LoadingDialog.show(getContext());
        HashMap<String, String> map = new HashMap<>();

        map.put("controller", "security");
        map.put("action", "platwithdraw");
        map.put("flag", "confirm");
        map.put("cardid", "");
        map.put("check", vo.check);
        map.put("handing_fee", vo.datas.handing_fee);
        map.put("money", vo.datas.money);
        map.put("nonce", UuidUtil.getID24());
        map.put("realCount", "");
        map.put("usdtType", vo.drawal_type);
        map.put("usdt_type", vo.usdt_type);
        map.put("usdtid", userid);//选中的提款地址

        CfLog.i("requestConfirmVirtual -->" + map);

        viewModel.postConfirmWithdrawVirtualMoYu(map);

    }
}
