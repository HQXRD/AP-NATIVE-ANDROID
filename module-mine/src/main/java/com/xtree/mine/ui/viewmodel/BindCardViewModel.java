package com.xtree.mine.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.net.HttpCallBack;
import com.xtree.base.utils.CfLog;
import com.xtree.base.vo.ProfileVo;
import com.xtree.mine.data.MineRepository;
import com.xtree.mine.vo.AWVo;
import com.xtree.mine.vo.BankCardVo;
import com.xtree.mine.vo.UserBankConfirmVo;
import com.xtree.mine.vo.UserBankProvinceVo;
import com.xtree.mine.vo.UserBindBaseVo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.disposables.Disposable;
import me.xtree.mvvmhabit.base.BaseViewModel;
import me.xtree.mvvmhabit.bus.event.SingleLiveData;
import me.xtree.mvvmhabit.utils.RxUtils;
import me.xtree.mvvmhabit.utils.SPUtils;
import me.xtree.mvvmhabit.utils.ToastUtils;

public class BindCardViewModel extends BaseViewModel<MineRepository> {

    public SingleLiveData<UserBindBaseVo<BankCardVo>> liveDataCardList = new SingleLiveData<>();
    public SingleLiveData<UserBindBaseVo<AWVo>> liveDataAWList = new SingleLiveData<>();
    public SingleLiveData<UserBankProvinceVo> liveDataBankProvinceList = new SingleLiveData<>();
    public SingleLiveData<List<UserBankProvinceVo.AreaVo>> liveDataCityList = new SingleLiveData<>();
    public SingleLiveData<UserBankConfirmVo> liveDataBindCardCheck = new SingleLiveData<>(); // 绑定银行卡确认
    public SingleLiveData<UserBankConfirmVo> liveDataBindCardResult = new SingleLiveData<>(); // 绑定银行卡结果
    public SingleLiveData<UserBankConfirmVo> liveDataDelCardCheck = new SingleLiveData<>(); // 锁定银行卡-检查
    public SingleLiveData<UserBankConfirmVo> liveDataDelCardResult = new SingleLiveData<>(); // 锁定银行卡结果
    public MutableLiveData<ProfileVo> liveDataProfile = new MutableLiveData<>(); // 个人信息

    public BindCardViewModel(@NonNull Application application, MineRepository model) {
        super(application, model);
    }

    public void getBankCardList(HashMap map) {
        Disposable disposable = (Disposable) model.getApiService().getBankCardList(map)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<UserBindBaseVo<BankCardVo>>() {
                    @Override
                    public void onResult(UserBindBaseVo<BankCardVo> vo) {
                        CfLog.d("******");
                        if (vo.msg_type == 1) {
                            ToastUtils.showLong(vo.message); // 页面超时！请重试。
                        } else {
                            liveDataCardList.setValue(vo);
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

    public void getAWList(HashMap map) {
        Disposable disposable = (Disposable) model.getApiService().getAWList(map)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<UserBindBaseVo<AWVo>>() {
                    @Override
                    public void onResult(UserBindBaseVo<AWVo> vo) {
                        CfLog.d("******");
                        if (vo.msg_type == 1) {
                            ToastUtils.showLong(vo.message); // 页面超时！请重试。
                        } else {
                            liveDataAWList.setValue(vo);
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

    public void getBankProvinceList(HashMap map) {
        Disposable disposable = (Disposable) model.getApiService().getBankProvinceList(map)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<UserBankProvinceVo>() {
                    @Override
                    public void onResult(UserBankProvinceVo vo) {
                        CfLog.d("******");
                        if (vo.msg_type == 1) {
                            ToastUtils.showLong(vo.message); // 您的银行卡已经锁定，无法绑定新银行卡！
                            finish();
                        } else {
                            liveDataBankProvinceList.setValue(vo);
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

    public void getCityList(HashMap map) {
        Disposable disposable = (Disposable) model.getApiService().getCityList(map)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<HashMap<String, List<UserBankProvinceVo.AreaVo>>>() {
                    @Override
                    public void onResult(HashMap<String, List<UserBankProvinceVo.AreaVo>> map) {
                        CfLog.d("******");
                        if (map.containsKey("city")) {
                            List<UserBankProvinceVo.AreaVo> list = map.get("city");
                            liveDataCityList.setValue(list);
                        } else {
                            liveDataCityList.setValue(new ArrayList<>());
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

    /**
     * 绑卡/支付宝/微信, 点下一步展示确认信息页
     *
     * @param queryMap
     * @param map
     */
    public void doBindCardByCheck(HashMap queryMap, HashMap map) {

        Disposable disposable = (Disposable) model.getApiService().doBindBankCard(queryMap, map)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<UserBankConfirmVo>() {
                    @Override
                    public void onResult(UserBankConfirmVo vo) {
                        CfLog.d("******");
                        if (vo.msg_type == 1) {
                            ToastUtils.showLong(vo.message); // 异常
                        } else {
                            liveDataBindCardCheck.setValue(vo);
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

    public void doBindCardBySubmit(HashMap queryMap, HashMap map) {

        Disposable disposable = (Disposable) model.getApiService().doBindBankCard(queryMap, map)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<UserBankConfirmVo>() {
                    @Override
                    public void onResult(UserBankConfirmVo vo) {
                        CfLog.d("******");
                        if (vo.msg_type == 3) {
                            ToastUtils.showLong(vo.message); // "绑定成功！温馨提示：新绑定卡需0小时后才能提现"
                            liveDataBindCardResult.setValue(vo);
                        } else if (vo.msg_type == 1) {
                            ToastUtils.showLong(vo.message); // "您提交的银行卡号格式不正确，请核对后重新提交！"
                            //liveDataBindCardResult.setValue(vo);
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

    public void delCardByCheck(HashMap queryMap, HashMap map) {
        delCard(queryMap, map, liveDataDelCardCheck);
    }

    public void delCardBySubmit(HashMap queryMap, HashMap map) {
        delCard(queryMap, map, liveDataDelCardResult);
    }

    public void delCard(HashMap queryMap, HashMap map, SingleLiveData<UserBankConfirmVo> liveData) {

        Disposable disposable = (Disposable) model.getApiService().delBankCard(queryMap, map)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<UserBankConfirmVo>() {
                    @Override
                    public void onResult(UserBankConfirmVo vo) {
                        CfLog.d("******");
                        // 1,2-失败, 3-成功
                        liveData.setValue(vo);
                        /*if (vo.msg_type == 3) {
                            ToastUtils.showLong(vo.message); // "操作成功"
                            liveDataDelCardResult.setValue(vo);
                        } else if (vo.msg_type == 1) {
                            ToastUtils.showLong(vo.message); // "银行账号或开户人姓名不正确！"
                            //liveDataDelCardResult.setValue(vo);
                        }*/
                    }

                    @Override
                    public void onError(Throwable t) {
                        CfLog.e("error, " + t.toString());
                        super.onError(t);
                    }
                });
        addSubscribe(disposable);
    }

    public void getProfile() {
        Disposable disposable = (Disposable) model.getApiService().getProfile()
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<ProfileVo>() {
                    @Override
                    public void onResult(ProfileVo vo) {
                        CfLog.i(vo.toString());
                        SPUtils.getInstance().put(SPKeyGlobal.HOME_PROFILE, new Gson().toJson(vo));
                        liveDataProfile.setValue(vo);
                    }

                    @Override
                    public void onError(Throwable t) {
                        CfLog.e("error, " + t.toString());
                    }
                });
        addSubscribe(disposable);
    }

}
