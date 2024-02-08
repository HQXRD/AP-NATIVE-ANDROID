package com.xtree.mine.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.Gson;
import com.gyf.immersionbar.ImmersionBar;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.xtree.base.global.Constant;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.router.RouterActivityPath;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.base.utils.CfLog;
import com.xtree.base.utils.DomainUtil;
import com.xtree.base.vo.ProfileVo;
import com.xtree.base.widget.BrowserActivity;
import com.xtree.base.widget.LoadingDialog;
import com.xtree.base.widget.MsgDialog;
import com.xtree.mine.BR;
import com.xtree.mine.R;
import com.xtree.mine.databinding.FragmentMineBinding;
import com.xtree.mine.ui.activity.LoginRegisterActivity;
import com.xtree.mine.ui.activity.MyWalletActivity;
import com.xtree.mine.ui.viewmodel.MineViewModel;
import com.xtree.mine.ui.viewmodel.factory.AppViewModelFactory;
import com.xtree.mine.vo.VipInfoVo;

import me.xtree.mvvmhabit.base.BaseFragment;
import me.xtree.mvvmhabit.utils.SPUtils;
import me.xtree.mvvmhabit.utils.ToastUtils;

/**
 * 我的/个人中心
 */
@Route(path = RouterFragmentPath.Mine.PAGER_MINE)
public class MineFragment extends BaseFragment<FragmentMineBinding, MineViewModel> {
    ProfileVo mProfileVo;
    VipInfoVo mVipInfoVo;
    String token;
    BasePopupView ppw;

    @Override
    public void onResume() {
        super.onResume();
        if (TextUtils.isEmpty(token)) {
            binding.ivwSetting.setClickable(false);
            binding.ivwMsg.setClickable(false);
            binding.tvwChangjianWenti.setClickable(false);
            binding.btnLogout.setVisibility(View.INVISIBLE);
            setChildClickable(binding.llMenu, false);
            setChildClickable(binding.llMenu01, false);
            setChildClickable(binding.llMenu02, false);
        } else {
            binding.ivwSetting.setClickable(true);
            binding.ivwMsg.setClickable(true);
            binding.tvwChangjianWenti.setClickable(true);
            binding.btnLogout.setVisibility(View.VISIBLE);
            setChildClickable(binding.llMenu, true);
            setChildClickable(binding.llMenu01, true);
            setChildClickable(binding.llMenu02, true);
        }
    }

    private void setChildClickable(ViewGroup vgp, boolean isClickable) {
        for (int i = 0; i < vgp.getChildCount(); i++) {
            vgp.getChildAt(i).setClickable(isClickable);
        }
    }

