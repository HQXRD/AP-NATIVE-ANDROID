package com.xtree.mine.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.net.HttpCallBack;
import com.xtree.base.utils.CfLog;
import com.xtree.mine.data.MineRepository;
import com.xtree.mine.vo.BindGoogleVO;
import com.xtree.mine.vo.GooglePswVO;
import com.xtree.mine.vo.ProfileVo;

import java.util.HashMap;

import io.reactivex.disposables.Disposable;
import me.xtree.mvvmhabit.base.BaseViewModel;
import me.xtree.mvvmhabit.utils.RxUtils;
import me.xtree.mvvmhabit.utils.SPUtils;
import me.xtree.mvvmhabit.utils.ToastUtils;

/** 谷歌验证ViewModel*/
public class GooglePwdViewModel extends BaseViewModel<MineRepository> {
    public MutableLiveData<ProfileVo> liveDataProfile = new MutableLiveData<>(); //查询个人信息
    public MutableLiveData<BindGoogleVO> liveDataBindGoogleVerify = new MutableLiveData<>() ; //谷歌验证
    public MutableLiveData<GooglePswVO> liveDataBindGoogleVerifyStr = new MutableLiveData<>(); //谷歌验证码文本

    public GooglePwdViewModel(@NonNull Application application) {
        super(application);
    }

    public GooglePwdViewModel(@NonNull Application application, MineRepository model) {
        super(application, model);
    }
    /**
     * 获取谷歌验证吗
     * @param map
     */
    public void bindVerifySecrets(HashMap<String , String > map){
        bindVerifySecret(map , liveDataBindGoogleVerifyStr);
    }

    /**
     * get请求
     */
    private void bindVerifySecret(HashMap<String ,String> map ,MutableLiveData<GooglePswVO> googleData)
    {
        Disposable disposable = (Disposable) model.getApiService().getGoogle()
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<GooglePswVO>() {
                    @Override
                    public void onResult(GooglePswVO vo) {

                        googleData.setValue(vo);
                    }

                });
        addSubscribe(disposable);
    }
    /**
     * 绑定谷歌验证码
     * @param map
     */
    public void  bindVerifyGoogle(HashMap<String , String> map){
        bindVerifyGoogle(map , liveDataBindGoogleVerify);
    }

    private void bindVerifyGoogle(HashMap<String , String> map  ,MutableLiveData<BindGoogleVO> liveData )
    {
        Disposable disposable = (Disposable) model.getApiService().bindGoogle(map)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer()).
                subscribeWith(new HttpCallBack<BindGoogleVO>() {
                    @Override
                    public void onResult(BindGoogleVO vo) {
                        CfLog.i("****** "); // vo.toString()
                        liveData.setValue(vo);
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

    /**
     * 获取用户个人信息
     */
    public void getProfile() {
        getProfile(liveDataProfile);
    }

    private void getProfile(MutableLiveData<ProfileVo> liveData) {

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
                        liveData.setValue(vo);
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
}
