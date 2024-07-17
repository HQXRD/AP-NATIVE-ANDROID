package com.xtree.mine.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xtree.base.router.RouterActivityPath;
import com.xtree.base.utils.CfLog;
import com.xtree.base.utils.TagUtils;
import com.xtree.base.widget.BrowserActivity;
import com.xtree.base.widget.WebAppInterface;
import com.xtree.mine.R;

import java.util.HashMap;

import me.xtree.mvvmhabit.utils.ToastUtils;

/**
 * 全局验证页 (HQAP2-4516)
 */
@Route(path = RouterActivityPath.Mine.PAGER_GLOBE_VERIFY)
public class GlobeVerifyActivity extends BrowserActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CfLog.i("******");
    }

    public WebAppInterface.ICallBack getCallBack() {

        return new WebAppInterface.ICallBack() {
            @Override
            public void close() {
                finish();
            }

            @Override
            public void goBack() {
                finish();
            }

            @Override
            public void callBack(String type, Object obj) {
                CfLog.i("type: " + type);
                if (TextUtils.equals(type, "captchaVerifySucceed")) {
                    TagUtils.tagEvent(getBaseContext(), "captchaVerifyOK");
                    ARouter.getInstance().build(RouterActivityPath.Main.PAGER_SPLASH)
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            .navigation();
                    finish();
                } else if (TextUtils.equals(type, "captchaVerifyFail")) {
                    TagUtils.tagEvent(getBaseContext(), "captchaVerifyFail");
                    CfLog.e("type error... type: " + type);
                    doFail(type, obj);
                } else {
                    TagUtils.tagEvent(getBaseContext(), "captchaVerifyError");
                    CfLog.e("type error... type: " + type);
                    doFail(type, obj);
                }
            }
        };
    }

    private void doFail(String type, Object obj) {
        CfLog.e("type: " + type);
        String json = "";
        HashMap<String, String> map = new HashMap<>();
        if (obj != null) {
            if (obj instanceof String) {
                CfLog.i("obj is String.");
                json = (String) obj;
            } else {
                json = new Gson().toJson(obj);
            }
            map = new Gson().fromJson(json, new TypeToken<HashMap<String, String>>() {
            }.getType());
        }

        if (map.containsKey("message")) {
            ToastUtils.showShort(map.get("message"));
        } else {
            ToastUtils.showShort(getResources().getString(R.string.txt_vf_failed_retry));
        }
    }

}