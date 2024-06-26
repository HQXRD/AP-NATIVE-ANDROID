package com.xtree.home.ui.fragment;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowMetrics;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.base.utils.AppUtil;
import com.xtree.base.utils.CfLog;
import com.xtree.base.utils.DomainUtil;
import com.xtree.base.utils.TagUtils;
import com.xtree.home.BR;
import com.xtree.home.BuildConfig;
import com.xtree.home.R;
import com.xtree.home.databinding.FragmentDebugBinding;
import com.xtree.home.ui.viewmodel.HomeViewModel;
import com.xtree.home.ui.viewmodel.factory.AppViewModelFactory;

import me.xtree.mvvmhabit.base.BaseFragment;
import me.xtree.mvvmhabit.utils.SPUtils;

@Route(path = RouterFragmentPath.Home.PG_DEBUG)
public class DebugFragment extends BaseFragment<FragmentDebugBinding, HomeViewModel> {

    private int clickCount = 0; // 点击次数 debug model

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setView();
    }

    private void setView() {

        int width = 0;
        int height = 0;
        DisplayMetrics dm = getResources().getDisplayMetrics();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            WindowMetrics wm = getActivity().getWindowManager().getCurrentWindowMetrics();
            width = wm.getBounds().width();
            height = wm.getBounds().height();
        } else {
            width = dm.widthPixels;
            height = dm.heightPixels;
        }

        PackageInfo pi = null;
        try {
            pi = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            CfLog.e(e.toString());
        }

        String versionCode;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            versionCode = pi.getLongVersionCode() + "";
        } else {
            versionCode = pi.versionCode + "";
        }

        binding.tvwVersion.setText("v" + pi.versionName + " (" + versionCode + ")");
        binding.tvwVersionName.setText(pi.versionName);
        binding.tvwVersionCode.setText(versionCode);
        binding.tvwBuildTime.setText(R.string.build_time); // 202402191635
        binding.tvwPkgName.setText(getActivity().getPackageName());
        binding.tvwRelease.setText(!BuildConfig.DEBUG + "");
        binding.tvwChannel.setText(R.string.channel_name);
        binding.tvwApi.setText(DomainUtil.getDomain());
        binding.tvwUsername.setText(SPUtils.getInstance().getString(SPKeyGlobal.USER_NAME, ""));
        binding.tvwSession.setText(SPUtils.getInstance().getString(SPKeyGlobal.USER_SHARE_SESSID, ""));
        binding.tvwToken.setText(SPUtils.getInstance().getString(SPKeyGlobal.USER_TOKEN, ""));
        binding.tvwDevId.setText(TagUtils.getDevId(getContext()));
        binding.tvwAndroidVersion.setText(Build.VERSION.RELEASE + " (" + Build.VERSION.SDK_INT + ")"); // 13 (33)
        binding.tvwManufacturer.setText(Build.MANUFACTURER);
        binding.tvwModel.setText(Build.MODEL);
        binding.tvwScreen.setText(width + " x " + height);
        binding.tvwTag.setText(TagUtils.isTag() + "");
        binding.tvwApiList.setText(getString(R.string.domain_url_list).replace(";", "\n").trim());

    }

    @Override
    public void initView() {
        binding.ivwBack.setOnClickListener(v -> getActivity().finish());
        binding.ivwCs.setOnClickListener(v -> AppUtil.goCustomerService(getContext()));

        binding.ivwLogo.setOnClickListener(v -> {
            if (clickCount++ >= 2) {
                clickCount = 0;
                binding.llMain.setVisibility(View.VISIBLE);
            }
        });

    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_debug;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public HomeViewModel initViewModel() {
        //使用自定义的ViewModelFactory来创建ViewModel，如果不重写该方法，则默认会调用LoginViewModel(@NonNull Application application)构造方法
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getActivity().getApplication());
        return new ViewModelProvider(this, factory).get(HomeViewModel.class);
    }

}
