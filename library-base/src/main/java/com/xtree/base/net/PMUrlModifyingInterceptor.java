package com.xtree.base.net;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.xtree.base.global.SPKeyGlobal;

import java.io.IOException;
import java.util.List;

import me.xtree.mvvmhabit.utils.SPUtils;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class PMUrlModifyingInterceptor implements Interceptor {
    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        boolean isAgent = SPUtils.getInstance().getBoolean(SPKeyGlobal.KEY_USE_AGENT);
        if(!isAgent){
            return chain.proceed(chain.request());
        }

        Request originalRequest = chain.request();
        HttpUrl originalUrl = originalRequest.url();
        List<String> paths = originalUrl.encodedPathSegments();
        HttpUrl.Builder builder = new HttpUrl.Builder();
        builder.addPathSegment("proxyobg");

        for (int i = 0; i < paths.size(); i ++){
            builder.addPathSegment(paths.get(i));
        }
        // 修改URL
        HttpUrl modifiedUrl = builder.scheme(originalUrl.scheme()).host(SPUtils.getInstance().getString(SPKeyGlobal.PM_API_SERVICE_URL)).build();

        // 创建新的请求
        Request newRequest = originalRequest.newBuilder()
                .url(modifiedUrl)
                .build();

        // 发送请求
        return chain.proceed(newRequest);
    }
}
