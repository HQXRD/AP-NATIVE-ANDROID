package com.xtree.recharge.ui.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.gson.Gson;
import com.luck.picture.lib.basic.PictureSelector;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.config.SelectMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.interfaces.OnResultCallbackListener;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.xtree.base.adapter.CacheViewHolder;
import com.xtree.base.adapter.CachedAutoRefreshAdapter;
import com.xtree.base.global.Constant;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.base.utils.CfLog;
import com.xtree.base.utils.DomainUtil;
import com.xtree.base.utils.TimeUtils;
import com.xtree.base.utils.UuidUtil;
import com.xtree.base.vo.ProfileVo;
import com.xtree.base.widget.BrowserActivity;
import com.xtree.base.widget.BrowserDialog;
import com.xtree.base.widget.DateTimePickerDialog;
import com.xtree.base.widget.ListDialog;
import com.xtree.base.widget.MsgDialog;
import com.xtree.recharge.BR;
import com.xtree.recharge.R;
import com.xtree.recharge.databinding.FragmentFeedbackBinding;
import com.xtree.base.widget.GlideEngine;
import com.xtree.base.utils.ImageUploadUtil;
import com.xtree.recharge.ui.viewmodel.RechargeViewModel;
import com.xtree.recharge.ui.viewmodel.factory.AppViewModelFactory;
import com.xtree.recharge.vo.FeedbackDep;
import com.xtree.recharge.vo.FeedbackImageUploadVo;
import com.xtree.recharge.vo.FeedbackVo;
import com.xtree.recharge.vo.OrderFeedbackVo;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import me.xtree.mvvmhabit.base.BaseFragment;
import me.xtree.mvvmhabit.utils.SPUtils;
import me.xtree.mvvmhabit.utils.ToastUtils;
import project.tqyb.com.library_res.databinding.ItemTextBinding;

/**
 * 充值-反馈
 */
@Route(path = RouterFragmentPath.Recharge.PAGER_RECHARGE_FEEDBACK)
public class FeedbackFragment extends BaseFragment<FragmentFeedbackBinding, RechargeViewModel> implements DateTimePickerDialog.ICallBack {

    private int feedbackType = 1; //1 微信 2 usdt
    private boolean isNext = false;//是否提交过
    private String imageRealPathString;//选择的图片地址
    private boolean imageSelector = false;//是否已选择图片
    private Uri imageUri;

    private FeedbackVo feedbackVo;//进入反馈页面回去的数据
    private boolean dataIsOk = false;

    private DateTimePickerDialog timePickerDialog;//时间选择器
    private ArrayList<OrderFeedbackVo> orderFeedbackVoArrayList = new ArrayList<>();//反馈中的订单
    private ArrayList<FeedbackVo.FeedbackModeInfo> modeInfoArrayList = new ArrayList<>();//存储支付方式List
    private ArrayList<FeedbackVo.FeedbackBankInfo> bankInfoArrayList = new ArrayList<>();//支付渠道

    private ArrayList<FeedbackVo.FeedbackProtocolInfo> protocolInfoArrayList = new ArrayList<>();//虚拟币支付协议

    private ArrayList<FeedbackDep> last3DepList = new ArrayList<FeedbackDep>();//反馈中订单信息

    public FeedbackFragment() {

    }

