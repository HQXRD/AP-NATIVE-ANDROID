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
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;
import com.xtree.base.adapter.CacheViewHolder;
import com.xtree.base.adapter.CachedAutoRefreshAdapter;
import com.xtree.base.router.RouterActivityPath;
import com.xtree.base.utils.CfLog;
import com.xtree.base.utils.StringUtils;
import com.xtree.base.utils.UuidUtil;
import com.xtree.base.widget.ListDialog;
import com.xtree.base.widget.LoadingDialog;
import com.xtree.mine.R;
import com.xtree.mine.data.Injection;
import com.xtree.mine.databinding.DialogBankWithdrawalUsdtBinding;
import com.xtree.mine.databinding.DialogWithdrawalUsdtConfirmBinding;
import com.xtree.mine.ui.viewmodel.ChooseWithdrawViewModel;
import com.xtree.mine.vo.ChooseInfoVo;
import com.xtree.mine.vo.USDTCashMoYuVo;
import com.xtree.mine.vo.USDTConfirmMoYuVo;
import com.xtree.mine.vo.USDTSecurityMoYuVo;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import me.xtree.mvvmhabit.utils.ToastUtils;
import me.xtree.mvvmhabit.utils.Utils;
import project.tqyb.com.library_res.databinding.ItemTextBinding;

/**
 * USDT虚拟币提款
 */
public class USDTWithdrawalDialog extends BottomPopupView implements FruitHorUSDTRecyclerViewAdapter.IUSDTFruitHorCallback {
    private LifecycleOwner owner;
    ChooseWithdrawViewModel viewModel;
    private ChooseInfoVo.ChannelInfo channelInfo;
    ArrayList<USDTCashMoYuVo.UsdtInfo> UsdtInfoTRC = new ArrayList<>(); //TRC20地址 仅用于钱包
    private USDTCashMoYuVo.UsdtInfo selectUsdtInfo;//选中的支付
    private USDTCashMoYuVo.Channel selectorTopChannel;//选中的支付通道

    private USDTCashMoYuVo cashMoYuVo;

    private USDTSecurityMoYuVo usdtSecurityVo;
    private USDTConfirmMoYuVo usdtConfirmVo;
    private BankWithdrawalDialog.BankWithdrawalClose bankClose;
    private
    @NonNull
    DialogBankWithdrawalUsdtBinding binding;
    private String checkCode;
    private String usdtType;
    private FruitHorUSDTRecyclerViewAdapter recyclerViewAdapter;

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

