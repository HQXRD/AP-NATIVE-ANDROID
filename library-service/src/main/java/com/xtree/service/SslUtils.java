package com.xtree.service;

import android.text.TextUtils;

import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;

@SuppressWarnings({"Convert2Lambda"})
public class SslUtils {

    private static void trustAllHttpsCertificates() throws Exception {
        TrustManager[] trustAllCerts = new TrustManager[1];
        TrustManager tm = new miTM();
        trustAllCerts[0] = tm;
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, null);
        javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    }

    /**
     * 忽略SSL证书校验
     *
     * @throws Exception e
     */
    public static void ignoreSsl() throws Exception {
        HostnameVerifier hv = new HostnameVerifier() {
            public boolean verify(String urlHostName, SSLSession session) {
                return true;
            }
        };
        trustAllHttpsCertificates();
        javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(hv);
    }

    /**
     * Created with IDEA
     * Author: www.itze.cn
     * Date: 2021-02-24
     * Email：gitlab@111.com
     * okhttp忽略所有SSL证书认证
     *
     * @return
     */
    public static OkHttpClient getUnsafeOkHttpClient() {
        try {
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public void checkServerTrusted(X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[]{};
                        }
                    }
            };
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            final javax.net.ssl.SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) (trustAllCerts[0]));
            builder.hostnameVerifier(new HostnameVerifier() {
                //这里存放不需要忽略SSL证书的域名，为空即忽略所有证书
                String[] ssls = {};

                @Override
                public boolean verify(String hostname, SSLSession session) {
                    if (TextUtils.isEmpty(hostname)) {
                        return false;
                    }
                    return !Arrays.asList(ssls).contains(hostname);
                }
            });

            OkHttpClient okHttpClient = builder.connectTimeout(60, TimeUnit.SECONDS).
                    writeTimeout(60, TimeUnit.SECONDS).readTimeout(60, TimeUnit.SECONDS).retryOnConnectionFailure(true).build();
            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static OkHttpClient getSafeOkHttpClient() {
        try {
            // 创建 SSLContext 实例，使用 TLSv1.2
            final SSLContext sslContext = SSLContext.getInstance("TLSv1.2");

            // 初始化 TrustManagerFactory，使用默认的 KeyStore
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init((KeyStore) null); // 使用默认 KeyStore

            // 获取 TrustManager
            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();

            // 初始化 SSLContext
            sslContext.init(null, trustManagers, new java.security.SecureRandom());

            // 创建 OkHttpClient
            OkHttpClient.Builder builder = new OkHttpClient.Builder()
                    .sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) trustManagers[0]) // 使用信任管理器
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true);

            return builder.build();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create a safe OkHttpClient", e);
        }
    }

    /**
     * 跳过证书效验的sslcontext
     *
     * @return
     * @throws Exception
     */
    private static SSLContext createIgnoreVerifySSL() throws Exception {
        SSLContext sc = SSLContext.getInstance("TLS");

        // 实现一个X509TrustManager接口，用于绕过验证，不用修改里面的方法
        X509TrustManager trustManager = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] paramArrayOfX509Certificate,
                                           String paramString) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] paramArrayOfX509Certificate,
                                           String paramString) throws CertificateException {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };
        sc.init(null, new TrustManager[]{trustManager}, null);
        return sc;
    }

    @SuppressWarnings({"unused", "RedundantThrows"})
    static class miTM implements TrustManager, X509TrustManager {
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public boolean isServerTrusted(X509Certificate[] certs) {
            return true;
        }

        public boolean isClientTrusted(X509Certificate[] certs) {
            return true;
        }


        public void checkServerTrusted(X509Certificate[] certs, String authType)
                throws CertificateException {
        }


        public void checkClientTrusted(X509Certificate[] certs, String authType)
                throws CertificateException {
        }
    }


}