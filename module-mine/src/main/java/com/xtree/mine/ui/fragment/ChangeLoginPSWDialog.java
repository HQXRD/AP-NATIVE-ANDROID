package com.xtree.mine.ui.fragment;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

import com.lxj.xpopup.core.CenterPopupView;
import com.lxj.xpopup.util.XPopupUtils;
import com.xtree.base.utils.RSAEncrypt;
import com.xtree.base.utils.UuidUtil;
import com.xtree.base.widget.LoadingDialog;
import com.xtree.mine.R;
import com.xtree.mine.data.Injection;
import com.xtree.mine.databinding.DialogChangeLoginPasswordBinding;
import com.xtree.mine.ui.viewmodel.VerifyViewModel;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.xtree.mvvmhabit.utils.SPUtils;
import me.xtree.mvvmhabit.utils.ToastUtils;
import me.xtree.mvvmhabit.utils.Utils;

/**
 * 修改登录密码 弹窗
 */
public class ChangeLoginPSWDialog extends CenterPopupView {

    LifecycleOwner owner;
    IChangeLoginPSWCallBack mCallBack;

    private DialogChangeLoginPasswordBinding binding;
    private VerifyViewModel viewModel;

    public interface IChangeLoginPSWCallBack {
        void changeLoginPSWSucc();
        void changeLoginClose();
    }

    public ChangeLoginPSWDialog(@NonNull Context context) {
        super(context);
    }

    public static ChangeLoginPSWDialog newInstance(@NonNull Context context, LifecycleOwner owner, IChangeLoginPSWCallBack mCallBack) {
        ChangeLoginPSWDialog dialog = new ChangeLoginPSWDialog(context);
        dialog.owner = owner;
        dialog.mCallBack = mCallBack;
        return dialog;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        binding = DialogChangeLoginPasswordBinding.bind(findViewById(R.id.ll_root_change_login));
        viewModel = new VerifyViewModel((Application) Utils.getContext(), Injection.provideHomeRepository());

        initView();
        initViewObservable();
    }

    private void initView() {
        binding.ivwClose.setOnClickListener(v ->{
            mCallBack.changeLoginClose();
        } );
        binding.dialogCancel.setOnClickListener(v ->{
            mCallBack.changeLoginClose();
        } );
        binding.dialogConfirm.setOnClickListener(v -> {
            requestData();
        });
    }

    private void initViewObservable() {

        viewModel.liveDataChangePwd.observe(owner, vo -> {
            ToastUtils.showLong(R.string.txt_change_succ);
            mCallBack.changeLoginPSWSucc();
            dismiss();
        });

    }

    private void requestData() {

        String oldCode = binding.edtOldCode.getText().toString().trim();
        String newCode = binding.edtNewCode.getText().toString().trim();
        String sureCode = binding.edtNewSureCode.getText().toString().trim();

        final String pattern = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,16}$";
        final Pattern r = Pattern.compile(pattern);
        Matcher matcherNewCode = r.matcher(newCode);
        Matcher matcherSureCode = r.matcher(sureCode);
        if (TextUtils.isEmpty(oldCode)) {
            ToastUtils.showError(getContext().getString(R.string.txt_change_login_old_err));
        } else if (TextUtils.isEmpty(newCode)) {
            ToastUtils.showError(getContext().getString(R.string.txt_change_login_new_err));
        } else if (TextUtils.isEmpty(sureCode)) {
            ToastUtils.showError(getContext().getString(R.string.txt_change_login_sure_err));
        } else if (!newCode.equals(sureCode)) {
            ToastUtils.showError(getContext().getString(R.string.txt_change_login_err));
        }
        else {
            LoadingDialog.show(getContext());
            HashMap<String, String> map = new HashMap<>();
            String public_key = SPUtils.getInstance().getString("public_key", "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDW+Gv8Xmk+EdTLQUU5fEAzhlVuFrI7GN4a8N\\/B0Oe63ORK8oBE1pK+t5U5Iz89K4zf7nX+tqQvzND5Z57NMwyqTYYb3TMbrKgjqF1K2YW08OaubjpdohMnDIibmPXNtrbRZpOf2xIaApR+wpqGS+Xw0LzKA8JPYDOPO4lseAtqVwIDAQAB");
            String loginOldPSW = RSAEncrypt.encrypt2(oldCode, public_key);
            String loginNewPSW = RSAEncrypt.encrypt2(newCode, public_key);

            map.put("nonce", UuidUtil.getID16());
            map.put("password_original", loginOldPSW);
            map.put("password", loginNewPSW);
            map.put("password_confirmation", loginNewPSW);
            viewModel.changeLoginPwd(map);
        }

    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_change_login_password;
    }

    @Override
    protected int getMaxHeight() {
        //return super.getMaxHeight();
        return (XPopupUtils.getScreenHeight(getContext()) * 50 / 100);
    }
}
