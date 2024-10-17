package com.xtree.base.widget;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;

import com.lxj.xpopup.core.CenterPopupView;
import com.lxj.xpopup.util.XPopupUtils;
import com.xtree.base.R;
import com.xtree.base.databinding.DialogAppErrorTipBinding;
import com.xtree.base.utils.AppUtil;
import com.xtree.base.utils.CfLog;

/**
 * App升级失败 弹窗
 */
public class AppUpdateErrorDialog extends CenterPopupView {

    boolean isSingleBtn;

    ICallBack mCallBack;
    DialogAppErrorTipBinding binding;

    private String jumpUrl;


  /*  public AppUpdateErrorDialog(@NonNull Context context) {
        super(context);
    }*/

    public interface ICallBack {
        void onClickLeft();

        void onClickRight();
    }


    public AppUpdateErrorDialog(@NonNull Context context, String jumpUrl , boolean isSingleBtn, AppUpdateErrorDialog.ICallBack mCallBack) {
        super(context);
        this.isSingleBtn = isSingleBtn;
        this.mCallBack = mCallBack;
        this.jumpUrl  = jumpUrl;
    }



    @Override
    protected void onCreate() {
        super.onCreate();

        initView();
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_app_error_tip;
    }

    @Override
    protected int getMaxHeight() {
        return (XPopupUtils.getScreenHeight(getContext()) * 4 / 10);
    }

    private void initView() {
        binding = DialogAppErrorTipBinding.bind(findViewById(R.id.ll_root));
        if (isSingleBtn) {
            binding.tvwLeft.setVisibility(View.GONE);
        }


        binding.tvwLeft.setOnClickListener(v -> {
            if (mCallBack != null) {
                mCallBack.onClickLeft();
            }
        });
        binding.tvwRight.setOnClickListener(v -> {
            if (mCallBack != null) {
                mCallBack.onClickRight();
            }
            if (!TextUtils.isEmpty(jumpUrl)) {
                AppUtil.goBrowser(getContext(), jumpUrl);
            } else {
                CfLog.e("****************  download url is null *********** ");
            }
        });
        CfLog.e("****************  download url is null ***********  --- 》 "  + jumpUrl);
    }


}
