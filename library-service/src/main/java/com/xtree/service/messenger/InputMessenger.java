package com.xtree.service.messenger;

import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;

import androidx.annotation.NonNull;

import com.xtree.service.message.MessageType;

import io.sentry.Sentry;

public abstract class InputMessenger implements IInputMessenger {

    private Messenger messenger;

    public InputMessenger() {
    }

    @Override
    public IBinder getBinder() {
        return new Messenger(new Handler(Looper.getMainLooper()) {

            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                MessageType.Input inputType = MessageType.Input.fromCode(msg.what);
                switch (inputType) {
                    case CONNECT:
                        messenger = msg.replyTo;
                        break;
                    default:
                        InputMessenger.this.handleMessage(msg);
                        break;
                }

            }
        }).getBinder();
    }

    private void _sendMessage(MessageType.Output outputType, Bundle obj) {
        try {
            Message msg = Message.obtain();
            msg.what = outputType.getCode(); // 将 enum 的 code 作为 what 值
            msg.obj = obj;
            messenger.send(msg);
        } catch (Exception e) {
            e.printStackTrace();
            Sentry.captureException(e);
        }
    }

    @Override
    public void sendMessage(MessageType.Output outputType, Bundle obj) {
        _sendMessage(outputType, obj);
    }

}
