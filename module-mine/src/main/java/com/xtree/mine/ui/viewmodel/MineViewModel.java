package com.xtree.mine.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;


import com.xtree.mine.data.MineRepository;

import me.xtree.mvvmhabit.base.BaseViewModel;
import me.xtree.mvvmhabit.bus.event.SingleLiveEvent;

/**
 * Created by goldze on 2018/6/21.
 */

public class MineViewModel extends BaseViewModel<MineRepository> {
    public SingleLiveEvent<String> itemClickEvent = new SingleLiveEvent<>();
    public MineViewModel(@NonNull Application application, MineRepository repository) {
        super(application, repository);
    }

}
