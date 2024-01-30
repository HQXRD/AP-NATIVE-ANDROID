package com.xtree.mine.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.net.HttpCallBack;
import com.xtree.base.net.RetrofitClient;
import com.xtree.base.utils.CfLog;
import com.xtree.base.vo.ProfileVo;
import com.xtree.mine.data.MineRepository;
import com.xtree.mine.vo.BalanceVo;
import com.xtree.mine.vo.VipInfoVo;
import com.xtree.mine.vo.VipUpgradeInfoVo;

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
    public SingleLiveData<BalanceVo> liveDataBalance = new SingleLiveData<>(); // 中心钱包
    public SingleLiveData<Boolean> liveData1kRecycle = new SingleLiveData<>(); // 1键回收
    public SingleLiveData<VipUpgradeInfoVo> liveDataVipUpgrade = new SingleLiveData<>(); // Vip升级资讯
    public SingleLiveData<VipInfoVo> liveDataVipInfo = new SingleLiveData<>(); // Vip个人资讯

    public MineViewModel(@NonNull Application application, MineRepository repository) {
        super(application, repository);
    }

    public void doLogout() {

        SPUtils.getInstance().remove(SPKeyGlobal.USER_TOKEN);
        SPUtils.getInstance().remove(SPKeyGlobal.USER_SHARE_SESSID);
        SPUtils.getInstance().remove(SPKeyGlobal.HOME_PROFILE);
        SPUtils.getInstance().remove(SPKeyGlobal.HOME_VIP_INFO);
        SPUtils.getInstance().remove(SPKeyGlobal.HOME_NOTICE_LIST);
        SPUtils.getInstance().remove(SPKeyGlobal.USER_ID);
        SPUtils.getInstance().remove(SPKeyGlobal.USER_NAME);
        SPUtils.getInstance().remove(SPKeyGlobal.MSG_PERSON_INFO);
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
                        SPUtils.getInstance().put(SPKeyGlobal.USER_ID, vo.userid);
                        SPUtils.getInstance().put(SPKeyGlobal.USER_NAME, vo.username);
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

    public void getBalance() {
        Disposable disposable = (Disposable) model.getApiService().getBalance()
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<BalanceVo>() {
                    @Override
                    public void onResult(BalanceVo vo) {
                        CfLog.d(vo.toString());
                        SPUtils.getInstance().put(SPKeyGlobal.WLT_CENTRAL_BLC, vo.balance);
                        liveDataBalance.setValue(vo);
                    }

                    @Override
                    public void onError(Throwable t) {
                        CfLog.e("error, " + t.toString());
                        super.onError(t);
                    }
                });
        addSubscribe(disposable);
    }

    public void do1kAutoRecycle() {
        Disposable disposable = (Disposable) model.getApiService().do1kAutoRecycle()
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<Object>() {
                    @Override
                    public void onResult(Object vo) {
                        CfLog.d("******");
                        liveData1kRecycle.setValue(true);
                    }

                    @Override
                    public void onError(Throwable t) {
                        CfLog.e("error, " + t.toString());
                        liveData1kRecycle.setValue(false);
                        super.onError(t);
                    }
                });
        addSubscribe(disposable);
    }

    public void getVipUpgradeInfo() {
        Disposable disposable = (Disposable) model.getApiService().getVipUpgradeInfo()
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<VipUpgradeInfoVo>() {
                    @Override
                    public void onResult(VipUpgradeInfoVo vo) {
                        CfLog.d(vo.toString());
                        SPUtils.getInstance().put(SPKeyGlobal.VIP_INFO, new Gson().toJson(vo));
                        liveDataVipUpgrade.setValue(vo);
                        String vipInfoJson = SPUtils.getInstance().getString(SPKeyGlobal.HOME_VIP_INFO, "");
                        if (!vipInfoJson.isEmpty()) {
                            VipInfoVo vipInfoVo = new Gson().fromJson(vipInfoJson, VipInfoVo.class);
                            liveDataVipInfo.setValue(vipInfoVo);
                        }
                        String json = SPUtils.getInstance().getString(SPKeyGlobal.HOME_PROFILE);
                        if (!vipInfoJson.isEmpty()) {
                            ProfileVo mProfileVo = new Gson().fromJson(json, ProfileVo.class);
                            liveDataProfile.setValue(mProfileVo);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        CfLog.e("error, " + t.toString());
                        super.onError(t);
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

    public void readVipCache() {
        CfLog.i("******");
        String VipJson = SPUtils.getInstance().getString(SPKeyGlobal.VIP_INFO);
        VipUpgradeInfoVo vo = new Gson().fromJson(VipJson, VipUpgradeInfoVo.class);
        if (vo != null) {
            CfLog.d(vo.toString());
            SPUtils.getInstance().put(SPKeyGlobal.VIP_INFO, new Gson().toJson(vo));
            liveDataVipUpgrade.setValue(vo);
            String vipInfoJson = SPUtils.getInstance().getString(SPKeyGlobal.HOME_VIP_INFO, "");
            if (!vipInfoJson.isEmpty()) {
                VipInfoVo vipInfoVo = new Gson().fromJson(vipInfoJson, VipInfoVo.class);
                liveDataVipInfo.setValue(vipInfoVo);
            }
            String homeJson = SPUtils.getInstance().getString(SPKeyGlobal.HOME_PROFILE);
            if (!vipInfoJson.isEmpty()) {
                ProfileVo mProfileVo = new Gson().fromJson(homeJson, ProfileVo.class);
                liveDataProfile.setValue(mProfileVo);
            }
        }
    }
}
