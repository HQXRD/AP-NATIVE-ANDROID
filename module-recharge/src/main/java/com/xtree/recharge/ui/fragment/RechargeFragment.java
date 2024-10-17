package com.xtree.recharge.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.text.HtmlCompat;
import androidx.core.widget.TextViewCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.binioter.guideview.Guide;
import com.binioter.guideview.GuideBuilder;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.xtree.base.global.Constant;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.router.RouterActivityPath;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.base.utils.AppUtil;
import com.xtree.base.utils.CfLog;
import com.xtree.base.utils.ClickUtil;
import com.xtree.base.utils.DomainUtil;
import com.xtree.base.utils.NumberUtils;
import com.xtree.base.utils.TagUtils;
import com.xtree.base.utils.TimeUtils;
import com.xtree.base.utils.UuidUtil;
import com.xtree.base.vo.ProfileVo;
import com.xtree.base.vo.RechargeOrderVo;
import com.xtree.base.widget.BrowserDialog;
import com.xtree.base.widget.ListDialog;
import com.xtree.base.widget.LoadingDialog;
import com.xtree.base.widget.MsgDialog;
import com.xtree.recharge.BR;
import com.xtree.recharge.R;
import com.xtree.recharge.data.source.request.ExCreateOrderRequest;
import com.xtree.recharge.databinding.FragmentRechargeBinding;
import com.xtree.recharge.ui.fragment.guide.GuideDialog;
import com.xtree.recharge.ui.fragment.guide.RechargeBankComponent;
import com.xtree.recharge.ui.fragment.guide.RechargeMoneyComponent;
import com.xtree.recharge.ui.fragment.guide.RechargeNameComponent;
import com.xtree.recharge.ui.fragment.guide.RechargeNextComponent;
import com.xtree.recharge.ui.viewmodel.RechargeViewModel;
import com.xtree.recharge.ui.viewmodel.factory.AppViewModelFactory;
import com.xtree.recharge.ui.widget.TipBindCardDialog;
import com.xtree.recharge.vo.BankCardVo;
import com.xtree.recharge.vo.BannersVo;
import com.xtree.recharge.vo.HiWalletVo;
import com.xtree.recharge.vo.PaymentDataVo;
import com.xtree.recharge.vo.PaymentTypeVo;
import com.xtree.recharge.vo.ProcessingDataVo;
import com.xtree.recharge.vo.RechargePayVo;
import com.xtree.recharge.vo.RechargeVo;
import com.youth.banner.adapter.BannerImageAdapter;
import com.youth.banner.holder.BannerImageHolder;
import com.youth.banner.indicator.CircleIndicator;
import com.youth.banner.listener.OnBannerListener;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.xtree.mvvmhabit.base.BaseFragment;
import me.xtree.mvvmhabit.bus.RxBus;
import me.xtree.mvvmhabit.utils.SPUtils;
import me.xtree.mvvmhabit.utils.ToastUtils;

/**
 * 充值页
 */
@Route(path = RouterFragmentPath.Recharge.PAGER_RECHARGE)
public class RechargeFragment extends BaseFragment<FragmentRechargeBinding, RechargeViewModel> {
    private static final int MSG_CLICK_CHANNEL = 1001;
    private static final int MSG_ADD_PAYMENT = 1002;
    private static final long REFRESH_DELAY = 30 * 60 * 1000L; // 刷新间隔等待时间(如果长时间没刷新)
    private static final String ONE_PAY_FIX = "onepayfix"; // 极速充值包含的关键字
    private static final String KEY_MANUAL = "manual"; // 人工充值

    private Method method;
    private Object object;
    //RechargeAdapter rechargeAdapter;
    RechargeTypeAdapter mTypeAdapter;
    RechargeChannelAdapter mChannelAdapter;
    AmountAdapter mAmountAdapter;
    double loadMin;
    double loadMax;
    //PaymentVo mPaymentVo;
    PaymentDataVo mPaymentDataVo;
    RechargeVo curRechargeVo;
    PaymentTypeVo curPaymentTypeVo;
    BasePopupView ppw = null; // 底部弹窗
    BasePopupView ppw2 = null; // 底部弹窗 (二层弹窗)
    BasePopupView ppw3 = null; // 极速充值绑定银行卡弹窗
    BasePopupView bindCardPPW = null;//绑定银行卡PopView
    public static BasePopupView bindPhonePPW = null;
    String bankId = ""; // 用户绑定的银行卡ID
    String bankCode = ""; // 付款银行编号 (极速充值用) ABC
    String hiWalletUrl; // 一键进入 HiWallet钱包
    HiWalletVo mHiWalletVo; // 一键进入 HiWallet钱包
    String tutorialUrl; // 充值教程
    List<RechargeVo> mRecommendList = new ArrayList<>(); // 推荐的充值渠道列表
    HashMap<String, RechargeVo> mapRechargeVo = new HashMap<>(); // 缓存的,跳转第三方链接的充值渠道
    boolean isShowedProcessPendCount = false; // 是否显示过 "订单未到账" 的提示
    boolean isNeedRefresh = false; // 是否正在跳转到其它页面 (绑定手机/YHK) (跳转后回来刷新用)
    boolean isNeedReset = false; // 是否正在跳转到其它页面 (极速充值订单) (跳转后回来刷新用)
    boolean isHidden = false; // 当前fragment是否隐藏. 解决从充值页切到首页/我的,再打开VIP,再返回,弹出弹窗(您已经充值x次)
    boolean isShowBack = false; // 是否显示返回按钮
    boolean isShowOrderDetail = false; // 是否显示充值订单详情,需要传订单号过来 (待处理的订单详情) 2024-03-27
    ProfileVo mProfileVo = null; // 个人信息
    // HQAP2-2963 这几个充值渠道 内部浏览器要加个外跳的按钮 2024-03-23
    String[] arrayBrowser = new String[]{"onepayfix3", "onepayfix4", "onepayfix5", "onepayfix6"};
    List<String> payCodeList = new ArrayList<>(); // 含弹出支付窗口的充值渠道类型列表(从缓存加载用)
    long lastRefresh = System.currentTimeMillis(); // 上次刷新时间

