package com.xtree.mine.ui.activity;

import static com.xtree.base.utils.EventConstant.EVENT_TOP_SPEED_FAILED;
import static com.xtree.base.utils.EventConstant.EVENT_TOP_SPEED_FINISH;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.xtree.base.global.Constant;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.net.HttpCallBack;
import com.xtree.base.router.RouterActivityPath;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.base.utils.AESUtil;
import com.xtree.base.utils.AppUtil;
import com.xtree.base.utils.BitmapUtil;
import com.xtree.base.utils.CfLog;
import com.xtree.base.utils.ClickUtil;
import com.xtree.base.utils.ClipboardUtil;
import com.xtree.base.utils.SPUtil;
import com.xtree.base.utils.StringUtils;
import com.xtree.base.utils.TagUtils;
import com.xtree.base.vo.EventVo;
import com.xtree.base.vo.PromotionCodeVo;
import com.xtree.base.widget.ConfigSwitchDialog;
import com.xtree.base.widget.LoadingDialog;
import com.xtree.base.widget.MsgDialog;
import com.xtree.mine.BR;
import com.xtree.mine.BuildConfig;
import com.xtree.mine.R;
import com.xtree.mine.data.Spkey;
import com.xtree.mine.databinding.ActivityLoginBinding;
import com.xtree.mine.ui.fragment.AgreementDialog;
import com.xtree.mine.ui.fragment.GoogleAuthDialog;
import com.xtree.mine.ui.viewmodel.LoginViewModel;
import com.xtree.mine.ui.viewmodel.factory.AppViewModelFactory;
import com.xtree.mine.vo.LoginResultVo;
import com.xtree.mine.vo.SettingsVo;
import com.xtree.mine.vo.RegisterVerificationCodeVo;
import com.xtree.weight.TopSpeedDomainFloatingWindows;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import me.xtree.mvvmhabit.base.BaseActivity;
import me.xtree.mvvmhabit.http.BusinessException;
import me.xtree.mvvmhabit.utils.KLog;
import me.xtree.mvvmhabit.utils.SPUtils;
import me.xtree.mvvmhabit.utils.ToastUtils;

@Route(path = RouterActivityPath.Mine.PAGER_LOGIN_REGISTER)
public class LoginRegisterActivity extends BaseActivity<ActivityLoginBinding, LoginViewModel> {

    private int viewType;//页面跳转状态

    private boolean loginRegType = false;//登录状态获取验证码 状态 默认不需要获取验证码状态
    private static final int HANDLER_INIT_VER = 0;//初始化获取注册验证码
    private static final int HANDLER_REFRESH_VER = 1;//手动点击获取注册验证码
    private static final int HANDLER_ERR_VER = 2;//获取验证码异常
    private static final int HANDLER_REFRESH_IMAGE_VER = 3;//刷新图片UI
    private static final int HANDLER_REFRESH_REG_VER_VIEW = 9;//刷新显示注册二维码View

    private static final int HANDLER_INIT_VER_LOGIN = 4;//初始化登录获取验证码
    private static final int HANDLER_REFRESH_VER_LOGIN = 5;//手动点击获取登录获取验证码
    private static final int HANDLER_ERR_VER_LOGIN = 6;//获取验证码异常
    private static final int HANDLER_REFRESH_IMAGE_VER_LOGIN = 7;//刷新图片UI
    private static final int HANDLER_REFRESH_VER_LOGIN_VIEW = 8;//刷新显示登录二维码UI

    private RegisterVerificationHandler mRegisterHandler;
    protected Handler mainThreadHandler;

    private class RegisterVerificationHandler extends Handler {

        RegisterVerificationHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HANDLER_INIT_VER:
                case HANDLER_REFRESH_VER:

