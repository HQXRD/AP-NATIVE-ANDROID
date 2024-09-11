package com.xtree.recharge.ui.widget;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;

import com.lxj.xpopup.core.CenterPopupView;
import com.xtree.recharge.R;
import com.xtree.recharge.databinding.DialogBindCardTipsBinding;

public class TipBindCardDialog extends CenterPopupView {

    private ICallBack mCallBack;
    private DialogBindCardTipsBinding binding;
    private String mContent;

    public TipBindCardDialog(@NonNull Context context) {
        super(context);
    }

    public TipBindCardDialog(@NonNull Context context, String content, ICallBack mCallBack) {
        super(context);
        this.mCallBack = mCallBack;
        this.mContent = content;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        initView();
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_bind_card_tips;
    }

    private void initView() {
        binding = DialogBindCardTipsBinding.bind(findViewById(R.id.ll_root));

        if (!TextUtils.isEmpty(mContent)) {
            binding.tvwCode.setText(mContent);
        }
        // 设置确认按钮的点击事件
        binding.tvwOk.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCallBack != null) {
                    mCallBack.onClickConfirm(); // 调用回调接口的方法
                }
                dismiss(); // 关闭对话框
            }
        });

        binding.ivwClose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss(); // 关闭对话框
            }
        });

    }

    // 定义回调接口
    public interface ICallBack {
        void onClickConfirm(); // 当用户点击确认按钮时触发的回调方法
    }
}
