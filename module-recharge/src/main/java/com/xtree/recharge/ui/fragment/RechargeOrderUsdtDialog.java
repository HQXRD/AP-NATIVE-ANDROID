package com.xtree.recharge.ui.fragment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;
import com.xtree.base.global.Constant;
import com.xtree.base.utils.CfLog;
import com.xtree.base.utils.DomainUtil;
import com.xtree.base.utils.QrcodeUtil;
import com.xtree.base.widget.BrowserDialog;
import com.xtree.recharge.R;
import com.xtree.recharge.databinding.DialogRcOrderUsdtBinding;
import com.xtree.recharge.vo.RechargePayVo;

import me.xtree.mvvmhabit.utils.ToastUtils;

public class RechargeOrderUsdtDialog extends BottomPopupView {

    RechargePayVo mRechargePayVo;
    DialogRcOrderUsdtBinding binding;

    public RechargeOrderUsdtDialog(@NonNull Context context) {
        super(context);
    }

    public RechargeOrderUsdtDialog(@NonNull Context context, RechargePayVo mRechargePayVo) {
        super(context);
        this.mRechargePayVo = mRechargePayVo;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        initView();
    }

    private void initView() {
        binding = DialogRcOrderUsdtBinding.bind(findViewById(R.id.ll_root));
        binding.ivwClose.setOnClickListener(v -> dismiss());
        binding.tvwCs.setOnClickListener(v -> goCustomerService());
        binding.tvwTitle.setText(mRechargePayVo.payname);
        binding.tvwMoney.setText(mRechargePayVo.money);
        binding.tvwRateAmount.setText(mRechargePayVo.rateamount);
        binding.tvwRate.setText(mRechargePayVo.rate);
        binding.tvwQrcodeUrl.setText(mRechargePayVo.qrcodeurl);

        String txt = mRechargePayVo.maxexpiretime + getContext().getString(R.string.txt_minutes); // xx分钟
        txt = "<font color=#EE5A5A> " + txt + " </font>"; // 加彩色
        txt = getContext().getString(R.string.txt_rc_submit_succ_pay_in_minutes_pls, txt).replace("\n", "<br>");
        binding.tvwMaxExpireTime.setText(HtmlCompat.fromHtml(txt, HtmlCompat.FROM_HTML_MODE_LEGACY));

        binding.tvwOk.setOnClickListener(v -> dismiss());

        binding.tvw01.setText("1. " + binding.tvw01.getText().toString());
        binding.tvw02.setText("2. " + binding.tvw02.getText().toString());

        String key1 = getContext().getString(R.string.txt_rc_fee_amount);
        String key2 = getContext().getString(R.string.txt_rc_received_amount);
        String msg = getContext().getString(R.string.txt_rc_usdt_tip);
        String txt1 = "<font color=#FF5050> " + key1 + " </font>";
        String txt2 = "<font color=#FF5050> " + key2 + " </font>";
        msg = msg.replace(key1, txt1).replace(key2, txt2);
        binding.tvw03.setText(HtmlCompat.fromHtml(msg, HtmlCompat.FROM_HTML_MODE_LEGACY));

        if (mRechargePayVo.isqrcode) {
            binding.ivwQrcode.setImageBitmap(QrcodeUtil.getQrcode(mRechargePayVo.qrcodeurl)); // 设置二维码图片
        }
        binding.tvwCopy.setOnClickListener(v -> copy(mRechargePayVo.qrcodeurl));
    }

    private void copy(String txt) {
        CfLog.d(txt);
        ClipboardManager cm = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData cd = ClipData.newPlainText("txt", txt);
        cm.setPrimaryClip(cd);
        ToastUtils.showLong(R.string.txt_copied);
    }

    private void goCustomerService() {
        String title = getContext().getString(R.string.txt_custom_center);
        String url = DomainUtil.getDomain2() + Constant.URL_CUSTOMER_SERVICE;
        new XPopup.Builder(getContext()).asCustom(new BrowserDialog(getContext(), title, url)).show();
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_rc_order_usdt;
    }

    @Override
    protected int getMaxHeight() {
        //return super.getMaxHeight();
        return (XPopupUtils.getScreenHeight(getContext()) * 75 / 100);
    }

}
