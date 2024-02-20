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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
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
import com.xtree.base.utils.ImageUploadUtil;
import com.xtree.base.utils.StringUtils;
import com.xtree.base.utils.TagUtils;
import com.xtree.base.utils.TimeUtils;
import com.xtree.base.utils.UuidUtil;
import com.xtree.base.vo.ProfileVo;
import com.xtree.base.widget.BrowserActivity;
import com.xtree.base.widget.BrowserDialog;
import com.xtree.base.widget.DateTimePickerDialog;
import com.xtree.base.widget.GlideEngine;
import com.xtree.base.widget.ListDialog;
import com.xtree.base.widget.LoadingDialog;
import com.xtree.base.widget.MsgDialog;
import com.xtree.recharge.BR;
import com.xtree.recharge.R;

import com.xtree.recharge.databinding.FragmentFeedbackEditBinding;
import com.xtree.recharge.ui.viewmodel.RechargeViewModel;
import com.xtree.recharge.ui.viewmodel.factory.AppViewModelFactory;
import com.xtree.recharge.vo.FeedbackCheckVo;

import com.xtree.recharge.vo.FeedbackDep;
import com.xtree.recharge.vo.FeedbackImageUploadVo;
import com.xtree.recharge.vo.FeedbackProtocolInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import me.xtree.mvvmhabit.base.BaseFragment;
import me.xtree.mvvmhabit.utils.SPUtils;
import me.xtree.mvvmhabit.utils.ToastUtils;
import project.tqyb.com.library_res.databinding.ItemTextBinding;

/**
 * 充值-反馈修改页面
 */
@Route(path = RouterFragmentPath.Recharge.PAGER_RECHARGE_FEEDBACK_EDIT)
public class FeedbackEditFragment extends BaseFragment<FragmentFeedbackEditBinding, RechargeViewModel> implements DateTimePickerDialog.ICallBack {
    private String feedbackId;
    private String receive_bank;//支付渠道ID
    private int feedbackType = 1; //1 微信 2 usdt
    private String imageRealPathString;//选择的图片地址
    private boolean imageSelector = false;//是否已选择图片
    private Uri imageUri;
    private FeedbackCheckVo feedbackCheckVo;//进入反馈页面回去的数据（包含有页面数据、支付渠道）
    private FeedbackCheckVo.FeedbackCheckInfo checkInfo;//反馈回来的页面数据

