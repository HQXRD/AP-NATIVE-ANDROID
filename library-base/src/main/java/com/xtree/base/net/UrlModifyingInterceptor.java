package com.xtree.base.net;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.xtree.base.utils.DomainUtil;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Set;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class UrlModifyingInterceptor implements Interceptor {
    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {

        Request originalRequest = chain.request();
        HttpUrl originalUrl = originalRequest.url();

        List<String> paths = originalUrl.encodedPathSegments();
        HttpUrl.Builder builder = new HttpUrl.Builder();

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
        String scheme = DomainUtil.getApiUrl().startsWith("http:") ? "http" : "https";
        URL url = new URL(DomainUtil.getApiUrl());
        int port = url.getPort() > 0 ? url.getPort() : TextUtils.equals(scheme, "http") ? 80 : 443;
        // 修改URL
        HttpUrl modifiedUrl = builder.scheme(scheme).host(url.getHost()).port(port).build();

        // 创建新的请求
        Request newRequest = originalRequest.newBuilder()
                .url(modifiedUrl)
                .build();

        // 发送请求
        return chain.proceed(newRequest);
    }
}
