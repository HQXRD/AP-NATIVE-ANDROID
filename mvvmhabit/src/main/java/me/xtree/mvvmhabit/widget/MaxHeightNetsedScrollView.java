package me.xtree.mvvmhabit.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;

import me.xtree.mvvmhabit.R;
import me.xtree.mvvmhabit.utils.ConvertUtils;

public class MaxHeightNetsedScrollView extends NestedScrollView {
    private int mMaxHeight;

    public void setmMaxHeight(int mMaxHeight) {
        this.mMaxHeight = mMaxHeight;
    }

    public MaxHeightNetsedScrollView(@NonNull Context context) {
        super(context);
    }

    public MaxHeightNetsedScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs);
    }

    public MaxHeightNetsedScrollView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context, attrs);
    }

    private void initialize(@NonNull Context context, @Nullable AttributeSet attrs){
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MaxHeightNetsedScrollView);
        //mMaxHeight = typedArray.getLayoutDimension(R.styleable.MaxHeightNetsedScrollView_maxHeight, mMaxHeight);
        mMaxHeight = ConvertUtils.px2dp((float) (ConvertUtils.getScreenHeight(context) * 0.6));
        mMaxHeight = ConvertUtils.dp2px(mMaxHeight);
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if(mMaxHeight > 0 && mMaxHeight < height){
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(mMaxHeight, MeasureSpec.AT_MOST);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
