package com.xtree.service;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class WebSocketManager {

    private static final WebSocketManager INSTANCE = new WebSocketManager(); // 静态单例实例
    private OkHttpClient okHttpClient;

    // 私有构造函数
    private WebSocketManager() {
        this.okHttpClient = SslUtils.getUnsafeOkHttpClient();//域名证书可能有问题，暂时用这个
    }

    // 提供获取单例实例的方法
    public static WebSocketManager getInstance() {
        return INSTANCE;
    }

    // synchronized 确保线程安全
    public synchronized WebSocket newWebSocket(String url, WebSocketListener listener) {
        // 创建请求并打开 WebSocket
        Request request = new Request.Builder().url(url).build();
        return okHttpClient.newWebSocket(request, listener);
    }
}
