package com.xtree.base.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xtree.base.R;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.utils.CfLog;

import java.util.HashMap;
import java.util.Map;

import me.xtree.mvvmhabit.utils.SPUtils;

public class BrowserActivity extends AppCompatActivity {
    public static final String ARG_TITLE = "title";
    public static final String ARG_URL = "url";
    public static final String ARG_IS_CONTAIN_TITLE = "isContainTitle";

    View vTitle;
    TextView tvwTitle;
    ImageView ivwBack;
    WebView mWebView;
    ImageView ivwLoading;
    ImageView ivwLaunch;
    int sslErrorCount = 0;

    String title = "";
    String url = "";
    boolean isContainTitle = false; // 网页自身是否包含标题(少数情况下会包含)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);

        initView();
        title = getIntent().getStringExtra(ARG_TITLE);
        isContainTitle = getIntent().getBooleanExtra(ARG_IS_CONTAIN_TITLE, false);
        if (!TextUtils.isEmpty(title)) {
            tvwTitle.setText(title);
        }
        if (isContainTitle) {
            vTitle.setVisibility(View.GONE);
        }

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

        //header.put("Source", "8");
        //header.put("UUID", TagUtils.getDeviceId(Utils.getContext()));
        CfLog.d("header: " + header); // new Gson().toJson(header)
        url = getIntent().getStringExtra("url");
        //setCookie(cookie, url); // 设置 cookie
        Uri uri = getIntent().getData();
        if (uri != null && TextUtils.isEmpty(url)) {
            url = uri.toString();
        }
        CfLog.i("url: " + url);
        if (TextUtils.isEmpty(url)) {
            finish();
        } else {
            mWebView.loadUrl(url, header);
        }

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_loading);
        animation.setRepeatMode(Animation.RESTART);
        animation.setDuration(20 * 1000);
        ivwLoading.startAnimation(animation);
    }

    private void initView() {
        vTitle = findViewById(R.id.v_title);
        tvwTitle = findViewById(R.id.tvw_title);
        ivwBack = findViewById(R.id.ivw_back);
        mWebView = findViewById(R.id.wv_main);
        ivwLoading = findViewById(R.id.ivw_loading);
        ivwLaunch = findViewById(R.id.ivw_launch);

        ivwBack.setOnClickListener(v -> finish());

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
                startActivity(intent);
            }
        });

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                CfLog.d("onPageStarted url:  " + url);
                //Log.d("---", "onPageStarted url:  " + url);
                setCookieInside();
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
                Toast.makeText(getBaseContext(), R.string.network_failed, Toast.LENGTH_SHORT).show();
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

    private void setCookie(String cookie, String url) {
        CookieManager cm = CookieManager.getInstance();
        cm.removeSessionCookies(null);
        cm.flush();
        //cm.removeSessionCookie();
        //CookieSyncManager.getInstance().sync();
        cm.setAcceptCookie(true);
        cm.setCookie(url, cookie);

        cm.setAcceptThirdPartyCookies(mWebView, true);
    }

    private void setCookieInside() {
        // auth, _sessionHandler 是验证类业务用的,比如绑USDT/绑YHK
        // AUTH, USER-PROFILE 是给VIP中心/报表 用的

        String token = SPUtils.getInstance().getString(SPKeyGlobal.USER_TOKEN);
        String sessid = SPUtils.getInstance().getString(SPKeyGlobal.USER_SHARE_SESSID);

        String json = SPUtils.getInstance().getString(SPKeyGlobal.HOME_PROFILE);
        HashMap mProfileVo = new Gson().fromJson(json, new TypeToken<HashMap>() {
        }.getType());

        long expires = System.currentTimeMillis() + 24 * 60 * 60 * 1000;
        HashMap map = new HashMap<>();
        map.put("data", mProfileVo);
        map.put("expires", expires);
        String userProfile = new Gson().toJson(map);

        map.clear();
        map.put("data", token);
        map.put("expires", expires);
        String auth = new Gson().toJson(map);

        String js = "";
        js += "(function() {" + "\n";
        js += "const d = new Date();" + "\n";
        js += "d.setTime(d.getTime() + (24*60*60*1000));" + "\n";
        js += "let expires = \"expires=\"+ d.toUTCString();" + "\n";
        js += "document.cookie = \"auth=" + token + ";\" + expires + \";path=/\";" + "\n";
        js += "document.cookie = \"_sessionHandler=" + sessid + ";\" + expires + \";path=/\";" + "\n";
        js += "localStorage.setItem('USER-PROFILE', '" + userProfile + "');" + "\n";
        js += "localStorage.setItem('AUTH', '" + auth + "');" + "\n";
        js += "})()" + "\n";

        CfLog.i(js.replace("\n", " \t"));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mWebView.evaluateJavascript(js, null);
        } else {
            mWebView.loadUrl("javascript:" + js);
        }
    }

    /**
     * @param ctx            上下文
     * @param title          标题
     * @param url            链接
     * @param isContainTitle url对应的网页是否包含标题,默认false
     */
    public static void start(Context ctx, String title, String url, boolean isContainTitle) {
        CfLog.i(title + ", isContainTitle: " + isContainTitle + ", url: " + url);
        Intent it = new Intent(ctx, BrowserActivity.class);
        it.putExtra(BrowserActivity.ARG_TITLE, title);
        it.putExtra(BrowserActivity.ARG_URL, url);
        it.putExtra(BrowserActivity.ARG_IS_CONTAIN_TITLE, isContainTitle);
        ctx.startActivity(it);
    }

    public static void start(Context ctx, String title, String url) {
        CfLog.i(title + ", isContainTitle: " + false + ", url: " + url);
        Intent it = new Intent(ctx, BrowserActivity.class);
        it.putExtra(BrowserActivity.ARG_TITLE, title);
        it.putExtra(BrowserActivity.ARG_URL, url);
        it.putExtra(BrowserActivity.ARG_IS_CONTAIN_TITLE, false);
        ctx.startActivity(it);
    }

}