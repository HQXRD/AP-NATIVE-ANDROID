package com.xtree.base.net;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.gson.Gson;
import com.xtree.base.utils.CfLog;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

public class RequestBuilder {
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");

    private String token;

    public RequestBuilder(String token) {
        CfLog.d("****************** token: " + token);
        this.token = token;
    }

    private Request doUploadFile(Request originalRequest, Map<String, String> files) {
        RequestBody formBody;
        MultipartBody.Builder bodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        Set<String> keys = files.keySet();
        Iterator it = keys.iterator();
        while (it.hasNext()) {
            String key = (String) it.next();
            File file = new File(files.get(key));
            if (file.exists()) {
                bodyBuilder.addFormDataPart(key, file.getName(), RequestBody.create(MEDIA_TYPE_PNG, file));
            }

        }
        formBody = bodyBuilder
                //                .addFormDataPart("param", str)
                //                .addFormDataPart("md5", md5)
                .build();
        Request request = new Request.Builder()
                .url(originalRequest.url())
                .post(formBody)
                .addHeader("token", token)
                .build();
        return request;
    }

    public Request newRequest(Request originalRequest) throws IOException {
        CfLog.d(getClass().getName());
        CfLog.d("contentType: " + originalRequest.body().contentType()); // multipart/form-data

        // originalRequest.body().contentType().toString().contains(MultipartBody.FORM.toString())  ;
        // if (originalRequest.url().toString().contains("/qrcode/upload"))

        if (originalRequest.body().contentType() != null
                && originalRequest.body().contentType().toString().contains(MultipartBody.FORM.toString())) {
            CfLog.i("************ upload");

            Request newRequest = originalRequest
                    .newBuilder()
                    .post(originalRequest.body())
                    .addHeader("token", token)
                    .build();
            return newRequest;
        }
        if (!"POST".equals(originalRequest.method())) {
            CfLog.e("非POST请求 " + originalRequest.url());
            //throw new IllegalArgumentException("请求方法必须是POST");
        }
        //开始修改请求
        RequestBody requestBody = originalRequest.body();
        FormBody formBody = null;
        if (null != requestBody && (requestBody instanceof FormBody) && (requestBody.contentLength() != 0)) {
            formBody = (FormBody) requestBody;
        }
        MediaType contentType = MediaType.parse("application/json");

        RequestBody newRequestBody = RequestBody.create(contentType, convert(formBody));
        //Request newRequest = originalRequest.newBuilder().post(newRequestBody).build();

        Request newRequest = originalRequest
                .newBuilder()
                .post(newRequestBody)
                .addHeader("token", token)
                .build();
        return newRequest;
    }

    /**
     * @return
     */
    private String convert(FormBody formBody) {
        CfLog.d("打印请求参数");
        Map<String, Object> map = new HashMap<>();
        if (null != formBody) {
            final int size = formBody.size();
            for (int index = 0; index < size; index++) {
                String name = formBody.name(index);
                String value = formBody.value(index);
                CfLog.d("%s ---> %s", name, value);
                map.put(name, value);
            }
        }

        return getRequestBody(map);
    }

    public String getRequestBody(Map<String, Object> data) {
        SerializerFeature[] serializerFeature = {
                SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteNullListAsEmpty,
                SerializerFeature.WriteNullStringAsEmpty,
                SerializerFeature.WriteNullNumberAsZero
        };

        AppTextMessageRequest messageRequest = new AppTextMessageRequest();

        if (TextUtils.isEmpty(token)) {
            token = "0000";
            CfLog.e("token为空");
        }

        Gson gson = new Gson();
        if (null != data && !data.isEmpty()) {
            String json = gson.toJson(data);
            CfLog.d("-------json: " + json);

            messageRequest.setData(json);
        }

        return JSON.toJSONString(data, serializerFeature);
    }
}
