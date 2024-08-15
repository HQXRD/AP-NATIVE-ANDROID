package com.xtree.mine.ui.rebateagrt.dialog;

import android.content.Context;

import androidx.annotation.NonNull;

import com.lxj.xpopup.core.CenterPopupView;
import com.lxj.xpopup.util.XPopupUtils;
import com.xtree.base.widget.TipDialog;
import com.xtree.mine.R;
import com.xtree.mine.databinding.DialogDividendTipBinding;

/**
 * Created by KAKA on 2024/3/21.
 * Describe: 分红确认弹窗
 */
public class DividendTipDialog extends CenterPopupView {

    private final String msg;
    private final TipDialog.ICallBack mCallBack;

    public DividendTipDialog(@NonNull Context context, String msg, TipDialog.ICallBack mCallBack) {
        super(context);
        this.msg = msg;
        this.mCallBack = mCallBack;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        initView();
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_dividend_tip;
    }

    @Override
    protected int getMaxHeight() {
        return (XPopupUtils.getScreenHeight(getContext()));
    }

    private void initView() {
        DialogDividendTipBinding binding = DialogDividendTipBinding.bind(findViewById(com.xtree.base.R.id.ll_root));

        binding.tvwMsg.setText(msg);

        binding.tvwLeft.setOnClickListener(v -> {
            if (mCallBack != null) {
                mCallBack.onClickLeft();
            }
        });
        binding.tvwRight.setOnClickListener(v -> {
            if (mCallBack != null) {
                mCallBack.onClickRight();
            }
        });
    }

}
