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
import com.lxj.xpopup.XPopup;
import com.xtree.base.global.Constant;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.base.utils.CfLog;
import com.xtree.base.utils.DomainUtil;
import com.xtree.base.utils.TagUtils;
import com.xtree.base.widget.BrowserActivity;
import com.xtree.base.widget.BrowserDialog;
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
    private String feedbackId;
    private ArrayList<FeedbackVo.FeedbackBankInfo> bankInfoList = new ArrayList<>();//支付渠道
    private ArrayList<FeedbackVo.FeedbackProtocolInfo> protocolInfoArrayList = new ArrayList<>();//虚拟币协议
    private FeedbackCheckVo feedbackCheckVo;//获取反馈信息页面详情

    @Override
    public void initView() {
        binding.ivwBack.setOnClickListener(v -> getActivity().finish());
        //我的客服
        binding.ivwCs.setOnClickListener(v -> {
            String title = getContext().getString(R.string.txt_custom_center);
            String url = DomainUtil.getDomain2() + Constant.URL_CUSTOMER_SERVICE;
            new XPopup.Builder(getContext()).asCustom(new BrowserDialog(getContext(), title, url)).show();
        });
        //消息中心
        binding.ivwMsg.setOnClickListener(v -> {
            String title = getString(R.string.txt_msg_center);
            String url = DomainUtil.getDomain2() + Constant.URL_MY_MESSAGES;
            BrowserActivity.start(getContext(), title, url, true);
        });
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
       // viewModel.getFeedbackCheckInfo(startTime, endTime);
        viewModel.getFeedbackCheckDetailInfo(feedbackId);
    }

    /**
     * 数据回调 数显UI
     */
    @Override
    public void initViewObservable() {

        //查询获取反馈页面详情
        viewModel.feedbackCheckVoSingleLiveData.observe(this, v -> {
            feedbackCheckVo = v;
            for (int i = 0; i < feedbackCheckVo.protocolInfo.size(); i++)
            {
                CfLog.i("protocolInfo = " + feedbackCheckVo.protocolInfo.get(i).toString());
            }
            referUIWithModel(feedbackCheckVo.list.get(0));
        });
    }

    private void referUIWithModel(FeedbackCheckVo.FeedbackCheckInfo info) {
        if (info.userpay_mode.equals("1"))
        {
            binding.tvwType.setText("银行卡/微信/支付宝");
        } else if (info.userpay_mode.equals("2")) {
            binding.tvwType.setText("虚拟货币");
        }
        binding.tvUsername.setText(info.username);//用户名
        if (info.userpay_mode.equals("1"))
        {
            binding.tvSelectorRightPaymentAccount.setText(info.userpay_bank);
            binding.tvSelectorRightPaymentName.setText(info.userpay_name);//付款人姓名
            binding.tvwCollectiontNameAgreement.setText(info.receive_name);//收款人姓名
            binding.etSelectorRightSavename.setText(info.userpay_amount);//存款金额
            binding.tvSelectorRightSavetime.setText(info.add_time);//存款精确时间
            binding.tvSelectorRightThree.setText(info.third_orderid);//第三单号
            binding.tvSelectorRightPayway.setText(info.receive_bank_text);//支付渠道
            binding.llInputUsdtAdd.setVisibility(View.GONE); //隐藏收款钱包地址

        } else if (info.userpay_mode.equals("2"))
        {
            binding.tvSelectorLeftPaymentAccount.setText(R.string.txt_tip_input_usdt_payment_account);
            for (int i = 0; i < feedbackCheckVo.protocolInfo.size(); i++)
            {
                if (info.userpay_virtual_protocol.equals(String.valueOf(feedbackCheckVo.protocolInfo.get(i).id)))
                {
                    binding.tvSelectorRightPaymentAccount.setText(info.userpay_bank);
                    binding.tvSelectorRightPaymentName.setText(feedbackCheckVo.protocolInfo.get(i).name);//付款钱包地址
                }

            }
            binding.tvSelectorLeftPaymentName.setText(R.string.txt_tip_input_usdt_payment_name);//协议
            binding.tvSelectorLeftCollectionName.setText(R.string.txt_tip_input_usdt_numb);//虚拟币数量
            binding.tvwCollectiontNameAgreement.setText(info.userpay_amount);
            binding.llSave.setVisibility(View.GONE);
            binding.tvSelectorRightSavetime.setText(info.userpay_time);//存款准确时间
            binding.tvSelectorRightThree.setText(info.third_orderid);
            binding.tvSelectorRightPayway.setText(info.receive_bank_text);//支付渠道
            binding.llInputUsdtAdd.setVisibility(View.VISIBLE);//显示收款钱包地址
            binding.etSelectorRightAdd.setText(info.receive_banknum);//收款钱包地址
        }
        String imageDownUrl = DomainUtil.getDomain2() + info.userpay_picture; //图片地址
        CfLog.i("imageDownUrl ==" + imageDownUrl);

        String cookie = "auth=" + SPUtils.getInstance().getString(SPKeyGlobal.USER_TOKEN)
                + ";" + SPUtils.getInstance().getString(SPKeyGlobal.USER_SHARE_COOKIE_NAME)
                + "=" + SPUtils.getInstance().getString(SPKeyGlobal.USER_SHARE_SESSID)
                + ";";
      cookie = "auth-expires-in=604800; userPasswordCheck=lowPass; " + cookie ;
        CfLog.e("cookie: " + cookie);

        GlideUrl glideUrl = new GlideUrl(imageDownUrl, new LazyHeaders.Builder()
                .addHeader("Content-Type","application/vnd.sc-api.v1.json")
                .addHeader("Authorization", "bearer " + SPUtils.getInstance().getString(SPKeyGlobal.USER_TOKEN))
                .addHeader("Cookie", cookie)
                .addHeader("UUID", TagUtils.getDeviceId(getContext()))
                .build());
        Glide.with(getContext()).load(glideUrl).placeholder(R.mipmap.ic_loading).error(R.mipmap.me_icon_name).into(binding.ivSelectorTipImage);

    }
}
