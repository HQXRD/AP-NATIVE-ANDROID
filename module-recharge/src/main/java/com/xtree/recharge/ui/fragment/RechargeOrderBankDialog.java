package com.xtree.recharge.ui.fragment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;
import com.xtree.base.utils.AppUtil;
import com.xtree.base.utils.CfLog;
import com.xtree.base.utils.DomainUtil;
import com.xtree.base.widget.BrowserDialog;
import com.xtree.recharge.R;
import com.xtree.recharge.databinding.DialogRcOrderBankBinding;
import com.xtree.recharge.vo.RechargePayVo;

import me.xtree.mvvmhabit.utils.ToastUtils;

public class RechargeOrderBankDialog extends BottomPopupView {

    RechargePayVo mRechargePayVo;
    DialogRcOrderBankBinding binding;

    public RechargeOrderBankDialog(@NonNull Context context) {
        super(context);
    }

    public RechargeOrderBankDialog(@NonNull Context context, RechargePayVo mRechargePayVo) {
        super(context);
        this.mRechargePayVo = mRechargePayVo;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        initView();
    }

    private void initView() {
        binding = DialogRcOrderBankBinding.bind(findViewById(R.id.ll_root));
        binding.ivwClose.setOnClickListener(v -> dismiss());
        binding.ivwCs.setOnClickListener(v -> AppUtil.goCustomerService(getContext()));
        binding.tvwTitle.setText(mRechargePayVo.payname);
        binding.tvwMoney.setText(mRechargePayVo.money);

        String txt = mRechargePayVo.maxexpiretime + getContext().getString(R.string.txt_minutes); // xx分钟
        txt = "<font color=#EE5A5A> " + txt + " </font>"; // 加彩色
        txt = getContext().getString(R.string.txt_rc_submit_succ_pay_in_minutes_pls, txt).replace("\n", "<br>");
        binding.tvwMaxExpireTime.setText(HtmlCompat.fromHtml(txt, HtmlCompat.FROM_HTML_MODE_LEGACY));

        binding.tvwToBankName.setText(mRechargePayVo.toBankName);
        binding.tvwToBankNameDetail.setText(mRechargePayVo.toBankNameDetail);
        binding.tvwBankAcctName.setText(mRechargePayVo.bankacctname);
        binding.tvwBankAcctCard.setText(mRechargePayVo.bankacctcard);
        binding.tvwMoney2.setText(mRechargePayVo.money);

        binding.tvw01.setText("1. " + binding.tvw01.getText().toString());
        binding.tvw02.setText("2. " + binding.tvw02.getText().toString());
        binding.tvw03.setText("3. " + binding.tvw03.getText().toString());
        binding.tvw04.setText("4. " + binding.tvw04.getText().toString());
        binding.tvw05.setText("5. " + binding.tvw05.getText().toString());

        binding.tvwCopyBankName.setOnClickListener(v -> copy(mRechargePayVo.toBankName));
        binding.tvwCopyBranch.setOnClickListener(v -> copy(mRechargePayVo.toBankNameDetail));
        binding.tvwCopyAccName.setOnClickListener(v -> copy(mRechargePayVo.bankacctname));
        binding.tvwCopyAccNum.setOnClickListener(v -> copy(mRechargePayVo.bankacctcard));
        binding.tvwCopyMoney.setOnClickListener(v -> copy(mRechargePayVo.money));
        binding.tvwCopyMoney2.setOnClickListener(v -> copy(mRechargePayVo.money));

        binding.tvwToHelp.setOnClickListener(v -> goHelp());

        //String bankInfo = mRechargePayVo.toBankName + "\n" + mRechargePayVo.toBankNameDetail + "\n" + mRechargePayVo.bankacctname + "\n" + mRechargePayVo.bankacctcard;
        //binding.tvwOk.setOnClickListener(v -> copy(bankInfo));
        binding.tvwOk.setOnClickListener(v -> dismiss());
    }

    private void copy(String txt) {
        CfLog.d(txt);
        ClipboardManager cm = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData cd = ClipData.newPlainText("txt", txt);
        cm.setPrimaryClip(cd);
        ToastUtils.showLong(R.string.txt_copied);
    }

    private void goHelp() {
        if (!TextUtils.isEmpty(mRechargePayVo.help_url)) {
            String title = getContext().getString(R.string.txt_rc_help);
            String url = mRechargePayVo.help_url;
            if (!url.startsWith("http")) {
                url = DomainUtil.getDomain2() + url;
            }
            new XPopup.Builder(getContext()).asCustom(new BrowserDialog(getContext(), title, url)).show();
        }
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_rc_order_bank;
    }

    @Override
    protected int getMaxHeight() {
        //return super.getMaxHeight();
        return (XPopupUtils.getScreenHeight(getContext()) * 75 / 100);
    }

}
