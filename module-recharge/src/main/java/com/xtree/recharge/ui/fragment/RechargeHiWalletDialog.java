package com.xtree.recharge.ui.fragment;

import android.content.Context;

import androidx.annotation.NonNull;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.CenterPopupView;
import com.lxj.xpopup.util.XPopupUtils;
import com.xtree.base.global.Constant;
import com.xtree.base.utils.AppUtil;
import com.xtree.base.widget.BrowserDialog;
import com.xtree.recharge.R;
import com.xtree.recharge.databinding.DialogHiwalletBinding;
import com.xtree.recharge.vo.HiWalletVo;

/**
 * 嗨钱包弹窗
 */
public class RechargeHiWalletDialog extends CenterPopupView {
    HiWalletVo mHiWalletVo;
    ICallBack mCallBack;
    DialogHiwalletBinding binding;

    public interface ICallBack {
        void callBack();
    }

    public RechargeHiWalletDialog(@NonNull Context context) {
        super(context);
    }

    public RechargeHiWalletDialog(@NonNull Context context, HiWalletVo mHiWalletVo, ICallBack mCallBack) {
        super(context);
        this.mHiWalletVo = mHiWalletVo;
        this.mCallBack = mCallBack;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        initView();
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_hiwallet;
    }

    @Override
    protected int getMaxHeight() {
        return (XPopupUtils.getScreenHeight(getContext()) * 8 / 10);
    }

    private void initView() {
        binding = DialogHiwalletBinding.bind(findViewById(R.id.ll_root));

        binding.ivwClose.setOnClickListener(v -> {
            dismiss();
        });
        binding.tvwOk.setOnClickListener(v -> {
            mCallBack.callBack();
            dismiss();
        });
        binding.tvwReg.setOnClickListener(v -> {
            new XPopup.Builder(getContext()).moveUpToKeyboard(false).asCustom(BrowserDialog.newInstance(getContext(), mHiWalletVo.login_url)).show();
            dismiss();
        });
        binding.tvwDownload.setOnClickListener(v -> {
            AppUtil.goBrowser(getContext(), Constant.URL_DOWNLOAD_HI_WALLET);
            dismiss();
        });

    }

}
