package com.xtree.mine.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.base.utils.AppUtil;
import com.xtree.base.utils.CfLog;
import com.xtree.mine.BR;
import com.xtree.mine.R;
import com.xtree.mine.databinding.FragmentForgetPasswordBinding;
import com.xtree.mine.ui.activity.LoginRegisterActivity;
import com.xtree.mine.ui.viewmodel.ForgetPasswordViewModel;
import com.xtree.mine.ui.viewmodel.factory.AppViewModelFactory;

import me.xtree.mvvmhabit.base.BaseFragment;

@Route(path = RouterFragmentPath.Mine.PAGER_FORGET_PASSWORD)
public class ForgetPasswordFragment extends BaseFragment<FragmentForgetPasswordBinding, ForgetPasswordViewModel> {
    private boolean mIsClickable = false;
    private String mEmailOPT = "";
    private String mPhoneOPT = "";
    private String mUsername = "";
    private String mSendType = "";
    private String mEmail = "";
    private String mPhone = "";
    private CountDownTimer mCountDownTimer = null;

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
    }

    @Override
    public void initView() {
        binding.ivwBack.setOnClickListener(v -> {
            if (mCountDownTimer != null) {
                mCountDownTimer.cancel();
            }
            getActivity().finish();
            Intent toLogin = new Intent(getContext(), LoginRegisterActivity.class);
            toLogin.putExtra(LoginRegisterActivity.ENTER_TYPE, LoginRegisterActivity.LOGIN_TYPE);
            startActivity(toLogin);
        });

        binding.llForgetPassword.setOnClickListener(v -> {
            hideKeyBoard();
        });

        binding.ivwCs.setOnClickListener(v -> AppUtil.goCustomerService(getContext()));

        binding.llCheckUsername.edtResetPasswordUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (binding.llCheckUsername.edtResetPasswordUsername.getText().toString().length() == 0) {
                    binding.llCheckUsername.twvResetPasswordUsername.setVisibility(View.VISIBLE);
                    binding.llCheckUsername.ivwResetPasswordNext.setImageDrawable(getResources().getDrawable(R.mipmap.me_reset_password_next_unable));
                    mIsClickable = false;
                } else {
                    binding.llCheckUsername.twvResetPasswordUsername.setVisibility(View.GONE);
                    binding.llCheckUsername.ivwResetPasswordNext.setImageDrawable(getResources().getDrawable(R.mipmap.me_reset_password_next_enable));
                    mIsClickable = true;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.llCheckUsername.ivwResetPasswordNext.setOnClickListener(v -> {
            if (mIsClickable) {
                CfLog.d("getForgetUserInfo");
                mUsername = binding.llCheckUsername.edtResetPasswordUsername.getText().toString();
                viewModel.getForgetUserInfo(mUsername);
                mIsClickable = false;
            }
        });

        binding.llCheckOtp.twvResetPasswordMail.setOnClickListener(v -> {
            binding.llCheckOtp.twvResetPasswordMail.setTextColor(getResources().getColor(R.color.clr_purple_bg01));
            binding.llCheckOtp.twvResetPasswordPhone.setTextColor(getResources().getColor(R.color.clr_grey_menu));
            binding.llCheckOtp.twvResetPasswordInfo.setText(mEmailOPT);
            mSendType = "email";
        });

        binding.llCheckOtp.twvResetPasswordPhone.setOnClickListener(v -> {
            binding.llCheckOtp.twvResetPasswordMail.setTextColor(getResources().getColor(R.color.clr_grey_menu));
            binding.llCheckOtp.twvResetPasswordPhone.setTextColor(getResources().getColor(R.color.clr_purple_bg01));
            binding.llCheckOtp.twvResetPasswordInfo.setText(mPhoneOPT);
            mSendType = "phone";
        });

        binding.llCheckOtp.btnGetOtp.setOnClickListener(v -> {
            CfLog.d("sendMessage");
            viewModel.sendMessage(mSendType);
        });

        binding.llCheckOtp.edtSetOpt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (binding.llCheckOtp.edtSetOpt.getText().toString().length() == 0) {
                    binding.llCheckOtp.twvSetOtpError.setVisibility(View.VISIBLE);
                    binding.llCheckOtp.twvSetOtpError.setText(R.string.txt_otp_can_not_null);
                    binding.llCheckOtp.ivwResetPasswordNext.setImageDrawable(getResources().getDrawable(R.mipmap.me_reset_password_next_unable));
                    mIsClickable = false;
                } else if (binding.llCheckOtp.edtSetOpt.getText().toString().length() == 6) {
                    binding.llCheckOtp.twvSetOtpError.setVisibility(View.GONE);
                    binding.llCheckOtp.ivwResetPasswordNext.setImageDrawable(getResources().getDrawable(R.mipmap.me_reset_password_next_enable));
                    mIsClickable = true;
                } else {
                    binding.llCheckOtp.twvSetOtpError.setVisibility(View.VISIBLE);
                    binding.llCheckOtp.twvSetOtpError.setText(R.string.txt_otp_not_six_number);
                    binding.llCheckOtp.ivwResetPasswordNext.setImageDrawable(getResources().getDrawable(R.mipmap.me_reset_password_next_unable));
                    mIsClickable = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.llCheckOtp.ivwResetPasswordNext.setOnClickListener(v -> {
            if (mIsClickable) {
                viewModel.sendMessageVerfyCode(binding.llCheckOtp.edtSetOpt.getText().toString(), mSendType);
                mIsClickable = false;
            }
        });

        binding.llResetPassword.edtResetPasswordResetPassword.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (binding.llResetPassword.edtResetPasswordResetPassword.getText().toString().length() < 6
                        || binding.llResetPassword.edtResetPasswordResetPassword.getText().toString().length() > 16) {
                    binding.llResetPassword.twvResetPasswordResetPasswordError.setVisibility(View.VISIBLE);
                    binding.llResetPassword.twvResetPasswordResetPasswordError.setText(R.string.txt_reset_password_length_error);
                } else if (hasCharRepeatedTriTimes(binding.llResetPassword.edtResetPasswordResetPassword.getText().toString())) {
                    binding.llResetPassword.twvResetPasswordResetPasswordError.setVisibility(View.VISIBLE);
                    binding.llResetPassword.twvResetPasswordResetPasswordError.setText(R.string.txt_reset_password_tri_same_error);
                } else if (!containsLetterAndDigit(binding.llResetPassword.edtResetPasswordResetPassword.getText().toString())) {
                    binding.llResetPassword.twvResetPasswordResetPasswordError.setVisibility(View.VISIBLE);
                    binding.llResetPassword.twvResetPasswordResetPasswordError.setText(R.string.txt_reset_password_form_error);
                } else {
                    binding.llResetPassword.twvResetPasswordResetPasswordError.setVisibility(View.GONE);
                }

                if (!binding.llResetPassword.edtResetPasswordResetPassword.getText().toString().equals(binding.llResetPassword.edtResetPasswordResetPasswordRecheck.getText().toString())) {
                    binding.llResetPassword.twvResetPasswordResetPasswordRecheckError.setVisibility(View.VISIBLE);
                    binding.llResetPassword.twvResetPasswordResetPasswordRecheckError.setText(R.string.txt_reset_password_not_same_error);
                    mIsClickable = false;
                } else {
                    binding.llResetPassword.twvResetPasswordResetPasswordRecheckError.setVisibility(View.GONE);
                    mIsClickable = true;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.llResetPassword.edtResetPasswordResetPasswordRecheck.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!binding.llResetPassword.edtResetPasswordResetPassword.getText().toString().equals(binding.llResetPassword.edtResetPasswordResetPasswordRecheck.getText().toString())) {
                    binding.llResetPassword.twvResetPasswordResetPasswordRecheckError.setVisibility(View.VISIBLE);
                    binding.llResetPassword.twvResetPasswordResetPasswordRecheckError.setText(R.string.txt_reset_password_not_same_error);
                    mIsClickable = false;
                } else {
                    binding.llResetPassword.twvResetPasswordResetPasswordRecheckError.setVisibility(View.GONE);
                    mIsClickable = true;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.llResetPassword.iwmResetPasswordConfirm.setOnClickListener(v -> {
            if (mIsClickable) {
                viewModel.sendChangePasswordSuccessful(binding.llResetPassword.edtResetPasswordResetPasswordRecheck.getText().toString());
                mIsClickable = false;
            }
        });

        binding.llResetPassword.iwmResetPasswordReset.setOnClickListener(v -> {
            binding.llResetPassword.edtResetPasswordResetPassword.setText("");
            binding.llResetPassword.edtResetPasswordResetPasswordRecheck.setText("");
            binding.llResetPassword.twvResetPasswordResetPasswordRecheckError.setVisibility(View.GONE);
        });

        binding.llResetPassword.ckbResetPasswordResetPassword.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                binding.llResetPassword.edtResetPasswordResetPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            } else {
                binding.llResetPassword.edtResetPasswordResetPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
        });

        binding.llResetPassword.ckbResetPasswordRecheck.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                binding.llResetPassword.edtResetPasswordResetPasswordRecheck.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            } else {
                binding.llResetPassword.edtResetPasswordResetPasswordRecheck.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
        });

        binding.llFinish.ivwResetPasswordNext.setOnClickListener(v -> {
            getActivity().finish();
            Intent toLogin = new Intent(getContext(), LoginRegisterActivity.class);
            toLogin.putExtra(LoginRegisterActivity.ENTER_TYPE, LoginRegisterActivity.LOGIN_TYPE);
            startActivity(toLogin);
        });
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.llCheckUsername.clCheckUsername.setVisibility(View.VISIBLE);
        binding.llCheckOtp.clCheckOtp.setVisibility(View.GONE);
        binding.llResetPassword.clResetPassword.setVisibility(View.GONE);
        binding.llFinish.clFinish.setVisibility(View.GONE);
        binding.llResetPassword.edtResetPasswordResetPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        binding.llResetPassword.edtResetPasswordResetPasswordRecheck.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_forget_password;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public ForgetPasswordViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getActivity().getApplication());
        return new ViewModelProvider(this, factory).get(ForgetPasswordViewModel.class);
    }

    @Override
    public void initViewObservable() {
        viewModel.liveDataUserInfo.observe(this, vo -> {
            mEmail = vo.email;
            mPhone = vo.phone;
            mEmailOPT = String.format(getResources().getString(R.string.txt_check_your_email), mUsername, mEmail);
            mPhoneOPT = String.format(getResources().getString(R.string.txt_check_your_phone), mUsername, mPhone);
            if (!vo.email.isEmpty() && !vo.phone.isEmpty()) {
                binding.llCheckOtp.twvResetPasswordMail.setVisibility(View.VISIBLE);
                binding.llCheckOtp.twvResetPasswordPhone.setVisibility(View.VISIBLE);
                mSendType = "email";
                binding.llCheckOtp.twvResetPasswordInfo.setText(mEmailOPT);
            } else {
                binding.llCheckOtp.twvResetPasswordMail.setVisibility(View.GONE);
                binding.llCheckOtp.twvResetPasswordPhone.setVisibility(View.GONE);
                if (!vo.email.isEmpty()) {
                    mSendType = "email";
                    binding.llCheckOtp.twvResetPasswordInfo.setText(mEmailOPT);
                } else {
                    mSendType = "phone";
                    binding.llCheckOtp.twvResetPasswordInfo.setText(mPhoneOPT);
                }
            }
            binding.llCheckUsername.clCheckUsername.setVisibility(View.GONE);
            binding.llCheckOtp.clCheckOtp.setVisibility(View.VISIBLE);
        });

        viewModel.liveDataCheckSendMessageSuccess.observe(this, this::countDown);

        viewModel.liveDataToken.observe(this, vo -> {
            if (mCountDownTimer != null) {
                mCountDownTimer.cancel();
            }
            binding.llCheckOtp.clCheckOtp.setVisibility(View.GONE);
            binding.llResetPassword.clResetPassword.setVisibility(View.VISIBLE);
        });

        viewModel.liveDataCheckPasswordSuccess.observe(this, vo -> {
            binding.llResetPassword.clResetPassword.setVisibility(View.GONE);
            binding.llFinish.clFinish.setVisibility(View.VISIBLE);
        });
    }

    private void countDown(int time) {
        final int[] countTime = {time};
        mCountDownTimer = new CountDownTimer(time * 1000L, 1000L) {
            @Override
            public void onTick(long millisUntilFinished) {
                binding.llCheckOtp.btnGetOtp.setEnabled(false);
                binding.llCheckOtp.btnGetOtp.setText(countTime[0] + "S");
                countTime[0]--;
                //CfLog.i(countTime[0] + "");
            }

            @Override
            public void onFinish() {
                binding.llCheckOtp.btnGetOtp.setEnabled(true);
                binding.llCheckOtp.btnGetOtp.setText(getString(R.string.txt_get_otp));
            }
        }.start();
    }

    public boolean hasCharRepeatedTriTimes(String str) {
        for (int i = 0; i < str.length() - 2; i++) {
            char currentChar = str.charAt(i);
            if (currentChar == str.charAt(i + 1) && currentChar == str.charAt(i + 2)) {
                return true;
            }
        }
        return false;
    }

    public boolean containsLetterAndDigit(String str) {
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
