package com.xtree.recharge.ui.fragment.guide;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

import com.lxj.xpopup.core.CenterPopupView;
import com.xtree.recharge.R;
import com.xtree.recharge.databinding.DialogRechargeGuideBinding;

import java.util.ArrayList;

import me.xtree.mvvmhabit.utils.ToastUtils;

/**
 * 充值引导 Dialog
 */
public class GuideDialog extends CenterPopupView {
    DialogRechargeGuideBinding binding ;


    public static GuideDialog newInstance(Context context) {
        GuideDialog dialog = new GuideDialog(context);
        return dialog;
    }
    public GuideDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_recharge_guide;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        initView();
    }
    private void  initView(){
        binding = DialogRechargeGuideBinding.bind(findViewById(R.id.ll_root_recharge_guide));
        binding.ivwClose.setOnClickListener(v -> {
            dismiss();
        });
        //跳过引导
        binding.tvSeniorBoot.setOnClickListener(v->{
            dismiss();
        });
        //进入引导
        binding.tvEnterBoot.setOnClickListener(v->{
            ToastUtils.showError("进入引导");
        });
    }
}
