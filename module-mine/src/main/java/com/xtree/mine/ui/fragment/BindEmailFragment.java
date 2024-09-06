package com.xtree.mine.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lxj.xpopup.XPopup;
import com.xtree.base.global.Constant;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.router.RouterActivityPath;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.base.utils.AppUtil;
import com.xtree.base.utils.CfLog;
import com.xtree.base.utils.TagUtils;
import com.xtree.base.utils.UuidUtil;
import com.xtree.base.vo.ProfileVo;
import com.xtree.base.widget.LoadingDialog;
import com.xtree.mine.BR;
import com.xtree.mine.R;
import com.xtree.mine.databinding.FragmentBindEmailBinding;
import com.xtree.mine.ui.viewmodel.VerifyViewModel;
import com.xtree.mine.ui.viewmodel.factory.AppViewModelFactory;
import com.xtree.mine.vo.VerifyVo;

import java.util.HashMap;
import java.util.Map;

import me.xtree.mvvmhabit.base.BaseFragment;
import me.xtree.mvvmhabit.utils.SPUtils;
import me.xtree.mvvmhabit.utils.ToastUtils;

@Route(path = RouterFragmentPath.Mine.PAGER_BIND_EMAIL)
public class BindEmailFragment extends BaseFragment<FragmentBindEmailBinding, VerifyViewModel> {

