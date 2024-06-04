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
import com.xtree.base.utils.TagUtils;
import com.xtree.base.vo.ProfileVo;
import com.xtree.base.widget.ListDialog;
import com.xtree.base.widget.LoadingDialog;
import com.xtree.base.widget.MsgDialog;
import com.xtree.base.widget.TipDialog;
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

import me.xtree.mvvmhabit.utils.SPUtils;
import me.xtree.mvvmhabit.utils.ToastUtils;
import me.xtree.mvvmhabit.utils.Utils;
import project.tqyb.com.library_res.databinding.ItemTextBinding;

/**
 * 虚拟币提款
 */
public class VirtualWithdrawalDialog extends BottomPopupView {

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
    //private BankWithdrawalDialog.BankWithdrawalClose bankWithdrawalClose;
    private BasePopupView ppwError = null; // 底部弹窗 (显示错误信息)
    private ProfileVo mProfileVo;

    public VirtualWithdrawalDialog(@NonNull Context context) {
        super(context);
    }

    public static VirtualWithdrawalDialog newInstance(Context context, LifecycleOwner owner, ChooseInfoVo.ChannelInfo channelInfo, BankWithdrawalDialog.BankWithdrawalClose bankWithdrawalClose) {
        VirtualWithdrawalDialog dialog = new VirtualWithdrawalDialog(context);
        context = context;
        dialog.context = context;
        dialog.owner = owner;
        dialog.channelInfo = channelInfo;
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

        String json = SPUtils.getInstance().getString(SPKeyGlobal.HOME_PROFILE);
        mProfileVo = new Gson().fromJson(json, ProfileVo.class);
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
        viewModel.virtualCashVoMutableLiveData.observe(owner, vo -> {
            virtualCashVo = vo;
            //|| virtualCashVo.rest == null || virtualCashVo.usdtinfo == null || virtualCashVo.usdtinfo.isEmpty()
            if (virtualCashVo == null) {
                showError();
            } else if (virtualCashVo.msg_type == 1 || virtualCashVo.msg_type == 2) {
                if (TextUtils.equals("您今天已没有可用提款次数", virtualCashVo.message)) {
                    refreshError(virtualCashVo.message);
                } else if (virtualCashVo.message != null) {
                    ToastUtils.showError(virtualCashVo.message);
                    dismiss();
                }
                return;
            } else {
                selectUsdtInfo = virtualCashVo.usdtinfo.get(0);
                refreshSetUI();
            }
        });
        //虚拟币确认提款信息
        viewModel.virtualSecurityVoMutableLiveData.observe(owner, vo -> {
            usdtSecurityVo = vo;
            if (usdtSecurityVo == null || usdtSecurityVo.datas == null) {
                if (usdtSecurityVo.msg_type == 2 && !TextUtils.isEmpty(usdtSecurityVo.message)){
                    showErrorMessage(usdtSecurityVo.message);
                    return;
                }else {
                    ToastUtils.showError(getContext().getString(R.string.txt_network_error));
                    dismiss();
                }

            } else {
                refreshSecurityUI();
            }

        });
        //虚拟币完成申请
        viewModel.virtualConfirmVoMutableLiveData.observe(owner, vo -> {
            TagUtils.tagEvent(getContext(), "wd", "vc");
            usdtConfirmVo = vo;
            if (usdtConfirmVo == null || usdtConfirmVo.user == null) {
                ToastUtils.showError(getContext().getString(R.string.txt_network_error));
                dismiss();
            } else {
                refreshConfirmUI();
            }
        });

    }

    private void requestData() {
        HashMap<String, String> map = new HashMap<>();
        map.put("usdt_type", channelInfo.type);
        CfLog.i("requestData =" + channelInfo.toString());
        viewModel.getChooseWithdrawVirtual(map);
    }

