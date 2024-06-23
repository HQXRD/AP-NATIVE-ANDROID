package com.xtree.base.widget;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.core.BottomPopupView;
import com.xtree.base.R;

public class LoadingDialog extends BottomPopupView {
    private Context mContext;
    public boolean isPurple = false;
    private static BasePopupView ppw;

    public LoadingDialog(@NonNull Context context) {
        super(context);
        mContext = context;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        initView();
    }

    public static BasePopupView show(Context context) {
        if (ppw == null || ppw.isDismiss()) {
            LoadingDialog dialog = new LoadingDialog(context);
            ppw = new XPopup.Builder(context)
                    .dismissOnTouchOutside(false)
                    .dismissOnBackPressed(true)
                    .asCustom(dialog)
                    .show();

        }
        return ppw;
    }
    public static BasePopupView show2(Context context) {
        if (ppw == null || ppw.isDismiss()) {
            LoadingDialog dialog = new LoadingDialog(context);
            dialog.isPurple = true;
            ppw = new XPopup.Builder(context)
                    .dismissOnTouchOutside(false)
                    .dismissOnBackPressed(true)
                    .asCustom(dialog)
                    .show();
        }
        return ppw;
    }
    public static void finish() {
        if (ppw != null && ppw.isShow()) {
            ppw.dismiss();
        }
    }

    private void initView() {
        ImageView ivwLoading = findViewById(R.id.ivw_loading);
        ConstraintLayout clLoading = findViewById(R.id.cl_loading);

        clLoading.setOnClickListener(v -> {
        });

        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.anim_loading_normal);
        animation.setRepeatMode(Animation.RESTART);
        animation.setDuration(1500);
        ivwLoading.startAnimation(animation);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_loading;
    }
}
