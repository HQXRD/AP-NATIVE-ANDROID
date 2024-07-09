package com.xtree.base.widget;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

import com.lxj.xpopup.core.CenterPopupView;
import com.lxj.xpopup.util.XPopupUtils;
import com.xtree.base.R;
import com.xtree.base.databinding.DialogUpdateBinding;
import com.xtree.base.utils.AppUtil;
import com.xtree.base.utils.CfLog;
import com.xtree.base.utils.DomainUtil;
import com.xtree.base.vo.AppUpdateVo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

import me.xtree.mvvmhabit.utils.ToastUtils;

/*App更新Dialog*/
public class AppUpdateDialog extends CenterPopupView {

    private static final int DOWN_UPDATE = 1;
    private static final int DOWN_OVER = 2;
    private static final int DOWN_START = 3;
    private static final int DOWN_FAIL = 4;

    private Context context;
    private boolean isWeakUpdate;//是否弱更新标志位 yes:若更新；NO:强更新
    private boolean isTest;

    private AppUpdateVo vo;
    private IAppUpdateCallBack mCallBack;

    private static int progress;//下载进度
    private static boolean intercept = false;
    private static Thread downLoadThread;//下载线程
    private static File apkFile;
    private static String saveFileName = "tmp.apk";
    private static String downLoadUrl;

    private DialogUpdateBinding binding;

    public interface IAppUpdateCallBack {
        void onUpdateCancel();

        void onUpdateForce(); //强制更新
    }

    public AppUpdateDialog(@NonNull Context context) {
        super(context);
    }

    public AppUpdateDialog(@NonNull Context context, boolean isWeakUpdate, AppUpdateVo vo, IAppUpdateCallBack mCallBack) {
        super(context);
        this.context = context;
        this.isWeakUpdate = isWeakUpdate;
        this.vo = vo;
        this.mCallBack = mCallBack;
    }

    public AppUpdateDialog(@NonNull Context context, boolean isWeakUpdate, boolean isTest, IAppUpdateCallBack mCallBack) {
        super(context);
        this.context = context;
        this.isWeakUpdate = isWeakUpdate;
        this.isTest = isTest;
        this.mCallBack = mCallBack;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        try {
            initView();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_update;
    }

    @Override
    protected int getMaxHeight() {
        return (XPopupUtils.getScreenHeight(getContext()) * 8 / 10);
    }

    private void initView() throws IOException {
        CfLog.i("****** ");
        saveFileName = vo.version_name + ".apk";
        apkFile = new File(this.context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), saveFileName);
        apkFile.deleteOnExit(); //删除旧的文件,重新下载
        if (!vo.download_url.startsWith("http")) {
            vo.download_url = DomainUtil.getH5Domain2() + vo.download_url;
        }
        CfLog.i("download_url: " + vo.download_url);

        CfLog.i("apkFile: " + apkFile.getAbsolutePath());
        binding = DialogUpdateBinding.bind(findViewById(R.id.ll_root_update));
        if (!isWeakUpdate) {
            //强更新
            binding.dialogUpdateCancel.setVisibility(View.GONE);
        }

        binding.tvwUpdateVersion.setText(getContext().getString(R.string.txt_update) + " " + vo.version_name);
        binding.tvwUpgradeTips.setText(vo.content);
        binding.dialogUpdateCancel.setOnClickListener(v -> {
            mCallBack.onUpdateCancel();
        });

        //立即更新
        binding.dialogUpdateConfirm.setOnClickListener(v -> {
            downLoadUrl = vo.download_url;
            CfLog.e("initView downLoadUrl = " + downLoadUrl);
            binding.tvwTitle.setText(getContext().getString(R.string.txt_update_tip));
            binding.tvwUpdateVersion.setVisibility(View.INVISIBLE);
            binding.dialogUpdateConfirm.setVisibility(View.INVISIBLE);

            binding.linearLayout.setVisibility(View.INVISIBLE);
            binding.tvwUpgradeTips.setVisibility(View.GONE);
            binding.llUpdateProgressbar.setVisibility(View.VISIBLE);
            mCallBack.onUpdateForce();
            mHandler.sendEmptyMessage(DOWN_START);//开始下载

            //打开浏览器下载安装
            binding.dialogUpdateTip.setVisibility(View.VISIBLE);
            binding.dialogUpdateTip.setText(getContext().getString(R.string.txt_update_bottom_error_tip));
            binding.dialogUpdateTip.setOnClickListener(v1 -> {
                if (!TextUtils.isEmpty(vo.download_url)) {
                    AppUtil.goBrowser(getContext(), vo.download_url);
                } else {
                    CfLog.e("****************  download url is null *********** ");
                }
            });
        });
    }

