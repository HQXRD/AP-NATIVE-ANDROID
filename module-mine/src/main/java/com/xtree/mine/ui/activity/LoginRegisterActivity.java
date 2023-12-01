package com.xtree.mine.ui.activity;


import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CompoundButton;

import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.xtree.base.router.RouterActivityPath;
import com.xtree.base.utils.SPUtil;
import com.xtree.mine.BR;
import com.xtree.mine.R;
import com.xtree.mine.data.Spkey;
import com.xtree.mine.databinding.ActivityLoginBinding;
import com.xtree.mine.ui.viewmodel.LoginViewModel;
import com.xtree.mine.ui.viewmodel.factory.AppViewModelFactory;

import me.xtree.mvvmhabit.base.BaseActivity;
import me.xtree.mvvmhabit.utils.ToastUtils;
@Route(path = RouterActivityPath.Mine.PAGER_LOGIN_REGISTER)
public class LoginRegisterActivity extends BaseActivity<ActivityLoginBinding, LoginViewModel> {

    public static final String ENTER_TYPE = "enter_type";
    public static final int  LOGIN_TYPE = 0x1001;
    public static final int REGISTER_TYPE = 0x1002;

    public interface LoginCallback{

        public void loginSuccess();
        public void loginFailure();
    }

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
        String username = "testkite1002";
        String pwd = "kite123456";


    }
    @Override
    public void initView(){

        Intent intent = getIntent();
        int viewType = intent.getIntExtra(ENTER_TYPE,LOGIN_TYPE);
        if(viewType == LOGIN_TYPE){
            binding.toRegisterArea.setVisibility(View.VISIBLE);
            binding.loginArea.setVisibility(View.VISIBLE);
            binding.toLoginArea.setVisibility(View.GONE);
            binding.meRegisterArea.setVisibility(View.GONE);
        }
        if(viewType == REGISTER_TYPE){
            binding.toLoginArea.setVisibility(View.VISIBLE);
            binding.meRegisterArea.setVisibility(View.VISIBLE);
            binding.toRegisterArea.setVisibility(View.GONE);
            binding.loginArea.setVisibility(View.GONE);
        }

        boolean isRememberPwd = SPUtil.get(getApplication()).get(Spkey.REMEMBER_PWD,false);
        if(isRememberPwd){
            binding.mePwdInput.setText(SPUtil.get(getApplication()).get(Spkey.PWD,""));
            binding.meAccountInput.setText(SPUtil.get(getApplication()).get(Spkey.ACCOUNT,""));
            binding.meCheckbox.setChecked(true);
        }else{
            binding.meCheckbox.setChecked(false);
            binding.mePwdInput.setText("");
            binding.meAccountInput.setText("");
        }

        binding.meCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SPUtil.get(getApplication()).put(Spkey.REMEMBER_PWD, isChecked);
                if(isChecked){
                    if(!TextUtils.isEmpty(binding.mePwdInput.getText().toString())){
                        SPUtil.get(getApplication()).put(Spkey.PWD,binding.mePwdInput.getText().toString());
                        SPUtil.get(getApplication()).put(Spkey.ACCOUNT,binding.meAccountInput.getText().toString());
                    }
                }else{
                    SPUtil.get(getApplication()).put(Spkey.PWD,"");
                    SPUtil.get(getApplication()).put(Spkey.ACCOUNT,"");
                }
            }
        });

        binding.meBtnLogin.setOnClickListener(v -> {
            if(!ifAgree()){
                ToastUtils.showLong(getResources().getString(R.string.me_agree_hint));
                return;
            }

            if(TextUtils.isEmpty(binding.meAccountInput.getText().toString())){
                ToastUtils.showLong(getResources().getString(R.string.me_account_hint));
                return;
            }

            if(TextUtils.isEmpty(binding.mePwdInput.getText().toString())){
                ToastUtils.showLong(getResources().getString(R.string.me_pwd_hint));
                return;
            }
            viewModel.login(binding.meAccountInput.getText().toString(), binding.mePwdInput.getText().toString(), new LoginCallback() {
                @Override
                public void loginSuccess() {
                    ARouter.getInstance().build(RouterActivityPath.Main.PAGER_MAIN).navigation();
                    LoginRegisterActivity.this.finish();
                }

                @Override
                public void loginFailure() {

                }
            });
        });

        binding.toRegisterArea.setOnClickListener(v->{
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

        register();
    }
    private boolean ifAgree(){
        return binding.agreementCheckbox.isChecked();
    }

    private void register(){

        binding.meRegisterAccountInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if(charSequence.toString().length() < 6 || charSequence.toString().length() > 12){
                    ToastUtils.showLong("用户名为6到12位");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.meRegisterPwdInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if(charSequence.toString().length() < 6 || charSequence.toString().length() > 12){
                    ToastUtils.showLong("密码为6到12位");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.meReinputRegisterPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if(charSequence.toString().length() < 6 || charSequence.toString().length() > 12){
                    ToastUtils.showLong("密码为6到12位");
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.meBtnRegister.setOnClickListener(view -> {

            if(!binding.registerAgreementCheckbox.isChecked()){
                ToastUtils.showLong(getResources().getString(R.string.me_agree_hint));
                return;
            }
            if(TextUtils.isEmpty(binding.meRegisterAccountInput.getText().toString())){
                ToastUtils.showLong(getResources().getString(R.string.me_account_hint));
                return;
            }
            if(TextUtils.isEmpty(binding.meRegisterPwdInput.getText().toString())){
                ToastUtils.showLong(getResources().getString(R.string.me_pwd_hint));
                return;
            }

            if(TextUtils.isEmpty(binding.meReinputRegisterPwd.getText().toString())){
                ToastUtils.showLong("请再次输入密码");
                return;
            }

            if(!binding.meReinputRegisterPwd.getText().toString().equals(binding.meRegisterPwdInput.getText().toString())){
                ToastUtils.showLong("两次输入密码不一致");
                return;
            }

            //验证输入参数
            viewModel.register(this,binding.meRegisterAccountInput.getText().toString(),binding.meRegisterPwdInput.getText().toString());

        });

    }

    @Override
    public LoginViewModel initViewModel() {
        //使用自定义的ViewModelFactory来创建ViewModel，如果不重写该方法，则默认会调用LoginViewModel(@NonNull Application application)构造方法
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getApplication());
        return new ViewModelProvider(this, factory).get(LoginViewModel.class);
    }
}
