package com.xtree.base.utils;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.xtree.base.net.HttpCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import me.xtree.mvvmhabit.http.ValidateResponse;
import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by KAKA on 2024/7/19.
 * Describe:
 */
final public class HttpGsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final TypeAdapter<T> adapter;

    HttpGsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        String bodyString = value.string();
        try {

            try (BufferedReader reader = new BufferedReader(new StringReader(bodyString))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    // 解析每行 JSON 数据
                    JSONObject jsonObject = new JSONObject(line);
                    // 判断是否包含 status 字段
                    if (jsonObject.has("status")) {
                        // 读取 status 字段的值
                        int status = jsonObject.getInt("status");

                        if (status == HttpCallBack.CodeRule.CODE_900001) {
                            ValidateResponse validateResponse = gson.fromJson(bodyString, ValidateResponse.class);
                            if (validateResponse != null && validateResponse.getData() != null) {
                                String ip = validateResponse.getData().get("ip");
                                String type = validateResponse.getData().get("type");

                                if (TextUtils.isEmpty(ip)) {
                                    ip = "";
                                }

                                AppUtil.goGlobeVerify(ip, type);
                            } else {
                                AppUtil.goGlobeVerify("", "");
                            }
                        }

                        reader.close();
                        break;
                    }
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            T result = adapter.fromJson(bodyString);
//            if (result instanceof BaseResponse) {
//                BaseResponse response = (BaseResponse) result;
//                response.setDataString(bodyString);
//            }
            return result;
        } finally {
            value.close();
        }
    }
}
