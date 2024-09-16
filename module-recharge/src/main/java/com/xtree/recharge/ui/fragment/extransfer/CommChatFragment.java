package com.xtree.recharge.ui.fragment.extransfer;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.comm100.livechat.core.VisitorClientCore;
import com.comm100.livechat.view.ChatWindowWebView;
import com.comm100.livechat.view.VisitorClientCustomJS;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.base.utils.CfLog;
import com.xtree.base.widget.LoadingDialog;
import com.xtree.recharge.BR;
import com.xtree.recharge.R;
import com.xtree.recharge.databinding.FragmentCommChatBinding;
import com.xtree.recharge.ui.fragment.RechargeFragment;
import com.xtree.recharge.ui.viewmodel.ExTransferViewModel;
import com.xtree.recharge.ui.viewmodel.RechargeViewModel;
import com.xtree.recharge.ui.viewmodel.factory.AppViewModelFactory;

import java.util.Map;
import java.util.Stack;
import java.util.Vector;

import me.xtree.mvvmhabit.base.AppManager;
import me.xtree.mvvmhabit.base.BaseFragment;

/**
 * Created by KAKA on 2024/6/17.
 * Describe: onePay 客服
 */
@Route(path = RouterFragmentPath.Transfer.PAGER_TRANSFER_EX_CHAT)
public class CommChatFragment extends BaseFragment<FragmentCommChatBinding, ExTransferViewModel> {

    private ChatWindowWebView mChatWindow;
    private Vector<String> injectCustomJSs = new Vector();
    private String prechatfillingScript = "";

    @Override
    public void initView() {
        binding.ivwBack.setOnClickListener(v -> getActivity().finish());

        this.mChatWindow = new ChatWindowWebView(getContext(), VisitorClientCore.getInstance().getPreChatFillingScipt());
        if (Build.VERSION.SDK_INT >= 23 &&
                (ContextCompat.checkSelfPermission(getContext(), "android.permission.WRITE_EXTERNAL_STORAGE") != 0 ||
                        ContextCompat.checkSelfPermission(getContext(), "android.permission.CAMERA") != 0)) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.CAMERA"}, 1);
        }

        LoadingDialog.show2(getContext());

        mChatWindow.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                StringBuilder scriptBuilder = new StringBuilder();
                scriptBuilder.append("Comm100API.onReady = function() {");
                if (injectCustomJSs.size() > 0) {
                    for (int i = 0; i < injectCustomJSs.size(); ++i) {
                        scriptBuilder.append((String) injectCustomJSs.get(i));
                    }
                }

                scriptBuilder.append(VisitorClientCustomJS.hideCloseLinkScript());
                if (prechatfillingScript != "") {
                    scriptBuilder.append(prechatfillingScript);
                }

                scriptBuilder.append("}");
                CfLog.i("ChatWindowWebView onPageFinished | javascript is" + scriptBuilder.toString());
                view.loadUrl("javascript:" + scriptBuilder.toString());

                LoadingDialog.finish();
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                CfLog.i("ChatWindowWebView onReceivedError " + error.toString());
                LoadingDialog.finish();
            }
        });

        binding.webgroup.addView(mChatWindow);
        this.mChatWindow.loadUrl(VisitorClientCore.getInstance().getChatUrl());
        CookieManager.getInstance().setAcceptCookie(true);
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_comm_chat;
    }

    @Override
    public int initVariableId() {
        return BR.model;
    }

    @Override
    public ExTransferViewModel initViewModel() {
        //以充值页作为共享载体
        Stack<Activity> activityStack = AppManager.getActivityStack();
        FragmentActivity fragmentActivity = requireActivity();
        for (Activity activity : activityStack) {
            try {
                FragmentActivity fa = (FragmentActivity) activity;
                for (Fragment fragment : fa.getSupportFragmentManager().getFragments()) {
                    if (fragment.getClass().getCanonicalName().equals(RechargeFragment.class.getCanonicalName())) {
                        fragmentActivity = fa;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        ExTransferViewModel viewmodel = new ViewModelProvider(fragmentActivity).get(ExTransferViewModel.class);
        AppViewModelFactory instance = AppViewModelFactory.getInstance(requireActivity().getApplication());
        viewmodel.setModel(instance.getmRepository());
        viewmodel.setRechargeViewModel(new ViewModelProvider(fragmentActivity).get(RechargeViewModel.class));
        return viewmodel;
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        viewModel.getUC().getStartContainerActivityEvent().removeObservers(this);
        viewModel.getUC().getStartContainerActivityEvent().observe(this, new Observer<Map<String, Object>>() {
            @Override
            public void onChanged(@Nullable Map<String, Object> params) {
            }
        });
    }

    @Override
    public void initData() {
        super.initData();
        binding.getModel().setActivity(getActivity());
    }

    public void onResume() {
        super.onResume();
        this.mChatWindow.onResume();
    }

    public void onPause() {
        super.onPause();
        this.mChatWindow.onPause();
    }

    @Override
    public void onDestroyView() {

        if (viewModel != null) {
            viewModel.reCheckOrder();
        }

        super.onDestroyView();
    }

    @Override
    public void getActivityResult(int requestCode, int resultCode, Intent data) {
        super.getActivityResult(requestCode, resultCode, data);
        this.mChatWindow.onActivityResult(requestCode, resultCode, data);
    }
}
