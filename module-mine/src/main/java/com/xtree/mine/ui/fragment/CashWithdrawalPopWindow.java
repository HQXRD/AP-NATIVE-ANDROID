package com.xtree.mine.ui.fragment;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static android.content.Context.WINDOW_SERVICE;

import android.content.Context;
import android.graphics.PixelFormat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.xtree.base.utils.AppUtil;
import com.xtree.mine.R;

import me.xtree.mvvmhabit.utils.ToastUtils;

/*web 提现 浮窗
onepayfix3、onepayfix4、onepayfix5、onepayfix6页面增加弹出按钮，点击，页面在浏览器新窗口打开
*   */
public class CashWithdrawalPopWindow extends RelativeLayout {
    private boolean isShow;//是否展示标志位
    private String jumpUrl;//跳转外部url地址
    private Context ctx;
    private WindowManager windowManager;
    private View floatView;
    private WindowManager.LayoutParams floatLp;

    public CashWithdrawalPopWindow(Context context, final String jumpUrl) {
        super(context);
        this.ctx = context;
        this.jumpUrl = jumpUrl;
        initView();
    }

    public CashWithdrawalPopWindow(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CashWithdrawalPopWindow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CashWithdrawalPopWindow(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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
        floatView = inflater.inflate(R.layout.cash_pop_window, null);

        floatLp = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);

        floatLp.gravity = Gravity.TOP;
        floatLp.x = displayMetrics.widthPixels / 2 - 80;
        floatLp.y = displayMetrics.heightPixels / 2 - 300;

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
        floatView.findViewById(R.id.ivw_cash_pop).setOnClickListener(v -> {
            if (!TextUtils.isEmpty(jumpUrl)) {
                AppUtil.goBrowser(ctx, jumpUrl);
            }

        });
    }

}