    private BasePopupView showGuideView;//显示充值引导

    Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            //super.handleMessage(msg);
            switch (msg.what) {
                case MSG_CLICK_CHANNEL:
                    if (binding.rcvPayChannel.getAdapter().getItemCount() > 0) {
                        RechargeVo vo = (RechargeVo) msg.obj;
                        View child = binding.rcvPayChannel.findViewWithTag(vo.bid);
                        if (child != null) {
                            child.performClick();
                        }
                        for (int i = 0; i < curPaymentTypeVo.payChannelList.size(); i++) {
                            if (curPaymentTypeVo.payChannelList.get(i).bid.equals(vo.bid)) {
                                binding.rcvPayChannel.scrollToPosition(i); // 自动滑动到选中的充值渠道
                                return;
                            }
                        }
                    }
                    break;
                case MSG_ADD_PAYMENT:
                    SPUtils.getInstance().put(SPKeyGlobal.RC_PAYMENT_THIRIFRAME, new Gson().toJson(mapRechargeVo));
                    break;
                default:
                    CfLog.i("****** default");
                    break;
            }
        }
    };

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_recharge;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    protected void initImmersionBar() {
    }

    @Override
    public RechargeViewModel initViewModel() {
        RechargeViewModel viewmodel = new ViewModelProvider(getActivity()).get(RechargeViewModel.class);
        AppViewModelFactory instance = AppViewModelFactory.getInstance(getActivity().getApplication());
        viewmodel.setModel(instance.getmRepository());
        return viewmodel;
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
        //viewModel.getPayments(); // 调用接口
        viewModel.getPaymentsTypeList(); // 调用接口
        viewModel.get1kEntry(); // 一键进入
        viewModel.getRechargeBanners(); // 获取广告轮播图
    }

    @Override
    public void initView() {
        isShowBack = getArguments().getBoolean("isShowBack");

        // 利用反射呼叫浮动弹窗
        if (isShowBack) {
            try {
                Class myClass = Class.forName("com.xtree.home.ui.custom.view.RechargeFloatingWindows");
                Constructor constructor = myClass.getConstructor(Context.class);
                object = constructor.newInstance(getContext());
                method = object.getClass().getMethod("show");
                method.invoke(object);
            } catch (Exception e) {
                CfLog.e(e.getMessage());
            }
        }

        if (isShowBack) {
            binding.ivwBack.setVisibility(View.VISIBLE);
            binding.vTop.setVisibility(View.GONE);
        } else {
            binding.ivwBack.setVisibility(View.GONE);
            binding.vTop.setVisibility(View.VISIBLE);
        }
        binding.ivwBack.setOnClickListener(v -> getActivity().finish());
        binding.llRoot.setOnClickListener(v -> hideKeyBoard());
        binding.llRoot2.setOnClickListener(v -> hideKeyBoard());
        //rechargeAdapter = new RechargeAdapter(getContext(), vo -> {
        //    CfLog.d(vo.toInfo());
        //    curRechargeVo = vo;
        //    onClickPayment(vo);
        //});
        mTypeAdapter = new RechargeTypeAdapter(getContext(), vo -> {
            CfLog.d(vo.toInfo());
            curRechargeVo = null;
            viewModel.curRechargeLiveData.setValue(curRechargeVo);
            onClickPaymentType(vo);
        });

        binding.rcvPmt.setLayoutManager(new GridLayoutManager(getContext(), 4));
        //binding.rcvPmt.setAdapter(rechargeAdapter);
        binding.rcvPmt.setAdapter(mTypeAdapter);
        binding.rcvPmt.setNestedScrollingEnabled(false); // 禁止滑动

        mChannelAdapter = new RechargeChannelAdapter(getContext(), vo -> {
            CfLog.i(vo.toInfo());
            curRechargeVo = vo;
            viewModel.curRechargeLiveData.setValue(curRechargeVo);
            onClickPayment(vo);
        });
        binding.rcvPayChannel.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.rcvPayChannel.setAdapter(mChannelAdapter);

        mAmountAdapter = new AmountAdapter(getContext(), str -> binding.edtAmount.setText(str));
        binding.rcvAmount.setAdapter(mAmountAdapter);
        binding.rcvAmount.setLayoutManager(new GridLayoutManager(getContext(), 4));

        binding.ivwCs.setOnClickListener(v -> AppUtil.goCustomerService(getContext()));
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
            //if (!TextUtils.isEmpty(tutorialUrl)) {
            //    String title = getString(R.string.txt_recharge_tutorial);
            //    new XPopup.Builder(getContext()).asCustom(new BrowserDialog(getContext(), title, tutorialUrl)).show();
            //}
            showWebDialog(getString(R.string.txt_recharge_tutorial), Constant.URL_RC_CNYT_TUTORIAL);
        });
        binding.tvwAntiFraud.setOnClickListener(v -> {
            // 防骗教程
            String title = getString(R.string.txt_rc_anti_fraud);
            String url = DomainUtil.getH5Domain2() + Constant.URL_ANTI_FRAUD;
            new XPopup.Builder(getContext()).asCustom(new BrowserDialog(getContext(), title, url)).show();
        });
        binding.tvwDownload.setOnClickListener(v -> {
            // 下载嗨钱包
            String url = Constant.URL_DOWNLOAD_HI_WALLET;
            AppUtil.goBrowser(getContext(), url);
        });

        binding.tvwBindPhone.setOnClickListener(v -> toBindPhonePage());
        binding.tvwBindYhk.setOnClickListener(v -> toBindPage(Constant.BIND_CARD));
        binding.tvwBankCard.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                CfLog.d("onTextChanged name: " + s);
                setNextButton();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
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
                    binding.tvwTipName.setVisibility(View.GONE);
                }
                setNextButton();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        // 文字大小可随长度而改变,解决长度太长显示不全的问题(HQAP2-3310)
        TextViewCompat.setAutoSizeTextTypeWithDefaults(binding.tvwAmountHint, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);
        TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(binding.tvwAmountHint, 12, 14, 1, TypedValue.COMPLEX_UNIT_SP);
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
                if (binding.tvwRealAmount.length() == 0) {
                    binding.tvwAmountHint.setVisibility(View.VISIBLE);
                } else {
                    binding.tvwAmountHint.setVisibility(View.INVISIBLE);
                }

                // 获取 实际充值金额 (测试环境 银行卡充值3)
                if (curRechargeVo != null && curRechargeVo.isrecharge_additional && s.length() >= 3) {
                    // 调用提交接口, 增加 perOrder=true
                    //getRealMoney();
                }

                if (curRechargeVo != null && !TextUtils.isEmpty(curRechargeVo.usdtrate)) {
                    setUsdtRate(curRechargeVo);
                }
                String amount = s.toString();

                // 预计支付
                setPrePay(curRechargeVo, amount);

                if (!TextUtils.isEmpty(amount) && (Double.parseDouble(amount) < loadMin || Double.parseDouble(amount) > loadMax)) {
                    binding.tvwTipAmount.setVisibility(View.VISIBLE);
                } else {
                    binding.tvwTipAmount.setVisibility(View.GONE);
                }

                if (!amount.equals(mAmountAdapter.getAmount())) {
                    mAmountAdapter.setAmount(amount);
                    mAmountAdapter.notifyDataSetChanged();
                }
                setNextButton();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.btnNext.setOnClickListener(v -> {
            // 下一步
            if (curRechargeVo.paycode.equals(KEY_MANUAL)) {
                goNextManual(); // 人工充值
            } else if (isOnePayFix(curRechargeVo)) {
                goNext2(); // 极速充值
            } else if (curRechargeVo.paycode.contains("hiwallet")) {
                goHiWallet(); // 嗨钱包
            } else {
                goNext(); // 普通充值
            }
        });

        setTipBottom(null); // 设置底部的文字提示

        binding.bnrTop.setIndicator(new CircleIndicator(getContext())); // 增加小圆点
        binding.bnrTop.setAdapter(new BannerImageAdapter<BannersVo>(new ArrayList<>()) {
            @Override
            public void onBindView(BannerImageHolder holder, BannersVo data, int position, int size) {
                //holder.imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                String url = data.picture.startsWith("http") ? data.picture : DomainUtil.getH5Domain2() + data.picture;
                Glide.with(getContext()).load(url).placeholder(R.mipmap.rc_bnr_ad).into(holder.imageView);
            }
        });
        binding.bnrTop.setOnBannerListener((OnBannerListener<BannersVo>) (data, position) -> {
            if (TextUtils.isEmpty(data.link)) {
                return;
            }

            String url = data.link.startsWith("http") ? data.link : DomainUtil.getH5Domain2() + data.link;
            CfLog.d(url);
            AppUtil.goBrowser(getContext(), url);
        });
    }

    /**
     * 使用hide和show后，可见不可见切换时，不再执行fragment生命周期方法，
     * 需要刷新时，使用onHiddenChanged代替
     */
    @Override
    public void onResume() {
        super.onResume();
        CfLog.i("****** isHidden: " + isHidden);
        if (!isHidden) {
            refresh(); // 刷新
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        // 在结束后浮动弹窗被删除
        if (isShowBack) {
            try {
                method = object.getClass().getMethod("removeView");
                method.invoke(object);
            } catch (Exception e) {
                CfLog.e(e.getMessage());
            }
        }
    }

    private void refresh() {
        CfLog.i("******");
        viewModel.readProfile();
        if (isNeedRefresh) {
            isNeedRefresh = false;
            resetView();

            //curRechargeVo = null; // 如果为空,连续点击x.bid会空指针
            //viewModel.getPayments(); // 绑定回来,刷新数据
            viewModel.getPaymentsTypeList(); // 绑定回来,刷新数据
            lastRefresh = System.currentTimeMillis();
            return;
        }
        if (isNeedReset) {
            resetView();
        }
        if (System.currentTimeMillis() - lastRefresh > REFRESH_DELAY) {
            CfLog.i("******");
            viewModel.getPaymentsTypeList(); // 长时间没有刷新,刷新一下数据
        }
    }

    /**
     * 设置为未选中, 相当于初始化
     */
    private void resetView() {
        binding.tvwCurPmt.setText("");
        binding.ivwCurPmt.setImageDrawable(null);
        binding.llBindInfo.setVisibility(View.GONE);
        binding.llDown.setVisibility(View.GONE);
        //if (curRechargeVo != null) {
        //    View child = binding.rcvPmt.findViewWithTag(curRechargeVo.bid);
        //    if (child != null) {
        //        child.setSelected(false); // 已选中的取消掉,刷新等待时间有点长
        //    }
        //}

        // 这里不点击选中是因为有些渠道会弹窗要求绑定手机/YHK 会出现死循环,(然后又要和其它端保持一致)
        //if (curPaymentTypeVo != null) {
        //    View childType = binding.rcvPmt.findViewWithTag(curPaymentTypeVo.id);
        //    if (childType != null) {
        //        childType.performClick();
        //    }
        //}
        // 要求从绑定页返回,不要选中充值方式 (和其它端保持一致)
        mTypeAdapter.reset(); // 重置,不选中
        mChannelAdapter.clear(); // 清空
        binding.llCurPmt.setVisibility(View.GONE); // 隐藏当前充值方式
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        CfLog.i("****** hidden: " + hidden);
        isHidden = hidden;
        if (hidden) {   // 不在最前端显示 相当于调用了onPause();

        } else {  // 第一次可见，不会执行到这里，只会执行onResume
            //网络数据刷新
            refresh();
        }
    }

    private void onClickPaymentType(PaymentTypeVo vo) {
        CfLog.i(vo.toInfo());
        CfLog.d("size: " + vo.payChannelList.size());
        curPaymentTypeVo = vo;
        binding.llCurPmt.setVisibility(View.VISIBLE);
        binding.tvwCurPmt.setText(vo.dispay_title);
        String url = DomainUtil.getH5Domain2() + vo.un_selected_image; // 未选中 彩色图片
        Glide.with(getContext()).load(url).placeholder(R.mipmap.ic_trans_76).into(binding.ivwCurPmt);
        binding.llBindInfo.setVisibility(View.GONE);
        binding.llDown.setVisibility(View.GONE); // 隐藏底部 用户输入的部分
        mChannelAdapter.clear();
        mChannelAdapter.addAll(vo.payChannelList);
        setTipBottom(vo); // 设置底部的文字提示

        // 如果只有一个渠道时，隐藏掉，并触发点击事件
        if (vo.payChannelList.size() == 1) {
            binding.rcvPayChannel.setVisibility(View.GONE);
            curRechargeVo = vo.payChannelList.get(0);
            viewModel.curRechargeLiveData.setValue(curRechargeVo);
            onClickPayment(vo.payChannelList.get(0));
        } else {
            binding.rcvPayChannel.setVisibility(View.VISIBLE);

            // 非跳转三方的,默认选中第一个
            RechargeVo vo2 = vo.payChannelList.get(0);
            if (!(vo2.op_thiriframe_use && !vo2.phone_needbind)) {
                Message msg2 = new Message();
                msg2.what = MSG_CLICK_CHANNEL;
                msg2.obj = vo2;
                mHandler.sendMessageDelayed(msg2, 350L);
            }
        }

    }

    private void onClickPayment(RechargeVo vo) {
        CfLog.i(vo.toString());
        CfLog.i("****** " + vo.title);

        // 替换为缓存的
        //if (mapRechargeVo.containsKey(vo.bid) && TextUtils.isEmpty(vo.op_thiriframe_url)) {
        //    vo = mapRechargeVo.get(vo.bid);
        //    curRechargeVo = vo;
        //    CfLog.i("****** update: " + vo.toString());
        //}
        //工商银行显示处理
        if (vo.op_disable_bank_status && vo.userBankList != null && vo.userBankList.isEmpty()) {
            ppw3 = new XPopup.Builder(getContext()).dismissOnTouchOutside(false)
                    .dismissOnBackPressed(false).asCustom(new TipBindCardDialog(getContext(), vo.op_thiriframe_msg, new TipBindCardDialog.ICallBack() {
                        @Override
                        public void onClickConfirm() {
                            toBindPage(Constant.BIND_CARD);
                        }
                    }));
            ppw3.show();
            return;
        }

        boolean isRecommend = vo.tips_recommended == 1;
        if ("1".equals(vo.low_rate_hint) && !isRecommend && !mRecommendList.isEmpty() && isTipTodayLow()) {
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
        bankCode = "";
        //binding.tvwCurPmt.setText(vo.title); // 用大类的名字
        //Drawable dr = getContext().getDrawable(R.drawable.rc_ic_pmt_selector);
        //dr.setLevel(Integer.parseInt(vo.bid));
        //binding.ivwCurPmt.setImageDrawable(dr);
        binding.tvwBankCard.setText("");
        binding.llBindInfo.setVisibility(View.GONE); // 默认隐藏
        binding.tvwBindPhone.setVisibility(View.GONE);
        binding.tvwBindYhk.setVisibility(View.GONE);
        binding.llDown.setVisibility(View.GONE); // 默认隐藏
        //setTipBottom(vo); // 设置底部的文字提示
        setStepBottom(); // 底部的操作步骤 (CNYT和USDT要用)

        //if (vo.op_thiriframe_use && vo.phone_needbind && vo.view_bank_card && vo.userBankList.isEmpty()) {
        //    // 绑定手机或者YHK
        //    CfLog.i("****** 绑定手机或者YHK");
        //    toBindPhoneOrCard();
        //    return;
        //}
        //if (vo.op_thiriframe_use && vo.phone_needbind && (!vo.view_bank_card || (vo.view_bank_card && !vo.userBankList.isEmpty()))) {
        // 加个 isNeedPhone, 解决偶现用户绑手机后,再次点击还会提示绑手机(充值列表接口响应慢,最新的列表还没有回来) 2024-07-11
        boolean isNeedPhone = mProfileVo != null && !mProfileVo.is_binding_phone; // && isNeedPhone

        if (vo.phone_needbind && isNeedPhone) {
            binding.llBindInfo.setVisibility(View.VISIBLE);
            binding.tvwBindPhone.setVisibility(View.VISIBLE);
        }
        if (vo.view_bank_card && vo.userBankList.isEmpty()) {
            binding.llBindInfo.setVisibility(View.VISIBLE);
            binding.tvwBindYhk.setVisibility(View.VISIBLE);
        }

        //嗨钱包需要绑定手机号和银行卡
        if (curRechargeVo.paycode.contains("hiwallet") && vo.userBankList.isEmpty()) {
            binding.llBindInfo.setVisibility(View.VISIBLE);
            binding.tvwBindYhk.setVisibility(View.VISIBLE);
        }

        if (vo.op_thiriframe_use && vo.phone_needbind && isNeedPhone) {
            // 绑定手机
            CfLog.i("****** 绑定手机");
            toBindPhoneNumber();
            return;
        }
       /* if (!vo.op_thiriframe_use && vo.userBankList.isEmpty() && vo.phone_needbind ==false){
            // 绑定手机
            CfLog.i("****** 绑定手机");
            toBindPhoneNumber();
            return;
        }*/
        //if (vo.op_thiriframe_use && vo.userBankList.isEmpty() && vo.view_bank_card && !vo.phone_needbind) {
        //极速充值在详情里面判断绑卡
        if (vo.view_bank_card && vo.userBankList.isEmpty() && !vo.paycode.contains(ONE_PAY_FIX)) {
            // 绑定YHK
            CfLog.i("****** 绑定YHK");
            toBindCard();
            return;
        }

        if ("false".equalsIgnoreCase(vo.bankcardstatus_onepayzfb) && vo.paycode.contains("zfb")) {
            // 请先绑定您的支付宝账号
            CfLog.i("****** 绑定ZFB");
            toBindAlipay();
            return;
        }
        if ("false".equalsIgnoreCase(vo.bankcardstatus_onepaywx) && vo.paycode.contains("wx")) {
            // 请先绑定您的微信账号
            CfLog.i("****** 绑定WX");
            toBindWeChat();
            return;
        }

        CfLog.i("****** not need bind...");

        // 打开网页类型的
        if (vo.op_thiriframe_use && !vo.phone_needbind) {
            CfLog.d(vo.title + ", jump: " + vo.op_thiriframe_url);
            binding.llDown.setVisibility(View.GONE); // 下面的部分隐藏
            if (isOnePayFix(vo)) {
                onClickPayment3(vo);
                viewModel.checkOrder(vo.bid); // 查极速充值的未完成订单
            } else if (!TextUtils.isEmpty(vo.op_thiriframe_url)) {
                TagUtils.tagEvent(getContext(), "rc", vo.bid); // 打点
//                String url = vo.op_thiriframe_url.startsWith("http") ? vo.op_thiriframe_url : DomainUtil.getApiUrl() + vo.op_thiriframe_url;
                String url = vo.op_thiriframe_url;
                if (!url.startsWith("http")) {
                    String separator;
                    if (DomainUtil.getApiUrl().endsWith("/") && url.startsWith("/")) {
                        url = url.substring(1);
                        separator = "";
                    } else if (DomainUtil.getApiUrl().endsWith("/") || url.startsWith("/")) {
                        separator = "";
                    } else {
                        separator = File.separator;
                    }
                    url = DomainUtil.getApiUrl() + separator + url;
                }
                showWebPayDialog(vo.title, url);
            } else if (vo.paycode.contains(ONE_PAY_FIX)) {
                // 极速充值
                CfLog.i(vo.bid + " , " + vo.title + " , " + vo.paycode);
//                viewModel.checkOrder(vo.bid); // 查极速充值的未完成订单
//                viewModel.getPayment(vo.bid); // 查详情,显示快选金额,银行列表用
                viewModel.getExPayment(vo.bid, getActivity());
            } else {
                // 如果没有链接,调详情接口获取
                viewModel.getPayment(vo.bid);
                LoadingDialog.show(getContext()); // Loading
            }

            return;
        }

        onClickPayment3(vo);
    }

    private void onClickPayment3(RechargeVo vo) {
        CfLog.i("****** llDown is visible");
        binding.llDown.setVisibility(View.VISIBLE); // 下面的部分显示

        // 人工充值
        if (vo.paycode.equals(KEY_MANUAL)) {
            CfLog.i("manual ****** ");
            binding.llBankCard.setVisibility(View.GONE);
            binding.llName.setVisibility(View.GONE);
            binding.llAmount.setVisibility(View.GONE);
            binding.llManual.setVisibility(View.VISIBLE);
            return;
        } else {
            binding.llName.setVisibility(View.VISIBLE);
            binding.llAmount.setVisibility(View.VISIBLE);
            binding.llManual.setVisibility(View.GONE);
        }

        // 显示/隐藏银行卡 userBankList
        if (vo.view_bank_card) {
            binding.llBankCard.setVisibility(View.VISIBLE);
            binding.tvwBankCard.setOnClickListener(v -> showBankCard(vo)); // 选择银行卡
            if (isOnePayFix(vo)) {
                if (vo.getOpBankList() != null && vo.getOpBankList().getUsed() != null
                        && !vo.getOpBankList().getUsed().isEmpty()) {
                    //去掉工商银行
                    ArrayList<RechargeVo.OpBankListDTO.BankInfoDTO> userBankList = new ArrayList<>();
                    for (RechargeVo.OpBankListDTO.BankInfoDTO bankCardVo : vo.getOpBankList().getUsed()) {
                        if (!TextUtils.isEmpty(bankCardVo.getBankName()) && bankCardVo.getBankName().indexOf("工商银行") == -1) {
                            userBankList.add(bankCardVo);
                        }
                    }

                    if (userBankList.isEmpty()) {
                        ppw3 = new XPopup.Builder(getContext()).dismissOnTouchOutside(false)
                                .dismissOnBackPressed(false).asCustom(new TipBindCardDialog(getContext(), "", new TipBindCardDialog.ICallBack() {
                                    @Override
                                    public void onClickConfirm() {
                                        toBindPage(Constant.BIND_CARD);
                                    }
                                }));
                        ppw3.show();
                    } else {
                        bankCode = userBankList.get(0).getBankCode();
                        binding.tvwBankCard.setText(userBankList.get(0).getBankName());
                    }
                } else if (!vo.userBankList.isEmpty()) {
                    //去掉工商银行
                    ArrayList<BankCardVo> userBankList = new ArrayList<>();
                    for (BankCardVo bankCardVo : vo.userBankList) {
                        if (!TextUtils.isEmpty(bankCardVo.name) && bankCardVo.name.indexOf("工商银行") == -1) {
                            userBankList.add(bankCardVo);
                        }
                    }
                    if (userBankList.isEmpty()) {
                        ppw3 = new XPopup.Builder(getContext()).dismissOnTouchOutside(false)
                                .dismissOnBackPressed(false).asCustom(new TipBindCardDialog(getContext(), "", new TipBindCardDialog.ICallBack() {
                                    @Override
                                    public void onClickConfirm() {
                                        toBindPage(Constant.BIND_CARD);
                                    }
                                }));
                        ppw3.show();
                    } else {
                        String txt = userBankList.get(0).name;
                        if (txt.contains("--")) {
                            txt = txt.split("--")[0];
                        }
                        bankId = userBankList.get(0).id;
                        binding.tvwBankCard.setText(txt);
                    }

                }
            } else if (!vo.userBankList.isEmpty()) {
                bankId = vo.userBankList.get(0).id;
                binding.tvwBankCard.setText(vo.userBankList.get(0).name);
            }
        } else {
            binding.llBankCard.setVisibility(View.GONE);
        }

        // 设置存款人姓名
        if (vo.realchannel_status && vo.phone_fillin_name) {
            CfLog.i("设置存款人姓名 = " + vo.accountname);
            binding.edtName.setText(vo.accountname);
            if (!TextUtils.isEmpty(vo.accountname)) {
                binding.edtName.setEnabled(false);
                binding.ivwClear.setVisibility(View.INVISIBLE);
                binding.tvwTipName.setVisibility(View.GONE);
            } else {
                binding.edtName.setText("");
                binding.edtName.setEnabled(true);
                binding.tvwTipName.setVisibility(View.VISIBLE);
            }
            binding.llName.setVisibility(View.VISIBLE);
        } else {
            binding.edtName.setText("");
            binding.edtName.setEnabled(true);
            binding.ivwClear.setVisibility(View.VISIBLE);
            //嗨钱包不隐藏姓名输入栏
            if (!curRechargeVo.paycode.contains("hiwallet")) {
                binding.llName.setVisibility(View.GONE);
            }
            binding.tvwTipName.setVisibility(View.VISIBLE);
        }

        loadMin = Double.parseDouble(vo.loadmin);
        loadMax = Double.parseDouble(vo.loadmax);
        String amount = binding.edtAmount.getText().toString();

        binding.tvwTipAmount.setText(getString(R.string.txt_enter_correct_amount, vo.loadmin, vo.loadmax));
        binding.tvwTipAmount.setVisibility(View.GONE);

        // 有一组金额按钮需要显示出来 (固额和非固额)
        if (vo.fixedamount_channelshow && vo.fixedamount_info.length > 0) {
            binding.edtAmount.setEnabled(false);
            binding.tvwAmountHint.setText(R.string.txt_choose_recharge_amount); // 请选择金额
            setAmountGrid(vo.fixedamount_info);
            // 固额 如果输入框的金额不是固额列表中的其中一个,显示提示文字
            if (!amount.isEmpty() && !Arrays.asList(vo.fixedamount_info).contains(amount)) {
                //binding.edtAmount.setText("");
                binding.tvwTipAmount.setText(getString(R.string.txt_choose_recharge_amount));
                binding.tvwTipAmount.setVisibility(View.VISIBLE);
            }
        } else {
            binding.edtAmount.setEnabled(true);
            String hint = getString(R.string.txt_enter_recharge_amount, vo.loadmin, vo.loadmax);
            binding.tvwAmountHint.setText(hint); // 请输入充值金额(最低%1$s元，最高%2$s元)
            //List<String> list = getFastMoney(vo.loadmin, vo.loadmax);
            //vo.fixedamount_info = list.toArray(new String[list.size()]);
            setAmountGrid(vo.recommendMoney);
        }

        //binding.edtAmount.setText(""); // 需求,不要清空上个渠道的充值金额

        if (!amount.isEmpty() && (Double.parseDouble(amount) < loadMin || Double.parseDouble(amount) > loadMax)) {
            //binding.edtAmount.setText("");
            binding.tvwTipAmount.setVisibility(View.VISIBLE);
        }

        setRate(vo); // 设置汇率提示信息
        setPrePay(vo, amount); // 设置预计支付
        setNextButton();
        //只有是极速充值 才显示充值引导
        if (isOnePayFix(vo)) {
            if (ppw3 != null && ppw3.isShow()) {
                return;
            }
            showGuideDialog(vo);
        }

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

    public  void  toBindPhoneNumber() {
        if (bindPhonePPW == null) {
            String msg = getString(R.string.txt_rc_bind_phone_pls);
            String left = getString(R.string.txt_cancel);
            String right = getString(R.string.txt_rc_bind_phone);
            MsgDialog dialog = new MsgDialog(getContext(), null, msg, left, right, new MsgDialog.ICallBack() {
                @Override
                public void onClickLeft() {
                    if (bindPhonePPW !=null){
                        bindPhonePPW.dismiss();
                        bindPhonePPW = null;
                    }

                   // bindPhonePPW = null;
                }

                @Override
                public void onClickRight() {
                    toBindPhonePage();
                    if (bindPhonePPW !=null){
                        bindPhonePPW.dismiss();
                        bindPhonePPW = null;
                    }
                    //bindPhonePPW = null;
                }
            });
            bindPhonePPW = new XPopup.Builder(getContext())
                    .dismissOnTouchOutside(false)
                    .dismissOnBackPressed(false)
                    .dismissOnBackPressed(true)
                    .asCustom(dialog);
            bindPhonePPW.show();
        } else {
            bindPhonePPW.dismiss();
            bindPhonePPW = null;
            String msg = getString(R.string.txt_rc_bind_phone_pls);
            String left = getString(R.string.txt_cancel);
            String right = getString(R.string.txt_rc_bind_phone);
            MsgDialog dialog = new MsgDialog(getContext(), null, msg, left, right, new MsgDialog.ICallBack() {
                @Override
                public void onClickLeft() {
                    if (bindPhonePPW !=null){
                        bindPhonePPW.dismiss();
                        bindPhonePPW = null;
                    }
                   /* bindPhonePPW.dismiss();
                    bindPhonePPW = null;*/
                }

                @Override
                public void onClickRight() {
                    toBindPhonePage();
                    if (bindPhonePPW !=null){
                        bindPhonePPW.dismiss();
                        bindPhonePPW = null;
                    }
                   /* bindPhonePPW.dismiss();
                    bindPhonePPW = null;*/
                }
            });
            bindPhonePPW = new XPopup.Builder(getContext())
                    .dismissOnTouchOutside(false)
                    .dismissOnBackPressed(false)
                    .dismissOnBackPressed(true)
                    .asCustom(dialog);
            bindPhonePPW.show();
        }

    }

    private void toBindCard() {

        CfLog.e("toBindCard ------ is toBindCardl");
        String msg = getString(R.string.txt_rc_bind_bank_card_pls);
        if (bindCardPPW == null) {
            MsgDialog dialog = new MsgDialog(getContext(), null, msg, new MsgDialog.ICallBack() {
                @Override
                public void onClickLeft() {
                    bindCardPPW.dismiss();
                    bindCardPPW = null;
                }

                @Override
                public void onClickRight() {
                    toBindPage(Constant.BIND_CARD);
                    bindCardPPW.dismiss();

                }
            });
            bindCardPPW = new XPopup.Builder(getContext())
                    .dismissOnTouchOutside(false)
                    .dismissOnBackPressed(false)
                    .dismissOnBackPressed(true)
                    .asCustom(dialog);
            bindCardPPW.show();
        } else {
            CfLog.e("toBindCard ------ is not  null");
            bindCardPPW = null;
            MsgDialog dialog = new MsgDialog(getContext(), null, msg, new MsgDialog.ICallBack() {
                @Override
                public void onClickLeft() {
                    bindCardPPW.dismiss();
                    bindCardPPW = null;
                }

                @Override
                public void onClickRight() {
                    toBindPage(Constant.BIND_CARD);
                    bindCardPPW.dismiss();

                }
            });
            bindCardPPW = new XPopup.Builder(getContext())
                    .dismissOnTouchOutside(false)
                    .dismissOnBackPressed(false)
                    .dismissOnBackPressed(true)
                    .asCustom(dialog);
            bindCardPPW.show();
        }

    }

    private  void toBindPhonePage() {
        String type = Constant.BIND_PHONE; // VERIFY_BIND_PHONE
        if (mProfileVo != null && mProfileVo.is_binding_email) {
            type = Constant.VERIFY_BIND_PHONE;
        }
        toBindPage(type);
    }

    private void toBindPage(String type) {
        isNeedRefresh = true;
        Bundle bundle = new Bundle();
        bundle.putString("type", type);

        if (bindCardPPW != null) {
            bindCardPPW.dismiss();

        }
        startContainerFragment(RouterFragmentPath.Mine.PAGER_SECURITY_VERIFY, bundle);
    }

    private void toBindAlipay() {
        toBindAlipayWechat(getString(R.string.txt_bind_zfb_type), getString(R.string.txt_rc_bind_alipay_pls));
    }

    private void toBindWeChat() {
        toBindAlipayWechat(getString(R.string.txt_bind_wechat_type), getString(R.string.txt_rc_bind_wechat_pls));
    }

    private void toBindAlipayWechat(String type, String msg) {
        MsgDialog dialog = new MsgDialog(getContext(), null, msg, new MsgDialog.ICallBack() {
            @Override
            public void onClickLeft() {
                ppw2.dismiss();
            }

            @Override
            public void onClickRight() {
                isNeedRefresh = true;
                // 绑定页面显示去充值按钮用
                SPUtils.getInstance().put(SPKeyGlobal.TYPE_RECHARGE_WITHDRAW, getString(R.string.txt_go_recharge));
                toBindPage(type); // bindcardzfb bindcardwx
                ppw2.dismiss();
            }
        });
        ppw2 = new XPopup.Builder(getContext())
                .dismissOnTouchOutside(false)
                .dismissOnBackPressed(false)
                .asCustom(dialog);
        ppw2.show();
    }

    /**
     * 获取 实际充值金额
     */
    private void getRealMoney() {
        String txt = binding.tvwRealAmount.getText().toString();
        String realName = binding.edtName.getText().toString().trim();

        Map<String, String> map = new HashMap<>();
        map.put("alipayName", ""); //
        map.put("amount", txt); //
        map.put("nonce", UuidUtil.getID16());
        map.put("rechRealname", realName); //
        map.put("bankid", bankId);
        map.put("perOrder", "true");
        map.put("orderKey", "");

        CfLog.i("****** " + map);
        viewModel.getRealMoney(curRechargeVo.bid, map);
    }

    private void setNextButton() {

        binding.btnNext.setEnabled(false); // 默认禁用

        if (curRechargeVo == null) {
            return;
        }
        if (curRechargeVo.paycode.equals(KEY_MANUAL)) {
            binding.btnNext.setEnabled(true);
            return;
        }

        // 普通银行卡充值 bankId非空; 极速充值 bankId,bankCode 至少要有一个非空
        if (curRechargeVo.view_bank_card) {
            if (TextUtils.isEmpty(bankId) && TextUtils.isEmpty(bankCode)) {
                return;
            }
        }

        String realName = binding.edtName.getText().toString().trim();
        if (curRechargeVo.realchannel_status && curRechargeVo.phone_fillin_name) {
            if (TextUtils.isEmpty(realName)) {
                return;
            }
        }

        String txt = binding.tvwRealAmount.getText().toString();
        double amount = Double.parseDouble(0 + txt);
        if (amount < loadMin || amount > loadMax) {
            return;
        }

        if (curRechargeVo.fixedamount_channelshow && curRechargeVo.fixedamount_info.length > 0) {
            // 固额 如果输入框的金额不是固额列表中的其中一个
            if (!txt.isEmpty() && !Arrays.asList(curRechargeVo.fixedamount_info).contains(txt)) {
                // "充值金额异常,请选择列表中的固定金额！",
                return;
            }
        }

        binding.btnNext.setEnabled(true);
    }

    private void goNext() {
        CfLog.i("******");
        String realName = binding.edtName.getText().toString().trim(); // 姓名
        String txt = binding.tvwRealAmount.getText().toString(); // 金额
        TagUtils.tagEvent(getContext(), "rc", curRechargeVo.bid); // 打点

        LoadingDialog.show(getContext()); // Loading
        Map<String, String> map = new HashMap<>();
        map.put("alipayName", ""); //
        map.put("amount", txt); // 充值金额
        // nonce: 如果第一次请求失败，第二次再请求 不能改变
        map.put("nonce", UuidUtil.getID16());
        map.put("rechRealname", realName); // 真实姓名
        map.put("bankid", bankId); // 用户绑定的银行卡id
        //map.put("perOrder", "false");
        //map.put("orderKey", "");

        viewModel.rechargePay(curRechargeVo.bid, map);
    }

    /**
     * 人工充值
     */
    private void goNextManual() {
        LoadingDialog.show(getContext());
        viewModel.getManualSignal();
    }

    /**
     * 极速充值
     */
    private void goNext2() {
        TagUtils.tagEvent(getContext(), "rc", curRechargeVo.bid); // 打点
        LoadingDialog.show(getContext()); // Loading

        String realName = binding.edtName.getText().toString().trim();
        String txt = binding.tvwRealAmount.getText().toString();

        Map<String, String> map = new HashMap<>();
        map.put("pid", curRechargeVo.bid); // 渠道ID
        map.put("payAmount", txt); // 金额
        if (TextUtils.isEmpty(bankCode)) {
            map.put("userBankId", bankId); // 用户绑定的银行卡id【可选】 1283086
        } else {
            map.put("payBankCode", bankCode); // 付款银行编号(和userBankId字段二选一)【可选】 ABC
        }
        map.put("payName", realName); // 付款人
        map.put("nonce", UuidUtil.getID16());
        CfLog.i(map.toString());
        viewModel.createOrder(map);
    }

    private void goHiWallet() {

        boolean isNeedPhone = mProfileVo != null && !mProfileVo.is_binding_phone;
        if (curRechargeVo.phone_needbind && isNeedPhone) {
            // 绑定手机
            CfLog.i("****** 绑定手机");
            toBindPhoneNumber();
            return;
        }
        if (curRechargeVo.userBankList.isEmpty()) {
            // 绑定YHK
            CfLog.i("****** 绑定YHK");
            toBindCard();
            return;
        }

        if (mHiWalletVo != null && !mHiWalletVo.is_registered) {
            TagUtils.tagEvent(getContext(), "rc", curRechargeVo.bid); // 打点
            // 弹窗 目前登入账号尚未绑定嗨钱包账号，确认是否继续操作？
            new XPopup.Builder(getContext())
                    .dismissOnTouchOutside(false)
                    .dismissOnBackPressed(false)
                    .asCustom(new RechargeHiWalletDialog(getContext(), mHiWalletVo, () -> goNext()))
                    .show();
        } else {
            // 下一步
            goNext();
        }
    }

    private void show1kEntryDialog() {
        if (TextUtils.isEmpty(hiWalletUrl)) {
            CfLog.e("hiWalletUrl is empty.");
            return;
        }

        String title = getString(R.string.txt_kind_tips);
        //String msg = getString(R.string.txt_is_to_open_hiwallet_wallet); //
        String txt = getString(R.string.txt_is_to_open_hiwallet_title); //
        txt = "<font color=#1E1E1E>" + txt + "</font><br>";
        txt += getString(R.string.txt_is_to_open_hiwallet_content);
        Spanned msg = HtmlCompat.fromHtml(txt, HtmlCompat.FROM_HTML_MODE_LEGACY);

        ppw = new XPopup.Builder(getContext())
                .dismissOnTouchOutside(false)
                .dismissOnBackPressed(false)
                .asCustom(new MsgDialog(getContext(), title, msg, false, new MsgDialog.ICallBack() {
                    @Override
                    public void onClickLeft() {
                        ppw.dismiss();
                    }

                    @Override
                    public void onClickRight() {
                        new XPopup.Builder(getContext()).moveUpToKeyboard(false).asCustom(BrowserDialog.newInstance(getContext(), hiWalletUrl)).show();
                        ppw.dismiss();
                    }
                }));
        ppw.show();
    }

    private void setAmountGrid(String[] array) {
        mAmountAdapter.clear(); // 清空
        if (array != null && array.length > 0) {
            mAmountAdapter.addAll(Arrays.asList(array)); // 数组转列表
        }
    }

    /**
     * 选择银行卡, 含普通充值渠道和极速充值渠道
     *
     * @param vo 充值渠道
     */
    private void showBankCard(RechargeVo vo) {
        if (ClickUtil.isFastClick()) {
            return;
        }

        if (isOnePayFix(vo)) {
            // 极速充值
            showBankCardExDialog(vo);
        } else {
            // 普通充值
            showBankCardDialog(vo);
        }
    }

    /**
     * 极速充值银行卡选择弹窗
     */
    private void showBankCardExDialog(RechargeVo vo) {
        RechargeVo re = vo; // liveData 可能为空
        if (re == null || re.getOpBankList() == null) {
            CfLog.e("****** bank list is null.");
            return;
        }

        RechargeVo.OpBankListDTO opBankList = re.getOpBankList();
        ArrayList<RechargeVo.OpBankListDTO.BankInfoDTO> bankInfoDTOS = new ArrayList<>();
        for (BankCardVo bankCardVo : vo.userBankList) {
            RechargeVo.OpBankListDTO.BankInfoDTO bankInfoDTO = new RechargeVo.OpBankListDTO.BankInfoDTO();
            if (!TextUtils.isEmpty(bankCardVo.name) && bankCardVo.name.indexOf("工商银行") == -1) {
                bankInfoDTO.setBankCode(bankCardVo.id);
                bankInfoDTO.setBankName(bankCardVo.name);
                bankInfoDTOS.add(bankInfoDTO);
            }

        }
        opBankList.setmBind(bankInfoDTOS);

        String string = binding.tvwBankCard.getText().toString();
        BankPickDialogFragment.show(getActivity(), opBankList, string)
                .setOnPickListner(model -> {
                    CfLog.d(model.toString());
                    if (TextUtils.isEmpty(model.getBankCode())) {
                        bankId = model.getBankId();
                    } else {
                        bankCode = model.getBankCode();
                    }
                    binding.tvwBankCard.setText(model.getBankName());
                });
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

        //String json = SPUtils.getInstance().getString(SPKeyGlobal.RC_PAYMENT_THIRIFRAME, "{}");
        //mapRechargeVo = new Gson().fromJson(json, new TypeToken<HashMap<String, RechargeVo>>() {
        //}.getType());

        isShowOrderDetail = getArguments().getBoolean("isShowOrderDetail");
        if (isShowOrderDetail) {
            String id = getArguments().getString("orderDetailId");
            RechargeOrderVo vo = getArguments().getParcelable("obj");
            CfLog.i("RechargeOrderVo: " + (vo != null));
            // 极速充值 带有onepayfix且bankId非空
            if (vo != null && vo.sysParamPrefix.contains(ONE_PAY_FIX) && !TextUtils.isEmpty(vo.bankId)) {
                if (TextUtils.isEmpty(vo.orderurl)) {
                    CfLog.i("RechargeOrderVo, bankId: " + vo.bankId);
                    LoadingDialog.show(getContext());
                    viewModel.checkOrder(vo.bankId); // 根据充值渠道ID 查询订单详情 (极速充值)
                    viewModel.liveDataExpTitle.setValue(vo.payport_nickname);
                } else {
                    //goPay(vo);
                    new XPopup.Builder(getContext()).asCustom(new BrowserDialog(getContext(), "", vo.orderurl)).show();
                }
            } else {
                viewModel.getOrderDetail(id); // 普通充值
            }

        }

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
        } else if (vo.paycode.equals("hqppaygobao")) {
            binding.tvwFxRate.setText(R.string.txt_rate_gobao_cny);
        } else if (vo.paycode.equals("hqppayokpay")) {
            binding.tvwFxRate.setText(R.string.txt_rate_okpay_cny);
        } else if (vo.paycode.equals("hqppaygopay")) {
            binding.tvwFxRate.setText(R.string.txt_rate_gopay_cny);
        } else {
            binding.tvwFxRate.setText("");
            binding.llRate.setVisibility(View.GONE);
        }

    }

    private void setPrePay(RechargeVo vo, String amount) {
        if (vo == null) {
            CfLog.i("vo is null.");
            binding.tvwPrePay.setVisibility(View.GONE);
            return;
        }
        double realUsdt = Double.parseDouble(0 + amount); // amount 不能为空
        String type = "";
        binding.tvwPrePay.setVisibility(View.VISIBLE);

        if (!TextUtils.isEmpty(vo.usdtrate)) {
            setUsdtRate(vo);
            return;
        } else if (vo.paycode.equals("hiwallet")) {
            type = "CNYT";
        } else if (vo.paycode.equals("hqppaytopay")) {
            type = "TOG";
        } else if (vo.paycode.equals("ebpay")) {
            type = "EB";
        } else if (vo.paycode.equals("hqppaygobao")) {
            type = "GB";
        } else if (vo.paycode.equals("hqppayokpay")) {
            type = "OKG";
        } else if (vo.paycode.equals("hqppaygopay")) {
            type = "GOP";
        } else {
            binding.tvwPrePay.setVisibility(View.GONE);
        }

        String txt = String.format(getString(R.string.format_change_range), NumberUtils.formatUp(realUsdt, 2), type);
        binding.tvwPrePay.setText(txt);
    }

    private void setUsdtRate(RechargeVo vo) {
        binding.tvwFxRate.setText(getString(R.string.txt_rate_usdt, vo.usdtrate));
        int realMoney = Integer.parseInt(0 + binding.tvwRealAmount.getText().toString());
        float realUsdt = realMoney / Float.parseFloat(vo.usdtrate);
        //String usdt = new DecimalFormat("#.##").format(realUsdt);
        //String usdt = String.format(getString(R.string.format_change_range), realUsdt);
        String usdt = String.format(getString(R.string.format_change_range), NumberUtils.formatUp(realUsdt, 2), "USDT");
        binding.tvwPrePay.setText(usdt);
    }

    /**
     * 重新设置选中的充值渠道 (加载缓存后,选中某个渠道,接口返回的数据回来,重设)
     */
    //private void resetCheckedChannel(PaymentVo vo) {
    //    if (curRechargeVo != null) {
    //        for (RechargeVo t : vo.chongzhiList) {
    //            if (curRechargeVo.bid.equals(t.bid)) {
    //                curRechargeVo = t;
    //            }
    //        }
    //    }
    //}
    private void resetCheckedChannel(PaymentDataVo vo) {
        if (curPaymentTypeVo == null) {
            return;
        }

        for (PaymentTypeVo t : vo.chongzhiList) {
            if (curPaymentTypeVo.id.equals(t.id)) {
                curPaymentTypeVo = t;
                if (curRechargeVo != null) {
                    for (RechargeVo t2 : t.payChannelList) {
                        if (curRechargeVo.bid.equals(t2.bid)) {
                            curRechargeVo = t2;
                            viewModel.curRechargeLiveData.setValue(curRechargeVo);
                            return;
                        }
                    }
                }
            }
        }
    }

    private void setStepBottom() {
        binding.llStep.setVisibility(View.GONE); // 默认隐藏

        if (curPaymentTypeVo.dispay_title.toUpperCase().contains("CNYT")) {
            binding.llStep.setVisibility(View.VISIBLE);
            binding.tvwStepTutorial.setText(getResources().getString(R.string.txt_rc_cnyt_tutorial));
            binding.tvwStepContent.setText(getResources().getString(R.string.txt_rc_cnyt_content));
            binding.tvwStepTutorial.setOnClickListener(v -> showWebDialog(getString(R.string.txt_rc_cnyt_tutorial), Constant.URL_RC_CNYT_TUTORIAL));
        } else if (curPaymentTypeVo.dispay_title.toUpperCase().contains("USDT")) {
            binding.llStep.setVisibility(View.VISIBLE);
            binding.tvwStepTutorial.setText(getResources().getString(R.string.txt_rc_usdt_tutorial));
            binding.tvwStepContent.setText(getResources().getString(R.string.txt_rc_usdt_content));
            binding.tvwStepTutorial.setOnClickListener(v -> showWebDialog(getString(R.string.txt_rc_usdt_tutorial), Constant.URL_RC_USDT_TUTORIAL));
        } else {
            CfLog.d("****** default, (not CNYT or USDT.)");
        }

    }

    private void setTipBottom(PaymentTypeVo vo) {
        String title = getString(R.string.txt_kind_tips) + "：\n"; // 温馨提示：
        if (vo == null || TextUtils.isEmpty(vo.channel_tips)) {
            String txt = getString(R.string.txt_bank_card);
            txt = getString(R.string.txt_rc_tip_yhk_1, txt) + "\n";
            String txt2 = getString(R.string.txt_rc_tip_yhk_2a, " ") + "\n";
            binding.tvwTipBottom.setText(title + txt + txt2);
            return;
        }

        String html = getString(R.string.txt_kind_tips) + "："; // 温馨提示：
        html += "\n" + vo.channel_tips;
        html = html.replace("\r", "\n").replace("\n\n", "<BR>").replace("\n", "<BR>");

        //binding.tvwTipBottom.setText(html);
        binding.tvwTipBottom.setText(HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_LEGACY));
    }

    private void setTipBottom2(RechargeVo vo) {

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
            binding.tvwTipSameAmount.setVisibility(View.GONE);
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

    /*private void setHiWallet(PaymentVo vo) {
        binding.llHiWallet.setVisibility(View.GONE);
        for (RechargeVo t : vo.chongzhiList) {
            if (!TextUtils.isEmpty(t.paycode) && t.paycode.contains("hiwallet")) {
                binding.llHiWallet.setVisibility(View.VISIBLE);
                return;
            }
        }
    }*/

    private void setHiWallet(PaymentDataVo vo) {
        binding.llHiWallet.setVisibility(View.GONE);
        for (PaymentTypeVo typeVo : vo.chongzhiList) {
            for (RechargeVo t : typeVo.payChannelList) {
                if (!TextUtils.isEmpty(t.paycode) && t.paycode.contains("hiwallet")) {
                    binding.llHiWallet.setVisibility(View.VISIBLE);
                    return;
                }
            }
        }
    }

    @Override
    public void initViewObservable() {

        viewModel.liveData1kEntry.observe(this, vo -> {
            // 一键进入 HiWallet钱包
            CfLog.i(vo.toString());
            hiWalletUrl = vo.login_url;
            mHiWalletVo = vo;
        });

        //viewModel.liveDataPayment.observe(getViewLifecycleOwner(), vo -> {
        //    resetCheckedChannel(vo); // 重新设置选中充值渠道 curRechargeVo
        //    mPaymentVo = vo;
        //    tutorialUrl = vo.bankdirect_url; // 充值教程
        //
        //    setRecommendList(); // 推荐的充值列表
        //    setMainList(vo.chongzhiList); // 显示充值列表九宫格
        //    showProcessDialog(vo.processingData); // 检查弹窗 充值次数
        //    setTipBottom(new RechargeVo()); // 恢复底部的默认提示
        //    setHiWallet(vo); // 显示/隐藏底部的 下载嗨钱包
        //});
        viewModel.liveDataPaymentData.observe(getViewLifecycleOwner(), vo -> {
            resetCheckedChannel(vo); // 重新设置选中充值渠道 curRechargeVo
            //mPaymentVo = vo;
            mPaymentDataVo = vo;
            tutorialUrl = vo.bankdirect_url; // 充值教程

            setRecommendList(); // 推荐的充值列表
            setMainList(vo.chongzhiList); // 显示充值列表九宫格
            showProcessDialog(vo.processingData); // 检查弹窗 充值次数
            setTipBottom(null); // 恢复底部的默认提示
            setHiWallet(vo); // 显示/隐藏底部的 下载嗨钱包

            // 查询某些充值渠道的详情
            //for (PaymentTypeVo typeVo : vo.chongzhiList) {
            //    for (RechargeVo vo3 : typeVo.payChannelList) {
            //        if (vo3.op_thiriframe_use && !vo3.phone_needbind) {
            //            viewModel.getPaymentCache(vo3.bid);
            //        }
            //    }
            //}
        });
        viewModel.liveDataRechargeCache.observe(getViewLifecycleOwner(), vo -> {
            // 某些充值渠道的详情
            mapRechargeVo.put(vo.bid, vo);
            mHandler.removeMessages(MSG_ADD_PAYMENT);
            mHandler.sendEmptyMessageDelayed(MSG_ADD_PAYMENT, 3000L);
        });
        //viewModel.liveDataRechargeList.observe(getViewLifecycleOwner(), list -> {
        //    setRecommendList(); // 推荐的充值列表
        //    setMainList(list); // 显示充值列表九宫格
        //});
        viewModel.liveDataPayTypeList.observe(getViewLifecycleOwner(), list -> {
            setRecommendList(); // 推荐的充值列表
            setMainList(list); // 显示充值列表九宫格
        });
        viewModel.liveDataPayCodeArr.observe(getViewLifecycleOwner(), list -> {
            CfLog.d();
            payCodeList.clear();
            payCodeList.addAll(list);
        });
        viewModel.liveDataTutorial.observe(getViewLifecycleOwner(), url -> tutorialUrl = url);

        viewModel.liveDataRecharge.observe(getViewLifecycleOwner(), vo -> {
            CfLog.d(vo.toString());

            //工商银行显示处理
            if (vo.op_disable_bank_status && vo.userBankList != null && vo.userBankList.isEmpty()) {
                ppw3 = new XPopup.Builder(getContext()).dismissOnTouchOutside(false)
                        .dismissOnBackPressed(false).asCustom(new TipBindCardDialog(getContext(), vo.op_thiriframe_msg, new TipBindCardDialog.ICallBack() {
                            @Override
                            public void onClickConfirm() {
                                toBindPage(Constant.BIND_CARD);
                            }
                        }));
                ppw3.show();
                return;
            }

            //绑定银行卡
            //&&isOnePayFix(vo)  去除银行判定
            /* if (vo.view_bank_card && vo.userBankList.isEmpty())  == false{*/

            if (vo.view_bank_card  && vo.userBankList.isEmpty()) {
                // 绑定YHK
                CfLog.i("****** 绑定YHK");
                toBindCard();
                return;
            }

            mapRechargeVo.put(vo.bid, vo);
            mHandler.removeMessages(MSG_ADD_PAYMENT);
            mHandler.sendEmptyMessageDelayed(MSG_ADD_PAYMENT, 3000L);
            //SPUtils.getInstance().put(SPKeyGlobal.RC_PAYMENT_THIRIFRAME, new Gson().toJson(mapRechargeVo));
            curRechargeVo = vo;
            viewModel.curRechargeLiveData.setValue(curRechargeVo);

            // 极速充值
            if (isOnePayFix(vo)) {
                // 银行列表,搜索页会用到
                onClickPayment3(vo);
                return;
            }

            if (TextUtils.isEmpty(vo.op_thiriframe_url)) {
                ToastUtils.showError(vo.op_thiriframe_msg);
                return;
            }
            String url = vo.op_thiriframe_url;
//            if (!url.startsWith("http")) {
//                url = DomainUtil.getApiUrl() + url;
//            }
            if (!url.startsWith("http")) {
                String separator;
                if (DomainUtil.getApiUrl().endsWith("/") && url.startsWith("/")) {
                    url = url.substring(1);
                    separator = "";
                } else if (DomainUtil.getApiUrl().endsWith("/") || url.startsWith("/")) {
                    separator = "";
                } else {
                    separator = File.separator;
                }
                url = DomainUtil.getApiUrl() + separator + url;
            }
            CfLog.d(vo.title + ", jump: " + url);
            showWebPayDialog(vo.title, url);
        });

        viewModel.liveDataRechargePay.observe(getViewLifecycleOwner(), vo -> {
            CfLog.i(vo.payname + ", bankcode: " + vo.bankcode + ", money: " + vo.money);
            goPay(vo);
        });

        // 极速充值 点下一步返回结果, 应跳转到 提交订单/转账汇款页
        viewModel.liveDataExpOrderData.observe(getViewLifecycleOwner(), vo -> {
            CfLog.i(vo.toString());

            viewModel.liveDataExpTitle.setValue(null);

            isNeedReset = true; // 取消选中,如果用户再点击,又要查未完成订单和详情
            String realName = binding.edtName.getText().toString().trim();
            String txt = binding.tvwRealAmount.getText().toString();
            ExCreateOrderRequest request = new ExCreateOrderRequest();
            request.setPid(curRechargeVo.bid);
            request.setPayAmount(txt);
            if (TextUtils.isEmpty(bankCode)) {
                request.setUserBankId(bankId);
            } else {
                request.setPayBankCode(bankCode);
            }
            request.setPayName(realName);
            request.setPayBankName(binding.tvwBankCard.getText().toString());

            RxBus.getDefault().postSticky(request);
            startContainerFragment(RouterFragmentPath.Transfer.PAGER_TRANSFER_EX_COMMIT);
        });

        //当前是否有极速订单
        viewModel.liveDataCurOrder.observe(getViewLifecycleOwner(), vo -> {
            CfLog.i("****** liveDataCurOrder");
            // 可能是用户点极速充值渠道来的; 也可能是从悬浮的未完成订单跳过来的(curRechargeVo为空)
            //String realName = binding.edtName.getText().toString().trim();
            //String txt = binding.tvwRealAmount.getText().toString();
            ExCreateOrderRequest request = new ExCreateOrderRequest();
            request.setPid(vo.getData().getBid()); // curRechargeVo.bid
            request.setPayAmount(vo.getData().getPayAmount()); // txt
            //if (TextUtils.isEmpty(bankCode)) {
            //    request.setUserBankId(bankId);
            //} else {
            //    request.setPayBankCode(bankCode);
            //}
            request.setPayBankCode(vo.getData().getPayBankCode()); // bankCode
            request.setPayName(vo.getData().getPayName()); // realName
            request.setPayBankName(vo.getData().getPayBankName()); // binding.tvwBankCard.getText().toString()

            //根据bid设置标题
            RechargeVo rechargeVo = viewModel.getChargeInfoById(request.getPid());
            if (rechargeVo != null) {
                viewModel.liveDataExpTitle.setValue(rechargeVo.title);
            }

            RxBus.getDefault().postSticky(request);
            String status = vo.getData().getStatus();
            switch (status) {
                case "11":
                    isNeedReset = true;
                    startContainerFragment(RouterFragmentPath.Transfer.PAGER_TRANSFER_EX_PAYEE);
                    break;
                case "13":
                    isNeedReset = true;
                    startContainerFragment(RouterFragmentPath.Transfer.PAGER_TRANSFER_EX_COMMIT);
                    break;
                case "14":
                    isNeedReset = true;
                    startContainerFragment(RouterFragmentPath.Transfer.PAGER_TRANSFER_EX_CONFIRM);
                    break;
                default:
                    CfLog.w("status: " + status);
                    break;
            }
        });

        // 无极速订单, 显示点击渠道后需要显示的 选择银行卡/姓名/金额等
        viewModel.liveDataExpNoOrder.observe(this, isNoOrder -> {
            CfLog.i("*****");
            if (isNoOrder) {
                viewModel.liveDataExpTitle.setValue(null);
            }
        });

        viewModel.liveDataRcBanners.observe(this, list -> {
            CfLog.i("*****");
            binding.bnrTop.setDatas(list);
            binding.bnrTop.setVisibility(View.VISIBLE);
        });
        viewModel.liveDataSignal.observe(this, vo -> {
            if (vo.containsKey("code")) {
                // 弹窗 人工充值
                RechargeManualDialog dialog = new RechargeManualDialog(getActivity(), vo.get("code"));
                ppw2 = new XPopup.Builder(getContext())
                        .dismissOnTouchOutside(false)
                        .asCustom(dialog);
                ppw2.show();
            }
        });
        viewModel.liveDataOrderDetail.observe(this, vo -> {
            // 弹出订单详情, (点击首页 悬浮按钮-待处理 跳转过来的)
            goPay(vo.res); // vo.cancel_reason 附带取消原因列表
        });

        viewModel.liveDataProfile.observe(this, vo -> {
            mProfileVo = vo;
        });
    }

    @Override
    public void onDestroyView() {
        viewModel.liveDataPayment.observe(getViewLifecycleOwner(), vo -> {
            // 修复频繁点击充值页和其它页时 有时会出现两个弹窗
        });
        viewModel.liveDataPaymentData.observe(getViewLifecycleOwner(), vo -> {
            // 修复频繁点击充值页和其它页时 有时会出现两个弹窗
        });
        super.onDestroyView();
    }

    /**
     * 是否极速充值 2024-06-05
     *
     * @param vo 充值渠道详情
     * @return true:是 false:否
     */
    private boolean isOnePayFix(RechargeVo vo) {
        RechargeVo.OpBankListDTO bk = vo.getOpBankList();
        if (bk == null) {
            return false;
        }

        boolean isNotEmpty = (bk.getTop() != null && !bk.getTop().isEmpty())
                || (bk.getOthers() != null && !bk.getOthers().isEmpty())
                || (bk.getUsed() != null && !bk.getUsed().isEmpty())
                || (bk.getHot() != null && !bk.getHot().isEmpty());
        return vo.paycode.contains(ONE_PAY_FIX) && isNotEmpty;
    }

    /**
     * 显示网页版的充值界面 <br/>
     * 某些网页版的充值方式 需要加个外跳的按钮, 解决内部加载白屏的问题
     */
    private void showWebPayDialog(String title, String url) {
        boolean isShowBank = curRechargeVo != null && Arrays.asList(arrayBrowser).contains(curRechargeVo.paycode);
        BrowserDialog dialog = new RechargeBrowserDialog(getContext(), title, url).setShowBank(isShowBank).set3rdLink(true);

        new XPopup.Builder(getContext()).asCustom(dialog).show();
    }

    private void goPay(RechargePayVo vo) {
        //String payCodes = "alipay,alipay2,alipaysm,weixin,juxinpay,juxinpay1,juxinpay2,juxinwx1"
        //        + ",juxinwex2,juxinzfb1,juxinzfb2,hqppay,ebpay,cryptohqppay2,cryptotrchqppay2"
        //        + ",hqppaytopay,hiwallet,hqppay6";
        //List<String> payCodeList = Arrays.asList(payCodes.split(",")); // payCodeArr
        //List<String> payCodeList = mPaymentDataVo.payCodeArr; // mPaymentDataVo 为null
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
        } else if (vo.isusdt && !TextUtils.isEmpty(vo.qrcodeurl)) {
            goPayUsdt(vo); // TRC20快付,不能跳转
        } else {
            CfLog.i("****** default...");
            goPayWeb(vo);
        }
    }

    private void goPayWeb(RechargePayVo vo) {
        new XPopup.Builder(getContext())
                .moveUpToKeyboard(false)
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
                    url = DomainUtil.getApiUrl() + url;
                }
            }
            CfLog.i(vo.payname + ", jump: " + url);

            // 弹窗
            new XPopup.Builder(getContext())
                    .moveUpToKeyboard(true)
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

    //private void setMainList(List<RechargeVo> list) {
    //    rechargeAdapter.clear();
    //    rechargeAdapter.addAll(list);
    //    //queryThirdDetail(list);
    //}
    private void setMainList(List<PaymentTypeVo> list) {
        mTypeAdapter.clear();
        mTypeAdapter.addAll(list);
    }

    /**
     * 提前查询需要跳转第三方的详情(链接)
     */
    private void queryThirdDetail(List<RechargeVo> list) {
        for (RechargeVo vo : list) {
            if (vo.op_thiriframe_use && !vo.phone_needbind) {
                CfLog.d(vo.title + ", jump: " + vo.op_thiriframe_url);
                // 调详情接口获取 跳转链接
                viewModel.getPayment(vo.bid);
            }
        }
    }

    /**
     * 推荐的充值列表
     */
    //private void setRecommendList() {
    //    if (mPaymentVo != null && !mPaymentVo.chongzhiList.isEmpty()) {
    //        mRecommendList.clear();
    //        for (RechargeVo t : mPaymentVo.chongzhiList) {
    //            if (t.tips_recommended == 1) {
    //                CfLog.i(t.title);
    //                mRecommendList.add(t);
    //            }
    //        }
    //    }
    //}
    private void setRecommendList() {
        if (mPaymentDataVo != null && !mPaymentDataVo.chongzhiList.isEmpty()) {
            mRecommendList.clear();
            for (PaymentTypeVo vo : mPaymentDataVo.chongzhiList) {
                for (RechargeVo t : vo.payChannelList) {
                    if (t.tips_recommended == 1) {
                        CfLog.i(t.title);
                        mRecommendList.add(t);
                    }
                }
            }
        }
    }

    private void showProcessDialog(ProcessingDataVo vo) {
        CfLog.i(vo.toString());
        // 某些情况下,不用弹窗 (弹订单详情/切到其它页)
        if (isShowOrderDetail || isHidden) {
            CfLog.i("showProcessDialog stop.");
            return;
        }

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

        } else if ((vo.depProcessCnt1 || vo.depProcessCnt3) && vo.userProcessCount > 0 && isTipTodayCount()) {
            // 您已经连续充值 次, 为了保证快速到账，请使用以下渠道进行充值或联系客服进行处理！
            CfLog.i("****** 您已经连续充值 次");
            String msg = getString(R.string.txt_rc_count_low_rate_hint, vo.userProcessCount);
            showRecommendDialog(msg, null);
        }
    }

    /**
     * 是否弹窗(充值次数)
     * 单号: 2684, 2024-03-15
     *
     * @return true:默认弹提示, false:今日不弹提示
     */
    private boolean isTipTodayCount() {
        String cacheDay = SPUtils.getInstance().getString(SPKeyGlobal.RC_NOT_TIP_TODAY_COUNT, "");
        String today = TimeUtils.getCurDate();
        return !today.equals(cacheDay);
    }

    /**
     * 是否弹窗(成功率低)
     * 单号: 2519, 2024-03-15
     *
     * @return true:默认弹提示, false:今日不弹提示
     */
    private boolean isTipTodayLow() {
        String cacheDay = SPUtils.getInstance().getString(SPKeyGlobal.RC_NOT_TIP_TODAY_LOW, "");
        String today = TimeUtils.getCurDate();
        return !today.equals(cacheDay);
    }

    private void showRecommendDialog(String msg, RechargeVo vo) {
        CfLog.i("****** 弹窗: " + msg);
        if (!mRecommendList.isEmpty()) {
            // 弹窗
            String url = DomainUtil.getH5Domain2() + Constant.URL_RC_CNYT_TUTORIAL; // 不用 tutorialUrl
            BasePopupView dialog = new RechargeRecommendDialog(getContext(), msg, url, mRecommendList, vo, curVo -> {
                CfLog.i("****** ");
                //onClickPayment(curVo);

                if (vo != null && curVo != null && vo.bid.equals(curVo.bid)) {
                    onClickPayment2(curVo);
                } else if (curVo != null) {
                    //for (int i = 0; i < mPaymentVo.chongzhiList.size(); i++) {
                    //    if (mPaymentVo.chongzhiList.get(i).bid.equals(curVo.bid)) {
                    //        CfLog.i("i: " + i);
                    //        binding.rcvPmt.scrollToPosition(i);
                    //        View child = binding.rcvPmt.findViewWithTag(curVo.bid);
                    //        if (child != null) {
                    //            child.performClick();
                    //        }
                    //    }
                    //}

                    // 选中大类和小类
                    for (int i = 0; i < mPaymentDataVo.chongzhiList.size(); i++) {
                        PaymentTypeVo typeVo = mPaymentDataVo.chongzhiList.get(i);
                        if (typeVo.deChannelArr.contains(curVo.bid)) {
                            binding.rcvPmt.scrollToPosition(i);
                            View child = binding.rcvPmt.findViewWithTag(typeVo.id);
                            if (child != null) {
                                child.performClick();
                                if (typeVo.deChannelArr.size() > 1) {
                                    // 选中推荐的那个充值渠道 (需要等列表加载完)
                                    Message msg2 = new Message();
                                    msg2.what = MSG_CLICK_CHANNEL;
                                    msg2.obj = curVo;
                                    mHandler.sendMessageDelayed(msg2, 350L);
                                }
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

    private void showWebDialog(String title, String path) {
        String url = path.startsWith("/") ? DomainUtil.getH5Domain2() + path : path;
        new XPopup.Builder(getContext()).asCustom(new BrowserDialog(getContext(), title, url)).show();
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
    /*  显示充值引导页面流程*/

    /**
     * 显示充值引导弹窗
     */
    private void showGuideDialog(RechargeVo vo) {
        showGuideView = new XPopup.Builder(getContext())
                .asCustom(GuideDialog.newInstance(getContext(), new GuideDialog.IGuideDialogCallback() {
                    @Override
                    public void guideJump() {
                        shipGuide();
                        showGuideView.dismiss();
                    }

                    @Override
                    public void guideEnter() {
                        showGuideView.dismiss();
                        showGuideByBank(vo);
                    }
                }));
        showGuideView.show();
    }

    private Guide bankGuide;

    /**
     * 显示付款银行卡引导页面
     */
    private void showGuideByBank(RechargeVo vo) {
        if (vo.fixedamount_info.length > 8) {

        }
        binding.mainScrollview.scrollTo(0, 800);
        GuideBuilder builder = new GuideBuilder();
        builder.setTargetView(binding.llBankCard)
                .setAlpha(150)
                .setHighTargetCorner(20)
                .setHighTargetPadding(10)
                .setAutoDismiss(false);
        builder.addComponent(new RechargeBankComponent(getContext(), new RechargeBankComponent.IRechargeBankCallback() {
            @Override
            public void rechargeBankJump() {
                //跳过
                shipGuide();
                dismissBankGuide();
            }

            @Override
            public void rechargeBankNext() {
                dismissBankGuide();
                //下一步
                showGuideByName(vo, false);
            }
        }));
        bankGuide = builder.createGuide();
        bankGuide.show(getActivity());
        bankGuide.setShouldCheckLocInWindow(false);
    }

    private void dismissBankGuide() {
        if (bankGuide != null) {
            bankGuide.dismiss();
            bankGuide = null;
        }
    }

    private Guide nameGuide;

    /**
     * 显示充值引导页面
     */
    private void showGuideByName(RechargeVo vo, boolean isShowBack) {
        if (isShowBack) {
            if (vo.fixedamount_info.length > 8) {
                binding.mainScrollview.scrollTo(0, -1000);
            }
        } else {
            if (vo.fixedamount_info.length > 8) {
                binding.mainScrollview.scrollTo(0, 1000);
            }
        }

        GuideBuilder builder = new GuideBuilder();
        builder.setTargetView(binding.llName)
                .setAlpha(150)
                .setHighTargetCorner(20)
                .setHighTargetPadding(10)
                .setAutoDismiss(false);

        builder.addComponent(new RechargeNameComponent(new RechargeNameComponent.IRechargeNameCallback() {
            @Override
            public void rechargeNamePrevious() {
                showGuideByBank(vo);
                dismissNameGuide();

            }

            @Override
            public void rechargeNameJump() {
                //跳过
                shipGuide();
                dismissNameGuide();
            }

            @Override
            public void rechargeNameNext() {
                dismissNameGuide();
                showGuideByMoney(vo, false);
            }

        }));
        nameGuide = builder.createGuide();
        nameGuide.show(getActivity());
    }

    private void dismissNameGuide() {
        if (nameGuide != null) {
            nameGuide.dismiss();
            nameGuide = null;
        }
    }

    private Guide moneyGuide;

    /**
     * 显示充值金额页面
     */
    private void showGuideByMoney(RechargeVo vo, boolean isBackShow) {
        if (isBackShow) {
            if (vo.fixedamount_info.length > 8) {
                binding.mainScrollview.scrollTo(0, -1000);
            }
        } else {
            if (vo.fixedamount_info.length > 8) {
                binding.mainScrollview.scrollTo(0, 1000);
            }
        }

        GuideBuilder builder = new GuideBuilder();
        //存款金额
        builder.setTargetView(binding.llAmount)
                .setAlpha(150)
                .setHighTargetCorner(20)
                .setHighTargetPadding(10)
                .setAutoDismiss(false);
        GuideBuilder builder1 = new GuideBuilder();

        builder.addComponent(new RechargeMoneyComponent(new RechargeMoneyComponent.IRechargeMoneyCallback() {
            @Override
            public void rechargeMoneyPrevious() {
                showGuideByName(vo, true);
                dismissMoneyGuide();

            }

            @Override
            public void rechargeMoneyJump() {
                dismissMoneyGuide();
                shipGuide();
            }

            @Override
            public void rechargeMoneyNext() {
                dismissMoneyGuide();
                showGuideByNext(vo);
            }

        }));
        moneyGuide = builder.createGuide();
        moneyGuide.show(getActivity());
    }

    private void dismissMoneyGuide() {
        if (moneyGuide != null) {
            moneyGuide.dismiss();
            moneyGuide = null;
        }
    }

    private Guide nextGuide;

    /**
     * 显示充值 下一步 引导页面
     */
    private void showGuideByNext(RechargeVo vo) {
        if (vo.fixedamount_info.length > 8) {
            binding.mainScrollview.scrollTo(0, 2000);
        }
        GuideBuilder builder = new GuideBuilder();
        builder.setTargetView(binding.btnNext)
                .setAlpha(150)
                .setHighTargetCorner(20)
                .setHighTargetPadding(10)
                .setAutoDismiss(false);

        builder.addComponent(new RechargeNextComponent(new RechargeNextComponent.IRechargeNextCallback() {
            @Override
            public void rechargeNextPrevious() {
                showGuideByMoney(vo, true);
                dismissNextGuide();

            }

            @Override
            public void rechargeNextJump() {
                shipGuide();
                dismissNextGuide();
            }

            @Override
            public void rechargeNextNext() {
                dismissNextGuide();
                startContainerFragment(RouterFragmentPath.Transfer.PAGER_TRANSFER_EX_COMMIT_GUI);
            }

        }));
        nextGuide = builder.createGuide();
        nextGuide.show(getActivity());
    }

    private void dismissNextGuide() {
        if (nextGuide != null) {
            nextGuide.dismiss();
            nextGuide = null;
        }
    }

    /**
     * 跳过引导
     */
    private void shipGuide() {
        //跳过引导
        LoadingDialog.show(getContext());
        viewModel.skipGuide();
    }
}
