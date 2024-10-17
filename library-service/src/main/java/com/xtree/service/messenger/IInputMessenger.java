package com.xtree.service.messenger;

import android.os.Bundle;
import android.os.Message;

import androidx.annotation.NonNull;

import com.xtree.service.message.MessageType;

import javax.annotation.Nullable;

public interface IInputMessenger extends IMessenger {

    public void sendMessage(@NonNull MessageType.Output outputType, @Nullable Bundle obj);

    public void handleMessage(Message msg);

}