    @Override
    public void initView() {
        binding.btnLogout.setOnClickListener(v -> showLogoutDialog());

        binding.ivwSetting.setOnClickListener(view -> {
            showAccountMgmt();
        });
        binding.ivwMsg.setOnClickListener(v -> {
            CfLog.i("****** ");
            startContainerFragment(RouterFragmentPath.Mine.PAGER_MSG);
        });

        binding.ckbEye.setOnCheckedChangeListener((buttonView, isChecked) -> {
            setBalance();
        });

        binding.tvw1kRecycle.setOnClickListener(v -> {
            CfLog.i("****** ");
            LoadingDialog.show(getActivity());
            viewModel.do1kAutoRecycle();
        });
        binding.ivwRefreshBlc.setOnClickListener(v -> {
            CfLog.i("****** ");
            LoadingDialog.show(getActivity());
            viewModel.getBalance();
        });

        binding.tvwWallet.setOnClickListener(view -> {
            Intent toMyWallet = new Intent(getContext(), MyWalletActivity.class);
            startActivity(toMyWallet);
        });
        binding.tvwTrans.setOnClickListener(v -> {
            // 转账
            startContainerFragment(RouterFragmentPath.Wallet.PAGER_TRANSFER);
        });
        //投注记录
        binding.tvwBet.setOnClickListener(v -> {
            CfLog.i("****** ");
            startContainerFragment(RouterFragmentPath.Mine.PAGER_BT_REPORT); // 投注记录
        });
        binding.tvwTransRecord.setOnClickListener(v -> {
            CfLog.i("****** ");
            startContainerFragment(RouterFragmentPath.Mine.PAGER_ACCOUNT_CHANGE); // 账变记录
        });
        binding.tvwSafe.setOnClickListener(v -> {
            CfLog.i("****** ");
            startContainerFragment(RouterFragmentPath.Mine.PAGER_SECURITY_CENTER);
        });

        binding.tvwInviteFriend.setOnClickListener(v -> {
            CfLog.i("****** ");
            goWebView(v, Constant.URL_INVITE_FRIEND);
        });
        binding.tvwGuanfangheyin.setOnClickListener(v -> {
            String title = ((TextView) v).getText().toString();
            // URL 不需要拼装
            BrowserActivity.start(getContext(), title, Constant.URL_PARTNER, true);
        });
        binding.tvwYinkuiBaobiao.setOnClickListener(v -> {
            startContainerFragment(RouterFragmentPath.Mine.PAGER_PROFIT_LOSS); // 盈亏报表
        });
        binding.tvwSanfangZhuanzhang.setOnClickListener(v -> {
            startContainerFragment(RouterFragmentPath.Mine.PAGER_THIRD_TRANSFER); // 三方转账
        });

        binding.tvwSecurityCenter.setOnClickListener(v -> {
            // 安全中心
            startContainerFragment(RouterFragmentPath.Mine.PAGER_SECURITY_CENTER);
        });
        binding.tvwZhanghuShezhi.setOnClickListener(v -> {
            showAccountMgmt();
        });
        //VIP中心
        binding.tvwVipZhongxin.setOnClickListener(v -> {
            startContainerFragment(RouterFragmentPath.Mine.PAGER_VIP_UPGRADE);
        });
        //VIP中心
        binding.ivwLevel.setOnClickListener(v->{
            startContainerFragment(RouterFragmentPath.Mine.PAGER_VIP_UPGRADE);
        });
        binding.tvwFanhuiBaobiao.setOnClickListener(v -> {
            startContainerFragment(RouterFragmentPath.Mine.PAGER_REBATE_REPORT); // 返水报表
        });

        binding.tvwTiyuGuize.setOnClickListener(v -> {
            goWebView(v, Constant.URL_SPORT_RULES, false);
        });
        binding.tvwChangjianWenti.setOnClickListener(v -> {
            startContainerFragment(RouterFragmentPath.Mine.PAGER_QUESTION);
        });
        binding.tvwBangzhuZhongxin.setOnClickListener(v -> {
            startContainerFragment(RouterFragmentPath.Mine.PAGER_INFO);
        });
        binding.tvwUsdtJiaocheng.setOnClickListener(v -> {
            goWebView(v, Constant.URL_TUTORIAL);
        });
        binding.textViewLogin.setOnClickListener(v -> {
            Intent toLogin = new Intent(getContext(), LoginRegisterActivity.class);
            toLogin.putExtra(LoginRegisterActivity.ENTER_TYPE, LoginRegisterActivity.LOGIN_TYPE);
            startActivity(toLogin);
        });
        binding.textViewRegister.setOnClickListener(v -> {
            Intent toRegister = new Intent(getContext(), LoginRegisterActivity.class);
            toRegister.putExtra(LoginRegisterActivity.ENTER_TYPE, LoginRegisterActivity.REGISTER_TYPE);
            startActivity(toRegister);
        });

        binding.llMenu.setOnClickListener(v -> {
            if (TextUtils.isEmpty(token)) {
                binding.textViewLogin.performClick();
            }
        });
        binding.llMenu01.setOnClickListener(v -> {
            if (TextUtils.isEmpty(token)) {
                binding.textViewLogin.performClick();
            }
        });
        binding.llMenu02.setOnClickListener(v -> {
            if (TextUtils.isEmpty(token)) {
                binding.textViewLogin.performClick();
            }
        });

    }

    private void goWebView(String title, String path) {
        String url = DomainUtil.getDomain2() + path;
        BrowserActivity.start(getContext(), title, url, true);
    }

    private void goWebView(View v, String path) {
        String title = ((TextView) v).getText().toString();
        String url = DomainUtil.getDomain2() + path;
        BrowserActivity.start(getContext(), title, url, true);
    }