                case HANDLER_REFRESH_VER_LOGIN:
                    viewModel.getCaptcha();//获取注册验证码
                    break;
                case HANDLER_INIT_VER_LOGIN:
                    viewModel.getLoginCaptcha();//获取登录验证码
                    break;
                case HANDLER_ERR_VER:
                    if (msg.obj.toString() != null || !TextUtils.isEmpty(msg.obj.toString())) {
                        ToastUtils.showError(msg.obj.toString());
                        binding.tvwPwdCheckVerification.setVisibility(View.VISIBLE);
                        binding.tvwPwdCheckVerification.setText(msg.obj.toString());

                    } else {
                        CfLog.e("*******************RegisterVerificationHandler  Error *******************  ");
                    }

                    break;
                case HANDLER_ERR_VER_LOGIN:
                    if (msg.obj.toString() != null || !TextUtils.isEmpty(msg.obj.toString())) {
                        ToastUtils.showError(msg.obj.toString());
                        binding.tvwPwdCheckLoginVerification.setVisibility(View.VISIBLE);
                        binding.tvwPwdCheckLoginVerification.setText(msg.obj.toString());

                    } else {
                        CfLog.e("*******************RegisterVerificationHandler  Error *******************  ");
                    }
                    break;
                case HANDLER_REFRESH_IMAGE_VER:

