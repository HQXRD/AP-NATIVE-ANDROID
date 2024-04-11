package com.xtree.recharge.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.text.HtmlCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.xtree.base.global.Constant;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.router.RouterActivityPath;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.base.utils.AppUtil;
import com.xtree.base.utils.CfLog;
import com.xtree.base.utils.DomainUtil;
import com.xtree.base.utils.NumberUtils;
import com.xtree.base.utils.TagUtils;
import com.xtree.base.utils.TimeUtils;
import com.xtree.base.utils.UuidUtil;
import com.xtree.base.vo.ProfileVo;
import com.xtree.base.widget.BrowserDialog;
import com.xtree.base.widget.ListDialog;
import com.xtree.base.widget.LoadingDialog;
import com.xtree.base.widget.MsgDialog;
import com.xtree.recharge.BR;
import com.xtree.recharge.R;
import com.xtree.recharge.databinding.FragmentRechargeBinding;
import com.xtree.recharge.ui.viewmodel.RechargeViewModel;
import com.xtree.recharge.ui.viewmodel.factory.AppViewModelFactory;
import com.xtree.recharge.vo.BannersVo;
import com.xtree.recharge.vo.PaymentDataVo;
import com.xtree.recharge.vo.PaymentTypeVo;
import com.xtree.recharge.vo.ProcessingDataVo;
import com.xtree.recharge.vo.RechargePayVo;
import com.xtree.recharge.vo.RechargeVo;
import com.youth.banner.adapter.BannerImageAdapter;
import com.youth.banner.holder.BannerImageHolder;
import com.youth.banner.indicator.CircleIndicator;
import com.youth.banner.listener.OnBannerListener;

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

    //RechargeAdapter rechargeAdapter;
    RechargeTypeAdapter mTypeAdapter;
    RechargeChannelAdapter mChannelAdapter;
    AmountAdapter mAmountAdapter;
    double loadMin;
    double loadMax;
    PaymentTypeVo mPaymentTypeVo;
    //PaymentVo mPaymentVo;
    PaymentDataVo mPaymentDataVo;
    RechargeVo curRechargeVo;
    PaymentTypeVo curPaymentTypeVo;
    BasePopupView ppw = null; // 底部弹窗
    BasePopupView ppw2 = null; // 底部弹窗 (二层弹窗)
    String bankId = ""; // 银行卡ID
    String hiWalletUrl; // 一键进入 HiWallet钱包
    String tutorialUrl; // 充值教程
    List<RechargeVo> mRecommendList = new ArrayList<>(); // 推荐的充值渠道列表
    //HashMap<String, RechargeVo> mapRechargeVo = new HashMap<>(); // 跳转第三方链接的充值渠道
    boolean isShowedProcessPendCount = false; // 是否显示过 "订单未到账" 的提示
    boolean isBinding = false; // 是否正在跳转到其它页面绑定手机/YHK (跳转后回来刷新用)
    boolean isShowBack = false; // 是否显示返回按钮
    boolean isShowOrderDetail = false; // 是否显示充值订单详情,需要传订单号过来 (待处理的订单详情) 2024-03-27
    ProfileVo mProfileVo = null; // 个人信息
    // HQAP2-2963 这几个充值渠道 内部浏览器要加个外跳的按钮 2024-03-23
    String[] arrayBrowser = new String[]{"onepayfix3", "onepayfix4", "onepayfix5", "onepayfix6"};
    private static final int MSG_CLICK_CHANNEL = 1001;
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
                    }
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
        //viewModel.getPayments(); // 调用接口
        viewModel.getPaymentsTypeList(); // 调用接口
        viewModel.get1kEntry(); // 一键进入
        viewModel.getRechargeBanners(); // 获取广告轮播图
    }

    @Override
    public void initView() {
        isShowBack = getArguments().getBoolean("isShowBack");
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
            CfLog.e(vo.toInfo());
            mPaymentTypeVo = vo;
            curRechargeVo = null;
            onClickPaymentType(vo);
        });

        binding.rcvPmt.setLayoutManager(new GridLayoutManager(getContext(), 4));
        //binding.rcvPmt.setAdapter(rechargeAdapter);
        binding.rcvPmt.setAdapter(mTypeAdapter);
        binding.rcvPmt.setNestedScrollingEnabled(false); // 禁止滑动

        mChannelAdapter = new RechargeChannelAdapter(getContext(), vo -> {
            CfLog.e(vo.toInfo());
            curRechargeVo = vo;
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
            String url = DomainUtil.getDomain2() + Constant.URL_ANTI_FRAUD;
            new XPopup.Builder(getContext()).asCustom(new BrowserDialog(getContext(), title, url)).show();
        });
        binding.tvwDownload.setOnClickListener(v -> {
            // 下载嗨钱包
            String url = Constant.URL_DOWNLOAD_HI_WALLET;
            getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
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
                    binding.tvwTipName.setVisibility(View.GONE);
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

                // 获取 实际充值金额 (测试环境 银行卡充值3)
                if (curRechargeVo != null && curRechargeVo.isrecharge_additional && s.length() >= 3) {
                    // 调用提交接口, 增加 perOrder=true
                    //getRealMoney();
                }

                if (curRechargeVo != null && !TextUtils.isEmpty(curRechargeVo.usdtrate)) {
                    setUsdtRate(curRechargeVo);
                }
                //if (!TextUtils.isEmpty(s.toString().trim())) {
                //    binding.tvwTipAmount.setVisibility(View.GONE);
                //} else {
                //    binding.tvwTipAmount.setVisibility(View.VISIBLE);
                //}
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.btnNext.setOnClickListener(v -> {
            // 下一步
            goNext();
        });

        setTipBottom(null); // 设置底部的文字提示

        binding.bnrTop.setIndicator(new CircleIndicator(getContext())); // 增加小圆点
        binding.bnrTop.setAdapter(new BannerImageAdapter<BannersVo>(new ArrayList<>()) {
            @Override
            public void onBindView(BannerImageHolder holder, BannersVo data, int position, int size) {
                //holder.imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                String url = data.picture.startsWith("http") ? data.picture : DomainUtil.getDomain2() + data.picture;
                Glide.with(getContext()).load(url).placeholder(R.mipmap.rc_bnr_ad).into(holder.imageView);
            }
        });
        binding.bnrTop.setOnBannerListener((OnBannerListener<BannersVo>) (data, position) -> {
            if (TextUtils.isEmpty(data.link)) {
                return;
            }

            String url = data.link.startsWith("http") ? data.link : DomainUtil.getDomain2() + data.link;
            CfLog.e(url);
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
        refresh();
    }

    private void refresh() {
        if (isBinding) {
            isBinding = false;
            binding.tvwCurPmt.setText("");
            binding.ivwCurPmt.setImageDrawable(null);
            binding.llDown.setVisibility(View.GONE);
            //if (curRechargeVo != null) {
            //    View child = binding.rcvPmt.findViewWithTag(curRechargeVo.bid);
            //    if (child != null) {
            //        child.setSelected(false); // 已选中的取消掉,刷新等待时间有点长
            //    }
            //}

            if (curPaymentTypeVo != null) {
                View childType = binding.rcvPmt.findViewWithTag(curPaymentTypeVo.id);
                if (childType != null) {
                    childType.performClick();
                }
            }

            //curRechargeVo = null; // 如果为空,连续点击x.bid会空指针
            //viewModel.getPayments(); // 绑定回来,刷新数据
            viewModel.getPaymentsTypeList(); // 绑定回来,刷新数据
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {   // 不在最前端显示 相当于调用了onPause();

        } else {  // 第一次可见，不会执行到这里，只会执行onResume
            //网络数据刷新
            refresh();
        }
    }

    private void onClickPaymentType(PaymentTypeVo vo) {
        CfLog.e(vo.toInfo());
        CfLog.e("size: " + vo.payChannelList.size());
        curPaymentTypeVo = vo;
        binding.llCurPmt.setVisibility(View.VISIBLE);
        binding.tvwCurPmt.setText(vo.dispay_title);
        String url = DomainUtil.getDomain2() + vo.un_selected_image; // 未选中 彩色图片
        Glide.with(getContext()).load(url).placeholder(R.mipmap.ic_trans_76).into(binding.ivwCurPmt);
        mChannelAdapter.clear();
        mChannelAdapter.addAll(vo.payChannelList);
        setTipBottom(vo); // 设置底部的文字提示

        // 如果只有一个渠道时，隐藏掉，并触发点击事件
        if (vo.payChannelList.size() == 1) {
            binding.rcvPayChannel.setVisibility(View.GONE);
            curRechargeVo = vo.payChannelList.get(0);
            onClickPayment(vo.payChannelList.get(0));
        } else {
            binding.rcvPayChannel.setVisibility(View.VISIBLE);
            binding.llDown.setVisibility(View.GONE); // 隐藏底部 用户输入的部分

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
        //binding.tvwCurPmt.setText(vo.title); // 用大类的名字
        //Drawable dr = getContext().getDrawable(R.drawable.rc_ic_pmt_selector);
        //dr.setLevel(Integer.parseInt(vo.bid));
        //binding.ivwCurPmt.setImageDrawable(dr);
        binding.tvwBankCard.setText("");
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
        if (vo.op_thiriframe_use && vo.phone_needbind) {
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
            TagUtils.tagEvent(getContext(), "rc", vo.bid); // 打点
            binding.llDown.setVisibility(View.GONE); // 下面的部分隐藏
            if (!TextUtils.isEmpty(vo.op_thiriframe_url)) {
                String url = DomainUtil.getDomain2() + vo.op_thiriframe_url;
                showWebPayDialog(vo.title, url);
            } else {
                // 如果没有链接,调详情接口获取
                viewModel.getPayment(vo.bid);
                LoadingDialog.show(getContext()); // Loading
            }

            return;
        }

        CfLog.i("****** llDown is visible");
        binding.llDown.setVisibility(View.VISIBLE); // 下面的部分显示

        // 人工充值
        if (vo.paycode.equals("manual")) {
            CfLog.i("manual ****** ");
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
            binding.tvwChooseBankCard.setVisibility(View.VISIBLE);
            binding.tvwBankCard.setVisibility(View.VISIBLE);
            binding.tvwBankCard.setOnClickListener(v -> showBankCardDialog(vo));
            if (!vo.userBankList.isEmpty()) {
                bankId = vo.userBankList.get(0).id;
                binding.tvwBankCard.setText(vo.userBankList.get(0).name);
            }
        } else {
            binding.tvwChooseBankCard.setVisibility(View.GONE);
            binding.tvwBankCard.setVisibility(View.GONE);
        }

        // 设置存款人姓名
        if (vo.realchannel_status && vo.phone_fillin_name) {
            CfLog.i("设置存款人姓名 = " + vo.accountname);
            binding.edtName.setText(vo.accountname);
            binding.llName.setVisibility(View.VISIBLE);
            binding.tvwTipName.setVisibility(View.GONE);
        } else {
            binding.edtName.setText("");
            binding.llName.setVisibility(View.GONE);
            binding.tvwTipName.setVisibility(View.VISIBLE);
        }

        // 有一组金额按钮需要显示出来 (固额和非固额)
        if (vo.fixedamount_channelshow && vo.fixedamount_info.length > 0) {
            binding.edtAmount.setEnabled(false);
            binding.edtAmount.setHint(R.string.txt_choose_recharge_amount); // 请选择金额
            setAmountGrid(vo);
        } else {
            binding.edtAmount.setEnabled(true);
            String hint = getString(R.string.txt_enter_recharge_amount, vo.loadmin, vo.loadmax);
            binding.edtAmount.setHint(hint); // 请输入充值金额(最低%1$s元，最高%2$s元)
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

        String msg = getString(R.string.txt_rc_bind_phone_pls);
        String left = getString(R.string.txt_cancel);
        String right = getString(R.string.txt_rc_bind_phone);
        MsgDialog dialog = new MsgDialog(getContext(), null, msg, left, right, new MsgDialog.ICallBack() {
            @Override
            public void onClickLeft() {

                ppw2.dismiss();
            }

            @Override
            public void onClickRight() {
                String type = Constant.BIND_PHONE; // VERIFY_BIND_PHONE
                if (mProfileVo != null && mProfileVo.is_binding_email) {
                    type = Constant.VERIFY_BIND_PHONE;
                }
                toBindPhoneOrEmail(type);
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

    private void goNext() {
        CfLog.i("******");
        if (curRechargeVo == null) {
            ToastUtils.showLong(R.string.pls_choose_recharge_type);
            return;
        }

        if (curRechargeVo.paycode.equals("manual")) {
            LoadingDialog.show(getContext());
            viewModel.getManualSignal();
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

        String txt = binding.tvwRealAmount.getText().toString();
        double amount = Double.parseDouble(0 + txt);
        if (amount < loadMin || amount > loadMax) {
            txt = String.format(getString(R.string.txt_recharge_range), curRechargeVo.loadmin, curRechargeVo.loadmax);
            ToastUtils.showLong(txt);
            return;
        }
        TagUtils.tagEvent(getContext(), "rc", curRechargeVo.bid); // 打点

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

        //String json = SPUtils.getInstance().getString(SPKeyGlobal.RC_PAYMENT_THIRIFRAME, "{}");
        //mapRechargeVo = new Gson().fromJson(json, new TypeToken<HashMap<String, RechargeVo>>() {
        //}.getType());

        isShowOrderDetail = getArguments().getBoolean("isShowOrderDetail");
        if (isShowOrderDetail) {
            String id = getArguments().getString("orderDetailId");
            viewModel.getOrderDetail(id);
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
        } else {
            binding.tvwFxRate.setText("");
            binding.llRate.setVisibility(View.GONE);
        }

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

    private void setUsdtRate(RechargeVo vo) {
        binding.tvwFxRate.setText(getString(R.string.txt_rate_usdt, vo.usdtrate));
        int realMoney = Integer.parseInt(0 + binding.tvwRealAmount.getText().toString());
        float realUsdt = realMoney / Float.parseFloat(vo.usdtrate);
        //String usdt = new DecimalFormat("#.##").format(realUsdt);
        //String usdt = String.format(getString(R.string.format_change_range), realUsdt);
        String usdt = String.format(getString(R.string.format_change_range), NumberUtils.formatUp(realUsdt, 2));
        binding.tvwPrePay.setText(usdt);
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

        viewModel.liveData1kEntry.observe(this, url -> {
            // 一键进入 HiWallet钱包
            hiWalletUrl = url;
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
        });
        //viewModel.liveDataRechargeList.observe(getViewLifecycleOwner(), list -> {
        //    setRecommendList(); // 推荐的充值列表
        //    setMainList(list); // 显示充值列表九宫格
        //});
        viewModel.liveDataPayTypeList.observe(getViewLifecycleOwner(), list -> {
            setRecommendList(); // 推荐的充值列表
            setMainList(list); // 显示充值列表九宫格
        });
        viewModel.liveDataTutorial.observe(getViewLifecycleOwner(), url -> tutorialUrl = url);

        viewModel.liveDataRecharge.observe(getViewLifecycleOwner(), vo -> {
            CfLog.d(vo.toString());
            //mapRechargeVo.put(vo.bid, vo);
            //SPUtils.getInstance().put(SPKeyGlobal.RC_PAYMENT_THIRIFRAME, new Gson().toJson(mapRechargeVo));
            if (TextUtils.isEmpty(vo.op_thiriframe_url)) {
                ToastUtils.showError(vo.op_thiriframe_msg);
                return;
            }
            String url = vo.op_thiriframe_url;
            if (!url.startsWith("http")) {
                url = DomainUtil.getDomain2() + url;
            }
            CfLog.d(vo.title + ", jump: " + url);
            showWebPayDialog(vo.title, url);
        });

        viewModel.liveDataRechargePay.observe(getViewLifecycleOwner(), vo -> {
            CfLog.i(vo.payname + ", bankcode: " + vo.bankcode + ", money: " + vo.money);
            goPay(vo);
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
        List<String> payCodeList = mPaymentDataVo.payCodeArr;
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
                    url = DomainUtil.getDomain2() + url;
                }
            }
            CfLog.i(vo.payname + ", jump: " + url);

            // 弹窗
            //getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
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
        if (!isShowedProcessPendCount && (vo.depProcessCnt1 || vo.depProcessCnt3) && !isShowOrderDetail) {
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

        } else if (vo.userProcessCount > 0 && isTipTodayCount() && !isShowOrderDetail) {
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
            String url = DomainUtil.getDomain2() + Constant.URL_RC_CNYT_TUTORIAL; // 不用 tutorialUrl
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
        String url = path.startsWith("/") ? DomainUtil.getDomain2() + path : path;
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

}
