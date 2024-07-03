package com.xtree.base.net;

import android.annotation.SuppressLint;
import android.os.Build;
import android.text.TextUtils;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;

import com.just.agentweb.WebViewClient;
import com.xtree.base.utils.CfLog;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.dnsoverhttps.DnsOverHttps;


/**
 * Created by KAKA on 2024/7/3.
 * Describe: DOH 自定义webclient
 */
public class DohWebViewClient extends WebViewClient {

    public static final String ARG_SEARCH_DNS_URL = "https://dns.alidns.com/dns-query";

    private OkHttpClient client;

    public DohWebViewClient() throws UnknownHostException {
        // 初始化 OkHttpClient 并配置自定义的 DNS 解析
        client = new OkHttpClient.Builder()
                .dns(new DnsOverHttps.Builder()
                        .client(new OkHttpClient())
                        .url(HttpUrl.get(ARG_SEARCH_DNS_URL))
                        .bootstrapDnsHosts(InetAddress.getByName("8.8.8.8"), InetAddress.getByName("114.114.114.114"))
                        .build())
                .build();
    }


    @SuppressLint("NewApi")
    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
        String scheme = request.getUrl().getScheme().trim();
        String method = request.getMethod();
        String url = request.getUrl().toString();
        CfLog.i("url:" + url);
        Request httpRequest = new Request.Builder().url(url).build();

        //拦截方案只能正常处理不带body的请求
        if ((scheme.equalsIgnoreCase("http") || scheme.equalsIgnoreCase("https")) && method.equalsIgnoreCase("get")) {
            try (Response response = client.newCall(httpRequest).execute()) {

                //对于POST请求的Body数据，WebResourceRequest接口中并没有提供，这里无法处理
                String contentType = response.body().contentType().toString();
                String mime = getMime(contentType);
                String charset = getCharset(contentType);
                int statusCode = response.code();
                String statusMessage = response.message();
                if (TextUtils.isEmpty(statusMessage)) {
                    statusMessage = getStatusMessage(response.code());
                }
                Headers headers = response.headers();
                Set<String> headerKeySet = headers.names();
                CfLog.i("code:" + response.code());
                CfLog.i("mime:" + mime + "; charset:" + charset);


                //无mime类型的请求不拦截
                if (TextUtils.isEmpty(mime)) {
                    CfLog.i("no MIME");
                    return super.shouldInterceptRequest(view, request);
                } else {
                    // 二进制资源无需编码信息
                    if (!isBinaryRes(mime) && TextUtils.isEmpty(charset)) {
                        charset = Charset.defaultCharset().displayName();
                    }
                    WebResourceResponse resourceResponse = new WebResourceResponse(mime, charset, response.body().byteStream());
                    resourceResponse.setStatusCodeAndReasonPhrase(statusCode, statusMessage);
                    Map<String, String> responseHeader = new HashMap<String, String>();
                    responseHeader.put(null, String.join(" ", "HTTP/1.1", String.valueOf(statusCode), statusMessage));
                    for (String key : headerKeySet) {
                        //可能包含key为null的报头，指向该http请求状态码
                        responseHeader.put(key, response.header(key));
                    }
                    resourceResponse.setResponseHeaders(responseHeader);
                    return resourceResponse;
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return super.shouldInterceptRequest(view, request);
    }

    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
        // API < 21 只能拦截URL参数
        return super.shouldInterceptRequest(view, url);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            view.loadUrl(request.getUrl().toString());
        } else {
            view.loadUrl(request.toString());
        }
        return true;
    }

    private String getStatusMessage(int statusCode) {
        switch (statusCode) {
            case 200:
                return "OK";
            case 404:
                return "Not Found";
            case 401:
                return "Unauthorized";
            default:
                return "Unknown Status Code: " + statusCode;
        }
    }

    /**
     * 从contentType中获取MIME类型
     *
     * @param contentType
     * @return
     */
    private String getMime(String contentType) {
        if (contentType == null) {
            return null;
        }
        return contentType.split(";")[0];
    }

    /**
     * 从contentType中获取编码信息
     *
     * @param contentType
     * @return
     */
    private String getCharset(String contentType) {
        if (contentType == null) {
            return null;
        }

        String[] fields = contentType.split(";");
        if (fields.length <= 1) {
            return null;
        }

        String charset = fields[1];
        if (!charset.contains("=")) {
            return null;
        }
        charset = charset.substring(charset.indexOf("=") + 1);
        return charset;
    }

    /**
     * 是否是二进制资源，二进制资源可以不需要编码信息
     *
     * @param mime
     * @return
     */
    private boolean isBinaryRes(String mime) {
        if (mime.startsWith("image") || mime.startsWith("audio") || mime.startsWith("video")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * header中是否含有cookie
     *
     * @param headers
     */
    private boolean containCookie(Map<String, String> headers) {
        for (Map.Entry<String, String> headerField : headers.entrySet()) {
            if (headerField.getKey().contains("Cookie")) {
                return true;
            }
        }
        return false;
    }

    private boolean needRedirect(int code) {
        return code >= 300 && code < 400;
    }

}
