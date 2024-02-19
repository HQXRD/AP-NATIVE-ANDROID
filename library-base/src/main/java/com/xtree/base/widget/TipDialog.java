package com.xtree.base.widget;

import android.content.Context;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.View;

import androidx.annotation.NonNull;

import com.lxj.xpopup.core.CenterPopupView;
import com.lxj.xpopup.util.XPopupUtils;
import com.xtree.base.R;
import com.xtree.base.databinding.DialogTipBinding;

public class TipDialog extends CenterPopupView {

    String title;
    String msg;
    String txtLeft;
    boolean isSingleBtn;
    String txtRight;
    ICallBack mCallBack;

    DialogTipBinding binding;

    public interface ICallBack {
        void onClickLeft();

        void onClickRight();
    }

    public TipDialog(@NonNull Context context, String title, String msg, ICallBack mCallBack) {
        super(context);
        this.title = title;
        this.msg = msg;
        this.mCallBack = mCallBack;
    }

    public TipDialog(@NonNull Context context, String title, String msg, String txtLeft, String txtRight, ICallBack mCallBack) {
        super(context);
        this.title = title;
        this.msg = msg;
        this.txtLeft = txtLeft;
        this.txtRight = txtRight;
        this.mCallBack = mCallBack;
    }

    public TipDialog(@NonNull Context context, String title, String msg, boolean isSingleBtn, ICallBack mCallBack) {
        super(context);
        this.title = title;
        this.msg = msg;
        this.isSingleBtn = isSingleBtn;
        this.mCallBack = mCallBack;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        initView();
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_tip;
    }

    @Override
    protected int getMaxHeight() {
        return (XPopupUtils.getScreenHeight(getContext()) * 4 / 10);
    }

    private void initView() {
        binding = DialogTipBinding.bind(findViewById(R.id.ll_root));

        if (!TextUtils.isEmpty(title)) {
            binding.tvwTitle.setText(title);
        }

        binding.tvwMsg.setText(msg);
        if (msg.length() > 39) {
            binding.tvwMsg2.setMovementMethod(ScrollingMovementMethod.getInstance());
            binding.tvwMsg.setVisibility(View.GONE);
            binding.tvwMsg2.setText(msg);
            binding.tvwMsg2.setVisibility(View.VISIBLE);
        }

        if (isSingleBtn) {
            binding.tvwLeft.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(txtLeft)) {
            binding.tvwLeft.setText(txtLeft);
            if (!txtLeft.equals(getContext().getString(R.string.txt_cancel))) {
                binding.tvwLeft.setBackground(getContext().getDrawable(R.drawable.bg_btn_selector));
                binding.tvwLeft.setTextColor(getResources().getColor(R.color.clr_text_code_selector));
            }
        }
        if (!TextUtils.isEmpty(txtRight)) {
            binding.tvwRight.setText(txtRight);
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
        });

    }

}
