package com.xtree.home.ui.custom.view;

import android.content.Context;

import androidx.annotation.NonNull;

import com.lxj.xpopup.core.CenterPopupView;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.utils.AppUtil;
import com.xtree.base.utils.TimeUtils;
import com.xtree.home.R;
import com.xtree.home.databinding.DialogPmTipBinding;

import me.xtree.mvvmhabit.utils.KLog;
import me.xtree.mvvmhabit.utils.SPUtils;

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
        binding.tvwCs.setOnClickListener(v -> AppUtil.goCustomerService(getContext()));
        binding.tvwPm.setOnClickListener(v -> mCallBack.onClickPM());
        binding.tvwFb.setOnClickListener(v -> mCallBack.onClickFB());

        String key= SPKeyGlobal.PM_NOT_TIP_TODAY;
        binding.cbTipPm.setOnCheckedChangeListener((buttonView, isChecked) -> {
            KLog.i("isChecked", isChecked);
            if (isChecked) {
                SPUtils.getInstance().put(key, TimeUtils.getCurDate());
            } else {
                SPUtils.getInstance().remove(key);
            }
        });

    }

}