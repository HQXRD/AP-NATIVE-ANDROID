package com.xtree.home.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

import com.lxj.xpopup.core.CenterPopupView;
import com.lxj.xpopup.util.XPopupUtils;
import com.xtree.base.utils.CfLog;
import com.xtree.home.R;
import com.xtree.home.databinding.DialogUpdateBinding;
import com.xtree.home.vo.UpdateVo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import me.xtree.mvvmhabit.utils.ToastUtils;

/**
 * 更新Dialog
 */
public class UpdateDialog extends CenterPopupView {
    private Context context;
    private String updateVersion;
    private String updateMessage;
    private boolean isWeakUpdate;//是否弱更新标志位 yes:若更新；NO:强更新
    private UpdateVo vo;
    ICallBack mCallBack;

    private static int progress;//下载进度
    private static final int DOWN_UPDATE = 1;
    private static final int DOWN_OVER = 2;
    private static final int DOWN_START = 3;
    private static boolean intercept = false;
    private static Thread downLoadThread;//下载线程
    private static File apkFile;
    private static final String saveFileName = "xingcai.apk";
    private static String downLoadUrl;

    DialogUpdateBinding binding;

    public interface ICallBack {
        void onUpdateCancel();
        void onUpdateForce(); //强制更新
    }

    public UpdateDialog(@NonNull Context context, UpdateVo vo, ICallBack mCallBack) {
        super(context);
        this.vo = vo;
        this.mCallBack = mCallBack;
    }

    public UpdateDialog(@NonNull Context context, boolean isWeakUpdate, UpdateVo vo, ICallBack mCallBack) {
        super(context);
        this.context = context;
        this.isWeakUpdate = isWeakUpdate;
        this.vo = vo;
        this.mCallBack = mCallBack;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        initView();
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_update;
    }

    @Override
    protected int getMaxHeight() {
        return (XPopupUtils.getScreenHeight(getContext()) * 8 / 10);
    }

    private void initView() {
        CfLog.e("UpdateDialog initView ");
        apkFile = new File(this.context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), saveFileName);

        CfLog.e("UpdateDialog initView  apkFile = " + apkFile);
        binding = DialogUpdateBinding.bind(findViewById(R.id.ll_root_update));
        if (!isWeakUpdate) {
            //强更新
            binding.dialogUpdateCancel.setVisibility(View.GONE);
        }
        updateVersion = vo.version_code;
        updateMessage = vo.content;
        binding.tvwUpdateVersion.setText(getContext().getString(R.string.txt_update) + updateVersion);
        binding.tvwUpgradeTips.setText(updateMessage);
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
            binding.dialogUpdateProgressbar.llRootUpdateProgressbar.setVisibility(View.VISIBLE);
            mCallBack.onUpdateForce();
            mHandler.sendEmptyMessage(DOWN_START);//开始下载
        });
    }

    /*下载Apk */
    private void downloadApk() {
        downLoadThread = new Thread(downApkWork);
        downLoadThread.start();
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case DOWN_START:
                    downloadApk();
                    break;
                case DOWN_UPDATE:
                    binding.dialogUpdateProgressbar.tvtBottomProgressbar.setText(progress + "%");
                    binding.dialogUpdateProgressbar.bottomProgressbar.setProgress(progress);
                    break;
                case DOWN_OVER:
                    ToastUtils.show(getContext().getString(R.string.txt_update_down_over_tip), ToastUtils.ShowType.Success);
                    installAPK();
                    break;
            }
        }
    };
    private final Runnable downApkWork = new Runnable() {
        @Override
        public void run() {
            URL url;
            try {
                SSLContext sslContext = SSLContext.getInstance("SSL");
                TrustManager[] trustManagers = {new DownUpdateManager()};
                sslContext.init(null, trustManagers, new SecureRandom());
                SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
                CfLog.e("initView downApkWork downLoadUrl = " + downLoadUrl + " \n apkFile = " + apkFile);
                url = new URL(downLoadUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                int length = connection.getContentLength();
                InputStream inputStream = connection.getInputStream();
                FileOutputStream fileOutputStream = new FileOutputStream(apkFile);
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
            } catch (Exception exception) {
                CfLog.e("APK下载失败");
            }
        }
    };

    /**
     * 安装APK
     */
    public void installAPK() {
        try {
            if (!apkFile.exists()) {
                return;
            }
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri uri = Uri.fromFile(apkFile);
            CfLog.e("1uri = " + uri);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
           /*     CfLog.e("Build.VERSION.SDK_INT >= Build.VERSION_CODES.M");
                String packageName = this.context.getApplicationContext().getPackageName();
                String authority = new StringBuilder(packageName).append(".fileProvider").toString();
                Uri apkUri = FileProvider.getUriForFile(this.context , authority , apkFile);
                intent.setDataAndType(apkUri, "application/vnd.android.package-archive");*/
                uri = FileProvider.getUriForFile(this.context, "com.itcast.fileprovider", apkFile);
                CfLog.e("2uri = " + uri);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                intent.setDataAndType(uri, "application/vnd.android.package-archive");
            } else {
                intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
            }
            context.startActivity(intent);
            android.os.Process.killProcess(android.os.Process.myPid());
        } catch (Exception exception) {

        }
    }

    private static class DownUpdateManager implements X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

}
