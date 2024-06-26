package com.xtree.base.net;


import android.util.Base64;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.xtree.base.utils.DomainUtil;
import com.xtree.base.utils.TagUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.zip.DataFormatException;
import java.util.zip.GZIPInputStream;

import kotlin.text.Charsets;
import me.xtree.mvvmhabit.http.HijackedException;
import me.xtree.mvvmhabit.utils.Utils;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

public class ExceptionInterceptor extends DecompressInterceptor {

    private final static String[] KEY_WORD = new String[]{"诈骗", "公安", "公安局"};

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Response response;
        Request request = chain.request();
        try {
            response = chain.proceed(request);
        } catch (Exception e) {
            throw e;
        }
        ResponseBody responseBody = response.body();
        BufferedSource source = responseBody.source();
        source.request(Long.MAX_VALUE); // Buffer the entire body.
        Buffer buffer = source.buffer();

        Charset charset = Charsets.UTF_8;
        MediaType contentType = responseBody.contentType();
        if (contentType != null) {
            charset = contentType.charset(Charsets.UTF_8);
        }
        String result = buffer.clone().readString(charset);

        boolean isCrypto = response.isSuccessful() && response.body() != null && response.header("X-Crypto") != null &&
                response.header("X-Crypto").equalsIgnoreCase("yes");

        if(isCrypto){
            try {
                byte[] base64DecodedData = android.util.Base64.decode(buffer.clone().readByteArray(), android.util.Base64.DEFAULT);
                result = decompressZlibText(base64DecodedData);
            } catch (DataFormatException e) {
                throw new RuntimeException(e);
            }
        }

        if(isJSONType(result)){
            return response;
        } else {
            TagUtils.tagEvent(Utils.getContext(), "event_json_conversion_error", DomainUtil.getApiUrl());
            if(isHijacked(result)) {
                throw new HijackedException(request.url(), result);
            }else {
                return response;
            }
        }
    }

    /**
     * 接口是否被劫持
     * @param result
     * @return
     */
    private boolean isHijacked(String result){
        for (int i = 0; i < KEY_WORD.length; i ++){
            if(result.contains(KEY_WORD[i])){
                return true;
            }
        }
        return false;
    }

    private boolean isJSONType(String str){
        try {
            new Gson().fromJson(str, Object.class);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    private String decrypt(String result) {
        result = result.replaceAll("\n", "");
        result = result.replaceAll("\r", "");
        result = result.replaceAll(" ", "");

        byte[] bytes = new byte[0];
        try {
            bytes = decompress(Base64.decode(result, Base64.DEFAULT));
            return new String(bytes, Charsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 数据解压缩
     *
     * @param data
     * @return
     * @throws Exception
     */
    private byte[] decompress(byte[] data) throws Exception {
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        // 解压缩

        decompress(bais, baos);

        data = baos.toByteArray();

        baos.flush();
        baos.close();

        bais.close();

        return data;
    }

    /**
     * 数据解压缩
     *
     * @param is
     * @param os
     * @throws Exception
     */
    private void decompress(InputStream is, OutputStream os)
            throws Exception {

        GZIPInputStream gis = new GZIPInputStream(is);

        int count;
        byte data[] = new byte[1024];
        while ((count = gis.read(data, 0, 1024)) != -1) {
            os.write(data, 0, count);
        }

        gis.close();
    }
}
