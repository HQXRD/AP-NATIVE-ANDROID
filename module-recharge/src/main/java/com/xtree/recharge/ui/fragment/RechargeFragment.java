package com.xtree.recharge.ui.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.core.text.HtmlCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.xtree.base.global.Constant;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.router.RouterActivityPath;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.base.utils.CfLog;
import com.xtree.base.utils.DomainUtil;
import com.xtree.base.utils.UuidUtil;
import com.xtree.base.widget.BrowserDialog;
import com.xtree.base.widget.ListDialog;
import com.xtree.base.widget.LoadingDialog;
import com.xtree.base.widget.MsgDialog;
import com.xtree.recharge.BR;
import com.xtree.recharge.R;
import com.xtree.recharge.databinding.FragmentRechargeBinding;
import com.xtree.recharge.ui.viewmodel.RechargeViewModel;
import com.xtree.recharge.ui.viewmodel.factory.AppViewModelFactory;
import com.xtree.recharge.vo.PaymentVo;
import com.xtree.recharge.vo.ProcessingDataVo;
import com.xtree.recharge.vo.RechargePayVo;
import com.xtree.recharge.vo.RechargeVo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.xtree.mvvmhabit.base.BaseFragment;
import me.xtree.mvvmhabit.utils.SPUtils;
import me.xtree.mvvmhabit.utils.ToastUtils;

/**
 * 充值页
 */
@Route(path = RouterFragmentPath.Recharge.PAGER_RECHARGE)
public class RechargeFragment extends BaseFragment<FragmentRechargeBinding, RechargeViewModel> {

