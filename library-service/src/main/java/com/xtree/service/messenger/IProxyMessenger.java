package com.xtree.service.messenger;

import android.os.Bundle;
import android.os.Messenger;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.xtree.service.message.MessageType;

public interface IProxyMessenger extends IMessenger {

    public void sendConnectMessage(@NonNull Messenger replayMessenger);

    public void sendMessage(@NonNull MessageType.Input inputType, @Nullable Bundle obj);
}
