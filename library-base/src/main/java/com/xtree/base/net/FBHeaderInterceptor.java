package com.xtree.base.net;

import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.utils.CfLog;
import com.xtree.base.utils.TagUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import me.xtree.mvvmhabit.utils.SPUtils;
import me.xtree.mvvmhabit.utils.Utils;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class FBHeaderInterceptor implements Interceptor {

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request.Builder builder = chain.request()
                .newBuilder();
        String platform = SPUtils.getInstance().getString("KEY_PLATFORM");
        String token;
        if(TextUtils.equals(platform, "fbxc")) {
            token = SPUtils.getInstance().getString(SPKeyGlobal.FBXC_TOKEN);
        } else {
            token = SPUtils.getInstance().getString(SPKeyGlobal.FB_TOKEN);
        }
        CfLog.e("========token======"+  token);
        builder.addHeader("Content-Type", "application/json; charset=utf-8");
        if (!TextUtils.isEmpty(token)) {
            builder.addHeader("Authorization", token);
        }
        builder.addHeader("App-RNID", "87jumkljo"); //
        builder.addHeader("Source", "8");
        builder.addHeader("UUID", TagUtils.getDeviceId(Utils.getContext()));
        //请求信息
        return chain.proceed(builder.build());
    }
}
