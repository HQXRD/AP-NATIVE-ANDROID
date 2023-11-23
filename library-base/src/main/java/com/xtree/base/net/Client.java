package com.xtree.base.net;

import com.xtree.base.utils.CfLog;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class Client {

    public static final int DEFAULT_TIMEOUT_SECONDS = 6;
    public static final int DEFAULT_READ_TIMEOUT_SECONDS = 60;
    public static final int DEFAULT_WRITE_TIMEOUT_SECONDS = 30;

    private static Retrofit mRetrofit;
    private static OkHttpClient client;

    private static String domainUrl = null; //
    private static String DOMAIN_URL_DEF = "https://www.weres.bar";//BuildConfig.SERVER_URL;

    /**
     * 基本域名（+path）
     *
     * @return
     */
    public static String baseUrl() {
        /*if (TextUtils.isEmpty(domainUrl))
        {
            //domainUrl = Settings.get().get("domain_url_def", DOMAIN_URL_DEF);
            // 缓存的可能是0个长度的空值''
            if ("".equals(domainUrl))
            {
                domainUrl = DOMAIN_URL_DEF;
            }
        }*/
        domainUrl = DOMAIN_URL_DEF;
        CfLog.i("get domainUrl: " + domainUrl);

        return domainUrl;
    }

    /**
     * 获取 OkHttpClient
     *
     * @return
     */
    public static OkHttpClient getClient() {

        if (null == client) {
            client = new OkHttpClient.Builder()
                    .connectTimeout(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                    .readTimeout(DEFAULT_READ_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                    .writeTimeout(DEFAULT_WRITE_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                    .addInterceptor(AccessTokenInterceptor.newInstance()) //token放在前面
                    .addInterceptor(new HttpLoggingInterceptor(message -> CfLog.d(message)).setLevel(HttpLoggingInterceptor.Level.BODY))
                    .sslSocketFactory(new SSLSocketFactoryCompat(SSLSocketFactoryCompat.trustAllCert), SSLSocketFactoryCompat.trustAllCert) //解决4.x版本ssl异常
                    .build();
        }
        return client;
    }

    public static Retrofit getRetrofit() {
        if (null == mRetrofit) {

            mRetrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl())
                    .client(getClient()) // 加这个
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }

        return mRetrofit;
    }

    /**
     * 获取服务对象   Rxjava+Retrofit建立在接口对象的基础上的
     * 泛型避免强制转换
     */
    public static <T> T getService(Class<T> clazz) {
        return getRetrofit().create(clazz);
    }

}
