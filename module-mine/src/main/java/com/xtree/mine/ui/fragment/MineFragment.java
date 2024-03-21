package com.xtree.mine.ui.fragment;

import android.content.Intent;
import android.net.Uri;
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
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.xtree.base.global.Constant;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.router.RouterActivityPath;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.base.utils.CfLog;
import com.xtree.base.utils.DomainUtil;
import com.xtree.base.utils.StringUtils;
import com.xtree.base.vo.ProfileVo;
import com.xtree.base.widget.BrowserActivity;
import com.xtree.base.widget.LoadingDialog;
import com.xtree.base.widget.MsgDialog;
import com.xtree.home.ui.fragment.UpdateDialog;
import com.xtree.home.vo.UpdateVo;
import com.xtree.mine.BR;
import com.xtree.mine.R;
import com.xtree.mine.databinding.FragmentMineBinding;
import com.xtree.mine.ui.activity.LoginRegisterActivity;
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
public class MineFragment extends BaseFragment<FragmentMineBinding, MineViewModel>  {
    ProfileVo mProfileVo;
    VipInfoVo mVipInfoVo;
    String token;
    BasePopupView ppw;
    private UpdateVo updateVo;
    private BasePopupView updateView;

    /**
     * 使用hide和show后，可见不可见切换时，不再执行fragment生命周期方法，
     * 需要刷新时，使用onHiddenChanged代替
     */
    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }

    private void refresh() {
        if (TextUtils.isEmpty(token)) {
            binding.ivwSetting.setClickable(false);
            binding.ivwMsg.setClickable(false);
            //binding.tvwQa.setClickable(false);
            binding.btnLogout.setVisibility(View.INVISIBLE);
            setChildClickable(binding.llMenu, false);
            setChildClickable(binding.llMenu01, false);
            setChildClickable(binding.llMenu02, false);
        } else {
            binding.ivwSetting.setClickable(true);
            binding.ivwMsg.setClickable(true);
            //binding.tvwQa.setClickable(true);
            binding.btnLogout.setVisibility(View.VISIBLE);
            setChildClickable(binding.llMenu, true);
            setChildClickable(binding.llMenu01, true);
            setChildClickable(binding.llMenu02, true);
            viewModel.readCache(); // 读取缓存
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {   // 可见

        } else {  // 第一次可见，不会执行到这里，只会执行onResume
            //网络数据刷新
            refresh();
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
            String title = getString(R.string.txt_is_1k_recycle_all_title);
            String msg = getString(R.string.txt_is_1k_recycle_all);
            String txtRight = getString(R.string.text_confirm);
            ppw = new XPopup.Builder(getContext()).asCustom(new MsgDialog(getContext(), title, msg, "", txtRight, new MsgDialog.ICallBack() {
                @Override
                public void onClickLeft() {
                    ppw.dismiss();
                }

                @Override
                public void onClickRight() {
                    LoadingDialog.show(getActivity());
                    viewModel.do1kAutoRecycle();
                    ppw.dismiss();
                }
            }));
            ppw.show();
        });
        binding.ivwRefreshBlc.setOnClickListener(v -> {
            CfLog.i("****** ");
            LoadingDialog.show(getActivity());
            viewModel.getBalance();
        });

        binding.tvwWallet.setOnClickListener(view -> {
            startContainerFragment(RouterFragmentPath.Mine.PAGER_MY_WALLET);
        });
        binding.tvwTrans.setOnClickListener(v -> {
            // 转账
            startContainerFragment(RouterFragmentPath.Wallet.PAGER_TRANSFER);
        });
        //投注记录
        binding.tvwBt.setOnClickListener(v -> {
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

        binding.tvwTeamMgmt.setOnClickListener(v -> {
            CfLog.i("****** ");
            startContainerFragment(RouterFragmentPath.Mine.PAGER_MEMBER_MANAGER);
        });

        //binding.tvwInviteFriend.setOnClickListener(v -> {
        //    CfLog.i("****** ");
        //    goWebView(v, Constant.URL_INVITE_FRIEND);
        //});
        //binding.tvwGuanfangheyin.setOnClickListener(v -> {
        //    String title = ((TextView) v).getText().toString();
        //    // URL 不需要拼装
        //    BrowserActivity.start(getContext(), title, Constant.URL_PARTNER, true);
        //});
        binding.tvwProfit.setOnClickListener(v -> {
            startContainerFragment(RouterFragmentPath.Mine.PAGER_PROFIT_LOSS); // 盈亏报表
        });
        binding.tvw3rdTrans.setOnClickListener(v -> {
            startContainerFragment(RouterFragmentPath.Mine.PAGER_THIRD_TRANSFER); // 三方转账
        });

        //binding.tvwSecurityCenter.setOnClickListener(v -> {
        //    // 安全中心
        //    startContainerFragment(RouterFragmentPath.Mine.PAGER_SECURITY_CENTER);
        //});
        //返水契约
        binding.tvwRebateContract.setOnClickListener(v->{
            startContainerFragment(RouterFragmentPath.Mine.PAGER_REBATE_AGREEMENT);
        });
        binding.tvwAccMg.setOnClickListener(v -> {
            showAccountMgmt();
        });

        binding.tvwSportRegular.setOnClickListener(v -> {
            goWebView(v, Constant.URL_SPORT_RULES, false);
        });

        binding.tvwTutorial.setOnClickListener(v -> {
            goWebView(v, Constant.URL_USDT_RECHARGE_TURTIAL, false);
        });

        //VIP中心
        //binding.tvwVipZhongxin.setOnClickListener(v -> {
        //    //startContainerFragment(RouterFragmentPath.Mine.PAGER_VIP_UPGRADE);
        //    BrowserActivity.start(getContext(), getString(R.string.txt_vip_center), DomainUtil.getDomain2() + Constant.URL_VIP_CENTER, true, false, true);
        //});
        //VIP中心
        //binding.ivwLevel.setOnClickListener(v -> {
        //    //startContainerFragment(RouterFragmentPath.Mine.PAGER_VIP_UPGRADE);
        //    BrowserActivity.start(getContext(), getString(R.string.txt_vip_center), DomainUtil.getDomain2() + Constant.URL_VIP_CENTER, true, false, true);
        //});
        //binding.tvwFanhuiBaobiao.setOnClickListener(v -> {
        //    startContainerFragment(RouterFragmentPath.Mine.PAGER_REBATE_REPORT); // 返水报表
        //});
        //binding.tvwQa.setOnClickListener(v -> {
        //    startContainerFragment(RouterFragmentPath.Mine.PAGER_QUESTION);
        //});
        binding.tvwHelp.setOnClickListener(v -> {
            //BrowserActivity.start(getContext(), ((TextView) v).getText().toString(), DomainUtil.getDomain2() + Constant.URL_HELP, false, false, true);
            //startContainerFragment(RouterFragmentPath.Mine.PAGER_INFO);
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(DomainUtil.getDomain2() + Constant.URL_HELP));
            startActivity(intent);
        });
        binding.tvwLogin.setOnClickListener(v -> {
            Intent toLogin = new Intent(getContext(), LoginRegisterActivity.class);
            toLogin.putExtra(LoginRegisterActivity.ENTER_TYPE, LoginRegisterActivity.LOGIN_TYPE);
            startActivity(toLogin);
        });
        //binding.textViewRegister.setOnClickListener(v -> {
        //    Intent toRegister = new Intent(getContext(), LoginRegisterActivity.class);
        //    toRegister.putExtra(LoginRegisterActivity.ENTER_TYPE, LoginRegisterActivity.REGISTER_TYPE);
        //    startActivity(toRegister);
        //});

        binding.llMenu.setOnClickListener(v -> {
            if (TextUtils.isEmpty(token)) {
                binding.tvwLogin.performClick();
            }
        });
        binding.llMenu01.setOnClickListener(v -> {
            if (TextUtils.isEmpty(token)) {
                binding.tvwLogin.performClick();
            }
        });
        binding.llMenu02.setOnClickListener(v -> {
            if (TextUtils.isEmpty(token)) {
                binding.tvwLogin.performClick();
            }
        });
        //检查更新
        binding.tvwUpgrade.setOnClickListener(v -> {
            LoadingDialog.show(getContext());
            viewModel.getUpdate();

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

        if (mProfileVo == null || TextUtils.isEmpty(token)) {
            CfLog.i("****** not login");
            binding.llLogin.setVisibility(View.VISIBLE);
            binding.clAlreadyLogin.setVisibility(View.GONE);
            //binding.llMenu.setVisibility(View.GONE);
            //binding.llMenu2.setVisibility(View.GONE);
            binding.btnLogout.setVisibility(View.GONE);
            binding.ivwMsg.setVisibility(View.INVISIBLE);
            binding.ivwSetting.setVisibility(View.INVISIBLE);

            // 未登录状态下,直接跳到登录页,并关闭当前页
            //ARouter.getInstance().build(RouterActivityPath.Mine.PAGER_LOGIN_REGISTER).navigation();
            //getActivity().finish();
        } else {
            CfLog.i("****** already login");
            //binding.llLogin.setVisibility(View.GONE);
            //binding.clAlreadyLogin.setVisibility(View.VISIBLE);
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
        viewModel.getVipUpgradeInfo();
    }

    private void resetView() {
        CfLog.i("******");
        if (mProfileVo != null) {
            binding.tvwName.setText(mProfileVo.username);
            setBalance();
        }
        //if (mVipInfoVo != null) {
        //    if (mVipInfoVo.sp.equals("1")) {
        //        binding.tvwVip.setText("VIP " + mVipInfoVo.display_level);
        //        binding.ivwLevel.setImageLevel(mVipInfoVo.display_level);
        //    } else {
        //        binding.tvwVip.setText("VIP " + mVipInfoVo.level);
        //        binding.ivwLevel.setImageLevel(mVipInfoVo.level);
        //    }
        //    if (mVipInfoVo.level >= 10) {
        //        binding.ivwLevel.setVisibility(View.INVISIBLE);
        //        //binding.ivwLevel10.setVisibility(View.VISIBLE);
        //        binding.middleArea.setBackgroundResource(R.mipmap.me_bg_top_10);
        //        binding.ivwVip10.setVisibility(View.VISIBLE);
        //        binding.ivwVip10.setOnClickListener(v -> binding.ivwLevel.performClick());
        //    }
        //}
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
        viewModel.liveDataVipInfo.observe(this, vo -> {
            // 个人信息
            mVipInfoVo = vo;
            resetView();
        });

        viewModel.liveDataBalance.observe(this, vo -> {
            mProfileVo.availablebalance = vo.balance;
            setBalance();
            ToastUtils.show(this.getString(R.string.txt_rc_tip_latest_balance), ToastUtils.ShowType.Success);
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
        //App更新
        viewModel.liveDataUpdate.observe(this, vo -> {
            CfLog.i(vo.toString());
            updateVo = vo;
            if (updateVo == null){
                return;
            }
            //存储服务器设置时间间隔
            SPUtils.getInstance().put(SPKeyGlobal.APP_INTERVAL_TIME, updateVo.interval_duration);
            //请求更新服务时间
            SPUtils.getInstance().put(SPKeyGlobal.APP_LAST_CHECK_TIME, System.currentTimeMillis());
            long versionCode = Long.valueOf(StringUtils.getVersionCode(getContext()));
            CfLog.i("versionCode = " + versionCode);
            if (versionCode < updateVo.version_code) {
                //线上版本大于本机版本
                if (updateVo.type == 0) {
                    //弱更
                    if (versionCode >= vo.version_code_min) {
                        showUpdate(true, updateVo); // 弱更
                    } else {
                        showUpdate(false, updateVo); // 强更
                    }

                } else if (updateVo.type == 1) {
                    //强更
                    showUpdate(false, updateVo);
                } else if (updateVo.type == 2) {
                    //热更
                }
            }

        });

        //viewModel.liveDataVipUpgrade.observe(this, vo -> {
        //    if (mVipInfoVo == null) {
        //        //binding.tvwLevelHint.setVisibility(View.INVISIBLE);
        //        binding.pbrLevel.setProgress(0);
        //        return;
        //    }
        //    if (vo.sp.equals("1")) {
        //        if (vo.level < vo.vip_upgrade.size() - 1) {
        //            int point = vo.vip_upgrade.get(vo.level + 1).display_active - mVipInfoVo.current_activity;
        //            int level = vo.vip_upgrade.get(vo.level + 1).display_level;
        //            String txt = getString(R.string.txt_level_hint_00);
        //            txt = String.format(txt, point, level);
        //            CfLog.i("txt " + txt);
        //            binding.tvwLevelHint.setText(txt);
        //            binding.pbrLevel.setProgress((int) (((double) mVipInfoVo.current_activity / (double) vo.vip_upgrade.get(vo.level + 1).display_active) * 100));
        //            binding.pbrLevel.setProgressDrawable(getResources().getDrawable(R.drawable.me_level_progressbar));
        //        } else {
        //            String txt = getString(R.string.txt_level_hint_10);
        //            txt = String.format(txt, vo.display_level);
        //            binding.tvwLevelHint.setText(txt);
        //            binding.pbrLevel.setProgress(100);
        //            binding.pbrLevel.setProgressDrawable(getResources().getDrawable(R.drawable.me_level_progressbar_100));
        //        }
        //    } else {
        //        if (vo.level < vo.vip_upgrade.size() - 1) {
        //            int point = vo.vip_upgrade.get(vo.level + 1).active - mVipInfoVo.current_activity;
        //            int level = vo.level + 1;
        //            String txt = getString(R.string.txt_level_hint_00);
        //            txt = String.format(txt, point, level);
        //            CfLog.d("txt " + txt);
        //            binding.tvwLevelHint.setText(txt);
        //            binding.pbrLevel.setProgress((int) (((double) mVipInfoVo.current_activity / (double) vo.vip_upgrade.get(vo.level + 1).active) * 100));
        //            binding.pbrLevel.setProgressDrawable(getResources().getDrawable(R.drawable.me_level_progressbar));
        //        } else {
        //            String txt = getString(R.string.txt_level_hint_10);
        //            txt = String.format(txt, vo.level);
        //            binding.tvwLevelHint.setText(txt);
        //            binding.pbrLevel.setProgress(100);
        //            binding.pbrLevel.setProgressDrawable(getResources().getDrawable(R.drawable.me_level_progressbar_100));
        //        }
        //    }
        //});
    }

    /**
     * 显示更新
     *
     * @param isWeakUpdate 是否弱更 true:是弱更 false:强更
     * @param vo           UpdateVo
     */
    private void showUpdate(final boolean isWeakUpdate, final UpdateVo vo) {
        if (updateView != null && updateView.isShow()) {
            return;
        }
        UpdateDialog dialog = new UpdateDialog(getContext(), isWeakUpdate, vo, new UpdateDialog.ICallBack() {
            @Override
            public void onUpdateCancel() {
                updateView.dismiss();
            }

            @Override
            public void onUpdateForce() {
            }
        });
        updateView = new XPopup.Builder(getContext())
                .dismissOnBackPressed(false)
                .dismissOnTouchOutside(false)
                .asCustom(dialog);
        updateView.show();
    }

    /**
     * 判断用户是否登陆
     */
    private boolean isLogin() {
        if (mProfileVo == null || TextUtils.isEmpty(mProfileVo.userid) || TextUtils.isEmpty(token)) {
            CfLog.i("****** not login");
            return false;
        }
        return true;
    }
}
