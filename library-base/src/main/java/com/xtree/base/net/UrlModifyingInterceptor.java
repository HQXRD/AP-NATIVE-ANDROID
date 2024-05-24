package com.xtree.base.net;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.xtree.base.global.SPKeyGlobal;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import me.xtree.mvvmhabit.utils.SPUtils;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class UrlModifyingInterceptor implements Interceptor {
    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        String platform = SPUtils.getInstance().getString("KEY_PLATFORM");
        boolean isAgent = SPUtils.getInstance().getBoolean(SPKeyGlobal.KEY_USE_AGENT + platform);
        if (!isAgent) {
            return chain.proceed(chain.request());
        }

        Request originalRequest = chain.request();
        HttpUrl originalUrl = originalRequest.url();

        List<String> paths = originalUrl.encodedPathSegments();
        HttpUrl.Builder builder = new HttpUrl.Builder();
        if (TextUtils.equals("obg", platform)) {
            builder.addPathSegment("proxyobg");
        } else {
            builder.addPathSegment("proxyfb");
        }

        for (int i = 0; i < paths.size(); i++) {
            builder.addPathSegment(paths.get(i));
        }

        Set<String> queryParameterNames = originalUrl.queryParameterNames();

        for (String queryParameterName : queryParameterNames) {
            List<String> values = originalUrl.queryParameterValues(queryParameterName);
            for (String value : values) {
                builder.addQueryParameter(queryParameterName, value);
            }
        }

        // 修改URL
        HttpUrl modifiedUrl = builder.scheme(originalUrl.scheme()).host(originalUrl.host()).build();

        // 创建新的请求
        Request newRequest = originalRequest.newBuilder()
                .url(modifiedUrl)
                .build();

        // 发送请求
        return chain.proceed(newRequest);
    }
}
