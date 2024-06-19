package com.xtree.mine.ui.fragment.withdrawal;

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
import com.xtree.base.utils.StringUtils;
import com.xtree.base.utils.UuidUtil;
import com.xtree.base.vo.ProfileVo;
import com.xtree.base.widget.ListDialog;
import com.xtree.base.widget.LoadingDialog;
import com.xtree.base.widget.MsgDialog;
import com.xtree.mine.R;
import com.xtree.mine.data.Injection;
import com.xtree.mine.databinding.DialogBankWithdrawalUsdtBinding;
import com.xtree.mine.ui.viewmodel.ChooseWithdrawViewModel;
import com.xtree.mine.vo.USDTCashVo;
import com.xtree.mine.vo.USDTSecurityVo;
import com.xtree.mine.vo.withdrawVo.WithdrawalInfoVo;
import com.xtree.mine.vo.withdrawVo.WithdrawalListVo;
import com.xtree.mine.vo.withdrawVo.WithdrawalSubmitVo;
import com.xtree.mine.vo.withdrawVo.WithdrawalVerifyVo;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import me.xtree.mvvmhabit.utils.SPUtils;
import me.xtree.mvvmhabit.utils.ToastUtils;
import me.xtree.mvvmhabit.utils.Utils;
import project.tqyb.com.library_res.databinding.ItemTextBinding;

/**
 * USDT虚拟币提款
 */
public class USDTWithdrawalDialog extends BottomPopupView implements FruitHorUSDTRecyclerViewAdapter.IUSDTFruitHorCallback {
    private LifecycleOwner owner;
    ChooseWithdrawViewModel viewModel;

    private BankWithdrawalDialog.BankWithdrawalClose bankClose;
    private
    @NonNull
    DialogBankWithdrawalUsdtBinding binding;
    private String checkCode;
    private String usdtType;
    private FruitHorUSDTRecyclerViewAdapter recyclerViewAdapter;//顶部选项卡adapter
    private BasePopupView ppwError = null; // 底部弹窗 (显示错误信息)

    private String usdtid;//第二步传递的 提款地址ide id
    private ProfileVo mProfileVo;

    private String wtype;

    private ArrayList<WithdrawalInfoVo.UserBankInfo> trc20BankInfoList;//只支持trc20提款地址

    private ArrayList<WithdrawalListVo> listVo;
    private WithdrawalInfoVo infoVo;
    private WithdrawalInfoVo.UserBankInfo selectorBankInfo;//选中的支付地址

    private WithdrawalVerifyVo verifyVo;
    private WithdrawalSubmitVo submitVo;
    private WithdrawalListVo changVo;//切换的Vo
    private BasePopupView errorPopView;

    public USDTWithdrawalDialog(@NonNull Context context) {
        super(context);
    }

    public static USDTWithdrawalDialog newInstance(Context context,
                                                   LifecycleOwner owner,
                                                   final String wtype,
                                                   ArrayList<WithdrawalListVo> listVo,
                                                   final WithdrawalInfoVo infoVo,
                                                   final String checkCode) {
        USDTWithdrawalDialog dialog = new USDTWithdrawalDialog(context);
        dialog.owner = owner;
        dialog.wtype = wtype;
        dialog.checkCode = checkCode;
        dialog.listVo = listVo;
        dialog.infoVo = infoVo;
        dialog.trc20BankInfoList = new ArrayList<>();
        for (int i = 0; i < dialog.infoVo.user_bank_info.size(); i++) {
            WithdrawalInfoVo.UserBankInfo bankInfo = dialog.infoVo.user_bank_info.get(i);
            //将TRC20地址组装在一起
            if (TextUtils.equals("TRC20_USDT", bankInfo.usdt_type)) {
                dialog.trc20BankInfoList.add(bankInfo);
            }
        }


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
        /*requestData();*/
        String json = SPUtils.getInstance().getString(SPKeyGlobal.HOME_PROFILE);
        mProfileVo = new Gson().fromJson(json, ProfileVo.class);
    }

    private void initView() {
        binding = DialogBankWithdrawalUsdtBinding.bind(findViewById(R.id.ll_root));
        binding.ivwClose.setOnClickListener(v -> dismiss());
        binding.tvwTitle.setText(getContext().getString(R.string.txt_withdrawal_usdt_title));
    }

    private void initData() {
        viewModel = new ChooseWithdrawViewModel((Application) Utils.getContext(), Injection.provideHomeRepository());
    }

