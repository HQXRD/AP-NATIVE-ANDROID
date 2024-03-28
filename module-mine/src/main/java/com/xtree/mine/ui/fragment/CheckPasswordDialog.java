package com.xtree.mine.ui.fragment;

import android.app.Application;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.xtree.base.utils.UuidUtil;
import com.xtree.base.widget.LoadingDialog;
import com.xtree.mine.BR;
import com.xtree.mine.R;
import com.xtree.mine.data.Injection;
import com.xtree.mine.databinding.DialogTransferMemberBinding;
import com.xtree.mine.ui.viewmodel.MineViewModel;
import com.xtree.mine.ui.viewmodel.VerifyViewModel;
import com.xtree.mine.ui.viewmodel.factory.AppViewModelFactory;
import com.xtree.mine.vo.VerifyVo;

import java.util.HashMap;
import java.util.Objects;

import me.xtree.mvvmhabit.base.BaseDialogFragment;
import me.xtree.mvvmhabit.utils.ConvertUtils;
import me.xtree.mvvmhabit.utils.ToastUtils;
import me.xtree.mvvmhabit.utils.Utils;

public class CheckPasswordDialog extends BaseDialogFragment<DialogTransferMemberBinding, MineViewModel> {
    String type;
    boolean isCallback;
    ICallBack mCallBack;
    MineViewModel mineViewModel;
    VerifyViewModel verifyViewModel;

    public interface ICallBack {
        void checkPassword(String checkCode);
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = Objects.requireNonNull(getDialog()).getWindow();
        WindowManager.LayoutParams params = Objects.requireNonNull(window).getAttributes();
        //设置显示在底部
        params.gravity = Gravity.BOTTOM;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = ConvertUtils.dp2px(300);
        window.setAttributes(params);
        View decorView = window.getDecorView();
        decorView.setBackground(new ColorDrawable(Color.TRANSPARENT));
    }

    @Override
    public void onClick(View v) {

    }

    public static CheckPasswordDialog getInstance(String type, boolean isCallback, ICallBack mCallBack) {
        CheckPasswordDialog checkPasswordDialog = new CheckPasswordDialog();
        checkPasswordDialog.type = type;
        checkPasswordDialog.mCallBack = mCallBack;
        checkPasswordDialog.isCallback = isCallback;
        return checkPasswordDialog;
    }

    @Override
    public void initData() {
        super.initData();
        binding.setVariable(BR.model, viewModel);
        verifyViewModel = new VerifyViewModel((Application) Utils.getContext(), Injection.provideHomeRepository());
        mineViewModel = new MineViewModel((Application) Utils.getContext(), Injection.provideHomeRepository());
    }

    @Override
    public void initView() {
        binding.ivwClose.setOnClickListener(v -> close());
        binding.btnCancel.setOnClickListener(v -> close());
        binding.btnConfirm.setOnClickListener(v -> checkPassword());
    }

    @Override
    public MineViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getActivity().getApplication());
        return new ViewModelProvider(this, factory).get(MineViewModel.class);
    }

    @Override
    public void initViewObservable() {
        mineViewModel.liveDataCheckPassword.observe(this, vo -> {
            if (isCallback) {
                if (!vo.isEmpty()) {
                    mCallBack.checkPassword(vo);
                    close();
                } else {
                    ToastUtils.showLong(vo);
                }
            } else {
                if (!vo.isEmpty()) {
                    VerifyVo verifyVo = new VerifyVo();
                    verifyVo.tokenSign = vo;
                    verifyVo.mark = type;
                    verifyViewModel.goOthers(getActivity(), type, verifyVo);
                    close();
                } else {
                    ToastUtils.showLong(vo);
                }
            }
        });
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.dialog_transfer_member;
    }

    private void checkPassword() {
        String code = binding.etTransferPassword.getText().toString();

        LoadingDialog.show(getContext());

        HashMap<String, String> map = new HashMap<>();
        map.put("flag", "check");
        map.put("nextact", "bindsequestion");
        map.put("nextcon", "user");
        map.put("secpass", code);
        map.put("nonce", UuidUtil.getID16());
        mineViewModel.checkMoneyPassword(map);
    }

    private void close() {
        dismiss();
    }
}