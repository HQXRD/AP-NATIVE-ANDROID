package com.xtree.recharge.ui.fragment;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;

import com.lxj.xpopup.core.CenterPopupView;
import com.lxj.xpopup.util.XPopupUtils;
import com.xtree.base.utils.AppUtil;
import com.xtree.recharge.R;
import com.xtree.recharge.databinding.DialogRcManualBinding;

public class RechargeManualDialog extends CenterPopupView {

    String code; // 暗号

    DialogRcManualBinding binding;

    public RechargeManualDialog(@NonNull Context context) {
        super(context);
    }

    public RechargeManualDialog(@NonNull Context context, String code) {
        super(context);
        this.code = code;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        initView();
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_rc_manual;
    }

    @Override
    protected int getMaxHeight() {
        return (XPopupUtils.getScreenHeight(getContext()) * 8 / 10);
    }

    private void initView() {
        binding = DialogRcManualBinding.bind(findViewById(R.id.ll_root));

        //String codeStr = "<span style=\"text-align: center;\">" + code + "</span>"; // center justify
        //binding.tvwCode.setText(HtmlCompat.fromHtml(codeStr, HtmlCompat.FROM_HTML_MODE_LEGACY));
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < code.length(); i++) {
            sb.append(code.charAt(i) + "  ");
        }
        binding.tvwCode.setText(sb.toString().trim());

        String fontStart = "<font color=#FF8A00>";
        String fontEnd = "</font>";
        String msg = fontStart + getContext().getString(R.string.txt_rc_m_4) + fontEnd;
        msg = getContext().getString(R.string.txt_rc_m_3, msg);
        binding.tvwMsg3.setText(HtmlCompat.fromHtml(msg, HtmlCompat.FROM_HTML_MODE_LEGACY));

        binding.tvwOk.setOnClickListener(v -> {
            AppUtil.goCustomerService(getContext());
            dismiss();
        });
    }

}