    private void initViewObservable() {

        //获取当前渠道详情
        viewModel.withdrawalInfoVoMutableLiveData.observe(owner, vo -> {
            infoVo = vo;
            if (infoVo != null && !infoVo.user_bank_info.isEmpty()) {
                trc20BankInfoList.clear();
                for (int i = 0; i < infoVo.user_bank_info.size(); i++) {
                    WithdrawalInfoVo.UserBankInfo bankInfo = infoVo.user_bank_info.get(i);
                    //将TRC20地址组装在一起
                    if (TextUtils.equals("TRC20_USDT", bankInfo.usdt_type)) {
                        trc20BankInfoList.add(bankInfo);
                    }
                }
                //业务正常 刷新页面
                refreshChangeUI(changVo, infoVo);
            } else {
                ToastUtils.showError(getContext().getString(R.string.txt_network_error));
            }
        });
        //获取当前渠道详情 错误信息
        viewModel.bankInfoVoErrorData.observe(owner, vo -> {
            final String message = vo;
            if (message != null && !TextUtils.isEmpty(message)) {
                showErrorDialog(message);
            } else {
                ToastUtils.showError(getContext().getString(R.string.txt_network_error));
            }
        });

        // 验证当前渠道信息
        viewModel.verifyVoMutableLiveData.observe(owner, vo -> {
            verifyVo = vo;
            CfLog.e("verifyVoMutableLiveData=" + vo.toString());
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

        //提款完成申请
        viewModel.submitVoMutableLiveData.observe(owner, vo -> {
            submitVo = vo;
            if (submitVo != null) {
                refreshSubmitUI(submitVo, null);
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

    }

    /**
     * 初始化顶部公共区域UI
     */
    private void initNoticeView() {
        //顶部公告区域
        String formatStr = getContext().getResources().getString(R.string.txt_withdraw_top_tip);
        String count, userCount, totalAmount;
        count = "<font color=#0C0319>" + infoVo.day_total_count + "</font>";
        userCount = "<font color=#F35A4E>" + infoVo.day_used_count + "</font>";
        totalAmount = "<font color=#F35A4E>" + infoVo.day_rest_amount + "</font>";
        String textTipSource = String.format(formatStr, count, userCount, totalAmount);
        binding.tvNotice.setText(HtmlCompat.fromHtml(textTipSource, HtmlCompat.FROM_HTML_MODE_LEGACY));

    }


    /**
     * 依据顶部卡片刷新提币地址
     *
     * @param changVo
     * @param infoVo
     */
    private void refreshChangeUI(WithdrawalListVo changVo, WithdrawalInfoVo infoVo) {
        //根据传入列表的地址数据判断提币数组数据 TRC情况下 只显示trc地址
        if (TextUtils.equals("TRC20_USDT", changVo.name)
                || changVo.name.contains("TRC")
                || changVo.name.contains("TRC20")
                || changVo.name.contains("trc")
                || changVo.name.contains("trc20")) {
            if (!trc20BankInfoList.isEmpty()) {
                String showAddress = trc20BankInfoList.get(0).usdt_type + "--" + trc20BankInfoList.get(0).account;
                CfLog.e("设置默认选中的提币地址=" + showAddress);
                //设置默认选中的提币地址
                selectorBankInfo = trc20BankInfoList.get(0);
                binding.tvBindAddress.setText(showAddress);
            } else {
                selectorBankInfo = null;
                binding.tvBindAddress.setText(" ");
            }
        } else {
            //收款地址 设置默认数据
            if (infoVo.user_bank_info != null && !infoVo.user_bank_info.isEmpty()) {
                String showAddress = infoVo.user_bank_info.get(0).usdt_type + "--" + infoVo.user_bank_info.get(0).account;
                //设置默认选中的提币地址
                selectorBankInfo = infoVo.user_bank_info.get(0);
                binding.tvBindAddress.setText(showAddress);
            } else {
                selectorBankInfo = null;
                binding.tvBindAddress.setText(" ");
                CfLog.e("****************** infoVo.user_bank_info is  null *********** ");
            }
        }
        //刷新提款类型
        if (changVo.name.contains("提款")) {
            binding.tvWithdrawalTypeShow.setText(changVo.name);
        } else {
            binding.tvWithdrawalTypeShow.setText(changVo.name + "提款");
        }
        //点击USDT收款地址
        binding.tvBindAddress.setOnClickListener(v -> {
            if (TextUtils.equals("TRC20_USDT", changVo.name)
                    || changVo.name.contains("TRC")
                    || changVo.name.contains("TRC20")
                    || changVo.name.contains("trc")
                    || changVo.name.contains("trc20")) {
                showCollectionDialog(trc20BankInfoList);
            } else {
                showCollectionDialog(infoVo.user_bank_info);
            }

        });
    }
    /**
     * 刷新 提款确认页面信息
     *
     * @param verifyVo
     */
    private void refreshVerifyUI(final WithdrawalVerifyVo verifyVo) {
        //刷新顶部进度条颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            binding.tvSetWithdrawalRequest.setTextColor(getContext().getColor(R.color.red));
            binding.tvConfirmWithdrawalRequest.setTextColor(getContext().getColor(R.color.red));
        }

        binding.llSetRequestView.setVisibility(View.GONE);
        //dialog_withdrawal_usdt_confirm
       /* binding.llVirtualConfirmView.setVisibility(View.VISIBLE);*/
        binding.llVirtualConfirmView.llConfirmView.setVisibility(View.VISIBLE);
        //用户名
        String userName = verifyVo.user_bank_info.user_name;
        String nickName = verifyVo.user_bank_info.nickname;
        String proUserName = mProfileVo.username;
        if (!TextUtils.isEmpty(userName)) {
            binding.llVirtualConfirmView.tvConfirmUserName.setText(StringUtils.splitWithdrawUserName(userName));
        } else if (!TextUtils.isEmpty(nickName)) {
            binding.llVirtualConfirmView.tvConfirmUserName.setText(StringUtils.splitWithdrawUserName(nickName));
        } else if (!TextUtils.isEmpty(proUserName)) {
            binding.llVirtualConfirmView.tvConfirmUserName.setText(StringUtils.splitWithdrawUserName(proUserName));
        }
        //可提款金额
        binding.llVirtualConfirmView.tvConfirmWithdrawalTypeShow.setText(verifyVo.quota);
        //提款金额方式
        binding.llVirtualConfirmView.tvConfirmAmountShow.setText(verifyVo.user_bank_info.usdt_type);
        //提款类型
        binding.llVirtualConfirmView.tvWithdrawalAmountTypeShow.setText(verifyVo.user_bank_info.usdt_type);
        //虚拟币类型
        binding.llVirtualConfirmView.tvWithdrawalVirtualTypeShow.setText(verifyVo.user_bank_info.usdt_type);
        //实际提款金额
        binding.llVirtualConfirmView.tvWithdrawalActualArrivalShow.setText(verifyVo.money_real);
        //汇率
        binding.llVirtualConfirmView.tvWithdrawalExchangeRateShow.setText(infoVo.rate);
        //提币地址
        binding.llVirtualConfirmView.tvWithdrawalAddressShow.setText(verifyVo.user_bank_info.account);
        //提款确定页面下一步
        binding.llVirtualConfirmView.ivConfirmNext.setOnClickListener(v -> {
            requestSubmit(verifyVo);
        });
        //提款确定页面上一步
        binding.llVirtualConfirmView.ivConfirmPrevious.setOnClickListener(v -> {
            //刷新顶部进度条颜色
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                binding.tvSetWithdrawalRequest.setTextColor(getContext().getColor(R.color.red));
                binding.tvConfirmWithdrawalRequest.setTextColor(getContext().getColor(R.color.cl_over_tip));
            }
            binding.llSetRequestView.setVisibility(View.VISIBLE);
            binding.llVirtualConfirmView.llConfirmView.setVisibility(View.GONE);
        });

    }

    /**
     * 初始化选项卡
     *
     * @param listVo
     */
    private void refreshTopUI(ArrayList<WithdrawalListVo> listVo) {
        recyclerViewAdapter = new FruitHorUSDTRecyclerViewAdapter( listVo, this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        binding.rvShowChooseCard.setLayoutManager(layoutManager);
        binding.rvShowChooseCard.addItemDecoration(new FruitHorUSDTRecyclerViewAdapter.SpacesItemDecoration(10));
        binding.rvShowChooseCard.setAdapter(recyclerViewAdapter);
        binding.rvShowChooseCard.setItemAnimator(new DefaultItemAnimator());

    }
    /**
     * 刷新 提款完成页面
     *
     * @param submitVo
     */
    private void refreshSubmitUI(final WithdrawalSubmitVo submitVo, final String message) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            binding.tvSetWithdrawalRequest.setTextColor(getContext().getColor(R.color.red));
            binding.tvConfirmWithdrawalRequest.setTextColor(getContext().getColor(R.color.red));
            binding.tvOverWithdrawalRequest.setTextColor(getContext().getColor(R.color.red));
        }

        binding.llVirtualConfirmView.llConfirmView.setVisibility(GONE);
        binding.llOverApply.setVisibility(VISIBLE);

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
        } else if (message != null && TextUtils.isEmpty(message)) {
            if (TextUtils.equals("账户提款申请成功", message)) {
                binding.ivOverApply.setVisibility(VISIBLE);
                binding.ivOverApply.setBackgroundResource(R.mipmap.ic_over_apply);
                binding.tvOverMsg.setVisibility(VISIBLE);
                binding.tvOverMsg.setText(submitVo.message);

            } else if (TextUtils.equals("请刷新后重试", message)) {
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

        //继续提款
        binding.ivContinueConfirmNext.setOnClickListener(v -> {
            dismiss();
        });
        //返回
        binding.ivContinueConfirmClose.setOnClickListener(v -> {
            dismiss();
        });
    }
    /*业务异常 跳转登录*/
    private void popLoginView() {
        ARouter.getInstance().build(RouterActivityPath.Mine.PAGER_LOGIN_REGISTER).navigation();
    }



    private void initListener() {
        hideKeyBoard();
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
                //换算到账个数
                String temp = s.toString();
                if (temp != null && !TextUtils.isEmpty(temp)) {
                    Double f1 = Double.parseDouble(temp);
                    Double f2 = Double.parseDouble(infoVo.rate);
                    DecimalFormat df = new DecimalFormat("0.00");
                    //实际到账个数
                    binding.tvInfoActualNumberShow.setText(df.format(f1 / f2));
                    //提款金额
                    binding.tvInfoWithdrawalAmountShow.setText(temp);
                } else if (TextUtils.isEmpty(temp)) {
                    binding.tvInfoActualNumberShow.setText("0");
                    //提款金额
                    binding.tvInfoWithdrawalAmountShow.setText("0");
                } else {
                    binding.tvInfoActualNumberShow.setText("0");
                    //提款金额
                    binding.tvInfoWithdrawalAmountShow.setText("0");
                }
            }
        });
        //点击下一步
        binding.ivNext.setOnClickListener(v -> {
            if (binding.etInputMoney.getText().length() > 9) {
                ToastUtils.showLong(R.string.txt_input_amount_tip);
            } else if (TextUtils.isEmpty(binding.etInputMoney.getText().toString())) {
                ToastUtils.showLong(R.string.txt_input_amount_tip);
            } else if (Double.valueOf(binding.etInputMoney.getText().toString()) > Double.valueOf(infoVo.max_money)) {
                ToastUtils.showLong(R.string.txt_input_amount_tip);
            } else if (Double.valueOf(binding.etInputMoney.getText().toString()) < Double.valueOf(infoVo.min_money)) {
                ToastUtils.showLong(R.string.txt_input_amount_tip);
            } else if (TextUtils.isEmpty(binding.tvBindAddress.getText().toString())) {
                ToastUtils.showLong(R.string.txt_select_withdrawal_address);
            } else {
                hideKeyBoard();
                String money = binding.etInputMoney.getText().toString().trim();
                String realCount = binding.tvInfoActualNumberShow.getText().toString().trim();
                String usdtId = usdtid;
                requestVerify(binding.etInputMoney.getText().toString().trim(), selectorBankInfo);
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




    ItemTextBinding binding2;
    BasePopupView ppw = null; // 底部弹窗 (选择**菜单)

    /**
     * 显示USDT收款地址
     */
    private void showCollectionDialog(ArrayList<WithdrawalInfoVo.UserBankInfo> infoArrayList) {
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
                    //设置选中的提款地址
                    selectorBankInfo = vo;
                    ppw.dismiss();
                });
            }
        };
        adapter.clear();
        adapter.addAll(infoArrayList);
        String selectString = getContext().getString(R.string.txt_select_add);
        ppw = new XPopup.Builder(getContext()).asCustom(new ListDialog(getContext(), selectString, adapter, 40));
        ppw.show();
    }

