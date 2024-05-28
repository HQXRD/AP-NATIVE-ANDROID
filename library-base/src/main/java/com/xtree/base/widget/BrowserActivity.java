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
import android.util.Base64;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.CookieManager;
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

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.luck.picture.lib.basic.PictureSelector;
import com.luck.picture.lib.config.SelectMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.interfaces.OnResultCallbackListener;
import com.xtree.base.R;
import com.xtree.base.databinding.ActivityBrowserBinding;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.base.utils.AppUtil;
import com.xtree.base.utils.CfLog;
import com.xtree.base.utils.DomainUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import me.xtree.mvvmhabit.base.ContainerActivity;
import me.xtree.mvvmhabit.utils.SPUtils;

/**
 * 浏览器页面 <p/>
 * 默认: (活动,邀请好友/VIP/...) 带header和token; <br/>
 * 彩票: 带header, 隐藏标题头;
 * 三方链接: (三方游戏/H5充值) 不带header和token;
 */
public class BrowserActivity extends AppCompatActivity {
    public static final String ARG_TITLE = "title";
    public static final String ARG_URL = "url";
    public static final String ARG_IS_CONTAIN_TITLE = "isContainTitle";
    public static final String ARG_IS_SHOW_LOADING = "isShowLoading";
    public static final String ARG_IS_GAME = "isGame";
    public static final String ARG_IS_LOTTERY = "isLottery";
    public static final String ARG_IS_3RD_LINK = "is3rdLink";
    public static final String ARG_IS_HELP_CENTTAL = "isHelpCentral";

    View vTitle;
    TextView tvwTitle;
    ImageView ivwBack;
    WebView mWebView;
    //ImageView ivwLaunch;
    ImageView ivwCs;
    ImageView ivwMsg;
    ImageView ivwRecharge;
    ImageView ivwJump;
    View layoutRight;

    int sslErrorCount = 0;

    boolean isLottery = false; // 是否彩票, 彩票需要header,需要注入IOS标题头样式
    boolean isShowLoading = false; // 展示loading弹窗
    boolean isHelpCentral = false;

    String title = "";
    String url = "";
    boolean isContainTitle = false; // 网页自身是否包含标题(少数情况下会包含)
    boolean isGame = false; // 三方游戏, 不需要header和token
    boolean is3rdLink = false; // 是否跳转到三方链接(如果是,就不用带header和cookie了)
    ValueCallback<Uri> mUploadCallbackBelow;
    ValueCallback<Uri[]> mUploadCallbackAboveL;
    ActivityBrowserBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBrowserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //setContentView(R.layout.activity_browser);

        initView();
        title = getIntent().getStringExtra(ARG_TITLE);
        isContainTitle = getIntent().getBooleanExtra(ARG_IS_CONTAIN_TITLE, false);
        isShowLoading = getIntent().getBooleanExtra(ARG_IS_SHOW_LOADING, false);
        isGame = getIntent().getBooleanExtra(ARG_IS_GAME, false);
        isLottery = getIntent().getBooleanExtra(ARG_IS_LOTTERY, false);
        is3rdLink = getIntent().getBooleanExtra(ARG_IS_3RD_LINK, false);
        isHelpCentral = getIntent().getBooleanExtra(ARG_IS_HELP_CENTTAL, false);

        if (!TextUtils.isEmpty(title)) {
            tvwTitle.setText(title);
        }
        if (isContainTitle) {
            //vTitle.setVisibility(View.GONE);
            // 创建一个 ConstraintLayout 对象
            ConstraintLayout constraintLayout = findViewById(R.id.cl_root);
            // 创建一个 ConstraintSet 对象
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(constraintLayout);
            // 设置某个 View 的 layout_constraintTop_toTopOf 属性
            constraintSet.connect(R.id.wv_main, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0);
            // 应用 ConstraintSet 中的设置
            constraintSet.applyTo(constraintLayout);
            findViewById(R.id.cl_title).setOnClickListener(v -> {
                // 解决点击左右上角会触发被遮挡的按钮
                CfLog.i("******");
            });
        }

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
        header.put("App-RNID", "87jumkljo"); //

