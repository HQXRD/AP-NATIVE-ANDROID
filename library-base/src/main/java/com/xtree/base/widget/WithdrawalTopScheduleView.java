package com.xtree.base.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.xtree.base.R;
import com.xtree.base.databinding.LayoutWithdrawalTopBinding;

/**
 * 提现
 */
public class WithdrawalTopScheduleView extends LinearLayout {
    /* public enum WithdrawalTopType{
         public static  final int TOP_TYPE_WEB = 0;
         public static final int TOP_TYPE_OTHER = 1;

     }*/
    private LayoutWithdrawalTopBinding binding;

    public WithdrawalTopScheduleView(Context context) {
        super(context);
        init(context, null);
    }

    public WithdrawalTopScheduleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public WithdrawalTopScheduleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void changeViewByType() {

    }

    private void init(Context context, AttributeSet attrs) {
        View.inflate(context, getLayoutId(), this);

        binding = LayoutWithdrawalTopBinding.bind(findViewById(R.id.ll_root_withdrawal_top));
        binding.withdrawalTopRequest.setText("123123123");
    }

    public int getLayoutId() {
        return R.layout.layout_withdrawal_top;
    }
}
