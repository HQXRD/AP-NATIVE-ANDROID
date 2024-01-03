package com.xtree.bet.weight;

import android.graphics.Rect;
import android.text.TextUtils;
import android.view.View;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

import com.xtree.bet.R;

import java.util.ArrayList;
import java.util.List;

import me.xtree.mvvmhabit.utils.ConvertUtils;

public class SideBar extends View {
    //SideBar上显示的字母
    private static final String[] CHARACTER = {/*"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"*/};
    private List<String> characterList = new ArrayList<>();
    //SideBar的高度
    private int width;
    //SideBar的宽度
    private int height;
    //SideBar中每个字母的显示区域的高度
    private float cellHeight;
    //画字母的画笔
    private Paint characterPaint;
    //画字母的画笔
    private Paint characterSelectedPaint;
    //SideBar上字母绘制的矩形区域
    private Rect textRect;
    //手指触摸在SideBar上的横纵坐标
    private float touchY;
    private float touchX;

    private OnSelectListener listener;

    public SideBar(Context context, List<String> characterList) {
        super(context);
        this.characterList = characterList;
        init(context);
    }


    public SideBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public SideBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SideBar(Context context) {
        super(context);
        init(context);
    }

    //初始化操作
    private void init(Context context) {
        textRect = new Rect();
        characterPaint = new Paint();
        characterPaint.setColor(context.getResources().getColor(R.color.bt_text_color_primary_2));
        characterSelectedPaint = new Paint();
        characterSelectedPaint.setColor(context.getResources().getColor(R.color.bt_color_car_dialog_hight_line2));
        height = ConvertUtils.dp2px(25) * characterList.size();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
                            int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) { //在这里测量SideBar的高度和宽度
            width = getMeasuredWidth();
            //SideBar的高度除以需要显示的字母的个数，就是每个字母显示区域的高度
            cellHeight = ConvertUtils.dp2px(25);
            height = (int) cellHeight * characterList.size();
            //根据SideBar的宽度和每个字母显示的高度，确定绘制字母的文字大小，这样处理的好处是，对于不同分辨率的屏幕，文字大小是可变的
            int textSize = ConvertUtils.sp2px(12)/*(int) ((width > cellHeight ? cellHeight : width) * (3.0f / 4))*/;
            characterPaint.setTextSize(textSize);
            characterSelectedPaint.setTextSize(textSize);
        }
    }

    //画出SideBar上的字母
    private void drawCharacters(Canvas canvas) {
        for (int i = 0; i < characterList.size(); i++) {
            String s = characterList.get(i);
            Paint paint = characterPaint;
            if(TextUtils.equals(s, getHint())){
                paint = characterSelectedPaint;
            }
            //获取画字母的矩形区域
            paint.getTextBounds(s, 0, s.length(), textRect);
            //根据上一步获得的矩形区域，画出字母
            canvas.drawText(s,
                    (width - textRect.width()) / 2f,
                    cellHeight * i + (cellHeight + textRect.height()) / 2f,
                    paint);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawCharacters(canvas);
    }

    //根据手指触摸的坐标，获取当前选择的字母
    private String getHint() {
        int index = (int) (touchY / cellHeight);
        if (index >= 0 && index < characterList.size()) {
            return characterList.get(index);
        }
        return null;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //获取手指触摸的坐标
                touchX = event.getX();
                touchY = event.getY();
                return true;
            case MotionEvent.ACTION_MOVE:
                //获取手指触摸的坐标
                /*touchX = event.getX();
                touchY = event.getY();
                if (listener != null && touchX > 0) {
                    listener.onSelect(getHint());
                }
                *//*if (listener != null && touchX < 0) {
                    listener.onMoveUp(getHint());
                }*/
                return true;
            case MotionEvent.ACTION_UP:
                touchY = event.getY();
                if (listener != null) {
                    int index = (int) (touchY / cellHeight);
                    listener.onSelect(index);
                }
                invalidate();
                return true;
        }
        return super.onTouchEvent(event);
    }

    //监听器，监听手指在SideBar上按下和抬起的动作
    public interface OnSelectListener {
        void onSelect(int index);

        //void onMoveUp(String s);
    }

    //设置监听器
    public void setOnSelectListener(OnSelectListener listener) {
        this.listener = listener;
    }
}
