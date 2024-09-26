package com.xtree.mine.ui.dialog;

import android.content.Context;

import androidx.annotation.NonNull;

import com.lxj.xpopup.core.CenterPopupView;
import com.xtree.mine.R;
import com.xtree.mine.databinding.DialogBonusInfoBinding;

public class BonusTipsDialog extends CenterPopupView {
    private DialogBonusInfoBinding binding;

    public BonusTipsDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        binding = DialogBonusInfoBinding.bind(findViewById(R.id.cl_root));
        binding.tvwOk.setOnClickListener(v -> dismiss());
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_bonus_info;
    }
}
