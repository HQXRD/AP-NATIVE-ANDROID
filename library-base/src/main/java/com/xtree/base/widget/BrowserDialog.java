package com.xtree.base.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.luck.picture.lib.basic.PictureSelector;
import com.luck.picture.lib.config.SelectMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.interfaces.OnResultCallbackListener;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;
import com.xtree.base.R;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.utils.CfLog;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import me.xtree.mvvmhabit.utils.SPUtils;

/**
 * 浏览器底部弹窗<p/>
 * 默认: (活动,邀请好友/VIP/...) 带header和token; <br/>
 * 三方链接: (三方游戏/H5充值) 不带header和token;
 * new XPopup.Builder(getContext()).asCustom(new BrowserDialog(getContext(), title, url)).show();
 */
public class BrowserDialog extends BottomPopupView {
    Context mContext;
    TextView tvwTitle;
    View vTitle;
    View clTitle;
    ImageView ivwClose;
    WebView mWebView;
    ImageView ivwLoading;
    ImageView ivwLaunch;
    //LinearLayout llBackground;
    int sslErrorCount = 0;

    protected String title;
    protected String url;
    protected int maxHeight = 85; // 最大高度百分比 10-100
    protected boolean isContainTitle = false; // 网页自身是否包含标题(少数情况下会包含)
    protected boolean isActivity = false; // 是否来自活动页面
    protected boolean is3rdLink = false; // 是否跳转到三方链接(如果是,就不用带header和cookie了)
    protected boolean isHideTitle = false; // 是否隐藏标题栏
    ValueCallback<Uri> mUploadCallbackBelow;
    ValueCallback<Uri[]> mUploadCallbackAboveL;

    public BrowserDialog(@NonNull Context context) {
        super(context);
        mContext = context;
    }

    public BrowserDialog(@NonNull Context context, String title, String url) {
        super(context);
        mContext = context;
        this.title = title;
        this.url = url;
    }

    public BrowserDialog(@NonNull Context context, String title, String url, boolean isContainTitle, boolean isActivity) {
        super(context);
        mContext = context;
        this.title = title;
        this.url = url;
        this.isContainTitle = isContainTitle;
        this.isActivity = isActivity;
    }

    public static BrowserDialog newInstance(@NonNull Context context, String title, String url) {
        BrowserDialog dialog = new BrowserDialog(context, title, url);
        return dialog;
    }

    public static BrowserDialog newInstance(@NonNull Context context, String url) {
        BrowserDialog dialog = new BrowserDialog(context, "", url);
        dialog.isHideTitle = true;
        return dialog;
    }

    public BrowserDialog setContainTitle(boolean isContainTitle) {
        this.isContainTitle = isContainTitle;
        return this;
    }

    public BrowserDialog set3rdLink(boolean is3rdLink) {
        this.is3rdLink = is3rdLink;
        return this;
    }

    public BrowserDialog setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
        return this;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        initView();

        if (isContainTitle) {
            vTitle.setVisibility(View.GONE);
            //llBackground.setBackground(getContext().getDrawable(R.drawable.bg_web_radius));
        }

        if (isHideTitle) {
            vTitle.setVisibility(View.GONE);
            clTitle.setVisibility(View.GONE);
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
        if (!SPUtils.getInstance().getString(SPKeyGlobal.USER_TOKEN).isEmpty()) {
            header.put("Cookie", cookie);
            header.put("Authorization", auth);
            header.put("Cache-Control", "no-cache");
            header.put("Pragme", "no-cache");
        }
        header.put("Content-Type", "application/vnd.sc-api.v1.json");
        header.put("App-RNID", "87jumkljo");

        //header.put("Source", "8");
        //header.put("UUID", TagUtils.getDeviceId(Utils.getContext()));
        //header.put("User-Agent", "Mozilla/5.0 (Linux; Android 11; SAMSUNG SM-G973U) AppleWebKit/537.36 (KHTML, like Gecko) SamsungBrowser/14.2 Chrome/87.0.4280.141 Mobile Safari/537.36");
        if (is3rdLink) {
            CfLog.d("not need header.");
            header.clear();
        }
        mWebView.addJavascriptInterface(new WebAppInterface(getContext(), ivwClose, () -> {
            CfLog.i("*******");
            //dismiss(); // only the original thread that created a view hierarchy can touch its views.
            ivwClose.post(() -> dismiss());
        }), "android");
        mWebView.loadUrl(url, header);

        ivwClose.setOnClickListener(v -> dismiss());

    }

