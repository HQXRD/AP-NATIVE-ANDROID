package com.xtree.mine.ui.fragment;

import android.app.Application;
import android.content.Context;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;
import com.xtree.base.utils.CfLog;
import com.xtree.base.utils.UuidUtil;
import com.xtree.mine.R;
import com.xtree.mine.data.Injection;
import com.xtree.mine.databinding.DialogGoogleAuthBinding;
import com.xtree.mine.ui.viewmodel.GooglePwdViewModel;

import java.util.HashMap;

import me.xtree.mvvmhabit.utils.Utils;

/**
 * 谷歌验证 弹窗
 */
public class GoogleAuthDialog extends BottomPopupView {

    LifecycleOwner owner;
    ICallBack mCallBack;
    DialogGoogleAuthBinding binding;
    GooglePwdViewModel viewModel;

    public interface ICallBack {
        void onAuthSucc();
    }

    public GoogleAuthDialog(@NonNull Context context) {
        super(context);
    }

    public GoogleAuthDialog(@NonNull Context context, LifecycleOwner owner, ICallBack mCallBack) {
        super(context);
        this.owner = owner;
        this.mCallBack = mCallBack;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        binding = DialogGoogleAuthBinding.bind(findViewById(R.id.ll_root));
        viewModel = new GooglePwdViewModel((Application) Utils.getContext(), Injection.provideHomeRepository());

        viewModel.liveDataAuth.observe(owner, vo -> {
            CfLog.d("****** google auth ok.");
            mCallBack.onAuthSucc();
            dismiss();
        });

        initView();
    }

    private void initView() {
        binding.ivwClose.setOnClickListener(v -> dismiss());
        binding.tvwOk.setOnClickListener(v -> requestData());

    }

    private void requestData() {
        String code = binding.edtCode.getText().toString();

        HashMap<String, String> map = new HashMap<>();
        map.put("code", code);
        map.put("nonce", UuidUtil.getID16());
        viewModel.authGoogleCode(map);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_google_auth;
    }

    @Override
    protected int getMaxHeight() {
        //return super.getMaxHeight();
        return (XPopupUtils.getScreenHeight(getContext()) * 50 / 100);
    }
}
