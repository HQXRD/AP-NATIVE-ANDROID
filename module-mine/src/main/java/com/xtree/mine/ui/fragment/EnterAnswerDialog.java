package com.xtree.mine.ui.fragment;

import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.core.CenterPopupView;
import com.lxj.xpopup.util.XPopupUtils;
import com.xtree.base.widget.MsgDialog;
import com.xtree.base.widget.TipDialog;
import com.xtree.mine.R;
import com.xtree.mine.databinding.DialogEnterAnswerBinding;

import me.xtree.mvvmhabit.utils.ToastUtils;
/*输入密保*/
public class EnterAnswerDialog extends CenterPopupView {
    public interface ICallBack {
        void onClickCancel();

        void onClickSure(final String key, final String answer, final String quesNum);
    }

    private String key;
    private ICallBack mCallBack;
    private String showContent;
    private DialogEnterAnswerBinding binding;
    private BasePopupView ppwError ;

    public EnterAnswerDialog(@NonNull Context context, String key, String showContent, ICallBack mCallBack) {
        super(context);
        this.key = key;
        this.showContent = showContent;
        this.mCallBack = mCallBack;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        initView();
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_enter_answer;
    }

    @Override
    protected int getMaxHeight() {
        return (XPopupUtils.getScreenHeight(getContext()) * 4 / 10);
    }

    private void initView() {
        binding = DialogEnterAnswerBinding.bind(findViewById(R.id.ll_root_enter_answer));
        if (showContent != null) {
            binding.tvwMsg.setText(showContent);
        }
        //取消
        binding.tvwLeft.setOnClickListener(v -> {
            this.mCallBack.onClickCancel();
        });
        //确定
        binding.tvwRight.setOnClickListener(v -> {
            if (TextUtils.isEmpty(binding.edAnswer.getText().toString().trim())) {
                ToastUtils.showError(getContext().getString(R.string.txt_security_question_tip));
            } else if (TextUtils.isEmpty(binding.tvwMsg.getText().toString().trim())) {
                showError(getContext().getString(R.string.txt_network_error));
            } else {
                this.mCallBack.onClickSure(key, binding.edAnswer.getText().toString().trim(), showContent);
            }
        });
    }

    /*展示异常信息*/
    private void  showError(final String message){
        if (ppwError == null) {
            final String title = getContext().getString(R.string.txt_kind_tips);
            ppwError = new XPopup.Builder(getContext()).asCustom(new MsgDialog(getContext(), title, message, true, new TipDialog.ICallBack() {
                @Override
                public void onClickLeft() {
                    ppwError.dismiss();
                    dismiss();
                }

                @Override
                public void onClickRight() {
                    ppwError.dismiss();
                    dismiss();
                }
            }));
        }
        ppwError.show();
    }

}


