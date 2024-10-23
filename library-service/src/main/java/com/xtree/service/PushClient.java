package com.xtree.service;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.xtree.base.utils.CfLog;
import com.xtree.service.message.MessageCenterThread;
import com.xtree.service.message.RemoteMessage;

import io.sentry.Sentry;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class PushClient implements IWebSocket {

    private MessageCenterThread messageCenter;

    @Override
    public void connectSocket(String url, long checkInterval) {
        if (messageCenter != null) {
            stopSocket();
        }
        messageCenter = new MessageCenterThread();
        CfLog.i(String.format("长链接开始 \t%s,\t%d", url, checkInterval));
        WebSocketManager.getInstance().newWebSocket(url, new WebSocketListener() {

            @Override
            public void onOpen(@NonNull WebSocket webSocket, @NonNull Response response) {
                messageCenter.startThread(webSocket, checkInterval);
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        messageCenter.sendHeart();
                    }
                },2000);

            }

            @Override
            public void onMessage(@NonNull WebSocket webSocket, @NonNull String text) {

                try {
                    Gson gson = new Gson();
                    RemoteMessage remoteMessage = gson.fromJson(text, RemoteMessage.class);
                    if (remoteMessage == null) {
                        CfLog.i("服务器返回空消息");
                        return;
                    }
                    CfLog.i("socket message:" + text);
                    switch (remoteMessage.getType()) {
                        case "open":
                            CfLog.i("长链接消息确认成功" + text);
                            break;
                        case "close"://服务端返回失败，主动断开
                            if (messageCenter != null) {
                                messageCenter.stopThread(false);
                                messageCenter = null;
                            }
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Sentry.captureException(e);
                }
            }

            @Override
            public void onClosed(@NonNull WebSocket webSocket, int code, @NonNull String reason) {
                messageCenter.stopThread(true);
                CfLog.i(String.format("服务器关闭%s", reason));
            }

            @Override
            public void onFailure(@NonNull WebSocket webSocket, @NonNull Throwable t, @Nullable Response response) {
                messageCenter.stopThread(false);
                CfLog.i(String.format("服务失败%s,%s", t,response));
            }
        });
    }


    @Override
    public void stopSocket() {
        if (messageCenter != null) {
            messageCenter.stopThread();
            messageCenter = null;
        }
    }

    @Override
    public void sendMessage(String message) {
        if (messageCenter != null) {
            messageCenter.sendMessage(message);
        }
    }

    @Override
    public boolean isRunning() {
        if (messageCenter != null) {
            return messageCenter.isRunning();
        }
        return false;
    }


}