    private static final String ARG_TYPE = "type";
    private static final String ARG_TYPE2 = "type2"; // 绑定成功后,办理其它业务
    private static final String ARG_TOKEN_SIGN = "tokenSign";
    private static final int MSG_TIMER = 3001;
    private String typeName;
    private String typeName2;
    private String tokenSign;
    private VerifyVo mVerifyVo;
    private String sendtype = "email";
    private TextWatcher textWatcher;

    Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == MSG_TIMER) {
                String str = binding.tvwCode.getText().toString();
                if (str.contains("s") && Integer.parseInt(str.replace("s", "")) > 0) {
                    int sec = Integer.parseInt(str.replace("s", ""));
                    sec--;
                    binding.tvwCode.setText(sec + "s");
                    mHandler.sendEmptyMessageDelayed(MSG_TIMER, 1000L);
                } else {
                    CfLog.i("************ ");
                    binding.tvwCode.setEnabled(true);
                    binding.tvwCode.setSelected(true);
                    binding.tvwCode.setText(R.string.txt_get_code);
                    if (!binding.edtNum.getText().toString().contains("*")) {
                        binding.edtNum.setEnabled(true);
                    }
                }

            }
        }
    };

    public static BindEmailFragment newInstance(String typeName, String tokenSign) {
        BindEmailFragment fragment = new BindEmailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TYPE, typeName);
        args.putString(ARG_TOKEN_SIGN, tokenSign);
        fragment.setArguments(args);
        return fragment;
    }

    public static BindEmailFragment newInstance(Bundle args) {
        BindEmailFragment fragment = new BindEmailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void initView() {
        binding.tvwOk.setEnabled(false);
        binding.llRoot.setOnClickListener(v -> hideKeyBoard());
        binding.tvwCode.setOnClickListener(v -> {
            binding.edtNum.setEnabled(false);
            LoadingDialog.show(getContext());
            getCode();
        });
        binding.tvwCs.setOnClickListener(v -> AppUtil.goCustomerService(getContext()));
        binding.tvwOk.setOnClickListener(v -> submit());
        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setBtn();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };
        TextWatcher textWatcherNum = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setNum();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };
        TextWatcher textWatcherCode = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setCode();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };
        binding.edtNum.addTextChangedListener(textWatcherNum);
        binding.edtCode.addTextChangedListener(textWatcherCode);
        binding.edtNum.addTextChangedListener(textWatcher);
        binding.edtCode.addTextChangedListener(textWatcher);
    }

    @Override
    public void initData() {
        super.initData();
        if (getArguments() != null) {
            typeName = getArguments().getString(ARG_TYPE);
            tokenSign = getArguments().getString(ARG_TOKEN_SIGN);
            typeName2 = getArguments().getString(ARG_TYPE2);
        }
        CfLog.i("****** typeName: " + typeName + ", tokenSign: " + tokenSign + ", typeName2: " + typeName2);

        String json = SPUtils.getInstance().getString(SPKeyGlobal.HOME_PROFILE);
        ProfileVo mProfileVo = new Gson().fromJson(json, ProfileVo.class);
        if (mProfileVo != null && mProfileVo.is_binding_email
                && !Constant.UPDATE_EMAIL2.equals(typeName)
                && !Constant.VERIFY_BIND_EMAIL2.equals(typeName)) {
            binding.tvwTitle.setText(R.string.txt_email_addr_already);
            binding.vDivLine.setVisibility(View.GONE);
            binding.tvwTipHint.setHeight(0);
            binding.edtNum.setEnabled(false); // 放在前面
            binding.edtNum.setText(mProfileVo.binding_email_info);
            binding.edtNum.setTextColor(getResources().getColor(R.color.clr_sec_verify_grey));
        }

        if (Constant.VERIFY_LOGIN.equals(typeName)) {
            String num = getArguments().getString(sendtype);
            String map = getArguments().getString("map");
            String username = getArguments().getString("username");
            CfLog.d(typeName + ", " + username + ", " + num + ", " + map);
            binding.edtNum.setEnabled(false);
            binding.edtNum.setText(num);
        }
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_bind_email;
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
    public void onDestroy() {
        mHandler.removeMessages(MSG_TIMER);
        super.onDestroy();
    }

    /**
     * 发送验证码
     */
    private void getCode() {
        Map<String, String> map = new HashMap<>();
        String num = binding.edtNum.getText().toString();
        CfLog.d("typeName: " + typeName + ", num: " + num);

        if (Constant.BIND_EMAIL.equals(typeName)) {
            // 发验证码 到新邮箱, 绑邮箱用的
            //@GET("/api/verify/singlesend")
            if (num.length() > 4 && num.contains("@")) {
                viewModel.singleSend1("bind", sendtype, num);
            } else {
                CfLog.d("phone/email is null or error...");
                if (!num.contains("*")) {
                    binding.edtNum.setEnabled(true);
                }
                LoadingDialog.finish();
            }

        } else if (Constant.RESET_LOGIN_PASSWORD.equals(typeName)) {
            // 发验证码 修改密码用的
            //@GET("/api/verify/singlesend")
            viewModel.singleSend2("resetloginpassword", sendtype, "");

        } else if (Constant.UPDATE_EMAIL.equals(typeName)) {
            // 发验证码 到老邮箱 更新邮箱用的 CHECK
            // /api/verify/updateverify
            map.put("flag", "initialSend");
            map.put("sendtype", sendtype);
            map.put("updatetype", sendtype);
            map.put("num", ""); // UI有*号
            map.put("nonce", UuidUtil.getID16());
            viewModel.updateVerify1(map);

        } else if (Constant.UPDATE_EMAIL2.equals(typeName)) {
            // 再一次 发验证码 到新邮箱 更新邮箱用的， 然后 成功关闭
            // /api/verify/updateverify
            map.put("flag", "updateSend");
            map.put("sendtype", sendtype);
            map.put("token", mVerifyVo.tokenSign);
            map.put("num", num); // 新输入的
            map.put("nonce", UuidUtil.getID16());
            viewModel.updateVerify3(map);

        } else if (Constant.VERIFY_BIND_PHONE.equals(typeName)) {
            // 邮箱验证后绑定手机 (发邮箱验证码) 第一步
            // /api/verify/bindverify
            map.put("flag", "initialSend");
            map.put("sendtype", sendtype);
            //map.put("updatetype", "phone");  // 网页没有
            map.put("num", ""); //
            map.put("nonce", UuidUtil.getID16());
            viewModel.bindVerify1(map);
        } else if (Constant.VERIFY_BIND_EMAIL2.equals(typeName)) {
            // 手机验证后绑定邮箱 (发邮箱验证码) 第三步
            // /api/verify/bindverify
            map.put("flag", "bindSend");
            map.put("sendtype", sendtype);
            //map.put("updatetype", "phone");  // 网页没有
            map.put("num", num); //
            map.put("token", tokenSign); // mVerifyVo.tokenSign
            map.put("nonce", UuidUtil.getID16());
            viewModel.bindVerify3(map);
        } else if (Constant.VERIFY_LOGIN.equals(typeName)) {
            // 异地登录/换设备登录
            // /api/auth/sendCode
            String username = getArguments().getString("username");
            map.put("username", username);
            map.put("type", sendtype);
            viewModel.sendCodeByLogin(map);
        } else if (!TextUtils.isEmpty(typeName2)) {
            CfLog.i(typeName + ", " + typeName2 + ", " + sendtype + ", " + num);
            viewModel.singleSend3(typeName, sendtype, num); // 绑定+验证
        } else {
            // 获取验证码，然后 跳到其它业务，比如 绑USDT
            //@GET("/api/verify/singlesend")
            viewModel.singleSend3(typeName, sendtype, "");

        }

    }

    /**
     * 提交验证码, (验证收到的验证码)，然后 a:绑邮箱/b:换绑新邮箱1/c:换绑新邮箱2/d:跳到其它业务
     */
    private void submit() {
        String code = binding.edtCode.getText().toString();
        if (code.isEmpty()) {
            return;
        }

        Map<String, String> map = new HashMap<>();
        if (Constant.BIND_EMAIL.equals(typeName)) {
            // 绑定邮箱 新 提交
            //@POST("/api/verify/singleverify")
            map.put("code", code);
            map.put("flag", "bind");
            map.put("has_securitypwd", "false");
            map.put("istokenreturn", "true");
            map.put("securitypass", "");
            map.put("sendtype", sendtype); // phone/email
            map.put("nonce", UuidUtil.getID16());
            viewModel.singleVerify1(map);

        } else if (Constant.RESET_LOGIN_PASSWORD.equals(typeName)) {
            // 修改密码
            //@POST("/api/verify/singleverify")
            map.put("code", code);
            map.put("flag", "resetloginpassword");
            map.put("has_securitypwd", "false");
            map.put("istokenreturn", "true");
            //map.put("securitypass", "");
            map.put("sendtype", sendtype); // phone/email
            map.put("nonce", UuidUtil.getID16());
            viewModel.singleVerify2(map);

        } else if (Constant.UPDATE_EMAIL.equals(typeName)) {
            // 更新邮箱 输入老邮箱的验证码 提交 CHECK
            // /api/verify/updateverify
            map.put("flag", "initialVerify");
            map.put("sendtype", sendtype); // phone/email
            map.put("code", code);
            map.put("updatetype", sendtype);
            map.put("nonce", UuidUtil.getID16());
            viewModel.updateVerify2(map);

        } else if (Constant.UPDATE_EMAIL2.equals(typeName)) {
            // 更新邮箱 输入新邮箱的验证码 提交
            // /api/verify/updateverify
            map.put("flag", "updateVerify");
            map.put("sendtype", sendtype); // phone/email
            map.put("code", code);
            map.put("nonce", UuidUtil.getID16());
            viewModel.updateVerify4(map);

        } else if (Constant.VERIFY_BIND_EMAIL2.equals(typeName)) {
            // 手机验证后绑定邮箱 (绑邮箱)
            // /api/verify/bindverify
            map.put("flag", "bindVerify");
            map.put("sendtype", sendtype); // phone/email
            map.put("code", code);
            map.put("nonce", UuidUtil.getID16());
            viewModel.bindVerify4(map);
        } else if (Constant.VERIFY_BIND_PHONE.equals(typeName)) {
            // 邮箱验证后绑定手机 (发邮箱验证码)
            // /api/verify/bindverify
            map.put("code", code);
            map.put("flag", "initialVerify");
            map.put("sendtype", sendtype);
            map.put("nonce", UuidUtil.getID16());
            viewModel.bindVerify2(map);
        } else if (Constant.VERIFY_LOGIN.equals(typeName)) {
            // 异地登录/换设备登录
            String json = getArguments().getString("map");
            map = new Gson().fromJson(json, new TypeToken<Map<String, String>>() {
            }.getType());
            map.put("ex_code", code);
            map.put("verify_type", sendtype);
            map.put("nonce", UuidUtil.getID16());
            viewModel.login(map);

        } else {
            // 验证收到的验证码，然后 跳到其它业务，比如 绑USDT
            // /api/verify/singleverify
            map.put("flag", typeName);
            map.put("sendtype", sendtype); // phone/email
            map.put("istokenreturn", "true");
            map.put("has_securitypwd", "false");
            map.put("code", code);
            map.put("securitypass", "");
            map.put("nonce", UuidUtil.getID16());
            viewModel.singleVerify3(map);

        }

    }

    public void initViewObservable() {
        viewModel.liveDataCode.observe(this, vo -> {
            CfLog.i("***********");
            ToastUtils.showLong(R.string.txt_already_send_code);
            binding.tvwCode.setEnabled(false);
            binding.tvwCode.setText(vo.timeoutsec + "s");
            mHandler.sendEmptyMessageDelayed(MSG_TIMER, 1000L);

        });
        viewModel.liveDataCodeFail.observe(this, vo -> {
            CfLog.i("***********");
            binding.tvwCode.setEnabled(true);
            binding.tvwCode.setSelected(true);
            binding.edtCode.setTextColor(getResources().getColor(R.color.clr_sec_verify_warn));
            if (!binding.edtNum.getText().toString().contains("*")) {
                binding.edtNum.setEnabled(true);
            }
        });
        viewModel.liveDataSingleVerify1.observe(this, vo -> {
            CfLog.i("*********** 绑定成功，返回上页");
            //mVerifyVo= vo;
            TagUtils.logEvent(getContext(), "bindEmail");
            ToastUtils.showLong(R.string.txt_bind_succ);
            //getActivity().finish();
            viewModel.getProfile2();

            // 绑定完成后,去办理其它业务
            if (!TextUtils.isEmpty(typeName2) && vo != null && !TextUtils.isEmpty(vo.mark) && !TextUtils.isEmpty(vo.tokenSign)) {
                CfLog.i("*********** goOthers: " + typeName2);
                viewModel.goOthers(getActivity(), typeName2, vo);
            }
        });
        viewModel.liveDataSingleVerify2.observe(this, vo -> {
            CfLog.i("*********** 验证成功, 去修改密码");
            mVerifyVo = vo;
            ToastUtils.showLong(R.string.txt_verify_succ);
            // 去修改密码
            viewModel.goOthers(getActivity(), typeName, vo);
            getActivity().finish();
        });
        viewModel.liveDataSingleVerify3.observe(this, vo -> {
            CfLog.i("*********** 验证成功, 去其它业务");
            mVerifyVo = vo;
            ToastUtils.showLong(R.string.txt_verify_succ);
            // 去其它业务 (绑U,绑YHK等)
            if (TextUtils.isEmpty(typeName2)) {
                viewModel.goOthers(getActivity(), typeName, vo); // 验证
            } else {
                viewModel.goOthers(getActivity(), typeName2, vo); // 验证(绑定)
            }

            getActivity().finish();
        });

        viewModel.liveDataUpdateVerify1.observe(this, vo -> {
            CfLog.i("***********");
            alreadySendCode(vo);
        });
        viewModel.liveDataUpdateVerify2.observe(this, verifyVo -> {
            mVerifyVo = verifyVo;
            typeName = Constant.UPDATE_EMAIL2;

            mHandler.removeMessages(MSG_TIMER);
            binding.vDivLine.setVisibility(View.VISIBLE);
            binding.tvwCode.setText(R.string.txt_get_code);
            binding.tvwCode.setEnabled(true);
            binding.tvwCode.setSelected(true);
            binding.tvwTitle.setText(R.string.txt_enter_new_email);
            binding.edtNum.setEnabled(true);
            binding.edtNum.setText("");
            binding.edtCode.setText("");
            binding.tvwTipWarn2.setVisibility(View.INVISIBLE);
            binding.edtNum.performClick();

        });
        viewModel.liveDataUpdateVerify3.observe(this, vo -> {
            CfLog.i("***********");
            alreadySendCode(vo);
        });
        viewModel.liveDataUpdateVerify4.observe(this, verifyVo -> {
            ToastUtils.showLong(R.string.txt_change_succ);
            //getActivity().finish();
            viewModel.getProfile2();
        });
        viewModel.liveDataBindVerify1.observe(this, vo -> {
            CfLog.i("***********");
            alreadySendCode(vo);
        });
        viewModel.liveDataBindVerify2.observe(this, vo -> {
            CfLog.i("*********** 验证成功, 跳去修改 手机 " + vo.toString());
            String type = Constant.VERIFY_BIND_PHONE2; // 另一验证绑自己
            Bundle bundle = new Bundle();
            bundle.putString("type", type);
            bundle.putString("tokenSign", vo.tokenSign);
            startContainerFragment(RouterFragmentPath.Mine.PAGER_SECURITY_VERIFY, bundle);
            getActivity().finish();
        });
        viewModel.liveDataBindVerify3.observe(this, vo -> {
            CfLog.i("***********");
            alreadySendCode(vo);
        });
        viewModel.liveDataBindVerify4.observe(this, verifyVo -> {
            CfLog.i("*********** 修改/绑定成功, ");
            //getActivity().finish();
            TagUtils.logEvent(getContext(), "bindEmail");
            viewModel.getProfile2();
        });
        viewModel.liveDataLogin.observe(this, vo -> {
            CfLog.i("*********** 登录-验证成功...");

            if (vo.twofa_required == 0) {
                //viewModel.setLoginSucc(vo);
                goMain();

            } else if (vo.twofa_required == 1) {
                CfLog.i("*********** 去谷歌验证...");
                GoogleAuthDialog dialog = new GoogleAuthDialog(getContext(), this, () -> {
                    viewModel.setLoginSucc(vo);
                    goMain();
                });
                new XPopup.Builder(getContext())
                        .dismissOnBackPressed(false)
                        .dismissOnTouchOutside(false)
                        .asCustom(dialog)
                        .show();
            }

        });

        viewModel.liveDataProfile2.observe(this, ov -> {
            CfLog.i("*********** 获取个人信息成功, ");
            getActivity().finish();
        });

    }

    private void goMain() {
        ARouter.getInstance().build(RouterActivityPath.Main.PAGER_MAIN)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)
                .navigation();
        getActivity().finish();
    }

    private void alreadySendCode(VerifyVo vo) {
        ToastUtils.showLong(R.string.txt_already_send_code);
        binding.tvwCode.setEnabled(false);
        binding.tvwCode.setText(vo.timeoutsec + "s");
        mHandler.sendEmptyMessageDelayed(MSG_TIMER, 1000L);
    }

    private void setNum() {
        String num = binding.edtNum.getText().toString().trim();
        if ((!num.isEmpty() && AppUtil.isEmail(num)) || !binding.edtNum.isEnabled()) {
            binding.tvwCode.setEnabled(true);
            binding.tvwCode.setSelected(true);
            binding.tvwTipHint.setVisibility(View.VISIBLE);
            binding.tvwTipWarn.setVisibility(View.GONE);
            binding.edtNum.setTextColor(getResources().getColor(R.color.clr_sec_verify_normal));
        } else {
            binding.tvwCode.setEnabled(false);
            if (num.isEmpty()) {
                binding.tvwTipHint.setVisibility(View.VISIBLE);
                binding.tvwTipWarn.setVisibility(View.GONE);
                binding.edtNum.setTextColor(getResources().getColor(R.color.clr_sec_verify_normal));
            } else if (AppUtil.isEmail(num)) {
                binding.edtNum.setTextColor(getResources().getColor(R.color.clr_sec_verify_normal));
            } else {
                binding.tvwTipHint.setVisibility(View.GONE);
                binding.tvwTipWarn.setVisibility(View.VISIBLE);
                binding.edtNum.setTextColor(getResources().getColor(R.color.clr_sec_verify_warn));
            }
        }
    }

    private void setCode() {
        String code = binding.edtCode.getText().toString().trim();
        if (code.length() == 6) {
            binding.tvwTipWarn2.setVisibility(View.INVISIBLE);
            binding.edtCode.setTextColor(getResources().getColor(R.color.clr_sec_verify_normal));
        } else {
            binding.tvwTipWarn2.setVisibility(View.VISIBLE);
            if (code.isEmpty()) {
                binding.tvwTipWarn2.setText(R.string.txt_enter_code_cannot_empty);
            } else {
                binding.tvwTipWarn2.setText(R.string.txt_enter_code_6);
                binding.edtCode.setTextColor(getResources().getColor(R.color.clr_sec_verify_warn));
            }
        }
    }

    private void setBtn() {
        String num = binding.edtNum.getText().toString().trim();
        String code = binding.edtCode.getText().toString().trim();

        if ((AppUtil.isEmail(num) || !binding.edtNum.isEnabled()) && code.length() == 6) {
            binding.tvwOk.setEnabled(true);
        } else {
            binding.tvwOk.setEnabled(false);
        }
    }

}
