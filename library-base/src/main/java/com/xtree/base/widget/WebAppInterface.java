package com.xtree.base.widget;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.JavascriptInterface;

import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xtree.base.R;
import com.xtree.base.global.Constant;
import com.xtree.base.router.RouterActivityPath;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.base.utils.AppUtil;
import com.xtree.base.utils.CfLog;
import com.xtree.base.utils.DomainUtil;

import java.util.HashMap;
import java.util.Map;

import me.xtree.mvvmhabit.base.ContainerActivity;
import me.xtree.mvvmhabit.utils.ToastUtils;

/**
 * H5调用安卓的方法
 */
public class WebAppInterface {
    final String TYPE_HOME = "goHome";
    final String TYPE_RECHARGE = "goRecharge";
    final String TYPE_WITHDRAW = "goWithdraw";
    final String TYPE_CS = "goCustomService";
    final String TYPE_VIP = "goVip";
    final String TYPE_BACK = "goBack";
    final String TYPE_CLOSE = "close";
    final String TYPE_ERROR_MSG = "errorMsg";

    private Context context;

    private ICallBack mCallBack;

    public interface ICallBack {
        void close();
    }

    public WebAppInterface(Context context, ICallBack mCallBack) {
        this.context = context;
        this.mCallBack = mCallBack;
    }

    @JavascriptInterface
    public void nativeFunction(String type) {
        CfLog.i("****** type: " + type);
        goApp(type, new HashMap<>());
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
        HashMap<String, Object> map = new HashMap<>();
        if (json != null) {
            CfLog.i("****** json: " + json); // json: {"msg":"xxx","data":[1,2,3]}
            map = new Gson().fromJson(json, new TypeToken<HashMap>() {
            }.getType());
        }
        goApp(type, map);
    }

    public void goApp(String type, Map<String, Object> map) {
        // 在这里处理 JavaScript 调用，并执行相应的原生功能
        CfLog.i("****** type: " + type);
        if (map != null) {
            CfLog.i("****** map: " + map);
        }
        switch (type) {
            case TYPE_HOME:
                goHome();
                close();
                break;
            case TYPE_RECHARGE:
                goRecharge();
                close();
                break;
            case TYPE_WITHDRAW:
                goWithdraw();
                close();
                break;
            case TYPE_CS:
                AppUtil.goCustomerService(context);
                close();
                break;
            case TYPE_VIP:
                BrowserActivity.start(context, context.getString(R.string.txt_vip_center),
                        DomainUtil.getDomain2() + Constant.URL_VIP_CENTER, true, false, true);
                close();
                break;
            case TYPE_BACK:
            case TYPE_CLOSE:
                close();
                break;
            case TYPE_ERROR_MSG:
                if (map.containsKey("msg")) {
                    ToastUtils.showError(map.get("msg").toString());
                }
                close();
                break;
            default:
                CfLog.i("****** default: " + type);
                break;
        }

    }

    private void close() {
        if (mCallBack != null) {
            mCallBack.close();
        }
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

}
