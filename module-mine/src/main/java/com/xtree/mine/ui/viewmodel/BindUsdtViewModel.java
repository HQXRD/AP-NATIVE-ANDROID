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
import com.xtree.mine.vo.UsdtVo;
import com.xtree.mine.vo.UserBindBaseVo;
import com.xtree.mine.vo.UserUsdtConfirmVo;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.disposables.Disposable;
import me.xtree.mvvmhabit.base.BaseViewModel;
import me.xtree.mvvmhabit.bus.event.SingleLiveData;
import me.xtree.mvvmhabit.http.BaseResponse;
import me.xtree.mvvmhabit.http.BusinessException;
import me.xtree.mvvmhabit.utils.RxUtils;
import me.xtree.mvvmhabit.utils.SPUtils;
import me.xtree.mvvmhabit.utils.ToastUtils;

public class BindUsdtViewModel extends BaseViewModel<MineRepository> {

    public SingleLiveData<UserBindBaseVo<UsdtVo>> liveDataCardList = new SingleLiveData<>();
    //public SingleLiveData<UserUsdtTypeVo> liveDataTypeList = new SingleLiveData<>();
    public SingleLiveData<UserUsdtConfirmVo> liveDataBindCardCheck = new SingleLiveData<>(); // 绑定卡确认
    public SingleLiveData<UserUsdtConfirmVo> liveDataBindCardResult = new SingleLiveData<>(); // 绑定卡结果
    public SingleLiveData<UserUsdtConfirmVo> liveDataRebindCard01 = new SingleLiveData<>(); // 重新绑定
    public SingleLiveData<UserUsdtConfirmVo> liveDataRebindCard02 = new SingleLiveData<>(); // 重新绑定
    public SingleLiveData<UserUsdtConfirmVo> liveDataRebindCard03 = new SingleLiveData<>(); // 重新绑定
    public SingleLiveData<UserUsdtConfirmVo> liveDataRebindCard04 = new SingleLiveData<>(); // 重新绑定
    public SingleLiveData<Boolean> liveDataVerify = new SingleLiveData<>(); // 验证账户
    public MutableLiveData<ProfileVo> liveDataProfile = new MutableLiveData<>(); // 个人信息
    public String key = ""; // usdt

    public BindUsdtViewModel(@NonNull Application application, MineRepository model) {
        super(application, model);
    }

    public void getCardList(HashMap map) {
        Disposable disposable = (Disposable) model.getApiService().getUsdtList(key, map)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<UserBindBaseVo<UsdtVo>>() {
                    @Override
                    public void onResult(UserBindBaseVo<UsdtVo> vo) {
                        CfLog.d("******");
                        if (vo.msg_type == 1 || vo.msg_type == 2) {
                            ToastUtils.showLong(vo.message); // 异常 2-用户无此访问权限
                            finish();
                        } else if (vo.status == 1) {
                            // 列表无数据,应该跳到增加绑定
                            liveDataCardList.setValue(vo);
                        } else {
                            liveDataCardList.setValue(vo); // smstype 6-成功
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

    //public void getUsdtType(HashMap map) {
    //    Disposable disposable = (Disposable) model.getApiService().getUsdtType(key, map)
    //            .compose(RxUtils.schedulersTransformer())
    //            .compose(RxUtils.exceptionTransformer())
    //            .subscribeWith(new HttpCallBack<UserUsdtTypeVo>() {
    //                @Override
    //                public void onResult(UserUsdtTypeVo vo) {
    //                    CfLog.d("******");
    //                    if (vo.msg_type == 1 || vo.msg_type == 2) {
    //                        ToastUtils.showLong(vo.message); // 异常
    //                        finish();
    //                    } else {
    //                        liveDataTypeList.setValue(vo);
    //                    }
    //                }
    //
    //                @Override
    //                public void onError(Throwable t) {
    //                    CfLog.e("error, " + t.toString());
    //                    super.onError(t);
    //                }
    //            });
    //    addSubscribe(disposable);
    //}

    /**
     * 绑卡, 点下一步展示确认信息页
     *
     * @param queryMap
     * @param map
     */
    public void doBindCardByCheck(HashMap queryMap, HashMap map) {

        Disposable disposable = (Disposable) model.getApiService().doBindUsdt(key, queryMap, map)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<UserUsdtConfirmVo>() {
                    @Override
                    public void onResult(UserUsdtConfirmVo vo) {
                        CfLog.d("******");
                        if (vo.msg_type == 1 || vo.msg_type == 2) {
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

    /**
     * 认证，最近加入的账户
     *
     * @param map
     */
    public void doVerify(HashMap qMap, HashMap map) {
        Disposable disposable = (Disposable) model.getApiService().verifyAcc(qMap, map)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<Map<String, String>>() {
                    @Override
                    public void onResult(Map<String, String> map) {
                        CfLog.d("******");

                        if (map.get("status").equals("10000")) {
                            liveDataVerify.setValue(true);
                        } else {
                            onFail(new BusinessException(0, map.get("message")));
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        CfLog.e("error, " + t.toString());
                        super.onError(t);
                    }

                    @Override
                    public void onFail(BusinessException t) {
                        super.onFail(t);
                    }
                });
        addSubscribe(disposable);
    }

    public void doBindCardBySubmit(HashMap queryMap, HashMap map) {

        Disposable disposable = (Disposable) model.getApiService().doBindUsdt(key, queryMap, map)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<UserUsdtConfirmVo>() {
                    @Override
                    public void onResult(UserUsdtConfirmVo vo) {
                        CfLog.d("******");
                        if (vo.msg_type == 1 || vo.msg_type == 2) {
                            ToastUtils.showLong(vo.message); // 异常
                        } else if (vo.msg_type == 3) {
                            ToastUtils.showLong(vo.message); // "绑定成功！温馨提示：新绑定卡需0小时后才能提现"
                            liveDataBindCardResult.setValue(vo);
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

    public void doRebindCard01(HashMap queryMap, HashMap map) {
        doRebindCard(queryMap, map, liveDataRebindCard01);
    }

    public void doRebindCard02(HashMap queryMap, HashMap map) {
        doRebindCard(queryMap, map, liveDataRebindCard02);
    }

    public void doRebindCard03(HashMap queryMap, HashMap map) {
        doRebindCard(queryMap, map, liveDataRebindCard03);
    }

    public void doRebindCard04(HashMap queryMap, HashMap map) {
        doRebindCard(queryMap, map, liveDataRebindCard04);
    }

    private void doRebindCard(HashMap queryMap, HashMap map, SingleLiveData<UserUsdtConfirmVo> liveData) {

        Disposable disposable = (Disposable) model.getApiService().doRebindUsdt(key, queryMap, map)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<UserUsdtConfirmVo>() {
                    @Override
                    public void onResult(UserUsdtConfirmVo vo) {
                        CfLog.d("******");
                        if (vo.msg_type == 1 || vo.msg_type == 2) {
                            ToastUtils.showLong(vo.message); // 异常 (钱包地址验证错误)
                        } else if (vo.msg_type == 3) {
                            ToastUtils.showLong(vo.message); // 提交成功
                            liveData.setValue(vo);
                        } else {
                            liveData.setValue(vo);
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
