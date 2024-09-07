package com.xtree.mine.ui.viewmodel;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.net.HttpCallBack;
import com.xtree.base.net.RetrofitClient;
import com.xtree.base.utils.CfLog;
import com.xtree.base.utils.MD5Util;
import com.xtree.base.utils.RSAEncrypt;
import com.xtree.base.utils.UuidUtil;
import com.xtree.base.vo.FBService;
import com.xtree.base.vo.PMService;
import com.xtree.base.vo.ProfileVo;
import com.xtree.mine.data.MineRepository;
import com.xtree.mine.vo.LoginResultVo;
import com.xtree.mine.vo.SettingsVo;

import java.util.HashMap;

import io.reactivex.disposables.Disposable;
import me.xtree.mvvmhabit.base.BaseViewModel;
import me.xtree.mvvmhabit.bus.event.SingleLiveData;
import me.xtree.mvvmhabit.http.BusinessException;
import me.xtree.mvvmhabit.utils.KLog;
import me.xtree.mvvmhabit.utils.RxUtils;
import me.xtree.mvvmhabit.utils.SPUtils;
import me.xtree.mvvmhabit.utils.ToastUtils;

public class LoginViewModel extends BaseViewModel<MineRepository> {

    public SingleLiveData<LoginResultVo> liveDataLogin = new SingleLiveData<>();
    public SingleLiveData<BusinessException> liveDataLoginFail = new SingleLiveData<>();
    public SingleLiveData<LoginResultVo> liveDataReg = new SingleLiveData<>();
    String public_key;

    public LoginViewModel(@NonNull Application application, MineRepository repository) {
        super(application, repository);
    }