    /**
     * 刷新初始UI
     */
    private void refreshSetUI() {
        binding.llSetRequestView.setVisibility(View.VISIBLE);
        String showRest = StringUtils.formatToSeparate(Float.valueOf(virtualCashVo.rest));
        //注意：每天限制提款5次，您已提款1次 提款时间为00:01至00:00，您今日剩余提款额度为 199900.00元
        final String notice = "<font color=#EE5A5A>注意:</font>";
        String times, count, startTime, endTime, rest;
        times = "<font color=#EE5A5A>" + String.valueOf(virtualCashVo.times) + "</font>";
        count = "<font color=#EE5A5A>" + virtualCashVo.count + "</font>";
        startTime = "<font color=#000000>" + virtualCashVo.wraptime.starttime + "</font>";
        endTime = "<font color=#000000>" + virtualCashVo.wraptime.endtime + "</font>";
        rest = StringUtils.formatToSeparate(Float.valueOf(virtualCashVo.rest));
        String testTxt = "<font color=#EE5A5A>" + rest + "</font>";
        String format = getContext().getResources().getString(R.string.txt_withdraw_bank_top_tip);
        String textSource = String.format(format, notice, times, count, startTime, endTime, testTxt);

        binding.tvNotice.setText(HtmlCompat.fromHtml(textSource, HtmlCompat.FROM_HTML_MODE_LEGACY));

        if (virtualCashVo.user != null) {
            if (virtualCashVo.user.username != null) {
                binding.tvUserNameShow.setText(virtualCashVo.user.username);
            } else if (virtualCashVo.user.nickname != null) {
                binding.tvUserNameShow.setText(virtualCashVo.user.nickname);
            }
        } else if (mProfileVo != null) {
            final String name = StringUtils.splitWithdrawUserName(mProfileVo.username);
            binding.tvUserNameShow.setText(name);
        }

        binding.tvWithdrawalTypeShow.setText(channelInfo.title);
        String quota = virtualCashVo.availablebalance;

        binding.tvWithdrawalAmountShow.setText(quota);//虚拟币 提款金额
        String temp = virtualCashVo.usdtinfo.get(0).min_money + "元,最高" + virtualCashVo.usdtinfo.get(0).max_money + "元";
        binding.tvWithdrawalTypeShow1.setText(temp);
        binding.tvInfoExchangeRateShow.setText(virtualCashVo.exchangerate);
        binding.tvCollectionUsdt.setText(virtualCashVo.usdtinfo.get(0).usdt_type + " " + virtualCashVo.usdtinfo.get(0).usdt_card);
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
            } else if (Double.valueOf(binding.etInputMoney.getText().toString()) > Double.valueOf(selectUsdtInfo.max_money)) {
                ToastUtils.showLong(R.string.txt_input_amount_tip);
            } else if (Double.valueOf(binding.etInputMoney.getText().toString()) < Double.valueOf(selectUsdtInfo.min_money)) {
                ToastUtils.showLong(R.string.txt_input_amount_tip);
            }  else {
                hideKeyBoard();
                requestWithdrawVirtual();
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

        if (virtualCashVo.user != null) {
            if (virtualCashVo.user.username != null) {
                binding.tvName.setText(virtualCashVo.user.username);
            } else if (virtualCashVo.user.nickname != null) {
                binding.tvName.setText(virtualCashVo.user.nickname);
            }
        } else if (mProfileVo != null) {
            final String name = StringUtils.splitWithdrawUserName(mProfileVo.username);
            binding.tvName.setText(name);
        }

        binding.tvWithdrawalAmount.setText(StringUtils.formatToSeparate(Float.valueOf(virtualCashVo.user.availablebalance)));
        binding.tvWithdrawalRequestAmount.setText(usdtSecurityVo.datas.money);
        binding.tvWithdrawalActualAmount.setText(usdtSecurityVo.datas.arrive);//实际提款金额
        binding.tvVirtualMoneyType.setText(usdtSecurityVo.usdt_type);//提款类型
        binding.tvWithdrawalAddressShow.setText(usdtSecurityVo.usdt_card);//提款地址
        binding.tvWithdrawalHandlingFeeShow.setText(usdtSecurityVo.datas.handing_fee);

        //下一步
        binding.ivConfirmNext.setOnClickListener(v -> {
            requestConfirmVirtual();
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
        if (usdtConfirmVo.msg_detail != null) {
            //msg_type 2的状态提款成功
            if (TextUtils.equals("账户提款申请成功", usdtConfirmVo.msg_detail) && usdtConfirmVo.msg_type.equals("2")) {
                binding.ivOverApply.setBackgroundResource(R.mipmap.ic_over_apply);
            } else if (TextUtils.equals("请刷新后重试", usdtConfirmVo.msg_detail)) {
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

    private void refreshError(String message) {
        binding.llVirtualTop.setVisibility(View.GONE);
        binding.llSetRequestView.setVisibility(View.GONE);
        binding.llVirtualConfirmView.setVisibility(View.GONE);
        binding.llOverApply.setVisibility(View.GONE);
        binding.tvShowNumberErrorMessage.setVisibility(View.VISIBLE);
        binding.tvShowNumberErrorMessage.setText(message);
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
                    binding.tvWithdrawalTypeShow1.setText(temp);

                    selectUsdtInfo = vo;

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
    private void requestWithdrawVirtual() {
        LoadingDialog.show(getContext());
        String money = binding.etInputMoney.getText().toString();
        HashMap<String, String> map = new HashMap<>();
        map.put("action", "platwithdraw");
        map.put("channel_child", "1");
        map.put("channel_typenum", "1");
        map.put("check", "1");
        map.put("controller", "security");
        map.put("flag", "withdraw");
        map.put("money", money);
        map.put("name", selectUsdtInfo.usdt_type);
        map.put("usdtid", selectUsdtInfo.id);
        String usdtType = channelInfo.type;
        map.put("usdtType", usdtType);
        CfLog.i("requestWithdrawVirtual -->" + map);
        viewModel.postPlatWithdrawVirtual(map);
    }

    /**
     * 设置提款 完成申请
     */
    private void requestConfirmVirtual() {
        LoadingDialog.show(getContext());
        HashMap<String, String> map = new HashMap<>();
        map.put("controller", "security");
        map.put("action", "platwithdraw");
        map.put("flag", "confirm");
        map.put("check", "1");
        map.put("name", "");
        map.put("money", usdtSecurityVo.datas.money);
        map.put("handing_fee", usdtSecurityVo.datas.handing_fee);
        map.put("cardid", "");
        map.put("play_source", "");
        map.put("usdtid", selectUsdtInfo.id);
        map.put("usdt_type", usdtSecurityVo.drawal_type);
        map.put("plot_id", usdtSecurityVo.datas.plot_id);
        map.put("channel_child", "");
        map.put("smscode", "");
        map.put("smstype", "");

        CfLog.i("requestConfirmVirtual -->" + map);

        viewModel.postConfirmWithdrawVirtual(map);

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
     *  显示错误提示信息
     * @param message
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
}
