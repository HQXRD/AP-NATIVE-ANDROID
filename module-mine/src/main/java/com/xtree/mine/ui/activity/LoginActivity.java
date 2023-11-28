package com.xtree.mine.ui.activity;


import android.os.Bundle;
import android.text.TextUtils;
import android.widget.CompoundButton;

import androidx.lifecycle.ViewModelProvider;

import com.xtree.base.utils.SPUtil;
import com.xtree.mine.BR;
import com.xtree.mine.R;
import com.xtree.mine.data.Spkey;
import com.xtree.mine.databinding.ActivityLoginBinding;
import com.xtree.mine.ui.viewmodel.LoginViewModel;
import com.xtree.mine.ui.viewmodel.factory.AppViewModelFactory;

import me.xtree.mvvmhabit.base.BaseActivity;
import me.xtree.mvvmhabit.utils.ToastUtils;

public class LoginActivity extends BaseActivity<ActivityLoginBinding, LoginViewModel> {

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

        String username = "testkite1002";
        String pwd = "kite123456";

        initView();

    }
    private void initView(){


        boolean isRememberPwd = SPUtil.get(getApplication()).get(Spkey.REMEMBER_PWD,false);
        if(isRememberPwd){
            binding.mePwdInput.setText(SPUtil.get(getApplication()).get(Spkey.PWD,""));
            binding.meCheckbox.setChecked(true);
        }else{
            binding.meCheckbox.setChecked(false);
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
            viewModel.login(getApplication(),binding.meAccountInput.getText().toString(),binding.mePwdInput.getText().toString());
        });
    }
    private boolean ifAgree(){
        return binding.agreementCheckbox.isChecked();
    }

    @Override
    public LoginViewModel initViewModel() {
        //使用自定义的ViewModelFactory来创建ViewModel，如果不重写该方法，则默认会调用LoginViewModel(@NonNull Application application)构造方法
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getApplication());
        return new ViewModelProvider(this, factory).get(LoginViewModel.class);
    }
}