    @Override
    public void initView() {

        binding.ivwBack.setOnClickListener(v -> getActivity().finish());
        binding.llRoot.setOnClickListener(v -> hideKeyBoard());
        binding.ivwNext.setOnClickListener(v -> {

            nextCheckInputWithPayway(feedbackType);
        });

        binding.tvUnorderSelectorPayway.setOnClickListener(v -> {
            showUnReceivedDep(last3DepList);
        });
        binding.tvwUnOrderSelector.setOnClickListener(v -> {
            showUnReceivedDep(last3DepList);
        });
        //未到账订单点击
        binding.llUnorder.setOnClickListener(v -> {
            showUnReceivedDep(last3DepList);
        });

        String json = SPUtils.getInstance().getString(SPKeyGlobal.HOME_PROFILE);
        ProfileVo mProfileVo = new Gson().fromJson(json, ProfileVo.class);
        if (mProfileVo != null) {
            binding.tvUsername.setText(mProfileVo.username);
        }
        referFeedbackUI("wechat");
        //我的客服
        binding.ivwCs.setOnClickListener(v -> {
            String title = getContext().getString(R.string.txt_custom_center);
            String url = DomainUtil.getDomain2() + Constant.URL_CUSTOMER_SERVICE;
            new XPopup.Builder(getContext()).asCustom(new BrowserDialog(getContext(), title, url)).show();
        });
        //消息中心
        binding.ivwMsg.setOnClickListener(v -> {
            startContainerFragment(RouterFragmentPath.Mine.PAGER_MSG);
        });
        //图片选择
        binding.llSelectorTipImage.setOnClickListener(v -> {
            gotoSelectMedia();
        });
        //注册Edittex监听
        initEditListener();
        //付款方式点击事件
        binding.tvwType.setOnClickListener(v -> {
            // CfLog.i("付款方式点击事件  获取页面数据=" + feedbackVo.modeInfo.get(1).name);
            showPayWay(feedbackVo);
        });
        //
        binding.llWechatPayway.setOnClickListener(v -> {
            // CfLog.i("付款方式点击事件  获取页面数据=" + feedbackVo.modeInfo.get(1).name);
            showPayWay(feedbackVo);
        });

        //时间选择器
        binding.tvSelectorRightSavetime.setOnClickListener(v -> {
            showTimeSelector();
        });
        //点击支付渠道
        binding.tvSelectorRightPayway.setOnClickListener(v -> {
            showPayReceive(feedbackVo);
        });
        //虚拟币反馈时候 协议选择点击
        binding.tvwCollectiontNameAgreement.setOnClickListener(v -> {
            showVirtualProtocol(feedbackVo);
        });
        binding.llCollectionNameAgreement.setOnClickListener(v -> {
            showVirtualProtocol(feedbackVo);
        });

    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_feedback;
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
    public void initData() {
        //获取页面数据
        String startTime = TimeUtils.parseTime(new Date(), TimeUtils.FORMAT_YY_MM_DD) + " 00:00";
        String endTime = TimeUtils.parseTime(new Date(), TimeUtils.FORMAT_YY_MM_DD_HH_MM);
        HashMap<String, String> map = new HashMap<>();
        map.put("starttime", startTime);
        map.put("endtime", endTime);
        viewModel.feedbackInfo(map);
    }

    @Override
    public void initViewObservable() {
        //获取进入反馈页面回去的数据
        viewModel.feedbackVoSingleLiveData.observe(this, o -> {
            feedbackVo = o;

            CfLog.i("未到账订单 = " + feedbackVo.list.size());
            orderFeedbackVoArrayList.addAll(feedbackVo.list);
            modeInfoArrayList.addAll(feedbackVo.modeInfo);//付款方式
            bankInfoArrayList.addAll(feedbackVo.banksInfo);//支付渠道
            protocolInfoArrayList.addAll(feedbackVo.protocolInfo);//虚拟币协议
            last3DepList.addAll(feedbackVo.last3Deps);// 反馈订单

        });

        //获取上传图片后的 服务器返回的图片地址
        viewModel.imageUploadVoSingleLiveData.observe(this, o -> {
            FeedbackImageUploadVo imageUploadVo = o;
            CfLog.i("上传后 获取的图片地址是 =" + imageUploadVo.url);
            //获得图片上传地址后 再将数据拼接 进行反馈
            feedbackAdd(imageUploadVo.url);
        });
        //获取上传数据后服务器状态
        viewModel.feedbackAddSingleLiveData.observe(this, o -> {
            showNextDialog();
        });
    }

    ItemTextBinding binding2;
    BasePopupView ppw = null; // 底部弹窗 (选择**菜单)

    /**
     * 显示 提交成功Dialog
     */
    private void showNextDialog() {
        String title = getContext().getString(R.string.txt_tip);
        String msg = getContext().getString(R.string.txt_submitted_suc);
        ppw = new XPopup.Builder(getContext()).asCustom(new MsgDialog(getContext(), title, msg, true, new MsgDialog.ICallBack() {
            @Override
            public void onClickLeft() {
                ppw.dismiss();
            }

            @Override
            public void onClickRight() {
                //跳转到充提记录页面
                Bundle bundle = new Bundle();
                bundle.putString("typeId", "feedback");
                startContainerFragment(RouterFragmentPath.Mine.PAGER_RECHARGE_WITHDRAW, bundle);
                ppw.dismiss();
            }
        }));
        ppw.show();

    }

    /**
     * 显示顶部反馈中的订单
     */
    private void showUnReceivedDep(ArrayList<FeedbackDep> list) {
        CachedAutoRefreshAdapter adapter = new CachedAutoRefreshAdapter<FeedbackDep>() {

            @NonNull
            @Override
            public CacheViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                CacheViewHolder holder = new CacheViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_text, parent, false));
                return holder;
            }

