package com.xtree.base.net.fastest

import com.drake.net.NetConfig
import com.drake.net.okhttp.trustSSLCertificate
import com.drake.net.request.UrlRequest
import okhttp3.ConnectionPool
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

/**
 *Created by KAKA on 2024/7/7.
 *Describe: 测速配置项
 */

//测速API
const val FASTEST_API = "/?speedTest=1"
const val FASTEST_H5_API = "/#/activity"

//测速请求组
const val FASTEST_GOURP_NAME = "fstestaRequest"

const val FASTEST_GOURP_NAME_H5 = "fstestaRequestH5"

//测速接口配置
val FASTEST_BLOCK: (UrlRequest.() -> Unit) = {
    addHeader("App-RNID", "87jumkljo")
}

//测速URL
fun getFastestAPI(host: String, api: String = FASTEST_API): String {
    return "${host}${api}"
}

/**
 * 配置网络客户端
 */
fun initNet() {
    val okHttpClientBuilder = OkHttpClient.Builder()
    okHttpClientBuilder.connectionPool(ConnectionPool(60, 6, TimeUnit.MINUTES))
    val dispatcher = Dispatcher()
    dispatcher.maxRequests = 1000 // 设置同时执行的最大请求数

    dispatcher.maxRequestsPerHost = 1000 // 设置每个主机同时执行的最大请求数

    okHttpClientBuilder.dispatcher(dispatcher)
    okHttpClientBuilder.trustSSLCertificate()
//    okHttpClientBuilder.addInterceptor(Interceptor { chain ->
//        val originalRequest: Request = chain.request()
//        val requestWithHeaders: Request = originalRequest.newBuilder()
//            .header("App-RNID", "87jumkljo")
//            .build()
//        chain.proceed(requestWithHeaders)
//    })
    NetConfig.initialize("", null, okHttpClientBuilder)
}