    /*下载Apk */
    private void downloadApk() {
        downLoadThread = new Thread(downApkWork);
        downLoadThread.start();
    }

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case DOWN_START:
                    downloadApk();
                    break;
                case DOWN_UPDATE:
                    binding.tvtBottomProgressbar.setText(progress + "%");
                    binding.bottomProgressbar.setProgress(progress);
                    break;
                case DOWN_OVER:
                    ToastUtils.show(getContext().getString(R.string.txt_update_down_over_tip), ToastUtils.ShowType.Success);
                    try {
                        installAPK();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case DOWN_FAIL:
                    ToastUtils.showSuccess(getContext().getString(R.string.txt_update_down_fail_tip));
                    dismiss();
                    break;
                default:
                    break;
            }
        }
    };
    private final Runnable downApkWork = new Runnable() {
        @Override
        public void run() {
            URL url;
            HttpURLConnection connection = null;
            InputStream inputStream = null;
            FileOutputStream fileOutputStream = null;
            try {
                //SSLContext sslContext = SSLContext.getInstance("SSL");
                //TrustManager[] trustManagers = {new DownUpdateManager()};
                //sslContext.init(null, trustManagers, new SecureRandom());
                //SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
                CfLog.i("initView downApkWork downLoadUrl = " + downLoadUrl + " \n apkFile = " + apkFile);
                url = new URL(downLoadUrl);
                connection = (HttpURLConnection) url.openConnection();
                connection.setReadTimeout(8000);
                connection.setConnectTimeout(180 * 1000);
                connection.connect();
                int length = connection.getContentLength();
                inputStream = connection.getInputStream();
                fileOutputStream = new FileOutputStream(apkFile);
                int count = 0;
                byte[] buf = new byte[1024];
                while (!intercept) {
                    int numberRead = inputStream.read(buf);
                    count += numberRead;
                    progress = (int) (((float) count / length) * 100);

                    mHandler.sendEmptyMessage(DOWN_UPDATE);
                    if (numberRead <= 0) {
                        mHandler.sendEmptyMessage(DOWN_OVER);
                        break;
                    }
                    fileOutputStream.write(buf, 0, numberRead);
                }
                fileOutputStream.close();
                inputStream.close();
            } catch (SocketTimeoutException e) {
                CfLog.e("APK download timeout: " + e);
                mHandler.sendEmptyMessage(DOWN_FAIL);
            } catch (Exception e) {
                CfLog.e("APK download error: " + e);
                mHandler.sendEmptyMessage(DOWN_FAIL);
            } finally {
                try {
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                    }
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    if (connection != null) {
                        connection.disconnect();
                    }
                } catch (IOException e) {
                    CfLog.e(e.toString());
                }

            }
        }
    };

    /**
     * 安装APK
     */
    public void installAPK() throws IOException {
        CfLog.i("******");
        if (apkFile == null || !apkFile.exists()) {
            CfLog.e("************installAPK  apkFile is null************");
            return;
        }

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        //Uri uri = Uri.fromFile(apkFile);
        //动态判断是否已经申请内存卡权限
        Uri uri =getApkUri(apkFile);
        CfLog.e("**************** installAPK  ****************"+uri.toString());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            uri = FileProvider.getUriForFile(this.context, getContext().getPackageName(), apkFile);
            CfLog.i(uri.toString()); //
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
        } else {
            /*Android M之前老版本写法*/
            intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
        //修复 HQAP2-4223 待验证
        //android.os.Process.killProcess(android.os.Process.myPid());

    }

    private Uri getApkUri(File apkFile) throws IOException {
        //如果没有设置SDcard写权限，需要授权才能安装
        try {
            String[] command = {"chmod", "777", apkFile.toString()};
            ProcessBuilder builder = new ProcessBuilder(command);
            builder.start();
        } catch (IOException ioException) {
            CfLog.e("**************** getApkUri  IOException  ****************");
        }

        Uri uri = Uri.fromFile(apkFile);
        return uri;
    }

    /**
     * 用于验证与信任SSL/TLS证书的管理器
     */
    private static class DownUpdateManager implements X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) {

        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) {

        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

}
