package com.xtree.base.net;

import android.content.Context;

import com.xtree.base.utils.CfLog;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class ProxyCallFactory implements Call.Factory {

    private OkHttpClient client;
    private Context ctx;

    public ProxyCallFactory(OkHttpClient client, Context ctx) {
        this.ctx = ctx;
        this.client = client;
    }

    @Override
    public Call newCall(Request request) {
        CfLog.d("*********************");
        try {
            // 要设置token
            String token = "token000"; //SPUtil.get(ctx).getToken();
            RequestBuilder builder = new RequestBuilder(token);
            return client.newCall(builder.newRequest(request));
        } catch (IOException e) {
            //CfLog.e("不能CallFactory中转换请求");
            CfLog.e(e.toString());
        }
        return client.newCall(request);
    }
}
