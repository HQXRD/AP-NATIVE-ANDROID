package com.xtree.mine.ui.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.Gson;
import com.gyf.immersionbar.ImmersionBar;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.router.RouterActivityPath;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.base.utils.CfLog;
import com.xtree.mine.BR;
import com.xtree.mine.R;
import com.xtree.mine.databinding.FragmentMineBinding;
import com.xtree.mine.ui.activity.LoginRegisterActivity;
import com.xtree.mine.ui.activity.MyWalletActivity;
import com.xtree.mine.ui.viewmodel.MineViewModel;
import com.xtree.mine.ui.viewmodel.factory.AppViewModelFactory;
import com.xtree.mine.vo.ProfileVo;
import com.xtree.mine.vo.VipInfoVo;

import me.xtree.mvvmhabit.base.BaseFragment;
import me.xtree.mvvmhabit.utils.SPUtils;

/**
 * Created by goldze on 2018/6/21
 */
@Route(path = RouterFragmentPath.Mine.PAGER_MINE)
public class MineFragment extends BaseFragment<FragmentMineBinding, MineViewModel> {
    ProfileVo mProfileVo;
    String token;

    @Override
    public void initView() {
        binding.btnLogout.setOnClickListener(v -> viewModel.doLogout());
        binding.tvwSecurityCenter.setOnClickListener(v -> startContainerFragment(RouterFragmentPath.Mine.PAGER_SECURITY_CENTER));
        binding.tvwSafe.setOnClickListener(v -> startContainerFragment(RouterFragmentPath.Mine.PAGER_SECURITY_CENTER));

    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initImmersionBar() {
        //设置共同沉浸式样式
        ImmersionBar.with(this)
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

        if (mProfileVo == null || mProfileVo.userid == 0 || TextUtils.isEmpty(token)) {
            CfLog.i("****** not login");
            binding.llLogin.setVisibility(View.VISIBLE);
            binding.clAlreadyLogin.setVisibility(View.INVISIBLE);
        } else {
            CfLog.i("****** already login");
            binding.llLogin.setVisibility(View.GONE);
            binding.clAlreadyLogin.setVisibility(View.VISIBLE);
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
        VipInfoVo vo2 = new Gson().fromJson(json, VipInfoVo.class);
        if (mProfileVo != null) {
            binding.tvwName.setText(mProfileVo.username);
            binding.tvwBalance.setText(mProfileVo.availablebalance);
        }
        if (vo2 != null) {
            binding.ivwVip.setImageLevel(vo2.display_level);
        }

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

        binding.iconSetting.setOnClickListener(view -> {
            popup();
        });

        binding.tvwWallet.setOnClickListener(view -> {
            Intent toMyWallet = new Intent(getContext(), MyWalletActivity.class);
            startActivity(toMyWallet);
        });

        binding.tvwTrans.setOnClickListener(v -> {
            // 转账
            startContainerFragment(RouterFragmentPath.Wallet.PAGER_TRANSFER);
        });

    }

    private void popup() {
        showBottomDialog();
    }

    private void showBottomDialog() {
        //1、使用Dialog、设置style
        final Dialog dialog = new Dialog(getActivity(), R.style.DialogTheme);
        //2、设置布局
        View view = View.inflate(getActivity(), R.layout.mine_account_popup_window, null);
        dialog.setContentView(view);

        Window window = dialog.getWindow();
        //设置弹出位置
        window.setGravity(Gravity.BOTTOM);
        //设置弹出动画
        window.setWindowAnimations(R.style.main_menu_animStyle);
        //设置对话框大小
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();
        dialog.findViewById(R.id.me_close_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }

    @Override
    public void initViewObservable() {

        viewModel.liveDataLogout.observe(this, isLogout -> {
            if (isLogout) {
                binding.llLogin.setVisibility(View.VISIBLE);
                binding.clAlreadyLogin.setVisibility(View.INVISIBLE);
                mProfileVo = null;
                ARouter.getInstance().build(RouterActivityPath.Main.PAGER_MAIN)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        .navigation();
            }
        });
    }
}
