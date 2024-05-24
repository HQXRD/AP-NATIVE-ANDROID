package com.xtree.mine.ui.fragment;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static android.content.Context.WINDOW_SERVICE;

import android.content.Context;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.xtree.mine.R;

/**
 * 活動提款流水刷新 按鈕
 */
public class ActivityRefreshPopWindow extends RelativeLayout {
    public interface  IActivityRefreshCallback{
        public void  OnClickListener();
    }
    private boolean isShow;//是否展示标志位
    private String jumpUrl;//跳转外部url地址
    private Context ctx;
    private WindowManager windowManager;
    private View floatView;
    private WindowManager.LayoutParams floatLp;
    private IActivityRefreshCallback iActivityRefreshCallback ;

    public ActivityRefreshPopWindow(Context context, final IActivityRefreshCallback callback) {
        super(context);
        this.ctx = context;
        this.iActivityRefreshCallback = callback;
        initView();
    }

    public ActivityRefreshPopWindow(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ActivityRefreshPopWindow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ActivityRefreshPopWindow(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void show() {
        if (!isShow) {
            windowManager.addView(floatView, floatLp);
            isShow = true;
        }
    }

    public void closeView() {
        if (isShow && floatView != null && windowManager != null) {
            windowManager.removeView(floatView);
            isShow = false;
        }
    }

    private void initView() {
        windowManager = (WindowManager) ctx.getSystemService(WINDOW_SERVICE);
        LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(LAYOUT_INFLATER_SERVICE);
        floatView = inflater.inflate(R.layout.activity_refrehs_window, null);

        floatLp = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);

        floatLp.gravity = Gravity.BOTTOM;
        floatLp.x = 0;
        floatLp.y = 0;
       /* floatLp.x = displayMetrics.widthPixels;
        floatLp.y = displayMetrics.heightPixels;*/

      /*  floatView.findViewById(R.id.ivw_cash_pop).setOnTouchListener(new View.OnTouchListener() {
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
                        windowManager.updateViewLayout(floatView, floatWindowLayoutUpdateParam);
                        break;
                }
                return false;
            }
        });*/
        floatView.findViewById(R.id.tvw_refer).setOnClickListener(v -> {
            if (iActivityRefreshCallback != null){
                iActivityRefreshCallback.OnClickListener();
            }

        });
    }

}