    /**
     * 设置提款 请求 下一步
     */
    private void requestWithdrawUSDT(String money, String realCount, String usdtId, String checkCode, final USDTCashVo cashMoYuVo) {
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
    private void requestConfirmUSDT(String money, String realCount, String handingFee, String usdt_type, String usdtType, String checkCode, USDTSecurityVo usdtSecurityVo) {
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
        map.put("usdtType", usdtSecurityVo.drawal_type);
        map.put("usdt_type", usdtSecurityVo.usdt_type);
        map.put("usdtid", usdtid);//选中提款地址

        CfLog.i("requestConfirmUSDT = " + map);

        viewModel.postConfirmWithdrawUSDTMoYu(map);

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

    /* 由于权限原因弹窗*/
    private void showErrorDialog(final String message) {
        if (message == null) {
            return;
        }
        errorPopView = new XPopup.Builder(getContext())
                .asCustom(new MsgDialog(getContext(), getContext().getString(R.string.txt_kind_tips), message, false, new MsgDialog.ICallBack() {
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

    @Override
    public void callbackWithFruitHor(WithdrawalListVo selectVo) {
        CfLog.e("callbackWithUSDTFruitHor=" + selectVo.toString());
        final String title = selectVo.title;
        final String selectorType = selectVo.type;
        changVo = selectVo;
        //获取当前选中的提款详情
        viewModel.getWithdrawalInfo(selectVo.name);
    }
}
