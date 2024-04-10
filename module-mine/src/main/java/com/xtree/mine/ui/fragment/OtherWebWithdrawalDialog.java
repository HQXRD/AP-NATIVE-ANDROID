package com.xtree.mine.ui.fragment;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.utils.CfLog;
import com.xtree.base.utils.DomainUtil;
import com.xtree.base.utils.StringUtils;
import com.xtree.base.widget.LoadingDialog;
import com.xtree.base.widget.MsgDialog;
import com.xtree.base.widget.TipDialog;
import com.xtree.mine.R;
import com.xtree.mine.data.Injection;
import com.xtree.mine.databinding.DialogOtherWithdrawalWebBinding;
import com.xtree.mine.ui.viewmodel.ChooseWithdrawViewModel;
import com.xtree.mine.vo.ChooseInfoVo;
import com.xtree.mine.vo.OtherWebWithdrawVo;

import java.util.HashMap;
import java.util.Map;

import me.xtree.mvvmhabit.utils.SPUtils;
import me.xtree.mvvmhabit.utils.Utils;

/**
 * 微信/支付宝提款
 */
public class OtherWebWithdrawalDialog extends BottomPopupView {

    private LifecycleOwner owner;
    private ChooseWithdrawViewModel viewModel;
    private OtherWebWithdrawVo otherWebWithdrawVo;
    private ChooseInfoVo.ChannelInfo chooseInfoVo;
    private BasePopupView ppwError = null; // 底部弹窗 (显示错误信息)
    private BasePopupView maskLoadPopView;
    private
    @NonNull
    DialogOtherWithdrawalWebBinding binding;

    public OtherWebWithdrawalDialog(@NonNull Context context) {
        super(context);
    }

    public static OtherWebWithdrawalDialog newInstance(Context context, LifecycleOwner owner, final ChooseInfoVo.ChannelInfo chooseInfoVo) {
        OtherWebWithdrawalDialog dialog = new OtherWebWithdrawalDialog(context);
        dialog.owner = owner;
        dialog.chooseInfoVo = chooseInfoVo;
        CfLog.i("OtherWebWithdrawalDialog");
        return dialog;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_other_withdrawal_web;
    }

    @Override
    protected int getMaxHeight() {
        return (XPopupUtils.getScreenHeight(getContext()) * 90 / 100);
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        initView();
        initData();
        initViewObservable();
        requestData();

    }

    private void initView() {
        binding = DialogOtherWithdrawalWebBinding.bind(findViewById(R.id.ll_root_other));
        binding.ivwClose.setOnClickListener(v -> dismiss());
        binding.tvwTitle.setText(chooseInfoVo.title);
        binding.maskH5View.setVisibility(View.VISIBLE);

    }

    private void initData() {
        viewModel = new ChooseWithdrawViewModel((Application) Utils.getContext(), Injection.provideHomeRepository());
    }

    private void initViewObservable() {

        viewModel.otherWebWithdrawVoMutableLiveData.observe(owner, vo -> {
            dismissLoading();
            otherWebWithdrawVo = vo;
            if (otherWebWithdrawVo.channel_list != null && !otherWebWithdrawVo.channel_list.isEmpty()) {

                if (TextUtils.equals("1",otherWebWithdrawVo.channel_list.get(0).thiriframe_status) && !TextUtils.isEmpty(otherWebWithdrawVo.channel_list.get(0).thiriframe_url)) {
                    //成功状态
                    String url = otherWebWithdrawVo.channel_list.get(0).thiriframe_url;
                    if (!StringUtils.isStartHttp(url)) {
                        url = DomainUtil.getDomain2() + url;
                    }
                    //为WebView 页面添加 跳转外部的浮窗
                    //showCashPopView(url);
                    binding.nsH5View.setVisibility(View.VISIBLE);
                    binding.nsH5View.loadUrl(url, getHeader());
                    this.initWebView(binding.nsH5View);
                    binding.nsH5View.setWebViewClient(new WebViewClient() {
                        @Override
                        public boolean shouldOverrideUrlLoading(WebView view, String url) {
                            // LoadingDialog.show(getContext());
                            view.loadUrl(url);
                            return true;
                        }

                        @Override
                        public void onPageStarted(WebView view, String url, Bitmap favicon) {

                        }

                        @Override
                        public void onPageFinished(WebView view, String url) {
                            super.onPageFinished(view, url);
                            dismissLoading();
                            binding.maskH5View.setVisibility(View.GONE);
                        }
                    });
                } else if (otherWebWithdrawVo.channel_list.get(0).thiriframe_msg != null && !TextUtils.isEmpty(otherWebWithdrawVo.channel_list.get(0).thiriframe_msg)) {
                    //异常状态
                    binding.maskH5View.setVisibility(View.VISIBLE);
                    binding.nsH5View.setVisibility(View.GONE);
                    dismissLoading();
                    showErrorByChannel();
                }
            }

        });

    }

    private void requestData() {
        showMaskLoading();
        viewModel.getWithdrawOther(chooseInfoVo.type);
    }

    /* 由于权限原因弹窗*/
    private void showErrorByChannel() {
        if (ppwError == null) {
            final String title = getContext().getString(R.string.txt_kind_tips);
            final String message = getContext().getString(R.string.txt_withdrawal_not_supported_tip);
            ppwError = new XPopup.Builder(getContext()).asCustom(new MsgDialog(getContext(), title, message, true, new TipDialog.ICallBack() {
                @Override
                public void onClickLeft() {
                    ppwError.dismiss();
                    dismiss();
                }

                @Override
                public void onClickRight() {
                    ppwError.dismiss();
                    dismiss();
                }
            }));
        }
        ppwError.show();
    }

    /*显示銀行卡提款loading */
    private void showMaskLoading() {
        if (maskLoadPopView == null) {
            maskLoadPopView = new XPopup.Builder(getContext()).asCustom(new LoadingDialog(getContext()));
        }

        maskLoadPopView.show();
    }

    /*关闭loading*/
    private void dismissLoading() {
        maskLoadPopView.dismiss();
    }

    private void initWebView(WebView webView) {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setSupportZoom(true);

        //settings.setAppCacheEnabled(true);
        settings.setUseWideViewPort(true);
        //settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        //settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        settings.setLoadWithOverviewMode(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setLoadsImagesAutomatically(true);
        settings.setSupportZoom(true);
    }

    private Map<String, String> getHeader() {
        String cookie = "auth=" + SPUtils.getInstance().getString(SPKeyGlobal.USER_TOKEN)
                + ";" + SPUtils.getInstance().getString(SPKeyGlobal.USER_SHARE_COOKIE_NAME)
                + "=" + SPUtils.getInstance().getString(SPKeyGlobal.USER_SHARE_SESSID)
                + ";";

        String auth = "bearer " + SPUtils.getInstance().getString(SPKeyGlobal.USER_TOKEN);
        Map<String, String> header = new HashMap<>();
        header.put("Cookie", cookie);
        header.put("Authorization", auth);
        header.put("App-RNID", "87jumkljo");
        return header;
    }
}
