package com.xtree.mine.ui.fragment.withdrawal;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * 银行卡取款 固定大额 多金额选择View
 */
public class WithdrawAmountSelectionGridView  extends GridView {
    public WithdrawAmountSelectionGridView(Context context) {
        super(context);
    }

    public WithdrawAmountSelectionGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WithdrawAmountSelectionGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public WithdrawAmountSelectionGridView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int customSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >>2 , MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, customSpec);
    }
}