        //header.put("Source", "8");
        //header.put("UUID", TagUtils.getDeviceId(Utils.getContext()));
        if ((isGame || is3rdLink) && !isLottery) {
            CfLog.d("not need header.");
            header.clear(); // 游戏 header和cookie只带其中一个即可; FB只能带cookie
        }
        header.put("App-RNID", "87jumkljo"); //
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
            if (isShowLoading) {
                LoadingDialog.show(this);
            }
            mWebView.loadUrl(url, header);
        }

        if (isGame) {
            layoutRight.setVisibility(View.VISIBLE);
            initRight();
        }

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_loading);
        animation.setRepeatMode(Animation.RESTART);
        animation.setDuration(20 * 1000);
    }

    private void initView() {
        vTitle = findViewById(R.id.v_title);
        tvwTitle = findViewById(R.id.tvw_title);
        ivwBack = findViewById(R.id.ivw_back);
        mWebView = findViewById(R.id.wv_main);
        //ivwLaunch = findViewById(R.id.ivw_launch);
        ivwCs = findViewById(R.id.ivw_cs);
        ivwMsg = findViewById(R.id.ivw_msg);
        ivwRecharge = findViewById(R.id.ivw_recharge);
        ivwJump = findViewById(R.id.ivw_jump);
        layoutRight = findViewById(R.id.layout_right);

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

        // 上传文件
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                // 网页加载进度
                if (newProgress > 0 && newProgress < 100) {
                    binding.pbMain.setProgress(newProgress);
                    binding.pbMain.setVisibility(View.VISIBLE);
                } else {
                    binding.pbMain.setVisibility(View.GONE);
                }
            }

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
                if (isLottery) {
                    setLotteryCookieInside();
                } else if (is3rdLink) {
                    CfLog.d("not need cookie.");
                } else {
                    if (!SPUtils.getInstance().getString(SPKeyGlobal.USER_TOKEN).isEmpty()) {
                        setCookieInside();
                    }
                }
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

    private void initRight() {
        ivwCs.setOnClickListener(v -> {
            // 客服
            AppUtil.goCustomerService(this);
        });
        ivwMsg.setOnClickListener(v -> {
            // 消息
            startContainerFragment(RouterFragmentPath.Mine.PAGER_MSG, null);
        });
        ivwRecharge.setOnClickListener(v -> {
            // 充值
            Bundle bundle = new Bundle();
            bundle.putBoolean("isShowBack", true);
            startContainerFragment(RouterFragmentPath.Recharge.PAGER_RECHARGE, bundle);
        });
        ivwJump.setOnClickListener(v -> {
            //传递token
            String token = SPUtils.getInstance().getString(SPKeyGlobal.USER_TOKEN);
            String urlBase64 = Base64.encodeToString(url.getBytes(), Base64.DEFAULT);
            String jumpUrl = DomainUtil.getDomain2() + "/static/sessionkeeper.html?token=" + token + "&tokenExpires=3600&url=" + urlBase64;
            CfLog.i("jumpUrl: " + jumpUrl);
            // 跳至外部浏览器
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(jumpUrl));
            try {
                startActivity(intent);
            } catch (Exception e) {
                CfLog.e("链接错误或无浏览器 " + e);
            }
        });
    }

    public void startContainerFragment(String path, Bundle bundle) {
        Intent intent = new Intent(this, ContainerActivity.class);
        intent.putExtra(ContainerActivity.ROUTER_PATH, path);
        if (bundle != null) {
            intent.putExtra(ContainerActivity.BUNDLE, bundle);
        }
        startActivity(intent);
    }

    private void hideLoading() {
        //ivwLaunch.setVisibility(View.GONE);
        LoadingDialog.finish();
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
        if (!isFinishing()) {
            dialog.show();
        }
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

    /**
     * 图片选择
     */
    private void gotoSelectMedia() {
        PictureSelector.create(this)
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
        if (isHelpCentral) {
            js += "const style = document.createElement('style');" + "\n";
            js += "style.type = 'text/css';" + "\n";
            js += "style.id = 'iOS_inject';" + "\n";
            js += "document.head.appendChild(style);" + "\n";
            js += "document.querySelector('#iOS_inject').innerHTML = '" +
                    " .rndx{ display: none !important;} .rndxs{ display: none !important;}" +
                    " .portal-warpper{ display: none !important;} .root{ margin-top: -2.53rem !important;}';" + "\n";
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

    private void setLotteryCookieInside() {
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
        js += "const style = document.createElement('style');" + "\n";
        js += "style.type = 'text/css';" + "\n";
        js += "style.id = 'iOS_inject';" + "\n";
        js += "document.head.appendChild(style);" + "\n";
        js += "document.querySelector('#iOS_inject').innerHTML = '.headerH5{display: none !important;}" +
                " .rndx{ display: none !important;} .rndxs{ display: none !important;}" +
                " .all-lottery-all{ margin-top: 0 !important;} .msg{ display: none !important;}" +
                " .menu{ display: none !important;} .countdown{ margin-right: .8rem;}';" + "\n";
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

    public static void start(Context ctx, String title, String url, boolean isContainTitle, boolean isGame) {
        CfLog.i(title + ", isContainTitle: " + false + ", url: " + url);
        Intent it = new Intent(ctx, BrowserActivity.class);
        it.putExtra(BrowserActivity.ARG_TITLE, title);
        it.putExtra(BrowserActivity.ARG_URL, url);
        it.putExtra(BrowserActivity.ARG_IS_CONTAIN_TITLE, isContainTitle);
        it.putExtra(BrowserActivity.ARG_IS_GAME, isGame);
        ctx.startActivity(it);
    }

    public static void start(Context ctx, String title, String url, boolean isContainTitle, boolean isGame, boolean isShowLoading) {
        CfLog.i(title + ", isContainTitle: " + false + ", url: " + url);
        Intent it = new Intent(ctx, BrowserActivity.class);
        it.putExtra(BrowserActivity.ARG_TITLE, title);
        it.putExtra(BrowserActivity.ARG_URL, url);
        it.putExtra(BrowserActivity.ARG_IS_CONTAIN_TITLE, isContainTitle);
        it.putExtra(BrowserActivity.ARG_IS_GAME, isGame);
        it.putExtra(BrowserActivity.ARG_IS_SHOW_LOADING, isShowLoading);
        ctx.startActivity(it);
    }

    public static void start(Context ctx, String title, String url, boolean isContainTitle, boolean isGame, boolean isShowLoading, boolean isHelpCentral) {
        CfLog.i(title + ", isContainTitle: " + false + ", url: " + url);
        Intent it = new Intent(ctx, BrowserActivity.class);
        it.putExtra(BrowserActivity.ARG_TITLE, title);
        it.putExtra(BrowserActivity.ARG_URL, url);
        it.putExtra(BrowserActivity.ARG_IS_CONTAIN_TITLE, isContainTitle);
        it.putExtra(BrowserActivity.ARG_IS_GAME, isGame);
        it.putExtra(BrowserActivity.ARG_IS_SHOW_LOADING, isShowLoading);
        it.putExtra(BrowserActivity.ARG_IS_HELP_CENTTAL, isHelpCentral);
        ctx.startActivity(it);
    }
}