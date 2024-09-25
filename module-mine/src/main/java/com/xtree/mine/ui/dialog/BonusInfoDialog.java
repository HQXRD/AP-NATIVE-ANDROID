package com.xtree.mine.ui.dialog;

import android.content.Context;

import androidx.annotation.NonNull;

import com.lxj.xpopup.core.CenterPopupView;
import com.xtree.mine.R;
import com.xtree.mine.databinding.DialogBonusRuleBinding;

public class BonusInfoDialog extends CenterPopupView {
    private DialogBonusRuleBinding binding;

    public BonusInfoDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        binding = DialogBonusRuleBinding.bind(findViewById(R.id.cl_root));
        binding.ivwClose.setOnClickListener(v -> dismiss());
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_bonus_rule;
    }
}
