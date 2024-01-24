package com.xtree.recharge.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.base.utils.CfLog;
import com.xtree.base.utils.DomainUtil;
import com.xtree.base.utils.TagUtils;
import com.xtree.recharge.BR;
import com.xtree.recharge.R;
import com.xtree.recharge.databinding.FragmentFeedbackDetailBinding;
import com.xtree.recharge.ui.viewmodel.RechargeViewModel;
import com.xtree.recharge.ui.viewmodel.factory.AppViewModelFactory;
import com.xtree.recharge.vo.FeedbackCheckVo;
import com.xtree.recharge.vo.FeedbackVo;

import java.util.ArrayList;

import me.xtree.mvvmhabit.base.BaseFragment;
import me.xtree.mvvmhabit.utils.SPUtils;

/**
 * 反馈详情页面
 */
@Route(path = RouterFragmentPath.Recharge.PAGER_RECHARGE_FEEDBACK_DETAIL)
public class FeedbackDetailFragment extends BaseFragment<FragmentFeedbackDetailBinding, RechargeViewModel> {

    //查询反馈页面的开始时间、结束时间
    private String feedbackId;
    private String startTime = "2024-01-17 00:00";
    private String endTime = "2024-01-17 23:59";
    private FeedbackVo feedbackVo;//获取反馈页面的配置信息 包括支付渠道、虚拟币协议

    private ArrayList<FeedbackVo.FeedbackBankInfo> bankInfoList = new ArrayList<>();//支付渠道
    private ArrayList<FeedbackVo.FeedbackProtocolInfo> protocolInfoArrayList = new ArrayList<>();//虚拟币协议
    private FeedbackCheckVo feedbackCheckVo;//获取反馈信息页面详情

    @Override
    public void initView() {
        binding.ivwBack.setOnClickListener(v -> getActivity().finish());
        binding.ivwNext.setOnClickListener(v -> getActivity().finish());
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_feedback_detail;
    }

    @Override
    public RechargeViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getActivity().getApplication());
        return new ViewModelProvider(this, factory).get(RechargeViewModel.class);
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initData() {
        //从上级页面穿过的feedbackId
        if (getArguments() != null) {
            feedbackId = getArguments().getString("id");
        }
        CfLog.i("从上级页面穿过的id = " + feedbackId);
        //获取页面数据
        viewModel.getFeedbackCheckInfo(startTime, endTime);
        viewModel.getFeedbackCheckDetailInfo(feedbackId);
    }

    /**
     * 数据回调 数显UI
     */
    @Override
    public void initViewObservable() {
        //获取反馈页面配置
        viewModel.feedbackVoSingleLiveData.observe(this, v -> {
            feedbackVo = v;
            bankInfoList.addAll(feedbackVo.banksInfo);
            protocolInfoArrayList.addAll(feedbackVo.protocolInfo);
        });
        //查询获取反馈页面详情
        viewModel.feedbackCheckVoSingleLiveData.observe(this, v -> {
            feedbackCheckVo = v;
            referUIWithModel(feedbackCheckVo.list.get(0));
        });
    }

    private void referUIWithModel(FeedbackCheckVo.FeedbackCheckInfo info) {
        if (info.userpay_mode.equals("1")) {
            binding.tvwType.setText("银行卡/微信/支付宝");
        } else if (info.userpay_mode.equals("2")) {
            binding.tvwType.setText("虚拟货币");
        }
        binding.tvUsername.setText(info.username);//用户名
        if (info.userpay_mode.equals("1")) {
            binding.tvSelectorRightPaymentAccount.setText(info.userpay_bank);
            binding.tvSelectorRightPaymentName.setText(info.userpay_name);//付款人姓名
            binding.tvwCollectiontNameAgreement.setText(info.receive_name);//收款人姓名
            binding.etSelectorRightSavename.setText(info.userpay_amount);//存款金额
            binding.tvSelectorRightSavetime.setText(info.add_time);//存款精确时间
            binding.tvSelectorRightThree.setText(info.third_orderid);//第三单号
            binding.tvSelectorRightPayway.setText(info.receive_bank_text);//支付渠道
            binding.llInputUsdtAdd.setVisibility(View.GONE); //隐藏收款钱包地址

        } else if (info.userpay_mode.equals("2")) {
            binding.tvSelectorLeftPaymentAccount.setText(R.string.txt_tip_input_usdt_payment_account);
            binding.tvSelectorRightPaymentName.setText(info.userpay_bank);//付款钱包地址
            binding.tvSelectorLeftPaymentName.setText(R.string.txt_tip_input_usdt_payment_name);//协议
            for (int i = 0; i < protocolInfoArrayList.size(); i++) {
                if (String.valueOf(protocolInfoArrayList.get(i)).endsWith(info.userpay_virtual_protocol)) {
                    binding.tvSelectorRightPaymentName.setText(protocolInfoArrayList.get(i).name); //协议名称
                }
            }
            binding.tvSelectorLeftCollectionName.setText(R.string.txt_tip_input_usdt_numb);//虚拟币数量
            binding.tvSelectorRightSavetime.setText(info.add_time);//存款准确时间
            binding.tvSelectorRightThree.setText(info.third_orderid);
            binding.tvSelectorRightPayway.setText(info.receive_bank_text);//支付渠道
            binding.llInputUsdtAdd.setVisibility(View.VISIBLE);//显示收款钱包地址
            binding.etSelectorRightAdd.setText(info.receive_banknum);//收款钱包地址
        }
        String imageDownUrl = DomainUtil.getDomain2() + info.userpay_picture; //图片地址
        CfLog.i("imageDownUrl ==" + imageDownUrl);
        //String cookie = "auth-expires-in=604800; userPasswordCheck=lowPass; " + "auth=" + ***;
        //CfLog.e("cookie: " + cookie);

        GlideUrl glideUrl = new GlideUrl(imageDownUrl, new LazyHeaders.Builder()
                .addHeader("Content-Type","application/vnd.sc-api.v1.json")
                //.addHeader("Authorization", "bearer " + SPUtils.getInstance().getString(SPKeyGlobal.USER_TOKEN))
                //.addHeader("Cookie", cookie)
                //.addHeader("UUID", TagUtils.getDeviceId(getContext()))
                .build());
        Glide.with(getContext()).load(glideUrl).placeholder(R.mipmap.ic_loading).error(R.mipmap.me_icon_name).into(binding.ivSelectorTipImage);

    }
}
