package com.xtree.base.net;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AccessTokenInterceptor implements Interceptor {

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {

        Request request = chain.request();
        HttpUrl url = request.url();
        //String path = url.url().toString();

        String token = "token000";
        request = request.newBuilder().header("token", token).build();

        return chain.proceed(request);
    }
}
