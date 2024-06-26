package com.xtree.mine.ui.fragment;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.luck.picture.lib.basic.PictureSelector;
import com.luck.picture.lib.config.SelectMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.interfaces.OnResultCallbackListener;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.utils.CfLog;
import com.xtree.base.utils.DomainUtil;
import com.xtree.base.utils.StringUtils;
import com.xtree.base.widget.GlideEngine;
import com.xtree.base.widget.ImageFileCompressEngine;
import com.xtree.base.widget.LoadingDialog;
import com.xtree.base.widget.MsgDialog;
import com.xtree.base.widget.TipDialog;
import com.xtree.mine.R;
import com.xtree.mine.data.Injection;
import com.xtree.mine.databinding.DialogOtherWithdrawalWebBinding;
import com.xtree.mine.ui.viewmodel.ChooseWithdrawViewModel;
import com.xtree.mine.vo.ChooseInfoVo;
import com.xtree.mine.vo.OtherWebWithdrawVo;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import me.xtree.mvvmhabit.utils.SPUtils;
import me.xtree.mvvmhabit.utils.Utils;

/**
 * 微信/支付宝提款
 */
public class OtherWebWithdrawalDialog extends BottomPopupView implements FruitHorOtherRecyclerViewAdapter.IOtherFruitHorCallback {

    private LifecycleOwner owner;
    private ChooseWithdrawViewModel viewModel;
    private OtherWebWithdrawVo otherWebWithdrawVo;
    private ChooseInfoVo.ChannelInfo chooseInfoVo;
    private BasePopupView ppwError = null; // 底部弹窗 (显示错误信息)
    private BasePopupView maskLoadPopView;
    private FruitHorOtherRecyclerViewAdapter recyclerViewAdapter;
    ValueCallback<Uri> mUploadCallbackBelow;
    ValueCallback<Uri[]> mUploadCallbackAboveL;
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
        CfLog.i("OtherWebWithdrawalDialog  dialog.chooseInfoVo = " + dialog.chooseInfoVo.toString());
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
        if (chooseInfoVo != null && !TextUtils.isEmpty(chooseInfoVo.title)) {
            binding.tvwTitle.setText(chooseInfoVo.title);
        } else {
            binding.tvwTitle.setText(getContext().getString(R.string.txt_withdrawal));
        }
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

                if (TextUtils.equals("1", otherWebWithdrawVo.channel_list.get(0).thiriframe_status)
                        && !TextUtils.isEmpty(otherWebWithdrawVo.channel_list.get(0).thiriframe_url)) {
                    refreshSetUI();
                } else if (otherWebWithdrawVo.channel_list.get(0).thiriframe_msg != null
                        && !TextUtils.isEmpty(otherWebWithdrawVo.channel_list.get(0).thiriframe_msg)) {
                    //异常状态
                    binding.maskH5View.setVisibility(View.VISIBLE);
                    binding.nsH5View.setVisibility(View.GONE);
                    dismissLoading();
                    showErrorByChannel();
                }
            }

        });

    }

    private void initOtherWebView(final OtherWebWithdrawVo vo) {
        //成功状态
        String url = vo.channel_list.get(0).thiriframe_url;
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

        // 上传文件
        binding.nsH5View.setWebChromeClient(new WebChromeClient() {

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

    /**
     * 刷新初始UI
     */
    private void refreshSetUI() {
        if (otherWebWithdrawVo.channel_list.isEmpty() || otherWebWithdrawVo.channel_list == null) {
            binding.llShowChooseCard.setVisibility(View.GONE);
        } else {
            refreshTopUI(otherWebWithdrawVo);
            initOtherWebView(otherWebWithdrawVo);
        }

        //注意：每天限制提款5次，您已提款1次 提款时间为00:01至00:00，您今日剩余提款额度为 199900.00元
        final  String notice = "<font color=#EE5A5A>注意:</font>";
        String times, count, starttime, endtime, rest;
        times = "<font color=#EE5A5A>" + String.valueOf(otherWebWithdrawVo.times) + "</font>";
        count = "<font color=#EE5A5A>" + otherWebWithdrawVo.count + "</font>";
        starttime = "<font color=#000000>" + otherWebWithdrawVo.wraptime.starttime + "</font>";
        endtime = "<font color=#000000>" + otherWebWithdrawVo.wraptime.endtime + "</font>";
        rest = StringUtils.formatToSeparate(Float.valueOf(otherWebWithdrawVo.rest));
        String testTxt = "<font color=#EE5A5A>" + rest + "</font>";
        String format = getContext().getResources().getString(R.string.txt_withdraw_bank_top_tip);
        String textSource = String.format(format,notice, times, count, starttime, endtime, testTxt);

        binding.tvNotice.setText(HtmlCompat.fromHtml(textSource, HtmlCompat.FROM_HTML_MODE_LEGACY));

    }

    private void refreshTopUI(OtherWebWithdrawVo vo) {

        for (int i = 0; i < vo.channel_list.size(); i++) {
          /*  if (i == 0) {
                vo.channel_list.get(0).flag = true;
            } else {
                vo.channel_list.get(i).flag = false;
            }
            selectorTopChannel = vo.channel_list.get(0);*/
        }
        recyclerViewAdapter = new FruitHorOtherRecyclerViewAdapter(vo.channel_list, this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        binding.rvShowChooseCard.setLayoutManager(layoutManager);
        binding.rvShowChooseCard.addItemDecoration(new FruitHorRecyclerViewAdapter.SpacesItemDecoration(10));
        binding.rvShowChooseCard.setAdapter(recyclerViewAdapter);
        binding.rvShowChooseCard.setItemAnimator(new DefaultItemAnimator());
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

    private void initWebView(final WebView webView) {
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

    @Override
    public void callbackWithFruitHor(OtherWebWithdrawVo.ChannelInfo selectVo) {

    }
}
