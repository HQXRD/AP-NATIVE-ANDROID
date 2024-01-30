package com.xtree.main.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;

import com.xtree.base.BuildConfig;
import com.xtree.base.utils.CfLog;
import com.xtree.base.utils.DomainUtil;
import com.xtree.main.R;

import java.util.Arrays;
import java.util.List;

import me.xtree.mvvmhabit.utils.ToastUtils;

/**
 * 冷启动
 */
public class SplashActivity extends Activity {

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        setFasterDomain();

        new Handler().postDelayed(() -> inMain(), 1 * 2500);
    }

    private void init() {
        String url = getString(R.string.domain_url);
        if (url.startsWith("http://") || url.startsWith("https://")) {
            DomainUtil.setDomainUrl(url);
        }

        if (BuildConfig.DEBUG) {
            ToastUtils.showLong("Debug model");
        }
    }

    /**
     * 线路竞速
     */
    private void setFasterDomain() {
        String urls = getString(R.string.domain_url_list);
        List<String> list = Arrays.asList(urls.split(";"));
        for (String url : list) {
            CfLog.i("url: " + url);
            if (url.startsWith("http://") || url.startsWith("https://")) {

            }
        }
    }

    /**
     * 进入主页面
     */
    private void inMain() {

        /*if(TextUtils.isEmpty(SPUtils.getInstance().getString(SPKeyGlobal.USER_TOKEN))){
            ARouter.getInstance().build(RouterActivityPath.Mine.PAGER_LOGIN_REGISTER)
                    .withInt(Constant.LoginRegisterActivity_ENTER_TYPE,Constant.LoginRegisterActivity_LOGIN_TYPE)
                    .navigation();
            finish();
        }else{
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }*/

        startActivity(new Intent(this, MainActivity.class));
        finish();

    }
}
