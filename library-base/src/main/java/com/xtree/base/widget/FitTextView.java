package com.xtree.base.widget;

import android.content.Context;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.Gravity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;



public class FitTextView extends androidx.appcompat.widget.AppCompatTextView {

    private Paint mTextPaint;
    private float mMaxTextSize;
    private float mMinTextSize;


    public FitTextView(@NonNull Context context) {
        this(context , null);
    }

    public FitTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setGravity(getGravity() | Gravity.CENTER_VERTICAL);
        setLines(1);
        initialise();
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        refitText(text.toString(), this.getWidth());
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (w != oldw) {
            refitText(this.getText().toString(), w);
        }
    }

    private void initialise(){
        mTextPaint = new TextPaint();
        mTextPaint.set(this.getPaint());
        // 最大的大小默认为特定的文本大小，除非它太小了
        mMaxTextSize = this.getTextSize();
    }
    private void refitText(String text, int textWidth) {
        if (textWidth > 0) {
            int availableWidth = textWidth - this.getPaddingLeft() - this.getPaddingRight();
            float trySize = mMaxTextSize;

            mTextPaint.setTextSize(trySize);
            while (mTextPaint.measureText(text) > availableWidth) {

                trySize -= 1;
                if (trySize <= mMinTextSize) {
                    trySize = mMinTextSize;
                    break;
                }
                mTextPaint.setTextSize(trySize);
            }

            // setTextSize参数值为sp值
            setTextSize(px2sp(getContext(), trySize));
        }
    }
    /**
     * 将px值转换为sp值，保证文字大小不变
     */
    public static float px2sp(Context context, float pxValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (pxValue / fontScale);
    }
}