                    refreshVeriImage((RegisterVerificationCodeVo) msg.obj);
                    break;
                case HANDLER_REFRESH_IMAGE_VER_LOGIN:
                    refreshLoginVeriImage((RegisterVerificationCodeVo) msg.obj);
                    break;
                case HANDLER_REFRESH_VER_LOGIN_VIEW:
                    refreshLoginVerView();
                    break;
                case HANDLER_REFRESH_REG_VER_VIEW:
                    if (viewType == LOGIN_TYPE) {
                        //登录状态不刷新注册页面验证码
                    } else {
                        refreshRegVerView();
                    }
                    //
                    break;
            }
        }

    }

    private void refreshLoginVerView() {
        binding.toRegisterArea.setVisibility(View.VISIBLE);
        binding.loginArea.setVisibility(View.VISIBLE);
        binding.llLoginVerification.setVisibility(View.VISIBLE);//显示登录二维码
        binding.toLoginArea.setVisibility(View.GONE);
        binding.meRegisterArea.setVisibility(View.GONE);
        binding.tvwRegisterWarning.setVisibility(View.GONE);
        binding.ivwRegisterWarning.setVisibility(View.GONE);

        Message msg = new Message();
        msg.what = HANDLER_INIT_VER_LOGIN;
        sendMessage(msg);

    }

    private void refreshRegVerView() {
        binding.toLoginArea.setVisibility(View.VISIBLE);
        binding.meRegisterArea.setVisibility(View.VISIBLE);
        binding.llEdtVerification.setVisibility(View.VISIBLE);
        hideRegister();
        binding.toRegisterArea.setVisibility(View.GONE);
        binding.loginArea.setVisibility(View.GONE);
        binding.tvwRegisterWarning.setVisibility(View.VISIBLE);
        binding.ivwRegisterWarning.setVisibility(View.VISIBLE);

        Message msg = new Message();
        msg.what = HANDLER_REFRESH_VER;
        sendMessage(msg);

    }

    private void refreshLoginVeriImage(final RegisterVerificationCodeVo vo) {
        if (!TextUtils.isEmpty(vo.image_url)) {
            binding.tvwPwdCheckVerification.setVisibility(View.INVISIBLE);
            Bitmap bitmap = BitmapUtil.base64StrToBitmap(vo.image_url);
            binding.ivLoginVerification.setImageBitmap(bitmap);
        } else {

        }
    }

    private void refreshVeriImage(final RegisterVerificationCodeVo vo) {
        if (!TextUtils.isEmpty(vo.image_url)) {
            binding.tvwPwdCheckVerification.setVisibility(View.INVISIBLE);
            Bitmap bitmap = BitmapUtil.base64StrToBitmap(vo.image_url);
            binding.ivVerification.setImageBitmap(bitmap);
        } else {

        }

    }

    public static final String ENTER_TYPE = "enter_type";
    public static final int LOGIN_TYPE = 0x1001;
    public static final int REGISTER_TYPE = 0x1002;

    private int clickCount = 0; // 点击次数 debug model
    private boolean mIsAcc = false;
    private boolean mIsPwd1 = false;
    private boolean mIsPwd2 = false;
    // private boolean mIsVer = true;//验证码
    private BasePopupView verifyPopView;//认证PoPView
    private SettingsVo settingsVo;
    private PromotionCodeVo promotionCodeVo;
    private String code;//剪切板获取的code
    private TopSpeedDomainFloatingWindows mTopSpeedDomainFloatingWindows;

    private RegisterVerificationCodeVo registerVerificationCodeVo;//注册验证码
    private RegisterVerificationCodeVo loginRegCodeVo;//登录验证码

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_login;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void initData() {
        viewModel.getSettings();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        viewModel.getSettings();
    }

    @Override
    public void initView() {

        if (BuildConfig.DEBUG) {
            binding.tvConfig.setVisibility(View.VISIBLE);
            binding.tvConfig.setOnClickListener(v -> new ConfigSwitchDialog().show(getSupportFragmentManager(), ConfigSwitchDialog.class.getName()));
        }


        mRegisterHandler = new RegisterVerificationHandler((Looper.getMainLooper()));
        mainThreadHandler = new Handler();

        mTopSpeedDomainFloatingWindows = new TopSpeedDomainFloatingWindows(this);
        mTopSpeedDomainFloatingWindows.show();
        binding.llRoot.setOnClickListener(v -> hideKeyBoard());
        binding.loginSubHeader.setOnClickListener(v -> {
            if (clickCount++ > 5) {
                clickCount = 0;
                startContainerFragment(RouterFragmentPath.Home.PG_DEBUG);
            }
        });

        Intent intent = getIntent();
        viewType = intent.getIntExtra(ENTER_TYPE, LOGIN_TYPE);
        //登录页面
        if (viewType == LOGIN_TYPE) {
            binding.toRegisterArea.setVisibility(View.VISIBLE);
            binding.loginArea.setVisibility(View.VISIBLE);
            binding.toLoginArea.setVisibility(View.GONE);
            binding.meRegisterArea.setVisibility(View.GONE);
            binding.tvwRegisterWarning.setVisibility(View.GONE);
            binding.ivwRegisterWarning.setVisibility(View.GONE);
        }
        //注册页面
        if (viewType == REGISTER_TYPE) {
            binding.toLoginArea.setVisibility(View.VISIBLE);
            binding.meRegisterArea.setVisibility(View.VISIBLE);
            hideRegister();
            binding.toRegisterArea.setVisibility(View.GONE);
            binding.loginArea.setVisibility(View.GONE);
            binding.tvwRegisterWarning.setVisibility(View.VISIBLE);
            binding.ivwRegisterWarning.setVisibility(View.VISIBLE);
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
        binding.tvwSkipLogin.setOnClickListener(v -> goMain(false));
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
            if (!loginRegType) {
                viewModel.login(acc, pwd);
            } else {
                String loginVerText = binding.edtLoginVerification.getText().toString();
                //验证码不能为空
                if (TextUtils.isEmpty(loginVerText)) {
                    binding.tvwPwdCheckVerification.setVisibility(View.VISIBLE);
                    binding.tvwPwdCheckVerification.setText(R.string.txt_otp_can_not_null);
                    ToastUtils.showError(this.getText(R.string.txt_otp_can_not_null));
                    return;
                }
                //验证码不是数字 验证码长度不是4位
                if (!StringUtils.isNumber(loginVerText) || loginVerText.length() != 4) {
                    binding.tvwPwdCheckVerification.setVisibility(View.VISIBLE);
                    binding.tvwPwdCheckVerification.setText(R.string.txt_otp_not_four_number);
                    ToastUtils.showError(this.getText(R.string.txt_otp_not_four_number));
                    return;
                }
                viewModel.loginAndVer(acc, pwd, loginRegCodeVo.key, loginVerText);
            }

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
            binding.tvwRegisterWarning.setVisibility(View.VISIBLE);
            binding.ivwRegisterWarning.setVisibility(View.VISIBLE);
            //读取Seting接口 获取是否展示注册二维码View
            if (settingsVo != null && TextUtils.equals("1", settingsVo.register_captcha_switch)) {
                binding.llEdtVerification.setVisibility(View.VISIBLE);
                //显示注册页面 刷新 获取 注册验证码
                Message msg = new Message();
                msg.what = HANDLER_INIT_VER;
                sendMessage(msg);
            }

        });
        binding.toLoginArea.setOnClickListener(v -> {
            binding.toRegisterArea.setVisibility(View.VISIBLE);
            binding.loginArea.setVisibility(View.VISIBLE);
            binding.toLoginArea.setVisibility(View.GONE);
            binding.meRegisterArea.setVisibility(View.GONE);
            hideRegister();
            binding.tvwRegisterWarning.setVisibility(View.GONE);
            binding.ivwRegisterWarning.setVisibility(View.GONE);
        });

        binding.edtAccReg.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (binding.edtAccReg.getText().toString().isEmpty()) {
                    binding.tvwUsernameWarning.setVisibility(View.VISIBLE);
                    binding.tvwUsernameWarning.setText(R.string.txt_username_empty);
                    mIsAcc = false;
                } else if (binding.edtAccReg.getText().toString().startsWith("0") || binding.edtAccReg.getText().toString().toLowerCase().startsWith("o")) {
                    binding.tvwUsernameWarning.setVisibility(View.VISIBLE);
                    binding.tvwUsernameWarning.setText(R.string.txt_user_name_should_not_0o);
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

                if (mIsAcc && mIsPwd1 && mIsPwd2) {
                    binding.btnRegister.setBackgroundResource(R.drawable.bg_register_enable);
                } else {
                    binding.btnRegister.setBackgroundResource(R.drawable.bg_register_unable);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.edtPwd1.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (binding.edtPwd1.getText().toString().isEmpty()) {
                    binding.tvwPwdWarning.setVisibility(View.VISIBLE);
                    binding.tvwPwdWarning.setText(R.string.txt_pwd_cannot_empty);
                    mIsPwd1 = false;
                } else if (binding.edtPwd1.getText().toString().length() < 6
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

                if (binding.edtPwd2.getText().toString().isEmpty()) {
                    mIsPwd2 = false;
                } else if (!binding.edtPwd1.getText().toString().equals(binding.edtPwd2.getText().toString())) {
                    binding.tvwPwdCheckWarning.setVisibility(View.VISIBLE);
                    binding.tvwPwdCheckWarning.setText(R.string.txt_pwd_should_same);
                    mIsPwd2 = false;
                } else {
                    binding.tvwPwdCheckWarning.setVisibility(View.INVISIBLE);
                    mIsPwd2 = true;
                }

                CfLog.i((mIsAcc && mIsPwd1 && mIsPwd2) + "");
                if (mIsAcc && mIsPwd1 && mIsPwd2) {
                    binding.btnRegister.setBackgroundResource(R.drawable.bg_register_enable);
                } else {
                    binding.btnRegister.setBackgroundResource(R.drawable.bg_register_unable);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.edtPwd2.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (binding.edtPwd2.getText().toString().isEmpty()) {
                    binding.tvwPwdCheckWarning.setVisibility(View.VISIBLE);
                    binding.tvwPwdCheckWarning.setText(R.string.txt_pwd_is_empty);
                    mIsPwd2 = false;
                } else if (!binding.edtPwd1.getText().toString().equals(binding.edtPwd2.getText().toString())) {
                    binding.tvwPwdCheckWarning.setVisibility(View.VISIBLE);
                    binding.tvwPwdCheckWarning.setText(R.string.txt_pwd_should_same);
                    mIsPwd2 = false;
                } else {
                    binding.tvwPwdCheckWarning.setVisibility(View.INVISIBLE);
                    mIsPwd2 = true;
                }

                CfLog.i((mIsAcc && mIsPwd1 && mIsPwd2) + "");
                if (mIsAcc && mIsPwd1 && mIsPwd2) {
                    binding.btnRegister.setBackgroundResource(R.drawable.bg_register_enable);
                } else {
                    binding.btnRegister.setBackgroundResource(R.drawable.bg_register_unable);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.btnRegister.setOnClickListener(view -> {

            String account = binding.edtAccReg.getText().toString().trim();
            String pwd1 = binding.edtPwd1.getText().toString();
            String pwd2 = binding.edtPwd2.getText().toString();
            String verificationTxt = binding.edtVerification.getText().toString();
            if (!binding.registerAgreementCheckbox.isChecked()) {
                ToastUtils.showLong(getResources().getString(R.string.me_agree_hint));
                showAgreementDialog(binding.registerAgreementCheckbox);
                return;
            }

            if (account.isEmpty()) {
                binding.tvwUsernameWarning.setVisibility(View.VISIBLE);
                binding.tvwUsernameWarning.setText(R.string.txt_username_empty);
                mIsAcc = false;
            }

            if (pwd1.isEmpty()) {
                binding.tvwPwdWarning.setVisibility(View.VISIBLE);
                binding.tvwPwdWarning.setText(R.string.txt_pwd_cannot_empty);
                mIsPwd1 = false;
            }

            if (pwd2.isEmpty()) {
                binding.tvwPwdCheckWarning.setVisibility(View.VISIBLE);
                binding.tvwPwdCheckWarning.setText(R.string.txt_pwd_is_empty);
                mIsPwd2 = false;
            }
            //注册页面需要判断Seting 状态 1需要判断
            if (settingsVo != null &&
                    settingsVo.register_captcha_switch != null
                    && TextUtils.equals("1", settingsVo.register_captcha_switch)) {
                //验证码不能为空
                if (verificationTxt.isEmpty()) {
                    binding.tvwPwdCheckVerification.setVisibility(View.VISIBLE);
                    binding.tvwPwdCheckVerification.setText(R.string.txt_otp_can_not_null);
                    ToastUtils.showError(this.getText(R.string.txt_otp_can_not_null));
                    // mIsVer = false;
                    return;
                }
                //验证码不是数字 验证码长度不是4位
                if (verificationTxt.length() != 4) {
                    binding.tvwPwdCheckVerification.setVisibility(View.VISIBLE);
                    binding.tvwPwdCheckVerification.setText(R.string.txt_otp_not_four_number);
                    ToastUtils.showError(this.getText(R.string.txt_otp_not_four_number));
                    //mIsVer = false;
                    return;
                }

            }

            if (!mIsAcc || !mIsPwd1 || !mIsPwd2) {
                return;
            }
             String netCode="";
            final String promotionCode=SPUtils.getInstance().getString(SPKeyGlobal.PROMOTION_CODE);
            if (!TextUtils.isEmpty(code)){
                netCode=code;
            }else if (!TextUtils.isEmpty(promotionCode)){
                netCode=promotionCode;
            }else {
                netCode="kygprka";
            }
            if (registerVerificationCodeVo !=null&&!TextUtils.isEmpty(registerVerificationCodeVo.key)){
                viewModel.register(account, pwd1, netCode, registerVerificationCodeVo.key, verificationTxt);
            }else{
                viewModel.register(account, pwd1, netCode, "", "");
            }
//            final String netCode =
//                    SPUtils.getInstance().getString(SPKeyGlobal.PROMOTION_CODE);
//            if (code != null && !TextUtils.isEmpty(code)
//                    &&registerVerificationCodeVo !=null
//                    &&!TextUtils.isEmpty(registerVerificationCodeVo.key)) {
//                viewModel.register(account, pwd1, code, registerVerificationCodeVo.key, verificationTxt);
//            } else if ( (netCode != null && !TextUtils.isEmpty(netCode))
//                    &&registerVerificationCodeVo !=null
//                    &&!TextUtils.isEmpty(registerVerificationCodeVo.key)) {
//                viewModel.register(account, pwd1, netCode, registerVerificationCodeVo.key, verificationTxt);
//            } else {
//                //增加
//                if (registerVerificationCodeVo != null
//                        && registerVerificationCodeVo.key != null
//                        && !TextUtils.isEmpty(registerVerificationCodeVo.key)
//                        && verificationTxt != null) {
//                    //为获取推广码 使用默认的推广码
//                    viewModel.register(account, pwd1, "kygprka", registerVerificationCodeVo.key, verificationTxt);
//                } else {
//                    viewModel.register(account, pwd1, "kygprka", "", "");
//                }
//            }
        });
        //点击 注册 验证码图片 手动刷新验证码图片
        binding.ivVerification.setOnClickListener(v -> {
            LoadingDialog.show(this);
            Message msg = new Message();
            msg.what = HANDLER_REFRESH_VER;
            sendMessage(msg);
        });

        //点击 登录页面 刷新验证码
        binding.ivLoginVerification.setOnClickListener(v -> {
            LoadingDialog.show(this);
            Message msg = new Message();
            msg.what = HANDLER_INIT_VER_LOGIN;
            sendMessage(msg);
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
                goMain(true);

            } else if (vo.twofa_required == 1) {
                CfLog.i("*********** 去谷歌验证...");
                GoogleAuthDialog dialog = new GoogleAuthDialog(this, this, () -> {
                    viewModel.setLoginSucc(vo);
                    TagUtils.tagEvent(getBaseContext(), TagUtils.EVENT_LOGIN);
                    goMain(true);
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
            //注册成功后直接登录
            if (vo != null && vo.userName != null && vo.userpass != null) {
                //viewModel.login(vo.userName, vo.userpass);
                viewModel.loginAndVerAuto(vo.userName, vo.userpass, vo.captcha);
            } else {
                CfLog.e("*********** userName /userpass is Null");
                goMain(false);
            }
        });
        //登录异常
        viewModel.liveDataLoginFail.observe(this, vo -> {
            if (vo.code == HttpCallBack.CodeRule.CODE_20208) {
                showVerifyDialog(vo);
            }

        });
        //登录异常 需要刷新出 登录二维码
        viewModel.liveDataLoginVerFail.observe(this, vo -> {
            if (vo.code == HttpCallBack.CodeRule.CODE_20204 ||
                    vo.code == HttpCallBack.CodeRule.CODE_20205 ||
                    vo.code == HttpCallBack.CodeRule.CODE_20206) {
                Message msg = new Message();
                msg.what = HANDLER_REFRESH_VER_LOGIN_VIEW;
                sendMessage(msg);
            }
        });
        //通过验证码注册 返回异常
        viewModel.regErrorLiveData.observe(this, vo -> {
            if (vo.code == HttpCallBack.CodeRule.CODE_20204 ||
                    vo.code == HttpCallBack.CodeRule.CODE_20205 ||
                    vo.code == HttpCallBack.CodeRule.CODE_20206) {
                Message msg = new Message();
                msg.what = HANDLER_REFRESH_VER;
                sendMessage(msg);
            }
        });
        //获取seting接口数据
        viewModel.liveDataSettings.observe(this, vo -> {
            settingsVo = vo;
            //注册页面是否显示 验证码
            if (TextUtils.equals("1", settingsVo.register_captcha_switch)) {
                Message msg = new Message();
                msg.what = HANDLER_REFRESH_REG_VER_VIEW;
                sendMessage(msg);
            }
        });
        //获取pro接口数据
        viewModel.promotionCodeVoMutableLiveData.observe(this, vo -> {
            promotionCodeVo = vo;
        });
        //获取注册验证码
        viewModel.verificationCodeMutableLiveData.observe(this, vo -> {
            registerVerificationCodeVo = vo;
            if (!TextUtils.isEmpty(registerVerificationCodeVo.image_url)) {
                Message msg = new Message();
                msg.what = HANDLER_REFRESH_IMAGE_VER;
                msg.obj = registerVerificationCodeVo;
                sendMessage(msg);
            } else {
                Message msg = new Message();
                msg.what = HANDLER_ERR_VER;
                msg.obj = registerVerificationCodeVo;
                sendMessage(msg);
            }
        });
        // 获取登录验证码
        viewModel.verLoginCodeMutableLiveData.observe(this, vo -> {
            loginRegType = true;// 登录状态获取验证码
            loginRegCodeVo = vo;
            if (!TextUtils.isEmpty(loginRegCodeVo.image_url)) {
                Message msg = new Message();
                msg.what = HANDLER_REFRESH_IMAGE_VER_LOGIN;
                msg.obj = loginRegCodeVo;
                sendMessage(msg);
            } else {
                Message msg = new Message();
                msg.what = HANDLER_ERR_VER_LOGIN;
                msg.obj = loginRegCodeVo;
                sendMessage(msg);
            }
        });
    }

    protected void sendMessage(Message message) {
        mRegisterHandler.sendMessage(message);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {

            String text = ClipboardUtil.getText(this);
            CfLog.e("onWindowFocusChanged=" + text);
            //promotionCode|*$#|ykrugupa 这种格式 是杏彩推广code形式
            if (text != null && text.contains("promotionCode|*$#|") || text.contains("promotionCode|")) {

                String[] strings = text.split("\\|");
                if (strings != null && strings.length == 3) {
                    String netCode = strings[2];
                    CfLog.e("ClipboardUtil = " + netCode);
                    code = netCode;
                } else {
                    CfLog.e("********strings.length!=3");
                }
            } else {
                viewModel.getPromotion();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // 用户点返回按钮时,要打开首页(有些页面跳转到登录页的同时,也会关闭自己)
        goMain(false);
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

    private void goMain(boolean isLogin) {
        KLog.i("isLogin", isLogin + "");
        ARouter.getInstance().build(RouterActivityPath.Main.PAGER_MAIN)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK)
                .withBoolean("isLogin", isLogin)
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

    /**
     * 显示认证Dialog
     *
     * @param vo
     */
    private void showVerifyDialog(final BusinessException vo) {
        String msg = getString(R.string.txt_ip_error_tip);
        final String title = getString(R.string.txt_kind_tips);
        verifyPopView = new XPopup.Builder(this)
                .dismissOnBackPressed(false)
                .dismissOnTouchOutside(false)
                .asCustom(new MsgDialog(this, title, msg, new MsgDialog.ICallBack() {
                    @Override
                    public void onClickLeft() {
                        verifyPopView.dismiss();
                    }

                    @Override
                    public void onClickRight() {
                        goSecurityVerify(vo);
                        verifyPopView.dismiss();
                    }
                }));
        verifyPopView.show();
    }

    /**
     * 跳转安全验证
     */
    private void goSecurityVerify(final BusinessException vo) {
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

    private void hideRegister() {
        binding.tvwUsernameWarning.setVisibility(View.INVISIBLE);
        binding.tvwPwdWarning.setVisibility(View.INVISIBLE);
        binding.tvwPwdCheckWarning.setVisibility(View.INVISIBLE);
        binding.tvwUsernameWarning.setText(R.string.txt_username_empty);
        binding.tvwPwdWarning.setText(R.string.txt_pwd_cannot_empty);
        binding.tvwPwdCheckWarning.setText(R.string.txt_pwd_is_empty);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventVo event) {
        switch (event.getEvent()) {
            case EVENT_TOP_SPEED_FINISH:
                mTopSpeedDomainFloatingWindows.refresh();
                break;
            case EVENT_TOP_SPEED_FAILED:
                mTopSpeedDomainFloatingWindows.onError();
                break;
        }
    }
}
