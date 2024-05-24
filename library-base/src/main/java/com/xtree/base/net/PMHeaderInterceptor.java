package com.xtree.base.net;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.xtree.base.global.SPKeyGlobal;

import java.io.IOException;

import me.xtree.mvvmhabit.utils.SPUtils;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class PMHeaderInterceptor implements Interceptor {

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request.Builder builder = chain.request()
                .newBuilder();

        String token = SPUtils.getInstance().getString(SPKeyGlobal.PM_TOKEN);
        builder.addHeader("Content-Type", "application/json; charset=utf-8");
        if (!TextUtils.isEmpty(token)) {
            builder.removeHeader("Authorization");
            builder.addHeader("Authorization", "bearer " + token);
        }
        builder.addHeader("App-RNID", "87jumkljo"); //
        builder.addHeader("lang", "zh"); //
        builder.addHeader("requestId", token);
        //请求信息
        return chain.proceed(builder.build());
    }
}
