package com.xtree.bet.weight;

import android.content.Context;
import android.telecom.Call;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import me.xtree.mvvmhabit.utils.ConvertUtils;

public class DraggableImageView extends androidx.appcompat.widget.AppCompatImageView {
    private int lastX;
    private int lastLeft;
    private int lastRight;
    private int maxRight;

    private View parentView;

    private CallBack callBack;

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public DraggableImageView(@NonNull Context context) {
        super(context);
    }

    public DraggableImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DraggableImageView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(!isEnabled()){
            return false;
        }
        //得到事件的坐标
        int eventX = (int) event.getRawX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //得到父视图的right
                if (maxRight == 0) {//保证只赋一次值
                    maxRight = (((View)getParent()).getRight()) - ((View)getParent()).getLeft();
                }
                //第一次记录lastX
                lastX = eventX;
                break;
            case MotionEvent.ACTION_MOVE:
                //计算事件的偏移
                int dx = eventX - lastX;
                //根据事件的偏移来移动imageView
                 int left = getLeft() + dx;
                 int right = getRight() + dx;
                //限制left >=0
                if (left < 0) {
                    right += -left;
                    left = 0;
                }
                //限制right <=maxRight
                if (right >= maxRight) {
                    left -= right - maxRight;
                    right = maxRight;
                }
                layout(left, getTop(), right, getBottom());
                //再次记录lastX/lastY
                lastX = eventX;
                lastRight = right;
                lastLeft = left;
                break;
            case MotionEvent.ACTION_UP:
                if(lastRight == maxRight) {
                    callBack.onActionUp();
                }
                layout(ConvertUtils.dp2px(5), getTop(), getWidth() + ConvertUtils.dp2px(5), getBottom());
                Animation translateAnimation = new TranslateAnimation(lastRight - getWidth(), 0, 0, 0);//设置平移的起点和终点
                translateAnimation.setDuration(300);//动画持续的时间为10s
                translateAnimation.setFillEnabled(true);//使其可以填充效果从而不回到原地
                translateAnimation.setFillAfter(true);//不回到起始位置
                this.setAnimation(translateAnimation);//给imageView添加的动画效果
                translateAnimation.startNow();//动画开始执行 放在最后即可
                break;
            default:
                break;
        }
        return true;//所有的motionEvent都交给imageView处理
    }

    public interface CallBack{
        void onActionUp();
    }

}
