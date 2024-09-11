package com.xtree.base.net.fastest

import android.util.Log
import okhttp3.Call
import okhttp3.Connection
import okhttp3.EventListener
import okhttp3.Protocol
import okio.IOException
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.Proxy
import java.util.concurrent.TimeUnit

/**
 *Created by KAKA on 2024/7/6.
 *Describe: 用于监听请求各阶段
 */
open class FastestEventListener: EventListener() {
    private var dnsStart: Long = 0 //dns开始解析时间
    private var connectStart: Long = 0  //开始连接时间
    private var callStart: Long = 0  //开始请求时间
    private var connectAquire: Long = 0  //连接获取时间

    override fun callStart(call: Call) {
        Log.v("Tags.CALL", "callStart")

        callStart = System.nanoTime()
    }

    override fun dnsStart(call: Call, domainName: String) {
        Log.v("Tags.CALL", "dnsStart")

        dnsStart = System.nanoTime()
    }

    override fun dnsEnd(
        call: Call,
        domainName: String,
        inetAddressList: List<@JvmSuppressWildcards InetAddress>
    ) {
        Log.v("Tags.CALL", "dnsEnd")

        var dnsEnd = System.nanoTime()
        var duration = TimeUnit.NANOSECONDS.toMillis(dnsEnd - dnsStart)
        Log.v("Tags.HTTP", "(域名解析时长): $duration ms")
    }


    override fun connectStart(call: Call, inetSocketAddress: InetSocketAddress, proxy: Proxy) {
        Log.v("Tags.CALL", "connectStart")

        connectStart = System.nanoTime()
    }

    override fun connectEnd(
        call: Call,
        inetSocketAddress: InetSocketAddress,
        proxy: Proxy,
        protocol: Protocol?
    ) {
        Log.v("Tags.CALL", "connectEnd")

        connectAquire = System.nanoTime()
        var duration = TimeUnit.NANOSECONDS.toMillis(connectAquire - connectStart)
        Log.v("Tags.HTTP", "(建连时长): $duration ms")
    }

    override fun connectionAcquired(call: Call, connection: Connection) {
        Log.v("Tags.CALL", "connectionAcquired")
    }

    override fun connectionReleased(call: Call, connection: Connection) {
        Log.v("Tags.CALL", "connectionReleased")

        var connectReleased = System.nanoTime()
        var duration = TimeUnit.NANOSECONDS.toMillis(connectReleased - connectAquire)
        Log.v("Tags.HTTP", "(连接保持时长): $duration ms")
    }

    override fun callEnd(call: Call) {
        Log.v("Tags.CALL", "callEnd")

        var callEnd = System.nanoTime()
        var duration = TimeUnit.NANOSECONDS.toMillis(callEnd - callStart)
        Log.v("Tags.total", "host: ${call.request().url.host} (请求总时长): $duration ms")
    }

    override fun callFailed(call: Call, ioe: IOException) {
        Log.v("Tags.CALL", "callFailed")

        var callEnd = System.nanoTime()
        var duration = TimeUnit.NANOSECONDS.toMillis(callEnd - callStart)
        Log.v("Tags.HTTP", "(请求总时长): $duration ms")
    }
}