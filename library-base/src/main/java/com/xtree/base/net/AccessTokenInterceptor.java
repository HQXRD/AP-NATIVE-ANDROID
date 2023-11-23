package com.xtree.base.net;

import androidx.annotation.NonNull;

import com.xtree.base.utils.TagUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AccessTokenInterceptor implements Interceptor {

    private static AccessTokenInterceptor instance;

    public static AccessTokenInterceptor newInstance() {
        if (instance == null) {
            instance = new AccessTokenInterceptor();
        }
        return instance;
    }

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {

        Request request = chain.request();
        //HttpUrl url = request.url();
        //String path = url.url().toString();
        //String token = "token000";
        request = request.newBuilder()
                //.header("token", token)
                .header("Content-Type", "application/vnd.sc-api.v1.json")
                .header("App-RNID", "87jumkljo")
                .header("UUID", TagUtils.getDeviceId())
                .header("Source", "8")
                .header("Authorization", "bearer null")
                .header("Cookie", "_vid_t=gCZ/m5RL49oUfQZ1paYfmNwC2EIBJAOCgVUCYXN/D/+3W+ulGvUhRkBIxc0s5hoPFXZ2YNZj5TAUr5ZtHQTrGaSC2KKypDouPRBIgKM=;" +
                        " _sessionHandler=96c67956a93d8f6df3568d07b1cf4bbf95a217fb45dbbe1cc21f0a45a29416b5")
                .build();

        return chain.proceed(request);
    }
}