    private void goWebView(View v, String path, boolean isContainTitle) {
        String title = ((TextView) v).getText().toString();
        String url = DomainUtil.getDomain2() + path;
        BrowserActivity.start(getContext(), title, url, isContainTitle);
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initImmersionBar() {
        //设置共同沉浸式样式
        ImmersionBar.with(this)
                .navigationBarColor(me.xtree.mvvmhabit.R.color.default_navigation_bar_color)
                .fitsSystemWindows(false)
                .statusBarDarkFont(false)
                .init();
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public MineViewModel initViewModel() {
        //使用自定义的ViewModelFactory来创建ViewModel，如果不重写该方法，则默认会调用LoginViewModel(@NonNull Application application)构造方法
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getActivity().getApplication());
        return new ViewModelProvider(this, factory).get(MineViewModel.class);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (mProfileVo == null || TextUtils.isEmpty(mProfileVo.userid) || TextUtils.isEmpty(token)) {
            CfLog.i("****** not login");
            binding.llLogin.setVisibility(View.VISIBLE);
            binding.clAlreadyLogin.setVisibility(View.INVISIBLE);
        } else {
            CfLog.i("****** already login");
            binding.llLogin.setVisibility(View.GONE);
            binding.clAlreadyLogin.setVisibility(View.VISIBLE);
            resetView();
        }
    }

    @Override
    public void initData() {
        token = SPUtils.getInstance().getString(SPKeyGlobal.USER_TOKEN);
        if (TextUtils.isEmpty(token)) {
            //ARouter.getInstance().build(RouterActivityPath.Mine.PAGER_LOGIN_REGISTER).navigation();
            //return;
        }
        String json = SPUtils.getInstance().getString(SPKeyGlobal.HOME_PROFILE);
        mProfileVo = new Gson().fromJson(json, ProfileVo.class);
        json = SPUtils.getInstance().getString(SPKeyGlobal.HOME_VIP_INFO);
        mVipInfoVo = new Gson().fromJson(json, VipInfoVo.class);

    }

    private void resetView() {
        if (mProfileVo != null) {
            binding.tvwName.setText(mProfileVo.username);
            setBalance();
        }
        if (mVipInfoVo != null) {
            binding.ivwVip.setImageLevel(mVipInfoVo.display_level);
            binding.ivwLevel.setImageLevel(mVipInfoVo.display_level);
            if (mVipInfoVo.display_level >= 10) {
                binding.ivwLevel.setVisibility(View.GONE);
                //binding.ivwLevel10.setVisibility(View.VISIBLE);
                binding.middleArea.setBackgroundResource(R.mipmap.me_bg_top_10);
            }

            if (mVipInfoVo.vip_upgrade != null) {
                if (mVipInfoVo.display_level < mVipInfoVo.vip_upgrade.size()) {
                    VipInfoVo.VipUpgradeVo vo = mVipInfoVo.vip_upgrade.get(mVipInfoVo.display_level + 1);
                    int point = vo.active - mVipInfoVo.current_activity;
                    int level = mVipInfoVo.display_level + 1;
                    String txt = getString(R.string.txt_level_hint_00);
                    txt = String.format(txt, point, level);
                    binding.tvwLevelHint.setText(txt);
                    binding.pbrLevel.setProgress((int)(((double)mVipInfoVo.current_activity /(double) vo.active) * 100));
                } else {
                    binding.tvwLevelHint.setText(R.string.txt_level_hint_10);
                    binding.pbrLevel.setProgress(100);
                }
            }
        }
    }

    private void setBalance() {
        if (binding.ckbEye.isChecked()) {
            binding.tvwBalance.setText(mProfileVo.availablebalance);
        } else {
            binding.tvwBalance.setText("******");
        }
    }

    private void showLogoutDialog() {
        String msg = getString(R.string.txt_will_u_logout);
        MsgDialog dialog = new MsgDialog(getContext(), null, msg, new MsgDialog.ICallBack() {
            @Override
            public void onClickLeft() {
                ppw.dismiss();
            }

            @Override
            public void onClickRight() {
                viewModel.doLogout();
                ppw.dismiss();
            }
        });
        ppw = new XPopup.Builder(getContext())
                .dismissOnTouchOutside(false)
                .dismissOnBackPressed(false)
                .asCustom(dialog);
        ppw.show();
    }

    private void showAccountMgmt() {
        new XPopup.Builder(getContext()).asCustom(new AccountMgmtDialog(getContext())).show();
    }

    @Override
    public void initViewObservable() {

        viewModel.liveDataLogout.observe(this, isLogout -> {
            if (isLogout) {
                binding.llLogin.setVisibility(View.VISIBLE);
                binding.clAlreadyLogin.setVisibility(View.INVISIBLE);
                mProfileVo = null;
                ARouter.getInstance().build(RouterActivityPath.Main.PAGER_MAIN)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK)
                        .navigation();
            }
        });

        viewModel.liveDataProfile.observe(this, vo -> {
            // 个人信息
            mProfileVo = vo;
            resetView();
        });

        viewModel.liveDataBalance.observe(this, vo -> {
            mProfileVo.availablebalance = vo.balance;
            setBalance();
        });
        viewModel.liveData1kRecycle.observe(this, isSuccess -> {
            if (isSuccess) {
                ToastUtils.showLong(R.string.txt_recycle_succ);
                viewModel.getBalance(); // 平台中心余额
                viewModel.getProfile();
            } else {
                ToastUtils.showLong(R.string.txt_recycle_fail);
            }
        });

    }
}
