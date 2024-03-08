package com.xtree.home.ui.custom.view;

import android.content.Context;

import androidx.annotation.NonNull;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.CenterPopupView;
import com.xtree.base.global.Constant;
import com.xtree.base.utils.DomainUtil;
import com.xtree.base.utils.SPUtil;
import com.xtree.base.widget.BrowserDialog;
import com.xtree.home.R;
import com.xtree.home.databinding.DialogPmTipBinding;

import me.xtree.mvvmhabit.utils.KLog;

public class TipPMDialog extends CenterPopupView {

    ICallBack mCallBack;

    DialogPmTipBinding binding;

    public TipPMDialog(@NonNull Context context) {
        super(context);
    }

    public interface ICallBack {
        void onClickPM();

        void onClickFB();
    }

    public TipPMDialog(@NonNull Context context, ICallBack mCallBack) {
        super(context);
        this.mCallBack = mCallBack;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        initView();
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_pm_tip;
    }

    private void initView() {
        binding = DialogPmTipBinding.bind(findViewById(R.id.ll_root));
        binding.tvwCs.setOnClickListener(v -> {
            String title = getContext().getString(R.string.txt_custom_center);
            String url = DomainUtil.getDomain2() + Constant.URL_CUSTOMER_SERVICE;
            new XPopup.Builder(getContext()).asCustom(new BrowserDialog(getContext(), title, url)).show();
        });
        binding.tvwPm.setOnClickListener(v -> {
            mCallBack.onClickPM();
        });
        binding.tvwFb.setOnClickListener(v -> {
            mCallBack.onClickFB();
        });
        binding.cbTipPm.setChecked(SPUtil.get(getContext()).get("todayIsCheck", false));
        SPUtil.get(getContext()).put("todayTime", System.currentTimeMillis());
        binding.cbTipPm.setOnCheckedChangeListener((buttonView, isChecked) -> {
            KLog.i("isChecked", isChecked);
            SPUtil.get(getContext()).put("todayIsCheck", isChecked);
            SPUtil.get(getContext()).put("todayTime", System.currentTimeMillis());
        });

    }

}