    public FeedbackEditFragment() {

    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_feedback_edit;
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
    public void initView() {

        binding.ivwBack.setOnClickListener(v -> getActivity().finish());
        binding.llRoot.setOnClickListener(v -> hideKeyBoard());
        //提交
        binding.ivwNext.setOnClickListener(v -> {
            nextCheckInputWithPayWay(feedbackType);
        });

        String json = SPUtils.getInstance().getString(SPKeyGlobal.HOME_PROFILE);
        ProfileVo mProfileVo = new Gson().fromJson(json, ProfileVo.class);

        binding.tvUsername.setText(mProfileVo.username);
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
        binding.llRemittanceScreenshot.setOnClickListener(v -> {
            gotoSelectMedia();
        });
        //注册Edittex监听
        initEditListener();
        //时间选择器
        binding.tvDepositTime.setOnClickListener(v -> {
            showTimeSelector();
        });
        //点击支付渠道
        binding.tvwPaymentChannel.setOnClickListener(v -> {
            showPayReceive(feedbackCheckVo);
        });
        //虚拟币反馈时候 协议选择点击
        binding.edProtocol.setOnClickListener(v -> {
            showVirtualProtocol(feedbackCheckVo);
        });
        //虚拟币 协议
        binding.llPaymentWalletProtocol.setOnClickListener(v -> {
            showVirtualProtocol(feedbackCheckVo);
        });
    }

    @Override
    public void initData() {
        //获取页面数据
        //从上级页面穿过的feedbackId
        if (getArguments() != null) {
            feedbackId = getArguments().getString("id");
        }
        CfLog.i("从上级页面穿过的id = " + feedbackId);
        HashMap<String, String> map = new HashMap<>();
        map.put("id", feedbackId);
        map.put("client", "m");
        LoadingDialog.show(getContext());
        viewModel.getEditFeedback(map);
    }

    @Override
    public void initViewObservable() {
        //获取进入反馈页面回去的数据
        viewModel.feedbackCheckVoSingleLiveData.observe(this, o -> {
            feedbackCheckVo = o;
            CfLog.e("feedbackCheckVoSingleLiveData = " + o.toString());
            for (int i = 0; i < feedbackCheckVo.list.size(); i++) {
                FeedbackCheckVo.FeedbackCheckInfo info = feedbackCheckVo.list.get(i);
                if (feedbackId.equals(String.valueOf(info.id))) {
                    checkInfo = info;
                }
                CfLog.e("feedbackCheckVo.list = " + feedbackCheckVo.list.get(i).toString());
            }

            referFeedbackUI(checkInfo);
        });

        //获取上传图片后的 服务器返回的图片地址
        viewModel.imageUploadVoSingleLiveData.observe(this, o -> {
            FeedbackImageUploadVo imageUploadVo = o;
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
     * 显示支付渠道
     */
    private void showPayReceive(FeedbackCheckVo vo) {
        if (vo == null || vo.banksInfo == null) {
            return;
        }

        CachedAutoRefreshAdapter adapter = new CachedAutoRefreshAdapter<FeedbackCheckVo.FeedbackBankInfo>() {
            @NonNull
            @Override
            public CacheViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                CacheViewHolder holder = new CacheViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_text, parent, false));
                return holder;
            }

            @Override
            public void onBindViewHolder(@NonNull CacheViewHolder holder, int position) {
                binding2 = ItemTextBinding.bind(holder.itemView);
                FeedbackCheckVo.FeedbackBankInfo voBankInfo = get(position);
                binding2.tvwTitle.setText(voBankInfo.name);
                binding2.tvwTitle.setOnClickListener(v -> {
                    CfLog.i("****** " + voBankInfo.toString());
                    binding.tvwPaymentChannel.setText(voBankInfo.name);
                    receive_bank = String.valueOf(voBankInfo.id);
                    ppw.dismiss();
                });
            }

        };

        adapter.addAll(feedbackCheckVo.banksInfo);
        ppw = new XPopup.Builder(getContext()).asCustom(new ListDialog(getContext(), "请选择支付渠道", adapter, 80));
        ppw.show();

    }

    /**
     * 显示虚拟币协议
     */
    private void showVirtualProtocol(FeedbackCheckVo vo) {
        if (vo == null || feedbackCheckVo.protocolInfo == null || feedbackCheckVo.protocolInfo.size() == 0) {
            return;
        }
        CachedAutoRefreshAdapter adapter = new CachedAutoRefreshAdapter<FeedbackProtocolInfo>() {
            @NonNull
            @Override
            public CacheViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                CacheViewHolder holder = new CacheViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_text, parent, false));
                return holder;
            }

