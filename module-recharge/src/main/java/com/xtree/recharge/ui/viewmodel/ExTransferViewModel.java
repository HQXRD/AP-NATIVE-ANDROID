package com.xtree.recharge.ui.viewmodel;

import android.app.Application;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;

import com.luck.picture.lib.basic.PictureSelector;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.config.SelectMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.interfaces.OnResultCallbackListener;
import com.xtree.base.utils.CfLog;
import com.xtree.base.widget.GlideEngine;
import com.xtree.base.widget.ImageFileCompressEngine;
import com.xtree.recharge.data.RechargeRepository;
import com.xtree.recharge.ui.fragment.ExSlipExampleDialogFragment;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import me.xtree.mvvmhabit.base.BaseViewModel;

/**
 * Created by KAKA on 2024/5/28.
 * Describe: 极速转账viewModel
 */
public class ExTransferViewModel extends BaseViewModel<RechargeRepository> {
    public ExTransferViewModel(@NonNull Application application) {
        super(application);
    }

    public ExTransferViewModel(@NonNull Application application, RechargeRepository model) {
        super(application, model);
    }

    public MutableLiveData<String> paymentTime = new MutableLiveData<>();
    public MutableLiveData<String> waitTime = new MutableLiveData<>();
    //凭证图片
    public MutableLiveData<Uri> voucher = new MutableLiveData<>();

    private WeakReference<FragmentActivity> mActivity = null;

    public void initData(FragmentActivity mActivity) {
        setActivity(mActivity);

        int timeCount = 60 * 30;
        Flowable.intervalRange(0, timeCount, 0, 1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(aLong -> {
                    long l = timeCount - aLong;
                    paymentTime.setValue(formatSeconds(l));
                })
                .doOnComplete(() -> {
                })
                .subscribe();
    }

    private void setActivity(FragmentActivity mActivity) {
        this.mActivity = new WeakReference<>(mActivity);
    }

    public String formatSeconds(long seconds) {
        long minutes = seconds / 60;
        long remainingSeconds = seconds % 60;
        return String.format("%02d:%02d", minutes, remainingSeconds);
    }

    /**
     * 取消订单
     */
    public void cancle() {

    }

    /**
     * 上传凭证
     */
    public void uploadVoucher() {

    }

    /**
     * 确认付款
     */
    public void confirmationPayment() {

    }

    /**
     * 回执单示例
     */
    public void showExample() {
        ExSlipExampleDialogFragment.show(mActivity.get());
    }

    /**
     * 图片选择
     */
    public void gotoSelectMedia() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                Intent getpermission = new Intent();
                getpermission.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                mActivity.get().startActivity(getpermission);
                return;
            }
        }
        PictureSelector.create(mActivity.get())
                .openGallery(SelectMimeType.ofImage())
                .isDisplayCamera(false)
                .setMaxSelectNum(1)
                .setImageEngine(GlideEngine.createGlideEngine())
                .setCompressEngine(ImageFileCompressEngine.create())
                .forResult(new OnResultCallbackListener<LocalMedia>() {
                    @Override
                    public void onResult(ArrayList<LocalMedia> result) {
                        if (result != null) {
                            for (int i = 0; i < result.size(); i++) {
                                String imageRealPathString = result.get(i).getCompressPath();
                                if (TextUtils.isEmpty(imageRealPathString)) {
                                    imageRealPathString = result.get(i).getRealPath();
                                }

                                if (PictureMimeType.isContent(imageRealPathString)) {
                                    voucher.setValue(Uri.parse(imageRealPathString));
                                } else {
                                    voucher.setValue(Uri.fromFile(new File(imageRealPathString)));
                                }
                                CfLog.i("获取图片地址是 uri ====== " + voucher.getValue());
                            }
                        }
                    }

                    @Override
                    public void onCancel() {

                    }
                });
    }

    /**
     * 上传图片
     */
    private void uploadImage(String imageFile) {
//        HashMap<String, String> uploadMap = new HashMap<String, String>();
//        uploadMap.put("type", "m4");
//        uploadMap.put("nonce", UuidUtil.getID16());//传入UUID);
//        uploadMap.put("filetype", "image/png");
//        uploadMap.put("filedata", ImageUploadUtil.bitmapToString(imageFile));
//        viewModel.feedbackImageUp(uploadMap);
    }

}

