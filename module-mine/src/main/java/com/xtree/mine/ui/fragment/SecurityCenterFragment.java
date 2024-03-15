package com.xtree.mine.ui.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.gson.Gson;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.xtree.base.global.Constant;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.base.utils.CfLog;
import com.xtree.base.utils.ClickUtil;
import com.xtree.base.vo.ProfileVo;
import com.xtree.base.widget.MsgDialog;
import com.xtree.mine.BR;
import com.xtree.mine.R;
import com.xtree.mine.databinding.FragmentSecurityCenterBinding;
import com.xtree.mine.ui.viewmodel.VerifyViewModel;
import com.xtree.mine.ui.viewmodel.factory.AppViewModelFactory;

import me.xtree.mvvmhabit.base.BaseFragment;
import me.xtree.mvvmhabit.utils.SPUtils;

/**
 * 安全中心
 */
@Route(path = RouterFragmentPath.Mine.PAGER_SECURITY_CENTER)
public class SecurityCenterFragment extends BaseFragment<FragmentSecurityCenterBinding, VerifyViewModel> {

    private ProfileVo mProfileVo;
    private BasePopupView basePopupView = null;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel.readCache();
        viewModel.getProfile();
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.readCache();
        viewModel.getProfile();//刷新用户信息
        viewModel.getCookie();//刷新cookie
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_security_center;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public VerifyViewModel initViewModel() {
        // return super.initViewModel();
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getActivity().getApplication());
        return new ViewModelProvider(this, factory).get(VerifyViewModel.class);
    }

    @Override
    public void initView() {
        binding.ivwBack.setOnClickListener(v -> getActivity().finish());
        binding.tvwResetPwd.setOnClickListener(v -> {
            CfLog.i("******");
            String type = Constant.RESET_LOGIN_PASSWORD;
            Bundle bundle = new Bundle();
            bundle.putString("type", type);
            //startContainerFragment(RouterFragmentPath.Mine.PAGER_SECURITY_VERIFY, bundle);
            startContainerFragment(RouterFragmentPath.Mine.PAGER_SECURITY_VERIFY_CHOOSE, bundle);
        });

        binding.tvwFundsPwd.setOnClickListener(v -> {
            if (mProfileVo != null) {
                startContainerFragment(RouterFragmentPath.Mine.PAGER_FUNDS_PWD);
            }
        });
        binding.tvwPwdSafe.setOnClickListener(v -> {
            if (ClickUtil.isFastClick()) {
                return;
            }
            if (mProfileVo != null) {
                if (!mProfileVo.has_securitypwd) {
                    startContainerFragment(RouterFragmentPath.Mine.PAGER_FUNDS_PWD);
                } else if (mProfileVo.set_question.equals("0")) {
                    new XPopup.Builder(requireContext()).asCustom(CheckPasswordDialog.newInstance(requireContext(), this, checkCode -> {
                        //密保问题设置弹窗
                        new XPopup.Builder(requireContext()).moveUpToKeyboard(false)
                                .dismissOnBackPressed(false)
                                .dismissOnTouchOutside(false)
                                .asCustom(new SetQuestionDialog(requireContext()))
                                .show();
                    })).show();
                }
            }
        });

        //跳转Google动态口令绑定页面
        binding.tvwGoogle.setOnClickListener(v -> {
            CfLog.i("****** google");

            if ((mProfileVo != null) && (mProfileVo.twofa == 1))//已完成谷歌动态口令绑定
            {
                showOverBindGoogle("已绑定", "您已经绑定谷歌验证无法进行重复绑定");
            } else {
                startContainerFragment(RouterFragmentPath.Mine.PAGER_BIND_GOOGLE_PWD);
            }
        });

        binding.tvwPhone.setOnClickListener(v -> {
            CfLog.i("******");
            String type;
            if (mProfileVo.is_binding_phone && mProfileVo.is_binding_email) {
                // 底部弹窗 选择一种验证方式,然后修改
                type = Constant.UPDATE_PHONE;
                Bundle bundle = new Bundle();
                bundle.putString("type", type);
                startContainerFragment(RouterFragmentPath.Mine.PAGER_SECURITY_VERIFY_CHOOSE, bundle);
                return;
            } else if (mProfileVo.is_binding_phone) {
                type = Constant.UPDATE_PHONE; // 更新
            } else if (mProfileVo.is_binding_email) {
                type = Constant.VERIFY_BIND_PHONE; // 另一验证绑自己
            } else {
                type = Constant.BIND_PHONE;
            }

            Bundle bundle = new Bundle();
            bundle.putString("type", type);
            startContainerFragment(RouterFragmentPath.Mine.PAGER_SECURITY_VERIFY, bundle);
        });
        binding.tvwEmail.setOnClickListener(v -> {
            CfLog.i("******");
            String type;
            if (mProfileVo.is_binding_phone && mProfileVo.is_binding_email) {
                // 底部弹窗 选择一种验证方式,然后修改
                type = Constant.UPDATE_EMAIL;
                Bundle bundle = new Bundle();
                bundle.putString("type", type);
                startContainerFragment(RouterFragmentPath.Mine.PAGER_SECURITY_VERIFY_CHOOSE, bundle);
                return;
            } else if (mProfileVo.is_binding_email) {
                type = Constant.UPDATE_EMAIL; // 更新
            } else if (mProfileVo.is_binding_phone) {
                type = Constant.VERIFY_BIND_EMAIL; // 另一验证绑自己
            } else {
                type = Constant.BIND_EMAIL;
            }

            Bundle bundle = new Bundle();
            bundle.putString("type", type);
            startContainerFragment(RouterFragmentPath.Mine.PAGER_SECURITY_VERIFY, bundle);
        });
        /*binding.tvwUsdt.setOnClickListener(v -> {
            CfLog.i("******");
            String type = Constant.BIND_USDT;
            Bundle bundle = new Bundle();
            bundle.putString("type", type);
            startContainerFragment(RouterFragmentPath.Mine.PAGER_SECURITY_VERIFY_CHOOSE, bundle);
        });*/

    }

    @Override
    public void initData() {
        String json = SPUtils.getInstance().getString(SPKeyGlobal.HOME_PROFILE);
        mProfileVo = new Gson().fromJson(json, ProfileVo.class);
        if (mProfileVo != null) {
            if (mProfileVo.is_binding_phone) {
                binding.tvwPhone.setText(getString(R.string.txt_change_phone_num) + mProfileVo.binding_phone_info);
            }
            if (mProfileVo.is_binding_email) {
                binding.tvwEmail.setText(getString(R.string.txt_change_email) + mProfileVo.binding_email_info);
            }
        }
    }

    @Override
    public void initViewObservable() {
        viewModel.liveDataProfile.observe(this, vo -> {
            mProfileVo = vo;
            if (!TextUtils.isEmpty(vo.binding_phone_info)) {
                binding.tvwPhone.setText(getString(R.string.txt_change_phone_num) + vo.binding_phone_info);
            }
            if (!TextUtils.isEmpty(vo.binding_phone_info)) {
                binding.tvwEmail.setText(getString(R.string.txt_change_email) + vo.binding_email_info);
            }
        });
    }

    /**
     * 显示已经绑定谷歌验证码提示
     */
    private void showOverBindGoogle(String title, String msg) {
        basePopupView = new XPopup.Builder(getContext()).asCustom(new MsgDialog(getContext(), title, msg, true, new MsgDialog.ICallBack() {
            @Override
            public void onClickLeft() {

            }

            @Override
            public void onClickRight() {
                basePopupView.dismiss();
            }

        }));
        basePopupView.show();
    }
}