package com.xtree.base.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;
import com.xtree.base.R;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.utils.CfLog;

import java.util.HashMap;
import java.util.Map;

import me.xtree.mvvmhabit.utils.SPUtils;

/**
 * 浏览器底部弹窗<p/>
 * new XPopup.Builder(getContext()).asCustom(new BrowserDialog(getContext(), title, url)).show();
 */
public class BrowserDialog extends BottomPopupView {

    TextView tvwTitle;
    View vTitle;
    ImageView ivwClose;
    WebView mWebView;
    ImageView ivwLoading;
    ImageView ivwLaunch;
    int sslErrorCount = 0;

    String title;
    String url;
    boolean isContainTitle = false; // 网页自身是否包含标题(少数情况下会包含)

    public BrowserDialog(@NonNull Context context) {
        super(context);
    }

    public BrowserDialog(@NonNull Context context, String title, String url) {
        super(context);
        this.title = title;
        this.url = url;
    }

    public BrowserDialog(@NonNull Context context, String title, String url, boolean isContainTitle) {
        super(context);
        this.title = title;
        this.url = url;
        this.isContainTitle = isContainTitle;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        initView();

        if (isContainTitle) {
            vTitle.setVisibility(View.GONE);
        }

        tvwTitle.setText(title);
        ivwLoading.setVisibility(View.GONE);

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
        header.put("App-RNID", "87jumkljo");

        //header.put("Source", "8");
        //header.put("UUID", TagUtils.getDeviceId(Utils.getContext()));
        //header.put("User-Agent", "Mozilla/5.0 (Linux; Android 11; SAMSUNG SM-G973U) AppleWebKit/537.36 (KHTML, like Gecko) SamsungBrowser/14.2 Chrome/87.0.4280.141 Mobile Safari/537.36");

        mWebView.loadUrl(url, header);

        ivwClose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }

    private void initView() {
        tvwTitle = findViewById(R.id.tvw_title);
        vTitle = findViewById(R.id.v_title);
        ivwClose = findViewById(R.id.ivw_close);

        mWebView = findViewById(R.id.wv_main);
        ivwLoading = findViewById(R.id.ivw_loading);
        ivwLaunch = findViewById(R.id.ivw_launch);

        mWebView.setFitsSystemWindows(true);
        setWebView(mWebView);

        // 下载文件
        mWebView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                CfLog.d("onDownloadStart url: " + url);
                /*CfLog.i("url: " + url
                        + ",\n contentLength: " + contentLength
                        + " (" + contentLength / 1024 / 1024 + "." + 100 * (contentLength / 1024 % 1024) / 1024 + "M)"
                        + ",\n mimetype: " + mimetype
                        + ",\n contentDisposition: " + contentDisposition
                        + ",\n userAgent: " + userAgent
                );*/
                //Log.d("---", "onDownloadStart url: " + url);
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                getContext().startActivity(intent);
            }
        });

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                CfLog.d("onPageStarted url:  " + url);
                //Log.d("---", "onPageStarted url:  " + url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                CfLog.d("onPageFinished url: " + url);
                //Log.d("---", "onPageFinished url: " + url);
                hideLoading();
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                //handler.proceed();
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
                Toast.makeText(getContext(), R.string.network_failed, Toast.LENGTH_SHORT).show();
            }

        });

    }

    private void hideLoading() {
        ivwLoading.setVisibility(View.GONE);
        ivwLoading.clearAnimation();
        ivwLaunch.setVisibility(View.GONE);
    }

    private void tipSsl(WebView view, SslErrorHandler handler) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setMessage(R.string.ssl_failed_will_u_continue); // SSL认证失败，是否继续访问？
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                handler.proceed();// 接受https所有网站的证书
            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                handler.cancel();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void setWebView(WebView webView) {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        //settings.setAppCacheEnabled(true);
        settings.setUseWideViewPort(true);
        //settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        //settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        settings.setLoadWithOverviewMode(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setLoadsImagesAutomatically(true);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_browser;
    }

    @Override
    protected int getMaxHeight() {
        //return super.getMaxHeight();
        return (XPopupUtils.getScreenHeight(getContext()) * 85 / 100);
    }

}
