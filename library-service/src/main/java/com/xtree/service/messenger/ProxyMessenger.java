package com.xtree.service.messenger;

import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import com.xtree.service.message.MessageType;

import io.sentry.Sentry;

public class ProxyMessenger implements IProxyMessenger {

    private Messenger messenger;

    public ProxyMessenger(IBinder binder) {
        this.messenger = new Messenger(binder);
    }

    @Override
    public IBinder getBinder() {
        return messenger.getBinder();
    }


    @Override
    public void sendConnectMessage(Messenger replayMessenger) {
        try {
            Message message = Message.obtain();
            message.what = MessageType.Input.CONNECT.getCode();
            message.replyTo = replayMessenger;
            messenger.send(message);
        } catch (RemoteException e) {
            e.printStackTrace();
            Sentry.captureException(e);
        }
    }

    @Override
    public void sendMessage(MessageType.Input inputType, Bundle obj) {
        try {
            // 发送一个 HEART 类型的消息
            Message msg = Message.obtain();
            msg.what = inputType.getCode(); // 将 enum 的 code 作为 what 值
            msg.obj = obj;
            messenger.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
            Sentry.captureException(e);
        }
    }
}