    public static USDTWithdrawalDialog newInstance(Context context, LifecycleOwner owner, ChooseInfoVo.ChannelInfo channelInfo, BankWithdrawalDialog.BankWithdrawalClose bankClose, final String checkCode, final String usdtType) {
        USDTWithdrawalDialog dialog = new USDTWithdrawalDialog(context);
        dialog.owner = owner;
        dialog.channelInfo = channelInfo;
        dialog.bankClose = bankClose;
        dialog.checkCode = checkCode;
        dialog.usdtType = usdtType;
        CfLog.i("USDTWithdrawalDialog checkCode = " + checkCode + " usdtType =" + usdtType);
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
        viewModel.usdtCashMoYuVoMutableLiveData.observe(owner, vo -> {
            cashMoYuVo = vo;
            if (cashMoYuVo == null || cashMoYuVo.usdtinfo == null || cashMoYuVo.count == null || cashMoYuVo.rest == null) {
                ToastUtils.showError(getContext().getString(R.string.txt_network_error));
                dismiss();
            }
            //异常
            else if (cashMoYuVo.msg_type == 2 || cashMoYuVo.msg_type == 1) {
                ToastUtils.show(cashMoYuVo.message, ToastUtils.ShowType.Fail);
                dismiss();
                return;
            } else if (cashMoYuVo.msg_type == 2 && ("您今天已没有可用提款次数").equals(cashMoYuVo.message)) {
                ToastUtils.showError(cashMoYuVo.message);
                dismiss();
                return;
            }  //"ur_here": "资金密码检查",
            else if (!TextUtils.isEmpty(cashMoYuVo.ur_here) && ("资金密码检查").equals(cashMoYuVo.ur_here)) {
                ToastUtils.showError(cashMoYuVo.ur_here);
                bankClose.closeBankByPSW();
                return;
            } else if ("2".equals(cashMoYuVo.msg_type) && "您的资金账户因为其他操作被锁定，请稍后重试".equals(cashMoYuVo.message)) {
                ToastUtils.showError(cashMoYuVo.message);
                dismiss();
                return;
            } else if ("由于您长时间未操作，请重新登录".equals(cashMoYuVo.message)) {
                ToastUtils.showError(cashMoYuVo.message);
                popLoginView();
            } else if (cashMoYuVo.usdtinfo == null || cashMoYuVo.usdtinfo.isEmpty()) {
                ToastUtils.showError(getContext().getString(R.string.txt_network_error));
            } else {
                selectUsdtInfo = cashMoYuVo.usdtinfo.get(0);
                refreshSetUI();
            }

        });
        //USDT确认提款信息
        viewModel.usdtSecurityMoYuVoMutableLiveData.observe(owner, vo -> {
            usdtSecurityVo = vo;
            if (usdtSecurityVo == null || usdtSecurityVo.datas == null) {
                ToastUtils.showError(getContext().getString(R.string.txt_network_error));
                dismiss();
            } else if (usdtSecurityVo.datas != null) {
                refreshSecurityUI();
            } else if ("资金密码检查".equals(usdtSecurityVo.ur_here)) {
                //业务异常跳转资金安全密码
                ToastUtils.showError("业务异常跳转资金安全密码");

            } else if ("2".equals(usdtSecurityVo.msg_type) && "您的资金账户因为其他操作被锁定，请稍后重试".equals(usdtSecurityVo.message)) {
                ToastUtils.showError(usdtSecurityVo.message);
                dismiss();
                return;
            } else {

            }
        });
        //USDT完成申请
        viewModel.usdtConfirmMoYuVoMutableLiveData.observe(owner, vo -> {
            usdtConfirmVo = vo;
            if (usdtConfirmVo == null || usdtConfirmVo.msg_detail == null) {
                ToastUtils.showError(getContext().getString(R.string.txt_network_error));
                dismiss();
            } else if ("资金密码检查".equals(usdtConfirmVo.ur_here)) {
                //业务异常跳转资金安全密码
                ToastUtils.showError("业务异常跳转资金安全密码");

            } else if ("2".equals(usdtConfirmVo.msg_type) && "您的资金账户因为其他操作被锁定，请稍后重试".equals(usdtConfirmVo.message)) {
                ToastUtils.showError(usdtConfirmVo.message);
                dismiss();
                return;
            } else {
                refreshConfirmUI();
                ;
            }

        });

    }

    /*业务异常 跳转登录*/
    private void popLoginView() {
        ARouter.getInstance().build(RouterActivityPath.Mine.PAGER_LOGIN_REGISTER).navigation();
    }

    private void requestData() {
        LoadingDialog.show(getContext());
        viewModel.getChooseWithdrawUSDTMoYu(checkCode, usdtType);
    }

