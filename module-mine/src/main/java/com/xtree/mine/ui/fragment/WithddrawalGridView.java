package com.xtree.mine.ui.fragment;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * 银行卡取款 固定大额 多金额选择View
 */
public class WithddrawalGridView  extends GridView {

    public WithddrawalGridView(Context context) {
        super(context);
    }

    public WithddrawalGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WithddrawalGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public WithddrawalGridView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int customSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >>2 , MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, customSpec);
    }
}
