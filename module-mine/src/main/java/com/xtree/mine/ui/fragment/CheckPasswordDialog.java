package com.xtree.mine.ui.fragment;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

import com.lxj.xpopup.core.BottomPopupView;
import com.xtree.base.utils.UuidUtil;
import com.xtree.base.widget.LoadingDialog;
import com.xtree.mine.R;
import com.xtree.mine.data.Injection;
import com.xtree.mine.databinding.DialogTransferMemberBinding;
import com.xtree.mine.ui.viewmodel.MineViewModel;

import java.util.HashMap;

import me.xtree.mvvmhabit.utils.ToastUtils;
import me.xtree.mvvmhabit.utils.Utils;

public class CheckPasswordDialog extends BottomPopupView {
    Context context;
    LifecycleOwner owner;
    ICallBack mCallBack;
    DialogTransferMemberBinding binding;
    MineViewModel viewModel;

    public interface ICallBack {
        void checkPassword(String checkCode);
    }

    public CheckPasswordDialog(@NonNull Context context) {
        super(context);
    }

    public static CheckPasswordDialog newInstance(@NonNull Context context, LifecycleOwner owner, ICallBack mCallBack) {
        CheckPasswordDialog checkPasswordDialog = new CheckPasswordDialog(context);
        checkPasswordDialog.context = context;
        checkPasswordDialog.owner = owner;
        checkPasswordDialog.mCallBack = mCallBack;
        return checkPasswordDialog;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        binding = DialogTransferMemberBinding.bind(findViewById(R.id.ll_root));
        viewModel = new MineViewModel((Application) Utils.getContext(), Injection.provideHomeRepository());

        viewModel.liveDataCheckPassword.observe(owner, vo -> {
            if (!vo.isEmpty()) {
                mCallBack.checkPassword(vo);
                dismiss();
            } else {
                ToastUtils.showLong(vo);
            }
        });

        initView();
    }

    private void initView() {
        binding.ivwClose.setOnClickListener(v -> dismiss());
        binding.btnCancel.setOnClickListener(v -> dismiss());
        binding.btnConfirm.setOnClickListener(v -> checkPassword());
    }

    private void checkPassword() {
        String code = binding.etTransferPassword.getText().toString();

        LoadingDialog.show(context);

        HashMap<String, String> map = new HashMap<>();
        map.put("flag", "check");
        map.put("nextact", "bindsequestion");
        map.put("nextcon", "user");
        map.put("secpass", code);
        map.put("nonce", UuidUtil.getID16());
        viewModel.checkMoneyPassword(map);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_transfer_member;
    }

    @Override
    protected int getMaxHeight() {
        return super.getMaxHeight();
    }
}