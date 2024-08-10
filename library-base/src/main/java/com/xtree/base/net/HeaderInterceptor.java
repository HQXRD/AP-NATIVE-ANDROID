package com.xtree.base.net;

import android.text.TextUtils;

import com.xtree.base.BuildConfig;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.utils.HmacSHA256Utils;
import com.xtree.base.utils.TagUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import me.xtree.mvvmhabit.utils.SPUtils;
import me.xtree.mvvmhabit.utils.Utils;
import okhttp3.HttpUrl;
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
        HttpUrl fullUrl = chain.request().url();
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
        builder.addHeader("X-Crypto", BuildConfig.DEBUG ? "no" : "yes");

        long sign1Ts = System.currentTimeMillis() / 1000;
        String query = fullUrl.encodedQuery();
        String path = fullUrl.encodedPath();
        StringBuilder encodeData = new StringBuilder();
        encodeData.append(sign1Ts).append("\n").append(path);
        if (!TextUtils.isEmpty(query)) {
            encodeData.append("?").append(query);
        }

        //对签名数据反编码
        String decode = encodeData.toString();
        try {
            decode = URLDecoder.decode(encodeData.toString(), "UTF-8");
        }catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String sign1 = HmacSHA256Utils.calculateHmacSHA256(HmacSHA256Utils.HMAC_KEY, decode);

        //加密签名
        builder.addHeader("X-Sign1", sign1);
        builder.addHeader("X-Sign1-Ts", String.valueOf(sign1Ts));

        //请求信息
        return chain.proceed(builder.build());
    }


}