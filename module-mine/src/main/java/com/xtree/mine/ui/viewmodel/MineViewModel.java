package com.xtree.mine.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.net.HttpCallBack;
import com.xtree.base.net.RetrofitClient;
import com.xtree.base.utils.CfLog;
import com.xtree.mine.data.MineRepository;
import com.xtree.mine.vo.ProfileVo;

import io.reactivex.disposables.Disposable;
import me.xtree.mvvmhabit.base.BaseViewModel;
import me.xtree.mvvmhabit.bus.event.SingleLiveData;
import me.xtree.mvvmhabit.utils.RxUtils;
import me.xtree.mvvmhabit.utils.SPUtils;
import me.xtree.mvvmhabit.utils.ToastUtils;

/**
 * Created by goldze on 2018/6/21.
 */
public class MineViewModel extends BaseViewModel<MineRepository> {
    public MutableLiveData<Boolean> liveDataLogout = new SingleLiveData<>();
    public MutableLiveData<ProfileVo> liveDataProfile = new MutableLiveData<>();

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

    public void getProfile() {
        Disposable disposable = (Disposable) model.getApiService().getProfile()
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<ProfileVo>() {
                    @Override
                    public void onResult(ProfileVo vo) {
                        CfLog.i(vo.toString());
                        SPUtils.getInstance().put(SPKeyGlobal.USER_AUTO_THRAD_STATUS, vo.auto_thrad_status);
                        SPUtils.getInstance().put(SPKeyGlobal.HOME_PROFILE, new Gson().toJson(vo));
                        liveDataProfile.setValue(vo);
                    }

                    @Override
                    public void onError(Throwable t) {
                        CfLog.e("error, " + t.toString());
                        super.onError(t);
                        ToastUtils.showLong("请求失败");
                    }
                });
        addSubscribe(disposable);
    }

    public void readCache() {
        CfLog.i("******");
        String json = SPUtils.getInstance().getString(SPKeyGlobal.HOME_PROFILE);
        ProfileVo vo = new Gson().fromJson(json, ProfileVo.class);
        if (vo != null) {
            liveDataProfile.setValue(vo);
        }
    }

}
