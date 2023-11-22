package com.xtree.base.net;

import com.xtree.base.utils.CfLog;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.concurrent.TimeUnit;

import okhttp3.Connection;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

public class LoggerInterceptor implements Interceptor {
    private static final Charset UTF8 = Charset.forName("UTF-8");

    public LoggerInterceptor() {

    }

    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        boolean logBody = true;
        boolean logHeaders = true;
        RequestBody requestBody = request.body();
        boolean hasRequestBody = requestBody != null;
        Connection connection = chain.connection();
        Protocol protocol = connection != null ? Protocol.HTTP_1_0 : Protocol.HTTP_1_1;
        String requestStartMessage = "--> " + request.method() + ' ' + protocol + ' ' + request.url();
        if (!logHeaders && hasRequestBody) {
            requestStartMessage = requestStartMessage + " (" + requestBody.contentLength() + "-byte body)";
        }

        CfLog.d(requestStartMessage);
        if (logHeaders) {
            if (hasRequestBody) {
                if (requestBody.contentType() != null) {
                    CfLog.d("Content-Type: " + requestBody.contentType());
                }

                if (requestBody.contentLength() != -1L) {
                    CfLog.d("Content-Length: " + requestBody.contentLength());
                }
            }

            Headers startNs = request.headers();
            int buffer = 0;

            for (int response = startNs.size(); buffer < response; ++buffer) {
                String tookMs = startNs.name(buffer);
                if (!"Content-Type".equalsIgnoreCase(tookMs) && !"Content-Length".equalsIgnoreCase(tookMs)) {
                    CfLog.d(tookMs + ": " + startNs.value(buffer));
                }
            }

            if (logBody && hasRequestBody) {
                if (this.bodyEncoded(request.headers())) {
                    CfLog.d("--> END " + request.method() + " (encoded body omitted)");
                } else {
                    Buffer var28 = new Buffer();
                    requestBody.writeTo(var28);
                    Charset var29 = UTF8;
                    MediaType var31 = requestBody.contentType();
                    if (var31 != null) {
                        var29 = var31.charset(UTF8);
                    }

                    CfLog.d("");
                    CfLog.d(var28.readString(var29));
                    CfLog.d("--> END " + request.method() + " (" + requestBody.contentLength() + "-byte body)");
                }
            } else {
                CfLog.d("--> END " + request.method());
            }
        }

        long var27 = System.nanoTime();
        Response var30 = chain.proceed(request);
        long var32 = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - var27);
        ResponseBody responseBody = var30.body();
        long contentLength = responseBody.contentLength();
        String bodySize = contentLength != -1L ? contentLength + "-byte" : "unknown-length";
        CfLog.d("<-- " + var30.code() + ' ' + var30.message() + " (" + var32 + "ms" + (!logHeaders ? ", " + bodySize + " body" : "") + ')' + ' ' +
                var30.request().url());
        if (logHeaders) {
            Headers headers = var30.headers();
            int source = 0;

            for (int buffer1 = headers.size(); source < buffer1; ++source) {
                CfLog.d(headers.name(source) + ": " + headers.value(source));
            }

            //if(logBody && HttpEngine.hasBody(var30)) {
            if (logBody) {
                if (this.bodyEncoded(var30.headers())) {
                    CfLog.d("<-- END HTTP (encoded body omitted)");
                } else {
                    BufferedSource var33 = responseBody.source();
                    var33.request(9223372036854775807L);
                    Buffer var34 = var33.buffer();
                    Charset charset = UTF8;
                    MediaType contentType = responseBody.contentType();
                    if (contentType != null) {
                        try {
                            charset = contentType.charset(UTF8);
                        } catch (UnsupportedCharsetException var26) {
                            CfLog.d("");
                            CfLog.e("Couldn\'t decode the response body; charset is likely malformed.");
                            CfLog.d("<-- END HTTP");
                            return var30;
                        }
                    }

                    if (contentLength != 0L) {
                        CfLog.d("");
                        String link = var30.request().url().toString();
                        CfLog.d(link.substring(link.lastIndexOf("/")) + " " + var34.clone().readString(charset));
                    }

                    CfLog.d("<-- END HTTP (" + var34.size() + "-byte body)");
                }
            } else {
                CfLog.d("<-- END HTTP");
            }
        }

        return var30;

    }

    private boolean bodyEncoded(Headers headers) {
        String contentEncoding = headers.get("Content-Encoding");
        return contentEncoding != null && !contentEncoding.equalsIgnoreCase("identity");
    }
}
