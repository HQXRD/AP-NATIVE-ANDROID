package com.xtree.mine.ui.fragment;

import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.lxj.xpopup.core.CenterPopupView;
import com.lxj.xpopup.util.XPopupUtils;
import com.xtree.base.utils.CfLog;
import com.xtree.mine.R;
import com.xtree.mine.databinding.DialogResetFundPswBinding;

import me.xtree.mvvmhabit.utils.ToastUtils;

/*密保问题页面 找回资金密保 重置资金密码*/
public class ReSetFundPSWDialog extends CenterPopupView {
    public interface ICallBack {
        void onClickCancel();

        void onClickSure(final String firstPSW , final String secondPSW);
    }
    private ICallBack mCallBack ;
    private DialogResetFundPswBinding binding ;
    public ReSetFundPSWDialog(@NonNull Context context, ICallBack mCallBack) {
        super(context);
        this.mCallBack = mCallBack;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        initView();
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_reset_fund_psw;
    }

    @Override
    protected int getMaxHeight() {
        return (XPopupUtils.getScreenHeight(getContext()) * 4 / 10);
    }

    private void initView(){
        binding = DialogResetFundPswBinding.bind(findViewById(R.id.ll_root_reset_psw));

        //取消
        binding.tvwLeft.setOnClickListener(v -> {
            this.mCallBack.onClickCancel();
        });
        //确定
        binding.tvwRight.setOnClickListener(v ->{
            String firstStr = binding.edFirst.getText().toString().trim() ;
            String secondStr =binding.edSecond.getText().toString().trim();
            CfLog.e(firstStr + "||" +secondStr);
            if (TextUtils.isEmpty(firstStr) || TextUtils.isEmpty(secondStr)){
                ToastUtils.showError(getContext().getString(R.string.txt_reset_fund_psw_tip));
            } else if (!firstStr.equals(secondStr)) {
                ToastUtils.showError(getContext().getString(R.string.txt_fund_psw_error_tip));
            } else{
                this.mCallBack.onClickSure(firstStr , secondStr);
            }
        });
    }
}