    private void refreshTopUI(USDTCashMoYuVo vo) {
        for (int i = 0; i < vo.channel_list.size(); i++) {
            if (i == 0) {
                vo.channel_list.get(0).flag = true;
            } else {
                vo.channel_list.get(i).flag = false;
            }
            selectorTopChannel = vo.channel_list.get(0);
        }
        recyclerViewAdapter = new FruitHorUSDTRecyclerViewAdapter(vo.channel_list, this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        binding.rvShowChooseCard.setLayoutManager(layoutManager);
        binding.rvShowChooseCard.addItemDecoration(new FruitHorRecyclerViewAdapter.SpacesItemDecoration(10));
        binding.rvShowChooseCard.setAdapter(recyclerViewAdapter);
        binding.rvShowChooseCard.setItemAnimator(new DefaultItemAnimator());
    }

    /**
     * 刷新初始UI
     */
    private void refreshSetUI() {
        if (cashMoYuVo.channel_list.isEmpty() || cashMoYuVo.channel_list == null) {
            binding.llShowChooseCard.setVisibility(View.GONE);
        } else {
            refreshTopUI(cashMoYuVo);
        }
        binding.llSetRequestView.setVisibility(View.VISIBLE);
        //注意：每天限制提款5次，您已提款1次 提款时间为00:01至00:00，您今日剩余提款额度为 199900.00元
        String times, count, starttime, endtime, rest;
        times = String.valueOf(cashMoYuVo.times);
        count = cashMoYuVo.count;
        starttime = cashMoYuVo.wraptime.starttime;
        endtime = cashMoYuVo.wraptime.endtime;
        rest = StringUtils.formatToSeparate(Float.valueOf(cashMoYuVo.rest));
        String format = getContext().getResources().getString(R.string.txt_withdraw_bank_top_tip);
        String textSource = String.format(format, times, count, starttime, endtime, rest);
        binding.tvNotice.setText(textSource);

        binding.tvUserNameShow.setText(cashMoYuVo.user.username);
        binding.tvWithdrawalTypeShow.setText(cashMoYuVo.usdtinfo.get(0).usdt_type);//提款类型
        String quota = cashMoYuVo.availablebalance;

        binding.tvWithdrawalAmountShow.setText(quota);//提款余额
        String temp = cashMoYuVo.usdtinfo.get(0).min_money + "元,最高" + cashMoYuVo.usdtinfo.get(0).max_money + "元";
        binding.tvWithdrawalTypeShow1.setText(temp);
        binding.tvInfoExchangeRateShow.setText(cashMoYuVo.exchangerate);
        binding.tvCollectionUsdt.setText(cashMoYuVo.usdtinfo.get(0).usdt_type + " " + cashMoYuVo.usdtinfo.get(0).usdt_card);
        binding.tvCollectionUsdt.setOnClickListener(v -> {
            showCollectionDialog(cashMoYuVo.usdtinfo);
        });
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
        selectUsdtInfo = cashMoYuVo.usdtinfo.get(0);
        String temp = selectUsdtInfo.min_money + "元,最高" + selectUsdtInfo.max_money + "元";
        binding.tvWithdrawalTypeShow1.setText(temp);
        //显示全部地址地址
        binding.tvCollectionUsdt.setText(cashMoYuVo.usdtinfo.get(0).usdt_type + " " + cashMoYuVo.usdtinfo.get(0).usdt_card);
        binding.tvWithdrawalAmountMethod.setText(cashMoYuVo.usdtinfo.get(0).usdt_type);//提款方式

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
                    Double f2 = Double.parseDouble(cashMoYuVo.exchangerate);
                    DecimalFormat df = new DecimalFormat("0.00");
                    binding.tvInfoActualNumberShow.setText(df.format(f1 / f2));
                } else if (TextUtils.isEmpty(temp)) {
                    binding.tvInfoActualNumberShow.setText("0");
                } else {
                    binding.tvInfoActualNumberShow.setText("0");
                }
            }
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
                String money = binding.etInputMoney.getText().toString().trim();
                String realCount = binding.tvInfoActualNumberShow.getText().toString().trim();
                String usdtId = binding.tvCollectionUsdt.getText().toString().trim();
                requestWithdrawUSDT(money, realCount, usdtId, checkCode, cashMoYuVo);
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
        binding.tvUserNameShow.setText(cashMoYuVo.user.username);
        binding.llVirtualConfirmView.llConfirmView.setVisibility(View.VISIBLE);
        DialogWithdrawalUsdtConfirmBinding bindView = binding.llVirtualConfirmView;
        binding.llVirtualConfirmView.tvConfirmUserNameShow.setText(usdtSecurityVo.user.username);
        bindView.tvConfirmWithdrawalTypeShow.setText(cashMoYuVo.user.availablebalance);
        bindView.tvConfirmAmountShow.setText(usdtSecurityVo.usdt_type);
        bindView.tvWithdrawalVirtualTypeShow.setText(usdtSecurityVo.usdt_type);

        bindView.tvWithdrawalActualArrivalShow.setText(usdtSecurityVo.datas.arrive);
        bindView.tvWithdrawalExchangeRateShow.setText(usdtSecurityVo.exchangerate);
        bindView.tvWithdrawalAddressShow.setText(usdtSecurityVo.usdt_card);
        // 提款类型
        bindView.tvWithdrawalAmountTypeShow.setText(usdtSecurityVo.usdt_type);
        bindView.tvWithdrawalHandlingFeeShow.setText(usdtSecurityVo.datas.handing_fee);

