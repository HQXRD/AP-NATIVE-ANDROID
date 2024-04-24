package com.xtree.base.widget;

import android.content.Context;

import androidx.annotation.NonNull;

import com.lxj.xpopup.core.CenterPopupView;
import com.xtree.base.R;
import com.xtree.base.databinding.DialogGameTipBinding;
import com.xtree.base.utils.TimeUtils;

import me.xtree.mvvmhabit.utils.KLog;
import me.xtree.mvvmhabit.utils.SPUtils;

public class TipGameDialog extends CenterPopupView {

    ICallBack mCallBack;
    private String title;
    private String key;
    DialogGameTipBinding binding;

    public TipGameDialog(@NonNull Context context) {
        super(context);
    }

    public TipGameDialog(@NonNull Context context, String title, String key, ICallBack mCallBack) {
        super(context);
        this.title = title;
        this.key = key;
        this.mCallBack = mCallBack;
    }

    public interface ICallBack {
        void onClickConfirm();
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        initView();
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_game_tip;
    }

    private void initView() {
        binding = DialogGameTipBinding.bind(findViewById(R.id.ll_root));
        binding.tvwConfirm.setOnClickListener(v -> {
            mCallBack.onClickConfirm();
            dismiss();
        });
        String msg = title + getContext().getString(R.string.text_cancel_tip);
        binding.tvwMsg.setText(msg);
        binding.cbTipPm.setOnCheckedChangeListener((buttonView, isChecked) -> {
            KLog.i("isChecked", isChecked);
            if (isChecked) {
                SPUtils.getInstance().put(key, TimeUtils.getCurDate());
            } else {
                SPUtils.getInstance().remove(key);
            }
        });

    }

}