package com.xtree.mine.ui.viewmodel;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.NonNull;


import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.utils.CfLog;
import com.xtree.base.utils.MD5Util;
import com.xtree.base.utils.RSAEncrypt;
import com.xtree.base.utils.SPUtil;
import com.xtree.mine.data.MineRepository;
import com.xtree.mine.vo.LoginResultVo;

import java.util.HashMap;
import java.util.UUID;

import io.reactivex.disposables.Disposable;
import me.xtree.mvvmhabit.base.BaseViewModel;
import me.xtree.mvvmhabit.bus.event.SingleLiveData;
import me.xtree.mvvmhabit.http.HttpCallBack;
import me.xtree.mvvmhabit.utils.KLog;
import me.xtree.mvvmhabit.utils.RxUtils;
import me.xtree.mvvmhabit.utils.SPUtils;
import me.xtree.mvvmhabit.utils.ToastUtils;

/**
 * Created by goldze on 2018/6/21.
 */

public class MineViewModel extends BaseViewModel<MineRepository> {
    public SingleLiveData<String> itemClickEvent = new SingleLiveData<>();
    public MineViewModel(@NonNull Application application, MineRepository repository) {
        super(application, repository);
    }
}