    private void initView() {
        tvwTitle = findViewById(R.id.tvw_title);
        vTitle = findViewById(R.id.v_title);
        clTitle = findViewById(R.id.cl_title);
        ivwClose = findViewById(R.id.ivw_close);
        //llBackground = findViewById(R.id.ll_background);

        mWebView = findViewById(R.id.wv_main);
        ivwLoading = findViewById(R.id.ivw_loading);
        ivwLaunch = findViewById(R.id.ivw_launch);

        mWebView.setFitsSystemWindows(true);
        setWebView(mWebView);
        LoadingDialog.show(mContext);
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

        // 上传文件
        mWebView.setWebChromeClient(new WebChromeClient() {

            /**
             * For Android >= 4.1
             * 16(Android 4.1.2) <= API <= 20(Android 4.4W.2)回调此方法
             */
            public void openFileChooser(ValueCallback<Uri> valueCallback, String acceptType, String capture) {
                CfLog.i("*********");
                mUploadCallbackBelow = valueCallback;
                //openImageChooserActivity();
                gotoSelectMedia();
            }

            /**
             * For Android >= 5.0
             * API >= 21(Android 5.0.1)回调此方法
             */
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                CfLog.i("*********");
                // (1)该方法回调时说明版本API >= 21，此时将结果赋值给 mUploadCallbackAboveL，使之 != null
                mUploadCallbackAboveL = filePathCallback;
                //openImageChooserActivity();
                gotoSelectMedia();
                return true;
            }

        });
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                CfLog.d("onPageStarted url:  " + url);
                //Log.d("---", "onPageStarted url:  " + url);

                CfLog.d("is3rdLink: " + is3rdLink);
                if (is3rdLink) {
                    return;
                }
                if (!SPUtils.getInstance().getString(SPKeyGlobal.USER_TOKEN).isEmpty()) {
                    setCookieInside();
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                CfLog.d("onPageFinished url: " + url);
                //Log.d("---", "onPageFinished url: " + url);
                LoadingDialog.finish();
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

    /**
     * 图片选择
     */
    private void gotoSelectMedia() {
        PictureSelector.create(getContext())
                .openGallery(SelectMimeType.ofImage())
                .setMaxSelectNum(1)
                .setImageEngine(GlideEngine.createGlideEngine())
                .setCompressEngine(ImageFileCompressEngine.create())
                .forResult(new OnResultCallbackListener<LocalMedia>() {
                    @Override
                    public void onResult(ArrayList<LocalMedia> list) {

                        ArrayList<Uri> results = new ArrayList<>();
                        for (LocalMedia t : list) {
                            Uri mUri = null;
                            if (t.getWatermarkPath() != null && !t.getWatermarkPath().isEmpty()) {
                                // 水印 /storage/emulated/0/Android/data/com.xxx.xxx/files/Mark/Mark_20220609xxx.jpg
                                mUri = Uri.fromFile(new File(t.getWatermarkPath()));
                            } else if (t.isCompressed()) {
                                // 压缩后的 /storage/emulated/0/Android/data/com.xxx.xxx/cache/luban_disk_cache/CMP_20220609xxx.jpg
                                mUri = Uri.fromFile(new File(t.getCompressPath()));
                                //mUri = Uri.fromFile(new File(t.getPath())); // content://media/external/images/media/29003
                            } else {
                                // 实际路径,压缩前的 /storage/emulated/0/Pictures/Screenshots/Screenshot_20220609_xx.jpg
                                mUri = Uri.fromFile(new File(t.getRealPath()));
                            }
                            CfLog.i(mUri.toString());
                            results.add(mUri);
                        }
                        mUploadCallbackAboveL.onReceiveValue(results.toArray(new Uri[results.size()]));
                    }

                    @Override
                    public void onCancel() {

                    }
                });
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
        settings.setSupportZoom(true);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_browser;
    }

    @Override
    protected int getMaxHeight() {
        if (maxHeight < 5 || maxHeight > 100) {
            maxHeight = 85;
        }
        return (XPopupUtils.getScreenHeight(getContext()) * maxHeight / 100);
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
        if (isActivity) {
            // title:隐藏标题 transform:隐藏由下而上动画
            js += "var style = document.createElement('style'); \n" +
                    "style.type = 'text/css'; \n" +
                    "style.id = 'iOS_inject'; \n" +
                    "style.innerHTML = '.popup-wrapper > .title{ visibility: hidden !important} " +
                    ".popup-wrapper{transform: translate3d(0, 0, 0) !important; animation: none !important}'; \n" +
                    "document.head.appendChild(style);" + "\n" +
                    "document.querySelector('#iOS_inject').innerHTML = '.rndx{display: none !important;}'; \n";
        }
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
}
