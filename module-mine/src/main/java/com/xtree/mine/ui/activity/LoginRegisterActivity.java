package com.xtree.mine.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;

import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.lxj.xpopup.XPopup;
import com.xtree.base.global.Constant;
import com.xtree.base.net.HttpCallBack;
import com.xtree.base.router.RouterActivityPath;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.base.utils.AESUtil;
import com.xtree.base.utils.CfLog;
import com.xtree.base.utils.DomainUtil;
import com.xtree.base.utils.SPUtil;
import com.xtree.base.utils.TagUtils;
import com.xtree.base.widget.BrowserDialog;
import com.xtree.mine.BR;
import com.xtree.mine.R;
import com.xtree.mine.data.Spkey;
import com.xtree.mine.databinding.ActivityLoginBinding;
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
            binding.loginArea.setVisibility(View.VISIBLE);
            binding.meRegisterArea.setVisibility(View.GONE);
        }
        if (viewType == REGISTER_TYPE) {
            binding.meRegisterArea.setVisibility(View.VISIBLE);
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
        binding.tvwCs.setOnClickListener(v -> goCustomerService());

        binding.btnLogin.setOnClickListener(v -> {
            if (!ifAgree()) {
                ToastUtils.showLong(getResources().getString(R.string.me_agree_hint));
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

        });
        binding.tvwAgreementRegister.setOnClickListener(v -> {
            //new XPopup.Builder(LoginRegisterActivity.this).asCustom().show();
        });


        binding.edtAccReg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.toString().length() < 6 || charSequence.toString().length() > 12) {
                    //ToastUtils.showLong(R.string.txt_user_name_should_6_12);
                }
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
                if (charSequence.toString().length() < 6 || charSequence.toString().length() > 12) {
                    //ToastUtils.showLong(R.string.txt_pwd_should_6_12);
                    //binding.edtPwd1.setError(getString(R.string.txt_pwd_should_6_12));
                }
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
                if (charSequence.length() < 6 || charSequence.length() > 12) {
                    //ToastUtils.showLong(R.string.txt_pwd_should_6_12);
                    //binding.edtPwd2.setError(getString(R.string.txt_pwd_should_6_12));
                }
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
                return;
            }
            if (TextUtils.isEmpty(account)) {
                ToastUtils.showLong(getResources().getString(R.string.me_account_hint));
                binding.edtAccReg.performClick();
                return;
            }

            if (account.length() < 6 || account.length() > 12) {
                ToastUtils.showLong(getResources().getString(R.string.txt_user_name_should_6_12));
                return;
            }

            if (TextUtils.isEmpty(pwd1)) {
                ToastUtils.showLong(getResources().getString(R.string.me_pwd_hint));
                return;
            }

            if (pwd1.length() < 6 || pwd1.length() > 12) {
                ToastUtils.showLong(getResources().getString(R.string.txt_pwd_should_6_12));
                return;
            }

            if (TextUtils.isEmpty(pwd2)) {
                ToastUtils.showLong(R.string.txt_enter_pwd_again);
                return;
            }

            if (!pwd2.equals(pwd1)) {
                ToastUtils.showLong(R.string.txt_pwd_should_same);
                return;
            }

            //验证输入参数
            viewModel.register(account, pwd1);

        });

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

    private void goCustomerService() {
        String title = getString(R.string.txt_custom_center);
        String url = DomainUtil.getDomain2() + Constant.URL_CUSTOMER_SERVICE;
        new XPopup.Builder(this).asCustom(new BrowserDialog(this, title, url)).show();
    }

    private void goMain() {
        ARouter.getInstance().build(RouterActivityPath.Main.PAGER_MAIN)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK)
                .navigation();
        finish();
    }

    private void goForgetPassword() {
        startContainerFragment(RouterFragmentPath.Mine.PAGER_FORGET_PASSWORD); // 忘记密码
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
}
