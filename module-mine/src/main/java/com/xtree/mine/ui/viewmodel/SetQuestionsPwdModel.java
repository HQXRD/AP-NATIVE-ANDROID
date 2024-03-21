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
import com.xtree.mine.vo.CheckQuestionVo;

import java.util.HashMap;

import io.reactivex.disposables.Disposable;
import me.xtree.mvvmhabit.base.BaseViewModel;
import me.xtree.mvvmhabit.http.BaseResponse2;
import me.xtree.mvvmhabit.utils.RxUtils;
import me.xtree.mvvmhabit.utils.SPUtils;
import me.xtree.mvvmhabit.utils.ToastUtils;

/*设置密保*/
public class SetQuestionsPwdModel extends BaseViewModel<MineRepository> {
    public MutableLiveData<BaseResponse2> setQuestionLiveData = new MutableLiveData<>(); // 设置密保
    public MutableLiveData<CheckQuestionVo> checkQuestionVoMutableLiveData = new MutableLiveData<>();//检查密保
    public MutableLiveData<BaseResponse2> response2MutableLiveData = new MutableLiveData<>();//密码找回

    public SetQuestionsPwdModel(@NonNull Application application, MineRepository model) {
        super(application, model);
    }

    /*设置密保*/
    public void putSecurityQuestions(final HashMap<String, String> map) {
        Disposable disposable = (Disposable) model.getApiService().putSecurityQuestions(map)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<BaseResponse2>() {
                    @Override
                    public void onResult(BaseResponse2 vo) {
                        setQuestionLiveData.setValue(vo);
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

    /*检查密保*/
    public void postCheckQuestion(final HashMap<String, String> map) {
        Disposable disposable = (Disposable) model.getApiService().postCheckQuestion(map)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<CheckQuestionVo>() {
                    @Override
                    public void onResult(CheckQuestionVo vo) {
                        checkQuestionVoMutableLiveData.setValue(vo);
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

    /*密码找回*/
    public void postRetrievePSW(final HashMap<String, String> map) {
        Disposable disposable = (Disposable) model.getApiService().postRetrievePSW(map)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<BaseResponse2>() {
                    @Override
                    public void onResult(BaseResponse2 vo) {
                        response2MutableLiveData.setValue(vo);
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

    /*刷新用户个人信息*/
    public void getProfile() {
        Disposable disposable = (Disposable) model.getApiService().getProfile()
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<ProfileVo>() {
                    @Override
                    public void onResult(ProfileVo vo) {
                        CfLog.i(vo.toString());
                        SPUtils.getInstance().put(SPKeyGlobal.USER_AUTO_THRAD_STATUS, vo.auto_thrad_status);
                        SPUtils.getInstance().put(SPKeyGlobal.HOME_PROFILE, new Gson().toJson(vo));
                        SPUtils.getInstance().put(SPKeyGlobal.USER_ID, vo.userid);
                        SPUtils.getInstance().put(SPKeyGlobal.USER_NAME, vo.username);

                    }

                    @Override
                    public void onError(Throwable t) {
                        CfLog.e("error, " + t.toString());
                        //super.onError(t);
                        //ToastUtils.showLong("请求失败");
                    }
                });
        addSubscribe(disposable);
    }
}