            @Override
            public void onBindViewHolder(@NonNull CacheViewHolder holder, int position) {
                binding2 = ItemTextBinding.bind(holder.itemView);
                FeedbackDep vo = get(position);
                String showMessage = "订单号: " + vo.id + " 金额: " + vo.money;
                CfLog.i("未到账订单信息是 ：" + showMessage);
                binding2.tvwTitle.setText(showMessage);
                binding2.tvwTitle.setOnClickListener(v -> {
                    binding.tvwUnOrderSelector.setText(showMessage);
                    ppw.dismiss();
                });
            }
        };
        adapter.addAll(list);
        ppw = new XPopup.Builder(getContext()).asCustom(new ListDialog(getContext(), "请选择未到账订单", adapter, 20));
        ppw.show();
    }

    /**
     * 显示未到账订单
     *
     * @param orderFeedbackVoArrayList
     */
    private void showUnReceivedOrders(ArrayList<OrderFeedbackVo> orderFeedbackVoArrayList) {
        CachedAutoRefreshAdapter adapter = new CachedAutoRefreshAdapter<OrderFeedbackVo>() {

            @NonNull
            @Override
            public CacheViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                CacheViewHolder holder = new CacheViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_text, parent, false));
                return holder;
            }

            @Override
            public void onBindViewHolder(@NonNull CacheViewHolder holder, int position) {
                binding2 = ItemTextBinding.bind(holder.itemView);
                OrderFeedbackVo vo = get(position);
                //订单号：XXXX 金额:xxxx
                String showMessage = "订单号: " + vo.id + "金额: " + vo.userpay_amount;
                CfLog.i("未到账订单信息是 ：" + showMessage);
                binding2.tvwTitle.setText(showMessage);
                binding2.tvwTitle.setOnClickListener(v -> {
                    binding.tvwUnOrderSelector.setText(showMessage);
                    ppw.dismiss();
                });

            }
        };

        adapter.addAll(orderFeedbackVoArrayList);
        ppw = new XPopup.Builder(getContext()).asCustom(new ListDialog(getContext(), "请选择未到账订单", adapter, 20));
        ppw.show();
    }

    /**
     * 显示付款方式
     */
    private void showPayWay(FeedbackVo vo) {
        CachedAutoRefreshAdapter adapter = new CachedAutoRefreshAdapter<FeedbackVo.FeedbackModeInfo>() {
            @NonNull
            @Override
            public CacheViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                CacheViewHolder holder = new CacheViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_text, parent, false));
                return holder;
            }

            @Override
            public void onBindViewHolder(@NonNull CacheViewHolder holder, int position) {
                binding2 = ItemTextBinding.bind(holder.itemView);
                FeedbackVo.FeedbackModeInfo vo = get(position);
                CfLog.i(" FeedbackVo.FeedbackModeInfo = " + vo.toString());
                binding2.tvwTitle.setText(vo.name);
                binding2.tvwTitle.setOnClickListener(v -> {
                    CfLog.i("feedbackVo " + vo);
                    binding.tvwType.setText(vo.name);
                    //feedbackType = 1; //1 微信 2 usdt
                    if (vo.name.equals("虚拟货币")) {
                        feedbackType = 2;
                        referFeedbackUI("usdt");
                    } else if (vo.name.contains("微信")) {
                        feedbackType = 1;
                        referFeedbackUI("wechat");
                    }

                    ppw.dismiss();
                });
            }

        };

        adapter.addAll(modeInfoArrayList);
        ppw = new XPopup.Builder(getContext()).asCustom(new ListDialog(getContext(), "请选择支付方式", adapter, 20));
        ppw.show();

    }

    /**
     * 显示支付渠道
     */
    private void showPayReceive(FeedbackVo vo) {
        if (vo == null || vo.banksInfo == null) {
            return;
        }

        CachedAutoRefreshAdapter adapter = new CachedAutoRefreshAdapter<FeedbackVo.FeedbackBankInfo>() {
            @NonNull
            @Override
            public CacheViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                CacheViewHolder holder = new CacheViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_text, parent, false));
                return holder;
            }

            @Override
            public void onBindViewHolder(@NonNull CacheViewHolder holder, int position) {
                binding2 = ItemTextBinding.bind(holder.itemView);
                FeedbackVo.FeedbackBankInfo voBankInfo = get(position);
                binding2.tvwTitle.setText(voBankInfo.name);
                binding2.tvwTitle.setOnClickListener(v -> {
                    CfLog.i("****** " + voBankInfo.toString());
                    //et_selector_right_payway;
                    binding.tvSelectorRightPayway.setText(voBankInfo.name);
                    ppw.dismiss();
                });
            }

        };

        adapter.addAll(bankInfoArrayList);
        ppw = new XPopup.Builder(getContext()).asCustom(new ListDialog(getContext(), "请选择支付渠道", adapter, 80));
        ppw.show();

    }

    /**
     * 显示虚拟币协议
     */
    private void showVirtualProtocol(FeedbackVo vo) {
        if (vo == null || protocolInfoArrayList == null || protocolInfoArrayList.size() == 0) {
            return;
        }
        CachedAutoRefreshAdapter adapter = new CachedAutoRefreshAdapter<FeedbackVo.FeedbackProtocolInfo>() {
            @NonNull
            @Override
            public CacheViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                CacheViewHolder holder = new CacheViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_text, parent, false));
                return holder;
            }

            @Override
            public void onBindViewHolder(@NonNull CacheViewHolder holder, int position) {
                binding2 = ItemTextBinding.bind(holder.itemView);
                FeedbackVo.FeedbackProtocolInfo voProtoclInfo = get(position);

                binding2.tvwTitle.setText(voProtoclInfo.name);
                binding2.tvwTitle.setOnClickListener(v -> {
                    CfLog.i("****** " + voProtoclInfo.toString());
                    binding.tvwCollectiontNameAgreement.setText(voProtoclInfo.name);
                    ppw.dismiss();
                });
            }

        };
        adapter.addAll(protocolInfoArrayList);
        ppw = new XPopup.Builder(getContext()).asCustom(new ListDialog(getContext(), "请选择协议", adapter, 80));
        ppw.show();

    }

    /**
     * 显示存款时间选择器
     */
    private void showTimeSelector() {
        new XPopup.Builder(getContext()).
                asCustom(DateTimePickerDialog.newInstance(getContext(), "*存款准确时间:", 6, "yyyy-MM-dd HH:mm:ss", this)).
                show();
    }

    /**
     * DateTimePickerDialog选中回调
     *
     * @param date
     */
    @Override
    public void setDate(String date) {
        binding.tvSelectorRightSavetime.setText(date);
    }

    /**
     * 根据顶部选择 刷新UI
     * wechat
     * usdt
     *
     * @param uiType
     */
    private void referFeedbackUI(String uiType) {
        //微信、支付宝
        if (uiType == "wechat") {

            //隐藏未到账View
            binding.llUnorder.setVisibility(View.VISIBLE);
            //付款方式
            binding.llWechatPayway.setVisibility(View.VISIBLE);
            binding.tvSelectorPayWay.setText(R.string.txt_tip_select_pay);
            binding.tvwType.setText(R.string.txt_tip_select_pay_wechat);

            //付款账户
            binding.llPaymentAccount.setVisibility(View.VISIBLE);
            binding.tvSelectorLeftPaymentAccount.setText(R.string.txt_tip_select_pay);
            binding.etSelectorRightPaymentAccount.setHint(R.string.txt_tip_input_wechat_bank_name);
            binding.ivSelectorTipPayWayAccount.setVisibility(View.VISIBLE);
            binding.ivSelectorTipPayWayAccount.setBackgroundResource(R.mipmap.cm_ic_hint_purple);
            binding.tvSelectorTipPaywayAccount.setVisibility(View.VISIBLE);
            binding.tvSelectorTipPaywayAccount.setText(R.string.txt_tip_input_wechat_pay);
            //付款人姓名
            binding.tvSelectorLeftPaymentName.setText(R.string.txt_tip_input_wechat_payment_name);
            binding.etSelectorRightPaymentName.setHint(R.string.txt_tip_input_wechat_pay_name);
            binding.ivSelectorTipPaywayName.setBackgroundResource(R.mipmap.cm_ic_hint_purple);
            binding.tvSelectorTipPaywayName.setText(R.string.txt_tip_input_pay_wechat_name);
            binding.ivSelectorTipPaywayName.setVisibility(View.VISIBLE);
            binding.tvSelectorTipPaywayName.setVisibility(View.VISIBLE);
            //收款人
            binding.tvSelectorLeftCollectionName.setText(R.string.txt_tip_input_wechat_collection_name);

            binding.etSelectorRightCollectiontName.setVisibility(View.VISIBLE);
            binding.tvwCollectiontNameAgreement.setVisibility(View.GONE); //协议
            binding.tvwCollectiontNameAgreement.setHint(R.string.txt_tip_input_usdt_payment_agreement);

            binding.etSelectorRightCollectiontName.setHint(R.string.txt_tip_input_wechat_pay_name);
            binding.ivSelectorTipCollectionName.setBackgroundResource(R.mipmap.cm_ic_hint_purple);
            binding.tvSelectorTipCollectiontName.setText(R.string.txt_tip_input_collection_wechat_name);
            binding.ivSelectorTipCollectionName.setVisibility(View.VISIBLE);
            binding.tvSelectorTipCollectiontName.setVisibility(View.VISIBLE);

            //存款金额
            binding.tvSelectorLeftSavename.setText(R.string.txt_tip_input_wechat_payment_numb);
            binding.etSelectorRightSavename.setHint(R.string.txt_tip_input_wechat_save_numb);
            binding.ivSelectorTipSavename.setBackgroundResource(R.mipmap.cm_ic_hint_purple);
            binding.tvSelectorTipSavename.setText(R.string.txt_tip_input_wechat_save_number);
            binding.ivSelectorTipSavename.setVisibility(View.VISIBLE);
            binding.tvSelectorTipSavename.setVisibility(View.VISIBLE);
            //存款时间
            binding.tvSelectorLeftSavetime.setText(R.string.txt_tip_input_wechat_payment_time);
            binding.ivSelectorTipSavetime.setVisibility(View.INVISIBLE); //占位隐藏
            binding.tvSelectorTipSavetime.setVisibility(View.INVISIBLE);
            //支付渠道
            binding.tvSelectorLeftPayway.setText(R.string.txt_tip_input_wechat_pay_way);
            binding.tvSelectorRightPayway.setHint(R.string.txt_tip_input_wechat_select_pay_way);
            //收款钱包地址
            binding.llInputUsdtAdd.setVisibility(View.GONE);
            binding.tvSelectorLeftAdd.setText(R.string.txt_tip_input_usdt_add);
            binding.etSelectorRightAdd.setHint(R.string.txt_tip_input_usdt_add_hint);

        }
        //虚拟币
        else if (uiType == "usdt") {
            //隐藏未到账View
            binding.llUnorder.setVisibility(View.VISIBLE);
            //付款方式 隐藏付款方式
            //binding.llWechatPayway.setVisibility(View.GONE);
            binding.tvSelectorPayWay.setText(R.string.txt_tip_select_pay);
            binding.llPaymentAccount.setVisibility(View.GONE);
            binding.tvSelectorLeftPaymentAccount.setText(R.string.txt_tip_select_pay);
            binding.etSelectorRightPaymentAccount.setHint(R.string.txt_tip_input_wechat_bank_name);
            binding.ivSelectorTipPayWayAccount.setBackgroundResource(R.mipmap.cm_ic_hint_purple);
            binding.tvSelectorTipPaywayAccount.setText(R.string.txt_tip_select_pay_bottom);
            //钱包地址
            binding.tvSelectorLeftPaymentName.setText(R.string.txt_tip_input_usdt_payment_account);
            binding.etSelectorRightPaymentName.setHint(R.string.txt_tip_input_usdt_selector_payment_account);
            binding.ivSelectorTipPaywayName.setBackgroundResource(R.mipmap.cm_ic_hint_purple);
            binding.ivSelectorTipPaywayName.setVisibility(View.INVISIBLE);
            binding.tvSelectorTipPaywayName.setText(R.string.txt_tip_input_pay_wechat_name);
            binding.tvSelectorTipPaywayName.setVisibility(View.INVISIBLE);
            //协议
            binding.tvSelectorLeftCollectionName.setText(R.string.txt_tip_input_usdt_payment_name);
            binding.etSelectorRightCollectiontName.setHint(R.string.txt_tip_input_usdt_payment_agreement);

            binding.etSelectorRightCollectiontName.setVisibility(View.GONE);
            binding.tvwCollectiontNameAgreement.setVisibility(View.VISIBLE); //协议
            binding.tvwCollectiontNameAgreement.setHint(R.string.txt_tip_input_usdt_payment_agreement);

            binding.ivSelectorTipCollectionName.setBackgroundResource(R.mipmap.cm_ic_hint_purple);
            binding.ivSelectorTipCollectionName.setVisibility(View.INVISIBLE);
            binding.tvSelectorTipCollectiontName.setText(R.string.txt_tip_input_collection_wechat_name);
            binding.tvSelectorTipCollectiontName.setVisibility(View.INVISIBLE);
            //虚拟币数量
            binding.tvSelectorLeftSavename.setText(R.string.txt_tip_input_usdt_numb);
            binding.etSelectorRightSavename.setHint(R.string.txt_tip_input_wechat_save_numb);
            binding.ivSelectorTipSavename.setBackgroundResource(R.mipmap.cm_ic_hint_purple);
            binding.ivSelectorTipSavename.setVisibility(View.INVISIBLE);
            binding.tvSelectorTipSavename.setText(R.string.txt_tip_input_wechat_save_number);
            binding.tvSelectorTipSavename.setVisibility(View.INVISIBLE);
            //存款时间
            binding.tvSelectorLeftSavetime.setText(R.string.txt_tip_input_wechat_payment_time);
            binding.ivSelectorTipSavetime.setVisibility(View.INVISIBLE); //占位隐藏
            binding.tvSelectorTipSavetime.setVisibility(View.INVISIBLE);

            //第三方订单号 提示语不显示
            binding.ivSelectorTipThree.setVisibility(View.INVISIBLE);
            binding.tvSelectorTipThree.setVisibility(View.INVISIBLE);

            //支付渠道
            binding.tvSelectorLeftPayway.setText(R.string.txt_tip_input_wechat_pay_way);
            binding.tvSelectorRightPayway.setHint(R.string.txt_tip_input_wechat_select_pay_way);
            //收款钱包地址
            binding.llInputUsdtAdd.setVisibility(View.VISIBLE);
            binding.tvSelectorLeftAdd.setText(R.string.txt_tip_input_usdt_add);
            binding.etSelectorRightAdd.setHint(R.string.txt_tip_input_usdt_add_hint);
            binding.ivSelectorTipAdd.setVisibility(View.INVISIBLE);
            binding.ivSelectorTipAdd.setBackgroundResource(R.mipmap.cm_ic_hint_red);
            binding.tvSelectorTipAdd.setVisibility(View.INVISIBLE);
            binding.tvSelectorTipAdd.setText(R.string.txt_tip_input_usdt_collection_name_err);

        }
    }

    /**
     * 付款账户检测//付款錢包地址
     */
    private boolean checkInputPaymentAccount(int feedbackType) {

        if (TextUtils.isEmpty(binding.etSelectorRightPaymentAccount.getText().toString())) //付款账户
        {
            binding.ivSelectorTipPayWayAccount.setBackgroundResource(R.mipmap.cm_ic_hint_red);
            if (feedbackType == 1) //微信
            {
                binding.tvSelectorTipPaywayAccount.setText(R.string.txt_tip_input_wechat_pay_err);
            } else if (feedbackType == 2) //虚拟币
            {
                if (TextUtils.isEmpty(binding.etSelectorRightPaymentName.getText().toString())) {
                    binding.llPaymentAccount.setVisibility(View.GONE);
                    binding.ivSelectorTipPaywayName.setVisibility(View.VISIBLE);
                    binding.ivSelectorTipPayWayAccount.setVisibility(View.VISIBLE);
                    binding.tvSelectorTipPaywayAccount.setVisibility(View.VISIBLE);
                    binding.tvSelectorTipPaywayAccount.setText(R.string.txt_tip_input_usdt_pay_err);
                    binding.ivSelectorTipPaywayName.setBackgroundResource(R.mipmap.cm_ic_hint_red);
                    binding.tvSelectorTipPaywayName.setVisibility(View.VISIBLE);
                    binding.tvSelectorTipPaywayName.setText(R.string.txt_tip_input_usdt_pay_err);
                    return false;
                }

            }
            return false;
        } else return true;
    }

    /**
     * 检测付款人//协议
     */
    private boolean checkInputPaymentName(int feedbackType) {
        if (TextUtils.isEmpty(binding.etSelectorRightPaymentName.getText().toString())) //付款人
        {
            if (feedbackType == 1) {
                binding.ivSelectorTipPaywayName.setBackgroundResource(R.mipmap.cm_ic_hint_red);
                binding.tvSelectorTipPaywayName.setText(R.string.txt_tip_input_pay_wechat_name_err);
            } else if (feedbackType == 2) {

            }

            return false;
        } else return true;
    }

    /**
     * 检测收款人姓名/
     */
    private boolean checkInputCollectionName(int feedbackType) {
        if (TextUtils.isEmpty(binding.etSelectorRightCollectiontName.getText().toString())) //收款人
        {
            if (feedbackType == 1) {
                binding.ivSelectorTipCollectionName.setBackgroundResource(R.mipmap.cm_ic_hint_red);
                binding.tvSelectorTipCollectiontName.setText(R.string.txt_tip_input_collection_wechat_name_err);
            }
            return false;
        } else return true;
    }

    /**
     * 检查存款金额/虚拟币数量
     */
    private boolean checkInputSaveName(int feedbackType) {
        if (TextUtils.isEmpty(binding.etSelectorRightSavename.getText().toString()))//存款金额
        {
            binding.ivSelectorTipSavename.setBackgroundResource(R.mipmap.cm_ic_hint_red);
            binding.tvSelectorTipSavename.setText(R.string.txt_tip_input_wechat_save_number_err);
            return false;
        }
        return true;
    }

    /**
     * 检查第三方订单
     */
    private boolean checkInputThirdOrder() {
        if (TextUtils.isEmpty(binding.tvSelectorRightThree.getText().toString()))//第三方订单号
        {
            binding.ivSelectorTipThree.setVisibility(View.VISIBLE);
            binding.ivSelectorTipThree.setBackgroundResource(R.mipmap.cm_ic_hint_red);
            binding.tvSelectorTipThree.setVisibility(View.VISIBLE);
            binding.tvSelectorTipThree.setText(R.string.txt_tip_input_wechat_other_order_err);
            return false;
        } else return true;
    }

    private void nextCheckInputWithPayway(int payType) {
        //1 支付宝 微信 2 虚拟币

        if (checkInputPaymentAccount(payType) && checkInputPaymentName(payType) &&
                checkInputCollectionName(payType) && checkInputSaveName(payType) &&
                checkInputThirdOrder() && imageSelector) {
            //提交图片
            uploadImage(imageRealPathString);
        } else if (!imageSelector) {
            CfLog.i("未上传图片");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            }
            ToastUtils.show("请上传充值明细截图"  , Toast.LENGTH_SHORT , 2);
        }
    }

    /**
     * 注册Editext监听事件
     */
    private void initEditListener() {
        TextChange textChange = new TextChange();
        textChange.setArrayList(binding.etSelectorRightPaymentAccount);//付款账户
        binding.etSelectorRightPaymentAccount.addTextChangedListener(textChange);

        textChange.setArrayList(binding.etSelectorRightPaymentName);//付款人
        binding.etSelectorRightPaymentName.addTextChangedListener(textChange);

        textChange.setArrayList(binding.etSelectorRightCollectiontName);//收款人
        binding.etSelectorRightCollectiontName.addTextChangedListener(textChange);

        textChange.setArrayList(binding.etSelectorRightSavename);//存款金额
        binding.etSelectorRightSavename.addTextChangedListener(textChange);

        textChange.setArrayList(binding.tvSelectorRightThree);//第三方单号
        binding.tvSelectorRightThree.addTextChangedListener(textChange);
    }

    /**
     * 多个输入框监听
     */
    private class TextChange implements TextWatcher {
        private ArrayList<EditText> arrayList = new ArrayList<>();

        public void setArrayList(EditText editable) {
            this.arrayList.add(editable);
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            for (int i = 0; i < arrayList.size(); i++) {
                EditText e = arrayList.get(i);
                if (e == binding.etSelectorRightPaymentAccount)
                {
                    CfLog.i("binding.etSelectorRightPaymentAccount---");
                    //监听付款账户Ed
                    if (binding.etSelectorRightPaymentAccount.getText().length() > 0) {
                        binding.ivSelectorTipPayWayAccount.setVisibility(View.INVISIBLE);
                        binding.tvSelectorTipPaywayAccount.setVisibility(View.INVISIBLE);
                    } else if (binding.etSelectorRightPaymentAccount.getText().length() == 0) {
                        binding.ivSelectorTipPayWayAccount.setVisibility(View.VISIBLE);
                        binding.tvSelectorTipPaywayAccount.setVisibility(View.VISIBLE);
                    }
                }
                //付款人姓名
                else if (e == binding.etSelectorRightPaymentName) {
                    CfLog.i("binding.etSelectorRightPaymentName---");
                    if (binding.etSelectorRightPaymentName.getText().length() > 0) {
                        binding.ivSelectorTipPaywayName.setVisibility(View.INVISIBLE);
                        binding.tvSelectorTipPaywayName.setVisibility(View.INVISIBLE);

                    } else if (binding.etSelectorRightPaymentName.getText().length() == 0) {
                        binding.ivSelectorTipPaywayName.setVisibility(View.VISIBLE);
                        binding.tvSelectorTipPaywayName.setVisibility(View.VISIBLE);
                    }
                }
                //收款人姓名
                else if (e == binding.etSelectorRightCollectiontName) {
                    if (feedbackType == 1) //wechat反馈模式
                    {
                        if (binding.etSelectorRightCollectiontName.getText().length() > 0) {
                            binding.ivSelectorTipCollectionName.setVisibility(View.INVISIBLE);
                            binding.tvSelectorTipCollectiontName.setVisibility(View.INVISIBLE);
                        } else if (binding.etSelectorRightCollectiontName.getText().length() == 0) {
                            binding.ivSelectorTipCollectionName.setVisibility(View.VISIBLE);
                            binding.tvSelectorTipCollectiontName.setVisibility(View.VISIBLE);
                        }
                    } else if (feedbackType == 2) {

                    }

                } else if (e == binding.etSelectorRightSavename) {
                    if (binding.etSelectorRightSavename.getText().length() > 0) {
                        binding.ivSelectorTipSavename.setVisibility(View.INVISIBLE);
                        binding.tvSelectorTipSavename.setVisibility(View.INVISIBLE);
                    } else if (binding.etSelectorRightSavename.getText().length() == 0) {
                        binding.ivSelectorTipSavename.setVisibility(View.VISIBLE);
                        binding.tvSelectorTipSavename.setVisibility(View.VISIBLE);
                    }
                }
                //第三方单号
                else if (e == binding.tvSelectorRightThree) {

                    if (binding.tvSelectorRightThree.getText().length() > 0) {
                        binding.ivSelectorTipThree.setVisibility(View.INVISIBLE);
                        binding.tvSelectorTipThree.setVisibility(View.INVISIBLE);
                    } else if (binding.tvSelectorRightThree.getText().length() == 0) {
                        binding.ivSelectorTipThree.setVisibility(View.VISIBLE);
                        binding.tvSelectorTipThree.setVisibility(View.VISIBLE);
                    }
                }
            }

        }
    }

    /**
     * 图片选择
     */
    private void gotoSelectMedia() {
        PictureSelector.create(getActivity()).openGallery(SelectMimeType.ofImage()).isDisplayCamera(false).
                setMaxSelectNum(1).setImageEngine(GlideEngine.createGlideEngine()).forResult(new OnResultCallbackListener<LocalMedia>() {
                    @Override
                    public void onResult(ArrayList<LocalMedia> result) {
                        if (result != null) {
                            for (int i = 0; i < result.size(); i++) {
                                imageRealPathString = result.get(i).getRealPath();
                                File imageRealPath = new File(imageRealPathString);

                                if (imageRealPath.exists()) {
                                    CfLog.i("获取图片地址Base64 ===== " + ImageUploadUtil.bitmapToString(imageRealPathString));
                                    Bitmap bitmap = BitmapFactory.decodeFile(imageRealPathString);
                                    binding.ivSelectorAdd.setVisibility(View.GONE);
                                    binding.ivSelectorTipImage.setVisibility(View.VISIBLE);
                                    binding.ivSelectorTipImage.setImageBitmap(bitmap);
                                    imageSelector = true;//向界面设置了选中图片
                                } else {
                                    CfLog.i("获取图片地址不存在是 ====== " + result.get(i).getRealPath());
                                }

                                if (PictureMimeType.isContent(imageRealPathString)) {
                                    imageUri = Uri.parse(imageRealPathString);
                                } else {
                                    imageUri = Uri.fromFile(new File(imageRealPathString));
                                }
                                CfLog.i("获取图片地址是 uri ====== " + imageUri);
                            }
                        }
                    }

                    @Override
                    public void onCancel() {

                    }
                });
    }

    /**
     * 上传图片
     */
    private void uploadImage(String imageFile) {
        HashMap<String, String> uploadMap = new HashMap<String, String>();
        uploadMap.put("type", "m4");
        uploadMap.put("nonce", UuidUtil.getID16());//传入UUID);
        uploadMap.put("filetype", "image/png");
        uploadMap.put("filedata", ImageUploadUtil.bitmapToString(imageFile));
        viewModel.feedbackImageUp(uploadMap);
    }

    /**
     * 提交
     *
     * @param userPic
     */
    private void feedbackAdd(String userPic) {
        if (feedbackType == 1)//微信
        {
            HashMap<String, String> uploadMap = new HashMap<String, String>();
            uploadMap.put("nonce", UuidUtil.getID16());//传入UUID);

            uploadMap.put("userpay_mode", "1");//微信
            uploadMap.put("userpay_bank", binding.etSelectorRightPaymentAccount.getText().toString());//付款账户
            uploadMap.put("userpay_amount", binding.etSelectorRightSavename.getText().toString());//存款金额
            uploadMap.put("userpay_time", binding.tvSelectorRightSavetime.getText().toString());//存款准确时间
            uploadMap.put("third_orderid", binding.tvSelectorRightThree.getText().toString()); //三方订单号
            uploadMap.put("userpay_picture", userPic); //上传图片三方地址
            String receiverBank = "";
            for (int i = 0; i < bankInfoArrayList.size(); i++) {
                if (bankInfoArrayList.get(i).name.equals(binding.tvSelectorRightPayway.getText().toString())) {
                    receiverBank = String.valueOf(bankInfoArrayList.get(i).id);
                }
            }
            uploadMap.put("receive_bank", receiverBank);//支付渠道
            uploadMap.put("userpay_virtual_protocol", "1");//用户支付协议 微信默认为1
            uploadMap.put("userpay_name", binding.etSelectorRightPaymentAccount.getText().toString()); //付款人
            uploadMap.put("receive_name", binding.etSelectorRightCollectiontName.getText().toString());//收款人

            CfLog.i("微信状态提交反馈  " + uploadMap.toString());

            viewModel.feedbackCustomAdd(uploadMap);
        } else {
            HashMap<String, String> uploadMap = new HashMap<String, String>();
            uploadMap.put("nonce", UuidUtil.getID16());//传入UUID);

            uploadMap.put("userpay_mode", "2");//虚拟币
            uploadMap.put("userpay_bank", binding.etSelectorRightPaymentAccount.getText().toString());//付款钱包地址
            uploadMap.put("userpay_amount", binding.etSelectorRightSavename.getText().toString());//存款金额
            uploadMap.put("userpay_time", binding.tvSelectorRightSavetime.getText().toString());//存款准确时间
            uploadMap.put("third_orderid", binding.tvSelectorRightThree.getText().toString()); //三方订单号
            uploadMap.put("userpay_picture", userPic); //上传图片三方地址
            String receiverBank = "";
            for (int i = 0; i < bankInfoArrayList.size(); i++) {
                if (bankInfoArrayList.get(i).name.equals(binding.tvSelectorRightPayway.getText().toString())) {
                    receiverBank = String.valueOf(bankInfoArrayList.get(i).id);
                }
            }
            uploadMap.put("receive_bank", receiverBank);//支付渠道
            uploadMap.put("userpay_virtual_protocol", "2");//用户支付协议 虚拟币默认为1
            uploadMap.put("userpay_name", binding.etSelectorRightPaymentAccount.getText().toString()); //付款人
            uploadMap.put("receive_name", binding.etSelectorRightCollectiontName.getText().toString());//收款钱包地址

            CfLog.i("虚拟币状态提交反馈  " + uploadMap.toString());

            viewModel.feedbackCustomAdd(uploadMap);
        }

    }
}