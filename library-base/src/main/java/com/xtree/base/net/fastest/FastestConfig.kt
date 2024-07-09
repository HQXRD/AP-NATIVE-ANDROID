package com.xtree.base.net.fastest

import com.drake.net.okhttp.trustSSLCertificate
import com.drake.net.request.UrlRequest

/**
 *Created by KAKA on 2024/7/7.
 *Describe: 测速配置项
 */

//测速API
const val FASTEST_API = "/?speedTest=1"
//测速请求组
const val FASTEST_GOURP_NAME = "fstestaRequest"

//测速接口配置
val FASTEST_BLOCK: (UrlRequest.() -> Unit) = {
    addHeader("App-RNID", "87jumkljo")
    setClient {
        trustSSLCertificate()
    }
}

//测速URL
fun getFastestAPI(host: String, api: String = FASTEST_API): String {
    return "${host}${api}"
}