            @Override
            public void onBindViewHolder(@NonNull CacheViewHolder holder, int position) {
                binding2 = ItemTextBinding.bind(holder.itemView);
                FeedbackProtocolInfo voProtocolInfo = get(position);

                binding2.tvwTitle.setText(voProtocolInfo.name);
                binding2.tvwTitle.setOnClickListener(v -> {
                    CfLog.i("****** " + voProtocolInfo.toString());
                    binding.edProtocol.setText(voProtocolInfo.name);
                    ppw.dismiss();
                });
            }

        };
        adapter.addAll(feedbackCheckVo.protocolInfo);
        ppw = new XPopup.Builder(getContext()).asCustom(new ListDialog(getContext(), "请选择协议", adapter, 80));
        ppw.show();

    }

    /**
     * 显示存款时间选择器
     */
    private void showTimeSelector() {
        new XPopup.Builder(getContext()).asCustom(DateTimePickerDialog.newInstance(getContext(), "*存款准确时间:", 6, "yyyy-MM-dd HH:mm:ss", this)).show();
    }

    /**
     * DateTimePickerDialog选中回调
     *
     * @param date
     */
    @Override
    public void setDate(String date) {
        binding.tvDepositTime.setText(date);
    }

    /**
     * 刷新UI
     */
    private void referFeedbackUI(FeedbackCheckVo.FeedbackCheckInfo info) {
        if (info.userpay_mode.equals("2")) {//虚拟币
            feedbackType = 2;
            binding.tvwPaymentMethod.setText(R.string.txt_tip_select_pay_usdt);
            binding.llPaymentAccount.setVisibility(View.GONE);//付款账户隐藏
            binding.llPaymentWalletAddress.setVisibility(View.VISIBLE);//付款钱包地址
            binding.etPaymentAddress.setText(info.userpay_bank);
            binding.llPaymentName.setVisibility(View.GONE);//付款人姓名
            binding.llCollectionName.setVisibility(View.GONE);//收款人姓名
            binding.llPaymentWalletProtocol.setVisibility(View.VISIBLE);//协议
            for (int i = 0; i < feedbackCheckVo.protocolInfo.size(); i++) {
                if (info.receive_banknum.equals(String.valueOf(feedbackCheckVo.protocolInfo.get(i).id))) {
                    binding.edProtocol.setText(feedbackCheckVo.protocolInfo.get(i).name);
                }
            }
            binding.llDepositAmount.setVisibility(View.GONE);//存款金额
            binding.llVirtualAmount.setVisibility(View.VISIBLE);//虚拟币数量
            binding.etVirtualAmount.setText(info.userpay_amount);
            binding.tvDepositTime.setText(info.add_time);//存款准确时间
            binding.edThirdOrderNumber.setText(info.third_orderid);//第三方单号

            for (int i = 0; i < feedbackCheckVo.banksInfo.size(); i++) {
                if (info.receive_bank.equals(String.valueOf(feedbackCheckVo.banksInfo.get(i).id))) {
                    binding.tvwPaymentChannel.setText(feedbackCheckVo.banksInfo.get(i).name);//支付渠道
                    receive_bank = info.receive_bank;
                }
            }
            binding.llCollectionWalletAddress.setVisibility(View.VISIBLE);//收款钱包地址
            binding.etCollectionWalletAddress.setText(info.receive_banknum);

        } else if (info.userpay_mode.equals("1")) {
            feedbackType = 1;
            binding.tvwPaymentMethod.setText(R.string.txt_tip_select_pay_wechat);
            binding.llPaymentAccount.setVisibility(View.VISIBLE);//付款账户
            binding.llPaymentWalletAddress.setVisibility(View.GONE);//付款钱包地址
            binding.etPaymentAccount.setText(info.userpay_bank);//付款账户
            binding.etPaymentName.setText(info.userpay_name);//付款人
            binding.etCollectionName.setText(info.receive_name);//收款人
            binding.etDepositAmount.setText(info.userpay_amount);//存款金额
            binding.tvDepositTime.setText(info.add_time);//存款准确时间
            binding.edThirdOrderNumber.setText(info.third_orderid);//第三方单号

            for (int i = 0; i < feedbackCheckVo.banksInfo.size(); i++) {
                if (info.receive_bank.equals(String.valueOf(feedbackCheckVo.banksInfo.get(i).id))) {
                    binding.tvwPaymentChannel.setText(feedbackCheckVo.banksInfo.get(i).name);//支付渠道
                    receive_bank = info.receive_bank;
                }
            }
            binding.llCollectionWalletAddress.setVisibility(View.GONE);//收款钱包地址
            binding.etCollectionWalletAddress.setText(info.receive_banknum);
        }
        //下载图片
        loadImage(info.userpay_picture);
    }

    /**
     * 付款账户检测//付款錢包地址
     */
    private boolean checkInputPaymentAccount(int feedbackType) {

        if (feedbackType == 1 && TextUtils.isEmpty(binding.etPaymentAccount.getText().toString().trim())) //微信
        {
            binding.ivPaymentAccount.setBackgroundResource(R.mipmap.cm_ic_hint_red);
            binding.tvPaymentAccountInfo.setText(R.string.txt_tip_input_wechat_pay_err);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                binding.tvPaymentAccountInfo.setTextColor(getContext().getColor(R.color.red));
            }
            return true;
        } else if (isEmpty(binding.etPaymentAddress)) {
            binding.ivPaymentAddress.setBackgroundResource(R.mipmap.cm_ic_hint_red);
            binding.tvPaymentAddressInfo.setText(R.string.txt_tip_input_usdt_pay_err);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                binding.tvPaymentAddressInfo.setTextColor(getContext().getColor(R.color.red));
            }
            return true;
        }

        return false;
    }

    /**
     * 检测付款人姓名
     */
    private boolean checkInputPaymentName() {
        CfLog.i("checkInputPaymentName 付款人姓名 ： " + binding.etPaymentName.getText().toString().trim());
        if (TextUtils.isEmpty(binding.etPaymentName.getText().toString().trim())) //付款人姓名
        {
            binding.ivPaymentNameInfo.setBackgroundResource(R.mipmap.cm_ic_hint_red);
            binding.tvPaymentNameInfo.setText(R.string.txt_tip_input_pay_wechat_name_err);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                binding.tvPaymentNameInfo.setTextColor(getContext().getColor(R.color.red));
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * 检测收款人姓名/
     */
    private boolean checkInputCollectionName() {
        if (isEmpty(binding.etCollectionName)) //收款人
        {
            binding.ivCollectionName.setBackgroundResource(R.mipmap.cm_ic_hint_red);
            binding.tvtCollectionName.setText(R.string.txt_tip_input_collection_wechat_name_err);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                binding.tvtCollectionName.setTextColor(getContext().getColor(R.color.red));
            }

            return true;
        } else return false;
    }

    /**
     * 检查存款金额/虚拟币数量
     */
    private boolean checkInputSaveName(int feedbackType) {
        if (feedbackType == 1 && isEmpty(binding.etDepositAmount))//微信存款金额
        {
            binding.ivDepositAmount.setBackgroundResource(R.mipmap.cm_ic_hint_red);
            binding.tvDepositAmountInfo.setText(R.string.txt_tip_input_wechat_save_number_err);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                binding.tvDepositAmountInfo.setTextColor(getContext().getColor(R.color.red));
            }
            return true;
        }
        //虚拟币
        if (isEmpty(binding.etVirtualAmount))//存款金额
        {
            binding.ivVirtualAmount.setBackgroundResource(R.mipmap.cm_ic_hint_red);
            binding.tvVirtualAmountInfo.setText(R.string.txt_tip_input_wechat_save_number_err);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                binding.tvVirtualAmountInfo.setTextColor(getContext().getColor(R.color.red));
            }
            return true;
        }
        return false;
    }

    /**
     * 检测存款时间
     */
    private boolean checkInputTime() {
        if (isEmpty(binding.tvDepositTime)) {
            binding.ivDepositTime.setBackgroundResource(R.mipmap.cm_ic_hint_red);
            binding.ivDepositTime.setVisibility(View.VISIBLE);
            binding.tvDepositTimeInfo.setVisibility(View.VISIBLE);
            binding.tvDepositTimeInfo.setText(R.string.txt_tip_input_usdt_save_number_time_err);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                binding.tvDepositTimeInfo.setTextColor(getContext().getColor(R.color.red));
            }
            return true;
        }
        return false;
    }

    /**
     * 检查第三方订单
     */
    private boolean checkInputThirdOrder() {
        if (isEmpty(binding.edThirdOrderNumber))//第三方订单号
        {
            binding.ivThirdOrderNumber.setVisibility(View.VISIBLE);
            binding.ivThirdOrderNumber.setBackgroundResource(R.mipmap.cm_ic_hint_red);
            binding.tvThirdOrderNumberInfo.setVisibility(View.VISIBLE);
            binding.tvThirdOrderNumberInfo.setText(R.string.txt_tip_input_wechat_other_order_err);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                binding.tvThirdOrderNumberInfo.setTextColor(getContext().getColor(R.color.red));
            }
            return true;
        } else return false;
    }

    /**
     * 检测支付渠道
     */
    private boolean checkInputPayChannel() {
        if (isEmpty(binding.tvwPaymentChannel)) {
            binding.ivPaymentChannel.setVisibility(View.VISIBLE);
            binding.ivPaymentChannel.setBackgroundResource(R.mipmap.cm_ic_hint_red);
            binding.tvPaymentChannelInfo.setVisibility(View.VISIBLE);
            binding.tvPaymentChannelInfo.setText(R.string.txt_tip_input_wechat_select_pay_way);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                binding.tvPaymentChannelInfo.setTextColor(getContext().getColor(R.color.red));
            }
            return true;
        }
        return false;
    }

    /**
     * 检测协议
     */
    private boolean checkAgreement() {
        if (TextUtils.isEmpty(binding.edProtocol.getText().toString())) {
            return true;
        } else return false;
    }

    private void nextCheckInputWithPayWay(int payType) {
        CfLog.i("nextCheckInputWithPayWay payType = " + payType);
        //1 支付宝 微信 2 虚拟币
        if (payType == 1) {
            if (TextUtils.isEmpty(binding.etPaymentAccount.getText().toString().trim())) {
                String t = binding.etPaymentAccount.getText().toString().trim();
                CfLog.i("nextCheckInputWithPayWay = " + t);
                ToastUtils.show(getContext().getString(R.string.txt_tip_input_wechat_pay_err), ToastUtils.ShowType.Fail);
            } else if (checkInputPaymentName()) {
                ToastUtils.show(getContext().getString(R.string.txt_tip_input_pay_wechat_name_err), ToastUtils.ShowType.Fail);
            } else if (checkInputCollectionName()) {
                ToastUtils.show(getContext().getString(R.string.txt_tip_input_collection_wechat_name_err), ToastUtils.ShowType.Fail);
            } else if (TextUtils.isEmpty(binding.etDepositAmount.getText().toString().trim())) {
                ToastUtils.show(getContext().getString(R.string.txt_tip_input_wechat_save_number_err), ToastUtils.ShowType.Fail);
            } else if (checkInputTime()) {
                ToastUtils.show(getContext().getString(R.string.txt_tip_input_usdt_save_number_time_err), ToastUtils.ShowType.Fail);
            } else if (checkInputThirdOrder()) {
                ToastUtils.show(getContext().getString(R.string.txt_tip_input_wechat_other_order_err), ToastUtils.ShowType.Fail);
            } else if (TextUtils.isEmpty(receive_bank)) {
                CfLog.i("tvwPaymentChannel = " + binding.tvwPaymentChannel.getText().toString().trim() + " ||receive_bank = " + receive_bank);
                ToastUtils.show(getContext().getString(R.string.txt_tip_input_wechat_pay_way_err), ToastUtils.ShowType.Fail);

            } else if (!imageSelector) {
                //未更换图片
                feedbackAdd(checkInfo.userpay_picture);
            } else {
                //提交图片
                uploadImage(imageRealPathString);
            }

        } else if (payType == 2) {
            if (TextUtils.isEmpty(binding.etPaymentAddress.getText().toString().trim())) {
                ToastUtils.show(getContext().getString(R.string.txt_tip_input_usdt_pay_err), ToastUtils.ShowType.Fail);
            } else if (checkAgreement()) {
                ToastUtils.show(getContext().getString(R.string.txt_tip_input_usdt_payment_agreement), ToastUtils.ShowType.Fail);
            } else if (TextUtils.isEmpty(binding.etVirtualAmount.getText().toString().trim())) {
                ToastUtils.show(getContext().getString(R.string.txt_tip_input_usdt_numb_error), ToastUtils.ShowType.Fail);
            } else if (checkInputTime()) {
                ToastUtils.show(getContext().getString(R.string.txt_tip_input_usdt_save_number_time_err), ToastUtils.ShowType.Fail);
            } else if (checkInputThirdOrder()) {
                ToastUtils.show(getContext().getString(R.string.txt_tip_input_wechat_other_order_err), ToastUtils.ShowType.Fail);
            } else if (checkInputPayChannel()) {
                ToastUtils.show(getContext().getString(R.string.txt_tip_input_wechat_pay_way_err), ToastUtils.ShowType.Fail);
            } else if (TextUtils.isEmpty(binding.etCollectionWalletAddress.getText().toString().trim())) {
                ToastUtils.show(getContext().getString(R.string.txt_tip_input_usdt_pay_err), ToastUtils.ShowType.Fail);
            } else if (!imageSelector) {
                //未更换图片
                feedbackAdd(checkInfo.userpay_picture);
            } else {
                //提交图片
                uploadImage(imageRealPathString);
            }
        }

    }

    /**
     * 注册Editext监听事件
     */
    private void initEditListener() {
        TextChange textChange = new TextChange();
        textChange.setArrayList(binding.etPaymentAccount);//付款账户
        binding.etPaymentAccount.addTextChangedListener(textChange);
        //付款人姓名
        textChange.setArrayList(binding.etPaymentName);
        binding.etPaymentName.addTextChangedListener(textChange);
        //收款人
        textChange.setArrayList(binding.etCollectionName);
        binding.etCollectionName.addTextChangedListener(textChange);
        //存款金额
        textChange.setArrayList(binding.etDepositAmount);
        binding.etDepositAmount.addTextChangedListener(textChange);
        //第三方单号
        textChange.setArrayList(binding.edThirdOrderNumber);
        binding.edThirdOrderNumber.addTextChangedListener(textChange);
        //准确时间
        binding.tvDepositTime.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString())) {
                    binding.ivDepositTime.setVisibility(View.VISIBLE);
                    binding.tvDepositTimeInfo.setVisibility(View.VISIBLE);
                } else {
                    binding.ivDepositTime.setVisibility(View.INVISIBLE);
                    binding.tvDepositTimeInfo.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    /**
     * 判断Text是否为空
     */
    private boolean isEmpty(TextView textView) {
        if (textView.getText().toString().trim().isEmpty()) {
            return true;
        }

        return false;
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
                if (e == binding.etPaymentAccount) //付款账户
                {
                    if (isEmpty(binding.etPaymentAccount)) {
                        binding.ivPaymentAccount.setVisibility(View.VISIBLE);
                        binding.tvPaymentAccountInfo.setVisibility(View.VISIBLE);
                    } else {
                        binding.ivPaymentAccount.setVisibility(View.INVISIBLE);
                        binding.tvPaymentAccountInfo.setVisibility(View.INVISIBLE);
                    }

                }
                //付款人姓名
                else if (e == binding.etPaymentName) {
                    if (isEmpty(binding.etPaymentName)) {
                        binding.ivPaymentNameInfo.setVisibility(View.VISIBLE);
                        binding.tvPaymentNameInfo.setVisibility(View.VISIBLE);
                    } else {
                        binding.ivPaymentNameInfo.setVisibility(View.INVISIBLE);
                        binding.tvPaymentNameInfo.setVisibility(View.INVISIBLE);
                    }
                }
                //收款人姓名
                else if (e == binding.etCollectionName) {
                    if (isEmpty(binding.etCollectionName)) {
                        binding.ivCollectionName.setVisibility(View.VISIBLE);
                        binding.tvtCollectionName.setVisibility(View.VISIBLE);
                    } else {
                        binding.ivCollectionName.setVisibility(View.INVISIBLE);
                        binding.tvtCollectionName.setVisibility(View.INVISIBLE);
                    }
                }
                //存款金额
                else if (e == binding.etDepositAmount) {
                    if (isEmpty(binding.etDepositAmount)) {
                        binding.ivDepositAmount.setVisibility(View.VISIBLE);
                        binding.tvDepositAmountInfo.setVisibility(View.VISIBLE);
                    } else {
                        binding.ivDepositAmount.setVisibility(View.INVISIBLE);
                        binding.tvDepositAmountInfo.setVisibility(View.INVISIBLE);
                    }
                }
                //第三方订单号
                else if (e == binding.edThirdOrderNumber) {
                    if (isEmpty(binding.edThirdOrderNumber)) {
                        binding.ivThirdOrderNumber.setVisibility(View.VISIBLE);
                        binding.tvThirdOrderNumberInfo.setVisibility(View.VISIBLE);
                    } else {
                        binding.ivThirdOrderNumber.setVisibility(View.INVISIBLE);
                        binding.tvThirdOrderNumberInfo.setVisibility(View.INVISIBLE);
                    }
                }
            }

        }
    }

    /**
     * 图片选择
     */
    private void gotoSelectMedia() {
        PictureSelector.create(getActivity()).openGallery(SelectMimeType.ofImage()).isDisplayCamera(false).setMaxSelectNum(1).setImageEngine(GlideEngine.createGlideEngine()).forResult(new OnResultCallbackListener<LocalMedia>() {
            @Override
            public void onResult(ArrayList<LocalMedia> result) {
                if (result != null) {
                    for (int i = 0; i < result.size(); i++) {
                        imageRealPathString = result.get(i).getRealPath();
                        File imageRealPath = new File(imageRealPathString);

                        if (imageRealPath.exists()) {
                            CfLog.i("获取图片地址Base64 ===== " + ImageUploadUtil.bitmapToString(imageRealPathString));
                            Bitmap bitmap = BitmapFactory.decodeFile(imageRealPathString);
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
        HashMap<String, String> uploadMap = new HashMap<String, String>();
        uploadMap.put("nonce", UuidUtil.getID16());//传入UUID
        if (feedbackType == 1)//微信
        {
            uploadMap.put("userpay_mode", "1");//微信
            uploadMap.put("userpay_bank", binding.etPaymentAccount.getText().toString());//付款账户
            uploadMap.put("userpay_amount", binding.etDepositAmount.getText().toString());//存款金额
            uploadMap.put("userpay_time", binding.tvDepositTime.getText().toString());//存款准确时间
            uploadMap.put("third_orderid", binding.edThirdOrderNumber.getText().toString()); //三方订单号
            uploadMap.put("userpay_picture", userPic); //上传图片三方地址
            uploadMap.put("receive_bank", receive_bank);//支付渠道
            uploadMap.put("userpay_virtual_protocol", "1");//用户支付协议 微信默认为1
            uploadMap.put("userpay_name", binding.etPaymentAccount.getText().toString()); //付款人
            uploadMap.put("receive_name", binding.etPaymentName.getText().toString());//收款人
            CfLog.i("微信状态提交反馈  " + uploadMap);

        } else {

            uploadMap.put("userpay_mode", "2");//虚拟币
            uploadMap.put("userpay_bank", binding.etPaymentAddress.getText().toString());//付款钱包地址
            uploadMap.put("userpay_amount", binding.etVirtualAmount.getText().toString());//存款金额
            uploadMap.put("userpay_time", binding.tvDepositTime.getText().toString());//存款准确时间
            uploadMap.put("third_orderid", binding.edThirdOrderNumber.getText().toString()); //三方订单号
            uploadMap.put("userpay_picture", userPic); //上传图片三方地址
            uploadMap.put("receive_bank", receive_bank);//支付渠道
            uploadMap.put("userpay_virtual_protocol", "2");//用户支付协议 虚拟币默认为1
            uploadMap.put("receive_banknum", binding.etCollectionWalletAddress.getText().toString());//收款钱包地址

            CfLog.i("虚拟币状态提交反馈  " + uploadMap);
        }
        LoadingDialog.show(getContext());
        viewModel.feedbackCustomAdd(uploadMap);
    }

    private void loadImage(String imageDownUrl) {
        String imageDownUrls = DomainUtil.getDomain2() + imageDownUrl; //图片地址
        CfLog.i("imageDownUrls ==" + imageDownUrls);

        String cookie = "auth=" + SPUtils.getInstance().getString(SPKeyGlobal.USER_TOKEN) + ";" + SPUtils.getInstance().getString(SPKeyGlobal.USER_SHARE_COOKIE_NAME) + "=" + SPUtils.getInstance().getString(SPKeyGlobal.USER_SHARE_SESSID) + ";";
        cookie = "auth-expires-in=604800; userPasswordCheck=lowPass; " + cookie;
        CfLog.e("cookie: " + cookie);

        GlideUrl glideUrl = new GlideUrl(imageDownUrls, new LazyHeaders.Builder().addHeader("Content-Type", "application/vnd.sc-api.v1.json").addHeader("Authorization", "bearer " + SPUtils.getInstance().getString(SPKeyGlobal.USER_TOKEN)).addHeader("Cookie", cookie).addHeader("UUID", TagUtils.getDeviceId(getContext())).build());
        Glide.with(getContext()).load(glideUrl).placeholder(R.mipmap.ic_loading).error(R.mipmap.me_icon_name).into(binding.ivSelectorTipImage);

    }
}