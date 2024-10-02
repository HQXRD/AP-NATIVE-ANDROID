package com.xtree.live.ui.main.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;

import com.xtree.live.data.LiveRepository;

import me.xtree.mvvmhabit.base.BaseViewModel;

public class ChatViewModel extends BaseViewModel<LiveRepository> {

    public ChatViewModel(@NonNull Application application) {
        super(application);
    }

    public ChatViewModel(@NonNull Application application, LiveRepository model) {
        super(application, model);
    }


}
