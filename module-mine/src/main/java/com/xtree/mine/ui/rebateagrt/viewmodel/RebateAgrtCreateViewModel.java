package com.xtree.mine.ui.rebateagrt.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;

import com.xtree.mine.data.MineRepository;

import me.xtree.mvvmhabit.base.BaseViewModel;

/**
 * Created by KAKA on 2024/3/13.
 * Describe: 返水契约创建弹窗 viewmodel
 */
public class RebateAgrtCreateViewModel  extends BaseViewModel<MineRepository> {
    public RebateAgrtCreateViewModel(@NonNull Application application) {
        super(application);
    }

    public RebateAgrtCreateViewModel(@NonNull Application application, MineRepository model) {
        super(application, model);
    }
}
