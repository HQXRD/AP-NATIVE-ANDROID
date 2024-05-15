package com.xtree.mine.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.xtree.base.global.Constant;
import com.xtree.base.net.HttpCallBack;
import com.xtree.base.router.RouterActivityPath;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.base.utils.AESUtil;
import com.xtree.base.utils.AppUtil;
import com.xtree.base.utils.CfLog;
import com.xtree.base.utils.ClickUtil;
import com.xtree.base.utils.SPUtil;
import com.xtree.base.utils.TagUtils;
import com.xtree.mine.BR;
import com.xtree.mine.R;
import com.xtree.mine.data.Spkey;
import com.xtree.mine.databinding.ActivityLoginBinding;
import com.xtree.mine.ui.fragment.AgreementDialog;
import com.xtree.mine.ui.fragment.GoogleAuthDialog;
import com.xtree.mine.ui.viewmodel.LoginViewModel;
import com.xtree.mine.ui.viewmodel.factory.AppViewModelFactory;
import com.xtree.mine.vo.LoginResultVo;

import java.util.HashMap;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import me.xtree.mvvmhabit.base.BaseActivity;
import me.xtree.mvvmhabit.utils.ToastUtils;

@Route(path = RouterActivityPath.Mine.PAGER_LOGIN_REGISTER)
public class LoginRegisterActivity extends BaseActivity<ActivityLoginBinding, LoginViewModel> {

    public static final String ENTER_TYPE = "enter_type";
    public static final int LOGIN_TYPE = 0x1001;
    public static final int REGISTER_TYPE = 0x1002;

    private int clickCount = 0; // 点击次数 debug model
    private boolean mIsAcc = false;
    private boolean mIsPwd1 = false;
    private boolean mIsPwd2 = false;

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_login;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initData() {
        viewModel.getSettings();
    }