        //下一步
        bindView.ivConfirmNext.setOnClickListener(v -> {
            //String money  , String realCount  ,String handingFee ,String usdt_type ,String usdtType ,  String checkCode
            String money = binding.etInputMoney.getText().toString().trim();
            String realCount = binding.tvInfoActualNumberShow.getText().toString().trim();
            String handingFee = binding.tvCollectionUsdt.getText().toString().trim();
            String usdt_type = usdtSecurityVo.usdt_type;
            String usdtType = usdtSecurityVo.usdt_type;

            requestConfirmUSDT(money, realCount, handingFee, usdt_type, usdtType, checkCode, usdtSecurityVo);
        });
        //上一步
        bindView.ivConfirmPrevious.setOnClickListener(v -> {
            binding.llSetRequestView.setVisibility(View.VISIBLE);
            binding.llVirtualConfirmView.llConfirmView.setVisibility(View.GONE);
            //tv_confirm_withdrawal_request
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                binding.tvConfirmWithdrawalRequest.setTextColor(getContext().getColor(R.color.cl_over_tip));
            }
        });
    }

    /**
     * 刷新完成申请UI
     */
    private void refreshConfirmUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            binding.tvOverWithdrawalRequest.setTextColor(getContext().getColor(R.color.clr_choose_20));
        }
        binding.llVirtualConfirmView.llConfirmView.setVisibility(View.GONE);
        binding.llOverApply.setVisibility(View.VISIBLE);
        //msg_type 为2 或者msg_detail为账户提款申请成功
        if (usdtConfirmVo.msg_detail.equals("账户提款申请成功") && usdtConfirmVo.msg_type.equals("2")) {
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
        binding.ivContinueConfirmClose.setOnClickListener(v -> {
            dismiss();
            bankClose.closeBankWithdrawal();
        });
    }

    ItemTextBinding binding2;
    BasePopupView ppw = null; // 底部弹窗 (选择**菜单)

    /**
     * 显示USDT收款地址
     */
    private void showAllCollectionDialog(String type) {
        if (type.contains("TRC") || type.contains("trc")) {
            showCollectionDialog(UsdtInfoTRC);
        } else {
            showCollectionDialog(cashMoYuVo.usdtinfo);
        }
    }

    private void showCollectionDialog(ArrayList<USDTCashMoYuVo.UsdtInfo> list) {
        CachedAutoRefreshAdapter adapter = new CachedAutoRefreshAdapter<USDTCashMoYuVo.UsdtInfo>() {
            @NonNull
            @Override
            public CacheViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                CacheViewHolder holder = new CacheViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_text, parent, false));
                return holder;
            }

            @Override
            public void onBindViewHolder(@NonNull CacheViewHolder holder, int position) {
                binding2 = ItemTextBinding.bind(holder.itemView);
                USDTCashMoYuVo.UsdtInfo vo = get(position);
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
    private void requestWithdrawUSDT(String money, String realCount, String usdtId, String checkCode, final USDTCashMoYuVo cashMoYuVo) {
        LoadingDialog.show(getContext());

      /*  "action": "platwithdraw",
                "check": "3d917f5496f73e6c2b06b86722354599",
                "controller": "security",
                "flag": "withdraw",
                  "usdtType": "2"
                  "nonce": "01f0291c4277f8fffb3c37212d1d8de6",
                "money": "10",

                "realCount": 1.38,
                "usdtid": "2789",
              */

        HashMap<String, String> map = new HashMap<>();
        map.put("action", "platwithdraw");
        map.put("controller", "security");
        map.put("flag", "withdraw");
        map.put("usdtType", cashMoYuVo.usdt_type);
        map.put("nonce", UuidUtil.getID24());

        map.put("money", money);
        map.put("realCount", realCount);
        map.put("usdtid", usdtId);
        map.put("check", checkCode);
        CfLog.i("requestWithdrawUSDT = " + map);
        viewModel.postPlatWithdrawUSDTMoYu(map);
    }

    /**
     * 设置提款 完成申请
     */
    private void requestConfirmUSDT(String money, String realCount, String handingFee, String usdt_type, String usdtType, String checkCode, USDTSecurityMoYuVo usdtSecurityVo) {
       /* {
         "controller": "security",
            "action": "platwithdraw",
            "flag": "confirm",
                "cardid": "",
                "check": "72e49e769b01a2067444a2b3c2a5853b",
                "handing_fee": "0.00",
                "money": 10,
                "nonce": "5327a1599a53bb460a55b783bd39633c",
                "realCount": 1.38,
                "usdt_type": "ERC20_USDT",
                "usdtid": "2789",
                "usdtType": "2"
        }*/
        LoadingDialog.show(getContext());
        HashMap<String, String> map = new HashMap<>();
        map.put("controller", "security");
        map.put("action", "platwithdraw");
        map.put("flag", "confirm");
        map.put("cardid", "");
        map.put("check", checkCode);
        map.put("handing_fee", usdtSecurityVo.datas.handing_fee);
        map.put("money", money);
        map.put("nonce", UuidUtil.getID24());
        map.put("realCount", realCount);
        map.put("usdt_type", usdt_type);
        map.put("usdtid", usdtSecurityVo.usdtid);
        map.put("usdtType", usdtSecurityVo.usdt_type);

        CfLog.i("requestConfirmUSDT = " + map);

        viewModel.postConfirmWithdrawUSDTMoYu(map);

    }

    @Override
    public void callbackWithFruitHor(USDTCashMoYuVo.Channel selectVo) {

        //点击了不同头部 数显View
        if (selectVo.id.equals(selectUsdtInfo.id)) {
            selectorTopChannel = selectVo;

        }
    }
}
