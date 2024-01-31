package com.xtree.home.ui.fragment;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.gyf.immersionbar.ImmersionBar;
import com.xtree.base.global.Constant;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.base.utils.CfLog;
import com.xtree.base.utils.DomainUtil;
import com.xtree.home.BR;
import com.xtree.home.R;
import com.xtree.home.databinding.FragmentAdsBinding;
import com.xtree.home.ui.viewmodel.HomeViewModel;
import com.xtree.home.ui.viewmodel.factory.AppViewModelFactory;

import java.util.HashMap;
import java.util.Map;

import me.xtree.mvvmhabit.base.BaseFragment;
import me.xtree.mvvmhabit.utils.SPUtils;

@Route(path = RouterFragmentPath.Home.AD)
public class AdsFragment extends BaseFragment<FragmentAdsBinding, HomeViewModel> {
    int sslErrorCount = 0;

    @Override
    public void initView() {
        //initWebView();
    }

    @Override
    protected void initImmersionBar() {
        //设置共同沉浸式样式
        ImmersionBar.with(this)
                .navigationBarColor(me.xtree.mvvmhabit.R.color.default_navigation_bar_color)
                .fitsSystemWindows(false)
                .statusBarDarkFont(true)
                .init();
    }

    public void initWebView() {
        String cookie = "auth=" + SPUtils.getInstance().getString(SPKeyGlobal.USER_TOKEN)
                + ";" + SPUtils.getInstance().getString(SPKeyGlobal.USER_SHARE_COOKIE_NAME)
                + "=" + SPUtils.getInstance().getString(SPKeyGlobal.USER_SHARE_SESSID)
                + ";";

        String auth = "bearer " + SPUtils.getInstance().getString(SPKeyGlobal.USER_TOKEN);

        CfLog.d("Cookie: " + cookie);
        CfLog.d("Authorization: " + auth);

        Map<String, String> header = new HashMap<>();
        header.put("Cookie", cookie);
        header.put("Authorization", auth);
        header.put("App-RNID", "87jumkljo"); //

        binding.wvAdsMain.setFitsSystemWindows(true);
        setWebView(binding.wvAdsMain);

        binding.wvAdsMain.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                CfLog.d("onPageStarted url:  " + url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                CfLog.d("onPageFinished url: " + url);
                //Log.d("---", "onPageFinished url: " + url);
                hideLoading();
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
//                handler.proceed();
                CfLog.d("onPageFinished url: " + error);
                hideLoading();
                if (sslErrorCount < 4) {
                    sslErrorCount++;
                    tipSsl(view, handler);
                } else {
                    handler.proceed();
                }
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                CfLog.e("errorCode: " + errorCode + ", description: " + description + ", failingUrl: " + failingUrl);
                hideLoading();
                Toast.makeText(getContext(), com.xtree.base.R.string.network_failed, Toast.LENGTH_SHORT).show();
            }
        });

        binding.wvAdsMain.loadUrl(DomainUtil.getDomain2() + Constant.URL_APP_CENTER, header);
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_ads;
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

    private void hideLoading() {
        binding.ivwAdsLoading.setVisibility(View.GONE);
        binding.ivwAdsLoading.clearAnimation();
    }

    private void tipSsl(WebView view, SslErrorHandler handler) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setMessage(com.xtree.base.R.string.ssl_failed_will_u_continue); // SSL认证失败，是否继续访问？
        builder.setPositiveButton(com.xtree.base.R.string.ok, (dialog, which) -> {
            handler.proceed();// 接受https所有网站的证书
        });

        builder.setNegativeButton(com.xtree.base.R.string.cancel, (dialog, which) -> handler.cancel());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void setWebView(WebView webView) {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setLoadsImagesAutomatically(true);
    }
}
