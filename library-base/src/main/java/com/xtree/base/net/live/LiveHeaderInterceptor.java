package com.xtree.base.net.live;

import android.text.TextUtils;

import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.utils.AppUtil;

import java.io.IOException;

import me.xtree.mvvmhabit.base.BaseApplication;
import me.xtree.mvvmhabit.utils.SPUtils;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by KAKA on 2024/10/23.
 * Describe: x9直播拦截器
 */
public class LiveHeaderInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request()
                .newBuilder();
        String token = SPUtils.getInstance().getString(SPKeyGlobal.USER_TOKEN);
        if (!TextUtils.isEmpty(token)) {
            builder.addHeader("Authorization", "bearer " + SPUtils.getInstance().getString(SPKeyGlobal.USER_TOKEN));
            builder.addHeader("Cookie", "auth=" + SPUtils.getInstance().getString(SPKeyGlobal.USER_TOKEN) + ";" +
                    SPUtils.getInstance().getString(SPKeyGlobal.USER_SHARE_COOKIE_NAME) + "=" + SPUtils.getInstance().getString(SPKeyGlobal.USER_SHARE_SESSID) + ";");
        }

        builder.addHeader("Content-Type", "application/vnd.sc-api.v1.json");
        builder.addHeader("x-live-Token", X9LiveInfo.INSTANCE.getToken());
        builder.addHeader("x-live-Visitor", X9LiveInfo.INSTANCE.getVisitor());
        builder.addHeader("x-live-Oaid", X9LiveInfo.INSTANCE.getOaid());
        builder.addHeader("x-live-Channel", X9LiveInfo.INSTANCE.getChannel());

        builder.addHeader("x-live-Time", String.valueOf((System.currentTimeMillis() / 1000)));
        builder.addHeader("x-live-Version", "android-" + AppUtil.getAppVersion(BaseApplication.getInstance().getApplicationContext()));

        builder.addHeader("x-live-Sign", X9LiveInfo.INSTANCE.getSign());

        return chain.proceed(builder.build());
    }
}
