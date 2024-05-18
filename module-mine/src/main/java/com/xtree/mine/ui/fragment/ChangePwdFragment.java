package com.xtree.mine.ui.fragment;

import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.xtree.base.router.RouterActivityPath;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.base.utils.CfLog;
import com.xtree.base.utils.RSAEncrypt;
import com.xtree.base.utils.UuidUtil;
import com.xtree.mine.BR;
import com.xtree.mine.R;
import com.xtree.mine.databinding.FragmentChangePwdBinding;
import com.xtree.mine.ui.viewmodel.VerifyViewModel;
import com.xtree.mine.ui.viewmodel.factory.AppViewModelFactory;

import java.util.HashMap;

import me.xtree.mvvmhabit.base.BaseFragment;
import me.xtree.mvvmhabit.utils.SPUtils;
import me.xtree.mvvmhabit.utils.ToastUtils;

/**
 * 修改密码
 */
@Route(path = RouterFragmentPath.Mine.PAGER_CHANGE_PWD)
public class ChangePwdFragment extends BaseFragment<FragmentChangePwdBinding, VerifyViewModel> {
    private static final String ARG_TOKEN_SIGN = "tokenSign";

    private String tokenSign;
    private String mark = "resetloginpassword";

    public ChangePwdFragment() {
    }

    @Override
    public void initView() {
        binding.llRoot.setOnClickListener(v -> hideKeyBoard());
        binding.ckbPwd.setOnCheckedChangeListener((buttonView, isChecked) -> setEdtPwd(isChecked, binding.edtPwd));
        binding.ckbPwd2.setOnCheckedChangeListener((buttonView, isChecked) -> setEdtPwd(isChecked, binding.edtPwd2));

        binding.tvwReset.setOnClickListener(v -> {
            binding.edtPwd.setText("");
            binding.edtPwd2.setText("");
        });
        binding.ivwBack.setOnClickListener( v ->{
            getActivity().finish();
        });

        binding.tvwOk.setOnClickListener(v -> {
            String pwd1 = binding.edtPwd.getText().toString();
            String pwd2 = binding.edtPwd2.getText().toString();

            if (pwd1.isEmpty() || pwd2.isEmpty())
            {
                showError(getString(R.string.txt_chang_psw_input_error));
                return;
            }
            if (!pwd1.equals(pwd2)) {
                showError(getString(R.string.txt_chang_psw_input_twice_error));
                return;
            }
            if (pwd1.length() < 6 || pwd1.length() > 16 ||pwd2.length() < 6 || pwd2.length() > 16  ){
                showError(getString(R.string.txt_chang_psw_input_length_error));
                return;
            }
            if (!pwd1.matches("^[0-9a-zA-Z]+$") || !pwd2.matches("^[0-9a-zA-Z]+$")){
                showError(getString(R.string.txt_chang_psw_input_format_error));
                return;
            }

            String public_key = SPUtils.getInstance().getString("public_key",
                    "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDW+Gv8Xmk+EdTLQUU5fEAzhlVuFrI7GN4a8N\\/B0Oe63ORK8oBE1pK+t5U5Iz89K4zf7nX+tqQvzND5Z57NMwyqTYYb3TMbrKgjqF1K2YW08OaubjpdohMnDIibmPXNtrbRZpOf2xIaApR+wpqGS+Xw0LzKA8JPYDOPO4lseAtqVwIDAQAB");
            String loginpass = RSAEncrypt.encrypt2(pwd1, public_key);

            HashMap<String, String> map = new HashMap<>();
            map.put("mark", mark);
            map.put("newpass", loginpass);
            map.put("nonce", UuidUtil.getID16());
            map.put("passtype", "login");
            map.put("token", tokenSign);
            viewModel.changePwd(map);
        });

    }

    @Override
    public void initData() {
        super.initData();
        if (getArguments() != null) {
            tokenSign = getArguments().getString(ARG_TOKEN_SIGN);
        }
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_change_pwd;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public VerifyViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getActivity().getApplication());
        return new ViewModelProvider(this, factory).get(VerifyViewModel.class);
    }

    @Override
    public void initViewObservable() {
        viewModel.liveDataChangePwd.observe(this, vo -> {
            CfLog.i("******");
            ToastUtils.showLong(R.string.txt_change_succ);
            getActivity().finish();
            ARouter.getInstance().build(RouterActivityPath.Mine.PAGER_LOGIN_REGISTER).navigation();
        });

    }

    private void setEdtPwd(boolean isChecked, EditText edt) {
        if (isChecked) {
            edt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        } else {
            edt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
        edt.setSelection(edt.length());
    }


    private void  showError(final String message){
        if (TextUtils.isEmpty(message) ||  message == null){
            return;
        }
        binding.tvwPswWarning.setVisibility(View.VISIBLE);
        binding.tvwPswWarning.setText(message);

    }
}