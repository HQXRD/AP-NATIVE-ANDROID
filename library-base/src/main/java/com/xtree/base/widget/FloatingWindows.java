package com.xtree.base.widget;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static android.content.Context.WINDOW_SERVICE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PixelFormat;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.xtree.base.R;
import com.xtree.base.utils.CfLog;

import me.xtree.mvvmhabit.utils.ConvertUtils;

public abstract class FloatingWindows extends RelativeLayout {
    protected Context mContext;
    protected ImageView ivwIcon;
    protected View secondaryLayout;
    protected View floatView;
    protected ConstraintLayout mainLayout;
    protected LinearLayout llLine;

    private boolean isShow = false;
    private int mFloatX;
    private int mFloatY;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams floatLp;

    public FloatingWindows(Context context) {
        super(context);
        mContext = context;
    }

    protected void onCreate(int layout) {
        initView(layout);
        initData();
    }

    protected void onCreate() {
        initView(0);
        initData();
    }

    public void removeView() {
        if (mWindowManager != null && floatView != null && isShow) {
            CfLog.i("Close floatView");
            //mWindowManager.removeView(floatView); // 会泄露
            mWindowManager.removeViewImmediate(floatView); // 不会泄露
            isShow = false;
        }
    }

    public void show() {
        if (!isShow) {
            CfLog.i("Show floatView");
            mWindowManager.addView(floatView, floatLp);
            isShow = true;
        }
    }

    protected void setStartLocation() {
        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT);
        params.endToStart = R.id.ivw_icon;
        params.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;

        mainLayout.addView(secondaryLayout, params);
    }

    protected void setBottomLocation() {
        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT);
        params.endToEnd = R.id.ivw_icon;
        params.topToBottom = R.id.ivw_icon;

        mainLayout.addView(secondaryLayout, params);
    }

    protected void removeSecond() {
        mainLayout.removeView(secondaryLayout);
    }

    public void setIcon(int icon) {
        ivwIcon.setImageResource(icon);
    }

    public void setPosition(int x, int y) {
        this.mFloatX = x;
        this.mFloatY = y;
    }

    @SuppressLint("ClickableViewAccessibility")
    protected void initView(int layout) {
        mWindowManager = (WindowManager) mContext.getSystemService(WINDOW_SERVICE);
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
        floatView = inflater.inflate(R.layout.floating_icon, null);
        if (layout != 0) {
            secondaryLayout = inflater.inflate(layout, null);
        }

        floatLp = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );
        DisplayMetrics displayMetrics = new DisplayMetrics();
        mWindowManager.getDefaultDisplay().getMetrics(displayMetrics);
        floatLp.gravity = Gravity.TOP;
        mFloatX = mFloatX == 0 ? displayMetrics.widthPixels / 2 - ConvertUtils.dp2px(30) : mFloatX;
        mFloatY = mFloatY == 0 ? displayMetrics.heightPixels / 2 + ConvertUtils.dp2px(100) : mFloatY;
        floatLp.x = mFloatX;
        floatLp.y = mFloatY;

        ivwIcon = floatView.findViewById(R.id.ivw_icon);
        mainLayout = floatView.findViewById(R.id.cl_root);
        llLine = floatView.findViewById(R.id.ll_line);

        mainLayout.setOnTouchListener(new View.OnTouchListener() {
            final WindowManager.LayoutParams floatWindowLayoutUpdateParam = floatLp;
            double x;
            double y;
            double px;
            double py;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x = floatWindowLayoutUpdateParam.x;
                        y = floatWindowLayoutUpdateParam.y;
                        px = event.getRawX();
                        py = event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        floatWindowLayoutUpdateParam.x = (int) ((x + event.getRawX()) - px);
                        floatWindowLayoutUpdateParam.y = (int) ((y + event.getRawY()) - py);
                        mWindowManager.updateViewLayout(floatView, floatWindowLayoutUpdateParam);
                        break;
                }
                return false;
            }
        });
    }

    public abstract void initData();
}