    @Override
    public void initView() {
        binding.btnRegister.setEnabled(false);
        binding.llRoot.setOnClickListener(v -> hideKeyBoard());
        binding.loginSubHeader.setOnClickListener(v -> {
            if (clickCount++ > 5) {
                clickCount = 0;
                startContainerFragment(RouterFragmentPath.Home.PG_DEBUG);
            }
        });

        Intent intent = getIntent();
        int viewType = intent.getIntExtra(ENTER_TYPE, LOGIN_TYPE);
        if (viewType == LOGIN_TYPE) {
            binding.toRegisterArea.setVisibility(View.VISIBLE);
            binding.loginArea.setVisibility(View.VISIBLE);
            binding.toLoginArea.setVisibility(View.GONE);
            binding.meRegisterArea.setVisibility(View.GONE);
        }
        if (viewType == REGISTER_TYPE) {
            binding.toLoginArea.setVisibility(View.VISIBLE);
            binding.meRegisterArea.setVisibility(View.VISIBLE);
            binding.toRegisterArea.setVisibility(View.GONE);
            binding.loginArea.setVisibility(View.GONE);
        }

        boolean isRememberPwd = SPUtil.get(getApplication()).get(Spkey.REMEMBER_PWD, false);
        if (isRememberPwd) {
            try {
                binding.edtPwd.setText(AESUtil.decryptData(SPUtil.get(getApplication()).get(Spkey.PWD, ""), checkSecretKey()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            binding.edtAccount.setText(SPUtil.get(getApplication()).get(Spkey.ACCOUNT, ""));
            binding.ckbRememberPwd.setChecked(true);
        } else {
            binding.ckbRememberPwd.setChecked(false);
            binding.edtPwd.setText("");
            binding.edtAccount.setText("");
        }

        binding.ckbEye.setOnCheckedChangeListener((buttonView, isChecked) -> setEdtPwd(isChecked, binding.edtPwd));
        binding.ckbPwd1.setOnCheckedChangeListener((buttonView, isChecked) -> setEdtPwd(isChecked, binding.edtPwd1));
        binding.ckbPwd2.setOnCheckedChangeListener((buttonView, isChecked) -> setEdtPwd(isChecked, binding.edtPwd2));

        binding.tvwForgetPwd.setOnClickListener(v -> goForgetPassword());

        //binding.tvwAgreement.setOnClickListener(v -> goMain());
        binding.tvwSkipLogin.setOnClickListener(v -> goMain());
        binding.tvwCs.setOnClickListener(v -> AppUtil.goCustomerService(this));

        binding.btnLogin.setOnClickListener(v -> {
            if (!ifAgree()) {
                ToastUtils.showLong(getResources().getString(R.string.me_agree_hint));
                showAgreementDialog(binding.ckbAgreement);
                return;
            }

            String acc = binding.edtAccount.getText().toString().trim();
            String pwd = binding.edtPwd.getText().toString();
            if (TextUtils.isEmpty(acc)) {
                ToastUtils.showLong(getResources().getString(R.string.me_account_hint));
                return;
            }

            if (TextUtils.isEmpty(pwd)) {
                ToastUtils.showLong(getResources().getString(R.string.me_pwd_hint));
                return;
            }
            viewModel.login(acc, pwd);
        });

        binding.tvwAgreement.setOnClickListener(v -> {
            showAgreementDialog(binding.ckbAgreement);
        });
        binding.tvwAgreementRegister.setOnClickListener(v -> {
            showAgreementDialog(binding.registerAgreementCheckbox);
        });

        binding.toRegisterArea.setOnClickListener(v -> {
            //显示注册页面，隐藏登录界面
            binding.toLoginArea.setVisibility(View.VISIBLE);
            binding.meRegisterArea.setVisibility(View.VISIBLE);
            binding.toRegisterArea.setVisibility(View.GONE);
            binding.loginArea.setVisibility(View.GONE);
        });
        binding.toLoginArea.setOnClickListener(v -> {
            binding.toRegisterArea.setVisibility(View.VISIBLE);
            binding.loginArea.setVisibility(View.VISIBLE);
            binding.toLoginArea.setVisibility(View.GONE);
            binding.meRegisterArea.setVisibility(View.GONE);
        });

        binding.edtAccReg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (binding.edtAccReg.getText().toString().startsWith("0") || binding.edtAccReg.getText().toString().toLowerCase().startsWith("o")) {
                    binding.tvwUsernameWarning.setVisibility(View.VISIBLE);
                    binding.tvwUsernameWarning.setText(R.string.txt_user_name_should_not_0o);
                    mIsAcc = false;
                } else if (binding.edtAccReg.getText().toString().isEmpty()) {
                    binding.tvwUsernameWarning.setVisibility(View.VISIBLE);
                    binding.tvwUsernameWarning.setText(R.string.txt_username_empty);
                    mIsAcc = false;
                } else if (!containsLetterAndDigit(binding.edtAccReg.getText().toString())) {
                    binding.tvwUsernameWarning.setVisibility(View.VISIBLE);
                    // 请输入由字母和数字组成的6到12位字符 (不能纯数字,不能纯字母,不能字母和数字以外的字符)
                    binding.tvwUsernameWarning.setText(R.string.txt_user_name_should_char_num);
                    mIsAcc = false;
                } else if ((binding.edtAccReg.getText().toString().length() < 6 || binding.edtAccReg.getText().toString().length() > 12)) {
                    binding.tvwUsernameWarning.setVisibility(View.VISIBLE);
                    binding.tvwUsernameWarning.setText(R.string.txt_user_name_should_char_num);
                    //ToastUtils.showLong(R.string.txt_user_name_should_6_12);
                    mIsAcc = false;
                } else {
                    binding.tvwUsernameWarning.setVisibility(View.INVISIBLE);
                    mIsAcc = true;
                }

                binding.btnRegister.setEnabled(mIsAcc && mIsPwd1 && mIsPwd2);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        binding.edtPwd1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (binding.edtPwd1.getText().toString().isEmpty()) {
                    binding.tvwPwdWarning.setVisibility(View.VISIBLE);
                    binding.tvwPwdWarning.setText(R.string.txt_pwd_cannot_empty);
                    mIsPwd1 = false;
                }else if (binding.edtPwd1.getText().toString().length() < 6
                        || binding.edtPwd1.getText().toString().length() > 16) {
                    binding.tvwPwdWarning.setVisibility(View.VISIBLE);
                    binding.tvwPwdWarning.setText(R.string.txt_pwd_should_6_16);
                    mIsPwd1 = false;
                } else if (!containsLetterAndDigit(binding.tvwPwdWarning.getText().toString())) {
                    binding.tvwPwdWarning.setVisibility(View.VISIBLE);
                    binding.tvwPwdWarning.setText(R.string.txt_pwd_should_6_16);
                    mIsPwd1 = false;
                } else {
                    binding.tvwPwdWarning.setVisibility(View.INVISIBLE);
                    mIsPwd1 = true;
                }

                if (!binding.edtPwd1.getText().toString().equals(binding.edtPwd2.getText().toString())) {
                    binding.tvwPwdCheckWarning.setVisibility(View.VISIBLE);
                    binding.tvwPwdCheckWarning.setText(R.string.txt_reset_password_not_same_error);
                    mIsPwd2 = false;
                } else {
                    binding.tvwPwdCheckWarning.setVisibility(View.INVISIBLE);
                    mIsPwd2 = true;
                }

                CfLog.i((mIsAcc && mIsPwd1 && mIsPwd2) + "");
                binding.btnRegister.setEnabled(mIsAcc && mIsPwd1 && mIsPwd2);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.edtPwd2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (!binding.edtPwd1.getText().toString().equals(binding.edtPwd2.getText().toString())) {
                    binding.tvwPwdCheckWarning.setVisibility(View.VISIBLE);
                    binding.tvwPwdCheckWarning.setText(R.string.txt_reset_password_not_same_error);
                    mIsPwd2 = false;
                } else {
                    binding.tvwPwdCheckWarning.setVisibility(View.INVISIBLE);
                    mIsPwd2 = true;
                }

                CfLog.i((mIsAcc && mIsPwd1 && mIsPwd2) + "");
                binding.btnRegister.setEnabled(mIsAcc && mIsPwd1 && mIsPwd2);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.btnRegister.setOnClickListener(view -> {

            String account = binding.edtAccReg.getText().toString().trim();
            String pwd1 = binding.edtPwd1.getText().toString();
            String pwd2 = binding.edtPwd2.getText().toString();
            if (!binding.registerAgreementCheckbox.isChecked()) {
                ToastUtils.showLong(getResources().getString(R.string.me_agree_hint));
                showAgreementDialog(binding.registerAgreementCheckbox);
                return;
            }

            // 改由UI显示，而非Toast
            //if (TextUtils.isEmpty(account)) {
            //    ToastUtils.showLong(getResources().getString(R.string.me_account_hint));
            //    binding.edtAccReg.performClick();
            //    return;
            //}
            //
            //if (account.length() < 6 || account.length() > 12) {
            //    ToastUtils.showLong(getResources().getString(R.string.txt_user_name_should_6_12));
            //    return;
            //}
            //
            //if (TextUtils.isEmpty(pwd1)) {
            //    ToastUtils.showLong(getResources().getString(R.string.me_pwd_hint));
            //    return;
            //}
            //
            //if (pwd1.length() < 6 || pwd1.length() > 16) {
            //    ToastUtils.showLong(getResources().getString(R.string.txt_pwd_should_6_16_error));
            //    return;
            //}
            //
            //if (TextUtils.isEmpty(pwd2)) {
            //    ToastUtils.showLong(R.string.txt_enter_pwd_again);
            //    return;
            //}
            //
            //if (!pwd2.equals(pwd1)) {
            //    ToastUtils.showLong(R.string.txt_pwd_should_same);
            //    return;
            //}

            //验证输入参数
            viewModel.register(account, pwd1);

        });

    }

    private void showAgreementDialog(CheckBox checkBox) {
        if (ClickUtil.isFastClick()) {
            return;
        }
        BasePopupView ppw = new XPopup.Builder(LoginRegisterActivity.this).asCustom(new AgreementDialog(LoginRegisterActivity.this, checkBox));
        ppw.show();
    }

    @Override
    public LoginViewModel initViewModel() {
        //使用自定义的ViewModelFactory来创建ViewModel，如果不重写该方法，则默认会调用LoginViewModel(@NonNull Application application)构造方法
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getApplication());
        return new ViewModelProvider(this, factory).get(LoginViewModel.class);
    }

    @Override
    public void initViewObservable() {
        viewModel.liveDataLogin.observe(this, vo -> {
            SPUtil.get(getApplication()).put(Spkey.REMEMBER_PWD, binding.ckbRememberPwd.isChecked());
            if (binding.ckbRememberPwd.isChecked()) {
                try {
                    SPUtil.get(getApplication()).put(Spkey.PWD, AESUtil.encryptData(binding.edtPwd.getText().toString(), checkSecretKey()));
                } catch (Exception e) {
                    CfLog.e(String.valueOf(e));
                }
                SPUtil.get(getApplication()).put(Spkey.ACCOUNT, binding.edtAccount.getText().toString());
            }

            if (vo.twofa_required == 0) {
                //viewModel.setLoginSucc(vo);
                TagUtils.tagEvent(getBaseContext(), TagUtils.EVENT_LOGIN);
                goMain();

            } else if (vo.twofa_required == 1) {
                CfLog.i("*********** 去谷歌验证...");
                GoogleAuthDialog dialog = new GoogleAuthDialog(this, this, () -> {
                    viewModel.setLoginSucc(vo);
                    TagUtils.tagEvent(getBaseContext(), TagUtils.EVENT_LOGIN);
                    goMain();
                });
                new XPopup.Builder(this)
                        .dismissOnBackPressed(false)
                        .dismissOnTouchOutside(false)
                        .asCustom(dialog)
                        .show();
            }
        });

        viewModel.liveDataReg.observe(this, vo -> {
            TagUtils.tagEvent(getBaseContext(), "reg");
            goMain();
        });

        viewModel.liveDataLoginFail.observe(this, vo -> {
            CfLog.d(vo.toString());
            if (vo.code == HttpCallBack.CodeRule.CODE_20208) {
                TagUtils.tagEvent(getBaseContext(), TagUtils.EVENT_LOGIN);
                String acc = binding.edtAccount.getText().toString().trim();
                HashMap<String, Object> map = (HashMap<String, Object>) vo.data;
                LoginResultVo vo2 = (LoginResultVo) map.get("data");
                Bundle bundle = new Bundle();
                bundle.putString("type", Constant.VERIFY_LOGIN);
                bundle.putString("username", acc);
                bundle.putString("map", map.get("loginArgs").toString());
                bundle.putString("phone", vo2.contacts.phone);
                bundle.putString("email", vo2.contacts.email);
                startContainerFragment(RouterFragmentPath.Mine.PAGER_SECURITY_VERIFY, bundle);
            }

        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // 用户点返回按钮时,要打开首页(有些页面跳转到登录页的同时,也会关闭自己)
        goMain();
    }

    private boolean ifAgree() {
        return binding.ckbAgreement.isChecked();
    }

    private void setEdtPwd(boolean isChecked, EditText edt) {
        if (isChecked) {
            edt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        } else {
            edt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
        edt.setSelection(edt.length());
    }

    private void goMain() {
        ARouter.getInstance().build(RouterActivityPath.Main.PAGER_MAIN)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK)
                .navigation();
        finish();
    }

    private void goForgetPassword() {
        startContainerFragment(RouterFragmentPath.Mine.PAGER_FORGET_PASSWORD); // 三方转账
    }

    private SecretKey checkSecretKey() throws Exception {
        // 不将key存入Keystore中主要是没有权限拿取私钥，这会导致取得的值都是空的，如果以后要快速登入使用指纹解锁就能使用keystore
        String key = SPUtil.get(getApplication()).get(Spkey.KEY, "");
        if (SPUtil.get(getApplication()).get(Spkey.KEY, "").isEmpty()) {
            key = Base64.encodeToString(AESUtil.getRSAKeyPair().getEncoded(), Base64.DEFAULT);
            SPUtil.get(getApplication()).put(Spkey.KEY, key);
        }
        return new SecretKeySpec(Base64.decode(key, Base64.DEFAULT), "AES");
    }

    private boolean containsLetterAndDigit(String str) {
        boolean containsLetter = false;
        boolean containsDigit = false;

        for (char c : str.toCharArray()) {
            if (Character.isLetter(c)) {
                containsLetter = true;
            } else if (Character.isDigit(c)) {
                containsDigit = true;
            }

            if (containsLetter && containsDigit) {
                return true;
            }
        }

        return false;
    }
}
