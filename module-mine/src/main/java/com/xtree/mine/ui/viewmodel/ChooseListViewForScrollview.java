package com.xtree.mine.ui.viewmodel;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class ChooseListViewForScrollview extends ListView {
    public ChooseListViewForScrollview(Context context) {
        super(context);
    }

    public ChooseListViewForScrollview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ChooseListViewForScrollview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ChooseListViewForScrollview(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int customSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >>2 , MeasureSpec.AT_MOST);

        super.onMeasure(widthMeasureSpec, customSpec);
    }
}
