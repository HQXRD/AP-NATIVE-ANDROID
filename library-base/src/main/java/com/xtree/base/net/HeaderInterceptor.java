package com.xtree.base.net;

import android.text.TextUtils;

import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.utils.TagUtils;

import java.io.IOException;

import me.xtree.mvvmhabit.utils.SPUtils;
import me.xtree.mvvmhabit.utils.Utils;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by goldze on 2017/5/10.
 */
public class HeaderInterceptor implements Interceptor {
    public HeaderInterceptor() {

    }

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
        builder.addHeader("App-RNID", "87jumkljo"); //
        builder.addHeader("Source", "9");
        builder.addHeader("UUID", TagUtils.getDeviceId(Utils.getContext()));
        builder.addHeader("X-Crypto", "yes");
        //请求信息
        return chain.proceed(builder.build());
    }
}