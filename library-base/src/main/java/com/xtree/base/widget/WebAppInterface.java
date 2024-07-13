package com.xtree.base.widget;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.webkit.JavascriptInterface;

import androidx.core.content.ContextCompat;

import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.Gson;
import com.lxj.xpopup.XPopup;
import com.xtree.base.R;
import com.xtree.base.global.Constant;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.router.RouterActivityPath;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.base.utils.AppUtil;
import com.xtree.base.utils.BtDomainUtil;
import com.xtree.base.utils.CfLog;
import com.xtree.base.utils.DomainUtil;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import me.xtree.mvvmhabit.base.ContainerActivity;
import me.xtree.mvvmhabit.utils.SPUtils;
import me.xtree.mvvmhabit.utils.ToastUtils;

/**
 * H5调用安卓的方法
 */
public class WebAppInterface {
    final String TYPE_LOGIN = "goLogin";
    final String TYPE_HOME = "goHome";
    final String TYPE_RECHARGE = "goRecharge";
    final String TYPE_WITHDRAW = "goWithdraw";
    final String TYPE_CS = "goCustomService";
    final String TYPE_VIP = "goVip";
    final String TYPE_GAME = "goGame";
    final String TYPE_ACTIVITY_DETAIL = "goActivityDetail"; // 打开活动详情
    final String TYPE_BROWSER = "goBrowser";
    final String TYPE_SAVE_IMAGE = "goSaveImage";
    final String TYPE_BACK = "goBack";
    final String TYPE_CLOSE = "close";
    final String TYPE_ERROR_MSG = "errorMsg";

    private Context context;
    private View mView;

    private ICallBack mCallBack;

    public interface ICallBack {
        void close();

        void goBack();
    }

    public WebAppInterface(Context context, View mView, ICallBack mCallBack) {
        this.context = context;
        this.mView = mView;
        this.mCallBack = mCallBack;
    }

    @JavascriptInterface
    public void nativeFunction(String type) {
        CfLog.i("****** type: " + type);
        if (!TextUtils.isEmpty(type) && type.startsWith("{")) {
            JsParameterVo vo = new Gson().fromJson(type, JsParameterVo.class);
            goApp(vo.type, vo);
            return;
        }

        goApp(type, null);
    }

    /**
     * JavaScript 调用原生功能的方法，方法名为 nativeFunction
     *
     * @param type 类型
     * @param json 参数
     */
    @JavascriptInterface
    public void nativeFunction(String type, String json) {
        CfLog.i("****** type: " + type); // type: goBack
        //HashMap<String, Object> map = new HashMap<>();
        JsParameterVo vo = null;
        if (!TextUtils.isEmpty(json)) {
            CfLog.i("****** json: " + json); // json: {"msg":"xxx","data":[1,2,3]}
            //map = new Gson().fromJson(json, new TypeToken<HashMap>() {
            //}.getType());
            vo = new Gson().fromJson(json, JsParameterVo.class);
        }
        goApp(type, vo);
    }

    public void goApp(String type, JsParameterVo vo) {
        // 在这里处理 JavaScript 调用，并执行相应的原生功能
        CfLog.i("****** type: " + type);
        if (vo != null) {
            CfLog.i("****** vo: " + vo);
        }

        String token = SPUtils.getInstance().getString(SPKeyGlobal.USER_TOKEN);
        boolean isLogin = !TextUtils.isEmpty(token);

        switch (type) {
            case TYPE_LOGIN:
                goLogin();
                close();
                break;
            case TYPE_HOME:
                goHome();
                close();
                break;
            case TYPE_RECHARGE:
                if (isLogin) {
                    goRecharge();
                } else {
                    goLogin();
                }
                close();
                break;
            case TYPE_WITHDRAW:
                if (isLogin) {
                    goWithdraw();
                } else {
                    goLogin();
                }
                close();
                break;
            case TYPE_CS:
                AppUtil.goCustomerService(context);
                close();
                break;
            case TYPE_VIP:
                if (isLogin) {
                    BrowserActivity.start(context, context.getString(R.string.txt_vip_center),
                            DomainUtil.getH5Domain2() + Constant.URL_VIP_CENTER, true, false, true);
                } else {
                    goLogin();
                }
                close();
                break;
            case TYPE_GAME:
                if (isLogin) {
                    if (!BtDomainUtil.hasDefaultLine(BtDomainUtil.PLATFORM_FBXC)) {
                        ToastUtils.showLong(context.getString(R.string.txt_venue_initializing));
                    } else {
                        ARouter.getInstance().build(RouterActivityPath.Bet.PAGER_BET_HOME).
                                withString("KEY_PLATFORM", BtDomainUtil.PLATFORM_FBXC).navigation();
                    }
                } else {
                    goLogin();
                }
                close();
                break;
            case TYPE_ACTIVITY_DETAIL:
                CfLog.i(vo.data.toString());
                mView.post(() -> {
                    String url = DomainUtil.getH5Domain2() + "/webapp/?isNative=1#/activity/" + vo.data;
                    new XPopup.Builder(context).moveUpToKeyboard(false).asCustom(BrowserDialog.newInstance(context, url)).show();
                });
                //close(); // 不能close,否则上级页面关闭,弹窗也被关闭
                break;
            case TYPE_SAVE_IMAGE:
                base64ToBitmap(String.valueOf(vo.data));
                break;
            case TYPE_BROWSER:
                AppUtil.goBrowser(context, vo.data.toString());
            case TYPE_BACK:
                goBack();
                break;
            case TYPE_CLOSE:
                close();
                break;
            case TYPE_ERROR_MSG:
                ToastUtils.showError(vo.msg);
                close();
                break;
            default:
                CfLog.i("****** default: " + type);
                close();
                break;
        }

    }

