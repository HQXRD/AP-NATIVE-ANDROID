package com.xtree.service.message;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Messenger;

import com.xtree.base.utils.CfLog;
import com.xtree.service.messenger.IProxyMessenger;
import com.xtree.service.messenger.ProxyMessenger;

public class PushServiceConnection implements ServiceConnection {
    private IProxyMessenger proxyMessenger;
    private boolean isBound = false;    // Track if Service is bound

    private Messenger replyMessenger;

    public PushServiceConnection(Messenger replyMessenger) {
        this.replyMessenger = replyMessenger;
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        CfLog.i("连接成功");
        proxyMessenger = new ProxyMessenger(iBinder);
        isBound = true;
        proxyMessenger.sendConnectMessage(replyMessenger);
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        proxyMessenger = null;
        isBound = false;

    }

    private void _sendMessageToService(MessageType.Input inputType, Bundle obj) {
        if (proxyMessenger != null) {
            proxyMessenger.sendMessage(inputType, obj);
        }
    }

    public void sendMessageToService(MessageType.Input inputType, Bundle obj) {
        _sendMessageToService(inputType, obj);
    }

    public boolean isBound() {
        return isBound;
    }
}
