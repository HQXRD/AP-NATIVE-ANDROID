package com.xtree.service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;

import androidx.annotation.Nullable;

import com.xtree.base.utils.CfLog;
import com.xtree.service.message.MessageType;
import com.xtree.service.messenger.IInputMessenger;
import com.xtree.service.messenger.InputMessenger;

public class WebSocketService extends Service {
    private PushClient pushClient;
    private IInputMessenger messenger;
    private long checkInterval = 30;
    private long retryNumber = 3;
    private long retryWaitingTime = 300;
    private long expireTime = 300;
    private long currentRetryNumber = 0;
    private boolean isLogin = false;

    private LooperHeartHandler mHandler = new LooperHeartHandler() {
        @Override
        public void handleMessage(Message msg) {

        }

        @Override
        public void heart() {
            if (pushClient != null && pushClient.isRunning() == false) {//需要启动重连接
                //释放资源
                pushClient.stopSocket();
                //往外发送请求url消息
                if (messenger != null) {
                    currentRetryNumber++;
                    if (currentRetryNumber > retryNumber && (currentRetryNumber - retryNumber) > (retryWaitingTime / checkInterval) &&
                            isLogin) {//出现重连异常
                        messenger.sendMessage(MessageType.Output.OBTAIN_LINK, null);
                    } else {
                        messenger.sendMessage(MessageType.Output.OBTAIN_LINK, null);
                    }
                }
            } else if (pushClient != null && pushClient.isRunning() == true) {
                currentRetryNumber = 0;
            }
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        pushClient = new PushClient();
        messenger = new InputMessenger() {

            @Override
            public void handleMessage(Message msg) {
                if (msg.obj != null && !(msg.obj instanceof Bundle)) {
                    CfLog.i("未知消息" + msg);
                    return;
                }
                MessageType.Input inputType = MessageType.Input.fromCode(msg.what);
                switch (inputType) {
                    case LINK:
                        if (msg.obj instanceof Bundle) {
                            Bundle params = (Bundle) msg.obj;
                            String url = params.getString("url");
                            checkInterval = params.getLong("checkInterval");
                            retryNumber = params.getLong("retryNumber");
                            retryWaitingTime = params.getLong("retryWaitingTime");
                            expireTime = params.getLong("expireTime");
                            pushClient.connectSocket(url, checkInterval * 1000);
                            mHandler.setHeartTime(checkInterval * 1000);
                            isLogin = true;
                        }
                        break;
                    case LOGOUT:
                        pushClient.stopSocket();
                        isLogin = false;
                        break;
                    default:
                        CfLog.i("未知消息" + msg);
                        break;
                }
            }
        };
        return messenger.getBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mHandler.start(30 * 1000);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mHandler != null) {
            mHandler.stop(); // 退出 Looper
        }

        if (pushClient != null) {
            pushClient.stopSocket();
        }
    }
}