    RechargeAdapter rechargeAdapter;
    AmountAdapter mAmountAdapter;
    double loadMin;
    double loadMax;
    PaymentVo mPaymentVo;
    RechargeVo curRechargeVo;
    BasePopupView ppw = null; // 底部弹窗
    BasePopupView ppw2 = null; // 底部弹窗 (二层弹窗)
    String bankId = ""; // 银行卡ID
    String hiWalletUrl; // 一键进入 HiWallet钱包
    String tutorialUrl; // 充值教程
    List<RechargeVo> mRecommendList = new ArrayList<>(); // 推荐的充值渠道列表
    HashMap<String, RechargeVo> mapRechargeVo = new HashMap<>(); // 跳转第三方链接的充值渠道
    boolean isShowedProcessPendCount = false; // 是否显示过 "订单未到账" 的提示
    boolean isBinding = false; // 是否正在跳转到其它页面绑定手机/YHK (跳转后回来刷新用)
    boolean isShowBack = false; // 是否显示返回按钮

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_recharge;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public RechargeViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getActivity().getApplication());
        return new ViewModelProvider(this, factory).get(RechargeViewModel.class);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 未登录状态下,直接跳到登录页,并关闭当前页
        String token = SPUtils.getInstance().getString(SPKeyGlobal.USER_TOKEN);
        if (TextUtils.isEmpty(token)) {
            ARouter.getInstance().build(RouterActivityPath.Mine.PAGER_LOGIN_REGISTER).navigation();
            getActivity().finish();
            return;
        }

        viewModel.readCache(); // 先读取缓存数据
        viewModel.getPayments(); // 调用接口
        viewModel.get1kEntry(); // 一键进入
    }

    @Override
    public void initView() {
        isShowBack = getArguments().getBoolean("isShowBack");
        if (isShowBack) {
            binding.ivwBack.setVisibility(View.VISIBLE);
        } else {
            binding.ivwBack.setVisibility(View.GONE);
        }
        binding.ivwBack.setOnClickListener(v -> getActivity().finish());
        binding.llRoot.setOnClickListener(v -> hideKeyBoard());
        binding.llRoot2.setOnClickListener(v -> hideKeyBoard());
        rechargeAdapter = new RechargeAdapter(getContext(), vo -> {
            CfLog.d(vo.toInfo());
            curRechargeVo = vo;
            onClickPayment(vo);
        });

        binding.rcvPmt.setLayoutManager(new GridLayoutManager(getContext(), 3));
        binding.rcvPmt.setAdapter(rechargeAdapter);
        binding.rcvPmt.setNestedScrollingEnabled(false); // 禁止滑动

        mAmountAdapter = new AmountAdapter(getContext(), str -> binding.edtAmount.setText(str));
        binding.rcvAmount.setAdapter(mAmountAdapter);
        binding.rcvAmount.setLayoutManager(new GridLayoutManager(getContext(), 4));

        binding.ivwCs.setOnClickListener(v -> {
            // 客服
            String title = getString(R.string.txt_custom_center);
            String url = DomainUtil.getDomain2() + Constant.URL_CUSTOMER_SERVICE;
            new XPopup.Builder(getContext()).asCustom(new BrowserDialog(getContext(), title, url)).show();
        });
        binding.ivwRule.setOnClickListener(v -> {
            // 反馈
            startContainerFragment(RouterFragmentPath.Recharge.PAGER_RECHARGE_FEEDBACK);
            /*String title = getString(R.string.txt_feedback);
            String url = DomainUtil.getDomain2() + Constant.URL_DEPOSIT_FEEDBACK;
            BrowserActivity.start(getContext(), title, url, true);*/
        });
        binding.ivwMsg.setOnClickListener(v -> {
            // 消息
            startContainerFragment(RouterFragmentPath.Mine.PAGER_MSG);
        });

        binding.ivw1k.setOnClickListener(v -> {
            // 1键进入
            show1kEntryDialog();
        });
        binding.tvwTutorial.setOnClickListener(v -> {
            // 充值教程
            if (!TextUtils.isEmpty(tutorialUrl)) {
                String title = getString(R.string.txt_recharge_tutorial);
                new XPopup.Builder(getContext()).asCustom(new BrowserDialog(getContext(), title, tutorialUrl)).show();
            }
        });

        binding.tvwBindPhone.setOnClickListener(v -> {
            //
        });
        binding.tvwBindYhk.setOnClickListener(v -> {

        });
        binding.ivwClear.setOnClickListener(v -> {
            binding.edtName.setText("");
            binding.tvwTipName.setVisibility(View.VISIBLE);
        });
        binding.edtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                CfLog.i("onTextChanged name: " + s);
                if (TextUtils.isEmpty(s.toString().trim())) {
                    binding.tvwTipName.setVisibility(View.VISIBLE);
                } else {
                    binding.tvwTipName.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.edtAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                CfLog.d("amount: " + s);
                if (s.toString().indexOf(".") > 0) {
                    binding.tvwRealAmount.setText(s.subSequence(0, s.toString().indexOf(".")));
                } else {
                    binding.tvwRealAmount.setText(s);
                }

                if (curRechargeVo != null && !TextUtils.isEmpty(curRechargeVo.usdtrate)) {
                    setUsdtRate(curRechargeVo);
                }
                if (!TextUtils.isEmpty(s.toString().trim())) {
                    binding.tvwTipAmount.setVisibility(View.INVISIBLE);
                } else {
                    binding.tvwTipAmount.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.btnNext.setOnClickListener(v -> {
            // 下一步
            goNext();
        });

        setTipBottom(new RechargeVo()); // 设置底部的文字提示

    }

    @Override
    public void onResume() {
        super.onResume();
        if (isBinding) {
            isBinding = false;
            binding.tvwCurPmt.setText("");
            binding.ivwCurPmt.setImageDrawable(null);
            binding.llDown.setVisibility(View.GONE);
            if (curRechargeVo != null) {
                View child = binding.rcvPmt.findViewWithTag(curRechargeVo.bid);
                if (child != null) {
                    child.setSelected(false); // 已选中的取消掉,刷新等待时间有点长
                }
            }

            //curRechargeVo = null; // 如果为空,连续点击x.bid会空指针
            viewModel.getPayments(); // 绑定回来,刷新数据
        }
    }

    private void onClickPayment(RechargeVo vo) {
        CfLog.i(vo.toString());
        CfLog.i("****** " + vo.title);
        boolean isRecommend = vo.tips_recommended == 1;
        if ("1".equals(vo.low_rate_hint) && !isRecommend && !mRecommendList.isEmpty()) {
            // 提示成功率低
            CfLog.i("****** 提示成功率低");
            String msg = getString(R.string.txt_rc_channel_low_rate_hint, vo.title);
            showRecommendDialog(msg, vo);
        } else {
            onClickPayment2(vo);
        }
    }

    private void onClickPayment2(RechargeVo vo) {
        CfLog.i("****** " + vo.title);
        bankId = "";
        binding.tvwCurPmt.setText(vo.title);
        binding.tvwBankCard.setText("");
        Drawable dr = getContext().getDrawable(R.drawable.rc_ic_pmt_selector);
        dr.setLevel(Integer.parseInt(vo.bid));
        binding.ivwCurPmt.setImageDrawable(dr);
        binding.llDown.setVisibility(View.GONE); // 默认隐藏
        setTipBottom(vo); // 设置底部的文字提示

        if (vo.op_thiriframe_use && vo.phone_needbind && vo.view_bank_card && vo.userBankList.isEmpty()) {
            // 绑定手机或者YHK
            CfLog.i("****** 绑定手机或者YHK");
            toBindPhoneOrCard();
            return;
        }
        if (vo.op_thiriframe_use && vo.phone_needbind && (!vo.view_bank_card || (vo.view_bank_card && !vo.userBankList.isEmpty()))) {
            // 绑定手机
            CfLog.i("****** 绑定手机");
            toBindPhoneNumber();
            return;
        }
        //if (vo.op_thiriframe_use && vo.userBankList.isEmpty() && vo.view_bank_card && !vo.phone_needbind) {
        if (vo.view_bank_card && vo.userBankList.isEmpty()) {
            // 绑定YHK
            CfLog.i("****** 绑定YHK");
            toBindCard();
            return;
        }
        CfLog.i("****** not need bind...");

        if (vo.op_thiriframe_use && !vo.phone_needbind) {
            CfLog.d(vo.title + ", jump: " + vo.op_thiriframe_url);
            binding.llDown.setVisibility(View.GONE); // 下面的部分隐藏
            if (!TextUtils.isEmpty(vo.op_thiriframe_url)) {
                String url = DomainUtil.getDomain2() + vo.op_thiriframe_url;
                new XPopup.Builder(getContext()).asCustom(new BrowserDialog(getContext(), vo.title, url)).show();
            } else {
                // 如果没有链接,调详情接口获取
                //viewModel.getPayment(vo.bid);
                if (mapRechargeVo.containsKey(vo.bid)) {
                    RechargeVo t2 = mapRechargeVo.get(vo.bid);
                    String url = t2.op_thiriframe_url;
                    if (!url.startsWith("http")) {
                        url = DomainUtil.getDomain2() + t2.op_thiriframe_url;
                    }
                    CfLog.d(vo.title + ", jump: " + url);
                    new XPopup.Builder(getContext()).asCustom(new BrowserDialog(getContext(), vo.title, url)).show();
                } else {
                    CfLog.e(vo.title + ", op_thiriframe_url is null...");
                }
            }

            return;
        }

        CfLog.i("****** llDown is visible");
        binding.llDown.setVisibility(View.VISIBLE); // 下面的部分显示

        // 显示/隐藏银行卡 userBankList
        if (vo.view_bank_card) {
            binding.tvwChooseBankCard.setVisibility(View.VISIBLE);
            binding.tvwBankCard.setVisibility(View.VISIBLE);
            binding.tvwBankCard.setOnClickListener(v -> showBankCardDialog(vo));
        } else {
            binding.tvwChooseBankCard.setVisibility(View.GONE);
            binding.tvwBankCard.setVisibility(View.GONE);
        }

        // 设置存款人姓名
        if (vo.realchannel_status && vo.phone_fillin_name) {
            CfLog.i("设置存款人姓名 = " + vo.accountname);
            binding.edtName.setText(vo.accountname);
            binding.llName.setVisibility(View.VISIBLE);
            binding.tvwTipName.setVisibility(View.INVISIBLE);
        } else {
            binding.edtName.setText("");
            binding.llName.setVisibility(View.GONE);
            binding.tvwTipName.setVisibility(View.VISIBLE);
        }

        // 有一组金额按钮需要显示出来 (固额和非固额)
        if (vo.fixedamount_channelshow && vo.fixedamount_info.length > 0) {
            binding.edtAmount.setEnabled(false);
            setAmountGrid(vo);
        } else {
            binding.edtAmount.setEnabled(true);
            List<String> list = getFastMoney(vo.loadmin, vo.loadmax);
            vo.fixedamount_info = list.toArray(new String[list.size()]);
            setAmountGrid(vo);
        }

        binding.edtAmount.setText("");

        loadMin = Double.parseDouble(vo.loadmin);
        loadMax = Double.parseDouble(vo.loadmax);
        setRate(vo); // 设置汇率提示信息

    }

    private void toBindPhoneOrCard() {
        String msg = getString(R.string.txt_rc_bind_personal_info);
        String left = getString(R.string.txt_rc_bind_phone_now);
        String right = getString(R.string.txt_rc_bind_bank_card_now);
        MsgDialog dialog = new MsgDialog(getContext(), null, msg, left, right, new MsgDialog.ICallBack() {
            @Override
            public void onClickLeft() {
                toBindPhoneNumber();
                ppw.dismiss();
            }

            @Override
            public void onClickRight() {
                toBindCard();
                ppw.dismiss();
            }
        });
        ppw = new XPopup.Builder(getContext())
                .dismissOnTouchOutside(false)
                .dismissOnBackPressed(false)
                .asCustom(dialog);
        ppw.show();
    }

    private void toBindPhoneNumber() {

        String msg = getString(R.string.txt_rc_bind_phone_email_pls);
        String left = getString(R.string.txt_rc_bind_phone);
        String right = getString(R.string.txt_rc_bind_email);
        MsgDialog dialog = new MsgDialog(getContext(), null, msg, left, right, new MsgDialog.ICallBack() {
            @Override
            public void onClickLeft() {
                toBindPhoneOrEmail(Constant.BIND_PHONE);
                ppw2.dismiss();
            }

            @Override
            public void onClickRight() {
                toBindPhoneOrEmail(Constant.BIND_EMAIL);
                ppw2.dismiss();
            }
        });
        ppw2 = new XPopup.Builder(getContext())
                .dismissOnTouchOutside(false)
                .dismissOnBackPressed(false)
                .asCustom(dialog);
        ppw2.show();
    }

    private void toBindCard() {

        String msg = getString(R.string.txt_rc_bind_bank_card_pls);
        MsgDialog dialog = new MsgDialog(getContext(), null, msg, true, new MsgDialog.ICallBack() {
            @Override
            public void onClickLeft() {
            }

            @Override
            public void onClickRight() {
                isBinding = true;
                Bundle bundle = new Bundle();
                bundle.putString("type", "bindcard");
                startContainerFragment(RouterFragmentPath.Mine.PAGER_SECURITY_VERIFY_CHOOSE, bundle);
                ppw2.dismiss();
            }
        });
        ppw2 = new XPopup.Builder(getContext())
                .dismissOnTouchOutside(false)
                .dismissOnBackPressed(false)
                .asCustom(dialog);
        ppw2.show();

    }

    private void toBindPhoneOrEmail(String type) {
        isBinding = true;
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        startContainerFragment(RouterFragmentPath.Mine.PAGER_SECURITY_VERIFY, bundle);
    }

    private void goNext() {
        CfLog.i("******");
        if (curRechargeVo == null) {
            ToastUtils.showLong(R.string.pls_choose_recharge_type);
            return;
        }

        if (curRechargeVo.view_bank_card) {
            if (TextUtils.isEmpty(bankId)) {
                ToastUtils.showLong(getString(R.string.txt_pls_select_payment_card));
                return;
            }
        }

        String realName = binding.edtName.getText().toString().trim();
        if (curRechargeVo.realchannel_status && curRechargeVo.phone_fillin_name) {
            if (TextUtils.isEmpty(realName)) {
                ToastUtils.showLong(getString(R.string.txt_pls_enter_ur_real_name));
                return;
            }
        }

        String txt = binding.edtAmount.getText().toString();
        double amount = Double.parseDouble(0 + txt);
        if (amount < loadMin || amount > loadMax) {
            txt = String.format(getString(R.string.txt_recharge_range), curRechargeVo.loadmin, curRechargeVo.loadmax);
            ToastUtils.showLong(txt);
            return;
        }

        LoadingDialog.show(getContext()); // Loading
        Map<String, String> map = new HashMap<>();
        map.put("alipayName", ""); //
        map.put("amount", txt); //
        // nonce: 如果第一次请求失败，第二次再请求 不能改变
        map.put("nonce", UuidUtil.getID16());
        map.put("rechRealname", realName); //

        map.put("bankid", bankId);
        //map.put("perOrder", "false");
        //map.put("orderKey", "");

        viewModel.rechargePay(curRechargeVo.bid, map);
    }

    private void show1kEntryDialog() {
        String title = getString(R.string.txt_kind_tips);
        String msg = getString(R.string.txt_is_to_open_hiwallet_wallet);
        ppw = new XPopup.Builder(getContext())
                .dismissOnTouchOutside(false)
                .dismissOnBackPressed(false)
                .asCustom(new MsgDialog(getContext(), title, msg, new MsgDialog.ICallBack() {
                    @Override
                    public void onClickLeft() {
                        ppw.dismiss();
                    }

                    @Override
                    public void onClickRight() {
                        new XPopup.Builder(getContext()).asCustom(new BrowserDialog(getContext(), "", hiWalletUrl)).show();
                        ppw.dismiss();
                    }
                }));
        ppw.show();
    }

    private void setAmountGrid(RechargeVo vo) {
        mAmountAdapter.clear();
        mAmountAdapter.addAll(Arrays.asList(vo.fixedamount_info));
    }

    private void showBankCardDialog(RechargeVo vo) {
        BankAdapter adapter = new BankAdapter(getContext(), bankCardVo -> {
            CfLog.i(bankCardVo.toString());
            bankId = bankCardVo.id;
            binding.tvwBankCard.setText(bankCardVo.name);
            if (ppw != null) {
                ppw.dismiss();
            }
        });
        adapter.addAll(vo.userBankList);
        String title = getString(R.string.txt_pls_select_payment_card);
        ppw = new XPopup.Builder(getContext()).asCustom(new ListDialog(getContext(), title, adapter));
        ppw.show();
    }

    @Override
    public void initData() {
        String token = SPUtils.getInstance().getString(SPKeyGlobal.USER_TOKEN);
        if (TextUtils.isEmpty(token)) {
            ARouter.getInstance().build(RouterActivityPath.Mine.PAGER_LOGIN_REGISTER).navigation();
            return;
        }

        String json = SPUtils.getInstance().getString(SPKeyGlobal.RC_PAYMENT_THIRIFRAME, "{}");
        mapRechargeVo = new Gson().fromJson(json, new TypeToken<HashMap<String, RechargeVo>>() {
        }.getType());

    }

    private void setRate(RechargeVo vo) {
        binding.llRate.setVisibility(View.VISIBLE);
        binding.tvwPrePay.setText("");

        if (!TextUtils.isEmpty(vo.usdtrate)) {
            setUsdtRate(vo);
        } else if (vo.paycode.equals("hiwallet")) {
            binding.tvwFxRate.setText(R.string.txt_rate_cnyt_cny);
        } else if (vo.paycode.equals("hqppaytopay")) {
            binding.tvwFxRate.setText(R.string.txt_rate_tog_cny);
        } else if (vo.paycode.equals("ebpay")) {
            binding.tvwFxRate.setText(R.string.txt_rate_eb_cny);
        } else {
            binding.tvwFxRate.setText("");
            binding.llRate.setVisibility(View.GONE);
        }

    }

    /**
     * 重新设置选中的充值渠道 (加载缓存后,选中某个渠道,接口返回的数据回来,重设)
     */
    private void resetCheckedChannel(PaymentVo vo) {
        if (curRechargeVo != null) {
            for (RechargeVo t : vo.chongzhiList) {
                if (curRechargeVo.bid.equals(t.bid)) {
                    curRechargeVo = t;
                }
            }
        }
    }

    private void setTipBottom(RechargeVo vo) {

        binding.tvwTipSameAmount.setVisibility(View.VISIBLE); // 默认显示
        binding.tvwTipChannel.setVisibility(View.GONE); // 默认隐藏

        String fontStart = "<font color=#EE5A5A>";
        String fontEnd = "</font>";
        String fontLine = "<BR>";
        String html = getString(R.string.txt_kind_tips) + "："; // 温馨提示：
        String tmp = "";

        String payCodes = "icbc,ccb,abc,cmb";

        List<String> payCodeList = Arrays.asList(payCodes.split(","));
        if (payCodeList.contains(vo.paycode)) {
            binding.tvwTipChannel.setVisibility(View.VISIBLE);
        }

        if (TextUtils.isEmpty(vo.paycode)) {
            // 进入充值页时,底部 默认的提示文字 2024-02-06
            binding.tvwTipSameAmount.setVisibility(View.INVISIBLE);
            tmp = getString(R.string.txt_bank_card);
            html += fontLine + getString(R.string.txt_rc_tip_yhk_1, tmp);
            html += fontLine + getString(R.string.txt_rc_tip_yhk_2a, "");
        } else if (vo.isusdt) {
            binding.tvwTipSameAmount.setVisibility(View.GONE);
            // ['jxpayusdt','cryptohqppay1','jxpaytokenerc','jxpaytokenerc3','cryptohqppay2'].includes(xx.paycode)? 'ERC20_USDT':'TRC20_USDT'
            tmp = fontStart + vo.udtType + "_USDT" + fontEnd; // 要修改
            html += fontLine + getString(R.string.txt_rc_tip_usdt_1);
            html += fontLine + getString(R.string.txt_rc_tip_usdt_2, tmp);

        } else if (vo.paycode.equals("ebpay")) {
            html += fontLine + getString(R.string.txt_rc_tip_ebpay_1);
            html += fontLine + getString(R.string.txt_rc_tip_ebpay_2);

        } else if (!vo.title.contains(getString(R.string.txt_alipay)) && !vo.title.contains(getString(R.string.txt_wechat))) {
            tmp = vo.paycode.equals("ecnyhqppay") ? getString(R.string.txt_ecny) : getString(R.string.txt_bank_card);
            //tmp = fontStart + tmp + fontEnd;
            html += fontLine + getString(R.string.txt_rc_tip_yhk_1, tmp);

            if (vo.randturnauto == 1) {
                html += fontLine + getString(R.string.txt_rc_tip_yhk_2);
            }
            if (vo.paycode.equals("ecnyhqppay")) {
                tmp = vo.randturnauto == 1 ? "3、" : "2、";
                html += fontLine + tmp + getString(R.string.txt_rc_tip_yhk_3);
            }
            if (vo.randturnauto != 1 && !vo.paycode.equals("ecnyhqppay")) {
                // 2、本通道只接受线上网银转账及手机银行转账。
                tmp = fontStart + vo.title + fontEnd;
                html += fontLine + getString(R.string.txt_rc_tip_yhk_2a, tmp);
            }

        } else {
            // title 包含'支付宝'或'微信'
            html += fontLine + getString(R.string.txt_rc_tip_alipay_wechat);
        }

        binding.tvwTipBottom.setVisibility(View.VISIBLE);
        binding.tvwTipBottom.setText(HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_LEGACY));

    }

    private void setUsdtRate(RechargeVo vo) {
        binding.tvwFxRate.setText(getString(R.string.txt_rate_usdt) + vo.usdtrate);
        int realMoney = Integer.parseInt(0 + binding.tvwRealAmount.getText().toString());
        float realUsdt = realMoney / Float.parseFloat(vo.usdtrate);
        //String usdt = new DecimalFormat("#.##").format(realUsdt);
        String usdt = String.format(getString(R.string.format_change_range), realUsdt);
        binding.tvwPrePay.setText(usdt);
    }

    @Override
    public void initViewObservable() {

        viewModel.liveData1kEntry.observe(this, url -> {
            // 一键进入 HiWallet钱包
            hiWalletUrl = url;
        });

        viewModel.liveDataPayment.observe(getViewLifecycleOwner(), vo -> {
            resetCheckedChannel(vo); // 重新设置选中充值渠道 curRechargeVo
            mPaymentVo = vo;
            tutorialUrl = vo.bankdirect_url; // 充值教程

            setRecommendList(); // 推荐的充值列表
            setMainList(vo.chongzhiList); // 显示充值列表九宫格
            showProcessDialog(vo.processingData); // 检查弹窗 充值次数
            setTipBottom(new RechargeVo()); // 恢复底部的默认提示
        });
        viewModel.liveDataRechargeList.observe(getViewLifecycleOwner(), list -> {
            setRecommendList(); // 推荐的充值列表
            setMainList(list); // 显示充值列表九宫格
        });
        viewModel.liveDataTutorial.observe(getViewLifecycleOwner(), url -> tutorialUrl = url);

        viewModel.liveDataRecharge.observe(getViewLifecycleOwner(), vo -> {
            CfLog.d(vo.toString());
            mapRechargeVo.put(vo.bid, vo);
            SPUtils.getInstance().put(SPKeyGlobal.RC_PAYMENT_THIRIFRAME, new Gson().toJson(mapRechargeVo));
        });

        viewModel.liveDataRechargePay.observe(getViewLifecycleOwner(), vo -> {
            CfLog.i(vo.payname + ", bankcode: " + vo.bankcode + ", money: " + vo.money);
            goPay(vo);
        });

    }

    @Override
    public void onDestroyView() {
        viewModel.liveDataPayment.observe(getViewLifecycleOwner(), vo -> {
            // 修复频繁点击充值页和其它页时 有时会出现两个弹窗
        });
        super.onDestroyView();
    }

    private void goPay(RechargePayVo vo) {
        String payCodes = "alipay,alipay2,alipaysm,weixin,juxinpay,juxinpay1,juxinpay2,juxinwx1"
                + ",juxinwex2,juxinzfb1,juxinzfb2,hqppay,ebpay,cryptohqppay2,cryptotrchqppay2"
                + ",hqppaytopay,hiwallet,hqppay6";
        List<String> payCodeList = Arrays.asList(payCodes.split(",")); // payCodeArr
        boolean isInArr = payCodeList.contains(vo.bankcode);
        CfLog.i(" bankcode: " + vo.bankcode + ", isInArr: " + isInArr + " isbank: " + vo.isbank + ", isusdt: " + vo.isusdt);
        if (vo.bankcode.equals("ucim") && vo.isRedirectMode && vo.isredirect) {
            // 卡转卡银行充值 (直接跳转) IM银行卡转账1, bankcode: ucim, money: 300.00
            goPayWeb2(vo); // IM银行卡转账1
        } else if (isInArr && vo.isredirect) {
            // 弹出充值提示窗口, 含按钮 “弹出支付窗口”,点击按钮再次跳转
            goPayWeb(vo);
        } else if (vo.isbank) {
            goPayBank(vo); // UC聚合支付,不能跳转
        } else if (vo.isusdt) {
            goPayUsdt(vo); // TRC20快付,不能跳转
        } else {
            goPayWeb(vo);
        }
    }

    private void goPayWeb(RechargePayVo vo) {
        new XPopup.Builder(getContext())
                .dismissOnTouchOutside(false)
                .dismissOnBackPressed(false)
                .asCustom(new RechargeOrderWebDialog(getContext(), vo, () -> {
                    if (isShowBack) {
                        getActivity().finish();
                    }
                }))
                .show();
    }

    private void goPayWeb2(RechargePayVo vo) {
        String url = vo.redirecturl;
        if (!TextUtils.isEmpty(url)) {
            if (!url.startsWith("http")) {
                if (vo.domainList.size() > 0) {
                    url = vo.domainList.get(0) + url;
                } else {
                    url = DomainUtil.getDomain2() + url;
                }
            }
            CfLog.i(vo.payname + ", jump: " + url);
            // 弹窗
            //getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            new XPopup.Builder(getContext())
                    .dismissOnTouchOutside(false)
                    .dismissOnBackPressed(false)
                    .asCustom(new BrowserDialog(getContext(), vo.payname, url))
                    .show();
        }
    }

    private void goPayBank(RechargePayVo vo) {
        new XPopup.Builder(getContext())
                .dismissOnTouchOutside(false)
                .dismissOnBackPressed(false)
                .asCustom(new RechargeOrderBankDialog(getContext(), vo))
                .show();
    }

    private void goPayUsdt(RechargePayVo vo) {
        new XPopup.Builder(getContext())
                .dismissOnTouchOutside(false)
                .dismissOnBackPressed(false)
                .asCustom(new RechargeOrderUsdtDialog(getContext(), vo))
                .show();
    }

    private void setMainList(List<RechargeVo> list) {
        rechargeAdapter.clear();
        rechargeAdapter.addAll(list);
        queryThirdDetail(list);
    }

    /**
     * 提前查询需要跳转第三方的详情(链接)
     */
    private void queryThirdDetail(List<RechargeVo> list) {
        for (RechargeVo vo : list) {
            if (!mapRechargeVo.containsKey(vo.bid) && vo.op_thiriframe_use && !vo.phone_needbind) {
                CfLog.d(vo.title + ", jump: " + vo.op_thiriframe_url);
                // 调详情接口获取 跳转链接
                viewModel.getPayment(vo.bid);
            }
        }
    }

    /**
     * 推荐的充值列表
     */
    private void setRecommendList() {
        if (mPaymentVo != null && !mPaymentVo.chongzhiList.isEmpty()) {
            mRecommendList.clear();
            for (RechargeVo t : mPaymentVo.chongzhiList) {
                if (t.tips_recommended == 1) {
                    CfLog.i(t.title);
                    mRecommendList.add(t);
                }
            }
        }
    }

    private void showProcessDialog(ProcessingDataVo vo) {
        CfLog.i(vo.toString());
        if (!isShowedProcessPendCount && (vo.depProcessCnt1 || vo.depProcessCnt3)) {
            isShowedProcessPendCount = true;
            // 有订单还未到账，为了能您的充值快速到账，请您进行反馈！
            CfLog.i("****** 有订单还未到账，为了能您的充值快速到账，请您进行反馈！");
            String msg = getString(R.string.txt_rc_order_not_received_contact_pls);

            MsgDialog dialog = new MsgDialog(getContext(), "", msg, true, new MsgDialog.ICallBack() {
                @Override
                public void onClickLeft() {
                }

                @Override
                public void onClickRight() {
                    ppw2.dismiss();
                }
            });
            ppw2 = new XPopup.Builder(getContext())
                    .dismissOnTouchOutside(false)
                    .dismissOnBackPressed(true)
                    .asCustom(dialog);
            ppw2.show();

        } else if (vo.userProcessCount > 0) {
            // 您已经连续充值 次, 为了保证快速到账，请使用以下渠道进行充值或联系客服进行处理！
            CfLog.i("****** 您已经连续充值 次");
            String msg = getString(R.string.txt_rc_count_low_rate_hint, vo.userProcessCount);
            showRecommendDialog(msg, null);
        }
    }

    private void showRecommendDialog(String msg, RechargeVo vo) {
        CfLog.i("****** 弹窗: " + msg);
        if (!mRecommendList.isEmpty()) {
            // 弹窗
            BasePopupView dialog = new RechargeRecommendDialog(getContext(), msg, tutorialUrl, mRecommendList, vo, curVo -> {
                CfLog.i("****** ");
                //onClickPayment(curVo);

                if (vo != null && curVo != null && vo.bid.equals(curVo.bid)) {
                    onClickPayment2(curVo);
                } else if (curVo != null) {
                    for (int i = 0; i < mPaymentVo.chongzhiList.size(); i++) {
                        if (mPaymentVo.chongzhiList.get(i).bid.equals(curVo.bid)) {
                            CfLog.i("i: " + i);
                            binding.rcvPmt.scrollToPosition(i);
                            View child = binding.rcvPmt.findViewWithTag(curVo.bid);
                            if (child != null) {
                                child.performClick();
                            }
                        }
                    }
                }

            });
            new XPopup.Builder(getContext())
                    .dismissOnTouchOutside(false)
                    .dismissOnBackPressed(false)
                    .asCustom(dialog)
                    .show();
        }
    }

    private List<String> getFastMoney(String loadMin, String loadMax) {
        if (loadMin.startsWith("0.")) {
            return getFastMoney(1, Integer.parseInt(loadMax));
        }
        return getFastMoney(Integer.parseInt(loadMin), Integer.parseInt(loadMax));
    }

    private List<String> getFastMoney(int loadMin, int loadMax) {
        List<String> list = new ArrayList<>();
        list.add(getFastMoney(0.1d, loadMin, loadMax) + "");
        list.add(getFastMoney(0.3d, loadMin, loadMax) + "");
        list.add(getFastMoney(0.5d, loadMin, loadMax) + "");
        list.add(loadMax + "");
        return list;
    }

    private int getFastMoney(double rate, int loadMin, int loadMax) {
        int min = Math.max(loadMin, 10);
        int max = Math.max(loadMax, 10);
        int dest = max - min;
        double value = (max - min) * rate + min;
        double money = 0;
        int amount = 0;
        if (dest <= 3000) {
            amount = (int) (Math.round(value / 100) * 100);
        } else {
            String str = String.valueOf(value);
            int length = str.indexOf(".");
            money = Math.round(value / Math.pow(10, length - 2)) * Math.pow(10, length - 2);
            amount = (int) (Math.round(money / 100) * 100);
        }
        //CfLog.d("amount: " + amount);
        return amount;
    }

}
