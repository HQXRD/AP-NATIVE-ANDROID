package com.xtree.base.utils;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import java.io.IOException;

import me.xtree.mvvmhabit.http.BaseResponse;
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
        String string = value.string();
        try {
            T result = adapter.fromJson(string);
            if (result instanceof BaseResponse) {
                BaseResponse response = (BaseResponse) result;
                response.setDataString(string);
            }
            return result;
        } finally {
            value.close();
        }
    }
}
