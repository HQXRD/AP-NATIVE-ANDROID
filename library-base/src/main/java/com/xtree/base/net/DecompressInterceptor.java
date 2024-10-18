package com.xtree.base.net;

import com.xtree.base.utils.CfLog;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 * Created by KAKA on 2024/6/15.
 * Describe: body 解密拦截器
 */
public class DecompressInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);

        // 检查响应是否成功且是否包含需要解压缩的内容
        if (response.isSuccessful() && response.body() != null && response.header("X-Crypto") != null && response.header("X-Crypto").equalsIgnoreCase("yes")) {
            ResponseBody decompressedBody = decompressResponseBody(response.body());
            return response.newBuilder().body(decompressedBody).build();
        }

        return response;
    }

    public ResponseBody decompressResponseBody(ResponseBody body) throws IOException {
        BufferedSource source = body.source();
        Buffer buffer = new Buffer();
        buffer.writeAll(source);

        byte[] compressedData = buffer.readByteArray();
        byte[] decompressedData = new byte[0];
        try {
            // 先进行 Base64 解码
            byte[] base64DecodedData = android.util.Base64.decode(compressedData, android.util.Base64.DEFAULT);
            decompressedData = decompressZlibText(base64DecodedData).getBytes();
        } catch (DataFormatException e) {
            throw new RuntimeException(e);
        }
        // 创建解压缩后的 ResponseBody
        MediaType contentType = body.contentType();
        return ResponseBody.create(contentType, decompressedData);
    }

    public String decompressZlibText(byte[] zlibData) throws IOException, DataFormatException {
        Inflater inflater = new Inflater();
        inflater.setInput(zlibData);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(zlibData.length);
        byte[] buffer = new byte[1024];

        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }
        } catch (DataFormatException e) {
            throw new DataFormatException("Invalid zlib data format");
        } finally {
            inflater.end();
            outputStream.close();
        }

        String str = outputStream.toString("UTF-8");
        CfLog.i(str);
        return str;
    }
}