    public void login(String userName, String pwd) {
        String password = MD5Util.generateMd5("") + MD5Util.generateMd5(pwd);
        //KLog.i("password: " + password);

        String public_key = SPUtils.getInstance().getString("public_key", "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDW+Gv8Xmk+EdTLQUU5fEAzhlVuFrI7GN4a8N\\/B0Oe63ORK8oBE1pK+t5U5Iz89K4zf7nX+tqQvzND5Z57NMwyqTYYb3TMbrKgjqF1K2YW08OaubjpdohMnDIibmPXNtrbRZpOf2xIaApR+wpqGS+Xw0LzKA8JPYDOPO4lseAtqVwIDAQAB");
        String loginpass = RSAEncrypt.encrypt2(pwd, public_key);
        //KLog.i("loginpass: " + loginpass);

        if (TextUtils.isEmpty(loginpass)) {
            return;
        }

        HashMap<String, String> map = new HashMap();
        map.put("username", userName);
        map.put("password", password);
        map.put("grant_type", "login");
        map.put("validcode", "");
        map.put("client_id", "10000005"); // h5:10000003, ios:10000004, android:10000005
        map.put("device_type", "app"); // pc,h5,app,(ios,android)
        map.put("loginpass", loginpass);
        map.put("nonce", UuidUtil.getID16());

        Disposable disposable = (Disposable) model.getApiService().login(map)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<LoginResultVo>() {
                    @Override
                    public void onResult(LoginResultVo vo) {
                        if (vo == null || TextUtils.isEmpty(vo.token)) {
                            //后台对登录增加了某些新功能,可能异常返回为空(短时间登录多次/多次输错密码)
                            ToastUtils.showLong("登录异常，请稍候再试");
                            return;
                        }
                        KLog.i(vo.toString());
                        //ToastUtils.showLong("登录成功");
                        vo.userName = userName;
                        if (vo.twofa_required == 0) {
                            setLoginSucc(vo); // 不需要谷歌验证
                        }
                        // 登录成功后获取FB体育请求服务地址
                        //getFBGameTokenApi();
                        liveDataLogin.setValue(vo); // twofa_required=1 时需要谷歌验证
                    }

                    @Override
                    public void onError(Throwable t) {
                        KLog.e(t.toString());
                        //super.onError(t);
                        ToastUtils.showLong("登录异常，请重试");
                    }

                    @Override
                    public void onFail(BusinessException t) {
                        super.onFail(t); // 弹提示
                        KLog.e(t.toString());

                        if (t.code == HttpCallBack.CodeRule.CODE_20208) {
                            HashMap<String, Object> map2 = new HashMap<>();
                            map2.put("loginArgs", new Gson().toJson(map));
                            map2.put("data", t.data);
                            t.data = map2;
                            liveDataLoginFail.setValue(t);
                        } else if (t.code == HttpCallBack.CodeRule.CODE_30018) {

                        }

                    }
                });
        addSubscribe(disposable);
    }

    public void setLoginSucc(LoginResultVo vo) {
        SPUtils.getInstance().put(SPKeyGlobal.USER_TOKEN, vo.token);
        SPUtils.getInstance().put(SPKeyGlobal.USER_TOKEN_TYPE, vo.token_type);
        SPUtils.getInstance().put(SPKeyGlobal.USER_SHARE_SESSID, vo.cookie.sessid);
        SPUtils.getInstance().put(SPKeyGlobal.USER_SHARE_COOKIE_NAME, vo.cookie.cookie_name);
        SPUtils.getInstance().put(SPKeyGlobal.USER_NAME, vo.userName); // 用户名
        // 解决登录后,首页显示为未登录,过2秒才显示登录名和金额的问题
        SPUtils.getInstance().put(SPKeyGlobal.HOME_PROFILE, new Gson().toJson(new ProfileVo(vo.userName, "***")));
        RetrofitClient.init();
    }

    public void register(String userName, String pwd) {
        HashMap<String, String> map = new HashMap();
        map.put("carryAuth", "false");
        map.put("code", "");
        map.put("nonce", UuidUtil.getID16());
        map.put("username", userName);
        map.put("userpass", pwd); // 明文

        Disposable disposable = (Disposable) model.getApiService().register(map)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<LoginResultVo>() {
                    @Override
                    public void onResult(LoginResultVo vo) {
                        if (vo == null) {
                            CfLog.e("result is null");
                            return;
                        }
                        ToastUtils.showLong("注册成功");
                        vo.userName = userName;
                        setLoginSucc(vo);
                        // 登录成功后获取FB体育请求服务地址
                        //getFBGameTokenApi();
                        liveDataReg.setValue(vo);
                    }

                    @Override
                    public void onError(Throwable t) {
                        KLog.e(t.toString());
                        super.onError(t);
                        ToastUtils.showLong("注册失败");
                    }
                });
        addSubscribe(disposable);
    }

    public void getSettings() {
        HashMap<String, String> map = new HashMap();
        map.put("fields", "customer_service_url,public_key,barrage_api_url," +
                "x9_customer_service_url," + "promption_code,default_promption_code");
        Disposable disposable = (Disposable) model.getApiService().getSettings(map)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<SettingsVo>() {
                    @Override
                    public void onResult(SettingsVo vo) {
                        //CfLog.i(vo.toString());
                        public_key = vo.public_key
                                .replace("\n", "")
                                .replace("\t", " ")
                                .replace("-----BEGIN PUBLIC KEY-----", "")
                                .replace("-----END PUBLIC KEY-----", "");

                        SPUtils.getInstance().put(SPKeyGlobal.PUBLIC_KEY, public_key);
                        SPUtils.getInstance().put("customer_service_url", vo.customer_service_url);
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        ToastUtils.showLong("请求失败");
                    }
                });
        addSubscribe(disposable);
    }
    public void clearCache() {

        SPUtils.getInstance().remove(SPKeyGlobal.USER_TOKEN);
        SPUtils.getInstance().remove(SPKeyGlobal.USER_SHARE_SESSID);
        SPUtils.getInstance().remove(SPKeyGlobal.HOME_PROFILE);
        SPUtils.getInstance().remove(SPKeyGlobal.HOME_VIP_INFO);
        SPUtils.getInstance().remove(SPKeyGlobal.HOME_NOTICE_LIST);
        SPUtils.getInstance().remove(SPKeyGlobal.USER_ID);
        SPUtils.getInstance().remove(SPKeyGlobal.USER_NAME);
        SPUtils.getInstance().remove(SPKeyGlobal.MSG_PERSON_INFO);
        SPUtils.getInstance().remove(SPKeyGlobal.FB_TOKEN);
        SPUtils.getInstance().remove(SPKeyGlobal.PM_TOKEN);

        RetrofitClient.init();

    }

}
