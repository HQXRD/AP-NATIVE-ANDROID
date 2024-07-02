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
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

/**
 * Created by KAKA on 2024/6/28.
 * Describe: WebViewClient 自定义DNS解析
 */
public class DnsWebViewClient extends WebViewClient {

    @SuppressLint("NewApi")
    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
        String scheme = request.getUrl().getScheme().trim();
        String method = request.getMethod();
        Map<String, String> headerFields = request.getRequestHeaders();
        String url = request.getUrl().toString();
        CfLog.i("url:" + url);
        //拦截方案只能正常处理不带body的请求
        if ((scheme.equalsIgnoreCase("http") || scheme.equalsIgnoreCase("https")) && method.equalsIgnoreCase("get")) {
            try {
                URLConnection connection = recursiveRequest(url, headerFields, null);

                if (connection == null) {
                    CfLog.i("connection null");
                    return super.shouldInterceptRequest(view, request);
                }

                //对于POST请求的Body数据，WebResourceRequest接口中并没有提供，这里无法处理
                String contentType = connection.getContentType();
                String mime = getMime(contentType);
                String charset = getCharset(contentType);
                HttpURLConnection httpURLConnection = (HttpURLConnection) connection;
                int statusCode = httpURLConnection.getResponseCode();
                String response = httpURLConnection.getResponseMessage();
                Map<String, List<String>> headers = httpURLConnection.getHeaderFields();
                Set<String> headerKeySet = headers.keySet();
                CfLog.i("code:" + httpURLConnection.getResponseCode());
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
                    WebResourceResponse resourceResponse = new WebResourceResponse(mime, charset, httpURLConnection.getInputStream());
                    resourceResponse.setStatusCodeAndReasonPhrase(statusCode, response);
                    Map<String, String> responseHeader = new HashMap<String, String>();
                    for (String key : headerKeySet) {
                        //HttpUrlConnection可能包含key为null的报头，指向该http请求状态码
                        responseHeader.put(key, httpURLConnection.getHeaderField(key));
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

    public URLConnection recursiveRequest(String path, Map<String, String> headers, String reffer) {
        HttpURLConnection conn;
        URL url = null;
        try {
            url = new URL(path);
            conn = (HttpURLConnection) url.openConnection();
//            String ip = dnsResolver.getIPV4ByHost(url.getHost());
            String ip = null;
            InetAddress address = DnsSocket.getInstance().resolveDNS(url.getHost());
            ip = address.getHostAddress();
            if (ip != null) {
                //通过PDNS获取IP成功，进行URL替换和HOST头设置
                CfLog.i("get IP: " + ip + " for host: " + url.getHost() + "from pdns resolver success!");
                String newUrl = path.replaceFirst(url.getHost(), ip);
                conn = (HttpURLConnection) new URL(newUrl).openConnection();

                if (headers != null) {
                    for (Map.Entry<String, String> field : headers.entrySet()) {
                        conn.setRequestProperty(field.getKey(), field.getValue());
                    }
                }
                // 设置HTTP请求头Host域
                conn.setRequestProperty("Host", url.getHost());
            } else {
                return null;
            }
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30000);
            conn.setInstanceFollowRedirects(false);
            if (conn instanceof HttpsURLConnection) {
                final HttpsURLConnection httpsURLConnection = (HttpsURLConnection) conn;
//                TlsSniSocketFactory sslSocketFactory = new TlsSniSocketFactory((HttpsURLConnection) conn);
//
//                // sni场景，创建SSLScocket
//                httpsURLConnection.setSSLSocketFactory(sslSocketFactory);
                // https场景，证书校验
                httpsURLConnection.setHostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        String host = httpsURLConnection.getRequestProperty("Host");
                        if (null == host) {
                            host = httpsURLConnection.getURL().getHost();
                        }
                        return HttpsURLConnection.getDefaultHostnameVerifier().verify(host, session);
                    }
                });
            }
            int code = conn.getResponseCode();// Network block
            if (needRedirect(code)) {
                // 原有报头中含有cookie，放弃拦截
                if (containCookie(headers)) {
                    return null;
                }

                String location = conn.getHeaderField("Location");
                if (location == null) {
                    location = conn.getHeaderField("location");
                }

                if (location != null) {
                    if (!(location.startsWith("http://") || location.startsWith("https://"))) {
                        //某些时候会省略host，只返回后面的path，所以需要补全url
                        URL originalUrl = new URL(path);
                        location = originalUrl.getProtocol() + "://" + originalUrl.getHost() + location;
                    }
                    CfLog.i("code:" + code + "; location:" + location + "; path" + path);
                    return recursiveRequest(location, headers, path);
                } else {
                    //无法获取location信息，让浏览器获取
                    return null;
                }
            } else {
                //redirect finish.
                CfLog.i("redirect finish");
                return conn;
            }
        } catch (MalformedURLException e) {
            CfLog.w("recursiveRequest MalformedURLException");
        } catch (IOException e) {
            CfLog.w("recursiveRequest IOException");
        } catch (Exception e) {
            CfLog.w("unknow exception");
        }
        return null;
    }

    private boolean needRedirect(int code) {
        return code >= 300 && code < 400;
    }

}
