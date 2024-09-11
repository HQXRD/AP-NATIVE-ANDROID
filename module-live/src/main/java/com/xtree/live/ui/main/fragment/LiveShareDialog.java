package com.xtree.live.ui.main.fragment;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;

import com.lxj.xpopup.core.CenterPopupView;
import com.lxj.xpopup.util.XPopupUtils;
import com.xtree.base.databinding.DialogUpdateBinding;
import com.xtree.base.utils.AppUtil;
import com.xtree.base.utils.CfLog;
import com.xtree.base.utils.DomainUtil;
import com.xtree.live.R;
import com.xtree.live.databinding.DialogLiveShareBinding;

import java.io.File;
import java.io.IOException;

import me.xtree.mvvmhabit.utils.ToastUtils;

/**
 *  直播间分享Dialog
 */
public class LiveShareDialog extends CenterPopupView {

    public interface ILiveShareCallback {

        void onClickShare(); //强制更新
    }
    private DialogLiveShareBinding binding;
    private String shareUrl ;//分享url
    private ILiveShareCallback iLiveShareCallback;

    public LiveShareDialog(@NonNull Context context, final String shareUrl, final  ILiveShareCallback iLiveShareCallback ) {
        super(context);
        this.shareUrl = shareUrl;
        this.iLiveShareCallback = iLiveShareCallback;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        initView();
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_live_share;
    }

    @Override
    protected int getMaxHeight() {
        return (XPopupUtils.getScreenHeight(getContext()) * 4 / 10);
    }

    private void initView() {
        CfLog.i("****** ");
        binding = DialogLiveShareBinding.bind(findViewById(R.id.ll_root));
        binding.tvShareBt.setOnClickListener(v -> {
            ToastUtils.showError("点击复制按钮");
            if (this.iLiveShareCallback != null){
                this.iLiveShareCallback.onClickShare();
            }
        });
        if (!this.shareUrl.isEmpty()){
            binding.tvShareUrl.setText(this.shareUrl);
        }
    }
}
