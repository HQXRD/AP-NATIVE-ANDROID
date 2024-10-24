package com.xtree.base.net.live;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.xtree.base.utils.MD5Util;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

/**
 * Created by KAKA on 2024/10/23.
 * Describe: x9直播拦截器
 */
public class LiveHeaderInterceptor implements Interceptor {

    //XT-LIVE 秘钥 ： RGx1YjzfdOW7pTM
    // 獲取特定客户端对应的key
    private static final String clientSpecialKey = "RGx1YjzfdOW7pTM";
    private final TypeReference<Map<String, String>> type = new TypeReference<Map<String, String>>() {
    };

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request.Builder builder = request.newBuilder();

        String time = String.valueOf((System.currentTimeMillis() / 1000));
        String version = formartVersion();
        String reqStr = formatParams(request);
        String uriPathRevKey = getUriPathRevKey(request);

        builder.addHeader("x-live-Token", X9LiveInfo.INSTANCE.getToken());
        builder.addHeader("x-live-Visitor", X9LiveInfo.INSTANCE.getVisitor());
        builder.addHeader("x-live-Oaid", X9LiveInfo.INSTANCE.getOaid());
        builder.addHeader("x-live-Channel", X9LiveInfo.INSTANCE.getChannel());
        builder.addHeader("x-live-Time", time);
        builder.addHeader("x-live-Version", version);

        // xLiveSign加密
        String signStr = reqStr + "key=" + clientSpecialKey + "/" + uriPathRevKey + "/"
                + time + "/" + X9LiveInfo.INSTANCE.getVisitor() + "/" + X9LiveInfo.INSTANCE.getOaid() + "/" + version;

        String xLiveSign = MD5Util.generateMd5(signStr);

        builder.addHeader("x-live-Sign", xLiveSign);

        return chain.proceed(builder.build());
    }

    private String formatParams(Request request) throws IOException {

        String reqStr = "";
        String method = request.method();

        if (method.equals("GET")) {
            // URL 中的查询参数
            String query = request.url().query();
            if (query != null) {
                reqStr = query;
            }
        }
        // 处理 POST 请求参数
        else if (method.equals("POST")) {
            RequestBody requestBody = request.body();
            if (requestBody instanceof FormBody) {
                FormBody formBody = (FormBody) requestBody;
                StringBuilder params = new StringBuilder();
                for (int i = 0; i < formBody.size(); i++) {
                    params.append(formBody.name(i)).append("=").append(formBody.value(i)).append("&");
                }
                reqStr = params.toString();
            } else if (requestBody != null) {
                Buffer buffer = new Buffer();
                requestBody.writeTo(buffer);
                String json = buffer.readUtf8();
                Map<String, String> params = JSON.parseObject(json, type);
                if (params == null || params.isEmpty()) {
                    return "";
                }

                // 獲取key并按字母由小到大排序
                List<String> sortKeys = new ArrayList<>(params.keySet());
                Collections.sort(sortKeys);

                // API所需GET或POST所有参数由小到大排序，并以key=value方式"&"符号连接
                StringBuilder reqStrBuilder = new StringBuilder();
                for (String key : sortKeys) {
                    reqStrBuilder.append(key).append("=").append(params.get(key)).append("&");
                }
                reqStr = reqStrBuilder.toString();
            }
        }
        if (!reqStr.isEmpty()) {
            return "channel_code=" + X9LiveInfo.INSTANCE.getChannel() + "&" + reqStr;
        }
        return reqStr;
    }

    private String getUriPathRevKey(Request request) {
        String[] segments = request.url().encodedPath().split("/");
        StringBuilder uriPathRevKey = new StringBuilder();
        for (String segment : segments) {
            if (!segment.isEmpty()) {
                uriPathRevKey.append(segment.charAt(segment.length() - 1));
            }
        }
        uriPathRevKey.reverse();
        return uriPathRevKey.toString();
    }

    private String formartVersion() {
        // 获取当前日期
        Date currentDate = new Date();
        // 定义日期格式
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        // 格式化日期
        return "android-" + dateFormat.format(currentDate);
    }
}
