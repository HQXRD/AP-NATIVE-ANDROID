package com.xtree.service.message;

import android.os.Message;

import com.xtree.service.LooperHeartHandler;

import io.sentry.Sentry;
import me.xtree.mvvmhabit.utils.KLog;
import okhttp3.WebSocket;

public class MessageCenterThread {

    private WebSocket webSocket;

    public void startThread(WebSocket webSocket,long checkInterval) {
        this.webSocket = webSocket;
        handler.start(checkInterval);
    }

    /**
     * @param flag true 真实关闭 false失败关闭
     */
    public void stopThread(boolean flag) {
        if (handler != null) {
            Message msg = handler.obtainMessage();
            if (msg != null) {
                msg.what = MessageType.Socket.STOP.getCode(); // 发送停止消息
                msg.obj = flag;
                handler.sendMessage(msg);
            }
        }
    }

    /**
     * 主动关闭
     */
    public void stopThread() {
        Message msg = handler.obtainMessage();
        if (msg != null) {
            msg.what = MessageType.Socket.STOP.getCode(); // 发送停止消息
            handler.sendMessage(msg);
            handler.stop();
        }

    }

    /**
     * 发送心跳消息
     */
    private void sendHeart() {
        Message msg = handler.obtainMessage();
        msg.what = MessageType.Socket.HEART.getCode(); // 发送心跳消息
        msg.obj = MessageCrater.createHeart();
        handler.sendMessage(msg);

    }

    public void sendMessage(String message) {
        Message msg = handler.obtainMessage();
        msg.what = MessageType.Socket.MESSAGE.getCode(); // 发送实体消息，一般都是json字符串，需要使用MessageCrater统一创建
        msg.obj = message;
        handler.sendMessage(msg);

    }

    /**
     * 消息中心是否正在运行
     *
     * @return
     */
    public boolean isRunning() {
        return handler.isRunning();
    }

    private final LooperHeartHandler handler = new LooperHeartHandler() {
        @Override
        public void handleMessage(Message msg) {
            try {
                MessageType.Socket socketType = MessageType.Socket.fromCode(msg.what);
                switch (socketType) {
                    case HEART:
                    case MESSAGE:
                        try {
                            if (webSocket != null) {
                                webSocket.send((String) msg.obj);
                            }
                        } catch (Exception e) {//消息转换失败
                            e.printStackTrace();
                            KLog.e(e.getMessage());
                            Sentry.captureException(e);
                        }
                        break;
                    case STOP: // 停止消息
                        if (!(msg.obj instanceof Boolean)) {
                            if (webSocket != null) {
                                int code = 1000; // 1000 表示正常关闭
                                String reason = "Closing the connection by client";
                                try {
                                    webSocket.close(code, reason); // 优雅关闭 WebSocket
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    KLog.e(e.getMessage());
                                    Sentry.captureException(e);
                                }
                            }
                        }
                        break;
                }
            } catch (Exception e) {
               e.printStackTrace();
               Sentry.captureException(e);
            }
        }

        @Override
        public void heart() {
            sendHeart();
        }
    };

}
