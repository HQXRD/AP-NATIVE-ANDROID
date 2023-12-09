package com.xtree.mine.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.net.RetrofitClient;
import com.xtree.mine.data.MineRepository;

import me.xtree.mvvmhabit.base.BaseViewModel;
import me.xtree.mvvmhabit.bus.event.SingleLiveData;
import me.xtree.mvvmhabit.utils.SPUtils;

/**
 * Created by goldze on 2018/6/21.
 */

public class MineViewModel extends BaseViewModel<MineRepository> {
    public SingleLiveData<String> itemClickEvent = new SingleLiveData<>();

    public MutableLiveData<Boolean> liveDataLogout = new SingleLiveData<>();

    public MineViewModel(@NonNull Application application, MineRepository repository) {
        super(application, repository);
    }

    public void doLogout() {

        SPUtils.getInstance().put(SPKeyGlobal.USER_TOKEN, "");
        SPUtils.getInstance().put(SPKeyGlobal.USER_SHARE_SESSID, "");
        SPUtils.getInstance().put(SPKeyGlobal.HOME_PROFILE, "");
        SPUtils.getInstance().put(SPKeyGlobal.HOME_VIP_INFO, "");
        SPUtils.getInstance().put(SPKeyGlobal.HOME_NOTICE_LIST, "[]");
        RetrofitClient.init();
        liveDataLogout.setValue(true);
    }

}
