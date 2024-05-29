package com.xtree.mine.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;

import com.xtree.base.net.HttpCallBack;
import com.xtree.base.utils.CfLog;
import com.xtree.base.utils.RSAEncrypt;
import com.xtree.base.utils.UuidUtil;
import com.xtree.mine.data.MineRepository;
import com.xtree.mine.vo.ForgetPasswordCheckInfoVo;
import com.xtree.mine.vo.ForgetPasswordTimeoutVo;
import com.xtree.mine.vo.ForgetPasswordVerifyVo;

import java.util.HashMap;

import io.reactivex.disposables.Disposable;
import me.xtree.mvvmhabit.base.BaseViewModel;
import me.xtree.mvvmhabit.bus.event.SingleLiveData;
import me.xtree.mvvmhabit.utils.RxUtils;
import me.xtree.mvvmhabit.utils.SPUtils;

public class ForgetPasswordViewModel extends BaseViewModel<MineRepository> {
    private String mUsername;
    private String mSendtype;
    private String mToken;
    public SingleLiveData<ForgetPasswordCheckInfoVo> liveDataUserInfo = new SingleLiveData<>(); // 可转账的平台
    public SingleLiveData<Integer> liveDataCheckSendMessageSuccess = new SingleLiveData<>(); // OTP确认是否正常
    public SingleLiveData<Boolean> liveDataToken = new SingleLiveData<>(); // OTP码正确
    public SingleLiveData<Boolean> liveDataCheckPasswordSuccess = new SingleLiveData<>(); // 密码正常变更

    public ForgetPasswordViewModel(@NonNull Application application, MineRepository model) {
        super(application, model);
    }

    public void getForgetUserInfo(String username) {
        HashMap<String, String> map = new HashMap<>();
        map.put("flag", "check");
        map.put("username", username);
        map.put("nonce", UuidUtil.getID16());

        mUsername = username;

        Disposable disposable = (Disposable) model.getApiService().getUserInfoApi(map)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<ForgetPasswordCheckInfoVo>() {
                    @Override
                    public void onResult(ForgetPasswordCheckInfoVo vo) {
                        CfLog.i(vo.toString());
                        liveDataUserInfo.setValue(vo);
                    }

                    @Override
                    public void onError(Throwable t) {
                        CfLog.e("error, " + t.toString());
                        super.onError(t);
                    }
                });
        addSubscribe(disposable);
    }

    public void sendMessage(String sendType) {
        HashMap<String, String> map = new HashMap<>();
        map.put("flag", "sendmessage");
        map.put("username", mUsername);
        map.put("smstype", "11");
        map.put("sendtype", sendType);
        map.put("nonce", UuidUtil.getID16());

        mSendtype = sendType;

        Disposable disposable = (Disposable) model.getApiService().getForgetPasswordOTP(map)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<ForgetPasswordTimeoutVo>() {
                    @Override
                    public void onResult(ForgetPasswordTimeoutVo vo) {
                        CfLog.i(vo.toString());
                        liveDataCheckSendMessageSuccess.setValue(vo.timeoutsec);
                    }

                    @Override
                    public void onError(Throwable t) {
                        CfLog.e("error, " + t.toString());
                        super.onError(t);
                    }
                });
        addSubscribe(disposable);
    }

    public void sendMessageVerfyCode(String smsCode, String sendType) {
        HashMap<String, String> map = new HashMap<>();
        map.put("flag", "verifycode");
        map.put("username", mUsername);
        map.put("smscode", smsCode);
        map.put("smstype", "11");
        map.put("sendtype", sendType);
        map.put("nonce", UuidUtil.getID16());

        mSendtype = sendType;

        Disposable disposable = (Disposable) model.getApiService().getUserTokenApi(map)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<ForgetPasswordVerifyVo>() {
                    @Override
                    public void onResult(ForgetPasswordVerifyVo vo) {
                        CfLog.i(vo.toString());
                        mToken = vo.token;
                        liveDataToken.setValue(true);
                    }

                    @Override
                    public void onError(Throwable t) {
                        CfLog.e("error, " + t.toString());
                        super.onError(t);
                    }
                });
        addSubscribe(disposable);
    }

    public void sendChangePasswordSuccessful(String password) {
        String public_key = SPUtils.getInstance().getString("public_key", "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDW+Gv8Xmk+EdTLQUU5fEAzhlVuFrI7GN4a8N\\/B0Oe63ORK8oBE1pK+t5U5Iz89K4zf7nX+tqQvzND5Z57NMwyqTYYb3TMbrKgjqF1K2YW08OaubjpdohMnDIibmPXNtrbRZpOf2xIaApR+wpqGS+Xw0LzKA8JPYDOPO4lseAtqVwIDAQAB");

        HashMap<String, String> map = new HashMap<>();
        map.put("flag", "changepassword");
        map.put("username", mUsername);
        map.put("sendtype", mSendtype);
        map.put("token", mToken);
        map.put("newpass", RSAEncrypt.encrypt2(password, public_key));
        map.put("nonce", UuidUtil.getID16());

        Disposable disposable = (Disposable) model.getApiService().getChangePasswordResult(map)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<Object>() {
                    @Override
                    public void onResult(Object vo) {
                        CfLog.i(vo.toString());
                        liveDataCheckPasswordSuccess.setValue(true);
                        mUsername = "";
                        mSendtype = "";
                        mToken = "";
                    }

                    @Override
                    public void onError(Throwable t) {
                        CfLog.e("error, " + t.toString());
                        super.onError(t);
                    }
                });
        addSubscribe(disposable);
    }
}