    private void close() {
        if (mCallBack != null) {
            mCallBack.close();
        }
    }

    private void goBack() {
        if (mCallBack != null) {
            mCallBack.goBack();
        }
    }

    private void goLogin() {
        ARouter.getInstance().build(RouterActivityPath.Mine.PAGER_LOGIN_REGISTER).navigation();
    }

    private void goHome() {
        ARouter.getInstance().build(RouterActivityPath.Main.PAGER_MAIN)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK)
                .navigation();
    }

    private void goRecharge() {
        Bundle bundle = new Bundle();
        bundle.putBoolean("isShowBack", true);
        Intent intent = new Intent(context, ContainerActivity.class);
        intent.putExtra(ContainerActivity.ROUTER_PATH, RouterFragmentPath.Recharge.PAGER_RECHARGE);
        intent.putExtra(ContainerActivity.BUNDLE, bundle);
        context.startActivity(intent);
    }

    private void goWithdraw() {
        Bundle bundle = new Bundle();
        bundle.putBoolean("isOpenWithdraw", true);
        Intent intent = new Intent(context, ContainerActivity.class);
        intent.putExtra(ContainerActivity.ROUTER_PATH, RouterFragmentPath.Mine.PAGER_MY_WALLET);
        intent.putExtra(ContainerActivity.BUNDLE, bundle);
        context.startActivity(intent);
    }

    private void base64ToBitmap(String base64Str) {
        CfLog.i("****** ");
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q
                && ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ToastUtils.showLong(context.getString(R.string.txt_permission_write));
            return;
        }

        // "data":"data:image/png;base64,iVBORw***
        if (base64Str.startsWith("data:image/") && base64Str.contains(",")) {
            base64Str = base64Str.split(",")[1];
        }

        byte[] decodedString = Base64.decode(base64Str, Base64.DEFAULT);
        InputStream inputStream = new ByteArrayInputStream(decodedString);
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
        try {
            inputStream.close();
        } catch (IOException e) {
            CfLog.e(e.toString());
        }
        Uri saveUri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new ContentValues());

        if (TextUtils.isEmpty(saveUri.toString())) {
            ToastUtils.showLong(context.getString(R.string.txt_save_fail_pls_screenshot));
            return;
        }
        ToastUtils.showLong(context.getString(R.string.txt_saving));
        try {
            OutputStream os = context.getContentResolver().openOutputStream(saveUri);
            boolean result = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            CfLog.i("****** result: " + result);
            if (result) {
                ToastUtils.showLong(context.getString(R.string.txt_ok));
            } else {
                ToastUtils.showLong(context.getString(R.string.txt_save_fail_pls_screenshot));
            }
        } catch (FileNotFoundException e) {
            CfLog.e(e.toString());
        }

    }

    private class JsParameterVo {
        public String type;
        public Object data;
        public String msg = "";

        @Override
        public String toString() {
            return "JsParameterVo { " +
                    "  type='" + type + '\'' +
                    ", data='" + data + '\'' +
                    ", msg='" + msg + '\'' +
                    '}';
        }
    }

}
