package com.xtree.base.widget;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.airbnb.lottie.LottieAnimationView;
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
        //ImageView ivwLoading = findViewById(R.id.ivw_loading);
        LottieAnimationView lavIcon = findViewById(R.id.lav_icon);
        ConstraintLayout clLoading = findViewById(R.id.cl_loading);

        clLoading.setOnClickListener(v -> {
        });

        if (isPurple) {
            // 设置图像文件夹路径
            lavIcon.setImageAssetsFolder("images_p/");

            // 从 assets 文件夹中加载 JSON 文件
            lavIcon.setAnimation("loadingp.json");
            lavIcon.playAnimation();
            clLoading.setBackgroundColor(getResources().getColor(R.color.clr_transparent));
        } else {
            // 设置图像文件夹路径
            lavIcon.setImageAssetsFolder("images_w/");

            // 从 assets 文件夹中加载 JSON 文件
            lavIcon.setAnimation("loadingw.json");
            lavIcon.playAnimation();
        }
        //Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.anim_loading_normal);
        //animation.setRepeatMode(Animation.RESTART);
        //animation.setDuration(1500);
        //ivwLoading.startAnimation(animation);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_loading;
    }
}
