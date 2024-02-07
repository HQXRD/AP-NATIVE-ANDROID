package com.xtree.base.widget;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.lxj.xpopup.core.BottomPopupView;
import com.xtree.base.R;

public class LoadingDialog extends BottomPopupView {
    private Context mContext;

    public LoadingDialog(@NonNull Context context) {
        super(context);
        mContext = context;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        initView();
    }

    private void initView() {
        ImageView ivwLoading = findViewById(R.id.ivw_loading);
        ConstraintLayout clLoading = findViewById(R.id.cl_loading);

        clLoading.setOnClickListener(v -> {
        });

        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.anim_loading);
        animation.setRepeatMode(Animation.RESTART);
        animation.setDuration(1500);
        ivwLoading.startAnimation(animation);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_loading;
    }
}
